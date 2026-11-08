package net.hfstack.rallyguard.debug.sim;

import net.hfstack.rallyguard.combat.WeaponProfile;
import net.hfstack.rallyguard.debug.SimProbe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Sonda de techo, laboratorio A (docs/SONDA_DE_TECHO.md, David 2026-11-08): the "guard that cheats". Simulation only -
 * nothing here runs outside a sim batch, and every switch is off unless the scenario carries its tag.
 * <p>
 * It cheats on INFORMATION and DECISIONS, never on the body (speed, damage, health, arrow physics stay Guard Villagers'):
 * <ul>
 *     <li>{@code oracle:aim} - lead with the target's full motion, falling included (the pro's second arrow), and the
 *     tightest spread the rank ladder allows;</li>
 *     <li>{@code oracle:cadence} - a bow's physical limit (20 ticks, a full draw) instead of Rally's 30-tick rule; this
 *     measures a RULE of ours, not a cheat of information;</li>
 *     <li>{@code oracle:target} - perfect knowledge of every enemy: shooters take the weakest enemy they can see within
 *     reach, melee the nearest; nobody is ever without a target;</li>
 *     <li>{@code oracle:kite} - an archer steps back from any melee enemy within 6 blocks to 11, then shoots again;</li>
 *     <li>{@code oracle:all} - all of the above.</li>
 * </ul>
 */
public final class SimOracle {
    private SimOracle() {
    }

    public static boolean aim, cadence, target, kite;
    private static final Map<UUID, Integer> KITING = new HashMap<>();

    public static void begin(SimScenario s) {
        boolean all = s.tags().contains("oracle:all");
        aim = all || s.tags().contains("oracle:aim");
        cadence = all || s.tags().contains("oracle:cadence");
        target = all || s.tags().contains("oracle:target");
        kite = all || s.tags().contains("oracle:kite");
        KITING.clear();
    }

    public static void reset() {
        aim = cadence = target = kite = false;
        KITING.clear();
    }

    public static boolean any() {
        return aim || cadence || target || kite;
    }

    /** While true, Rally's own archery movement leaves this guard's navigation alone. */
    public static boolean kiting(Guard g) {
        return kite && KITING.containsKey(g.getUUID());
    }

    /** Where the target's centre will be in {@code ticks}: full motion, gravity for a falling target, never underground. */
    public static Vec3 predict(LivingEntity t, double ticks) {
        Vec3 c = new Vec3(t.getX(), t.getY(0.5D), t.getZ());
        Vec3 v = t.getDeltaMovement();
        double x = c.x + v.x * ticks, z = c.z + v.z * ticks, y = c.y;
        if (t.isNoGravity()) {
            y = c.y + v.y * ticks; // flyers: straight line
        } else if (!t.onGround()) {
            y = c.y + v.y * ticks - 0.5D * 0.08D * ticks * ticks; // falling or jumping
            int ground = t.level().getHeight(Heightmap.Types.MOTION_BLOCKING, (int) Math.floor(x), (int) Math.floor(z));
            y = Math.max(y, ground + t.getBbHeight() * 0.5D);
        }
        return new Vec3(x, y, z);
    }

    /** Every 5 ticks: targets and kiting for the blue guards. */
    public static void tick(ServerLevel level, int tick, List<Guard> blue, List<LivingEntity> red) {
        if (!(target || kite) || tick % 5 != 0) return;
        for (Guard g : blue) {
            WeaponProfile w = WeaponProfile.forGuard(g);
            boolean shooter = w != null && w.firesArrows();
            if (target) pickTarget(g, shooter, w, red);
            if (kite && shooter) kite(g, red);
        }
    }

    private static void pickTarget(Guard g, boolean shooter, WeaponProfile w, List<LivingEntity> red) {
        LivingEntity best = null;
        double bestScore = Double.MAX_VALUE;
        for (LivingEntity e : red) {
            if (e == null || !e.isAlive()) continue;
            double d = g.distanceTo(e);
            double score;
            if (shooter) {
                boolean usable = d <= w.maxRange() && g.hasLineOfSight(e);
                score = usable ? e.getHealth() : 1000.0D + d; // weakest we can shoot, else the nearest
            } else {
                score = d;
            }
            if (score < bestScore) {
                bestScore = score;
                best = e;
            }
        }
        if (best != null && best != g.getTarget()) {
            g.setTarget(best);
            SimProbe.count(g, "act:oracleTarget");
        }
    }

    private static void kite(Guard g, List<LivingEntity> red) {
        LivingEntity near = null;
        double nd = 6.0D;
        for (LivingEntity e : red) {
            if (e == null || !e.isAlive() || e instanceof RangedAttackMob) continue;
            double d = g.distanceTo(e);
            if (d < nd) {
                nd = d;
                near = e;
            }
        }
        UUID id = g.getUUID();
        if (near != null) {
            Vec3 away = g.position().subtract(near.position()).multiply(1, 0, 1);
            away = away.lengthSqr() < 1.0E-4 ? new Vec3(1, 0, 0) : away.normalize();
            Vec3 to = g.position().add(away.scale(11.0D - nd));
            g.getNavigation().moveTo(to.x, to.y, to.z, 1.2D);
            if (KITING.put(id, 40) == null) SimProbe.count(g, "act:oracleKite");
        } else {
            Integer left = KITING.get(id);
            if (left != null) {
                if (left <= 5) KITING.remove(id);
                else KITING.put(id, left - 5);
            }
        }
    }
}
