package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.formation.Formation;
import net.hfstack.rallyguard.formation.FormationShape;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3D: "converge into this formation, here" - the first concrete consumer of the
 * {@link Formation} engine. Unlike {@code RecallOrder} (an instant teleport, matching
 * the pre-existing per-guard Summon it generalizes), Muster deliberately uses real
 * pathfinding navigation, per the design brief's own instruction ("normal Minecraft
 * navigation moves each guard toward its assigned position... do NOT build custom
 * pathfinding") - guards visibly walk/run into position rather than snapping there.
 * <p>
 * Unlike every other order so far, Muster needs data beyond "which guards" - an anchor
 * position, a facing, and a shape - so it is deliberately NOT registered in
 * {@link OrderRegistry} as a bare string id: {@code GuardCommandNetworking} constructs
 * a fresh instance of this class per invocation, from
 * {@code IssueMusterOrderC2SPayload}'s fields, and calls {@link #execute} directly.
 * {@link OrderExecutor} itself stays exactly as small as it already was;
 * parameterization happens by constructing the order with its data, not by growing the
 * interface - see {@link OrderExecutor}'s own javadoc.
 * <p>
 * Destination v1 limitation: for this pass, the Ledger always sends the PLAYER's own
 * current position/facing as the anchor (see {@code GuardCommandScreen#buildMusterBar})
 * - there is no in-world "mark a destination elsewhere" interaction yet. The wire
 * format ({@code Destination}'s full x/y/z/facingYaw) already supports an arbitrary
 * point though, so a future destination-marking UI is a client-only addition; nothing
 * here or in the payload needs to change for it.
 * <p>
 * Every guard's assigned slot is recorded in {@link FormationRegistry} as a side
 * effect, which is what makes {@code ReformOrder} possible later - see that registry's
 * own javadoc for why this doesn't make the Order abstraction itself stateful.
 */
public final class MusterOrder implements OrderExecutor {

    private static final double MUSTER_SPEED = 1.0D;

    private final Vec3 anchor;
    private final float facingYaw;
    private final FormationShape shape;

    public MusterOrder(Vec3 anchor, float facingYaw, FormationShape shape) {
        this.anchor = anchor;
        this.facingYaw = facingYaw;
        this.shape = shape;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        Formation formation = new Formation(shape, Formation.DEFAULT_SPACING);
        List<Vec3> positions = formation.positions(anchor, facingYaw, guards.size());

        ServerLevel level = player.serverLevel();
        FormationRegistry registry = FormationRegistry.get(level);

        for (int i = 0; i < guards.size(); i++) {
            Guard guard = guards.get(i);
            Vec3 target = positions.get(i);

            if (guard.isPassenger()) {
                guard.stopRiding();
            }
            guard.getNavigation().moveTo(target.x, target.y, target.z, MUSTER_SPEED);

            registry.setSlot(guard.getUUID(), new FormationRegistry.Slot(
                    anchor, facingYaw, shape, Formation.DEFAULT_SPACING, i, guards.size()));
        }
    }
}
