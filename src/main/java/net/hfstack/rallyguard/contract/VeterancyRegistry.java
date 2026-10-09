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
 * E2A block 2 / E3A block V - every guard's veterancy, saved per guard UUID in the
 * overworld's data like the roster ({@code rallyguard_veterancy}). Only guards that have
 * earned something have an entry.
 * <ul>
 *     <li>{@code xp} - rank XP (combat + command), decides the rank 0..5;</li>
 *     <li>{@code commandXp} - the part earned by leading (V2), also feeds the leadership score;</li>
 *     <li>{@code paths} - after rank 5 (V0): survival, command, mount bond, history, each with
 *     diminishing returns ({@link Path});</li>
 *     <li>{@code weaponXp} - weapon mastery per archetype (A8);</li>
 *     <li>history counters, fights per enemy type (≤ {@link #MAX_TYPES}), traits, and a short
 *     story ({@link #MAX_STORY} lines, V7).</li>
 * </ul>
 */
public final class VeterancyRegistry extends SavedData {

    private static final String DATA_ID = "rallyguard_veterancy";
    public static final int MAX_TYPES = 64;
    public static final int MAX_STORY = 12;

    /** E3A V0: the four non-weapon progress paths after rank 5 (weapon mastery is {@code weaponXp}). */
    public enum Path { SURVIVAL, COMMAND, MOUNT, HISTORY }

    /** One guard's record. Mutate through {@link #edit} so it gets saved. */
    public static final class Record {
        public double xp, commandXp;
        public int rank;
        public int killsMelee, killsRanged, killsGun;
        public int nearDeath, battles, totems, reliefsTaken;
        public int secondsMounted, secondsWithCommander;
        public final double[] paths = new double[Path.values().length];
        public final Map<String, Double> weaponXp = new HashMap<>();
        public final Map<String, Integer> enemyTypes = new HashMap<>();
        /** E3B V2: threat memory - enemy type -> {dread 0..1, game tick of the last hit} (≤ 16 types, fades). */
        public final Map<String, double[]> dread = new HashMap<>();
        public final List<String> traits = new ArrayList<>();
        /** "gameDay|kind|arg" lines, newest last. */
        public final List<String> story = new ArrayList<>();

        public void addStory(long gameDay, String kind, String arg) {
            story.add(gameDay + "|" + kind + "|" + arg.replace("|", "/"));
            while (story.size() > MAX_STORY) story.remove(0);
        }

        CompoundTag save() {
            CompoundTag t = new CompoundTag();
            t.putDouble("Xp", xp);
            t.putDouble("CommandXp", commandXp);
            t.putInt("Rank", rank);
            t.putIntArray("Counts", new int[]{killsMelee, killsRanged, killsGun, nearDeath, battles, totems,
                    reliefsTaken, secondsMounted, secondsWithCommander});
            CompoundTag p = new CompoundTag();
            for (Path path : Path.values()) p.putDouble(path.name(), paths[path.ordinal()]);
            t.put("Paths", p);
            CompoundTag w = new CompoundTag();
            for (Map.Entry<String, Double> e : weaponXp.entrySet()) w.putDouble(e.getKey(), e.getValue());
            t.put("Weapons", w);
            CompoundTag types = new CompoundTag();
            for (Map.Entry<String, Integer> e : enemyTypes.entrySet()) types.putInt(e.getKey(), e.getValue());
            t.put("Types", types);
            CompoundTag dr = new CompoundTag();
            for (Map.Entry<String, double[]> e : dread.entrySet()) {
                CompoundTag one = new CompoundTag();
                one.putDouble("V", e.getValue()[0]);
                one.putDouble("T", e.getValue()[1]);
                dr.put(e.getKey(), one);
            }
            t.put("Dread", dr);
            t.put("Traits", strings(traits));
            t.put("Story", strings(story));
            return t;
        }

        static Record load(CompoundTag t) {
            Record r = new Record();
            r.xp = t.getDouble("Xp");
            r.commandXp = t.getDouble("CommandXp");
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
            CompoundTag p = t.getCompound("Paths");
            for (Path path : Path.values()) r.paths[path.ordinal()] = p.getDouble(path.name());
            CompoundTag w = t.getCompound("Weapons");
            for (String k : w.getAllKeys()) r.weaponXp.put(k, w.getDouble(k));
            CompoundTag types = t.getCompound("Types");
            for (String k : types.getAllKeys()) r.enemyTypes.put(k, types.getInt(k));
            CompoundTag dr = t.getCompound("Dread");
            for (String k : dr.getAllKeys()) {
                CompoundTag one = dr.getCompound(k);
                r.dread.put(k, new double[]{one.getDouble("V"), one.getDouble("T")});
            }
            readStrings(t.getList("Traits", Tag.TAG_STRING), r.traits);
            readStrings(t.getList("Story", Tag.TAG_STRING), r.story);
            return r;
        }

        private static ListTag strings(List<String> values) {
            ListTag list = new ListTag();
            for (String s : values) list.add(StringTag.valueOf(s));
            return list;
        }

        private static void readStrings(ListTag list, List<String> into) {
            for (int i = 0; i < list.size(); i++) into.add(list.getString(i));
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
