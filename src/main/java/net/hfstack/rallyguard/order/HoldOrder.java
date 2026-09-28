package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * HOLD GROUND (order id {@code HOLD}). Phase 3D's Hold was "stop right now" - it cleared
 * the target and navigation for an instant, and the guard's AI resumed a moment later
 * (a documented limitation, and for DEFENSIVE guards even the clear was vetoed by the
 * stance gate). Phase 3E makes it durable: every guard stops, drops its current fight and
 * takes a post exactly where it stands (see {@link GuardPosts}) - so a squad caught in
 * the open plants itself, keeps its current shape, only engages what comes within its
 * post leash, and walks back after each fight. Fire permission and designated target are
 * separate primitives and are left as they were.
 */
public final class HoldOrder implements OrderExecutor {
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        for (Guard guard : guards) {
            CommandTargeting.disengage(guard);
            GuardPosts.post(guard, GuardPosts.standableNear(guard.level(), Vec3.atBottomCenterOf(guard.blockPosition())));
        }
    }
}
