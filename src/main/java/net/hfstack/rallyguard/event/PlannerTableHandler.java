package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.force.PlannerService;
import net.hfstack.rallyguard.item.ScrollOfRallyingItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * E3A A4: clicking a laid Battle Planner sheet (an item frame on a planner table, see
 * {@link ScrollOfRallyingItem#useOn}) opens the planner instead of turning the item in the
 * frame. The table must still be inside one of the commander's HQ zones.
 */
public final class PlannerTableHandler {

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof ItemFrame frame) || !ScrollOfRallyingItem.isPlannerFrame(frame)) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        if (event.getEntity() instanceof ServerPlayer player) {
            if (PlannerService.inHqZone(player, frame.blockPosition())) {
                PlannerService.open(player);
            } else {
                player.displayClientMessage(net.minecraft.network.chat.Component.translatable("gui.rallyguard.planner.need_hq",
                        net.hfstack.rallyguard.config.RallyConfig.i(net.hfstack.rallyguard.config.RallyConfig.HQ_RADIUS, 16)), true);
            }
        }
    }
}
