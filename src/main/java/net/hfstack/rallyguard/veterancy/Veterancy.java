package net.hfstack.rallyguard.veterancy;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.VeterancyRegistry;
import net.hfstack.rallyguard.debug.DebugRanks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * E2A / E3A block V: the one place every system asks about a guard's veterancy
 * ({@code docs/ARQUITECTURA_VETERANIA.md}).
 * <ul>
 *     <li><b>Rank</b> 0..5 = decision quality (0 Recruit, 1 Trained, 2 Experienced, 3 Veteran,
 *     4 Elite, 5 Legend; names in {@code lang}). There is no rank 6. The earned rank lives in
 *     {@link VeterancyRegistry}; {@code /rally debug rank} test ranks apply on top (higher wins).</li>
 *     <li><b>Mastery</b> is separate (V0): after rank 5, XP raises no stat; it is split into
 *     paths - weapon mastery (A8, by archetype), survival, command, mount bond, history -
 *     each with diminishing returns and a cap ({@link #pathLevel}).</li>
 *     <li>One {@link Trait} per rank gained (history + {@link Genetics}); traits are
 *     PREFERENCES read by the decision ladder, never combat numbers.</li>
 *     <li>A rank-5 guard with the same kit has the same health, damage and speed as a rank-0
 *     one. Nobody is immortal.</li>
 * </ul>
 */
public final class Veterancy {
    private Veterancy() {
    }

    public static final int MAX_RANK = 5;
    private static final int[] DEFAULT_XP = {100, 350, 900, 2000, 4500};

    /** Cached registry (looked up once per server; cleared on stop). */
    @Nullable
    private static VeterancyRegistry registry;

    private static VeterancyRegistry registry(ServerLevel level) {
        VeterancyRegistry r = registry;
        if (r == null) {
            r = VeterancyRegistry.get(level);
            registry = r;
        }
        return r;
    }

    public static void clearCache() {
        registry = null;
        RECKLESS_UNTIL.clear();
        Genetics.clearAll();
    }

    @Nullable
    public static VeterancyRegistry.Record record(Guard guard) {
        if (!(guard.level() instanceof ServerLevel level)) return null;
        return registry(level).find(guard.getUUID());
    }

    public static VeterancyRegistry.Record edit(ServerLevel level, UUID guardId) {
        return registry(level).edit(guardId);
    }

    public static int rank(Guard guard) {
        int earned = guard.level() instanceof ServerLevel level ? registry(level).rankOf(guard.getUUID()) : 0;
        return Math.max(0, Math.min(MAX_RANK, Math.max(earned, DebugRanks.of(guard.getUUID()))));
    }

    /** E3A V3: the guard's traits as a bit set of {@link Trait} ordinals (for the decision ladder). */
    public static int traitBits(Guard guard) {
        VeterancyRegistry.Record r = record(guard);
        if (r == null) return 0;
        int bits = 0;
        for (String t : r.traits) {
            try {
                bits |= 1 << Trait.valueOf(t).ordinal();
            } catch (IllegalArgumentException ignored) {
                // a trait name from a newer/older version
            }
        }
        return bits;
    }

    public static boolean has(Guard guard, Trait trait) {
        VeterancyRegistry.Record r = record(guard);
        return r != null && r.traits.contains(trait.name());
    }

    /** XP needed to reach {@code rank} (1..5). */
    public static int xpFor(int rank) {
        return RallyConfig.at(RallyConfig.RANK_XP, rank - 1, DEFAULT_XP);
    }

    /** XP width of the band the guard is climbing now (rank r -> r+1); past rank 5, rank 5's band. */
    public static double band(int rank) {
        int r = Math.min(rank, MAX_RANK - 1);
        return Math.max(1, xpFor(r + 1) - (r == 0 ? 0 : xpFor(r)));
    }

    /**
     * E3A V0: command-capacity steps from XP past rank 5 - 0 by default (config
     * {@code veterancy.legendCapacitySteps}); command now comes from the leadership score.
     */
    public static int legendBonus(Guard guard) {
        int maxSteps = RallyConfig.i(RallyConfig.LEGEND_CAPACITY_STEPS, 0);
        if (maxSteps <= 0) return 0;
        VeterancyRegistry.Record r = record(guard);
        if (r == null || r.rank < MAX_RANK) return 0;
        double base = xpFor(MAX_RANK);
        double extra = Math.max(0.0D, r.xp - base);
        return Math.min(maxSteps, (int) Math.floor(Math.log(1.0D + extra / base) / Math.log(2.0D)));
    }

    /** Level of a post-rank-5 path: cap × (1 − e^(−xp/scale)) - diminishing, never above the cap. */
    public static double pathLevel(double pathXp) {
        double cap = RallyConfig.d(RallyConfig.PATH_CAP, 100.0D);
        double scale = RallyConfig.d(RallyConfig.PATH_SCALE, 2000.0D);
        return cap * (1.0D - Math.exp(-Math.max(0.0D, pathXp) / scale));
    }

    /** 0..1 progress of a path (level / cap). */
    public static double pathShare(Guard guard, VeterancyRegistry.Path path) {
        VeterancyRegistry.Record r = record(guard);
        if (r == null) return 0.0D;
        return pathLevel(r.paths[path.ordinal()]) / RallyConfig.d(RallyConfig.PATH_CAP, 100.0D);
    }

    /**
     * Adds rank XP and promotes as far as it reaches, one trait per rank, telling the
     * commander. Past rank 5 the XP goes to the path {@code overflow} instead (V0), so rank
     * 5 never grows stats. {@code command} marks command XP (V2), also kept apart.
     */
    public static void addXp(ServerLevel level, Guard guard, double amount, VeterancyRegistry.Path overflow, boolean command) {
        if (amount <= 0.0D) return;
        if (net.hfstack.rallyguard.debug.SimProbe.blocksXp(guard)) return; // simulation ranks stay fixed
        VeterancyRegistry.Record r = edit(level, guard.getUUID());
        if (command) r.commandXp += amount;
        if (r.rank >= MAX_RANK) {
            r.paths[overflow.ordinal()] += amount;
            r.xp += amount;
            net.hfstack.rallyguard.debug.SimProbe.xp(guard, amount);
            return;
        }
        r.xp += amount;
        net.hfstack.rallyguard.debug.SimProbe.xp(guard, amount);
        while (r.rank < MAX_RANK && r.xp >= xpFor(r.rank + 1)) {
            r.rank++;
            net.hfstack.rallyguard.force.Leadership.invalidate();
            Trait trait = chooseTrait(r, guard, r.rank);
            if (trait != null) r.traits.add(trait.name());
            r.addStory(level.getDayTime() / 24000L, "rank", r.rank + (trait == null ? "" : ":" + trait.name()));
            announce(level, guard, r.rank, trait);
        }
    }

    /** Combat XP (overflow to SURVIVAL past rank 5). */
    public static void addXp(ServerLevel level, Guard guard, double amount) {
        addXp(level, guard, amount, VeterancyRegistry.Path.SURVIVAL, false);
    }

    private static void announce(ServerLevel level, Guard guard, int rank, @Nullable Trait trait) {
        UUID owner = GuardOwnership.getOwner(guard);
        ServerPlayer commander = owner == null ? null : level.getServer().getPlayerList().getPlayer(owner);
        if (commander == null) return;
        Component traitName = trait == null ? Component.literal("-")
                : Component.translatable("gui.rallyguard.trait." + trait.name().toLowerCase(Locale.ROOT));
        commander.sendSystemMessage(Component.translatable("gui.rallyguard.vet.rank_up",
                guard.getName(), rankComponent(rank), traitName).withStyle(ChatFormatting.AQUA));
        // E3A V4: what the new rank adds, in one line.
        commander.sendSystemMessage(Component.translatable("gui.rallyguard.rankdesc." + rank).withStyle(ChatFormatting.GRAY));
    }

    public static Component rankComponent(int rank) {
        String name = rankName(rank);
        return name.isEmpty() ? Component.translatable("gui.rallyguard.rank." + rank) : Component.literal(name);
    }

    /**
     * The trait a new rank brings: the best-scoring one the guard doesn't have yet, scored
     * from its history plus a genetic bias (ties broken by a per-guard roll, so two veterans
     * with the same history still differ).
     */
    @Nullable
    static Trait chooseTrait(VeterancyRegistry.Record r, Guard guard, int rank) {
        UUID guardId = guard.getUUID();
        List<Affinity> genes = Genetics.of(guard);
        Map<Trait, Double> score = new HashMap<>();
        score.put(Trait.TANK, r.nearDeath * 3.0D + r.totems * 4.0D + (genes.contains(Affinity.TOUGH) ? 6 : 0));
        score.put(Trait.MARKSMAN, (r.killsRanged + r.killsGun) * 2.0D);
        score.put(Trait.DUELIST, r.killsMelee * 2.0D);
        score.put(Trait.RIDER, r.secondsMounted / 120.0D);
        score.put(Trait.STEADY, r.battles * 3.0D + (genes.contains(Affinity.CALM) ? 6 : 0)
                - (Genetics.timid(guard) ? 4 : 0));
        score.put(Trait.ASSAULT, r.killsMelee + (genes.contains(Affinity.CONFIDENT) ? 6.0D : 0.0D));
        score.put(Trait.SCOUT, r.enemyTypes.size() * 2.0D + (genes.contains(Affinity.ATHLETIC) ? 6 : 0));
        score.put(Trait.LEADER, r.commandXp / 20.0D + r.reliefsTaken * 4.0D);
        Random roll = new Random(guardId.getLeastSignificantBits() + rank * 7919L);
        Trait best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (Trait t : Trait.values()) {
            if (r.traits.contains(t.name())) continue;
            double s = score.getOrDefault(t, 0.0D) + roll.nextDouble(); // < 1: only breaks ties / near-ties
            if (s > bestScore) {
                bestScore = s;
                best = t;
            }
        }
        return best;
    }

    // ==== Reckless (after repeated revives) ====

    private static final Map<UUID, Long> RECKLESS_UNTIL = new HashMap<>();

    public static void makeReckless(Guard guard, long untilTick) {
        RECKLESS_UNTIL.put(guard.getUUID(), untilTick);
    }

    /** A guard revived again and again fights recklessly for a while: no retreat, no leash. */
    public static boolean isReckless(Guard guard) {
        Long until = RECKLESS_UNTIL.get(guard.getUUID());
        if (until == null) return false;
        Level level = guard.level();
        if (level.getGameTime() > until) {
            RECKLESS_UNTIL.remove(guard.getUUID());
            return false;
        }
        return true;
    }

    public static void forget(UUID guardId) {
        RECKLESS_UNTIL.remove(guardId);
        Genetics.forget(guardId);
    }

    // ==== Names / Ledger ====

    /** Configured name of a rank ({@code veterancy.rankNames}), or "" for the translated default. */
    public static String rankName(int rank) {
        List<? extends String> names = RallyConfig.get(RallyConfig.RANK_NAMES::get, List.of());
        return rank >= 0 && rank < names.size() ? names.get(rank) : "";
    }

    /**
     * E3A V4/V7/V11: the card part of the Ledger tag - "traits|story|lastReason": traits as
     * comma-separated names, the last 3 story lines as "day^kind^arg" joined by "~", and the
     * guard's last decision reason.
     */
    public static String cardTag(Guard guard) {
        VeterancyRegistry.Record r = record(guard);
        String traits = r == null ? "" : String.join(",", r.traits);
        StringBuilder story = new StringBuilder();
        if (r != null) {
            int from = Math.max(0, r.story.size() - 3);
            for (int i = from; i < r.story.size(); i++) {
                String[] p = r.story.get(i).split("\\|", 3);
                if (p.length < 3) continue;
                if (story.length() > 0) story.append('~');
                story.append(p[0]).append('^').append(p[1]).append('^').append(p[2].replaceAll("[~^|]", " "));
            }
        }
        return traits + "|" + story + "|" + net.hfstack.rallyguard.tactics.DecisionTrace.last(guard.getUUID()).name();
    }

    /** The Ledger row's command field: "rank|captain (0/1)|configured name". */
    public static String ledgerTag(Guard guard, boolean captain) {
        int rank = rank(guard);
        return rank + "|" + (captain ? 1 : 0) + "|" + rankName(rank).replace("|", "/");
    }
}
