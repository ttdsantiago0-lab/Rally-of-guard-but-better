package net.hfstack.rallyguard;

import com.mojang.logging.LogUtils;
import net.hfstack.rallyguard.combat.RangedFire;
import net.hfstack.rallyguard.compat.McaMobCompat;
import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.effect.ModEffects;
import net.hfstack.rallyguard.event.CommanderDeathHandler;
import net.hfstack.rallyguard.event.GuardFallDamageHandler;
import net.hfstack.rallyguard.event.GuardRosterCleanupHandler;
import net.hfstack.rallyguard.event.GuardMountDisciplineHandler;
import net.hfstack.rallyguard.event.GuardStanceHandler;
import net.hfstack.rallyguard.event.HiredGuardsNeutralityHandler;
import net.hfstack.rallyguard.event.InteractGuardHandler;
import net.hfstack.rallyguard.event.RallyFriendlyFireHandler;
import net.hfstack.rallyguard.item.ModItems;
import net.hfstack.rallyguard.network.NetworkBootstrap;
import net.hfstack.rallyguard.screen.ModMenus;
import net.hfstack.rallyguard.util.GuardVillagersConfigPatcher;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
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
 * Phase 2C added: the hire-menu registry ({@link ModMenus#register}) and the guard
 * interaction handler ({@link InteractGuardHandler}), registered on
 * {@code net.neoforged.neoforge.common.NeoForge.EVENT_BUS} - the NeoForge equivalent of
 * Forge's {@code MinecraftForge.EVENT_BUS} (same pattern Guard Villagers' own
 * {@code GuardVillagers.java} uses for its own game-event handlers).
 * <p>
 * Phase 2D adds: {@link NetworkBootstrap#register}, subscribed on the mod event bus
 * exactly like Guard Villagers' own {@code modEventBus.addListener(this::register)} -
 * registers the three Commander's Ledger network payloads
 * (open-guard-command/guard-action/guard-list). No UI consumes them yet (that is Phase
 * 2E's Commander's Ledger GUI); this stage only wires the network layer itself, per
 * PORTING_PLAN.md section 10.
 * <p>
 * Phase 2E-2F-2G-2I add: the Commander's Ledger item/GUI, patrol (already driven by
 * Phase 2D's typed API against Guard Villagers' own WalkBackToCheckPointGoal), the
 * Scroll of Rallying, and {@link GuardVillagersConfigPatcher#onCommonSetup} (forces
 * Guard Villagers' {@code followHero} config off - a real prerequisite for both the
 * Scroll of Rallying's follow behavior and guard combat backup, per PORTING_PLAN.md
 * section 17.2 point 4).
 * <p>
 * Phase 2H adds: {@link RallyFriendlyFireHandler} (owner can't accidentally hurt their
 * own following/rallied guard) and {@link HiredGuardsNeutralityHandler} (a player's own
 * hired guards don't turn on them after they hit a guard that isn't theirs) - both
 * registered on {@code NeoForge.EVENT_BUS} the same way as {@link InteractGuardHandler}.
 * This is the last stage of the original porting plan; everything past this point is
 * user-directed expansion (see PORTING_PLAN.md and the published roadmap).
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
        modEventBus.addListener(NetworkBootstrap::register);
        modEventBus.addListener(GuardVillagersConfigPatcher::onCommonSetup);

        NeoForge.EVENT_BUS.register(new InteractGuardHandler());
        NeoForge.EVENT_BUS.register(new RallyFriendlyFireHandler());
        NeoForge.EVENT_BUS.register(new HiredGuardsNeutralityHandler());
        NeoForge.EVENT_BUS.register(new GuardFallDamageHandler());
        NeoForge.EVENT_BUS.register(new GuardRosterCleanupHandler());
        NeoForge.EVENT_BUS.register(new GuardStanceHandler());
        NeoForge.EVENT_BUS.register(new GuardMountDisciplineHandler());
        NeoForge.EVENT_BUS.register(new CommanderDeathHandler());
        NeoForge.EVENT_BUS.register(new RangedFire());

        // E1: Rally's own settings (config/rallyguard-common.toml).
        container.registerConfig(ModConfig.Type.COMMON, RallyConfig.SPEC);

        // Phase 3H: optional integrations - each is null (and nothing registers) when the
        // other mod isn't installed.
        ScorchedGunsCompat scorchedGuns = ScorchedGunsCompat.createIfLoaded();
        if (scorchedGuns != null) NeoForge.EVENT_BUS.register(scorchedGuns);
        McaMobCompat.registerIfLoaded();

        LOGGER.info("[{}] Loaded (NeoForge 1.21.1, MC 1.21.1): items, mob effects, guard "
                + "hiring/ownership, Commander's Ledger (networking + GUI + patrol), "
                + "Scroll of Rallying, the Guard Villagers followHero patch, and "
                + "friendly-fire/neutrality (Phase 2H) registered. Original porting plan "
                + "complete.", MOD_ID);
    }
}
