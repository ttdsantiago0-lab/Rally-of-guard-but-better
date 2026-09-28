package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.formation.FormationShape;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * RETREAT - "disengage and fall back there". Built per invocation by
 * {@code GuardCommandNetworking} (it needs a destination, so it is not a bare
 * {@link OrderRegistry} id).
 * <p>
 * Phase 3E: Phase 3D's Retreat was a single {@code navigation.moveTo} - guards still
 * holding a target turned straight back into the fight, and on arrival they wandered off.
 * Now a retreat actually disengages (drops the current fight AND the designated target,
 * so they don't turn around for it) and ends with the squad re-formed and posted at the
 * fallback point, in the same shape it last mustered in (Line if it never mustered) -
 * i.e. a retreat is a {@link MusterOrder} somewhere safer, preceded by a clean break.
 * Fire permission is left alone: a retreating squad under Hold Fire stays silent.
 */
public final class RetreatOrder implements OrderExecutor {

    private final Vec3 destination;
    private final float facingYaw;

    public RetreatOrder(Vec3 destination, float facingYaw) {
        this.destination = destination;
        this.facingYaw = facingYaw;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        if (guards.isEmpty()) return;

        DirectiveRegistry directives = DirectiveRegistry.get(player.serverLevel());
        for (Guard guard : guards) {
            directives.setTarget(guard.getUUID(), null, "");
            CommandTargeting.disengage(guard);
        }

        FormationRegistry.Slot previous = FormationRegistry.get(player.serverLevel()).slotOf(guards.get(0).getUUID());
        FormationShape shape = previous != null ? previous.shape() : FormationShape.LINE;
        new MusterOrder(destination, facingYaw, shape).execute(player, guards);
    }
}
