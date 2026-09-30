package net.hfstack.rallyguard.order;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Phase 3F: lets a guard actually RIDE its mount into battle (approved design,
 * docs/INVESTIGACION_CBC_MONTURAS.md section 2) - horses first.
 * <p>
 * Why this is needed: a vanilla horse only lets a {@code Player} control it
 * ({@code AbstractHorse#getControllingPassenger}), so a guard in the saddle is just a
 * passenger and the horse keeps wandering on its own AI; Guard Villagers' movement goals
 * steer the GUARD's navigation, which does nothing while it is carried. Until now Rally
 * simply dismounted any guard that had to go somewhere.
 * <p>
 * The driver doesn't write any movement AI: it reads what the guard wants - fight its
 * target, hold its post, follow its commander - and hands that destination to the
 * MOUNT's own navigation ({@link #drive}), refreshed every half second by
 * {@code GuardMountDisciplineHandler}. Guard Villagers already fights from the saddle
 * ({@code GuardMeleeGoal#mobHitBox} widens the reach by the vehicle's box; bows and
 * crossbows just work), so combat needs nothing else. Any {@link PathfinderMob} not
 * steered by a player counts as drivable (horses, donkeys, mules, llamas, and most
 * modded ground mounts); boats, minecarts and the like are not, and still dismount.
 * Flying mounts (Ice and Fire's hippogryph) are a later, separate step.
 */
public final class MountDriver {
    private MountDriver() {
    }

    /** Navigation speed modifiers for the mount (multiplied by its own speed stat). */
    public static final double CHARGE_SPEED = 2.0D;
    public static final double MARCH_SPEED = 1.6D;
    public static final double FOLLOW_SPEED = 1.8D;

    private static final double ARRIVE_DISTANCE = 1.5D;
    private static final double RANGED_STANDOFF = 12.0D;
    private static final double FOLLOW_START = 7.0D;
    private static final double FOLLOW_STOP = 4.0D;
    private static final double FOLLOW_TELEPORT = 48.0D;

    /** True for a vehicle Rally can steer for a guard: a pathfinding mob that no player
     *  is controlling (and that isn't itself a guard). */
    public static boolean isDrivable(@Nullable Entity vehicle) {
        return vehicle instanceof PathfinderMob mob
                && !(mob instanceof Guard)
                && !(mob.getControllingPassenger() instanceof Player);
    }

    /** The guard's drivable mount, or null if it's on foot (or on something else). */
    @Nullable
    public static PathfinderMob mountOf(Guard guard) {
        Entity vehicle = guard.getVehicle();
        return isDrivable(vehicle) ? (PathfinderMob) vehicle : null;
    }

    /** Dismounts only from vehicles the driver can't steer (boat, minecart...). */
    public static void dismountIfUndrivable(Guard guard) {
        if (guard.isPassenger() && mountOf(guard) == null) {
            guard.stopRiding();
        }
    }

    /** Teleports the guard - WITH its mount if it has a drivable one (the rider comes
     *  along), otherwise dismounting first as every order did before (a vehicle re-drives
     *  its passenger's position every tick, so moving only the rider doesn't stick). */
    public static void relocate(Guard guard, double x, double y, double z) {
        PathfinderMob mount = mountOf(guard);
        if (mount != null) {
            mount.teleportTo(x, y, z);
            mount.getNavigation().stop();
            return;
        }
        if (guard.isPassenger()) {
            guard.stopRiding();
        }
        guard.moveTo(x, y, z, guard.getYRot(), guard.getXRot());
    }

    /** Starts the guard (or its mount) walking to {@code pos} right away. */
    public static void moveTo(Guard guard, BlockPos pos, double footSpeed) {
        PathfinderMob mount = mountOf(guard);
        if (mount != null) {
            mount.getNavigation().moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, MARCH_SPEED);
        } else {
            guard.getNavigation().moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, footSpeed);
        }
    }

    /**
     * One steering update, in priority order: fight the guard's target (ranged riders
     * keep a stand-off distance and let the horse stand still so they can shoot); else
     * go to / stay on the guard's post; else keep up with the commander if following
     * (teleporting horse and rider when left far behind, like Guard Villagers does for
     * guards on foot); else leave the mount alone.
     */
    public static void drive(ServerLevel level, Guard guard, PathfinderMob mount) {
        LivingEntity target = guard.getTarget();
        if (target != null && target.isAlive()) {
            boolean ranged = guard.getMainHandItem().getItem() instanceof ProjectileWeaponItem
                    || ScorchedGunsCompat.isGun(guard.getMainHandItem());
            if (ranged && mount.distanceToSqr(target) < RANGED_STANDOFF * RANGED_STANDOFF) {
                mount.getNavigation().stop();
            } else {
                mount.getNavigation().moveTo(target, CHARGE_SPEED);
            }
            return;
        }

        BlockPos post = CommandTargeting.postOf(guard);
        if (post != null) {
            Vec3 spot = Vec3.atBottomCenterOf(post);
            double dx = mount.getX() - spot.x;
            double dz = mount.getZ() - spot.z;
            if (dx * dx + dz * dz > ARRIVE_DISTANCE * ARRIVE_DISTANCE) {
                mount.getNavigation().moveTo(spot.x, spot.y, spot.z, MARCH_SPEED);
            } else {
                mount.getNavigation().stop();
            }
            return;
        }

        if (guard.isFollowing()) {
            UUID ownerId = guard.getOwnerId();
            Player owner = ownerId == null ? null : level.getPlayerByUUID(ownerId);
            if (owner == null) return;
            double d2 = mount.distanceToSqr(owner);
            if (d2 > FOLLOW_TELEPORT * FOLLOW_TELEPORT && owner.onGround()) {
                double ox = (level.getRandom().nextDouble() - 0.5D) * 6.0D;
                double oz = (level.getRandom().nextDouble() - 0.5D) * 6.0D;
                relocate(guard, owner.getX() + ox, owner.getY(), owner.getZ() + oz);
            } else if (d2 > FOLLOW_START * FOLLOW_START) {
                mount.getNavigation().moveTo(owner, FOLLOW_SPEED);
            } else if (d2 < FOLLOW_STOP * FOLLOW_STOP) {
                mount.getNavigation().stop();
            }
        }
    }
}
