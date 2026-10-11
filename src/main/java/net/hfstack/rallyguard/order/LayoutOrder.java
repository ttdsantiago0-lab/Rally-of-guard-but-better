package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.command.GroupProfile;
import net.hfstack.rallyguard.command.SlotQueue;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.formation.Formation;
import net.hfstack.rallyguard.formation.FormationLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * E3B G1 - changes the GEOMETRY of the selected groups (order ids {@code LAYOUT_DENSITY_<D>},
 * {@code LAYOUT_LOAD_<L>}, {@code LAYOUT_RESET}; sent by the group card's cycle buttons and by
 * the planner). The new layout is stored for each squad/unit involved (FormationRegistry, so the
 * next Muster, Reform or unit directive uses it) and every guard already in a formation is
 * re-slotted in place - same anchor, facing and shape - GRADUALLY: front slots first, the last
 * one within {@code groups.layoutTransitionSeconds} ({@link SlotQueue#enqueueAfter}). With a
 * non-default layout the toughest guards take the most exposed slots and shooters the rear.
 */
public final class LayoutOrder implements OrderExecutor {

    private final @Nullable FormationLayout.Density density;
    private final @Nullable FormationLayout.Load load;

    public LayoutOrder(@Nullable FormationLayout.Density density, @Nullable FormationLayout.Load load) {
        this.density = density;
        this.load = load;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        if (!RallyConfig.on(RallyConfig.LAYOUT_ENABLED)) {
            player.displayClientMessage(Component.translatable("gui.rallyguard.layout.disabled"), true);
            return;
        }
        ServerLevel level = player.serverLevel();
        FormationLayout applied = apply(level, guards, density, load);
        player.displayClientMessage(Component.translatable("gui.rallyguard.layout.changed", guards.size(),
                Component.translatable("gui.rallyguard.layout.density." + applied.density().name().toLowerCase(Locale.ROOT)),
                Component.translatable("gui.rallyguard.layout.load." + applied.load().name().toLowerCase(Locale.ROOT))), true);
    }

    /**
     * Stores and applies a layout change for these guards' units; returns the resulting layout
     * of the first unit. Null density/load = keep that part (both null = reset to DEFAULT).
     * Also used by the planner and the instruction interpreter (no player needed).
     */
    public static FormationLayout apply(ServerLevel level, List<Guard> guards, @Nullable FormationLayout.Density density,
                                        @Nullable FormationLayout.Load load) {
        FormationRegistry registry = FormationRegistry.get(level);
        SquadRegistry squads = SquadRegistry.get(level);
        FormationLayout first = null;
        Map<String, FormationLayout> byUnit = new LinkedHashMap<>();
        for (Guard g : guards) {
            UUID owner = GuardOwnership.getOwner(g);
            Optional<String> path = squads.pathOf(g.getUUID());
            FormationRegistry.Slot slot = registry.slotOf(g.getUUID());
            String key = owner + "#" + path.orElse("");
            FormationLayout next = byUnit.get(key);
            if (next == null) {
                FormationLayout current = owner != null && path.isPresent() ? registry.unitLayout(owner, path.get())
                        : slot != null ? slot.layout() : FormationLayout.DEFAULT;
                next = density == null && load == null ? FormationLayout.DEFAULT
                        : new FormationLayout(density != null ? density : current.density(), load != null ? load : current.load(),
                        current.gaps());
                byUnit.put(key, next);
                if (owner != null && path.isPresent()) registry.setUnitLayout(owner, path.get(), next);
            }
            if (first == null) first = next;
        }
        reslot(level, guards, byUnit);
        return first == null ? FormationLayout.DEFAULT : first;
    }

    /** Re-slots every guard that has a formation, per formation (same anchor / facing / shape / count). */
    static void reslot(ServerLevel level, List<Guard> guards, Map<String, FormationLayout> byUnit) {
        FormationRegistry registry = FormationRegistry.get(level);
        SquadRegistry squads = SquadRegistry.get(level);
        Map<String, List<Guard>> formations = new LinkedHashMap<>();
        Map<String, FormationRegistry.Slot> sample = new LinkedHashMap<>();
        Map<String, FormationLayout> layoutOf = new LinkedHashMap<>();
        for (Guard g : guards) {
            FormationRegistry.Slot slot = registry.slotOf(g.getUUID());
            if (slot == null) continue;
            String unitKey = GuardOwnership.getOwner(g) + "#" + squads.pathOf(g.getUUID()).orElse("");
            FormationLayout layout = byUnit.getOrDefault(unitKey, slot.layout());
            String fk = String.format(Locale.ROOT, "%.1f,%.1f,%.1f,%.0f,%s,%d,%s", slot.anchor().x, slot.anchor().y,
                    slot.anchor().z, slot.facingYaw(), slot.shape(), slot.count(), unitKey);
            formations.computeIfAbsent(fk, k -> new ArrayList<>()).add(g);
            sample.putIfAbsent(fk, slot);
            layoutOf.putIfAbsent(fk, layout);
        }
        int transition = 20 * RallyConfig.i(RallyConfig.LAYOUT_TRANSITION_SECONDS, 8);
        for (Map.Entry<String, List<Guard>> e : formations.entrySet()) {
            FormationRegistry.Slot old = sample.get(e.getKey());
            FormationLayout layout = layoutOf.get(e.getKey());
            List<Guard> members = e.getValue();
            Formation formation = new Formation(old.shape(), old.spacing(), layout);
            List<Vec3> positions = formation.positions(old.anchor(), old.facingYaw(), old.count());
            List<Integer> order = formation.exposureOrder(old.count());
            if (layout.isDefault()) {
                members.sort(Comparator.comparingInt((Guard m) -> registry.slotOf(m.getUUID()).index()));
            } else {
                members.sort(Comparator.comparingDouble((Guard m) -> -GroupProfile.toughness(m)).thenComparing(Guard::getUUID));
            }
            int step = Math.max(1, transition / Math.max(1, members.size()));
            for (int i = 0; i < members.size() && i < order.size(); i++) {
                Guard m = members.get(i);
                int index = layout.isDefault() ? registry.slotOf(m.getUUID()).index() : order.get(i);
                if (index < 0 || index >= positions.size()) continue;
                registry.setSlot(m.getUUID(), new FormationRegistry.Slot(old.anchor(), old.facingYaw(), old.shape(),
                        old.spacing(), index, old.count(), layout));
                SlotQueue.enqueueAfter(m, positions.get(index), 1.0D, Math.min(transition, i * step));
            }
        }
    }

    /** Order id -> executor, for the registry: LAYOUT_DENSITY_X, LAYOUT_LOAD_X, LAYOUT_RESET. */
    @Nullable
    public static OrderExecutor forId(String id) {
        String u = id.toUpperCase(Locale.ROOT);
        if (u.equals("LAYOUT_RESET")) return new LayoutOrder(null, null);
        try {
            if (u.startsWith("LAYOUT_DENSITY_")) {
                return new LayoutOrder(FormationLayout.Density.valueOf(u.substring("LAYOUT_DENSITY_".length())), null);
            }
            if (u.startsWith("LAYOUT_LOAD_")) {
                return new LayoutOrder(null, FormationLayout.Load.valueOf(u.substring("LAYOUT_LOAD_".length())));
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
        return null;
    }
}
