package net.hfstack.rallyguard.order;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Phase 3H: the commander's CURRENT SELECTION, kept on the server so every way of
 * commanding shares it - the Ledger's rows and chips, sneak-selecting a guard in the
 * world with the Command key, and the Command / Fire hotkeys (which act on it, or on the
 * guards following you when nothing is selected). The Ledger opens with this selection
 * and sends its changes back; the server shows a small marker over each selected guard,
 * visible only to its commander.
 * <p>
 * In memory only (like {@link CommanderMarks}): a selection is a moment-to-moment thing,
 * not army state. Guards are kept by UUID so the selection survives them unloading.
 */
public final class CommanderSelection {
    private CommanderSelection() {
    }

    private static final Map<UUID, LinkedHashSet<UUID>> SELECTED = new HashMap<>();

    public static Set<UUID> of(UUID commander) {
        Set<UUID> set = SELECTED.get(commander);
        return set == null ? Set.of() : Collections.unmodifiableSet(set);
    }

    public static void set(UUID commander, Collection<UUID> guards) {
        if (guards.isEmpty()) {
            SELECTED.remove(commander);
        } else {
            SELECTED.put(commander, new LinkedHashSet<>(guards));
        }
    }

    /** Toggles one guard; returns true if it is selected afterwards. */
    public static boolean toggle(UUID commander, UUID guard) {
        LinkedHashSet<UUID> set = SELECTED.computeIfAbsent(commander, k -> new LinkedHashSet<>());
        if (set.remove(guard)) {
            if (set.isEmpty()) SELECTED.remove(commander);
            return false;
        }
        set.add(guard);
        return true;
    }

    /** A guard that died leaves every selection. */
    public static void forgetGuard(UUID guard) {
        SELECTED.values().forEach(set -> set.remove(guard));
        SELECTED.values().removeIf(Set::isEmpty);
    }

    public static final int MARKER_INTERVAL_TICKS = 10;
    private static final double MARKER_RANGE = 96.0D;
    private static final int MAX_MARKERS = 64;

    /**
     * The "which ones are selected" cue (user review: "un triangulito encima"): a small
     * green sparkle over each selected guard, sent ONLY to its commander (other players
     * see nothing), for at most {@link #MAX_MARKERS} guards within {@link #MARKER_RANGE}
     * - one tiny packet per guard every half second. Vanilla's happy-villager particle:
     * no custom rendering, nothing another mod can break.
     */
    public static void showMarkers(ServerLevel level) {
        if (SELECTED.isEmpty()) return;
        for (Map.Entry<UUID, LinkedHashSet<UUID>> entry : SELECTED.entrySet()) {
            if (!(level.getPlayerByUUID(entry.getKey()) instanceof ServerPlayer commander)) continue;
            int shown = 0;
            for (UUID guardId : entry.getValue()) {
                if (shown >= MAX_MARKERS) break;
                Entity guard = level.getEntity(guardId);
                if (guard == null || guard.distanceToSqr(commander) > MARKER_RANGE * MARKER_RANGE) continue;
                level.sendParticles(commander, ParticleTypes.HAPPY_VILLAGER, true,
                        guard.getX(), guard.getY() + guard.getBbHeight() + 0.5D, guard.getZ(),
                        2, 0.08D, 0.05D, 0.08D, 0.0D);
                shown++;
            }
        }
    }

    public static Map<UUID, LinkedHashSet<UUID>> all() {
        return Collections.unmodifiableMap(SELECTED);
    }

    public static void clearAll() {
        SELECTED.clear();
    }
}
