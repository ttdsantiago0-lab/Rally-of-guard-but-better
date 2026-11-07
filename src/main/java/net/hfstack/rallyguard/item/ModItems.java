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

    // 2026-11-06: the Roman shield in five tiers (docs/ESCOLTA_Y_TESTUDO.md §5). Uses: a vanilla shield has 336.
    public static final DeferredHolder<Item, ScutumItem> SCUTUM_WOOD = REGISTER.register("scutum_wood",
            () -> new ScutumItem(new Item.Properties().durability(240), s -> s.is(net.minecraft.tags.ItemTags.PLANKS)));
    /** The iron scutum (the first one, 2026-11-06 - its id stays "scutum"). */
    public static final DeferredHolder<Item, ScutumItem> SCUTUM = REGISTER.register("scutum",
            () -> new ScutumItem(new Item.Properties().durability(672), s -> s.is(net.neoforged.neoforge.common.Tags.Items.INGOTS_IRON)));
    public static final DeferredHolder<Item, ScutumItem> SCUTUM_DIAMOND = REGISTER.register("scutum_diamond",
            () -> new ScutumItem(new Item.Properties().durability(1300), s -> s.is(net.neoforged.neoforge.common.Tags.Items.GEMS_DIAMOND)));
    public static final DeferredHolder<Item, ScutumItem> SCUTUM_NETHERITE = REGISTER.register("scutum_netherite",
            () -> new ScutumItem(new Item.Properties().durability(2000).fireResistant(),
                    s -> s.is(net.neoforged.neoforge.common.Tags.Items.INGOTS_NETHERITE)));
    /** Dragonsteel (Ice and Fire's dragon forge): mended with any dragonsteel ingot; only craftable with Ice and Fire. */
    public static final DeferredHolder<Item, ScutumItem> SCUTUM_DRAGONSTEEL = REGISTER.register("scutum_dragonsteel",
            () -> new ScutumItem(new Item.Properties().durability(3000).fireResistant(), s -> {
                net.minecraft.resources.ResourceLocation id = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(s.getItem());
                return "iceandfire".equals(id.getNamespace()) && id.getPath().startsWith("dragonsteel_") && id.getPath().endsWith("_ingot");
            }));

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
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(SCUTUM_WOOD.get());
            event.accept(SCUTUM.get());
            event.accept(SCUTUM_DIAMOND.get());
            event.accept(SCUTUM_NETHERITE.get());
            event.accept(SCUTUM_DRAGONSTEEL.get());
        }
    }
}
