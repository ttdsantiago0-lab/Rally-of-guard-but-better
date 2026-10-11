package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * E3B G0 - "Defend" in a single guard's panel ({@code docs/GRUPOS_Y_SUBGRUPOS.md} §1: Attack,
 * Defend, Move, Stay, Kamikaze, Mount): the guard takes its COMMANDER as its ward - exactly what
 * pointing G at yourself would do ({@link CommanderHotkeys#defend}): it stays beside you and
 * fights whatever threatens you. No new mechanism, one more way to reach an existing one.
 */
public final class DefendOrder implements OrderExecutor {
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        CommanderHotkeys.defend(player, guards, player);
    }
}
