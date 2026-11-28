package net.hfstack.rallyguard.debug;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * E1-E: how much server time RALLY spends per tick, per system ({@code /rally debug perf}).
 * Each instrumented section adds {@code System.nanoTime()} deltas here (two calls, tens of
 * nanoseconds); a server-tick hook closes each tick to track the worst one. The report
 * shows the average ms/tick, the worst tick and the call count since the last report.
 * It measures Rally only - not Guard Villagers' own AI or vanilla entity ticking.
 */
public final class Perf {
    /** Registered once on the game bus for the per-tick close-out. */
    public Perf() {
    }

    public enum Sys {
        STANCE_LOOP, COMBAT_GATE, ENGAGEMENT, FRIEND_INDEX, PACK, RANGED_GOAL, MOUNTS, KAMIKAZE,
        MARKERS, FRIENDLY_FIRE, SIM, SLOTS, LADDER, FORCES,
        /** E3B: morale (per guard, 1 Hz) and the instruction runner (per subgroup, 1 Hz). Nested in the loops above. */
        MORALE, INSTRUCTIONS,
        /** F56: the 4 Hz FieldManual dodge/kite loop, the Breach loop and the once-a-second guard collection
         *  (GuardStanceHandler.onLevelTick); none of them nested in another system. */
        FIELD_MANUAL, BREACH, SCAN,
        /** F60: the three parts of ForceBrain.step, nested in FORCES - units + roles, the report loop, rules/apply. */
        FORCES_UNITS, FORCES_REPORT, FORCES_APPLY,
        /** F61: the four parts of FORCES_UNITS - the loaded walk of Forces.units, its GroupProfile.cached calls, its
         *  captainOf calls + final sort, and Roles.assign in ForceBrain.step (the first three count whoever calls units). */
        FORCES_GROUP, FORCES_PROFILE, FORCES_CAPTAIN, FORCES_ROLES,
        /** F62: inside whoever calls them - Cohesion.of's member walk when its cache expired, both Cohesion.compute
         *  calls, every GroupProfile.of in cached / consistencyNoMorale, and rebuilding the UnitOrders unit index. */
        COHESION_MEMBERS, COHESION_COMPUTE, PROFILE_OF, UNIT_INDEX,
        /** F134: FieldManual.kite per guard (inside FIELD_MANUAL) and the big-enemy cache rebuild (inside KITE). */
        KITE, KITE_SCAN,
        /** F137: the five parts of Tactics.step, nested in LADDER - the squad picture, the TerrainSense block, morale
         *  (state + captainOf + the cohesion line), building the Situation + DecisionLadder.decide, and apply. */
        LADDER_PICTURE, LADDER_TERRAIN, LADDER_MORALE, LADDER_DECIDE, LADDER_APPLY,
        /** F188: STANCE_LOOP's own upkeep split, nested in STANCE_LOOP - FieldManual.second, maintain (with its stance
         *  arguments), VeterancyEvents.second, HqLogistics + HqRelax, and normalize + UnitOrders + WaterSafety. */
        ST_FM2, ST_MAINTAIN, ST_VET, ST_HQ, ST_UPKEEP,
        /** F188: RangedFireGoal.plan() from canUse/canContinueToUse (outside RANGED_GOAL). Rally's own work; F189: now
         *  counted in the per-tick Rally total and its histograms (only SIM stays apart). */
        RANGED_PLAN,
        /** F192: RangedFireGoal.tickInner split, nested in RANGED_GOAL - plan(), setLookAt, move(p) (every 10 ticks) and
         *  fire(p) with the cooldown arithmetic. The rest of RANGED_GOAL (memo check, counters) has no bucket. */
        RG_PLAN, RG_LOOK, RG_MOVE, RG_FIRE
    }

    /** E3B S: cumulative nanoseconds per system and the tick count, for a per-run breakdown (copy). */
    public static long[] snapshot() {
        long[] out = new long[N + 2];
        System.arraycopy(TOTAL, 0, out, 0, N);
        out[N] = ticks;
        out[N + 1] = serverNanos; // F56: whole server tick time (ServerTickEvent.Pre -> Post), cumulative
        return out;
    }

