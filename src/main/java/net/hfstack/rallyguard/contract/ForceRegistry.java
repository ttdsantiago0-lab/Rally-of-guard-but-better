package net.hfstack.rallyguard.contract;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * E3A A1 - forces, saved like the squad registry ({@code rallyguard_forces}). A FORCE is one
 * of a commander's squads that contains units (its "Squad/Unit" subgroups, kept in
 * {@link SquadRegistry}); this record only adds what a force has on top: its doctrine, the
 * units it declares (so an empty unit can exist before guards join), and each unit's role
 * (A3: anvil, hammer, screen, support, flank, reserve - "" = let the general decide).
 */
public final class ForceRegistry extends SavedData {

    private static final String DATA_ID = "rallyguard_forces";

    public static final class Force {
        public final UUID owner;
        public final String squad;
        public String doctrine = "STANDARD";
        /** declared unit name -> role ("" = automatic) */
        public final Map<String, String> units = new LinkedHashMap<>();

        Force(UUID owner, String squad) {
            this.owner = owner;
            this.squad = squad;
        }
    }

    private final Map<String, Force> forces = new LinkedHashMap<>();

    private static String key(UUID owner, String squad) {
        return owner + "#" + squad.toLowerCase(Locale.ROOT);
    }

    public static ForceRegistry get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(ForceRegistry::new, ForceRegistry::load, null), DATA_ID);
    }

    private static ForceRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        ForceRegistry r = new ForceRegistry();
        ListTag list = tag.getList("Forces", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag t = list.getCompound(i);
            Force f = new Force(t.getUUID("Owner"), t.getString("Squad"));
            f.doctrine = t.getString("Doctrine");
            CompoundTag units = t.getCompound("Units");
            for (String u : units.getAllKeys()) f.units.put(u, units.getString(u));
            r.forces.put(key(f.owner, f.squad), f);
        }
        return r;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Force f : forces.values()) {
            CompoundTag t = new CompoundTag();
            t.putUUID("Owner", f.owner);
            t.putString("Squad", f.squad);
            t.putString("Doctrine", f.doctrine);
            CompoundTag units = new CompoundTag();
            f.units.forEach(units::putString);
            t.put("Units", units);
            list.add(t);
        }
        tag.put("Forces", list);
        return tag;
    }

    @Nullable
    public Force find(UUID owner, String squad) {
        return forces.get(key(owner, squad));
    }

    public Force create(UUID owner, String squad) {
        setDirty();
        return forces.computeIfAbsent(key(owner, squad), k -> new Force(owner, squad));
    }

    public boolean dissolve(UUID owner, String squad) {
        boolean removed = forces.remove(key(owner, squad)) != null;
        if (removed) setDirty();
        return removed;
    }

    public List<Force> ownedBy(UUID owner) {
        List<Force> out = new ArrayList<>();
        for (Force f : forces.values()) if (f.owner.equals(owner)) out.add(f);
        return out;
    }

    public List<Force> all() {
        return new ArrayList<>(forces.values());
    }

    /** Call after mutating a {@link Force} returned by {@link #find}. */
    public void changed() {
        setDirty();
    }
}
