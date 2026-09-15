package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.Stance;
import net.hfstack.rallyguard.contract.StanceRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import tallestegg.guardvillagers.common.entities.Guard;
import tallestegg.guardvillagers.configuration.GuardConfig;

import java.util.List;
import java.util.UUID;

/**
 * Phase 3C: enforces the three non-default {@link Stance} values for owned guards
 * ({@link Stance#DEFENSIVE} needs no code at all - it IS Guard Villagers' own
 * out-of-the-box AI, untouched).
 * <p>
 * {@code LivingChangeTargetEvent} confirmed real on this exact NeoForge/MC 1.21.1
 * version via TWO independent sources, per this project's established rule for a
 * first-time-used API: nekoyue's ForgeJavaDocs-NG mirror (pinned to
 * {@code 1.21.x-neoforge}) and the real NeoForge source at
 * {@code github.com/neoforged/NeoForge} on the {@code 1.21.x} branch. The two sources
 * actually DISAGREED on the method names - the javadoc mirror said
 * {@code getNewTarget()}/{@code setNewTarget()}/{@code getOriginalTarget()}, while the
 * raw source (more literal, and matching a known javadoc-accuracy issue reported against
 * this exact class - {@code neoforged/NeoForge#1996}) gives the real names:
 * {@code getNewAboutToBeSetTarget()}/{@code setNewAboutToBeSetTarget(LivingEntity)}/
 * {@code getOriginalAboutToBeSetTarget()}. The raw source wins per this project's
 * established two-source-conflict resolution (trust the more literal source - see the
 * {@code EditBox.setHint} lesson in PORTING_PLAN.md). The class extends
 * {@code LivingEvent} (so {@code getEntity()} is inherited) and implements
 * {@code ICancellableEvent}, same shape already proven working in this project for
 * {@code LivingIncomingDamageEvent} ({@code GuardFallDamageHandler}) - {@code
 * event.setCanceled(true)} blocks the target change entirely.
 * <p>
 * {@link Stance#PASSIVE} cancels every target-change attempt for the guard - this is
 * deliberately broader than "don't retaliate when hit": it also blocks
 * {@code HeroHurtTargetGoal} (joining the owner's fight), {@code DefendVillageGuardGoal},
 * and the vanilla anger-at-a-player goal, since "won't fight back" was the user's own
 * framing, not "won't retaliate specifically."
 * <p>
 * {@link Stance#AGGRESSIVE} and {@link Stance#RUTHLESS} both need a guard to acquire a
 * target it was never given by being hit or by its owner's fight - something none of
 * Guard Villagers' own {@code targetSelector} goals do without the global
 * {@code AttackAllMobs} config flag (which is not per-guard, and this project needs a
 * per-guard stance). Since {@code Mob#targetSelector} is a protected field (not
 * reachable from outside the class without extending it, and this project deliberately
 * avoids mixins - PORTING_PLAN.md section 4.2), a real AI Goal can't be added to an
 * existing {@code Guard} instance from here. Instead, {@link #onLevelTick} periodically
 * (once a second, not every tick - see {@link #SCAN_INTERVAL_TICKS}) scans for a nearby
 * valid target and calls {@code guard.setTarget(...)} directly when the guard currently
 * has none: this is the EXACT SAME mechanism Guard Villagers' own
 * {@code HurtByTargetGoal}/{@code HeroHurtTargetGoal} use to hand a guard a target, so
 * the existing, unmodified attack-execution goals ({@code GuardMeleeGoal},
 * {@code GuardBowAttack}, the crossbow goal) pick it up on their own very next
 * {@code canUse()} check with zero new combat code needed.
 * <p>
 * {@link Stance#AGGRESSIVE} still respects Guard Villagers' own
 * {@code GuardConfig.COMMON.MobBlackList} (Creepers etc. stay protected) - it only
 * considers a real vanilla {@code Monster} that isn't on that list, i.e. "engage nearby
 * threats," not "attack literally anything." {@link Stance#RUTHLESS} ("modo asesinos")
 * deliberately does NOT consult the blacklist at all - confirmed feasible by reading
 * {@code Guard.java}'s real source directly: {@code Guard#canAttack(LivingEntity)} (the
 * method that actually enforces the blacklist) is consulted only inside the
 * target-ACQUISITION goals' own {@code TargetingConditions} (built by
 * {@code NearestAttackableTargetGoal}), never by the attack-EXECUTION goals -
 * {@code GuardMeleeGoal extends MeleeAttackGoal} and its own
 * {@code checkAndPerformAttack}/{@code canPerformAttack}/{@code doHurtTarget} chain never
 * calls {@code canAttack} again. A target handed to the guard by THIS class - bypassing
 * acquisition entirely - is therefore never blacklist-checked. Not yet in-game confirmed
 * that a Ruthless guard actually LANDS a hit on an otherwise-blacklisted mob (e.g. a
 * Creeper), as opposed to merely walking toward it - flagged as an open item in
 * PORTING_PLAN.md until tested. Both stances exclude the guard's own owner and every
 * other guard owned by that same owner (never friendly fire your own army, reusing
 * {@link GuardOwnership#isOwnedBy}), and RUTHLESS additionally excludes {@code Player}s
 * outright as a deliberate first-pass safety choice - unprovoked player-vs-player combat
 * is intentionally left to Phase 3D's future "attack a specific player" ORDER, which the
 * owner deliberately issues, rather than something a stance triggers automatically just
 * by a bystander player walking near a Ruthless guard.
 */
