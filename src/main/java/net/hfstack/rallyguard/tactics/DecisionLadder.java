package net.hfstack.rallyguard.tactics;

import net.hfstack.rallyguard.combat.FireMode;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.veterancy.Trait;

import javax.annotation.Nullable;

/**
 * E2A block 4 / E3A V3-V5-V10 - the decision ladder as one PURE function: same
 * {@link Situation} and rank in, same {@link Decision} out, no world access.
 * <p>
 * Rank table ({@code docs/ARQUITECTURA_VETERANIA.md} §2; the table says what each rank ADDS,
 * lower ranks stay competent):
 * <pre>
 *  0 Recruit     obeys, attacks its target, keeps formation loosely, basic defence; retreats
 *                below the doctrine line (kept from E2A - the table puts it at rank 1: noted)
 *  1 Trained     keeps its weapon's band; Steady/Tank/mounted Rider return to their slot
 *  2 Experienced holds formation under pressure (slot), simple cover, own fire style when alone
 *                (TerrainSense for captains ≥ 2 - kept; the table says rank 3: noted)
 *  3 Veteran     target priority, concentrates fire with neighbours (focus fire)
 *  4 Elite       picks the right weapon for the threat, repositions after shooting
 *  5 Legend      the advanced decision core ({@link AdvancedCore})
 * </pre>
 * Traits are PREFERENCES (V3): Marksman - high ground, longer band, flyers first (from rank 1);
 * Duelist - counter-attacks whoever hits it; Rider - holds formation and charges when mounted;
 * Assault - opens the encounter; Tank - holds the line, retreats later; Steady - never leaves
 * its unit; Scout - spots priority threats earlier. Rank 5 executes them better (core).
 * Inside a group a guard's fire style follows the group's ordered style; alone, a guard of
 * rank ≥ 2 picks its own (A3).
 */
public final class DecisionLadder {
    private DecisionLadder() {
    }

    /** Ordinals are what the simulation counts per rung - append only (≤ SimProbe.RUNGS). */
    public enum Rung {
        UNSTUCK, OBEY_DIRECTIVE, SEEK_TARGET, HOLD_FORMATION, RETURN_TO_SLOT, TAKE_COVER,
        REPOSITION_CLOSER, REPOSITION_AWAY, SWITCH_PRIORITY, RETREAT, FIRE, ADVANCE, NONE,
        FOCUS_FIRE, COUNTER, SWAP_WEAPON, SCOOT, TAKE_POSITION, PAIR_BOUND
    }

    /** @param style a fire style the guard picked itself, or null to keep the current one */
    public record Decision(Rung rung, @Nullable FireMode style, DecisionReason reason) {
        static Decision of(Rung rung, DecisionReason reason) {
            return new Decision(rung, null, reason);
        }
    }

    /** E3B R1 (A5): a retreating guard fights again only above its retreat line plus this. */
    static final double REENTRY_MARGIN = 0.20D;

    /** Below this share of health even an obeying guard saves itself (rung 1 exception). */
    static final double DANGER_HEALTH = 0.20D;

    public static Decision decide(Situation s, int rank) {
        if (rank >= 5 && s.advancedAllowed()) return AdvancedCore.decide(s);
        return ladder(s, rank);
    }

