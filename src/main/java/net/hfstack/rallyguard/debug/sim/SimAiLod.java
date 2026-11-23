package net.hfstack.rallyguard.debug.sim;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

/**
 * F127 (O9, sim only, measurement): "AI by distance". With the scenario tag {@code lod:ai:N:D} the mobs THIS run
 * spawned (both sides and their mounts) skip {@code Mob#serverAiStep} (via {@code mixin/sim/MobAiLodMixin}) on every
 * tick that is not their AI turn while they are more than D blocks (horizontal) from a fixed observer 16 blocks
 * behind blue's anchor. D = 0 = every sim mob (the lever's ceiling). Movement physics ({@code travel}, gravity,
 * knockback, pushing) still run every tick. No tag: {@link #active} stays false and the mixin only reads it.
 * <p>
 * "AI turn" = {@code ((tickCount + id) / 2) % N == 0}: vanilla alternates a full goal tick and a light one by
 * {@code (tickCount + id) % 2}, so each AI window of 2 ticks keeps one of each.
 * Counters are kept here (not one map update per mob per tick, which would itself cost) and written to the run's
 * extras at the end: {@code lod:skipped}, {@code lod:ran}, {@code lod:far}.
 */
public final class SimAiLod {
    private SimAiLod() {
    }

    /** Read by the mixin on every mob's AI step: true only during a sim run with a {@code lod:ai} tag. */
    public static boolean active;

    private static final IntOpenHashSet IDS = new IntOpenHashSet();
    private static int n = 1;
    private static double distSq, ox, oz;
    private static long skipped, ran, far;

    /** Start of a run: reads {@code lod:ai:N:D}; observer = (x, z). Always resets the previous run's state. */
    public static void begin(SimScenario s, double x, double z) {
        reset();
        String spec = s == null ? "" : s.tag("lod");
        if (!spec.startsWith("ai:")) return;
        String[] p = spec.substring(3).split(":");
        try {
            n = Math.max(1, Integer.parseInt(p[0]));
            double d = p.length > 1 ? Double.parseDouble(p[1]) : 0.0D;
            distSq = d <= 0 ? -1.0D : d * d;
        } catch (NumberFormatException e) {
            SimExtras.put("lod:bad", 1.0D);
            return;
        }
        ox = x;
        oz = z;
        SimExtras.put("lod:n", n);
        SimExtras.put("lod:d", distSq < 0 ? 0.0D : Math.sqrt(distSq));
        active = true;
    }

    /** A mob the sim spawned (unit or mount): eligible for the skip. */
    public static void track(Entity e) {
        if (e != null) IDS.add(e.getId());
    }

    /** Called from the mixin: true = skip this tick's AI step. */
    public static boolean skip(Mob mob) {
        if (!IDS.contains(mob.getId()) || mob.level().isClientSide()) return false;
        if (distSq >= 0) {
            double dx = mob.getX() - ox, dz = mob.getZ() - oz;
            if (dx * dx + dz * dz <= distSq) return false;
        }
        far++;
        if (((mob.tickCount + mob.getId()) / 2) % n == 0) {
            ran++;
            return false;
        }
        skipped++;
        return true;
    }

    /** End of a run, before {@code SimExtras.finish}: counters into the extras (also 0 for the control arm). */
    public static void report() {
        SimExtras.put("lod:skipped", skipped);
        SimExtras.put("lod:ran", ran);
        SimExtras.put("lod:far", far);
        active = false;
    }

    public static void reset() {
        active = false;
        IDS.clear();
        n = 1;
        distSq = -1.0D;
        skipped = ran = far = 0;
    }
}
