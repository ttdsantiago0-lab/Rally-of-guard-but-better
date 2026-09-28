package net.hfstack.rallyguard.contract;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Phase 3E ("Commander's Intent"): the small amount of per-guard combat intent that has
 * to outlive a single order click - whether the guard is under a HOLD FIRE instruction,
 * and which entity (if any) its commander explicitly told it to fight. Everything else a
 * guard "is doing" is already stored somewhere that existed before this phase: its post
 * is Guard Villagers' own patrol position ({@code Guard#getPatrolPos}), its posture is
 * {@link StanceRegistry}, its formation slot is {@link FormationRegistry}.
 * <p>
 * Why this has to persist at all (unlike the one-shot {@code OrderExecutor}s): Guard
 * Villagers' own AI re-decides targets every few ticks, so a one-shot
 * {@code setTarget(...)} from an order is overwritten as soon as anything else happens.
 * Hold fire and a designated target are standing instructions that
 * {@code GuardStanceHandler} keeps enforcing (its target-change gate and its
 * once-a-second scan) until the commander changes them - which is the whole difference
 * between "the AI picked a fight" and "the commander decided who we fight".
 * <p>
 * Same {@code SavedData} shape as {@link StanceRegistry} (world-global, overworld-backed;
 * see {@link SquadRegistry} for the verification history of this shape). A guard with no
 * entry has the default directive - weapons free, no designated target - and default
 * entries are never stored, so an untouched world keeps this file empty.
 */
public final class DirectiveRegistry extends SavedData {

    private static final String DATA_ID = "rallyguard_directives";

    /** The standing combat intent for one guard. {@code target} null = no designated
     *  target; {@code targetLabel} is display-only (the name shown in the Ledger), never
     *  used to resolve anything. */
    public record Directive(boolean holdFire, @Nullable UUID target, String targetLabel) {
        public static final Directive DEFAULT = new Directive(false, null, "");

        boolean isDefault() {
            return !holdFire && target == null;
        }
    }

    private final Map<UUID, Directive> directives = new LinkedHashMap<>();

    public static DirectiveRegistry get(ServerLevel anyLevel) {
        ServerLevel overworld = anyLevel.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(DirectiveRegistry::new, DirectiveRegistry::load, null),
                DATA_ID);
    }

    private static DirectiveRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        DirectiveRegistry registry = new DirectiveRegistry();
        ListTag list = tag.getList("Entries", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entryTag = list.getCompound(i);
            UUID guardId = entryTag.getUUID("GuardId");
            boolean holdFire = entryTag.getBoolean("HoldFire");
            UUID target = entryTag.hasUUID("Target") ? entryTag.getUUID("Target") : null;
            String label = entryTag.getString("TargetLabel");
            Directive directive = new Directive(holdFire, target, label);
            if (!directive.isDefault()) {
                registry.directives.put(guardId, directive);
            }
        }
        return registry;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Directive> e : directives.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("GuardId", e.getKey());
            entryTag.putBoolean("HoldFire", e.getValue().holdFire());
            if (e.getValue().target() != null) {
                entryTag.putUUID("Target", e.getValue().target());
            }
            entryTag.putString("TargetLabel", e.getValue().targetLabel());
            list.add(entryTag);
        }
        tag.put("Entries", list);
        return tag;
    }

    // ==== Query/mutation API ====

    /** Never returns null - a guard with no entry has {@link Directive#DEFAULT}. */
    public Directive directiveOf(UUID guardId) {
        return directives.getOrDefault(guardId, Directive.DEFAULT);
    }

    public void setHoldFire(UUID guardId, boolean holdFire) {
        Directive current = directiveOf(guardId);
        put(guardId, new Directive(holdFire, current.target(), current.targetLabel()));
    }

    /** {@code target == null} clears the designated target. */
    public void setTarget(UUID guardId, @Nullable UUID target, String label) {
        Directive current = directiveOf(guardId);
        put(guardId, new Directive(current.holdFire(), target, target == null ? "" : label));
    }

    /** Drops every designated-target reference to {@code targetId} - called when that
     *  entity dies for good, so guards don't keep a dead mob as their standing target
     *  forever. Players are deliberately never passed here (they respawn; a squad told
     *  to hunt a player keeps hunting until the commander says otherwise). */
    public void clearTargetEverywhere(UUID targetId) {
        boolean changed = false;
        for (Map.Entry<UUID, Directive> e : directives.entrySet()) {
            Directive d = e.getValue();
            if (targetId.equals(d.target())) {
                e.setValue(new Directive(d.holdFire(), null, ""));
                changed = true;
            }
        }
        if (changed) {
            directives.values().removeIf(Directive::isDefault);
            setDirty();
        }
    }

    /** Called on the guard's own death, same reasoning as every other per-guard
     *  registry's {@code remove}. */
    public void remove(UUID guardId) {
        if (directives.remove(guardId) != null) {
            setDirty();
        }
    }

    private void put(UUID guardId, Directive directive) {
        Directive previous = directive.isDefault()
                ? directives.remove(guardId)
                : directives.put(guardId, directive);
        if (!directive.equals(previous == null ? Directive.DEFAULT : previous)) {
            setDirty();
        }
    }
}
