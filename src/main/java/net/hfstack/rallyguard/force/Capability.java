package net.hfstack.rallyguard.force;

/**
 * E3A A4 / ARQUITECTURA_VETERANIA §8 - what a squad or unit CAN do, from its composition.
 * Links between groups and plan phases are written against capabilities, never concrete
 * skills, so new roles (engineers, recon...) enter without rewriting the planner. The last
 * four are reserved: no composition provides them yet, and a plan needing them reports
 * "missing capabilities" instead of failing silently.
 */
public enum Capability {
    MELEE_LINE, RANGED_SUPPORT, MOUNTED_CHARGE, SCREEN, ESCORT, HOLD_POSITION, RETREAT_COVER,
    RECON, ENGINEER, LOGISTICS, ASSASSIN
}
