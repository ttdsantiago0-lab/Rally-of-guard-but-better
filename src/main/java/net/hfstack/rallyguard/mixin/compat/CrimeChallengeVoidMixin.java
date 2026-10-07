package net.hfstack.rallyguard.mixin.compat;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * E2A B7 (David: MCA Crime's "Stop where you stand..." fired from Rally's own guards).
 * MCA Crime's {@code GuardChallengeService.challenge(level, guard, player)} (per
 * {@code docs/API_MODS.md}) is where every guard challenge starts; Mob Compat makes
 * Guard Villagers' guards count as MCA authority. A guard with an OWNER is a Rally soldier,
 * not village law, so its challenge is skipped. Option (b) of API_MODS.md: option (a)
 * would need Mob Compat's classification API, which isn't documented there. Narrow:
 * owned guards only; applied only when {@link CompatMixinPlugin} finds the method with
 * this return type (Void); mod absent = no mixin.
 */
@Pseudo
@Mixin(targets = "dev.otectus.mcacrime.enforcement.GuardChallengeService", remap = false)
public abstract class CrimeChallengeVoidMixin {

    @Inject(method = "challenge", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private static void rallyguard$skipRallyGuards(ServerLevel level, LivingEntity guard, ServerPlayer player, CallbackInfo ci) {
        if (guard instanceof Guard g && g.getOwnerId() != null) {
            ci.cancel();
        }
    }
}
