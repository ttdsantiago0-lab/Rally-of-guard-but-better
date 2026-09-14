package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * Phase 3A.1 (requested after real testing, then refined after in-game feedback on
 * 2026-09-14): makes an owned guard highly resistant to fall damage - "casi como si
 * tuvieran caida de pluma" (almost like they had Feather Falling), not immune.
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
 * event-hook extension points (PORTING_PLAN.md section 4.2). The follow/teleport-to-catch-up
 * behavior itself (which prompted this - "la ia de seguirme me encanta") is completely
 * untouched, only its worst-case consequence is softened.
 * <p>
 * First cut made an owned guard fully immune to fall damage; after trying it in-game the
 * user asked for resistance instead of immunity, so this now keeps only
 * {@link #RETAINED_FRACTION} of the incoming fall damage via
 * {@link LivingIncomingDamageEvent#setAmount(float)} (confirmed real on this exact
 * NeoForge version via the NeoForge source itself and CraftTweaker's NeoForge event docs
 * before first use here) rather than canceling the event outright. Trade-off, stated
 * plainly: this still applies to any fall, not just a post-teleport one (e.g. being
 * ordered off a genuine cliff), and it is a flat percentage rather than a capped amount
 * like the vanilla Feather Falling enchantment - a short teleport-catch-up fall becomes a
 * non-issue, an extreme one (falling from flight height) can still do noticeable, rarely
 * lethal, damage.
 */
public final class GuardFallDamageHandler {

    /** Fraction of incoming fall damage an owned guard still takes (10% retained = 90%
     *  reduction) - deliberately not 0 (immunity) and not vanilla Feather Falling's
     *  smaller per-level reduction, per the user's "resistant, not immune" request. */
    private static final float RETAINED_FRACTION = 0.10f;

    @SubscribeEvent
    public void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) return;
        if (!(victim instanceof Guard guard)) return;
        if (!GuardOwnership.hasOwner(guard)) return;
        if (!event.getSource().is(DamageTypeTags.IS_FALL)) return;

        event.setAmount(event.getAmount() * RETAINED_FRACTION);
    }
}
