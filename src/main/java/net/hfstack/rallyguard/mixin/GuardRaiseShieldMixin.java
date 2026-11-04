package net.hfstack.rallyguard.mixin;

import net.hfstack.rallyguard.combat.FieldManual;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * Field manual, shield up against shooters (David 2026-11-04): Guard Villagers' {@code RaiseShieldGoal#raiseShield}
 * decides each tick whether to block; its "always" switch is a single global config. The tournament showed "always up"
 * wins against skeletons and loses against a surrounding zombie horde, so Rally decides it per guard once a second
 * ({@link FieldManual#wantsShieldUp}: target shoots, not surrounded) and only ever ADDS a "yes" here - the goal's own
 * checks (shield in hand, cooldown, target) in {@code canUse} still apply.
 */
@Mixin(targets = "tallestegg.guardvillagers.common.entities.Guard$RaiseShieldGoal")
public abstract class GuardRaiseShieldMixin {

    @Shadow
    @Final
    public Guard guard;

    @Inject(method = "raiseShield", at = @At("HEAD"), cancellable = true, require = 0)
    private void rallyguard$shieldUpAgainstShooters(CallbackInfoReturnable<Boolean> cir) {
        if (guard != null && guard.getTarget() != null && guard.shieldCoolDown == 0 && FieldManual.wantsShieldUp(guard)) {
            cir.setReturnValue(true);
        }
    }
}
