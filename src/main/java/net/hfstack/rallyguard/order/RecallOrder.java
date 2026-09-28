package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * RECALL - "to me": the batch version of the per-guard Ledger Summon (dismount, then
 * teleport beside the commander, mirroring {@code GuardCommandNetworking#summon}).
 * <p>
 * Phase 3E: a recalled guard also leaves its post and starts FOLLOWING. Before, it was
 * teleported and then either walked back to its old post (if it had one) or wandered
 * off, so "Recall" only really meant "come here for a second". Recall is now the one
 * order that turns a deployed squad back into the commander's escort; Muster/Hold Ground
 * deploy it again.
 */
public final class RecallOrder implements OrderExecutor {
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        for (Guard guard : guards) {
            // A vehicle re-drives its passenger's position every tick, so moveTo(...) on a
            // still-mounted guard has no lasting effect until it's dismounted first.
            if (guard.isPassenger()) {
                guard.stopRiding();
            }
            GuardPosts.unpost(guard);
            double ox = (player.getRandom().nextDouble() - 0.5) * 2.5;
            double oz = (player.getRandom().nextDouble() - 0.5) * 2.5;
            guard.moveTo(player.getX() + ox, player.getY(), player.getZ() + oz,
                    guard.getYRot(), guard.getXRot());
            guard.getNavigation().stop();
            guard.setFollowing(true);
        }
    }
}
