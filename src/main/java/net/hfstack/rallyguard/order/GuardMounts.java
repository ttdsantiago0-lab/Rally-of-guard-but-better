package net.hfstack.rallyguard.order;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * E3A F8 - the Ledger row's mount/dismount button (David, B-04: "una montura es única de un
 * guardia"). Dismounting remembers the mount as THAT guard's; mounting takes the guard's own
 * mount back if it is free and within {@link #OWN_RADIUS}, else the nearest free drivable
 * mount within {@link #FREE_RADIUS} (a horse must be tamed and not someone else's), never
 * one another guard rides. The mount driver steers from there (one guard per mount).
 */
public final class GuardMounts {
    private GuardMounts() {
    }

    static final double OWN_RADIUS = 32.0D;
    static final double FREE_RADIUS = 12.0D;
    private static final Map<UUID, UUID> MOUNT_OF = new HashMap<>();

    public static Component toggle(ServerPlayer player, Guard guard) {
        PathfinderMob riding = MountDriver.mountOf(guard);
        if (guard.isPassenger()) {
            if (riding != null) MOUNT_OF.put(guard.getUUID(), riding.getUUID());
            guard.stopRiding();
            return Component.translatable("gui.rallyguard.key.dismounted", guard.getName().getString());
        }
        PathfinderMob mount = ownMount(guard);
        if (mount == null) {
            mount = guard.level().getEntitiesOfClass(PathfinderMob.class, guard.getBoundingBox().inflate(FREE_RADIUS),
                            m -> usable(player, m)).stream()
                    .min(Comparator.comparingDouble(m -> m.distanceToSqr(guard))).orElse(null);
        }
        if (mount == null) return Component.translatable("gui.rallyguard.command.no_mount", (int) FREE_RADIUS);
        guard.startRiding(mount, true);
        MOUNT_OF.put(guard.getUUID(), mount.getUUID());
        return Component.translatable("gui.rallyguard.key.mounted", guard.getName().getString(), mount.getName().getString());
    }

    @Nullable
    private static PathfinderMob ownMount(Guard guard) {
        UUID id = MOUNT_OF.get(guard.getUUID());
        if (id == null || !(guard.level() instanceof net.minecraft.server.level.ServerLevel level)) return null;
        Entity e = level.getEntity(id);
        return e instanceof PathfinderMob m && m.isAlive() && !m.isVehicle()
                && m.distanceToSqr(guard) <= OWN_RADIUS * OWN_RADIUS ? m : null;
    }

    private static boolean usable(ServerPlayer player, PathfinderMob m) {
        if (!m.isAlive() || m.isVehicle() || m instanceof Enemy || !MountDriver.isDrivable(m)) return false;
        if (m instanceof AbstractHorse horse) {
            UUID owner = horse.getOwnerUUID();
            return horse.isTamed() && (owner == null || owner.equals(player.getUUID()));
        }
        return true;
    }

    public static void forget(UUID guardId) {
        MOUNT_OF.remove(guardId);
    }
}
