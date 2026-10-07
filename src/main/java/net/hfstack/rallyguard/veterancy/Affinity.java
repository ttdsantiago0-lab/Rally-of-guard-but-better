package net.hfstack.rallyguard.veterancy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * E2A block 2 - genetics: 1-2 affinities fixed at birth ({@code docs/VETERANIA.md} §1.1).
 * The names follow MCA's personalities/traits so an MCA-backed guard reads the same; MCA's
 * real values aren't reachable here (no documented accessor in {@code docs/API_MODS.md} for
 * a bridged guard's personality - open question), so every guard rolls them from its UUID:
 * stable across restarts with nothing saved, and different per guard. Genetics BIAS trait
 * choice and nerve, they decide nothing alone.
 */
public enum Affinity {
    ATHLETIC, CONFIDENT, TOUGH, GLOOMY, SHY, CALM;

    public static List<Affinity> of(UUID guardId) {
        Random random = new Random(guardId.getMostSignificantBits() ^ (guardId.getLeastSignificantBits() * 31L));
        Affinity[] all = values();
        List<Affinity> out = new ArrayList<>(2);
        out.add(all[random.nextInt(all.length)]);
        if (random.nextInt(3) == 0) {
            Affinity second = all[random.nextInt(all.length)];
            if (second != out.get(0)) out.add(second);
        }
        return out;
    }

    /** GLOOMY and SHY guards lose their nerve sooner (higher retreat threshold). */
    public static boolean timid(UUID guardId) {
        List<Affinity> a = of(guardId);
        return a.contains(GLOOMY) || a.contains(SHY);
    }
}
