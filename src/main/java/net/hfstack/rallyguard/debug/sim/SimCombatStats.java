package net.hfstack.rallyguard.debug.sim;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.damagesource.DamageSource;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 2026-11-09 (David): per-battle measures that explain WHY a fight goes the way it goes, not only who won. Written into
 * each run's "extra":
 * <ul>
 *     <li>{@code dmgDealt} - health the blue side really took off the red side (after armour, caps, resistances);
 *     {@code guardSeconds} - seconds each blue guard stayed alive, summed; {@code dps} = dmgDealt / guardSeconds, the
 *     damage one guard deals per second alive. Against the training dummy (round 33) it is the guard's ceiling; against a
 *     monster, the ratio to the dummy says how much the monster's own defences take away;</li>
 *     <li>{@code hurt:<type>} - damage the blue side took by damage type (breath, laser, stomp, arrow...): which attack
 *     does the killing;</li>
 *     <li>{@code aoeHits} - times one attacker hurt 3 or more guards in the same tick (bunched up under an area attack);</li>
 *     <li>{@code hitsOnRed}, {@code wastedHits} - blue attacks that reached a red unit, and those its own rules cancelled
 *     (immune phase, shield, invulnerability): attacking when it can't be hurt;</li>
 *     <li>{@code iframeHits}, {@code iframeDmg} (2026-11-11) - hits that landed while the target was still invulnerable from
 *     the previous one: the waste that coordinated fire ({@code combat.FireWindows}) tries to remove;</li>
 *     <li>{@code nnDist} - mean distance from each guard to its nearest comrade (sampled each second): how spread out;</li>
 *     <li>{@code aggroShield}, {@code aggroOther}, {@code aggroSwitches} - seconds the red units targeted a shield-bearer
 *     or another guard, and how often they changed target: who draws the boss.</li>
 * </ul>
 */
public final class SimCombatStats {
    private SimCombatStats() {
    }

    private static double dmgDealt, guardTicks, nnSum, aggroShield, aggroOther, iframeDmg, mountDmg, mountedTicks;
    private static int shotsMoving, shotsStill, hitsMoving, hitsStill, mountDeaths, mountsN;
    private static int nnN, aoeHits, hitsOnRed, wastedHits, iframeHits, aggroSwitches, hitTick = -1;
    private static final Map<String, Double> HURT = new HashMap<>();
    private static final Map<UUID, Set<UUID>> VICTIMS = new HashMap<>();
    private static final Map<UUID, UUID> LAST_TARGET = new HashMap<>();

    public static void begin() {
        dmgDealt = guardTicks = nnSum = aggroShield = aggroOther = iframeDmg = mountDmg = mountedTicks = 0;
        shotsMoving = shotsStill = hitsMoving = hitsStill = mountDeaths = mountsN = 0;
        nnN = aoeHits = hitsOnRed = wastedHits = iframeHits = aggroSwitches = 0;
        hitTick = -1;
        HURT.clear();
        VICTIMS.clear();
        LAST_TARGET.clear();
    }

    /** Every blue-vs-red damage that landed (LivingDamageEvent.Post): v = victim side, a = attacker side. */
    public static void damage(LivingEntity victim, Entity attacker, DamageSource src, float amount, int v, int a, int tick) {
        if (a == 0 && v == 1) dmgDealt += amount;
        if (v == 0 && a == 1) {
            HURT.merge("hurt:" + src.getMsgId(), (double) amount, Double::sum);
            if (attacker != null) {
                if (tick != hitTick) {
                    VICTIMS.clear();
                    hitTick = tick;
                }
                Set<UUID> s = VICTIMS.computeIfAbsent(attacker.getUUID(), k -> new HashSet<>());
                if (s.add(victim.getUUID()) && s.size() == 3) aoeHits++;
            }
        }
    }

