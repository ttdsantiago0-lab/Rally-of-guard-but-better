package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.server.level.ServerPlayer;
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
 * guard - sword sweep, a stray arrow, anything - while fighting next to it. Ported from
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
        if (!(victim instanceof Guard guard)) return;

        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        Entity direct = source.getDirectEntity();

        ServerPlayer attackerPlayer = null;
        if (attacker instanceof ServerPlayer sp) {
            attackerPlayer = sp;
        } else if (direct instanceof ServerPlayer sp2) {
            attackerPlayer = sp2;
        }

        if (attackerPlayer == null) return;
        if (!GuardOwnership.isOwnedBy(guard, attackerPlayer.getUUID())) return;
        if (!guard.isFollowing()) return;

        event.setCanceled(true);
    }
}
