package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.combat.WeaponProfile;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;
import tallestegg.guardvillagers.configuration.GuardConfig;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Phase 3E: the few targeting rules every Rally system shares, in one place.
 * <p>
 * <b>Commander-issued targets.</b> NeoForge patches {@code Mob#setTarget} to post a
 * {@code LivingChangeTargetEvent} for EVERY call - including Rally's own (verified in
 * NeoForge's {@code patches/net/minecraft/world/entity/Mob.java.patch}, 1.21.x branch).
 * {@code GuardStanceHandler} uses that event as the guard's combat gate (stance, hold
 * fire, post leash), so an explicit order routed through a plain {@code setTarget} was
 * silently vetoed by the guard's own stance - e.g. the Attack order did nothing to a
 * DEFENSIVE (default) guard. {@link #issue} marks the call as the commander's so the
 * gate lets it through: explicit orders outrank posture, which is the point of giving
 * orders. The flag is a plain static because every caller runs on the server thread.
 * <p>
 * <b>What Guard Villagers will refuse.</b> {@code Guard#setTarget} (Guard Villagers'
 * own override, read from its real 1.21.1 source) silently ignores targets that are
 * team-allied, on the {@code MobBlackList} config, the guard's owner, or a pet of the
 * owner - before the NeoForge event even fires. (It also refuses Hero of the Village,
 * but for owned guards {@code mixin.GuardHeroOfTheVillageMixin} removes that rule.) Any scan
 * that picks "the nearest candidate" must apply the same filter first, otherwise the
 * guard keeps re-picking a target it can never have (the old RUTHLESS stance did exactly
 * this next to a Creeper, and then ignored every other mob around it). {@link
 * #wouldAccept} mirrors that override line for line.
 * <p>
 * <b>Post leash.</b> A guard holding a post (Guard Villagers' patrol position - which is
 * what Muster/Reform/Hold Ground now set) only engages things within {@link #POST_LEASH}
 * of that post, and drops anything that leads it further away, so Guard Villagers'
 * {@code WalkBackToCheckPointGoal} brings it straight back. This is what lets a squad hold
 * a wall or a gate instead of chasing every mob in sight.
 */
public final class CommandTargeting {
    private CommandTargeting() {
    }

    /** How far from its post a posted guard will engage. Wide enough for a bow's
     *  ~15-block reach, small enough that a gate squad stays at the gate. */
    public static final double POST_LEASH = 16.0D;
    /** How far a free (un-posted) guard will go after its designated target on its own. */
    public static final double PURSUIT_RANGE = 32.0D;

    private static boolean issuing;

    /** Sets the guard's target as an explicit commander decision (see class javadoc).
     *  {@code null} simply disengages. */
    public static void issue(Guard guard, @Nullable LivingEntity target) {
        issuing = true;
        try {
            guard.setTarget(target);
        } finally {
            issuing = false;
        }
    }

    /** True only while {@link #issue} is running - read by the combat gate. */
    public static boolean isIssuing() {
        return issuing;
    }

    /** Clears the current target and stops moving toward it. */
    public static void disengage(Guard guard) {
        guard.setTarget(null);
        guard.setAggressive(false);
        guard.getNavigation().stop();
    }

    /** Mirrors {@code Guard#setTarget}'s own refusal rules (see class javadoc). */
    public static boolean wouldAccept(Guard guard, LivingEntity target) {
        if (target == null || !target.isAlive() || target == guard) return false;
        if (guard.isAlliedTo(target)) return false;
        if (GuardConfig.COMMON.MobBlackList.get().contains(EntityType.getKey(target.getType()).toString())) return false;
        if (target == guard.getVehicle()) return false; // never its own mount
        UUID ownerId = GuardOwnership.getOwner(guard);
        if (ownerId != null) {
            if (target instanceof Player player && player.getUUID().equals(ownerId)) return false;
            if (target instanceof TamableAnimal pet && ownerId.equals(pet.getOwnerUUID())) return false;
            // E1: horses, llamas... (OwnableEntity, not TamableAnimal) of the commander are ours too.
            if (target instanceof OwnableEntity owned && ownerId.equals(owned.getOwnerUUID())) return false;
            if (GuardOwnership.isOwnedBy(target, ownerId)) return false; // never the commander's own guards
            if (carriesOurSide(target, ownerId)) return false;
        }
        return true;
    }

    /** Phase 3G: a mount carrying the commander or one of the commander's guards is part
     *  of our side - no stance or scan may pick it (RUTHLESS used to be able to attack a
     *  squadmate's horse, or the commander's). */
    private static boolean carriesOurSide(LivingEntity mount, UUID ownerId) {
        for (Entity passenger : mount.getPassengers()) {
            if (passenger.getUUID().equals(ownerId) || GuardOwnership.isOwnedBy(passenger, ownerId)) {
                return true;
            }
        }
        return false;
    }

    /** The guard's post, or null if it isn't holding one. */
    @Nullable
    public static BlockPos postOf(Guard guard) {
        return guard.isPatrolling() ? guard.getPatrolPos() : null;
    }

    /** Whether {@code target} is somewhere this guard is allowed to go and fight it:
     *  within {@link #POST_LEASH} of its post if it holds one, otherwise within
     *  {@code freeRange} of the guard itself. */
    public static boolean inReach(Guard guard, LivingEntity target, double freeRange) {
        BlockPos post = postOf(guard);
        if (post != null) {
            double leash = leashFor(guard);
            return target.distanceToSqr(Vec3.atCenterOf(post)) <= leash * leash;
        }
        WeaponProfile weapon = WeaponProfile.of(guard.getMainHandItem());
        double range = weapon == null ? freeRange : Math.max(freeRange, weapon.maxRange());
        return target.distanceToSqr(guard) <= range * range;
    }

    /**
     * E1-G (David's test: archers marked a far zombie but didn't shoot until it was close,
     * while it shot them from further away). The post leash was 16 blocks for everyone,
     * measured to the TARGET - so a bow's real reach was cut to 16. A ranged guard's leash
     * is now {@code max(POST_LEASH, 0.8 x maxRange)} of its weapon ({@link WeaponProfile}).
     */
    public static double leashFor(Guard guard) {
        WeaponProfile weapon = WeaponProfile.of(guard.getMainHandItem());
        return weapon == null ? POST_LEASH : Math.max(POST_LEASH, 0.8D * weapon.maxRange());
    }

    /** True if {@code other} is a guard serving a DIFFERENT commander that is currently
     *  fighting our side (this guard, its commander, or another of its commander's
     *  guards). Guard Villagers never lets guards retaliate against guards
     *  ({@code HurtByTargetGoal(this, Guard.class, ...)} ignores them), which is right for
     *  one village but means two players' armies would never fight back at each other.
     *  Requiring the other guard to actually be targeting our side - not merely to have
     *  hit us once - keeps a stray arrow from a friend's army from starting a war. */
    public static boolean isHostileGuard(Guard guard, LivingEntity other) {
        if (!(other instanceof Guard enemy) || enemy == guard) return false;
        UUID ours = GuardOwnership.getOwner(guard);
        UUID theirs = GuardOwnership.getOwner(enemy);
        if (ours == null || theirs == null || ours.equals(theirs)) return false;
        if (guard.isAlliedTo(enemy)) return false;
        Player ourCommander = guard.level().getPlayerByUUID(ours);
        Player theirCommander = guard.level().getPlayerByUUID(theirs);
        if (ourCommander != null && theirCommander != null && ourCommander.isAlliedTo(theirCommander)) return false;

        LivingEntity theirTarget = enemy.getTarget();
        if (theirTarget == null) return false;
        return theirTarget.getUUID().equals(ours) || GuardOwnership.isOwnedBy(theirTarget, ours);
    }
}
