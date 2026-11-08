package net.hfstack.rallyguard.mixin.compat;

import net.hfstack.rallyguard.contract.Factions;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * E3A F1 (David's sim: guards of two sides never hurt each other - 6 of 10 runs stuck).
 * Guard Villagers' {@code Guard#setTarget} and {@code Guard#canAttack} each test the target
 * against {@code GuardConfig.COMMON.MobBlackList}, whose default includes
 * {@code guardvillagers:guard}. That list check (the only {@code List.contains} in either
 * method, read from GV's 1.21.1 source) now answers "not blacklisted" when the target is a
 * guard of an ENEMY faction ({@link Factions#enemies}); for anything else the real list
 * answers. Unowned village guards are never enemies of anyone here, so village behavior is
 * unchanged. No public API can change a check inside another mod's method, hence a mixin;
 * {@link CompatMixinPlugin} applies it only if both methods still contain that call.
 * <p>
 * The target is captured at HEAD (server thread; a ThreadLocal keeps client calls apart).
 */
@Mixin(value = Guard.class, remap = false)
public abstract class GuardFactionMixin {

    private static final ThreadLocal<LivingEntity> RALLYGUARD$TARGET = new ThreadLocal<>();

    // E3B R9 (B13): this mixin is only applied when CompatMixinPlugin found EXACTLY one List.contains in each
    // method, so inside that condition every injection is required; the captured target is cleared on RETURN
    // (the ThreadLocal used to keep the last target entity alive).
    @Inject(method = "setTarget", at = @At("HEAD"), require = 1)
    private void rallyguard$captureSetTarget(LivingEntity target, CallbackInfo ci) {
        RALLYGUARD$TARGET.set(target);
    }

    @Inject(method = "setTarget", at = @At("RETURN"), require = 1)
    private void rallyguard$releaseSetTarget(LivingEntity target, CallbackInfo ci) {
        RALLYGUARD$TARGET.remove();
    }

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), require = 1)
    private void rallyguard$captureCanAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        RALLYGUARD$TARGET.set(target);
    }

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("RETURN"), require = 1)
    private void rallyguard$releaseCanAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        RALLYGUARD$TARGET.remove();
    }

    @Redirect(method = {"setTarget", "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"},
            at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"), require = 1)
    private boolean rallyguard$enemyFactionNotBlacklisted(List<?> blacklist, Object id) {
        LivingEntity target = RALLYGUARD$TARGET.get();
        if (target != null && Factions.enemies((Guard) (Object) this, target)) return false;
        // Field manual (David 2026-11-04, docs/MANUAL_DE_CAMPO.md): a player's shooters and shield-bearers may attack
        // creepers (GV blacklists them for every guard). Here and not in a mixin of its own: two @Redirects on this
        // same List.contains conflict, and Mixin skips the second one.
        if ("minecraft:creeper".equals(id) && net.hfstack.rallyguard.combat.FieldManual.mayTargetCreeper((Guard) (Object) this)) return false;
        // 2026-11-08: an angry enderman is fought back (same blacklist, same reason to be here)
        if ("minecraft:enderman".equals(id) && target != null
                && net.hfstack.rallyguard.combat.FieldManual.mayTargetEnderman((Guard) (Object) this, target)) return false;
        return blacklist.contains(id);
    }
}
