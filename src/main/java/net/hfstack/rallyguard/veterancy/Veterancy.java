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
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * E2A: the one place every system asks about a guard's veterancy (ranks 0..5,
 * {@code docs/DECISIONES_TACTICAS.md} §1, {@code docs/VETERANIA.md}). The earned rank lives
 * in {@link VeterancyRegistry}; {@code /rally debug rank} test ranks still apply on top
 * (the higher wins) so simulations can set ranks directly.
 * <p>
 * XP comes only from real combat ({@link VeterancyEvents}); each rank gained adds one
 * {@link Trait} chosen from the guard's history and genetics ({@link Affinity}). A rank
 * changes decisions (range, accuracy, command capacity, the decision ladder's rungs), never
 * raw damage; a Master is not immortal. Past rank 5, XP keeps adding command capacity with
 * diminishing returns ({@link #legendBonus}).
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

    public static boolean has(Guard guard, Trait trait) {
        VeterancyRegistry.Record r = record(guard);
        return r != null && r.traits.contains(trait.name());
    }

    /** XP needed to reach {@code rank} (1..5). */
    public static int xpFor(int rank) {
        return RallyConfig.at(RallyConfig.RANK_XP, rank - 1, DEFAULT_XP);
    }

    /** Extra command-capacity steps past rank 5: +1 each time the XP beyond rank 5 doubles (max 10). */
    public static int legendBonus(Guard guard) {
        VeterancyRegistry.Record r = record(guard);
        if (r == null || r.rank < MAX_RANK) return 0;
        double base = xpFor(MAX_RANK);
        double extra = Math.max(0.0D, r.xp - base);
        return Math.min(10, (int) Math.floor(Math.log(1.0D + extra / base) / Math.log(2.0D)));
    }

    /** Adds XP and promotes as far as it reaches, one trait per rank, telling the commander. */
    public static void addXp(ServerLevel level, Guard guard, double amount) {
        if (amount <= 0.0D) return;
        VeterancyRegistry.Record r = edit(level, guard.getUUID());
        r.xp += amount;
        while (r.rank < MAX_RANK && r.xp >= xpFor(r.rank + 1)) {
            r.rank++;
            Trait trait = chooseTrait(r, guard.getUUID(), r.rank);
            if (trait != null) r.traits.add(trait.name());
            announce(level, guard, r.rank, trait);
        }
    }

    private static void announce(ServerLevel level, Guard guard, int rank, @Nullable Trait trait) {
        UUID owner = GuardOwnership.getOwner(guard);
        ServerPlayer commander = owner == null ? null : level.getServer().getPlayerList().getPlayer(owner);
        if (commander == null) return;
        Component rankName = rankComponent(rank);
        Component traitName = trait == null ? Component.literal("-")
                : Component.translatable("gui.rallyguard.trait." + trait.name().toLowerCase(java.util.Locale.ROOT));
        commander.sendSystemMessage(Component.translatable("gui.rallyguard.vet.rank_up",
                guard.getName(), rankName, traitName).withStyle(ChatFormatting.AQUA));
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
    static Trait chooseTrait(VeterancyRegistry.Record r, UUID guardId, int rank) {
        List<Affinity> genes = Affinity.of(guardId);
        Map<Trait, Double> score = new HashMap<>();
        score.put(Trait.TANK, r.nearDeath * 3.0D + r.totems * 4.0D + (genes.contains(Affinity.TOUGH) ? 6 : 0));
        score.put(Trait.MARKSMAN, (r.killsRanged + r.killsGun) * 2.0D);
        score.put(Trait.DUELIST, r.killsMelee * 2.0D);
        score.put(Trait.RIDER, r.secondsMounted / 120.0D);
        score.put(Trait.STEADY, r.battles * 3.0D + (genes.contains(Affinity.CALM) ? 6 : 0)
                - (Affinity.timid(guardId) ? 4 : 0));
        score.put(Trait.ASSAULT, r.killsMelee + (genes.contains(Affinity.CONFIDENT) ? 6.0D : 0.0D));
        score.put(Trait.SCOUT, r.enemyTypes.size() * 2.0D + (genes.contains(Affinity.ATHLETIC) ? 6 : 0));
        Random roll = new Random(guardId.getLeastSignificantBits() + rank * 7919L);
        Trait best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (Trait t : Trait.values()) {
            if (r.traits.contains(t.name())) continue;
            double s = score.get(t) + roll.nextDouble(); // < 1: only breaks ties / near-ties
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
    }

    // ==== Names / Ledger ====

    /** Configured name of a rank ({@code veterancy.rankNames}), or "" for the translated default. */
    public static String rankName(int rank) {
        List<? extends String> names = RallyConfig.get(RallyConfig.RANK_NAMES::get, List.of());
        return rank >= 0 && rank < names.size() ? names.get(rank) : "";
    }

    /** The Ledger row's command field: "rank|captain (0/1)|configured name". */
    public static String ledgerTag(Guard guard, boolean captain) {
        int rank = rank(guard);
        return rank + "|" + (captain ? 1 : 0) + "|" + rankName(rank).replace("|", "/");
    }
}