    static Decision ladder(Situation s, int rank) {
        // 0 - escape when stuck (cheap, any rank).
        if (s.stuck()) return Decision.of(Rung.UNSTUCK, DecisionReason.STUCK);

        // 1 - a directive in force is obeyed, except in immediate low-health danger.
        SquadDirectives.Kind d = s.directive();
        Decision mission = mission(s);
        if (mission != null) return mission;

        // 2 - no target (or nothing to shoot with).
        if (!s.hasTarget() || s.outOfAmmo()) {
            if (s.outOfAmmo() && rank >= 1 && s.canSwapMelee()) return Decision.of(Rung.SWAP_WEAPON, DecisionReason.AMMO_LOW);
            if (s.enemiesNear() > 0 && !s.outOfAmmo()) {
                return rank >= 1 && s.has(Trait.ASSAULT)
                        ? Decision.of(Rung.ADVANCE, DecisionReason.OPEN_ENCOUNTER)
                        : Decision.of(Rung.SEEK_TARGET, DecisionReason.SEEK_ENEMY);
            }
            return s.slotDistance() >= 0 ? Decision.of(Rung.HOLD_FORMATION, DecisionReason.FORMATION_HOLD)
                    : Decision.of(Rung.NONE, DecisionReason.NONE);
        }

        // Duelist preference: answer whoever hits it in melee.
        if (!s.ranged() && rank >= 1 && s.has(Trait.DUELIST) && s.attackerNotTarget()) {
            return Decision.of(Rung.COUNTER, DecisionReason.COUNTERATTACK);
        }

        // 3 - out of my slot for no reason (my target is beyond the leash): go back.
        boolean slotKeeper = rank >= 2 || (rank >= 1 && (s.has(Trait.STEADY) || s.has(Trait.TANK)
                || (s.has(Trait.RIDER) && s.mounted())));
        double slotTolerance = 6.0D * (s.has(Trait.RIDER) && s.mounted() ? 0.6D : 1.0D) * (s.has(Trait.TANK) ? 0.8D : 1.0D)
                * s.leaderTolerance() // E3A A1: a better leader keeps the formation tighter
                * (1.0D - 0.3D * s.mountBond()); // E3B V1: a bonded rider strays less
        if (slotKeeper && s.slotDistance() > slotTolerance && s.targetBeyondLeash()
                && (d == SquadDirectives.Kind.HOLD || d == SquadDirectives.Kind.MUSTER)) {
            return Decision.of(Rung.RETURN_TO_SLOT, DecisionReason.FORMATION_HOLD);
        }

        // Rank 4: the right weapon for the threat.
        if (rank >= 4) {
            if (s.ranged() && s.targetDistance() < s.minRange() && s.canSwapMelee()) {
                return Decision.of(Rung.SWAP_WEAPON, DecisionReason.WEAPON_MISMATCH);
            }
            if (!s.ranged() && s.targetIsPriority() && s.priorityAir() && s.canSwapRanged()) {
                return Decision.of(Rung.SWAP_WEAPON, DecisionReason.WEAPON_MISMATCH);
            }
        }

        // 4 - cover / high ground: ranged guards, anyone under half health, a Marksman for height.
        boolean wantsCover = s.ranged() || s.health() < 0.5D || (s.has(Trait.MARKSMAN) && s.highGround());
        // E3B R1 (A4): never take cover while a retreat is in force (cover ↔ retreat used to alternate).
        if (rank >= 2 && !s.retreating() && s.doctrine().terrainWeight > 0 && s.coverDistance() > 1.5D && s.coverDistance() <= 6.0D && wantsCover) {
            return new Decision(Rung.TAKE_COVER, s.ranged() && !s.inGroup() ? FireMode.STILL : null,
                    s.highGround() ? DecisionReason.HIGH_GROUND : DecisionReason.COVER_AVAILABLE);
        }

        // 5 - the weapon's band (doctrine and Marksman shift the preferred distance).
        if (rank >= 1 && s.ranged()) {
            double want = preferredDistance(s);
            if (s.targetDistance() < s.minRange()) {
                return new Decision(Rung.REPOSITION_AWAY, rank >= 2 && !s.inGroup() ? FireMode.RETREATING : null,
                        DecisionReason.TOO_CLOSE);
            }
            if (s.targetDistance() > s.maxRange() || (rank >= 2 && s.targetDistance() > want * 1.6D)) {
                return new Decision(Rung.REPOSITION_CLOSER, rank >= 2 && !s.inGroup() ? FireMode.ADVANCING : null,
                        DecisionReason.OUT_OF_RANGE);
            }
        }

        // E3B V2 - threat memory (ranks 3-4): a melee guard does not trade blows with what nearly killed it.
        if (rank >= 3 && dreadStepBack(s)) return Decision.of(Rung.REPOSITION_AWAY, DecisionReason.THREAT_MEMORY);

        // 6 - priority threats first (respecting the engagement cap, applied by the gate).
        boolean spotsPriority = rank >= 3 || (rank >= 2 && s.has(Trait.SCOUT))
                || (rank >= 1 && s.has(Trait.MARKSMAN) && s.priorityAir());
        // E3B R1 (A2): priority and focus never undo each other, and a target changes at most every 3 s.
        if (spotsPriority && s.priorityThreat() && !s.targetIsPriority() && s.retargetReady()) {
            return Decision.of(Rung.SWITCH_PRIORITY, s.priorityAir() ? DecisionReason.TARGET_PRIORITY_AIR
                    : DecisionReason.TARGET_PRIORITY);
        }
        // Rank 3: concentrate fire with the neighbours.
        if (rank >= 3 && s.ranged() && s.focusAvailable() && s.retargetReady() && !s.targetIsPriority()
                && !(spotsPriority && s.priorityThreat())) {
            return Decision.of(Rung.FOCUS_FIRE, DecisionReason.FOCUS_FIRE);
        }
        // Rank 4: reposition after shooting (every ~6 s, staggered by guard).
        if (rank >= 4 && s.ranged() && (s.turn() + (s.pairEven() ? 0 : 3)) % 6 == 0) {
            return Decision.of(Rung.SCOOT, DecisionReason.SHOOT_AND_MOVE);
        }

        // 7 - final: retreat / fire / advance.
        // E3B R1 (A5): once retreating, it only fights again above the line + 0.2 (no 5 s in-and-out).
        if (!s.reckless() && (s.health() < retreatHealth(s) || (s.retreating() && s.health() < retreatHealth(s) + REENTRY_MARGIN))) {
            return Decision.of(Rung.RETREAT, DecisionReason.LOW_HEALTH_RETREAT);
        }
        if (s.ranged()) return new Decision(Rung.FIRE, rank >= 2 && !s.inGroup() ? styleFor(s) : null, DecisionReason.ENGAGE);
        if (s.doctrine().avoidEngaging && s.threatsNear() > s.alliesNear() + 1 && !s.reckless()) {
            return Decision.of(Rung.RETREAT, DecisionReason.OUTNUMBERED);
        }
        // E3B M: a shaken guard in a group keeps its place instead of charging.
        if (s.morale() == 1 && s.inGroup() && s.targetDistance() > 4.0D && s.roll() < s.moraleStrength()) {
            return Decision.of(Rung.HOLD_FORMATION, DecisionReason.MORALE_SHAKEN);
        }
        boolean opener = s.has(Trait.ASSAULT) || (s.has(Trait.RIDER) && s.mounted());
        return Decision.of(Rung.ADVANCE, opener ? DecisionReason.OPEN_ENCOUNTER : DecisionReason.ENGAGE);
    }

