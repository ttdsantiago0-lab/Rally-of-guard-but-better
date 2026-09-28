package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.List;

/**
 * "Fight THIS." - Phase 3E rework of the Phase 3D Attack order.
 * <p>
 * Phase 3D's Attack always meant "nearest monster to me", applied with a one-shot
 * {@code setTarget} that (a) was silently vetoed for DEFENSIVE/PASSIVE guards by their own
 * stance gate, and (b) was forgotten by the guard the moment anything else happened. Now:
 * <ul>
 *     <li>The commander picks WHO via a {@link TargetSpec} (nearest hostile, whoever
 *     attacked us, the entity marked with the Ledger, or a specific player), resolved
 *     server-side before this order is built.</li>
 *     <li>The target is stored as each guard's designated target in
 *     {@link DirectiveRegistry} - a standing instruction {@code GuardStanceHandler} keeps
 *     re-applying (once a second) whenever the guard loses it and it is back in reach.
 *     Players are never auto-cleared from it (they respawn); dead mobs are.</li>
 *     <li>Attack also lifts HOLD FIRE for these guards - an explicit "fight this" order
 *     is the commander releasing them.</li>
 *     <li>Whether guards leave their position to do it is NOT decided here: a posted
 *     guard (Muster/Hold Ground) engages the target only inside its post leash, a free
 *     guard pursues it. The same order therefore produces a charging cavalry squad or a
 *     wall of archers focusing one enemy, depending on how the squad was deployed - the
 *     composability the design brief asks for, without a second order.</li>
 * </ul>
 * A {@code null} target (spec {@link TargetSpec#NONE}) clears the designated target and
 * the current fight, returning the squad to its stance's own judgement.
 */
public final class AttackOrder implements OrderExecutor {

    @Nullable
    private final LivingEntity target;

    public AttackOrder(@Nullable LivingEntity target) {
        this.target = target;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        DirectiveRegistry directives = DirectiveRegistry.get(player.serverLevel());
        for (Guard guard : guards) {
            if (target == null) {
                directives.setTarget(guard.getUUID(), null, "");
                CommandTargeting.disengage(guard);
                continue;
            }
            directives.setTarget(guard.getUUID(), target.getUUID(), target.getName().getString());
            directives.setHoldFire(guard.getUUID(), false);
            if (CommandTargeting.wouldAccept(guard, target)
                    && CommandTargeting.inReach(guard, target, CommandTargeting.PURSUIT_RANGE)) {
                CommandTargeting.issue(guard, target);
            }
        }
    }
}
