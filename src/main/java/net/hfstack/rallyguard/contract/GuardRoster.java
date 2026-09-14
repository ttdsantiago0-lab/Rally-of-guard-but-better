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
 * Phase 3B: a durable "last known state" snapshot per guard, independent of whether the
 * guard is currently loaded. This is what lets the Commander's Ledger list guard count,
 * name, mount, armor condition and patrol state "from anywhere" (PORTING_PLAN.md's own
 * phrasing for this phase) instead of only guards found by
 * {@code ServerLevel#getEntities(...)} in the player's currently-loaded chunks, which is
 * how {@code GuardCommandNetworking#sendGuardList} worked before this phase (Phase 3A
 * and earlier).
 * <p>
 * Deliberately NOT a source of truth for anything actionable - it is a read-only cache
 * for display only. Actions (Summon, toggle Patrol, Assign Squad) still require the
 * guard to actually be loaded (a live {@code Guard} instance, found the same way
 * {@link #recordSnapshot} feeds this cache) - a roster-only entry has no way to be acted
 * on without genuinely loading that chunk, which this phase does not attempt. Squad
 * membership itself is NOT duplicated here: {@link SquadRegistry} already has its own
 * durable-by-UUID design (Phase 3A) and stays the single source of truth for "which
 * squad is this guard in" - the Ledger looks that up separately.
 * <p>
 * Same {@code SavedData} pattern and same verification history as {@link SquadRegistry}
 * (see that class's javadoc for the full rationale on why THIS project's 1.21.1
 * {@code SavedData} shape - the {@code SavedData.Factory<T>(constructor, deserializer,
 * DataFixTypes)} record, {@code DimensionDataStorage#computeIfAbsent} - was verified via
 * mappings.dev rather than the locally-cloned {@code /tmp/neoforge-src}, which turned out
 * to be several Minecraft versions ahead). Attached to the OVERWORLD's data storage for
 * the same reason: one world-global registry regardless of which dimension a guard or
 * its owner happens to be in.
 * <p>
 * A snapshot only ever gets fresher when a guard is actually observed loaded (see
 * {@link #recordSnapshot}'s call sites: {@code HireGuardMenu} at hire time, and
 * {@code GuardCommandNetworking#sendGuardList} every time that owner's Ledger is opened
 * while the guard happens to be loaded) - there is no periodic background scan. This is
 * a deliberate, documented trade-off: a guard's cached mount/armor can be stale until the
 * next time it's loaded while its owner opens the Ledger, which is an acceptable cost for
 * "can I see it at all," the actual ask, versus "is this number live-updating," which it
 * was never asked to be.
 */
public final class GuardRoster extends SavedData {

    private static final String DATA_ID = "rallyguard_roster";

    private final Map<UUID, Entry> entries = new LinkedHashMap<>();

    /**
     * One guard's last-observed state. {@code mount} is blank for "not mounted" (or
     * "unknown, never observed" - the two are indistinguishable here, which is fine
     * since an unmounted guard is the common case and worth defaulting to). {@code armor}
     * is a compact, deliberately unlocalized wire format - four comma-separated values
     * for HEAD, CHEST, LEGS, FEET in that fixed order, each either {@code "-"} (slot
     * empty) or a 0-100 integer percent of durability remaining - parsed and localized
     * client-side ({@code GuardCommandScreen}) rather than pre-formatted into an English
     * sentence server-side, so a future translation pass doesn't need to touch this
     * class.
     */
    public static final class Entry {
        public final UUID owner;
        public String name;
        public boolean patrolling;
        public String mount;
        public String armor;
        public long lastSeenTick;

        private Entry(UUID owner, String name, boolean patrolling, String mount, String armor, long lastSeenTick) {
            this.owner = owner;
            this.name = name;
            this.patrolling = patrolling;
            this.mount = mount;
            this.armor = armor;
            this.lastSeenTick = lastSeenTick;
        }
    }

    /**
     * Fetches (creating on first use) the single world-global roster, always resolved
     * against the OVERWORLD's data storage regardless of which dimension
     * {@code anyLevel} is - identical pattern to {@link SquadRegistry#get}.
     */
    public static GuardRoster get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(GuardRoster::new, GuardRoster::load, null),
                DATA_ID);
    }

    private static GuardRoster load(CompoundTag tag, HolderLookup.Provider registries) {
        GuardRoster roster = new GuardRoster();
        ListTag list = tag.getList("Entries", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entryTag = list.getCompound(i);
            UUID guardId = entryTag.getUUID("GuardId");
            UUID owner = entryTag.getUUID("Owner");
            String name = entryTag.getString("Name");
            boolean patrolling = entryTag.getBoolean("Patrolling");
            String mount = entryTag.getString("Mount");
            String armor = entryTag.getString("Armor");
            long lastSeenTick = entryTag.getLong("LastSeenTick");
            roster.entries.put(guardId, new Entry(owner, name, patrolling, mount, armor, lastSeenTick));
        }
        return roster;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Entry> e : entries.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("GuardId", e.getKey());
            entryTag.putUUID("Owner", e.getValue().owner);
            entryTag.putString("Name", e.getValue().name);
            entryTag.putBoolean("Patrolling", e.getValue().patrolling);
            entryTag.putString("Mount", e.getValue().mount);
            entryTag.putString("Armor", e.getValue().armor);
            entryTag.putLong("LastSeenTick", e.getValue().lastSeenTick);
            list.add(entryTag);
        }
        tag.put("Entries", list);
        return tag;
    }

    // ==== Query/mutation API ====

    /**
     * Upserts a guard's cached snapshot. Called only with data freshly read from an
     * actually-loaded {@code Guard} instance (see the class javadoc for call sites) -
     * never with guessed/interpolated values.
     */
    public void recordSnapshot(UUID guardId, UUID owner, String name, boolean patrolling,
                                String mount, String armor, long tick) {
        entries.put(guardId, new Entry(owner, name, patrolling, mount, armor, tick));
        setDirty();
    }

    /**
     * Drops a guard's cached snapshot entirely - called when a guard dies
     * ({@code GuardRosterCleanupHandler}), so the roster (and therefore the Ledger)
     * doesn't keep listing a dead guard forever with no way to ever refresh or remove
     * it otherwise.
     */
    public void remove(UUID guardId) {
        if (entries.remove(guardId) != null) {
            setDirty();
        }
    }

    /**
     * Every cached entry owned by {@code owner}, keyed by guard UUID. Returns a
     * snapshot copy (a new map), not the internal map itself, so callers can't
     * accidentally mutate roster state by iterating it.
     */
    public Map<UUID, Entry> entriesOwnedBy(UUID owner) {
        Map<UUID, Entry> result = new LinkedHashMap<>();
        for (Map.Entry<UUID, Entry> e : entries.entrySet()) {
            if (e.getValue().owner.equals(owner)) {
                result.put(e.getKey(), e.getValue());
            }
        }
        return result;
    }
}
