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
        MORALE, INSTRUCTIONS
    }

    /** E3B S: cumulative nanoseconds per system and the tick count, for a per-run breakdown (copy). */
    public static long[] snapshot() {
        long[] out = new long[N + 1];
        System.arraycopy(TOTAL, 0, out, 0, N);
        out[N] = ticks;
        return out;
    }

    private static final int N = Sys.values().length;
    private static final long[] TOTAL = new long[N];
    private static final long[] CALLS = new long[N];
    private static final long[] CURRENT = new long[N];
    private static final long[] WORST = new long[N];
    private static long ticks;

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

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        ticks++;
        long tickSum = 0;
        int sim = Sys.SIM.ordinal();
        for (int i = 0; i < N; i++) {
            if (CURRENT[i] > WORST[i]) WORST[i] = CURRENT[i];
            if (i != sim) tickSum += CURRENT[i];
            CURRENT[i] = 0;
        }
        lastTickNanos = tickSum;
        allNanos += tickSum;
    }

    /** Report lines since the last report, then resets the window. */
    public static List<String> reportAndReset() {
        List<String> out = new ArrayList<>();
        long t = Math.max(1, ticks);
        double sum = 0;
        for (Sys s : Sys.values()) {
            int i = s.ordinal();
            double avg = TOTAL[i] / 1.0e6 / t;
            sum += avg;
            if (CALLS[i] == 0) continue;
            out.add(String.format(Locale.ROOT, "%-14s %7.3f ms/tick  worst %7.3f ms  calls %d",
                    s.name().toLowerCase(Locale.ROOT), avg, WORST[i] / 1.0e6, CALLS[i]));
        }
        out.add(0, String.format(Locale.ROOT, "Rally total %.3f ms/tick over %d ticks", sum, t));
        java.util.Arrays.fill(TOTAL, 0);
        java.util.Arrays.fill(CALLS, 0);
        java.util.Arrays.fill(WORST, 0);
        ticks = 0;
        return out;
    }
}