    /** A blue attack that reached a red unit (LivingIncomingDamageEvent, cancelled ones included). */
    public static void incoming(boolean cancelled, boolean inIframes, float amount) {
        hitsOnRed++;
        if (cancelled) wastedHits++;
        else if (inIframes) { // 2026-11-11: landed inside the half-second of invulnerability (only the excess counts)
            iframeHits++;
            iframeDmg += amount;
        }
    }

    /** F2 (2026-11-11): cavalry diagnosis - horse damage and deaths, time in the saddle, accuracy moving vs still. */
    public static void mountHurt(float amount) {
        mountDmg += amount;
    }

    public static void shot(boolean moving) {
        if (moving) shotsMoving++;
        else shotsStill++;
    }

    public static void arrowHit(boolean moving) {
        if (moving) hitsMoving++;
        else hitsStill++;
    }

    public static void mountsEnd(List<? extends Entity> mounts) {
        mountsN = mounts.size();
        for (Entity m : mounts) if (!m.isAlive()) mountDeaths++;
    }

    /** Each fight tick. */
    public static void tick(int tick, List<Guard> blue, List<LivingEntity> red) {
        guardTicks += blue.size();
        for (Guard g : blue) if (g.isPassenger()) mountedTicks++;
        if (tick % 20 != 0) return;
        for (Guard g : blue) {
            double best = Double.MAX_VALUE;
            for (Guard o : blue) if (o != g) best = Math.min(best, g.distanceToSqr(o));
            if (best < Double.MAX_VALUE) {
                nnSum += Math.sqrt(best);
                nnN++;
            }
        }
        for (LivingEntity e : red) {
            if (!(e instanceof Mob m) || !m.isAlive()) continue;
            LivingEntity t = m.getTarget();
            if (!(t instanceof Guard)) continue;
            if (net.hfstack.rallyguard.combat.FieldManual.shieldBearer((Guard) t)) aggroShield++;
            else aggroOther++;
            UUID before = LAST_TARGET.put(m.getUUID(), t.getUUID());
            if (before != null && !before.equals(t.getUUID())) aggroSwitches++;
        }
    }

    public static void end() {
        double gs = guardTicks / 20.0D;
        SimExtras.put("dmgDealt", Math.round(dmgDealt * 10) / 10.0D);
        SimExtras.put("guardSeconds", Math.round(gs * 10) / 10.0D);
        SimExtras.put("dps", gs <= 0 ? 0 : Math.round(dmgDealt / gs * 100) / 100.0D);
        SimExtras.put("aoeHits", aoeHits);
        SimExtras.put("hitsOnRed", hitsOnRed);
        SimExtras.put("wastedHits", wastedHits);
        if (mountsN > 0 || shotsMoving + shotsStill > 0) {
            SimExtras.put("mountDmg", Math.round(mountDmg * 10) / 10.0D);
            SimExtras.put("mountDeaths", mountDeaths);
            SimExtras.put("mountedShare", guardTicks <= 0 ? 0 : Math.round(mountedTicks / guardTicks * 100) / 100.0D);
            SimExtras.put("shotsMoving", shotsMoving);
            SimExtras.put("shotsStill", shotsStill);
            SimExtras.put("accMoving", shotsMoving == 0 ? -1 : Math.round(100.0D * hitsMoving / shotsMoving) / 100.0D);
            SimExtras.put("accStill", shotsStill == 0 ? -1 : Math.round(100.0D * hitsStill / shotsStill) / 100.0D);
        }
        SimExtras.put("iframeHits", iframeHits); // hits that landed inside the target's invulnerability half-second
        SimExtras.put("iframeDmg", Math.round(iframeDmg * 10) / 10.0D); // their raw damage (only the excess over the last hit counted)
        SimExtras.put("nnDist", nnN == 0 ? -1 : Math.round(nnSum / nnN * 10) / 10.0D);
        SimExtras.put("aggroShield", aggroShield);
        SimExtras.put("aggroOther", aggroOther);
        SimExtras.put("aggroSwitches", aggroSwitches);
        for (Map.Entry<String, Double> e : HURT.entrySet()) SimExtras.put(e.getKey(), Math.round(e.getValue() * 10) / 10.0D);
    }
}
