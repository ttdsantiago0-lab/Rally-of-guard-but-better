package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.contract.VeterancyRegistry;
import net.hfstack.rallyguard.event.GuardStanceHandler;
import net.hfstack.rallyguard.veterancy.Trait;
import net.hfstack.rallyguard.veterancy.Veterancy;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * E3A A1 - the leadership score ({@code docs/ARQUITECTURA_VETERANIA.md} §4-§5), 0..100:
 * weighted (config {@code leadership.scoreWeights}) sum of rank, the LEADER trait, command XP
 * (diminishing), the experience of the guard's unit (mean rank) and its command mastery path.
 * <p>
 * Recomputed ONLY on events - a rank-up, a squad/unit change, a death, a relief - which bump
 * {@link #invalidate() the epoch}; between events every read is a cache hit. The captain of a
 * unit is its best score, the general of a force the best score among its captains; nothing is
 * appointed by hand and no entity is created.
 * <p>
 * The score changes EXECUTION only, through bounded multipliers (§5): response delay to a new
 * directive, formation tolerance before breaking, over-pursuit (leash), and reorganization /
 * relief time. Never health, damage or speed.
 */
public final class Leadership {
    private Leadership() {
    }

    private static final double[] DEFAULT_WEIGHTS = {40, 15, 20, 15, 10};
    private static long epoch;
    private static final Map<UUID, double[]> SCORE = new HashMap<>(); // guard -> {epoch, score}
    private static final Map<String, double[]> UNIT_XP = new HashMap<>(); // unit path -> {epoch, mean rank}
    private static final Map<UUID, double[]> QUALITY = new HashMap<>(); // guard -> {tick, quality of its leader}

    /** Something that changes scores happened (rank-up, squad change, death, relief). */
    public static void invalidate() {
        epoch++;
        if (SCORE.size() > 8192) SCORE.clear();
        if (UNIT_XP.size() > 2048) UNIT_XP.clear();
    }

    private static double weight(int i) {
        List<? extends Double> w = RallyConfig.get(RallyConfig.LEADERSHIP_WEIGHTS::get, List.of());
        return i < w.size() ? w.get(i) : DEFAULT_WEIGHTS[i];
    }

    public static double score(Guard guard) {
        double[] cached = SCORE.get(guard.getUUID());
        if (cached != null && cached[0] == epoch) return cached[1];
        double s = compute(guard);
        SCORE.put(guard.getUUID(), new double[]{epoch, s});
        return s;
    }

    private static double compute(Guard guard) {
        int rank = Veterancy.rank(guard);
        VeterancyRegistry.Record r = Veterancy.record(guard);
        double commandXp = r == null ? 0 : r.commandXp;
        double s = weight(0) * rank / Veterancy.MAX_RANK
                + (Veterancy.has(guard, Trait.LEADER) ? weight(1) : 0)
                + weight(2) * (1.0D - Math.exp(-commandXp / 300.0D))
                + weight(3) * unitExperience(guard)
                + weight(4) * Veterancy.pathShare(guard, VeterancyRegistry.Path.COMMAND);
        return Math.max(0, Math.min(100, s));
    }

    /** 0..1: the mean rank of the guard's unit (its squad/unit path), cached per epoch. */
    private static double unitExperience(Guard guard) {
        if (!(guard.level() instanceof ServerLevel level)) return 0;
        Optional<String> path = SquadRegistry.get(level).pathOf(guard.getUUID());
        if (path.isEmpty()) return Veterancy.rank(guard) / (double) Veterancy.MAX_RANK;
        String key = guard.getOwnerId() + "#" + path.get();
        double[] cached = UNIT_XP.get(key);
        if (cached != null && cached[0] == epoch) return cached[1];
        SquadRegistry squads = SquadRegistry.get(level);
        int n = 0;
        double sum = 0;
        for (Guard g : GuardStanceHandler.loaded(level.dimension())) {
            if (g.isAlive() && path.equals(squads.pathOf(g.getUUID()))) {
                n++;
                sum += Veterancy.rank(g);
            }
        }
        double v = n == 0 ? 0 : sum / n / Veterancy.MAX_RANK;
        UNIT_XP.put(key, new double[]{epoch, v});
        return v;
    }

    /**
     * 0..1 quality of the leader this guard answers to: the captain of its pushed directive,
     * else itself. Cached 2 s per guard.
     */
    public static double quality(Guard guard) {
        long now = guard.level().getGameTime();
        double[] cached = QUALITY.get(guard.getUUID());
        if (cached != null && now - (long) cached[0] < 40) return cached[1];
        Guard leader = guard;
        SquadDirectives.Directive d = SquadDirectives.of(guard.getUUID());
        if (d != null && d.captain() != null && guard.level() instanceof ServerLevel level) {
            Entity e = level.getEntity(d.captain());
            if (e instanceof Guard c && c.isAlive()) leader = c;
        }
        double q = score(leader) / 100.0D;
        // E3B G2: a consistent group executes a little better, an incoherent one a little worse (±15 % max).
        if (guard.level() instanceof ServerLevel lvl) {
            q = Math.max(0.0D, Math.min(1.0D, q * net.hfstack.rallyguard.command.GroupProfile.qualityFactor(
                    net.hfstack.rallyguard.command.GroupProfile.forGuard(lvl, guard))));
        }
        QUALITY.put(guard.getUUID(), new double[]{now, q});
        return q;
    }

    private static double lerp(double worst, double best, double q) {
        return worst + (best - worst) * Math.max(0, Math.min(1, q));
    }

    /** Ticks before a new directive is carried out (best leader 0, worst {@code maxResponseDelayTicks}). */
    public static int responseDelay(Guard guard) {
        return (int) Math.round((1.0D - quality(guard)) * RallyConfig.i(RallyConfig.MAX_RESPONSE_DELAY, 40));
    }

    /** Ticks a unit needs to reorganize under a new captain of this guard's score. */
    public static int reliefDelay(Guard newCaptain) {
        return (int) Math.round((1.0D - score(newCaptain) / 100.0D) * RallyConfig.i(RallyConfig.MAX_RELIEF_DELAY, 100));
    }

    /** Post-leash multiplier: a good leader keeps its guards from chasing too far. */
    public static double pursuitFactor(Guard guard) {
        return lerp(RallyConfig.d(RallyConfig.PURSUIT_WORST, 1.15D), RallyConfig.d(RallyConfig.PURSUIT_BEST, 0.85D), quality(guard));
    }

    /** Formation tolerance multiplier before a guard counts as out of its slot. */
    public static double toleranceFactor(Guard guard) {
        return lerp(RallyConfig.d(RallyConfig.TOLERANCE_WORST, 1.3D), RallyConfig.d(RallyConfig.TOLERANCE_BEST, 0.7D), quality(guard));
    }

    /** The score shown on the Ledger card. */
    public static String ledgerScore(Guard guard) {
        return String.valueOf((int) Math.round(score(guard)));
    }

    public static void forget(UUID guardId) {
        SCORE.remove(guardId);
        QUALITY.remove(guardId);
    }

    public static void clearAll() {
        SCORE.clear();
        UNIT_XP.clear();
        QUALITY.clear();
        epoch++;
    }
}
