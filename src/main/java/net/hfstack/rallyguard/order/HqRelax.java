package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.contract.HqRegistry;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.tactics.DecisionLadder;
import net.hfstack.rallyguard.tactics.DecisionReason;
import net.hfstack.rallyguard.tactics.DecisionTrace;
import net.hfstack.rallyguard.veterancy.VeterancyEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * E3A A6 (David C-04, J-02) - the HQ is a ZONE around its banner ({@code hq.radius}), not a
 * building: a camp in the woods or a castle, entered through any door or gap (the HQ spots are
 * a flood fill from the banner, {@link GuardPosts#reachableSpots}); an interior is optional.
 * <p>
 * A guard posted inside its squad's HQ zone that has REACHED its spot, with no target and not
 * fighting, becomes RELAXED: its formation slot is dropped (it breaks ranks by itself - Break
 * Ranks is no longer a Ledger button), its mount is parked ({@link HqLogistics}) and every
 * 30-60 s it strolls to another spot inside half the zone. Any new order ({@link GuardModes})
 * ends it; leaving the zone, it calls its mount back and rides off (HqLogistics). Checked in
 * each guard's once-a-second turn; config {@code hq.relaxOnArrival}.
 */
public final class HqRelax {
    private HqRelax() {
    }

    private static final Map<UUID, Long> RELAXED = new HashMap<>(); // guard -> next stroll tick

    public static boolean isRelaxed(Guard guard) {
        return RELAXED.containsKey(guard.getUUID());
    }

    public static void tick(ServerLevel level, Guard guard) {
        if (!RallyConfig.get(RallyConfig.HQ_RELAX::get, Boolean.TRUE)) return;
        long now = level.getGameTime();
        Long next = RELAXED.get(guard.getUUID());
        if (next != null) {
            if (now >= next && guard.getTarget() == null) stroll(level, guard, now);
            return;
        }
        BlockPos post = CommandTargeting.postOf(guard);
        if (post == null || guard.getTarget() != null || VeterancyEvents.inFight(guard)) return;
        BlockPos hq = hqOf(level, guard);
        double r = RallyConfig.i(RallyConfig.HQ_RADIUS, 16);
        if (hq == null || post.distSqr(hq) > r * r || guard.distanceToSqr(Vec3.atBottomCenterOf(post)) > 4.0D) return;
        // Arrived home: break ranks, park, relax.
        FormationRegistry.get(level).remove(guard.getUUID());
        if (guard.isPassenger()) HqLogistics.park(guard);
        RELAXED.put(guard.getUUID(), now + 600 + level.getRandom().nextInt(600));
        DecisionTrace.record(guard, DecisionLadder.Rung.NONE, DecisionReason.RELAXED);
    }

    private static void stroll(ServerLevel level, Guard guard, long now) {
        BlockPos hq = hqOf(level, guard);
        if (hq == null) {
            leave(guard.getUUID());
            return;
        }
        RandomSource rnd = level.getRandom();
        double r = RallyConfig.i(RallyConfig.HQ_RADIUS, 16) / 2.0D;
        Vec3 spot = Vec3.atBottomCenterOf(hq).add((rnd.nextDouble() * 2 - 1) * r, 0, (rnd.nextDouble() * 2 - 1) * r);
        GuardModes.postRelaxed(guard, GuardPosts.standableNear(level, spot));
        RELAXED.put(guard.getUUID(), now + 600 + rnd.nextInt(600));
    }

    @Nullable
    static BlockPos hqOf(ServerLevel level, Guard guard) {
        SquadRegistry squads = SquadRegistry.get(level);
        return squads.squadOf(guard.getUUID())
                .map(s -> RetreatPlanner.hqOf(squads, HqRegistry.get(level), s))
                .filter(h -> h.dimension.equals(level.dimension().location().toString()))
                .map(h -> h.pos).orElse(null);
    }

    public static void leave(UUID guardId) {
        RELAXED.remove(guardId);
    }

    public static void clearAll() {
        RELAXED.clear();
    }
}
