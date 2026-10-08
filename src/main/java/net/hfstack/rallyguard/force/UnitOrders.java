package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.command.SlotQueue;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.HqRegistry;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.formation.Formation;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.order.CommandTargeting;
import net.hfstack.rallyguard.order.Engagement;
import net.hfstack.rallyguard.order.GuardModes;
import net.hfstack.rallyguard.order.GuardPosts;
import net.hfstack.rallyguard.order.RetreatPlanner;
import net.hfstack.rallyguard.veterancy.VeterancyEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * E3A A2 - VERSIONED unit directives: one record per unit ("owner#Squad/Unit"), never one
 * message per guard. A force rule, an operation or a plan phase {@link #issue}s a new
 * {@link UnitOrder} (version + 1); each guard compares its unit's version with the one it
 * applied, in its own staggered once-a-second turn ({@link #applyIfNewer}), and carries the
 * order out locally (its slot in the unit's formation, its fire style...). That is what lets
 * an order reach hundreds of guards without a per-tick cost.
 * <p>
 * Precedence ({@code docs/ORDENES_JERARQUIA.md}): a player's explicit order to a guard wins
 * for {@code force.playerOverrideSeconds} (default 60) over every automatic directive
 * (FORCE, PLAN, CAPTAIN); after that the newest directive of its unit applies again. Among
 * automatic directives the unit keeps one record, so the newest version always wins.
 */
public final class UnitOrders {
    private UnitOrders() {
    }

    public enum Source { PLAYER, PLAN, FORCE, CAPTAIN }

    /** What a unit is told to do. Positions are the unit's anchor; each guard finds its own slot. */
    public enum Kind { HOLD, MOVE, RETREAT_HQ, FALLBACK, ATTACK_AREA, FIRE_STYLE, FOLLOW, RELAX, FREE }

    /**
     * @param style    fire style for FIRE_STYLE / HOLD (null = unchanged)
     * @param radius   ATTACK_AREA radius
     */
    public record UnitOrder(long version, Kind kind, Source source, @Nullable Vec3 anchor, float facingYaw,
                            FormationShape shape, @Nullable FireMode style, double radius, long issuedTick, String why) {
    }

    private static final Map<String, UnitOrder> ORDERS = new HashMap<>();
    private static final Map<UUID, Long> APPLIED = new HashMap<>();
    private static final Map<UUID, Long> PLAYER_ORDER = new HashMap<>();
    /** One shared SquadDirectives object per (unit, version): the ladder sees one group. */
    private static final Map<String, SquadDirectives.Directive> DIRECTIVE = new HashMap<>();

    public static String unitKey(UUID owner, String unitPath) {
        return owner + "#" + unitPath.toLowerCase(Locale.ROOT);
    }

    @Nullable
    public static String unitKeyOf(ServerLevel level, Guard guard) {
        UUID owner = GuardOwnership.getOwner(guard);
        if (owner == null) return null;
        Optional<String> path = SquadRegistry.get(level).pathOf(guard.getUUID());
        return path.map(p -> unitKey(owner, p)).orElse(null);
    }

    @Nullable
    public static UnitOrder current(String unitKey) {
        return ORDERS.get(unitKey);
    }

    /** Issues a new version for a unit; returns it. */
    public static UnitOrder issue(ServerLevel level, String unitKey, Kind kind, Source source, @Nullable Vec3 anchor,
                                  float yaw, FormationShape shape, @Nullable FireMode style, double radius, String why) {
        UnitOrder previous = ORDERS.get(unitKey);
        long version = previous == null ? 1 : previous.version() + 1;
        UnitOrder order = new UnitOrder(version, kind, source, anchor, yaw, shape, style, radius, level.getGameTime(), why);
        ORDERS.put(unitKey, order);
        DIRECTIVE.remove(unitKey);
        return order;
    }

    /** The player gave these guards an explicit order now (they ignore automatic directives for a while). */
    public static void notePlayerOrder(Collection<Guard> guards) {
        for (Guard g : guards) PLAYER_ORDER.put(g.getUUID(), g.level().getGameTime());
    }

    public static boolean underPlayerOrder(Guard guard) {
        Long t = PLAYER_ORDER.get(guard.getUUID());
        return t != null && guard.level().getGameTime() - t < 20L * RallyConfig.i(RallyConfig.PLAYER_OVERRIDE_SECONDS, 60);
    }

    /**
     * In the guard's own turn: if its unit has a newer directive than the one it carried out,
     * carry it out now (unless a player's order is still fresh for it). Cheap when nothing changed.
     */
    public static void applyIfNewer(ServerLevel level, Guard guard) {
        if (ORDERS.isEmpty()) return;
        String key = unitKeyOf(level, guard);
        if (key == null) return;
        UnitOrder order = ORDERS.get(key);
        if (order == null) return;
        Long applied = APPLIED.get(guard.getUUID());
        if (applied != null && applied >= order.version()) return;
        if (order.source() != Source.PLAYER && underPlayerOrder(guard)) return; // the player's last word wins for now
        APPLIED.put(guard.getUUID(), order.version());
        apply(level, guard, key, order);
        net.hfstack.rallyguard.debug.SimProbe.unitOrderApplied(guard, order.source().name());
    }

    private static void apply(ServerLevel level, Guard guard, String key, UnitOrder order) {
        List<Guard> unit = members(level, key);
        unit.sort((a, b) -> a.getUUID().compareTo(b.getUUID()));
        int index = Math.max(0, unit.indexOf(guard));
        SquadDirectives.Directive d = DIRECTIVE.computeIfAbsent(key, k -> new SquadDirectives.Directive(kindFor(order.kind()),
                order.anchor(), order.facingYaw(), captainOf(unit), order.issuedTick(), 0, 1));
        SquadDirectives.push(List.of(guard), d);
        switch (order.kind()) {
            case HOLD, MOVE, FALLBACK -> {
                if (order.anchor() == null) return;
                List<Vec3> slots = new Formation(order.shape(), Formation.DEFAULT_SPACING)
                        .positions(order.anchor(), order.facingYaw(), Math.max(1, unit.size()));
                SlotQueue.enqueue(guard, slots.get(Math.min(index, slots.size() - 1)), order.kind() == Kind.FALLBACK ? 1.1D : 1.0D);
                if (order.style() != null) DirectiveRegistry.get(level).setFireMode(guard.getUUID(), order.style().name());
            }
            case FIRE_STYLE -> {
                if (order.style() != null) DirectiveRegistry.get(level).setFireMode(guard.getUUID(), order.style().name());
            }
            case RETREAT_HQ -> {
                CommandTargeting.disengage(guard);
                BlockPos home = hqOf(level, guard);
                if (home == null) {
                    GuardModes.follow(guard);
                    return;
                }
                double a = index * 2.399963D; // golden angle: spread the unit around the HQ
                double r = 1.5D + Math.sqrt(index) * 1.2D;
                Vec3 spot = Vec3.atBottomCenterOf(home).add(Math.cos(a) * r, 0, Math.sin(a) * r);
                GuardModes.postAndMove(guard, spot, 1.15D);
            }
            case ATTACK_AREA -> {
                if (order.anchor() == null) return;
                LivingEntity enemy = nearestEnemy(level, guard, order.anchor(), order.radius());
                if (enemy != null && (Engagement.isRanged(guard) || Engagement.tryEngage(guard, enemy))) {
                    CommandTargeting.issue(guard, enemy);
                } else {
                    GuardModes.postAndMove(guard, order.anchor(), 1.1D);
                }
            }
            case FOLLOW -> GuardModes.follow(guard);
            case RELAX, FREE -> GuardModes.free(guard);
        }
    }

    private static SquadDirectives.Kind kindFor(Kind k) {
        return switch (k) {
            case HOLD, MOVE -> SquadDirectives.Kind.HOLD;
            case RETREAT_HQ, FALLBACK -> SquadDirectives.Kind.RETREAT;
            case ATTACK_AREA -> SquadDirectives.Kind.ATTACK;
            case FOLLOW -> SquadDirectives.Kind.FOLLOW;
            default -> SquadDirectives.Kind.FREE;
        };
    }

    @Nullable
    private static UUID captainOf(List<Guard> unit) {
        Guard c = net.hfstack.rallyguard.command.CommandStructure.captainOf(unit);
        return c == null ? null : c.getUUID();
    }

    /** Loaded, living guards of a unit key. */
    public static List<Guard> members(ServerLevel level, String key) {
        List<Guard> out = new ArrayList<>();
        SquadRegistry squads = SquadRegistry.get(level);
        for (Guard g : net.hfstack.rallyguard.event.GuardStanceHandler.loaded(level.dimension())) {
            if (!g.isAlive()) continue;
            UUID owner = GuardOwnership.getOwner(g);
            if (owner == null) continue;
            Optional<String> path = squads.pathOf(g.getUUID());
            if (path.isPresent() && key.equals(unitKey(owner, path.get()))) out.add(g);
        }
        return out;
    }

    @Nullable
    private static BlockPos hqOf(ServerLevel level, Guard guard) {
        SquadRegistry squads = SquadRegistry.get(level);
        return squads.squadOf(guard.getUUID())
                .map(s -> RetreatPlanner.hqOf(squads, HqRegistry.get(level), s))
                .filter(h -> h.dimension.equals(level.dimension().location().toString()))
                .map(h -> h.pos).orElse(null);
    }

    @Nullable
    private static LivingEntity nearestEnemy(ServerLevel level, Guard guard, Vec3 at, double radius) {
        LivingEntity best = null;
        double bestD = radius * radius;
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class,
                new net.minecraft.world.phys.AABB(at, at).inflate(radius), LivingEntity::isAlive)) {
            if (!VeterancyEvents.realEnemy(guard, e) || !CommandTargeting.wouldAccept(guard, e)) continue;
            double d = e.position().distanceToSqr(at);
            if (d < bestD) {
                bestD = d;
                best = e;
            }
        }
        return best;
    }

    /** The version of its unit's directive this guard carried out (0 = none). */
    public static long appliedVersion(UUID guardId) {
        return APPLIED.getOrDefault(guardId, 0L);
    }

    public static void forget(UUID guardId) {
        APPLIED.remove(guardId);
        PLAYER_ORDER.remove(guardId);
    }

    public static void clearAll() {
        ORDERS.clear();
        APPLIED.clear();
        PLAYER_ORDER.clear();
        DIRECTIVE.clear();
    }

    /** Spot used by unit maneuvers: {@code standableNear} of a point. */
    static Vec3 ground(ServerLevel level, Vec3 p) {
        return Vec3.atBottomCenterOf(GuardPosts.standableNear(level, p));
    }
}
