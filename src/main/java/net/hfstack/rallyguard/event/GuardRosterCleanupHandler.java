package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.combat.GuardPack;
import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.GuardRoster;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.contract.StanceRegistry;
import net.hfstack.rallyguard.order.CommanderSelection;
import net.hfstack.rallyguard.order.GuardModes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.UUID;

/**
 * Phase 3B: drops a dead guard's cached {@link GuardRoster} entry (and its
 * {@link SquadRegistry} membership, if any) so the Commander's Ledger stops listing it
 * forever.
 * <p>
 * Without this, {@link GuardRoster} - deliberately populated only opportunistically
 * (hire time, or whenever {@code GuardCommandNetworking#sendGuardList} happens to find
 * the guard loaded - see that class's javadoc) and never swept by any periodic
 * background scan - would keep a dead guard's last-known snapshot around permanently:
 * there is no other event that would ever remove it. That would make the Ledger's
 * guard count slowly drift wrong as guards die, which defeats the point of a roster
 * that is supposed to be a trustworthy "what do I own" view. This handler is the one
 * and only removal path.
 * <p>
 * Folding in the {@link SquadRegistry#unassign} call here too fixes a related,
 * pre-existing gap noted since Phase 3A: squads never dropped dead members either,
 * for the exact same reason (no death hook existed until now). Both caches key on the
 * same guard UUID and both need the identical "this guard is gone for good" signal, so
 * one handler serving both avoids a second near-duplicate class.
 * <p>
 * Phase 3D: same reasoning again for {@link FormationRegistry} - a dead guard's
 * remembered Muster slot (anchor/facing/shape/index/count, used by Reform) would
 * otherwise sit in the registry forever with no other removal path. All four
 * per-guard caches are cleaned up from this single handler.
 * <p>
 * {@link LivingDeathEvent} confirmed real on this NeoForge/MC 1.21.1 version via
 * nekoyue's ForgeJavaDocs-NG mirror pinned to {@code 1.21.x-neoforge} (package
 * {@code net.neoforged.neoforge.event.entity.living}, constructor
 * {@code LivingDeathEvent(LivingEntity entity, DamageSource source)}, inherits
 * {@code getEntity()}) before first use here - same two-source discipline as every
 * other first-time API this project has touched, though here a second source wasn't
 * needed since the javadoc mirror was unambiguous and this event class predates this
 * project's own {@code /tmp/neoforge-src} version-mismatch problems.
 */
