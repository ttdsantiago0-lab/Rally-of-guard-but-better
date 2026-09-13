package net.hfstack.rallyguard.util;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tallestegg.guardvillagers.configuration.GuardConfig;

/**
 * Phase 2I (minimal scope): disables Guard Villagers' "only follow the player if they
 * have Hero of the Village" requirement, the same behavior the Forge 1.20.1 donor's
 * {@code GuardVillagersConfigPatcher} provided (config-file surgery + runtime reflection
 * forcing, since Forge load order didn't guarantee direct compile-time access to Guard
 * Villagers' own classes there).
 * <p>
 * This is a deliberate, simpler departure from the donor: this project already depends on
 * {@code tallestegg.guardvillagers} at compile time (the same jar backs {@code Guard},
 * {@code GuardOwnership}, and every network handler since Phase 2C), so
 * {@code GuardConfig.COMMON.followHero} is a plain public
 * {@code net.neoforged.neoforge.common.ModConfigSpec.BooleanValue} field, reachable
 * directly with no reflection needed - confirmed by reading Guard Villagers' own
 * {@code configuration/GuardConfig.java} source (line ~53: {@code public final
 * ModConfigSpec.BooleanValue followHero;}, default {@code true}, defined at line 140).
 * <p>
 * Why this matters beyond "guards follow without a status effect": Guard Villagers'
 * {@code Guard#getOwner()} itself returns {@code null} whenever {@code followHero} is
 * `true` and the player lacks the vanilla Hero of the Village effect (verified by reading
 * that getter directly) - and {@code getOwner()} gates not just {@code FollowHeroGoal}
 * (continuous following) but also {@code HeroHurtByTargetGoal}/{@code HeroHurtTargetGoal}
 * (a guard defending its owner / joining a fight the owner started). Without this patch,
 * neither the Scroll of Rallying's "follow" (Phase 2G) nor combat backup would ever
 * actually engage, regardless of how correctly those features set
 * {@code Guard#setFollowing(true)} - this was found and documented as a real dependency
 * during the Phase 2E/2F Q&amp;A round, PORTING_PLAN.md section 17.2 point 4.
 * <p>
 * Called from {@code FMLCommonSetupEvent} (fires on both logical sides, after every mod
 * is constructed and every mod's own {@code ModConfigSpec} has already been loaded from
 * disk) - deliberately not the mod constructor itself, since {@code ModConfigSpec}
 * values are not safely readable/writable until the corresponding config file has been
 * loaded and attached to the spec.
 */
public final class GuardVillagersConfigPatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(RallyOfTheGuard.MOD_ID);

    private GuardVillagersConfigPatcher() {
    }

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                if (!GuardConfig.COMMON.followHero.get()) {
                    LOGGER.info("[{}] Guard Villagers followHero already false - no patch needed.", RallyOfTheGuard.MOD_ID);
                    return;
                }
                GuardConfig.COMMON.followHero.set(Boolean.FALSE);
                LOGGER.info("[{}] Guard Villagers followHero forced to false: guards no longer "
                        + "require the Hero of the Village effect to follow/back up their owner.", RallyOfTheGuard.MOD_ID);
            } catch (Exception e) {
                // Never let a cross-mod config tweak crash startup - Guard Villagers not
                // being present/loaded yet, or a future GuardConfig shape change, should
                // degrade to "the old Hero-of-the-Village requirement still applies", not
                // a hard crash.
                LOGGER.error("[{}] Failed to patch Guard Villagers' followHero config.", RallyOfTheGuard.MOD_ID, e);
            }
        });
    }
}