    /** F56: per-run worst tick per system and of the whole server, reset by {@link #beginRun()} (sim).
     *  F58: also switches on run mode (steady-state histograms + JVM pause detection) until {@link #endRun()}. */
    public static void beginRun() {
        java.util.Arrays.fill(RUN_WORST, 0);
        serverRunWorst = 0;
        java.util.Arrays.fill(RALLY_HIST, 0);
        java.util.Arrays.fill(SRV_HIST, 0);
        java.util.Arrays.fill(PEAK_COPY, 0);
        runTicks = regTicks = rallyRegNanos = srvRegNanos = 0;
        rallyGt5 = rallyGt10 = srvGt50 = 0;
        rallyRegMax = srvRegMax = 0;
        gcTicks = gcRegMs = 0;
        rallyPeakNoGc = srvPeakNoGc = 0;
        rallyPeakTick = -1;
        java.util.Arrays.fill(RALLY_LATE_HIST, 0); // F199
        java.util.Arrays.fill(SRV_LATE_HIST, 0);
        lateTicks = rallyLateMax = srvLateMax = 0;
        regimeOpen = false; // F201
        java.util.Arrays.fill(APPLY_TOTAL, 0); // F141: per-rung apply cost is run-scoped
        java.util.Arrays.fill(APPLY_CALLS, 0);
        java.util.Arrays.fill(APPLY_CURRENT, 0);
        java.util.Arrays.fill(APPLY_RUN_WORST, 0);
        gcPreValid = false;
        anaN = 0; // F195
        shotsCur = 0;
        plansPrev = CALLS[Sys.RG_PLAN.ordinal()] + CALLS[Sys.RANGED_PLAN.ordinal()];
        if (gcBeans == null) {
            try { // standard Java (java.management), not a Minecraft API; fail soft = no GC detection (gcTicks stays 0)
                gcBeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
            } catch (Throwable t) {
                gcBeans = List.of();
                com.mojang.logging.LogUtils.getLogger().warn("[rallyguard] GC beans unavailable, F58 GC ticks not detected", t);
            }
        }
        runActive = true;
    }

    /** F58: the sim run is over - run mode off (normal play never pays for the histograms or the GC reads). */
    public static void endRun() {
        runActive = false;
        regimeOpen = false; // F201
        gcPreValid = false;
    }

    /** F58: true for the systems whose sections run INSIDE another measured system (never summed in the total). */
    public static boolean nested(Sys sys) {
        return NESTED[sys.ordinal()];
    }

    /** Worst single tick of {@code sys} since {@link #beginRun()}, in nanoseconds. */
    public static long runWorstNanos(Sys sys) {
        return RUN_WORST[sys.ordinal()];
    }

    /** Worst whole-server tick since {@link #beginRun()}, in nanoseconds. */
    public static long serverRunWorstNanos() {
        return serverRunWorst;
    }

    private static final int N = Sys.values().length;
    private static final long[] TOTAL = new long[N];
    private static final long[] CALLS = new long[N];
    private static final long[] CURRENT = new long[N];
    private static final long[] WORST = new long[N];
    private static long ticks;
    private static final long[] RUN_WORST = new long[N];
    private static long serverStart, serverNanos, serverRunWorst;

    /**
     * F58: sections measured inside another one - PACK, MORALE and LADDER inside STANCE_LOOP (the per-guard slice of
     * GuardStanceHandler.onLevelTick); INSTRUCTIONS and FORCES inside SLOTS (ForceBrain.tick and InstructionRunner.tick,
     * both called only between SLOTS' t0 and its add; InstructionRunner adds to FORCES too). Checked with grep on
     * 2026-11-19: no Perf.add of these five happens anywhere else. They keep their own lines but are never added to
     * the per-tick total (they were counted twice before F58).
     */
    private static final boolean[] NESTED = new boolean[N];

