package net.hfstack.rallyguard.formation;

import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * E2A block 1 - where each SUBUNIT of a large formation stands (FEEDBACK_E1 B10). The
 * army keeps the ordered shape at two scales: every subunit forms the shape around its
 * own anchor, and the subunit anchors themselves are laid out with the same shape,
 * spaced by the largest subunit's footprint plus two slots - a V of 200 becomes a V of
 * small V's, each one walkable and each with its own captain.
 */
public final class UnitLayout {
    private UnitLayout() {
    }

    /** One anchor per subunit (index 0 = {@code anchor}, the front / apex). */
    public static List<Vec3> anchors(FormationShape shape, double spacing, Vec3 anchor, float facingYaw, int[] sizes) {
        if (sizes.length <= 1) return List.of(anchor);
        double footprint = 0.0D;
        for (int size : sizes) footprint = Math.max(footprint, footprint(shape, spacing, size));
        double macroSpacing = footprint + spacing * 2.0D;
        return new Formation(shape, macroSpacing).positions(anchor, facingYaw, sizes.length);
    }

    /** Larger of the width and depth (blocks) one subunit of {@code size} occupies. */
    static double footprint(FormationShape shape, double spacing, int size) {
        double minR = 0, maxR = 0, minF = 0, maxF = 0;
        for (double[] o : shape.localOffsets(size, spacing)) {
            minR = Math.min(minR, o[0]);
            maxR = Math.max(maxR, o[0]);
            minF = Math.min(minF, o[1]);
            maxF = Math.max(maxF, o[1]);
        }
        return Math.max(maxR - minR, maxF - minF);
    }
}
