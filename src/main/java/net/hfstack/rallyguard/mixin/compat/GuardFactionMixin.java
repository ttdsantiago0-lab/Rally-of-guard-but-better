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

    @Inject(method = "setTarget", at = @At("HEAD"), require = 0)
    private void rallyguard$captureSetTarget(LivingEntity target, CallbackInfo ci) {
        RALLYGUARD$TARGET.set(target);
    }

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), require = 0)
    private void rallyguard$captureCanAttack(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        RALLYGUARD$TARGET.set(target);
    }

    @Redirect(method = {"setTarget", "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z"},
            at = @At(value = "INVOKE", target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"), require = 0)
    private boolean rallyguard$enemyFactionNotBlacklisted(List<?> blacklist, Object id) {
        LivingEntity target = RALLYGUARD$TARGET.get();
        if (target != null && Factions.enemies((Guard) (Object) this, target)) return false;
        return blacklist.contains(id);
    }
}
