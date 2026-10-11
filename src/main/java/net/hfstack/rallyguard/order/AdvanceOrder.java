package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.formation.Formation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * ADVANCE - the formation moves as a formation (Phase 3G; the §32 note deferred
 * Advance until it could mean something different from Muster - now it does). Since
 * Phase 3H it marches to the marked formation point when one is set (facing the way it
 * travels), otherwise {@link #STEP} blocks straight ahead.
 * <p>
 * Each selected guard's remembered formation ({@link FormationRegistry.Slot}: anchor,
 * facing, shape, spacing, its slot, the group size) is shifted {@link #STEP} blocks
 * along its own facing, and the guard is re-posted on its same slot at the new anchor.
 * Every guard of the formation computes the same shift from the same remembered data,
 * so the whole shape moves together without any new "formation entity" - and a guard
 * that was pulled away by a fight simply rejoins at its advanced slot. Repeat to keep
 * advancing; Retreat to fall back; Reform to re-tighten in place. Guards that never
 * mustered have no formation to advance and are skipped.
 */
public final class AdvanceOrder implements OrderExecutor {

    /** How far the formation advances when no formation point is marked. */
    public static final double STEP = 16.0D;
    private static final double SPEED = 1.0D;

    /**
     * E2A block 1: subunits keep their own anchors. With a marked point the whole army is
     * moved so its CENTRE (mean of the subunit anchors) lands on the mark, every subunit
     * keeping its place relative to that centre, rotated to the new facing - before,
     * every slot was re-anchored on the mark and the subunits collapsed into one. Moves
     * are handed out over the next ticks ({@link SlotQueue}).
     */
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        FormationRegistry registry = FormationRegistry.get(player.serverLevel());
        CommanderMarks.MarkedPosition mark = CommanderMarks.markedPosition(player.getUUID(), player.serverLevel());

        double cx = 0, cz = 0, cy = 0;
        int n = 0;
        java.util.Set<Vec3> distinct = new java.util.HashSet<>();
        FormationRegistry.Slot reference = null;
        for (Guard guard : guards) {
            FormationRegistry.Slot slot = registry.slotOf(guard.getUUID());
            if (slot == null) continue;
            if (reference == null) reference = slot;
            if (distinct.add(slot.anchor())) {
                cx += slot.anchor().x;
                cy += slot.anchor().y;
                cz += slot.anchor().z;
                n++;
            }
        }
        if (reference == null) return;
        Vec3 centre = new Vec3(cx / n, cy / n, cz / n);

        Vec3 newCentre;
        float facing;
        if (mark != null) {
            // Phase 3H: march TO the marked point, facing the direction of travel.
            newCentre = Vec3.atBottomCenterOf(mark.pos());
            double dx = newCentre.x - centre.x;
            double dz = newCentre.z - centre.z;
            facing = (dx * dx + dz * dz) < 1.0E-4 ? reference.facingYaw()
                    : (float) Math.toDegrees(Math.atan2(-dx, dz));
        } else {
            double yawRad = Math.toRadians(reference.facingYaw());
            newCentre = centre.add(-Math.sin(yawRad) * STEP, 0.0D, Math.cos(yawRad) * STEP);
            facing = reference.facingYaw();
        }
        double turn = Math.toRadians(facing - reference.facingYaw());
        double cos = Math.cos(turn), sin = Math.sin(turn);

        for (Guard guard : guards) {
            FormationRegistry.Slot slot = registry.slotOf(guard.getUUID());
            if (slot == null) continue;
            // Rotate the subunit's offset from the centre by the change of facing (yaw grows
            // counter-clockwise seen from above, so (x, z) turns like a standard rotation).
            double rx = slot.anchor().x - centre.x, rz = slot.anchor().z - centre.z;
            double ox = rx * cos - rz * sin, oz = rx * sin + rz * cos;
            Vec3 anchor = new Vec3(newCentre.x + ox, slot.anchor().y + (newCentre.y - centre.y), newCentre.z + oz);
            float slotFacing = mark != null ? facing : slot.facingYaw();
            FormationRegistry.Slot moved = new FormationRegistry.Slot(anchor, slotFacing, slot.shape(),
                    slot.spacing(), slot.index(), slot.count(), slot.layout());

            List<Vec3> positions = slot.formation().positions(anchor, slotFacing, slot.count());
            if (slot.index() < 0 || slot.index() >= positions.size()) continue;

            registry.setSlot(guard.getUUID(), moved);
            net.hfstack.rallyguard.command.SlotQueue.enqueue(guard, positions.get(slot.index()), SPEED);
        }
    }
}
