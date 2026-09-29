package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.formation.Formation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * ADVANCE - the formation moves forward as a formation (Phase 3G; the §32 note deferred
 * Advance until it could mean something different from Muster - now it does).
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

    public static final double STEP = 8.0D;
    private static final double SPEED = 1.0D;

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        FormationRegistry registry = FormationRegistry.get(player.serverLevel());
        for (Guard guard : guards) {
            FormationRegistry.Slot slot = registry.slotOf(guard.getUUID());
            if (slot == null) continue;

            double yawRad = Math.toRadians(slot.facingYaw());
            Vec3 anchor = slot.anchor().add(-Math.sin(yawRad) * STEP, 0.0D, Math.cos(yawRad) * STEP);
            FormationRegistry.Slot moved = new FormationRegistry.Slot(anchor, slot.facingYaw(), slot.shape(),
                    slot.spacing(), slot.index(), slot.count());

            List<Vec3> positions = new Formation(slot.shape(), slot.spacing())
                    .positions(anchor, slot.facingYaw(), slot.count());
            if (slot.index() < 0 || slot.index() >= positions.size()) continue;

            registry.setSlot(guard.getUUID(), moved);
            GuardPosts.postAndMove(guard, positions.get(slot.index()), SPEED);
        }
    }
}
