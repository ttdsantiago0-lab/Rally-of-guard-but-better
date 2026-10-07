package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * E2A B1 - 49 of the 53 guard deaths in David's E1 test were drowning (a lake next to the
 * test area). Three causes, all fixed at the root in {@link GuardPosts} (a spot with
 * water at the feet or head is no longer "standable", ground-snapping stops at water, and
 * a dry spot is searched around instead), plus this once-a-second check for guards that
 * still end up in water:
 * <ul>
 *     <li>a rider whose head is under water gets off (the horse swims, the rider drowns);</li>
 *     <li>a post that is in water moves to the nearest dry spot (within 12 blocks);</li>
 *     <li>a guard under water gets an upward push and, if it holds no post, walks to the
 *     nearest dry spot.</li>
 * </ul>
 * Runs inside the stance loop's spread-out pass (no extra scan).
 */
public final class WaterSafety {
    private WaterSafety() {
    }

    public static void tick(ServerLevel level, Guard guard) {
        if (!guard.isInWater()) return;
        if (guard.isPassenger() && guard.isUnderWater()) guard.stopRiding();

        BlockPos post = CommandTargeting.postOf(guard);
        if (post != null && GuardPosts.isWet(level, post)) {
            BlockPos dry = GuardPosts.dryNear(level, post, GuardPosts.DRY_SEARCH_RADIUS, GuardPosts.FOOT_HEADROOM);
            if (dry != null) {
                if (DirectiveRegistry.get(level).directiveOf(guard.getUUID()).ward() != null) {
                    GuardModes.postKeepingWard(guard, dry);
                } else {
                    GuardModes.post(guard, dry);
                }
                post = dry;
            }
        }
        if (guard.isUnderWater()) {
            guard.setDeltaMovement(guard.getDeltaMovement().add(0.0D, 0.3D, 0.0D));
            guard.getJumpControl().jump();
            BlockPos dest = post != null ? post
                    : GuardPosts.dryNear(level, guard.blockPosition(), GuardPosts.DRY_SEARCH_RADIUS, GuardPosts.FOOT_HEADROOM);
            if (dest != null) guard.getNavigation().moveTo(dest.getX() + 0.5D, dest.getY(), dest.getZ() + 0.5D, 1.1D);
        }
    }
}
