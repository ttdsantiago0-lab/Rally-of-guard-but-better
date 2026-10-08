package net.hfstack.rallyguard.veterancy;

import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import net.hfstack.rallyguard.contract.VeterancyRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * E3A A8 (H-06, central) - weapon mastery by ARCHETYPE, saved per guard UUID
 * ({@link VeterancyRegistry.Record#weaponXp}). It grows only from real combat XP earned
 * while holding that weapon (the same capped, threat-valued XP as rank), with diminishing
 * returns ({@link Veterancy#pathLevel}); what it changes is in {@code combat/MasteryEffects}.
 * Archetypes are generic (by item class), never a specific mod: bow, crossbow, gun (any
 * Scorched Guns gun), sword, axe, trident, other ranged, other melee.
 */
public final class WeaponMastery {
    private WeaponMastery() {
    }

    public static String archetype(ItemStack stack) {
        if (stack.isEmpty()) return "fist";
        if (ScorchedGunsCompat.isGun(stack)) return "gun";
        if (stack.getItem() instanceof CrossbowItem) return "crossbow";
        if (stack.getItem() instanceof BowItem) return "bow";
        if (stack.getItem() instanceof ProjectileWeaponItem) return "ranged";
        if (stack.getItem() instanceof TridentItem) return "trident";
        if (stack.getItem() instanceof AxeItem) return "axe";
        if (stack.getItem() instanceof SwordItem) return "sword";
        if (stack.getItem() instanceof ShieldItem) return "shield";
        return "melee";
    }

    /** Adds the combat XP just earned to the mastery of the weapon in hand. */
    static void gain(ServerLevel level, Guard guard, double xp) {
        if (xp <= 0 || net.hfstack.rallyguard.debug.SimProbe.blocksXp(guard)) return;
        String type = archetype(guard.getMainHandItem());
        Veterancy.edit(level, guard.getUUID()).weaponXp.merge(type, xp, Double::sum);
    }

    /** 0..1 mastery of the weapon the guard holds now. */
    public static double level(Guard guard) {
        VeterancyRegistry.Record r = Veterancy.record(guard);
        if (r == null) return 0.0D;
        Double xp = r.weaponXp.get(archetype(guard.getMainHandItem()));
        return xp == null ? 0.0D : Veterancy.pathLevel(xp) / Math.max(1.0E-6D,
                net.hfstack.rallyguard.config.RallyConfig.d(net.hfstack.rallyguard.config.RallyConfig.PATH_CAP, 100.0D));
    }
}
