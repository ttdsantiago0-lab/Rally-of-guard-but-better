package net.hfstack.rallyguard.combat;

/**
 * E1-G: arrow ballistics, matching vanilla {@code AbstractArrow}: each tick the arrow
 * moves by its velocity, then the velocity is multiplied by 0.99 (drag in air) and
 * 0.05 is taken off its vertical part (gravity).
 * <p>
 * {@link #solve} finds the launch pitch that lands an arrow fired at {@code speed} on a
 * point {@code dist} blocks away and {@code dy} blocks higher, by bisection (at most
 * {@link #ITERATIONS} trajectory simulations). {@code high = true} picks the steep arc
 * (a volley over walls and hills); false the flat one (an aimed long shot).
 */
public final class Ballistics {
    private Ballistics() {
    }

    public static final int ITERATIONS = 8;
    private static final double GRAVITY = 0.05D;
    private static final double DRAG = 0.99D;
    private static final int MAX_TICKS = 300;
    /** Pitch of the longest shot with this drag (a little under 45 degrees). */
    private static final double APEX = Math.toRadians(42.0D);

    /** Result: pitch in radians and flight time in ticks; {@code null} if out of reach. */
    public record Shot(double pitch, int ticks) {
    }

    public static Shot solve(double dist, double dy, double speed, boolean high) {
        double lo = high ? APEX : Math.toRadians(-30.0D);
        double hi = high ? Math.toRadians(85.0D) : APEX;
        double[] atApex = heightAt(dist, speed, APEX);
        if (atApex == null || atApex[0] < dy) return null; // even the best arc falls short
        double pitch = (lo + hi) / 2.0D;
        int ticks = (int) atApex[1];
        for (int i = 0; i < ITERATIONS; i++) {
            pitch = (lo + hi) / 2.0D;
            double[] r = heightAt(dist, speed, pitch);
            boolean over = r != null && r[0] > dy;
            if (r != null) ticks = (int) r[1];
            // Low arc: more pitch -> higher at the target. High arc: more pitch -> lower.
            if (high == over) lo = pitch; else hi = pitch;
        }
        return new Shot(pitch, Math.max(1, ticks));
    }

    private static final java.util.Map<Integer, Double> MAX_RANGE = new java.util.HashMap<>();

    /**
     * E3A F3: the furthest an arrow launched at {@code speed} lands back at its launch
     * height (best arc, same drag and gravity as above) - the weapon's PHYSICAL reach, used
     * for volleys instead of a configured cap. Cached per 0.01 of speed.
     */
    public static double maxRange(double speed) {
        int key = (int) Math.round(speed * 100.0D);
        return MAX_RANGE.computeIfAbsent(key, k -> {
            double vx = speed * Math.cos(APEX), vy = speed * Math.sin(APEX), x = 0, y = 0;
            for (int t = 0; t < MAX_TICKS; t++) {
                x += vx;
                y += vy;
                vx *= DRAG;
                vy = vy * DRAG - GRAVITY;
                if (y < 0 && vy < 0) return x;
            }
            return x;
        });
    }

    /** {height, ticks} when the arrow has travelled {@code dist} horizontally, or null
     *  if it never gets that far. */
    private static double[] heightAt(double dist, double speed, double pitch) {
        double vx = speed * Math.cos(pitch);
        double vy = speed * Math.sin(pitch);
        double x = 0.0D, y = 0.0D;
        for (int t = 1; t <= MAX_TICKS; t++) {
            double nx = x + vx, ny = y + vy;
            if (nx >= dist) {
                double f = vx <= 0 ? 0 : (dist - x) / vx;
                return new double[]{y + vy * f, t};
            }
            x = nx;
            y = ny;
            vx *= DRAG;
            vy = vy * DRAG - GRAVITY;
            if (vx < 1.0E-3) return null;
        }
        return null;
    }
}
