package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Phase 3H: the Ledger's selection changed. The server keeps it as the commander's
 * selection ({@code order.CommanderSelection}), which the hotkeys act on and which is
 * marked over each selected guard in the world. Entity ids are re-resolved server-side
 * (owned, loaded guards only), same as every order payload.
 */
public record SetSelectionC2SPayload(List<Integer> entityIds) implements CustomPacketPayload {
    public static final Type<SetSelectionC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "set_selection"));

    public static final StreamCodec<FriendlyByteBuf, SetSelectionC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.apply(ByteBufCodecs.list(4096)), SetSelectionC2SPayload::entityIds,
            SetSelectionC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
