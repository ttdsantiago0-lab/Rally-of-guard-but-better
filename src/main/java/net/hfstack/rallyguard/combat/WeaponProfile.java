package net.hfstack.rallyguard.combat;

import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import net.hfstack.rallyguard.config.RallyConfig;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import tallestegg.guardvillagers.configuration.GuardConfig;

import javax.annotation.Nullable;

/**
 * E1-G: one table of ranged weapons - how far each one really reaches. Values come from
 * the config (defaults: bow 40 / 15 / 5, crossbow 44 / 16 / 5, gun 40 / 20 / 6).
 * <ul>
 *     <li>{@code maxRange}: the furthest a guard engages with it (and its post leash is at
 *     least 0.8x this).</li>
 *     <li>{@code optimalRange}: where it likes to stand; a guard further away closes in
 *     to here, not to sword range.</li>
 *     <li>{@code minRange}: closer than this, a RETREATING guard steps back.</li>
 *     <li>{@code directRange}: what Guard Villagers' own bow/crossbow goal covers (its
 *     attack radius: 15 for bows, its crossbow config value); Rally fires the shots
 *     beyond it ({@code RangedFireGoal}).</li>
 * </ul>
 * Scorched Guns ranges come from the config: reading them from each gun's data would
 * need more reflection than it's worth (the gun AI handles its own range anyway).
 */
public record WeaponProfile(Kind kind, double maxRange, double optimalRange, double minRange,
                            double directRange, float arrowSpeed) {

    public enum Kind { BOW, CROSSBOW, GUN }

    @Nullable
    public static WeaponProfile of(ItemStack weapon) {
        if (weapon.getItem() instanceof BowItem) {
            return new WeaponProfile(Kind.BOW, RallyConfig.i(RallyConfig.BOW_MAX, 40),
                    RallyConfig.i(RallyConfig.BOW_OPTIMAL, 15), RallyConfig.i(RallyConfig.BOW_MIN, 5), 15.0D, 3.0F);
        }
        if (weapon.getItem() instanceof CrossbowItem) {
            double direct;
            try {
                direct = GuardConfig.COMMON.guardCrossbowAttackRadius.get();
            } catch (RuntimeException e) {
                direct = 8.0D;
            }
            return new WeaponProfile(Kind.CROSSBOW, RallyConfig.i(RallyConfig.CROSSBOW_MAX, 44),
                    RallyConfig.i(RallyConfig.CROSSBOW_OPTIMAL, 16), RallyConfig.i(RallyConfig.CROSSBOW_MIN, 5), direct, 3.15F);
        }
        if (ScorchedGunsCompat.isGun(weapon)) {
            return new WeaponProfile(Kind.GUN, RallyConfig.i(RallyConfig.GUN_MAX, 40),
                    RallyConfig.i(RallyConfig.GUN_OPTIMAL, 20), RallyConfig.i(RallyConfig.GUN_MIN, 6), 40.0D, 0.0F);
        }
        return null;
    }

    /** Bows and crossbows: Rally can fire them itself (long shots, volleys). */
    public boolean firesArrows() {
        return kind != Kind.GUN;
    }
}
