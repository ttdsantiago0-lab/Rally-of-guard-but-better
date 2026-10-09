package net.hfstack.rallyguard.morale;

import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * E3B V4 - unit experience ("cohesión de unidad"): guards that FIGHT TOGETHER build a bond
 * (0..1) that grows with the unit's combat seconds (≈ 63 % after 8 minutes of fighting) and
 * DECAYS when members change - the share of the unit that is new is the share of the bond lost.
 * It enters {@link Cohesion} and the leadership score / execution quality (formation tolerance,
 * response time), never stats. Memory only (it is rebuilt by fighting again after a restart).
 */
public final class UnitBond {
    private UnitBond() {
    }

    /** Bond gained per second of combat: 1 - e^(-t / 480 s). */
    static final double GAIN = 1.0D / 480.0D;

    private static final class Bond {
        Set<UUID> members = new HashSet<>();
        double value;
        long tick = Long.MIN_VALUE / 2;
    }

    private static final Map<String, Bond> BONDS = new HashMap<>();

    /** Once a second per unit (from Cohesion): membership change decay, then combat gain. */
    static double update(String key, List<Guard> members, boolean fighting, long now) {
        Bond b = BONDS.computeIfAbsent(key, k -> new Bond());
        if (now - b.tick < 20) return b.value;
        b.tick = now;
        Set<UUID> present = new HashSet<>();
        for (Guard g : members) present.add(g.getUUID());
        if (!b.members.isEmpty() && !present.equals(b.members)) {
            int newcomers = 0;
            for (UUID id : present) if (!b.members.contains(id)) newcomers++;
            double share = present.isEmpty() ? 1.0D : (double) newcomers / present.size();
            b.value *= 1.0D - share; // a unit that is half new keeps half its bond
        }
        b.members = present;
        if (fighting && present.size() >= 2) b.value += (1.0D - b.value) * GAIN;
        if (BONDS.size() > 4096) BONDS.clear();
        return b.value;
    }

    /** The bond of a unit key, 0 if unknown. */
    public static double of(String key) {
        Bond b = BONDS.get(key);
        return b == null ? 0.0D : b.value;
    }

    public static void clearAll() {
        BONDS.clear();
    }
}
