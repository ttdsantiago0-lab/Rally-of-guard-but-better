package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.debug.Perf;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * E3A A2/A3/A4 - the force level, run at most once a second per force (forces are staggered
 * over the 20 ticks of a second, Perf system {@code forces}). For each force:
 * <ol>
 *     <li>its units publish their {@link SituationReports reports} (only on change);</li>
 *     <li>the general proposes roles ({@link Roles});</li>
 *     <li>{@link ForceRules} (doctrine defaults + the active plan's) are evaluated with
 *     hysteresis; per unit the highest-priority active rule wins and is compiled to a
 *     versioned {@link UnitOrders unit directive} - only when the winning action changes;</li>
 *     <li>the force's operation ({@link Operations}, A3) and the plan's phases and links
 *     ({@link Plans}, A4) issue their unit directives for units no rule overrides.</li>
 * </ol>
 * A higher level only compiles directives for the level below and never skips it
 * (ARQUITECTURA_VETERANIA §10): plan -> force -> unit directive -> guard's own ladder.
 */
public final class ForceBrain {
    private ForceBrain() {
    }

    /** unit key -> the rule action last compiled for it ("" = none). */
    private static final Map<String, String> LAST_ACTION = new HashMap<>();

    /**
     * E3B R4 (B3): a force retreat in progress - the operation it interrupted (restored when the
     * retreat ends), when it started, and whether the player cancelled it (an explicit order to the
     * force's guards, or another operation started over it).
     */
    private static final class Retreat {
        final Object saved;
        final long start;
        boolean cancelled;

        Retreat(Object saved, long start) {
            this.saved = saved;
            this.start = start;
        }
    }

    private static final Map<String, Retreat> RETREATS = new HashMap<>();

    public static String forceKey(ForceRegistry.Force f) {
        return f.owner + "#" + f.squad.toLowerCase(java.util.Locale.ROOT);
    }

    public static void tick(ServerLevel level) {
        ForceRegistry registry = ForceRegistry.get(level);
        List<ForceRegistry.Force> forces = registry.all();
        if (forces.isEmpty()) return;
        // E3B R8 (B12): a dimension with no loaded owned guards has no force to run (each force used to be
        // stepped once per dimension per second, two of them on nothing).
        if (net.hfstack.rallyguard.event.GuardStanceHandler.loaded(level.dimension()).isEmpty()) return;
        long now = level.getGameTime();
        long t0 = Perf.t0();
        for (int i = 0; i < forces.size(); i++) {
            if ((i + now) % 20 != 0) continue;
            try {
                step(level, forces.get(i), now);
            } catch (RuntimeException e) {
                com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] force {} skipped after an error", forces.get(i).squad, e);
            }
        }
        Perf.add(Perf.Sys.FORCES, t0);
    }

    private static void step(ServerLevel level, ForceRegistry.Force force, long now) {
        List<Forces.UnitView> units = new ArrayList<>();
        for (Forces.UnitView u : Forces.units(level, force)) if (!u.members().isEmpty()) units.add(u);
        if (units.isEmpty()) return;
        String fkey = forceKey(force);
        Map<String, String> roles = Roles.assign(level, force, units);
        Map<String, SituationReports.Report> reports = new HashMap<>();
        int alive = 0, total = 0;
        for (Forces.UnitView u : units) {
            String ukey = UnitOrders.unitKey(force.owner, u.path());
            SituationReports.Report r = SituationReports.update(fkey, ukey, u.members(), now);
            reports.put(u.path(), r);
            alive += r.alive();
            total += r.total();
        }
        double forceCasualties = total == 0 ? 0 : 1.0D - (double) alive / total;
        Doctrine doctrine = Doctrine.parse(force.doctrine);
        List<ForceRules.Rule> rules = new ArrayList<>(ForceRules.defaults(doctrine));
        rules.addAll(Plans.rulesFor(level, force));

        Map<String, ForceRules.Rule> winners = new HashMap<>();
        boolean retreatWanted = false;
        for (ForceRules.Rule rule : rules) {
            if (rule.scope() == ForceRules.Scope.FORCE) {
                ForceRules.State st = ForceRules.state(fkey, rule);
                ForceRules.evaluate(rule, st, forceCasualties, now);
                if (!st.active()) continue;
                if (rule.action() == ForceRules.Action.RETREAT_HQ) {
                    retreatWanted = true; // A3: a force retreat is ORDERLY - support first, the line covering it
                    continue;
                }
                for (Forces.UnitView u : units) winners.merge(u.path(), rule, ForceBrain::better);
                continue;
            }
            for (Forces.UnitView u : units) {
                String role = roles.getOrDefault(u.path(), "");
                if (!rule.roleFilter().isEmpty() && !rule.roleFilter().equals(role)) continue;
                SituationReports.Report r = reports.get(u.path());
                ForceRules.State st = ForceRules.state(UnitOrders.unitKey(force.owner, u.path()), rule);
                ForceRules.evaluate(rule, st, metric(rule.metric(), r, now), now);
                if (st.active()) winners.merge(u.path(), rule, ForceBrain::better);
            }
        }

        List<Forces.UnitView> free = new ArrayList<>();
        for (Forces.UnitView u : units) {
            String ukey = UnitOrders.unitKey(force.owner, u.path());
            ForceRules.Rule win = winners.get(u.path());
            if (win != null && (win.action() == ForceRules.Action.FALLBACK || win.action() == ForceRules.Action.RETREAT_HQ)
                    && Plans.heldUntilRelieved(force, u.path())) {
                win = null; // A4 link "hold until relieved": this unit keeps its post until the relief arrives
            }
            String action = win == null ? "" : win.action().name();
            String last = LAST_ACTION.getOrDefault(ukey, "");
            if (!action.equals(last)) {
                LAST_ACTION.put(ukey, action);
                if (win != null) compile(level, force, u, units, roles, reports.get(u.path()), win, ukey);
                else Operations.resume(level, force, u, ukey); // rule over: back to the operation
            }
            if (win == null) free.add(u);
        }
        boolean retreating = forceRetreat(level, force, fkey, retreatWanted, units, reports, now);
        Operations.step(level, force, free, roles, reports, now);
        Plans.step(level, force, units, roles, reports, now, retreating);
    }

    /**
     * E3B R4 (B3): starts the orderly retreat ONCE (remembering what it interrupts), never re-imposes
     * it over the player (an explicit order to the force's guards or another operation cancels it),
     * and when the rule ends puts back the interrupted operation and the plan's phase. True while a
     * retreat is running (the plan waits).
     */
    private static boolean forceRetreat(ServerLevel level, ForceRegistry.Force force, String fkey, boolean wanted,
                                        List<Forces.UnitView> units, Map<String, SituationReports.Report> reports, long now) {
        Retreat r = RETREATS.get(fkey);
        if (!wanted) {
            if (r != null) {
                RETREATS.remove(fkey);
                if (!r.cancelled) {
                    Operations.restore(level, force, r.saved);
                    Plans.reenter(force);
                }
            }
            return false;
        }
        if (r == null) {
            Object saved = Operations.current(force) == Operations.Type.ORDERLY_RETREAT ? null : Operations.snapshot(force);
            RETREATS.put(fkey, new Retreat(saved, now));
            Vec3 c = centreOf(reports);
            Operations.start(level, force, Operations.Type.ORDERLY_RETREAT, c, Operations.facing(c, enemyOf(reports)));
            return true;
        }
        if (r.cancelled) return false;
        List<tallestegg.guardvillagers.common.entities.Guard> all = new ArrayList<>();
        for (Forces.UnitView u : units) all.addAll(u.members());
        if (UnitOrders.lastPlayerOrder(all) > r.start || Operations.current(force) != Operations.Type.ORDERLY_RETREAT) {
            r.cancelled = true; // the player decided otherwise: no retreat re-imposed until the rule ends
            return false;
        }
        return true;
    }

    /**
     * E3B R4 (B4): forgets everything the force level keeps in memory for this squad - rule states,
     * last compiled actions, unit directives, operation, plan, retreat, reports. Called on dissolve,
     * rename, disband and plan cancel/execute.
     */
    public static void forget(java.util.UUID owner, String squad) {
        String prefix = UnitOrders.unitKey(owner, squad);
        LAST_ACTION.keySet().removeIf(k -> k.equals(prefix) || k.startsWith(prefix + "/"));
        RETREATS.remove(prefix);
        ForceRules.forgetPrefix(prefix);
        UnitOrders.forgetPrefix(prefix);
        Operations.forget(prefix);
        Plans.forget(prefix);
        SituationReports.forgetForce(prefix, prefix);
    }

    private static Vec3 centreOf(Map<String, SituationReports.Report> reports) {
        double x = 0, y = 0, z = 0;
        int n = 0;
        for (SituationReports.Report r : reports.values()) {
            if (r.centre() == null) continue;
            x += r.centre().x;
            y += r.centre().y;
            z += r.centre().z;
            n++;
        }
        return n == 0 ? Vec3.ZERO : new Vec3(x / n, y / n, z / n);
    }

    @Nullable
    private static Vec3 enemyOf(Map<String, SituationReports.Report> reports) {
        for (SituationReports.Report r : reports.values()) if (r.enemyCentre() != null) return r.enemyCentre();
        return null;
    }

    private static ForceRules.Rule better(ForceRules.Rule a, ForceRules.Rule b) {
        return b.priority() > a.priority() ? b : a; // ties: the earlier (definition order) stays
    }

    static double metric(ForceRules.Metric m, @Nullable SituationReports.Report r, long now) {
        if (r == null) return 0;
        return switch (m) {
            case CASUALTIES -> r.casualties();
            case HEALTH -> r.health();
            case AMMO -> r.ammo();
            case CONTACT_SECONDS -> r.contactSeconds(now);
            case ENEMIES_SEEN -> r.enemiesSeen();
        };
    }

    /** A rule's action -> one versioned unit directive (source FORCE). */
    private static void compile(ServerLevel level, ForceRegistry.Force force, Forces.UnitView unit, List<Forces.UnitView> all,
                                Map<String, String> roles, @Nullable SituationReports.Report report, ForceRules.Rule rule,
                                String ukey) {
        Vec3 centre = report == null ? null : report.centre();
        Vec3 enemy = report == null ? null : report.enemyCentre();
        float facing = Operations.facing(centre, enemy);
        switch (rule.action()) {
            case RETREAT_HQ, RESUPPLY -> UnitOrders.issue(level, ukey, UnitOrders.Kind.RETREAT_HQ, UnitOrders.Source.FORCE,
                    null, 0, FormationShape.LINE, null, 0, rule.id());
            case FALLBACK -> {
                if (centre == null) return;
                Vec3 back = Operations.away(centre, enemy, 10.0D);
                UnitOrders.issue(level, ukey, UnitOrders.Kind.FALLBACK, UnitOrders.Source.FORCE, UnitOrders.ground(level, back),
                        facing, FormationShape.LINE, FireMode.RETREATING, 0, rule.id());
            }
            case HOLD -> {
                if (centre == null) return;
                UnitOrders.issue(level, ukey, UnitOrders.Kind.HOLD, UnitOrders.Source.FORCE, UnitOrders.ground(level, centre),
                        facing, FormationShape.LINE, null, 0, rule.id());
            }
            case VOLLEY -> UnitOrders.issue(level, ukey, UnitOrders.Kind.FIRE_STYLE, UnitOrders.Source.FORCE, null, 0,
                    FormationShape.LINE, FireMode.VOLLEY, 0, rule.id());
            case RELEASE_RESERVE -> {
                // Relief: the reserve takes the failing unit's place and role.
                if (centre == null) return;
                for (Forces.UnitView r : all) {
                    if (!"RESERVE".equals(roles.get(r.path()))) continue;
                    UnitOrders.issue(level, UnitOrders.unitKey(force.owner, r.path()), UnitOrders.Kind.HOLD,
                            UnitOrders.Source.FORCE, UnitOrders.ground(level, centre), facing, FormationShape.LINE, null, 0, rule.id());
                }
            }
        }
    }

    public static void clearAll() {
        LAST_ACTION.clear();
        RETREATS.clear();
        ForceRules.clearAll();
        SituationReports.clearAll();
        UnitOrders.clearAll();
        Operations.clearAll();
        Plans.clearAll();
    }
}
