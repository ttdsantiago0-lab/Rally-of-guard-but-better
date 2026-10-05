package net.hfstack.rallyguard.network;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.hfstack.rallyguard.network.payload.AssignSquadC2SPayload;
import net.hfstack.rallyguard.network.payload.AssignStanceC2SPayload;
import net.hfstack.rallyguard.network.payload.CommandKeyC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardActionC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardManageC2SPayload;
import net.hfstack.rallyguard.network.payload.SetSelectionC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardListS2CPayload;
import net.hfstack.rallyguard.network.payload.IssueAttackOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueKamikazeOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueMusterOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueRetreatOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.OpenGuardCommandC2SPayload;
import net.hfstack.rallyguard.network.payload.SquadActionC2SPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers Rally of the Guard's Commander's Ledger network payloads.
 * <p>
 * {@code SimpleChannel}/{@code NetworkRegistry.ChannelBuilder} (the Forge 1.20.1
 * mechanism this class replaces) no longer exists on NeoForge 1.21.1 - the modern
 * mechanism is {@code RegisterPayloadHandlersEvent} + {@code PayloadRegistrar}, verified
 * end-to-end against Guard Villagers' own real, compiling NeoForge 1.21.1 source
 * ({@code GuardVillagers.java}'s {@code register(RegisterPayloadHandlersEvent)} method -
 * see PORTING_PLAN.md section 6.2/15). Subscribed once, on the mod event bus, from
 * {@link RallyOfTheGuard}'s constructor - same {@code modEventBus.addListener(...)}
 * pattern already used there for creative-tab registration.
 */
public final class NetworkBootstrap {
    private NetworkBootstrap() {
    }

    /** Bumped if a payload's wire shape ever changes; matches GV's own {@code versioned(...)} use. */
    // "6" (E1): guard list carries fire mode + pack summary; pack menu payload.
    // "5" (Phase 3H.1): guard list carries squad totals + omitted count, capped rows.
    // "4" (Phase 3H): guard-list shape (unit paths, guard id, ward, selection) + manage/selection/hotkey payloads.
    // "3" (Phase 3G): squad actions. "2" (Phase 3E): the guard-list payload's shape changed and an attack payload was
    // added - a client and server on different versions must not connect.
    private static final String PROTOCOL_VERSION = "6";

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(RallyOfTheGuard.MOD_ID).versioned(PROTOCOL_VERSION);

        registrar.playToServer(
                OpenGuardCommandC2SPayload.TYPE,
                OpenGuardCommandC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleOpenGuardCommand);

        registrar.playToServer(
                GuardActionC2SPayload.TYPE,
                GuardActionC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleGuardAction);

        // Phase 3A: Commander's Ledger multi-select "assign to squad" action.
        registrar.playToServer(
                AssignSquadC2SPayload.TYPE,
                AssignSquadC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleAssignSquad);

        // Phase 3C: Commander's Ledger multi-select "set stance" action.
        registrar.playToServer(
                AssignStanceC2SPayload.TYPE,
                AssignStanceC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleAssignStance);

        // Phase 3D: Commander's Ledger multi-select "issue order" action (Order
        // abstraction - see the order package). Covers every zero-parameter order
        // (Recall/Hold/Attack/Reform, looked up by id in OrderRegistry).
        registrar.playToServer(
                IssueOrderC2SPayload.TYPE,
                IssueOrderC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleIssueOrder);

        // Phase 3D: Muster and Retreat need a destination the bare order-id dispatch
        // above can't carry, so each gets its own small payload - see
        // MusterOrder/OrderRegistry's javadoc for why these two aren't in OrderRegistry.
        registrar.playToServer(
                IssueMusterOrderC2SPayload.TYPE,
                IssueMusterOrderC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleIssueMusterOrder);

        registrar.playToServer(
                IssueAttackOrderC2SPayload.TYPE,
                IssueAttackOrderC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleIssueAttackOrder);

        registrar.playToServer(
                IssueKamikazeOrderC2SPayload.TYPE,
                IssueKamikazeOrderC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleIssueKamikazeOrder);

        registrar.playToServer(
                IssueRetreatOrderC2SPayload.TYPE,
                IssueRetreatOrderC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleIssueRetreatOrder);

        registrar.playToServer(
                SquadActionC2SPayload.TYPE,
                SquadActionC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleSquadAction);

        // Phase 3H: per-guard Forget/Rename, the shared selection, and the command hotkeys.
        registrar.playToServer(
                GuardManageC2SPayload.TYPE,
                GuardManageC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleGuardManage);

        registrar.playToServer(
                SetSelectionC2SPayload.TYPE,
                SetSelectionC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleSetSelection);

        registrar.playToServer(
                CommandKeyC2SPayload.TYPE,
                CommandKeyC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleCommandKey);

        registrar.playToClient(
                GuardListS2CPayload.TYPE,
                GuardListS2CPayload.STREAM_CODEC,
                GuardCommandNetworking::handleGuardList);
    }
}
