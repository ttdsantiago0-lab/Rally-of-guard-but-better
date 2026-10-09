package net.hfstack.rallyguard.tactics;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.tactics.DecisionLadder.Decision;
import net.hfstack.rallyguard.tactics.DecisionLadder.Rung;
import net.hfstack.rallyguard.veterancy.Trait;

/**
 * E3A V5 - the advanced decision core, RANK 5 ONLY ({@code docs/ARQUITECTURA_VETERANIA.md}
 * §3), native to Rally, pure. Ten steps in order:
 * <ol>
 *     <li>mission / order in force · 2. own survival · 3. obligations (ward, formation slot) ·
 *     4. threat assessment (odds) · 5. target priority (priority threats, focus fire) ·
 *     6. weapon suitability for that threat · 7. terrain (choose a position BEFORE attacking:
 *     high ground or cover) · 8. formation · 9. action (fire-and-move in pairs, reposition
 *     after shooting, counter, charge) · 10. resume the mission when nothing is left.</li>
 * </ol>
 * It runs only when an enemy is known and within the Perf budget
 * ({@code veterancy.advancedBudgetMs}); otherwise {@link Tactics} falls back to the normal
 * ladder ({@link DecisionReason#DEGRADED_BUDGET}). Trait capstones: the preference is applied
 * with better execution (tighter tolerances, earlier triggers) - never more damage.
 */
final class AdvancedCore {
    private AdvancedCore() {
    }

