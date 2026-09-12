package net.hfstack.rallyguard;

import com.mojang.logging.LogUtils;
import net.hfstack.rallyguard.effect.ModEffects;
import net.hfstack.rallyguard.event.InteractGuardHandler;
import net.hfstack.rallyguard.item.ModItems;
import net.hfstack.rallyguard.screen.ModMenus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/**
 * Constructor shape (ModContainer, IEventBus) and the {@code @Mod(MOD_ID)} annotation
 * package are the modern NeoForge 1.21.1 entry point idiom, verified against Guard
 * Villagers' own real NeoForge 1.21.1 source (tallestegg.guardvillagers.GuardVillagers)
 * during Phase 0 - not guessed or carried over from the Forge 1.20.1
 * (FMLJavaModLoadingContext.get()) style.
 * <p>
 * Phase 2C adds: the hire-menu registry ({@link ModMenus#register}) and the guard
 * interaction handler ({@link InteractGuardHandler}), registered on
 * {@code net.neoforged.neoforge.common.NeoForge.EVENT_BUS} - the NeoForge equivalent of
 * Forge's {@code MinecraftForge.EVENT_BUS} (same pattern Guard Villagers' own
 * {@code GuardVillagers.java} uses for its own game-event handlers). Networking, the
 * Commander's Ledger, patrol, rally logic, friendly-fire/neutrality handlers, and the
 * Guard Villagers config patcher are still later stages (Phase 2D-2I per
 * PORTING_PLAN.md section 10) and are intentionally not added yet.
 */
@Mod(RallyOfTheGuard.MOD_ID)
public final class RallyOfTheGuard {
    public static final String MOD_ID = "rallyguard";
    private static final Logger LOGGER = LogUtils.getLogger();

    public RallyOfTheGuard(ModContainer container, IEventBus modEventBus) {
        ModItems.REGISTER.register(modEventBus);
        ModEffects.REGISTER.register(modEventBus);
        ModMenus.register(modEventBus);
        modEventBus.addListener(ModItems::registerCreativeTabContents);

        NeoForge.EVENT_BUS.register(new InteractGuardHandler());

        LOGGER.info("[{}] Phase 2C loaded (NeoForge 1.21.1, MC 1.21.1): items, mob "
                + "effects, and guard hiring/ownership registered. No networking/Ledger/"
                + "patrol/rally/friendly-fire behavior yet.", MOD_ID);
    }
}
