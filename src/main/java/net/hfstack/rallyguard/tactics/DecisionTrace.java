package net.hfstack.rallyguard.tactics;

import net.hfstack.rallyguard.config.RallyConfig;
import net.minecraft.world.entity.Entity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * E3A V11 - decision telemetry. Always: the guard's LAST reason (one enum per guard, shown in
 * its Ledger card). Only in debug mode ({@code debug.decisionTrace}, or while a simulation
 * runs): a ring of the last {@link #SIZE} reasons per guard, written only when the reason
 * changes (never every tick), and running counts per reason for the simulation summary.
 * {@code /rally debug why <guard>} prints the ring.
 */
public final class DecisionTrace {
    private DecisionTrace() {
    }

    public static final int SIZE = 8;

    private record Entry(long tick, DecisionLadder.Rung rung, DecisionReason reason) {
    }

    private static final Map<UUID, DecisionReason> LAST = new HashMap<>();
    private static final Map<UUID, Deque<Entry>> RING = new HashMap<>();
    private static final EnumMap<DecisionReason, Long> COUNTS = new EnumMap<>(DecisionReason.class);
    /** Forced on by the simulation runner while a batch runs. */
    public static boolean forced;

    public static boolean enabled() {
        return forced || RallyConfig.get(RallyConfig.DECISION_TRACE::get, Boolean.FALSE);
    }

    public static void record(Entity guard, DecisionLadder.Rung rung, DecisionReason reason) {
        DecisionReason previous = LAST.put(guard.getUUID(), reason);
        if (previous == reason || !enabled()) return;
        Deque<Entry> ring = RING.computeIfAbsent(guard.getUUID(), k -> new ArrayDeque<>(SIZE));
        if (ring.size() >= SIZE) ring.removeFirst();
        ring.addLast(new Entry(guard.level().getGameTime(), rung, reason));
        COUNTS.merge(reason, 1L, Long::sum);
    }

    public static DecisionReason last(UUID guardId) {
        return LAST.getOrDefault(guardId, DecisionReason.NONE);
    }

    /** Oldest first: "tick RUNG REASON". */
    public static List<String> lines(UUID guardId, long now) {
        List<String> out = new ArrayList<>();
        Deque<Entry> ring = RING.get(guardId);
        if (ring == null) return out;
        for (Entry e : ring) out.add(String.format("-%.1fs  %s  %s", (now - e.tick()) / 20.0D, e.rung(), e.reason()));
        return out;
    }

    /** Counts per reason since the last reset (simulation). */
    public static Map<DecisionReason, Long> counts() {
        return new EnumMap<>(COUNTS);
    }

    public static void resetCounts() {
        COUNTS.clear();
    }

    public static void forget(UUID guardId) {
        LAST.remove(guardId);
        RING.remove(guardId);
    }

    public static void clearAll() {
        LAST.clear();
        RING.clear();
        COUNTS.clear();
    }
}
