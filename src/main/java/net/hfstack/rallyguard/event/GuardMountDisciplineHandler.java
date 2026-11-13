package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.order.KamikazeOrder;
import net.hfstack.rallyguard.order.MountDriver;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
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
 * <p>
 * <b>Phase 3F - that support now exists for horses and other ground mounts.</b> This
 * handler is now the mount DRIVER loop: twice a second, every owned guard riding a
 * drivable mount ({@link MountDriver#isDrivable}) has its intent - target, post, or
 * following its commander - handed to the mount's own navigation
 * ({@link MountDriver#drive}), so cavalry follows, musters, holds posts and charges
 * without dismounting. Only guards on something the driver can't steer (a boat, a
 * minecart...) are still dismounted when told to follow or hold a post, as before.
 */
public final class GuardMountDisciplineHandler {

    /** Twice a second: often enough that the mount's own wander goals can't drag a rider
     *  far off course, rare enough to stay cheap with many guards. */
    private static final int DRIVE_INTERVAL_TICKS = 10;

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!net.hfstack.rallyguard.config.RallyConfig.on(net.hfstack.rallyguard.config.RallyConfig.MOUNT_DRIVER)) return;
        // E1-E: no entity scan here - the stance loop's once-a-second guard list, and each
        // tick drives the riders whose index falls in this tick's tenth (every rider
        // still gets two updates a second, spread instead of all at once).
        long t0 = net.hfstack.rallyguard.debug.Perf.t0();
        List<Guard> loaded = GuardStanceHandler.loaded(level.dimension());
        int phase = (int) (level.getGameTime() % DRIVE_INTERVAL_TICKS);
        for (int i = phase; i < loaded.size(); i += DRIVE_INTERVAL_TICKS) {
            Guard guard = loaded.get(i);
            if (guard.isRemoved() || !guard.isPassenger()) continue;
            SafeTick.run(guard, () -> {
                PathfinderMob mount = MountDriver.mountOf(guard);
                if (mount != null) {
                    // One guard per mount (user decision, 2026-09-29): only the first rider
                    // is the mount's guard; any other guard sharing it gets off.
                    if (mount.getFirstPassenger() != guard) {
                        guard.stopRiding();
                        return;
                    }
                    MountDriver.applyBondHealth(guard, mount); // 2026-11-13: the bond keeps the horse alive
                    if (!KamikazeOrder.isRunning(guard)) MountDriver.drive(level, guard, mount);
                } else if (guard.isFollowing() || guard.isPatrolling()) {
                    guard.stopRiding();
                }
            });
        }
        net.hfstack.rallyguard.debug.Perf.add(net.hfstack.rallyguard.debug.Perf.Sys.MOUNTS, t0);
    }
}
