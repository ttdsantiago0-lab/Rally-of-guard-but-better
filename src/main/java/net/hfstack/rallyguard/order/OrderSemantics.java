package net.hfstack.rallyguard.order;

import com.mojang.logging.LogUtils;
import net.hfstack.rallyguard.combat.WeaponProfile;
import net.hfstack.rallyguard.compat.ScorchedGunsCompat;
import org.slf4j.Logger;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * E2A block 5 - universal orders (FEEDBACK_E1 §C): what every order means for every role,
 * and how a mixed group degrades an order that doesn't apply to some of it.
 * <pre>
 * order          melee                 archer (bow/crossbow)      gunner (Scorched Guns)   mounted melee
 * FOLLOW/RECALL  come / come and hold  same                       same                     same, riding
 * HOLD           hold the post         hold, shoot from it        hold, shoot from it      halt the horse
 * ADVANCE        march the formation   march, stop at optimal     same as archer           ride forward
 * REFORM         back to its slot      same                       same                     same
 * BREAK_RANKS    free                  free                       free                     free
 * RETREAT        fall back (HQ, point) same                       same                     gallop back
 * ATTACK         engage (cap applies)  fire from range (ladder)   fire from range          charge
 * FIRE_ADVANCING -> ADVANCE            as ordered                 as ordered               -> ADVANCE
 * FIRE_STILL     -> HOLD               as ordered                 as ordered               -> HOLD
 * FIRE_VOLLEY    -> HOLD (screen)      as ordered                 as ordered               -> HOLD
 * FIRE_RETREATING-> HOLD (no kiting)   as ordered                 as ordered               -> HOLD
 * FIRE_NORMAL    nothing to change     as ordered                 as ordered               nothing
 * </pre>
 * Movement orders mean the same thing for everyone and are resolved locally by each role
 * (the decision ladder keeps archers at range, the mount driver makes riders charge). Only
 * the fire styles are role-specific; every degradation is logged at DEBUG and reported to
 * the commander - never a silent no-op.
 */
public final class OrderSemantics {
    private OrderSemantics() {
    }

    private static final Logger LOGGER = LogUtils.getLogger();
    /** The plan key for "nothing to do for this role". */
    public static final String NOTHING = "";

    public enum Role { MELEE, ARCHER, GUNNER, MOUNTED }

    public static Role roleOf(Guard guard) {
        if (ScorchedGunsCompat.isGun(guard.getMainHandItem())) return Role.GUNNER;
        WeaponProfile weapon = WeaponProfile.of(guard.getMainHandItem());
        if (weapon != null && weapon.firesArrows()) return Role.ARCHER;
        return guard.isPassenger() ? Role.MOUNTED : Role.MELEE;
    }

    /** The order {@code role} actually carries out for {@code orderId} ({@link #NOTHING} = none). */
    public static String effective(String orderId, Role role) {
        String id = orderId.toUpperCase(Locale.ROOT);
        if (!id.startsWith("FIRE_") || role == Role.ARCHER || role == Role.GUNNER) return id;
        return switch (id) {
            case "FIRE_ADVANCING" -> "ADVANCE";
            case "FIRE_STILL", "FIRE_VOLLEY", "FIRE_RETREATING" -> "HOLD";
            default -> NOTHING; // FIRE_NORMAL: a melee guard has no fire style to reset
        };
    }

    /** One executable part of an order: who carries out which order id, and why. */
    public record Part(String orderId, List<Guard> guards, Role role, boolean degraded) {
    }

    /**
     * Splits an order over a mixed group: as-ordered guards first, then one part per
     * (role, degraded order). Degradations are logged; {@link #NOTHING} parts are returned
     * too (so the commander can be told) but must not be executed.
     */
    public static List<Part> plan(String orderId, List<Guard> guards) {
        String id = orderId.toUpperCase(Locale.ROOT);
        Map<String, Part> parts = new LinkedHashMap<>();
        for (Guard g : guards) {
            Role role = roleOf(g);
            String eff = effective(id, role);
            boolean degraded = !eff.equals(id);
            String key = degraded ? role + ">" + eff : id;
            parts.computeIfAbsent(key, k -> new Part(eff, new ArrayList<>(), role, degraded)).guards().add(g);
        }
        for (Part p : parts.values()) {
            if (p.degraded()) {
                LOGGER.debug("[rallyguard] order {} does not apply to {} {} guard(s): {}", id, p.guards().size(),
                        p.role(), p.orderId().isEmpty() ? "nothing to do" : "degraded to " + p.orderId());
            }
        }
        return new ArrayList<>(parts.values());
    }
}