    static Decision decide(Situation s) {
        // 1. mission
        Decision mission = DecisionLadder.mission(s);
        if (mission != null) return mission;
        // 2. survival
        if (s.stuck()) return Decision.of(Rung.UNSTUCK, DecisionReason.STUCK);
        double retreatLine = DecisionLadder.retreatHealth(s) * (s.has(Trait.TANK) ? 0.8D : 1.0D);
        boolean low = s.health() < retreatLine || (s.retreating() && s.health() < retreatLine + DecisionLadder.REENTRY_MARGIN);
        if (!s.reckless() && low) {
            // E3B R1 (A4): cover is the retreat's destination, chosen once - never cover ↔ retreat.
            return !s.retreating() && s.coverDistance() > 1.5D && s.coverDistance() <= 8.0D
                    ? Decision.of(Rung.TAKE_COVER, DecisionReason.LOW_HEALTH_RETREAT)
                    : Decision.of(Rung.RETREAT, DecisionReason.LOW_HEALTH_RETREAT);
        }
        // 10 (early). nothing to fight: resume the mission
        if (!s.hasTarget() || s.outOfAmmo()) {
            if (s.outOfAmmo() && s.canSwapMelee()) return Decision.of(Rung.SWAP_WEAPON, DecisionReason.AMMO_LOW);
            if (s.enemiesNear() > 0 && !s.outOfAmmo()) return Decision.of(Rung.SEEK_TARGET, DecisionReason.SEEK_ENEMY);
            return s.slotDistance() > 1.5D ? Decision.of(Rung.RETURN_TO_SLOT, DecisionReason.RESUME_MISSION)
                    : Decision.of(Rung.HOLD_FORMATION, DecisionReason.RESUME_MISSION);
        }
        // 3. obligations: the formation slot it was given
        SquadDirectives.Kind d = s.directive();
        boolean holding = d == SquadDirectives.Kind.HOLD || d == SquadDirectives.Kind.MUSTER;
        double tolerance = (s.has(Trait.STEADY) || (s.has(Trait.RIDER) && s.mounted()) ? 3.0D : 5.0D) * s.leaderTolerance()
                * (1.0D - 0.3D * s.mountBond()); // E3B V1
        if (holding && s.slotDistance() > tolerance && s.targetBeyondLeash()) {
            return Decision.of(Rung.RETURN_TO_SLOT, DecisionReason.FORMATION_HOLD);
        }
        // 4. threat assessment: hopeless odds -> orderly retreat (nobody is immortal)
        // E3B R1 (A3): real allies near (own guards, not only the shared directive), enemies that matter,
        // and never under ALL_OUT.
        if (!s.reckless() && s.doctrine() != Doctrine.ALL_OUT && s.doctrine().retreatHealth > 0
                && s.threatsNear() >= 3 * Math.max(1, s.alliesNear())) {
            return Decision.of(Rung.RETREAT, DecisionReason.OUTNUMBERED);
        }
        // 4b. threat memory (E3B V2): what nearly killed it weighs more - step back, never trade blows with it
        if (DecisionLadder.dreadStepBack(s)) return Decision.of(Rung.REPOSITION_AWAY, DecisionReason.THREAT_MEMORY);
        // 5. target priority
        if (s.priorityThreat() && !s.targetIsPriority() && (s.ranged() || !s.priorityAir()) && s.retargetReady()) {
            return Decision.of(Rung.SWITCH_PRIORITY, s.priorityAir() ? DecisionReason.TARGET_PRIORITY_AIR : DecisionReason.TARGET_PRIORITY);
        }
        if (s.ranged() && s.focusAvailable() && s.retargetReady() && !s.targetIsPriority() && !s.priorityThreat()) {
            return Decision.of(Rung.FOCUS_FIRE, DecisionReason.FOCUS_FIRE);
        }
        // 6. weapon suitability
        if (s.ranged() && s.targetDistance() < s.minRange()) {
            return s.canSwapMelee() && !s.has(Trait.MARKSMAN)
                    ? Decision.of(Rung.SWAP_WEAPON, DecisionReason.WEAPON_MISMATCH)
                    : new Decision(Rung.REPOSITION_AWAY, s.inGroup() ? null : FireMode.RETREATING, DecisionReason.TOO_CLOSE);
        }
        if (!s.ranged() && s.targetIsPriority() && s.priorityAir()) {
            if (s.canSwapRanged()) return Decision.of(Rung.SWAP_WEAPON, DecisionReason.WEAPON_MISMATCH);
            if (s.coverDistance() > 1.5D && s.coverDistance() <= 8.0D) return Decision.of(Rung.TAKE_COVER, DecisionReason.WEAPON_MISMATCH);
        }
        // 7. terrain: take a position before attacking (ranged), high ground first for a Marksman
        if (s.ranged() && s.doctrine().terrainWeight > 0 && s.coverDistance() > 1.5D && s.coverDistance() <= 8.0D
                && (s.highGround() || !s.has(Trait.MARKSMAN))) {
            return new Decision(Rung.TAKE_POSITION, s.inGroup() ? null : FireMode.STILL,
                    s.highGround() ? DecisionReason.HIGH_GROUND : DecisionReason.COVER_AVAILABLE);
        }
        if (s.ranged()) {
            double want = DecisionLadder.preferredDistance(s);
            if (s.targetDistance() > s.maxRange() || s.targetDistance() > want * 1.4D) {
                return new Decision(Rung.REPOSITION_CLOSER, s.inGroup() ? null : FireMode.ADVANCING, DecisionReason.OUT_OF_RANGE);
            }
        }
        // 8. formation (inside a group: stay close to the slot while fighting)
        if (s.inGroup() && holding && s.slotDistance() > tolerance + 3.0D) {
            return Decision.of(Rung.RETURN_TO_SLOT, DecisionReason.FORMATION_HOLD);
        }
        // 9. action
        if (s.ranged()) {
            // Fire and move by pairs: half the pair moves while the other half fires, every 2 s.
            boolean myHalfMoves = ((s.turn() / 2) % 2 == 0) == s.pairEven();
            if (s.alliesNear() >= 2 && myHalfMoves && s.turn() % 2 == 0) return Decision.of(Rung.PAIR_BOUND, DecisionReason.PAIR_BOUND);
            if (!myHalfMoves && s.turn() % 5 == 0) return Decision.of(Rung.SCOOT, DecisionReason.SHOOT_AND_MOVE);
            return new Decision(Rung.FIRE, s.inGroup() ? null : DecisionLadder.styleFor(s), DecisionReason.ENGAGE);
        }
        if (s.attackerNotTarget() && (s.has(Trait.DUELIST) || s.health() < 0.6D)) {
            return Decision.of(Rung.COUNTER, DecisionReason.COUNTERATTACK);
        }
        boolean opener = s.has(Trait.ASSAULT) || (s.has(Trait.RIDER) && s.mounted());
        return Decision.of(Rung.ADVANCE, opener ? DecisionReason.OPEN_ENCOUNTER : DecisionReason.ENGAGE);
    }
}
