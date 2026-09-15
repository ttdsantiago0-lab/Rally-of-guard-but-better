package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3D: "come to me, right now" for a whole batch of guards at once - the group
 * generalization of the existing per-guard Ledger Summon action
 * ({@code GuardCommandNetworking#summon}), whose dismount-then-moveTo-near-player logic
 * this deliberately mirrors line for line rather than inventing a second slightly
 * different implementation. Kept as its own small class (rather than a private method
 * directly in {@code GuardCommandNetworking}) so it can be registered as one more
 * {@link OrderExecutor} in {@link OrderRegistry} alongside every future order, and so a
 * future Battle Planner step that wants to trigger "Recall" programmatically (not just
 * from a Ledger button click) has something to call directly.
 */
public final class RecallOrder implements OrderExecutor {
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        for (Guard guard : guards) {
            // Same reasoning as summon()'s mounted-guard fix (PORTING_PLAN.md, Phase
            // 2F/2026-09-13): a vehicle re-drives its passenger's position every tick, so
            // moveTo(...) on a still-mounted guard has no lasting effect until it's
            // dismounted first.
            if (guard.isPassenger()) {
                guard.stopRiding();
            }
            double ox = (player.getRandom().nextDouble() - 0.5) * 2.5;
            double oz = (player.getRandom().nextDouble() - 0.5) * 2.5;
            guard.moveTo(player.getX() + ox, player.getY(), player.getZ() + oz,
                    guard.getYRot(), guard.getXRot());
        }
    }
}