public final class GuardRosterCleanupHandler {

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) return;
        ServerLevel level = (ServerLevel) victim.level();

        // Phase 3E: a dead mob can't stay anyone's designated target. Players are left
        // alone on purpose - they respawn, and a squad told to fight a player keeps
        // that order until its commander changes it.
        if (!(victim instanceof Player)) {
            DirectiveRegistry.get(level).clearTargetEverywhere(victim.getUUID());
        }

        if (!(victim instanceof Guard guard)) return;
        if (!GuardOwnership.hasOwner(guard)) return;
        // A totem (or another mod) that cancels the death means the guard lives on.
        if (event.isCanceled()) return;

        UUID ownerId = GuardOwnership.getOwner(guard);
        String squadPath = SquadRegistry.get(level).pathOf(guard.getUUID()).orElse("");
        GuardPack.dropAll(level, guard.getUUID(), guard.position()); // E1-A: the pack is loot
        forget(level, guard.getUUID());
        reportLosses(level, ownerId, squadPath, guard.getName().getString(), false);
    }

    /**
     * Phase 3H: a guard can also leave for good WITHOUT a death event - Guard Villagers
     * turns a guard killed by a zombie into a zombie villager and returns from
     * {@code Guard#die} before NeoForge's death hook runs (read from its real source),
     * and other mods/commands can discard entities outright. Each of those left a
     * permanent "away" row in the Ledger (the user's "cementerio de nombres"). Any owned
     * guard removed with a destroying reason (KILLED/DISCARDED - not unloaded, not a
     * dimension change) is cleaned up here too; if nothing had cleaned it up yet, the
     * commander is told it was lost.
     */
    @SubscribeEvent
    public void onLeaveLevel(EntityLeaveLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!(event.getEntity() instanceof Guard guard) || !GuardOwnership.hasOwner(guard)) return;
        Entity.RemovalReason reason = guard.getRemovalReason();
        if (reason == null || !reason.shouldDestroy()) return;
        DirectiveRegistry.get(level).clearTargetEverywhere(guard.getUUID()); // nobody defends/hunts it now
        if (!GuardRoster.get(level).contains(guard.getUUID())
                && SquadRegistry.get(level).pathOf(guard.getUUID()).isEmpty()) return; // already handled

        UUID ownerId = GuardOwnership.getOwner(guard);
        String squadPath = SquadRegistry.get(level).pathOf(guard.getUUID()).orElse("");
        GuardPack.dropAll(level, guard.getUUID(), guard.position()); // turned zombie / removed: pack drops
        forget(level, guard.getUUID());
        reportLosses(level, ownerId, squadPath, guard.getName().getString(), true);
    }

    /** Every per-guard record Rally keeps, dropped in one place. */
    private static void forget(ServerLevel level, UUID guardId) {
        GuardRoster.get(level).remove(guardId);
        SquadRegistry.get(level).unassign(guardId);
        // Phase 3C/3D/3E: stance, remembered formation slot and directives would
        // otherwise sit in their registries forever with no other removal path.
        StanceRegistry.get(level).remove(guardId);
        FormationRegistry.get(level).remove(guardId);
        DirectiveRegistry.get(level).remove(guardId);
        GuardModes.forget(guardId);
        CommanderSelection.forgetGuard(guardId);
        net.hfstack.rallyguard.combat.Ammo.forget(guardId);
        net.hfstack.rallyguard.order.HqLogistics.forget(guardId);
        net.hfstack.rallyguard.command.SquadDirectives.forget(guardId);
        net.hfstack.rallyguard.command.SlotQueue.cancel(guardId);
        net.hfstack.rallyguard.contract.VeterancyRegistry.get(level).remove(guardId);
        net.hfstack.rallyguard.veterancy.VeterancyEvents.forget(guardId);
        net.hfstack.rallyguard.tactics.Tactics.forget(guardId);
    }

    /**
     * Phase 3H (user review: "ver qué guardias mueren y cuántos quedan"). Guard Villagers
     * already tells the owner HOW the guard died (its own {@code Guard#die} sends the
     * vanilla death message), so Rally adds only what a commander needs next: how many
     * are left in that guard's squad/unit and in the whole army. One grey line, right
     * under Guard Villagers' own.
     */
    private static void reportLosses(ServerLevel level, UUID ownerId, String squadPath, String name, boolean lost) {
        if (ownerId == null) return;
        ServerPlayer owner = level.getServer().getPlayerList().getPlayer(ownerId);
        if (owner == null) return;
        if (lost) {
            // No death message came from Guard Villagers in this case - say what happened.
            owner.sendSystemMessage(Component.translatable("gui.rallyguard.command.guard_lost", name)
                    .withStyle(ChatFormatting.GRAY));
        }
        // Phase 3H.1: the line names the guard itself too, so a renamed guard is always
        // recognizable in the chat, whatever Guard Villagers' own message says.
        int army = GuardRoster.get(level).countOwnedBy(ownerId);
        Component line;
        if (squadPath.isEmpty()) {
            line = Component.translatable("gui.rallyguard.command.loss_army", name, army);
        } else {
            int inGroup = SquadRegistry.get(level).countMembers(ownerId, squadPath);
            line = Component.translatable("gui.rallyguard.command.loss_squad", name,
                    squadPath.replace(SquadRegistry.PATH_SEPARATOR, " › "), inGroup, army);
        }
        owner.sendSystemMessage(line.copy().withStyle(ChatFormatting.GRAY));
    }
}
