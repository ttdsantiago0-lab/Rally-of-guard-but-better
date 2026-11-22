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
        net.hfstack.rallyguard.combat.FireWindows.reset(s.tags().contains("fire:sync") ? net.hfstack.rallyguard.combat.FireWindows.Mode.SYNC
                : s.tags().contains("fire:spread") ? net.hfstack.rallyguard.combat.FireWindows.Mode.SPREAD
                : s.tags().contains("fire:off") ? net.hfstack.rallyguard.combat.FireWindows.Mode.OFF : null); // null = game default
        // F79/F81: blue windows off (was the whole mode OFF; same effect while red has no guards) - F85: red ones apart
        net.hfstack.rallyguard.combat.FireWindows.simOffBlue = noGroup || s.tags().contains("off:group:fire");
        net.hfstack.rallyguard.combat.FireWindows.simOffRed = noGroupRed || s.tags().contains("off:group:red:fire");
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
        hurtTrigger = s.tags().contains("retreat:hurt");
        REFUGE_TIME.clear();
        RELAY_MOUNTED.clear(); // F15
        RELAYS.clear();
        IN_REFUGE.clear();
        orderGiven = false;
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
        net.hfstack.rallyguard.order.Engagement.simBigCap = bigCap(s.tag("cap")); // F43: cap:big=<n>
        InstructionRunner.simFlankFixed = s.tags().contains("flank:fixed"); // F30
        net.hfstack.rallyguard.combat.Breach.simOff = s.tags().contains("off:breach"); // F53 (door:breach is set by SimRunner)
        net.hfstack.rallyguard.combat.Breach.simNoClose = s.tags().contains("door:noclose"); // F53
        net.hfstack.rallyguard.combat.Breach.simMeleeFirst = s.tags().contains("door:meleeFirst"); // F64
        BLUE_RANGED.clear(); // F64
        blueSide = null;
        deathProbe = s.tags().contains("probe:deaths"); // F66
        net.hfstack.rallyguard.force.SituationReports.simSliceReports = s.tags().contains("perf:sliceReports"); // F60
        net.hfstack.rallyguard.force.Leadership.simOnePassUnitXp = !s.tags().contains("off:unitXp"); // F61, F74: ON unless off:unitXp
        net.hfstack.rallyguard.force.UnitOrders.simUnitIndex = !s.tags().contains("off:unitIndex"); // F62, F74: ON unless off:unitIndex
        InstructionRunner.SIM_FLANK_SIDE.clear();
        AttractionProbe.begin(s); // F1
        SimOracle.begin(s); // sonda de techo: oracle:aim|cadence|target|kite|all
        SimCombatStats.begin();
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
        EXTRA.clear();
    }

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
                if (earlyK > 0 || hurtTrigger) delay = Integer.MAX_VALUE; // F69: the trigger, not the clock, gives the order
                if (delay <= 0) {
                    InstructionRunner.start(level, blueOwner, blue, i, blueAt, blueYaw);
                    orderGiven = true; // F15
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
        }
        double refuge = 16.0D;
        try {
            refuge = Double.parseDouble(scenario.tag("refuge"));
        } catch (NumberFormatException ignored) {
            refuge = 16.0D;
        }
        if (seen == 0 || seen < earlyK * alive || nearest > refuge + 14.0D + 8.0D) return;
        EXTRA.put("earlyAt", (double) samples);
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
        if (pendingInstruction != null && (earlyK > 0 || hurtTrigger)) triggerTick(blue, red); // F69
        if (pendingInstruction != null && samples >= (Integer) pendingInstruction[6]) { // F5: the order comes mid-fight
            Object[] a = pendingInstruction;
            pendingInstruction = null;
            List<Guard> alive = new java.util.ArrayList<>();
            for (Guard g : (List<Guard>) a[2]) if (g.isAlive() && !UNIT_OWN.contains(UNIT_OF.getOrDefault(g.getUUID(), 0))) alive.add(g); // F16: a unit already on its own order keeps it
            if (!alive.isEmpty()) InstructionRunner.start((ServerLevel) a[0], (UUID) a[1], alive,
                    (InstructionInterpreter.Instruction) a[3], alive.get(0).position(), (Float) a[5]);
            EXTRA.put("orderAt", (double) samples);
            orderGiven = true; // F15
        }
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

    // ==== Per-tick checks (E3B R3 and later); true = end the run now ====

    private static net.minecraft.world.phys.Vec3 startCentre;

    private static net.hfstack.rallyguard.contract.HqRegistry.Hq hq;
    private static ServerLevel hqLevel;

    public static boolean tick(ServerLevel level, net.minecraft.server.level.ServerPlayer player, int tick, int formTicks,
                               List<Guard> blue) {
        if (scenario == null) return false;
        moraleTick(level, tick, formTicks, blue);
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
            "fills", "reconSightings", "ambushSprung", "ambushDiscovered", "lanePassed", "laneTimedOut", "delaySteps", "pursuitLimited"};

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
                    net.hfstack.rallyguard.force.Plans.execute(level, f, net.hfstack.rallyguard.force.Plans.parse(
                            planJson(kind, squad, c, enemy, facing)));
                    EXTRA.put("planStarted", 1.0D);
                } catch (RuntimeException e) {
                    EXTRA.put("planStarted", 0.0D);
                    com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] sim plan {} rejected: {}", kind, e.toString());
                }
            }
        }
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
            case "ambush" -> links = "{\"type\":\"AMBUSH\",\"from\":\"" + u1 + "\",\"to\":\"" + u2
                    + "\",\"trigger\":\"CONTACT\",\"value\":4,\"params\":{\"side\":\"LEFT\"}}";
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
        pursuitEnd(); // F87: before redSide is cleared
        resetPursuit();
        net.hfstack.rallyguard.combat.FieldManual.simKiteBig = null; // F34: never outlives its run
        net.hfstack.rallyguard.combat.FieldManual.simKiteBigOff = false; // F48
        net.hfstack.rallyguard.order.Engagement.simBigCap = 0; // F43: never outlives its run
        net.hfstack.rallyguard.force.SituationReports.simSliceReports = false; // F60: never outlives its run
        net.hfstack.rallyguard.force.Leadership.simOnePassUnitXp = true; // F61, F74: back to the default outlives its run
        net.hfstack.rallyguard.force.UnitOrders.simUnitIndex = true; // F62, F74: back to the default outlives its run
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
        for (String pre : new String[]{"linkSkipped:", "linkStep:", "death:", "act:", "kit:"}) { // E3-COORD M2
            for (Map.Entry<String, Long> e : net.hfstack.rallyguard.debug.SimProbe.countedWithPrefix(pre, 0).entrySet()) {
                EXTRA.put(e.getKey(), (double) e.getValue());
            }
        }
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
