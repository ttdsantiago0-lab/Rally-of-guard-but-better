package net.hfstack.rallyguard.veterancy;

/**
 * E2A block 2 - one trait per rank gained, chosen from genetics + history
 * ({@code docs/VETERANIA.md} §1.3). Traits change DECISIONS (the decision ladder, totem
 * handling), never raw damage.
 */
public enum Trait {
    /** Came back from near death many times: brings a totem to hand faster, retreats later. */
    TANK,
    /** Many ranged/gun kills: takes long shots, prefers height and line of sight. */
    MARKSMAN,
    /** Many melee kills: presses the attack, ignores the "too crowded" retreat sooner. */
    DUELIST,
    /** Long hours mounted: keeps formation on horseback. */
    RIDER,
    /** Survived many battles: never breaks formation under pressure. */
    STEADY,
    /** Confident attacker: advances first. */
    ASSAULT,
    /** Fought many kinds of enemy: spots priority threats (flyers, ranged, explosive) first. */
    SCOUT,
    /** E3A A1: led units well (command XP, reliefs taken): the leadership trait of the score. */
    LEADER
}
