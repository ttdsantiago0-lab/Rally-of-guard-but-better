package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.formation.FormationShape;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Phase 3H: command WITHOUT opening anything (user review: "minecraft no es un juego de
 * turnos... debes ingeniarte algo"). Two keys, one context-sensitive verb each - the
 * same "point and command" every RTS uses, so there is nothing new to learn:
 * <p>
 * <b>Command (G)</b> - on what the commander is looking at (up to {@link #REACH} blocks):
 * <ul>
 *     <li>an enemy &rarr; ATTACK it (designated target, like the Ledger's Attack);</li>
 *     <li>anyone else (a villager, a friend, your horse, another player who is not
 *     fighting you) &rarr; DEFEND it: stay beside it and fight whatever threatens it;</li>
 *     <li>one of your guards &rarr; select only that guard (then the next G orders it);</li>
 *     <li>a block &rarr; go there and hold it, in the formation they last used;</li>
 *     <li>nothing (the sky) &rarr; FOLLOW me.</li>
 * </ul>
 * <b>Sneak + Command</b> - management: on your guard, add/remove it from the selection;
 * on a free mount (a tamed horse, or any rideable creature), the nearest guard in
 * command mounts it; on a mount one of your guards rides, that guard dismounts; on a
 * block, mark it (the anchor Muster/Retreat use, same as sneak-clicking with the Ledger);
 * on nothing, clear the selection.
 * <p>
 * <b>Fire (H)</b> - toggle HOLD FIRE / FIRE AT WILL. <b>Sneak + Fire</b> - RETREAT to the
 * marked point (or to you), keeping their formation.
 * <p>
 * WHO obeys: the commander's current selection ({@link CommanderSelection} - set in the
 * Ledger or with sneak + G), loaded and in this dimension; with nothing selected, the
 * guards following the commander within {@link #FOLLOWER_RADIUS}. Never "every guard":
 * villages thousands of blocks apart stay untouched (user rule, 2026-09-28).
 * <p>
 * Every verb is an existing order ({@link AttackOrder}, {@link MusterOrder},
 * {@link FollowOrder}, {@link RetreatOrder}, Hold Fire / Release) or a DirectiveRegistry
 * ward - the keys are a remote control, not new logic (docs/ARQUITECTURA.md §4.3).
 */
public final class CommanderHotkeys {
    private CommanderHotkeys() {
    }

    public static final String KEY_ORDER = "ORDER";
    public static final String KEY_FIRE = "FIRE";

    public static final double REACH = 64.0D;
    public static final double FOLLOWER_RADIUS = 64.0D;
    /** How far a guard walks to a mount it was told to take. */
    private static final double MOUNT_RADIUS = 24.0D;

    public static void handle(ServerPlayer player, String key, boolean sneaking) {
        if (KEY_ORDER.equals(key)) {
            if (sneaking) manage(player); else command(player);
        } else if (KEY_FIRE.equals(key)) {
            if (sneaking) retreat(player); else toggleFire(player);
        }
    }

    // ==== Who obeys ====

    /** The selection (loaded, owned, this level), or else the guards following nearby. */
    public static List<Guard> scope(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        List<Guard> guards = new ArrayList<>();
        Set<UUID> selection = CommanderSelection.of(player.getUUID());
        for (UUID id : selection) {
            if (level.getEntity(id) instanceof Guard guard && guard.isAlive()
                    && GuardOwnership.isOwnedBy(guard, player.getUUID())) {
                guards.add(guard);
            }
        }
        if (!guards.isEmpty() || !selection.isEmpty()) return guards;
        return level.getEntities(EntityTypeTest.forClass(Guard.class),
                player.getBoundingBox().inflate(FOLLOWER_RADIUS),
                g -> g.isAlive() && GuardOwnership.isOwnedBy(g, player.getUUID())
                        && GuardModes.modeOf(g) == GuardModes.Mode.FOLLOW);
    }

    // ==== G ====

    private static void command(ServerPlayer player) {
        HitResult hit = look(player);
        if (hit instanceof EntityHitResult eh && eh.getEntity() instanceof Guard guard
                && GuardOwnership.isOwnedBy(guard, player.getUUID())) {
            CommanderSelection.set(player.getUUID(), List.of(guard.getUUID()));
            say(player, Component.translatable("gui.rallyguard.key.selected_one", guard.getName().getString()));
            return;
        }

        List<Guard> guards = scope(player);
        if (guards.isEmpty()) {
            say(player, Component.translatable("gui.rallyguard.key.nobody"));
            return;
        }

        if (hit instanceof EntityHitResult eh && eh.getEntity() instanceof LivingEntity living) {
            if (isEnemy(player, guards.get(0), living)) {
                new AttackOrder(living).execute(player, guards);
                say(player, Component.translatable("gui.rallyguard.command.attack_issued", guards.size(),
                        living.getName().getString()));
            } else {
                defend(player, guards, living);
            }
        } else if (hit instanceof BlockHitResult bh && hit.getType() == HitResult.Type.BLOCK) {
            BlockPos floor = bh.getBlockPos().relative(bh.getDirection());
            FormationShape shape = rememberedShape(player.serverLevel(), guards);
            new MusterOrder(Vec3.atBottomCenterOf(floor), player.getYRot(), shape).execute(player, guards);
            say(player, Component.translatable("gui.rallyguard.key.moved", guards.size()));
        } else {
            new FollowOrder().execute(player, guards);
            say(player, Component.translatable("gui.rallyguard.key.follow", guards.size()));
        }
    }

    /** DEFEND: each guard keeps {@code ward} as its charge (DirectiveRegistry) and is
     *  posted next to it; GuardStanceHandler keeps re-posting it as the ward moves and
     *  sets it on whatever attacks the ward. */
    private static void defend(ServerPlayer player, List<Guard> guards, LivingEntity ward) {
        DirectiveRegistry directives = DirectiveRegistry.get(player.serverLevel());
        for (Guard guard : guards) {
            directives.setTarget(guard.getUUID(), null, "");
            GuardModes.postKeepingWard(guard, GuardPosts.besideWard(guard, ward));
            directives.setWard(guard.getUUID(), ward.getUUID(), ward.getName().getString());
            CommandTargeting.disengage(guard);
        }
        say(player, Component.translatable("gui.rallyguard.key.defend", guards.size(), ward.getName().getString()));
    }

    // ==== Sneak + G ====

    private static void manage(ServerPlayer player) {
        HitResult hit = look(player);
        if (hit instanceof EntityHitResult eh) {
            Entity entity = eh.getEntity();
            if (entity instanceof Guard guard && GuardOwnership.isOwnedBy(guard, player.getUUID())) {
                boolean now = CommanderSelection.toggle(player.getUUID(), guard.getUUID());
                say(player, Component.translatable(now ? "gui.rallyguard.key.added" : "gui.rallyguard.key.removed",
                        guard.getName().getString(), CommanderSelection.of(player.getUUID()).size()));
                return;
            }
            if (entity instanceof PathfinderMob mount && MountDriver.isDrivable(mount)) {
                mountOrDismount(player, mount);
                return;
            }
            say(player, Component.translatable("gui.rallyguard.key.nothing_to_manage"));
        } else if (hit instanceof BlockHitResult bh && hit.getType() == HitResult.Type.BLOCK) {
            BlockPos floor = bh.getBlockPos().relative(bh.getDirection());
            CommanderMarks.markPosition(player.getUUID(), player.level().dimension(), floor, player.getYRot());
            say(player, Component.translatable("gui.rallyguard.key.marked",
                    floor.getX() + " " + floor.getY() + " " + floor.getZ()));
        } else {
            CommanderSelection.set(player.getUUID(), List.of());
            say(player, Component.translatable("gui.rallyguard.key.cleared"));
        }
    }

    /**
     * The mounting answer to "¿toca montarlos antes de usar el scroll?": no - point at a
     * horse and the nearest guard in command walks over and mounts it. The mount must be
     * free (no rider), and if it's a horse it must be tamed and not someone else's.
     * Pointing at a mount one of your guards rides makes that guard dismount.
     */
    private static void mountOrDismount(ServerPlayer player, PathfinderMob mount) {
        for (Entity passenger : mount.getPassengers()) {
            if (passenger instanceof Guard rider && GuardOwnership.isOwnedBy(rider, player.getUUID())) {
                rider.stopRiding();
                say(player, Component.translatable("gui.rallyguard.key.dismounted", rider.getName().getString()));
                return;
            }
        }
        if (mount.isVehicle() || mount instanceof Enemy) {
            say(player, Component.translatable("gui.rallyguard.key.mount_taken"));
            return;
        }
        if (mount instanceof AbstractHorse horse) {
            UUID horseOwner = horse.getOwnerUUID();
            if (!horse.isTamed() || (horseOwner != null && !horseOwner.equals(player.getUUID()))) {
                say(player, Component.translatable("gui.rallyguard.key.mount_untamed"));
                return;
            }
        }
        Guard rider = scope(player).stream()
                .filter(g -> !g.isPassenger() && g.distanceToSqr(mount) <= MOUNT_RADIUS * MOUNT_RADIUS)
                .min(Comparator.comparingDouble(g -> g.distanceToSqr(mount)))
                .orElse(null);
        if (rider == null) {
            say(player, Component.translatable("gui.rallyguard.key.no_rider", (int) MOUNT_RADIUS));
            return;
        }
        // Guard Villagers never mounts on its own; a forced ride is what its datapack
        // spawns use too. The mount driver steers it from here on.
        rider.startRiding(mount, true);
        say(player, Component.translatable("gui.rallyguard.key.mounted", rider.getName().getString(),
                mount.getName().getString()));
    }

    // ==== H ====

    private static void toggleFire(ServerPlayer player) {
        List<Guard> guards = scope(player);
        if (guards.isEmpty()) {
            say(player, Component.translatable("gui.rallyguard.key.nobody"));
            return;
        }
        DirectiveRegistry directives = DirectiveRegistry.get(player.serverLevel());
        boolean anyFiring = guards.stream().anyMatch(g -> !directives.directiveOf(g.getUUID()).holdFire());
        OrderExecutor order = OrderRegistry.get(anyFiring ? "HOLD_FIRE" : "RELEASE_FIRE");
        if (order == null) return;
        order.execute(player, guards);
        say(player, Component.translatable(anyFiring ? "gui.rallyguard.key.hold_fire" : "gui.rallyguard.key.fire_at_will",
                guards.size()));
    }

    private static void retreat(ServerPlayer player) {
        List<Guard> guards = scope(player);
        if (guards.isEmpty()) {
            say(player, Component.translatable("gui.rallyguard.key.nobody"));
            return;
        }
        CommanderMarks.MarkedPosition mark = CommanderMarks.markedPosition(player.getUUID(), player.serverLevel());
        Vec3 point = mark != null ? Vec3.atBottomCenterOf(mark.pos()) : player.position();
        float facing = mark != null ? mark.facingYaw() : player.getYRot();
        new RetreatOrder(point, facing).execute(player, guards);
        say(player, Component.translatable("gui.rallyguard.command.retreat_issued", guards.size()));
    }

    // ==== Helpers ====

    /** Server-side ray cast from the commander's eyes: the first entity in the line of
     *  sight (not blocked by terrain), else the block, else a MISS. */
    private static HitResult look(ServerPlayer player) {
        HitResult blockHit = player.pick(REACH, 1.0F, false);
        Vec3 eye = player.getEyePosition();
        double reach = blockHit.getType() == HitResult.Type.MISS ? REACH : blockHit.getLocation().distanceTo(eye);
        Vec3 end = eye.add(player.getViewVector(1.0F).scale(reach));
        AABB box = player.getBoundingBox().expandTowards(player.getViewVector(1.0F).scale(reach)).inflate(1.0D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(player, eye, end, box,
                e -> !e.isSpectator() && e.isPickable() && e != player.getVehicle(), reach * reach);
        return entityHit != null ? entityHit : blockHit;
    }

    /** An enemy is anything that is plainly hostile (a monster, a mob hunting our side,
     *  a guard fighting us) or that recently hurt us. Everything else is someone to
     *  DEFEND - so pointing at another player starts a war only if they started it. */
    private static boolean isEnemy(ServerPlayer player, Guard sample, LivingEntity target) {
        if (!CommandTargeting.wouldAccept(sample, target)) return false;
        if (target instanceof Enemy) return true;
        if (CommandTargeting.isHostileGuard(sample, target)) return true;
        if (target instanceof Mob mob && mob.getTarget() != null && isOurSide(player, mob.getTarget())) return true;
        LivingEntity aggressor = CommanderMarks.recentAggressor(player.getUUID(), player.serverLevel());
        return aggressor != null && aggressor == target;
    }

    private static boolean isOurSide(ServerPlayer player, LivingEntity entity) {
        return entity == player || GuardOwnership.isOwnedBy(entity, player.getUUID());
    }

    private static FormationShape rememberedShape(ServerLevel level, List<Guard> guards) {
        FormationRegistry formations = FormationRegistry.get(level);
        for (Guard guard : guards) {
            FormationRegistry.Slot slot = formations.slotOf(guard.getUUID());
            if (slot != null) return slot.shape();
        }
        return FormationShape.LINE;
    }

    private static void say(ServerPlayer player, Component message) {
        player.displayClientMessage(message, true);
    }
}
