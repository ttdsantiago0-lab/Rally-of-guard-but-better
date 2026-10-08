package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * FOLLOW - "come with me". Phase 3G split this out of Recall on purpose: following is a
 * movement MODE (the guard walks - or rides - after its commander wherever they go, via
 * Guard Villagers' own follow goal and teleport-to-owner, or the mount driver), while
 * Recall is "come to where I am now and wait there". No teleport here: guards that are
 * far away catch up on their own (Guard Villagers teleports followers past 12 blocks).
 */
public final class FollowOrder implements OrderExecutor {
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        // E3A A5: following is in formation, relative to the commander (FormationFollow);
        // the same group ordered to follow again cycles behind/ahead/escort/column/fan.
        FormationFollow.order(player, guards);
    }
}
