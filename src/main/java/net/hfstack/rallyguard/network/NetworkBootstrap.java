package net.hfstack.rallyguard.network;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.hfstack.rallyguard.network.payload.AssignSquadC2SPayload;
import net.hfstack.rallyguard.network.payload.AssignStanceC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardActionC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardListS2CPayload;
import net.hfstack.rallyguard.network.payload.IssueMusterOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.IssueRetreatOrderC2SPayload;
import net.hfstack.rallyguard.network.payload.OpenGuardCommandC2SPayload;
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
    private static final String PROTOCOL_VERSION = "1";

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
                IssueRetreatOrderC2SPayload.TYPE,
                IssueRetreatOrderC2SPayload.STREAM_CODEC,
                GuardCommandNetworking::handleIssueRetreatOrder);

        registrar.playToClient(
                GuardListS2CPayload.TYPE,
                GuardListS2CPayload.STREAM_CODEC,
                GuardCommandNetworking::handleGuardList);
    }
}
