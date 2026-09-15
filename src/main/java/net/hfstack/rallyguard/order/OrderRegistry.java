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
 * Only {@code RECALL} and {@code HOLD} are registered in this first pass. The much
 * longer order vocabulary discussed for the future Battle Planner (Muster, Attack,
 * Advance, Defend, Follow, Retreat, Reform, Escort, Guard Post, Resupply, Kamikaze) is
 * deliberately NOT registered here yet - adding one later is exactly one more
 * {@link #register} call plus (usually) one new small {@link OrderExecutor}
 * implementation, nothing about this registry's shape assumes exactly two entries.
 * Kamikaze specifically is intentionally not among them: per the project's explicit
 * direction, the Order system itself needs to be proven clean and working first, before
 * Kamikaze becomes one of its consumers.
 */
public final class OrderRegistry {
    private OrderRegistry() {
    }

    private static final Map<String, OrderExecutor> ORDERS = new LinkedHashMap<>();

    static {
        register("RECALL", new RecallOrder());
        register("HOLD", new HoldOrder());
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
