package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.command.CommandStructure;
import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.event.GuardStanceHandler;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.minecraft.server.level.ServerLevel;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * E3A A1 - reading forces: which force a guard belongs to, its units (with members, computed
 * captain, capabilities and role) and its computed general. Everything is derived on demand
 * from {@link ForceRegistry} + {@link SquadRegistry} + the loaded guards; nothing is stored
 * about captains or generals.
 * <p>
 * A unit's TYPE follows its composition ({@link #capabilities}): infantry
 * ({@code MELEE_LINE}), archers ({@code RANGED_SUPPORT}), cavalry ({@code MOUNTED_CHARGE}),
 * or reserve (its declared role).
 */
public final class Forces {
    private Forces() {
    }

    /** One unit of a force: its path "Squad/Unit" ("Squad" for guards in no unit). */
    public record UnitView(String path, String name, List<Guard> members, @Nullable Guard captain,
                           EnumSet<Capability> capabilities, String role) {
    }

    @Nullable
    public static ForceRegistry.Force forceOf(ServerLevel level, Guard guard) {
        UUID owner = GuardOwnership.getOwner(guard);
        if (owner == null) return null;
        Optional<SquadRegistry.Squad> squad = SquadRegistry.get(level).squadOf(guard.getUUID());
        if (squad.isEmpty()) return null;
        return ForceRegistry.get(level).find(owner, squad.get().name);
    }

    /** The doctrine of the force the guard belongs to, or null if it is in none. */
    @Nullable
    public static Doctrine doctrineOf(ServerLevel level, Guard guard) {
        ForceRegistry.Force f = forceOf(level, guard);
        return f == null ? null : Doctrine.parse(f.doctrine);
    }

    /** The force's units (declared ones first, then any other unit its guards are in), best-captain order. */
    public static List<UnitView> units(ServerLevel level, ForceRegistry.Force force) {
        SquadRegistry squads = SquadRegistry.get(level);
        Map<String, List<Guard>> byUnit = new LinkedHashMap<>();
        for (String declared : force.units.keySet()) byUnit.put(declared.toLowerCase(Locale.ROOT), new ArrayList<>());
        for (Guard g : GuardStanceHandler.loaded(level.dimension())) {
            if (!g.isAlive() || !force.owner.equals(GuardOwnership.getOwner(g))) continue;
            Optional<String> path = squads.pathOf(g.getUUID());
            if (path.isEmpty()) continue;
            String[] parts = SquadRegistry.splitPath(path.get());
            if (!parts[0].equalsIgnoreCase(force.squad)) continue;
            String unit = parts.length > 1 ? parts[1].toLowerCase(Locale.ROOT) : "";
            byUnit.computeIfAbsent(unit, k -> new ArrayList<>()).add(g);
        }
        List<UnitView> out = new ArrayList<>();
        for (Map.Entry<String, List<Guard>> e : byUnit.entrySet()) {
            String name = e.getKey();
            String role = "";
            for (Map.Entry<String, String> d : force.units.entrySet()) if (d.getKey().equalsIgnoreCase(name)) role = d.getValue();
            List<Guard> members = e.getValue();
            String path = name.isEmpty() ? force.squad : force.squad + SquadRegistry.PATH_SEPARATOR + name;
            // E3B G2: capabilities come from the unit's (cached) group profile - one source of truth.
            EnumSet<Capability> caps = members.isEmpty() ? EnumSet.noneOf(Capability.class)
                    : EnumSet.copyOf(net.hfstack.rallyguard.command.GroupProfile.cached(
                    UnitOrders.unitKey(force.owner, path), members, level.getGameTime()).capabilities());
            out.add(new UnitView(path, name, members, CommandStructure.captainOf(members), caps, role));
        }
        out.sort(Comparator.comparingDouble((UnitView u) -> u.captain() == null ? -1 : -Leadership.score(u.captain())));
        return out;
    }

    /** The force's general: the best leadership score among its unit captains. */
    @Nullable
    public static Guard generalOf(ServerLevel level, ForceRegistry.Force force) {
        Guard best = null;
        for (UnitView u : units(level, force)) {
            if (u.captain() != null && (best == null || Leadership.score(u.captain()) > Leadership.score(best))) best = u.captain();
        }
        return best;
    }

    /**
     * What a group can do. E3B G2: derived from its {@link net.hfstack.rallyguard.command.GroupProfile}
     * (same rules as E3A: half or more melee on foot = MELEE_LINE, SCREEN from 4; half or more
     * shooters = RANGED_SUPPORT, RETREAT_COVER from 2; half or more mounted = MOUNTED_CHARGE;
     * 2+ = ESCORT and HOLD_POSITION; plus RECON for scouts).
     */
    public static EnumSet<Capability> capabilities(List<Guard> members) {
        return net.hfstack.rallyguard.command.GroupProfile.of(members).capabilities();
    }

    /** "infantry" / "archers" / "cavalry" / "reserve" / "mixed" for a unit. */
    public static String typeOf(UnitView u) {
        if ("RESERVE".equalsIgnoreCase(u.role())) return "reserve";
        if (u.capabilities().contains(Capability.MOUNTED_CHARGE)) return "cavalry";
        if (u.capabilities().contains(Capability.RANGED_SUPPORT)) return "archers";
        if (u.capabilities().contains(Capability.MELEE_LINE)) return "infantry";
        return "mixed";
    }
}
