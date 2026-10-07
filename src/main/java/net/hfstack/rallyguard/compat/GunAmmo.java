package net.hfstack.rallyguard.compat;

import net.hfstack.rallyguard.combat.Ammo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Phase 3H.1 - gunners need ammunition (user: "no quiero que sea infinita... un sistema
 * de logística cuando vuelven a base... pero que no se les gasten las balas como a
 * cualquier persona").
 * <p>
 * Scorched Guns' own mob AI refills the magazine for free on every reload (read from
 * {@code GunAttackGoal#refillCurrentWeapon}). Rally wraps that goal ({@link Rationed})
 * and charges each reload against real ammo items:
 * <ul>
 *     <li><b>Where the ammo is:</b> the guard's OFF hand - Guard Villagers gives a guard
 *     exactly six slots (four armor, two hands; read from its real source), so a gunner
 *     carries a stack of its gun's own ammo item there. (A bigger pack - crates, a
 *     donkey, stacks over 64 - is in docs/BACKLOG.md.)</li>
 *     <li><b>Cheaper than a player:</b> one ammo item per
 *     {@link net.hfstack.rallyguard.combat.Ammo#SHOTS_PER_ITEM} rounds reloaded (players
 *     pay one item per round). Since E1 every ammo payment goes through
 *     {@code combat.Ammo} (off hand, then the war pack and backpacks).</li>
 *     <li><b>Empty:</b> no ammo and an empty magazine - the gun AI stops and Guard
 *     Villagers' own melee takes over.</li>
 *     <li><b>Resupply = coming home:</b> an idle gunner (no target) refills its off-hand
 *     stack from any container (chest, barrel, crate...) within {@link #SUPPLY_RADIUS}
 *     blocks of where it stands - an armory next to the HQ banner is where a squad
 *     reloads after a Retreat.</li>
 * </ul>
 * The gun's ammo item comes from Scorched Guns' public API by reflection
 * ({@code GunItem#getModifiedGun(ItemStack) -> Gun#getProjectile() -> Projectile#getItem()}),
 * and the magazine from its {@code AmmoCount} custom-data key; any failure leaves the gun
 * unrationed rather than broken.
 */
public final class GunAmmo {
    private GunAmmo() {
    }

    public static final int SUPPLY_RADIUS = 4;

    private static Method getModifiedGun;
    private static Method getProjectile;
    private static Method getItem;
    private static boolean reflectionFailed;

    /** The ammo item {@code gun} fires, or null if unknown. */
    @Nullable
    public static Item ammoFor(ItemStack gun) {
        if (reflectionFailed || !ScorchedGunsCompat.isGun(gun)) return null;
        try {
            if (getModifiedGun == null) {
                getModifiedGun = gun.getItem().getClass().getMethod("getModifiedGun", ItemStack.class);
            }
            Object gunData = getModifiedGun.invoke(gun.getItem(), gun);
            if (getProjectile == null) getProjectile = gunData.getClass().getMethod("getProjectile");
            Object projectile = getProjectile.invoke(gunData);
            if (getItem == null) getItem = projectile.getClass().getMethod("getItem");
            return getItem.invoke(projectile) instanceof Item item ? item : null;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            reflectionFailed = true;
            return null;
        }
    }

    private static Method getSpeed, getLife;

    /**
     * E2A B4: how far a Scorched Guns gun reaches: its projectile's speed (blocks/tick) x
     * life (ticks), from {@code Gun#getProjectile()} ({@code getSpeed()}, {@code getLife()} -
     * public in Scorched Guns' Gun.Projectile). Capped at 160; 0 if it can't be read.
     */
    public static double rangeOf(ItemStack gun) {
        if (reflectionFailed || !ScorchedGunsCompat.isGun(gun)) return 0;
        try {
            if (getModifiedGun == null) getModifiedGun = gun.getItem().getClass().getMethod("getModifiedGun", ItemStack.class);
            Object gunData = getModifiedGun.invoke(gun.getItem(), gun);
            if (getProjectile == null) getProjectile = gunData.getClass().getMethod("getProjectile");
            Object projectile = getProjectile.invoke(gunData);
            if (getSpeed == null) getSpeed = projectile.getClass().getMethod("getSpeed");
            if (getLife == null) getLife = projectile.getClass().getMethod("getLife");
            double speed = ((Number) getSpeed.invoke(projectile)).doubleValue();
            double life = ((Number) getLife.invoke(projectile)).doubleValue();
            return Math.min(160.0D, Math.max(0.0D, speed * life));
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            return 0;
        }
    }

    public static int magazine(ItemStack gun) {
        return gun.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("AmmoCount");
    }

    private static void setMagazine(ItemStack gun, int rounds) {
        CompoundTag tag = gun.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putInt("AmmoCount", Math.max(0, rounds));
        gun.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    /** Ammo items (all sources) that fit the main-hand gun. */
    public static int carried(Guard guard) {
        Item ammo = ammoFor(guard.getMainHandItem());
        return ammo == null ? 0 : Ammo.count(guard, s -> s.is(ammo));
    }

    /** True if the gunner can still fire: rounds in the magazine or ammo to reload with
     *  (or the ammo item is unknown - then the gun simply isn't rationed). */
    public static boolean canFight(Guard guard) {
        ItemStack gun = guard.getMainHandItem();
        Item ammo = ammoFor(gun);
        return ammo == null || magazine(gun) > 0 || Ammo.available(guard, s -> s.is(ammo));
    }

    /** Charges a reload from {@code before} to the current magazine against the off
     *  hand; with too little ammo, the magazine only gets what was paid for. */
    static void chargeReload(Guard guard, int before) {
        ItemStack gun = guard.getMainHandItem();
        Item ammo = ammoFor(gun);
        if (ammo == null) return;
        int after = magazine(gun);
        if (after <= before) return;
        int rounds = after - before;
        int paid = Ammo.consume(guard, s -> s.is(ammo), rounds);
        if (paid < rounds) setMagazine(gun, before + paid);
    }

    /** Idle resupply from a container near the guard (see class javadoc). */
    public static void resupply(Guard guard) {
        if (guard.getTarget() != null) return;
        Item ammo = ammoFor(guard.getMainHandItem());
        if (ammo == null) return;
        ItemStack off = guard.getOffhandItem();
        if (!off.isEmpty() && !off.is(ammo)) return; // the off hand holds something else (shield, TNT)
        int room = off.isEmpty() ? new ItemStack(ammo).getMaxStackSize() : off.getMaxStackSize() - off.getCount();
        if (room <= 0) return;

        Level level = guard.level();
        BlockPos center = guard.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-SUPPLY_RADIUS, -2, -SUPPLY_RADIUS),
                center.offset(SUPPLY_RADIUS, 2, SUPPLY_RADIUS))) {
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof Container container)) continue;
            for (int i = 0; i < container.getContainerSize() && room > 0; i++) {
                ItemStack stack = container.getItem(i);
                if (!stack.is(ammo)) continue;
                int take = Math.min(room, stack.getCount());
                ItemStack taken = container.removeItem(i, take);
                if (off.isEmpty()) {
                    off = taken;
                    guard.setItemSlot(net.minecraft.world.entity.EquipmentSlot.OFFHAND, off);
                } else {
                    off.grow(taken.getCount());
                }
                room -= taken.getCount();
                container.setChanged();
            }
            if (room <= 0) return;
        }
    }

    /**
     * Scorched Guns' gun goal, rationed: it only runs while the gunner can fight, and
     * every reload it performs is paid for. Everything else (aim, bursts, range,
     * movement) is Scorched Guns' own.
     */
    static final class Rationed extends Goal {
        private final Guard guard;
        private final Goal inner;

        Rationed(Guard guard, Goal inner) {
            this.guard = guard;
            this.inner = inner;
            this.setFlags(inner.getFlags());
        }

        Goal inner() {
            return inner;
        }

        @Override
        public boolean canUse() {
            return inner.canUse() && canFight(guard);
        }

        @Override
        public boolean canContinueToUse() {
            return inner.canContinueToUse() && canFight(guard);
        }

        @Override
        public void start() {
            inner.start();
        }

        @Override
        public void stop() {
            inner.stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return inner.requiresUpdateEveryTick();
        }

        @Override
        public boolean isInterruptable() {
            return inner.isInterruptable();
        }

        @Override
        public void tick() {
            int before = magazine(guard.getMainHandItem());
            inner.tick();
            chargeReload(guard, before);
        }
    }
}
