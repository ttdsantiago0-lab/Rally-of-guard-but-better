package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.tactics.Doctrine;
import net.hfstack.rallyguard.tactics.Tactics;
import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * E2A block 5 - sets the playbook doctrine (decision-ladder weights, {@link Doctrine}) for the
 * selected guards; registered as {@code DOCTRINE_<NAME>}. The Battle Planner (E3) will only
 * have to issue these. Saved per guard in DirectiveRegistry (E3A F6).
 */
public final class DoctrineOrder implements OrderExecutor {
    private final Doctrine doctrine;

    public DoctrineOrder(Doctrine doctrine) {
        this.doctrine = doctrine;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        for (Guard guard : guards) Tactics.setDoctrine(player.serverLevel(), guard.getUUID(), doctrine);
    }
}
