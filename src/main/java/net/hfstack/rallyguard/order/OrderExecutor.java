package net.hfstack.rallyguard.order;

import net.minecraft.server.level.ServerPlayer;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3D: the smallest reusable abstraction behind "what should this guard/group do
 * right now?" - deliberately just one method. An order is a one-shot, stateless
 * dispatch action: given the player who issued it and the (already ownership-checked,
 * already resolved to live entities) guards it applies to, it does whatever that order
 * means. It does NOT persist anything itself - a guard that needs to remember it's
 * mid-order (e.g. a future Kamikaze run, or a future multi-step Escort) stores that on
 * itself or in its own registry, the same way {@code StanceRegistry} already does for
 * Stance; this interface only covers "apply the order once, right now." ({@code
 * MusterOrder} recording each guard's slot into {@code FormationRegistry} is that
 * pattern in action - the order call itself is still one-shot, it's the order's
 * RESULT that gets remembered, in its own registry, not in this interface.)
 * <p>
 * Kept separate from {@code contract} (which holds persistent {@code SavedData}
 * registries - {@link net.hfstack.rallyguard.contract.StanceRegistry},
 * {@link net.hfstack.rallyguard.contract.SquadRegistry},
 * {@link net.hfstack.rallyguard.contract.GuardRoster}) precisely because Orders are not
 * persisted state - issuing "Recall" doesn't need to survive a server restart, unlike a
 * guard's squad or stance.
 * <p>
 * Deliberately still has NO target parameter (entity/position/formation) on the
 * interface itself - {@link RecallOrder}/{@link HoldOrder}/{@code AttackOrder}/
 * {@code ReformOrder} don't need one. The orders that DO need extra data
 * ({@code MusterOrder}: anchor + facing + shape; {@code RetreatOrder}: a destination)
 * resolved that need the way this javadoc originally left open: by taking the data as
 * CONSTRUCTOR parameters rather than growing this interface's method signature -
 * {@code GuardCommandNetworking} builds a fresh, freshly-parameterized instance per
 * invocation from that order's own payload, then calls the exact same
 * {@code execute(player, guards)} every other order implements. This keeps the
 * interface itself exactly as small as it always was, while still letting an order
 * carry whatever it individually needs - the "one Order system, different scopes and
 * parameters" property the design brief asks for.
 */
@FunctionalInterface
public interface OrderExecutor {
    void execute(ServerPlayer player, List<Guard> guards);
}
