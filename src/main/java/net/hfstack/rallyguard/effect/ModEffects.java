package net.hfstack.rallyguard.effect;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Ported from the Forge 1.20.1 {@code ModEffects} (which used
 * {@code net.minecraftforge.registries.DeferredRegister}/{@code RegistryObject}).
 * NeoForge equivalents, verified against Guard Villagers' own real 1.21.1 source
 * (GuardItems.java, which registers against {@code net.minecraft.core.registries.Registries.ITEM}
 * the same way): {@code net.neoforged.neoforge.registries.DeferredRegister}/{@code DeferredHolder},
 * keyed by the vanilla {@code ResourceKey<Registry<MobEffect>>} constant
 * {@code Registries.MOB_EFFECT} (confirmed present and unchanged in Minecraft 1.21.1).
 */
public final class ModEffects {
    public static final DeferredRegister<MobEffect> REGISTER =
            DeferredRegister.create(Registries.MOB_EFFECT, RallyOfTheGuard.MOD_ID);

    public static final DeferredHolder<MobEffect, RallyCommanderEffect> RALLY_COMMANDER =
            REGISTER.register("rally_commander", RallyCommanderEffect::new);

    private ModEffects() {
    }
}
