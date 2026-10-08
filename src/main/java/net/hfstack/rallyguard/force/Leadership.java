package net.hfstack.rallyguard.force;

import tallestegg.guardvillagers.common.entities.Guard;

/** E3A A1 - leadership score (filled in by block A1). */
public final class Leadership {
    private Leadership() {
    }

    /** The score shown on the Ledger card, "-" when not computed. */
    public static String ledgerScore(Guard guard) {
        return "-";
    }
}
