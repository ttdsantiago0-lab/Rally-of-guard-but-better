package net.hfstack.rallyguard.tactics;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.command.SquadDirectives;

import javax.annotation.Nullable;

/**
 * E2A block 4 - the playbook's decision ladder ({@code docs/DECISIONES_TACTICAS.md} §2) as
 * one PURE function: same {@link Situation} and rank in, same {@link Decision} out, no world
 * access - easy to test, cheap to run in the stance loop's once-a-second turn.
 * <p>
 * Rungs are tried top-down; the first that applies decides. Each rank unlocks rungs:
 * <pre>
 *  0  stuck?                                  -> UNSTUCK                       (all)
 *  1  directive in force?                     -> OBEY / RETREAT (low-health danger)   (0)
 *  2  no target?                              -> SEEK / HOLD_FORMATION           (0)
 *  3  out of my slot without reason?          -> RETURN_TO_SLOT                  (2; Steady 1)
 *  4  not in cover, good cover ≤ 6 blocks?    -> TAKE_COVER (ranged or health &lt; 50 %) (2)
 *  5  outside my weapon's band?               -> REPOSITION_CLOSER / _AWAY       (1)
 *  6  priority threat (flyer, ranged, explosive, boss)? -> SWITCH_PRIORITY      (3; Scout 2)
 *  7  final                                   -> RETREAT (health &lt; doctrine) / FIRE / ADVANCE (0)
 * </pre>
 * Veterans (rank ≥ 2) with no fire style ordered by the player also choose their own
 * {@link FireMode} from the weapon and situation ({@link Decision#style}); with a player's
 * order they only use that one (the executor never overrides an ordered style).
 */
public final class DecisionLadder {
    private DecisionLadder() {
    }

    /** Ordinals are what the simulation counts per rung - keep the order (≤ 16). */
    public enum Rung {
        UNSTUCK, OBEY_DIRECTIVE, SEEK_TARGET, HOLD_FORMATION, RETURN_TO_SLOT, TAKE_COVER,
        REPOSITION_CLOSER, REPOSITION_AWAY, SWITCH_PRIORITY, RETREAT, FIRE, ADVANCE, NONE
    }

    /** @param style a fire style the guard picked itself, or null to keep the current one */
    public record Decision(Rung rung, @Nullable FireMode style) {
        static Decision of(Rung rung) {
            return new Decision(rung, null);
        }
    }

    /** Below this share of health even an obeying guard saves itself (rung 1 exception). */
    static final double DANGER_HEALTH = 0.20D;

    public static Decision decide(Situation s, int rank) {
        // 0 - escape when stuck (cheap, any rank).
        if (s.stuck()) return Decision.of(Rung.UNSTUCK);

        // 1 - a directive in force is obeyed, except in immediate low-health danger.
        SquadDirectives.Kind d = s.directive();
        if (d == SquadDirectives.Kind.RETREAT || d == SquadDirectives.Kind.ROUTED) return Decision.of(Rung.RETREAT);
        if (!s.reckless() && s.health() < danger(s) && s.hasTarget()) return Decision.of(Rung.RETREAT);
        if (d == SquadDirectives.Kind.FOLLOW || d == SquadDirectives.Kind.DEFEND) return Decision.of(Rung.OBEY_DIRECTIVE);
        if (s.holdFire()) return Decision.of(s.slotDistance() > 6 ? Rung.RETURN_TO_SLOT : Rung.OBEY_DIRECTIVE);

        // 2 - no target: look for one (the stance loop does), else keep formation.
        if (!s.hasTarget() || s.outOfAmmo()) {
            if (s.enemiesNear() > 0 && !s.outOfAmmo()) return Decision.of(Rung.SEEK_TARGET);
            return Decision.of(s.slotDistance() >= 0 ? Rung.HOLD_FORMATION : Rung.NONE);
        }

        // 3 - out of my slot for no reason (my target is beyond the leash): go back.
        if ((rank >= 2 || (rank >= 1 && s.steady())) && s.slotDistance() > 6 && s.targetBeyondLeash()
                && (d == SquadDirectives.Kind.HOLD || d == SquadDirectives.Kind.MUSTER)) {
            return Decision.of(Rung.RETURN_TO_SLOT);
        }

        // 4 - cover: ranged guards, or anyone under half health, take good cover nearby.
        if (rank >= 2 && s.doctrine().terrainWeight > 0 && s.coverDistance() > 1.5D && s.coverDistance() <= 6.0D
                && (s.ranged() || s.health() < 0.5D)) {
            return new Decision(Rung.TAKE_COVER, rank >= 2 && s.ranged() ? FireMode.STILL : null);
        }

        // 5 - the weapon's band (doctrine shifts the preferred distance).
        if (rank >= 1 && s.ranged()) {
            double want = s.doctrine().preferRange == 2 ? Math.max(s.optimalRange(), s.maxRange() * 0.75D)
                    : s.doctrine().preferRange == 0 ? Math.max(s.minRange() + 1, s.optimalRange() * 0.6D)
                    : s.optimalRange();
            if (s.targetDistance() < s.minRange()) {
                return new Decision(Rung.REPOSITION_AWAY, rank >= 2 ? FireMode.RETREATING : null);
            }
            if (s.targetDistance() > s.maxRange() || (rank >= 2 && s.targetDistance() > want * 1.6D)) {
                return new Decision(Rung.REPOSITION_CLOSER, rank >= 2 ? FireMode.ADVANCING : null);
            }
        }

        // 6 - priority threats first (respecting the engagement cap, applied by the gate).
        if ((rank >= 3 || (rank >= 2 && s.scout())) && s.priorityThreat() && !s.targetIsPriority()) {
            return Decision.of(Rung.SWITCH_PRIORITY);
        }

        // 7 - final: retreat / fire / advance.
        if (!s.reckless() && s.health() < retreatHealth(s)) return Decision.of(Rung.RETREAT);
        if (s.ranged()) return new Decision(Rung.FIRE, rank >= 2 ? styleFor(s) : null);
        if (s.doctrine().avoidEngaging && s.enemiesNear() > s.alliesNear() + 1 && !s.reckless()) {
            return Decision.of(Rung.RETREAT);
        }
        return Decision.of(Rung.ADVANCE);
    }

    static double danger(Situation s) {
        return s.tank() ? DANGER_HEALTH * 0.6D : s.timid() ? DANGER_HEALTH * 1.5D : DANGER_HEALTH;
    }

    /** The doctrine's retreat line, moved by traits (Tank later, timid sooner). */
    static double retreatHealth(Situation s) {
        double h = s.doctrine().retreatHealth;
        if (s.tank()) h *= 0.6D;
        if (s.timid()) h = Math.min(0.9D, h * 1.3D);
        return h;
    }

    /** A veteran's own style: kite when pressed, volley at range in a group, stand still otherwise. */
    static FireMode styleFor(Situation s) {
        if (s.targetDistance() < s.optimalRange() * 0.6D) return FireMode.RETREATING;
        if (s.alliesNear() >= 4 && s.targetDistance() > s.optimalRange()) return FireMode.VOLLEY;
        if (s.enemyFleeing()) return FireMode.ADVANCING;
        return FireMode.STILL;
    }
}
