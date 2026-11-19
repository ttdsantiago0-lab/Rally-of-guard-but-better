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
        FORCES_GROUP, FORCES_PROFILE, FORCES_CAPTAIN, FORCES_ROLES
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
        gcPreValid = false;
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
                Sys.FORCES_GROUP, Sys.FORCES_PROFILE, Sys.FORCES_CAPTAIN, Sys.FORCES_ROLES}) NESTED[s.ordinal()] = true; // F61: inside FORCES_UNITS
    }

    // ==== F58 run mode (sim only: between beginRun() and endRun()) ====
    /** Ticks skipped after beginRun() before the steady-state measures start (spawn, chunk loading, JIT). */
    public static final int WARMUP_TICKS = 100;
    private static final long BUCKET_NANOS = 250_000L; // 0.25 ms
    private static final int BUCKETS = 400;            // up to 100 ms, then one overflow bucket
    private static final int[] RALLY_HIST = new int[BUCKETS + 1];
    private static final int[] SRV_HIST = new int[BUCKETS + 1];
    private static final long[] PEAK_COPY = new long[N];
    private static boolean runActive, gcPreValid;
    private static long runTicks, regTicks, rallyRegNanos, srvRegNanos;
    private static long rallyGt5, rallyGt10, srvGt50, rallyRegMax, srvRegMax;
    private static long gcTicks, gcRegMs, gcCountPre, gcTimePre;
    private static long rallyPeakNoGc, srvPeakNoGc, rallyPeakTick;
    private static List<java.lang.management.GarbageCollectorMXBean> gcBeans;

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
        out.put("srv_peakNoGc", srvPeakNoGc / 1.0e6);
        if (rallyPeakTick < 0) return;
        out.put("rally_peakTick", (double) rallyPeakTick);
        int sim = Sys.SIM.ordinal();
        int bestSys = -1, bestNested = -1;
        for (int i = 0; i < N; i++) {
            if (i == sim || PEAK_COPY[i] <= 0) continue;
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
        int sim = Sys.SIM.ordinal();
        for (int i = 0; i < N; i++) {
            if (CURRENT[i] > WORST[i]) WORST[i] = CURRENT[i];
            if (CURRENT[i] > RUN_WORST[i]) RUN_WORST[i] = CURRENT[i];
            if (i != sim && !NESTED[i]) tickSum += CURRENT[i]; // F58: own time only (nested sections are inside others)
        }
        if (runActive) runTick(tickSum, srvTick);
        java.util.Arrays.fill(CURRENT, 0);
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

    /** Report lines since the last report, then resets the window. */
    public static List<String> reportAndReset() {
        List<String> out = new ArrayList<>();
        long t = Math.max(1, ticks);
        double sum = 0;
        for (Sys s : Sys.values()) {
            int i = s.ordinal();
            double avg = TOTAL[i] / 1.0e6 / t;
            if (!NESTED[i]) sum += avg; // F58: nested sections are already inside their parent's time
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
