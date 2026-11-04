package net.hfstack.rallyguard.morale;

import net.hfstack.rallyguard.combat.FriendIndex;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.debug.SimProbe;
import net.hfstack.rallyguard.event.GuardStanceHandler;
import net.hfstack.rallyguard.force.Leadership;
import net.hfstack.rallyguard.force.SituationReports;
import net.hfstack.rallyguard.force.UnitOrders;
import net.hfstack.rallyguard.order.CommandTargeting;
import net.hfstack.rallyguard.order.HqRelax;
import net.hfstack.rallyguard.tactics.DecisionLadder;
import net.hfstack.rallyguard.tactics.DecisionReason;
import net.hfstack.rallyguard.tactics.DecisionTrace;
import net.hfstack.rallyguard.tactics.Tactics;
import net.hfstack.rallyguard.veterancy.Threat;
import net.hfstack.rallyguard.veterancy.Trait;
import net.hfstack.rallyguard.veterancy.Veterancy;
import net.hfstack.rallyguard.veterancy.VeterancyEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * E3B M ({@code docs/MORAL_Y_COHESION.md}) - each owned guard's morale, 0..100: its will to keep
 * fighting. It changes DECISIONS only (preferences in the ladder and the stance gate) - never
 * health, damage or speed, and a fleeing guard keeps its kit and comes back.
 * <ul>
 *     <li><b>Events</b> (applied at the guard's next turn): hits (more from the flank or the back),
 *     deaths of comrades near it (more for the captain; less with distance; diminishing), kills by
 *     its side (momentum).</li>
 *     <li><b>Once a second</b>, staggered with the stance loop: casualties of its unit (strongest
 *     between 10 % and 30 %), local odds, low health, isolation, a threat out of scale, no captain,
 *     fatigue, broken neighbours (contagion, bounded); and up: its captain near (by leadership score),
 *     the HQ near, reinforcements, its formation kept, rest. Negatives × the rank's sensitivity
 *     (config; × 0.85 for Steady or Tank). Ranks 4-5 near raise the BASE their neighbours drift
 *     back to ("example", ≤ +10, diminishing).</li>
 *     <li><b>States</b> {@link MoraleState} with hysteresis (config thresholds) and a minimum time
 *     in each state; ROTO only climbs back by RALLYING (captain or HQ banner within
 *     {@code morale.rallyRadius} and morale over the rally line; a good captain rallies faster);
 *     DESTROZADO (broken, no captain or general, over half the unit lost) only by resting at the HQ.</li>
 * </ul>
 * {@code morale.strength} scales the effects (0 = measured and shown only); {@code morale.enabled}
 * off = nothing at all. Cost: a few map reads and one friend-index count per guard and second.
 */
public final class Morale {
    private Morale() {
    }

    static final class Mo {
        double value = 70.0D;
        MoraleState state = MoraleState.FIRME;
        long since;
        double pending;
        String cause = "";
        double causeWeight;
        long restSince = -1;
        long contactSince = -1;
        int lastAllies = -1;
        double deathsRecent;
        long deathsAt;
        long firstBroken = -1;
    }

    private static final Map<UUID, Mo> MORALE = new HashMap<>();
    /** Per commander: where its broken guards and its rank-4/5 steady guards stood this second. */
    private static final Map<UUID, List<Vec3>> BROKEN = new HashMap<>(), EXAMPLES = new HashMap<>();
    private static long indexTick = Long.MIN_VALUE / 2;

    // ==== Config ====

    public static boolean enabled() {
        return RallyConfig.on(RallyConfig.MORALE_ENABLED);
    }

    /** 0..1: how much morale changes decisions (0 when disabled). */
    public static double strength() {
        return enabled() ? RallyConfig.d(RallyConfig.MORALE_STRENGTH, 0.6D) : 0.0D;
    }

    private static final double[] W = {30, 0.5, 6, 10, 3, 1.5, 1, 1, 1.5, 1, 1, 2, 1.5, 0.5, 1.5, 2, 0.3, 0.33, 0.5};
    private static final double[] SENS = {1.0, 0.9, 0.8, 0.68, 0.55, 0.45};
    private static final double[] THR = {55, 38, 20, 62, 45, 28, 30};

    static double w(int i) {
        List<? extends Double> l = RallyConfig.get(RallyConfig.MORALE_WEIGHTS::get, List.of());
        return i < l.size() ? l.get(i) : W[i];
    }

    static double thr(int i) {
        List<? extends Double> l = RallyConfig.get(RallyConfig.MORALE_THRESHOLDS::get, List.of());
        return i < l.size() ? l.get(i) : THR[i];
    }

    /** Multiplier of negative factors for this guard (rank table, × 0.85 Steady/Tank). */
    public static double sensitivity(Guard g) {
        int rank = Veterancy.rank(g);
        List<? extends Double> l = RallyConfig.get(RallyConfig.MORALE_SENSITIVITY::get, List.of());
        double s = rank < l.size() ? l.get(rank) : SENS[Math.min(rank, SENS.length - 1)];
        if (Veterancy.has(g, Trait.STEADY) || Veterancy.has(g, Trait.TANK)) s *= 0.85D;
        return s;
    }

    // ==== Reads ====

    public static MoraleState state(Guard g) {
        if (!enabled()) return MoraleState.FIRME;
        Mo m = MORALE.get(g.getUUID());
        return m == null ? MoraleState.FIRME : m.state;
    }

    /** 0..100, or -1 when morale is off. */
    public static double value(Guard g) {
        if (!enabled()) return -1;
        Mo m = MORALE.get(g.getUUID());
        return m == null ? 70.0D : m.value;
    }

    /** Lang key of the main cause of the last drop ("" = none). */
    public static String cause(Guard g) {
        Mo m = MORALE.get(g.getUUID());
        return m == null ? "" : m.cause;
    }

    /** Mean morale of these guards (-1 when off or empty). */
    public static double mean(Collection<Guard> guards) {
        if (!enabled() || guards.isEmpty()) return -1;
        double s = 0;
        for (Guard g : guards) s += value(g);
        return s / guards.size();
    }

    /** How many of these are broken (ROTO / DESTROZADO). */
    public static int broken(Collection<Guard> guards) {
        int n = 0;
        for (Guard g : guards) if (state(g).broken()) n++;
        return n;
    }

    /** The worst state among these (FIRME if none). */
    public static MoraleState worst(Collection<Guard> guards) {
        MoraleState w = MoraleState.FIRME;
        for (Guard g : guards) if (state(g).worseThan(w)) w = state(g);
        return w;
    }

    // ==== Events ====

    /** A hit on an owned guard: dmg as share of its health × the weight; from the flank or back counts more. */
    public static void hit(Guard victim, @Nullable LivingEntity attacker, float damage) {
        if (!enabled() || damage <= 0) return;
        double loss = W0() * damage / Math.max(1.0F, victim.getMaxHealth());
        if (attacker != null) {
            double yaw = Math.toRadians(victim.getYRot()); // same convention as Formation: forward = (-sin, cos)
            Vec3 look = new Vec3(-Math.sin(yaw), 0, Math.cos(yaw));
            Vec3 to = attacker.position().subtract(victim.position()).multiply(1, 0, 1);
            if (to.lengthSqr() > 1.0E-4 && look.dot(to.normalize()) < 0.0D) {
                loss *= 1.0D + w(1);
                push(victim, loss, "gui.rallyguard.morale.cause.flank");
                return;
            }
        }
        push(victim, loss, "gui.rallyguard.morale.cause.hit");
    }

    private static double W0() {
        return w(0);
    }

    /** A comrade died: every guard of the same commander within 16 blocks loses morale (diminishing). */
    public static void allyDied(ServerLevel level, Guard dead, boolean wasCaptain) {
        if (!enabled()) return;
        UUID owner = GuardOwnership.getOwner(dead);
        if (owner == null) return;
        long now = level.getGameTime();
        for (Guard g : GuardStanceHandler.loaded(level.dimension())) {
            if (g == dead || !g.isAlive() || !owner.equals(GuardOwnership.getOwner(g))) continue;
            double d = g.distanceTo(dead);
            if (d > 16.0D) continue;
            Mo m = mo(g);
            if (now - m.deathsAt > 200) m.deathsRecent = 0;
            m.deathsAt = now;
            double loss = (w(2) + (wasCaptain ? w(3) : 0.0D)) * (1.0D - d / 16.0D) / (1.0D + m.deathsRecent);
            m.deathsRecent += 1.0D;
            push(g, loss, wasCaptain ? "gui.rallyguard.morale.cause.captain_died" : "gui.rallyguard.morale.cause.comrade_died");
        }
    }

    /** One of this commander's guards killed an enemy: momentum for its comrades within 16. */
    public static void enemyKilled(ServerLevel level, Guard killer) {
        if (!enabled()) return;
        UUID owner = GuardOwnership.getOwner(killer);
        if (owner == null) return;
        for (Guard g : GuardStanceHandler.loaded(level.dimension())) {
            if (g.isAlive() && owner.equals(GuardOwnership.getOwner(g)) && g.distanceToSqr(killer) <= 256.0D) {
                mo(g).pending += w(14);
            }
        }
    }

    /** E3B M: the unit lost more captains than it can replace ({@code CaptainSuccession}) - its routed state IS ROTO. */
    public static void breakUnit(Collection<Guard> unit) {
        if (!enabled()) return;
        for (Guard g : unit) {
            Mo m = mo(g);
            m.value = Math.min(m.value, thr(2) - 1.0D);
            setState(g, m, MoraleState.ROTO, "gui.rallyguard.morale.cause.unit_routed");
        }
    }

    private static void push(Guard g, double loss, String cause) {
        Mo m = mo(g);
        double l = loss * sensitivity(g);
        m.pending -= l;
        if (l >= m.causeWeight || m.cause.isEmpty()) {
            m.cause = cause;
            m.causeWeight = l;
        }
    }

    private static Mo mo(Guard g) {
        return MORALE.computeIfAbsent(g.getUUID(), k -> {
            Mo m = new Mo();
            m.since = g.level().getGameTime();
            return m;
        });
    }

    // ==== Once a second per guard ====

    public static void second(ServerLevel level, Guard guard) {
        if (!enabled()) return;
        long now = level.getGameTime();
        rebuildIndex(level, now);
        Mo m = mo(guard);
        showState(level, guard, m.state); // David 2026-11-04: see fear, so a fleeing guard isn't mistaken for a bug
        UUID owner = GuardOwnership.getOwner(guard);
        double sens = sensitivity(guard);
        double health = guard.getHealth() / Math.max(1.0F, guard.getMaxHealth());
        boolean fighting = VeterancyEvents.inFight(guard);
        double delta = m.pending;
        m.pending = 0;
        m.causeWeight *= 0.8D;

        // -- negatives (× sensitivity) --
        String unitKey = UnitOrders.unitKeyOf(level, guard);
        SituationReports.Report report = unitKey == null ? null : SituationReports.last(unitKey);
        double casualties = report == null ? 0 : report.casualties();
        double pressure = smooth((casualties - 0.10D) / 0.20D);
        if (pressure > 0) delta -= neg(m, w(4) * pressure * sens, "gui.rallyguard.morale.cause.casualties");
        int[] odds = Tactics.odds(guard);
        if (odds != null && odds[0] > odds[1]) {
            delta -= neg(m, w(5) * Math.min(3.0D, (double) odds[0] / odds[1] - 1.0D) * sens, "gui.rallyguard.morale.cause.outnumbered");
        }
        if (fighting && health < 0.3D) delta -= neg(m, w(6) * sens, "gui.rallyguard.morale.cause.wounded");
        int allies = owner == null ? 1 : FriendIndex.countNear(level, owner, guard.position(), 8.0D);
        if (fighting && allies <= 1) delta -= neg(m, w(7) * sens, "gui.rallyguard.morale.cause.alone");
        LivingEntity target = guard.getTarget();
        if (target != null && target.isAlive()) {
            double threat = Threat.of(target);
            if (threat >= 4.0D) delta -= neg(m, w(8) * threat / 8.0D * sens, "gui.rallyguard.morale.cause.monster");
        }
        Guard captain = captainOf(level, guard);
        if (fighting && captain == null) delta -= neg(m, w(9) * sens, "gui.rallyguard.morale.cause.no_captain");
        if (fighting) {
            if (m.contactSince < 0) m.contactSince = now;
            if (now - m.contactSince > 400 && health < 0.5D) delta -= neg(m, w(10) * sens, "gui.rallyguard.morale.cause.fatigue");
        } else {
            m.contactSince = -1;
        }
        int brokenNear = owner == null ? 0 : countNear(BROKEN.get(owner), guard.position(), 8.0D, guard);
        if (brokenNear > 0) {
            delta -= neg(m, w(11) * Math.min(3.0D, Math.sqrt(brokenNear)) * sens, "gui.rallyguard.morale.cause.contagion");
        }

        // -- positives --
        boolean leaderNear = captain != null && captain != guard && captain.distanceToSqr(guard) <= 256.0D;
        if (leaderNear) delta += w(12) * Leadership.score(captain) / 100.0D;
        BlockPos hq = HqRelax.hqOf(level, guard);
        double hqR = RallyConfig.i(RallyConfig.HQ_RADIUS, 16);
        boolean hqNear = hq != null && guard.distanceToSqr(Vec3.atCenterOf(hq)) <= hqR * hqR;
        if (hqNear) delta += w(13);
        int alliesWide = owner == null ? 1 : FriendIndex.countNear(level, owner, guard.position(), 16.0D);
        if (m.lastAllies >= 0 && alliesWide - m.lastAllies >= 2) delta += w(15) * Math.min(3.0D, (alliesWide - m.lastAllies) / 2.0D);
        m.lastAllies = alliesWide;
        BlockPos post = CommandTargeting.postOf(guard);
        SquadDirectives.Directive d = SquadDirectives.of(guard.getUUID());
        if (post != null && d != null && (d.kind() == SquadDirectives.Kind.HOLD || d.kind() == SquadDirectives.Kind.MUSTER)
                && guard.distanceToSqr(Vec3.atBottomCenterOf(post)) <= 9.0D) {
            delta += w(16);
        }
        boolean resting = HqRelax.isRelaxed(guard);
        if (resting) delta += w(17);
        double base = 70.0D + example(owner, guard);
        if (m.value < base && delta >= -0.01D) delta += w(18);

        m.value = Math.max(0.0D, Math.min(100.0D, m.value + delta));
        if (m.value > base && delta <= 0 && !fighting) m.value = Math.max(base, m.value - 0.5D);

        // -- rally (broken / wavering, captain or banner close, over the rally line) --
        double rallyR = RallyConfig.i(RallyConfig.MORALE_RALLY_RADIUS, 16);
        boolean rallyPoint = (captain != null && captain != guard && captain.distanceToSqr(guard) <= rallyR * rallyR)
                || (hq != null && guard.distanceToSqr(Vec3.atCenterOf(hq)) <= rallyR * rallyR);
        if (m.state.broken() && m.state != MoraleState.DESTROZADO && rallyPoint) {
            m.value = Math.min(100.0D, m.value + 2.0D + 4.0D * (captain == null || captain == guard ? 0.5D : Leadership.score(captain) / 100.0D));
        }
        transition(level, guard, m, now, captain, casualties, rallyPoint, resting);
    }

    private static double neg(Mo m, double amount, String cause) {
        if (amount >= m.causeWeight || m.cause.isEmpty()) {
            m.cause = cause;
            m.causeWeight = amount;
        }
        return amount;
    }

    private static double smooth(double x) {
        double t = Math.max(0.0D, Math.min(1.0D, x));
        return t * t * (3.0D - 2.0D * t);
    }

    /** Ranks 4-5 within 8 blocks raise the base: +5 for the first, diminishing, at most +10. */
    private static double example(@Nullable UUID owner, Guard guard) {
        if (owner == null) return 0;
        int n = countNear(EXAMPLES.get(owner), guard.position(), 8.0D, guard);
        if (Veterancy.rank(guard) >= 4) n = Math.max(0, n - 1); // its own example doesn't count
        double bonus = 0, step = 5.0D;
        for (int i = 0; i < n && bonus < 10.0D; i++) {
            bonus += step;
            step *= 0.5D;
        }
        return Math.min(10.0D, bonus);
    }

    private static void transition(ServerLevel level, Guard g, Mo m, long now, @Nullable Guard captain, double casualties,
                                   boolean rallyPoint, boolean resting) {
        long min = 20L * RallyConfig.i(RallyConfig.MORALE_MIN_STATE_SECONDS, 5);
        boolean canMove = now - m.since >= min || m.value < 5.0D;
        MoraleState s = m.state;
        MoraleState next = s;
        switch (s) {
            case FIRME -> {
                if (m.value < thr(0)) next = MoraleState.INQUIETO;
            }
            case INQUIETO -> {
                if (m.value < thr(1)) next = MoraleState.VACILANTE;
                else if (m.value >= thr(3)) next = MoraleState.FIRME;
            }
            case VACILANTE -> {
                if (m.value < thr(2)) next = MoraleState.ROTO;
                else if (m.value >= thr(4)) next = MoraleState.INQUIETO;
            }
            case ROTO -> {
                boolean noLeader = captain == null && generalGone(level, g);
                if (noLeader && casualties > 0.5D) next = MoraleState.DESTROZADO;
                else if (rallyPoint && m.value >= thr(6)) next = MoraleState.VACILANTE; // RALLIED
            }
            case DESTROZADO -> {
                if (resting) {
                    if (m.restSince < 0) m.restSince = now;
                    if (now - m.restSince >= 20L * RallyConfig.i(RallyConfig.MORALE_SHATTERED_REST, 120)) {
                        m.value = Math.max(m.value, 60.0D);
                        next = MoraleState.FIRME;
                    }
                } else {
                    m.restSince = -1;
                }
            }
        }
        if (next != s && (canMove || next == MoraleState.DESTROZADO)) {
            String why = next == MoraleState.VACILANTE && s == MoraleState.ROTO ? "gui.rallyguard.morale.cause.rallied" : m.cause;
            setState(g, m, next, why);
        }
    }

    private static void setState(Guard g, Mo m, MoraleState next, String cause) {
        MoraleState before = m.state;
        m.state = next;
        m.since = g.level().getGameTime();
        if (!cause.isEmpty()) m.cause = cause;
        if (next.broken() && m.firstBroken < 0) m.firstBroken = m.since;
        DecisionReason reason = switch (next) {
            case FIRME -> DecisionReason.NONE;
            case INQUIETO -> DecisionReason.MORALE_SHAKEN;
            case VACILANTE -> before == MoraleState.ROTO ? DecisionReason.RALLIED : DecisionReason.MORALE_WAVERING;
            case ROTO, DESTROZADO -> DecisionReason.ROUTED_FLEE;
        };
        if (reason != DecisionReason.NONE) DecisionTrace.record(g, DecisionLadder.Rung.NONE, reason);
        SimProbe.count(g, "morale_" + next.name());
        if (before == MoraleState.ROTO && next == MoraleState.VACILANTE) {
            SimProbe.count(g, "rallies");
            if (g.level() instanceof ServerLevel sl && net.hfstack.rallyguard.config.RallyConfig.on(net.hfstack.rallyguard.config.RallyConfig.MORALE_SHOW_ICONS)) {
                sl.sendParticles(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER, g.getX(), g.getY() + g.getBbHeight() + 0.4D, g.getZ(),
                        8, 0.3D, 0.2D, 0.3D, 0.0D);
            }
        }
    }

    /** Once a second, a small sign above a frightened guard (nothing for FIRME / INQUIETO). Server particles: no client code. */
    private static void showState(ServerLevel level, Guard g, MoraleState state) {
        if (state == MoraleState.FIRME || state == MoraleState.INQUIETO) return;
        if (!net.hfstack.rallyguard.config.RallyConfig.on(net.hfstack.rallyguard.config.RallyConfig.MORALE_SHOW_ICONS)) return;
        double y = g.getY() + g.getBbHeight() + 0.35D;
        if (state == MoraleState.VACILANTE) {
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.FALLING_WATER, g.getX(), y, g.getZ(), 3, 0.2D, 0.05D, 0.2D, 0.0D);
        } else { // ROTO / DESTROZADO
            level.sendParticles(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE, g.getX(), y, g.getZ(),
                    state == MoraleState.DESTROZADO ? 4 : 2, 0.15D, 0.05D, 0.15D, 0.01D);
        }
    }

    /** True if the guard's force has no living general (or it is in no force). Only asked for broken guards. */
    private static boolean generalGone(ServerLevel level, Guard g) {
        net.hfstack.rallyguard.contract.ForceRegistry.Force f = net.hfstack.rallyguard.force.Forces.forceOf(level, g);
        return f == null || net.hfstack.rallyguard.force.Forces.generalOf(level, f) == null;
    }

    /** The guard's captain (its pushed directive's), alive and loaded; null if none. */
    @Nullable
    public static Guard captainOf(ServerLevel level, Guard guard) {
        SquadDirectives.Directive d = SquadDirectives.of(guard.getUUID());
        if (d == null || d.captain() == null) return null;
        Entity e = level.getEntity(d.captain());
        return e instanceof Guard c && c.isAlive() ? c : null;
    }

    private static void rebuildIndex(ServerLevel level, long now) {
        if (now - indexTick < 20 || indexTick == now) return;
        indexTick = now;
        BROKEN.clear();
        EXAMPLES.clear();
        for (Guard g : GuardStanceHandler.loaded(level.dimension())) {
            UUID owner = GuardOwnership.getOwner(g);
            if (owner == null || !g.isAlive()) continue;
            Mo m = MORALE.get(g.getUUID());
            if (m != null && m.state.broken()) BROKEN.computeIfAbsent(owner, k -> new ArrayList<>()).add(g.position());
            else if (Veterancy.rank(g) >= 4 && (m == null || m.state == MoraleState.FIRME)) {
                EXAMPLES.computeIfAbsent(owner, k -> new ArrayList<>()).add(g.position());
            }
        }
    }

    private static int countNear(@Nullable List<Vec3> points, Vec3 p, double r, Guard self) {
        if (points == null) return 0;
        int n = 0;
        for (Vec3 v : points) {
            double dsq = v.distanceToSqr(p);
            if (dsq <= r * r && dsq > 1.0E-6) n++;
        }
        return n;
    }

    /**
     * The stance the gate applies under this morale (strength > 0): wavering = only answers whoever hits it
     * (starts no attack); broken = PASSIVE (stops fighting unless cornered - PASSIVE still answers a hit).
     */
    public static net.hfstack.rallyguard.contract.Stance limitStance(UUID guardId, net.hfstack.rallyguard.contract.Stance stance) {
        if (strength() <= 0) return stance;
        Mo m = MORALE.get(guardId);
        if (m == null) return stance;
        if (m.state.broken()) return net.hfstack.rallyguard.contract.Stance.PASSIVE;
        if (m.state == MoraleState.VACILANTE && (stance == net.hfstack.rallyguard.contract.Stance.AGGRESSIVE
                || stance == net.hfstack.rallyguard.contract.Stance.RUTHLESS)) {
            return net.hfstack.rallyguard.contract.Stance.DEFENSIVE;
        }
        return stance;
    }

    /** Seconds since the guard first broke this run (-1 = never) - simulation metric. */
    public static long firstBrokenTick(Guard g) {
        Mo m = MORALE.get(g.getUUID());
        return m == null ? -1 : m.firstBroken;
    }

    public static void forget(UUID guardId) {
        MORALE.remove(guardId);
    }

    public static void clearAll() {
        MORALE.clear();
        BROKEN.clear();
        EXAMPLES.clear();
        Cohesion.clearAll();
    }
}
