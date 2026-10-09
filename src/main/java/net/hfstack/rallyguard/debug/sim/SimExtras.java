package net.hfstack.rallyguard.debug.sim;

import net.hfstack.rallyguard.command.GroupProfile;
import net.hfstack.rallyguard.command.InstructionInterpreter;
import net.hfstack.rallyguard.command.InstructionRunner;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * E3B block S - simulation v4 hooks, kept out of {@code SimRunner} so each E3B system adds its
 * scenarios here: {@code SimRunner} calls {@link #setup} after a run is organized,
 * {@link #sample} once a second during the fight, and {@link #finish} into the run record
 * ({@link SimRun#extra}). Tags:
 * <ul>
 *     <li>{@code profile} - a group-profile test: the blue side's consistency is measured at
 *     spawn and the run ends at once ({@code groupprofile-*});</li>
 *     <li>{@code instruction:NAME} - blue is given that general instruction (and is not driven
 *     at the enemy: the instruction decides) - {@code instruction-*}; metrics: mean range
 *     (meanEnemyDist), mean per-sample closest distance ({@code minEnemyDist}) and seconds with
 *     a blue unit within 2.5 blocks of a red one ({@code contactSeconds}).</li>
 * </ul>
 * Role {@code random} (a scrambled group) gives each guard a random kit and a 1-in-3 mount.
 */
public final class SimExtras {
    private SimExtras() {
    }

    private static SimScenario scenario;
    private static String instruction = "";
    private static double minDistSum, absMin;
    private static int minDistN, contactSamples;
    private static final Map<String, Double> EXTRA = new LinkedHashMap<>();

    /** Reset at the start of every run. */
    public static void begin(SimScenario s) {
        scenario = s;
        instruction = s.tag("instruction").toUpperCase(Locale.ROOT);
        minDistSum = 0;
        minDistN = 0;
        absMin = Double.NaN;
        contactSamples = 0;
        EXTRA.clear();
    }

    /** A profile-only scenario ends right after spawning. */
    public static boolean profileOnly(SimScenario s) {
        return s.tags().contains("profile");
    }

    /** Blue follows a general instruction: the simulation must not drive it at the enemy. */
    public static boolean blueInstructed() {
        return !instruction.isEmpty() || (scenario != null && scenario.tags().contains("nodrive"));
    }

    /** The kit of guard {@code index} in a "random" side: weapon role and whether it rides. */
    public static String[] randomKit(long seed, int index) {
        Random r = new Random(seed * 31L + index * 977L);
        String[] roles = {"melee", "archer", "crossbow", "axe", "spear", "fist", "shield"};
        String role = roles[r.nextInt(roles.length)];
        return new String[]{role, r.nextInt(3) == 0 ? "mounted" : "foot"};
    }

    /** After spawning and organizing: profile measures and instructions. */
    public static void setup(ServerLevel level, UUID blueOwner, List<Guard> blue, Vec3 blueAt, float blueYaw) {
        if (scenario == null || blue.isEmpty()) return;
        GroupProfile.Profile p = GroupProfile.of(blue);
        EXTRA.put("consistency", (double) p.consistency());
        EXTRA.put("identity", p.identity() ? 1.0D : 0.0D);
        EXTRA.put("kinds", (double) p.counts().size());
        if (!instruction.isEmpty()) {
            InstructionInterpreter.Instruction i = InstructionInterpreter.parse(instruction);
            if (i != null) {
                InstructionRunner.start(level, blueOwner, blue, i, blueAt, blueYaw);
                EXTRA.put("family", (double) p.family().ordinal());
            }
        }
    }

    /** Once a second: closest blue-red distance and contact. */
    public static void sample(List<LivingEntity> blue, List<LivingEntity> red) {
        if (scenario == null || instruction.isEmpty()) return;
        double best = Double.MAX_VALUE;
        for (LivingEntity b : blue) {
            if (!b.isAlive()) continue;
            for (LivingEntity r : red) {
                if (!r.isAlive()) continue;
                best = Math.min(best, b.distanceTo(r));
            }
        }
        if (best == Double.MAX_VALUE) return;
        minDistSum += best;
        minDistN++;
        absMin = Double.isNaN(absMin) ? best : Math.min(absMin, best);
        if (best <= 2.5D) contactSamples++;
    }

    // ==== Per-tick checks (E3B R3 and later); true = end the run now ====

    private static net.minecraft.world.phys.Vec3 startCentre;

    private static net.hfstack.rallyguard.contract.HqRegistry.Hq hq;
    private static ServerLevel hqLevel;

    public static boolean tick(ServerLevel level, net.minecraft.server.level.ServerPlayer player, int tick, int formTicks,
                               List<Guard> blue) {
        if (scenario == null) return false;
        if (scenario.tags().contains("hqremount") || scenario.tags().contains("relaxfollow")) {
            return hqTest(level, player, tick, formTicks, blue);
        }
        UUID owner = net.hfstack.rallyguard.debug.SimRunner.SIM_OWNER;
        String squad = net.hfstack.rallyguard.debug.SimRunner.SIM_SQUAD;
        if (scenario.tags().contains("split")) {
            // R3: split a unit of 10 into 5 + 5 without anyone dying -> 0 % casualties, no force retreat.
            if (tick == formTicks + 40) {
                net.hfstack.rallyguard.contract.SquadRegistry squads = net.hfstack.rallyguard.contract.SquadRegistry.get(level);
                for (int i = 0; i < blue.size(); i += 2) {
                    squads.assign(blue.get(i).getUUID(), owner, squad + net.hfstack.rallyguard.contract.SquadRegistry.PATH_SEPARATOR + "U2");
                }
            } else if (tick == formTicks + 160) {
                double worst = 0;
                for (String u : new String[]{"U1", "U2"}) {
                    net.hfstack.rallyguard.force.SituationReports.Report r = net.hfstack.rallyguard.force.SituationReports.last(
                            net.hfstack.rallyguard.force.UnitOrders.unitKey(owner, squad + net.hfstack.rallyguard.contract.SquadRegistry.PATH_SEPARATOR + u));
                    if (r != null) worst = Math.max(worst, r.casualties());
                }
                net.hfstack.rallyguard.contract.ForceRegistry.Force f = net.hfstack.rallyguard.contract.ForceRegistry.get(level).find(owner, squad);
                EXTRA.put("splitCasualties", worst);
                EXTRA.put("forceRetreat", f != null && net.hfstack.rallyguard.force.Operations.current(f)
                        == net.hfstack.rallyguard.force.Operations.Type.ORDERLY_RETREAT ? 1.0D : 0.0D);
                return true;
            }
        }
        if (scenario.tags().contains("retreatop")) {
            // R4 (B3): half the force dies -> orderly retreat; a player order cancels it and the HOLD_LINE comes back.
            net.hfstack.rallyguard.contract.ForceRegistry.Force f = net.hfstack.rallyguard.contract.ForceRegistry.get(level).find(owner, squad);
            if (f == null) return false;
            if (tick == formTicks + 40) {
                for (int i = 0; i < blue.size() / 2; i++) blue.get(i).kill();
            } else if (tick == formTicks + 120) {
                EXTRA.put("retreatStarted", net.hfstack.rallyguard.force.Operations.current(f)
                        == net.hfstack.rallyguard.force.Operations.Type.ORDERLY_RETREAT ? 1.0D : 0.0D);
                net.hfstack.rallyguard.force.UnitOrders.notePlayerOrder(blue);
            } else if (tick == formTicks + 180) {
                EXTRA.put("opRestored", net.hfstack.rallyguard.force.Operations.current(f)
                        == net.hfstack.rallyguard.force.Operations.Type.HOLD_LINE ? 1.0D : 0.0D);
                return true;
            }
        }
        if (scenario.tags().contains("plancancel")) {
            // R4 (B4): a cancelled plan's "retreat at 20 %" must not leak into a new plan with "retreat at 50 %".
            net.hfstack.rallyguard.contract.ForceRegistry.Force f = net.hfstack.rallyguard.contract.ForceRegistry.get(level).find(owner, squad);
            if (f == null || blue.isEmpty()) return false;
            Vec3 c = InstructionRunner.centre(blue);
            String phase = String.format(Locale.ROOT, "{\"objective\":\"HOLD\",\"x\":%.1f,\"z\":%.1f}", c.x, c.z);
            if (tick == formTicks + 20) {
                net.hfstack.rallyguard.force.Plans.execute(level, f, net.hfstack.rallyguard.force.Plans.parse(
                        "{\"name\":\"a\",\"force\":\"" + squad + "\",\"retreatAt\":0.2,\"phases\":[" + phase + "]}"));
            } else if (tick == formTicks + 40) {
                for (int i = 0; i < 3 && i < blue.size(); i++) blue.get(i).kill();
            } else if (tick == formTicks + 100) {
                EXTRA.put("firstRetreat", net.hfstack.rallyguard.force.Operations.current(f)
                        == net.hfstack.rallyguard.force.Operations.Type.ORDERLY_RETREAT ? 1.0D : 0.0D);
                net.hfstack.rallyguard.force.Plans.cancel(f);
                net.hfstack.rallyguard.force.Plans.execute(level, f, net.hfstack.rallyguard.force.Plans.parse(
                        "{\"name\":\"b\",\"force\":\"" + squad + "\",\"retreatAt\":0.5,\"phases\":[" + phase + "]}"));
            } else if (tick == formTicks + 180) {
                EXTRA.put("inheritedRetreat", net.hfstack.rallyguard.force.Operations.current(f)
                        == net.hfstack.rallyguard.force.Operations.Type.ORDERLY_RETREAT ? 1.0D : 0.0D);
                return true;
            }
        }
        if (scenario.tags().contains("freshcentre") && !blue.isEmpty()) {
            // R3: a unit that marches 40 blocks without losing health still reports where it is.
            String key = net.hfstack.rallyguard.force.UnitOrders.unitKey(owner, squad + net.hfstack.rallyguard.contract.SquadRegistry.PATH_SEPARATOR + "U1");
            if (tick == formTicks + 20) {
                startCentre = InstructionRunner.centre(blue);
                net.hfstack.rallyguard.force.UnitOrders.issue(level, key, net.hfstack.rallyguard.force.UnitOrders.Kind.MOVE,
                        net.hfstack.rallyguard.force.UnitOrders.Source.FORCE, startCentre.add(40, 0, 0), 180.0F,
                        net.hfstack.rallyguard.formation.FormationShape.COLUMN, null, 0, "sim fresh centre");
            } else if (tick == formTicks + 500) {
                net.hfstack.rallyguard.force.SituationReports.Report r = net.hfstack.rallyguard.force.SituationReports.last(key);
                Vec3 real = InstructionRunner.centre(blue);
                double err = r == null || r.centre() == null ? 99 : Math.sqrt(Math.pow(r.centre().x - real.x, 2) + Math.pow(r.centre().z - real.z, 2));
                EXTRA.put("reportCentreError", err);
                EXTRA.put("unitMoved", startCentre == null ? 0 : Math.sqrt(Math.pow(real.x - startCentre.x, 2) + Math.pow(real.z - startCentre.z, 2)));
                return true;
            }
        }
        return false;
    }

    /**
     * R5: an HQ at blue's spot. {@code hqremount}: mounted guards rest there 3 min (parked), are sent
     * 30 blocks away and must ride again. {@code relaxfollow}: relaxed guards told to Follow must not
     * walk back to the HQ (checked after 70 s, past the 30-60 s stroll timer).
     */
    private static boolean hqTest(ServerLevel level, net.minecraft.server.level.ServerPlayer player, int tick, int formTicks,
                                  List<Guard> blue) {
        if (blue.isEmpty()) return false;
        UUID owner = net.hfstack.rallyguard.debug.SimRunner.SIM_OWNER;
        String squad = net.hfstack.rallyguard.debug.SimRunner.SIM_SQUAD;
        boolean remount = scenario.tags().contains("hqremount");
        int t = tick - formTicks;
        if (t == 10) {
            Vec3 c = InstructionRunner.centre(blue);
            net.minecraft.core.BlockPos pos = net.minecraft.core.BlockPos.containing(c);
            hq = net.hfstack.rallyguard.contract.HqRegistry.get(level).create(owner, "SimHQ", level.dimension().location().toString(), pos);
            hqLevel = level;
            net.hfstack.rallyguard.contract.SquadRegistry.get(level).setHqId(owner, squad, hq.id);
            for (Guard g : blue) net.hfstack.rallyguard.order.GuardModes.postAndMove(g, c, 1.0D);
        }
        if (hq == null) return false;
        if (remount) {
            if (t == 10 + 3600) {
                int parked = 0;
                for (Guard g : blue) if (!g.isPassenger()) parked++;
                EXTRA.put("parkedShare", (double) parked / blue.size());
                for (Guard g : blue) net.hfstack.rallyguard.order.GuardModes.postAndMove(g, g.position().add(30, 0, 0), 1.0D);
            } else if (t == 10 + 3600 + 600) {
                int riding = 0;
                for (Guard g : blue) if (g.isPassenger()) riding++;
                EXTRA.put("remountedShare", (double) riding / blue.size());
                return true;
            }
        } else {
            if (t == 200) {
                int relaxed = 0;
                for (Guard g : blue) if (net.hfstack.rallyguard.order.HqRelax.isRelaxed(g)) relaxed++;
                EXTRA.put("relaxedShare", (double) relaxed / blue.size());
                if (player != null) net.hfstack.rallyguard.order.FormationFollow.order(player, blue);
            } else if (t == 200 + 1400) {
                double r = net.hfstack.rallyguard.config.RallyConfig.i(net.hfstack.rallyguard.config.RallyConfig.HQ_RADIUS, 16);
                int away = 0;
                for (Guard g : blue) {
                    boolean home = g.distanceToSqr(Vec3.atCenterOf(hq.pos)) <= r * r;
                    if (!home && !net.hfstack.rallyguard.order.HqRelax.isRelaxed(g)) away++;
                }
                EXTRA.put("stayedWithCommander", (double) away / blue.size());
                if (player != null) for (Guard g : blue) net.hfstack.rallyguard.order.FormationFollow.leave(g.getUUID());
                return true;
            }
        }
        return false;
    }

    /** Named SimProbe counters copied for blue (E3B R1 and later blocks). */
    private static final String[] COUNTERS = {"terrainReads", "coreDecisions", "outnumberedRetreats", "rungChanges"};

    public static void finish(SimRun r) {
        if (hq != null && hqLevel != null) {
            // the test HQ goes with the run (it would otherwise stay in the simulation commander's list)
            net.hfstack.rallyguard.contract.HqRegistry.get(hqLevel).delete(hq);
        }
        hq = null;
        hqLevel = null;
        for (String c : COUNTERS) EXTRA.put(c, (double) net.hfstack.rallyguard.debug.SimProbe.counted(c, 0));
        double minutes = Math.max(1, r.ticks) / 1200.0D;
        if (r.start[0] > 0) EXTRA.put("rungChangesPerGuardMinute", EXTRA.get("rungChanges") / r.start[0] / minutes);
        int unstuck = net.hfstack.rallyguard.tactics.DecisionLadder.Rung.UNSTUCK.ordinal();
        EXTRA.put("unstuckDecisions", (double) (unstuck < r.ladder.length ? r.ladder[unstuck] : 0));
        int cover = net.hfstack.rallyguard.tactics.DecisionLadder.Rung.TAKE_COVER.ordinal();
        int position = net.hfstack.rallyguard.tactics.DecisionLadder.Rung.TAKE_POSITION.ordinal();
        EXTRA.put("coverDecisions", (double) ((cover < r.ladder.length ? r.ladder[cover] : 0)
                + (position < r.ladder.length ? r.ladder[position] : 0)));
        if (minDistN > 0) {
            EXTRA.put("minEnemyDist", minDistSum / minDistN);
            EXTRA.put("absMinEnemyDist", absMin);
            EXTRA.put("contactSeconds", (double) contactSamples);
        }
        r.extra.putAll(EXTRA);
        scenario = null;
        instruction = "";
    }
}
