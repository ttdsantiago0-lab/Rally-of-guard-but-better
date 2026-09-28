package net.hfstack.rallyguard.order;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * Phase 3E: "hold this spot" for a guard, built entirely on Guard Villagers' OWN patrol
 * post instead of new AI.
 * <p>
 * Read from Guard Villagers' real 1.21.1 source ({@code Guard.WalkBackToCheckPointGoal}
 * and {@code Guard.GuardGroundPathNavigation}): a guard that is patrolling, not
 * following, and has no target walks back to its exact patrol block whenever its
 * navigation is idle, and its navigation reports "done" while it stands on that block -
 * which also stops the random-stroll goals from wandering it away. In other words Guard
 * Villagers already ships a durable "stand here, fight, come back" behavior; it just
 * only ever set that block to "wherever the player was standing".
 * <p>
 * Before this phase, Muster/Reform/Retreat were a single {@code navigation.moveTo(...)}
 * - the next goal that touched navigation (random stroll, walk-back-to-an-older-post,
 * follow-owner) overrode it, so formations dissolved within seconds, and a patrolling
 * guard walked straight back to its OLD post. Posting each guard at its formation slot
 * makes the formation itself the thing Guard Villagers' AI defends and returns to.
 * {@code CommandTargeting#POST_LEASH} keeps it from being dragged far off the post.
 */
public final class GuardPosts {
    private GuardPosts() {
    }

    private static final int[] VERTICAL_SEARCH = {0, 1, -1, 2, -2, 3, -3};

    /** Makes {@code pos} this guard's post: dismounts (a vehicle drives its passenger, the
     *  same reason every other order dismounts), stops following, and sets Guard
     *  Villagers' patrol position/flag. */
    public static void post(Guard guard, BlockPos pos) {
        if (guard.isPassenger()) {
            guard.stopRiding();
        }
        guard.setFollowing(false);
        guard.setPatrolPos(pos);
        guard.setPatrolling(true);
    }

    /** Releases the guard from any post (it goes back to Guard Villagers' free roaming,
     *  or following if the caller sets that next). */
    public static void unpost(Guard guard) {
        guard.setPatrolling(false);
        guard.setPatrolPos(null);
    }

    /** Posts the guard at {@code target} (snapped to somewhere it can actually stand) and
     *  starts walking it there right away at {@code speed}. */
    public static BlockPos postAndMove(Guard guard, Vec3 target, double speed) {
        BlockPos slot = standableNear(guard.level(), target);
        post(guard, slot);
        guard.getNavigation().moveTo(slot.getX() + 0.5D, slot.getY(), slot.getZ() + 0.5D, speed);
        return slot;
    }

    /**
     * The closest block column position to {@code p} (searching a few blocks up and down)
     * where a guard can stand: two free blocks with something solid under them. Formation
     * slots are computed flat at the anchor's height; on uneven ground a slot can land
     * inside a hill or over a drop, and Guard Villagers' walk-back goal wants to stand on
     * its exact block - so an unreachable slot would leave the guard retrying forever.
     * Falls back to the unsnapped block if nothing nearby is standable.
     */
    public static BlockPos standableNear(Level level, Vec3 p) {
        BlockPos base = BlockPos.containing(p);
        for (int dy : VERTICAL_SEARCH) {
            BlockPos candidate = base.above(dy);
            if (isStandable(level, candidate)) {
                return candidate;
            }
        }
        return base;
    }

    private static boolean isStandable(Level level, BlockPos pos) {
        BlockPos head = pos.above();
        BlockPos floor = pos.below();
        return level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                && level.getBlockState(head).getCollisionShape(level, head).isEmpty()
                && !level.getBlockState(floor).getCollisionShape(level, floor).isEmpty();
    }
}
