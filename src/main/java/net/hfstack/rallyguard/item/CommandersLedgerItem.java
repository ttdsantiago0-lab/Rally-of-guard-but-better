package net.hfstack.rallyguard.item;

import net.hfstack.rallyguard.client.ClientHooks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Commander's Ledger - opens the Commander's Ledger command panel when used.
 * <p>
 * Phase 2E: ports the Forge 1.20.1 {@code use(...)} behavior - request the guard list
 * from the server (which responds with {@code GuardListS2CPayload} and opens
 * {@link net.hfstack.rallyguard.screen.GuardCommandScreen} - see
 * {@code GuardCommandNetworking#handleGuardList}). Reaches the client-only send through
 * {@link ClientHooks#requestOpenGuardPanel()} rather than calling networking code
 * directly from this common-side class - see that class's javadoc for why a direct
 * guarded call (no reflection, unlike the Fabric donor) is safe here.
 */
public class CommandersLedgerItem extends Item {
    public CommandersLedgerItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (level.isClientSide()) {
            ClientHooks.requestOpenGuardPanel();
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tips, TooltipFlag flag) {
        tips.add(Component.translatable("item.rallyguard.commanders_ledger.tooltip"));
        super.appendHoverText(stack, context, tips, flag);
    }
}
