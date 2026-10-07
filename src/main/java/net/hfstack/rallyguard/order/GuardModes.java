package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.compat.McaMobCompat;
import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Phase 3G: the ONE place that decides where a guard wants to be - its movement mode.
 * <p>
 * <b>What was wrong.</b> Guard Villagers keeps exactly two native flags - {@code Following}
 * and {@code Patrolling} (+ a patrol position) - and its own inventory screen toggles
 * them independently (read from its real source: {@code GuardFollowPacket} flips
 * following, {@code GuardSetPatrolPosPacket} sets patrolling + position; neither touches
 * the other). Rally then wrote the same flags from half a dozen places, each with its
 * own idea of what to clear. Result: a guard could be "following" AND "patrolling" at
 * once (Guard Villagers' goals then silently let Follow win, so a guard told to stay
 * kept walking off), a "Release" button cleared Follow as a side effect, and so on.
 * <p>
 * <b>Principle.</b> A guard is in exactly one mode, and the native flags ARE that mode -
 * no parallel Rally copy, so Guard Villagers' own buttons and the Ledger always agree:
 * <ul>
 *     <li>{@link Mode#FREE} - neither flag: Guard Villagers' own roaming / village duty.</li>
 *     <li>{@link Mode#FOLLOW} - following its commander (this is also what a Rally is).</li>
 *     <li>{@link Mode#POST} - holding a position (Guard Villagers' "patrol" point): a
 *     formation slot, a wall, the spot it was summoned to.</li>
 * </ul>
 * Everything else a guard has - stance, hold fire, designated target, formation slot,
 * squad - is orthogonal to the mode and never changes it. Orders are the only things
 * that change modes, and only through the three setters below.
 * <p>
 * If the native screen still produces both flags (click Follow on a posted guard, or
 * Patrol on a following one), {@link #normalize} resolves it within a second in favor of
 * the flag that was turned on LAST - i.e. the player's latest click wins, whichever UI
 * it came from.
 */
public final class GuardModes {
    private GuardModes() {
    }

    public enum Mode { FREE, FOLLOW, POST }

    private static final byte FOLLOWING = 1;
    private static final byte PATROLLING = 2;

    /** What Rally last saw (or wrote) per loaded guard: flags, post and owner. Used to know
     *  which flag was turned on most recently and, since E1, to spot changes made behind
     *  Rally's back (MCA Mob Compat's proxy villager writes these flags and the owner). */
    private record Seen(byte flags, BlockPos post, UUID owner) {
    }

    private static final Map<UUID, Seen> LAST_SEEN = new HashMap<>();
    /** guard -> game time of its owner's last right-click interaction with it. */
    private static final Map<UUID, Long> OWNER_TOUCH = new HashMap<>();
    /** guard -> {window start, reverts in it} - to report MCA fighting back every tick. */
    private static final Map<UUID, long[]> REVERTS = new HashMap<>();
    private static final long ADOPT_WINDOW_TICKS = 20L * 60L;
    private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();

    /** The owner just interacted with this guard (GV screen or MCA menu): flag changes in
     *  the next ~60 s are taken as the owner's orders. */
    public static void noteOwnerInteraction(Guard guard) {
        OWNER_TOUCH.put(guard.getUUID(), guard.level().getGameTime());
    }

    public static Mode modeOf(Guard guard) {
        if (guard.isPatrolling() && guard.getPatrolPos() != null) return Mode.POST;
        if (guard.isFollowing()) return Mode.FOLLOW;
        return Mode.FREE;
    }

    /** FREE: no post, not following - Guard Villagers' own default behavior. */
    public static void free(Guard guard) {
        net.hfstack.rallyguard.command.SlotQueue.cancel(guard.getUUID()); // E2A: a newer order wins
        clearWard(guard);
        GuardPosts.unpost(guard);
        guard.setFollowing(false);
        remember(guard);
    }

    /** FOLLOW: leave any post and follow the commander. */
    public static void follow(Guard guard) {
        net.hfstack.rallyguard.command.SlotQueue.cancel(guard.getUUID()); // E2A: a newer order wins
        clearWard(guard);
        GuardPosts.unpost(guard);
        guard.setFollowing(true);
        remember(guard);
    }

    /** POST at {@code pos} (see {@link GuardPosts#post}). */
    public static void post(Guard guard, BlockPos pos) {
        net.hfstack.rallyguard.command.SlotQueue.cancel(guard.getUUID()); // E2A: a newer order wins
        clearWard(guard);
        GuardPosts.post(guard, pos);
        remember(guard);
    }

    /** POST at a formation slot and start walking (or riding) there right away - see
     *  {@link GuardPosts#postAndMove}. Muster, Reform and Advance use this. */
    public static BlockPos postAndMove(Guard guard, Vec3 target, double speed) {
        net.hfstack.rallyguard.command.SlotQueue.cancel(guard.getUUID()); // E2A: a newer order wins
        clearWard(guard);
        BlockPos slot = GuardPosts.postAndMove(guard, target, speed);
        remember(guard);
        return slot;
    }

    /** DEFEND's own re-posting next to its ward: the same POST, but it keeps the ward
     *  (every other order ends a ward - see {@link #clearWard}). Only the ward loop and
     *  the Defend order itself call this. */
    public static void postKeepingWard(Guard guard, BlockPos pos) {
        net.hfstack.rallyguard.command.SlotQueue.cancel(guard.getUUID()); // E2A: a newer order wins
        GuardPosts.post(guard, pos);
        remember(guard);
    }

    /** POST right where the guard stands. */
    public static void postHere(Guard guard) {
        post(guard, GuardPosts.standableNear(guard.level(), Vec3.atBottomCenterOf(guard.blockPosition())));
    }

    /**
     * Resolves contradictory native flags (see class javadoc) and records the current
     * ones. Called once a second for every loaded owned guard.
     */
    public static void normalize(Guard guard) {
        if (McaMobCompat.active() && reconcileExternal(guard)) return;
        boolean following = guard.isFollowing();
        boolean patrolling = guard.isPatrolling();

        if (patrolling && guard.getPatrolPos() == null) {
            guard.setPatrolling(false); // "patrolling nowhere" - Guard Villagers ignores it anyway
            patrolling = false;
        }
        if (following && patrolling) {
            Seen seen = LAST_SEEN.get(guard.getUUID());
            Byte previous = seen == null ? null : seen.flags();
            boolean followTurnedOnLast = previous != null
                    && (previous & PATROLLING) != 0 && (previous & FOLLOWING) == 0;
            if (followTurnedOnLast) {
                GuardPosts.unpost(guard);
            } else {
                guard.setFollowing(false); // patrol turned on last (or unknown): hold the spot
            }
        }
        remember(guard);
    }

    /** DEFEND is itself a movement driver (it keeps re-posting the guard next to its
     *  ward), so any other mode order ends it - one authority over where a guard goes. */
    private static void clearWard(Guard guard) {
        if (guard.level() instanceof ServerLevel level) {
            DirectiveRegistry registry = DirectiveRegistry.get(level);
            if (registry.directiveOf(guard.getUUID()).ward() != null) {
                registry.setWard(guard.getUUID(), null, "");
            }
        }
    }

    /**
     * E1 (David's decision: MCA keeps the guard's identity, but Rally owns movement and
     * ownership). Only when MCA Mob Compat is installed: if the flags or the owner changed
     * since Rally last saw them, without going through this class -
     * <ul>
     *     <li>a changed owner of a hired guard is put back (and logged);</li>
     *     <li>changed flags are ADOPTED as the owner's order if the owner interacted with
     *     the guard in the last ~60 s (MCA "follow" -> {@link #follow}, "stay" ->
     *     {@link #postHere}), otherwise REVERTED.</li>
     * </ul>
     * Returns true if it handled the guard this pass.
     */
    private static boolean reconcileExternal(Guard guard) {
        Seen seen = LAST_SEEN.get(guard.getUUID());
        if (seen == null) return false;
        byte now = flagsOf(guard);
        UUID owner = guard.getOwnerId();
        boolean ownerChanged = seen.owner() != null && !seen.owner().equals(owner);
        if (!ownerChanged && now == seen.flags()) return false;

        if (ownerChanged) {
            guard.setOwnerId(seen.owner());
            LOGGER.warn("[rallyguard] Owner of guard {} changed outside Rally ({} -> {}); restored",
                    guard.getUUID(), seen.owner(), owner);
        }
        if (now != seen.flags()) {
            Long touched = OWNER_TOUCH.get(guard.getUUID());
            boolean ownerAsked = touched != null && guard.level().getGameTime() - touched <= ADOPT_WINDOW_TICKS;
            if (ownerAsked) {
                if (guard.isFollowing()) follow(guard);
                else if (guard.isPatrolling()) postHere(guard);
                else free(guard);
                return true;
            }
            guard.setFollowing((seen.flags() & FOLLOWING) != 0);
            if ((seen.flags() & PATROLLING) != 0 && seen.post() != null) {
                guard.setPatrolPos(seen.post());
                guard.setPatrolling(true);
            } else {
                guard.setPatrolling(false);
            }
            noteRevert(guard);
        }
        remember(guard);
        return true;
    }

    private static void noteRevert(Guard guard) {
        long now = guard.level().getGameTime();
        long[] w = REVERTS.computeIfAbsent(guard.getUUID(), k -> new long[]{now, 0});
        if (now - w[0] > 600) {
            w[0] = now;
            w[1] = 0;
        }
        if (++w[1] == 3) {
            LOGGER.warn("[rallyguard] Guard {} keeps getting its movement changed outside Rally "
                    + "(MCA Mob Compat?) - reverted 3 times in 30 s", guard.getUUID());
        }
    }

    private static byte flagsOf(Guard guard) {
        byte flags = 0;
        if (guard.isFollowing()) flags |= FOLLOWING;
        if (guard.isPatrolling()) flags |= PATROLLING;
        return flags;
    }

    public static void forget(UUID guardId) {
        OWNER_TOUCH.remove(guardId);
        REVERTS.remove(guardId);
        LAST_SEEN.remove(guardId);
    }

    public static void clearAll() {
        LAST_SEEN.clear();
        OWNER_TOUCH.clear();
        REVERTS.clear();
    }

    private static void remember(Guard guard) {
        LAST_SEEN.put(guard.getUUID(), new Seen(flagsOf(guard), guard.getPatrolPos(), guard.getOwnerId()));
    }
}
