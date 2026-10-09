package net.hfstack.rallyguard.formation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E3B G1 ({@code docs/GRUPOS_Y_SUBGRUPOS.md} §2) - the GEOMETRY of a formation, as parameters
 * on top of a {@link FormationShape} (never new shapes):
 * <ul>
 *     <li><b>density</b> - CERRADA (spacing ×0.6, shields together), NORMAL (×1), ABIERTA
 *     (×1.6, spreads fire and explosions);</li>
 *     <li><b>load</b> - how the weight is split over (left, centre, right): the generator gives
 *     more depth (more rows) where the weight is and fewer slots where it isn't; who stands in
 *     the loaded zone is decided by role ({@link #exposureOrder}: the toughest first, shooters
 *     behind);</li>
 *     <li><b>gaps</b> - lanes (lateral position, width 2-6 blocks) where no guard stands, so
 *     another unit can pass through (Battle Planner {@code OPEN_LANE}); closing a lane = a
 *     layout without it.</li>
 * </ul>
 * Pure geometry in the shape's local space (right, forward), exactly like
 * {@link FormationShape#localOffsets}: always returns {@code count} offsets, any count, and
 * the DEFAULT layout returns the shape's offsets untouched (existing formations don't move).
 */
public record FormationLayout(Density density, Load load, List<Gap> gaps) {

    public enum Density {
        CERRADA(0.6D), NORMAL(1.0D), ABIERTA(1.6D);

        public final double factor;

        Density(double factor) {
            this.factor = factor;
        }
    }

    /** Weight of (left, centre, right); normalized when used. */
    public enum Load {
        EQUILIBRADA(1, 1, 1), CENTRO(0.6D, 1.8D, 0.6D), ALA_IZQUIERDA(1.8D, 0.8D, 0.4D),
        ALA_DERECHA(0.4D, 0.8D, 1.8D), AMBAS_ALAS(1.4D, 0.2D, 1.4D);

        final double[] weights;

        Load(double left, double centre, double right) {
            this.weights = new double[]{left, centre, right};
        }

        /** Normalized weight of zone 0 (left), 1 (centre), 2 (right). */
        public double share(int zone) {
            return weights[zone] / (weights[0] + weights[1] + weights[2]);
        }
    }

    /** A lane: centred {@code lateral} blocks to the formation's right (negative = left), {@code width} wide. */
    public record Gap(double lateral, double width) {
        public Gap {
            width = Math.max(2.0D, Math.min(6.0D, width));
        }
    }

    public static final FormationLayout DEFAULT = new FormationLayout(Density.NORMAL, Load.EQUILIBRADA, List.of());

    public FormationLayout {
        gaps = gaps == null ? List.of() : List.copyOf(gaps);
        if (density == null) density = Density.NORMAL;
        if (load == null) load = Load.EQUILIBRADA;
    }

    public boolean isDefault() {
        return density == Density.NORMAL && load == Load.EQUILIBRADA && gaps.isEmpty();
    }

    public FormationLayout withDensity(Density d) {
        return new FormationLayout(d, load, gaps);
    }

    public FormationLayout withLoad(Load l) {
        return new FormationLayout(density, l, gaps);
    }

    public FormationLayout withGap(Gap gap) {
        List<Gap> out = new ArrayList<>(gaps);
        if (out.size() < 4) out.add(gap);
        return new FormationLayout(density, load, out);
    }

    public FormationLayout withoutGaps() {
        return new FormationLayout(density, load, List.of());
    }

    /** Spacing actually used between slots. */
    public double spacing(double base) {
        return base * density.factor;
    }

    // ==== Generation ====

    /** {@code count} local {right, forward} offsets of {@code shape} under this layout. */
    public List<double[]> apply(FormationShape shape, int count, double baseSpacing) {
        double s = spacing(baseSpacing);
        List<double[]> offsets = shape.localOffsets(count, s);
        if (count >= 3 && load != Load.EQUILIBRADA) {
            offsets = shape == FormationShape.CIRCLE ? redistributeRing(offsets, s) : redistribute(shape, offsets, count, s);
        }
        for (Gap gap : gaps) {
            for (double[] o : offsets) o[0] += o[0] >= gap.lateral() ? gap.width() / 2.0D : -gap.width() / 2.0D;
        }
        return offsets;
    }

    /** How "deep" a slot is: forward offset (front = high); for a CIRCLE its radius (outer ring = front). */
    static double depthKey(FormationShape shape, double[] o) {
        return shape == FormationShape.CIRCLE ? Math.hypot(o[0], o[1]) : o[1];
    }

    /** Zone 0 (left third), 1 (centre) or 2 (right third) of a lateral offset within [min, max]. */
    static int zone(double lateral, double min, double max) {
        double w = max - min;
        if (w < 1.0E-6) return 1;
        if (lateral < min + w / 3.0D) return 0;
        if (lateral > max - w / 3.0D) return 2;
        return 1;
    }

    /**
     * Moves slots from the light zones to the loaded ones: the rear-most slots of a zone with
     * more than its share are dropped, and rows are added behind the rear row of a zone with
     * less (for a CIRCLE, inward along the same angle). Count is preserved exactly.
     */
    private List<double[]> redistribute(FormationShape shape, List<double[]> base, int count, double s) {
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        for (double[] o : base) {
            min = Math.min(min, o[0]);
            max = Math.max(max, o[0]);
        }
        if (max - min < s * 1.5D) { // a single file: give it a notional width so the wings exist
            min = -s;
            max = s;
        }
        List<List<double[]>> zones = List.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        for (double[] o : base) zones.get(zone(o[0], min, max)).add(o);
        int[] target = targets(count);
        double frontDepth = -Double.MAX_VALUE;
        for (double[] o : base) frontDepth = Math.max(frontDepth, depthKey(shape, o));
        List<double[]> out = new ArrayList<>(count);
        for (int z = 0; z < 3; z++) {
            List<double[]> mine = zones.get(z);
            mine.sort(Comparator.comparingDouble((double[] o) -> -depthKey(shape, o)).thenComparingDouble(o -> o[0]));
            int want = target[z];
            if (mine.size() >= want) {
                out.addAll(mine.subList(0, want));
                continue;
            }
            out.addAll(mine);
            List<double[]> rear = rearRow(shape, mine, s);
            if (rear.isEmpty()) { // an empty zone: start one row at the front, at the zone's centre
                double lateral = min + (max - min) * (z + 0.5D) / 3.0D;
                rear = new ArrayList<>();
                rear.add(new double[]{lateral, shape == FormationShape.CIRCLE ? 0.0D : frontDepth + s});
            }
            int added = 0;
            for (int k = 1; added < want - mine.size() && k < 4096; k++) {
                for (double[] r : rear) {
                    if (added >= want - mine.size()) break;
                    out.add(behind(shape, r, k * s, s));
                    added++;
                }
            }
        }
        return out;
    }

    /**
     * A CIRCLE carries its load as ANGULAR density: each ring keeps its slot count, but the slots
     * are spread by the load's weight per arc (left / centre / right thirds of the ring), and every
     * ring grows by the same factor if the densest arc would put guards closer than 0.75 spacing.
     */
    private List<double[]> redistributeRing(List<double[]> base, double s) {
        Map<Long, Integer> rings = new java.util.TreeMap<>(Comparator.reverseOrder());
        for (double[] o : base) rings.merge(Math.round(Math.hypot(o[0], o[1]) * 100.0D), 1, Integer::sum);
        int steps = 720;
        double[] cdf = new double[steps + 1];
        for (int i = 0; i < steps; i++) {
            double theta = 2 * Math.PI * (i + 0.5D) / steps;
            int z = zone(Math.sin(theta), -1.0D, 1.0D);
            cdf[i + 1] = cdf[i] + load.share(z) / arcFraction(z);
        }
        List<double[]> out = new ArrayList<>(base.size());
        double minArc = Double.MAX_VALUE;
        for (Map.Entry<Long, Integer> ring : rings.entrySet()) {
            double r = ring.getKey() / 100.0D;
            int m = ring.getValue();
            double[] angles = new double[m];
            for (int i = 0; i < m; i++) {
                double target = cdf[steps] * (i + 0.5D) / m;
                int k = java.util.Arrays.binarySearch(cdf, target);
                if (k < 0) k = -k - 2;
                k = Math.max(0, Math.min(steps - 1, k));
                double frac = (target - cdf[k]) / Math.max(1.0E-12, cdf[k + 1] - cdf[k]);
                angles[i] = 2 * Math.PI * (k + frac) / steps;
            }
            for (int i = 0; i < m && m > 1 && r > 0; i++) {
                double next = i + 1 < m ? angles[i + 1] : angles[0] + 2 * Math.PI;
                minArc = Math.min(minArc, r * (next - angles[i]));
            }
            for (int i = 0; i < m; i++) out.add(new double[]{r * Math.sin(angles[i]), r * Math.cos(angles[i])});
        }
        double grow = minArc == Double.MAX_VALUE ? 1.0D : Math.max(1.0D, 0.75D * s / minArc);
        if (grow > 1.0D) for (double[] o : out) {
            o[0] *= grow;
            o[1] *= grow;
        }
        return out;
    }

    /** Share of a ring's circumference in zone {@code z} when zones split sin(θ) in thirds. */
    private static double arcFraction(int z) {
        double centre = 4.0D * Math.asin(1.0D / 3.0D) / (2 * Math.PI);
        return z == 1 ? centre : (1.0D - centre) / 2.0D;
    }

    /** Largest-remainder split of {@code count} by the load's shares. */
    int[] targets(int count) {
        int[] t = new int[3];
        double[] rem = new double[3];
        int sum = 0;
        for (int z = 0; z < 3; z++) {
            double exact = count * load.share(z);
            t[z] = (int) Math.floor(exact);
            rem[z] = exact - t[z];
            sum += t[z];
        }
        while (sum < count) {
            int best = 0;
            for (int z = 1; z < 3; z++) if (rem[z] > rem[best]) best = z;
            t[best]++;
            rem[best] = -1;
            sum++;
        }
        return t;
    }

    private static List<double[]> rearRow(FormationShape shape, List<double[]> zone, double s) {
        List<double[]> out = new ArrayList<>();
        if (zone.isEmpty()) return out;
        double rear = Double.MAX_VALUE;
        for (double[] o : zone) rear = Math.min(rear, depthKey(shape, o));
        for (double[] o : zone) if (depthKey(shape, o) <= rear + s * 0.25D) out.add(o);
        return out;
    }

    /** A new slot {@code back} blocks behind {@code o} (inward for a ring, never through its centre). */
    private static double[] behind(FormationShape shape, double[] o, double back, double s) {
        if (shape == FormationShape.CIRCLE) {
            double r = Math.hypot(o[0], o[1]);
            if (r - back >= s * 0.75D) {
                double k = (r - back) / r;
                return new double[]{o[0] * k, o[1] * k};
            }
        }
        return new double[]{o[0], o[1] - back};
    }

    /**
     * Slot indices from the most exposed to the least: front first, and within the same row the
     * loaded zone first. Used to hand the toughest guards the exposed slots and the shooters the
     * rear ones (callers sort their guards by toughness and zip).
     */
    public List<Integer> exposureOrder(FormationShape shape, List<double[]> offsets) {
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        for (double[] o : offsets) {
            min = Math.min(min, o[0]);
            max = Math.max(max, o[0]);
        }
        final double lo = min, hi = max;
        List<Integer> idx = new ArrayList<>(offsets.size());
        for (int i = 0; i < offsets.size(); i++) idx.add(i);
        idx.sort(Comparator.comparingDouble((Integer i) -> -(depthKey(shape, offsets.get(i))
                + load.share(zone(offsets.get(i)[0], lo, hi)) * 0.5D)).thenComparingInt(i -> i));
        return idx;
    }

    // ==== Text form (saved with the formation, sent in orders) ====

    /** "DENSITY:LOAD[:lateral/width,lateral/width]" - e.g. "CERRADA:CENTRO" or "NORMAL:EQUILIBRADA:0/4". */
    public String encode() {
        StringBuilder sb = new StringBuilder(density.name()).append(':').append(load.name());
        if (!gaps.isEmpty()) {
            sb.append(':');
            for (int i = 0; i < gaps.size(); i++) {
                if (i > 0) sb.append(',');
                sb.append(String.format(Locale.ROOT, "%.1f/%.1f", gaps.get(i).lateral(), gaps.get(i).width()));
            }
        }
        return sb.toString();
    }

    /** Lenient parse: anything unreadable falls back to the default part. */
    public static FormationLayout parse(String text) {
        if (text == null || text.isBlank()) return DEFAULT;
        String[] p = text.trim().split(":");
        Density d = Density.NORMAL;
        Load l = Load.EQUILIBRADA;
        List<Gap> gaps = new ArrayList<>();
        try {
            d = Density.valueOf(p[0].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            // keep NORMAL
        }
        if (p.length > 1) {
            try {
                l = Load.valueOf(p[1].toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                // keep EQUILIBRADA
            }
        }
        if (p.length > 2) {
            for (String g : p[2].split(",")) {
                String[] lw = g.split("/");
                if (lw.length != 2) continue;
                try {
                    gaps.add(new Gap(Double.parseDouble(lw[0]), Double.parseDouble(lw[1])));
                } catch (NumberFormatException ignored) {
                    // skip a broken lane
                }
                if (gaps.size() >= 4) break;
            }
        }
        return new FormationLayout(d, l, gaps);
    }

    public static Density nextDensity(Density d) {
        return Density.values()[(d.ordinal() + 1) % Density.values().length];
    }

    public static Load nextLoad(Load l) {
        return Load.values()[(l.ordinal() + 1) % Load.values().length];
    }
}
