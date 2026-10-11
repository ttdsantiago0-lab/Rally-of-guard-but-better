package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.command.InstructionRunner;
import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.debug.SimProbe;
import net.hfstack.rallyguard.formation.FormationLayout;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E3B P1 ({@code docs/BATTLE_PLANNER_V2.md} Pilar 1) - the complex instructions of a plan, all written
 * in capabilities and carried out through the same versioned unit directives ({@link UnitOrders},
 * source PLAN) - no parallel system:
 * <ul>
 *     <li><b>FILL_POLICY</b> (plan level, {@link #fillPolicy}) - a unit that falls below half strength,
 *     routs or vanishes leaves a GAP with its post and role; the substitute is the next of its chain
 *     ("if A falls: B; if B falls: C"), else the best candidate by (capability that covers the lost
 *     one) → (lowest importance, so nothing valuable is stripped) → (nearest) → (reserve first). A
 *     substitute that falls is replaced the same way. Generalizes RELIEF.</li>
 *     <li><b>AMBUSH</b> (from A in contact, to B the ambusher) - B waits HIDDEN at its wait point
 *     (doctrine AMBUSH, only answering whoever hits it, far from known enemies; an enemy within 14
 *     blocks "discovers" it and it strikes at once); on the trigger (A in contact N s, a signal...) it
 *     takes the enemy position from A's REPORT (B never saw it), goes round to the flank or rear
 *     ({@code side} LEFT / RIGHT / REAR) and charges. Hiding is an approximation (distance and
 *     restraint), not invisibility.</li>
 *     <li><b>OPEN_LANE</b> (from X the formation, to Y the unit passing) - X's layout gets a lane
 *     ({@code lateral}, {@code width}); Y marches through it; when Y is past X's line (or after
 *     {@code close} s) the lane closes.</li>
 *     <li><b>COUNTERATTACK</b> (to the reserve) - released when the enemy overextends (its known centre
 *     advanced {@code over} blocks toward us), engages {@code share} of our units, or on the trigger;
 *     it strikes the weakest known enemy group.</li>
 *     <li><b>DELAY</b> (from the main body, to the rearguard) - the body withdraws to the HQ at a quick
 *     pace while the rearguard harasses and falls back {@code steps} times every {@code every} s.
 *     Generalizes STAGGERED_RETREAT.</li>
 *     <li>Experimental: <b>PINCER</b> (A and B close from opposite sides and strike together),
 *     <b>MOVING_SCREEN</b> (the unit stays between the enemy and the HQ), <b>SIGNAL_RESERVE</b> (the
 *     reserve goes in on the commander's signal).</li>
 * </ul>
 * TEMPO (LENTO/NORMAL/RAPIDO/ASALTO) multiplies the navigation speed REQUESTED by a unit's orders
 * ({@link UnitOrders#setTempo}); an assault pace costs cohesion.
 */
final class PlanV2 {
    private PlanV2() {
    }

    /** Per-link state: phase of its little state machine, since when, a remembered point, steps done. */
    static final class LinkState {
        int phase;
        long since;
        @Nullable Vec3 point;
        int steps;
        long lastStep;
        boolean done;
    }

    static boolean handles(Plans.LinkType t) {
        return switch (t) {
            case AMBUSH, OPEN_LANE, COUNTERATTACK, DELAY, PINCER, MOVING_SCREEN, SIGNAL_RESERVE -> true;
            default -> false;
        };
    }

    // ==== Phase: tempo and layout ====

    static void enterPhase(ServerLevel level, ForceRegistry.Force force, Plans.Active a, Plans.Phase p, List<Forces.UnitView> units) {
        Plans.Tempo phaseTempo = Plans.Tempo.parse(p.tempo());
        FormationLayout layout = p.layout().isEmpty() ? null : FormationLayout.parse(p.layout());
        for (Forces.UnitView u : units) {
            String key = UnitOrders.unitKey(force.owner, u.path());
            String own = a.plan.tempo().get(u.path());
            if (own == null) own = a.plan.tempo().get(u.name());
            UnitOrders.setTempo(key, (own != null ? Plans.Tempo.parse(own) : phaseTempo).factor);
            if (layout != null) FormationRegistry.get(level).setUnitLayout(force.owner, u.path(), layout);
        }
    }

    // ==== FILL_POLICY ====

    static int importanceOf(Plans.Plan plan, Forces.UnitView u, String role) {
        Integer v = plan.importance().get(u.path());
        if (v == null) v = plan.importance().get(u.name());
        if (v != null) return v;
        return switch (role) {
            case "ANVIL", "SUPPORT" -> 80;
            case "RESERVE" -> 20;
            default -> 50;
        };
    }

    private static Capability mainCapability(EnumSet<Capability> caps) {
        if (caps.contains(Capability.MELEE_LINE)) return Capability.MELEE_LINE;
        if (caps.contains(Capability.RANGED_SUPPORT)) return Capability.RANGED_SUPPORT;
        if (caps.contains(Capability.MOUNTED_CHARGE)) return Capability.MOUNTED_CHARGE;
        return Capability.HOLD_POSITION;
    }

    static void fillPolicy(ServerLevel level, ForceRegistry.Force force, Plans.Active a, List<Forces.UnitView> units,
                           Map<String, String> roles, Map<String, SituationReports.Report> reports, long now) {
        Map<String, Forces.UnitView> present = new java.util.HashMap<>();
        for (Forces.UnitView u : units) present.put(u.path(), u);
        List<String> gaps = new ArrayList<>();
        for (Map.Entry<String, Vec3> seen : new ArrayList<>(a.lastCentre.entrySet())) {
            if (!present.containsKey(seen.getKey()) && !a.filled.containsKey(seen.getKey())) gaps.add(seen.getKey()); // vanished
        }
        for (Forces.UnitView u : units) {
            SituationReports.Report r = reports.get(u.path());
            if (r == null) continue;
            if (r.centre() != null) a.lastCentre.put(u.path(), r.centre());
            boolean gap = r.casualties() >= 0.5D || r.routedShare() >= 0.5D;
            // A substitute that falls leaves a gap too (it is replaced the same way, by its own chain or the best candidate).
            if (gap && !a.filled.containsKey(u.path())) gaps.add(u.path());
        }
        for (String lost : gaps) {
            Forces.UnitView lostView = present.get(lost);
            Vec3 post = a.lastCentre.get(lost);
            if (post == null) continue;
            EnumSet<Capability> lostCaps = lostView != null ? lostView.capabilities() : EnumSet.of(Capability.HOLD_POSITION);
            Capability need = mainCapability(lostCaps);
            Forces.UnitView sub = null;
            for (String c : a.plan.fill().getOrDefault(lost, List.of())) { // the chain first
                Forces.UnitView cand = present.get(c);
                if (cand == null) cand = byName(present, c);
                if (cand != null && usable(a, cand, reports)) {
                    sub = cand;
                    break;
                }
            }
            if (sub == null) {
                List<Forces.UnitView> cands = new ArrayList<>();
                for (Forces.UnitView u : units) if (!u.path().equals(lost) && usable(a, u, reports)) cands.add(u);
                cands.sort(Comparator.comparingInt((Forces.UnitView u) -> u.capabilities().contains(need) ? 0 : 1)
                        .thenComparingInt(u -> importanceOf(a.plan, u, roles.getOrDefault(u.path(), "")))
                        .thenComparingDouble(u -> {
                            SituationReports.Report r = reports.get(u.path());
                            return r == null || r.centre() == null ? 1.0E9 : r.centre().distanceToSqr(post);
                        })
                        .thenComparingInt(u -> "RESERVE".equals(roles.get(u.path())) ? 0 : 1));
                if (!cands.isEmpty()) sub = cands.get(0);
            }
            a.filled.put(lost, sub == null ? "" : sub.path());
            if (sub == null) continue;
            String lostRole = force.units.getOrDefault(lostView == null ? lost.substring(lost.indexOf('/') + 1) : lostView.name(),
                    roles.getOrDefault(lost, ""));
            if (!lostRole.isEmpty()) {
                force.units.put(sub.name(), lostRole);
                net.hfstack.rallyguard.contract.ForceRegistry.get(level).changed();
            }
            SituationReports.Report lr = reports.get(lost);
            float facing = Operations.facing(post, lr == null ? null : lr.enemyCentre());
            UnitOrders.issue(level, UnitOrders.unitKey(force.owner, sub.path()), UnitOrders.Kind.HOLD, UnitOrders.Source.PLAN,
                    UnitOrders.ground(level, post), facing, FormationShape.LINE, null, 0, "plan fill " + lost);
            for (Guard g : sub.members()) SimProbe.count(g, "fills");
        }
    }

    private static boolean usable(Plans.Active a, Forces.UnitView u, Map<String, SituationReports.Report> reports) {
        // not a lost unit, and not already standing in for another one
        if (u.members().isEmpty() || a.filled.containsKey(u.path()) || a.filled.containsValue(u.path())) return false;
        SituationReports.Report r = reports.get(u.path());
        return r == null || (r.casualties() < 0.5D && r.routedShare() < 0.5D);
    }

    @Nullable
    private static Forces.UnitView byName(Map<String, Forces.UnitView> present, String name) {
        for (Forces.UnitView u : present.values()) if (u.name().equalsIgnoreCase(name) || u.path().equalsIgnoreCase(name)) return u;
        return null;
    }

    // ==== The new links ====

    /** One second of a v2 link; true if it issued something (the destination is busy this second). */
    static boolean step(ServerLevel level, ForceRegistry.Force force, Plans.Active a, Plans.Link link,
                        @Nullable Forces.UnitView from, Forces.UnitView to, Map<String, SituationReports.Report> reports, long now) {
        String id = link.type() + ">" + link.from() + ">" + link.to();
        LinkState st = a.linkStates.computeIfAbsent(id, k -> new LinkState());
        if (st.done) return false;
        SituationReports.Report fr = from == null ? null : reports.get(from.path());
        SituationReports.Report tr = reports.get(to.path());
        String toKey = UnitOrders.unitKey(force.owner, to.path());
        return switch (link.type()) {
            case AMBUSH -> ambush(level, force, a, link, st, fr, tr, to, toKey, now);
            case OPEN_LANE -> lane(level, force, a, link, st, from, fr, tr, toKey, now);
            case COUNTERATTACK -> counter(level, force, a, link, st, reports, tr, toKey, now);
            case DELAY -> delay(level, force, a, link, st, from, fr, tr, to, toKey, now);
            case PINCER -> pincer(level, force, a, link, st, from, fr, tr, toKey, now);
            case MOVING_SCREEN -> screen(level, force, link, st, tr, to, toKey, now);
            case SIGNAL_RESERVE -> signalReserve(level, force, a, link, st, tr, toKey, now);
            default -> false;
        };
    }

    /** The link's own trigger (NONE = "when the source unit is in contact"). */
    private static boolean fired(Plans.Active a, Plans.Link link, @Nullable SituationReports.Report fr, long now) {
        return switch (link.trigger()) {
            case NONE -> fr != null && fr.contactSince() >= 0;
            case TIME -> (now - a.phaseStart) / 20.0D >= link.value();
            case CASUALTIES -> fr != null && fr.casualties() >= link.value();
            case CONTACT -> fr != null && fr.contactSince() >= 0 && fr.contactSeconds(now) >= link.value();
            case ENEMY_SEEN -> fr != null && fr.enemiesSeen() > 0;
            case SIGNAL -> a.signals.contains((int) link.value());
        };
    }

    @Nullable
    private static Vec3 enemyFor(ForceRegistry.Force force, @Nullable SituationReports.Report near, long now) {
        if (near != null && near.enemyCentre() != null) return near.enemyCentre();
        List<SituationReports.KnownEnemy> known = SituationReports.knownEnemies(ForceBrain.forceKey(force), now);
        if (known.isEmpty()) return null;
        if (near == null || near.centre() == null) return known.get(known.size() - 1).centre();
        SituationReports.KnownEnemy best = null;
        for (SituationReports.KnownEnemy k : known) {
            if (best == null || k.centre().distanceToSqr(near.centre()) < best.centre().distanceToSqr(near.centre())) best = k;
        }
        return best.centre();
    }

    private static boolean ambush(ServerLevel level, ForceRegistry.Force force, Plans.Active a, Plans.Link link, LinkState st,
                                  @Nullable SituationReports.Report fr, @Nullable SituationReports.Report tr, Forces.UnitView to,
                                  String toKey, long now) {
        Vec3 here = tr == null ? null : tr.centre();
        if (here == null) return false;
        if (st.phase == 0) { // go to the wait point, hidden
            Vec3 wait = link.params().containsKey("wx") ? new Vec3(link.num("wx", here.x), here.y, link.num("wz", here.z))
                    : link.x() != 0 || link.z() != 0 ? new Vec3(link.x(), here.y, link.z()) : here;
            UnitOrders.issue(level, toKey, UnitOrders.Kind.HOLD, UnitOrders.Source.PLAN, UnitOrders.ground(level, wait), 0,
                    FormationShape.COLUMN, FireMode.STILL, 0, "plan ambush wait", FormationLayout.DEFAULT.withDensity(
                            FormationLayout.Density.CERRADA), 1.0D);
            InstructionRunner.holdBack(to.members(), true);
            InstructionRunner.overrideDoctrine(to.members(), Doctrine.AMBUSH);
            st.phase = 1;
            st.since = now;
            return true;
        }
        if (st.phase == 1) { // waiting: discovered, or the trigger
            boolean discovered = false;
            for (SituationReports.KnownEnemy k : SituationReports.knownEnemies(ForceBrain.forceKey(force), now)) {
                if (k.tick() >= a.start && k.centre().distanceToSqr(here) < 14 * 14) discovered = true;
            }
            if (!discovered && !fired(a, link, fr, now)) return false;
            Vec3 enemy = enemyFor(force, fr, now);
            if (enemy == null) return false;
            Vec3 axis = flat(enemy.subtract(fr != null && fr.centre() != null ? fr.centre() : here));
            String side = link.str("side", "REAR").toUpperCase(Locale.ROOT);
            Vec3 left = new Vec3(axis.z, 0, -axis.x);
            Vec3 approach = switch (side) {
                case "LEFT" -> enemy.add(left.scale(12));
                case "RIGHT" -> enemy.subtract(left.scale(12));
                default -> enemy.add(axis.scale(12));
            };
            InstructionRunner.holdBack(to.members(), false);
            st.point = enemy;
            UnitOrders.issue(level, toKey, UnitOrders.Kind.MOVE, UnitOrders.Source.PLAN, UnitOrders.ground(level, approach),
                    Operations.facing(approach, enemy), FormationShape.COLUMN, null, 0, "plan ambush approach", null,
                    Plans.Tempo.RAPIDO.factor);
            st.phase = 2;
            st.since = now;
            for (Guard g : to.members()) SimProbe.count(g, discovered ? "ambushDiscovered" : "ambushSprung");
            return true;
        }
        if (st.phase == 2 && (now - st.since > 200 || (st.point != null && here.distanceToSqr(st.point) < 16 * 16))) {
            Vec3 enemy = enemyFor(force, fr, now);
            if (enemy == null) enemy = st.point;
            if (enemy == null) return false;
            UnitOrders.issue(level, toKey, UnitOrders.Kind.ATTACK_AREA, UnitOrders.Source.PLAN, enemy, Operations.facing(here, enemy),
                    FormationShape.V, null, 16.0D, "plan ambush strike", null, Plans.Tempo.ASALTO.factor);
            InstructionRunner.overrideDoctrine(to.members(), null);
            st.phase = 3;
            st.done = true;
            return true;
        }
        return false;
    }

    private static boolean lane(ServerLevel level, ForceRegistry.Force force, Plans.Active a, Plans.Link link, LinkState st,
                                @Nullable Forces.UnitView from, @Nullable SituationReports.Report fr,
                                @Nullable SituationReports.Report tr, String toKey, long now) {
        if (from == null || fr == null || fr.centre() == null || tr == null || tr.centre() == null) return false;
        String fromKey = UnitOrders.unitKey(force.owner, from.path());
        UnitOrders.UnitOrder xo = UnitOrders.current(fromKey);
        float facing = xo != null ? xo.facingYaw() : Operations.facing(fr.centre(), fr.enemyCentre());
        Vec3 anchor = xo != null && xo.anchor() != null ? xo.anchor() : fr.centre();
        Vec3 fwd = Operations.forward(facing);
        Vec3 right = new Vec3(-fwd.z, 0, fwd.x);
        double lateral = link.num("lateral", 0), width = link.num("width", 4);
        if (st.phase == 0) {
            boolean go = link.trigger() == Plans.Trigger.NONE || fired(a, link, fr, now);
            if (!go) return false;
            FormationLayout base = FormationRegistry.get(level).unitLayout(force.owner, from.path());
            FormationLayout open = base.withoutGaps().withGap(new FormationLayout.Gap(lateral, width));
            FormationRegistry.get(level).setUnitLayout(force.owner, from.path(), open);
            reissue(level, fromKey, xo, anchor, facing, open, "plan lane open");
            Vec3 beyond = anchor.add(fwd.scale(14)).add(right.scale(lateral));
            UnitOrders.issue(level, toKey, UnitOrders.Kind.MOVE, UnitOrders.Source.PLAN, UnitOrders.ground(level, beyond), facing,
                    FormationShape.COLUMN, null, 0, "plan lane pass", null, 1.0D);
            st.phase = 1;
            st.since = now;
            return true;
        }
        if (st.phase == 1) {
            boolean past = tr.centre().subtract(anchor).dot(fwd) > 4.0D;
            if (!past && now - st.since < 20L * (long) link.num("close", 20)) return false;
            FormationLayout closed = FormationRegistry.get(level).unitLayout(force.owner, from.path()).withoutGaps();
            FormationRegistry.get(level).setUnitLayout(force.owner, from.path(), closed);
            reissue(level, fromKey, UnitOrders.current(fromKey), anchor, facing, closed, "plan lane closed");
            st.done = true;
            for (Guard g : from.members()) SimProbe.count(g, past ? "lanePassed" : "laneTimedOut");
            return true;
        }
        return false;
    }

    /** X keeps doing what it did, with the new layout (a lane opened or closed). */
    private static void reissue(ServerLevel level, String key, @Nullable UnitOrders.UnitOrder o, Vec3 anchor, float facing,
                                FormationLayout layout, String why) {
        UnitOrders.Kind kind = o == null || o.anchor() == null ? UnitOrders.Kind.HOLD : o.kind();
        if (kind != UnitOrders.Kind.HOLD && kind != UnitOrders.Kind.MOVE && kind != UnitOrders.Kind.FALLBACK) kind = UnitOrders.Kind.HOLD;
        UnitOrders.issue(level, key, kind, UnitOrders.Source.PLAN, anchor, facing, o == null ? FormationShape.LINE : o.shape(),
                o == null ? null : o.style(), 0, why, layout, 1.0D);
    }

    private static boolean counter(ServerLevel level, ForceRegistry.Force force, Plans.Active a, Plans.Link link, LinkState st,
                                   Map<String, SituationReports.Report> reports, @Nullable SituationReports.Report tr,
                                   String toKey, long now) {
        List<SituationReports.KnownEnemy> known = new ArrayList<>();
        for (SituationReports.KnownEnemy k : SituationReports.knownEnemies(ForceBrain.forceKey(force), now)) {
            if (k.tick() >= a.start) known.add(k);
        }
        if (known.isEmpty()) return false;
        Vec3 enemyNow = known.get(known.size() - 1).centre();
        if (st.point == null) st.point = known.get(0).centre(); // where the enemy first stood
        Vec3 ours = null;
        int units = 0, inContact = 0;
        double x = 0, z = 0;
        for (SituationReports.Report r : reports.values()) {
            if (r.centre() == null) continue;
            units++;
            x += r.centre().x;
            z += r.centre().z;
            if (r.contactSince() >= 0) inContact++;
        }
        if (units > 0) ours = new Vec3(x / units, enemyNow.y, z / units);
        boolean overextended = ours != null && st.point.distanceTo(ours) - enemyNow.distanceTo(ours) >= link.num("over", 10);
        boolean engaged = units > 0 && (double) inContact / units >= link.num("share", 0.5);
        boolean manual = link.trigger() != Plans.Trigger.NONE && fired(a, link, null, now);
        if (!overextended && !engaged && !manual) return false;
        SituationReports.KnownEnemy weakest = known.get(0);
        for (SituationReports.KnownEnemy k : known) if (now - k.tick() < 400 && k.count() < weakest.count()) weakest = k;
        UnitOrders.issue(level, toKey, UnitOrders.Kind.ATTACK_AREA, UnitOrders.Source.PLAN, weakest.centre(),
                tr == null || tr.centre() == null ? 0 : Operations.facing(tr.centre(), weakest.centre()), FormationShape.V, null, 16.0D,
                "plan counterattack", null, Plans.Tempo.ASALTO.factor);
        st.done = true;
        return true;
    }

    private static boolean delay(ServerLevel level, ForceRegistry.Force force, Plans.Active a, Plans.Link link, LinkState st,
                                 @Nullable Forces.UnitView from, @Nullable SituationReports.Report fr,
                                 @Nullable SituationReports.Report tr, Forces.UnitView to, String toKey, long now) {
        if (tr == null || tr.centre() == null) return false;
        if (st.phase == 0) {
            if (link.trigger() != Plans.Trigger.NONE && !fired(a, link, fr, now)) return false;
            if (from != null) {
                UnitOrders.issue(level, UnitOrders.unitKey(force.owner, from.path()), UnitOrders.Kind.RETREAT_HQ, UnitOrders.Source.PLAN,
                        null, 0, FormationShape.COLUMN, null, 0, "plan delay: main body", null, Plans.Tempo.RAPIDO.factor);
            }
            InstructionRunner.overrideDoctrine(to.members(), Doctrine.HARASS);
            st.phase = 1;
            st.lastStep = now - 1000;
        }
        int steps = (int) link.num("steps", 4);
        long every = 20L * (long) Math.max(3, link.num("every", 8));
        if (now - st.lastStep < every) return st.phase == 1 && st.steps == 0;
        st.lastStep = now;
        if (st.steps >= steps) {
            UnitOrders.issue(level, toKey, UnitOrders.Kind.RETREAT_HQ, UnitOrders.Source.PLAN, null, 0, FormationShape.LINE,
                    null, 0, "plan delay: rearguard home", null, Plans.Tempo.RAPIDO.factor);
            InstructionRunner.overrideDoctrine(to.members(), null);
            st.done = true;
            return true;
        }
        st.steps++;
        Vec3 enemy = enemyFor(force, tr, now);
        Vec3 back = Operations.away(tr.centre(), enemy, link.num("dist", 8));
        UnitOrders.issue(level, toKey, UnitOrders.Kind.FALLBACK, UnitOrders.Source.PLAN, UnitOrders.ground(level, back),
                Operations.facing(back, enemy), FormationShape.LINE, FireMode.RETREATING, 0, "plan delay step " + st.steps, null, 1.0D);
        for (Guard g : to.members()) SimProbe.count(g, "delaySteps");
        return true;
    }

    private static boolean pincer(ServerLevel level, ForceRegistry.Force force, Plans.Active a, Plans.Link link, LinkState st,
                                  @Nullable Forces.UnitView from, @Nullable SituationReports.Report fr,
                                  @Nullable SituationReports.Report tr, String toKey, long now) {
        if (from == null || fr == null || tr == null || fr.centre() == null || tr.centre() == null) return false;
        String fromKey = UnitOrders.unitKey(force.owner, from.path());
        Vec3 enemy = enemyFor(force, fr, now);
        if (enemy == null) enemy = enemyFor(force, tr, now);
        if (enemy == null) return false;
        Vec3 axis = flat(enemy.subtract(fr.centre().add(tr.centre()).scale(0.5)));
        Vec3 left = new Vec3(axis.z, 0, -axis.x);
        Vec3 pa = enemy.add(left.scale(12)), pb = enemy.subtract(left.scale(12));
        if (st.phase == 0) {
            if (link.trigger() != Plans.Trigger.NONE && !fired(a, link, fr, now)) return false;
            UnitOrders.issue(level, fromKey, UnitOrders.Kind.MOVE, UnitOrders.Source.PLAN, UnitOrders.ground(level, pa),
                    Operations.facing(pa, enemy), FormationShape.COLUMN, null, 0, "plan pincer A", null, Plans.Tempo.RAPIDO.factor);
            UnitOrders.issue(level, toKey, UnitOrders.Kind.MOVE, UnitOrders.Source.PLAN, UnitOrders.ground(level, pb),
                    Operations.facing(pb, enemy), FormationShape.COLUMN, null, 0, "plan pincer B", null, Plans.Tempo.RAPIDO.factor);
            st.phase = 1;
            st.since = now;
            return true;
        }
        boolean both = fr.centre().distanceToSqr(pa) < 49 && tr.centre().distanceToSqr(pb) < 49;
        if (st.phase == 1 && (both || now - st.since > 20L * (long) link.num("wait", 20))) {
            for (String key : new String[]{fromKey, toKey}) {
                UnitOrders.issue(level, key, UnitOrders.Kind.ATTACK_AREA, UnitOrders.Source.PLAN, enemy, 0, FormationShape.V, null,
                        16.0D, "plan pincer strike", null, Plans.Tempo.ASALTO.factor);
            }
            st.done = true;
            return true;
        }
        return false;
    }

    private static boolean screen(ServerLevel level, ForceRegistry.Force force, Plans.Link link, LinkState st,
                                  @Nullable SituationReports.Report tr, Forces.UnitView to, String toKey, long now) {
        if (tr == null || tr.centre() == null || to.members().isEmpty()) return false;
        BlockPos hq = net.hfstack.rallyguard.order.HqRelax.hqOf(level, to.members().get(0));
        Vec3 enemy = enemyFor(force, tr, now);
        if (hq == null || enemy == null) return false;
        Vec3 home = Vec3.atBottomCenterOf(hq);
        double d = Math.sqrt(home.distanceToSqr(enemy));
        Vec3 spot = home.add(flat(enemy.subtract(home)).scale(Math.min(d / 2.0D, link.num("max", 20))));
        if (st.point != null && st.point.distanceToSqr(spot) < 16) return false;
        st.point = spot;
        UnitOrders.issue(level, toKey, UnitOrders.Kind.HOLD, UnitOrders.Source.PLAN, UnitOrders.ground(level, spot),
                Operations.facing(spot, enemy), FormationShape.LINE, null, 0, "plan moving screen", null, 1.0D);
        return true;
    }

    private static boolean signalReserve(ServerLevel level, ForceRegistry.Force force, Plans.Active a, Plans.Link link, LinkState st,
                                         @Nullable SituationReports.Report tr, String toKey, long now) {
        if (!a.signals.contains((int) link.value())) return false;
        Vec3 target = enemyFor(force, tr, now);
        if (target == null && (link.x() != 0 || link.z() != 0)) target = new Vec3(link.x(), tr == null || tr.centre() == null ? 64 : tr.centre().y, link.z());
        if (target == null) return false;
        UnitOrders.issue(level, toKey, UnitOrders.Kind.ATTACK_AREA, UnitOrders.Source.PLAN, target, 0, FormationShape.V, null, 16.0D,
                "plan reserve on signal", null, Plans.Tempo.RAPIDO.factor);
        st.done = true;
        return true;
    }

    private static Vec3 flat(Vec3 v) {
        Vec3 f = new Vec3(v.x, 0, v.z);
        return f.lengthSqr() < 1.0E-6 ? new Vec3(0, 0, 1) : f.normalize();
    }
}
