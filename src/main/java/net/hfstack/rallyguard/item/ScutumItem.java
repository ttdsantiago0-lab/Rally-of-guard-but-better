package net.hfstack.rallyguard.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

import java.util.function.Predicate;

/**
 * The scutum, the Roman shield (David 2026-11-06; {@code docs/ESCOLTA_Y_TESTUDO.md} §5), in five tiers (David, same day:
 * "crafteos desde el más básico hasta uno de netherita, incluso uno de dragonforge"): wood, iron, diamond, netherite and
 * dragonsteel (Ice and Fire's dragon forge - its recipe only loads with Ice and Fire). A tier changes only what an item
 * is made of: uses, what repairs it, whether it burns. Every tier is a {@link ShieldItem}, so everything that asks "is
 * this a shield?" ({@code canPerformAction(SHIELD_BLOCK)}: Guard Villagers' raise-shield goal, our field manual, other
 * mods) treats it as one, and every tier does what makes it a scutum, which lives elsewhere and keys on this class:
 * <ul>
 *     <li>in a testudo, it blocks projectiles from any side while a comrade stands next to its bearer
 *     ({@code combat/Testudo});</li>
 *     <li>a breath it blocked does not leave its bearer burning or frozen ({@code combat/Scutum}: Ice and Fire applies
 *     the fire, frost or knockback after the hit, whether or not the shield stopped it).</li>
 * </ul>
 * Placeholder art (flat item models) until it gets its own.
 */
public class ScutumItem extends ShieldItem {
    private final Predicate<ItemStack> repair;

    public ScutumItem(Properties properties, Predicate<ItemStack> repair) {
        super(properties);
        this.repair = repair;
    }

    /** Each tier is mended with what it is made of (a vanilla shield takes planks, whatever it is). */
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack material) {
        return repair.test(material);
    }
}
