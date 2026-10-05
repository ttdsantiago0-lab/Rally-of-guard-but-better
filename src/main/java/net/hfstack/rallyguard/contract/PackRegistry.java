package net.hfstack.rallyguard.contract;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * E1-A: every guard's war pack - six slots of its own beyond its hands and armor - kept
 * by guard UUID in world data (the same persistent mechanism as the roster), so it
 * survives reloads and dimension changes no matter what happens to the entity. Packs are
 * created on first use; an empty pack is not saved.
 */
public final class PackRegistry extends SavedData {

    public static final int SIZE = 6;
    private static final String DATA_ID = "rallyguard_packs";

    private final Map<UUID, SimpleContainer> packs = new HashMap<>();

    public static PackRegistry get(ServerLevel anyLevel) {
        return anyLevel.getServer().overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(PackRegistry::new, PackRegistry::load, null), DATA_ID);
    }

    private static PackRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        PackRegistry r = new PackRegistry();
        ListTag list = tag.getList("Packs", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag t = list.getCompound(i);
            NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
            ContainerHelper.loadAllItems(t, items, registries);
            SimpleContainer c = r.track(new SimpleContainer(SIZE));
            for (int s = 0; s < SIZE; s++) c.setItem(s, items.get(s));
            r.packs.put(t.getUUID("Guard"), c);
        }
        return r;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, SimpleContainer> e : packs.entrySet()) {
            if (e.getValue().isEmpty()) continue;
            CompoundTag t = new CompoundTag();
            t.putUUID("Guard", e.getKey());
            NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
            for (int s = 0; s < SIZE; s++) items.set(s, e.getValue().getItem(s));
            ContainerHelper.saveAllItems(t, items, registries);
            list.add(t);
        }
        tag.put("Packs", list);
        return tag;
    }

    private SimpleContainer track(SimpleContainer c) {
        c.addListener(changed -> setDirty());
        return c;
    }

    /** The guard's pack, created empty on first use. */
    public SimpleContainer of(UUID guard) {
        return packs.computeIfAbsent(guard, k -> track(new SimpleContainer(SIZE)));
    }

    /** The pack only if it exists (no creation). */
    public SimpleContainer peek(UUID guard) {
        return packs.get(guard);
    }

    public SimpleContainer remove(UUID guard) {
        SimpleContainer c = packs.remove(guard);
        if (c != null) setDirty();
        return c;
    }
}
