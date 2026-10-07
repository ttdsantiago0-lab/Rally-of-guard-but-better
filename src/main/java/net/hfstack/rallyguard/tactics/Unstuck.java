package net.hfstack.rallyguard.tactics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * E2A block 4 - cheap escape for stuck guards (v1). Checked once a second: a guard that
 * wants to move (a path or a target more than 3 blocks away) but has moved less than 0.3
 * blocks for {@link #STILL_SECONDS} seconds is stuck. Escalation, one step per attempt:
 * jump and push toward the goal; then drop and recompute the path; then - only if it holds a
 * pickaxe, {@code mobGriefing} is on and it is boxed in (≥ 3 of 4 sides solid) - dig the
 * feet and head blocks toward the goal (or up), at most {@link #MAX_DUG} blocks per episode,
 * never block entities or unbreakable blocks. Swimming out is {@code WaterSafety}'s job.
 */
public final class Unstuck {
    private Unstuck() {
    }

    static final int STILL_SECONDS = 4;
    static final int MAX_DUG = 6;

    private static final class Progress {
        Vec3 pos;
        int still, attempts, dug;

        Progress(Vec3 pos) {
            this.pos = pos;
        }
    }

    private static final Map<UUID, Progress> PROGRESS = new HashMap<>();

    /** Once a second: true when the guard is stuck (see the class javadoc). */
    public static boolean check(Guard guard) {
        boolean wants = !guard.getNavigation().isDone()
                || (guard.getTarget() != null && guard.getTarget().distanceToSqr(guard) > 9.0D);
        if (!wants || guard.isPassenger()) {
            PROGRESS.remove(guard.getUUID());
            return false;
        }
        Progress p = PROGRESS.get(guard.getUUID());
        Vec3 now = guard.position();
        if (p == null) {
            PROGRESS.put(guard.getUUID(), new Progress(now));
            return false;
        }
        if (p.pos.distanceToSqr(now) > 0.09D) {
            p.pos = now;
            p.still = 0;
            p.attempts = 0;
            p.dug = 0;
            return false;
        }
        p.still++;
        return p.still >= STILL_SECONDS;
    }

    public static void attempt(ServerLevel level, Guard guard, @Nullable Vec3 goal) {
        Progress p = PROGRESS.computeIfAbsent(guard.getUUID(), k -> new Progress(guard.position()));
        p.attempts++;
        p.still = 0;
        if (guard.isInWater()) return; // WaterSafety swims it out
        Vec3 dir = goal == null ? Vec3.ZERO : goal.subtract(guard.position()).multiply(1, 0, 1);
        if (dir.lengthSqr() > 1.0E-4) dir = dir.normalize();

        if (p.attempts == 1) {
            guard.getJumpControl().jump();
            guard.setDeltaMovement(guard.getDeltaMovement().add(dir.x * 0.25D, 0.1D, dir.z * 0.25D));
            return;
        }
        if (p.attempts == 2 || !canDig(level, guard) || p.dug >= MAX_DUG || !boxedIn(level, guard)) {
            guard.getNavigation().stop();
            if (goal != null) guard.getNavigation().moveTo(goal.x, goal.y, goal.z, 1.0D);
            guard.getJumpControl().jump();
            return;
        }
        Direction face = dir.lengthSqr() < 1.0E-4 ? Direction.UP : Direction.getNearest(dir.x, 0.0D, dir.z);
        BlockPos feet = guard.blockPosition();
        BlockPos[] targets = face == Direction.UP
                ? new BlockPos[]{feet.above(2)}
                : new BlockPos[]{feet.relative(face), feet.above().relative(face)};
        for (BlockPos pos : targets) {
            if (p.dug >= MAX_DUG) break;
            BlockState state = level.getBlockState(pos);
            float hardness = state.getDestroySpeed(level, pos);
            if (state.isAir() || hardness < 0.0F || hardness >= 50.0F || state.hasBlockEntity()) continue;
            if (level.destroyBlock(pos, true, guard)) p.dug++;
        }
        if (goal != null) guard.getNavigation().moveTo(goal.x, goal.y, goal.z, 1.0D);
    }

    private static boolean canDig(ServerLevel level, Guard guard) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) return false;
        return pickaxe(guard.getMainHandItem()) || pickaxe(guard.getOffhandItem());
    }

    private static boolean pickaxe(ItemStack stack) {
        return stack.getItem() instanceof PickaxeItem || stack.is(ItemTags.PICKAXES);
    }

    /** 4 reads: at least 3 of the 4 horizontal neighbours at feet height are solid. */
    private static boolean boxedIn(ServerLevel level, Guard guard) {
        BlockPos feet = guard.blockPosition();
        int solid = 0;
        for (Direction d : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(feet.relative(d)).blocksMotion()) solid++;
        }
        return solid >= 3;
    }

    public static void forget(UUID guardId) {
        PROGRESS.remove(guardId);
    }

    public static void clearAll() {
        PROGRESS.clear();
    }
}
