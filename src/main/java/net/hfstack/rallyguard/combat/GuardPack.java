package net.hfstack.rallyguard.combat;

import net.hfstack.rallyguard.compat.GunAmmo;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.PackRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * E1-A / E1-B: what a guard does with its war pack ({@link PackRegistry}: six slots of
 * its own, opened from the Ledger's ▤ button).
 * <ul>
 *     <li><b>Ammo</b> - {@link Ammo} takes arrows and gun rounds from the off hand, then the
 *     pack, then any backpack inside it.</li>
 *     <li><b>Backpacks (E1-B)</b> - any item in a pack slot that exposes NeoForge's item
 *     handler capability ({@code Capabilities.ItemHandler.ITEM}) with more than six slots
 *     (e.g. Sophisticated Backpacks) is used as extra storage for ammo and consumables.
 *     Recognized by the capability alone, not by mod id - no dependency.</li>
 *     <li><b>Off hand</b> - out of combat, an empty off hand is refilled with the main
 *     weapon's ammo from the pack.</li>
 *     <li><b>Second weapon</b> - a ranged guard out of ammo swaps to a sword/axe/trident
 *     from its pack; it swaps back out of combat once it has ammo again.</li>
 *     <li><b>Consumables</b> - below 40% health it eats a golden apple, else drinks a
 *     potion, else eats other food from the pack (at most every 5 s).</li>
 *     <li><b>Resupply</b> - idle near a chest/barrel (≤4 blocks) it fills the pack with
 *     its weapon's ammo.</li>
 *     <li><b>Death</b> - the pack drops where the guard fell (or turned zombie); a guard
 *     forgotten from the Ledger hands its pack to its commander.</li>
 * </ul>
 * Runs from the stance loop (once a second per guard, spread over 20 ticks).
 */
public final class GuardPack {
    private GuardPack() {
    }

    public static final int SUPPLY_RADIUS = 4;
    private static final int CONSUME_COOLDOWN_TICKS = 100;

    private static final Map<UUID, Long> LAST_CONSUMED = new HashMap<>();
    /** Guards whose main hand Rally swapped to a melee weapon for lack of ammo. */
    private static final Set<UUID> SWAPPED = new HashSet<>();

    // ==== Storage ====

    @Nullable
    private static SimpleContainer peek(Guard guard) {
        return guard.level() instanceof ServerLevel level ? PackRegistry.get(level).peek(guard.getUUID()) : null;
    }

    /** The guard's pack (created if needed); server side only. */
    public static SimpleContainer of(ServerLevel level, UUID guard) {
        return PackRegistry.get(level).of(guard);
    }

    /** E1-B: the item's handler if it is a backpack (an item handler with more than six slots). */
    @Nullable
    public static IItemHandler backpackOf(ItemStack stack) {
        if (stack.isEmpty()) return null;
        IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
        return handler != null && handler.getSlots() > PackRegistry.SIZE ? handler : null;
    }

    /** Pack slots, then backpack slots - every place ammo and consumables are taken from. */
    private static List<Ammo.Slot> storeSlots(Guard guard) {
        List<Ammo.Slot> out = new ArrayList<>();
        SimpleContainer pack = peek(guard);
        if (pack == null) return out;
        for (int i = 0; i < pack.getContainerSize(); i++) {
            final int idx = i;
            out.add(new Ammo.Slot() {
                @Override
                public ItemStack peek() {
                    return pack.getItem(idx);
                }

                @Override
                public int take(int n) {
                    return pack.removeItem(idx, n).getCount();
                }
            });
        }
        for (int i = 0; i < pack.getContainerSize(); i++) {
            IItemHandler bag = backpackOf(pack.getItem(i));
            if (bag == null) continue;
            for (int j = 0; j < bag.getSlots(); j++) {
                final int slot = j;
                out.add(new Ammo.Slot() {
                    @Override
                    public ItemStack peek() {
                        return bag.getStackInSlot(slot);
                    }

                    @Override
                    public int take(int n) {
                        return bag.extractItem(slot, n, false).getCount();
                    }
                });
            }
        }
        return out;
    }

    static void addAmmoSlots(Guard guard, List<Ammo.Slot> out) {
        out.addAll(storeSlots(guard));
    }

    /** "usedSlots:ammoItems" for the Ledger's details ("" if not loaded server-side). */
    public static String summary(Guard guard) {
        SimpleContainer pack = peek(guard);
        int used = 0;
        if (pack != null) {
            for (int i = 0; i < pack.getContainerSize(); i++) if (!pack.getItem(i).isEmpty()) used++;
        }
        Predicate<ItemStack> ammo = ammoFor(guard.getMainHandItem());
        return used + ":" + (ammo == null ? 0 : Ammo.count(guard, ammo));
    }

    /** The ammo a weapon uses, or null for a melee weapon / unknown gun ammo. */
    @Nullable
    public static Predicate<ItemStack> ammoFor(ItemStack weapon) {
        WeaponProfile profile = WeaponProfile.of(weapon);
        if (profile == null) return null;
        if (profile.firesArrows()) return Ammo.ARROWS;
        Item item = GunAmmo.ammoFor(weapon);
        return item == null ? null : s -> s.is(item);
    }

    // ==== Once a second ====

    public static void tick(ServerLevel level, Guard guard, boolean resupplyNow) {
        consume(level, guard);
        boolean inCombat = guard.getTarget() != null;
        swapForAmmo(guard, inCombat);
        if (inCombat) return;
        Predicate<ItemStack> ammo = ammoFor(guard.getMainHandItem());
        if (ammo != null && guard.getOffhandItem().isEmpty()) takeToOffhand(guard, ammo);
        if (resupplyNow && ammo != null && RallyConfig.on(RallyConfig.PACK_RESUPPLY)) resupply(level, guard, ammo);
    }

    private static boolean isDry(Guard guard, ItemStack weapon) {
        Predicate<ItemStack> ammo = ammoFor(weapon);
        if (ammo == null) return false;
        if (ammo == Ammo.ARROWS && !RallyConfig.on(RallyConfig.FINITE_ARROWS)) return false;
        if (weapon == guard.getMainHandItem() && !Ammo.ARROWS.equals(ammo)) return !GunAmmo.canFight(guard);
        return !Ammo.available(guard, ammo);
    }

    private static boolean isMelee(ItemStack s) {
        return s.getItem() instanceof SwordItem || s.getItem() instanceof AxeItem || s.getItem() instanceof TridentItem;
    }

    private static void swapForAmmo(Guard guard, boolean inCombat) {
        SimpleContainer pack = peek(guard);
        if (pack == null) return;
        ItemStack main = guard.getMainHandItem();
        if (!SWAPPED.contains(guard.getUUID())) {
            if (!isDry(guard, main)) return;
            for (int i = 0; i < pack.getContainerSize(); i++) {
                if (isMelee(pack.getItem(i))) {
                    swap(guard, pack, i);
                    SWAPPED.add(guard.getUUID());
                    return;
                }
            }
            return;
        }
        if (inCombat || !isMelee(main)) {
            if (!isMelee(main)) SWAPPED.remove(guard.getUUID()); // someone changed it by hand
            return;
        }
        for (int i = 0; i < pack.getContainerSize(); i++) {
            ItemStack candidate = pack.getItem(i);
            Predicate<ItemStack> ammo = ammoFor(candidate);
            if (ammo != null && Ammo.count(guard, ammo) > 0) {
                swap(guard, pack, i);
                SWAPPED.remove(guard.getUUID());
                return;
            }
        }
    }

    private static void swap(Guard guard, SimpleContainer pack, int slot) {
        ItemStack fromPack = pack.getItem(slot);
        pack.setItem(slot, guard.getMainHandItem().copy());
        guard.setItemSlot(EquipmentSlot.MAINHAND, fromPack);
        net.hfstack.rallyguard.debug.SimProbe.weaponSwap(guard);
    }

    private static void consume(ServerLevel level, Guard guard) {
        if (guard.getHealth() > guard.getMaxHealth() * 0.4F) return;
        Long last = LAST_CONSUMED.get(guard.getUUID());
        if (last != null && level.getGameTime() - last < CONSUME_COOLDOWN_TICKS) return;
        List<Ammo.Slot> slots = storeSlots(guard);
        Ammo.Slot pick = find(slots, s -> s.is(Items.GOLDEN_APPLE) || s.is(Items.ENCHANTED_GOLDEN_APPLE));
        if (pick == null) pick = find(slots, s -> s.is(Items.POTION));
        if (pick == null) pick = find(slots, s -> s.get(DataComponents.FOOD) != null);
        if (pick == null) return;
        ItemStack one = pick.peek().copyWithCount(1);
        if (pick.take(1) != 1) return;
        LAST_CONSUMED.put(guard.getUUID(), level.getGameTime());
        PotionContents potion = one.get(DataComponents.POTION_CONTENTS);
        if (one.is(Items.POTION) && potion != null) {
            potion.forEachEffect(guard::addEffect);
            return;
        }
        FoodProperties food = one.get(DataComponents.FOOD);
        if (food != null) guard.eat(level, one, food);
    }

    @Nullable
    private static Ammo.Slot find(List<Ammo.Slot> slots, Predicate<ItemStack> what) {
        for (Ammo.Slot s : slots) {
            ItemStack st = s.peek();
            if (!st.isEmpty() && what.test(st)) return s;
        }
        return null;
    }

    /** Moves a stack matching {@code what} from the pack to the (empty, or same-item) off
     *  hand. Returns true if the off hand holds such an item afterwards. */
    public static boolean takeToOffhand(Guard guard, Predicate<ItemStack> what) {
        ItemStack off = guard.getOffhandItem();
        if (!off.isEmpty() && what.test(off)) return true;
        if (!off.isEmpty()) return false;
        SimpleContainer pack = peek(guard);
        if (pack == null) return false;
        for (int i = 0; i < pack.getContainerSize(); i++) {
            if (!pack.getItem(i).isEmpty() && what.test(pack.getItem(i))) {
                guard.setItemSlot(EquipmentSlot.OFFHAND, pack.removeItemNoUpdate(i));
                pack.setChanged();
                return true;
            }
        }
        return false;
    }

    /** Fills the pack with {@code ammo} from containers within {@link #SUPPLY_RADIUS}. */
    private static void resupply(ServerLevel level, Guard guard, Predicate<ItemStack> ammo) {
        SimpleContainer pack = of(level, guard.getUUID());
        BlockPos center = guard.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(center.offset(-SUPPLY_RADIUS, -2, -SUPPLY_RADIUS),
                center.offset(SUPPLY_RADIUS, 2, SUPPLY_RADIUS))) {
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof Container container)) continue;
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (stack.isEmpty() || !ammo.test(stack)) continue;
                ItemStack left = pack.addItem(stack.copy());
                int moved = stack.getCount() - left.getCount();
                if (moved > 0) {
                    container.removeItem(i, moved);
                    container.setChanged();
                }
                if (!left.isEmpty()) return; // pack full
            }
        }
    }

    // ==== Death / forget ====

    /** Drops the guard's pack at {@code pos} (death, zombie conversion). */
    public static void dropAll(ServerLevel level, UUID guard, Vec3 pos) {
        SimpleContainer pack = PackRegistry.get(level).remove(guard);
        forget(guard);
        if (pack == null) return;
        for (int i = 0; i < pack.getContainerSize(); i++) {
            ItemStack s = pack.getItem(i);
            if (!s.isEmpty()) Containers.dropItemStack(level, pos.x, pos.y, pos.z, s);
        }
    }

    /** Hands a forgotten guard's pack to its commander. */
    public static void giveTo(ServerPlayer player, UUID guard) {
        SimpleContainer pack = PackRegistry.get(player.serverLevel()).remove(guard);
        forget(guard);
        if (pack == null) return;
        for (int i = 0; i < pack.getContainerSize(); i++) {
            ItemStack s = pack.getItem(i);
            if (!s.isEmpty() && !player.getInventory().add(s)) player.drop(s, false);
        }
    }

    public static void forget(UUID guard) {
        LAST_CONSUMED.remove(guard);
        SWAPPED.remove(guard);
    }

    public static void clearAll() {
        LAST_CONSUMED.clear();
        SWAPPED.clear();
    }

    /** True if the level object is a server level with this guard's pack - for menus. */
    public static boolean isServer(Level level) {
        return level instanceof ServerLevel;
    }
}
