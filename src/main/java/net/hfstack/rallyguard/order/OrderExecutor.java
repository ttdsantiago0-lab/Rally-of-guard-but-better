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
 * Stance; this interface only covers "apply the order once, right now."
 * <p>
 * Kept separate from {@code contract} (which holds persistent {@code SavedData}
 * registries - {@link net.hfstack.rallyguard.contract.StanceRegistry},
 * {@link net.hfstack.rallyguard.contract.SquadRegistry},
 * {@link net.hfstack.rallyguard.contract.GuardRoster}) precisely because Orders are not
 * persisted state - issuing "Recall" doesn't need to survive a server restart, unlike a
 * guard's squad or stance.
 * <p>
 * This first pass intentionally has NO target parameter (entity/position/area) - neither
 * of the two first orders ({@link RecallOrder}, {@link HoldOrder}) needs one. A future
 * order that does (Attack, Muster, Kamikaze) will need a richer call shape; per the
 * project's "don't add speculative APIs unless genuinely needed now" rule, that shape is
 * deliberately not designed yet, only left possible: nothing here (a functional
 * interface, a string-keyed {@link OrderRegistry}, a flat
 * {@code List<Integer> entityIds}-based payload) assumes exactly zero targets forever -
 * the Battle Planner's own future "chain of orders" only needs each order to be
 * addressable by a stable string id and runnable against a batch of guards, both of
 * which already hold here.
 */
@FunctionalInterface
public interface OrderExecutor {
    void execute(ServerPlayer player, List<Guard> guards);
}
