package net.hfstack.rallyguard.contract;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * E1-C: headquarters as their own records (id, owner, name, position, dimension),
 * permanent until the commander deletes them. A squad points at one by
 * {@code SquadRegistry.Squad#hqId}: its own by default (claiming a banner for a squad
 * without one creates one named after it), shareable on purpose from the Ledger, never a
 * common one by default. Breaking the banner doesn't delete the HQ - it keeps its last
 * position. World-global, overworld-backed, like every other Rally registry.
 */
public final class HqRegistry extends SavedData {

    private static final String DATA_ID = "rallyguard_hqs";

    public static final class Hq {
        public final UUID id;
        public final UUID owner;
        public String name;
        public BlockPos pos;
        public String dimension;

        Hq(UUID id, UUID owner, String name, BlockPos pos, String dimension) {
            this.id = id;
            this.owner = owner;
            this.name = name;
            this.pos = pos;
            this.dimension = dimension;
        }
    }

    private final Map<UUID, Hq> hqs = new LinkedHashMap<>();
    /** The HQ each commander picked in the Ledger (a banner they sneak-command links to it). */
    private static final Map<UUID, UUID> SELECTED = new HashMap<>();

    public static HqRegistry get(ServerLevel anyLevel) {
        return anyLevel.getServer().overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(HqRegistry::new, HqRegistry::load, null), DATA_ID);
    }

    private static HqRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        HqRegistry r = new HqRegistry();
        ListTag list = tag.getList("Hqs", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag t = list.getCompound(i);
            Hq hq = new Hq(t.getUUID("Id"), t.getUUID("Owner"), t.getString("Name"),
                    new BlockPos(t.getInt("X"), t.getInt("Y"), t.getInt("Z")), t.getString("Dim"));
            r.hqs.put(hq.id, hq);
        }
        return r;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Hq hq : hqs.values()) {
            CompoundTag t = new CompoundTag();
            t.putUUID("Id", hq.id);
            t.putUUID("Owner", hq.owner);
            t.putString("Name", hq.name);
            t.putInt("X", hq.pos.getX());
            t.putInt("Y", hq.pos.getY());
            t.putInt("Z", hq.pos.getZ());
            t.putString("Dim", hq.dimension);
            list.add(t);
        }
        tag.put("Hqs", list);
        return tag;
    }

    public Hq create(UUID owner, String name, String dimension, BlockPos pos) {
        Hq hq = new Hq(UUID.randomUUID(), owner, name, pos.immutable(), dimension);
        hqs.put(hq.id, hq);
        setDirty();
        return hq;
    }

    @Nullable
    public Hq byId(@Nullable UUID id) {
        return id == null ? null : hqs.get(id);
    }

    /** The HQ if it exists AND belongs to {@code owner}. */
    @Nullable
    public Hq owned(UUID owner, @Nullable UUID id) {
        Hq hq = byId(id);
        return hq != null && hq.owner.equals(owner) ? hq : null;
    }

    public List<Hq> ownedBy(UUID owner) {
        List<Hq> out = new ArrayList<>();
        for (Hq hq : hqs.values()) if (hq.owner.equals(owner)) out.add(hq);
        return out;
    }

    public void move(Hq hq, String dimension, BlockPos pos) {
        hq.pos = pos.immutable();
        hq.dimension = dimension;
        setDirty();
    }

    public void rename(Hq hq, String name) {
        hq.name = name;
        setDirty();
    }

    /** Deletes the HQ; squads pointing at it are left without one (by the caller). */
    public void delete(Hq hq) {
        hqs.remove(hq.id);
        SELECTED.values().removeIf(hq.id::equals);
        setDirty();
    }

    public static void select(UUID commander, @Nullable UUID hqId) {
        if (hqId == null) SELECTED.remove(commander); else SELECTED.put(commander, hqId);
    }

    @Nullable
    public static UUID selected(UUID commander) {
        return SELECTED.get(commander);
    }

    public static void clearSelections() {
        SELECTED.clear();
    }
}
