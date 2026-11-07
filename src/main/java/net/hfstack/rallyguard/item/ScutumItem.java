package net.hfstack.rallyguard.item;

import net.minecraft.world.item.ShieldItem;

/**
 * The scutum, the Roman shield (David 2026-11-06; {@code docs/ESCOLTA_Y_TESTUDO.md} §5). A {@link ShieldItem}, so
 * everything that asks "is this a shield?" ({@code canPerformAction(SHIELD_BLOCK)}: Guard Villagers' raise-shield goal,
 * our field manual, other mods) treats it as one. What it adds lives elsewhere and keys on this item:
 * <ul>
 *     <li>in a testudo, it blocks projectiles from any side while a comrade stands next to its bearer
 *     ({@code combat/Testudo});</li>
 *     <li>a breath it blocked does not leave its bearer burning or frozen ({@code combat/Scutum}: Ice and Fire applies
 *     the fire, frost or knockback after the hit, whether or not the shield stopped it).</li>
 * </ul>
 * Twice the vanilla shield's durability. Placeholder art (flat item model) until it gets its own.
 */
public class ScutumItem extends ShieldItem {
    public ScutumItem(Properties properties) {
        super(properties);
    }
}
