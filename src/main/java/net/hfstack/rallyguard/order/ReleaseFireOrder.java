package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3E: RELEASE FIRE - lifts {@link HoldFireOrder}. Guards with a designated target
 * that is already within reach open on it in the same tick (every selected guard at
 * once, which is what makes a volley); the rest simply return to their stance's normal
 * judgement, and the once-a-second scan in {@code GuardStanceHandler} picks up their
 * designated target as soon as it comes into reach.
 */
public final class ReleaseFireOrder implements OrderExecutor {
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        DirectiveRegistry directives = DirectiveRegistry.get(player.serverLevel());
        for (Guard guard : guards) {
            directives.setHoldFire(guard.getUUID(), false);

            DirectiveRegistry.Directive directive = directives.directiveOf(guard.getUUID());
            if (directive.target() == null) continue;
            Entity entity = player.serverLevel().getEntity(directive.target());
            if (entity instanceof LivingEntity target
                    && CommandTargeting.wouldAccept(guard, target)
                    && CommandTargeting.inReach(guard, target, CommandTargeting.PURSUIT_RANGE)) {
                CommandTargeting.issue(guard, target);
            }
        }
    }
}
