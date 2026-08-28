package net.hfstack.rallyguard.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Scroll of Rallying - teleports the player's guards to them when used while
 * a "rally" is active, ended by using it again.
 * <p>
 * Phase 2B scope only: registration and static tooltip text. The Forge 1.20.1
 * {@code use(...)} override (rally/dismiss logic, the RALLY_COMMANDER effect,
 * ownership checks) and the "active"/glint indicator (a 1.21.1 data component in
 * this port, per PORTING_PLAN.md section 10, Stage G) depend on Phase 2C
 * (ownership), Phase 2G (rally logic) and are intentionally NOT ported yet -
 * this item does nothing on use for now, per PORTING_PLAN.md section 10, Stage B
 * ("no behavior yet").
 */
public class ScrollOfRallyingItem extends Item {
    public ScrollOfRallyingItem(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tips, TooltipFlag flag) {
        tips.add(Component.translatable("tooltip.rallyguard.scroll_of_rallying.tooltip_desc"));
        super.appendHoverText(stack, context, tips, flag);
    }
}
