package net.hfstack.rallyguard.contract;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.HashMap;
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
    /** Phase 3H.1 scale: guard -> its squad, so lookups don't scan every squad (the
     *  Ledger asks once per guard; with thousands of guards and squads that added up). */
    private final Map<UUID, Squad> byMember = new HashMap<>();

    public static final class Squad {
        public final UUID id;
        public final UUID owner;
        public String name;
        public final Set<UUID> members = new LinkedHashSet<>();
        /** Phase 3H: optional sub-group ("unit") per member - e.g. squad "Pueblo1" with
         *  units "Arqueros", "Caballeria"... A member with no entry is in the squad but in
         *  no unit. Written with a "Squad/Unit" path (see {@link #assign}). */
        public final Map<UUID, String> units = new LinkedHashMap<>();
        /** E1-C: the squad's HQ ({@link HqRegistry}); null = none. */
        @Nullable
        public UUID hqId;
        /** Phase 3H.1's inline HQ, only read to migrate it into {@link HqRegistry}
         *  ({@code order.RetreatPlanner#hqOf}). */
        @Nullable
        public BlockPos hq;
        public String hqDimension = "";

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
            ListTag unitList = squadTag.getList("Units", Tag.TAG_COMPOUND); // absent before 3H
            for (int j = 0; j < unitList.size(); j++) {
                CompoundTag unitTag = unitList.getCompound(j);
                UUID member = UUID.fromString(unitTag.getString("Guard"));
                if (squad.members.contains(member)) {
                    squad.units.put(member, unitTag.getString("Unit"));
                }
            }

            if (squadTag.hasUUID("HqId")) squad.hqId = squadTag.getUUID("HqId"); // E1
            if (squadTag.contains("HqDim")) { // 3H.1 inline HQ, migrated on first use
                squad.hq = new BlockPos(squadTag.getInt("HqX"), squadTag.getInt("HqY"), squadTag.getInt("HqZ"));
                squad.hqDimension = squadTag.getString("HqDim");
            }

            registry.squads.put(id, squad);
            for (UUID member : squad.members) registry.byMember.put(member, squad);
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

            ListTag unitList = new ListTag();
            for (Map.Entry<UUID, String> unit : squad.units.entrySet()) {
                CompoundTag unitTag = new CompoundTag();
                unitTag.putString("Guard", unit.getKey().toString());
                unitTag.putString("Unit", unit.getValue());
                unitList.add(unitTag);
            }
            squadTag.put("Units", unitList);
            if (squad.hqId != null) squadTag.putUUID("HqId", squad.hqId);
            if (squad.hq != null) {
                squadTag.putInt("HqX", squad.hq.getX());
                squadTag.putInt("HqY", squad.hq.getY());
                squadTag.putInt("HqZ", squad.hq.getZ());
                squadTag.putString("HqDim", squad.hqDimension);
            }

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
        return Optional.ofNullable(byMember.get(guardId));
    }

    /** E1-C: points this owner's squad at an HQ (null = none). */
    public boolean setHqId(UUID owner, String squadName, @Nullable UUID hqId) {
        Squad squad = findByOwnerAndName(owner, splitPath(squadName)[0]);
        if (squad == null) return false;
        squad.hqId = hqId;
        squad.hq = null;
        squad.hqDimension = "";
        setDirty();
        return true;
    }

    /** Squads (of this owner) using an HQ. */
    public List<Squad> usingHq(UUID owner, UUID hqId) {
        return squads.values().stream().filter(s -> s.owner.equals(owner) && hqId.equals(s.hqId))
                .collect(Collectors.toList());
    }

    /** Sets (or with {@code pos == null} clears) the legacy inline HQ (3H.1). */
    public boolean setHq(UUID owner, String squadName, String dimension, @Nullable BlockPos pos) {
        Squad squad = findByOwnerAndName(owner, splitPath(squadName)[0]);
        if (squad == null) return false;
        squad.hq = pos;
        squad.hqDimension = pos == null ? "" : dimension;
        setDirty();
        return true;
    }

    /** Separator between squad and unit in a group path ("Pueblo1/Arqueros"). */
    public static final String PATH_SEPARATOR = "/";

    /** Splits a group path into {squad, unit} (unit "" when none), both trimmed. Only the
     *  first separator counts: groups are two levels deep, never more. */
    public static String[] splitPath(String path) {
        String p = path == null ? "" : path.trim();
        int i = p.indexOf(PATH_SEPARATOR);
        if (i < 0) return new String[]{p, ""};
        return new String[]{p.substring(0, i).trim(), p.substring(i + 1).replace(PATH_SEPARATOR, " ").trim()};
    }

    /** "Squad" or "Squad/Unit" for this guard, if it is in a squad. */
    public Optional<String> pathOf(UUID guardId) {
        return squadOf(guardId).map(squad -> {
            String unit = squad.units.get(guardId);
            return unit == null || unit.isEmpty() ? squad.name : squad.name + PATH_SEPARATOR + unit;
        });
    }

    /** Members of "Squad" (every unit included) or of "Squad/Unit" only. */
    public Set<UUID> membersOf(UUID owner, String groupPath) {
        String[] path = splitPath(groupPath);
        Squad squad = findByOwnerAndName(owner, path[0]);
        if (squad == null) return Set.of();
        if (path[1].isEmpty()) return new LinkedHashSet<>(squad.members);
        Set<UUID> out = new LinkedHashSet<>();
        squad.units.forEach((guard, unit) -> {
            if (unit.equalsIgnoreCase(path[1])) out.add(guard);
        });
        return out;
    }

    public int countMembers(UUID owner, String groupPath) {
        return membersOf(owner, groupPath).size();
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
        Squad squad = byMember.remove(guardId);
        if (squad != null && squad.members.remove(guardId)) {
            squad.units.remove(guardId);
            setDirty();
        }
    }

    public enum RenameResult { RENAMED, NOT_FOUND, NAME_TAKEN }

    /**
     * Phase 3G squad management: renames one of this owner's squads (case-insensitive
     * match on the old name). Refuses a new name another of the owner's squads already
     * uses, rather than silently merging two squads.
     */
    public RenameResult rename(UUID owner, String oldPath, String newName) {
        String[] path = splitPath(oldPath);
        Squad squad = findByOwnerAndName(owner, path[0]);
        if (squad == null) return RenameResult.NOT_FOUND;
        if (!path[1].isEmpty()) {
            // Renaming a unit inside the squad.
            String newUnit = splitPath(newName)[1].isEmpty() ? newName.trim() : splitPath(newName)[1];
            boolean found = false;
            for (Map.Entry<UUID, String> e : squad.units.entrySet()) {
                if (e.getValue().equalsIgnoreCase(path[1])) {
                    e.setValue(newUnit);
                    found = true;
                }
            }
            if (found) setDirty();
            return found ? RenameResult.RENAMED : RenameResult.NOT_FOUND;
        }
        newName = splitPath(newName)[0];
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
    public Set<UUID> disband(UUID owner, String groupPath) {
        String[] path = splitPath(groupPath);
        Squad squad = findByOwnerAndName(owner, path[0]);
        if (squad == null) return Set.of();
        if (!path[1].isEmpty()) {
            // Dissolving a unit: its members stay in the squad, just without a unit.
            Set<UUID> former = new LinkedHashSet<>();
            squad.units.entrySet().removeIf(e -> {
                boolean match = e.getValue().equalsIgnoreCase(path[1]);
                if (match) former.add(e.getKey());
                return match;
            });
            if (!former.isEmpty()) setDirty();
            return former;
        }
        squads.remove(squad.id);
        for (UUID member : squad.members) byMember.remove(member);
        setDirty();
        return new LinkedHashSet<>(squad.members);
    }

    /**
     * Moves a guard into the named squad for this owner, creating the squad on first
     * use. Always removes the guard from any squad it was previously in first, so
     * membership stays exclusive. Phase 3H: {@code groupPath} may be "Squad/Unit" to also
     * put the guard in a unit (sub-group) of that squad.
     */
    public void assign(UUID guardId, UUID owner, String groupPath) {
        String[] path = splitPath(groupPath);
        if (path[0].isEmpty()) return;
        unassign(guardId);

        Squad squad = findByOwnerAndName(owner, path[0]);
        if (squad == null) {
            squad = new Squad(UUID.randomUUID(), owner, path[0]);
            squads.put(squad.id, squad);
        }
        squad.members.add(guardId);
        byMember.put(guardId, squad);
        if (!path[1].isEmpty()) {
            // Reuse the exact spelling of an existing unit so "arqueros" joins "Arqueros".
            String unit = path[1];
            for (String existing : squad.units.values()) {
                if (existing.equalsIgnoreCase(unit)) {
                    unit = existing;
                    break;
                }
            }
            squad.units.put(guardId, unit);
        }
        setDirty();
    }
}
