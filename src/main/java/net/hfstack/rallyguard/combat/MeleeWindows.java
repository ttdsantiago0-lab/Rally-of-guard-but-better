package net.hfstack.rallyguard.combat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * F193 (O7, SIMULATION ONLY, tag {@code melee:sync}): staggered melee blows. Guard Villagers' {@code GuardMeleeGoal}
 * strikes in {@code checkAndPerformAttack} when {@code canPerformAttack(enemy)} is true and only then spends its
 * 20-tick cooldown. A target hit in the last half second ({@code invulnerableTime > 10}) only takes the difference of a
 * stronger blow, so a second guard hitting it then wastes the swing (F186/F187: ~10 such blows per 8 v 8 fight, all by
 * a guard other than the one that opened the window). Holding the blow while the window is open keeps the cooldown, so
 * the swing lands on the first free tick: two guards on one target fall into alternation on their own (A at t, B at
 * t+10...), without changing target or movement. Applied by {@code mixin/sim/GuardMeleeSyncMixin}; off outside a sim
 * run with the tag ({@link #simSync} false costs one field read).
 */
public final class MeleeWindows {
    private MeleeWindows() {
    }

    /** Set by SimExtras.begin from the tag {@code melee:sync}; cleared with FireWindows.reset at batch end. */
    public static boolean simSync = false;

    /** F206 (tag {@code melee:spread}, only with {@code melee:sync}): a guard held 10 ticks in a row switches target. */
    public static boolean simSpread = false;
    private static final int SPREAD_AFTER = 10;
    private static final double SPREAD_REACH = 4.0D;
    private static final java.util.Map<java.util.UUID, Integer> HELD = new java.util.HashMap<>();

    /** Called from FireWindows.reset (batch end / run start). */
    static void resetSpread() {
        HELD.clear();
    }

    /** True if this guard should hold its blow now (owned, group layer on for its side, target in its window). */
    public static boolean hold(Guard guard, LivingEntity enemy) {
        boolean h = holdNow(guard, enemy);
        if (simSpread && guard != null) {
            if (!h) HELD.remove(guard.getUUID());
            else if (HELD.merge(guard.getUUID(), 1, Integer::sum) >= SPREAD_AFTER) spread(guard, enemy);
        }
        return h;
    }

    private static boolean holdNow(Guard guard, LivingEntity enemy) {
        if (!simSync || guard == null || enemy == null) return false;
        if (enemy.invulnerableTime <= 10) return false;
        if (net.hfstack.rallyguard.contract.GuardOwnership.getOwner(guard) == null) return false; // never plain GV (norally:red)
        boolean red = net.hfstack.rallyguard.debug.SimProbe.sideOf(guard) == 1;
        if (red ? FireWindows.simOffRed : FireWindows.simOffBlue) return false; // off:group of its side (as FireWindows.on)
        net.hfstack.rallyguard.debug.sim.SimExtras.count("meleeHold");
        return true;
    }

    /**
     * F206: nearest live enemy (other sim side) within 4 blocks, not the current target, out of its window and accepted
     * by CommandTargeting; setTarget goes through GuardStanceHandler#onChangeTarget (Engagement cap). If the gate
     * refuses, the counter stays and it is tried again next tick.
     */
    private static void spread(Guard guard, LivingEntity current) {
        if (!(guard.level() instanceof ServerLevel level)) return;
        int mine = net.hfstack.rallyguard.debug.SimProbe.sideOf(guard);
        if (mine < 0) return;
        java.util.List<LivingEntity> near = level.getEntitiesOfClass(LivingEntity.class,
                guard.getBoundingBox().inflate(SPREAD_REACH), e -> e != current && e != guard && e.isAlive()
                        && e.invulnerableTime <= 10 && net.hfstack.rallyguard.debug.SimProbe.sideOf(e) >= 0
                        && net.hfstack.rallyguard.debug.SimProbe.sideOf(e) != mine);
        LivingEntity best = null;
        double bestD = Double.MAX_VALUE;
        int checked = 0;
        for (LivingEntity e : near) {
            if (++checked > 16) break; // circuit breaker: never scan a crowd
            double d = guard.distanceTo(e);
            if (d > SPREAD_REACH || d >= bestD) continue;
            if (!net.hfstack.rallyguard.order.CommandTargeting.wouldAccept(guard, e)) continue;
            best = e;
            bestD = d;
        }
        if (best == null) return;
        guard.setTarget(best);
        if (guard.getTarget() != best) return; // the gate said no: do not insist this tick
        HELD.put(guard.getUUID(), 0);
        net.hfstack.rallyguard.debug.sim.SimExtras.count("meleeSwitch");
    }
}
