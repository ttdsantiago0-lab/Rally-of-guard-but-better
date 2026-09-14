package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * Phase 3A.1 (requested after real testing): makes an owned guard immune to fall damage.
 * <p>
 * Real report that led to this: while flying, guards following the player try to
 * "teleport to catch up" and sometimes land somewhere precarious, then fall and die.
 * Confirmed the mechanism by reading Guard Villagers' real source
 * ({@code Guard#shouldTryTeleportToOwner}/{@code #tryToTeleportToOwner}/
 * {@code #teleportToAroundBlockPos}, {@code Guard.java}): a following guard more than 12
 * blocks from its owner (config permitting) teleports to a random spot within a few
 * blocks of the owner's position, filtered by {@code canTeleportTo}'s own
 * {@code PathType.WALKABLE} check. That filter already tries to avoid an obviously unsafe
 * landing, but a player flying at height can still leave the only "walkable" spot nearby
 * somewhere precarious (a mountain peak, a tree canopy, the edge of a floating build) -
 * an ordinary, undramatic ledge for a grounded mob that becomes lethal fall damage from
 * that height once the guard's own AI takes one more step.
 * <p>
 * That teleport-placement logic lives entirely inside Guard Villagers' own compiled
 * class, not this project's source - fixing the placement itself would need a mixin,
 * which this project has deliberately avoided everywhere else in favor of typed-API/
 * event-hook extension points (PORTING_PLAN.md section 4.2). The narrower, safer fix
 * used here instead: an owned guard simply never takes fall damage, the same shape as
 * the existing friendly-fire immunity for a following/rallied guard
 * ({@link RallyFriendlyFireHandler}) - the follow/teleport-to-catch-up behavior itself
 * (which prompted this - "la ia de seguirme me encanta") is completely untouched, only
 * its worst-case consequence is removed. Trade-off, stated plainly: this is blanket
 * immunity, not just post-teleport - an owned guard won't take fall damage from any
 * cause (e.g. being ordered off a genuine cliff), not only from a bad teleport landing.
 */
public final class GuardFallDamageHandler {

    @SubscribeEvent
    public void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) return;
        if (!(victim instanceof Guard guard)) return;
        if (!GuardOwnership.hasOwner(guard)) return;
        if (!event.getSource().is(DamageTypeTags.IS_FALL)) return;

        event.setCanceled(true);
    }
}
