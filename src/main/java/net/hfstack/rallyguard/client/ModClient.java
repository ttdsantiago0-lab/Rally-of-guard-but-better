package net.hfstack.rallyguard.client;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.hfstack.rallyguard.screen.HireGuardScreen;
import net.hfstack.rallyguard.screen.PackScreen;
import net.hfstack.rallyguard.screen.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Client-only setup: registers the hiring screen for {@link ModMenus#HIRE_MENU}. Required
 * to complete the Phase 2C hiring flow end-to-end - without this, the menu opens but the
 * client has no {@code Screen} to show for it.
 * <p>
 * Not present in Forge's {@code client/ModClient.java} equivalent in this exact minimal
 * form: the Forge version also called {@code GuardVillagersConfigPatcher} (config-file
 * surgery + a client-tick handler forcing Guard Villagers' {@code followHero} setting) -
 * that is Phase 2I territory per PORTING_PLAN.md section 4.1/section 5 (GV's own
 * {@code GuardConfig.COMMON.followHero} is planned to be used directly there instead) and
 * is intentionally not ported here.
 * <p>
 * Forge -&gt; NeoForge, corrected after a real build failure: Forge's
 * {@code MenuScreens.register(MenuType, ScreenConstructor)} call (used directly inside
 * {@code FMLClientSetupEvent}) does NOT carry over - the actual 1.21.1 compiler confirmed
 * that overload is now <b>private</b> ("has private access in MenuScreens"). The real
 * replacement, confirmed against NeoForge's own source
 * ({@code net.neoforged.neoforge.client.event.RegisterMenuScreensEvent}, which wraps that
 * same private method and exposes a public {@code register(MenuType&lt;? extends M&gt;,
 * MenuScreens.ScreenConstructor&lt;M,U&gt;)}) and the official NeoForged 1.21.1 "Screens"
 * documentation, is a dedicated mod-bus event fired specifically for this purpose - not
 * something to call inside {@code FMLClientSetupEvent} at all.
 * <p>
 * {@code @Mod.EventBusSubscriber(modid, bus = MOD, value = Dist.CLIENT)} becomes
 * {@code @net.neoforged.fml.common.EventBusSubscriber(modid, value = Dist.CLIENT)} -
 * verified against NeoForge's own FancyModLoader source, confirmed present at this exact
 * package.
 */
@EventBusSubscriber(modid = RallyOfTheGuard.MOD_ID, value = Dist.CLIENT)
public final class ModClient {
    private ModClient() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.HIRE_MENU.get(), HireGuardScreen::new);
        event.register(ModMenus.PACK_MENU.get(), PackScreen::new);
    }
}
