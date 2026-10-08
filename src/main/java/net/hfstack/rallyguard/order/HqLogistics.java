package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.HqRegistry;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * E2A B6 (David: guards struggled into a one-door house used as HQ; riders brought their
 * horses inside; one guard stayed in and didn't leave with the order). Once a second per
 * owned guard, inside the stance loop:
 * <ul>
 *     <li><b>Doors</b>: owned guards can open doors (Guard Villagers only enables that
 *     when its {@code GuardsOpenDoors} config is on): the ground navigation is allowed
 *     through doors and a vanilla {@link OpenDoorGoal} is added once. That is what lets
 *     them in through a single door - and out again when the next order comes.</li>
 *     <li><b>Park</b>: a rider posted within {@link #PARK_RADIUS} of its squad's HQ gets
 *     off; the mount is remembered.</li>
 *     <li><b>Remount</b>: once the guard's next order takes it away from the HQ (no post,
 *     or a post further than {@link #LEAVE_RADIUS}), the parked mount walks to the guard
 *     and it gets back on (within 2.5 blocks). The guard's own orders are never changed
 *     here - the horse comes to it - so HQ logistics can't fight an order.</li>
 * </ul>
 */
public final class HqLogistics {
    private HqLogistics() {
    }

    /** E3A F4: a rider parks only once within this of its own spot (was 8 around the HQ). */
    private static final double PARK_RADIUS = 3.0D;
    /** Spots this close to the HQ are "home" (the HQ zone; A6 makes it configurable). */
    private static final double HOME_RADIUS = 12.0D;
    private static final double LEAVE_RADIUS = 12.0D;
    /** E3A F4: a guard at home is "in" only within this of its spot; otherwise it is sent again. */
    private static final double INSIDE_RADIUS = 1.2D;
    private static final double CALL_RADIUS = 48.0D;
    private static final long FORGET_AFTER_TICKS = 20L * 120L;

    private record Parked(UUID mount, long since) {
    }

    private static final Map<UUID, Parked> PARKED = new HashMap<>();
    private static final Set<UUID> DOORS = new HashSet<>();

    public static void tick(ServerLevel level, Guard guard) {
        ensureDoors(guard);
        HqRegistry.Hq hq = hqOf(level, guard);
        BlockPos post = CommandTargeting.postOf(guard);
        PathfinderMob mount = MountDriver.mountOf(guard);

        if (mount != null) {
            if (hq != null && post != null && Vec3.atCenterOf(post).distanceToSqr(Vec3.atCenterOf(hq.pos)) <= HOME_RADIUS * HOME_RADIUS
                    && guard.distanceToSqr(Vec3.atCenterOf(post)) <= PARK_RADIUS * PARK_RADIUS) {
                guard.stopRiding();
                mount.getNavigation().stop();
                PARKED.put(guard.getUUID(), new Parked(mount.getUUID(), level.getGameTime()));
            }
            return;
        }

        // E3A F4: Guard Villagers' walk-back gives up for 10 s whenever a path fails (a closed
        // door, a crowd at the entrance); a guard sent home that stopped short is sent again.
        if (hq != null && post != null && guard.getTarget() == null && guard.getNavigation().isDone()
                && Vec3.atCenterOf(post).distanceToSqr(Vec3.atCenterOf(hq.pos)) <= HOME_RADIUS * HOME_RADIUS
                && guard.distanceToSqr(Vec3.atBottomCenterOf(post)) > INSIDE_RADIUS * INSIDE_RADIUS) {
            guard.getNavigation().moveTo(post.getX() + 0.5D, post.getY(), post.getZ() + 0.5D, 1.0D);
        }

        Parked parked = PARKED.get(guard.getUUID());
        if (parked == null) return;
        boolean leaving = hq == null || post == null
                || Vec3.atCenterOf(post).distanceToSqr(Vec3.atCenterOf(hq.pos)) > LEAVE_RADIUS * LEAVE_RADIUS;
        if (!leaving) return;
        Entity e = level.getEntity(parked.mount());
        if (!(e instanceof PathfinderMob horse) || !horse.isAlive() || horse.isVehicle()
                || level.getGameTime() - parked.since() > FORGET_AFTER_TICKS
                || horse.distanceToSqr(guard) > CALL_RADIUS * CALL_RADIUS) {
            PARKED.remove(guard.getUUID());
            return;
        }
        if (horse.distanceToSqr(guard) <= 2.5D * 2.5D) {
            guard.startRiding(horse, true);
            PARKED.remove(guard.getUUID());
        } else {
            horse.getNavigation().moveTo(guard, MountDriver.GALLOP_SPEED);
        }
    }

    private static void ensureDoors(Guard guard) {
        if (!DOORS.add(guard.getUUID())) return;
        if (guard.getNavigation() instanceof GroundPathNavigation nav) nav.setCanOpenDoors(true);
        boolean hasDoorGoal = guard.goalSelector.getAvailableGoals().stream()
                .anyMatch(w -> w.getGoal() instanceof OpenDoorGoal);
        if (!hasDoorGoal) guard.goalSelector.addGoal(4, new OpenDoorGoal(guard, true));
    }

    private static HqRegistry.Hq hqOf(ServerLevel level, Guard guard) {
        SquadRegistry squads = SquadRegistry.get(level);
        HqRegistry.Hq hq = squads.squadOf(guard.getUUID())
                .map(s -> RetreatPlanner.hqOf(squads, HqRegistry.get(level), s)).orElse(null);
        return hq != null && hq.dimension.equals(level.dimension().location().toString()) ? hq : null;
    }

    public static void forget(UUID guard) {
        PARKED.remove(guard);
        DOORS.remove(guard);
    }

    public static void clearAll() {
        PARKED.clear();
        DOORS.clear();
    }
}
