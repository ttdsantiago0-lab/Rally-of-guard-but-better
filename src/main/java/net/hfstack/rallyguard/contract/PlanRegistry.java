package net.hfstack.rallyguard.contract;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * E3A A4 - Battle Planner plans saved per player ({@code rallyguard_plans}): name -> the plan
 * as JSON (validated by {@code force.Plans} when saved and when executed). At most
 * {@link #MAX_PLANS} per player, {@link #MAX_JSON} characters each.
 */
public final class PlanRegistry extends SavedData {

    private static final String DATA_ID = "rallyguard_plans";
    public static final int MAX_PLANS = 32;
    public static final int MAX_JSON = 16384;

    private final Map<UUID, Map<String, String>> plans = new LinkedHashMap<>();

    public static PlanRegistry get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(PlanRegistry::new, PlanRegistry::load, null), DATA_ID);
    }

    private static PlanRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        PlanRegistry r = new PlanRegistry();
        ListTag list = tag.getList("Plans", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag t = list.getCompound(i);
            r.plans.computeIfAbsent(t.getUUID("Owner"), k -> new LinkedHashMap<>()).put(t.getString("Name"), t.getString("Json"));
        }
        return r;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        plans.forEach((owner, byName) -> byName.forEach((name, json) -> {
            CompoundTag t = new CompoundTag();
            t.putUUID("Owner", owner);
            t.putString("Name", name);
            t.putString("Json", json);
            list.add(t);
        }));
        tag.put("Plans", list);
        return tag;
    }

    /** Saves (or replaces) a plan; false if the player is at the limit or the JSON is too long. */
    public boolean put(UUID owner, String name, String json) {
        Map<String, String> mine = plans.computeIfAbsent(owner, k -> new LinkedHashMap<>());
        if (json.length() > MAX_JSON || (!mine.containsKey(name) && mine.size() >= MAX_PLANS)) return false;
        mine.put(name, json);
        setDirty();
        return true;
    }

    public boolean remove(UUID owner, String name) {
        Map<String, String> mine = plans.get(owner);
        boolean removed = mine != null && mine.remove(name) != null;
        if (removed) setDirty();
        return removed;
    }

    public String find(UUID owner, String name) {
        Map<String, String> mine = plans.get(owner);
        if (mine == null) return null;
        for (Map.Entry<String, String> e : mine.entrySet()) if (e.getKey().equalsIgnoreCase(name)) return e.getValue();
        return null;
    }

    public List<String> names(UUID owner) {
        Map<String, String> mine = plans.get(owner);
        List<String> out = mine == null ? new ArrayList<>() : new ArrayList<>(mine.keySet());
        out.sort(String.CASE_INSENSITIVE_ORDER);
        return out;
    }

    static String norm(String s) {
        return s.toLowerCase(Locale.ROOT);
    }
}
