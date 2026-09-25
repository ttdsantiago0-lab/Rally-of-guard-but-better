package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.formation.Formation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3D: "get back into formation" - for each guard, looks up the {@link
 * FormationRegistry.Slot} that {@code MusterOrder} last recorded for it (anchor,
 * facing, shape, spacing, its own slot index and the formation's member count at
 * Muster time), recomputes that SAME formation's positions via
 * {@link Formation#positions}, and sends the guard back to its own slot - restoring
 * the anchor/shape/facing exactly as mustered, per the design brief's own reading
 * ("REFORM = restore the existing formation around the group's current/known
 * anchor"). Reuses the identical position calculation {@code MusterOrder} uses; this
 * class adds no formation math of its own.
 * <p>
 * A guard with no remembered slot (never Mustered, or its slot was cleared) is
 * silently skipped - same tolerance every other batch order already has for a guard
 * it can't act on. Guards are moved via pathfinding navigation, not teleported,
 * matching Muster's own "let real Minecraft navigation do the moving" choice.
 * Registered as a bare, zero-parameter {@link OrderRegistry} entry (unlike Muster/
 * Retreat, Reform needs no data beyond "which guards" - everything else already lives
 * in {@link FormationRegistry}).
 */
public final class ReformOrder implements OrderExecutor {

    private static final double REFORM_SPEED = 1.0D;

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        ServerLevel level = player.serverLevel();
        FormationRegistry registry = FormationRegistry.get(level);

        for (Guard guard : guards) {
            FormationRegistry.Slot slot = registry.slotOf(guard.getUUID());
            if (slot == null) continue;

            Formation formation = new Formation(slot.shape(), slot.spacing());
            List<Vec3> positions = formation.positions(slot.anchor(), slot.facingYaw(), slot.count());
            if (slot.index() < 0 || slot.index() >= positions.size()) continue;

            Vec3 target = positions.get(slot.index());
            if (guard.isPassenger()) {
                guard.stopRiding();
            }
            guard.getNavigation().moveTo(target.x, target.y, target.z, REFORM_SPEED);
        }
    }
}
