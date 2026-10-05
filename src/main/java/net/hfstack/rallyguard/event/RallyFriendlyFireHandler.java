package net.hfstack.rallyguard.event;

import com.mojang.logging.LogUtils;
import net.hfstack.rallyguard.config.RallyConfig;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import org.slf4j.Logger;
import tallestegg.guardvillagers.common.entities.Guard;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Friendly fire inside one commander's side. "Our side" of commander C is: C, C's guards,
 * any mount one of C's guards is riding, and (against C's guards' fire) C's own tamed
 * animals and horses ({@code OwnableEntity}).
 * <p>
 * Layers, each catching what the one before can miss (E1 rewrite after David's test:
 * guards still hurt other guards' horses):
 * <ol>
 *     <li><b>{@link AttackEntityEvent}</b> - the commander's own melee swing at one of
 *     their guards or at a mount one of their guards rides: cancelled.</li>
 *     <li><b>{@link ProjectileImpactEvent}</b> - any vanilla-style projectile (arrows,
 *     tridents, Rally's volleys) whose owner is the commander or one of their guards
 *     hitting our side: the impact is cancelled, so the arrow flies on instead of even
 *     knocking the horse back. A projectile with NO owner hitting a mount ridden by an
 *     owned guard is cancelled too (its rider is, by definition, a guard of that
 *     commander within 64 blocks - the rule David asked for).</li>
 *     <li><b>{@link LivingIncomingDamageEvent}</b> - every damage that still arrives,
 *     identified by the damage source's attacker, or by the direct entity's owner when
 *     the attacker is missing (some modded bullets): from our side to our side it is
 *     cancelled; our own EXPLOSIONS (a kamikaze) are halved and capped instead (user
 *     decision 2026-09-28). Projectile damage with no identifiable shooter on a mount of
 *     an owned guard is cancelled.</li>
 *     <li><b>Diagnostics</b> - every hit that reaches a mount ridden by an owned guard is
 *     logged at INFO (damage type, direct entity class, attacker class, what was done),
 *     at most once per mount every 2 s, so a source that still slips through shows up
 *     in the log (config {@code debug.logMountDamage}).</li>
 * </ol>
 */
public final class RallyFriendlyFireHandler {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<UUID, Long> LAST_LOG = new HashMap<>();

    // ==== Layer 1 ====

    @SubscribeEvent
    public void onPlayerAttackEntity(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        UUID player = event.getEntity().getUUID();
        Entity target = event.getTarget();
        if (GuardOwnership.isOwnedBy(target, player) || riderOwners(target).contains(player)) {
            event.setCanceled(true);
        }
    }

    // ==== Layer 2 ====

    @SubscribeEvent
    public void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (projectile.level().isClientSide()) return;
        if (!(event.getRayTraceResult() instanceof EntityHitResult hit)) return;
        Entity victim = hit.getEntity();
        Entity owner = projectile.getOwner();
        UUID side = sideOf(owner);
        if (side != null) {
            if (isOurSide(victim, side, owner instanceof Guard)) event.setCanceled(true);
            return;
        }
        if (owner == null && !riderOwners(victim).isEmpty()) {
            event.setCanceled(true); // unknown shooter at a mount carrying an owned guard
        }
    }

    // ==== Layer 3 (+ 4) ====

    @SubscribeEvent
    public void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) return;
        long t0 = net.hfstack.rallyguard.debug.Perf.t0();
        try {
            onDamage(event, victim);
        } finally {
            net.hfstack.rallyguard.debug.Perf.add(net.hfstack.rallyguard.debug.Perf.Sys.FRIENDLY_FIRE, t0);
        }
    }

    private void onDamage(LivingIncomingDamageEvent event, LivingEntity victim) {

        DamageSource source = event.getSource();
        Entity direct = source.getDirectEntity();
        Entity attacker = source.getEntity();
        if (attacker == null && direct instanceof Projectile p) attacker = p.getOwner();
        if (attacker == null && (direct instanceof Guard || direct instanceof Player)) attacker = direct;

        Set<UUID> riders = riderOwners(victim);
        String outcome = "allowed";
        UUID side = sideOf(attacker);
        if (side != null && attacker != victim && isOurSide(victim, side, attacker instanceof Guard)) {
            outcome = softenOwnBlastOrCancel(event);
        } else if (attacker == null && !riders.isEmpty()
                && (direct != null || source.is(DamageTypeTags.IS_PROJECTILE))) {
            event.setCanceled(true); // projectile/entity damage with no traceable shooter
            outcome = "cancelled (unknown shooter)";
        }

        if (!riders.isEmpty()) logMountHit(victim, source, direct, attacker, event.getAmount(), outcome);
    }

    // ==== Sides ====

    /** The commander {@code e} fights for: a player is their own side, an owned guard its
     *  owner's. Null for anything else. */
    @Nullable
    private static UUID sideOf(@Nullable Entity e) {
        if (e instanceof Player p) return p.getUUID();
        if (e instanceof Guard g && GuardOwnership.hasOwner(g)) return GuardOwnership.getOwner(g);
        return null;
    }

    /** Is {@code victim} on commander {@code side}'s side? {@code byGuard}: the hit comes
     *  from a guard - then the commander themself and their owned animals count too. */
    private static boolean isOurSide(Entity victim, UUID side, boolean byGuard) {
        if (GuardOwnership.isOwnedBy(victim, side)) return true;
        if (riderOwners(victim).contains(side)) return true;
        if (byGuard) {
            if (victim instanceof Player p && p.getUUID().equals(side)) return true;
            if (victim instanceof OwnableEntity owned && side.equals(owned.getOwnerUUID())) return true;
        }
        return false;
    }

    /** Owners of the owned guards riding {@code entity} (empty for most entities). */
    private static Set<UUID> riderOwners(Entity entity) {
        Set<UUID> out = null;
        for (Entity passenger : entity.getPassengers()) {
            if (passenger instanceof Guard rider && GuardOwnership.hasOwner(rider)) {
                if (out == null) out = new HashSet<>();
                out.add(GuardOwnership.getOwner(rider));
            }
        }
        return out == null ? Set.of() : out;
    }

    private static void logMountHit(LivingEntity mount, DamageSource source, @Nullable Entity direct,
                                    @Nullable Entity attacker, float amount, String outcome) {
        if (!RallyConfig.on(RallyConfig.LOG_MOUNT_DAMAGE)) return;
        long now = mount.level().getGameTime();
        Long last = LAST_LOG.get(mount.getUUID());
        if (last != null && now - last < 40) return;
        LAST_LOG.put(mount.getUUID(), now);
        if (LAST_LOG.size() > 512) LAST_LOG.clear();
        LOGGER.info("[rallyguard] mount hit: {} type={} direct={} attacker={} amount={} -> {}",
                mount.getType().getDescriptionId(), source.getMsgId(),
                direct == null ? "none" : direct.getClass().getName(),
                attacker == null ? "none" : attacker.getClass().getName(), amount, outcome);
    }

    /** Share of an own-side explosion's damage that still lands on a friend. */
    private static final float OWN_BLAST_FACTOR = 0.5F;
    /** Most damage (4 hearts) one own-side explosion can do to one friend. */
    private static final float OWN_BLAST_CAP = 8.0F;

    /**
     * Friendly damage from our own side: accidents (arrows, sweeps, bullets) are
     * cancelled; our own EXPLOSIONS (a kamikaze charge) still hurt, but halved and capped
     * at {@link #OWN_BLAST_CAP} (user decision 2026-09-28: full immunity was "muy roto",
     * yet one bad call shouldn't wipe out a squad).
     */
    private static String softenOwnBlastOrCancel(LivingIncomingDamageEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_EXPLOSION)) {
            event.setAmount(Math.min(event.getAmount() * OWN_BLAST_FACTOR, OWN_BLAST_CAP));
            return "softened (own explosion)";
        }
        event.setCanceled(true);
        return "cancelled (own side)";
    }
}
