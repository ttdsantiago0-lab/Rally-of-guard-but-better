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
        return !instruction.isEmpty();
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

    /** Named SimProbe counters copied for blue (E3B R1 and later blocks). */
    private static final String[] COUNTERS = {"terrainReads", "coreDecisions", "outnumberedRetreats", "rungChanges"};

    public static void finish(SimRun r) {
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
