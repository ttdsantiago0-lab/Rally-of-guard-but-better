package net.hfstack.rallyguard.debug;

import net.minecraft.world.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * E1-F: the simulation's counters. Every hook starts with {@link #active} - a single
 * static boolean - so outside a simulation they cost nothing. Only entities spawned by
 * the running simulation are counted.
 */
public final class SimProbe {
    private SimProbe() {
    }

    static boolean active;
    static final Set<UUID> GUARDS = new HashSet<>();
    static final Set<UUID> ENEMIES = new HashSet<>();

    static long shots, hits, ammoItems, weaponSwaps, orders;

    static void begin() {
        active = true;
        shots = hits = ammoItems = weaponSwaps = orders = 0;
    }

    static void end() {
        active = false;
        GUARDS.clear();
        ENEMIES.clear();
    }

    public static boolean isActive() {
        return active;
    }

    private static boolean ours(Entity e) {
        return e != null && GUARDS.contains(e.getUUID());
    }

    public static void shot(Entity shooter) {
        if (active && ours(shooter)) shots++;
    }

    /** A projectile shot by a simulation guard struck a simulation enemy. */
    public static void hit(Entity shooter, Entity victim) {
        if (active && ours(shooter) && victim != null && ENEMIES.contains(victim.getUUID())) hits++;
    }

    public static void ammo(Entity guard, int items) {
        if (active && ours(guard)) ammoItems += items;
    }

    public static void weaponSwap(Entity guard) {
        if (active && ours(guard)) weaponSwaps++;
    }

    public static void order(Entity guard) {
        if (active && ours(guard)) orders++;
    }
}
