package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3E: HOLD FIRE - a combat permission, not a stance and not an archer feature.
 * <p>
 * The guards immediately drop whatever they are fighting and then refuse every new
 * target (the combat gate in {@code GuardStanceHandler} cancels it) - retaliation
 * included, because an ambush or a wall that answers the first stray arrow is not
 * holding fire. Everything else keeps working: they still hold their post, keep their
 * formation, keep their designated target in mind, and follow movement orders. Only
 * {@link ReleaseFireOrder} or an explicit {@link AttackOrder} lifts it.
 * <p>
 * Combined with Muster/Hold Ground and a designated target this gives ambushes, walls
 * that wait for the enemy to come into range, and several squads opening fire on the same
 * signal - without any squad-type-specific code.
 */
public final class HoldFireOrder implements OrderExecutor {
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        DirectiveRegistry directives = DirectiveRegistry.get(player.serverLevel());
        for (Guard guard : guards) {
            directives.setHoldFire(guard.getUUID(), true);
            CommandTargeting.disengage(guard);
        }
    }
}
