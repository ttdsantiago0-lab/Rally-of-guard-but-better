package net.hfstack.rallyguard.mixin.sim;

import net.hfstack.rallyguard.debug.sim.SimAiLod;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * F127 (O9, measurement, SIMULATION ONLY): how much of a 400 v 400 tick is mob AI. Skips {@code Mob#serverAiStep}
 * (sensing, goal/target selectors, navigation, move/look/jump controls) for the sim's own mobs on ticks that are not
 * their AI turn ({@link SimAiLod}). No public API can skip only the AI: {@code setNoAi} also stops
 * {@code LivingEntity#travel} (the body), and cancelling {@code EntityTickEvent.Pre} skips the whole tick.
 * Outside a sim run with a {@code lod:ai} tag {@link SimAiLod#active} is false and this costs one field read.
 * Not a game mechanism: if the lever pays, the game version is designed separately (F127 verdict).
 * Verified against Mojang's official 1.21.1 mappings: {@code void serverAiStep()} on {@code Mob}; Guard Villagers'
 * {@code Guard} (1.21.1 source) does not override it.
 */
@Mixin(Mob.class)
public abstract class MobAiLodMixin {

    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void rallyguard$simAiLod(CallbackInfo ci) {
        if (SimAiLod.active && SimAiLod.skip((Mob) (Object) this)) ci.cancel();
    }
}
