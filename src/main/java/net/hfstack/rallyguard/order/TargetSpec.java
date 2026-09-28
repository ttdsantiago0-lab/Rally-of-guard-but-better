package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Phase 3E: the small, fixed set of ways a commander can say WHO a squad fights - kept
 * deliberately short and predictable instead of a general target-acquisition system:
 * <ul>
 *     <li>{@link #NEAREST} - the nearest hostile to the commander right now (what the old
 *     Attack button always did).</li>
 *     <li>{@link #ATTACKER} - counterattack: whoever most recently hurt the commander or
 *     one of their guards.</li>
 *     <li>{@link #MARKED} - the entity the commander pointed at with the Ledger
 *     (sneak + right-click).</li>
 *     <li>{@code PLAYER:<uuid>} - a specific player in the same dimension (the PvP case:
 *     "these guards fight THIS player").</li>
 *     <li>{@link #NONE} - no designated target; the squad goes back to its stance.</li>
 * </ul>
 * Specs travel as plain strings (same fail-soft convention as stance/order ids): an
 * unknown spec resolves to nothing rather than desyncing anything. The client only ever
 * sends the id; the server re-resolves it itself and never trusts a client-side name.
 */
public final class TargetSpec {
    private TargetSpec() {
    }

    public static final String NEAREST = "NEAREST";
    public static final String ATTACKER = "ATTACKER";
    public static final String MARKED = "MARKED";
    public static final String NONE = "NONE";
    public static final String PLAYER_PREFIX = "PLAYER:";

    private static final double NEAREST_RADIUS = 32.0D;

    public static String player(UUID playerId) {
        return PLAYER_PREFIX + playerId;
    }

    /** Resolves {@code spec} to a live entity in the commander's level, or null if the
     *  spec is {@link #NONE}, unknown, or names something no longer there. */
    @Nullable
    public static LivingEntity resolve(String spec, ServerPlayer commander, List<Guard> guards) {
        if (spec == null) return null;
        ServerLevel level = commander.serverLevel();
        if (spec.equals(NEAREST)) {
            return nearestHostile(level, commander, guards.isEmpty() ? null : guards.get(0));
        }
        if (spec.equals(ATTACKER)) {
            return CommanderMarks.recentAggressor(commander.getUUID(), level);
        }
        if (spec.equals(MARKED)) {
            return CommanderMarks.markedEntity(commander.getUUID(), level);
        }
        if (spec.startsWith(PLAYER_PREFIX)) {
            try {
                UUID id = UUID.fromString(spec.substring(PLAYER_PREFIX.length()));
                Player target = level.getPlayerByUUID(id);
                return target != null && target.isAlive() && target != commander ? target : null;
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    /** The nearest living thing near the commander that is plainly an enemy - a monster,
     *  or another commander's guard currently fighting our side - and that the guards
     *  would actually accept as a target (see {@link CommandTargeting#wouldAccept}). */
    @Nullable
    private static LivingEntity nearestHostile(ServerLevel level, ServerPlayer commander, @Nullable Guard sample) {
        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class,
                commander.getBoundingBox().inflate(NEAREST_RADIUS),
                c -> c.isAlive()
                        && (c instanceof Monster || (sample != null && CommandTargeting.isHostileGuard(sample, c)))
                        && (sample == null || CommandTargeting.wouldAccept(sample, c)));

        LivingEntity nearest = null;
        double best = NEAREST_RADIUS * NEAREST_RADIUS;
        for (LivingEntity candidate : nearby) {
            double d = candidate.distanceToSqr(commander);
            if (d <= best) {
                best = d;
                nearest = candidate;
            }
        }
        return nearest;
    }
}
