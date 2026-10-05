package net.hfstack.rallyguard.combat;

import java.util.Locale;

/**
 * E1-G: how a ranged guard moves while it shoots (per guard; the Ledger sets it for a
 * whole selection/squad, and the Battle Planner's doctrines will set it later).
 * <ul>
 *     <li>{@link #NORMAL}: Guard Villagers' own fire up close; beyond it Rally closes to
 *     the weapon's optimal range and shoots (long shots).</li>
 *     <li>{@link #STILL}: shoots from where it stands, never moves for the fight.</li>
 *     <li>{@link #ADVANCING}: shoots while walking toward the target.</li>
 *     <li>{@link #RETREATING}: keeps at least the weapon's minimum range, stepping back
 *     while it shoots when the enemy closes in.</li>
 *     <li>{@link #VOLLEY}: stands still and arcs arrows at targets out of direct range or
 *     out of sight.</li>
 * </ul>
 */
public enum FireMode {
    NORMAL, STILL, ADVANCING, RETREATING, VOLLEY;

    public static FireMode parse(String s) {
        if (s == null || s.isEmpty()) return NORMAL;
        try {
            return valueOf(s.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return NORMAL;
        }
    }

    public FireMode next() {
        return values()[(ordinal() + 1) % values().length];
    }
}
