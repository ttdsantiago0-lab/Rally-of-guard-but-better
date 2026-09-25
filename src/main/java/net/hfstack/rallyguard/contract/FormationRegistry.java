package net.hfstack.rallyguard.contract;

import net.hfstack.rallyguard.formation.FormationShape;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Phase 3D: remembers, per guard, the last formation {@code MusterOrder} placed it
 * into - anchor position, facing, shape, spacing, and which slot index it occupied -
 * so {@code ReformOrder} can send a guard back to that same slot later without the
 * player re-specifying the whole formation. Same {@code SavedData} shape as
 * {@link StanceRegistry}/{@link SquadRegistry} (a world-global registry on the
 * OVERWORLD's data storage; see either's javadoc for the full verification history
 * behind this shape, not re-verified again here).
 * <p>
 * This does NOT make Order dispatch itself stateful - {@code OrderExecutor} remains a
 * one-shot, no-persisted-state abstraction (see its own javadoc). It is specifically
 * Muster's RESULT ("this guard is now slot 2 of a 5-guard Line formation anchored
 * here, facing there") that is worth remembering, the same way assigning a Squad or a
 * Stance already writes to a small persistent registry as a side effect of a network
 * handler - this is that same established pattern, just for formations.
 * <p>
 * A guard with no entry here simply has nothing to Reform to - {@code ReformOrder}
 * silently skips it, the same tolerance every other batch order already has for a
 * guard it can't act on.
 */
public final class FormationRegistry extends SavedData {

    private static final String DATA_ID = "rallyguard_formations";

    private final Map<UUID, Slot> slots = new LinkedHashMap<>();

    /**
     * Everything {@code ReformOrder} needs to recompute a guard's assigned position
     * later, without needing to see the other guards mustered alongside it - each guard
     * remembers the WHOLE formation it was part of, not just its own point, since
     * {@code Formation.positions(...)} needs the full shape/count/spacing to regenerate
     * any one slot's position.
     */
    public record Slot(Vec3 anchor, float facingYaw, FormationShape shape, double spacing,
                        int index, int count) {
    }

    public static FormationRegistry get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(FormationRegistry::new, FormationRegistry::load, null),
                DATA_ID);
    }

    private static FormationRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        FormationRegistry registry = new FormationRegistry();
        ListTag list = tag.getList("Slots", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entryTag = list.getCompound(i);
            UUID guardId = entryTag.getUUID("GuardId");
            Vec3 anchor = new Vec3(entryTag.getDouble("AnchorX"), entryTag.getDouble("AnchorY"),
                    entryTag.getDouble("AnchorZ"));
            float facingYaw = entryTag.getFloat("FacingYaw");
            FormationShape shape = parseShapeOrDefault(entryTag.getString("Shape"));
            double spacing = entryTag.getDouble("Spacing");
            int index = entryTag.getInt("Index");
            int count = entryTag.getInt("Count");
            registry.slots.put(guardId, new Slot(anchor, facingYaw, shape, spacing, index, count));
        }
        return registry;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Slot> e : slots.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("GuardId", e.getKey());
            Slot slot = e.getValue();
            entryTag.putDouble("AnchorX", slot.anchor().x);
            entryTag.putDouble("AnchorY", slot.anchor().y);
            entryTag.putDouble("AnchorZ", slot.anchor().z);
            entryTag.putFloat("FacingYaw", slot.facingYaw());
            entryTag.putString("Shape", slot.shape().name());
            entryTag.putDouble("Spacing", slot.spacing());
            entryTag.putInt("Index", slot.index());
            entryTag.putInt("Count", slot.count());
            list.add(entryTag);
        }
        tag.put("Slots", list);
        return tag;
    }

    /** Returns {@code null} when the guard has no remembered slot (never Mustered, or
     *  its slot was cleared) - callers must treat that as "nothing to Reform to." */
    public Slot slotOf(UUID guardId) {
        return slots.get(guardId);
    }

    public void setSlot(UUID guardId, Slot slot) {
        slots.put(guardId, slot);
        setDirty();
    }

    /** Drops a guard's remembered slot entirely - called on death, same reasoning as
     *  every other per-guard registry's own {@code remove}. */
    public void remove(UUID guardId) {
        if (slots.remove(guardId) != null) {
            setDirty();
        }
    }

    private static FormationShape parseShapeOrDefault(String raw) {
        try {
            return FormationShape.valueOf(raw);
        } catch (IllegalArgumentException | NullPointerException e) {
            return FormationShape.LINE;
        }
    }
}
