package net.hfstack.rallyguard.tactics;

import net.hfstack.rallyguard.command.SquadDirectives;

import javax.annotation.Nullable;

/**
 * E2A block 4 / E3A V - everything {@link DecisionLadder} looks at, built once a second per
 * guard from CHEAP signals only (playbook §0.3): health, distances, the squad's shared
 * picture, ammo, enemy type, the squad's cached terrain reading, the guard's traits
 * (preferences, never numbers). No block or entity scans happen here - see
 * {@link Tactics#step}.
 *
 * @param traits          bit set of {@link net.hfstack.rallyguard.veterancy.Trait} ordinals
 * @param directive       the unit's pushed directive kind (null = none: free judgement)
 * @param playerOrder     the directive came from the commander in the last 30 s
 * @param inGroup         the guard shares its directive with others (A3: a group's style rules)
 * @param slotDistance    blocks from its post/formation slot (-1 = no post)
 * @param targetBeyondLeash the current target is farther from the post than the leash
 * @param coverDistance   blocks to the squad's best cover/defensible spot (-1 = none known)
 * @param highGround      that spot is also high ground
 * @param priorityThreat  a flyer / ranged / explosive / boss enemy is known to the squad
 * @param priorityAir     that threat flies
 * @param focusAvailable  squadmates concentrate on another enemy within reach (focus fire)
 * @param attackerNotTarget it was just hit in melee range by something it isn't fighting
 * @param canSwapMelee    a melee weapon waits in its pack
 * @param canSwapRanged   a ranged weapon with ammo waits in its pack
 * @param turn            game seconds (decisions on a rhythm: fire and move, pairs)
 * @param pairEven        its half of a fire-and-move pair
 * @param advancedAllowed rank 5 may use the advanced core (enemy known, Perf budget left)
 * @param leaderTolerance formation tolerance multiplier from the unit leader's score (A1; 1 = neutral)
 * @param threatsNear     E3B R1 (A3): enemies that matter to this guard - hunting our side or within 12 blocks
 * @param retargetReady   E3B R1 (A2): at least 3 s since the ladder last changed this guard's target
 * @param retreating      E3B R1 (A4/A5): falling back now, or latched until health is back over the line + 0.2
 * @param morale          E3B M: {@link net.hfstack.rallyguard.morale.MoraleState} ordinal (0 = FIRME)
 * @param moraleStrength  E3B M: 0..1 how much morale changes decisions (0 = only measured)
 * @param leaderNear      E3B M: its captain is alive within 16 blocks
 * @param cohesive        E3B M: its unit's cohesion is ≥ 60 (a wavering cohesive unit refuses to attack, holds)
 * @param roll            E3B M: 0..1, fixed for this guard and second (probabilistic preferences, reproducible)
 */
public record Situation(int rank, int traits, boolean timid, boolean reckless, boolean mounted,
                        double health, boolean hasTarget, double targetDistance, boolean ranged,
                        double minRange, double optimalRange, double maxRange, boolean outOfAmmo,
                        boolean holdFire, @Nullable SquadDirectives.Kind directive, boolean playerOrder, boolean inGroup,
                        double slotDistance, boolean targetBeyondLeash, double coverDistance, boolean highGround,
                        boolean priorityThreat, boolean priorityAir, boolean targetIsPriority, boolean focusAvailable,
                        boolean attackerNotTarget, int enemiesNear, int alliesNear, boolean enemyFleeing,
                        boolean canSwapMelee, boolean canSwapRanged, boolean stuck, boolean inWater,
                        long turn, boolean pairEven, boolean advancedAllowed, double leaderTolerance, Doctrine doctrine,
                        int threatsNear, boolean retargetReady, boolean retreating,
                        int morale, double moraleStrength, boolean leaderNear, boolean cohesive, double roll) {

    public boolean has(net.hfstack.rallyguard.veterancy.Trait trait) {
        return (traits & (1 << trait.ordinal())) != 0;
    }
}
