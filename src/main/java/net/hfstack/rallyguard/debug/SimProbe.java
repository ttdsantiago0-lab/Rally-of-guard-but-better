package net.hfstack.rallyguard.debug;

import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * E1-F / E2A block 3: the simulation's counters, per side (0 = blue, 1 = red). Every hook
 * starts with {@link #active} - a single static boolean - so outside a simulation they cost
 * nothing. Only entities spawned by the running simulation are counted.
 */
public final class SimProbe {
    private SimProbe() {
    }

    public static final int BLUE = 0, RED = 1;
    /** Decision-ladder rungs counted (block 4: {@code DecisionLadder.Rung} ordinals). */
    public static final int RUNGS = 24;

    static boolean active;
    /** sim entity -> side */
    static final Map<UUID, Integer> SIDE = new HashMap<>();

    static final long[] shots = new long[2], hits = new long[2], ammoItems = new long[2], weaponSwaps = new long[2],
            orders = new long[2], retreats = new long[2], drowned = new long[2];
    static final double[] damage = new double[2];
    static final long[] ladder = new long[RUNGS];

    static void begin() {
        active = true;
        for (long[] a : new long[][]{shots, hits, ammoItems, weaponSwaps, orders, retreats, drowned, ladder}) {
            java.util.Arrays.fill(a, 0);
        }
        damage[0] = damage[1] = 0;
        xpGained[0] = xpGained[1] = commandXpGained[0] = commandXpGained[1] = 0;
        friendlyFire[0] = friendlyFire[1] = 0;
        unitOrders.clear();
        COUNTS.clear();
    }

    static void end() {
        active = false;
        SIDE.clear();
    }

    public static boolean isActive() {
        return active;
    }

    /** Side of a simulation entity, or -1. */
    public static int sideOf(Entity e) {
        if (!active || e == null) return -1;
        Integer s = SIDE.get(e.getUUID());
        return s == null ? -1 : s;
    }

    /** True for an entity the running simulation spawned. */
    public static boolean isSimEntity(Entity e) {
        return active && e != null && SIDE.containsKey(e.getUUID());
    }

    /** E3A V8: simulation units earn no XP (ranks stay as set), except in pace scenarios. */
    static boolean allowXp;
    static final double[] xpGained = new double[2], commandXpGained = new double[2];

    public static boolean blocksXp(Entity e) {
        return isSimEntity(e) && !allowXp;
    }

    public static void xp(Entity guard, double amount) {
        int s = sideOf(guard);
        if (s >= 0) xpGained[s] += amount;
    }

    public static void commandXp(Entity guard, double amount) {
        int s = sideOf(guard);
        if (s >= 0) commandXpGained[s] += amount;
    }

    public static void shot(Entity shooter) {
        int s = sideOf(shooter);
        if (s >= 0) shots[s]++;
    }

    /** A projectile shot by a simulation unit struck a unit of the other side. */
    public static void hit(Entity shooter, Entity victim) {
        int s = sideOf(shooter), v = sideOf(victim);
        if (s >= 0 && v >= 0 && s != v) hits[s]++;
    }

    public static void ammo(Entity guard, int items) {
        int s = sideOf(guard);
        if (s >= 0) ammoItems[s] += items;
    }

    public static void weaponSwap(Entity guard) {
        int s = sideOf(guard);
        if (s >= 0) weaponSwaps[s]++;
    }

    public static void order(Entity guard) {
        int s = sideOf(guard);
        if (s >= 0) orders[s]++;
    }

    public static void retreat(Entity guard) {
        int s = sideOf(guard);
        if (s >= 0) retreats[s]++;
    }

    /** The decision ladder chose {@code rung} for a simulation guard. */
    public static void ladder(Entity guard, int rung) {
        if (active && rung >= 0 && rung < RUNGS && SIDE.containsKey(guard.getUUID())) ladder[rung]++;
    }

    static final long[] friendlyFire = new long[2];

    /** E3A V9: a friendly hit by a simulation unit was blocked (it was going to hit its own side). */
    public static void friendlyFire(Entity attacker) {
        int s = sideOf(attacker);
        if (s >= 0) friendlyFire[s]++;
    }

    /** E3A A2: unit directives applied per source (precedence scenario). */
    static final Map<String, Long> unitOrders = new HashMap<>();

    public static void unitOrderApplied(Entity guard, String source) {
        if (sideOf(guard) >= 0) unitOrders.merge(source, 1L, Long::sum);
    }

    /** E3B: named counters per side (terrain reads, core decisions, rung changes, ...). */
    static final Map<String, long[]> COUNTS = new HashMap<>();

    public static void count(Entity e, String key) {
        int s = sideOf(e);
        if (s >= 0) COUNTS.computeIfAbsent(key, k -> new long[2])[s]++;
    }

    public static long counted(String key, int side) {
        long[] c = COUNTS.get(key);
        return c == null ? 0 : c[side];
    }

    static void damage(int side, double amount) {
        damage[side] += amount;
    }

    static void drowned(int side) {
        drowned[side]++;
    }
}
