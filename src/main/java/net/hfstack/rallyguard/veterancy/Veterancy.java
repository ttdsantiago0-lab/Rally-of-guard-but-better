package net.hfstack.rallyguard.veterancy;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.debug.DebugRanks;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * E2A: the one place every system asks "what rank is this guard?" (0..5). Block 1 reads
 * the test ranks ({@code /rally debug rank}); block 2 adds the earned rank.
 */
public final class Veterancy {
    private Veterancy() {
    }

    public static final int MAX_RANK = 5;

    public static int rank(Guard guard) {
        return Math.max(0, Math.min(MAX_RANK, DebugRanks.of(guard.getUUID())));
    }

    /** Configured name of a rank ({@code veterancy.rankNames}), or "" for the translated default. */
    public static String rankName(int rank) {
        List<? extends String> names = RallyConfig.get(RallyConfig.RANK_NAMES::get, List.of());
        return rank >= 0 && rank < names.size() ? names.get(rank) : "";
    }

    /** The Ledger row's command field: "rank|captain (0/1)|configured name". */
    public static String ledgerTag(Guard guard, boolean captain) {
        int rank = rank(guard);
        return rank + "|" + (captain ? 1 : 0) + "|" + rankName(rank).replace("|", "/");
    }
}
