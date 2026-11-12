package net.hfstack.rallyguard.order;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import net.hfstack.rallyguard.config.RallyConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Enemy;
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
    /** E1-D: "con G, el guardia montado va lo más rápido posible" - full gallop until
     *  {@link #GALLOP_UNTIL} blocks from the destination, then a march to settle. */
    public static final double GALLOP_SPEED = 3.0D;
    private static final double GALLOP_UNTIL = 3.0D;
    private static final double GALLOP_DISTANCE = 14.0D;

    private static final double ARRIVE_DISTANCE = 1.5D;
    private static final double RANGED_STANDOFF = 12.0D;
    private static final double FOLLOW_START = 7.0D;
    private static final double FOLLOW_STOP = 4.0D;
    private static final double FOLLOW_TELEPORT = 48.0D;

    /** E1-D: ground mounts guards ride, by entity type tag (datapacks can add modded
     *  mounts; default list in {@code data/rallyguard/tags/entity_type/guard_mounts.json}:
     *  horse, mule, donkey, llamas, camel, saddled pig, strider). */
    public static final TagKey<EntityType<?>> GUARD_MOUNTS =
            TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("rallyguard", "guard_mounts"));

    /** True for a vehicle Rally can steer for a guard: a pathfinding mob no player is
     *  controlling, that isn't a guard, and is in {@link #GUARD_MOUNTS} - or, unless the
     *  config limits mounts to the tag ({@code systems.mountsFromTagOnly}), any other
     *  non-hostile pathfinding mob (keeps datapack mounts like Candicorn working). A pig
     *  must be saddled. Flying mounts are not handled (on hold). */
    public static boolean isDrivable(@Nullable Entity vehicle) {
        if (!(vehicle instanceof PathfinderMob mob) || mob instanceof Guard
                || mob.getControllingPassenger() instanceof Player) return false;
        if (mob instanceof Pig pig && !pig.isSaddled()) return false;
        if (mob.getType().is(GUARD_MOUNTS)) return true;
        return !RallyConfig.get(RallyConfig.MOUNTS_FROM_TAG_ONLY::get, Boolean.FALSE) && !(mob instanceof Enemy);
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
            double dx = mount.getX() - (pos.getX() + 0.5D), dz = mount.getZ() - (pos.getZ() + 0.5D);
            boolean far = dx * dx + dz * dz > GALLOP_UNTIL * GALLOP_UNTIL;
            mount.getNavigation().moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, far ? GALLOP_SPEED : MARCH_SPEED);
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
            // Ronda 11 (2026-11-05): stopping a mounted archer at its own reach against shooters (as on foot) was
            // measured and REVERTED - vs skeletons the cavalry's wins fell from 77-87 % to 9-11 % (n = 30, two seed
            // sets): from 40 blocks on horseback they don't kill them. The 12-block stand-off stays.
            if (ranged && !riderMode().isEmpty() && riderSim(level, guard, mount, target)) return;
            if (!ranged && simMelee.equals("hitrun") && hitAndRun(level, guard, mount, target)) return;
            if (ranged && mount.distanceToSqr(target) < RANGED_STANDOFF * RANGED_STANDOFF) {
                mount.getNavigation().stop();
            } else {
                repath(level, mount, target, CHARGE_SPEED);
            }
            return;
        }

        BlockPos post = CommandTargeting.postOf(guard);
        if (post != null) {
            Vec3 spot = Vec3.atBottomCenterOf(post);
            double dx = mount.getX() - spot.x;
            double dz = mount.getZ() - spot.z;
            if (dx * dx + dz * dz > ARRIVE_DISTANCE * ARRIVE_DISTANCE) {
                double far = dx * dx + dz * dz;
                repath(level, mount, null, spot.x, spot.y, spot.z,
                        far > GALLOP_UNTIL * GALLOP_UNTIL ? GALLOP_SPEED : MARCH_SPEED);
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
                // Phase 3H.1 (user: the horse "no corre" when called): gallop while far
                // behind, settle to a trot once close.
                repath(level, mount, owner, d2 > GALLOP_DISTANCE * GALLOP_DISTANCE ? GALLOP_SPEED : FOLLOW_SPEED);
            } else if (d2 < FOLLOW_STOP * FOLLOW_STOP) {
                mount.getNavigation().stop();
            }
        }
    }

    // ==== F2 (2026-11-11, cola de Fable): cavalry diagnosis arms, simulation only ====
    /** Simulation tag {@code rider:standoff} (arm e: hold 48-56 from the target, still, long shots), {@code rider:kite}
     *  (gunner: 48-56; melee enemy closer than 14: gallop out to 24; otherwise the 12-block rule) or {@code rider:off} (the
     *  old 12-block stop). "" = the game's rule: kite since 2026-11-11 (config {@code riderKite}; round 49). */
    public static String simRider = "";

    public static String riderMode() {
        if (!simRider.isEmpty()) return simRider.equals("off") ? "" : simRider;
        return RallyConfig.on(RallyConfig.RIDER_KITE) ? "kite" : "";
    }

    private static boolean riderSim(ServerLevel level, Guard guard, PathfinderMob mount, LivingEntity target) {
        double d = mount.distanceTo(target);
        boolean gunner = net.hfstack.rallyguard.combat.FieldManual.gunner(target);
        double near, far;
        String mode = riderMode();
        if (mode.equals("standoff") || gunner) {
            near = 48.0D;
            far = 56.0D;
        } else if (mode.equals("kite") && !(target instanceof net.minecraft.world.entity.monster.RangedAttackMob)) {
            near = 14.0D;
            far = 24.0D;
        } else {
            return false;
        }
        if (d >= near && d <= far + 8.0D) {
            mount.getNavigation().stop();
            return true;
        }
        if (d > far + 8.0D) return false; // too far: the normal rule rides in
        Vec3 away = mount.position().subtract(target.position()).multiply(1, 0, 1);
        away = away.lengthSqr() < 1.0E-4 ? new Vec3(1, 0, 0) : away.normalize();
        Vec3 spot = mount.position().add(away.scale(far - d));
        repath(level, mount, null, spot.x, spot.y, spot.z, GALLOP_SPEED);
        net.hfstack.rallyguard.debug.SimProbe.count(guard, "act:riderOut");
        return true;
    }

    /** F2c (2026-11-12), simulation tag {@code melee:hitrun}: a rider with a melee weapon charges, strikes, rides out
     *  12 blocks for 1.5 s, and charges again (instead of staying in the crowd). "" = the game's charge. */
    public static String simMelee = "";
    private static final java.util.Map<java.util.UUID, Long> RIDE_OUT = new java.util.HashMap<>();

    private static boolean hitAndRun(ServerLevel level, Guard guard, PathfinderMob mount, LivingEntity target) {
        long now = level.getGameTime();
        Long until = RIDE_OUT.get(guard.getUUID());
        if (until != null && now < until) return true; // still riding out
        if (guard.getLastHurtMob() == target && now - guard.getLastHurtMobTimestamp() <= 10
                && (until == null || now - until > 20)) {
            Vec3 away = mount.position().subtract(target.position()).multiply(1, 0, 1);
            away = away.lengthSqr() < 1.0E-4 ? new Vec3(1, 0, 0) : away.normalize();
            Vec3 spot = mount.position().add(away.scale(12.0D));
            mount.getNavigation().moveTo(spot.x, spot.y, spot.z, GALLOP_SPEED);
            if (RIDE_OUT.size() > 256) RIDE_OUT.values().removeIf(t -> t < now);
            RIDE_OUT.put(guard.getUUID(), now + 30L);
            net.hfstack.rallyguard.debug.SimProbe.count(guard, "act:rideOut");
            return true;
        }
        return false;
    }

    // ==== E2A B10: path budget (FEEDBACK_E1: single 35 ms "mounts" spike with 200 guards) ====

    /** Most NEW mount paths computed per server tick across all riders; the rest keep
     *  their current path and try again on their next drive (half a second later). */
    private static final int PATHS_PER_TICK = 6;
    /** A running path whose end is this close to the wanted spot is kept as is. */
    private static final double KEEP_PATH_WITHIN = 2.5D;

    private static long budgetTick = Long.MIN_VALUE;
    private static int budgetUsed;

    private static void repath(ServerLevel level, PathfinderMob mount, Entity target, double speed) {
        repath(level, mount, target, target.getX(), target.getY(), target.getZ(), speed);
    }

    /**
     * Sends the mount toward a spot (or an entity) without re-running A* every drive: an
     * unfinished path already ending near the spot only gets its speed refreshed, and new
     * paths are capped per tick ({@link #PATHS_PER_TICK}) so a whole cavalry squad
     * re-pathing on the same tick can't stack up into one lag spike.
     */
    private static void repath(ServerLevel level, PathfinderMob mount, Entity target,
                               double x, double y, double z, double speed) {
        BlockPos end = mount.getNavigation().isDone() ? null : mount.getNavigation().getTargetPos();
        if (end != null && end.distToCenterSqr(x, y, z) < KEEP_PATH_WITHIN * KEEP_PATH_WITHIN) {
            mount.getNavigation().setSpeedModifier(speed);
            return;
        }
        long now = level.getGameTime();
        if (now != budgetTick) {
            budgetTick = now;
            budgetUsed = 0;
        }
        if (budgetUsed >= PATHS_PER_TICK) return;
        budgetUsed++;
        if (target != null) {
            mount.getNavigation().moveTo(target, speed);
        } else {
            mount.getNavigation().moveTo(x, y, z, speed);
        }
    }
}
