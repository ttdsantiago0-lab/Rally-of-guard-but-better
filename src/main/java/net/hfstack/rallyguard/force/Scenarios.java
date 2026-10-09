package net.hfstack.rallyguard.force;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.command.InstructionInterpreter;
import net.hfstack.rallyguard.command.InstructionRunner;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.debug.SimProbe;
import net.hfstack.rallyguard.formation.FormationShape;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E3B P2 ({@code docs/BATTLE_PLANNER_V2.md} Pilar 2) - scenarios: situations the game DETECTS
 * and to which a planned force RESPONDS. Each {@link Entry} is data (kind, on/off, priority,
 * enter / exit thresholds, cooldown, response per role) saved with the plan; each {@link Kind}
 * has one small detector returning a number. Once a second per force (inside the force brain's
 * plan step):
 * <ol>
 *     <li>every active entry's detector value goes through the same hysteresis as the force rules
 *     ({@link ForceRules#evaluate}: enter / exit thresholds, cooldown, minimum duration);</li>
 *     <li>the highest-priority active scenario wins; when the winner changes its response is
 *     issued ONCE per unit (versioned unit directives, source FORCE - the player's own orders
 *     still win for 60 s);</li>
 *     <li>while a scenario runs, the plan waits (no phase change, no links) and the running
 *     operation is put aside; when it ends both are put back, as after a force retreat.</li>
 * </ol>
 * Adding a scenario = one {@link Kind} constant + its detector case + its default entry.
 */
public final class Scenarios {
    private Scenarios() {
    }

    /** The library. {@code minSeconds}: how long a response lasts at least once started. */
    public enum Kind {
        EMBOSCADA_ENEMIGA(20), ASALTO_A_ESTRUCTURA(10), DEFENSA_DE_ESTRUCTURA(15), OLEADA(20), AMENAZA_AEREA(10),
        JEFE(15), ENEMIGO_EN_FUGA(5), BATALLA_PERDIDA(30), INFERIORIDAD(20), CAPITAN_CAIDO(10);

        public final int minSeconds;

        Kind(int minSeconds) {
            this.minSeconds = minSeconds;
        }

        @Nullable
        public static Kind parse(String s) {
            try {
                return valueOf(s.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException | NullPointerException e) {
                return null;
            }
        }
    }

    /** What a role does in a response. INSTR_X starts instruction X ({@link InstructionInterpreter.Instruction}). */
    public static final List<String> ACTIONS = List.of("NONE", "HOLD", "FALLBACK", "RETREAT_HQ", "ATTACK", "SCREEN_HQ",
            "VOLLEY", "COVER", "PURSUE", "REFORM", "DELAY", "INSTR_HOSTIGAR", "INSTR_MANTENER_LINEA", "INSTR_AVANZAR",
            "INSTR_RETIRARSE", "INSTR_FLANQUEAR");

    /**
     * One scenario response of a plan.
     *
     * @param response role (ANVIL, HAMMER, SCREEN, SUPPORT, FLANK, RESERVE, or "*" for any other) -> action
     */
    public record Entry(Kind kind, boolean active, int priority, double enter, double exit, int cooldownSeconds,
                        Map<String, String> response) {
        public String actionFor(String role) {
            String a = response.get(role);
            if (a == null) a = response.get("*");
            return a == null ? "NONE" : a;
        }
    }

    /** Runtime state of a plan's scenarios (kept in its {@link Plans.Active}). */
    static final class Run {
        final Map<Kind, ForceRules.State> states = new EnumMap<>(Kind.class);
        @Nullable
        Kind current;
        long since;
        long issuedAt;
        @Nullable
        Object savedOperation;
        /** Per unit path: enemies it saw last second (ambush = a sudden appearance from nothing). */
        final Map<String, Integer> prevSeen = new HashMap<>();
        /** Per unit path: where its pursuit began (ENEMIGO_EN_FUGA is a LIMITED pursuit). */
        final Map<String, Vec3> pursuitFrom = new HashMap<>();
        final Map<Kind, Integer> activations = new EnumMap<>(Kind.class);
    }

    /** owner#unitPath -> tick its captain fell (CAPITAN_CAIDO), fed by {@code CaptainSuccession}. */
    private static final Map<String, Long> CAPTAIN_FELL = new HashMap<>();

    // ==== Library ====

    /** The default library, every entry editable in the planner (Scenarios tab). */
    public static List<Entry> defaults() {
        List<Entry> out = new ArrayList<>();
        int wave = RallyConfig.i(RallyConfig.SCENARIO_WAVE_SIZE, 12);
        out.add(entry(Kind.CAPITAN_CAIDO, 90, 1, 0, 30, "*", "REFORM"));
        out.add(entry(Kind.BATALLA_PERDIDA, 85, 0.35, 0.20, 120,
                "ANVIL", "DELAY", "SCREEN", "DELAY", "SUPPORT", "RETREAT_HQ", "RESERVE", "COVER", "*", "FALLBACK"));
        out.add(entry(Kind.EMBOSCADA_ENEMIGA, 80, 2, 0, 60,
                "ANVIL", "HOLD", "SCREEN", "HOLD", "SUPPORT", "VOLLEY", "HAMMER", "ATTACK", "FLANK", "ATTACK", "*", "HOLD"));
        out.add(entry(Kind.DEFENSA_DE_ESTRUCTURA, 75, 1, 0, 60,
                "ANVIL", "SCREEN_HQ", "SCREEN", "SCREEN_HQ", "SUPPORT", "VOLLEY", "RESERVE", "SCREEN_HQ", "*", "SCREEN_HQ"));
        out.add(entry(Kind.JEFE, 70, 1, 0, 60,
                "ANVIL", "INSTR_HOSTIGAR", "SCREEN", "INSTR_HOSTIGAR", "SUPPORT", "VOLLEY", "HAMMER", "INSTR_HOSTIGAR",
                "*", "COVER"));
        out.add(entry(Kind.AMENAZA_AEREA, 65, 1, 0, 60, "SUPPORT", "VOLLEY", "*", "COVER"));
        out.add(entry(Kind.INFERIORIDAD, 60, 1.6, 1.1, 90,
                "ANVIL", "DELAY", "SCREEN", "DELAY", "SUPPORT", "COVER", "*", "FALLBACK"));
        out.add(entry(Kind.OLEADA, 55, wave, wave * 0.5, 60,
                "ANVIL", "HOLD", "SCREEN", "HOLD", "SUPPORT", "VOLLEY", "HAMMER", "HOLD", "*", "HOLD"));
        out.add(entry(Kind.ASALTO_A_ESTRUCTURA, 40, 1, 0, 60,
                "ANVIL", "ATTACK", "HAMMER", "ATTACK", "SUPPORT", "VOLLEY", "SCREEN", "ATTACK", "*", "HOLD"));
        out.add(entry(Kind.ENEMIGO_EN_FUGA, 30, 1, 0, 60, "HAMMER", "PURSUE", "FLANK", "PURSUE", "*", "REFORM"));
        return out;
    }

    private static Entry entry(Kind kind, int priority, double enter, double exit, int cooldown, String... roleAction) {
        Map<String, String> response = new LinkedHashMap<>();
        for (int i = 0; i + 1 < roleAction.length; i += 2) response.put(roleAction[i], roleAction[i + 1]);
        return new Entry(kind, true, priority, enter, exit, cooldown, response);
    }

    /** What a scenario's response needs from the force (the planner warns if no unit has it). */
    public static EnumSet<Capability> needs(Kind kind) {
        return switch (kind) {
            case AMENAZA_AEREA -> EnumSet.of(Capability.RANGED_SUPPORT);
            case JEFE -> EnumSet.of(Capability.RANGED_SUPPORT, Capability.MELEE_LINE);
            case ENEMIGO_EN_FUGA -> EnumSet.of(Capability.MOUNTED_CHARGE, Capability.MELEE_LINE);
            case BATALLA_PERDIDA, INFERIORIDAD -> EnumSet.of(Capability.RETREAT_COVER, Capability.HOLD_POSITION);
            case DEFENSA_DE_ESTRUCTURA -> EnumSet.of(Capability.HOLD_POSITION);
            case ASALTO_A_ESTRUCTURA -> EnumSet.of(Capability.MELEE_LINE);
            case EMBOSCADA_ENEMIGA, OLEADA, CAPITAN_CAIDO -> EnumSet.noneOf(Capability.class);
        };
    }

    // ==== JSON (saved with the plan) ====

    /** Reads the plan's "scenarios" array: missing kinds keep their defaults, unknown ones are rejected. */
    public static List<Entry> parse(JsonArray array) {
        Map<Kind, Entry> byKind = new EnumMap<>(Kind.class);
        for (Entry e : defaults()) byKind.put(e.kind(), e);
        if (array.size() > Kind.values().length * 2) throw new IllegalArgumentException("scenarios");
        for (JsonElement el : array) {
            if (!el.isJsonObject()) throw new IllegalArgumentException("scenario");
            JsonObject o = el.getAsJsonObject();
            Kind kind = Kind.parse(o.has("kind") ? o.get("kind").getAsString() : "");
            if (kind == null) throw new IllegalArgumentException("scenario kind");
            Entry base = byKind.get(kind);
            boolean active = o.has("active") ? o.get("active").getAsBoolean() : base.active();
            int priority = o.has("priority") ? clampInt(o.get("priority").getAsInt(), 0, 100) : base.priority();
            double enter = o.has("enter") ? finite(o.get("enter").getAsDouble()) : base.enter();
            double exit = o.has("exit") ? finite(o.get("exit").getAsDouble()) : base.exit();
            if (exit > enter) exit = enter; // hysteresis must be a band, never inverted
            int cooldown = o.has("cooldown") ? clampInt(o.get("cooldown").getAsInt(), 0, 3600) : base.cooldownSeconds();
            Map<String, String> response = new LinkedHashMap<>(base.response());
            if (o.has("response") && o.get("response").isJsonObject()) {
                response.clear();
                for (Map.Entry<String, JsonElement> r : o.getAsJsonObject("response").entrySet()) {
                    String role = r.getKey().toUpperCase(Locale.ROOT);
                    String action = r.getValue().getAsString().toUpperCase(Locale.ROOT);
                    if (!role.equals("*") && !Roles.ALL.contains(role)) throw new IllegalArgumentException("scenario role");
                    if (!ACTIONS.contains(action)) throw new IllegalArgumentException("scenario action");
                    response.put(role, action);
                }
            }
            byKind.put(kind, new Entry(kind, active, priority, enter, exit, cooldown, response));
        }
        return new ArrayList<>(byKind.values());
    }

    private static int clampInt(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    private static double finite(double v) {
        if (!Double.isFinite(v) || Math.abs(v) > 1.0E6D) throw new IllegalArgumentException("scenario number");
        return v;
    }

    public static JsonArray toJson(List<Entry> entries) {
        JsonArray out = new JsonArray();
        for (Entry e : entries) {
            JsonObject o = new JsonObject();
            o.addProperty("kind", e.kind().name());
            o.addProperty("active", e.active());
            o.addProperty("priority", e.priority());
            o.addProperty("enter", e.enter());
            o.addProperty("exit", e.exit());
            o.addProperty("cooldown", e.cooldownSeconds());
            JsonObject r = new JsonObject();
            e.response().forEach(r::addProperty);
            o.add("response", r);
            out.add(o);
        }
        return out;
    }

    // ==== Hooks ====

    /** A unit's captain fell (from {@code CaptainSuccession}); its force's CAPITAN_CAIDO sees it for 15 s. */
    public static void captainFell(java.util.UUID owner, String unitPath, long now) {
        if (CAPTAIN_FELL.size() > 1024) CAPTAIN_FELL.clear();
        CAPTAIN_FELL.put(owner + "#" + unitPath.toLowerCase(Locale.ROOT), now);
    }

    /** The scenario currently running on the force, or null (planner, {@code /rally debug why}). */
    @Nullable
    public static Kind current(ForceRegistry.Force force) {
        Plans.Active a = Plans.activeState(force);
        return a == null ? null : a.scenarios.current;
    }

    /** How many times each scenario started in this plan (planner and simulation). */
    public static Map<Kind, Integer> activations(ForceRegistry.Force force) {
        Plans.Active a = Plans.activeState(force);
        return a == null ? Map.of() : new EnumMap<>(a.scenarios.activations);
    }

    /** Warnings: active scenarios whose response needs a capability no unit of the force has. */
    public static List<String> warnings(List<Entry> entries, List<Forces.UnitView> units) {
        EnumSet<Capability> have = EnumSet.noneOf(Capability.class);
        for (Forces.UnitView u : units) have.addAll(u.capabilities());
        List<String> out = new ArrayList<>();
        for (Entry e : entries) {
            if (!e.active()) continue;
            EnumSet<Capability> need = needs(e.kind());
            if (need.isEmpty()) continue;
            boolean any = false;
            for (Capability c : need) any |= have.contains(c);
            if (!any) out.add(e.kind().name() + ":" + need.iterator().next().name());
        }
        return out;
    }

    // ==== Runtime ====

    /** Once a second per planned force. True while a scenario holds the force (the plan waits). */
    static boolean step(ServerLevel level, ForceRegistry.Force force, Plans.Active a, List<Forces.UnitView> units,
                        Map<String, String> roles, Map<String, SituationReports.Report> reports, long now) {
        Run run = a.scenarios;
        if (!RallyConfig.on(RallyConfig.SCENARIOS_ENABLED) || a.plan.scenarios().isEmpty()) {
            if (run.current != null) end(level, force, a, units, now);
            return false;
        }
        Facts f = facts(level, force, a, units, reports, now);
        Entry best = null;
        for (Entry e : a.plan.scenarios()) {
            if (!e.active()) continue;
            ForceRules.State st = run.states.computeIfAbsent(e.kind(), k -> new ForceRules.State());
            ForceRules.Rule rule = new ForceRules.Rule("scn-" + e.kind().name(), ForceRules.Scope.FORCE,
                    ForceRules.Metric.CASUALTIES, e.enter(), e.exit(), true, ForceRules.Action.HOLD, e.priority(),
                    e.cooldownSeconds() * 20, e.kind().minSeconds * 20, "");
            boolean was = st.active();
            ForceRules.evaluate(rule, st, detect(e.kind(), f), now);
            if (st.active() && !was) {
                run.activations.merge(e.kind(), 1, Integer::sum);
                for (Forces.UnitView u : units) {
                    if (!u.members().isEmpty()) SimProbe.count(u.members().get(0), "scn:" + e.kind().name());
                }
            }
            if (st.active() && (best == null || e.priority() > best.priority())) best = e;
        }
        for (Forces.UnitView u : units) {
            SituationReports.Report r = reports.get(u.path());
            run.prevSeen.put(u.path(), r == null ? 0 : r.enemiesSeen());
        }
        if (best == null) {
            if (run.current != null) end(level, force, a, units, now);
            return false;
        }
        if (run.current != best.kind()) {
            if (run.current == null) {
                run.savedOperation = Operations.snapshot(force);
                Operations.stop(force); // the operation would re-issue its own orders over the response
            }
            run.current = best.kind();
            run.since = now;
            run.pursuitFrom.clear();
            respond(level, force, best, units, roles, reports, f, now);
        } else if (now - run.issuedAt >= 300) {
            respond(level, force, best, units, roles, reports, f, now); // moving targets: refresh every 15 s
        } else {
            limitPursuit(level, force, run, best, units, roles, reports);
        }
        return true;
    }

    private static void end(ServerLevel level, ForceRegistry.Force force, Plans.Active a, List<Forces.UnitView> units,
                            long now) {
        Run run = a.scenarios;
        run.current = null;
        run.pursuitFrom.clear();
        List<Guard> all = new ArrayList<>();
        for (Forces.UnitView u : units) all.addAll(u.members());
        InstructionRunner.release(all);
        Operations.restore(level, force, run.savedOperation);
        run.savedOperation = null;
        if (a.phase >= 0) a.reenter = true;
    }

    // ==== Detection ====

    /** What the detectors read, gathered once per step. */
    private record Facts(int alive, int total, double moraleMean, int maxSeen, int flyers, int bosses, boolean fleeing,
                         int ambushed, @Nullable Vec3 centre, @Nullable Vec3 enemy, double hqEnemyDist, boolean assault,
                         boolean captainFell, int contactUnits) {
    }

    private static Facts facts(ServerLevel level, ForceRegistry.Force force, Plans.Active a, List<Forces.UnitView> units,
                               Map<String, SituationReports.Report> reports, long now) {
        Run run = a.scenarios;
        int alive = 0, total = 0, maxSeen = 0, ambushed = 0, contact = 0;
        double morale = 0, cx = 0, cy = 0, cz = 0;
        int mn = 0, cn = 0;
        Vec3 enemy = null;
        double ambushRange = RallyConfig.i(RallyConfig.SCENARIO_AMBUSH_RANGE, 10);
        boolean fell = false;
        for (Forces.UnitView u : units) {
            SituationReports.Report r = reports.get(u.path());
            Long t = CAPTAIN_FELL.get(force.owner + "#" + u.path().toLowerCase(Locale.ROOT));
            if (t != null && now - t <= 300) fell = true;
            if (r == null) continue;
            alive += r.alive();
            total += r.total();
            if (r.alive() > 0) {
                morale += r.morale() * r.alive();
                mn += r.alive();
            }
            maxSeen = Math.max(maxSeen, r.enemiesSeen());
            if (r.contactSince() >= 0) contact++;
            if (r.centre() != null) {
                cx += r.centre().x;
                cy += r.centre().y;
                cz += r.centre().z;
                cn++;
            }
            if (r.enemyCentre() != null && enemy == null) enemy = r.enemyCentre();
            int before = run.prevSeen.getOrDefault(u.path(), -1);
            if (before == 0 && r.enemiesSeen() > 0 && r.centre() != null && r.enemyCentre() != null
                    && r.centre().distanceTo(r.enemyCentre()) <= ambushRange) {
                ambushed += r.enemiesSeen(); // nothing a second ago, now enemies at arm's length
            }
        }
        Vec3 centre = cn == 0 ? null : new Vec3(cx / cn, cy / cn, cz / cn);
        int flyers = 0, bosses = 0;
        boolean fleeing = false;
        double hqDist = Double.MAX_VALUE;
        BlockPos hq = hqOf(level, units);
        for (SituationReports.KnownEnemy k : SituationReports.knownEnemies(ForceBrain.forceKey(force), now)) {
            flyers = Math.max(flyers, k.flyers());
            bosses = Math.max(bosses, k.bosses());
            fleeing |= k.fleeing();
            if (hq != null) hqDist = Math.min(hqDist, Math.sqrt(k.centre().distanceToSqr(Vec3.atBottomCenterOf(hq))));
        }
        boolean assault = false;
        if (a.phase >= 0 && a.phase < a.plan.phases().size() && centre != null) {
            Plans.Phase p = a.plan.phases().get(a.phase);
            if (p.objective() == Plans.Objective.ATTACK_ZONE || p.objective() == Plans.Objective.ATTACK_GROUP) {
                double dx = centre.x - p.x(), dz = centre.z - p.z();
                assault = contact > 0 && Math.sqrt(dx * dx + dz * dz) <= Math.max(8.0D, p.radius()) + 16.0D;
            }
        }
        return new Facts(alive, total, mn == 0 ? 100 : morale / mn, maxSeen, flyers, bosses, fleeing, ambushed, centre,
                enemy, hqDist, assault, fell, contact);
    }

    @Nullable
    private static BlockPos hqOf(ServerLevel level, List<Forces.UnitView> units) {
        for (Forces.UnitView u : units) {
            if (!u.members().isEmpty()) return net.hfstack.rallyguard.order.HqRelax.hqOf(level, u.members().get(0));
        }
        return null;
    }

    /** One small detector per kind: a number compared with the entry's enter / exit thresholds. */
    private static double detect(Kind kind, Facts f) {
        double casualties = f.total() <= 0 ? 0 : 1.0D - (double) f.alive() / f.total();
        return switch (kind) {
            case EMBOSCADA_ENEMIGA -> f.ambushed();
            case ASALTO_A_ESTRUCTURA -> f.assault() ? 1 : 0;
            case DEFENSA_DE_ESTRUCTURA -> f.hqEnemyDist() <= RallyConfig.i(RallyConfig.SCENARIO_STRUCTURE_RADIUS, 32) ? 1 : 0;
            case OLEADA -> f.maxSeen();
            case AMENAZA_AEREA -> f.flyers();
            case JEFE -> f.bosses();
            case ENEMIGO_EN_FUGA -> f.fleeing() && casualties < 0.5D && f.moraleMean() >= 40 ? 1 : 0;
            // Lost battle: casualties count fully only with morale low too (morale off -> mean 100 -> 0.7 x).
            case BATALLA_PERDIDA -> f.moraleMean() < 45 ? casualties : casualties * 0.7D;
            case INFERIORIDAD -> f.alive() <= 0 ? 0 : (double) f.maxSeen() / f.alive();
            case CAPITAN_CAIDO -> f.captainFell() ? 1 : 0;
        };
    }

    // ==== Response ====

    private static void respond(ServerLevel level, ForceRegistry.Force force, Entry e, List<Forces.UnitView> units,
                                Map<String, String> roles, Map<String, SituationReports.Report> reports, Facts f, long now) {
        Plans.Active a = Plans.activeState(force);
        if (a != null) a.scenarios.issuedAt = now;
        BlockPos hq = hqOf(level, units);
        String why = "scenario:" + e.kind().name();
        for (Forces.UnitView u : units) {
            String action = e.actionFor(roles.getOrDefault(u.path(), ""));
            if (action.equals("NONE")) continue;
            SituationReports.Report r = reports.get(u.path());
            Vec3 centre = r != null && r.centre() != null ? r.centre() : f.centre();
            if (centre == null) continue;
            Vec3 enemy = r != null && r.enemyCentre() != null ? r.enemyCentre() : f.enemy();
            float facing = Operations.facing(centre, enemy);
            String key = UnitOrders.unitKey(force.owner, u.path());
            if (action.startsWith("INSTR_")) {
                InstructionInterpreter.Instruction in = InstructionInterpreter.parse(action.substring(6));
                if (in != null && InstructionRunner.instructionOf(key) != in) {
                    InstructionRunner.start(level, force.owner, u.members(), in, centre, facing);
                }
                continue;
            }
            switch (action) {
                case "HOLD", "REFORM" -> UnitOrders.issue(level, key, UnitOrders.Kind.HOLD, UnitOrders.Source.FORCE,
                        UnitOrders.ground(level, centre), facing, FormationShape.LINE, null, 0, why);
                case "FALLBACK" -> UnitOrders.issue(level, key, UnitOrders.Kind.FALLBACK, UnitOrders.Source.FORCE,
                        UnitOrders.ground(level, Operations.away(centre, enemy, 10.0D)), facing, FormationShape.LINE,
                        FireMode.RETREATING, 0, why);
                case "DELAY" -> UnitOrders.issue(level, key, UnitOrders.Kind.FALLBACK, UnitOrders.Source.FORCE,
                        UnitOrders.ground(level, Operations.away(centre, enemy, 5.0D)), facing, FormationShape.LINE,
                        FireMode.RETREATING, 0, why);
                case "RETREAT_HQ" -> UnitOrders.issue(level, key, UnitOrders.Kind.RETREAT_HQ, UnitOrders.Source.FORCE,
                        null, 0, FormationShape.LINE, null, 0, why);
                case "ATTACK" -> {
                    if (enemy == null) continue;
                    UnitOrders.issue(level, key, UnitOrders.Kind.ATTACK_AREA, UnitOrders.Source.FORCE,
                            UnitOrders.ground(level, enemy), facing, FormationShape.LINE, FireMode.ADVANCING, 12, why);
                }
                case "PURSUE" -> {
                    if (enemy == null) continue;
                    // A LIMITED pursuit: at most 20 blocks from where it started.
                    if (a != null) a.scenarios.pursuitFrom.putIfAbsent(u.path(), centre);
                    Vec3 to = capped(centre, enemy, 20.0D);
                    UnitOrders.issue(level, key, UnitOrders.Kind.ATTACK_AREA, UnitOrders.Source.FORCE,
                            UnitOrders.ground(level, to), facing, FormationShape.LINE, FireMode.ADVANCING, 8, why);
                }
                case "SCREEN_HQ" -> {
                    Vec3 post = centre;
                    if (hq != null) {
                        Vec3 home = Vec3.atBottomCenterOf(hq);
                        post = enemy == null ? home : capped(home, enemy, 6.0D);
                    }
                    UnitOrders.issue(level, key, UnitOrders.Kind.HOLD, UnitOrders.Source.FORCE,
                            UnitOrders.ground(level, post), Operations.facing(post, enemy), FormationShape.LINE, null, 0, why);
                }
                case "VOLLEY" -> UnitOrders.issue(level, key, UnitOrders.Kind.FIRE_STYLE, UnitOrders.Source.FORCE, null, 0,
                        FormationShape.LINE, FireMode.VOLLEY, 0, why);
                case "COVER" -> {
                    Vec3 back = f.centre() == null ? centre : Operations.away(f.centre(), enemy, 6.0D);
                    UnitOrders.issue(level, key, UnitOrders.Kind.HOLD, UnitOrders.Source.FORCE,
                            UnitOrders.ground(level, back), facing, FormationShape.LINE, FireMode.STILL, 0, why);
                }
                default -> {
                }
            }
        }
    }

    /** A pursuing unit that ran past its leash (24 blocks) stops and reforms where it is. */
    private static void limitPursuit(ServerLevel level, ForceRegistry.Force force, Run run, Entry e,
                                     List<Forces.UnitView> units, Map<String, String> roles,
                                     Map<String, SituationReports.Report> reports) {
        if (run.pursuitFrom.isEmpty()) return;
        for (Forces.UnitView u : units) {
            Vec3 from = run.pursuitFrom.get(u.path());
            SituationReports.Report r = reports.get(u.path());
            if (from == null || r == null || r.centre() == null) continue;
            if (!"PURSUE".equals(e.actionFor(roles.getOrDefault(u.path(), "")))) continue;
            if (r.centre().distanceTo(from) <= 24.0D) continue;
            run.pursuitFrom.remove(u.path());
            UnitOrders.issue(level, UnitOrders.unitKey(force.owner, u.path()), UnitOrders.Kind.HOLD, UnitOrders.Source.FORCE,
                    UnitOrders.ground(level, r.centre()), Operations.facing(r.centre(), r.enemyCentre()),
                    FormationShape.LINE, null, 0, "scenario:pursuit-limit");
            if (!u.members().isEmpty()) SimProbe.count(u.members().get(0), "pursuitLimited");
        }
    }

    /** {@code from} moved toward {@code to} by at most {@code max} blocks. */
    private static Vec3 capped(Vec3 from, Vec3 to, double max) {
        Vec3 d = to.subtract(from);
        double len = d.length();
        return len <= max || len < 1.0E-6D ? to : from.add(d.scale(max / len));
    }

    public static void clearAll() {
        CAPTAIN_FELL.clear();
    }
}
