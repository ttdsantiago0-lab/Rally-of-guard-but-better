package net.hfstack.rallyguard.contract;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Phase 3A: durable squad membership, independent of whether a guard is currently loaded.
 * A {@link Squad} is just a name plus a set of member guard UUIDs, owned by one player.
 * Squads are NOT tied to a village/location - {@code Squad} itself carries no home point
 * (that's Phase 3E) and no stance (Phase 3C); this class only answers "who's in this
 * squad" and "which squad is this guard in."
 * <p>
 * Stored via vanilla's {@code SavedData} (confirmed real 1.21.1 API via mappings.dev,
 * since Minecraft's SavedData was reworked to a Codec-based {@code SavedDataType} in a
 * LATER version - the locally cloned {@code /tmp/neoforge-src} used for Phase 2H's event
 * verification turned out to be on NeoForge's {@code 26.2.x} branch, several Minecraft
 * versions ahead of this project's actual 1.21.1/NeoForge 21.1.215 target, so it was
 * NOT used here; this class's shape - abstract {@code save(CompoundTag,
 * HolderLookup.Provider)}, the {@code SavedData.Factory<T>(Supplier<T> constructor,
 * BiFunction<CompoundTag, HolderLookup.Provider, T> deserializer, DataFixTypes type)}
 * record, and {@code DimensionDataStorage#computeIfAbsent(Factory<T>, String)} - is
 * confirmed against mappings.dev's 1.21.1-pinned pages instead). Attached to the
 * OVERWORLD's data storage rather than whatever dimension the player happens to be in,
 * so squads stay a single, world-global registry regardless of which dimension a guard
 * or its owner is standing in.
 * <p>
 * Members are stored by guard UUID (not runtime entity id, which is only valid for the
 * lifetime of one loaded instance) so membership survives a guard unloading/reloading -
 * this is what lets Phase 3B later show squad membership for guards that aren't
 * currently loaded at all.
 */
public final class SquadRegistry extends SavedData {

    private static final String DATA_ID = "rallyguard_squads";

    private final Map<UUID, Squad> squads = new LinkedHashMap<>();

    public static final class Squad {
        public final UUID id;
        public final UUID owner;
        public String name;
        public final Set<UUID> members = new LinkedHashSet<>();

        private Squad(UUID id, UUID owner, String name) {
            this.id = id;
            this.owner = owner;
            this.name = name;
        }
    }

    /**
     * Fetches (creating on first use) the single world-global squad registry. Always
     * resolves to the OVERWORLD's data storage via {@code level.getServer().overworld()},
     * regardless of which dimension {@code anyLevel} is - see the class javadoc.
     */
    public static SquadRegistry get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(SquadRegistry::new, SquadRegistry::load, null),
                DATA_ID);
    }

    private static SquadRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        SquadRegistry registry = new SquadRegistry();
        ListTag squadList = tag.getList("Squads", Tag.TAG_COMPOUND);
        for (int i = 0; i < squadList.size(); i++) {
            CompoundTag squadTag = squadList.getCompound(i);
            UUID id = squadTag.getUUID("Id");
            UUID owner = squadTag.getUUID("Owner");
            String name = squadTag.getString("Name");
            Squad squad = new Squad(id, owner, name);

            ListTag memberList = squadTag.getList("Members", Tag.TAG_STRING);
            for (int j = 0; j < memberList.size(); j++) {
                squad.members.add(UUID.fromString(memberList.getString(j)));
            }

            registry.squads.put(id, squad);
        }
        return registry;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag squadList = new ListTag();
        for (Squad squad : squads.values()) {
            CompoundTag squadTag = new CompoundTag();
            squadTag.putUUID("Id", squad.id);
            squadTag.putUUID("Owner", squad.owner);
            squadTag.putString("Name", squad.name);

            ListTag memberList = new ListTag();
            for (UUID member : squad.members) {
                memberList.add(StringTag.valueOf(member.toString()));
            }
            squadTag.put("Members", memberList);

            squadList.add(squadTag);
        }
        tag.put("Squads", squadList);
        return tag;
    }

    // ==== Query/mutation API used by GuardCommandNetworking ====

    /**
     * Which squad (if any) a guard currently belongs to. A guard is in at most one
     * squad at a time - assigning it to a new one implicitly removes it from any other.
     */
    public Optional<Squad> squadOf(UUID guardId) {
        return squads.values().stream().filter(s -> s.members.contains(guardId)).findFirst();
    }

    public List<Squad> squadsOwnedBy(UUID owner) {
        return squads.values().stream().filter(s -> s.owner.equals(owner))
                .collect(Collectors.toList());
    }

    private Squad findByOwnerAndName(UUID owner, String name) {
        return squads.values().stream()
                .filter(s -> s.owner.equals(owner) && s.name.equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    /**
     * Removes a guard from whatever squad it's currently in, if any. Does not delete a
     * squad that becomes empty as a result - an empty squad is still meaningful once
     * Phase 3C/3E give it a stance/home point of its own.
     */
    public void unassign(UUID guardId) {
        for (Squad squad : squads.values()) {
            if (squad.members.remove(guardId)) {
                setDirty();
                return;
            }
        }
    }

    public enum RenameResult { RENAMED, NOT_FOUND, NAME_TAKEN }

    /**
     * Phase 3G squad management: renames one of this owner's squads (case-insensitive
     * match on the old name). Refuses a new name another of the owner's squads already
     * uses, rather than silently merging two squads.
     */
    public RenameResult rename(UUID owner, String oldName, String newName) {
        Squad squad = findByOwnerAndName(owner, oldName);
        if (squad == null) return RenameResult.NOT_FOUND;
        Squad clash = findByOwnerAndName(owner, newName);
        if (clash != null && clash != squad) return RenameResult.NAME_TAKEN;
        squad.name = newName;
        setDirty();
        return RenameResult.RENAMED;
    }

    /**
     * Phase 3G squad management: dissolves one of this owner's squads. Its guards are
     * not touched otherwise - they simply have no squad any more. Returns the former
     * members (empty if there was no such squad).
     */
    public Set<UUID> disband(UUID owner, String name) {
        Squad squad = findByOwnerAndName(owner, name);
        if (squad == null) return Set.of();
        squads.remove(squad.id);
        setDirty();
        return new LinkedHashSet<>(squad.members);
    }

    /**
     * Moves a guard into the named squad for this owner, creating the squad on first
     * use. Always removes the guard from any squad it was previously in first, so
     * membership stays exclusive.
     */
    public void assign(UUID guardId, UUID owner, String squadName) {
        unassign(guardId);

        Squad squad = findByOwnerAndName(owner, squadName);
        if (squad == null) {
            squad = new Squad(UUID.randomUUID(), owner, squadName);
            squads.put(squad.id, squad);
        }
        squad.members.add(guardId);
        setDirty();
    }
}
