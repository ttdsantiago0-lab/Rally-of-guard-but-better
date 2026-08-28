package net.hfstack.rallyguard.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Commander's Ledger - opens the guard command panel when used.
 * <p>
 * Phase 2B scope only: registration and static tooltip text. The Forge 1.20.1
 * {@code use(...)} override (which sends a C2S packet to open the command GUI)
 * depends on networking (Phase 2D) and the command screen (Phase 2E) and is
 * intentionally NOT ported yet - this item does nothing on use for now, per
 * PORTING_PLAN.md section 10, Stage B ("no behavior yet").
 */
public class CommandersLedgerItem extends Item {
    public CommandersLedgerItem(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tips, TooltipFlag flag) {
        tips.add(Component.translatable("item.rallyguard.commanders_ledger.tooltip"));
        super.appendHoverText(stack, context, tips, flag);
    }
}
