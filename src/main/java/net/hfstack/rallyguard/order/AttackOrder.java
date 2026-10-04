package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.contract.DirectiveRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.List;

/**
 * "Fight THIS." - Phase 3E rework of the Phase 3D Attack order.
 * <p>
 * Phase 3D's Attack always meant "nearest monster to me", applied with a one-shot
 * {@code setTarget} that (a) was silently vetoed for DEFENSIVE/PASSIVE guards by their own
 * stance gate, and (b) was forgotten by the guard the moment anything else happened. Now:
 * <ul>
 *     <li>The commander picks WHO via a {@link TargetSpec} (nearest hostile, whoever
 *     attacked us, the entity marked with the Ledger, or a specific player), resolved
 *     server-side before this order is built.</li>
 *     <li>The target is stored as each guard's designated target in
 *     {@link DirectiveRegistry} - a standing instruction {@code GuardStanceHandler} keeps
 *     re-applying (once a second) whenever the guard loses it and it is back in reach.
 *     Players are never auto-cleared from it (they respawn); dead mobs are.</li>
 *     <li>Phase 3H.1 (user review: "alto al fuego se pisa la cola con G"): Attack no
 *     longer lifts HOLD FIRE. Hold fire is the top of the combat hierarchy
 *     (docs/ARQUITECTURA.md §1): under it, Attack only DESIGNATES the target - the
 *     guards open fire the moment the commander releases them (H / Release Fire). That is
 *     the real-world "target acquired, hold until my command".</li>
 *     <li>Attack and DEFEND are one job at a time: Attack ends a ward, Defend ends a
 *     designated target.</li>
 *     <li>Whether guards leave their position to do it is NOT decided here: a posted
 *     guard (Muster/Hold Ground) engages the target only inside its post leash, a free
 *     guard pursues it. The same order therefore produces a charging cavalry squad or a
 *     wall of archers focusing one enemy, depending on how the squad was deployed - the
 *     composability the design brief asks for, without a second order.</li>
 * </ul>
 * A {@code null} target (spec {@link TargetSpec#NONE}) clears the designated target and
 * the current fight, returning the squad to its stance's own judgement.
 */
public final class AttackOrder implements OrderExecutor {

    @Nullable
    private final LivingEntity target;

    public AttackOrder(@Nullable LivingEntity target) {
        this.target = target;
    }

    private int holding;

    /** How many guards of the last {@link #execute} kept the target only as designated,
     *  because they are under HOLD FIRE. */
    public int holding() {
        return holding;
    }

    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        DirectiveRegistry directives = DirectiveRegistry.get(player.serverLevel());
        holding = 0;
        for (Guard guard : guards) {
            if (target == null) {
                directives.setTarget(guard.getUUID(), null, "");
                CommandTargeting.disengage(guard);
                continue;
            }
            directives.setTarget(guard.getUUID(), target.getUUID(), target.getName().getString());
            directives.setWard(guard.getUUID(), null, "");
            if (directives.directiveOf(guard.getUUID()).holdFire()) {
                holding++;
                continue;
            }
            if (CommandTargeting.wouldAccept(guard, target)
                    && CommandTargeting.inReach(guard, target, CommandTargeting.PURSUIT_RANGE)
                    && Engagement.tryEngage(guard, target)) { // the rest screen it (stance loop)
                CommandTargeting.issue(guard, target);
            }
        }
    }
}
