package net.hfstack.rallyguard.command;

import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * E2A block 1 - the hierarchy's message: the leader decides, a DIRECTIVE is pushed ONCE
 * to its unit, and each soldier resolves it with local information (playbook §0.2).
 * <p>
 * One {@link Directive} object is shared by every member it was pushed to (pushing is a
 * map write per member, once per order); readers - the decision ladder's first rung
 * (block 4) - only look their own entry up. Memory only: after a restart the guards keep
 * their posts and formation slots (saved elsewhere) and simply have no live directive.
 */
public final class SquadDirectives {
    private SquadDirectives() {
    }

    public enum Kind {
        /** Hold this ground (a post, a formation slot). */
        HOLD,
        /** Take the formation at {@link Directive#anchor}. */
        MUSTER,
        /** March the formation forward. */
        ADVANCE,
        /** Fight a designated target / engage. */
        ATTACK,
        /** Fall back (retreat point, HQ, commander). */
        RETREAT,
        /** Stay with the commander. */
        FOLLOW,
        /** Guard someone (DEFEND ward). */
        DEFEND,
        /** No standing order: free to act on their own judgement. */
        FREE,
        /** The unit lost more captains than it can replace: broken ranks, low morale. */
        ROUTED
    }

    /**
     * @param captain   the unit's computed captain when it was issued
     * @param unitIndex which subunit of the order (0 = front)
     * @param units     how many subunits the order made
     */
    public record Directive(Kind kind, @Nullable Vec3 anchor, float facingYaw, @Nullable UUID captain,
                            long issuedTick, int unitIndex, int units) {
    }

    private static final Map<UUID, Directive> OF = new HashMap<>();

    public static void push(Collection<Guard> members, Directive directive) {
        for (Guard g : members) OF.put(g.getUUID(), directive);
    }

    @Nullable
    public static Directive of(UUID guardId) {
        return OF.get(guardId);
    }

    public static void forget(UUID guardId) {
        OF.remove(guardId);
    }

    public static void clearAll() {
        OF.clear();
    }

    /**
     * Pushes a registered order (anything but Muster, which pushes per subunit itself) as
     * a directive: the guards keep the grouping of their previous directive (so an army
     * mustered into subunits stays in subunits under Advance or Reform), each group gets
     * one new directive of {@code kind} with its captain recomputed; guards without a
     * directive form one more group.
     */
    public static void pushOrder(Kind kind, List<Guard> guards, long now) {
        net.hfstack.rallyguard.force.UnitOrders.notePlayerOrder(guards); // E3A A2: the player's word wins for a while
        Map<Directive, List<Guard>> groups = new IdentityHashMap<>();
        List<Guard> loose = new ArrayList<>();
        for (Guard g : guards) {
            Directive d = OF.get(g.getUUID());
            if (d == null) loose.add(g);
            else groups.computeIfAbsent(d, x -> new ArrayList<>()).add(g);
        }
        for (Map.Entry<Directive, List<Guard>> e : groups.entrySet()) {
            Directive old = e.getKey();
            Guard captain = CommandStructure.captainOf(e.getValue());
            push(e.getValue(), new Directive(kind, old.anchor(), old.facingYaw(),
                    captain == null ? null : captain.getUUID(), now, old.unitIndex(), old.units()));
        }
        if (!loose.isEmpty()) {
            Guard captain = CommandStructure.captainOf(loose);
            push(loose, new Directive(kind, null, 0.0F, captain == null ? null : captain.getUUID(), now, 0, 1));
        }
    }

    /** Directive kind for a registered order id (OrderRegistry names), FREE if unknown. */
    public static Kind kindOfOrder(String orderId) {
        return switch (orderId) {
            case "RECALL", "FOLLOW" -> Kind.FOLLOW;
            case "HOLD", "REFORM" -> Kind.HOLD;
            case "ADVANCE" -> Kind.ADVANCE;
            case "ATTACK" -> Kind.ATTACK;
            case "RETREAT" -> Kind.RETREAT;
            case "DEFEND" -> Kind.DEFEND;
            default -> Kind.FREE;
        };
    }
}
