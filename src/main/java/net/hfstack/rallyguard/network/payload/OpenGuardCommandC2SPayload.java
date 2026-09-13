package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * C2S, no fields: the client asks the server for its current list of owned guards, so it
 * can open the Commander's Ledger GUI (Phase 2E) once {@link GuardListS2CPayload} comes
 * back. {@code CustomPacketPayload}/{@code Type}/{@code StreamCodec} shape verified
 * against Guard Villagers' own real, compiling NeoForge 1.21.1 networking source (see
 * PORTING_PLAN.md section 6.2/15) - the SimpleChannel/NetworkRegistry system this
 * replaces no longer exists on NeoForge 1.21.1. This is this project's first zero-field
 * payload, so {@code StreamCodec.unit(...)} (confirmed via the official NeoForged 1.21.x
 * networking docs, since Guard Villagers itself has no zero-field payload to copy)
 * replaces the field-carrying {@code StreamCodec.composite(...)} pattern for this one
 * case.
 */
public record OpenGuardCommandC2SPayload() implements CustomPacketPayload {
    public static final Type<OpenGuardCommandC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "open_guard_command"));

    public static final StreamCodec<FriendlyByteBuf, OpenGuardCommandC2SPayload> STREAM_CODEC =
            StreamCodec.unit(new OpenGuardCommandC2SPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
