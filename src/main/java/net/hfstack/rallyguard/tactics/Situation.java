package net.hfstack.rallyguard.tactics;

import net.hfstack.rallyguard.command.SquadDirectives;

import javax.annotation.Nullable;

/**
 * E2A block 4 - everything {@link DecisionLadder} looks at, built once a second per guard
 * from CHEAP signals only (playbook §0.3): health, distances, allies/enemies counted by the
 * squad's shared picture, ammo, enemy type, and the squad's cached terrain reading. No block
 * or entity scans happen here - see {@link Tactics#situationOf}.
 *
 * @param directive       the unit's pushed directive kind (null = none: free judgement)
 * @param playerOrder     the directive came from the commander in the last 30 s
 * @param slotDistance    blocks from its post/formation slot (-1 = no post)
 * @param targetBeyondLeash the current target is farther from the post than the leash
 * @param coverDistance   blocks to the squad's best cover/defensible spot (-1 = none known)
 * @param priorityThreat  a flyer / ranged / explosive / boss enemy is known to the squad
 * @param targetIsPriority the current target already is one
 * @param enemiesNear     enemies the squad knows within 16 blocks of the guard's unit
 * @param alliesNear      the squad's living members
 * @param stuck           no progress for 4 s while it had somewhere to go
 */
public record Situation(int rank, boolean tank, boolean steady, boolean scout, boolean timid, boolean reckless,
                        double health, boolean hasTarget, double targetDistance, boolean ranged,
                        double minRange, double optimalRange, double maxRange, boolean outOfAmmo,
                        boolean holdFire, @Nullable SquadDirectives.Kind directive, boolean playerOrder,
                        double slotDistance, boolean targetBeyondLeash, double coverDistance,
                        boolean priorityThreat, boolean targetIsPriority, int enemiesNear, int alliesNear,
                        boolean enemyFleeing, boolean stuck, boolean inWater, Doctrine doctrine) {
}
