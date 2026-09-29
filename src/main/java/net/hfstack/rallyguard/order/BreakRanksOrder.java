package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3E: BREAK RANKS - release the guards from their post/formation (and from
 * following) so they act freely on their stance again. Their formation slot stays
 * remembered, so {@link ReformOrder} puts them straight back; their fire permission and
 * designated target are untouched (those are separate primitives - e.g. "Break Ranks"
 * then "Attack" is a charge out of formation).
 */
public final class BreakRanksOrder implements OrderExecutor {
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        for (Guard guard : guards) {
            GuardModes.free(guard);
            guard.getNavigation().stop();
        }
    }
}
