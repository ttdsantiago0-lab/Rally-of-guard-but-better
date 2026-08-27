package net.hfstack.rallyguard;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * Phase 1 build skeleton only. No gameplay is registered here yet - see PORTING_PLAN.md
 * for the staged MVP implementation order (Phase 2, stages A-I).
 *
 * Constructor shape (ModContainer, IEventBus) and the {@code @Mod(MOD_ID)} annotation
 * package are the modern NeoForge 1.21.1 entry point idiom, verified against Guard
 * Villagers' own real NeoForge 1.21.1 source (tallestegg.guardvillagers.GuardVillagers)
 * during Phase 0 - not guessed or carried over from the Forge 1.20.1
 * (FMLJavaModLoadingContext.get()) style.
 */
@Mod(RallyOfTheGuard.MOD_ID)
public final class RallyOfTheGuard {
    public static final String MOD_ID = "rallyguard";
    private static final Logger LOGGER = LogUtils.getLogger();

    public RallyOfTheGuard(ModContainer container, IEventBus modEventBus) {
        LOGGER.info("[{}] Phase 1 build skeleton loaded (NeoForge 1.21.1, MC 1.21.1). "
                + "No gameplay registered yet.", MOD_ID);
    }
}
