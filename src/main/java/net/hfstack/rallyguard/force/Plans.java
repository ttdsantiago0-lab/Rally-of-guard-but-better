package net.hfstack.rallyguard.force;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * E3A A4 - Battle Planner plans: parsing/validation and execution (the "plan" level, which
 * only compiles directives for the force and unit levels below it).
 * <p>
 * A plan: {@code {name, force, doctrine, retreatAt, roles {unit: role}, phases [{objective,
 * x, z, x2, z2, radius, yaw, trigger, value}], links [{type, from, to, trigger, value, x, z}]}}.
 * Phases run in order; each phase's TRIGGER (time in s, force casualties ≥ value, enemy seen)
 * moves to the next. Objectives: HOLD (hold the line), ATTACK_ZONE (advance by turns there),
 * ATTACK_GROUP (attack the group around a point), ESCORT / RALLY (follow the commander - the
 * old scroll's "rally" is this template), PATROL (A ⇄ B every 30 s), RETREAT (orderly).
 * <p>
 * Links between groups ({@code docs/BATTLE_PLANNER.md} §9), each written in CAPABILITIES:
 * RELIEF (Y takes X's post and role when X falls below the value or is gone), COVER (Y covers
 * X's movement or retreat with fire), REINFORCE (Y joins when X has been in contact for value
 * s), RENDEZVOUS (Y goes to a point), SCREEN (Y protects X's front), ESCORT (Y follows X),
 * HOLD_UNTIL_RELIEVED (X keeps its post - its fall-back rules are suppressed - until Y
 * arrives), STAGGERED_RETREAT (X and Y fall back in turns, one covering the other). A link whose
 * destination lacks the needed capability is reported ("faltan capacidades: X") in the planner
 * and the log, never executed silently. One active link per destination unit; cooldown 10 s.
 */
public final class Plans {
    private Plans() {
    }

    public enum Objective { HOLD, ATTACK_ZONE, ATTACK_GROUP, ESCORT, PATROL, RETREAT, RALLY }

    public enum Trigger { NONE, TIME, CASUALTIES, ENEMY_SEEN, CONTACT }

    public enum LinkType { RELIEF, COVER, REINFORCE, RENDEZVOUS, SCREEN, ESCORT, HOLD_UNTIL_RELIEVED, STAGGERED_RETREAT }

    public record Phase(Objective objective, double x, double z, double x2, double z2, double radius, float yaw,
                        Trigger trigger, double value) {
    }

    public record Link(LinkType type, String from, String to, Trigger trigger, double value, double x, double z) {
    }

    public record Plan(String name, String force, Doctrine doctrine, double retreatAt, Map<String, String> roles,
                       List<Phase> phases, List<Link> links) {
    }

    /** What each link needs from its DESTINATION group (any one of them). */
    public static EnumSet<Capability> needs(LinkType t) {
        return switch (t) {
            case COVER -> EnumSet.of(Capability.RANGED_SUPPORT, Capability.RETREAT_COVER);
            case SCREEN -> EnumSet.of(Capability.SCREEN, Capability.MELEE_LINE);
            case ESCORT -> EnumSet.of(Capability.ESCORT);
            case REINFORCE -> EnumSet.of(Capability.MELEE_LINE, Capability.RANGED_SUPPORT, Capability.MOUNTED_CHARGE);
            case RELIEF, HOLD_UNTIL_RELIEVED, RENDEZVOUS, STAGGERED_RETREAT -> EnumSet.of(Capability.HOLD_POSITION);
        };
    }

    private static final class Active {
        final Plan plan;
        int phase = -1;
        long phaseStart;
        final Map<String, Long> linkFired = new HashMap<>();
        /** E3B R4 (B5): held unit path -> tick the hold expires (never "forever"). */
        final Map<String, Long> holdUntilRelieved = new HashMap<>();
        int staggerTurn;
        long staggerAt;
        /** E3B R4: tick the plan started (ENEMY_SEEN ignores older sightings); re-enter the phase after a retreat. */
        long start = -1;
        boolean reenter;

        Active(Plan plan) {
            this.plan = plan;
        }
    }

    private static final Map<String, Active> ACTIVE = new HashMap<>();
    /** E3B R9: last warning list logged per force. */
    private static final Map<String, String> LOGGED = new HashMap<>();

    // ==== Parsing ====

    /** Parses and validates a plan; throws IllegalArgumentException with a readable reason. */
    public static Plan parse(String json) {
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();
        String name = str(o, "name", "plan");
        if (name.isBlank() || name.length() > 40) throw new IllegalArgumentException("name");
        String force = str(o, "force", "");
        Doctrine doctrine = Doctrine.parse(str(o, "doctrine", "STANDARD"));
        double retreatAt = Math.max(0, Math.min(1, num(o, "retreatAt", 0)));
        if (force.length() > 65) throw new IllegalArgumentException("force");
        Map<String, String> roles = new HashMap<>();
        if (o.has("roles")) {
            for (Map.Entry<String, JsonElement> e : o.getAsJsonObject("roles").entrySet()) {
                if (roles.size() >= MAX_ROLES) break; // E3B R6 (B8): ≤ 48 roles, keys ≤ 64
                if (e.getKey().length() > 64) continue;
                String role = e.getValue().getAsString().toUpperCase(Locale.ROOT);
                if (Roles.isRole(role)) roles.put(e.getKey(), role);
            }
        }
        List<Phase> phases = new ArrayList<>();
        if (o.has("phases")) {
            for (JsonElement e : o.getAsJsonArray("phases")) {
                if (phases.size() >= 12) break;
                JsonObject p = e.getAsJsonObject();
                phases.add(new Phase(Objective.valueOf(str(p, "objective", "HOLD").toUpperCase(Locale.ROOT)),
                        num(p, "x", 0), num(p, "z", 0), num(p, "x2", 0), num(p, "z2", 0),
                        Math.max(4, Math.min(64, num(p, "radius", 16))), (float) num(p, "yaw", 0),
                        Trigger.valueOf(str(p, "trigger", "NONE").toUpperCase(Locale.ROOT)), num(p, "value", 0)));
            }
        }
        List<Link> links = new ArrayList<>();
        if (o.has("links")) {
            for (JsonElement e : o.getAsJsonArray("links")) {
                if (links.size() >= 16) break;
                JsonObject l = e.getAsJsonObject();
                links.add(new Link(LinkType.valueOf(str(l, "type", "COVER").toUpperCase(Locale.ROOT)), str(l, "from", ""),
                        str(l, "to", ""), Trigger.valueOf(str(l, "trigger", "NONE").toUpperCase(Locale.ROOT)),
                        num(l, "value", 0), num(l, "x", 0), num(l, "z", 0)));
            }
        }
        return new Plan(name, force, doctrine, retreatAt, roles, phases, links);
    }

    /** E3B R6: most declared roles a plan (and a force) may hold. */
    public static final int MAX_ROLES = 48;

    private static String str(JsonObject o, String k, String d) {
        String v = o.has(k) && !o.get(k).isJsonNull() ? o.get(k).getAsString() : d;
        if (v.length() > 64) throw new IllegalArgumentException(k); // E3B R6: keys and names ≤ 64
        return v;
    }

    private static double num(JsonObject o, String k, double d) {
        double v = o.has(k) && !o.get(k).isJsonNull() ? o.get(k).getAsDouble() : d;
        if (!Double.isFinite(v)) throw new IllegalArgumentException(k); // E3B R6: no NaN / infinity
        return v;
    }

    /** E3B R6 (B8): every phase and link point must be finite and within 512 blocks of the player (no far chunks). */
    public static void checkNear(Plan plan, net.minecraft.server.level.ServerPlayer player) {
        for (Phase p : plan.phases()) {
            if ((p.x() != 0 || p.z() != 0) && !net.hfstack.rallyguard.network.NetSafety.near(player, p.x(), p.z())) {
                throw new IllegalArgumentException("far");
            }
            if ((p.x2() != 0 || p.z2() != 0) && !net.hfstack.rallyguard.network.NetSafety.near(player, p.x2(), p.z2())) {
                throw new IllegalArgumentException("far");
            }
        }
        for (Link l : plan.links()) {
            if ((l.x() != 0 || l.z() != 0) && !net.hfstack.rallyguard.network.NetSafety.near(player, l.x(), l.z())) {
                throw new IllegalArgumentException("far");
            }
        }
    }

    // ==== Running ====

    /** Starts a plan on its force: doctrine and declared roles are written to the force. */
    public static void execute(ServerLevel level, ForceRegistry.Force force, Plan plan) {
        force.doctrine = plan.doctrine().name();
        plan.roles().forEach((unit, role) -> force.units.put(unitName(force, unit), role));
        ForceRegistry.get(level).changed();
        ForceBrain.forget(force.owner, force.squad); // E3B R4 (B4): start clean (rule states, retreats, old directives)
        Active fresh = new Active(plan);
        fresh.start = level.getGameTime();
        ACTIVE.put(ForceBrain.forceKey(force), fresh);
        Leadership.invalidate();
    }

    public static void cancel(ForceRegistry.Force force) {
        ACTIVE.remove(ForceBrain.forceKey(force));
        Operations.stop(force);
        ForceBrain.forget(force.owner, force.squad); // E3B R4 (B4): a new plan never inherits "active" rule states
    }

    /** E3B R4 (B3): after a force retreat ends, the plan's current phase is entered again. */
    static void reenter(ForceRegistry.Force force) {
        Active a = ACTIVE.get(ForceBrain.forceKey(force));
        if (a != null && a.phase >= 0) a.reenter = true;
    }

    static void forget(String forceKey) {
        ACTIVE.remove(forceKey);
        LOGGED.remove(forceKey);
    }

    @Nullable
    public static Plan active(ForceRegistry.Force force) {
        Active a = ACTIVE.get(ForceBrain.forceKey(force));
        return a == null ? null : a.plan;
    }

    public static int activePhase(ForceRegistry.Force force) {
        Active a = ACTIVE.get(ForceBrain.forceKey(force));
        return a == null ? -1 : a.phase;
    }

    /** The plan's own rules for the force brain (its "retreat at X % casualties" contingency). */
    static List<ForceRules.Rule> rulesFor(ServerLevel level, ForceRegistry.Force force) {
        Active a = ACTIVE.get(ForceBrain.forceKey(force));
        if (a == null || a.plan.retreatAt() <= 0) return List.of();
        double at = a.plan.retreatAt();
        return List.of(new ForceRules.Rule("plan-retreat", ForceRules.Scope.FORCE, ForceRules.Metric.CASUALTIES, at,
                Math.max(0, at - 0.2D), true, ForceRules.Action.RETREAT_HQ, 120, 200, 600, ""));
    }

    /** True while a HOLD_UNTIL_RELIEVED link keeps this unit on its post (its fall-back rules wait). */
    static boolean heldUntilRelieved(ForceRegistry.Force force, String unitPath) {
        Active a = ACTIVE.get(ForceBrain.forceKey(force));
        return a != null && a.holdUntilRelieved.containsKey(unitPath.toLowerCase(Locale.ROOT));
    }

    /** E3B R4 (B5): how long a HOLD_UNTIL_RELIEVED keeps a unit if no relief arrives (link value s, default 120). */
    static final long DEFAULT_HOLD_TICKS = 20L * 120L;

    /** {@code frozen}: a force retreat is in force - the plan waits (no phase change, no links). */
    static void step(ServerLevel level, ForceRegistry.Force force, List<Forces.UnitView> units, Map<String, String> roles,
                     Map<String, SituationReports.Report> reports, long now, boolean frozen) {
        Active a = ACTIVE.get(ForceBrain.forceKey(force));
        if (a == null) return;
        if (a.start < 0) a.start = now;
        a.holdUntilRelieved.values().removeIf(until -> now > until); // E3B R4 (B5): never forever
        if (frozen) return;
        if (a.reenter) {
            a.reenter = false;
            if (a.phase < a.plan.phases().size()) enter(level, force, a, a.phase, units, reports, now);
        }
        // Phases
        if (a.phase < 0) enter(level, force, a, 0, units, reports, now);
        else if (a.phase < a.plan.phases().size() && triggered(a.plan.phases().get(a.phase).trigger(),
                a.plan.phases().get(a.phase).value(), a.phaseStart, force, reports, now, a.start)) {
            enter(level, force, a, a.phase + 1, units, reports, now);
        }
        if (a.phase >= 0 && a.phase < a.plan.phases().size()
                && a.plan.phases().get(a.phase).objective() == Objective.PATROL && (now - a.phaseStart) % 600 < 20) {
            patrol(level, force, a, units, now);
        }
        // Links
        Map<String, Forces.UnitView> byPath = new HashMap<>();
        for (Forces.UnitView u : units) byPath.put(u.path().toLowerCase(Locale.ROOT), u);
        Set<String> busy = new HashSet<>();
        for (Link link : a.plan.links()) {
            Forces.UnitView from = byPath.get(link.from().toLowerCase(Locale.ROOT));
            Forces.UnitView to = byPath.get(link.to().toLowerCase(Locale.ROOT));
            if (to == null && link.type() == LinkType.HOLD_UNTIL_RELIEVED) {
                a.holdUntilRelieved.remove(link.from().toLowerCase(Locale.ROOT)); // E3B R4 (B5): the relief is gone
            }
            if (to == null || busy.contains(to.path())) continue;
            if (missing(link, to) != null) continue; // reported by the planner (warnings), never executed
            SituationReports.Report fr = from == null ? null : reports.get(from.path());
            String id = link.type() + ">" + link.to();
            Long fired = a.linkFired.get(id);
            if (fired != null && now - fired < 200) continue;
            if (!linkTriggered(link, from, fr, a, now)) continue;
            if (fire(level, force, a, link, from, fr, to, reports.get(to.path()), now)) {
                a.linkFired.put(id, now);
                busy.add(to.path());
            }
        }
    }

    /**
     * E3B R4 (B6): NONE = move on after {@code value} s (60 by default) instead of blocking the plan;
     * CONTACT needs real contact (value ≤ 0 = any contact); ENEMY_SEEN only counts sightings made after
     * the plan started.
     */
    private static boolean triggered(Trigger t, double value, long since, ForceRegistry.Force force,
                                     Map<String, SituationReports.Report> reports, long now, long planStart) {
        return switch (t) {
            case NONE -> (now - since) / 20.0D >= (value > 0 ? value : 60.0D);
            case TIME -> (now - since) / 20.0D >= value;
            case CASUALTIES -> forceCasualties(reports) >= value;
            case ENEMY_SEEN -> SituationReports.knownEnemies(ForceBrain.forceKey(force), now).stream()
                    .anyMatch(k -> k.tick() >= planStart);
            case CONTACT -> reports.values().stream().anyMatch(r -> r.contactSince() >= 0 && r.contactSeconds(now) >= value);
        };
    }

    private static double forceCasualties(Map<String, SituationReports.Report> reports) {
        int alive = 0, total = 0;
        for (SituationReports.Report r : reports.values()) {
            alive += r.alive();
            total += r.total();
        }
        return total == 0 ? 0 : 1.0D - (double) alive / total;
    }

    private static void enter(ServerLevel level, ForceRegistry.Force force, Active a, int index, List<Forces.UnitView> units,
                              Map<String, SituationReports.Report> reports, long now) {
        a.phase = index;
        a.phaseStart = now;
        if (index >= a.plan.phases().size()) return; // plan finished: the last phase's state stays
        Phase p = a.plan.phases().get(index);
        Vec3 point = new Vec3(p.x(), centreY(reports), p.z());
        Vec3 centre = centre(reports);
        switch (p.objective()) {
            case HOLD -> Operations.start(level, force, Operations.Type.HOLD_LINE, point, p.yaw());
            case ATTACK_ZONE -> Operations.startTo(level, force, Operations.Type.ADVANCE_BY_TURNS, centre,
                    Operations.facing(centre, point), point); // E3B R4 (B6): stops and holds at the zone
            case RETREAT -> Operations.start(level, force, Operations.Type.ORDERLY_RETREAT, centre, Operations.facing(point, centre));
            case ATTACK_GROUP -> {
                Operations.stop(force);
                for (Forces.UnitView u : units) {
                    UnitOrders.issue(level, UnitOrders.unitKey(force.owner, u.path()), UnitOrders.Kind.ATTACK_AREA,
                            UnitOrders.Source.PLAN, point, p.yaw(), FormationShape.V, null, p.radius(), "plan attack group");
                }
            }
            case ESCORT, RALLY -> {
                Operations.stop(force);
                for (Forces.UnitView u : units) {
                    UnitOrders.issue(level, UnitOrders.unitKey(force.owner, u.path()), UnitOrders.Kind.FOLLOW,
                            UnitOrders.Source.PLAN, null, 0, FormationShape.CIRCLE, null, 0, "plan " + p.objective());
                }
            }
            case PATROL -> {
                Operations.stop(force);
                patrol(level, force, a, units, now);
            }
        }
    }

    private static void patrol(ServerLevel level, ForceRegistry.Force force, Active a, List<Forces.UnitView> units, long now) {
        Phase p = a.plan.phases().get(a.phase);
        boolean toB = ((now - a.phaseStart) / 600) % 2 == 1;
        Vec3 dest = toB ? new Vec3(p.x2(), 0, p.z2()) : new Vec3(p.x(), 0, p.z());
        for (Forces.UnitView u : units) {
            Vec3 at = UnitOrders.ground(level, new Vec3(dest.x, u.members().isEmpty() ? 64 : u.members().get(0).getY(), dest.z));
            UnitOrders.issue(level, UnitOrders.unitKey(force.owner, u.path()), UnitOrders.Kind.MOVE, UnitOrders.Source.PLAN,
                    at, p.yaw(), FormationShape.COLUMN, null, 0, "plan patrol");
        }
    }

    private static boolean linkTriggered(Link link, @Nullable Forces.UnitView from, @Nullable SituationReports.Report fr,
                                         Active a, long now) {
        return switch (link.trigger()) {
            case NONE -> true;
            case TIME -> (now - a.phaseStart) / 20.0D >= link.value();
            case CASUALTIES -> from == null || (fr != null && fr.casualties() >= link.value());
            case CONTACT -> fr != null && fr.contactSince() >= 0 && fr.contactSeconds(now) >= link.value();
            case ENEMY_SEEN -> fr != null && fr.enemiesSeen() > 0;
        };
    }

    /** Carries a triggered link out as unit directives (source PLAN). True if something was issued. */
    private static boolean fire(ServerLevel level, ForceRegistry.Force force, Active a, Link link, @Nullable Forces.UnitView from,
                                @Nullable SituationReports.Report fr, Forces.UnitView to, @Nullable SituationReports.Report tr,
                                long now) {
        String toKey = UnitOrders.unitKey(force.owner, to.path());
        Vec3 fromCentre = fr == null ? null : fr.centre();
        Vec3 enemy = fr != null && fr.enemyCentre() != null ? fr.enemyCentre() : tr == null ? null : tr.enemyCentre();
        float facing = Operations.facing(fromCentre, enemy);
        String why = "link " + link.type().name().toLowerCase(Locale.ROOT);
        switch (link.type()) {
            case RELIEF -> {
                if (fromCentre == null && from != null) return false;
                Vec3 post = fromCentre != null ? fromCentre : new Vec3(link.x(), tr == null || tr.centre() == null ? 64 : tr.centre().y, link.z());
                if (from != null) {
                    force.units.put(to.name(), force.units.getOrDefault(from.name(), ""));
                    ForceRegistry.get(level).changed(); // E3B R9 (B14): the new role is saved
                }
                UnitOrders.issue(level, toKey, UnitOrders.Kind.HOLD, UnitOrders.Source.PLAN, UnitOrders.ground(level, post),
                        facing, FormationShape.LINE, null, 0, why);
                a.holdUntilRelieved.remove(link.from().toLowerCase(Locale.ROOT));
            }
            case COVER -> {
                if (fromCentre == null) return false;
                UnitOrders.UnitOrder fromOrder = from == null ? null : UnitOrders.current(UnitOrders.unitKey(force.owner, from.path()));
                boolean moving = fromOrder != null && (fromOrder.kind() == UnitOrders.Kind.FALLBACK
                        || fromOrder.kind() == UnitOrders.Kind.RETREAT_HQ || fromOrder.kind() == UnitOrders.Kind.MOVE);
                if (!moving && link.trigger() == Trigger.NONE) return false;
                UnitOrders.issue(level, toKey, UnitOrders.Kind.HOLD, UnitOrders.Source.PLAN,
                        UnitOrders.ground(level, Operations.away(fromCentre, enemy, 6)), facing, FormationShape.LINE,
                        FireMode.VOLLEY, 0, why);
            }
            case REINFORCE -> {
                if (enemy == null) return false;
                UnitOrders.issue(level, toKey, UnitOrders.Kind.ATTACK_AREA, UnitOrders.Source.PLAN, enemy, facing,
                        FormationShape.V, null, 16, why);
            }
            case RENDEZVOUS -> UnitOrders.issue(level, toKey, UnitOrders.Kind.MOVE, UnitOrders.Source.PLAN,
                    UnitOrders.ground(level, new Vec3(link.x(), tr == null || tr.centre() == null ? 64 : tr.centre().y, link.z())),
                    0, FormationShape.COLUMN, null, 0, why);
            case SCREEN -> {
                if (fromCentre == null) return false;
                Vec3 front = enemy == null ? fromCentre : fromCentre.add(enemy.subtract(fromCentre).multiply(1, 0, 1).normalize().scale(5));
                UnitOrders.issue(level, toKey, UnitOrders.Kind.HOLD, UnitOrders.Source.PLAN, UnitOrders.ground(level, front),
                        facing, FormationShape.LINE, null, 0, why);
            }
            case ESCORT -> {
                if (fromCentre == null) return false;
                UnitOrders.issue(level, toKey, UnitOrders.Kind.MOVE, UnitOrders.Source.PLAN,
                        UnitOrders.ground(level, fromCentre.add(2, 0, 2)), facing, FormationShape.CIRCLE, null, 0, why);
            }
            case HOLD_UNTIL_RELIEVED -> {
                if (from == null || fromCentre == null) return false;
                a.holdUntilRelieved.putIfAbsent(link.from().toLowerCase(Locale.ROOT),
                        now + (link.value() > 0 ? (long) (link.value() * 20) : DEFAULT_HOLD_TICKS));
                if (tr != null && tr.centre() != null && tr.centre().distanceTo(fromCentre) < 8) {
                    a.holdUntilRelieved.remove(link.from().toLowerCase(Locale.ROOT)); // relieved: X may fall back now
                }
                UnitOrders.issue(level, toKey, UnitOrders.Kind.MOVE, UnitOrders.Source.PLAN, UnitOrders.ground(level, fromCentre),
                        facing, FormationShape.LINE, null, 0, why);
            }
            case STAGGERED_RETREAT -> {
                if (from == null || fromCentre == null) return false;
                if (now - a.staggerAt < 160) return false;
                a.staggerAt = now;
                a.staggerTurn++;
                boolean fromMoves = a.staggerTurn % 2 == 1;
                Forces.UnitView mover = fromMoves ? from : to;
                Forces.UnitView holder = fromMoves ? to : from;
                SituationReports.Report mr = fromMoves ? fr : tr;
                if (mr == null || mr.centre() == null) return false;
                UnitOrders.issue(level, UnitOrders.unitKey(force.owner, mover.path()), UnitOrders.Kind.FALLBACK,
                        UnitOrders.Source.PLAN, UnitOrders.ground(level, Operations.away(mr.centre(), enemy, 10)), facing,
                        FormationShape.LINE, FireMode.RETREATING, 0, why);
                UnitOrders.issue(level, UnitOrders.unitKey(force.owner, holder.path()), UnitOrders.Kind.FIRE_STYLE,
                        UnitOrders.Source.PLAN, null, 0, FormationShape.LINE, FireMode.STILL, 0, why);
            }
        }
        return true;
    }

    /** Null if the destination can carry the link out, else the capabilities it lacks. */
    @Nullable
    public static EnumSet<Capability> missing(Link link, Forces.UnitView to) {
        EnumSet<Capability> need = needs(link.type());
        for (Capability c : need) if (to.capabilities().contains(c)) return null;
        return need;
    }

    /** Planner warnings: every link/phase the force can't carry out, as "faltan capacidades" lines (also logged). */
    public static List<String> warnings(ServerLevel level, ForceRegistry.Force force, Plan plan) {
        List<String> out = new ArrayList<>();
        Map<String, Forces.UnitView> byPath = new HashMap<>();
        for (Forces.UnitView u : Forces.units(level, force)) byPath.put(u.path().toLowerCase(Locale.ROOT), u);
        for (Link l : plan.links()) {
            Forces.UnitView to = byPath.get(l.to().toLowerCase(Locale.ROOT));
            if (to == null) {
                out.add(l.type() + ": unknown unit " + l.to());
                continue;
            }
            EnumSet<Capability> miss = missing(l, to);
            if (miss != null) out.add(l.type() + " -> " + to.path() + ": faltan capacidades " + miss);
        }
        // E3B R9: logged once per change of the warning list (the planner asks for them on every refresh).
        String digest = plan.name() + "|" + String.join("|", out);
        if (!digest.equals(LOGGED.get(ForceBrain.forceKey(force)))) {
            LOGGED.put(ForceBrain.forceKey(force), digest);
            for (String w : out) LogUtils.getLogger().info("[rallyguard] plan '{}': {}", plan.name(), w);
        }
        return out;
    }

    private static double centreY(Map<String, SituationReports.Report> reports) {
        for (SituationReports.Report r : reports.values()) if (r.centre() != null) return r.centre().y;
        return 64;
    }

    private static Vec3 centre(Map<String, SituationReports.Report> reports) {
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

    /** "Squad/Unit" or "Unit" -> the declared unit name. */
    private static String unitName(ForceRegistry.Force force, String unit) {
        int slash = unit.indexOf('/');
        return slash >= 0 ? unit.substring(slash + 1) : unit.equalsIgnoreCase(force.squad) ? "" : unit;
    }

    /** Serializes a plan back to JSON (planner state). */
    public static JsonObject toJson(Plan p) {
        JsonObject o = new JsonObject();
        o.addProperty("name", p.name());
        o.addProperty("force", p.force());
        o.addProperty("doctrine", p.doctrine().name());
        o.addProperty("retreatAt", p.retreatAt());
        JsonObject roles = new JsonObject();
        p.roles().forEach(roles::addProperty);
        o.add("roles", roles);
        JsonArray phases = new JsonArray();
        for (Phase ph : p.phases()) {
            JsonObject j = new JsonObject();
            j.addProperty("objective", ph.objective().name());
            j.addProperty("x", ph.x());
            j.addProperty("z", ph.z());
            j.addProperty("x2", ph.x2());
            j.addProperty("z2", ph.z2());
            j.addProperty("radius", ph.radius());
            j.addProperty("yaw", ph.yaw());
            j.addProperty("trigger", ph.trigger().name());
            j.addProperty("value", ph.value());
            phases.add(j);
        }
        o.add("phases", phases);
        JsonArray links = new JsonArray();
        for (Link l : p.links()) {
            JsonObject j = new JsonObject();
            j.addProperty("type", l.type().name());
            j.addProperty("from", l.from());
            j.addProperty("to", l.to());
            j.addProperty("trigger", l.trigger().name());
            j.addProperty("value", l.value());
            j.addProperty("x", l.x());
            j.addProperty("z", l.z());
            links.add(j);
        }
        o.add("links", links);
        return o;
    }

    public static void clearAll() {
        ACTIVE.clear();
        LOGGED.clear();
    }
}
