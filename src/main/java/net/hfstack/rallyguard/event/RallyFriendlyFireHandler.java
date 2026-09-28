package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * Phase 2H, part 1: stops a player from accidentally hurting their own following/rallied
 * guard - sword sweep, a stray arrow, anything - while fighting next to it. Extended
 * 2026-09-15 (real bug reported by the user) to also stop two of the SAME owner's guards
 * from accidentally damaging EACH OTHER (stray arrows/melee sweeps), unconditionally -
 * not gated on Following, since that case has no legitimate reason to ever happen. See
 * {@link #onIncomingDamage} for the guard-vs-guard half specifically. Originally ported
 * from
 * the Forge 1.20.1 donor's {@code RallyFriendlyFireHandler}, which used two hooks: a
 * direct-hit cancel on {@code AttackEntityEvent}, and a catch-all on
 * {@code LivingAttackEvent} for everything else (sweep attacks, projectiles, tridents -
 * anything that damages without going through {@code Player#attack(Entity)} directly).
 * <p>
 * Forge -&gt; NeoForge translation, both verified against the real NeoForge 1.21.1 source
 * tree (not guessed):
 * <p>
 * 1. {@code net.minecraftforge.event.entity.player.AttackEntityEvent} moves package to
 * {@code net.neoforged.neoforge.event.entity.player.AttackEntityEvent} - same shape
 * ({@code getEntity()} returns the attacking {@code Player}, {@code getTarget()} the
 * victim, cancellable).
 * <p>
 * 2. {@code LivingAttackEvent} does not exist in 1.21.1 NeoForge (confirmed: no such file
 * anywhere under {@code net.neoforged.neoforge.event.entity.living} in the real NeoForge
 * source). Its replacement is {@code LivingIncomingDamageEvent}, fired from
 * {@code LivingEntity#hurt(DamageSource, float)} "after invulnerability checks but before
 * any damage processing" (per its own javadoc) - functionally the same interception point
 * the donor relied on (before the hit lands), and still cancellable. The attacker is read
 * the same way the donor did: {@code DamageSource#getEntity()} for a direct melee hit, or
 * {@code DamageSource#getDirectEntity()} for a projectile/thrown weapon (both confirmed
 * still present on {@code DamageSource} via mappings.dev for 1.21.1 - unchanged from the
 * Forge donor's usage).
 * <p>
 * Whether a guard counts as "in a state where friendly fire should be blocked" uses this
 * project's typed API policy (PORTING_PLAN.md section 4.2, applied since Phase 2C):
 * {@code Guard#isFollowing()} directly, instead of the donor's NBT round-trip
 * ({@code saveWithoutId} + reading the "Following"/"rallyguard:in_rally" tags by hand).
 * The donor's own {@code isInRallyOrFollowing} helper checked BOTH flags, but
 * {@code rallyguard:in_rally} was already established as dead/no-op NBT in this port
 * (see {@code ScrollOfRallyingItem}'s javadoc and PORTING_PLAN.md section 4.2 point 4) -
 * the Scroll of Rallying only ever calls {@code setFollowing(true)} on a rallied guard,
 * never sets any custom "in rally" flag, so checking {@code isFollowing()} alone is the
 * complete, non-dead equivalent of the donor's two-flag check.
 */
public final class RallyFriendlyFireHandler {

    @SubscribeEvent
    public void onPlayerAttackEntity(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        Player player = event.getEntity();
        Entity target = event.getTarget();

        if (!(target instanceof Guard guard)) return;
        if (!GuardOwnership.isOwnedBy(guard, player.getUUID())) return;
        if (!guard.isFollowing()) return;

        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) return;

        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        Entity direct = source.getDirectEntity();

        // A commander is never hurt by their own guards' stray arrows or swings (guards
        // never target their owner on purpose, so those are always accidents). Their own
        // side's EXPLOSIONS (a kamikaze charge names the guard as its cause) do hurt, but
        // halved and capped - see softenOwnBlast.
        if (victim instanceof Player commander) {
            Entity cause = attacker instanceof Guard ? attacker : direct;
            if (cause instanceof Guard ownGuard && GuardOwnership.isOwnedBy(ownGuard, commander.getUUID())) {
                softenOwnBlastOrCancel(event);
            }
            return;
        }

        // Mount protection (the "guard arrows hit horses" report, PORTING_PLAN.md section
        // 17.4): a guard's shot never hurts a mount that it - or another guard of the same
        // commander - is riding. Vanilla projectiles only skip their shooter, not its
        // vehicle or a squadmate's.
        if (!(victim instanceof Guard)) {
            Guard shooter = null;
            if (attacker instanceof Guard g) {
                shooter = g;
            } else if (direct instanceof Guard g2) {
                shooter = g2;
            }
            if (shooter != null && GuardOwnership.hasOwner(shooter)) {
                for (Entity passenger : victim.getPassengers()) {
                    if (passenger instanceof Guard rider
                            && GuardOwnership.isOwnedBy(rider, GuardOwnership.getOwner(shooter))) {
                        softenOwnBlastOrCancel(event);
                        return;
                    }
                }
            }
            return;
        }
        Guard guard = (Guard) victim;

        ServerPlayer attackerPlayer = null;
        if (attacker instanceof ServerPlayer sp) {
            attackerPlayer = sp;
        } else if (direct instanceof ServerPlayer sp2) {
            attackerPlayer = sp2;
        }

        if (attackerPlayer != null && GuardOwnership.isOwnedBy(guard, attackerPlayer.getUUID())
                && guard.isFollowing()) {
            event.setCanceled(true);
            return;
        }

        // FIX (2026-09-15, real bug reported by the user): "cuando estan libres, sin
        // rally, pueden hacerse dano entre ellos" - two of the SAME owner's guards could
        // still damage each other (a stray arrow, a melee sweep hitting an ally standing
        // next to the intended target) whenever they weren't actively Following/rallied.
        // Guard Villagers' own HurtByTargetGoal already stops a hit guard from
        // RETALIATING against another Guard (registerGoals() explicitly ignores
        // Guard.class/IronGolem.class as retaliation sources), but that only prevents a
        // guard from choosing to fight back - it never stopped the original stray hit
        // from landing in the first place, and stances never deliberately target a
        // squadmate either (see GuardStanceHandler#isEligible), so any guard-on-guard
        // damage that does happen is accidental collateral, not a design choice. Blocked
        // unconditionally (not gated on isFollowing, unlike the player-vs-own-guard case
        // above) since two guards sharing an owner should never hurt each other, in or
        // out of a Rally.
        Guard attackerGuard = null;
        if (attacker instanceof Guard ag) {
            attackerGuard = ag;
        } else if (direct instanceof Guard dg) {
            attackerGuard = dg;
        }

        if (attackerGuard != null && attackerGuard != guard
                && GuardOwnership.hasOwner(guard) && GuardOwnership.hasOwner(attackerGuard)
                && GuardOwnership.getOwner(guard) != null
                && GuardOwnership.getOwner(guard).equals(GuardOwnership.getOwner(attackerGuard))) {
            // Phase 3F: except the commander's own explosions (kamikaze), which hurt -
            // softened - instead of being ignored.
            softenOwnBlastOrCancel(event);
        }
    }

    /** Share of an own-side explosion's damage that still lands on a friend. */
    private static final float OWN_BLAST_FACTOR = 0.5F;
    /** Most damage (4 hearts) one own-side explosion can do to one friend. */
    private static final float OWN_BLAST_CAP = 8.0F;

    /**
     * Friendly damage from our own side: accidents (arrows, sweeps) are cancelled; our own
     * EXPLOSIONS (a kamikaze charge) still hurt, but halved and capped at
     * {@link #OWN_BLAST_CAP}. User decision (2026-09-28): full immunity was "muy roto",
     * yet one bad call shouldn't wipe out the commander or a whole squad. The first line
     * of defense is {@code KamikazeOrder} refusing to light the charge next to a friend;
     * this only limits what still gets caught.
     */
    private static void softenOwnBlastOrCancel(LivingIncomingDamageEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
            event.setAmount(Math.min(event.getAmount() * OWN_BLAST_FACTOR, OWN_BLAST_CAP));
        } else {
            event.setCanceled(true);
        }
    }
}
