package net.hfstack.rallyguard.debug;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * E1-F: a test-only rank per guard ({@code /rally debug rank <n>}, the simulation's
 * veterans and captain). Until E2 the rank changes NOTHING in behavior - the baseline
 * documents exactly that. E2 replaces this with the real veterancy store.
 */
public final class DebugRanks {
    private DebugRanks() {
    }

    private static final Map<UUID, Integer> RANKS = new HashMap<>();

    public static void set(UUID guard, int rank) {
        if (rank <= 0) RANKS.remove(guard); else RANKS.put(guard, rank);
    }

    public static int of(UUID guard) {
        return RANKS.getOrDefault(guard, 0);
    }

    public static void forget(UUID guard) {
        RANKS.remove(guard);
    }
}
