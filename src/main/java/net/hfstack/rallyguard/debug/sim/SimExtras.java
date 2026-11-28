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

    /** True while a simulation run is in progress. */
    public static boolean running() {
        return scenario != null;
    }
    private static String instruction = "";
    private static double minDistSum, absMin;
    private static int minDistN, contactSamples;
    private static final Map<String, Double> EXTRA = new LinkedHashMap<>();

    /** A value for this run's report ("extra"), e.g. the waves' progress (SimRunner). */
    public static void put(String key, double value) {
        EXTRA.put(key, value);
    }

    /** Adds 1 to a counter in this run's report (F15b). */
    public static void count(String key) {
        EXTRA.merge(key, 1.0D, Double::sum);
    }

    /** A value already put this run, or -1. */
    public static double get(String key) {
        Double v = EXTRA.get(key);
        return v == null ? -1.0D : v;
    }

    private static long[] perfStart;

    /** Reset at the start of every run. */
    public static void begin(SimScenario s) {
        scenario = s;
        perfStart = net.hfstack.rallyguard.debug.Perf.snapshot(); // E3B S: per-system cost of this run
        net.hfstack.rallyguard.debug.Perf.beginRun(); // F56: per-run worst tick per system and of the server
        ATTRS.clear();
        // E3-COORD M3: a control that differs in ONE thing - tag "nofill" switches the fill policy off for this run
        net.hfstack.rallyguard.force.Plans.fillOverride = s.tags().contains("nofill") ? Boolean.FALSE : null;
        // field manual (David 2026-11-04): each rule can be switched off per run to keep measuring it against the old way
        net.hfstack.rallyguard.combat.FieldManual.simNoClearance = s.tags().contains("off:clearance");
        net.hfstack.rallyguard.combat.FieldManual.simNoCreepers = s.tags().contains("off:creepers");
        net.hfstack.rallyguard.combat.FieldManual.simNoShieldUp = s.tags().contains("off:shieldUp");
        net.hfstack.rallyguard.combat.FieldManual.simNoLeftHand = s.tags().contains("off:leftHand");
        net.hfstack.rallyguard.combat.FieldManual.simNoHold = s.tags().contains("off:hold");
        net.hfstack.rallyguard.combat.FieldManual.simNoDodge = s.tags().contains("off:dodge");
        net.hfstack.rallyguard.combat.FieldManual.simNoRankBand = s.tags().contains("off:rankBand"); // F94
        net.hfstack.rallyguard.combat.FieldManual.simNoOutnumbered = s.tags().contains("off:outnumbered"); // F94
        net.hfstack.rallyguard.combat.FieldManual.simNoRankFocus = s.tags().contains("off:rankFocus"); // F106
        net.hfstack.rallyguard.combat.FieldManual.simNoScoot = s.tags().contains("off:scoot"); // F106
        net.hfstack.rallyguard.combat.FieldManual.simNoLowRetreat = s.tags().contains("off:lowRetreat"); // F159
        { // F172: noRule:<id> (repeatable) drops that force rule for this run
            java.util.Set<String> noRules = new java.util.HashSet<>();
            for (String t : s.tags()) if (t.startsWith("noRule:") && t.length() > 7) noRules.add(t.substring(7));
            net.hfstack.rallyguard.force.ForceBrain.simNoRules = noRules.isEmpty() ? java.util.Set.of() : noRules;
        }
        net.hfstack.rallyguard.morale.Morale.simOff = s.tags().contains("off:morale");
        net.hfstack.rallyguard.morale.Morale.simOldOdds = s.tags().contains("off:moraleExchange");
        net.hfstack.rallyguard.combat.GuardPack.simNoCalmEat = s.tags().contains("off:calmEat");
        net.hfstack.rallyguard.order.Escort.simOld = s.tags().contains("off:escort");
        net.hfstack.rallyguard.order.Escort.simNoScreen = s.tags().contains("off:screen");
        net.hfstack.rallyguard.combat.Testudo.simAnyShield = s.tags().contains("testudo:anyShield");
        boolean noGroup = s.tags().contains("off:group"); // F79: each guard alone - fire windows off, no shared picture
        // F81: one piece of the group layer at a time (exact tags: a sub-tag never switches the other pieces)
        net.hfstack.rallyguard.tactics.Tactics.simNoGroup = noGroup || s.tags().contains("off:group:target");
        // F85: the red side's guards (mirror battles) have their own switches; off:group without suffix stays blue only
        boolean noGroupRed = s.tags().contains("off:group:red");
        net.hfstack.rallyguard.tactics.Tactics.simNoGroupRed = noGroupRed || s.tags().contains("off:group:red:target");
        net.hfstack.rallyguard.tactics.Tactics.simFocusOpen = s.tags().contains("focus:open"); // F103
        net.hfstack.rallyguard.tactics.Tactics.simFocusSeed = s.tags().contains("focus:seed"); // F126
        net.hfstack.rallyguard.tactics.Tactics.simFocusQuota = 0; // F126
        try {
            String q = s.tag("focus:quota");
            if (!q.isEmpty()) net.hfstack.rallyguard.tactics.Tactics.simFocusQuota = Integer.parseInt(q);
        } catch (NumberFormatException ignored) {
            net.hfstack.rallyguard.tactics.Tactics.simFocusQuota = 0;
        }
        net.hfstack.rallyguard.combat.FireWindows.reset(s.tags().contains("fire:sync") ? net.hfstack.rallyguard.combat.FireWindows.Mode.SYNC
                : s.tags().contains("fire:spread") ? net.hfstack.rallyguard.combat.FireWindows.Mode.SPREAD
                : s.tags().contains("fire:off") ? net.hfstack.rallyguard.combat.FireWindows.Mode.OFF : null); // null = game default
        // F79/F81: blue windows off (was the whole mode OFF; same effect while red has no guards) - F85: red ones apart
        net.hfstack.rallyguard.combat.FireWindows.simOffBlue = noGroup || s.tags().contains("off:group:fire");
        net.hfstack.rallyguard.combat.FireWindows.simOffRed = noGroupRed || s.tags().contains("off:group:red:fire");
        net.hfstack.rallyguard.combat.MeleeWindows.simSync = s.tags().contains("melee:sync"); // F193
        net.hfstack.rallyguard.combat.FireWindows.simStagger = s.tags().contains("fire:stagger"); // F199
        net.hfstack.rallyguard.debug.sim.SimSampler.start(s); // F201: tag sample:<ms>, measure only
        net.hfstack.rallyguard.combat.FieldManual.simAdaptive = s.tags().contains("standoff:adaptive") ? Boolean.TRUE
                : s.tags().contains("standoff:fixed") ? Boolean.FALSE : null;
        net.hfstack.rallyguard.command.InstructionInterpreter.simOldRetreat = s.tags().contains("retreat:old");
        net.hfstack.rallyguard.command.InstructionInterpreter.simFlee = s.tags().contains("retreat:flee");
        net.hfstack.rallyguard.command.InstructionRunner.retreatPoint = null;
        pendingInstruction = null;
        earlyK = 0; // F69
        try {
            String k = s.tag("retreat:early");
            if (!k.isEmpty()) earlyK = Double.parseDouble(k);
        } catch (NumberFormatException ignored) {
            earlyK = 0;
        }
        early0K = 0; // F129
        forceT = 0;
        forceBlue0 = 0;
        forcePMax = 0;
        seenMax = 0;
        try {
            String k0 = s.tag("retreat:early0");
            if (!k0.isEmpty()) early0K = Double.parseDouble(k0);
            String t = s.tag("retreat:force");
            if (!t.isEmpty()) forceT = Double.parseDouble(t);
        } catch (NumberFormatException ignored) {
            early0K = 0;
            forceT = 0;
        }
        hurtTrigger = s.tags().contains("retreat:hurt");
        spreadS = 0; // F132
        try {
            String sp = s.tag("spread");
            if (!sp.isEmpty()) spreadS = Double.parseDouble(sp);
        } catch (NumberFormatException ignored) {
            spreadS = 0;
        }
        REFUGE_TIME.clear();
        RELAY_MOUNTED.clear(); // F15
        RELAYS.clear();
        IN_REFUGE.clear();
        orderGiven = false;
        HOLD_AT.clear(); // F173
        HOLD_RANGED.clear(); // F174
        SETTLE_AT.clear();
        settleSample = -1;
        advReset(); // F178
        blueOwnerId = null;
        relayNoCharge = s.tags().contains("relayNoCharge"); // F15b
        net.hfstack.rallyguard.order.MountDriver.simRider = s.tag("rider"); // F2: "" (game) | standoff | kite
        net.hfstack.rallyguard.order.MountDriver.simMelee = s.tag("melee"); // F2c: "" | hitrun
        net.hfstack.rallyguard.combat.FieldManual.resetStandOff();
        net.hfstack.rallyguard.combat.FieldManual.simLosRelease = s.tags().contains("standoff:losRelease");
        net.hfstack.rallyguard.combat.FieldManual.simCover = s.tags().contains("standoff:cover"); // F68
        net.hfstack.rallyguard.combat.FieldManual.simCoverOff = s.tags().contains("off:cover"); // F73
        net.hfstack.rallyguard.combat.FieldManual.simKiteBig = kiteBig(s.tag("kite")); // F34: kite:big=<near>-<far>
        net.hfstack.rallyguard.combat.FieldManual.simKiteBigOff = "big=off".equals(s.tag("kite")); // F48: the old 6-11 rule
        net.hfstack.rallyguard.combat.FieldManual.simHostileBig = s.tags().contains("kite:hostileBig"); // F117
        net.hfstack.rallyguard.combat.FieldManual.simKiteLazy = s.tags().contains("perf:kiteLazy"); // F134
        net.hfstack.rallyguard.tactics.Tactics.simCloserReuse = s.tags().contains("perf:closerReuse"); // F144
        net.hfstack.rallyguard.tactics.Tactics.simCloserYield = !s.tags().contains("off:closerYield"); // F158, F166, F176: ON unless off:closerYield
        net.hfstack.rallyguard.combat.RangedFireGoal.simNoHighAbove = s.tags().contains("fire:noHighAbove"); // F118
        net.hfstack.rallyguard.combat.RangedFireGoal.simPlanMemo = s.tags().contains("perf:planMemo"); // F189 (F190: no confirma, OFF)
        net.hfstack.rallyguard.order.Engagement.simBigCap = bigCap(s.tag("cap")); // F43: cap:big=<n>
        InstructionRunner.simFlankFixed = s.tags().contains("flank:fixed"); // F30
        net.hfstack.rallyguard.combat.Breach.simOff = s.tags().contains("off:breach"); // F53 (door:breach is set by SimRunner)
        net.hfstack.rallyguard.combat.Breach.simNoClose = s.tags().contains("door:noclose"); // F53
        net.hfstack.rallyguard.combat.Breach.simMeleeFirst = s.tags().contains("door:meleeFirst"); // F64
        BLUE_RANGED.clear(); // F64
        blueSide = null;
        deathProbe = s.tags().contains("probe:deaths"); // F66
        splitProbe = s.tags().contains("probe:split"); // F161
        unitMark = s.tags().contains("unitMark"); // F185
        SPLIT_POS.clear();
        net.hfstack.rallyguard.force.SituationReports.simSliceReports = s.tags().contains("perf:sliceReports"); // F60
        net.hfstack.rallyguard.force.Leadership.simOnePassUnitXp = !s.tags().contains("off:unitXp"); // F61, F74: ON unless off:unitXp
        net.hfstack.rallyguard.force.UnitOrders.simUnitIndex = !s.tags().contains("off:unitIndex"); // F62, F74: ON unless off:unitIndex
        InstructionRunner.SIM_FLANK_SIDE.clear();
        AttractionProbe.begin(s); // F1
        SimOracle.begin(s); // sonda de techo: oracle:aim|cadence|target|kite|all
        SimCombatStats.begin();
        SimCombatStats.lureBegin(s.tag("lure")); // F128: bait accounting only
        net.hfstack.rallyguard.combat.Testudo.simOff = s.tags().contains("off:testudo");
        net.hfstack.rallyguard.combat.FieldManual.simNoThreats = s.tags().contains("off:threats");
        net.hfstack.rallyguard.combat.FieldManual.simNoKite = s.tags().contains("off:kite");
        net.hfstack.rallyguard.combat.FieldManual.simNoStandOff = s.tags().contains("off:standoff");
        net.hfstack.rallyguard.combat.FieldManual.simNoEnderman = s.tags().contains("off:enderman");
        SHATTERED.clear();
        firstBrokenTick = -1;
        shatteredReturned = 0;
        brokenAt60 = Double.NaN;
        brokenMax = 0;
        instruction = s.tag("instruction").toUpperCase(Locale.ROOT);
        unitBegin(s.tag("unitInstr")); // F16
        minDistSum = 0;
        minDistN = 0;
        absMin = Double.NaN;
        contactSamples = 0;
        samples = 0;
        firstContact = -1;
        firstKnown = -1;
        resetPursuit(); // F87
        resetPlanUnits(); // F164
        EXTRA.clear();
        highGroundY = Integer.MIN_VALUE; // F115
        highAlive = 0;
        highUp = 0;
    }

    /** F115: arena floor under blue on a hill run (MIN_VALUE = not measured) and the blue-high counts per sample. */
    private static int highGroundY = Integer.MIN_VALUE;
    private static long highAlive, highUp;

    /** A profile-only scenario ends right after spawning. */
    public static boolean profileOnly(SimScenario s) {
        return s.tags().contains("profile");
    }

    /** Blue follows a general instruction: the simulation must not drive it at the enemy. */
    public static boolean blueInstructed() {
        return !instruction.isEmpty() || !UNIT_PLAN.isEmpty() || (scenario != null && scenario.tags().contains("nodrive"));
    }

    /** The kit of guard {@code index} in a "random" side: weapon role and whether it rides. */
    public static String[] randomKit(long seed, int index) {
        Random r = new Random(seed * 31L + index * 977L);
        String[] roles = {"melee", "archer", "crossbow", "axe", "spear", "fist", "shield"};
        String role = roles[r.nextInt(roles.length)];
        return new String[]{role, r.nextInt(3) == 0 ? "mounted" : "foot"};
    }

    /** After spawning and organizing: profile measures and instructions. */
    /** F33 (sim only, tag nogrief): the server whose mobGriefing this run switched off, null when untouched. */
    @javax.annotation.Nullable
    private static net.minecraft.server.MinecraftServer griefServer;

    private static void gamerule(net.minecraft.server.MinecraftServer server, String rule) {
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), "gamerule " + rule);
    }

    public static void setup(ServerLevel level, UUID blueOwner, List<Guard> blue, Vec3 blueAt, float blueYaw) {
        if (scenario == null || blue.isEmpty()) return;
        // F33: tag nogrief = mobGriefing off for this run (the boss cannot break the floor), restored in finish
        if (scenario.tags().contains("nogrief") && griefServer == null
                && level.getGameRules().getBoolean(net.minecraft.world.level.GameRules.RULE_MOBGRIEFING)) {
            griefServer = level.getServer();
            gamerule(griefServer, "mobGriefing false");
        }
        blueOwnerId = blueOwner; // F15
        highGroundY = "hill".equals(scenario.terrain()) || "hillwide".equals(scenario.terrain()) || "height".equals(scenario.position()) ? (int) Math.floor(blueAt.y) : Integer.MIN_VALUE; // F115
        for (Guard g : blue) ATTRS.put(g.getUUID(), rallyAttrs(g)); // E3-COORD M1: Rally's own share of each attribute at spawn
        // E3B S global check (1): mean spawn attributes of blue, compared across ranks in the report.
        double[] mean = new double[3];
        for (Guard g : blue) {
            double[] a = attrs(g);
            for (int k = 0; k < 3; k++) mean[k] += a[k] / blue.size();
        }
        EXTRA.put("attrHealth", mean[0]);
        EXTRA.put("attrDamage", mean[1]);
        EXTRA.put("attrSpeed", mean[2]);
        veterancySetup(level, blue);
        GroupProfile.Profile p = GroupProfile.of(blue);
        EXTRA.put("consistency", (double) p.consistency());
        EXTRA.put("identity", p.identity() ? 1.0D : 0.0D);
        EXTRA.put("kinds", (double) p.counts().size());
        String refuge = scenario.tag("refuge"); // F5: a refuge D blocks behind blue (blue faces yaw 180 = north... away = +z)
        if (!refuge.isEmpty()) {
            try {
                Vec3 back = Vec3.directionFromRotation(0, blueYaw + 180.0F).multiply(1, 0, 1).normalize();
                InstructionRunner.retreatPoint = blueAt.add(back.scale(Double.parseDouble(refuge)));
            } catch (NumberFormatException ignored) {
                // no refuge
            }
        }
        unitSetup(level, blueOwner, blue, blueYaw); // F16: units must exist before the general instruction splits by path
        if (!instruction.isEmpty()) {
            InstructionInterpreter.Instruction i = InstructionInterpreter.parse(instruction);
            if (i != null) {
                int delay = 0;
                try {
                    delay = scenario.tag("instructionAt").isEmpty() ? 0 : Integer.parseInt(scenario.tag("instructionAt"));
                } catch (NumberFormatException ignored) {
                    delay = 0;
                }
                if (earlyK > 0 || hurtTrigger || early0K > 0 || forceT > 0) delay = Integer.MAX_VALUE; // F69/F129: the trigger, not the clock, gives the order
                if (delay <= 0) {
                    InstructionRunner.start(level, blueOwner, blue, i, blueAt, blueYaw);
                    orderGiven = true; // F15
                    holdMark(blue); // F173
                }
                else pendingInstruction = new Object[]{level, blueOwner, new java.util.ArrayList<>(blue), i, blueAt, blueYaw, delay};
                EXTRA.put("family", (double) p.family().ordinal());
            }
        }
        unitTick(); // F16: the per-unit entries due at 0 s
    }

    /**
     * E3B V tests - blue's veterancy state set before the fight (sim XP stays blocked, these write the record):
     * {@code dread:TYPE:V} (threat memory of an entity type, e.g. dread:minecraft:creeper:0.9),
     * {@code mountbond:S} (seconds ridden), {@code mastery:ARCH:XP} (weapon mastery XP of an archetype).
     */
    private static void veterancySetup(ServerLevel level, List<Guard> blue) {
        String dread = scenario.tag("dread");
        String bond = scenario.tag("mountbond");
        String mastery = scenario.tag("mastery");
        String trait = scenario.tag("trait").toUpperCase(Locale.ROOT);
        if (dread.isEmpty() && bond.isEmpty() && mastery.isEmpty() && trait.isEmpty()) return;
        long now = level.getGameTime();
        for (Guard g : blue) {
            net.hfstack.rallyguard.contract.VeterancyRegistry.Record r = net.hfstack.rallyguard.veterancy.Veterancy.edit(level, g.getUUID());
            try {
                int cut = dread.lastIndexOf(':');
                if (cut > 0) r.dread.put(dread.substring(0, cut), new double[]{Double.parseDouble(dread.substring(cut + 1)), now});
                if (!bond.isEmpty()) r.secondsMounted = Integer.parseInt(bond);
                int mc = mastery.lastIndexOf(':');
                if (mc > 0) r.weaponXp.put(mastery.substring(0, mc), Double.parseDouble(mastery.substring(mc + 1)));
                if (!trait.isEmpty() && !r.traits.contains(trait)) r.traits.add(trait); // E3B C1: e.g. trait:SCOUT
            } catch (NumberFormatException ignored) {
                // a malformed tag sets nothing
            }
        }
    }

    private static Object[] pendingInstruction;

    // ==== F69 (2026-11-22): retreat triggers (simulation only, off without their tag) ====

    /** {@code retreat:early:K}: K of the anticipated trigger (0 = off). */
    private static double earlyK;
    /** F129 {@code retreat:early0:K}: the head count of F69 with the blue guards alive at first sight as denominator. */
    private static double early0K;
    /** F129 {@code retreat:force:T}: seen force (sum of health x max(1, attack damage)) per blue guard at first sight. */
    private static double forceT;
    /** F129: blue guards alive the first second a red was seen (0 = not yet), and the run's maxima of P and seen. */
    private static int forceBlue0;
    private static double forcePMax, seenMax;
    /** {@code retreat:hurt}: the reactive trigger - the order comes the first second a blue guard is hurt or dead. */
    private static boolean hurtTrigger;
    /** What the squad can see: a red within this many blocks of a living blue guard and in its line of sight. */
    private static final double EARLY_SIGHT = 48.0D;

    /**
     * Once a second while the instruction waits. Early: blue sees enemies >= K x living guards (a red counts when some
     * living blue guard has it within {@link #EARLY_SIGHT} blocks and in line of sight; the Tactics picture is not used:
     * its box is 24 blocks and idle guards build none) and the nearest seen is <= refuge + 14 + 8 blocks. Then the
     * waiting instruction is due now ({@code earlyAt}, {@code earlyDist}, {@code earlySeen}; {@code earlyLate} = 1 when
     * the nearest was already inside refuge + 14). Hurt: due the first second a blue guard lost health ({@code hurtAt}).
     */
    private static void triggerTick(List<LivingEntity> blue, List<LivingEntity> red) {
        int alive = 0;
        boolean hurt = false;
        for (LivingEntity b : blue) {
            if (b.isAlive()) {
                alive++;
                if (b.getHealth() < b.getMaxHealth()) hurt = true;
            } else hurt = true;
        }
        if (alive == 0) return;
        if (hurtTrigger) {
            if (!hurt) return;
            EXTRA.put("hurtAt", (double) samples);
            pendingInstruction[6] = samples;
            return;
        }
        int seen = 0;
        double nearest = Double.MAX_VALUE;
        double forceSum = 0.0D;
        for (LivingEntity r : red) {
            if (!r.isAlive()) continue;
            double rd = Double.MAX_VALUE;
            for (LivingEntity b : blue) {
                if (!b.isAlive()) continue;
                double d = b.distanceTo(r);
                if (d <= EARLY_SIGHT && d < rd && b.hasLineOfSight(r)) rd = d;
            }
            if (rd == Double.MAX_VALUE) continue;
            seen++;
            nearest = Math.min(nearest, rd);
            double dmg = 1.0D;
            try {
                if (r.getAttributes().hasAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)) {
                    dmg = r.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE);
                }
            } catch (RuntimeException ignored) {
                dmg = 1.0D;
            }
            forceSum += r.getHealth() * Math.max(1.0D, dmg);
        }
        // F129: denominator fixed at first sight; the maxima are written in every arm with a waiting instruction
        if (seen > 0 && forceBlue0 == 0) {
            forceBlue0 = alive;
            EXTRA.put("forceBlue0", (double) alive);
        }
        double force = forceBlue0 > 0 ? forceSum / forceBlue0 : 0.0D;
        if (force > forcePMax) {
            forcePMax = force;
            EXTRA.put("forcePMax", Math.round(force * 10.0D) / 10.0D);
        }
        if (seen > seenMax) {
            seenMax = seen;
            EXTRA.put("seenMax", (double) seen);
        }
        if (earlyK <= 0 && early0K <= 0 && forceT <= 0) return; // measure only (espera arms)
        double refuge = 16.0D;
        try {
            refuge = Double.parseDouble(scenario.tag("refuge"));
        } catch (NumberFormatException ignored) {
            refuge = 16.0D;
        }
        if (seen == 0 || nearest > refuge + 14.0D + 8.0D) return;
        if (forceT > 0) {
            if (force < forceT) return;
            EXTRA.put("forceAt", (double) samples);
            EXTRA.put("forceP", Math.round(force * 10.0D) / 10.0D);
        } else if (early0K > 0) {
            if (seen < early0K * forceBlue0) return;
            EXTRA.put("earlyAt", (double) samples);
        } else {
            if (seen < earlyK * alive) return;
            EXTRA.put("earlyAt", (double) samples);
        }
        EXTRA.put("earlyDist", Math.round(nearest * 10.0D) / 10.0D);
        EXTRA.put("earlySeen", (double) seen);
        EXTRA.put("earlyLate", nearest < refuge + 14.0D ? 1.0D : 0.0D);
        pendingInstruction[6] = samples;
    }
    private static final Map<UUID, Integer> REFUGE_TIME = new java.util.HashMap<>();
    /** F64: each blue guard's role at first sight (true = ranged weapon), and the live blue list, for survRanged/Melee. */
    private static final Map<UUID, Boolean> BLUE_RANGED = new java.util.HashMap<>();
    private static List<LivingEntity> blueSide;
    /** F66: tag "probe:deaths" - where and how each blue guard dies (only a measure, no rule). */
    private static boolean deathProbe;

    /**
     * F66: one blue guard's death, by role (R = ranged weapon at first sight, M = melee): distance to the attacker
     * ({@code dk}), whether it saw its own target ({@code dlos}), under a roof ({@code din}), a wooden door in the
     * radius-3 cube around it ({@code ddoor}), moving ({@code dmove}), seconds into the run ({@code dsec}, a sum) and
     * the count ({@code dn}). Called from {@code SimRunner#onDeath} for blue guards only.
     */
    public static void deathProbe(Guard g, net.minecraft.world.damagesource.DamageSource source) {
        if (!deathProbe || scenario == null) return;
        Boolean ranged = BLUE_RANGED.get(g.getUUID());
        if (ranged == null) ranged = net.hfstack.rallyguard.combat.WeaponProfile.forGuard(g) != null;
        String r = ranged ? "R" : "M";
        net.minecraft.world.entity.Entity by = source.getEntity();
        String dk;
        if (by == null || by == g) dk = "none";
        else {
            double d = g.distanceTo(by);
            dk = d < 8.0D ? "lt8" : d < 16.0D ? "8-16" : d <= 32.0D ? "16-32" : "gt32";
        }
        count("dk:" + r + ":" + dk);
        LivingEntity t = g.getTarget();
        count("dlos:" + r + ":" + (t == null || !t.isAlive() ? "notarget" : g.hasLineOfSight(t) ? "yes" : "no"));
        net.minecraft.core.BlockPos pos = g.blockPosition();
        count("din:" + r + ":" + (g.level().canSeeSky(pos.above()) ? "out" : "in"));
        boolean door = false;
        for (net.minecraft.core.BlockPos q : net.minecraft.core.BlockPos.betweenClosed(pos.offset(-3, -3, -3), pos.offset(3, 3, 3))) {
            if (g.level().getBlockState(q).is(net.minecraft.tags.BlockTags.WOODEN_DOORS)) {
                door = true;
                break;
            }
        }
        count("ddoor:" + r + ":" + (door ? "lt3" : "ge3"));
        Vec3 v = g.getDeltaMovement();
        String mv = net.hfstack.rallyguard.combat.FieldManual.kiting(g) ? "kite"
                : Math.sqrt(v.x * v.x + v.z * v.z) < 0.03D ? "still" : "other";
        count("dmove:" + r + ":" + mv);
        EXTRA.merge("dsec:" + r, (double) samples, Double::sum);
        count("dn:" + r);
    }

    // ==== F161 (O9) probe:split - what the survivors of a big battle are doing (read only, decides nothing) ====

    private static boolean splitProbe;
    /** F185 (sim only, tag unitMark): each unitInstr order also files the hold/advance/flank marks and the unit family. */
    private static boolean unitMark;
    /** Last 6 one-second positions per unit (both sides), only while probe:split is on. */
    private static final Map<UUID, java.util.ArrayDeque<Vec3>> SPLIT_POS = new java.util.HashMap<>();
    private static final int[] SPLIT_SECONDS = {30, 60, 90, 120};

    private static void splitTick(List<LivingEntity> blue, List<LivingEntity> red) {
        for (List<LivingEntity> side : List.of(blue, red)) {
            for (LivingEntity e : side) {
                if (!e.isAlive()) continue;
                java.util.ArrayDeque<Vec3> q = SPLIT_POS.computeIfAbsent(e.getUUID(), k -> new java.util.ArrayDeque<>());
                q.addLast(e.position());
                while (q.size() > 6) q.removeFirst();
            }
        }
        for (int at : SPLIT_SECONDS) {
            if (samples != at) continue;
            splitSide(at, "blue", blue, red);
            splitSide(at, "red", red, blue);
        }
    }

    private static void splitSide(int at, String sideName, List<LivingEntity> own, List<LivingEntity> enemy) {
        int alive = 0, noTarget = 0, farEnemy = 0, still = 0, navIdle = 0, following = 0, patrolling = 0, noTargetNear = 0, nearN = 0;
        double nearSum = 0;
        for (LivingEntity e : own) {
            if (!e.isAlive()) continue;
            alive++;
            double best = Double.MAX_VALUE;
            for (LivingEntity o : enemy) if (o.isAlive()) best = Math.min(best, e.distanceToSqr(o));
            double near = best < Double.MAX_VALUE ? Math.sqrt(best) : Double.NaN;
            if (!Double.isNaN(near)) {
                nearSum += near;
                nearN++;
            }
            if (Double.isNaN(near) || near > 32.0D) farEnemy++;
            boolean none = false;
            if (e instanceof net.minecraft.world.entity.Mob m) {
                LivingEntity t = m.getTarget();
                none = t == null || !t.isAlive();
                if (m.getNavigation().isDone()) navIdle++;
            }
            if (none) {
                noTarget++;
                if (!Double.isNaN(near) && near <= 16.0D) noTargetNear++;
            }
            java.util.ArrayDeque<Vec3> q = SPLIT_POS.get(e.getUUID());
            if (q != null && q.size() >= 6 && q.peekFirst().distanceTo(q.peekLast()) < 1.0D) still++;
            if (e instanceof Guard g) {
                if (g.isFollowing()) following++;
                if (g.isPatrolling()) patrolling++;
            }
        }
        String k = "split:" + at + ":" + sideName + ":";
        EXTRA.put(k + "alive", (double) alive);
        double n = Math.max(1, alive);
        EXTRA.put(k + "noTarget", noTarget / n);
        EXTRA.put(k + "farEnemy", farEnemy / n);
        EXTRA.put(k + "still", still / n);
        EXTRA.put(k + "navIdle", navIdle / n);
        EXTRA.put(k + "following", following / n);
        EXTRA.put(k + "patrolling", patrolling / n);
        EXTRA.put(k + "noTargetNear", noTargetNear / n);
        if (nearN > 0) EXTRA.put(k + "nearEnemyMean", nearSum / nearN);
    }

    /** Once a second: closest blue-red distance and contact. */
    @SuppressWarnings("unchecked")
    public static void sample(List<LivingEntity> blue, List<LivingEntity> red) {
        redSide = red; // E3B P: the planner / scenario hooks move the red side
        blueSide = blue; // F64
        for (LivingEntity b : blue) {
            if (b instanceof Guard g && g.isAlive() && !BLUE_RANGED.containsKey(g.getUUID())) {
                BLUE_RANGED.put(g.getUUID(), net.hfstack.rallyguard.combat.WeaponProfile.forGuard(g) != null);
            }
        }
        if (scenario == null) return;
        samples++;
        if (splitProbe) splitTick(blue, red); // F161
        if (pendingInstruction != null) triggerTick(blue, red); // F69 (F129: also measures P in arms without a trigger)
        if (pendingInstruction != null && samples >= (Integer) pendingInstruction[6]) { // F5: the order comes mid-fight
            Object[] a = pendingInstruction;
            pendingInstruction = null;
            List<Guard> alive = new java.util.ArrayList<>();
            for (Guard g : (List<Guard>) a[2]) if (g.isAlive() && !UNIT_OWN.contains(UNIT_OF.getOrDefault(g.getUUID(), 0))) alive.add(g); // F16: a unit already on its own order keeps it
            if (!alive.isEmpty()) InstructionRunner.start((ServerLevel) a[0], (UUID) a[1], alive,
                    (InstructionInterpreter.Instruction) a[3], alive.get(0).position(), (Float) a[5]);
            EXTRA.put("orderAt", (double) samples);
            orderGiven = true; // F15
            holdMark(alive); // F173
        }
        settleTick(blue); // F174
        advTick(blue); // F178
        Vec3 rp = InstructionRunner.retreatPoint;
        if (rp != null) {
            for (LivingEntity b : blue) {
                if (b.isAlive() && b.position().multiply(1, 0, 1).distanceTo(rp.multiply(1, 0, 1)) <= 8.0D) REFUGE_TIME.putIfAbsent(b.getUUID(), samples);
            }
            refugeLeaves(blue, rp); // F15 telemetry: who leaves the refuge, and why
        }
        relayTick(blue); // F15: relay horses during a flee
        unitTick(); // F16: per-unit instructions due now
        unitSample(red);
        if (firstContact < 0 && closest(blue, red) <= 3.0D) firstContact = samples; // E3B P1 TEMPO: seconds to contact
        if (highGroundY != Integer.MIN_VALUE) { // F115: guard-seconds alive standing >= 2 blocks above the arena floor
            for (LivingEntity b : blue) {
                if (!b.isAlive()) continue;
                highAlive++;
                if (b.getY() >= highGroundY + 2.0D) highUp++;
            }
        }
        if (instruction.isEmpty() && !scenario.tags().contains("measuredist")) return;
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

    // ==== E3B M: morale scenarios ====

    private static final Map<UUID, double[]> ATTRS = new java.util.HashMap<>();
    private static final java.util.Set<UUID> SHATTERED = new java.util.HashSet<>();
    private static int firstBrokenTick, shatteredReturned;
    private static double brokenAt60 = Double.NaN, brokenMax;

    private static double[] attrs(Guard g) {
        return new double[]{g.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH),
                g.getAttributes().hasAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
                        ? g.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE) : 0,
                g.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED)};
    }

    /** Tag "key:a:b" -> {a, b} (missing parts = -1). */
    private static double[] tagNums(String key) {
        String v = scenario.tag(key);
        double[] out = {-1, -1};
        if (v.isEmpty()) return out;
        String[] p = v.split(":");
        for (int i = 0; i < Math.min(2, p.length); i++) {
            try {
                out[i] = Double.parseDouble(p[i]);
            } catch (NumberFormatException ignored) {
                // keep -1
            }
        }
        return out;
    }

    private static void moraleTick(ServerLevel level, int tick, int formTicks, List<Guard> blue) {
        int t = tick - formTicks;
        double[] kc = tagNums("killcaptain"), ks = tagNums("killshare"), bo = tagNums("breakone"), ba = tagNums("breakall");
        if (kc[0] >= 0 && t == (int) (kc[0] * 20)) {
            java.util.Set<UUID> captains = new java.util.HashSet<>();
            for (Guard g : blue) {
                net.hfstack.rallyguard.command.SquadDirectives.Directive d = net.hfstack.rallyguard.command.SquadDirectives.of(g.getUUID());
                if (d != null && d.captain() != null) captains.add(d.captain());
            }
            for (Guard g : blue) if (captains.contains(g.getUUID())) g.kill();
        }
        if (ks[0] >= 0 && t == (int) (Math.max(0, ks[1]) * 20)) {
            int n = (int) Math.round(blue.size() * ks[0]);
            for (int i = blue.size() - 1; i >= 0 && n > 0; i--, n--) blue.get(i).kill();
        }
        if (bo[0] >= 0 && t == (int) (bo[0] * 20) && !blue.isEmpty()) {
            net.hfstack.rallyguard.morale.Morale.breakUnit(List.of(blue.get(blue.size() / 2)));
        }
        if (ba[0] >= 0 && t == (int) (ba[0] * 20)) {
            List<Guard> rank = new java.util.ArrayList<>();
            for (Guard g : blue) {
                net.hfstack.rallyguard.command.SquadDirectives.Directive d = net.hfstack.rallyguard.command.SquadDirectives.of(g.getUUID());
                if (d == null || !g.getUUID().equals(d.captain())) rank.add(g);
            }
            net.hfstack.rallyguard.morale.Morale.breakUnit(rank);
        }
        if (t > 0 && t % 20 == 0 && !blue.isEmpty()) {
            int broken = 0;
            for (Guard g : blue) {
                net.hfstack.rallyguard.morale.MoraleState st = net.hfstack.rallyguard.morale.Morale.state(g);
                if (st.broken()) broken++;
                if (st == net.hfstack.rallyguard.morale.MoraleState.DESTROZADO) SHATTERED.add(g.getUUID());
                else if (SHATTERED.remove(g.getUUID())) shatteredReturned++;
            }
            if (broken > 0 && firstBrokenTick < 0) firstBrokenTick = t;
            double share = (double) broken / blue.size();
            brokenMax = Math.max(brokenMax, share);
            if (t == 1200) brokenAt60 = share;
        }
    }

    // ==== F132 (sim only, tag spread:S): minimum spacing between blue guards ====
    /** Spacing in blocks; 0 = off (no tag). */
    private static double spreadS;

    /** Every 10 ticks of the fight: each blue guard with a comrade closer than S asks for one navigation step (at most
     *  2 blocks) away from its nearest comrade. Never touches target, mode (GuardModes), stance or directives; a guard
     *  that is kiting / standing off keeps that move (it owns the navigation while it lasts). */
    private static void spreadTick(int tick, int formTicks, List<Guard> blue) {
        if (spreadS <= 0 || tick < formTicks || tick % 10 != 0) return;
        double s2 = spreadS * spreadS;
        for (Guard g : blue) {
            if (g.isRemoved() || !g.isAlive() || g.isPassenger()) continue;
            net.hfstack.rallyguard.event.SafeTick.run(g, () -> spreadStep(g, blue, s2));
        }
    }

    private static void spreadStep(Guard g, List<Guard> blue, double s2) {
        if (net.hfstack.rallyguard.combat.FieldManual.kiting(g)) return;
        Guard near = null;
        double best = s2;
        for (Guard o : blue) {
            if (o == g || o.isRemoved() || !o.isAlive()) continue;
            double d = g.distanceToSqr(o);
            if (d < best) {
                best = d;
                near = o;
            }
        }
        if (near == null) return;
        Vec3 away = g.position().subtract(near.position()).multiply(1, 0, 1);
        if (away.lengthSqr() < 1.0E-4) { // same spot: a direction of its own (by id) so the two do not walk together
            double a = (g.getUUID().hashCode() & 0xFFFF) / 65536.0D * Math.PI * 2;
            away = new Vec3(Math.cos(a), 0, Math.sin(a));
        } else {
            away = away.normalize();
        }
        double step = Math.min(2.0D, spreadS - Math.sqrt(best));
        if (step <= 0) return;
        Vec3 dest = g.position().add(away.scale(step));
        if (g.getNavigation().moveTo(dest.x, dest.y, dest.z, 1.0D)) net.hfstack.rallyguard.debug.SimProbe.count(g, "act:spreadStep");
    }

    // ==== Per-tick checks (E3B R3 and later); true = end the run now ====

    private static net.minecraft.world.phys.Vec3 startCentre;

    private static net.hfstack.rallyguard.contract.HqRegistry.Hq hq;
    private static ServerLevel hqLevel;

    public static boolean tick(ServerLevel level, net.minecraft.server.level.ServerPlayer player, int tick, int formTicks,
                               List<Guard> blue) {
        if (scenario == null) return false;
        moraleTick(level, tick, formTicks, blue);
        spreadTick(tick, formTicks, blue); // F132
        planTick(level, tick, formTicks, blue);
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
    private static final String[] COUNTERS = {"terrainReads", "coreDecisions", "outnumberedRetreats", "rungChanges",
            "rallies", "pursuitDeaths", "morale_ROTO", "morale_DESTROZADO", "morale_VACILANTE",
            "fills", "reconSightings", "ambushSprung", "ambushDiscovered", "lanePassed", "laneTimedOut", "delaySteps", "pursuitLimited",
            "reorderFreed", "reorderPushed"}; // F167

    // ==== E3B P: planner v2 and scenarios ====

    private static List<LivingEntity> redSide = List.of();
    private static int samples, firstContact = -1, firstKnown = -1;

    private static double closest(List<LivingEntity> blue, List<LivingEntity> red) {
        double best = Double.MAX_VALUE;
        for (LivingEntity b : blue) {
            if (!b.isAlive()) continue;
            for (LivingEntity r : red) if (r.isAlive()) best = Math.min(best, b.distanceTo(r));
        }
        return best;
    }
    private static boolean redHeld;
    private static boolean redFleeing;

    /**
     * F87: true from {@code redflee:T} on. Before F87 SimRunner.drive handed every fleeing red guard a blue target again
     * each second, one second after the flee cleared it, so the "flight" was mostly a return to the fight.
     */
    public static boolean redFleeing() {
        return redFleeing;
    }

    /** {@code redwait:T}: the red side is not driven at blue before T s (an ambush must come out of nowhere). */
    public static boolean redHeld() {
        return redHeld;
    }

    private static String unit(String squad, int n) {
        return squad + net.hfstack.rallyguard.contract.SquadRegistry.PATH_SEPARATOR + "U" + n;
    }

    /**
     * Tags: {@code plan:KIND} executes a plan on blue's force 1 s after the fight starts (hold, assault, fill,
     * ambush, lane, delay, counter, tempo; the plan's scenarios are the default library, {@code noscn} = none);
     * {@code ambushdrop:T} puts the red side 6 blocks behind blue at T s; {@code redwait:T} holds red until T s;
     * {@code redflee:T} sends red guards away from blue from T s on; {@code killunit:N:T} kills unit UN at T s;
     * {@code hq} gives blue an HQ at its start. Metrics: scenario activations (scn_KIND), fills, ambush, lane.
     */
    private static void planTick(ServerLevel level, int tick, int formTicks, List<Guard> blue) {
        int t = tick - formTicks;
        UUID owner = net.hfstack.rallyguard.debug.SimRunner.SIM_OWNER;
        String squad = net.hfstack.rallyguard.debug.SimRunner.SIM_SQUAD;
        // E3B C1: seconds until blue's force knows of any enemy (recon-* scenarios).
        if (firstKnown < 0 && t > 0 && t % 20 == 0) {
            net.hfstack.rallyguard.contract.ForceRegistry.Force kf = net.hfstack.rallyguard.contract.ForceRegistry.get(level).find(owner, squad);
            if (kf != null && !net.hfstack.rallyguard.force.SituationReports.knownEnemies(
                    net.hfstack.rallyguard.force.ForceBrain.forceKey(kf), level.getGameTime()).isEmpty()) {
                firstKnown = t / 20;
            }
        }
        double[] rw = tagNums("redwait");
        redHeld = rw[0] >= 0 && t < (int) (rw[0] * 20);
        if (redHeld) { // F179: red mobs (not guards) carry their own AI; hold them still too until T s
            boolean any = false;
            for (LivingEntity r : redSide) {
                if (!r.isAlive() || r instanceof Guard || !(r instanceof net.minecraft.world.entity.Mob m)) continue;
                m.setTarget(null);
                m.getNavigation().stop();
                any = true;
            }
            if (any) EXTRA.merge("act:redHeldTicks", 1.0D, Double::sum);
        }
        if (blue.isEmpty()) return;
        Vec3 c = InstructionRunner.centre(blue);
        Vec3 enemy = centreOf(redSide);
        float facing = enemy == null ? 0.0F : InstructionRunner.yaw(c, enemy);
        if (t == 10 && scenario.tags().contains("hq") && hq == null) {
            net.minecraft.core.BlockPos pos = net.minecraft.core.BlockPos.containing(c);
            hq = net.hfstack.rallyguard.contract.HqRegistry.get(level).create(owner, "SimHQ", level.dimension().location().toString(), pos);
            hqLevel = level;
            net.hfstack.rallyguard.contract.SquadRegistry.get(level).setHqId(owner, squad, hq.id);
        }
        String kind = scenario.tag("plan").toLowerCase(Locale.ROOT);
        if (!kind.isEmpty() && t == 40) {
            net.hfstack.rallyguard.contract.ForceRegistry.Force f = net.hfstack.rallyguard.contract.ForceRegistry.get(level).find(owner, squad);
            if (f != null) {
                try {
                    // E3-COORD: the acting unit of a link must be ABLE to carry it out (as a player would pick it): the
                    // first unit after U1 with a capability the link needs, else U2 (then the report shows missing_cap)
                    linkTo = null;
                    if (kind.equals("ambush") || kind.equals("counter")) {
                        net.hfstack.rallyguard.force.Plans.LinkType lt = kind.equals("ambush")
                                ? net.hfstack.rallyguard.force.Plans.LinkType.AMBUSH : net.hfstack.rallyguard.force.Plans.LinkType.COUNTERATTACK;
                        for (net.hfstack.rallyguard.force.Forces.UnitView u : net.hfstack.rallyguard.force.Forces.units(level, f)) {
                            if (u.path().equalsIgnoreCase(unit(squad, 1))) continue;
                            boolean able = false;
                            for (net.hfstack.rallyguard.force.Capability cap : net.hfstack.rallyguard.force.Plans.needs(lt)) {
                                if (u.capabilities().contains(cap)) able = true;
                            }
                            if (able) {
                                linkTo = u.path();
                                break;
                            }
                        }
                    }
                    if (kind.equals("ambush")) ambushTeleport(level, squad, blue, c, enemy); // F168: off without ambushwait
                    net.hfstack.rallyguard.force.Plans.execute(level, f, net.hfstack.rallyguard.force.Plans.parse(
                            planJson(kind, squad, c, enemy, facing)));
                    EXTRA.put("planStarted", 1.0D);
                    planUnitsBegin(level, squad, blue); // F164
                } catch (RuntimeException e) {
                    EXTRA.put("planStarted", 0.0D);
                    com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] sim plan {} rejected: {}", kind, e.toString());
                }
            }
        }
        if (t > 40 && t % 20 == 0) planUnitsTick(t / 20); // F164
        double[] ad = tagNums("ambushdrop");
        if (ad[0] >= 0 && t == (int) (ad[0] * 20)) {
            Vec3 fwd = new Vec3(-Math.sin(Math.toRadians(facing)), 0, Math.cos(Math.toRadians(facing)));
            Vec3 drop = c.subtract(fwd.scale(6));
            int i = 0;
            for (LivingEntity r : redSide) {
                if (!r.isAlive()) continue;
                double ox = (i % 4 - 1.5D) * 1.5D, oz = (i / 4) * -1.5D;
                r.teleportTo(drop.x + ox, drop.y, drop.z + oz);
                i++;
            }
        }
        double[] rf = tagNums("redflee");
        redFleeing = rf[0] >= 0 && t >= (int) (rf[0] * 20); // F87: SimRunner.drive leaves fleeing red alone
        if (rf[0] >= 0 && t >= (int) (rf[0] * 20) && t % 40 == 0 && enemy != null) {
            Vec3 away = enemy.subtract(c).multiply(1, 0, 1);
            if (away.lengthSqr() > 1.0E-4D) {
                Vec3 to = enemy.add(away.normalize().scale(16));
                for (LivingEntity r : redSide) {
                    if (r instanceof Guard g && g.isAlive()) {
                        g.setTarget(null);
                        net.hfstack.rallyguard.order.GuardModes.postAndMove(g, to, 1.2D);
                    }
                }
            }
        }
        pursuitTick(t, blue, c, enemy); // F87: pursuit measures (telemetry only)
        double[] ku = tagNums("killunit");
        if (ku[0] >= 1 && t == (int) (Math.max(0, ku[1]) * 20)) {
            String path = unit(squad, (int) ku[0]);
            net.hfstack.rallyguard.contract.SquadRegistry squads = net.hfstack.rallyguard.contract.SquadRegistry.get(level);
            for (Guard g : blue) {
                if (squads.pathOf(g.getUUID()).map(path::equalsIgnoreCase).orElse(false)) g.kill();
            }
        }
    }

    // ==== F164 (2026-11-24): per-unit measures of a plan run with unit:N, telemetry only (never changes a decision) ====
    // U1 / U2 = blue guards whose squad path is U1 / U2 when the plan starts (t = 2 s). Seconds count from the fight
    // start (t / 20). springS = first second with ambushSprung or ambushDiscovered counted; u1DeadAtSpring = U1 dead
    // then (-1 = never sprung); u2ContactS = first second a live U2 guard is <= 4 blocks from a live red (-1 = never);
    // u1FirstDeathS (-1 = none); missingCapS = seconds the AMBUSH link was skipped for missing_cap (Plans steps a force
    // once a second, so the counter is seconds). Kept apart from UNIT_OF: unitTracking() changes InstructionRunner.
    private static final List<Guard> PLAN_U1 = new java.util.ArrayList<>(), PLAN_U2 = new java.util.ArrayList<>();
    private static int planSpringS = -1, planU1AtSpring = -1, planU2ContactS = -1, planU1FirstDeathS = -1;

    private static void resetPlanUnits() {
        PLAN_U1.clear();
        PLAN_U2.clear();
        planSpringS = -1;
        planU1AtSpring = -1;
        planU2ContactS = -1;
        planU1FirstDeathS = -1;
    }

    private static void planUnitsBegin(ServerLevel level, String squad, List<Guard> blue) {
        resetPlanUnits();
        if (scenario == null || scenario.tag("unit").isEmpty()) return;
        net.hfstack.rallyguard.contract.SquadRegistry squads = net.hfstack.rallyguard.contract.SquadRegistry.get(level);
        String u1 = unit(squad, 1), u2 = unit(squad, 2);
        for (Guard g : blue) {
            String path = squads.pathOf(g.getUUID()).orElse("");
            if (path.equalsIgnoreCase(u1)) PLAN_U1.add(g);
            else if (path.equalsIgnoreCase(u2)) PLAN_U2.add(g);
        }
    }

    private static int deadOf(List<Guard> gs) {
        int n = 0;
        for (Guard g : gs) if (!g.isAlive()) n++;
        return n;
    }

    private static void planUnitsTick(int s) {
        if (PLAN_U1.isEmpty() && PLAN_U2.isEmpty()) return;
        int dead1 = deadOf(PLAN_U1);
        if (planU1FirstDeathS < 0 && dead1 > 0) planU1FirstDeathS = s;
        if (planSpringS < 0 && net.hfstack.rallyguard.debug.SimProbe.counted("ambushSprung", 0)
                + net.hfstack.rallyguard.debug.SimProbe.counted("ambushDiscovered", 0) > 0) {
            planSpringS = s;
            planU1AtSpring = dead1;
        }
        if (planU2ContactS < 0) {
            for (Guard g : PLAN_U2) {
                if (!g.isAlive()) continue;
                for (LivingEntity r : redSide) {
                    if (r.isAlive() && g.distanceToSqr(r) <= 16.0D) {
                        planU2ContactS = s;
                        break;
                    }
                }
                if (planU2ContactS >= 0) break;
            }
        }
    }

    private static void planUnitsFinish() {
        if (PLAN_U1.isEmpty() && PLAN_U2.isEmpty()) return;
        EXTRA.put("u1Size", (double) PLAN_U1.size());
        EXTRA.put("u2Size", (double) PLAN_U2.size());
        EXTRA.put("u1Dead", (double) deadOf(PLAN_U1));
        EXTRA.put("u2Dead", (double) deadOf(PLAN_U2));
        EXTRA.put("springS", (double) planSpringS);
        EXTRA.put("u1DeadAtSpring", (double) planU1AtSpring);
        EXTRA.put("u2ContactS", (double) planU2ContactS);
        EXTRA.put("u1FirstDeathS", (double) planU1FirstDeathS);
        EXTRA.put("missingCapS", (double) net.hfstack.rallyguard.debug.SimProbe.counted("linkSkipped:AMBUSH:missing_cap", 0));
        resetPlanUnits();
    }

    // ==== F87 (2026-11-22): pursuit measures, telemetry only (never changes a decision) ====
    // T = redflee:T, or fleemeasure:T in the no-flee control arms. fleeRed0 = red alive at T; fleeDist0 / fleeDistMax =
    // blue-red centre distance at T / largest after T; fleeChaseMax / fleeChaseSec = most blue guards with a live red
    // target, and seconds with at least one, from T + 5 s on. At the end: redKilledAfterFlee, redFledAlive (red alive
    // farther than 24 blocks from blue's last centre).
    private static int fleeRed0 = -1, fleeChaseMax, fleeChaseSec;
    private static double fleeDist0 = -1, fleeDistMax = -1;
    @javax.annotation.Nullable
    private static Vec3 fleeBlueCentre;

    private static double fleeTagSeconds() {
        double[] rf = tagNums("redflee");
        return rf[0] >= 0 ? rf[0] : tagNums("fleemeasure")[0];
    }

    private static void resetPursuit() {
        fleeRed0 = -1;
        fleeChaseMax = 0;
        fleeChaseSec = 0;
        fleeDist0 = -1;
        fleeDistMax = -1;
        fleeBlueCentre = null;
    }

    private static void pursuitTick(int t, List<Guard> blue, Vec3 c, @javax.annotation.Nullable Vec3 enemy) {
        double ts = fleeTagSeconds();
        if (ts < 0) return;
        int at = (int) (ts * 20);
        fleeBlueCentre = c;
        if (t < at || t % 20 != 0) return;
        double dist = enemy == null ? -1 : Math.sqrt(Math.pow(enemy.x - c.x, 2) + Math.pow(enemy.z - c.z, 2));
        if (fleeRed0 < 0) {
            int alive = 0;
            for (LivingEntity r : redSide) if (r.isAlive()) alive++;
            fleeRed0 = alive;
            fleeDist0 = dist;
        }
        if (dist > fleeDistMax) fleeDistMax = dist;
        if (t >= at + 100) {
            int chasing = 0;
            for (Guard g : blue) {
                LivingEntity tg = g.isAlive() ? g.getTarget() : null;
                if (tg != null && tg.isAlive() && redSide.contains(tg)) chasing++;
            }
            fleeChaseMax = Math.max(fleeChaseMax, chasing);
            if (chasing > 0) fleeChaseSec++;
        }
    }

    private static void pursuitEnd() {
        if (scenario == null || fleeTagSeconds() < 0) return;
        int alive = 0, fled = 0;
        for (LivingEntity r : redSide) {
            if (!r.isAlive()) continue;
            alive++;
            if (fleeBlueCentre != null && Math.sqrt(Math.pow(r.getX() - fleeBlueCentre.x, 2)
                    + Math.pow(r.getZ() - fleeBlueCentre.z, 2)) > 24.0D) fled++;
        }
        EXTRA.put("fleeRed0", (double) fleeRed0);
        EXTRA.put("redKilledAfterFlee", fleeRed0 < 0 ? 0.0D : (double) Math.max(0, fleeRed0 - alive));
        EXTRA.put("redFledAlive", (double) fled);
        EXTRA.put("fleeDist0", Math.round(fleeDist0 * 10.0D) / 10.0D);
        EXTRA.put("fleeDistMax", Math.round(fleeDistMax * 10.0D) / 10.0D);
        EXTRA.put("fleeChaseMax", (double) fleeChaseMax);
        EXTRA.put("fleeChaseSec", (double) fleeChaseSec);
    }

    @javax.annotation.Nullable
    private static Vec3 centreOf(List<LivingEntity> side) {
        double x = 0, y = 0, z = 0;
        int n = 0;
        for (LivingEntity e : side) {
            if (!e.isAlive()) continue;
            x += e.getX();
            y += e.getY();
            z += e.getZ();
            n++;
        }
        return n == 0 ? null : new Vec3(x / n, y / n, z / n);
    }

    /** The test plans: one HOLD phase at blue's spot (or an assault on red) plus the link under test. */
    /** E3-COORD: the acting unit chosen for the link under test (null = U2). */
    @javax.annotation.Nullable
    private static String linkTo;

    /**
     * F168 (O7), tag {@code ambushwait:L:B}: the ambusher's wait point = blue's centre + L blocks to the LEFT of the
     * blue -> red axis (the same {@code left = (axis.z, 0, -axis.x)} PlanV2.ambush strikes from with side LEFT) and B
     * blocks back. Null without the tag or without an enemy.
     */
    @javax.annotation.Nullable
    private static Vec3 ambushWait(Vec3 c, @javax.annotation.Nullable Vec3 enemy) {
        double[] aw = tagNums("ambushwait");
        if (aw[0] < 0 || enemy == null) return null;
        Vec3 axis = enemy.subtract(c).multiply(1, 0, 1);
        if (axis.lengthSqr() < 1.0E-4D) return null;
        axis = axis.normalize();
        Vec3 left = new Vec3(axis.z, 0, -axis.x);
        return c.add(left.scale(aw[0])).subtract(axis.scale(Math.max(0, aw[1])));
    }

    /** F168: puts the ambusher unit (the link's acting unit, else U2) at its wait point when the plan starts. */
    private static void ambushTeleport(ServerLevel level, String squad, List<Guard> blue, Vec3 c, @javax.annotation.Nullable Vec3 enemy) {
        Vec3 w = ambushWait(c, enemy);
        if (w == null) return;
        String path = linkTo != null ? linkTo : unit(squad, 2);
        net.hfstack.rallyguard.contract.SquadRegistry squads = net.hfstack.rallyguard.contract.SquadRegistry.get(level);
        int i = 0;
        for (Guard g : blue) {
            if (!g.isAlive() || !squads.pathOf(g.getUUID()).map(path::equalsIgnoreCase).orElse(false)) continue;
            double ox = (i % 3 - 1) * 1.5D, oz = (i / 3) * 1.5D;
            g.getNavigation().stop();
            g.teleportTo(w.x + ox, g.getY(), w.z + oz);
            i++;
        }
        EXTRA.put("ambushWaitTeleported", (double) i);
    }

    private static String planJson(String kind, String squad, Vec3 c, @javax.annotation.Nullable Vec3 enemy, float facing) {
        String u1 = unit(squad, 1), u2 = linkTo != null ? linkTo : unit(squad, 2), u3 = unit(squad, 3);
        Vec3 e = enemy == null ? c : enemy;
        String hold = String.format(Locale.ROOT, "{\"objective\":\"HOLD\",\"x\":%.1f,\"z\":%.1f,\"yaw\":%.1f}", c.x, c.z, facing);
        String phases = hold;
        String links = "";
        String extra = "";
        switch (kind) {
            case "assault" -> phases = String.format(Locale.ROOT,
                    "{\"objective\":\"ATTACK_ZONE\",\"x\":%.1f,\"z\":%.1f,\"radius\":12,\"yaw\":%.1f}", e.x, e.z, facing);
            case "tempo" -> phases = String.format(Locale.ROOT,
                    "{\"objective\":\"ATTACK_ZONE\",\"x\":%.1f,\"z\":%.1f,\"radius\":12,\"yaw\":%.1f,\"tempo\":\"%s\"}",
                    e.x, e.z, facing, scenario.tag("tempo").isEmpty() ? "NORMAL" : scenario.tag("tempo").toUpperCase(Locale.ROOT));
            case "fill" -> extra = ",\"importance\":{\"" + u1 + "\":90,\"" + u3 + "\":10},\"fill\":{\"" + u1 + "\":[\"" + u3 + "\"]}";
            case "ambush" -> {
                // F168: ambushtrig:SEEN = spring when U1 sees the enemy; ambushwait:L:B = a real wait point (wx, wz)
                String trig = "SEEN".equalsIgnoreCase(scenario.tag("ambushtrig"))
                        ? "\"trigger\":\"ENEMY_SEEN\",\"value\":0" : "\"trigger\":\"CONTACT\",\"value\":4";
                Vec3 w = ambushWait(c, enemy);
                String wait = w == null ? "" : String.format(Locale.ROOT, ",\"wx\":\"%.1f\",\"wz\":\"%.1f\"", w.x, w.z);
                links = "{\"type\":\"AMBUSH\",\"from\":\"" + u1 + "\",\"to\":\"" + u2
                        + "\"," + trig + ",\"params\":{\"side\":\"LEFT\"" + wait + "}}";
            }
            case "lane" -> links = "{\"type\":\"OPEN_LANE\",\"from\":\"" + u1 + "\",\"to\":\"" + u2
                    + "\",\"trigger\":\"TIME\",\"value\":3,\"params\":{\"lateral\":\"0\",\"width\":\"4\",\"close\":\"25\"}}";
            case "delay" -> links = "{\"type\":\"DELAY\",\"from\":\"" + u1 + "\",\"to\":\"" + u2
                    + "\",\"trigger\":\"CONTACT\",\"value\":2,\"params\":{\"steps\":\"3\",\"every\":\"6\"}}";
            case "counter" -> links = "{\"type\":\"COUNTERATTACK\",\"from\":\"" + u1 + "\",\"to\":\"" + u2
                    + "\",\"params\":{\"share\":\"0.5\",\"over\":\"8\"}}";
            default -> {
            }
        }
        String scn = scenario.tags().contains("noscn") ? ",\"scenarios\":[" + noScenarios() + "]" : "";
        return "{\"name\":\"sim-" + kind + "\",\"force\":\"" + squad + "\",\"phases\":[" + phases + "],\"links\":[" + links + "]"
                + extra + scn + "}";
    }

    /** Every scenario switched off (a plan WITHOUT scenario responses, for the planner2-* controls). */
    private static String noScenarios() {
        StringBuilder b = new StringBuilder();
        for (net.hfstack.rallyguard.force.Scenarios.Kind k : net.hfstack.rallyguard.force.Scenarios.Kind.values()) {
            if (b.length() > 0) b.append(',');
            b.append("{\"kind\":\"").append(k.name()).append("\",\"active\":false}");
        }
        return b.toString();
    }

    /**
     * E3B S global check (2), fixed in E3-COORD M1: morale, consistency and the rest never change health,
     * damage or speed. Only what RALLY can change is compared - each attribute's base value plus the
     * modifiers whose id is in the {@code rallyguard} namespace - because the full value also moves with
     * equipment (a weapon swap, a shield) and potion effects, which made the old check fail exactly when blue
     * had survivors. No blue survivor = nothing to compare: the metric stays absent (n/a), never "equal".
     */
    public static void attributesCheck(List<Guard> blueAlive) {
        if (ATTRS.isEmpty()) return;
        String[] names = {"health", "damage", "speed"};
        boolean compared = false, same = true;
        for (Guard g : blueAlive) {
            double[] a = ATTRS.get(g.getUUID());
            if (a == null) continue;
            compared = true;
            double[] b = rallyAttrs(g);
            for (int i = 0; i < a.length; i++) {
                if (Math.abs(a[i] - b[i]) > 1.0E-6) {
                    same = false;
                    EXTRA.merge("attrChanged_" + names[i], 1.0D, Double::sum);
                }
            }
        }
        if (compared) EXTRA.put("attributesUnchanged", same ? 1.0D : 0.0D);
    }

    /** Base value + Rally's own modifiers of max health, attack damage and movement speed. */
    private static double[] rallyAttrs(Guard g) {
        java.util.List<net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute>> list = java.util.List.of(
                net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH,
                net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE,
                net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED);
        double[] out = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            net.minecraft.world.entity.ai.attributes.AttributeInstance inst = g.getAttribute(list.get(i));
            if (inst == null) continue;
            double v = inst.getBaseValue();
            for (net.minecraft.world.entity.ai.attributes.AttributeModifier m : inst.getModifiers()) {
                if (net.hfstack.rallyguard.RallyOfTheGuard.MOD_ID.equals(m.id().getNamespace())) v += m.amount();
            }
            out[i] = v;
        }
        return out;
    }

    // ==== F15 (2026-11-14): relay horses and why guards leave the refuge (simulation only) ====

    /** Tag {@code relay:N:D}: the riderless horses SimRunner left behind blue for this run. */
    public static final List<net.minecraft.world.entity.Entity> RELAYS = new java.util.ArrayList<>();
    private static final java.util.Set<UUID> RELAY_MOUNTED = new java.util.HashSet<>();
    private static final java.util.Set<UUID> IN_REFUGE = new java.util.HashSet<>();
    private static boolean orderGiven;
    /** F173 (sim only, telemetry): where each blue guard stood when the general instruction was given. */
    private static final Map<UUID, Vec3> HOLD_AT = new java.util.HashMap<>();

    /** F174 (sim only, telemetry): family at the order (true = bow/crossbow in the main hand, "R"; false = the rest, "I"). */
    private static final Map<UUID, Boolean> HOLD_RANGED = new java.util.HashMap<>();
    /** F174: where each blue guard stood 5 s after the order (the settled line), and the sample that marks it (-1 = none). */
    private static final Map<UUID, Vec3> SETTLE_AT = new java.util.HashMap<>();
    private static int settleSample = -1;

    private static void holdMark(List<? extends LivingEntity> guards) {
        EXTRA.put("orderGiven", 1.0D);
        for (LivingEntity g : guards) {
            if (!g.isAlive()) continue;
            HOLD_AT.putIfAbsent(g.getUUID(), g.position());
            net.minecraft.world.item.Item hand = g.getMainHandItem().getItem();
            HOLD_RANGED.putIfAbsent(g.getUUID(), hand instanceof net.minecraft.world.item.BowItem
                    || hand instanceof net.minecraft.world.item.CrossbowItem);
        }
        if (settleSample < 0) settleSample = samples + 5; // F174: one settled mark, 5 s (5 samples) after the first order
        if (advSample < 0) advSample = samples; // F178: the advance marks count from the first order
    }

    // ==== F178 (2026-11-25, sim only, telemetry): how far and how together blue advances after the order ====

    /** F178: sample of the first order (-1 = none); advance direction (blue centre at the order -> red centre), red centre then. */
    private static int advSample = -1;
    @javax.annotation.Nullable
    private static Vec3 advDir, advRed0;
    /** F182: blue centre at the order (mean of HOLD_AT), fixed with advDir. */
    @javax.annotation.Nullable
    private static Vec3 advBlue0;
    /** F178 marks at 10 / 20 s; F181 adds 30 / 60 / 90 s (seconds after the first order). */
    private static final int[] ADV_MARKS = {10, 20, 30, 60, 90};
    private static final boolean[] ADV_DONE = new boolean[ADV_MARKS.length];
    /** F181: true once the first InstructionRunner "end:*" of this run has been filed (instrEndSec). */
    private static boolean instrEndSeen;

    private static void advReset() {
        advSample = -1;
        advDir = null;
        advRed0 = null;
        advBlue0 = null;
        java.util.Arrays.fill(ADV_DONE, false);
        instrEndSeen = false;
    }

    /**
     * F181 (sim only, telemetry): InstructionRunner ended a unit's instruction; {@code why} is the rf text after "end:"
     * cut to kind:source ("override:ATTACK:FORCE", "empty", "released", ...). Files act:instrEnd:&lt;why&gt; and, for
     * the first one, instrEndSec = seconds since the first order (-1 before it). No-op outside a sim run.
     */
    public static void instrEnd(String why) {
        if (!running()) return;
        EXTRA.merge("act:instrEnd:" + why, 1.0D, Double::sum);
        if (!instrEndSeen) {
            instrEndSeen = true;
            EXTRA.put("instrEndSec", advSample < 0 ? -1.0D : (double) (samples - advSample));
        }
    }

    /** F178: fixes the direction on the first sample with red known (red is still held by redwait), then the 10 s / 20 s marks. */
    private static void advTick(List<LivingEntity> blue) {
        if (advSample < 0 || HOLD_AT.isEmpty()) return;
        if (advDir == null) {
            Vec3 red = centreOf(redSide);
            if (red == null) return;
            double x = 0, z = 0;
            for (Vec3 v : HOLD_AT.values()) {
                x += v.x;
                z += v.z;
            }
            Vec3 d = new Vec3(red.x - x / HOLD_AT.size(), 0, red.z - z / HOLD_AT.size());
            if (d.lengthSqr() < 1.0E-6D) return;
            advDir = d.normalize();
            advRed0 = red;
            advBlue0 = new Vec3(x / HOLD_AT.size(), 0, z / HOLD_AT.size());
        }
        for (int i = 0; i < ADV_MARKS.length; i++) {
            if (!ADV_DONE[i] && samples >= advSample + ADV_MARKS[i]) {
                ADV_DONE[i] = true;
                advMark(ADV_MARKS[i], blue);
            }
        }
    }

    /** F178: adv{m} mean progress p = (pos - HOLD_AT) . advDir, adv{m}Sd, adv{m}Lag (p < 0.5 x median), adv{m}Back (p < 0), adv{m}N, by family R / I too; adv{m}RedMove, adv{m}Gap. */
    private static void advMark(int m, List<LivingEntity> blue) {
        advFamily(m, blue, null);
        advFamily(m, blue, Boolean.TRUE);
        advFamily(m, blue, Boolean.FALSE);
        flkFamily(m, blue, null); // F180
        flkFamily(m, blue, Boolean.TRUE);
        flkFamily(m, blue, Boolean.FALSE);
        flkAngFamily(m, blue, null); // F182
        flkAngFamily(m, blue, Boolean.TRUE);
        flkAngFamily(m, blue, Boolean.FALSE);
        int marked = 0, inInstr = 0; // F181: instrAlive{m} = share of living marked blue guards still under an instruction
        for (LivingEntity b : blue) {
            if (b == null || !b.isAlive() || !HOLD_AT.containsKey(b.getUUID())) continue;
            marked++;
            if (InstructionRunner.simHasGuard(b.getUUID())) inInstr++;
        }
        if (marked > 0) EXTRA.put("instrAlive" + m, (double) inInstr / marked);
        Vec3 red = centreOf(redSide);
        if (red != null && advRed0 != null) {
            double dx = red.x - advRed0.x, dz = red.z - advRed0.z;
            EXTRA.put("adv" + m + "RedMove", Math.sqrt(dx * dx + dz * dz));
            EXTRA.put("adv" + m + "Gap", closest(blue, redSide));
        }
    }

    private static void advFamily(int m, List<LivingEntity> blue, @javax.annotation.Nullable Boolean ranged) {
        String k = "adv" + m, f = ranged == null ? "" : ranged ? "R" : "I";
        List<Double> ps = new java.util.ArrayList<>();
        for (LivingEntity b : blue) {
            if (b == null || !b.isAlive()) continue;
            Vec3 at = HOLD_AT.get(b.getUUID());
            if (at == null) continue;
            if (ranged != null && !ranged.equals(HOLD_RANGED.getOrDefault(b.getUUID(), Boolean.FALSE))) continue;
            ps.add((b.getX() - at.x) * advDir.x + (b.getZ() - at.z) * advDir.z);
        }
        EXTRA.put(k + "N" + f, (double) ps.size());
        if (ps.isEmpty()) return;
        java.util.Collections.sort(ps);
        int n = ps.size();
        double median = n % 2 == 1 ? ps.get(n / 2) : (ps.get(n / 2 - 1) + ps.get(n / 2)) / 2.0D;
        double sum = 0;
        for (double p : ps) sum += p;
        double mean = sum / n, var = 0;
        int lag = 0, back = 0;
        for (double p : ps) {
            var += (p - mean) * (p - mean);
            if (p < 0.5D * median) lag++;
            if (p < 0) back++;
        }
        EXTRA.put(k + f, mean);
        EXTRA.put(k + "Sd" + f, Math.sqrt(var / n));
        EXTRA.put(k + "Lag" + f, (double) lag / n);
        EXTRA.put(k + "Back" + f, (double) back / n);
    }

    /**
     * F180 (sim only, telemetry): lateral spread l = |(pos - HOLD_AT) . perp|, perp = advDir turned 90 degrees in the
     * horizontal plane. flk{m} mean l, flk{m}Lag (l < 0.5 x median l), flk{m}Side (majority-side fraction among those
     * with l >= 3; 0.5 = both sides, 1 = all to one), flk{m}N; by family with suffix R / I.
     */
    private static void flkFamily(int m, List<LivingEntity> blue, @javax.annotation.Nullable Boolean ranged) {
        String k = "flk" + m, f = ranged == null ? "" : ranged ? "R" : "I";
        double px = -advDir.z, pz = advDir.x;
        List<Double> ls = new java.util.ArrayList<>();
        int open = 0, left = 0;
        for (LivingEntity b : blue) {
            if (b == null || !b.isAlive()) continue;
            Vec3 at = HOLD_AT.get(b.getUUID());
            if (at == null) continue;
            if (ranged != null && !ranged.equals(HOLD_RANGED.getOrDefault(b.getUUID(), Boolean.FALSE))) continue;
            double s = (b.getX() - at.x) * px + (b.getZ() - at.z) * pz;
            double l = Math.abs(s);
            ls.add(l);
            if (l >= 3.0D) {
                open++;
                if (s < 0) left++;
            }
        }
        EXTRA.put(k + "N" + f, (double) ls.size());
        if (ls.isEmpty()) return;
        java.util.Collections.sort(ls);
        int n = ls.size();
        double median = n % 2 == 1 ? ls.get(n / 2) : (ls.get(n / 2 - 1) + ls.get(n / 2)) / 2.0D;
        double sum = 0;
        int lag = 0;
        for (double l : ls) {
            sum += l;
            if (l < 0.5D * median) lag++;
        }
        EXTRA.put(k + f, sum / n);
        EXTRA.put(k + "Lag" + f, (double) lag / n);
        if (open > 0) EXTRA.put(k + "Side" + f, (double) Math.max(left, open - left) / open);
    }

    /**
     * F182 (sim only, telemetry): angle in degrees (0-180), seen from the red centre at the order (advRed0), between
     * the blue axis (advBlue0 - advRed0) and the family: flkAng{m} = angle of (family centroid - advRed0),
     * flkAngGuard{m} = mean per-guard angle, flkAngOpen{m} = share of guards with angle >= 25, flkAng{m}N; suffix R / I.
     */
    private static void flkAngFamily(int m, List<LivingEntity> blue, @javax.annotation.Nullable Boolean ranged) {
        if (advRed0 == null || advBlue0 == null) return;
        String k = "flkAng" + m, f = ranged == null ? "" : ranged ? "R" : "I";
        double ax = advBlue0.x - advRed0.x, az = advBlue0.z - advRed0.z;
        if (ax * ax + az * az < 1.0E-6D) return;
        double cx = 0, cz = 0, angSum = 0;
        int n = 0, open = 0;
        for (LivingEntity b : blue) {
            if (b == null || !b.isAlive() || !HOLD_AT.containsKey(b.getUUID())) continue;
            if (ranged != null && !ranged.equals(HOLD_RANGED.getOrDefault(b.getUUID(), Boolean.FALSE))) continue;
            double a = angleDeg(ax, az, b.getX() - advRed0.x, b.getZ() - advRed0.z);
            angSum += a;
            if (a >= 25.0D) open++;
            cx += b.getX();
            cz += b.getZ();
            n++;
        }
        EXTRA.put(k + "N" + f, (double) n);
        if (n == 0) return;
        EXTRA.put(k + f, angleDeg(ax, az, cx / n - advRed0.x, cz / n - advRed0.z));
        EXTRA.put(k.replace("flkAng", "flkAngGuard") + f, angSum / n);
        EXTRA.put(k.replace("flkAng", "flkAngOpen") + f, (double) open / n);
    }

    /** F182: unsigned horizontal angle between two vectors, degrees 0-180 (0 if the second is ~zero). */
    private static double angleDeg(double ax, double az, double bx, double bz) {
        double la = Math.sqrt(ax * ax + az * az), lb = Math.sqrt(bx * bx + bz * bz);
        if (la < 1.0E-6D || lb < 1.0E-6D) return 0.0D;
        double c = (ax * bx + az * bz) / (la * lb);
        return Math.toDegrees(Math.acos(Math.max(-1.0D, Math.min(1.0D, c))));
    }

    /** F182 (sim only, telemetry): which branch of InstructionRunner FLANK_POSITION ran (act:flankSpot:enemy / noEnemy). */
    public static void flankSpot(boolean enemySeen) {
        if (!running()) return;
        EXTRA.merge(enemySeen ? "act:flankSpot:enemy" : "act:flankSpot:noEnemy", 1.0D, Double::sum);
    }

    /** F174: once, 5 s after the order, the position of every living blue guard that was marked at the order. */
    private static void settleTick(List<LivingEntity> blue) {
        if (settleSample < 0 || samples < settleSample || !SETTLE_AT.isEmpty()) return;
        for (LivingEntity b : blue) {
            if (b != null && b.isAlive() && HOLD_AT.containsKey(b.getUUID())) SETTLE_AT.putIfAbsent(b.getUUID(), b.position());
        }
        settleSample = Integer.MAX_VALUE; // done
    }

    /** F173: holdDrift = mean horizontal distance of living blue guards to their spot at the order; holdBroken = share > 6 blocks; holdN = how many.
     *  F174: the same by family (suffix R / I), against the settled spot (settle*), and against the GV post (post*, patrolShare). */
    private static void holdFinish() {
        if (HOLD_AT.isEmpty() || blueSide == null) return;
        driftOf(HOLD_AT, "hold", null);
        driftOf(HOLD_AT, "hold", Boolean.TRUE);
        driftOf(HOLD_AT, "hold", Boolean.FALSE);
        driftOf(SETTLE_AT, "settle", null);
        driftOf(SETTLE_AT, "settle", Boolean.TRUE);
        driftOf(SETTLE_AT, "settle", Boolean.FALSE);
        int alive = 0, posted = 0, broken = 0;
        double sum = 0;
        for (LivingEntity b : blueSide) {
            if (b == null || !b.isAlive()) continue;
            alive++;
            if (!(b instanceof Guard g) || !g.isPatrolling() || g.getPatrolPos() == null) continue;
            net.minecraft.core.BlockPos p = g.getPatrolPos();
            double dx = b.getX() - (p.getX() + 0.5D), dz = b.getZ() - (p.getZ() + 0.5D);
            double d = Math.sqrt(dx * dx + dz * dz);
            posted++;
            sum += d;
            if (d > 6.0D) broken++;
        }
        EXTRA.put("postN", (double) posted);
        if (alive > 0) EXTRA.put("patrolShare", (double) posted / alive);
        if (posted > 0) {
            EXTRA.put("postDrift", sum / posted);
            EXTRA.put("postBroken", (double) broken / posted);
        }
    }

    /** F173/F174: drift of living blue guards to {@code ref}; {@code ranged} null = all, TRUE = family R, FALSE = family I. */
    private static void driftOf(Map<UUID, Vec3> ref, String key, @javax.annotation.Nullable Boolean ranged) {
        String k = ranged == null ? "" : ranged ? "R" : "I";
        int n = 0, broken = 0;
        double sum = 0;
        for (LivingEntity b : blueSide) {
            if (b == null || !b.isAlive()) continue;
            Vec3 at = ref.get(b.getUUID());
            if (at == null) continue;
            if (ranged != null && !ranged.equals(HOLD_RANGED.getOrDefault(b.getUUID(), Boolean.FALSE))) continue;
            double dx = b.getX() - at.x, dz = b.getZ() - at.z;
            double d = Math.sqrt(dx * dx + dz * dz);
            n++;
            sum += d;
            if (d > 6.0D) broken++;
        }
        EXTRA.put(key + "N" + k, (double) n);
        if (n > 0) {
            EXTRA.put(key + "Drift" + k, sum / n);
            EXTRA.put(key + "Broken" + k, (double) broken / n);
        }
    }
    private static UUID blueOwnerId;
    /** F15b (tag {@code relayNoCharge}, with {@code relay:N:D}): a relay rider in the flee drops its target and does not charge. */
    private static boolean relayNoCharge;

    /**
     * F15b (simulation only): true for a guard that mounted a relay horse, is still riding, while the flee is on and the
     * order was given, and only with tag {@code relayNoCharge}. {@code MountDriver.drive} then drops its target
     * ({@code setTarget(null)}, which the combat gate always allows) and steers to its FALLBACK post instead of charging.
     */
    public static boolean relayRiderFleeing(Guard g) {
        return relayNoCharge && orderGiven && InstructionInterpreter.simFlee && !RELAYS.isEmpty()
                && g.isPassenger() && RELAY_MOUNTED.contains(g.getUUID());
    }

    /**
     * Once a second, only with relays and the flee on ({@code retreat:flee}) and after the order came: a blue guard on
     * foot within 12 blocks of a free mount takes it ({@code GuardMounts.mountNearestFree}, the Ledger button's rules)
     * and goes on fleeing mounted (the mount driver steers to the guard's FALLBACK post). Counts {@code relayMounted}.
     */
    private static void relayTick(List<LivingEntity> blue) {
        if (RELAYS.isEmpty() || !orderGiven || !InstructionInterpreter.simFlee) return;
        for (LivingEntity b : blue) {
            if (!(b instanceof Guard g) || !g.isAlive() || g.isPassenger()) continue;
            if (net.hfstack.rallyguard.order.GuardMounts.mountNearestFree(g, blueOwnerId)) {
                if (RELAY_MOUNTED.add(g.getUUID())) EXTRA.merge("relayMounted", 1.0D, Double::sum);
            }
        }
    }

    /** A blue guard that was in the refuge (≤ 8 blocks) and is now out of it: {@code refugeLeft} + its cause. */
    private static void refugeLeaves(List<LivingEntity> blue, Vec3 rp) {
        for (LivingEntity b : blue) {
            if (!b.isAlive()) continue;
            boolean in = b.position().multiply(1, 0, 1).distanceTo(rp.multiply(1, 0, 1)) <= 8.0D;
            if (in) {
                IN_REFUGE.add(b.getUUID());
            } else if (IN_REFUGE.remove(b.getUUID())) {
                EXTRA.merge("refugeLeft", 1.0D, Double::sum);
                LivingEntity t = b instanceof net.minecraft.world.entity.Mob m ? m.getTarget() : null;
                String cause = t != null && t.isAlive()
                        ? "target:" + net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(t.getType()).getPath()
                        : net.hfstack.rallyguard.tactics.Tactics.lastRungName(b.getUUID());
                EXTRA.merge("refugeLeaveCause:" + (cause.isEmpty() ? "none" : cause), 1.0D, Double::sum);
            }
        }
    }

    /** F5: at the end of a run with a refuge - who got there alive, who is out of contact, how long it took. */
    public static void refugeEnd(List<LivingEntity> blue, List<LivingEntity> red) {
        Vec3 rp = InstructionRunner.retreatPoint;
        if (rp == null || blue.isEmpty()) return;
        int start = blue.size(), there = 0, free = 0, in20 = 0;
        double t = 0;
        for (LivingEntity b : blue) {
            if (!b.isAlive()) continue;
            double toRefuge = b.position().multiply(1, 0, 1).distanceTo(rp.multiply(1, 0, 1));
            if (toRefuge <= 8.0D) there++;
            if (toRefuge <= 20.0D) in20++; // F27: the flee column is 17.5 blocks long, longer than the 8 of atRefuge
            boolean hunted = false;
            for (LivingEntity e : red) if (e.isAlive() && e instanceof net.minecraft.world.entity.Mob m && m.getTarget() == b) hunted = true;
            if (!hunted) free++;
        }
        for (int v : REFUGE_TIME.values()) t += v;
        EXTRA.put("atRefuge", Math.round(100.0D * there / start) / 100.0D);
        EXTRA.put("refugeIn20", Math.round(100.0D * in20 / start) / 100.0D); // F27: share of the starting guards, like atRefuge
        EXTRA.put("outOfContact", Math.round(100.0D * free / start) / 100.0D);
        EXTRA.put("refugeSeconds", REFUGE_TIME.isEmpty() ? -1 : Math.round(t / REFUGE_TIME.size() * 10) / 10.0D);
        EXTRA.put("reachedRefuge", (double) REFUGE_TIME.size() / start);
        if (!RELAYS.isEmpty()) { // F15: always written with relays, so 0 is visible
            EXTRA.putIfAbsent("relayMounted", 0.0D);
            EXTRA.putIfAbsent("refugeLeft", 0.0D);
            if (relayNoCharge) EXTRA.putIfAbsent("relayDropTarget", 0.0D); // F15b
        }
    }

    /** F43: "big=12" -> 12; anything else (or no tag) -> 0, the game's cap. */
    private static int bigCap(String tag) {
        if (!tag.startsWith("big=")) return 0;
        try {
            return Math.max(0, Integer.parseInt(tag.substring(4).trim()));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /** F34: "big=10-16" -> {10, 16}; anything else (or no tag) -> null, today's rule. */
    @javax.annotation.Nullable
    private static double[] kiteBig(String tag) {
        if (!tag.startsWith("big=")) return null;
        String[] p = tag.substring(4).split("-");
        if (p.length != 2) return null;
        try {
            double near = Double.parseDouble(p[0]), far = Double.parseDouble(p[1]);
            return near > 0 && far > near ? new double[]{near, far} : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void finish(SimRun r) {
        holdFinish(); // F173: before blueSide is cleared
        HOLD_AT.clear();
        HOLD_RANGED.clear(); // F174
        SETTLE_AT.clear();
        settleSample = -1;
        advReset(); // F178
        if (highGroundY != Integer.MIN_VALUE && highAlive > 0) EXTRA.put("blueHighFrac", (double) highUp / highAlive); // F115
        highGroundY = Integer.MIN_VALUE;
        pursuitEnd(); // F87: before redSide is cleared
        resetPursuit();
        net.hfstack.rallyguard.combat.FieldManual.simKiteBig = null; // F34: never outlives its run
        net.hfstack.rallyguard.combat.FieldManual.simKiteBigOff = false; // F48
        net.hfstack.rallyguard.order.Engagement.simBigCap = 0; // F43: never outlives its run
        net.hfstack.rallyguard.force.SituationReports.simSliceReports = false; // F60: never outlives its run
        net.hfstack.rallyguard.force.Leadership.simOnePassUnitXp = true; // F61, F74: back to the default outlives its run
        net.hfstack.rallyguard.force.UnitOrders.simUnitIndex = true; // F62, F74: back to the default outlives its run
        net.hfstack.rallyguard.tactics.Tactics.simCloserYield = true; // F176: back to the default outlives its run
        if (net.hfstack.rallyguard.combat.Breach.active()) { // F49/F53: doors the rule opened still open at the end
            // F53: one close pass without the 100-tick wait first (in the game: the next 5 s)
            try {
                net.hfstack.rallyguard.combat.Breach.closeAllNow();
            } catch (RuntimeException e) {
                com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] Breach.closeAllNow failed", e);
            }
            EXTRA.put("aldea:doorsOpenEnd", (double) net.hfstack.rallyguard.combat.Breach.openRemembered());
            if (!net.hfstack.rallyguard.combat.Breach.simNoClose) EXTRA.putIfAbsent("act:breachClose", 0.0D);
        }
        if (net.hfstack.rallyguard.combat.Breach.active() && blueSide != null && !BLUE_RANGED.isEmpty()) {
            // F64: blue alive at the end by the role each had at first sight (ranged weapon or not), and their starts
            int startR = 0, startM = 0, survR = 0, survM = 0;
            for (boolean ranged : BLUE_RANGED.values()) {
                if (ranged) startR++;
                else startM++;
            }
            for (LivingEntity b : blueSide) {
                Boolean ranged = BLUE_RANGED.get(b.getUUID());
                if (ranged == null || !b.isAlive()) continue;
                if (ranged) survR++;
                else survM++;
            }
            EXTRA.put("blue.startRanged", (double) startR);
            EXTRA.put("blue.startMelee", (double) startM);
            EXTRA.put("blue.survRanged", (double) survR);
            EXTRA.put("blue.survMelee", (double) survM);
            EXTRA.putIfAbsent("act:breachRanged", 0.0D);
            EXTRA.putIfAbsent("act:breachMelee", 0.0D);
            if (net.hfstack.rallyguard.combat.Breach.simMeleeFirst) EXTRA.putIfAbsent("act:breachYield", 0.0D);
        }
        if (deathProbe) { // F66: zeros so a run with no deaths still reads 0
            EXTRA.putIfAbsent("dn:R", 0.0D);
            EXTRA.putIfAbsent("dn:M", 0.0D);
        }
        deathProbe = false; // F66: never outlives its run
        splitProbe = false; // F161: never outlives its run
        unitMark = false; // F185: never outlives its run
        SPLIT_POS.clear();
        BLUE_RANGED.clear();
        blueSide = null;
        net.hfstack.rallyguard.combat.Breach.simMeleeFirst = false; // F64
        net.hfstack.rallyguard.combat.Breach.reset(false); // F46: never outlives its run
        net.hfstack.rallyguard.combat.Breach.simOff = false; // F53
        net.hfstack.rallyguard.combat.Breach.simNoClose = false; // F53
        net.hfstack.rallyguard.combat.FieldManual.simCoverOff = false; // F73: never outlives its run
        if (griefServer != null) { // F33: mobGriefing back as it was
            gamerule(griefServer, "mobGriefing true");
            griefServer = null;
        }
        if (hq != null && hqLevel != null) {
            // the test HQ goes with the run (it would otherwise stay in the simulation commander's list)
            net.hfstack.rallyguard.contract.HqRegistry.get(hqLevel).delete(hq);
        }
        hq = null;
        hqLevel = null;
        for (String c : COUNTERS) EXTRA.put(c, (double) net.hfstack.rallyguard.debug.SimProbe.counted(c, 0));
        EXTRA.put("convertedToRed", (double) net.hfstack.rallyguard.debug.SimProbe.counted("converted", 1)); // E3-COORD
        if (SimGvConfig.creepersAllowed()) EXTRA.put("gvCreepers", 1.0D); // torneo: guards could attack creepers
        if (SimGvConfig.shieldAlways()) EXTRA.put("gvShieldAlways", 1.0D);
        double clearance = SimGvConfig.clearance(-1.0D);
        if (clearance >= 0.0D) EXTRA.put("clearance", clearance);
        // torneo 2026-11-04: "act:" = what the guards actually DID (shots by arc, shots not taken and why, shield
        // blocks), so a winning run can be described by its behaviour and not only by the response's label.
        for (String pre : new String[]{"linkSkipped:", "linkStep:", "death:", "act:", "kit:", "uo:"}) { // E3-COORD M2
            for (Map.Entry<String, Long> e : net.hfstack.rallyguard.debug.SimProbe.countedWithPrefix(pre, 0).entrySet()) {
                EXTRA.put(e.getKey(), (double) e.getValue());
            }
        }
        // F167: unit orders applied to red guards ("uoRed:SOURCE:KIND"), and red's reorder counters
        for (Map.Entry<String, Long> e : net.hfstack.rallyguard.debug.SimProbe.countedWithPrefix("uo:", 1).entrySet()) {
            EXTRA.put("uoRed:" + e.getKey().substring(3), (double) e.getValue());
        }
        // F172: the FORCE rule behind each unit order applied ("uoRule:<id>" blue, "uoRuleRed:<id>" red)
        for (int side = 0; side < 2; side++) {
            for (Map.Entry<String, Long> e : net.hfstack.rallyguard.debug.SimProbe.countedWithPrefix("uoRule:", side).entrySet()) {
                EXTRA.put((side == 0 ? "uoRule:" : "uoRuleRed:") + e.getKey().substring(7), (double) e.getValue());
            }
        }
        for (String c : new String[]{"reorderFreed", "reorderPushed"}) {
            long n = net.hfstack.rallyguard.debug.SimProbe.counted(c, 1);
            if (n > 0) EXTRA.put(c + "Red", (double) n);
        }
        planUnitsFinish(); // F164 (after the linkSkipped: counters)
        for (net.hfstack.rallyguard.force.Scenarios.Kind k : net.hfstack.rallyguard.force.Scenarios.Kind.values()) {
            long n = net.hfstack.rallyguard.debug.SimProbe.counted("scn:" + k.name(), 0);
            if (n > 0) EXTRA.put("scn_" + k.name(), (double) n);
        }
        String expected = scenario == null ? "" : scenario.tag("expectscn").toUpperCase(Locale.ROOT);
        if (!expected.isEmpty()) {
            EXTRA.put("scnDetected", net.hfstack.rallyguard.debug.SimProbe.counted("scn:" + expected, 0) > 0 ? 1.0D : 0.0D);
        }
        if (firstContact >= 0) EXTRA.put("firstContactSeconds", (double) firstContact);
        if (firstKnown >= 0) EXTRA.put("firstKnownSeconds", (double) firstKnown);
        net.hfstack.rallyguard.debug.sim.SimSampler.stop(EXTRA); // F201: smp* keys (nothing without sample:<ms>)
        if (perfStart != null) { // E3B S: ms/tick per system during this run
            long[] now = net.hfstack.rallyguard.debug.Perf.snapshot();
            net.hfstack.rallyguard.debug.Perf.Sys[] sys = net.hfstack.rallyguard.debug.Perf.Sys.values();
            long ticks = Math.max(1, now[sys.length] - perfStart[sys.length]);
            if (now[sys.length] >= perfStart[sys.length]) {
                for (net.hfstack.rallyguard.debug.Perf.Sys sy : sys) {
                    double ms = (now[sy.ordinal()] - perfStart[sy.ordinal()]) / 1.0e6 / ticks;
                    if (ms > 0) EXTRA.put("perf_" + sy.name(), ms);
                    long worst = net.hfstack.rallyguard.debug.Perf.runWorstNanos(sy); // F56
                    if (worst > 0) EXTRA.put("perfPeak_" + sy.name(), worst / 1.0e6);
                }
                long srv = now[sys.length + 1] - perfStart[sys.length + 1]; // F56: whole server, ms per tick
                if (srv > 0) EXTRA.put("srv_ms", srv / 1.0e6 / ticks);
                long srvPeak = net.hfstack.rallyguard.debug.Perf.serverRunWorstNanos();
                if (srvPeak > 0) EXTRA.put("srv_peakMs", srvPeak / 1.0e6);
                net.hfstack.rallyguard.debug.Perf.runExtras(EXTRA); // F58: steady-state percentiles, GC ticks, peak breakdown
                net.hfstack.rallyguard.debug.Perf.applyExtras(EXTRA, ticks); // F141: LADDER_APPLY per rung
            }
            net.hfstack.rallyguard.debug.Perf.endRun(); // F58: run mode off between runs and in normal play
            perfStart = null;
        }
        for (Map.Entry<String, Double> e : net.hfstack.rallyguard.debug.SimProbe.xpSources(0).entrySet()) {
            EXTRA.put("xpSrc_" + e.getKey(), e.getValue()); // E3B V9
        }
        redSide = List.of();
        redHeld = false;
        redFleeing = false;
        samples = 0;
        firstContact = -1;
        double minutes = Math.max(1, r.ticks) / 1200.0D;
        if (r.start[0] > 0) EXTRA.put("rungChangesPerGuardMinute", EXTRA.get("rungChanges") / r.start[0] / minutes);
        if (firstBrokenTick >= 0) EXTRA.put("firstBrokenSeconds", firstBrokenTick / 20.0D);
        if (!Double.isNaN(brokenAt60)) EXTRA.put("brokenShareAt60", brokenAt60);
        EXTRA.put("brokenShareMax", brokenMax);
        EXTRA.put("shatteredReturned", (double) shatteredReturned);
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
        unitFinish();
        r.extra.putAll(EXTRA);
        scenario = null;
        instruction = "";
    }

    // ==== F16: per-unit instructions (sim only, tag unitInstr) ====

    /** One entry of {@code unitInstr}: unit k gets instruction {@code name} at {@code at} s. */
    private record UnitEntry(int unit, String name, int at) {
    }

    private static final List<UnitEntry> UNIT_PLAN = new java.util.ArrayList<>();
    private static final java.util.Set<UnitEntry> UNIT_DONE = new java.util.HashSet<>();
    /** Units that already received an entry of their own (the general instruction no longer reaches them). */
    private static final java.util.Set<Integer> UNIT_OWN = new java.util.HashSet<>();
    private static final Map<UUID, Integer> UNIT_OF = new java.util.HashMap<>();
    private static final Map<Integer, Integer> UNIT_FIRST_CONTACT = new java.util.TreeMap<>();
    private static final Map<Integer, double[]> UNIT_NUM = new java.util.TreeMap<>(); // dealt, taken, distSum, distN
    private static final Map<Integer, Vec3> UNIT_START = new java.util.HashMap<>();
    private static final Map<String, Long> UNIT_ACT = new java.util.TreeMap<>();
    private static final List<Guard> UNIT_GUARDS = new java.util.ArrayList<>();
    private static ServerLevel unitLevel;
    private static UUID unitOwner;
    private static float unitYaw;
    /** True when the simulation itself put blue into units for unitInstr (SimRunner disbands the squad after the run). */
    private static boolean unitSquads;

    /**
     * {@code unitInstr:U1=NAME@T,U2=NAME@T,...} - unit k ({@code unit:N} guards each, default 8, in spawn order) gets
     * instruction NAME ({@link InstructionInterpreter#parse}) at T s (default 0); a unit may have several entries (e.g.
     * {@code U3=ESPERAR@0,U3=FLANQUEAR@20}); a unit without one follows the general {@code instruction}. The extra name
     * {@code ESPERAR} (sim only, not an instruction of the game): the unit stands posted where it is, takes no target
     * and only answers whoever hits it (the AMBUSH hold-back of PlanV2), and is never driven.
     */
    private static void unitBegin(String spec) {
        UNIT_PLAN.clear();
        UNIT_DONE.clear();
        UNIT_OWN.clear();
        UNIT_OF.clear();
        UNIT_FIRST_CONTACT.clear();
        UNIT_NUM.clear();
        UNIT_START.clear();
        UNIT_ACT.clear();
        UNIT_GUARDS.clear();
        InstructionRunner.SIM_REAL_BOUNDS.clear(); // F16c
        unitLevel = null;
        unitOwner = null;
        unitSquads = false;
        if (spec.isEmpty()) return;
        for (String part : spec.split(",")) {
            String p = part.trim().toUpperCase(Locale.ROOT);
            int eq = p.indexOf('=');
            if (!p.startsWith("U") || eq < 2) continue;
            try {
                int k = Integer.parseInt(p.substring(1, eq));
                String rest = p.substring(eq + 1);
                int at = rest.indexOf('@');
                String name = at < 0 ? rest : rest.substring(0, at);
                int t = at < 0 ? 0 : Integer.parseInt(rest.substring(at + 1));
                if (k >= 1 && !name.isEmpty()) UNIT_PLAN.add(new UnitEntry(k, name, Math.max(0, t)));
            } catch (NumberFormatException ignored) {
                // a malformed entry is skipped
            }
        }
    }

    /** True once per run that used unitInstr without a "force" (SimRunner then disbands the sim squad). */
    public static boolean unitSquadsUsed() {
        boolean u = unitSquads;
        unitSquads = false;
        return u;
    }

    private static void unitSetup(ServerLevel level, UUID owner, List<Guard> blue, float yaw) {
        if (UNIT_PLAN.isEmpty()) return;
        int size = 8;
        try {
            if (!scenario.tag("unit").isEmpty()) size = Math.max(1, Integer.parseInt(scenario.tag("unit")));
        } catch (NumberFormatException ignored) {
            size = 8;
        }
        String squad = net.hfstack.rallyguard.debug.SimRunner.SIM_SQUAD;
        boolean assign = !scenario.tags().contains("force"); // a force scenario already split blue the same way
        net.hfstack.rallyguard.contract.SquadRegistry squads = net.hfstack.rallyguard.contract.SquadRegistry.get(level);
        for (int i = 0; i < blue.size(); i++) {
            Guard g = blue.get(i);
            int k = i / size + 1;
            if (assign) squads.assign(g.getUUID(), owner, unit(squad, k));
            UNIT_OF.put(g.getUUID(), k);
            UNIT_GUARDS.add(g);
            UNIT_NUM.putIfAbsent(k, new double[4]);
        }
        unitSquads = assign;
        for (Map.Entry<Integer, double[]> e : UNIT_NUM.entrySet()) {
            List<Guard> m = unitMembers(e.getKey());
            if (!m.isEmpty()) UNIT_START.put(e.getKey(), InstructionRunner.centre(m));
        }
        unitLevel = level;
        unitOwner = owner;
        unitYaw = yaw;
    }

    private static List<Guard> unitMembers(int k) {
        List<Guard> out = new java.util.ArrayList<>();
        for (Guard g : UNIT_GUARDS) if (g.isAlive() && UNIT_OF.getOrDefault(g.getUUID(), 0) == k) out.add(g);
        return out;
    }

    /** Starts every entry that is due (at most once each). */
    private static void unitTick() {
        if (UNIT_PLAN.isEmpty() || unitLevel == null) return;
        for (UnitEntry e : UNIT_PLAN) {
            if (e.at() > samples || !UNIT_DONE.add(e)) continue;
            List<Guard> m = unitMembers(e.unit());
            UNIT_OWN.add(e.unit());
            if (m.isEmpty()) continue;
            Vec3 c = InstructionRunner.centre(m);
            Vec3 enemy = redSide.isEmpty() ? null : centreOf(redSide);
            float facing = enemy == null ? unitYaw : InstructionRunner.yaw(c, enemy);
            if (e.name().equals("ESPERAR")) {
                InstructionRunner.release(m);
                InstructionRunner.holdBack(m, true);
                for (Guard g : m) {
                    g.setTarget(null);
                    net.hfstack.rallyguard.order.GuardModes.postHere(g);
                }
            } else {
                InstructionInterpreter.Instruction i = InstructionInterpreter.parse(e.name());
                if (i == null) {
                    EXTRA.put("unitInstrBad", 1.0D);
                    continue;
                }
                InstructionRunner.start(unitLevel, unitOwner, m, i, c, facing);
                if (unitMark) { // F185: holdMark is cumulative (putIfAbsent per guard; sample marks only the first time)
                    holdMark(m);
                    EXTRA.put("u" + e.unit() + "Family", (double) GroupProfile.of(m).family().ordinal());
                }
                // F16c: a per-unit RETIRARSE bounds with FALLBACK (real movement); see InstructionRunner.SIM_REAL_BOUNDS
                String key = net.hfstack.rallyguard.force.UnitOrders.unitKeyOf(unitLevel, m.get(0));
                if (key != null) {
                    if (i == InstructionInterpreter.Instruction.RETIRARSE) InstructionRunner.SIM_REAL_BOUNDS.add(key);
                    else InstructionRunner.SIM_REAL_BOUNDS.remove(key);
                    // F30: U3 and U4 flank on opposite sides (odd unit = +1, even = -1); read only under flank:fixed
                    InstructionRunner.SIM_FLANK_SIDE.put(key, e.unit() % 2 == 1 ? 1 : -1);
                }
            }
            EXTRA.put("u" + e.unit() + "OrderAt:" + e.name(), (double) samples);
        }
    }

    /** Once a second: each unit's closest distance to the enemy. */
    private static void unitSample(List<LivingEntity> red) {
        if (UNIT_NUM.isEmpty()) return;
        for (Map.Entry<Integer, double[]> e : UNIT_NUM.entrySet()) {
            double best = Double.MAX_VALUE;
            for (Guard g : unitMembers(e.getKey())) {
                for (LivingEntity r : red) if (r.isAlive()) best = Math.min(best, g.distanceTo(r));
            }
            if (best == Double.MAX_VALUE) continue;
            e.getValue()[2] += best;
            e.getValue()[3]++;
        }
    }

    /** SimRunner (blue-red damage): first contact, damage dealt and taken per unit. */
    public static void unitDamage(LivingEntity victim, @javax.annotation.Nullable net.minecraft.world.entity.Entity attacker, float amount) {
        if (UNIT_OF.isEmpty()) return;
        Integer kv = UNIT_OF.get(victim.getUUID());
        Integer ka = attacker == null ? null : UNIT_OF.get(attacker.getUUID());
        if (kv != null) {
            UNIT_FIRST_CONTACT.putIfAbsent(kv, samples);
            UNIT_NUM.get(kv)[1] += amount;
        }
        if (ka != null) {
            UNIT_FIRST_CONTACT.putIfAbsent(ka, samples);
            UNIT_NUM.get(ka)[0] += amount;
        }
    }

    /** SimProbe: an "act:" counter of a blue guard, also kept per unit ("u2:act:..."). */
    public static void unitAct(net.minecraft.world.entity.Entity e, String key, long n) {
        if (UNIT_OF.isEmpty() || e == null) return;
        Integer k = UNIT_OF.get(e.getUUID());
        if (k != null) UNIT_ACT.merge("u" + k + ":" + key, n, Long::sum);
    }

    /** F16e: true while a unitInstr run tracks units (always false in the game). */
    public static boolean unitTracking() {
        return !UNIT_OF.isEmpty();
    }

    /** F16d: an "rf:" counter of a RETREAT_FIGHTING unit (InstructionRunner), filed as "u2:rf:..." by one member's id. */
    public static void unitRf(UUID member, String key, long n) {
        if (UNIT_OF.isEmpty() || member == null || n == 0) return;
        Integer k = UNIT_OF.get(member);
        if (k != null) UNIT_ACT.merge("u" + k + ":" + key, n, Long::sum);
    }

    private static void unitFinish() {
        if (UNIT_NUM.isEmpty()) return;
        for (Map.Entry<Integer, double[]> e : UNIT_NUM.entrySet()) {
            int k = e.getKey();
            double[] v = e.getValue();
            int start = 0;
            for (Guard g : UNIT_GUARDS) if (UNIT_OF.getOrDefault(g.getUUID(), 0) == k) start++;
            List<Guard> alive = unitMembers(k);
            if (!alive.isEmpty() && unitLevel != null) { // F16d: the unit's RETIRARSE still running when the fight ended
                String key = net.hfstack.rallyguard.force.UnitOrders.unitKeyOf(unitLevel, alive.get(0));
                if (key != null && InstructionRunner.instructionOf(key) == InstructionInterpreter.Instruction.RETIRARSE) {
                    EXTRA.put("u" + k + ":rf:running", 1.0D);
                }
            }
            EXTRA.put("u" + k + "Start", (double) start);
            EXTRA.put("u" + k + "Alive", (double) alive.size());
            EXTRA.put("u" + k + "FirstContactS", (double) UNIT_FIRST_CONTACT.getOrDefault(k, -1)); // -1 = never
            EXTRA.put("u" + k + "DmgDealt", v[0]);
            EXTRA.put("u" + k + "DmgTaken", v[1]);
            if (v[3] > 0) EXTRA.put("u" + k + "MeanEnemyDist", v[2] / v[3]);
            Vec3 s0 = UNIT_START.get(k);
            if (s0 != null && !alive.isEmpty()) {
                Vec3 c = InstructionRunner.centre(alive);
                EXTRA.put("u" + k + "Moved", Math.sqrt(Math.pow(c.x - s0.x, 2) + Math.pow(c.z - s0.z, 2)));
            }
        }
        for (Map.Entry<String, Long> e : UNIT_ACT.entrySet()) EXTRA.put(e.getKey(), (double) e.getValue());
    }
}
