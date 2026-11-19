package net.hfstack.rallyguard.combat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.lang.ref.WeakReference;
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
 * <p>
 * F53 (2026-11-19): the game's default (config {@code breachDoors}, true), opening AND closing; {@link #active()}.
 * The close waits for no living {@link Enemy} within 24 blocks (was 8: in the village the next house's Pax stood
 * beyond 8 and the door shut mid-fight). Remembered doors are kept per dimension (at most 256, the oldest forgotten).
 * Simulation: {@code off:breach} ({@link #simOff}) turns the rule off, {@code door:noclose} ({@link #simNoClose})
 * keeps it from closing; the old tag {@code door:close} no longer does anything.
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
    /** F53: simulation only, tag {@code off:breach} (set in SimExtras.begin, cleared in finish). */
    public static boolean simOff;
    /** F53: simulation only, tag {@code door:noclose}: open but never close (set in SimExtras.begin). */
    public static boolean simNoClose;
    /**
     * F64 (2026-11-19): simulation only, tag {@code door:meleeFirst} (set in SimExtras.begin, cleared in finish): a
     * ranged guard yields the door ({@code act:breachYield}: neither opens nor walks) while another living, unmounted
     * melee guard of the same owner stands within {@link #YIELD_RANGE} blocks of it. False in the game.
     */
    public static boolean simMeleeFirst;
    private static final double YIELD_RANGE = 16.0D;
    private static final long CLOSE_AFTER_TICKS = 100L;
    private static final double CLOSE_ENEMY_RANGE = 24.0D; // F53: 8 -> 24
    private static final int MAX_REMEMBERED = 256; // F53, per dimension
    /** F49/F53: per dimension, lower halves of the doors this rule opened -> game tick it opened them. */
    private static final Map<ResourceKey<Level>, Opened> OPENED = new HashMap<>();

    private static final class Opened {
        final WeakReference<ServerLevel> level;
        final LinkedHashMap<BlockPos, Long> doors = new LinkedHashMap<>();

        Opened(ServerLevel level) {
            this.level = new WeakReference<>(level);
        }
    }

    /** F53: is the rule on - the sim tag {@code door:breach}, or config {@code breachDoors} unless {@code off:breach}. */
    public static boolean active() {
        return simOn || (!simOff && net.hfstack.rallyguard.config.RallyConfig.on(net.hfstack.rallyguard.config.RallyConfig.BREACH_DOORS));
    }

    /** Resets the per-run state; {@link #simOff} / {@link #simNoClose} are set separately (SimExtras.begin). */
    public static void reset(boolean on) {
        simOn = on;
        BLIND_SINCE.clear();
        OPENED.clear();
    }

    /** The remembered doors of {@code level}'s dimension; a holder from another (unloaded) world is replaced. */
    private static Opened opened(ServerLevel level, boolean create) {
        Opened o = OPENED.get(level.dimension());
        if (o != null && o.level.get() != level) {
            OPENED.remove(level.dimension());
            o = null;
        }
        if (o == null && create) {
            o = new Opened(level);
            OPENED.put(level.dimension(), o);
        }
        return o;
    }

    /** One guard's step; called every {@link #INTERVAL_TICKS} ticks through SafeTick, only while {@link #active()}. */
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
        boolean ranged = WeaponProfile.forGuard(g) != null; // F64
        if (simMeleeFirst && ranged && meleeNear(level, g, door)) {
            net.hfstack.rallyguard.debug.SimProbe.count(g, "act:breachYield");
            return;
        }
        BlockState state = level.getBlockState(door);
        if (g.distanceToSqr(Vec3.atBottomCenterOf(door)) <= OPEN_REACH * OPEN_REACH) {
            if (state.getBlock() instanceof DoorBlock block) {
                block.setOpen(g, level, state, door, true);
                net.hfstack.rallyguard.debug.SimProbe.count(g, "act:breach");
                net.hfstack.rallyguard.debug.SimProbe.count(g, ranged ? "act:breachRanged" : "act:breachMelee"); // F64
                Opened o = opened(level, true); // F49/F53
                o.doors.remove(door);
                o.doors.put(door.immutable(), now);
                if (o.doors.size() > MAX_REMEMBERED) {
                    Iterator<BlockPos> oldest = o.doors.keySet().iterator();
                    oldest.next();
                    oldest.remove();
                }
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

    /** F64: another living, unmounted melee guard of {@code g}'s owner within {@link #YIELD_RANGE} of the door. */
    private static boolean meleeNear(ServerLevel level, Guard g, BlockPos door) {
        UUID owner = g.getOwnerId();
        if (owner == null) return false;
        Vec3 c = Vec3.atBottomCenterOf(door);
        for (Guard o : net.hfstack.rallyguard.event.GuardStanceHandler.loaded(level.dimension())) {
            if (o == g || !o.isAlive() || o.isPassenger() || !owner.equals(o.getOwnerId())) continue;
            if (o.distanceToSqr(c) > YIELD_RANGE * YIELD_RANGE) continue;
            if (WeaponProfile.forGuard(o) == null) return true;
        }
        return false;
    }

    /** F49/F53: once per level every {@link #INTERVAL_TICKS} ticks, only while {@link #active()}. */
    public static void closeStep(ServerLevel level) {
        close(level, false);
    }

    /**
     * F53, simulation end: one close pass over every dimension without the 100-tick wait (in the game the same doors
     * would close within the next 5 s), before {@link #openRemembered} is read.
     */
    public static void closeAllNow() {
        for (Opened o : new java.util.ArrayList<>(OPENED.values())) {
            ServerLevel level = o.level.get();
            if (level != null) close(level, true);
        }
    }

    private static void close(ServerLevel level, boolean noWait) {
        if (simNoClose) return;
        Opened o = opened(level, false);
        if (o == null || o.doors.isEmpty()) return;
        long now = level.getGameTime();
        for (Iterator<Map.Entry<BlockPos, Long>> it = o.doors.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<BlockPos, Long> e = it.next();
            BlockPos pos = e.getKey();
            if (!level.isLoaded(pos)) continue; // never load a chunk for this
            BlockState state = level.getBlockState(pos);
            if (!(state.getBlock() instanceof DoorBlock block) || !state.getValue(DoorBlock.OPEN)) {
                it.remove(); // closed by someone else, or gone
                continue;
            }
            if (!noWait && now - e.getValue() < CLOSE_AFTER_TICKS) continue;
            AABB box = new AABB(pos).expandTowards(0.0D, 1.0D, 0.0D).inflate(0.5D);
            if (!level.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive).isEmpty()) continue;
            AABB near = new AABB(pos).inflate(CLOSE_ENEMY_RANGE);
            if (!level.getEntitiesOfClass(LivingEntity.class, near, x -> x.isAlive() && x instanceof Enemy).isEmpty()) continue;
            block.setOpen(null, level, state, pos, false);
            net.hfstack.rallyguard.debug.sim.SimExtras.count("act:breachClose");
            it.remove();
        }
    }

    /** F49/F53: how many of the doors this rule opened are still open (telemetry {@code aldea:doorsOpenEnd}). */
    public static int openRemembered() {
        int n = 0;
        for (Opened o : OPENED.values()) {
            ServerLevel level = o.level.get();
            if (level == null) continue;
            for (BlockPos pos : o.doors.keySet()) {
                if (!level.isLoaded(pos)) continue;
                BlockState state = level.getBlockState(pos);
                if (state.getBlock() instanceof DoorBlock && state.getValue(DoorBlock.OPEN)) n++;
            }
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
