package net.hfstack.rallyguard.contract;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * E2A block 2 - every guard's veterancy, saved per guard UUID in the overworld's data like
 * the roster ({@code rallyguard_veterancy}). Only guards that have earned something have an
 * entry. History counters are cheap ints; {@code enemyTypes} (fights per enemy type, for
 * diminishing returns and variety) is capped at {@link #MAX_TYPES} entries per guard.
 */
public final class VeterancyRegistry extends SavedData {

    private static final String DATA_ID = "rallyguard_veterancy";
    public static final int MAX_TYPES = 64;

    /** One guard's record. Mutate through the registry's methods so it gets saved. */
    public static final class Record {
        public double xp;
        public int rank;
        public int killsMelee, killsRanged, killsGun;
        public int nearDeath, battles, totems, reliefsTaken;
        public int secondsMounted, secondsWithCommander;
        public final Map<String, Integer> enemyTypes = new HashMap<>();
        public final List<String> traits = new ArrayList<>();

        CompoundTag save() {
            CompoundTag t = new CompoundTag();
            t.putDouble("Xp", xp);
            t.putInt("Rank", rank);
            t.putIntArray("Counts", new int[]{killsMelee, killsRanged, killsGun, nearDeath, battles, totems,
                    reliefsTaken, secondsMounted, secondsWithCommander});
            CompoundTag types = new CompoundTag();
            for (Map.Entry<String, Integer> e : enemyTypes.entrySet()) types.putInt(e.getKey(), e.getValue());
            t.put("Types", types);
            ListTag tr = new ListTag();
            for (String s : traits) tr.add(StringTag.valueOf(s));
            t.put("Traits", tr);
            return t;
        }

        static Record load(CompoundTag t) {
            Record r = new Record();
            r.xp = t.getDouble("Xp");
            r.rank = t.getInt("Rank");
            int[] c = t.getIntArray("Counts");
            int[] f = new int[9];
            System.arraycopy(c, 0, f, 0, Math.min(c.length, f.length));
            r.killsMelee = f[0];
            r.killsRanged = f[1];
            r.killsGun = f[2];
            r.nearDeath = f[3];
            r.battles = f[4];
            r.totems = f[5];
            r.reliefsTaken = f[6];
            r.secondsMounted = f[7];
            r.secondsWithCommander = f[8];
            CompoundTag types = t.getCompound("Types");
            for (String k : types.getAllKeys()) r.enemyTypes.put(k, types.getInt(k));
            ListTag tr = t.getList("Traits", Tag.TAG_STRING);
            for (int i = 0; i < tr.size(); i++) r.traits.add(tr.getString(i));
            return r;
        }
    }

    private final Map<UUID, Record> records = new LinkedHashMap<>();

    public static VeterancyRegistry get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(VeterancyRegistry::new, VeterancyRegistry::load, null),
                DATA_ID);
    }

    private static VeterancyRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        VeterancyRegistry registry = new VeterancyRegistry();
        ListTag list = tag.getList("Entries", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag e = list.getCompound(i);
            registry.records.put(e.getUUID("GuardId"), Record.load(e.getCompound("Record")));
        }
        return registry;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Record> e : records.entrySet()) {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("GuardId", e.getKey());
            entry.put("Record", e.getValue().save());
            list.add(entry);
        }
        tag.put("Entries", list);
        return tag;
    }

    /** The guard's record, or null if it has none yet. */
    public Record find(UUID guardId) {
        return records.get(guardId);
    }

    /** The guard's record, created on first use; marks the data dirty (caller mutates it). */
    public Record edit(UUID guardId) {
        setDirty();
        return records.computeIfAbsent(guardId, k -> new Record());
    }

    public int rankOf(UUID guardId) {
        Record r = records.get(guardId);
        return r == null ? 0 : r.rank;
    }

    public void remove(UUID guardId) {
        if (records.remove(guardId) != null) setDirty();
    }
}
