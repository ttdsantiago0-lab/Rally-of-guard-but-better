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
 * {@link StanceRegistry}: it only fights back against whatever hit the guard itself
 * (Phase 3C's original "no code needed" assumption was disproved in testing - see
 * {@code event.GuardStanceHandler}). All four values are enforced by
 * {@code event.GuardStanceHandler}, and since Phase 3E an explicit commander order
 * (Attack, a designated target) outranks every one of them:
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
 *     entity except players, the guard's own owner, the owner's other guards and pets.
 *     Correction (Phase 3E): it does NOT bypass the mob blacklist - Guard Villagers'
 *     own {@code Guard#setTarget} override silently refuses blacklisted mobs (read from
 *     its real source), so that would need a mixin; see {@code GuardStanceHandler}.</li>
 * </ul>
 * Kamikaze, the fifth behavior from the original design conversation, is deliberately
 * NOT in this enum and never will be added here (reclassified 2026-09-15, per the
 * user's own direction): it is no longer planned as an ordinary Stance value at all, but
 * as a special Order - a one-shot, explicitly player-issued command (see
 * {@code net.hfstack.rallyguard.order}'s package javadoc and PORTING_PLAN.md's Phase 3D
 * section for the reasoning), not a persistent per-guard posture like the four values
 * below. A guard given a future Kamikaze order stays whatever Stance it already had;
 * Kamikaze is a temporary override of behavior, not a fifth choice alongside
 * Passive/Defensive/Aggressive/Ruthless. Nothing about this enum's four values or its
 * wire format (a String name, see {@code AssignStanceC2SPayload}) needs to change to
 * support that.
 */
public enum Stance {
    PASSIVE,
    DEFENSIVE,
    AGGRESSIVE,
    RUTHLESS
}
