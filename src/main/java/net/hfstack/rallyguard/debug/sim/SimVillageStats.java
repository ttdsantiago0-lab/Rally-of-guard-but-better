package net.hfstack.rallyguard.debug.sim;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * F29 (2026-11-17, telemetry only, sim only): where the red units left alive in the village are, and whether anybody
 * can see them. Written into "extra" only when the run's terrain is {@code village}:
 * <ul>
 *     <li>at spawn: {@code aldea:redStartIn} (a solid block 2-4 above the feet: under a roof), {@code aldea:redStartRoof}
 *     (feet 3 or more above the arena floor: on a roof or a wall);</li>
 *     <li>at the end, over the living red units: {@code aldea:redRoof}, {@code aldea:redIn}, {@code aldea:redStreet}
 *     (the rest), {@code aldea:redNoTarget} (no living target), {@code aldea:nnDist} (mean flat-plus-height distance
 *     to the nearest living guard, -1 if none);</li>
 *     <li>at the end, over the living guards: {@code aldea:blueNoTarget}, {@code aldea:blueNoLos} (a living target
 *     but no line of sight to it).</li>
 * </ul>
 * Roof is tested before indoors (a unit on a roof has nothing over it anyway).
 */
public final class SimVillageStats {
    private SimVillageStats() {
    }

    private static boolean on(SimArena arena, SimScenario s) {
        return arena != null && s != null && "village".equals(s.terrain());
    }

    /** 2 = roof, 1 = indoors, 0 = street. */
    private static int place(SimArena arena, LivingEntity e) {
        BlockPos feet = e.blockPosition();
        if (feet.getY() >= arena.y + 3) return 2;
        ServerLevel level = arena.level;
        for (int dy = 2; dy <= 4; dy++) {
            BlockPos p = feet.above(dy);
            if (!level.getBlockState(p).getCollisionShape(level, p).isEmpty()) return 1;
        }
        return 0;
    }

    /** Right after both sides spawned. */
    public static void start(SimArena arena, SimScenario s, List<LivingEntity> red) {
        if (!on(arena, s)) return;
        int in = 0, roof = 0;
        for (LivingEntity e : red) {
            if (!e.isAlive()) continue;
            int k = place(arena, e);
            if (k == 2) roof++;
            else if (k == 1) in++;
        }
        SimExtras.put("aldea:redStartIn", in);
        SimExtras.put("aldea:redStartRoof", roof);
    }

    /** In endRun, before the units are removed. */
    public static void end(SimArena arena, SimScenario s, List<Guard> blue, List<LivingEntity> red) {
        if (!on(arena, s)) return;
        int in = 0, roof = 0, street = 0, redNoTarget = 0, blueNoTarget = 0, blueNoLos = 0, nnN = 0;
        double nnSum = 0;
        for (LivingEntity e : red) {
            if (!e.isAlive()) continue;
            int k = place(arena, e);
            if (k == 2) roof++;
            else if (k == 1) in++;
            else street++;
            LivingEntity t = e instanceof Mob m ? m.getTarget() : null;
            if (t == null || !t.isAlive()) redNoTarget++;
            double best = Double.MAX_VALUE;
            for (Guard g : blue) if (g.isAlive()) best = Math.min(best, e.distanceToSqr(g));
            if (best < Double.MAX_VALUE) {
                nnSum += Math.sqrt(best);
                nnN++;
            }
        }
        for (Guard g : blue) {
            if (!g.isAlive()) continue;
            LivingEntity t = g.getTarget();
            if (t == null || !t.isAlive()) blueNoTarget++;
            else if (!g.hasLineOfSight(t)) blueNoLos++;
        }
        SimExtras.put("aldea:redIn", in);
        SimExtras.put("aldea:redRoof", roof);
        SimExtras.put("aldea:redStreet", street);
        SimExtras.put("aldea:redNoTarget", redNoTarget);
        SimExtras.put("aldea:blueNoTarget", blueNoTarget);
        SimExtras.put("aldea:blueNoLos", blueNoLos);
        SimExtras.put("aldea:nnDist", nnN == 0 ? -1 : Math.round(nnSum / nnN * 10) / 10.0D);
    }
}
