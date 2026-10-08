package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * E3A A3 - force maneuvers. A force runs at most one {@link Type operation}; each second the
 * force brain calls {@link #step} with the units no force rule is overriding, and the
 * operation issues ONE versioned unit directive per unit and phase (never per guard):
 * <ul>
 *     <li>{@code HOLD_LINE} - anvil holds the line, screen 3 ahead, support 6 behind standing
 *     still (volley with 4+ shooters), reserve 14 behind, flank units 12 to each side, hammer
 *     10 behind ready;</li>
 *     <li>{@code FALLBACK_FIGHTING} - the whole line steps back 6 blocks every 6 s, shooters
 *     firing while falling back, up to 5 steps, then holds;</li>
 *     <li>{@code ADVANCE_BY_TURNS} - the front group (anvil, screen, hammer) and the fire group
 *     (support, flank) take turns: one moves 8 blocks while the other holds and shoots; when the
 *     enemy is within 12 blocks it becomes HOLD_LINE;</li>
 *     <li>{@code FLANK} - flank and hammer units go around to 15 blocks on the enemy's side,
 *     then attack its centre; the others hold the line;</li>
 *     <li>{@code ORDERLY_RETREAT} - support and reserve fall back to the HQ first while the
 *     anvil and screen step back covering them; after 10 s everyone retreats.</li>
 * </ul>
 * Captains translate the maneuver into their unit's formation (anvil/support LINE, hammer V,
 * flank/reserve COLUMN) and fire style; individual guards then fight with their ladder,
 * following their group's style (A3: in a group a guard couples to the group's doctrine).
 */
public final class Operations {
    private Operations() {
    }

    public enum Type { HOLD_LINE, FALLBACK_FIGHTING, ADVANCE_BY_TURNS, FLANK, ORDERLY_RETREAT }

    static final class Op {
        final Type type;
        Vec3 anchor;
        final float facing;
        int phase;
        long phaseStart;
        int steps;
        final Set<String> issued = new HashSet<>();
        /** The role each unit had when its order was issued: a new role (a captain fell) means a new order. */
        final Map<String, String> roles = new HashMap<>();

        Op(Type type, Vec3 anchor, float facing, long now) {
            this.type = type;
            this.anchor = anchor;
            this.facing = facing;
            this.phaseStart = now;
        }
    }

    private static final Map<String, Op> OPS = new HashMap<>();

    /** Starts (or replaces) the force's operation at {@code anchor}, facing {@code facingYaw}. */
    public static void start(ServerLevel level, ForceRegistry.Force force, Type type, Vec3 anchor, float facingYaw) {
        OPS.put(ForceBrain.forceKey(force), new Op(type, anchor, facingYaw, level.getGameTime()));
    }

    public static void stop(ForceRegistry.Force force) {
        OPS.remove(ForceBrain.forceKey(force));
    }

    @Nullable
    public static Type current(ForceRegistry.Force force) {
        Op op = OPS.get(ForceBrain.forceKey(force));
        return op == null ? null : op.type;
    }

    /** Starts the operation only if it isn't already the running one (rules call this every second). */
    static void ensure(ServerLevel level, ForceRegistry.Force force, Type type, Vec3 anchor, float facing) {
        Op op = OPS.get(ForceBrain.forceKey(force));
        if (op == null || op.type != type) start(level, force, type, anchor, facing);
    }

