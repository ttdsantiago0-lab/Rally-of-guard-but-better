package net.hfstack.rallyguard.client;

import net.hfstack.rallyguard.network.payload.OpenGuardCommandC2SPayload;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Thin client-side entry point used by common-side code ({@code CommandersLedgerItem.use()})
 * to trigger a client-&gt;server request without embedding networking calls directly in
 * the item class - matches this project's own architecture plan (PORTING_PLAN.md section
 * 2.1/5: "client/ClientHooks.java ... just sends a packet via the new payload API").
 * <p>
 * Only ever invoked from behind a {@code level.isClientSide()} guard (see
 * {@code CommandersLedgerItem.use()}), so it is safe for this class to exist in the same
 * jar a dedicated server loads - this project doesn't split client/common source sets
 * (section 2.1), and a dedicated server simply never calls into this method. The Fabric
 * port reached its own equivalent through reflection (Class.forName(...)) - not needed
 * here: the Forge 1.20.1 donor already proves a direct guarded call is safe on Forge/
 * NeoForge (its {@code CommandersLedgerItem.use()} calls
 * {@code NetworkBootstrap.CHANNEL.sendToServer(...)} inline, no reflection), so this
 * class is called directly the same way.
 * <p>
 * {@code PacketDistributor.sendToServer(...)} - the same client-&gt;server call shape
 * Guard Villagers' own {@code GuardInventoryScreen.java} uses for its follow/patrol
 * buttons (verified real source, not guessed).
 */
public final class ClientHooks {
    private ClientHooks() {
    }

    public static void requestOpenGuardPanel() {
        PacketDistributor.sendToServer(new OpenGuardCommandC2SPayload());
    }
}
