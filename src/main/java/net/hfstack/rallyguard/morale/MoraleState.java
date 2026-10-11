package net.hfstack.rallyguard.morale;

/**
 * E3B M ({@code docs/MORAL_Y_COHESION.md} §3) - a guard's morale state. Effects are
 * PREFERENCES in the decision ladder only (never health, damage or speed):
 * <ul>
 *     <li>FIRME (≥ 60) - normal;</li>
 *     <li>INQUIETO (40-60) - retreats a little sooner, keeps its place instead of charging;</li>
 *     <li>VACILANTE (20-40) - breaks on contact unless its captain is near; starts no attack; in a
 *     cohesive unit it REFUSES to attack but holds its ground;</li>
 *     <li>ROTO (&lt; 20) - stops fighting unless cornered and runs to its captain or HQ;</li>
 *     <li>DESTROZADO - broken, without captain or general and with over half the unit lost: no rally
 *     in this battle, only rest at the HQ brings it back.</li>
 * </ul>
 * Ordinals are compared ("worse than"): append only.
 */
public enum MoraleState {
    FIRME, INQUIETO, VACILANTE, ROTO, DESTROZADO;

    public boolean broken() {
        return this == ROTO || this == DESTROZADO;
    }

    public boolean worseThan(MoraleState other) {
        return ordinal() > other.ordinal();
    }
}
