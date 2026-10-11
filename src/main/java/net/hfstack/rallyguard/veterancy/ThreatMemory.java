package net.hfstack.rallyguard.veterancy;

import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.VeterancyRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E3B V2 - the "survival" path with real effects: THREAT MEMORY by enemy type, saved per guard
 * UUID ({@link VeterancyRegistry.Record#dread}). What hurt a guard badly weighs more: each hit adds
 * 1.5 × (damage / max health), a hit that leaves it under a quarter of its health adds 0.25 more,
 * capped at 1. It FADES by half every {@code threatMemoryHalfLifeDays} game days and only the 16
 * strongest types are kept. Ranks 3-4 (the ladder) and 5 (the advanced core's threat assessment)
 * read it for the current target: a ranged guard keeps more distance, a melee guard steps back
 * instead of trading blows, and everyone retreats a little sooner from it - preferences only, never
 * health, damage or speed. The survival path (past rank 5) deepens it: a seasoned survivor reads the
 * same memory with up to +30 % weight.
 */
public final class ThreatMemory {
    private ThreatMemory() {
    }

    public static final int MAX_TYPES = 16;

    public static boolean enabled() {
        return RallyConfig.on(RallyConfig.THREAT_MEMORY);
    }

    static String typeOf(LivingEntity e) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).toString();
    }

    /** A real enemy hurt this guard for {@code amount}. */
    public static void hurt(ServerLevel level, Guard guard, LivingEntity enemy, float amount) {
        if (!enabled() || amount <= 0.0F) return;
        String type = typeOf(enemy);
        double add = 1.5D * amount / Math.max(1.0F, guard.getMaxHealth());
        if (guard.getHealth() < guard.getMaxHealth() * 0.25F) add += 0.25D;
        VeterancyRegistry.Record r = Veterancy.edit(level, guard.getUUID());
        long now = level.getGameTime();
        double[] cur = r.dread.get(type);
        double v = cur == null ? 0.0D : faded(cur, now);
        r.dread.put(type, new double[]{Math.min(1.0D, v + add), now});
        if (r.dread.size() > MAX_TYPES) {
            String weakest = null;
            double low = Double.MAX_VALUE;
            for (Map.Entry<String, double[]> e : r.dread.entrySet()) {
                double f = faded(e.getValue(), now);
                if (f < low) {
                    low = f;
                    weakest = e.getKey();
                }
            }
            if (weakest != null) r.dread.remove(weakest);
        }
    }

    private static double faded(double[] entry, long now) {
        double days = Math.max(0.0D, (now - entry[1]) / 24000.0D);
        double half = Math.max(1, RallyConfig.i(RallyConfig.THREAT_MEMORY_HALF_LIFE_DAYS, 3));
        return entry[0] * Math.pow(0.5D, days / half);
    }

    /** 0..1: how much this guard dreads {@code enemy}'s type now (0 if memory is off or never hurt by it). */
    public static double of(Guard guard, LivingEntity enemy) {
        if (!enabled()) return 0.0D;
        VeterancyRegistry.Record r = Veterancy.record(guard);
        if (r == null || r.dread.isEmpty()) return 0.0D;
        double[] e = r.dread.get(typeOf(enemy));
        if (e == null) return 0.0D;
        double weight = 1.0D + 0.3D * Veterancy.pathShare(guard, VeterancyRegistry.Path.SURVIVAL);
        return Math.min(1.0D, faded(e, guard.level().getGameTime()) * weight);
    }

    /** The {@code n} strongest memories as "type:percent" (for the card and {@code /rally debug why}). */
    public static List<String> top(Guard guard, int n) {
        List<String> out = new ArrayList<>();
        VeterancyRegistry.Record r = Veterancy.record(guard);
        if (r == null || r.dread.isEmpty()) return out;
        long now = guard.level().getGameTime();
        List<Map.Entry<String, double[]>> list = new ArrayList<>(r.dread.entrySet());
        list.sort((a, b) -> Double.compare(faded(b.getValue(), now), faded(a.getValue(), now)));
        for (Map.Entry<String, double[]> e : list) {
            if (out.size() >= n) break;
            int pct = (int) Math.round(faded(e.getValue(), now) * 100);
            if (pct <= 0) continue;
            String type = e.getKey();
            int colon = type.indexOf(':');
            out.add((colon >= 0 && type.startsWith("minecraft") ? type.substring(colon + 1) : type).toLowerCase(Locale.ROOT) + ":" + pct);
        }
        return out;
    }
}
