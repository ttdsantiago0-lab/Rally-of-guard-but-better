package net.hfstack.rallyguard.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * "Rally Commander" effect, applied to a player while a Scroll of Rallying is active.
 * Ported from the Forge 1.20.1 implementation verbatim: the effect itself carries no
 * behavior of its own (no tick/duration overrides) - it exists purely as a marker
 * that other systems (the Scroll of Rallying, friendly-fire handling) check for.
 * Constructor signature and arguments unchanged in Minecraft 1.21.1
 * (verified: {@code protected MobEffect(MobEffectCategory, int)} still exists).
 */
public class RallyCommanderEffect extends MobEffect {
    public RallyCommanderEffect() {
        super(MobEffectCategory.NEUTRAL, 0xFFD700); // gold
    }
}
