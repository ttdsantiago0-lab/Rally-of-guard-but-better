package net.hfstack.rallyguard.formation;

import java.util.ArrayList;
import java.util.List;

/**
 * Phase 3D: one of the configurations the {@link Formation} engine can arrange guards
 * into. Deliberately NOT one class per shape (no {@code LineAI}/{@code ColumnAI}/
 * {@code VAI}) - a shape is nothing but a rule for generating local (right, forward)
 * offsets around an anchor for a given member count and spacing; {@link Formation} does
 * every actual position/rotation/navigation-order that turns those offsets into real
 * guard movement, identically for every shape. Adding a future shape (Wedge, Circle, U,
 * Row, a shield-wall) means adding one more enum constant with its own
 * {@link #localOffsets}, never a new AI system.
 * <p>
 * Local space convention: {@code right} is positive to the formation's right (the same
 * side a marching entity's right hand would be on when facing the formation's facing
 * direction), {@code forward} is positive in the facing direction. {@link Formation}
 * rotates these by the formation's facing yaw and translates them by its anchor -
 * shapes never see world coordinates at all, which is what makes them this simple.
 * <p>
 * Four shapes are implemented so far (LINE, COLUMN, V, CIRCLE) - enough to prove the
 * engine handles a side-by-side shape, a front-to-back shape, an asymmetric pointed
 * shape, and a fully-symmetric radial shape without four different systems. Wedge/U/
 * Row/shield-wall/escort/cavalry-wedge are all future {@link #localOffsets}
 * implementations, not architecture changes.
 */
public enum FormationShape {
    /** Side by side, perpendicular to facing, centered on the anchor:
     *  {@code G G G G G} with the anchor at the middle guard's feet. */
    LINE {
        @Override
        public List<double[]> localOffsets(int count, double spacing) {
            List<double[]> offsets = new ArrayList<>(count);
            double mid = (count - 1) / 2.0;
            for (int i = 0; i < count; i++) {
                offsets.add(new double[]{spacing * (i - mid), 0.0});
            }
            return offsets;
        }
    },

    /** One behind another along the facing axis, centered on the anchor - a marching
     *  column, front guard first:
     *  <pre>
     *  G
     *  G
     *  G
     *  G
     *  </pre> */
    COLUMN {
        @Override
        public List<double[]> localOffsets(int count, double spacing) {
            List<double[]> offsets = new ArrayList<>(count);
            double mid = (count - 1) / 2.0;
            for (int i = 0; i < count; i++) {
                offsets.add(new double[]{0.0, spacing * (mid - i)});
            }
            return offsets;
        }
    },

    /** A wedge/chevron: member 0 is the apex, sitting exactly at the anchor and pointing
     *  in the facing direction; every following pair trails one rank further back and
     *  further out, alternating right then left:
     *  <pre>
     *  G       G
     *    G   G
     *      G
     *  </pre>
     *  (apex at the bottom, facing up the page.) An even count leaves the last rank with
     *  only its right-hand member and no matching left one - a plain, honest reading of
     *  "one more guard than fits symmetrically," not a bug needing special-casing. */
    V {
        @Override
        public List<double[]> localOffsets(int count, double spacing) {
            List<double[]> offsets = new ArrayList<>(count);
            offsets.add(new double[]{0.0, 0.0});
            int rank = 1;
            while (offsets.size() < count) {
                offsets.add(new double[]{rank * spacing, -rank * spacing});
                if (offsets.size() < count) {
                    offsets.add(new double[]{-rank * spacing, -rank * spacing});
                }
                rank++;
            }
            return offsets;
        }
    },

    /** A ring centered on the anchor, member 0 directly forward (the "leader" spot,
     *  same forward-of-anchor convention V's apex uses) and the rest spaced evenly
     *  clockwise around the circle from there:
     *  <pre>
     *      G
     *  G       G
     *  G       G
     *      G
     *  </pre>
     *  Radius is picked so adjacent members stay at least {@code spacing} apart along
     *  the ring for larger groups ({@code spacing * count / (2*PI)}), with a floor of
     *  {@code spacing / 2} so small counts (2-3 guards) don't collapse toward the
     *  center. A single guard sits exactly at the anchor rather than on a
     *  zero-reason-to-exist ring of its own. */
    CIRCLE {
        @Override
        public List<double[]> localOffsets(int count, double spacing) {
            List<double[]> offsets = new ArrayList<>(count);
            if (count == 0) {
                return offsets;
            }
            if (count == 1) {
                offsets.add(new double[]{0.0, 0.0});
                return offsets;
            }
            double radius = Math.max(spacing / 2.0, spacing * count / (2 * Math.PI));
            for (int i = 0; i < count; i++) {
                double theta = 2 * Math.PI * i / count;
                offsets.add(new double[]{radius * Math.sin(theta), radius * Math.cos(theta)});
            }
            return offsets;
        }
    };

    /**
     * {@code count} local {@code {right, forward}} offset pairs (blocks, before
     * rotation/translation) for this shape. Every implementation above naturally
     * returns an empty list for {@code count == 0} (their loops simply don't run), so
     * callers don't need to special-case an empty selection.
     */
    public abstract List<double[]> localOffsets(int count, double spacing);
}
