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
 * ({@link FieldManual#wantsShieldUp}: under fire from a shooter - its target, one that hit it in the last 5 s, or an
 * enemy shooter within 24 blocks that can see it - and not surrounded) and only ever ADDS a "yes" here - the goal's own checks (shield in hand, cooldown) in {@code canUse}
 * still apply.
 */
// remap = false: Guard Villagers' own method/field names have no mappings (with remap on, Mixin skipped it silently)
@Mixin(targets = "tallestegg.guardvillagers.common.entities.Guard$RaiseShieldGoal", remap = false)
public abstract class GuardRaiseShieldMixin {

    @Shadow(remap = false)
    @Final
    public Guard guard;

    @Inject(method = "raiseShield", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void rallyguard$shieldUpAgainstShooters(CallbackInfoReturnable<Boolean> cir) {
        // No target required: "under fire" also counts a recent hit or a nearby shooter that sees it (FieldManual).
        if (guard != null && guard.shieldCoolDown == 0 && FieldManual.wantsShieldUp(guard)) {
            cir.setReturnValue(true);
        }
    }
}