public final class GuardStanceHandler {

    private static final double AGGRO_RADIUS = 16.0D;
    private static final double AGGRO_RADIUS_SQ = AGGRO_RADIUS * AGGRO_RADIUS;

    /** Once a second (20 ticks), not every tick - a nearby-entity scan for every owned
     *  guard on every tick would not scale to the ~100+ guard vision this project is
     *  built around. A 1-second granularity for ACQUIRING a new target is not expected
     *  to feel sluggish in practice, but this hasn't been in-game confirmed yet. */
    private static final int SCAN_INTERVAL_TICKS = 20;

    @SubscribeEvent
    public void onChangeTarget(LivingChangeTargetEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Guard guard)) return;
        if (!GuardOwnership.hasOwner(guard)) return;
        if (!(guard.level() instanceof ServerLevel level)) return;

        Stance stance = StanceRegistry.get(level).stanceOf(guard.getUUID());
        if (stance == Stance.PASSIVE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (level.getGameTime() % SCAN_INTERVAL_TICKS != 0) return;

        List<? extends Guard> guards = level.getEntities(EntityTypeTest.forClass(Guard.class),
                GuardOwnership::hasOwner);
        if (guards.isEmpty()) return;

        StanceRegistry registry = StanceRegistry.get(level);
        for (Guard guard : guards) {
            if (guard.getTarget() != null) continue; // already has a target - existing goals handle it

            Stance stance = registry.stanceOf(guard.getUUID());
            if (stance != Stance.AGGRESSIVE && stance != Stance.RUTHLESS) continue;

            LivingEntity candidate = findTarget(level, guard, stance);
            if (candidate != null) {
                guard.setTarget(candidate);
            }
        }
    }

    private LivingEntity findTarget(ServerLevel level, Guard guard, Stance stance) {
        UUID ownerId = GuardOwnership.getOwner(guard);

        List<? extends LivingEntity> nearby = level.getEntities(EntityTypeTest.forClass(LivingEntity.class),
                candidate -> isEligible(candidate, guard, ownerId, stance));

        LivingEntity nearest = null;
        double nearestDistSq = AGGRO_RADIUS_SQ;
        for (LivingEntity candidate : nearby) {
            double distSq = candidate.distanceToSqr(guard);
            if (distSq <= nearestDistSq) {
                nearest = candidate;
                nearestDistSq = distSq;
            }
        }
        return nearest;
    }

    private boolean isEligible(LivingEntity candidate, Guard guard, UUID ownerId, Stance stance) {
        if (candidate == guard || !candidate.isAlive()) return false;
        if (candidate.distanceToSqr(guard) > AGGRO_RADIUS_SQ) return false;
        if (GuardOwnership.isOwnedBy(candidate, ownerId)) return false; // never the owner's own guards
        if (candidate instanceof Player player && player.getUUID().equals(ownerId)) return false; // never the owner

        if (stance == Stance.AGGRESSIVE) {
            return candidate instanceof Monster
                    && !GuardConfig.COMMON.MobBlackList.get().contains(candidate.getEncodeId());
        }

        // RUTHLESS: any other living entity - see the class javadoc for why Players are
        // still excluded as a deliberate first-pass safety choice.
        return !(candidate instanceof Player);
    }
}
