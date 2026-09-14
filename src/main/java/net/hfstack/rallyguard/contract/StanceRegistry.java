package net.hfstack.rallyguard.contract;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Phase 3C: durable per-guard {@link Stance}, keyed by guard UUID. Same {@code SavedData}
 * shape as {@link SquadRegistry} and {@link GuardRoster} - a world-global registry
 * attached to the OVERWORLD's data storage regardless of which dimension a guard is in
 * (see {@link SquadRegistry}'s javadoc for the full verification history behind this
 * shape; not re-verified here since it's already proven twice in this project).
 * <p>
 * A guard with no entry here is {@link Stance#DEFENSIVE} - the implicit default, matching
 * Guard Villagers' own out-of-the-box behavior. Deliberately NOT storing an explicit
 * {@code DEFENSIVE} entry for every guard: {@link #setStance} removes the map entry
 * entirely when set back to {@code DEFENSIVE}, so a world where nobody has touched
 * stances yet (or where everyone reverts to the default) keeps this file essentially
 * empty, and {@link #stanceOf} treats "no entry" and "explicitly set to DEFENSIVE" as the
 * same thing on purpose.
 */
public final class StanceRegistry extends SavedData {

    private static final String DATA_ID = "rallyguard_stances";

    private final Map<UUID, Stance> stances = new LinkedHashMap<>();

    public static StanceRegistry get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(StanceRegistry::new, StanceRegistry::load, null),
                DATA_ID);
    }

    private static StanceRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        StanceRegistry registry = new StanceRegistry();
        ListTag list = tag.getList("Entries", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entryTag = list.getCompound(i);
            UUID guardId = entryTag.getUUID("GuardId");
            Stance stance = parseOrDefault(entryTag.getString("Stance"));
            if (stance != Stance.DEFENSIVE) {
                registry.stances.put(guardId, stance);
            }
        }
        return registry;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Stance> e : stances.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("GuardId", e.getKey());
            entryTag.putString("Stance", e.getValue().name());
            list.add(entryTag);
        }
        tag.put("Entries", list);
        return tag;
    }

    // ==== Query/mutation API ====

    /** Never returns null - a guard with no entry is {@link Stance#DEFENSIVE}. */
    public Stance stanceOf(UUID guardId) {
        return stances.getOrDefault(guardId, Stance.DEFENSIVE);
    }

    public void setStance(UUID guardId, Stance stance) {
        boolean changed;
        if (stance == Stance.DEFENSIVE) {
            changed = stances.remove(guardId) != null;
        } else {
            Stance previous = stances.put(guardId, stance);
            changed = previous != stance;
        }
        if (changed) {
            setDirty();
        }
    }

    /** Drops a guard's stance entry entirely - called on death, same reasoning as
     *  {@link GuardRoster#remove} (a dead guard shouldn't keep taking up space here). */
    public void remove(UUID guardId) {
        if (stances.remove(guardId) != null) {
            setDirty();
        }
    }

    private static Stance parseOrDefault(String raw) {
        try {
            return Stance.valueOf(raw);
        } catch (IllegalArgumentException e) {
            // Unknown/corrupt value (e.g. an older save from before a stance was
            // renamed) - fall back to the safe default rather than crash loading the
            // whole registry over one bad entry.
            return Stance.DEFENSIVE;
        }
    }
}
