package net.hfstack.rallyguard.tactics;

/**
 * E3A V11 - why a guard decided what it did ({@code docs/ARQUITECTURA_VETERANIA.md} §9).
 * One code per decision; recorded only in debug mode and only when it CHANGES
 * ({@link DecisionTrace}); the simulation counts them per code.
 */
public enum DecisionReason {
    NONE,
    STUCK,
    ORDER_OVERRIDE,
    LEADER_DIRECTIVE,
    UNIT_BROKEN,
    LOW_HEALTH_RETREAT,
    PROTECT_ALLY,
    FORMATION_HOLD,
    SEEK_ENEMY,
    COVER_AVAILABLE,
    HIGH_GROUND,
    TOO_CLOSE,
    OUT_OF_RANGE,
    TARGET_PRIORITY_AIR,
    TARGET_PRIORITY,
    FOCUS_FIRE,
    COUNTERATTACK,
    WEAPON_MISMATCH,
    AMMO_LOW,
    OUTNUMBERED,
    OPEN_ENCOUNTER,
    SHOOT_AND_MOVE,
    PAIR_BOUND,
    TAKE_POSITION,
    RESUME_MISSION,
    ENGAGE,
    DEGRADED_BUDGET,
    RELAXED,
    /** E3B G3/V7: following its subgroup's interpreted instruction. */
    INSTRUCTION,
    /** E3B M: morale shaken (INQUIETO) - keeps its place instead of charging. */
    MORALE_SHAKEN,
    /** E3B M: wavering (VACILANTE) - breaks on contact, starts no attack. */
    MORALE_WAVERING,
    /** E3B M: broken - runs to its captain or HQ. */
    ROUTED_FLEE,
    /** E3B M: rallied around its captain or banner. */
    RALLIED,
    /** E3B M: a cohesive unit that wavers holds its ground but refuses to attack. */
    REFUSE_ATTACK,
    /** E3B V2: keeps its distance from a kind of enemy that nearly killed it before. */
    THREAT_MEMORY
}
