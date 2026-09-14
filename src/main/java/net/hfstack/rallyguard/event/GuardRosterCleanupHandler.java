package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.GuardRoster;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.contract.StanceRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import tallestegg.guardvillagers.common.entities.Guard;

/**
 * Phase 3B: drops a dead guard's cached {@link GuardRoster} entry (and its
 * {@link SquadRegistry} membership, if any) so the Commander's Ledger stops listing it
 * forever.
 * <p>
 * Without this, {@link GuardRoster} - deliberately populated only opportunistically
 * (hire time, or whenever {@code GuardCommandNetworking#sendGuardList} happens to find
 * the guard loaded - see that class's javadoc) and never swept by any periodic
 * background scan - would keep a dead guard's last-known snapshot around permanently:
 * there is no other event that would ever remove it. That would make the Ledger's
 * guard count slowly drift wrong as guards die, which defeats the point of a roster
 * that is supposed to be a trustworthy "what do I own" view. This handler is the one
 * and only removal path.
 * <p>
 * Folding in the {@link SquadRegistry#unassign} call here too fixes a related,
 * pre-existing gap noted since Phase 3A: squads never dropped dead members either,
 * for the exact same reason (no death hook existed until now). Both caches key on the
 * same guard UUID and both need the identical "this guard is gone for good" signal, so
 * one handler serving both avoids a second near-duplicate class.
 * <p>
 * {@link LivingDeathEvent} confirmed real on this NeoForge/MC 1.21.1 version via
 * nekoyue's ForgeJavaDocs-NG mirror pinned to {@code 1.21.x-neoforge} (package
 * {@code net.neoforged.neoforge.event.entity.living}, constructor
 * {@code LivingDeathEvent(LivingEntity entity, DamageSource source)}, inherits
 * {@code getEntity()}) before first use here - same two-source discipline as every
 * other first-time API this project has touched, though here a second source wasn't
 * needed since the javadoc mirror was unambiguous and this event class predates this
 * project's own {@code /tmp/neoforge-src} version-mismatch problems.
 */
public final class GuardRosterCleanupHandler {

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (victim.level().isClientSide()) return;
        if (!(victim instanceof Guard guard)) return;
        if (!GuardOwnership.hasOwner(guard)) return;

        ServerLevel level = (ServerLevel) victim.level();
        GuardRoster.get(level).remove(guard.getUUID());
        SquadRegistry.get(level).unassign(guard.getUUID());
        // Phase 3C: same reasoning - a dead guard's stance entry (if it had a
        // non-default one) would otherwise sit in StanceRegistry forever with no other
        // removal path.
        StanceRegistry.get(level).remove(guard.getUUID());
    }
}
