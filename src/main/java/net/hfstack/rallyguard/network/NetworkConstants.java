package net.hfstack.rallyguard.network;

/**
 * Action codes carried by {@code GuardActionC2SPayload} over the Commander's Ledger
 * networking (Phase 2D). Values match the Forge 1.20.1 and Fabric 1.21.1 ports exactly
 * so no other code (and no saved-world state) needs renumbering.
 */
public final class NetworkConstants {
    private NetworkConstants() {
    }

    public static final int ACTION_SUMMON = 1;
    public static final int ACTION_TOGGLE_PATROL = 2;
}
