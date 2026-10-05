package net.hfstack.rallyguard.combat;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * E1: the ONE place ammunition is paid for (decision 2026-10-05: "la munición sale solo
 * del inventario, nada virtual; 1 ítem cada 4 disparos, constante en un único sitio").
 * Arrows (bows, crossbows, Rally's long shots and volleys) and Scorched Guns rounds all go
 * through {@link #consume}.
 * <p>
 * Where ammo is looked for, in order: the guard's off hand, then its war pack and any
 * backpack inside it ({@link #slots}). Each guard keeps a small "credit" of shots already
 * paid for, so one item really covers {@link #SHOTS_PER_ITEM} shots.
 */
public final class Ammo {
    private Ammo() {
    }

    /** Shots one ammo item pays for. The only place this number lives. */
    public static final int SHOTS_PER_ITEM = 4;

    public static final Predicate<ItemStack> ARROWS = s -> s.getItem() instanceof ArrowItem;

    /** One place ammo can be taken from. */
    public interface Slot {
        ItemStack peek();

        /** Removes up to {@code n} items; returns how many were removed. */
        int take(int n);
    }

    private static final Map<UUID, Integer> CREDIT = new HashMap<>();

    /** Every ammo source of this guard, in the order they are used. */
    public static List<Slot> slots(Guard guard) {
        List<Slot> out = new ArrayList<>();
        out.add(new Slot() {
            @Override
            public ItemStack peek() {
                return guard.getOffhandItem();
            }

            @Override
            public int take(int n) {
                ItemStack off = guard.getOffhandItem();
                int t = Math.min(n, off.getCount());
                off.shrink(t);
                if (off.isEmpty()) guard.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                return t;
            }
        });
        GuardPack.addAmmoSlots(guard, out);
        return out;
    }

    /** Items of this kind the guard carries (all sources). */
    public static int count(Guard guard, Predicate<ItemStack> isAmmo) {
        int n = 0;
        for (Slot s : slots(guard)) {
            ItemStack st = s.peek();
            if (!st.isEmpty() && isAmmo.test(st)) n += st.getCount();
        }
        return n;
    }

    /** True if at least one more shot can be paid. */
    public static boolean available(Guard guard, Predicate<ItemStack> isAmmo) {
        return CREDIT.getOrDefault(guard.getUUID(), 0) > 0 || count(guard, isAmmo) > 0;
    }

    /** Pays for {@code shots}; returns how many were actually paid (0 = dry). */
    public static int consume(Guard guard, Predicate<ItemStack> isAmmo, int shots) {
        UUID id = guard.getUUID();
        int credit = CREDIT.getOrDefault(id, 0);
        int paid = Math.min(credit, shots);
        credit -= paid;
        while (paid < shots) {
            if (!takeOne(guard, isAmmo)) break;
            credit += SHOTS_PER_ITEM;
            int more = Math.min(credit, shots - paid);
            paid += more;
            credit -= more;
        }
        if (credit > 0) CREDIT.put(id, credit); else CREDIT.remove(id);
        return paid;
    }

    private static boolean takeOne(Guard guard, Predicate<ItemStack> isAmmo) {
        for (Slot s : slots(guard)) {
            ItemStack st = s.peek();
            if (!st.isEmpty() && isAmmo.test(st) && s.take(1) == 1) return true;
        }
        return false;
    }

    public static void forget(UUID guard) {
        CREDIT.remove(guard);
    }

    public static void clearAll() {
        CREDIT.clear();
    }
}
