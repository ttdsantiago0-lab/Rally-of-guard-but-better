package net.hfstack.rallyguard.order;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Phase 3D: a plain, in-memory string-keyed dispatch table from order id to
 * {@link OrderExecutor} - deliberately NOT a {@code SavedData}/{@code Registry} like
 * {@code StanceRegistry}/{@code SquadRegistry} (see {@link OrderExecutor}'s javadoc for
 * why orders aren't persisted state). A plain static map is all "look up what RECALL
 * currently means and run it" needs.
 * <p>
 * A string id (matching the existing {@code Stance}/squad-name convention, e.g.
 * {@code AssignStanceC2SPayload}'s own javadoc) rather than an enum ordinal, for the same
 * reason: an unrecognized id (an older/newer client, or a future order added to one side
 * only) fails soft - {@link #get} returns {@code null} and the caller just skips it -
 * instead of desyncing an enum's ordinal positions across versions.
 * <p>
 * Every order that needs nothing beyond "which guards" to run is registered here:
 * {@code RECALL}, {@code HOLD} (Hold Ground), {@code REFORM}, and since Phase 3E
 * {@code BREAK_RANKS}, {@code HOLD_FIRE} and {@code RELEASE_FIRE}. {@code MUSTER},
 * {@code RETREAT} and (since Phase 3E, when it gained a target) {@code ATTACK} are
 * deliberately NOT registered here: each needs data (a destination / a target) the
 * bare string-id dispatch this registry does can't carry, so
 * {@code GuardCommandNetworking} constructs those three directly from their own payloads
 * instead (see {@code MusterOrder}'s javadoc) - still the exact same
 * {@link OrderExecutor} abstraction, just not looked up by name here. The rest of the
 * order vocabulary discussed for the future Battle Planner (Advance, Defend, Follow,
 * Escort, Guard Post, Resupply, Kamikaze) is deliberately NOT registered here yet -
 * adding a zero-parameter one later is exactly one more {@link #register} call plus one
 * new small {@link OrderExecutor} implementation, nothing about this registry's shape
 * assumes a fixed entry count. Kamikaze specifically is intentionally not among them:
 * per the project's explicit direction, the Order system itself needed to be proven
 * clean and working first, before Kamikaze becomes one of its consumers.
 */
public final class OrderRegistry {
    private OrderRegistry() {
    }

    private static final Map<String, OrderExecutor> ORDERS = new LinkedHashMap<>();

    static {
        register("RECALL", new RecallOrder());
        register("FOLLOW", new FollowOrder());
        register("ADVANCE", new AdvanceOrder());
        register("HOLD", new HoldOrder());
        register("REFORM", new ReformOrder());
        register("BREAK_RANKS", new BreakRanksOrder());
        register("HOLD_FIRE", new HoldFireOrder());
        register("RELEASE_FIRE", new ReleaseFireOrder());
        // E1-G fire modes
        for (net.hfstack.rallyguard.combat.FireMode mode : net.hfstack.rallyguard.combat.FireMode.values()) {
            register("FIRE_" + mode.name(), new FireModeOrder(mode));
        }
    }

    public static void register(String orderId, OrderExecutor executor) {
        ORDERS.put(orderId.toUpperCase(Locale.ROOT), executor);
    }

    /** Returns {@code null} for an unrecognized or blank id - callers must treat that as
     *  a silent no-op, same tolerance {@code GuardCommandNetworking#handleAction} already
     *  has for an unknown action code. */
    public static OrderExecutor get(String orderId) {
        if (orderId == null) {
            return null;
        }
        return ORDERS.get(orderId.toUpperCase(Locale.ROOT));
    }
}
