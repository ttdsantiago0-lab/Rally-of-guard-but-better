package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * C2S, Phase 3D: the client asks the server to send every entity id in
 * {@code entityIds} to {@code destination} - no formation involved (see
 * {@code RetreatOrder}), structurally the simplest of the new order payloads. Reuses
 * {@link Destination} purely for its position; the facing field is unused by Retreat
 * but not worth a second, position-only record for one unused field.
 */
public record IssueRetreatOrderC2SPayload(List<Integer> entityIds, Destination destination)
        implements CustomPacketPayload {
    public static final Type<IssueRetreatOrderC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "issue_retreat_order"));

    public static final StreamCodec<FriendlyByteBuf, IssueRetreatOrderC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.apply(ByteBufCodecs.list(4096)), IssueRetreatOrderC2SPayload::entityIds,
            Destination.STREAM_CODEC, IssueRetreatOrderC2SPayload::destination,
            IssueRetreatOrderC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
