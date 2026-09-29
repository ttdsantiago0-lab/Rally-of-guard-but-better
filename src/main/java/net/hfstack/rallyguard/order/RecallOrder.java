package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.formation.Formation;
import net.hfstack.rallyguard.formation.FormationShape;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * RECALL - "to my command position, and wait there".
 * <p>
 * Phase 3E made Recall teleport-and-FOLLOW; the user's review (2026-09-29) pointed out
 * that an explicit summons means "under my immediate command": the guards should arrive
 * and stay put until the next instruction, not trail after the commander (that is
 * {@link FollowOrder}) and not wander off (Guard Villagers' free mode). So now the
 * selected guards are teleported - riders WITH their mounts ({@link MountDriver#relocate})
 * - straight into formation a few blocks in front of the commander, in the shape they
 * last mustered in (Line if none), and each one is POSTED on its slot. The slots are
 * remembered like a Muster, so Reform / Advance work on a recalled group immediately.
 */
public final class RecallOrder implements OrderExecutor {

    /** Distance in front of the commander where the recalled formation is centered. */
    private static final double FRONT_OFFSET = 3.0D;

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        if (guards.isEmpty()) return;

        FormationRegistry registry = FormationRegistry.get(player.serverLevel());
        FormationRegistry.Slot previous = registry.slotOf(guards.get(0).getUUID());
        FormationShape shape = previous != null ? previous.shape() : FormationShape.LINE;

        float yaw = player.getYRot();
        double yawRad = Math.toRadians(yaw);
        Vec3 anchor = new Vec3(player.getX() - Math.sin(yawRad) * FRONT_OFFSET, player.getY(),
                player.getZ() + Math.cos(yawRad) * FRONT_OFFSET);

        Formation formation = new Formation(shape, Formation.DEFAULT_SPACING);
        List<Vec3> positions = formation.positions(anchor, yaw, guards.size());
        for (int i = 0; i < guards.size(); i++) {
            Guard guard = guards.get(i);
            boolean mounted = MountDriver.mountOf(guard) != null;
            BlockPos slot = GuardPosts.standableNear(player.level(), positions.get(i),
                    mounted ? GuardPosts.MOUNTED_HEADROOM : GuardPosts.FOOT_HEADROOM);
            MountDriver.relocate(guard, slot.getX() + 0.5D, slot.getY(), slot.getZ() + 0.5D);
            guard.getNavigation().stop();
            GuardModes.post(guard, slot);
            registry.setSlot(guard.getUUID(), new FormationRegistry.Slot(anchor, yaw, shape,
                    Formation.DEFAULT_SPACING, i, guards.size()));
        }
    }
}
