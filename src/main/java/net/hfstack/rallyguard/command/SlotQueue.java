package net.hfstack.rallyguard.command;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.order.GuardModes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * E2A block 1 - formation slots handed out over several ticks (FEEDBACK_E1 B10: a V of
 * 200 never formed, while Follow worked at once). Muster/Advance used to post and path
 * every guard on the SAME tick: 200 ground snaps and 200 A* searches at once. Now an
 * order records the slots (cheap) and queues the moves here; each server tick hands out
 * {@code command.slotsPerTick} of them (default 12, so 200 guards take under a second).
 * <p>
 * Any other mode change for a queued guard (Follow, Recall, Hold, Retreat...) goes
 * through {@link GuardModes}, which calls {@link #cancel} - an older queued slot can never
 * overwrite a newer order.
 */
public final class SlotQueue {
    private SlotQueue() {
    }

    private record Move(ResourceKey<Level> dimension, Vec3 target, double speed, long readyTick, boolean follow) {
    }

    private static final Map<UUID, Move> PENDING = new LinkedHashMap<>();

    public static void enqueue(Guard guard, Vec3 target, double speed) {
        PENDING.remove(guard.getUUID()); // re-queued: goes to the back with its new slot
        // E3A A1: a unit under a weaker leader takes longer to start (bounded, see Leadership).
        long ready = guard.level().getGameTime() + net.hfstack.rallyguard.force.Leadership.responseDelay(guard);
        PENDING.put(guard.getUUID(), new Move(guard.level().dimension(), target, speed, ready, false));
    }

    /** E3A A5: a formation-follow slot - posts the guard without releasing it from the follow group. */
    public static void enqueueFollow(Guard guard, Vec3 target, double speed) {
        PENDING.remove(guard.getUUID());
        long ready = guard.level().getGameTime() + net.hfstack.rallyguard.force.Leadership.responseDelay(guard) / 2;
        PENDING.put(guard.getUUID(), new Move(guard.level().dimension(), target, speed, ready, true));
    }

    public static void cancel(UUID guardId) {
        PENDING.remove(guardId);
    }

    public static boolean isQueued(UUID guardId) {
        return PENDING.containsKey(guardId);
    }

    /** Called every tick per level: hands out the next batch of slots in this dimension. */
    public static void drain(ServerLevel level) {
        if (PENDING.isEmpty()) return;
        int budget = RallyConfig.i(RallyConfig.SLOTS_PER_TICK, 12);
        List<Guard> guards = new ArrayList<>(budget);
        List<Move> moves = new ArrayList<>(budget);
        Iterator<Map.Entry<UUID, Move>> it = PENDING.entrySet().iterator();
        while (it.hasNext() && guards.size() < budget) {
            Map.Entry<UUID, Move> e = it.next();
            if (!e.getValue().dimension().equals(level.dimension())) continue;
            if (e.getValue().readyTick() > level.getGameTime()) continue; // still "getting the order"
            it.remove();
            Entity entity = level.getEntity(e.getKey());
            if (entity instanceof Guard guard && guard.isAlive()) {
                guards.add(guard);
                moves.add(e.getValue());
            }
        }
        for (int i = 0; i < guards.size(); i++) {
            Guard guard = guards.get(i);
            Move move = moves.get(i);
            if (move.follow()) {
                net.hfstack.rallyguard.event.SafeTick.run(guard, () -> GuardModes.postForFollow(guard, move.target(), move.speed()));
            } else {
                net.hfstack.rallyguard.event.SafeTick.run(guard, () -> GuardModes.postAndMove(guard, move.target(), move.speed()));
            }
        }
    }

    public static void clearAll() {
        PENDING.clear();
    }
}