    /** Rung 1 (shared with the advanced core): orders, routs, immediate danger, guarding, hold fire. */
    @Nullable
    static Decision mission(Situation s) {
        SquadDirectives.Kind d = s.directive();
        if (d == SquadDirectives.Kind.RETREAT) return Decision.of(Rung.RETREAT, DecisionReason.ORDER_OVERRIDE);
        if (d == SquadDirectives.Kind.ROUTED) {
            return s.has(Trait.STEADY) ? Decision.of(Rung.HOLD_FORMATION, DecisionReason.UNIT_BROKEN)
                    : Decision.of(Rung.RETREAT, DecisionReason.UNIT_BROKEN);
        }
        Decision morale = morale(s);
        if (morale != null) return morale;
        if (!s.reckless() && s.health() < danger(s) && s.hasTarget()) return Decision.of(Rung.RETREAT, DecisionReason.LOW_HEALTH_RETREAT);
        if (d == SquadDirectives.Kind.DEFEND) return Decision.of(Rung.OBEY_DIRECTIVE, DecisionReason.PROTECT_ALLY);
        if (d == SquadDirectives.Kind.FOLLOW) {
            return Decision.of(Rung.OBEY_DIRECTIVE, s.playerOrder() ? DecisionReason.ORDER_OVERRIDE : DecisionReason.LEADER_DIRECTIVE);
        }
        if (s.holdFire()) {
            return s.slotDistance() > 6 ? Decision.of(Rung.RETURN_TO_SLOT, DecisionReason.ORDER_OVERRIDE)
                    : Decision.of(Rung.OBEY_DIRECTIVE, DecisionReason.ORDER_OVERRIDE);
        }
        return null;
    }

