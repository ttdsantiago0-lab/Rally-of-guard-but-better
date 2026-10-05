package net.hfstack.rallyguard.screen;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Ported from the Forge 1.20.1 {@code ModMenus} (which used
 * {@code net.minecraftforge.registries.DeferredRegister}/{@code RegistryObject} and
 * {@code net.minecraftforge.common.extensions.IForgeMenuType}).
 * <p>
 * NeoForge equivalent verified against the official NeoForged 1.21.1 "Menus"
 * documentation: {@code net.neoforged.neoforge.common.extensions.IMenuTypeExtension.create(IContainerFactory)}
 * replaces {@code IForgeMenuType.create(...)}, keyed by the vanilla
 * {@code ResourceKey<Registry<MenuType<?>>>} constant {@code Registries.MENU} (confirmed
 * present and unchanged in Minecraft 1.21.1) instead of {@code ForgeRegistries.MENU_TYPES}.
 * Registry name ("hire_menu") preserved exactly from the Forge port.
 */
public final class ModMenus {
    private ModMenus() {
    }

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, RallyOfTheGuard.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<HireGuardMenu>> HIRE_MENU =
            MENUS.register("hire_menu",
                    () -> IMenuTypeExtension.create((windowId, inv, buf) -> new HireGuardMenu(windowId, inv, buf)));

    /** E1-A: a guard's war pack. */
    public static final DeferredHolder<MenuType<?>, MenuType<PackMenu>> PACK_MENU =
            MENUS.register("pack_menu",
                    () -> IMenuTypeExtension.create((windowId, inv, buf) -> new PackMenu(windowId, inv, buf)));

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
