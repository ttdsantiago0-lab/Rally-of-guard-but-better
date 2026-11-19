package net.hfstack.rallyguard.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * F46 (2026-11-19, docs/equipo/COLA.md), simulation only (tag {@code door:breach}): "abrir brecha". F44 measured that a
 * closed house door is what keeps the village fight from ending (open doors: 57-73 % -> 97-100 % strict wins). Guard
 * Villagers' and vanilla's door goals only fire while the guard walks a live path through the door, and the melee goal
 * stops walking 2.5 blocks from its target - so a guard whose target hides behind a door just stands outside.
 * <p>
 * Rule (composition, no mixin, no block broken): an owned guard with a live target within 16 blocks that has had no
 * line of sight to it for at least 20 ticks looks for the nearest closed wooden door to the TARGET (6 blocks
 * horizontally, +-2 vertically). Within 2.5 blocks of it the guard opens it ({@link DoorBlock#setOpen}, the guard as the
 * entity; counts {@code act:breach}); otherwise it walks to the block in front of the door on its side (renewed every
 * 10 ticks while still blind; counts {@code act:breachWalk}). v1 never closes the door and never changes mode, stance
 * or target. Cost: the block scan only for guards that meet the condition, at most every 10 ticks.
 */
public final class Breach {
    private Breach() {
    }

    /** Set per simulation run by the runner from the tag {@code door:breach}; false in the game. */
    public static boolean simOn;
    public static final int INTERVAL_TICKS = 10;
    private static final double TARGET_RANGE = 16.0D, OPEN_REACH = 2.5D;
    private static final int SEARCH_H = 6, SEARCH_V = 2;
    private static final long BLIND_TICKS = 20L;
    private static final Map<UUID, Long> BLIND_SINCE = new HashMap<>();

    public static void reset(boolean on) {
        simOn = on;
        BLIND_SINCE.clear();
    }

    /** One guard's step; called every {@link #INTERVAL_TICKS} ticks through SafeTick, only while {@link #simOn}. */
    public static void step(ServerLevel level, Guard g) {
        LivingEntity target = g.getTarget();
        long now = level.getGameTime();
        if (target == null || !target.isAlive() || g.isPassenger() || g.distanceToSqr(target) > TARGET_RANGE * TARGET_RANGE
                || g.getSensing().hasLineOfSight(target)) {
            BLIND_SINCE.remove(g.getUUID());
            return;
        }
        if (BLIND_SINCE.size() > 512) BLIND_SINCE.values().removeIf(t -> t < now - 200L); // guards that died blind
        long since = BLIND_SINCE.computeIfAbsent(g.getUUID(), k -> now);
        if (now - since < BLIND_TICKS) return;

        BlockPos door = closedDoorNear(level, target.blockPosition());
        if (door == null) return;
        BlockState state = level.getBlockState(door);
        if (g.distanceToSqr(Vec3.atBottomCenterOf(door)) <= OPEN_REACH * OPEN_REACH) {
            if (state.getBlock() instanceof DoorBlock block) {
                block.setOpen(g, level, state, door, true);
                net.hfstack.rallyguard.debug.SimProbe.count(g, "act:breach");
            }
            return;
        }
        Direction facing = state.getValue(DoorBlock.FACING);
        BlockPos a = door.relative(facing), b = door.relative(facing.getOpposite());
        BlockPos front = g.distanceToSqr(Vec3.atBottomCenterOf(a)) <= g.distanceToSqr(Vec3.atBottomCenterOf(b)) ? a : b;
        if (g.getNavigation().moveTo(front.getX() + 0.5D, front.getY(), front.getZ() + 0.5D, 1.0D)) {
            net.hfstack.rallyguard.debug.SimProbe.count(g, "act:breachWalk");
        }
    }

    /** Lower half of the nearest closed wooden door to {@code centre}, or null. */
    @javax.annotation.Nullable
    private static BlockPos closedDoorNear(ServerLevel level, BlockPos centre) {
        BlockPos best = null;
        double bestD = Double.MAX_VALUE;
        for (BlockPos p : BlockPos.betweenClosed(centre.offset(-SEARCH_H, -SEARCH_V, -SEARCH_H),
                centre.offset(SEARCH_H, SEARCH_V, SEARCH_H))) {
            BlockState s = level.getBlockState(p);
            if (!s.is(BlockTags.WOODEN_DOORS) || !(s.getBlock() instanceof DoorBlock)) continue;
            if (s.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER || s.getValue(DoorBlock.OPEN)) continue;
            double d = p.distSqr(centre);
            if (d < bestD) {
                bestD = d;
                best = p.immutable(); // betweenClosed reuses one mutable position
            }
        }
        return best;
    }
}
