package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/** E1-G: sets the selected guards' {@link FireMode} (order ids FIRE_NORMAL, FIRE_STILL,
 *  FIRE_ADVANCING, FIRE_RETREATING, FIRE_VOLLEY). Orthogonal to the movement mode. */
public final class FireModeOrder implements OrderExecutor {
    private final FireMode mode;

    public FireModeOrder(FireMode mode) {
        this.mode = mode;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        DirectiveRegistry directives = DirectiveRegistry.get(player.serverLevel());
        for (Guard guard : guards) {
            directives.setFireMode(guard.getUUID(), mode.name());
        }
    }
}
