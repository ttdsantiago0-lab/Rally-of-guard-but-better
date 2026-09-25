package net.hfstack.rallyguard.network;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.GuardRoster;
import net.hfstack.rallyguard.contract.Stance;
import net.hfstack.rallyguard.contract.StanceRegistry;
import net.hfstack.rallyguard.contract.SquadRegistry;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.network.payload.AssignSquadC2SPayload;
import net.hfstack.rallyguard.network.payload.AssignStanceC2SPayload;
import net.hfstack.rallyguard.network.payload.Destination;
import net.hfstack.rallyguard.network.payload.GuardActionC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardListS2CPayload;
import net.hfstack.rallyguard.network.payload.IssueMusterOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueRetreatOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.OpenGuardCommandC2SPayload;
import net.hfstack.rallyguard.order.MusterOrder;
import net.hfstack.rallyguard.order.OrderExecutor;
import net.hfstack.rallyguard.order.OrderRegistry;
import net.hfstack.rallyguard.order.RetreatOrder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.hfstack.rallyguard.screen.GuardCommandScreen;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

    /**
     * Phase 3A: the Commander's Ledger's multi-select "assign to squad" action. Runs on
     * a batch of entity ids at once (one packet per click of "Assign", not one per
     * guard), re-checking ownership per guard exactly like {@link #handleAction} does for
     * single-guard actions - a stale/spoofed entity id just gets silently skipped rather
     * than trusted.
     */
    public static void handleAssignSquad(AssignSquadC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                assignSquad(serverPlayer, payload.entityIds(), payload.squadName());
            }
        });
    }

    /**
     * Phase 3C: the Commander's Ledger's multi-select "set stance" action - structurally
     * identical to {@link #handleAssignSquad} (batch, per-guard ownership re-check,
     * re-sends the guard list afterward so the client's stance labels resync in one shot).
     */
    public static void handleAssignStance(AssignStanceC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                assignStance(serverPlayer, payload.entityIds(), payload.stance());
            }
        });
    }

    /**
     * Phase 3D: the Commander's Ledger's multi-select "issue order" action - covers
     * every zero-parameter order (Recall/Hold/Attack/Reform, looked up by id in
     * {@link OrderRegistry}); structurally identical to {@link #handleAssignStance}/
     * {@link #handleAssignSquad} (batch, per-guard ownership re-check, re-sends the
     * guard list afterward).
     */
    public static void handleIssueOrder(IssueOrderC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                issueOrder(serverPlayer, payload.entityIds(), payload.orderId());
            }
        });
    }

    /**
     * Phase 3D: the Commander's Ledger's Muster action - unlike {@link #handleIssueOrder},
     * this order needs a destination and a formation shape, so it gets its own payload
     * and constructs a fresh {@link MusterOrder} per call instead of going through
     * {@link OrderRegistry}'s bare-id lookup (see that registry's javadoc).
     */
    public static void handleIssueMusterOrder(IssueMusterOrderC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                musterOrder(serverPlayer, payload.entityIds(), payload.destination(), payload.shape());
            }
        });
    }

    /** Phase 3D: the Commander's Ledger's Retreat action - same reasoning as
     *  {@link #handleIssueMusterOrder} (needs a destination, so its own payload). */
    public static void handleIssueRetreatOrder(IssueRetreatOrderC2SPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                retreatOrder(serverPlayer, payload.entityIds(), payload.destination());
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
        //
        // Note this scan is still scoped to `level` - the dimension the PLAYER is
        // currently in - so it only ever finds guards loaded in that same dimension.
        // That was the Ledger's whole guard list before Phase 3B; now it's just the
        // "online" half (see below).
        List<? extends Guard> guards = level.getEntities(EntityTypeTest.forClass(Guard.class),
                guard -> GuardOwnership.isOwnedBy(guard, player.getUUID()));

        SquadRegistry squadRegistry = SquadRegistry.get(level);
        GuardRoster roster = GuardRoster.get(level);
        StanceRegistry stanceRegistry = StanceRegistry.get(level);

        List<GuardListS2CPayload.Entry> entries = new ArrayList<>(guards.size());
        Set<UUID> onlineIds = new HashSet<>(guards.size());

        for (Guard guard : guards) {
            UUID guardId = guard.getUUID();
            onlineIds.add(guardId);

            String squadName = squadRegistry.squadOf(guardId).map(squad -> squad.name).orElse("");
            String name = guard.getName().getString();
            boolean patrolling = guard.isPatrolling();
            String mount = GuardOwnership.describeMount(guard);
            String armor = GuardOwnership.describeArmor(guard);
            String stance = stanceRegistry.stanceOf(guardId).name();

            // Phase 3B: every guard actually found loaded refreshes its durable
            // snapshot right here, so the NEXT time the Ledger is opened while this
            // guard isn't loaded, its last-seen name/mount/armor/patrol state is still
            // shown instead of the guard just vanishing from the list. See
            // GuardRoster's own javadoc for why this "refresh opportunistically,
            // whenever loaded" approach (rather than a periodic background scan) is a
            // deliberate, acceptable trade-off here. Stance itself is NOT cached in the
            // roster - StanceRegistry already works for unloaded guards by its own
            // design (Phase 3C mirrors Phase 3A's squad-lookup treatment here), so an
            // offline row's stance is looked up live below, not from a stale snapshot.
            roster.recordSnapshot(guardId, player.getUUID(), name, patrolling, mount, armor,
                    level.getGameTime());

            entries.add(new GuardListS2CPayload.Entry(guard.getId(), name, patrolling, squadName,
                    new GuardListS2CPayload.Entry.Condition(mount, armor), stance));
        }

        // Phase 3B: every OTHER guard this player owns that the roster remembers but
        // wasn't found loaded just now - shown with the cached snapshot instead of
        // silently missing from the list. entityId is the OFFLINE_ENTITY_ID sentinel;
        // GuardCommandScreen disables selection/Summon/Patrol for these rows since
        // there's no live entity to act on (see GuardListS2CPayload's own javadoc).
        // Squad membership is still looked up live from SquadRegistry, not cached here -
        // that registry already works for unloaded guards by design (Phase 3A).
        for (Map.Entry<UUID, GuardRoster.Entry> e : roster.entriesOwnedBy(player.getUUID()).entrySet()) {
            UUID guardId = e.getKey();
            if (onlineIds.contains(guardId)) continue;

            GuardRoster.Entry cached = e.getValue();
            String squadName = squadRegistry.squadOf(guardId).map(squad -> squad.name).orElse("");
            String stance = stanceRegistry.stanceOf(guardId).name();
            entries.add(new GuardListS2CPayload.Entry(GuardListS2CPayload.Entry.OFFLINE_ENTITY_ID,
                    cached.name, cached.patrolling, squadName,
                    new GuardListS2CPayload.Entry.Condition(cached.mount, cached.armor), stance));
        }

        // Server -> one specific client: same call shape Guard Villagers' own source uses
        // (Guard.java's openGui()) to push GuardOpenInventoryPacket to its owner.
        player.connection.send(new GuardListS2CPayload(entries));
    }

    /**
     * Phase 3D: resolves {@code entityIds} to live, owned guards - a stale/spoofed/
     * not-owned id is silently skipped rather than trusted, same tolerance
     * {@link #assignSquad}/{@link #assignStance} already have. Every batch order
     * (issueOrder, musterOrder, retreatOrder) shares this one resolution step rather
     * than repeating the same loop three times.
     */
    private static List<Guard> resolveOwnedGuards(ServerLevel level, ServerPlayer player, List<Integer> entityIds) {
        List<Guard> guards = new ArrayList<>();
        for (int entityId : entityIds) {
            Entity entity = level.getEntity(entityId);
            if (!(entity instanceof Guard guard)) continue;
            if (!GuardOwnership.isOwnedBy(guard, player.getUUID())) continue;
            guards.add(guard);
        }
        return guards;
    }

    /**
     * Phase 3D: hands the resolved batch to whichever {@link OrderExecutor} {@code
     * orderId} names in {@link OrderRegistry}. An unrecognized order id (older/newer
     * client, or a typo) is a silent no-op, same tolerance {@link #handleAction}
     * already has for an unknown action code.
     */
    private static void issueOrder(ServerPlayer player, List<Integer> entityIds, String orderId) {
        OrderExecutor executor = OrderRegistry.get(orderId);
        if (executor == null) {
            return;
        }

        List<Guard> guards = resolveOwnedGuards(player.serverLevel(), player, entityIds);
        if (guards.isEmpty()) {
            return;
        }

        executor.execute(player, guards);

        String orderKey = "gui.rallyguard.order." + orderId.toLowerCase(Locale.ROOT);
        player.sendSystemMessage(Component.translatable("gui.rallyguard.command.order_issued",
                guards.size(), Component.translatable(orderKey).getString()));

        sendGuardList(player);
    }

    /**
     * Phase 3D: resolves the batch, parses {@code shapeName} (falling back to
     * {@link FormationShape#LINE} for an unrecognized value, same soft-fail convention
     * as {@link #parseStanceOrDefault}), converts {@code destination} into a world
     * {@link Vec3} anchored at the block's center, and runs a fresh {@link MusterOrder}
     * built from those three pieces.
     */
    private static void musterOrder(ServerPlayer player, List<Integer> entityIds, Destination destination,
                                     String shapeName) {
        List<Guard> guards = resolveOwnedGuards(player.serverLevel(), player, entityIds);
        if (guards.isEmpty()) {
            return;
        }

        FormationShape shape = parseShapeOrDefault(shapeName);
        Vec3 anchor = new Vec3(destination.x() + 0.5, destination.y(), destination.z() + 0.5);
        new MusterOrder(anchor, destination.facingYawDegrees(), shape).execute(player, guards);

        player.sendSystemMessage(Component.translatable("gui.rallyguard.command.muster_issued",
                guards.size(), Component.translatable("gui.rallyguard.formation."
                        + shape.name().toLowerCase(Locale.ROOT)).getString()));

        sendGuardList(player);
    }

    /** Phase 3D: resolves the batch and runs a fresh {@link RetreatOrder} to
     *  {@code destination}'s position (its facing is unused by Retreat). */
    private static void retreatOrder(ServerPlayer player, List<Integer> entityIds, Destination destination) {
        List<Guard> guards = resolveOwnedGuards(player.serverLevel(), player, entityIds);
        if (guards.isEmpty()) {
            return;
        }

        Vec3 target = new Vec3(destination.x() + 0.5, destination.y(), destination.z() + 0.5);
        new RetreatOrder(target).execute(player, guards);

        player.sendSystemMessage(Component.translatable("gui.rallyguard.command.retreat_issued", guards.size()));

        sendGuardList(player);
    }

    private static FormationShape parseShapeOrDefault(String raw) {
        try {
            return FormationShape.valueOf(raw);
        } catch (IllegalArgumentException | NullPointerException e) {
            return FormationShape.LINE;
        }
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
        // FIX (2026-09-13, real bug reported by the user in-game: guards mounted on a
        // horse via the mounts datapack could not be summoned through the Commander's
        // Ledger). A vehicle drives its passenger's position every tick (see vanilla
        // Entity#positionRider), so calling moveTo(...) on a guard that is still a
        // passenger has no lasting effect - the horse simply repositions the guard back
        // onto itself on the next tick. Dismounting first (Entity#stopRiding(), a plain
        // vanilla API, not Guard Villagers-specific) removes the guard from the vehicle's
        // passenger list so the teleport actually sticks.
        if (guard.isPassenger()) {
            guard.stopRiding();
        }
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
            // FIX (2026-09-13, Phase 2F, same reasoning as summon()'s mounted-guard fix
            // above): Guard Villagers' own WalkBackToCheckPointGoal drives a guard to its
            // patrol position through Mob#getNavigation(), which cannot move an entity
            // that is currently a vehicle's passenger. Dismount first so a guard patrol-
            // ordered while riding a horse actually walks instead of silently never
            // starting.
            if (guard.isPassenger()) {
                guard.stopRiding();
            }
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

    /**
     * Phase 3A: assigns (or, when {@code squadName} is blank after trimming, clears) a
     * squad for every owned guard among {@code entityIds}. Guards that don't resolve to
     * an owned {@link Guard} (already gone, or not this player's) are silently skipped,
     * same tolerance {@link #handleAction} already has for a stale single entity id.
     * Re-sends the guard list afterward so the client's squad labels immediately reflect
     * the change - unlike {@link #summon}/{@link #togglePatrol}, which mutate the
     * client's local copy optimistically, a multi-entity batch is simpler to just let the
     * server resolve authoritatively and resync in one shot.
     */
    private static void assignSquad(ServerPlayer player, List<Integer> entityIds, String squadName) {
        ServerLevel level = player.serverLevel();
        SquadRegistry registry = SquadRegistry.get(level);
        String trimmed = squadName == null ? "" : squadName.trim();

        int affected = 0;
        for (int entityId : entityIds) {
            Entity entity = level.getEntity(entityId);
            if (!(entity instanceof Guard guard)) continue;
            if (!GuardOwnership.isOwnedBy(guard, player.getUUID())) continue;

            if (trimmed.isEmpty()) {
                registry.unassign(guard.getUUID());
            } else {
                registry.assign(guard.getUUID(), player.getUUID(), trimmed);
            }
            applySquadNameColor(guard, trimmed);
            affected++;
        }

        if (affected > 0) {
            Component msg = trimmed.isEmpty()
                    ? Component.translatable("gui.rallyguard.command.squad_cleared", affected)
                    : Component.translatable("gui.rallyguard.command.squad_assigned", affected, trimmed);
            player.sendSystemMessage(msg);
        }

        sendGuardList(player);
    }

    /**
     * Phase 3C: sets (or, for an unrecognized/blank stance name, silently falls back to
     * {@link Stance#DEFENSIVE} for) every owned guard among {@code entityIds}. Same
     * ownership-recheck/skip-if-not-owned tolerance as {@link #assignSquad}, and the
     * same "resync via a fresh guard list afterward" choice, since this is also a
     * multi-entity batch simpler to resolve authoritatively than to optimistically patch
     * client-side.
     */
    private static void assignStance(ServerPlayer player, List<Integer> entityIds, String stanceName) {
        ServerLevel level = player.serverLevel();
        StanceRegistry registry = StanceRegistry.get(level);
        Stance stance = parseStanceOrDefault(stanceName);

        int affected = 0;
        for (int entityId : entityIds) {
            Entity entity = level.getEntity(entityId);
            if (!(entity instanceof Guard guard)) continue;
            if (!GuardOwnership.isOwnedBy(guard, player.getUUID())) continue;

            registry.setStance(guard.getUUID(), stance);
            // FIX (2026-09-15, real bug reported by the user): switching a guard TO
            // Passive used to only block FUTURE target changes (via GuardStanceHandler's
            // LivingChangeTargetEvent cancel) - a guard already mid-fight (e.g. Ruthless,
            // engaged with a Creeper) kept fighting until it killed or lost that target,
            // only going quiet afterward, instead of disengaging immediately like the
            // player expects "Passive" to mean. Clearing the live target here (when one
            // exists) makes the switch take effect immediately for any currently-loaded
            // guard; an unloaded guard simply has no target to clear and will pick up the
            // new stance normally once loaded.
            if (stance == Stance.PASSIVE) {
                guard.setTarget(null);
            }
            affected++;
        }

        if (affected > 0) {
            player.sendSystemMessage(Component.translatable("gui.rallyguard.command.stance_set", affected,
                    Component.translatable("gui.rallyguard.stance." + stance.name().toLowerCase(Locale.ROOT)).getString()));
        }

        sendGuardList(player);
    }

    private static Stance parseStanceOrDefault(String raw) {
        try {
            return Stance.valueOf(raw);
        } catch (IllegalArgumentException | NullPointerException e) {
            return Stance.DEFENSIVE;
        }
    }

    /**
     * Phase 3A.1 (requested after real testing): color a guard's ALREADY-EXISTING name
     * tag by squad, so you can tell at a glance in the world which squad a guard belongs
     * to, without needing to open the Ledger. Deliberately conservative:
     * <ul>
     *     <li>Only touches guards that already have a visible name tag
     *     ({@code getCustomName()} non-null) - a guard with no name tag has nothing to
     *     color, and this never gives one a name it didn't already have.</li>
     *     <li>Only changes the {@code Component}'s color style, never its text - rebuilt
     *     from {@code getCustomName().getString()}, so the guard's actual name (however
     *     it was spelled when the name tag was used) is preserved exactly.</li>
     *     <li>The color is picked deterministically from the squad's name
     *     ({@code String#hashCode()} into a small fixed palette) rather than stored
     *     anywhere new, so every guard in the same squad always renders the same color
     *     with zero extra state to keep in sync - and clearing a guard's squad restores
     *     the same gold {@link GuardOwnership#GOLD_NAME_COLOR} every freshly-hired guard
     *     already gets (see {@link GuardOwnership#setOwner}), instead of leaving it
     *     uncolored.</li>
     * </ul>
     */
    private static void applySquadNameColor(Guard guard, String squadName) {
        Component current = guard.getCustomName();
        if (current == null) {
            return;
        }
        String plainText = current.getString();
        if (plainText.isEmpty()) {
            return;
        }

        MutableComponent updated = Component.literal(plainText);
        updated = squadName.isEmpty()
                ? updated.withStyle(s -> s.withColor(GuardOwnership.GOLD_NAME_COLOR))
                : updated.withStyle(colorForSquad(squadName));
        guard.setCustomName(updated);
    }

    private static final ChatFormatting[] SQUAD_NAME_COLORS = {
            ChatFormatting.AQUA, ChatFormatting.GREEN, ChatFormatting.YELLOW,
            ChatFormatting.LIGHT_PURPLE, ChatFormatting.GOLD, ChatFormatting.BLUE,
            ChatFormatting.DARK_AQUA, ChatFormatting.DARK_PURPLE, ChatFormatting.DARK_GREEN,
            ChatFormatting.GRAY
    };

    private static ChatFormatting colorForSquad(String squadName) {
        int index = Math.floorMod(squadName.toLowerCase(Locale.ROOT).hashCode(), SQUAD_NAME_COLORS.length);
        return SQUAD_NAME_COLORS[index];
    }
}