    static void step(ServerLevel level, ForceRegistry.Force force, List<Forces.UnitView> free, Map<String, String> roles,
                     Map<String, SituationReports.Report> reports, long now) {
        Op op = OPS.get(ForceBrain.forceKey(force));
        if (op == null || free.isEmpty()) return;
        Vec3 fwd = forward(op.facing);
        Vec3 right = new Vec3(-fwd.z, 0, fwd.x);
        double secs = (now - op.phaseStart) / 20.0D;
        switch (op.type) {
            case HOLD_LINE -> {
            }
            case FALLBACK_FIGHTING -> {
                if (secs >= 6 && op.steps < 5) {
                    op.anchor = op.anchor.subtract(fwd.scale(6));
                    op.steps++;
                    nextPhase(op, now);
                }
            }
            case ADVANCE_BY_TURNS -> {
                Vec3 enemy = enemyNear(reports);
                if (enemy != null && enemy.distanceTo(op.anchor) < 12.0D) {
                    OPS.put(ForceBrain.forceKey(force), new Op(Type.HOLD_LINE, op.anchor, op.facing, now));
                    return;
                }
                if (secs >= 8) {
                    op.anchor = op.anchor.add(fwd.scale(4)); // each turn the moving group gains 8 on the holder
                    nextPhase(op, now);
                }
            }
            case FLANK -> {
                if (op.phase == 0 && secs >= 15) nextPhase(op, now);
            }
            case ORDERLY_RETREAT -> {
                if (op.phase == 0 && secs >= 10) nextPhase(op, now);
            }
        }
        Vec3 enemy = enemyNear(reports);
        Doctrine doctrine = Doctrine.parse(force.doctrine);
        for (Forces.UnitView u : free) {
            String role = roles.getOrDefault(u.path(), "ANVIL");
            if (!role.equals(op.roles.put(u.path(), role))) op.issued.remove(u.path() + "#" + op.phase); // re-planned
            String mark = u.path() + "#" + op.phase;
            if (op.issued.contains(mark)) continue;
            op.issued.add(mark);
            issueFor(level, force, op, u, roles.getOrDefault(u.path(), "ANVIL"), fwd, right, enemy, doctrine);
        }
    }

    private static void nextPhase(Op op, long now) {
        op.phase++;
        op.phaseStart = now;
    }

    /** A rule ended for this unit: it gets the operation's current order again on the next step. */
    static void resume(ServerLevel level, ForceRegistry.Force force, Forces.UnitView unit, String unitKey) {
        Op op = OPS.get(ForceBrain.forceKey(force));
        if (op != null) op.issued.remove(unit.path() + "#" + op.phase);
    }

    private static void issueFor(ServerLevel level, ForceRegistry.Force force, Op op, Forces.UnitView u, String role,
                                 Vec3 fwd, Vec3 right, @Nullable Vec3 enemy, Doctrine doctrine) {
        String key = UnitOrders.unitKey(force.owner, u.path());
        boolean shooters = u.capabilities().contains(Capability.RANGED_SUPPORT);
        FireMode supportStyle = countRanged(u) >= 4 ? FireMode.VOLLEY : doctrine == Doctrine.HARASS ? FireMode.RETREATING : FireMode.STILL;
        FormationShape shape = switch (role) {
            case "HAMMER" -> FormationShape.V;
            case "FLANK", "RESERVE" -> FormationShape.COLUMN;
            default -> FormationShape.LINE;
        };
        Vec3 slot = lineSlot(op.anchor, role, fwd, right, u.path());
        String why = op.type.name().toLowerCase(Locale.ROOT);
        switch (op.type) {
            case HOLD_LINE -> hold(level, key, slot, op.facing, shape, shooters ? supportStyle : null, why);
            case FALLBACK_FIGHTING -> UnitOrders.issue(level, key, UnitOrders.Kind.FALLBACK, UnitOrders.Source.FORCE,
                    UnitOrders.ground(level, slot), op.facing, shape, shooters ? FireMode.RETREATING : null, 0, why);
            case ADVANCE_BY_TURNS -> {
                boolean frontGroup = role.equals("ANVIL") || role.equals("SCREEN") || role.equals("HAMMER");
                boolean moving = (op.phase % 2 == 0) == frontGroup;
                Vec3 at = moving ? slot.add(fwd.scale(4)) : slot.subtract(fwd.scale(4));
                hold(level, key, at, op.facing, shape, shooters ? (moving ? FireMode.ADVANCING : FireMode.STILL) : null, why);
            }
            case FLANK -> {
                boolean flanker = role.equals("FLANK") || role.equals("HAMMER");
                if (!flanker) {
                    hold(level, key, slot, op.facing, shape, shooters ? supportStyle : null, why);
                } else if (op.phase == 0 && enemy != null) {
                    double side = (u.path().hashCode() & 1) == 0 ? 15 : -15;
                    UnitOrders.issue(level, key, UnitOrders.Kind.MOVE, UnitOrders.Source.FORCE,
                            UnitOrders.ground(level, enemy.add(right.scale(side))), op.facing, FormationShape.COLUMN, null, 0, why);
                } else if (enemy != null) {
                    UnitOrders.issue(level, key, UnitOrders.Kind.ATTACK_AREA, UnitOrders.Source.FORCE, enemy, op.facing,
                            FormationShape.V, null, 16.0D, why);
                }
            }
            case ORDERLY_RETREAT -> {
                boolean coverer = role.equals("ANVIL") || role.equals("SCREEN");
                if (op.phase == 0 && coverer) {
                    UnitOrders.issue(level, key, UnitOrders.Kind.FALLBACK, UnitOrders.Source.FORCE,
                            UnitOrders.ground(level, slot.subtract(fwd.scale(8))), op.facing, FormationShape.LINE, null, 0, why);
                } else {
                    UnitOrders.issue(level, key, UnitOrders.Kind.RETREAT_HQ, UnitOrders.Source.FORCE, null, 0,
                            FormationShape.LINE, null, 0, why);
                }
            }
        }
    }

