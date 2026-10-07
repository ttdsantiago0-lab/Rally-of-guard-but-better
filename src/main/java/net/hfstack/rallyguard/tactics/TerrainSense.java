package net.hfstack.rallyguard.tactics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * E2A block 4 - terrain valuation exactly as the playbook §3: computed only for a squad led
 * by a Veteran (rank ≥ 2), at most once a second per squad, cached {@link #CACHE_TICKS}
 * (3 s), over at most {@link #CANDIDATES} (8) positions 6 blocks around the leader, with at
 * most {@link #READS} (12) block reads per candidate - no raycasts, no volume walks. An
 * explosion within 24 blocks invalidates a squad's reading (craters then read as cover).
 * <p>
 * Reads per candidate: standable (2) · cover, 3 samples toward the enemy at chest height (3)
 * · water below (1) · sky (1) · left/right walls at chest (2) and feet (2) height,
 * perpendicular to the enemy axis · one forest sample (1) = 12.
 */
public final class TerrainSense {
    private TerrainSense() {
    }

    public static final int CANDIDATES = 8;
    public static final int READS = 12;
    static final int CACHE_TICKS = 60;
    static final double RADIUS = 6.0D;

    public enum Tag { COVER_GOOD, COVER_BAD, HEIGHT_GOOD, HEIGHT_BAD, DEFENSIBLE, OPEN, NEAR_WATER, IN_FOREST, CHOKEPOINT }

    public record Candidate(BlockPos pos, EnumSet<Tag> tags, double score) {
    }

    /** @param best the best-scoring candidate with cover or a defensible spot (null = none) */
    public record Reading(long tick, Vec3 origin, List<Candidate> candidates, @Nullable Candidate best) {
    }

    private static final Map<String, Reading> CACHE = new HashMap<>();

    /**
     * The squad's reading, computed now if the cached one is older than 3 s (or an explosion
     * dropped it). Callers ask at most once a second per squad. {@code enemy} = where the
     * known enemy is.
     */
    @Nullable
    public static Reading read(ServerLevel level, String squadKey, Vec3 origin, Vec3 enemy, boolean ranged,
                               Doctrine doctrine, long now) {
        Reading cached = CACHE.get(squadKey);
        if (cached != null && now - cached.tick() < CACHE_TICKS) return cached;
        Reading fresh = compute(level, origin, enemy, ranged, doctrine, now);
        CACHE.put(squadKey, fresh);
        return fresh;
    }

    static Reading compute(ServerLevel level, Vec3 origin, Vec3 enemy, boolean ranged, Doctrine doctrine, long now) {
        List<Candidate> out = new ArrayList<>(CANDIDATES);
        Candidate best = null;
        double ex = enemy.x - origin.x, ez = enemy.z - origin.z;
        double len = Math.max(1.0E-3, Math.sqrt(ex * ex + ez * ez));
        double fx = ex / len, fz = ez / len;      // toward the enemy
        double px = -fz, pz = fx;                  // perpendicular
        for (int i = 0; i < CANDIDATES; i++) {
            double a = Math.PI * 2.0D * i / CANDIDATES;
            BlockPos feet = BlockPos.containing(origin.x + Math.cos(a) * RADIUS, origin.y, origin.z + Math.sin(a) * RADIUS);
            // 2 reads: standable here, or one block up.
            if (!level.getBlockState(feet).isAir()) {
                feet = feet.above();
                if (!level.getBlockState(feet).isAir()) continue;
            } else if (level.getBlockState(feet.below()).isAir()) {
                feet = feet.below();
            }
            EnumSet<Tag> tags = EnumSet.noneOf(Tag.class);
            // 3 reads: cover toward the enemy.
            boolean front = false, cover = false;
            for (int k = 1; k <= 3; k++) {
                BlockPos p = BlockPos.containing(feet.getX() + 0.5 + fx * k, feet.getY() + 1, feet.getZ() + 0.5 + fz * k);
                boolean solid = solid(level.getBlockState(p));
                if (solid) cover = true;
                if (k == 1) front = solid;
            }
            tags.add(cover ? Tag.COVER_GOOD : Tag.COVER_BAD);
            // 0 reads: height against the enemy.
            double dy = feet.getY() - enemy.y;
            if (dy >= 3) tags.add(Tag.HEIGHT_GOOD);
            else if (dy <= -3) tags.add(Tag.HEIGHT_BAD);
            // 1 read: water.
            if (!level.getFluidState(feet.below()).isEmpty() || !level.getFluidState(feet).isEmpty()) tags.add(Tag.NEAR_WATER);
            // 1 read: sky; 2 reads: side walls at chest height; with the front sample = defensible.
            boolean roofed = !level.canSeeSky(feet.above());
            BlockPos left = BlockPos.containing(feet.getX() + 0.5 + px * 2, feet.getY(), feet.getZ() + 0.5 + pz * 2);
            BlockPos right = BlockPos.containing(feet.getX() + 0.5 - px * 2, feet.getY(), feet.getZ() + 0.5 - pz * 2);
            boolean wallL = solid(level.getBlockState(left.above())), wallR = solid(level.getBlockState(right.above()));
            int sides = (wallL ? 1 : 0) + (wallR ? 1 : 0) + (front ? 1 : 0);
            tags.add(roofed && sides >= 2 ? Tag.DEFENSIBLE : Tag.OPEN);
            // 2 reads: narrow passage (walls on both sides at feet height).
            if (solid(level.getBlockState(left)) && solid(level.getBlockState(right))) tags.add(Tag.CHOKEPOINT);
            // 1 read: forest.
            BlockState tree = level.getBlockState(feet.offset(3, 2, 3));
            if (tree.is(BlockTags.LOGS) || tree.is(BlockTags.LEAVES)) tags.add(Tag.IN_FOREST);

            Candidate c = new Candidate(feet, tags, score(tags, ranged, doctrine));
            out.add(c);
            if ((tags.contains(Tag.COVER_GOOD) || tags.contains(Tag.DEFENSIBLE)) && (best == null || c.score() > best.score())) {
                best = c;
            }
        }
        return new Reading(now, origin, out, best != null && best.score() > 0 ? best : null);
    }

    /** −2..+2 from the tags, weighted by the doctrine (playbook §3 "Reglas de uso"). */
    static double score(EnumSet<Tag> tags, boolean ranged, Doctrine d) {
        double s = 0;
        if (tags.contains(Tag.COVER_GOOD)) s += (ranged ? 1.0D : 0.5D) * d.cover;
        if (tags.contains(Tag.COVER_BAD)) s -= 0.25D * d.cover;
        if (tags.contains(Tag.HEIGHT_GOOD)) s += (ranged ? 1.0D : 0.5D) * d.height;
        if (tags.contains(Tag.HEIGHT_BAD)) s -= (ranged ? 1.0D : 0.25D) * d.height;
        if (tags.contains(Tag.DEFENSIBLE)) s += 0.5D * d.defensible;
        if (tags.contains(Tag.CHOKEPOINT)) s += 0.5D * d.chokepoint;
        if (tags.contains(Tag.IN_FOREST)) s += 0.5D * d.forest - (ranged ? 0.25D : 0.0D);
        if (tags.contains(Tag.NEAR_WATER)) s -= ranged ? 0.5D : 0.0D;
        return Math.max(-2.0D, Math.min(2.0D, s * d.terrainWeight));
    }

    private static boolean solid(BlockState s) {
        return !s.isAir() && s.getFluidState().isEmpty() && !s.is(BlockTags.LEAVES) && s.blocksMotion();
    }

    /** An explosion: every squad reading taken within 24 blocks of it is dropped. */
    public static void invalidateNear(Vec3 at) {
        Iterator<Map.Entry<String, Reading>> it = CACHE.entrySet().iterator();
        while (it.hasNext()) {
            if (it.next().getValue().origin().distanceToSqr(at) < 24.0D * 24.0D) it.remove();
        }
    }

    public static void purge(long now) {
        CACHE.values().removeIf(r -> now - r.tick() > 20L * 30L);
    }

    public static void clearAll() {
        CACHE.clear();
    }
}
