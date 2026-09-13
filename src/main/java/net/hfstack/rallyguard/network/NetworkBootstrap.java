package net.hfstack.rallyguard.network;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.hfstack.rallyguard.network.payload.GuardActionC2SPayload;
import net.hfstack.rallyguard.network.payload.GuardListS2CPayload;
import net.hfstack.rallyguard.network.payload.OpenGuardCommandC2SPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers Rally of the Guard's three Commander's Ledger network payloads.
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

        registrar.playToClient(
                GuardListS2CPayload.TYPE,
                GuardListS2CPayload.STREAM_CODEC,
                GuardCommandNetworking::handleGuardList);
    }
}
