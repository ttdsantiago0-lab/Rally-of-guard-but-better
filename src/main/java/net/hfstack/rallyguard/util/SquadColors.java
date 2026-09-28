package net.hfstack.rallyguard.util;

import net.minecraft.ChatFormatting;

import java.util.Locale;

/**
 * One squad -> one color, everywhere: world name tags (server) and the Commander's
 * Ledger (client) call the same pure function, so a squad reads the same color in the
 * world and in the book with no stored state to keep in sync. Moved here from
 * {@code GuardCommandNetworking} (Phase 3A.1) in Phase 3E so the client can use it too.
 */
public final class SquadColors {
    private SquadColors() {
    }

    private static final ChatFormatting[] PALETTE = {
            ChatFormatting.AQUA, ChatFormatting.GREEN, ChatFormatting.YELLOW,
            ChatFormatting.LIGHT_PURPLE, ChatFormatting.GOLD, ChatFormatting.BLUE,
            ChatFormatting.DARK_AQUA, ChatFormatting.DARK_PURPLE, ChatFormatting.DARK_GREEN,
            ChatFormatting.GRAY
    };

    public static ChatFormatting colorFor(String squadName) {
        int index = Math.floorMod(squadName.toLowerCase(Locale.ROOT).hashCode(), PALETTE.length);
        return PALETTE[index];
    }
}
