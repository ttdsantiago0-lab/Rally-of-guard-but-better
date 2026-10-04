package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.compat.GunAmmo;
import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Phase 3H.1 - the first piece of "trained soldiers" behavior (user review: "una
 * cuartilla de 10 guardias atacando un solo objetivo se ven desorganizados y
 * amontonados"; "un aldeano zombie bebé convierte a 3 guardias"). The cause, read from
 * Guard Villagers' real source: its {@code HurtByTargetGoal(...).setAlertOthers()} hands
 * the attacker to EVERY nearby guard, and an explicit Attack order did the same, so ten
 * swords crowd one small mob, block each other and give a zombie ten victims to convert.
 * <p>
 * <b>Rule:</b> only as many MELEE guards of one commander engage one target as can
 * physically stand around it - {@link #capFor} grows with the target's width (3 around
 * a baby zombie, 4 around a zombie, 8 around a ravager or a dragon). The rest:
 * <ul>
 *     <li>pick another enemy if their stance lets them choose (focus is distributed
 *     across a group of enemies instead of all piling on the first one); or</li>
 *     <li>if the commander ordered that target, hold a "screen" ring a few blocks around
 *     it, ready to step in the moment an engaged guard falls or backs off.</li>
 * </ul>
 * Ranged guards (bow, crossbow, a Scorched Guns gun) are never capped - they don't
 * crowd, they shoot. Self-defense is never capped either: a guard always answers the one
 * hitting it.
 * <p>
 * Cost: the table is rebuilt once a second from the guard list the stance loop already
 * collects (one pass, O(guards)); the combat gate only reads and increments it. No
 * per-event entity scans, so it scales with the army.
 */
public final class Engagement {
    private Engagement() {
    }

    private record Key(UUID owner, UUID target) {
    }

    private static final Map<ResourceKey<Level>, Map<Key, Set<UUID>>> TABLES = new HashMap<>();

    /** How far the screen ring stands from the target. */
    public static final double SCREEN_RADIUS = 4.5D;

    public static boolean isRanged(Guard guard) {
        return guard.getMainHandItem().getItem() instanceof ProjectileWeaponItem
                || (ScorchedGunsCompat.isGun(guard.getMainHandItem()) && GunAmmo.canFight(guard)); // dry gunners melee
    }

    /** Melee guards that fit around {@code target}. */
    public static int capFor(LivingEntity target) {
        return Mth.clamp(Math.round(target.getBbWidth() * 4.0F) + 2, 3, 8);
    }

    /** Rebuilds this level's table: for each (commander, target), the nearest melee
     *  guards up to the cap are "engaged"; the others are over the cap. */
    public static void rebuild(ResourceKey<Level> dimension, List<? extends Guard> guards) {
        Map<Key, List<Guard>> byTarget = new HashMap<>();
        Map<UUID, Integer> caps = new HashMap<>();
        for (Guard guard : guards) {
            LivingEntity target = guard.getTarget();
            UUID owner = GuardOwnership.getOwner(guard);
            if (target == null || !target.isAlive() || owner == null || isRanged(guard)) continue;
            byTarget.computeIfAbsent(new Key(owner, target.getUUID()), k -> new ArrayList<>()).add(guard);
            caps.putIfAbsent(target.getUUID(), capFor(target));
        }
        Map<Key, Set<UUID>> table = new HashMap<>();
        for (Map.Entry<Key, List<Guard>> e : byTarget.entrySet()) {
            List<Guard> list = e.getValue();
            int cap = caps.get(e.getKey().target());
            Set<UUID> engaged = new LinkedHashSet<>();
            if (list.size() > cap) {
                LivingEntity target = list.get(0).getTarget();
                list.sort(Comparator.comparingDouble(g -> g.distanceToSqr(target)));
            }
            for (int i = 0; i < Math.min(cap, list.size()); i++) engaged.add(list.get(i).getUUID());
            table.put(e.getKey(), engaged);
        }
        TABLES.put(dimension, table);
    }

    /**
     * May this melee guard engage {@code target} now? True if it's already one of the
     * engaged, or there is room (it then takes the place). Ranged guards always may.
     */
    public static boolean tryEngage(Guard guard, LivingEntity target) {
        if (isRanged(guard)) return true;
        UUID owner = GuardOwnership.getOwner(guard);
        if (owner == null) return true;
        ResourceKey<Level> dim = guard.level().dimension();
        Set<UUID> engaged = TABLES.computeIfAbsent(dim, d -> new HashMap<>())
                .computeIfAbsent(new Key(owner, target.getUUID()), k -> new LinkedHashSet<>());
        if (engaged.contains(guard.getUUID())) return true;
        if (engaged.size() >= capFor(target)) return false;
        engaged.add(guard.getUUID());
        return true;
    }

    /** True if {@code target} already has its full share of this guard's commander's
     *  melee guards (other than this one) - used to spread a stance's target choice. */
    public static boolean isSaturated(Guard guard, LivingEntity target) {
        if (isRanged(guard)) return false;
        UUID owner = GuardOwnership.getOwner(guard);
        if (owner == null) return false;
        Map<Key, Set<UUID>> table = TABLES.get(guard.level().dimension());
        Set<UUID> engaged = table == null ? null : table.get(new Key(owner, target.getUUID()));
        return engaged != null && !engaged.contains(guard.getUUID()) && engaged.size() >= capFor(target);
    }

    /** This guard's own place on the screen ring around {@code target} (fixed angle per
     *  guard, so the ring spreads out instead of bunching on one side). */
    public static BlockPos screenSpot(Guard guard, LivingEntity target) {
        double angle = (guard.getUUID().getMostSignificantBits() & 0xFFFFL) / 65536.0D * Math.PI * 2.0D;
        Vec3 spot = target.position().add(Math.cos(angle) * SCREEN_RADIUS, 0.0D, Math.sin(angle) * SCREEN_RADIUS);
        return GuardPosts.standableNear(guard.level(), spot);
    }

    public static void clearAll() {
        TABLES.clear();
    }
}
