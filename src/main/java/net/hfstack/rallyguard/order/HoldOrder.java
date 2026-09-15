package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3D: "stop whatever you're doing, right now" for a whole batch of guards - the
 * group generalization of the existing per-guard
 * {@code GuardCommandNetworking#resetNavigation}, which this mirrors exactly (clear
 * target, stop being aggressive, stop navigation). Deliberately does NOT touch
 * patrol/following flags the way {@code togglePatrol} does - Hold is meant as a plain
 * "stand down" order usable regardless of a guard's current patrol/follow state, not a
 * mode switch; a guard that was patrolling before Hold is still marked as patrolling
 * afterward, it has just stopped moving and fighting for this instant.
 */
public final class HoldOrder implements OrderExecutor {
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        for (Guard guard : guards) {
            guard.setTarget(null);
            guard.setAggressive(false);
            guard.getNavigation().stop();
        }
    }
}