    static {
        for (Sys s : new Sys[]{Sys.PACK, Sys.MORALE, Sys.LADDER, Sys.INSTRUCTIONS, Sys.FORCES,
                Sys.FORCES_UNITS, Sys.FORCES_REPORT, Sys.FORCES_APPLY, // F60: inside FORCES
                Sys.FORCES_GROUP, Sys.FORCES_PROFILE, Sys.FORCES_CAPTAIN, Sys.FORCES_ROLES, // F61: inside FORCES_UNITS
                Sys.COHESION_MEMBERS, Sys.COHESION_COMPUTE, Sys.PROFILE_OF, Sys.UNIT_INDEX, // F62
                Sys.KITE, Sys.KITE_SCAN, // F134: inside FIELD_MANUAL
                Sys.LADDER_PICTURE, Sys.LADDER_TERRAIN, Sys.LADDER_MORALE, Sys.LADDER_DECIDE, Sys.LADDER_APPLY, // F137
                Sys.ST_FM2, Sys.ST_MAINTAIN, Sys.ST_VET, Sys.ST_HQ, Sys.ST_UPKEEP, // F188: inside STANCE_LOOP
                Sys.RG_PLAN, Sys.RG_LOOK, Sys.RG_MOVE, Sys.RG_FIRE}) // F192: inside RANGED_GOAL
            NESTED[s.ordinal()] = true;
    }

    /** F188: systems never summed in the Rally per-tick total (F189: only SIM; RANGED_PLAN counts again). */
    private static boolean apart(int i) {
        return i == Sys.SIM.ordinal();
    }

    // ==== F58 run mode (sim only: between beginRun() and endRun()) ====
    /** Ticks skipped after beginRun() before the steady-state measures start (spawn, chunk loading, JIT). */
    public static final int WARMUP_TICKS = 100;
    private static final long BUCKET_NANOS = 250_000L; // 0.25 ms
    private static final int BUCKETS = 1600;           // F88: up to 400 ms (was 400 = 100 ms), then one overflow bucket
    private static final int[] RALLY_HIST = new int[BUCKETS + 1];
    private static final int[] SRV_HIST = new int[BUCKETS + 1];
    private static final long[] PEAK_COPY = new long[N];
    private static boolean runActive, gcPreValid;
    private static long runTicks, regTicks, rallyRegNanos, srvRegNanos;
    private static long rallyGt5, rallyGt10, srvGt50, rallyRegMax, srvRegMax;
    private static long gcTicks, gcRegMs, gcCountPre, gcTimePre;
    private static long rallyPeakNoGc, srvPeakNoGc, rallyPeakTick;
    private static List<java.lang.management.GarbageCollectorMXBean> gcBeans;
    /** F199 (measure only): the same steady-state percentiles without its first LATE_SKIP ticks (10 s; as p99Early). */
    private static final int LATE_SKIP = 200;
    private static final int[] RALLY_LATE_HIST = new int[BUCKETS + 1];
    private static final int[] SRV_LATE_HIST = new int[BUCKETS + 1];
    private static long lateTicks, rallyLateMax, srvLateMax;
    /** F201: true while the steady state is being measured (read by the SimSampler thread, hence volatile). */
    private static volatile boolean regimeOpen;

    /** F201: the steady state of a sim run is open (after the warm-up, before endRun). */
    public static boolean inRegime() {
        return regimeOpen;
    }

    private static long gcCount() {
        long c = 0;
        for (java.lang.management.GarbageCollectorMXBean b : gcBeans) c += Math.max(0, b.getCollectionCount());
        return c;
    }

    private static long gcTimeMs() {
        long c = 0;
        for (java.lang.management.GarbageCollectorMXBean b : gcBeans) c += Math.max(0, b.getCollectionTime());
        return c;
    }

    private static int bucket(long nanos) {
        return (int) Math.min(BUCKETS, Math.max(0, nanos / BUCKET_NANOS));
    }

