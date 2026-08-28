package net.hfstack.rallyguard.item;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Ported from the Forge 1.20.1 {@code ModItems} (which used
 * {@code net.minecraftforge.registries.DeferredRegister}/{@code RegistryObject} and
 * {@code net.minecraftforge.event.BuildCreativeModeTabContentsEvent}).
 * NeoForge equivalents verified against Guard Villagers' own real 1.21.1 source
 * (GuardItems.java / GuardVillagers.java):
 * <ul>
 *   <li>{@code net.neoforged.neoforge.registries.DeferredRegister}/{@code DeferredHolder},
 *       keyed by {@code Registries.ITEM} (same pattern GuardItems.java uses).</li>
 *   <li>{@code net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent} - package
 *       moved from {@code net.minecraftforge.event} to {@code net.neoforged.neoforge.event}.</li>
 * </ul>
 * Registry names ("scroll_of_rallying", "commanders_ledger") and the {@code stacksTo(1)}
 * item property are preserved exactly from the Forge port.
 */
public final class ModItems {
    public static final DeferredRegister<Item> REGISTER =
            DeferredRegister.create(Registries.ITEM, RallyOfTheGuard.MOD_ID);

    public static final DeferredHolder<Item, ScrollOfRallyingItem> SCROLL_OF_RALLYING =
            REGISTER.register("scroll_of_rallying",
                    () -> new ScrollOfRallyingItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, CommandersLedgerItem> COMMANDERS_LEDGER =
            REGISTER.register("commanders_ledger",
                    () -> new CommandersLedgerItem(new Item.Properties().stacksTo(1)));

    private ModItems() {
    }

    /**
     * Adds both items to the vanilla "Tools and Utilities" creative tab -
     * same tab the Forge 1.20.1 port used.
     */
    public static void registerCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(SCROLL_OF_RALLYING.get());
            event.accept(COMMANDERS_LEDGER.get());
        }
    }
}
