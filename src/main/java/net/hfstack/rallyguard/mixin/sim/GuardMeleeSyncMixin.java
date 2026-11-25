package net.hfstack.rallyguard.mixin.sim;

import net.hfstack.rallyguard.combat.MeleeWindows;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * F193 (O7, SIMULATION ONLY, tag {@code melee:sync}): an owned guard holds its melee blow while the target is inside
 * its half-second invulnerability window ({@link MeleeWindows}). Why a mixin: {@code GuardMeleeGoal#checkAndPerformAttack}
 * spends the 20-tick cooldown as soon as {@code canPerformAttack} says yes, and the damage events arrive only after that
 * (the swing is already lost); no public API can hold the blow. Answering "not yet" from {@code canPerformAttack} keeps
 * the cooldown, so the blow lands on the first free tick. Only ever ADDS a "no"; {@code KickGoal} is untouched.
 * Verified against Guard Villagers' 1.21.1 source: {@code public static class GuardMeleeGoal extends MeleeAttackGoal},
 * field {@code public final Guard guard}, {@code protected boolean canPerformAttack(LivingEntity mob)}.
 */
// remap = false: as GuardRaiseShieldMixin (Guard Villagers' own class; the name is the Mojang one at runtime)
@Mixin(targets = "tallestegg.guardvillagers.common.entities.Guard$GuardMeleeGoal", remap = false)
public abstract class GuardMeleeSyncMixin {

    @Shadow(remap = false)
    @Final
    public Guard guard;

    @Inject(method = "canPerformAttack", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void rallyguard$holdInWindow(LivingEntity enemy, CallbackInfoReturnable<Boolean> cir) {
        if (MeleeWindows.simSync && MeleeWindows.hold(guard, enemy)) cir.setReturnValue(false);
    }
}
