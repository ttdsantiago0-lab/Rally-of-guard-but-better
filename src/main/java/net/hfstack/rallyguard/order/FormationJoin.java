package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Phase 3H.1 (user decision, 2026-10-04: "quiero que los nuevos miembros entren a
 * formación, como si fuera la vida real"): a guard assigned to a squad or unit that is
 * standing in formation takes a place in it right away - the formation is re-mustered on
 * the same anchor, facing and shape with the newcomer included.
 * <p>
 * "The" formation of a group is the one most of its loaded members remember (members
 * planted elsewhere on purpose - a sniper on a roof - keep their own spot). A recruit
 * further than {@link #JOIN_RADIUS} from it doesn't march across the world; it joins on
 * the next Reform instead.
 */
public final class FormationJoin {
    private FormationJoin() {
    }

    public static final double JOIN_RADIUS = 128.0D;

    /** Returns how many newcomers joined. */
    public static int join(ServerPlayer player, String groupPath, List<Guard> newcomers) {
        if (groupPath.isEmpty() || newcomers.isEmpty()) return 0;
        ServerLevel level = player.serverLevel();
        FormationRegistry formations = FormationRegistry.get(level);
        Set<UUID> members = SquadRegistry.get(level).membersOf(player.getUUID(), groupPath);

        // Loaded veterans of the group, grouped by the formation (anchor) they remember.
        Map<String, List<Guard>> byFormation = new HashMap<>();
        Map<String, FormationRegistry.Slot> slotOf = new HashMap<>();
        Set<UUID> newIds = new java.util.HashSet<>();
        for (Guard g : newcomers) newIds.add(g.getUUID());
        for (UUID id : members) {
            if (newIds.contains(id)) continue;
            if (!(level.getEntity(id) instanceof Guard guard) || !guard.isAlive()
                    || !GuardOwnership.isOwnedBy(guard, player.getUUID())) continue;
            FormationRegistry.Slot slot = formations.slotOf(id);
            if (slot == null) continue;
            String key = Math.round(slot.anchor().x) + "," + Math.round(slot.anchor().y) + "," + Math.round(slot.anchor().z);
            byFormation.computeIfAbsent(key, k -> new ArrayList<>()).add(guard);
            slotOf.putIfAbsent(key, slot);
        }
        String best = null;
        for (Map.Entry<String, List<Guard>> e : byFormation.entrySet()) {
            if (best == null || e.getValue().size() > byFormation.get(best).size()) best = e.getKey();
        }
        if (best == null) return 0;

        FormationRegistry.Slot slot = slotOf.get(best);
        List<Guard> formation = new ArrayList<>(byFormation.get(best));
        // Keep the old order so veterans mostly keep their places; newcomers fill the end.
        formation.sort(java.util.Comparator.comparingInt(g -> {
            FormationRegistry.Slot s = formations.slotOf(g.getUUID());
            return s == null ? Integer.MAX_VALUE : s.index();
        }));
        int joined = 0;
        for (Guard recruit : newcomers) {
            if (recruit.distanceToSqr(slot.anchor()) <= JOIN_RADIUS * JOIN_RADIUS) {
                formation.add(recruit);
                joined++;
            }
        }
        if (joined == 0) return 0;
        new MusterOrder(slot.anchor(), slot.facingYaw(), slot.shape()).execute(player, formation);
        return joined;
    }
}
