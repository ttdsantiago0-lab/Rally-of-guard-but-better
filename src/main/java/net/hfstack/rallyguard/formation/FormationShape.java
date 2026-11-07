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
 * <p>
 * <b>Phase 3G - every shape scales</b> (user brief: 5, 50, hundreds of guards with the
 * same system). A shape is a rule for ANY count, not a picture for 8 guards: LINE wraps
 * into ranks of at most {@link #MIN_RANK_WIDTH} (extra guards stand in rear ranks
 * behind the front line), COLUMN widens into up to {@link #MIN_COLUMN_FILES} files,
 * V stays a hollow chevron for a small group and becomes a filled wedge beyond
 * {@link #HOLLOW_V_MAX}, CIRCLE adds concentric rings instead of an ever-wider single
 * ring. Slot 0 is always the front/leader position, so manual slot assignment (a future
 * "Command Staff") can map guards onto the same slot indices at any size. Small groups
 * get exactly the same layout as before (existing remembered formations don't move).
 */
public enum FormationShape {
    /** Side by side, perpendicular to facing, centered on the anchor; beyond
     *  {@link #MIN_RANK_WIDTH} guards, further ranks form up behind the first. */
    LINE {
        @Override
        public List<double[]> localOffsets(int count, double spacing) {
            List<double[]> offsets = new ArrayList<>(count);
            // Wide enough to stay roughly 3:1 (width:depth) for big armies, never narrower
            // than MIN_RANK_WIDTH - so up to that many guards it is one single line.
            int width = Math.max(MIN_RANK_WIDTH, (int) Math.ceil(Math.sqrt(count * 3.0)));
            int placed = 0;
            int rank = 0;
            while (placed < count) {
                int inRank = Math.min(width, count - placed);
                double mid = (inRank - 1) / 2.0;
                for (int i = 0; i < inRank; i++) {
                    offsets.add(new double[]{spacing * (i - mid), -spacing * rank});
                }
                placed += inRank;
                rank++;
            }
            return offsets;
        }
    },

    /** One behind another along the facing axis, centered on the anchor - a marching
     *  column, front first; a large column marches several files abreast (at most
     *  {@link #MIN_COLUMN_FILES}) so it doesn't stretch hundreds of blocks. */
    COLUMN {
        @Override
        public List<double[]> localOffsets(int count, double spacing) {
            List<double[]> offsets = new ArrayList<>(count);
            if (count == 0) return offsets;
            int maxFiles = Math.max(MIN_COLUMN_FILES, (int) Math.ceil(Math.sqrt(count / 4.0)));
            int files = Math.min(maxFiles, Math.max(1, (int) Math.ceil(count / (double) COLUMN_FILE_LENGTH)));
            int rows = (int) Math.ceil(count / (double) files);
            double rowMid = (rows - 1) / 2.0;
            double fileMid = (files - 1) / 2.0;
            for (int i = 0; i < count; i++) {
                int row = i / files;
                int file = i % files;
                offsets.add(new double[]{spacing * (file - fileMid), spacing * (rowMid - row)});
            }
            return offsets;
        }
    },

    /** A chevron: member 0 is the apex at the anchor, pointing in the facing direction;
     *  each following pair trails one rank further back and further out. Beyond
     *  {@link #HOLLOW_V_MAX} guards it becomes a filled wedge (rank r holds 2r+1 guards),
     *  so a large army stays a compact spearhead instead of two endless arms. */
    V {
        @Override
        public List<double[]> localOffsets(int count, double spacing) {
            List<double[]> offsets = new ArrayList<>(count);
            if (count == 0) return offsets;
            if (count <= HOLLOW_V_MAX) {
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
            int rank = 0;
            while (offsets.size() < count) {
                offsets.add(new double[]{0.0, -rank * spacing});
                for (int k = 1; k <= rank && offsets.size() < count; k++) {
                    offsets.add(new double[]{k * spacing, -rank * spacing});
                    if (offsets.size() < count) {
                        offsets.add(new double[]{-k * spacing, -rank * spacing});
                    }
                }
                rank++;
            }
            return offsets;
        }
    },

    /** A ring centered on the anchor, member 0 directly forward (the leader spot). Up to
     *  {@link #RING_CAPACITY} guards form one ring; larger groups form concentric rings,
     *  outermost first, so the perimeter is always manned before the inside. A single
     *  guard sits exactly at the anchor. */
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
            if (count <= RING_CAPACITY) {
                // Phase 3H: at least `spacing` of radius - a 3-guard "circle" used to be
                // 1.25 blocks across, so guards crowded each other's slot blocks and kept
                // shuffling (user review 2026-09-30).
                addRing(offsets, count, Math.max(spacing, spacing * count / (2 * Math.PI)));
                return offsets;
            }
            // Several concentric rings, `spacing` apart, members `spacing` apart along each
            // ring: grow the outer radius until all the rings together can hold everyone,
            // then man them from the outside in (the center stays open).
            double outer = spacing * RING_CAPACITY / (2 * Math.PI);
            while (ringCapacity(outer, spacing) < count) {
                outer += spacing;
            }
            int remaining = count;
            for (double radius = outer; remaining > 0 && radius >= spacing; radius -= spacing) {
                int inRing = Math.min(Math.max(1, (int) Math.floor(2 * Math.PI * radius / spacing)), remaining);
                addRing(offsets, inRing, radius);
                remaining -= inRing;
            }
            return offsets;
        }

        private int ringCapacity(double outer, double spacing) {
            int total = 0;
            for (double radius = outer; radius >= spacing; radius -= spacing) {
                total += Math.max(1, (int) Math.floor(2 * Math.PI * radius / spacing));
            }
            return total;
        }

        private void addRing(List<double[]> offsets, int n, double radius) {
            for (int i = 0; i < n; i++) {
                double theta = 2 * Math.PI * i / n;
                offsets.add(new double[]{radius * Math.sin(theta), radius * Math.cos(theta)});
            }
        }
    },

    /**
     * 2026-11-06 testudo (David: "una formación romana en que todos avanzan con escudos arriba";
     * {@code combat/Testudo}): a tight block, ONE block between guards whatever the spacing asked, about twice as wide
     * as deep (8 guards: two ranks of four), slot 0 at the middle of the front rank. Shield-bearers in it keep the
     * shields up and hold their place; with a shield made for it they block projectiles from every side.
     */
    TESTUDO {
        @Override
        public List<double[]> localOffsets(int count, double spacing) {
            List<double[]> offsets = new ArrayList<>(count);
            if (count == 0) return offsets;
            double s = Math.min(spacing, 1.0D);
            int width = Math.max(2, (int) Math.ceil(Math.sqrt(count * 2.0D)));
            int placed = 0;
            int rank = 0;
            while (placed < count) {
                int inRank = Math.min(width, count - placed);
                double mid = (inRank - 1) / 2.0D;
                // the middle of each rank first, then outwards, so slot 0 is the front centre
                List<Integer> order = new ArrayList<>();
                for (int k = 0; k < inRank; k++) order.add(k);
                order.sort((a, b) -> Double.compare(Math.abs(a - mid), Math.abs(b - mid)));
                for (int k : order) offsets.add(new double[]{s * (k - mid), -s * rank});
                placed += inRank;
                rank++;
            }
            return offsets;
        }
    };

    /** A LINE of up to this many guards is a single rank; bigger lines grow ranks
     *  behind the first, keeping roughly a 3:1 width:depth. */
    public static final int MIN_RANK_WIDTH = 16;
    /** A COLUMN adds a file for every this many guards... */
    public static final int COLUMN_FILE_LENGTH = 12;
    /** ...up to at least this many files abreast (more for very large columns). */
    public static final int MIN_COLUMN_FILES = 4;
    /** Up to this many guards a V is a hollow chevron; above, a filled wedge. */
    public static final int HOLLOW_V_MAX = 15;
    /** Up to this many guards a CIRCLE is a single ring. */
    public static final int RING_CAPACITY = 24;

    /**
     * {@code count} local {@code {right, forward}} offset pairs (blocks, before
     * rotation/translation) for this shape - always exactly {@code count} of them, for
     * any count (an empty list for 0), so callers never special-case group size.
     */
    public abstract List<double[]> localOffsets(int count, double spacing);
}
