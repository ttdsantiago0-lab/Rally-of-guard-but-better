package net.hfstack.rallyguard.force;

import net.hfstack.rallyguard.contract.ForceRegistry;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.Map;

/** E3A A4 - Battle Planner plans (filled in by block A4). */
public final class Plans {
    private Plans() {
    }

    static List<ForceRules.Rule> rulesFor(ServerLevel level, ForceRegistry.Force force) {
        return List.of();
    }

    static void step(ServerLevel level, ForceRegistry.Force force, List<Forces.UnitView> units, Map<String, String> roles,
                     Map<String, SituationReports.Report> reports, long now) {
    }

    static void clearAll() {
    }
}
