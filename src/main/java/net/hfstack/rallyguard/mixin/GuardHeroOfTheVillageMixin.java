package net.hfstack.rallyguard.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * Hero of the Village no longer shields anyone from a COMMANDED guard (user decision,
 * 2026-09-28: "Héroe de la aldea no puede hacer que no te targeteen guardias").
 * <p>
 * Guard Villagers' own {@code Guard#setTarget} and {@code Guard#canAttack} both refuse
 * any target that has the Hero of the Village effect (read from its real 1.21.1 source).
 * That is a sensible rule for a village's own guards, but it made a PvP enemy who had
 * won a raid completely untargetable by a player's army - no order, stance or
 * retaliation could touch them. No public API can change a check inside another mod's
 * method, so this is one of the project's few mixins (see CLAUDE.md).
 * <p>
 * Deliberately narrow: it only redirects the single {@code hasEffect} call inside those
 * two methods (the Hero check - each method has exactly one), and only for guards that
 * have an owner. Unowned village guards keep Guard Villagers' behavior unchanged.
 */
@Mixin(Guard.class)
public abstract class GuardHeroOfTheVillageMixin {

    // E3B R9 (B13): require = 0 - a different Guard Villagers version without this call must not stop the game
    // from starting (the config's defaultRequire = 1 would); it then simply does nothing.
    @Redirect(method = {"setTarget", "canAttack"},
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z"), require = 0)
    private boolean rallyguard$heroDoesNotShieldFromCommandedGuards(LivingEntity target, Holder<MobEffect> effect) {
        if (effect == MobEffects.HERO_OF_THE_VILLAGE && ((Guard) (Object) this).getOwnerId() != null) {
            return false;
        }
        return target.hasEffect(effect);
    }
}
