package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3D: "fall back to this position" - deliberately the simplest of the new
 * orders, no formation math at all (per the design brief: "keep it simple... the core
 * behavior is: group, fallback position, move there"). Guards pathfind toward the
 * fallback point individually rather than teleporting - visibly running back, unlike
 * {@code RecallOrder}'s instant "come to me now."
 * <p>
 * Like {@code MusterOrder}, needs a destination beyond "which guards," so it's built
 * per-invocation by {@code GuardCommandNetworking} from
 * {@code IssueRetreatOrderC2SPayload} rather than registered in {@link OrderRegistry}.
 * Shares the same v1 destination limitation as Muster: this pass always uses the
 * player's own current position (see {@code GuardCommandScreen#buildMusterBar}); a
 * real fallback point elsewhere is future work, not a change to this class or the wire
 * format.
 */
public final class RetreatOrder implements OrderExecutor {

    private static final double RETREAT_SPEED = 1.0D;

    private final Vec3 destination;

    public RetreatOrder(Vec3 destination) {
        this.destination = destination;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        for (Guard guard : guards) {
            if (guard.isPassenger()) {
                guard.stopRiding();
            }
            guard.getNavigation().moveTo(destination.x, destination.y, destination.z, RETREAT_SPEED);
        }
    }
}
