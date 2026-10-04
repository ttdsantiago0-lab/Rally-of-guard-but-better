package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.order.CommandTargeting;
import net.hfstack.rallyguard.order.GuardModes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3H.1 (user: "toca evaluar qué ocurre con un ejército si el jugador muere").
 * <p>
 * Decision (proposed, cheap, reversible): the guards that were FOLLOWING the commander
 * stop where their commander fell and HOLD that spot - which is also where the
 * commander's items dropped, so they guard them until the commander comes back. If the
 * killer is a living thing they would fight, it becomes their designated target
 * ("vengeance"), inside their post leash only, so they don't scatter across the map.
 * Posted guards (walls, HQs, other villages) keep doing exactly what they were doing:
 * one death never moves an empire. Orders resume normally after respawn.
 * <p>
 * Future (docs/BACKLOG.md, veterancy): command succession - the most veteran guard takes
 * command of a leaderless squad, up to 3-4 hand-offs.
 */
public final class CommanderDeathHandler {

    private static final double VENGEANCE_RANGE = 48.0D;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !(event.getEntity() instanceof ServerPlayer commander)) return;
        ServerLevel level = commander.serverLevel();

        List<? extends Guard> followers = level.getEntities(EntityTypeTest.forClass(Guard.class),
                g -> g.isAlive() && GuardOwnership.isOwnedBy(g, commander.getUUID())
                        && GuardModes.modeOf(g) == GuardModes.Mode.FOLLOW);
        if (followers.isEmpty()) return;

        LivingEntity killer = event.getSource().getEntity() instanceof LivingEntity k && k != commander
                && k.isAlive() && k.distanceToSqr(commander) <= VENGEANCE_RANGE * VENGEANCE_RANGE ? k : null;
        DirectiveRegistry directives = DirectiveRegistry.get(level);
        int avenging = 0;
        for (Guard guard : followers) {
            SafeTick.run(guard, () -> GuardModes.postHere(guard));
            if (killer != null && CommandTargeting.wouldAccept(guard, killer)) {
                directives.setTarget(guard.getUUID(), killer.getUUID(), killer.getName().getString());
                avenging++;
            }
        }
        commander.sendSystemMessage(killer != null && avenging > 0
                ? Component.translatable("gui.rallyguard.command.commander_fell_avenge", followers.size(), killer.getName().getString())
                : Component.translatable("gui.rallyguard.command.commander_fell", followers.size()));
    }
}
