package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.command.CommandStructure;
import net.hfstack.rallyguard.command.SlotQueue;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.formation.Formation;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.formation.UnitLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * E3A A5 - "follow me in formation" for hundreds (David C-05: with hundreds following, the
 * commander ends up in the middle of the crowd and can't walk). Instead of Guard Villagers'
 * own follow AI, the group holds formation slots RELATIVE TO THE COMMANDER and the slots are
 * re-issued when the commander has moved 4 blocks or turned 35° (checked twice a second):
 * <ul>
 *     <li>BEHIND - lines 5 blocks behind; AHEAD - lines in front, clear of the commander;
 *     ESCORT - rings around the commander (the centre slot is skipped); COLUMN - files behind;
 *     FAN - a V opening behind.</li>
 * </ul>
 * Slots are handed out over time ({@link SlotQueue}, with the leaders' response delay) and
 * per subunit with its computed captain ({@link CommandStructure#partition} + {@link UnitLayout}),
 * so 200 guards re-form without a spike. A guard more than 48 blocks behind is brought along
 * like Guard Villagers' own follow does. Any other order releases a guard from the group
 * ({@link GuardModes}). Pressing Follow again for the same group cycles the mode.
 */
public final class FormationFollow {
    private FormationFollow() {
    }

    public enum Mode { BEHIND, AHEAD, ESCORT, COLUMN, FAN }

    private static final double MOVE_TRIGGER = 4.0D;
    private static final float TURN_TRIGGER = 35.0F;
    private static final double CATCH_UP = 48.0D;

    private static final class Group {
        Mode mode;
        final Set<UUID> members = new HashSet<>();
        Vec3 lastAnchor;
        float lastYaw;

        Group(Mode mode) {
            this.mode = mode;
        }
    }

    private static final Map<UUID, Group> GROUPS = new HashMap<>();
    private static final Map<UUID, UUID> COMMANDER_OF = new HashMap<>();

    /** Follow / G-at-the-sky: start following in formation, or cycle the mode if this group already follows. */
    public static void order(ServerPlayer player, List<Guard> guards) {
        Group g = GROUPS.get(player.getUUID());
        boolean same = g != null && !guards.isEmpty();
        if (same) for (Guard guard : guards) if (!g.members.contains(guard.getUUID())) same = false;
        Mode mode = same ? Mode.values()[(g.mode.ordinal() + 1) % Mode.values().length] : Mode.BEHIND;
        for (Guard guard : guards) {
            leave(guard.getUUID());
            HqRelax.leave(guard.getUUID()); // E3B R5 (B2): a relaxed guard told to follow stays with the commander
            net.hfstack.rallyguard.command.SlotQueue.cancel(guard.getUUID());
        }
        Group group = GROUPS.computeIfAbsent(player.getUUID(), k -> new Group(mode));
        group.mode = mode;
        for (Guard guard : guards) {
            group.members.add(guard.getUUID());
            COMMANDER_OF.put(guard.getUUID(), player.getUUID());
        }
        group.lastAnchor = null; // re-slot now
        player.displayClientMessage(Component.translatable("gui.rallyguard.follow.mode",
                Component.translatable("gui.rallyguard.follow." + mode.name().toLowerCase(Locale.ROOT)), group.members.size()), true);
        reslot(player, group);
    }

    public static boolean isFollowing(Guard guard) {
        return COMMANDER_OF.containsKey(guard.getUUID());
    }

    /** Any other order for this guard: it leaves the formation-follow group. */
    public static void leave(UUID guardId) {
        UUID commander = COMMANDER_OF.remove(guardId);
        if (commander == null) return;
        Group g = GROUPS.get(commander);
        if (g != null) {
            g.members.remove(guardId);
            if (g.members.isEmpty()) GROUPS.remove(commander);
        }
    }

    /** Twice a second per level: re-slot groups whose commander moved or turned; bring stragglers. */
    public static void tick(ServerLevel level) {
        if (GROUPS.isEmpty() || level.getGameTime() % 10 != 0) return;
        for (Map.Entry<UUID, Group> e : new ArrayList<>(GROUPS.entrySet())) {
            if (!(level.getPlayerByUUID(e.getKey()) instanceof ServerPlayer player)) continue;
            Group g = e.getValue();
            boolean moved = g.lastAnchor == null || g.lastAnchor.distanceToSqr(player.position()) > MOVE_TRIGGER * MOVE_TRIGGER
                    || Math.abs(net.minecraft.util.Mth.wrapDegrees(player.getYRot() - g.lastYaw)) > TURN_TRIGGER;
            if (moved && player.onGround()) reslot(player, g);
            for (UUID id : g.members) {
                Entity entity = level.getEntity(id);
                if (entity instanceof Guard guard && guard.isAlive() && guard.distanceToSqr(player) > CATCH_UP * CATCH_UP
                        && player.onGround()) {
                    MountDriver.relocate(guard, player.getX(), player.getY(), player.getZ());
                }
            }
        }
    }

    private static void reslot(ServerPlayer player, Group g) {
        ServerLevel level = player.serverLevel();
        List<Guard> guards = new ArrayList<>();
        for (UUID id : g.members) if (level.getEntity(id) instanceof Guard guard && guard.isAlive()) guards.add(guard);
        g.lastAnchor = player.position();
        g.lastYaw = player.getYRot();
        if (guards.isEmpty()) return;
        float yaw = player.getYRot();
        Vec3 fwd = new Vec3(-Math.sin(Math.toRadians(yaw)), 0, Math.cos(Math.toRadians(yaw)));
        FormationShape shape = switch (g.mode) {
            case COLUMN -> FormationShape.COLUMN;
            case FAN -> FormationShape.V;
            case ESCORT -> FormationShape.CIRCLE;
            default -> FormationShape.LINE;
        };
        List<CommandStructure.Subunit> units = CommandStructure.partition(guards);
        int[] sizes = new int[units.size()];
        for (int k = 0; k < sizes.length; k++) sizes[k] = units.get(k).members().size() + (g.mode == Mode.ESCORT ? 1 : 0);
        double depth = 0;
        for (int s : sizes) depth = Math.max(depth, s / 16.0D * Formation.DEFAULT_SPACING);
        Vec3 anchor = switch (g.mode) {
            case BEHIND -> player.position().subtract(fwd.scale(5));
            case AHEAD -> player.position().add(fwd.scale(6 + depth + Formation.DEFAULT_SPACING));
            case ESCORT -> player.position();
            default -> player.position().subtract(fwd.scale(4));
        };
        // Behind the commander the lines open AWAY from it: the formation faces the commander's way,
        // ranks extend backwards from the anchor. Ahead, ranks extend back toward the commander but
        // the anchor is pushed out by their depth.
        List<Vec3> anchors = UnitLayout.anchors(shape, Formation.DEFAULT_SPACING, anchor, yaw, sizes);
        long now = level.getGameTime();
        for (int k = 0; k < units.size(); k++) {
            CommandStructure.Subunit unit = units.get(k);
            Vec3 unitAnchor = GuardPosts.groundAnchor(level, anchors.get(Math.min(k, anchors.size() - 1)));
            List<Vec3> slots = new Formation(shape, Formation.DEFAULT_SPACING).positions(unitAnchor, yaw, sizes[k]);
            if (g.mode == Mode.ESCORT) slots.removeIf(p -> p.distanceToSqr(player.position()) < 2.25D);
            SquadDirectives.push(unit.members(), new SquadDirectives.Directive(SquadDirectives.Kind.FOLLOW, unitAnchor, yaw,
                    unit.captain().getUUID(), now, k, units.size()));
            for (int i = 0; i < unit.members().size() && i < slots.size(); i++) {
                SlotQueue.enqueueFollow(unit.members().get(i), slots.get(i), 1.2D);
            }
        }
    }

    public static void forget(UUID guardId) {
        leave(guardId);
    }

    public static void clearAll() {
        GROUPS.clear();
        COMMANDER_OF.clear();
    }
}
