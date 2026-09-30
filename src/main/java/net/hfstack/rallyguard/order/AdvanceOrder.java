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

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        FormationRegistry registry = FormationRegistry.get(player.serverLevel());
        CommanderMarks.MarkedPosition mark = CommanderMarks.markedPosition(player.getUUID(), player.serverLevel());
        for (Guard guard : guards) {
            FormationRegistry.Slot slot = registry.slotOf(guard.getUUID());
            if (slot == null) continue;

            Vec3 anchor;
            float facing;
            if (mark != null) {
                // Phase 3H: march the formation TO the marked point, facing the direction
                // of travel (user review: a fixed 8-block step felt arbitrary).
                anchor = Vec3.atBottomCenterOf(mark.pos());
                double dx = anchor.x - slot.anchor().x;
                double dz = anchor.z - slot.anchor().z;
                facing = (dx * dx + dz * dz) < 1.0E-4 ? slot.facingYaw()
                        : (float) Math.toDegrees(Math.atan2(-dx, dz));
            } else {
                double yawRad = Math.toRadians(slot.facingYaw());
                anchor = slot.anchor().add(-Math.sin(yawRad) * STEP, 0.0D, Math.cos(yawRad) * STEP);
                facing = slot.facingYaw();
            }
            FormationRegistry.Slot moved = new FormationRegistry.Slot(anchor, facing, slot.shape(),
                    slot.spacing(), slot.index(), slot.count());

            List<Vec3> positions = new Formation(slot.shape(), slot.spacing())
                    .positions(anchor, facing, slot.count());
            if (slot.index() < 0 || slot.index() >= positions.size()) continue;

            registry.setSlot(guard.getUUID(), moved);
            GuardModes.postAndMove(guard, positions.get(slot.index()), SPEED);
        }
    }
}
