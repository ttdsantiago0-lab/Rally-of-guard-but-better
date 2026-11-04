package net.hfstack.rallyguard.debug.sim;

import tallestegg.guardvillagers.configuration.GuardConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Torneo 2026-11-04: per-run switches for behaviour that lives in Guard Villagers' own config or in a Rally constant,
 * so the simulation can MEASURE a behaviour before anyone decides it should become a doctrine. Each one is set when a
 * run starts and put back when it ends (or the batch stops); nothing outside a simulation run is touched, and nothing
 * is written to disk (the same runtime {@code ConfigValue#set} {@code GuardVillagersConfigPatcher} uses, plus
 * {@code clearCache} so the next {@code get()} reads it whatever the value's restart type).
 * <ul>
 *     <li>{@code gv:creepers} - takes {@code minecraft:creeper} off GV's {@code MobBlackList} (default: villager,
 *     iron golem, wandering trader, guard, creeper, enderman), which makes {@code Guard#setTarget} refuse creepers:
 *     in ronda 1 no guard ever shot or hit one. Rally's {@code CommandTargeting.wouldAccept} reads the same list.</li>
 *     <li>{@code gv:shieldAlways} - GV's "Have Guards raise their shield all the time?" ({@code GuardRaiseShield}):
 *     the shield stays up whenever the guard has a target, instead of only within 4 blocks.</li>
 *     <li>{@code clearance:<blocks>} - how far from the landing point a friend must be for an archer to shoot
 *     ({@code RangedFireGoal}, 5 by default). Friendly arrows already fly through our own side
 *     ({@code RallyFriendlyFireHandler}), and the first real-kit calibration showed archers declining 2-5 shots for
 *     every one they took because of it.</li>
 * </ul>
 */
public final class SimGvConfig {
    private SimGvConfig() {
    }

    private static final String CREEPER = "minecraft:creeper";
    private static List<String> savedBlackList;
    private static Boolean savedRaiseShield;
    /** Archer friend clearance for this run, or NaN for the default. Read by {@code RangedFireGoal}. */
    private static double clearance = Double.NaN;

    /** Applies the scenario's switches; returns a note if something couldn't be applied, else "". */
    public static String apply(SimScenario scenario) {
        restore();
        StringBuilder note = new StringBuilder();
        if (scenario.tags().contains("gv:creepers")) {
            try {
                List<String> now = new ArrayList<>();
                for (Object o : GuardConfig.COMMON.MobBlackList.get()) now.add(String.valueOf(o));
                if (now.contains(CREEPER)) {
                    savedBlackList = new ArrayList<>(now);
                    now.remove(CREEPER);
                    GuardConfig.COMMON.MobBlackList.set(now);
                    GuardConfig.COMMON.MobBlackList.clearCache();
                }
            } catch (RuntimeException e) {
                savedBlackList = null;
                note.append(scenario.id()).append(": no se pudo quitar el creeper de la lista negra de Guard Villagers (")
                        .append(e).append(") ");
            }
        }
        if (scenario.tags().contains("gv:shieldAlways")) {
            try {
                savedRaiseShield = GuardConfig.COMMON.GuardRaiseShield.get();
                GuardConfig.COMMON.GuardRaiseShield.set(Boolean.TRUE);
                GuardConfig.COMMON.GuardRaiseShield.clearCache();
            } catch (RuntimeException e) {
                savedRaiseShield = null;
                note.append(scenario.id()).append(": no se pudo activar GuardRaiseShield (").append(e).append(") ");
            }
        }
        String c = scenario.tag("clearance");
        if (!c.isEmpty()) {
            try {
                clearance = Math.max(0.0D, Double.parseDouble(c));
            } catch (NumberFormatException e) {
                note.append(scenario.id()).append(": clearance '").append(c).append("' no es un número ");
            }
        }
        return note.toString().trim();
    }

    /** Puts everything back (no-op when nothing was changed). */
    public static void restore() {
        try {
            if (savedBlackList != null) {
                GuardConfig.COMMON.MobBlackList.set(savedBlackList);
                GuardConfig.COMMON.MobBlackList.clearCache();
            }
            if (savedRaiseShield != null) {
                GuardConfig.COMMON.GuardRaiseShield.set(savedRaiseShield);
                GuardConfig.COMMON.GuardRaiseShield.clearCache();
            }
        } catch (RuntimeException ignored) {
            // the server is going down; nothing was written to disk anyway
        }
        savedBlackList = null;
        savedRaiseShield = null;
        clearance = Double.NaN;
    }

    /** True while this run's guards may attack creepers. */
    public static boolean creepersAllowed() {
        return savedBlackList != null;
    }

    /** True while this run's guards keep their shields up whenever they have a target. */
    public static boolean shieldAlways() {
        return savedRaiseShield != null;
    }

    /** The archer friend clearance for this run, or {@code fallback} outside a run that sets one. */
    public static double clearance(double fallback) {
        return Double.isNaN(clearance) ? fallback : clearance;
    }
}
