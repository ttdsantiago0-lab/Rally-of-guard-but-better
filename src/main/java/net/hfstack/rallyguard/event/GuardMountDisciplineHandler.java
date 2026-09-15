package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Fixes a real bug reported by the user (2026-09-15): Guard Villagers' OWN native
 * inventory-GUI menu - the basic right-click screen with its own Follow and Patrol
 * toggle buttons ({@code GuardFollowPacket}/{@code GuardSetPatrolPosPacket}, confirmed
 * by reading {@code GuardInventoryScreen.java}'s real source directly) - does not
 * dismount a mounted guard before/after toggling either flag. This project already
 * fixed the exact same "horse dominates the guard" problem for its OWN three trigger
 * points (Ledger Summon in {@code GuardCommandNetworking#summon}, Ledger Patrol-toggle
 * in {@code #togglePatrol}, and Rally cast in
 * {@code ScrollOfRallyingItem#rallyGuardToPlayer}) by calling
 * {@code guard.stopRiding()} before moving/reassigning the guard - but Guard Villagers'
 * own native menu is entirely its own packet/handler code that this project has never
 * touched, so a guard toggled to Follow or Patrol from THAT menu while already mounted
 * kept riding, and the horse's own independent AI (wander/idle - horses have no "follow
 * owner" goal in vanilla) kept moving it regardless of what the guard was told to do,
 * exactly matching the user's report ("el caballo aun domina al guardia").
 * <p>
 * Rather than duplicate the same dismount check at yet another trigger point - or
 * attempt to intercept Guard Villagers' own packets, which this project's mixin-avoidance
 * policy (PORTING_PLAN.md section 4.2) rules out - this generalizes the fix into one
 * periodic (once-a-second, matching {@link GuardStanceHandler}'s own scan interval)
 * sweep: any owned guard that is BOTH currently mounted AND either following or
 * patrolling gets dismounted. This covers every current and future trigger point
 * automatically (our own Ledger, Rally, and Guard Villagers' native menu alike) with a
 * single, small, typed-API-only check - no new state, no NBT, no mixin. The mounts
 * datapack already excludes {@code Following:1b}/{@code Patrolling:1b} guards from its
 * own auto-mount selector, so a guard dismounted here will not be immediately
 * re-mounted by that separate system.
 * <p>
 * This is deliberately a STOPGAP, not a real "mounted guard follows/patrols while
 * riding" feature - it exists because neither vanilla horses nor Guard Villagers'
 * {@code FollowHeroGoal}/{@code WalkBackToCheckPointGoal} know how to move a mount a
 * non-player rider is sitting on. Real mounted-movement support belongs to the master
 * vision brief's Transport abstraction (see {@code rally-of-the-guard-vision.md}) and
 * is expected to land as part of (or alongside) Phase 7, not here.
 */
public final class GuardMountDisciplineHandler {

    private static final int SCAN_INTERVAL_TICKS = 20;

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (level.getGameTime() % SCAN_INTERVAL_TICKS != 0) return;

        List<? extends Guard> guards = level.getEntities(EntityTypeTest.forClass(Guard.class),
                GuardOwnership::hasOwner);
        for (Guard guard : guards) {
            if (!guard.isPassenger()) continue;
            if (guard.isFollowing() || guard.isPatrolling()) {
                guard.stopRiding();
            }
        }
    }
}