    /** Upper edge of the bucket holding quantile q (never above the largest value seen), in ms. */
    private static double pct(int[] hist, long n, double q, long maxNanos) {
        if (n <= 0) return 0;
        long need = Math.max(1, (long) Math.ceil(q * n));
        long c = 0;
        for (int i = 0; i <= BUCKETS; i++) {
            c += hist[i];
            if (c >= need) return Math.min((i + 1) * BUCKET_NANOS, maxNanos) / 1.0e6;
        }
        return maxNanos / 1.0e6;
    }

    /** F58: the run-mode measures as {@code extra.*} keys (SimExtras.finish); nothing when run mode never reached steady state. */
    public static void runExtras(java.util.Map<String, Double> out) {
        out.put("perfRegTicks", (double) regTicks);
        if (regTicks <= 0) return;
        out.put("rally_msReg", rallyRegNanos / 1.0e6 / regTicks);
        out.put("rally_p50", pct(RALLY_HIST, regTicks, 0.50, rallyRegMax));
        out.put("rally_p99", pct(RALLY_HIST, regTicks, 0.99, rallyRegMax));
        out.put("rally_p999", pct(RALLY_HIST, regTicks, 0.999, rallyRegMax));
        out.put("rally_gt5", (double) rallyGt5);
        out.put("rally_gt10", (double) rallyGt10);
        out.put("rally_peakReg", rallyRegMax / 1.0e6);
        out.put("srv_msReg", srvRegNanos / 1.0e6 / regTicks);
        out.put("srv_p50", pct(SRV_HIST, regTicks, 0.50, srvRegMax));
        out.put("srv_p99", pct(SRV_HIST, regTicks, 0.99, srvRegMax));
        out.put("srv_gt50", (double) srvGt50);
        out.put("srv_peakReg", srvRegMax / 1.0e6);
        out.put("gcTicks", (double) gcTicks);
        out.put("gcMs", (double) gcRegMs);
        out.put("rally_peakNoGc", rallyPeakNoGc / 1.0e6);
        anaExtras(out); // F195
        if (lateTicks > 0) { // F199
            out.put("lateTicks", (double) lateTicks);
            out.put("rally_p99_late", pct(RALLY_LATE_HIST, lateTicks, 0.99, rallyLateMax));
            out.put("rally_worst_late", rallyLateMax / 1.0e6);
            out.put("srv_p99_late", pct(SRV_LATE_HIST, lateTicks, 0.99, srvLateMax));
        }
        out.put("srv_peakNoGc", srvPeakNoGc / 1.0e6);
        if (rallyPeakTick < 0) return;
        out.put("rally_peakTick", (double) rallyPeakTick);
        int bestSys = -1, bestNested = -1;
        for (int i = 0; i < N; i++) {
            if (apart(i) || PEAK_COPY[i] <= 0) continue;
            if (NESTED[i]) {
                if (bestNested < 0 || PEAK_COPY[i] > PEAK_COPY[bestNested]) bestNested = i;
            } else if (bestSys < 0 || PEAK_COPY[i] > PEAK_COPY[bestSys]) {
                bestSys = i;
            }
        }
        Sys[] all = Sys.values();
        if (bestSys >= 0) { // a string can't be an extra value: the system name goes in the key (= 1)
            out.put("rally_peakSys_" + all[bestSys].name(), 1.0D);
            out.put("rally_peakSysMs", PEAK_COPY[bestSys] / 1.0e6);
        }
        if (bestNested >= 0) {
            out.put("rally_peakNested_" + all[bestNested].name(), 1.0D);
            out.put("rally_peakNestedMs", PEAK_COPY[bestNested] / 1.0e6);
        }
    }

    public static long t0() {
        return System.nanoTime();
    }

    /** F141: adds an already measured duration (the same delta can then also go to {@link #addApply}). */
    public static void addNanos(Sys sys, long d) {
        int i = sys.ordinal();
        TOTAL[i] += d;
        CALLS[i]++;
        CURRENT[i] += d;
    }

