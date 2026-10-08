package net.hfstack.rallyguard.veterancy;

import com.mojang.logging.LogUtils;
import net.hfstack.rallyguard.config.RallyConfig;
import tallestegg.guardvillagers.common.entities.Guard;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * E3A V6 - a guard's genetics: MCA's personality/traits when they can be read, else the
 * UUID roll of {@link Affinity}. MCA's values on a guard are not reachable through any API
 * documented in {@code docs/API_MODS.md} (a Mob Compat guard is bridged to a hidden MCA
 * villager), so this tries, by reflection and fail-soft, each chain in
 * {@code veterancy.mcaPersonalityAccessors}: either {@code "a.b.c"} (no-argument methods
 * called on the guard, then on each result) or {@code "some.Class#staticMethod.b.c"} (a
 * public static method taking the guard first). The final value's {@code name()} /
 * {@code toString()} is mapped to an {@link Affinity}: ATHLETIC/PEPPY -> ATHLETIC,
 * CONFIDENT -> CONFIDENT, GLOOMY -> GLOOMY, SHY/SENSITIVE -> SHY, LAZY/ODD... -> CALM;
 * MCA's TOUGH trait -> TOUGH. Results are cached per guard; the first failure of a chain
 * is logged once at DEBUG. Open question for David/Cowork: the real accessor on 1.21.1.
 */
public final class Genetics {
    private Genetics() {
    }

    private static final Map<UUID, List<Affinity>> CACHE = new HashMap<>();
    private static boolean warned;

    public static List<Affinity> of(Guard guard) {
        return CACHE.computeIfAbsent(guard.getUUID(), id -> {
            List<Affinity> mca = fromMca(guard);
            return mca.isEmpty() ? Affinity.of(id) : mca;
        });
    }

    public static boolean timid(Guard guard) {
        List<Affinity> a = of(guard);
        return a.contains(Affinity.GLOOMY) || a.contains(Affinity.SHY);
    }

    private static List<Affinity> fromMca(Guard guard) {
        List<Affinity> out = new ArrayList<>();
        List<? extends String> chains = RallyConfig.get(RallyConfig.MCA_ACCESSORS::get, List.of());
        for (String chain : chains) {
            Object value = resolve(guard, chain);
            if (value == null) continue;
            addMapped(out, value);
            if (!out.isEmpty()) break;
        }
        return out;
    }

    private static void addMapped(List<Affinity> out, Object value) {
        if (value instanceof Collection<?> many) {
            for (Object o : many) addMapped(out, o);
            return;
        }
        String name = value instanceof Enum<?> e ? e.name() : String.valueOf(value);
        Affinity a = switch (name.toUpperCase(Locale.ROOT)) {
            case "ATHLETIC", "PEPPY" -> Affinity.ATHLETIC;
            case "CONFIDENT", "GRUMPY" -> Affinity.CONFIDENT;
            case "GLOOMY" -> Affinity.GLOOMY;
            case "SHY", "SENSITIVE" -> Affinity.SHY;
            case "TOUGH" -> Affinity.TOUGH;
            case "LAZY", "ODD", "FRIENDLY", "WITTY", "FLIRTY", "GREEDY" -> Affinity.CALM;
            default -> null;
        };
        if (a != null && !out.contains(a) && out.size() < 2) out.add(a);
    }

    private static Object resolve(Guard guard, String chain) {
        try {
            Object current;
            String[] steps;
            int hash = chain.indexOf('#');
            if (hash > 0) {
                Class<?> owner = Class.forName(chain.substring(0, hash));
                String rest = chain.substring(hash + 1);
                steps = rest.split("\\.");
                Method first = null;
                for (Method m : owner.getMethods()) {
                    if (m.getName().equals(steps[0]) && m.getParameterCount() == 1
                            && m.getParameterTypes()[0].isInstance(guard) && java.lang.reflect.Modifier.isStatic(m.getModifiers())) {
                        first = m;
                        break;
                    }
                }
                if (first == null) return null;
                current = first.invoke(null, guard);
                steps = java.util.Arrays.copyOfRange(steps, 1, steps.length);
            } else {
                current = guard;
                steps = chain.split("\\.");
            }
            for (String step : steps) {
                if (current == null || step.isEmpty()) return current;
                Method m = current.getClass().getMethod(step);
                current = m.invoke(current);
            }
            return current;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            if (!warned) {
                warned = true;
                LogUtils.getLogger().debug("[rallyguard] MCA genetics not readable via '{}' ({}); using UUID affinities", chain, e.toString());
            }
            return null;
        }
    }

    public static void forget(UUID guardId) {
        CACHE.remove(guardId);
    }

    public static void clearAll() {
        CACHE.clear();
    }
}
