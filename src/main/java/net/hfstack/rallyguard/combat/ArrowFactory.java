package net.hfstack.rallyguard.combat;

import net.hfstack.rallyguard.config.RallyConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * E2A B3 (David: a guard's dragon bow and dragon arrows hit softer than his). The old shot
 * used {@code ProjectileUtil.getMobArrow}, which always makes the arrow from a VANILLA
 * arrow item and then overwrites its base damage with the mob formula - so modded arrows
 * lost their entity and their damage, and the bow lost its full-draw critical.
 * <p>
 * Now a guard's arrow is made the way a player's is (per {@code docs/API_MODS.md}, read
 * from Ice and Fire's {@code DragonBowItem}): from the REAL ammo stack with
 * {@code ArrowItem#createArrow(level, ammo, shooter, weapon)} (keeps the modded arrow
 * entity, its base damage, and records the weapon, so the weapon's own enchantment effects
 * - Power, Punch - apply when it hits, as in vanilla 1.21), then the weapon's
 * {@code customArrow}, then:
 * <ul>
 *     <li>critical, always - a guard always shoots at full draw (and crossbow bolts are
 *     always critical in vanilla);</li>
 *     <li>Flame: set on fire for 100 s, as the player's shot;</li>
 *     <li>weapons whose OWN release code adds Power to the base damage (they don't rely
 *     on the 1.21 enchantment effect) - listed by class name in
 *     {@code ranged.legacyPowerWeaponClasses}, default Ice and Fire's DragonBowItem - also
 *     get {@code base + 0.5 x Power + 0.5}, matching that weapon's player shot.</li>
 * </ul>
 * Nothing here is specific to one mod.
 */
public final class ArrowFactory {
    private ArrowFactory() {
    }

    public static AbstractArrow create(ServerLevel level, Guard guard, ItemStack weapon, ItemStack ammo) {
        ItemStack round = ammo.isEmpty() ? new ItemStack(Items.ARROW) : ammo;
        ArrowItem arrowItem = round.getItem() instanceof ArrowItem a ? a : (ArrowItem) Items.ARROW;
        AbstractArrow arrow = arrowItem.createArrow(level, round, guard, weapon);
        if (weapon.getItem() instanceof BowItem bow) arrow = bow.customArrow(arrow, round, weapon); // as Guard Villagers does
        arrow.setCritArrow(true);

        int flame = level(level, Enchantments.FLAME, weapon);
        if (flame > 0) arrow.igniteForSeconds(100.0F);
        if (isLegacyPowerWeapon(weapon)) {
            int power = level(level, Enchantments.POWER, weapon);
            if (power > 0) arrow.setBaseDamage(arrow.getBaseDamage() + power * 0.5D + 0.5D);
        }
        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        return arrow;
    }

    private static int level(ServerLevel level, ResourceKey<Enchantment> key, ItemStack weapon) {
        try {
            Holder<Enchantment> holder = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(key);
            return EnchantmentHelper.getItemEnchantmentLevel(holder, weapon);
        } catch (RuntimeException e) {
            return 0;
        }
    }

    private static boolean isLegacyPowerWeapon(ItemStack weapon) {
        List<? extends String> classes = RallyConfig.get(RallyConfig.LEGACY_POWER_WEAPONS::get, List.of());
        String name = weapon.getItem().getClass().getName();
        for (String c : classes) if (c.equals(name)) return true;
        return false;
    }
}