    // ==== F141: Tactics.apply split by rung (measure only; the same delta also goes to LADDER_APPLY) ====
    private static final net.hfstack.rallyguard.tactics.DecisionLadder.Rung[] RUNGS =
            net.hfstack.rallyguard.tactics.DecisionLadder.Rung.values();
    private static final long[] APPLY_TOTAL = new long[RUNGS.length];
    private static final long[] APPLY_CALLS = new long[RUNGS.length];
    private static final long[] APPLY_CURRENT = new long[RUNGS.length];
    private static final long[] APPLY_RUN_WORST = new long[RUNGS.length];

    /** F141: one apply(...) of rung {@code rung} took {@code d} nanoseconds. */
    public static void addApply(net.hfstack.rallyguard.tactics.DecisionLadder.Rung rung, long d) {
        int i = rung.ordinal();
        APPLY_TOTAL[i] += d;
        APPLY_CALLS[i]++;
        APPLY_CURRENT[i] += d;
    }

    /** F141: per-rung keys since {@link #beginRun()} - perf_APPLY_X (ms/tick), perfPeak_APPLY_X (worst tick, ms),
     *  cnt_APPLY_X (applies per tick). Rungs never applied are left out. */
    public static void applyExtras(java.util.Map<String, Double> out, long runTickCount) {
        long t = Math.max(1, runTickCount);
        for (int i = 0; i < RUNGS.length; i++) {
            if (APPLY_CALLS[i] == 0) continue;
            String n = RUNGS[i].name();
            out.put("perf_APPLY_" + n, APPLY_TOTAL[i] / 1.0e6 / t);
            out.put("perfPeak_APPLY_" + n, APPLY_RUN_WORST[i] / 1.0e6);
            out.put("cnt_APPLY_" + n, APPLY_CALLS[i] / (double) t);
        }
    }

    public static void add(Sys sys, long t0) {
        long d = System.nanoTime() - t0;
        int i = sys.ordinal();
        TOTAL[i] += d;
        CALLS[i]++;
        CURRENT[i] += d;
    }

    /** E2A block 3: Rally's whole cost (every system but SIM) in the last closed tick, and in total. */
    private static long lastTickNanos, allNanos;

    public static long lastTickNanos() {
        return lastTickNanos;
    }

    public static long allNanos() {
        return allNanos;
    }

    /** F56: NeoForge 21.1 fires Pre at the start of MinecraftServer#tickServer and Post at its end (all levels inside). */
    @SubscribeEvent
    public void onServerTickStart(ServerTickEvent.Pre event) {
        if (runActive) { // F58: JVM collections that land inside this tick
            gcCountPre = gcCount();
            gcTimePre = gcTimeMs();
            gcPreValid = true;
        }
        serverStart = System.nanoTime();
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        long srvTick = -1;
        if (serverStart != 0) {
            srvTick = System.nanoTime() - serverStart;
            serverNanos += srvTick;
            if (srvTick > serverRunWorst) serverRunWorst = srvTick;
            serverStart = 0;
        }
        ticks++;
        long tickSum = 0;
        for (int i = 0; i < N; i++) {
            if (CURRENT[i] > WORST[i]) WORST[i] = CURRENT[i];
            if (CURRENT[i] > RUN_WORST[i]) RUN_WORST[i] = CURRENT[i];
            if (!apart(i) && !NESTED[i]) tickSum += CURRENT[i]; // F58: own time only (nested sections are inside others)
        }
        if (runActive) {
            long plansNow = CALLS[Sys.RG_PLAN.ordinal()] + CALLS[Sys.RANGED_PLAN.ordinal()]; // F195
            int plans = (int) Math.min(Integer.MAX_VALUE, plansNow - plansPrev);
            plansPrev = plansNow;
            int phase = -1;
            try {
                phase = (int) (event.getServer().overworld().getGameTime() % 20L);
            } catch (RuntimeException ignored) {
                phase = -1;
            }
            anaPending(phase, plans);
            runTick(tickSum, srvTick);
        }
        shotsCur = 0; // F195: shots belong to the tick they were fired in
        java.util.Arrays.fill(CURRENT, 0);
        for (int i = 0; i < APPLY_CURRENT.length; i++) { // F141
            if (APPLY_CURRENT[i] > APPLY_RUN_WORST[i]) APPLY_RUN_WORST[i] = APPLY_CURRENT[i];
            APPLY_CURRENT[i] = 0;
        }
        lastTickNanos = tickSum;
        allNanos += tickSum;
    }

