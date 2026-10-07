package net.hfstack.rallyguard.order;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
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

    /** Makes {@code pos} this guard's post: stops following and sets Guard Villagers'
     *  patrol position/flag. A guard on a drivable mount stays in the saddle (the mount
     *  driver takes it to the post - Phase 3F); on anything else it dismounts, since a
     *  vehicle drives its passenger's position. */
    public static void post(Guard guard, BlockPos pos) {
        MountDriver.dismountIfUndrivable(guard);
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
        boolean mounted = MountDriver.mountOf(guard) != null;
        BlockPos slot = standableNear(guard.level(), target, mounted ? MOUNTED_HEADROOM : FOOT_HEADROOM);
        post(guard, slot);
        MountDriver.moveTo(guard, slot, speed);
        return slot;
    }

    /** Phase 3H DEFEND: each defender's own spot around its ward - a fixed angle per
     *  guard (from its UUID) on a small ring, so a dozen escorts surround the ward
     *  instead of piling onto one block. */
    public static BlockPos besideWard(Guard guard, Entity ward) {
        double angle = (guard.getUUID().getLeastSignificantBits() & 0xFFFFL) / 65536.0D * Math.PI * 2.0D;
        double radius = 2.5D;
        Vec3 spot = ward.position().add(Math.cos(angle) * radius, 0.0D, Math.sin(angle) * radius);
        return standableNear(guard.level(), spot);
    }

    /** Free blocks needed above the slot's floor: a guard on foot needs 2; a rider on a
     *  horse needs 3, otherwise it suffocates under low ceilings (PORTING_PLAN.md §29). */
    public static final int FOOT_HEADROOM = 2;
    public static final int MOUNTED_HEADROOM = 3;

    /**
     * The closest block column position to {@code p} (searching a few blocks up and down)
     * where a guard can stand: two free blocks with something solid under them. Formation
     * slots are computed flat at the anchor's height; on uneven ground a slot can land
     * inside a hill or over a drop, and Guard Villagers' walk-back goal wants to stand on
     * its exact block - so an unreachable slot would leave the guard retrying forever.
     * Falls back to the unsnapped block if nothing nearby is standable.
     */
    public static BlockPos standableNear(Level level, Vec3 p) {
        return standableNear(level, p, FOOT_HEADROOM);
    }

    public static BlockPos standableNear(Level level, Vec3 p, int headroom) {
        BlockPos base = BlockPos.containing(p);
        for (int dy : VERTICAL_SEARCH) {
            BlockPos candidate = base.above(dy);
            if (isStandable(level, candidate, headroom)) {
                return candidate;
            }
        }
        // Phase 3H.1: nothing standable nearby - most often the anchor is in the air (the
        // commander gave the order while flying). Drop straight down to the ground
        // instead of posting the guard on an unreachable block in mid-air.
        BlockPos below = groundBelow(level, base, headroom);
        if (below != null) return below;
        // E2A B1 (49 of 53 guard deaths in David's test were drowning): over a lake there
        // is no dry spot straight below - look around for the nearest dry one instead of
        // posting the guard on the lake bed.
        BlockPos dry = dryNear(level, base, DRY_SEARCH_RADIUS, headroom);
        return dry != null ? dry : base;
    }

    /** How far {@link #dryNear} looks sideways for dry ground. */
    public static final int DRY_SEARCH_RADIUS = 12;

    /** The nearest standable (dry) spot around {@code center}, ring by ring, within
     *  {@code radius}; null if none. At most ~(2r+1)^2 x 7 cheap block reads. */
    public static BlockPos dryNear(Level level, BlockPos center, int radius, int headroom) {
        for (int r = 1; r <= radius; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (Math.abs(dx) != r && Math.abs(dz) != r) continue; // ring only
                    BlockPos column = center.offset(dx, 0, dz);
                    for (int dy : VERTICAL_SEARCH) {
                        BlockPos c = column.above(dy);
                        if (isStandable(level, c, headroom)) return c;
                    }
                }
            }
        }
        return null;
    }

    /** True if a guard standing at {@code pos} would have water (or any fluid) at its
     *  feet or head. */
    public static boolean isWet(Level level, BlockPos pos) {
        return !level.getFluidState(pos).isEmpty() || !level.getFluidState(pos.above()).isEmpty();
    }

    /**
     * E2A B6: places a group around {@code start} (an HQ banner inside a house) on spots
     * actually REACHABLE from it - a flood fill over standable, dry cells (4 neighbours,
     * one block up or down per step) - so the room fills first and the rest line up out
     * through the door, instead of formation slots landing inside walls. At most
     * {@code count} spots, never more than {@link #FLOOD_MAX_VISITED} cells visited.
     */
    public static java.util.List<BlockPos> reachableSpots(Level level, BlockPos start, int count) {
        java.util.List<BlockPos> out = new java.util.ArrayList<>();
        BlockPos first = isStandable(level, start, FOOT_HEADROOM) ? start : standableNear(level, Vec3.atBottomCenterOf(start));
        java.util.ArrayDeque<BlockPos> queue = new java.util.ArrayDeque<>();
        java.util.Set<Long> seen = new java.util.HashSet<>();
        queue.add(first);
        seen.add(first.asLong());
        int visited = 0;
        while (!queue.isEmpty() && out.size() < count && visited < FLOOD_MAX_VISITED) {
            BlockPos p = queue.poll();
            visited++;
            if (!isStandable(level, p, FOOT_HEADROOM)) continue;
            out.add(p);
            for (net.minecraft.core.Direction d : net.minecraft.core.Direction.Plane.HORIZONTAL) {
                for (int dy = -1; dy <= 1; dy++) {
                    BlockPos n = p.relative(d).above(dy);
                    if (n.distManhattan(first) > 24 || !seen.add(n.asLong())) continue;
                    if (isStandable(level, n, FOOT_HEADROOM)) {
                        queue.add(n);
                        break;
                    }
                }
            }
        }
        return out;
    }

    private static final int FLOOD_MAX_VISITED = 800;

    /** How far down {@link #groundBelow} looks for ground. */
    private static final int MAX_DROP = 64;

    /** The first standable spot straight below {@code from} (within {@link #MAX_DROP}),
     *  or null. */
    public static BlockPos groundBelow(Level level, BlockPos from, int headroom) {
        int minY = Math.max(level.getMinBuildHeight(), from.getY() - MAX_DROP);
        for (BlockPos.MutableBlockPos pos = from.mutable(); pos.getY() > minY; pos.move(0, -1, 0)) {
            if (!level.getFluidState(pos).isEmpty()) return null; // reached water: not here
            if (isStandable(level, pos, headroom)) return pos.immutable();
        }
        return null;
    }

    /**
     * Phase 3H.1 (user review: formations given "volando" went crazy): a formation is laid
     * out flat at its anchor's height, so an anchor in the air put every slot in the air.
     * The anchor is first brought down to the ground under it (if it isn't already on
     * ground within a few blocks); slots then only adjust ±3 blocks for slopes.
     */
    public static Vec3 groundAnchor(Level level, Vec3 anchor) {
        BlockPos base = BlockPos.containing(anchor);
        for (int dy : VERTICAL_SEARCH) {
            if (isStandable(level, base.above(dy), FOOT_HEADROOM)) return anchor;
        }
        BlockPos ground = groundBelow(level, base, FOOT_HEADROOM);
        return ground == null ? anchor : new Vec3(anchor.x, ground.getY(), anchor.z);
    }

    private static boolean isStandable(Level level, BlockPos pos, int headroom) {
        BlockPos floor = pos.below();
        if (level.getBlockState(floor).getCollisionShape(level, floor).isEmpty()) {
            return false;
        }
        for (int i = 0; i < headroom; i++) {
            BlockPos free = pos.above(i);
            if (!level.getBlockState(free).getCollisionShape(level, free).isEmpty()) {
                return false;
            }
            // E2A B1: a spot under water is not a place to stand (guards were posted on
            // lake beds and drowned holding their post).
            if (!level.getFluidState(free).isEmpty()) return false;
        }
        return true;
    }
}
