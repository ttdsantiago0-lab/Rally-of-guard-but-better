package net.hfstack.rallyguard.contract;

/**
 * Phase 3C: a persistent behavioral posture for an owned guard, set per-guard from the
 * Commander's Ledger (bulk-applicable to every currently-selected guard, same pattern as
 * Phase 3A's squad assignment). Resolves the open design question from the original
 * Phase 3 design conversation ("does Stance need to be per-guard as well as per-squad?")
 * as per-guard only - a "set my whole squad to X" action is just the existing multi-select
 * applied to this too, no separate per-squad storage needed.
 * <p>
 * {@link #DEFENSIVE} is the implicit default for any guard with no explicit entry in
 * {@link StanceRegistry} - it requires zero new behavior, since it's simply Guard
 * Villagers' own out-of-the-box AI (retaliate if hit, join the owner's fight, defend the
 * village) with nothing added or removed. The other three values are enforced by
 * {@code event.GuardStanceHandler}:
 * <ul>
 *     <li>{@link #PASSIVE} - never acquires a target at all, by canceling every
 *     {@code LivingChangeTargetEvent} for the guard (blocks retaliation, joining the
 *     owner's fight, village defense, and anger-at-a-player alike - "won't fight back"
 *     means all of it, not just melee).</li>
 *     <li>{@link #AGGRESSIVE} - proactively engages nearby vanilla {@code Monster}s
 *     (still respecting Guard Villagers' own {@code MobBlackList}, e.g. Creepers) even
 *     without being attacked first, via a periodic direct {@code setTarget(...)} scan
 *     (see the handler's own javadoc for why this doesn't need a mixin).</li>
 *     <li>{@link #RUTHLESS} ("modo asesinos") - proactively engages ANY nearby living
 *     entity except the guard's own owner and the owner's other guards, deliberately
 *     bypassing the mob blacklist - confirmed feasible by reading Guard Villagers' real
 *     source: the blacklist check ({@code Guard#canAttack}) is only consulted by
 *     target-acquisition goals, never by the attack-execution goals, so a directly-set
 *     target skips it entirely. Not yet in-game tested for whether the guard actually
 *     lands hits on a normally-blacklisted mob, not just walks at it - see PORTING_PLAN.md.</li>
 * </ul>
 * Kamikaze, the fifth stance from the original design conversation, is deliberately NOT
 * part of this first Phase 3C pass - it needs the specific Lucky TNT throwable item
 * identified first (see PORTING_PLAN.md's Phase 3C section), so it isn't in this enum
 * yet. Adding it later just means adding a value here and a new branch in the handler -
 * nothing about this first pass's wire format assumes exactly four values.
 */
public enum Stance {
    PASSIVE,
    DEFENSIVE,
    AGGRESSIVE,
    RUTHLESS
}