    /** F58: one closed tick in run mode - warm-up skip, histograms, GC-tick detection, the record tick's breakdown. */
    private static void runTick(long rally, long srv) {
        boolean gc = false;
        if (gcPreValid) {
            long dc = gcCount() - gcCountPre;
            if (dc > 0) {
                gc = true;
                if (runTicks >= WARMUP_TICKS) gcRegMs += Math.max(0, gcTimeMs() - gcTimePre);
            }
            gcPreValid = false;
        }
        runTicks++;
        if (runTicks <= WARMUP_TICKS || srv < 0) return;
        regTicks++;
        regimeOpen = true; // F201
        anaRecord(rally, gc); // F195
        if (regTicks > LATE_SKIP) { // F199: steady state without its first 10 s
            lateTicks++;
            RALLY_LATE_HIST[bucket(rally)]++;
            SRV_LATE_HIST[bucket(srv)]++;
            if (rally > rallyLateMax) rallyLateMax = rally;
            if (srv > srvLateMax) srvLateMax = srv;
        }
        rallyRegNanos += rally;
        srvRegNanos += srv;
        RALLY_HIST[bucket(rally)]++;
        SRV_HIST[bucket(srv)]++;
        if (rally > 5_000_000L) rallyGt5++;
        if (rally > 10_000_000L) rallyGt10++;
        if (srv > 50_000_000L) srvGt50++;
        if (rally > rallyRegMax) rallyRegMax = rally;
        if (srv > srvRegMax) srvRegMax = srv;
        if (gc) {
            gcTicks++;
            return;
        }
        if (srv > srvPeakNoGc) srvPeakNoGc = srv;
        if (rally > rallyPeakNoGc) {
            rallyPeakNoGc = rally;
            rallyPeakTick = runTicks;
            System.arraycopy(CURRENT, 0, PEAK_COPY, 0, N);
        }
    }

    // ==== F195 (O8, measure only): anatomy of the worst 1 % of steady-state ticks ====
    /** Arrows fired in the current tick (RangedFireGoal, where fire() returned true). */
    private static int shotsCur;
    private static long plansPrev;
    private static int pendPhase, pendPlans;
    private static int anaN;
    private static long[] anaRally = new long[4096], anaRg = new long[4096], anaStance = new long[4096];
    private static int[] anaShots = new int[4096], anaPlans = new int[4096];
    private static byte[] anaPhase = new byte[4096];
    private static boolean[] anaGc = new boolean[4096];

    /** F195: one arrow left a bow/crossbow this tick (counted only in run mode). */
    public static void shot() {
        if (runActive) shotsCur++;
    }

    private static void anaPending(int phase, int plans) {
        pendPhase = phase;
        pendPlans = plans;
    }

    /** F195: a steady-state tick (called from runTick after the warm-up), before CURRENT is cleared. */
    private static void anaRecord(long rally, boolean gc) {
        if (anaN == anaRally.length) {
            int n = anaN * 2;
            anaRally = java.util.Arrays.copyOf(anaRally, n);
            anaRg = java.util.Arrays.copyOf(anaRg, n);
            anaStance = java.util.Arrays.copyOf(anaStance, n);
            anaShots = java.util.Arrays.copyOf(anaShots, n);
            anaPlans = java.util.Arrays.copyOf(anaPlans, n);
            anaPhase = java.util.Arrays.copyOf(anaPhase, n);
            anaGc = java.util.Arrays.copyOf(anaGc, n);
        }
        int i = anaN++;
        anaRally[i] = rally;
        anaRg[i] = CURRENT[Sys.RANGED_GOAL.ordinal()];
        anaStance[i] = CURRENT[Sys.STANCE_LOOP.ordinal()];
        anaShots[i] = shotsCur;
        anaPlans[i] = pendPlans;
        anaPhase[i] = (byte) pendPhase;
        anaGc[i] = gc;
    }

