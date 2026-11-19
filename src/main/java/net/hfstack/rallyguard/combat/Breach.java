package net.hfstack.rallyguard.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
 * <p>
 * F49 (2026-11-19), simulation only (tag {@code door:close}, read in SimExtras.begin): v2 remembers every door it
 * opened ({@link #OPENED}, position -> tick) and {@link #closeStep} shuts one again ({@link DoorBlock#setOpen} with no
 * entity; counts {@code act:breachClose}) once it has been open 100 ticks, nothing living stands in its block (the
 * door's two blocks inflated 0.5) and no living {@link Enemy} is within 8 blocks. A remembered door someone else closed
 * (or that is no longer a door) is forgotten. {@link #openRemembered} gives {@code aldea:doorsOpenEnd}.
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
    /** F49: set per simulation run by SimExtras.begin from the tag {@code door:close}; false in the game. */
    public static boolean closeOn;
    private static final long CLOSE_AFTER_TICKS = 100L;
    private static final double CLOSE_ENEMY_RANGE = 8.0D;
    /** F49: lower halves of the doors this rule opened -> game tick it opened them (sim only, cleared every run). */
    private static final Map<BlockPos, Long> OPENED = new LinkedHashMap<>();
    @javax.annotation.Nullable
    private static ServerLevel openedLevel;

    /** Resets the per-run state; {@link #closeOn} is set separately (SimExtras.begin) and is not touched here. */
    public static void reset(boolean on) {
        simOn = on;
        BLIND_SINCE.clear();
        OPENED.clear();
        openedLevel = null;
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
                OPENED.put(door.immutable(), now); // F49
                openedLevel = level;
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

    /** F49: once per level every {@link #INTERVAL_TICKS} ticks, only while {@link #simOn} and {@link #closeOn}. */
    public static void closeStep(ServerLevel level) {
        if (!closeOn || OPENED.isEmpty() || level != openedLevel) return;
        long now = level.getGameTime();
        for (Iterator<Map.Entry<BlockPos, Long>> it = OPENED.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<BlockPos, Long> e = it.next();
            BlockPos pos = e.getKey();
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof DoorBlock block) || !state.getValue(DoorBlock.OPEN)) {
                it.remove(); // closed by someone else, or gone
                continue;
            }
            if (now - e.getValue() < CLOSE_AFTER_TICKS) continue;
            AABB box = new AABB(pos).expandTowards(0.0D, 1.0D, 0.0D).inflate(0.5D);
            if (!level.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive).isEmpty()) continue;
            AABB near = new AABB(pos).inflate(CLOSE_ENEMY_RANGE);
            if (!level.getEntitiesOfClass(LivingEntity.class, near, x -> x.isAlive() && x instanceof Enemy).isEmpty()) continue;
            block.setOpen(null, level, state, pos, false);
            net.hfstack.rallyguard.debug.sim.SimExtras.count("act:breachClose");
            it.remove();
        }
    }

    /** F49: how many of the doors this rule opened are still open (telemetry {@code aldea:doorsOpenEnd}). */
    public static int openRemembered() {
        ServerLevel level = openedLevel;
        if (level == null) return 0;
        int n = 0;
        for (BlockPos pos : OPENED.keySet()) {
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof DoorBlock && state.getValue(DoorBlock.OPEN)) n++;
        }
        return n;
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
