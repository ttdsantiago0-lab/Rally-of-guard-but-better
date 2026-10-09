package net.hfstack.rallyguard.morale;

import net.hfstack.rallyguard.command.GroupProfile;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.event.GuardStanceHandler;
import net.hfstack.rallyguard.force.UnitOrders;
import net.hfstack.rallyguard.veterancy.VeterancyEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * E3B M ({@code docs/MORAL_Y_COHESION.md} §1.2) - how well a UNIT holds together, 0..100, apart
 * from its morale: formation integrity (mean distance to the unit's centre), its captain alive,
 * how consistent its composition is ({@link GroupProfile}) and how long it has fought together
 * ({@link UnitBond}, V4):
 * <pre>cohesion = 100 × (0.35 integrity + 0.25 captain + 0.20 consistency + 0.20 bond)</pre>
 * Computed at most once a second per unit, by whichever member's turn comes first (in practice the
 * captain's, which moves first), and cached. A cohesive unit (≥ 60) that wavers REFUSES to attack
 * instead of running ({@link MoraleState#VACILANTE}).
 */
public final class Cohesion {
    private Cohesion() {
    }

    private record Cached(double value, long tick) {
    }

    private static final Map<String, Cached> CACHE = new HashMap<>();

    /** The guard's unit key: its squad/unit (or ad-hoc group), else its pushed directive, else itself. */
    public static String unitKey(ServerLevel level, Guard guard) {
        String key = UnitOrders.unitKeyOf(level, guard);
        if (key != null) return key;
        SquadDirectives.Directive d = SquadDirectives.of(guard.getUUID());
        return d != null ? "d" + System.identityHashCode(d) : "g" + guard.getUUID();
    }

    /** The members of {@link #unitKey} that are loaded and alive. */
    public static List<Guard> members(ServerLevel level, Guard guard, String key) {
        if (key.startsWith("g")) return List.of(guard);
        if (key.startsWith("d")) {
            SquadDirectives.Directive d = SquadDirectives.of(guard.getUUID());
            List<Guard> out = new ArrayList<>();
            for (Guard g : GuardStanceHandler.loaded(level.dimension())) {
                if (g.isAlive() && SquadDirectives.of(g.getUUID()) == d) out.add(g);
            }
            if (!out.contains(guard)) out.add(guard);
            return out;
        }
        return UnitOrders.members(level, key);
    }

    /** 0..100 for the guard's unit (cached one second). */
    public static double of(ServerLevel level, Guard guard) {
        String key = unitKey(level, guard);
        long now = level.getGameTime();
        Cached c = CACHE.get(key);
        if (c != null && now - c.tick() < 20) return c.value();
        List<Guard> members = members(level, guard, key);
        double v = compute(level, key, members, now);
        if (CACHE.size() > 4096) CACHE.clear();
        CACHE.put(key, new Cached(v, now));
        return v;
    }

    /** Cached value only (-1 = not computed in the last 2 s). */
    public static double peek(String key, long now) {
        Cached c = CACHE.get(key);
        return c == null || now - c.tick() > 40 ? -1 : c.value();
    }

    static double compute(ServerLevel level, String key, List<Guard> members, long now) {
        if (members.size() <= 1) {
            boolean fighting = !members.isEmpty() && VeterancyEvents.inFight(members.get(0));
            UnitBond.update(key, members, fighting, now);
            return 100.0D;
        }
        double cx = 0, cz = 0;
        boolean fighting = false;
        for (Guard g : members) {
            cx += g.getX();
            cz += g.getZ();
            if (VeterancyEvents.inFight(g)) fighting = true;
        }
        cx /= members.size();
        cz /= members.size();
        double spread = 0;
        for (Guard g : members) spread += Math.sqrt((g.getX() - cx) * (g.getX() - cx) + (g.getZ() - cz) * (g.getZ() - cz));
        spread /= members.size();
        double integrity = Math.max(0.0D, Math.min(1.0D, (12.0D - spread) / 8.0D)); // ≤ 4 blocks = 1, ≥ 12 = 0
        double captain = 0.5D;
        SquadDirectives.Directive d = SquadDirectives.of(members.get(0).getUUID());
        if (d != null && d.captain() != null) {
            Entity e = level.getEntity(d.captain());
            captain = e instanceof Guard c && c.isAlive() ? 1.0D : 0.0D;
        }
        double consistency = GroupProfile.cached(key, members, now).consistency() / 100.0D;
        double bond = UnitBond.update(key, members, fighting, now);
        double c = 100.0D * (0.35D * integrity + 0.25D * captain + 0.20D * consistency + 0.20D * bond);
        // E3B P1 TEMPO: an assault pace stretches the ranks - cohesion pays for it.
        if (UnitOrders.tempoOf(key) >= 1.4D) c -= 15.0D;
        return Math.max(0.0D, c);
    }

    public static void clearAll() {
        CACHE.clear();
        UnitBond.clearAll();
    }
}
