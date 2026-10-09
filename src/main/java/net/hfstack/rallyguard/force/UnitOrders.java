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
import net.hfstack.rallyguard.formation.FormationLayout;
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
    /**
     * @param layout E3B G1: formation geometry (null = the unit's stored layout)
     * @param tempo  E3B P1 TEMPO: multiplier of the navigation speed REQUESTED for this order
     *               (never the entity's own speed or attributes); 1 = normal
     */
    public record UnitOrder(long version, Kind kind, Source source, @Nullable Vec3 anchor, float facingYaw,
                            FormationShape shape, @Nullable FireMode style, double radius, long issuedTick, String why,
                            @Nullable FormationLayout layout, double tempo) {
    }

    private static final Map<String, UnitOrder> ORDERS = new HashMap<>();
    /** E3B R4 (A10): what each guard last carried out - the unit AND its version (versions are per unit). */
    private record Applied(String key, long version) {
    }

    private static final Map<UUID, Applied> APPLIED = new HashMap<>();
    /** A guard new to a unit ignores that unit's directive if it is older than this (a stale RETREAT_HQ...). */
    static final long STALE_FOR_NEWCOMER_TICKS = 20L * 60L;
    private static final Map<UUID, Long> PLAYER_ORDER = new HashMap<>();
    /** E3B G3: ad-hoc groups (guards in no squad given one instruction): guard -> key, key -> members. */
    private static final Map<UUID, String> ADHOC = new HashMap<>();
    private static final Map<String, java.util.Set<UUID>> ADHOC_MEMBERS = new HashMap<>();
    /** One shared SquadDirectives object per (unit, version): the ladder sees one group. */
    private static final Map<String, SquadDirectives.Directive> DIRECTIVE = new HashMap<>();

    public static String unitKey(UUID owner, String unitPath) {
        return owner + "#" + unitPath.toLowerCase(Locale.ROOT);
    }

    @Nullable
    public static String unitKeyOf(ServerLevel level, Guard guard) {
        String adhoc = ADHOC.get(guard.getUUID());
        if (adhoc != null) return adhoc;
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
        return issue(level, unitKey, kind, source, anchor, yaw, shape, style, radius, why, null, 1.0D);
    }

    /** E3B: the same, with a formation layout (null = the unit's) and a tempo (navigation speed multiplier). */
    public static UnitOrder issue(ServerLevel level, String unitKey, Kind kind, Source source, @Nullable Vec3 anchor,
                                  float yaw, FormationShape shape, @Nullable FireMode style, double radius, String why,
                                  @Nullable FormationLayout layout, double tempo) {
        UnitOrder previous = ORDERS.get(unitKey);
        long version = previous == null ? 1 : previous.version() + 1;
        UnitOrder order = new UnitOrder(version, kind, source, anchor, yaw, shape, style, radius, level.getGameTime(), why,
                layout, Math.max(0.5D, Math.min(1.6D, tempo)));
        ORDERS.put(unitKey, order);
        DIRECTIVE.remove(unitKey);
        return order;
    }

    /** The player gave these guards an explicit order now (they ignore automatic directives for a while). */
    public static void notePlayerOrder(Collection<Guard> guards) {
        for (Guard g : guards) PLAYER_ORDER.put(g.getUUID(), g.level().getGameTime());
        net.hfstack.rallyguard.command.InstructionRunner.release(guards); // E3B G3: a new order ends an instruction
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
        Applied applied = APPLIED.get(guard.getUUID());
        boolean sameUnit = applied != null && applied.key().equals(key);
        if (sameUnit && applied.version() >= order.version()) return;
        if (order.source() != Source.PLAYER && underPlayerOrder(guard)) return; // the player's last word wins for now
        APPLIED.put(guard.getUUID(), new Applied(key, order.version()));
        // E3B R4 (A10): a guard that just joined this unit doesn't run a directive issued long before it came.
        if (!sameUnit && level.getGameTime() - order.issuedTick() > STALE_FOR_NEWCOMER_TICKS) return;
        apply(level, guard, key, order);
        net.hfstack.rallyguard.debug.SimProbe.unitOrderApplied(guard, order.source().name());
    }

    /** E3B R8 (B12): the member list of a unit, computed once per (key, version) and second, shared by its guards. */
    private record Members(long version, long tick, List<Guard> list) {
    }

    private static final Map<String, Members> MEMBERS = new HashMap<>();

    private static List<Guard> membersFor(ServerLevel level, String key, UnitOrder order) {
        Members m = MEMBERS.get(key);
        long now = level.getGameTime();
        if (m != null && m.version() == order.version() && now - m.tick() < 20) return new ArrayList<>(m.list());
        List<Guard> list = members(level, key);
        if (MEMBERS.size() > 4096) MEMBERS.clear();
        MEMBERS.put(key, new Members(order.version(), now, list));
        return new ArrayList<>(list);
    }

    private static void apply(ServerLevel level, Guard guard, String key, UnitOrder order) {
        List<Guard> unit = membersFor(level, key, order);
        if (!unit.contains(guard)) unit.add(guard);
        FormationLayout layout = layoutOf(level, key, order);
        if (layout.isDefault()) {
            unit.sort((a, b) -> a.getUUID().compareTo(b.getUUID()));
        } else { // E3B G1: the toughest take the exposed slots (same order computed by every member)
            unit.sort(java.util.Comparator.comparingDouble((Guard m) -> -net.hfstack.rallyguard.command.GroupProfile.toughness(m))
                    .thenComparing(Guard::getUUID));
        }
        int rankInUnit = Math.max(0, unit.indexOf(guard));
        double tempo = order.tempo() <= 0 ? 1.0D : order.tempo();
        SquadDirectives.Directive d = DIRECTIVE.computeIfAbsent(key, k -> new SquadDirectives.Directive(kindFor(order.kind()),
                order.anchor(), order.facingYaw(), captainOf(unit), order.issuedTick(), 0, 1));
        SquadDirectives.push(List.of(guard), d);
        switch (order.kind()) {
            case HOLD, MOVE, FALLBACK -> {
                if (order.anchor() == null) return;
                Formation formation = new Formation(order.shape(), Formation.DEFAULT_SPACING, layout);
                List<Vec3> slots = formation.positions(order.anchor(), order.facingYaw(), Math.max(1, unit.size()));
                List<Integer> exposure = formation.exposureOrder(Math.max(1, unit.size()));
                int slot = exposure.get(Math.min(rankInUnit, exposure.size() - 1));
                SlotQueue.enqueue(guard, slots.get(Math.min(slot, slots.size() - 1)), (order.kind() == Kind.FALLBACK ? 1.1D : 1.0D) * tempo);
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
                double a = rankInUnit * 2.399963D; // golden angle: spread the unit around the HQ
                double r = 1.5D + Math.sqrt(rankInUnit) * 1.2D;
                Vec3 spot = Vec3.atBottomCenterOf(home).add(Math.cos(a) * r, 0, Math.sin(a) * r);
                GuardModes.postAndMove(guard, spot, 1.15D * tempo);
            }
            case ATTACK_AREA -> {
                if (order.anchor() == null) return;
                LivingEntity enemy = nearestEnemy(level, guard, order.anchor(), order.radius());
                if (enemy != null && (Engagement.isRanged(guard) || Engagement.tryEngage(guard, enemy))) {
                    CommandTargeting.issue(guard, enemy);
                } else {
                    GuardModes.postAndMove(guard, order.anchor(), 1.1D * tempo);
                }
            }
            case FOLLOW -> GuardModes.follow(guard);
            case RELAX, FREE -> GuardModes.free(guard);
        }
    }

    /** E3B G1: the order's layout, else the one stored for the unit, else DEFAULT (always DEFAULT when disabled). */
    static FormationLayout layoutOf(ServerLevel level, String key, UnitOrder order) {
        if (!RallyConfig.on(RallyConfig.LAYOUT_ENABLED)) return FormationLayout.DEFAULT;
        if (order.layout() != null) return order.layout();
        int hash = key.indexOf('#');
        if (hash <= 0) return FormationLayout.DEFAULT;
        try {
            UUID owner = UUID.fromString(key.substring(0, hash));
            return net.hfstack.rallyguard.contract.FormationRegistry.get(level).unitLayout(owner, key.substring(hash + 1));
        } catch (IllegalArgumentException e) {
            return FormationLayout.DEFAULT;
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

    /**
     * E3B G3: binds guards that belong to no squad into an ad-hoc unit {@code key} (an instruction
     * given to a loose selection), so it gets versioned directives like any unit.
     */
    public static void bindAdhoc(String key, Collection<Guard> guards) {
        java.util.Set<UUID> set = ADHOC_MEMBERS.computeIfAbsent(key, k -> new java.util.HashSet<>());
        for (Guard g : guards) {
            String old = ADHOC.put(g.getUUID(), key);
            if (old != null && !old.equals(key)) {
                java.util.Set<UUID> was = ADHOC_MEMBERS.get(old);
                if (was != null) was.remove(g.getUUID());
            }
            set.add(g.getUUID());
        }
    }

    public static void unbindAdhoc(String key) {
        java.util.Set<UUID> set = ADHOC_MEMBERS.remove(key);
        if (set != null) for (UUID id : set) ADHOC.remove(id, key);
        ORDERS.remove(key);
        DIRECTIVE.remove(key);
    }

    /** Loaded, living guards of a unit key. */
    public static List<Guard> members(ServerLevel level, String key) {
        List<Guard> out = new ArrayList<>();
        java.util.Set<UUID> adhoc = ADHOC_MEMBERS.get(key);
        if (adhoc != null) {
            for (UUID id : adhoc) {
                if (level.getEntity(id) instanceof Guard g && g.isAlive()) out.add(g);
            }
            return out;
        }
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
        Applied a = APPLIED.get(guardId);
        return a == null ? 0L : a.version();
    }

    /** E3B R4: the newest explicit player order among these guards (tick, -1 = none). */
    public static long lastPlayerOrder(Collection<Guard> guards) {
        long best = -1;
        for (Guard g : guards) best = Math.max(best, PLAYER_ORDER.getOrDefault(g.getUUID(), -1L));
        return best;
    }

    /** E3B R4: drops the directives of a squad and its units ("owner#squad" and "owner#squad/..."). */
    public static void forgetPrefix(String prefix) {
        ORDERS.keySet().removeIf(k -> k.equals(prefix) || k.startsWith(prefix + "/"));
        DIRECTIVE.keySet().removeIf(k -> k.equals(prefix) || k.startsWith(prefix + "/"));
    }

    public static void forget(UUID guardId) {
        APPLIED.remove(guardId);
        PLAYER_ORDER.remove(guardId);
        String adhoc = ADHOC.remove(guardId);
        if (adhoc != null) {
            java.util.Set<UUID> set = ADHOC_MEMBERS.get(adhoc);
            if (set != null) set.remove(guardId);
        }
    }

    public static void clearAll() {
        ORDERS.clear();
        APPLIED.clear();
        PLAYER_ORDER.clear();
        DIRECTIVE.clear();
        ADHOC.clear();
        ADHOC_MEMBERS.clear();
        MEMBERS.clear();
    }

    /** Spot used by unit maneuvers: {@code standableNear} of a point. */
    static Vec3 ground(ServerLevel level, Vec3 p) {
        return Vec3.atBottomCenterOf(GuardPosts.standableNear(level, p));
    }
}
