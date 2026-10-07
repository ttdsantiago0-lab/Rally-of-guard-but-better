package net.hfstack.rallyguard.command;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.veterancy.Veterancy;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * E2A block 1 - command capacity and the computed captain (playbook
 * {@code docs/DECISIONES_TACTICAS.md} §0.4 and §1). Pure functions over the guards they
 * are handed; nothing here is stored.
 * <ul>
 *     <li>{@link #capacity}: how many guards a guard can coordinate, from its veterancy
 *     rank (config {@code command.capacityByRank}, default 4/10/25/45/80/120);</li>
 *     <li>{@link #captainOf}: the guard with the highest capacity in a unit - a captain is
 *     a property of the unit, never a special entity (ties: higher rank, then the lower
 *     UUID, so every caller picks the same one);</li>
 *     <li>{@link #partition}: a group larger than its captain's capacity is split into
 *     subunits, each led by the best remaining guard and filled with the guards nearest
 *     to it. Subunit sizes are clamped to {@code command.minSubunitSize..maxSubunitSize}
 *     so 200 recruits (capacity 4) become 25 subunits of 8, not 50 of 4.</li>
 * </ul>
 * Cost: one sort plus, per subunit, one distance sort of the guards still unassigned -
 * run once per order, never per tick.
 */
public final class CommandStructure {
    private CommandStructure() {
    }

    private static final int[] DEFAULT_CAPACITY = {4, 10, 25, 45, 80, 120};

    /** One subunit: its captain (also {@code members.get(0)}) and every member. */
    public record Subunit(Guard captain, List<Guard> members) {
    }

    public static int capacity(int rank) {
        return Math.max(1, RallyConfig.at(RallyConfig.COMMAND_CAPACITY, rank, DEFAULT_CAPACITY));
    }

    /** Rank capacity, plus 10 per legend step past rank 5 (diminishing: each step needs double the XP). */
    public static int capacity(Guard guard) {
        return capacity(Veterancy.rank(guard)) + 10 * Veterancy.legendBonus(guard);
    }

    /** Highest capacity first, then highest rank, then lowest UUID (stable). */
    public static final Comparator<Guard> BEST_FIRST = Comparator
            .comparingInt((Guard g) -> -capacity(g))
            .thenComparingInt(g -> -Veterancy.rank(g))
            .thenComparing(Guard::getUUID);

    /** The unit's captain: the guard with the highest command capacity, or null if empty. */
    public static Guard captainOf(Collection<Guard> unit) {
        Guard best = null;
        for (Guard g : unit) {
            if (g == null || !g.isAlive()) continue;
            if (best == null || BEST_FIRST.compare(g, best) < 0) best = g;
        }
        return best;
    }

    /** How many guards a subunit led by {@code captain} holds. */
    public static int subunitSize(Guard captain) {
        int min = RallyConfig.i(RallyConfig.MIN_SUBUNIT, 8);
        int max = Math.max(min, RallyConfig.i(RallyConfig.MAX_SUBUNIT, 120));
        return Math.max(min, Math.min(max, capacity(captain)));
    }

    /**
     * Splits {@code guards} into subunits (see the class javadoc). A group within its
     * captain's capacity stays one subunit. Subunits come out best captain first - the
     * first is the one a formation puts in front.
     */
    public static List<Subunit> partition(List<Guard> guards) {
        List<Subunit> out = new ArrayList<>();
        if (guards.isEmpty()) return out;
        List<Guard> remaining = new ArrayList<>(guards);
        remaining.sort(BEST_FIRST);
        if (remaining.size() <= subunitSize(remaining.get(0))) {
            out.add(new Subunit(remaining.get(0), remaining));
            return out;
        }
        while (!remaining.isEmpty()) {
            Guard captain = remaining.remove(0);
            int size = subunitSize(captain);
            List<Guard> members = new ArrayList<>(Math.min(size, remaining.size() + 1));
            members.add(captain);
            if (!remaining.isEmpty() && size > 1) {
                List<Guard> byDistance = new ArrayList<>(remaining);
                byDistance.sort(Comparator.comparingDouble(g -> g.distanceToSqr(captain)));
                int take = Math.min(size - 1, byDistance.size());
                List<Guard> taken = byDistance.subList(0, take);
                members.addAll(taken);
                remaining.removeAll(new java.util.HashSet<>(taken));
            }
            out.add(new Subunit(captain, members));
        }
        return out;
    }
}
