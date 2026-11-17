package net.hfstack.rallyguard.command;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.debug.Perf;
import net.hfstack.rallyguard.formation.FormationLayout;
import net.hfstack.rallyguard.force.UnitOrders;
import net.hfstack.rallyguard.tactics.DecisionLadder;
import net.hfstack.rallyguard.tactics.DecisionReason;
import net.hfstack.rallyguard.tactics.DecisionTrace;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.hfstack.rallyguard.veterancy.VeterancyEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * E3B G3 - carries out a general instruction, one SUBGROUP at a time: the group emits the
 * instruction once (anchor + facing), each subgroup (a squad unit, or the loose guards as one
 * ad-hoc unit) reads it through its own {@link GroupProfile} ({@link InstructionInterpreter})
 * and turns its {@link InstructionInterpreter.Behavior} into VERSIONED unit directives
 * ({@link UnitOrders}, source PLAYER - it is the player's standing instruction).
 * <p>
 * Each subgroup is evaluated at most once a second (staggered over the 20 ticks), with ONE
 * entity query around its centre for the enemy (the squad pictures only exist for busy guards);
 * a new directive is issued only when the wanted action, its spot (4-block grid) or its fire
 * style change. A subgroup stops when another order reaches it - a newer unit directive that is
 * not its own, or any explicit player order ({@link #release}). Inside the instruction its
 * guards fight with the instruction's doctrine ({@link #doctrineOverride}).
 */
public final class InstructionRunner {
    private InstructionRunner() {
    }

    /**
     * F5 (2026-11-12): where a withdrawal goes - a refuge, a fort, the HQ. Null = straight away from the enemy. The
     * simulation sets it per run (tag {@code refuge:D}); the Battle Planner will set it in the game.
     */
    @Nullable
    public static Vec3 retreatPoint;

    /**
     * F16c (simulation only, filled by SimExtras for per-unit RETIRARSE; always empty in the game): unit keys whose
     * RETREAT_FIGHTING bounds are FALLBACK instead of MOVE. A MOVE becomes a squad HOLD directive, and a rank-1 guard
     * with a target never returns to its slot (DecisionLadder rung 3 needs rank 2+), so the unit kept fighting where
     * it stood (rounds 56/56b). The hold phase stays HOLD (the unit still fights between bounds).
     */
    public static final java.util.Set<String> SIM_REAL_BOUNDS = new java.util.HashSet<>();

    static final class Group {
        final UUID owner;
        final InstructionInterpreter.Instruction instruction;
        Vec3 anchor;
        final float facing;
        final long start;
        long lastAdvance;

        Group(UUID owner, InstructionInterpreter.Instruction instruction, Vec3 anchor, float facing, long start) {
            this.owner = owner;
            this.instruction = instruction;
            this.anchor = anchor;
            this.facing = facing;
            this.start = start;
            this.lastAdvance = start;
        }
    }

    static final class Unit {
        final String key;
        final Group group;
        final boolean adhoc;
        /** F16f: the dimension the unit was started in; {@link #tick} runs once per level and must skip the others. */
        final ResourceKey<Level> dimension;
        InstructionInterpreter.Behavior behavior;
        GroupProfile.Family family;
        long lastVersion = -1;
        String lastSig = "";
        int phase;
        long phaseStart;
        double angle = Double.NaN;
        int side;
        /** F16d (sim telemetry): one member, to file the unit's rf:* counters even after it is gone. */
        @Nullable
        UUID simRef;
        /** F16e (sim telemetry / sim fix): the guards the unit started with, and whether it was re-bound ad hoc. */
        final java.util.Set<UUID> simIds = new java.util.LinkedHashSet<>();
        boolean simRebound;

        Unit(String key, Group group, boolean adhoc, ResourceKey<Level> dimension) {
            this.key = key;
            this.group = group;
            this.adhoc = adhoc;
            this.dimension = dimension;
        }
    }

    private static final Map<String, Unit> UNITS = new LinkedHashMap<>();
    private static final Map<UUID, Doctrine> DOCTRINE = new HashMap<>();
    /** Guards whose subgroup's instruction says "only answer whoever hits you" (engage = false). */
    private static final java.util.Set<UUID> HOLD_BACK = new java.util.HashSet<>();
    private static long issued;

    // ==== Starting / stopping ====

    /**
     * Starts {@code instruction} for these guards (all of one owner): split by squad/unit path,
     * the loose ones as one ad-hoc unit. Returns the subgroups started ("Path=FAMILY").
     */
    public static List<String> start(ServerLevel level, UUID owner, List<Guard> guards,
                                     InstructionInterpreter.Instruction instruction, Vec3 anchor, float facing) {
        List<String> out = new ArrayList<>();
        if (!RallyConfig.on(RallyConfig.INSTRUCTIONS_ENABLED) || guards.isEmpty()) return out;
        release(guards);
        SquadRegistry squads = SquadRegistry.get(level);
        Map<String, List<Guard>> byPath = new LinkedHashMap<>();
        for (Guard g : guards) {
            Optional<String> path = squads.pathOf(g.getUUID());
            byPath.computeIfAbsent(path.orElse(""), k -> new ArrayList<>()).add(g);
        }
        long now = level.getGameTime();
        Group group = new Group(owner, instruction, anchor, facing, now);
        int index = 0;
        for (Map.Entry<String, List<Guard>> e : byPath.entrySet()) {
            boolean adhoc = e.getKey().isEmpty();
            String key = adhoc ? "adhoc#" + owner + "#" + e.getValue().get(0).getUUID() : UnitOrders.unitKey(owner, e.getKey());
            if (adhoc) UnitOrders.bindAdhoc(key, e.getValue());
            Unit u = new Unit(key, group, adhoc, level.dimension());
            GroupProfile.Profile profile = GroupProfile.cached(key, e.getValue(), now);
            u.family = profile.family();
            u.behavior = InstructionInterpreter.resolve(instruction, profile);
            u.phaseStart = now;
            u.side = index++ % 2 == 0 ? 1 : -1;
            u.simRef = e.getValue().get(0).getUUID();
            for (Guard g : e.getValue()) u.simIds.add(g.getUUID()); // F16e
            UNITS.put(key, u);
            rf(u, "start"); // F16d
            for (Guard g : e.getValue()) {
                DOCTRINE.put(g.getUUID(), u.behavior.doctrine());
                if (!u.behavior.engage()) HOLD_BACK.add(g.getUUID());
            }
            out.add((adhoc ? "-" : e.getKey()) + "=" + u.family.name() + (profile.identity() ? "" : "*"));
        }
        return out;
    }

    /** Any explicit player order to these guards ends the instruction of their subgroups. */
    public static void release(Collection<Guard> guards) {
        if (UNITS.isEmpty()) return;
        for (Guard g : guards) {
            HOLD_BACK.remove(g.getUUID());
            if (DOCTRINE.remove(g.getUUID()) == null) continue;
            Iterator<Map.Entry<String, Unit>> it = UNITS.entrySet().iterator();
            while (it.hasNext()) {
                Unit u = it.next().getValue();
                if (u.key.equals(keyOfAny(g))) {
                    rf(u, "end:released"); // F16d
                    stop(u);
                    it.remove();
                }
            }
        }
    }

    @Nullable
    private static String keyOfAny(Guard g) {
        return g.level() instanceof ServerLevel level ? UnitOrders.unitKeyOf(level, g) : null;
    }

    private static void stop(Unit u) {
        if (u.adhoc || u.simRebound) UnitOrders.unbindAdhoc(u.key); // F16e: a sim re-bound unit is ad hoc too
    }

    /** E3B G3: the doctrine a guard fights with while its subgroup follows an instruction (null = none). */
    @Nullable
    public static Doctrine doctrineOverride(UUID guardId) {
        return DOCTRINE.get(guardId);
    }

    /**
     * The stance the gate applies while an instruction is in force: an instruction with
     * {@code engage = false} lowers AGGRESSIVE / RUTHLESS to DEFENSIVE (answer only whoever hits).
     */
    public static net.hfstack.rallyguard.contract.Stance effectiveStance(UUID guardId, net.hfstack.rallyguard.contract.Stance stance) {
        if (!HOLD_BACK.contains(guardId)) return stance;
        return stance == net.hfstack.rallyguard.contract.Stance.AGGRESSIVE || stance == net.hfstack.rallyguard.contract.Stance.RUTHLESS
                ? net.hfstack.rallyguard.contract.Stance.DEFENSIVE : stance;
    }

    /** E3B P1 (AMBUSH): these guards only answer whoever hits them (hidden, waiting) - or again take targets. */
    public static void holdBack(Collection<Guard> guards, boolean on) {
        for (Guard g : guards) {
            if (on) HOLD_BACK.add(g.getUUID());
            else HOLD_BACK.remove(g.getUUID());
        }
    }

    /** E3B P1: a plan link sets the doctrine its guards fight with (null = back to their own). */
    public static void overrideDoctrine(Collection<Guard> guards, @Nullable Doctrine doctrine) {
        for (Guard g : guards) {
            if (doctrine == null) DOCTRINE.remove(g.getUUID());
            else DOCTRINE.put(g.getUUID(), doctrine);
        }
    }

    @Nullable
    public static InstructionInterpreter.Instruction instructionOf(String unitKey) {
        Unit u = UNITS.get(unitKey);
        return u == null ? null : u.group.instruction;
    }

    public static boolean active() {
        return !UNITS.isEmpty();
    }

    // ==== Each tick (a subgroup ≤ once a second) ====

    public static void tick(ServerLevel level) {
        if (UNITS.isEmpty()) return;
        long now = level.getGameTime();
        long t0 = Perf.t0();
        List<Unit> list = new ArrayList<>(UNITS.values());
        for (int i = 0; i < list.size(); i++) {
            if ((i + now) % 20 != 0) continue;
            Unit u = list.get(i);
            if (!u.dimension.equals(level.dimension())) continue; // F16f: another level's unit (UNITS is global)
            try {
                if (!step(level, u, now)) {
                    UNITS.remove(u.key);
                    stop(u);
                }
            } catch (RuntimeException e) {
                rf(u, "end:broken"); // F16d
                com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] instruction for {} dropped after an error", u.key, e);
                UNITS.remove(u.key);
                stop(u);
            }
        }
        Perf.add(Perf.Sys.FORCES, t0);
    }

    /** One evaluation; false = the subgroup is done (empty, superseded or finished). */
    private static boolean step(ServerLevel level, Unit u, long now) {
        List<Guard> members = UnitOrders.members(level, u.key);
        if (members.isEmpty() && rfOn(u)) members = simKeep(level, u); // F16e: sim unitInstr route only
        if (members.isEmpty()) {
            rf(u, "end:empty"); // F16d
            return false;
        }
        UnitOrders.UnitOrder current = UnitOrders.current(u.key);
        if (u.lastVersion >= 0 && (current == null || current.version() != u.lastVersion)) {
            rf(u, current == null ? "end:override:none" : "end:override:" + current.kind() + ":" + current.source() + ":"
                    + String.valueOf(current.why()).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_")); // F16d: who took the unit
            for (Guard g : members) {
                DOCTRINE.remove(g.getUUID());
                HOLD_BACK.remove(g.getUUID());
            }
            return false; // superseded by another directive
        }
        Vec3 centre = centre(members);
        Guard ref = members.get(0);
        Enemy enemy = enemy(level, ref, centre);
        InstructionInterpreter.Behavior b = u.behavior;
        Group gr = u.group;
        Vec3 fwd = forward(gr.facing);
        Vec3 right = new Vec3(-fwd.z, 0, fwd.x);
        double reach = Math.max(3.0D, GroupProfile.cached(u.key, members, now).reachMean());
        double secs = (now - u.phaseStart) / 20.0D;
        FormationLayout layout = FormationLayout.DEFAULT.withDensity(b.density());

        Action a;
        switch (b.mode()) {
            case KEEP_DISTANCE -> {
                if (enemy == null) {
                    a = Action.hold(centre, gr.facing, b.fire());
                } else {
                    double d = horizontal(centre, enemy.centre);
                    double near = reach * b.near(), far = reach * b.far();
                    Vec3 away = flat(centre.subtract(enemy.centre));
                    float face = yaw(centre, enemy.centre);
                    double mid = (near + far) / 2.0D;
                    if (d < near) a = new Action(UnitOrders.Kind.FALLBACK, enemy.centre.add(away.scale(mid)), face, b.fire());
                    else if (d > far) a = new Action(UnitOrders.Kind.MOVE, enemy.centre.add(away.scale(mid)), face, b.fire());
                    else a = Action.hold(centre, face, b.fire());
                }
            }
            case THREATEN_FLANK -> {
                if (enemy == null) {
                    a = Action.hold(gr.anchor, gr.facing, null);
                } else {
                    Vec3 toUs = flat(centre.subtract(enemy.centre));
                    Vec3 side = new Vec3(-toUs.z, 0, toUs.x).scale(u.side);
                    Vec3 spot = enemy.centre.add(side.scale(b.offset())).add(toUs.scale(b.offset() * 0.5D));
                    a = Action.hold(spot, yaw(spot, enemy.centre), null);
                }
            }
            case HIT_AND_RUN -> {
                int cycle = Math.max(2, b.cycle());
                if (secs >= cycle) {
                    u.phase = (u.phase + 1) % 2;
                    u.phaseStart = now;
                }
                if (enemy == null) {
                    a = Action.hold(gr.anchor, gr.facing, null);
                } else if (u.phase == 0) {
                    a = new Action(UnitOrders.Kind.ATTACK_AREA, enemy.centre, yaw(centre, enemy.centre), null);
                } else {
                    Vec3 away = flat(centre.subtract(enemy.centre));
                    Vec3 side = new Vec3(-away.z, 0, away.x).scale(u.side);
                    a = new Action(UnitOrders.Kind.FALLBACK, enemy.centre.add(away.add(side).normalize().scale(b.offset())),
                            yaw(centre, enemy.centre), null);
                }
            }
            case HOLD -> a = Action.hold(gr.anchor.add(fwd.scale(b.offset())), gr.facing, b.fire());
            case RESERVE -> {
                Vec3 post = gr.anchor.subtract(fwd.scale(Math.abs(b.offset())));
                if (enemy != null && b.counter() > 0 && horizontal(enemy.centre, gr.anchor) <= b.counter()) {
                    a = new Action(UnitOrders.Kind.ATTACK_AREA, enemy.centre, yaw(post, enemy.centre), null);
                } else {
                    a = Action.hold(post, gr.facing, b.fire());
                }
            }
            case ADVANCE, ADVANCE_TURNS -> {
                advanceAnchor(gr, b, enemy, fwd, now);
                Vec3 spot = gr.anchor.add(fwd.scale(b.offset() < 0 ? b.offset() : 0));
                boolean contact = enemy != null && horizontal(enemy.centre, centre) <= Math.max(b.counter(), reach * b.far());
                if (enemy != null && b.counter() > 0 && horizontal(enemy.centre, centre) <= b.counter()) {
                    a = new Action(UnitOrders.Kind.ATTACK_AREA, enemy.centre, yaw(centre, enemy.centre), null);
                } else if (b.mode() == InstructionInterpreter.Mode.ADVANCE_TURNS && (contact || (now / 20L / Math.max(2, b.cycle())) % 2 == 1)) {
                    a = Action.hold(centre, gr.facing, net.hfstack.rallyguard.combat.FireMode.VOLLEY);
                } else {
                    a = new Action(UnitOrders.Kind.MOVE, spot, gr.facing, b.fire());
                }
            }
            case FLANK_ADVANCE -> {
                advanceAnchor(gr, b, enemy, fwd, now);
                a = new Action(UnitOrders.Kind.MOVE, gr.anchor.add(right.scale(u.side * b.offset())), gr.facing, b.fire());
            }
            case RETREAT_STAGGERED, RETREAT_LAST -> {
                int wait = b.mode() == InstructionInterpreter.Mode.RETREAT_LAST ? Math.max(0, b.cycle()) : 0;
                int cycle = Math.max(3, b.mode() == InstructionInterpreter.Mode.RETREAT_LAST ? 6 : b.cycle());
                double since = (now - gr.start) / 20.0D;
                if (since < wait) {
                    a = Action.hold(centre, enemy == null ? gr.facing : yaw(centre, enemy.centre), b.fire());
                } else {
                    int steps = (int) ((since - wait) / cycle);
                    if (steps >= 5) {
                        a = new Action(UnitOrders.Kind.RETREAT_HQ, null, 0, null);
                    } else {
                        Vec3 from = enemy == null ? gr.anchor.add(fwd.scale(20)) : enemy.centre;
                        Vec3 away = flat(gr.anchor.subtract(from));
                        Vec3 spot = gr.anchor.add(away.scale(b.offset() * (steps + 1)));
                        a = new Action(UnitOrders.Kind.FALLBACK, spot, yaw(spot, from), b.fire());
                    }
                }
            }
            case RETREAT_FAST -> a = new Action(UnitOrders.Kind.RETREAT_HQ, null, 0, null);
            case RETREAT_TO_POINT -> { // F5: run to the refuge without fighting (engage=false)
                Vec3 to = retreatPoint != null ? retreatPoint : centre.add(flat(centre.subtract(
                        enemy == null ? gr.anchor.add(fwd.scale(20)) : enemy.centre)).scale(24));
                // FALLBACK, not MOVE: it becomes a squad RETREAT directive, which the decision ladder obeys first.
                // With MOVE (round 52) the ladder's SEEK_TARGET pulled the guards back to the enemy and only 10-40 %
                // reached the refuge.
                a = new Action(UnitOrders.Kind.FALLBACK, to, yaw(centre, to), null);
            }
            case RETREAT_FIGHTING -> {
                // 2026-11-11: fighting withdrawal - hold and fire, fall back a bound still firing, repeat; never stop
                // fighting while the enemy can reach us, run on once it can't. MOVE, not FALLBACK: a FALLBACK becomes a squad
                // RETREAT directive, and a retreating guard stops fighting (what round 39 measured).
                Vec3 from = enemy == null ? gr.anchor.add(fwd.scale(20)) : enemy.centre;
                Vec3 away = flat(centre.subtract(from));
                if (retreatPoint != null && horizontal(centre, retreatPoint) > 3.0D) away = flat(retreatPoint.subtract(centre)); // F5
                if (away.lengthSqr() < 1.0E-4) away = fwd.scale(-1);
                float face = yaw(centre, from);
                double d = enemy == null ? Double.MAX_VALUE : horizontal(centre, enemy.centre);
                int cycle = Math.max(2, b.cycle());
                rf(u, "step"); // F16d: one evaluation (about once a second)
                if (rfOn(u)) { // F16e: unit size this step, before the directive (sums: divide by step)
                    rfAdd(u, "members", members.size());
                    rfAdd(u, "adhoc", UnitOrders.adhocSize(u.key));
                    rfAdd(u, "loaded", loadedOf(level, u));
                }
                if (secs >= cycle) {
                    u.phase = (u.phase + 1) % 2;
                    u.phaseStart = now;
                    rf(u, "phaseFlip");
                }
                if (enemy == null) {
                    rf(u, "noThreat");
                } else { // F16d: distance to the enemy centre and the bound thresholds, x10, summed over the steps with an enemy
                    rfAdd(u, "dX10", Math.round(d * 10));
                    rfAdd(u, "farX10", Math.round(reach * b.far() * 10));
                    rfAdd(u, "nearX10", Math.round(reach * b.near() * 10));
                }
                UnitOrders.Kind bound = SIM_REAL_BOUNDS.contains(u.key) ? UnitOrders.Kind.FALLBACK : UnitOrders.Kind.MOVE; // F16c
                if (bound == UnitOrders.Kind.FALLBACK) rf(u, "realBounds"); // F16d: the F16c key matched this unit
                if (enemy == null || d > reach * b.far()) {
                    a = new Action(bound, centre.add(away.scale(Math.max(8.0D, b.offset() * 2))), face, b.fire());
                    rf(u, "jump:far");
                } else if (d < reach * b.near() || u.phase == 1) {
                    a = new Action(bound, centre.add(away.scale(b.offset())), face, b.fire());
                    rf(u, d < reach * b.near() ? "jump:near" : "jump:phase");
                } else {
                    a = Action.hold(centre, face, b.fire());
                    rf(u, "hold");
                }
            }
            case COVER_FLANKS -> {
                if ((now - gr.start) / 20.0D >= Math.max(1, b.cycle())) {
                    a = new Action(UnitOrders.Kind.RETREAT_HQ, null, 0, null);
                } else {
                    Vec3 spot = gr.anchor.add(right.scale(u.side * b.offset())).subtract(fwd.scale(4));
                    a = Action.hold(spot, gr.facing, null);
                }
            }
            case FLANK_POSITION -> {
                if (enemy == null) {
                    a = Action.hold(gr.anchor.add(right.scale(u.side * 10.0D)), gr.facing, b.fire());
                } else {
                    Vec3 toUs = flat(gr.anchor.subtract(enemy.centre));
                    Vec3 side = new Vec3(-toUs.z, 0, toUs.x).scale(u.side);
                    Vec3 spot = enemy.centre.add(side.add(toUs).normalize().scale(reach * b.far()));
                    a = Action.hold(spot, yaw(spot, enemy.centre), b.fire());
                }
            }
            case ENCIRCLE -> {
                if (enemy == null) {
                    a = Action.hold(centre, gr.facing, b.fire());
                } else {
                    if (Double.isNaN(u.angle)) u.angle = Math.atan2(centre.z - enemy.centre.z, centre.x - enemy.centre.x);
                    if (secs >= Math.max(2, b.cycle())) {
                        u.angle += u.side * Math.toRadians(30);
                        u.phaseStart = now;
                    }
                    double r = reach * b.far();
                    Vec3 spot = enemy.centre.add(Math.cos(u.angle) * r, 0, Math.sin(u.angle) * r);
                    a = new Action(UnitOrders.Kind.MOVE, spot, yaw(spot, enemy.centre), b.fire());
                }
            }
            case FIX_FRONT -> {
                if (enemy == null) {
                    a = Action.hold(gr.anchor, gr.facing, null);
                } else {
                    Vec3 toUs = flat(gr.anchor.subtract(enemy.centre));
                    Vec3 spot = enemy.centre.add(toUs.scale(b.offset()));
                    a = Action.hold(spot, yaw(spot, enemy.centre), null);
                }
            }
            case FLANK_CHARGE -> {
                if (enemy == null) {
                    a = Action.hold(gr.anchor.add(right.scale(u.side * b.offset())), gr.facing, null);
                } else {
                    Vec3 dir = flat(enemy.centre.subtract(gr.anchor));
                    Vec3 side = new Vec3(-dir.z, 0, dir.x).scale(u.side);
                    Vec3 rear = enemy.centre.add(dir.scale(b.offset())).add(side.scale(b.offset() * 0.6D));
                    boolean there = horizontal(centre, rear) <= Math.max(3.0D, b.counter());
                    if (u.phase == 0 && (there || secs >= Math.max(5, b.cycle()))) {
                        u.phase = 1;
                        u.phaseStart = now;
                    }
                    a = u.phase == 0 ? new Action(UnitOrders.Kind.MOVE, rear, yaw(rear, enemy.centre), null)
                            : new Action(UnitOrders.Kind.ATTACK_AREA, enemy.centre, yaw(centre, enemy.centre), null);
                }
            }
            default -> a = Action.hold(centre, gr.facing, b.fire());
        }

        String sig = a.kind + "|" + (a.at == null ? "-" : Math.round(a.at.x / 4) + "," + Math.round(a.at.z / 4)) + "|" + a.fire;
        if (sig.equals(u.lastSig) && current != null) {
            rf(u, "same"); // F16d: same action and spot as the last directive, nothing issued
            rfPost(level, u);
            return true;
        }
        u.lastSig = sig;
        rf(u, "issued:" + a.kind); // F16d
        Vec3 at = a.at == null ? null : a.kind == UnitOrders.Kind.ATTACK_AREA ? a.at : groundOf(level, a.at, ref);
        UnitOrders.UnitOrder issuedOrder = UnitOrders.issue(level, u.key, a.kind, UnitOrders.Source.PLAYER, at, a.facing,
                b.shape(), a.fire, 16.0D, "instr " + gr.instruction.name().toLowerCase(Locale.ROOT), layout, b.tempo());
        u.lastVersion = issuedOrder.version();
        issued++;
        boolean retreatStep = b.mode() == InstructionInterpreter.Mode.RETREAT_FIGHTING && a.kind != UnitOrders.Kind.HOLD; // F16c
        for (Guard g : members) {
            DOCTRINE.put(g.getUUID(), b.doctrine());
            DecisionTrace.record(g, DecisionLadder.Rung.OBEY_DIRECTIVE, DecisionReason.INSTRUCTION);
            if (retreatStep) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:retreat"); // sim telemetry only (no-op in game)
        }
        rfPost(level, u);
        return true;
    }

    /** What the subgroup should do now. {@code fire} null = keep the current style. */
    private record Action(UnitOrders.Kind kind, @Nullable Vec3 at, float facing,
                          @Nullable net.hfstack.rallyguard.combat.FireMode fire) {
        static Action hold(Vec3 at, float facing, @Nullable net.hfstack.rallyguard.combat.FireMode fire) {
            return new Action(UnitOrders.Kind.HOLD, at, facing, fire);
        }
    }

    /** ADVANCE: the shared anchor gains {@code |offset|} (≥ 4) every cycle until the enemy is within reach. */
    private static void advanceAnchor(Group gr, InstructionInterpreter.Behavior b, @Nullable Enemy enemy, Vec3 fwd, long now) {
        int cycle = Math.max(2, b.cycle());
        if (now - gr.lastAdvance < cycle * 20L) return;
        if (enemy != null && horizontal(enemy.centre, gr.anchor) <= 10.0D) return;
        gr.lastAdvance = now;
        gr.anchor = gr.anchor.add(fwd.scale(Math.max(4.0D, Math.abs(b.offset()))));
    }

    // ==== Helpers ====

    record Enemy(Vec3 centre, int count) {
    }

    /** One query per subgroup and second: the real enemies within 40 blocks of its centre. */
    @Nullable
    static Enemy enemy(ServerLevel level, Guard ref, Vec3 centre) {
        double x = 0, y = 0, z = 0;
        int n = 0;
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, new AABB(centre, centre).inflate(40.0D, 12.0D, 40.0D),
                LivingEntity::isAlive)) {
            if (!VeterancyEvents.realEnemy(ref, e)) continue;
            x += e.getX();
            y += e.getY();
            z += e.getZ();
            n++;
        }
        return n == 0 ? null : new Enemy(new Vec3(x / n, y / n, z / n), n);
    }

    /** The centre of the real enemies near {@code centre} (one query), or null. */
    @Nullable
    public static Vec3 enemyCentre(ServerLevel level, Guard ref, Vec3 centre) {
        Enemy e = enemy(level, ref, centre);
        return e == null ? null : e.centre();
    }

    public static Vec3 centre(List<Guard> members) {
        double x = 0, y = 0, z = 0;
        for (Guard g : members) {
            x += g.getX();
            y += g.getY();
            z += g.getZ();
        }
        int n = Math.max(1, members.size());
        return new Vec3(x / n, y / n, z / n);
    }

    private static Vec3 groundOf(ServerLevel level, Vec3 p, Guard ref) {
        return Vec3.atBottomCenterOf(net.hfstack.rallyguard.order.GuardPosts.standableNear(level, new Vec3(p.x, ref.getY(), p.z)));
    }

    static Vec3 flat(Vec3 v) {
        Vec3 f = new Vec3(v.x, 0, v.z);
        return f.lengthSqr() < 1.0E-6 ? new Vec3(0, 0, 1) : f.normalize();
    }

    static double horizontal(Vec3 a, Vec3 b) {
        double dx = a.x - b.x, dz = a.z - b.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    static Vec3 forward(float yaw) {
        double r = Math.toRadians(yaw);
        return new Vec3(-Math.sin(r), 0, Math.cos(r));
    }

    public static float yaw(Vec3 from, Vec3 to) {
        double dx = to.x - from.x, dz = to.z - from.z;
        return dx * dx + dz * dz < 1.0E-4 ? 0.0F : (float) Math.toDegrees(Math.atan2(-dx, dz));
    }

    public static long issued() {
        return issued;
    }

    /**
     * F16d (simulation telemetry only): a counter of a RETREAT_FIGHTING unit, filed per sim unit as
     * {@code u<k>:rf:<what>}. A no-op in the game (SimExtras keeps no units outside a {@code unitInstr} run).
     */
    private static void rf(Unit u, String what) {
        rfAdd(u, what, 1L);
    }

    /** F16e: true only for a RETREAT_FIGHTING unit of a sim unitInstr run (never in the game). */
    private static boolean rfOn(Unit u) {
        return u.simRef != null && u.behavior != null && u.behavior.mode() == InstructionInterpreter.Mode.RETREAT_FIGHTING
                && net.hfstack.rallyguard.debug.sim.SimExtras.unitTracking();
    }

    /** F16e: how many of the unit's starting guards are in this second's owned-guard list. */
    private static int loadedOf(ServerLevel level, Unit u) {
        int n = 0;
        for (Guard g : net.hfstack.rallyguard.event.GuardStanceHandler.loaded(level.dimension())) {
            if (u.simIds.contains(g.getUUID())) n++;
        }
        return n;
    }

    /** F16e: the unit's size and ad-hoc set right after this step's directive (sums over the steps that got here). */
    private static void rfPost(ServerLevel level, Unit u) {
        if (!rfOn(u)) return;
        rf(u, "post");
        rfAdd(u, "membersPost", UnitOrders.members(level, u.key).size());
        rfAdd(u, "adhocPost", UnitOrders.adhocSize(u.key));
        rfAdd(u, "loadedPost", loadedOf(level, u));
    }

    /**
     * F16e (sim unitInstr route only): {@code members()} came back empty (F16d: after the 1st step, 9/9). Files WHY
     * for the unit's starting guards ({@code rf:why:alive|loaded|inSquad|pathMatch|ownerMatch}, counts of guards) and,
     * for a sim RETIRARSE unit ({@link #SIM_REAL_BOUNDS}), re-binds the living ones as an ad-hoc unit under the SAME
     * key (versions and directives carry on; {@link UnitOrders#unitKeyOf} prefers the ad-hoc binding).
     */
    private static List<Guard> simKeep(ServerLevel level, Unit u) {
        rf(u, "emptySeen");
        SquadRegistry squads = SquadRegistry.get(level);
        java.util.Set<UUID> loaded = new java.util.HashSet<>();
        for (Guard g : net.hfstack.rallyguard.event.GuardStanceHandler.loaded(level.dimension())) loaded.add(g.getUUID());
        List<Guard> alive = new ArrayList<>();
        for (UUID id : u.simIds) {
            if (!(level.getEntity(id) instanceof Guard g) || !g.isAlive()) continue;
            alive.add(g);
            rf(u, "why:alive");
            if (loaded.contains(id)) rf(u, "why:loaded");
            UUID owner = GuardOwnership.getOwner(g);
            if (u.group.owner.equals(owner)) rf(u, "why:ownerMatch");
            Optional<String> path = squads.pathOf(id);
            if (path.isPresent()) {
                rf(u, "why:inSquad");
                if (owner != null && u.key.equals(UnitOrders.unitKey(owner, path.get()))) rf(u, "why:pathMatch");
            }
        }
        if (alive.isEmpty() || !SIM_REAL_BOUNDS.contains(u.key)) return List.of();
        UnitOrders.bindAdhoc(u.key, alive);
        u.simRebound = true;
        rf(u, "rebound");
        return UnitOrders.members(level, u.key);
    }

    private static void rfAdd(Unit u, String what, long n) {
        if (u.simRef == null || u.behavior == null) return;
        // F27: the sim flee (retreat:flee -> RETREAT_TO_POINT) files the same counters (start, issued:FALLBACK, same,
        // end:*), or a per-unit flee has no proof of life. Telemetry only; rfOn (simKeep, sizes) stays RETREAT_FIGHTING.
        InstructionInterpreter.Mode mode = u.behavior.mode();
        if (mode != InstructionInterpreter.Mode.RETREAT_FIGHTING && mode != InstructionInterpreter.Mode.RETREAT_TO_POINT) return;
        net.hfstack.rallyguard.debug.sim.SimExtras.unitRf(u.simRef, "rf:" + what, n);
    }

    public static void forget(UUID guardId) {
        DOCTRINE.remove(guardId);
        HOLD_BACK.remove(guardId);
    }

    public static void clearAll() {
        for (Unit u : UNITS.values()) stop(u);
        UNITS.clear();
        DOCTRINE.clear();
        HOLD_BACK.clear();
        issued = 0;
        InstructionInterpreter.reset();
    }

    /** The subgroups currently under an instruction, for /rally debug and the card: "key=INSTR/FAMILY". */
    public static List<String> describe() {
        List<String> out = new ArrayList<>();
        for (Unit u : UNITS.values()) out.add(u.key + "=" + u.group.instruction + "/" + u.family + "/" + u.behavior.mode());
        return out;
    }

    /** Owner check helper for callers resolving a selection. */
    static boolean sameOwner(UUID owner, Guard g) {
        return owner.equals(GuardOwnership.getOwner(g));
    }
}
