package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.entity.EntityTypeTest;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3D: "engage the nearest threat to me, right now" - a deliberately simplified
 * first pass of Attack. The brief's ideal ("select guard/group, select a specific
 * target entity, Attack") doesn't map cleanly onto how this order is actually issued:
 * the Commander's Ledger is an open GUI when the player clicks Attack, so "whatever is
 * under the player's crosshair" means nothing at that moment - the crosshair isn't on
 * the world while a menu is open. Building real click-to-target selection needs either
 * a server-side world raycast or a separate "mark a target in the world first" step,
 * both genuinely more machinery than this pass calls for, and risks overcomplicating
 * targeting exactly the way the design brief's own Kamikaze section warns against.
 * <p>
 * Instead: the target is resolved deterministically, server-side - the nearest real
 * vanilla {@link Monster} within {@link #SEARCH_RADIUS} of the PLAYER (not the guards,
 * who may be scattered) that isn't one of the player's own guards. This is "explicit"
 * in the sense that matters here (a stated, reproducible rule, not emergent AI
 * wandering aggro) and fully server-validated (the scan only ever finds currently
 * loaded, currently alive entities - nothing client-supplied to distrust). No mob
 * blacklist is consulted, mirroring {@code Stance#RUTHLESS}'s already-proven direct-
 * {@code setTarget} bypass ({@code GuardStanceHandler}'s periodic scan uses the exact
 * same mechanism this class does) - a manually-issued Attack order is a deliberate
 * player decision, not a stance quietly picking fights on its own.
 * <p>
 * True click/mark-a-specific-target selection is real future work, not built here - see
 * PORTING_PLAN.md's Phase 3D section for the tradeoff this simplification makes.
 */
public final class AttackOrder implements OrderExecutor {

    private static final double SEARCH_RADIUS = 32.0D;
    private static final double SEARCH_RADIUS_SQ = SEARCH_RADIUS * SEARCH_RADIUS;

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        ServerLevel level = player.serverLevel();
        Monster target = findNearestMonster(level, player);
        if (target == null) {
            player.sendSystemMessage(Component.translatable("gui.rallyguard.command.attack_no_target"));
            return;
        }

        for (Guard guard : guards) {
            guard.setTarget(target);
        }
    }

    private Monster findNearestMonster(ServerLevel level, ServerPlayer player) {
        List<? extends LivingEntity> nearby = level.getEntities(EntityTypeTest.forClass(LivingEntity.class),
                candidate -> candidate instanceof Monster
                        && candidate.isAlive()
                        && !GuardOwnership.isOwnedBy(candidate, player.getUUID())
                        && candidate.distanceToSqr(player) <= SEARCH_RADIUS_SQ);

        Monster nearest = null;
        double nearestDistSq = SEARCH_RADIUS_SQ;
        for (LivingEntity candidate : nearby) {
            double distSq = candidate.distanceToSqr(player);
            if (distSq <= nearestDistSq) {
                nearest = (Monster) candidate;
                nearestDistSq = distSq;
            }
        }
        return nearest;
    }
}
