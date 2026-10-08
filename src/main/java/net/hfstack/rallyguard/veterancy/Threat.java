package net.hfstack.rallyguard.veterancy;

import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * E3A V1 - how dangerous an enemy is, in "zombies" (a 20-health, 3-damage walker = 1.0).
 * From cheap facts only: max health, attack damage, armor, flying, shooting, exploding;
 * players and guards (armed, thinking enemies) count double. Clamped to [0.25, 8] so a
 * boss is worth a lot but finite - the per-victim cap in {@code VeterancyEvents} then keeps
 * one boss fight from skipping a rank.
 */
public final class Threat {
    private Threat() {
    }

    public static double of(LivingEntity e) {
        double health = e.getMaxHealth() / 20.0D;
        double damage = e.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)
                ? e.getAttributeValue(Attributes.ATTACK_DAMAGE) / 3.0D : 0.5D;
        double armor = 1.0D + e.getArmorValue() / 20.0D;
        double t = (0.6D * health + 0.4D * damage) * armor;
        if (e instanceof FlyingMob || (e.isNoGravity() && !e.onGround())) t *= 1.5D;
        if (e instanceof RangedAttackMob) t *= 1.3D;
        if (e instanceof Creeper) t *= 1.3D;
        if (e instanceof Player || e instanceof Guard) t *= 2.0D;
        return Math.max(0.25D, Math.min(8.0D, t));
    }
}
