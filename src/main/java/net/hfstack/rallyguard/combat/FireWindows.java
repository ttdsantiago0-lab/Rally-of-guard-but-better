package net.hfstack.rallyguard.combat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 2026-11-11 (RALLY_TACTICAL_SYSTEM.md, phase 2, "fuego coordinado"): Minecraft gives a creature half a second of
 * invulnerability after each hit (it only takes the difference if a stronger blow lands in that half second). Rounds
 * 33-35 measured the consequence: past a few guards, more archers on ONE target add almost nothing (i-frames cap the
 * damage), while spreading shots over several targets or timing them does. This class books, per target, the tick at
 * which the last arrow aimed at it will land, so an archer whose arrow would land inside that window can do something
 * better:
 * <ul>
 *     <li>{@code SPREAD} - shoot another enemy whose window is free (nearest to the archer, within reach and in sight);
 *     with no such enemy, shoot anyway;</li>
 *     <li>{@code SYNC} - the same, and with no other enemy wait for the window instead of wasting the arrow.</li>
 * </ul>
 * Cost: a map lookup per shot; an entity query only on a conflict, capped to the first 16 candidates. Off (the old
 * behaviour) unless a simulation scenario carries {@code fire:spread} or {@code fire:sync}: it becomes a default only
 * after it is measured (docs/LABORATORIO.md protocol).
 */
public final class FireWindows {
    private FireWindows() {
    }

    public enum Mode { OFF, SPREAD, SYNC }

    /** Set per simulation run from its tags; OFF everywhere else until measured. */
    public static Mode mode = Mode.OFF;
    private static final int WINDOW = 10; // ticks: LivingEntity#hurt ignores weaker blows while invulnerableTime > 10
    private static final Map<Integer, Long> LANDING = new HashMap<>();

    public static void reset(Mode m) {
        mode = m;
        LANDING.clear();
    }

    public static boolean on() {
        return mode != Mode.OFF;
    }

    /** True if an arrow landing at {@code arrive} would fall inside the target's invulnerability window. */
    public static boolean busy(LivingEntity target, long now, long arrive) {
        if (target.invulnerableTime - (arrive - now) > WINDOW) return true; // still invulnerable from a hit (any source)
        Long booked = LANDING.get(target.getId());
        return booked != null && Math.abs(arrive - booked) < WINDOW;
    }

    public static void book(LivingEntity target, long now, long arrive) {
        if (LANDING.size() > 512) {
            for (Iterator<Long> it = LANDING.values().iterator(); it.hasNext(); ) if (it.next() < now - 20) it.remove();
        }
        LANDING.merge(target.getId(), arrive, Math::max);
    }

    /** Another enemy this archer can hit whose window is free, or null. */
    public static LivingEntity alternative(Guard guard, LivingEntity current, double reach, long now, double arrowSpeed) {
        if (!(guard.level() instanceof ServerLevel level)) return null;
        double r = Math.min(reach, 32.0D);
        List<LivingEntity> near = level.getEntitiesOfClass(LivingEntity.class, guard.getBoundingBox().inflate(r),
                e -> e != current && e != guard && e.isAlive() && hostile(guard, e));
        LivingEntity best = null;
        double bestD = Double.MAX_VALUE;
        int checked = 0;
        for (LivingEntity e : near) {
            if (++checked > 16) break; // circuit breaker: never scan a crowd
            double d = guard.distanceTo(e);
            if (d > r || d >= bestD) continue;
            long arrive = now + Math.max(1L, Math.round(d / (arrowSpeed * 0.9D)));
            if (busy(e, now, arrive) || !guard.getSensing().hasLineOfSight(e)) continue;
            best = e;
            bestD = d;
        }
        return best;
    }

    private static boolean hostile(Guard guard, LivingEntity e) {
        java.util.UUID mine = net.hfstack.rallyguard.contract.GuardOwnership.getOwner(guard);
        if (e instanceof Guard other) { // an enemy army's guard: hostile while it fights one of ours
            java.util.UUID theirs = net.hfstack.rallyguard.contract.GuardOwnership.getOwner(other);
            if (mine != null && mine.equals(theirs)) return false;
            return other.getTarget() instanceof Guard;
        }
        if (e instanceof Mob m && m.getTarget() instanceof Guard) return true;
        return e instanceof Enemy && !(e instanceof net.minecraft.world.entity.monster.Creeper);
    }
}