    /** F195: the worst 1 % = the ceil(1 %) ticks with the largest Rally total (exact, not the histogram's bucket edge)
     *  against all steady-state ticks. Keys p99Shots/shotsMean/shotsTickMax, p99Gc/gcShare, p99Phase/p99PhaseMode
     *  (gameTime % 20 of the overworld, read at the tick's end), p99Early (first 10 s of the steady state),
     *  p99Plans/plansMean, p99RgShare/p99StanceShare, p99N and p99Min (ms). */
    private static void anaExtras(java.util.Map<String, Double> out) {
        int n = anaN;
        if (n <= 0) return;
        long[] sorted = java.util.Arrays.copyOf(anaRally, n);
        java.util.Arrays.sort(sorted);
        int k = (int) Math.max(1, Math.ceil(0.01D * n));
        long thr = sorted[n - k];
        double shotsAll = 0, plansAll = 0, gcAll = 0;
        int shotsMax = 0;
        double wShots = 0, wPlans = 0, wGc = 0, wEarly = 0, wRally = 0, wRg = 0, wSt = 0;
        int w = 0;
        int[] phases = new int[21];
        for (int i = 0; i < n; i++) {
            shotsAll += anaShots[i];
            plansAll += anaPlans[i];
            if (anaGc[i]) gcAll++;
            if (anaShots[i] > shotsMax) shotsMax = anaShots[i];
            if (anaRally[i] < thr) continue;
            w++;
            wShots += anaShots[i];
            wPlans += anaPlans[i];
            if (anaGc[i]) wGc++;
            if (i < 200) wEarly++;
            wRally += anaRally[i];
            wRg += anaRg[i];
            wSt += anaStance[i];
            phases[anaPhase[i] < 0 ? 20 : anaPhase[i]]++;
        }
        int mode = 0;
        for (int p = 1; p < 20; p++) if (phases[p] > phases[mode]) mode = p;
        out.put("p99N", (double) w);
        out.put("p99Min", thr / 1.0e6);
        out.put("shotsMean", shotsAll / n);
        out.put("shotsTickMax", (double) shotsMax);
        out.put("p99Shots", wShots / w);
        out.put("gcShare", gcAll / n);
        out.put("p99Gc", wGc / w);
        out.put("p99Phase", phases[mode] / (double) w);
        out.put("p99PhaseMode", (double) mode);
        out.put("p99Early", wEarly / w);
        out.put("plansMean", plansAll / n);
        out.put("p99Plans", wPlans / w);
        out.put("p99RgShare", wRally > 0 ? wRg / wRally : 0);
        out.put("p99StanceShare", wRally > 0 ? wSt / wRally : 0);
    }

    /** Report lines since the last report, then resets the window. */
    public static List<String> reportAndReset() {
        List<String> out = new ArrayList<>();
        long t = Math.max(1, ticks);
        double sum = 0;
        for (Sys s : Sys.values()) {
            int i = s.ordinal();
            double avg = TOTAL[i] / 1.0e6 / t;
            if (!NESTED[i] && !apart(i)) sum += avg; // F58 (F189: only SIM apart): nested sections are already inside their parent's time
            if (CALLS[i] == 0) continue;
            out.add(String.format(Locale.ROOT, "%-14s %7.3f ms/tick  worst %7.3f ms  calls %d%s",
                    s.name().toLowerCase(Locale.ROOT), avg, WORST[i] / 1.0e6, CALLS[i], NESTED[i] ? "  (nested)" : ""));
        }
        out.add(0, String.format(Locale.ROOT, "Rally total %.3f ms/tick over %d ticks (nested not summed)", sum, t));
        java.util.Arrays.fill(TOTAL, 0);
        java.util.Arrays.fill(CALLS, 0);
        java.util.Arrays.fill(WORST, 0);
        ticks = 0;
        return out;
    }
}
