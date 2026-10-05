package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.order.GuardModes;
import net.hfstack.rallyguard.screen.HireGuardMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * If a player right-clicks (main hand) an unowned Guard, opens the hiring screen instead
 * of letting Guard Villagers' own {@code Guard.mobInteract} run - GV's own interaction only
 * opens its inventory GUI for a player who already has Hero of the Village/reputation/is
 * already the owner (verified by reading {@code Guard.mobInteract} in the real 2.4.11 jar
 * source tree), so an unowned guard interacted with by an ordinary player would otherwise
 * just silently consume the click. Ported from the Forge 1.20.1
 * {@code PlayerInteractEvent.EntityInteractSpecific} handler - same event, same guard
 * detection order (this handler runs before {@code Guard.mobInteract} because
 * {@code EntityInteractSpecific} fires earlier in the vanilla interaction pipeline than
 * {@code mobInteract}), same "already owned -> let Guard Villagers handle it" bail-out.
 * <p>
 * Forge -&gt; NeoForge: {@code net.minecraftforge.event.entity.player.PlayerInteractEvent}
 * moved package to {@code net.neoforged.neoforge.event.entity.player.PlayerInteractEvent}
 * (method names on the event unchanged - verified against the real NeoForge 1.21.1
 * javadoc). Forge's {@code NetworkHooks.openScreen(player, provider, extraDataWriter)} is
 * replaced by NeoForge's {@code ServerPlayer.openMenu(MenuProvider, Consumer&lt;RegistryFriendlyByteBuf&gt;)}
 * (verified against the official NeoForged 1.21.1 "Menus" documentation and the real
 * {@code IContainerFactory}/{@code IPlayerExtension} signatures).
 */
public final class InteractGuardHandler {

    @SubscribeEvent
    public void onInteractEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        var player = event.getEntity();
        var target = event.getTarget();

        if (!GuardOwnership.isGuard(target)) return;
        if (GuardOwnership.hasOwner(target)) {
            // E1: the owner's own interactions (Guard Villagers' screen, MCA's menu) may
            // change the guard's movement flags - GuardModes adopts those for ~60 s.
            if (target instanceof Guard guard && GuardOwnership.isOwnedBy(guard, player.getUUID())) {
                GuardModes.noteOwnerInteraction(guard);
            }
            return; // let Guard Villagers handle it
        }

        if (!(player instanceof ServerPlayer sp)) return;
        openHire(sp, target);

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    /** Opens Rally's hiring screen for an unowned guard (also used by the MCA bridge). */
    public static void openHire(ServerPlayer sp, Entity target) {
        int guardId = target.getId();
        MenuProvider provider = new SimpleMenuProvider(
                (syncId, inv, p) -> new HireGuardMenu(syncId, inv, guardId),
                Component.translatable("gui.rallyguard.hire.title")
        );
        sp.openMenu(provider, buf -> buf.writeVarInt(guardId));
    }
}
