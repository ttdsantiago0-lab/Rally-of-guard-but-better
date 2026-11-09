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

    /** A value for this run's report ("extra"), e.g. the waves' progress (SimRunner). */
    public static void put(String key, double value) {
        EXTRA.put(key, value);
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
        SimOracle.begin(s); // sonda de techo: oracle:aim|cadence|target|kite|all
        SimCombatStats.begin();
        net.hfstack.rallyguard.combat.Testudo.simOff = s.tags().contains("off:testudo");
        net.hfstack.rallyguard.combat.FieldManual.simNoThreats = s.tags().contains("off:threats");
        net.hfstack.rallyguard.combat.FieldManual.simNoKite = s.tags().contains("off:kite");
        net.hfstack.rallyguard.combat.FieldManual.simNoEnderman = s.tags().contains("off:enderman");
        SHATTERED.clear();
        firstBrokenTick = -1;
        shatteredReturned = 0;
        brokenAt60 = Double.NaN;
        brokenMax = 0;
        instruction = s.tag("instruction").toUpperCase(Locale.ROOT);
        minDistSum = 0;
        minDistN = 0;
        absMin = Double.NaN;
        contactSamples = 0;
        samples = 0;
        firstContact = -1;
        firstKnown = -1;
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
        if (!instruction.isEmpty()) {
            InstructionInterpreter.Instruction i = InstructionInterpreter.parse(instruction);
            if (i != null) {
                InstructionRunner.start(level, blueOwner, blue, i, blueAt, blueYaw);
                EXTRA.put("family", (double) p.family().ordinal());
            }
        }
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

    /** Once a second: closest blue-red distance and contact. */
    public static void sample(List<LivingEntity> blue, List<LivingEntity> red) {
        redSide = red; // E3B P: the planner / scenario hooks move the red side
        if (scenario == null) return;
        samples++;
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
        double[] ku = tagNums("killunit");
        if (ku[0] >= 1 && t == (int) (Math.max(0, ku[1]) * 20)) {
            String path = unit(squad, (int) ku[0]);
            net.hfstack.rallyguard.contract.SquadRegistry squads = net.hfstack.rallyguard.contract.SquadRegistry.get(level);
            for (Guard g : blue) {
                if (squads.pathOf(g.getUUID()).map(path::equalsIgnoreCase).orElse(false)) g.kill();
            }
        }
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

    public static void finish(SimRun r) {
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
                }
            }
            perfStart = null;
        }
        for (Map.Entry<String, Double> e : net.hfstack.rallyguard.debug.SimProbe.xpSources(0).entrySet()) {
            EXTRA.put("xpSrc_" + e.getKey(), e.getValue()); // E3B V9
        }
        redSide = List.of();
        redHeld = false;
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
        r.extra.putAll(EXTRA);
        scenario = null;
        instruction = "";
    }
}
