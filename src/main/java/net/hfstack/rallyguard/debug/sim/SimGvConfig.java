package net.hfstack.rallyguard.debug.sim;

import tallestegg.guardvillagers.configuration.GuardConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Torneo 2026-11-04: Guard Villagers' {@code MobBlackList} (default: villager, iron golem, wandering trader,
 * guard, <b>creeper</b>, enderman) makes {@code Guard#setTarget} silently refuse creepers, so in ronda 1 no guard
 * ever shot or hit one (0 shots, 0 damage in all 59 creeper rows): "victory" only meant the creepers had all
 * blown themselves up. A scenario tagged {@code gv:creepers} takes {@code minecraft:creeper} off that list for
 * its run only (the same runtime {@code ConfigValue#set} {@code GuardVillagersConfigPatcher} uses, plus
 * {@code clearCache} so the next {@code get()} reads it whatever the value's restart type), and the original
 * list comes back when the run ends or the batch stops. Nothing outside a simulation run is touched.
 * Rally's own filter ({@code CommandTargeting.wouldAccept}) reads the same list, so both agree.
 */
public final class SimGvConfig {
    private SimGvConfig() {
    }

    private static final String CREEPER = "minecraft:creeper";
    private static List<String> saved;

    /** Applies the scenario's GV overrides; returns a note if something couldn't be applied, else "". */
    public static String apply(SimScenario scenario) {
        restore();
        if (!scenario.tags().contains("gv:creepers")) return "";
        try {
            List<String> now = new ArrayList<>();
            for (Object o : GuardConfig.COMMON.MobBlackList.get()) now.add(String.valueOf(o));
            if (!now.contains(CREEPER)) return "";
            saved = new ArrayList<>(now);
            now.remove(CREEPER);
            GuardConfig.COMMON.MobBlackList.set(now);
            GuardConfig.COMMON.MobBlackList.clearCache();
            return "";
        } catch (RuntimeException e) {
            saved = null;
            return scenario.id() + ": no se pudo quitar el creeper de la lista negra de Guard Villagers (" + e + ")";
        }
    }

    /** Puts the original list back (no-op when nothing was changed). */
    public static void restore() {
        if (saved == null) return;
        try {
            GuardConfig.COMMON.MobBlackList.set(saved);
            GuardConfig.COMMON.MobBlackList.clearCache();
        } catch (RuntimeException ignored) {
            // the server is going down; nothing was written to disk anyway
        }
        saved = null;
    }

    /** 1 while this run's guards may attack creepers. */
    public static boolean creepersAllowed() {
        return saved != null;
    }
}
