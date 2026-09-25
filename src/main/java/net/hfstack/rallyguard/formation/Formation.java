package net.hfstack.rallyguard.formation;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * Phase 3D: the one reusable formation engine - an anchor position, a facing (yaw
 * degrees), and a {@link FormationShape} configuration, turned into real world
 * positions for however many guards need one. This is deliberately the entire engine;
 * {@link FormationShape} only ever hands back shape-local offsets, this class is the
 * only place that knows how to rotate/translate them into the world.
 * <p>
 * Yaw-to-direction math verified against Minecraft's own documented yaw convention
 * (yaw 0&#176; = south/+Z, yaw increases counter-clockwise viewed from above - i.e.
 * north=-Z, south=+Z, east=+X, west=-X) rather than assumed: forward = (-sin(yaw),
 * cos(yaw)) as (x, z), cross-checked two independent ways before use here - it
 * reproduces the known cardinal mapping at yaw 0/90/180/270 (south/west/north/east),
 * and it is the same horizontal-plane expression as vanilla's own well-established
 * view-vector formula. "Right" (the direction {@link FormationShape}'s positive
 * {@code right} offset moves toward) is forward rotated 90&#176; clockwise viewed from
 * above: {@code right = (-forward.z, forward.x)} - verified geometrically too (facing
 * south, right points west, matching the standard compass-facing/right-hand rule).
 */
public record Formation(FormationShape shape, double spacing) {

    /** Default spacing (blocks) between adjacent formation slots - enough room for two
     *  guards to stand and swing a weapon without crowding. A single named constant so
     *  every caller tunes together; not derived from anything deeper than "a reasonable
     *  starting default." */
    public static final double DEFAULT_SPACING = 2.5;

    /**
     * World-space positions (all sharing {@code anchor}'s Y - formation members are
     * assumed to stand at roughly their anchor's ground level; real terrain and
     * navigation handle the rest, per the "do not build custom pathfinding" instruction)
     * for {@code count} members of this formation, anchored at {@code anchor} and
     * facing {@code facingYawDegrees}. Returned in the same order {@link
     * FormationShape#localOffsets} produced them, so index N is always the same slot
     * for a given (shape, count) pair - that stability is what lets {@code
     * FormationRegistry} remember "guard X was slot N" and recompute the same position
     * again later for Reform.
     */
    public List<Vec3> positions(Vec3 anchor, float facingYawDegrees, int count) {
        double yawRad = Math.toRadians(facingYawDegrees);
        double forwardX = -Math.sin(yawRad);
        double forwardZ = Math.cos(yawRad);
        double rightX = -forwardZ;
        double rightZ = forwardX;

        List<double[]> local = shape.localOffsets(count, spacing);
        List<Vec3> result = new ArrayList<>(local.size());
        for (double[] offset : local) {
            double right = offset[0];
            double forward = offset[1];
            double x = anchor.x + rightX * right + forwardX * forward;
            double z = anchor.z + rightZ * right + forwardZ * forward;
            result.add(new Vec3(x, anchor.y, z));
        }
        return result;
    }
}
