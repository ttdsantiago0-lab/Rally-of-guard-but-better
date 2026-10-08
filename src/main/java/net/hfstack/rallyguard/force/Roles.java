package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.contract.ForceRegistry;
import net.hfstack.rallyguard.tactics.Doctrine;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E3A A3 - unit roles inside a force: ANVIL (holds the line), HAMMER (strikes), SCREEN
 * (protects the ranged units' front), SUPPORT (ranged fire from behind), FLANK (goes around),
 * RESERVE (waits). A role declared with {@code /rallyforce role} (or in the planner) is kept;
 * every other unit gets one from its composition and the force's doctrine, recomputed on each
 * call - so when captains fall and units shrink or merge, the general re-plans on the next
 * evaluation. This is the general's proposal; the player corrects it.
 */
public final class Roles {
    private Roles() {
    }

    public static final List<String> ALL = List.of("ANVIL", "HAMMER", "SCREEN", "SUPPORT", "FLANK", "RESERVE");

    public static boolean isRole(String role) {
        return ALL.contains(role.toUpperCase(Locale.ROOT));
    }

    /** Unit path -> role, for every unit of the force (declared roles first, then automatic). */
    public static Map<String, String> assign(ServerLevel level, ForceRegistry.Force force, List<Forces.UnitView> units) {
        Map<String, String> out = new LinkedHashMap<>();
        Doctrine doctrine = Doctrine.parse(force.doctrine);
        List<Forces.UnitView> melee = new ArrayList<>(), ranged = new ArrayList<>(), cavalry = new ArrayList<>();
        for (Forces.UnitView u : units) {
            if (u.members().isEmpty()) continue;
            if (!u.role().isEmpty()) {
                out.put(u.path(), u.role().toUpperCase(Locale.ROOT));
                continue;
            }
            switch (Forces.typeOf(u)) {
                case "cavalry" -> cavalry.add(u);
                case "archers" -> ranged.add(u);
                default -> melee.add(u);
            }
        }
        for (Forces.UnitView u : ranged) out.put(u.path(), "SUPPORT");
        boolean striking = doctrine == Doctrine.ALL_OUT || doctrine == Doctrine.HARASS;
        for (Forces.UnitView u : cavalry) out.put(u.path(), striking ? "HAMMER" : "FLANK");
        melee.sort((a, b) -> Integer.compare(b.members().size(), a.members().size()));
        boolean keepReserve = (doctrine == Doctrine.HOLD_POSITION || doctrine == Doctrine.AVOID_CASUALTIES)
                && units.size() >= 3 && melee.size() >= 2;
        for (int i = 0; i < melee.size(); i++) {
            Forces.UnitView u = melee.get(i);
            String role;
            if (i == 0) role = "ANVIL";
            else if (keepReserve && i == melee.size() - 1) role = "RESERVE";
            else if (!ranged.isEmpty() && !out.containsValue("SCREEN")) role = "SCREEN";
            else role = striking ? "HAMMER" : "FLANK";
            out.put(u.path(), role);
        }
        return out;
    }
}