    /**
     * E3B M ({@code docs/MORAL_Y_COHESION.md} §3) - morale as preferences, scaled by {@code morale.strength}:
     * broken = run (unless cornered: then it answers whoever hits it); wavering = breaks on contact unless
     * its captain is near, starts no attack, and a COHESIVE wavering unit refuses to attack but holds.
     */
    @Nullable
    static Decision morale(Situation s) {
        if (s.moraleStrength() <= 0 || s.reckless()) return null;
        net.hfstack.rallyguard.morale.MoraleState m = net.hfstack.rallyguard.morale.MoraleState.values()[s.morale()];
        if (m.broken()) {
            boolean cornered = s.attackerNotTarget() && s.roll() >= s.moraleStrength();
            return cornered ? Decision.of(Rung.COUNTER, DecisionReason.COUNTERATTACK) : Decision.of(Rung.RETREAT, DecisionReason.ROUTED_FLEE);
        }
        if (m == net.hfstack.rallyguard.morale.MoraleState.VACILANTE) {
            if (s.cohesive()) {
                if (s.hasTarget() && !s.attackerNotTarget()) return Decision.of(Rung.HOLD_FORMATION, DecisionReason.REFUSE_ATTACK);
                return null;
            }
            boolean contact = s.hasTarget() && s.targetDistance() <= 3.0D;
            if (contact && !s.leaderNear() && s.roll() < 0.6D * s.moraleStrength()) {
                return Decision.of(Rung.RETREAT, DecisionReason.MORALE_WAVERING);
            }
            if (!s.hasTarget() && s.enemiesNear() > 0) return Decision.of(Rung.HOLD_FORMATION, DecisionReason.MORALE_WAVERING);
        }
        return null;
    }

    /** E3B V2: a melee guard hurt, close to a target it dreads (≥ 0.5), steps back instead of trading blows. */
    static boolean dreadStepBack(Situation s) {
        return !s.ranged() && !s.reckless() && s.hasTarget() && s.dread() >= 0.5D && s.targetDistance() < 2.5D
                && s.health() < 0.7D;
    }

    static double preferredDistance(Situation s) {
        double want = s.doctrine().preferRange == 2 ? Math.max(s.optimalRange(), s.maxRange() * 0.75D)
                : s.doctrine().preferRange == 0 ? Math.max(s.minRange() + 1, s.optimalRange() * 0.6D)
                : s.optimalRange();
        if (s.morale() == 1) want *= 1.0D + 0.15D * s.moraleStrength(); // E3B M: shaken shooters keep more distance
        if (s.rank() >= 3 && s.dread() > 0) want = Math.min(s.maxRange(), want * (1.0D + 0.3D * s.dread())); // E3B V2
        return s.has(Trait.MARKSMAN) ? Math.min(s.maxRange(), want * 1.2D) : want;
    }

    static double danger(Situation s) {
        return s.has(Trait.TANK) ? DANGER_HEALTH * 0.6D : s.timid() ? DANGER_HEALTH * 1.5D : DANGER_HEALTH;
    }

    /** The doctrine's retreat line, moved by traits (Tank later, Assault later, timid sooner). */
    static double retreatHealth(Situation s) {
        double h = s.doctrine().retreatHealth;
        if (s.has(Trait.TANK)) h *= 0.6D;
        if (s.has(Trait.ASSAULT)) h *= 0.8D;
        if (s.timid()) h = Math.min(0.9D, h * 1.3D);
        // E3B M: shaken retreats a little sooner, wavering sooner still (× strength).
        if (s.morale() == 1) h = Math.min(0.9D, h + 0.10D * s.moraleStrength());
        else if (s.morale() == 2) h = Math.min(0.9D, h + 0.20D * s.moraleStrength());
        h *= 1.0D - s.composure(); // E3B V6: the axe and the shield hold on longer (≤ 50 %)
        if (s.rank() >= 3 && s.dread() > 0) h = Math.min(0.9D, h + 0.15D * s.dread()); // E3B V2: leaves sooner what it fears
        return h;
    }

    /** A guard's own style when alone: kite when pressed, volley at range, stand still otherwise. */
    static FireMode styleFor(Situation s) {
        if (s.targetDistance() < s.optimalRange() * 0.6D) return FireMode.RETREATING;
        if (s.alliesNear() >= 4 && s.targetDistance() > s.optimalRange()) return FireMode.VOLLEY;
        if (s.enemyFleeing() || s.has(Trait.ASSAULT)) return FireMode.ADVANCING;
        return FireMode.STILL;
    }
}
