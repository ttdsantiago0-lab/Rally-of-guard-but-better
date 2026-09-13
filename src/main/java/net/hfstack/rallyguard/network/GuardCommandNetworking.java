package net.hfstack.rallyguard.network;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.network.payload.GuardActionC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardListS2CPayload;
import net.hfstack.rallyguard.network.payload.OpenGuardCommandC2SPayload;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.hfstack.rallyguard.screen.GuardCommandScreen;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.List;

/**
 * Server-side logic behind the Commander's Ledger networking (Phase 2D). Ported from the
 * Forge 1.20.1 / Fabric 1.21.1 {@code GuardCommandNetworking}, with one deliberate
 * behavior change per PORTING_PLAN.md section 4/4.2: every guard-state read/write here
 * goes through Guard Villagers' own typed {@code Guard} API
 * ({@code isPatrolling()}/{@code setPatrolling(boolean)}/{@code setFollowing(boolean)}/
 * {@code setPatrolPos(BlockPos)}, confirmed null-safe via {@code Guard.java}'s own
 * {@code Optional.ofNullable(position)} implementation) instead of the donor ports'
 * {@code saveWithoutId}/{@code load} NBT round-trip - the same policy already applied to
 * ownership in {@link GuardOwnership} during Phase 2C.
 * <p>
 * The donor ports' redundant {@code rallyguard:in_rally} write on the summon action is
 * dropped rather than ported forward: PORTING_PLAN.md section 4.2 point 4 found that key
 * can never actually be read back (a bare custom NBT key doesn't survive a fresh
 * {@code saveWithoutId()} call on an entity whose own save/load methods don't declare it),
 * so it was very likely always a no-op in both existing ports - this is a documented
 * simplification, not a feature removal.
 * <p>
 * Messages use {@code ServerPlayer.sendSystemMessage(Component)} (chat), matching the
 * exact call shape already verified compiling in {@link GuardOwnership} during Phase 2C,
 * rather than the donor ports' {@code displayClientMessage(Component, true)} (action bar)
 * - deliberately kept consistent with this project's one already-proven overload instead
 * of assuming an unverified one. Worth revisiting for UI polish once the Ledger GUI
 * (Phase 2E) is in front of the player.
 * <p>
 * Registered from {@link NetworkBootstrap}; nothing calls into this class from a UI yet -
 * the Commander's Ledger GUI that will trigger these packets is Phase 2E, per
 * PORTING_PLAN.md section 10 ("D. Networking ... No UI consumes them yet beyond what C
 * already needs").
 */
public final class GuardCommandNetworking {
    private GuardCommandNetworking() {
    }

    public static void handleOpenGuardCommand(OpenGuardCommandC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                sendGuardList(serverPlayer);
            }
        });
    }

    public static void handleGuardAction(GuardActionC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                handleAction(serverPlayer, payload.entityId(), payload.action());
            }
        });
    }

    public static void handleGuardList(GuardListS2CPayload payload, IPayloadContext context) {
        // Phase 2E: the Commander's Ledger GUI now exists - open/refresh it with the
        // guard list the server just sent. Already running on the client main thread
        // via IPayloadContext.enqueueWork(...) (see GuardCommandScreen.openFromPayload's
        // own javadoc), so no extra thread marshaling is needed here.
        context.enqueueWork(() -> GuardCommandScreen.openFromPayload(payload));
    }

    // ==== Server-side implementation ====

    private static void sendGuardList(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        // FIX (2026-09-13, real bug found via the user's own log, not guessed): the
        // Forge/Fabric donors' giant-AABB sweep (-3.0e7..3.0e7, "search the whole
        // world") gets silently ABORTED by the Sable mod's own entity-getter mixin
        // (SubLevelInclusiveLevelEntityGetter.get(...) logs "Aborting entity get for
        // abnormally large AABB" and returns nothing) - confirmed directly in
        // logs/latest.log, 57 occurrences, one per Commander's Ledger open. This meant
        // GuardOwnership.setOwner(...) was working correctly (confirmed by the
        // "reporting for duty" chat message actually appearing), but the guard never
        // showed up in the list because Sable silently emptied the query, not because
        // ownership failed to persist.
        // Fix: use the no-AABB ServerLevel#getEntities(EntityTypeTest, Predicate)
        // overload instead - it doesn't go through Sable's bounding-box guard at all,
        // and is more correct anyway (no arbitrary box size to get wrong for a large
        // world). Retyped to the compile-time Guard class via EntityTypeTest.forClass(...)
        // instead of a ForgeRegistries/EntityType lookup by ResourceLocation, matching
        // the simplification GuardOwnership.isGuard(...) already made in Phase 2C.
        List<? extends Guard> guards = level.getEntities(EntityTypeTest.forClass(Guard.class),
                guard -> GuardOwnership.isOwnedBy(guard, player.getUUID()));

        List<GuardListS2CPayload.Entry> entries = new ArrayList<>(guards.size());
        for (Guard guard : guards) {
            entries.add(new GuardListS2CPayload.Entry(guard.getId(), guard.getName().getString(), guard.isPatrolling()));
        }

        // Server -> one specific client: same call shape Guard Villagers' own source uses
        // (Guard.java's openGui()) to push GuardOpenInventoryPacket to its owner.
        player.connection.send(new GuardListS2CPayload(entries));
    }

    private static void handleAction(ServerPlayer player, int entityId, int action) {
        ServerLevel level = player.serverLevel();
        Entity e = level.getEntity(entityId);

        if (!(e instanceof Guard guard)) {
            player.sendSystemMessage(Component.translatable("gui.rallyguard.command.not_found"));
            return;
        }
        if (!GuardOwnership.isOwnedBy(guard, player.getUUID())) {
            player.sendSystemMessage(Component.translatable("gui.rallyguard.command.not_owner"));
            return;
        }

        switch (action) {
            case NetworkConstants.ACTION_SUMMON -> summon(player, guard);
            case NetworkConstants.ACTION_TOGGLE_PATROL -> togglePatrol(player, guard);
            default -> {
                // Unknown action code - ignore, matching the donor ports' behavior of a
                // silent no-op default (their switch has no default branch at all).
            }
        }
    }

    private static void summon(ServerPlayer player, Guard guard) {
        double ox = (player.getRandom().nextDouble() - 0.5) * 2.5;
        double oz = (player.getRandom().nextDouble() - 0.5) * 2.5;
        guard.moveTo(player.getX() + ox, player.getY(), player.getZ() + oz, guard.getYRot(), guard.getXRot());
        player.sendSystemMessage(Component.translatable("gui.rallyguard.command.summoned"));
    }

    private static void togglePatrol(ServerPlayer player, Guard guard) {
        if (guard.isPatrolling()) {
            guard.setPatrolling(false);
            guard.setFollowing(false);
            guard.setPatrolPos(null);
            resetNavigation(guard);
            player.sendSystemMessage(Component.translatable("gui.rallyguard.command.patrol_off"));
        } else {
            guard.setFollowing(false);
            guard.setPatrolPos(player.blockPosition());
            guard.setPatrolling(true);
            resetNavigation(guard);
            player.sendSystemMessage(Component.translatable("gui.rallyguard.command.patrol_on"));
        }
    }

    private static void resetNavigation(Guard guard) {
        // Generic Mob/Mob.getNavigation() API, unrelated to the ownership/state-API
        // decision above - carried over unchanged from the Forge/Fabric ports
        // (PORTING_PLAN.md section 4.2, final bullet).
        guard.setTarget(null);
        guard.setAggressive(false);
        guard.getNavigation().stop();
    }
}