    private static void hold(ServerLevel level, String key, Vec3 at, float facing, FormationShape shape,
                             @Nullable FireMode style, String why) {
        UnitOrders.issue(level, key, UnitOrders.Kind.HOLD, UnitOrders.Source.FORCE, UnitOrders.ground(level, at), facing,
                shape, style, 0, why);
    }

    /** Where a unit of {@code role} stands in the force's line around the anchor. */
    static Vec3 lineSlot(Vec3 anchor, String role, Vec3 fwd, Vec3 right, String path) {
        return switch (role) {
            case "SCREEN" -> anchor.add(fwd.scale(3));
            case "SUPPORT" -> anchor.subtract(fwd.scale(6));
            case "RESERVE" -> anchor.subtract(fwd.scale(14));
            case "HAMMER" -> anchor.subtract(fwd.scale(10)).add(right.scale(6));
            case "FLANK" -> anchor.add(right.scale((path.hashCode() & 1) == 0 ? 12 : -12));
            default -> anchor;
        };
    }

    private static int countRanged(Forces.UnitView u) {
        int n = 0;
        for (var g : u.members()) {
            var w = net.hfstack.rallyguard.combat.WeaponProfile.of(g.getMainHandItem());
            if (w != null) n++;
        }
        return n;
    }

    @Nullable
    private static Vec3 enemyNear(Map<String, SituationReports.Report> reports) {
        for (SituationReports.Report r : reports.values()) if (r.enemyCentre() != null) return r.enemyCentre();
        return null;
    }

    static Vec3 forward(float yaw) {
        double rad = Math.toRadians(yaw);
        return new Vec3(-Math.sin(rad), 0, Math.cos(rad));
    }

    /** Yaw facing from {@code from} toward {@code to} (Formation convention), 0 if unknown. */
    static float facing(@Nullable Vec3 from, @Nullable Vec3 to) {
        if (from == null || to == null) return 0.0F;
        double dx = to.x - from.x, dz = to.z - from.z;
        return dx * dx + dz * dz < 1.0E-4 ? 0.0F : (float) Math.toDegrees(Math.atan2(-dx, dz));
    }

    /** {@code from} moved {@code dist} blocks directly away from {@code enemy} (or not at all). */
    static Vec3 away(Vec3 from, @Nullable Vec3 enemy, double dist) {
        if (enemy == null) return from;
        Vec3 d = from.subtract(enemy).multiply(1, 0, 1);
        return d.lengthSqr() < 1.0E-4 ? from : from.add(d.normalize().scale(dist));
    }

    static void clearAll() {
        OPS.clear();
    }
}
