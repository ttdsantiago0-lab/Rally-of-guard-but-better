package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.contract.ForceRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/** E3A A3 - force maneuvers (filled in by block A3). */
public final class Operations {
    private Operations() {
    }

    static void step(ServerLevel level, ForceRegistry.Force force, List<Forces.UnitView> free, Map<String, String> roles,
                     Map<String, SituationReports.Report> reports, long now) {
    }

    static void resume(ServerLevel level, ForceRegistry.Force force, Forces.UnitView unit, String unitKey) {
    }

    /** Yaw facing from {@code from} toward {@code to} (Formation convention), 0 if unknown. */
    static float facing(@Nullable Vec3 from, @Nullable Vec3 to) {
        if (from == null || to == null) return 0.0F;
        double dx = to.x - from.x, dz = to.z - from.z;
        return dx * dx + dz * dz < 1.0E-4 ? 0.0F : (float) Math.toDegrees(Math.atan2(-dx, dz));
    }

    /** {@code from} moved {@code dist} blocks directly away from {@code enemy} (or not at all). */
    static Vec3 away(Vec3 from, @Nullable Vec3 enemy, double dist) {
        if (enemy == null) return from;
        Vec3 d = from.subtract(enemy).multiply(1, 0, 1);
        return d.lengthSqr() < 1.0E-4 ? from : from.add(d.normalize().scale(dist));
    }

    static void clearAll() {
    }
}
