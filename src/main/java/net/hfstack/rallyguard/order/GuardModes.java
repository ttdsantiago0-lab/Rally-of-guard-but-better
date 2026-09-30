package net.hfstack.rallyguard.order;

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

    /** Last flags seen per loaded guard, to know which flag was turned on most recently. */
    private static final Map<UUID, Byte> LAST_SEEN = new HashMap<>();

    public static Mode modeOf(Guard guard) {
        if (guard.isPatrolling() && guard.getPatrolPos() != null) return Mode.POST;
        if (guard.isFollowing()) return Mode.FOLLOW;
        return Mode.FREE;
    }

    /** FREE: no post, not following - Guard Villagers' own default behavior. */
    public static void free(Guard guard) {
        clearWard(guard);
        GuardPosts.unpost(guard);
        guard.setFollowing(false);
        remember(guard);
    }

    /** FOLLOW: leave any post and follow the commander. */
    public static void follow(Guard guard) {
        clearWard(guard);
        GuardPosts.unpost(guard);
        guard.setFollowing(true);
        remember(guard);
    }

    /** POST at {@code pos} (see {@link GuardPosts#post}). */
    public static void post(Guard guard, BlockPos pos) {
        clearWard(guard);
        GuardPosts.post(guard, pos);
        remember(guard);
    }

    /** POST at a formation slot and start walking (or riding) there right away - see
     *  {@link GuardPosts#postAndMove}. Muster, Reform and Advance use this. */
    public static BlockPos postAndMove(Guard guard, Vec3 target, double speed) {
        clearWard(guard);
        BlockPos slot = GuardPosts.postAndMove(guard, target, speed);
        remember(guard);
        return slot;
    }

    /** DEFEND's own re-posting next to its ward: the same POST, but it keeps the ward
     *  (every other order ends a ward - see {@link #clearWard}). Only the ward loop and
     *  the Defend order itself call this. */
    public static void postKeepingWard(Guard guard, BlockPos pos) {
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
        boolean following = guard.isFollowing();
        boolean patrolling = guard.isPatrolling();

        if (patrolling && guard.getPatrolPos() == null) {
            guard.setPatrolling(false); // "patrolling nowhere" - Guard Villagers ignores it anyway
            patrolling = false;
        }
        if (following && patrolling) {
            Byte previous = LAST_SEEN.get(guard.getUUID());
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

    public static void forget(UUID guardId) {
        LAST_SEEN.remove(guardId);
    }

    public static void clearAll() {
        LAST_SEEN.clear();
    }

    private static void remember(Guard guard) {
        byte flags = 0;
        if (guard.isFollowing()) flags |= FOLLOWING;
        if (guard.isPatrolling()) flags |= PATROLLING;
        LAST_SEEN.put(guard.getUUID(), flags);
    }
}
