package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * C2S, Phase 3D: the client asks the server to Muster every entity id in
 * {@code entityIds} into a {@code shape} (a {@code FormationShape} enum name, e.g.
 * {@code "LINE"}) formation anchored at {@code destination} - three top-level fields
 * (entityIds, destination, shape), comfortably under the 6-field
 * {@code StreamCodec.composite} ceiling since {@link Destination} nests its own four
 * fields into one. Same string-over-enum rationale as every other order-carrying
 * payload in this project: an unrecognized shape name fails soft server-side (falls
 * back to a safe default) instead of desyncing an enum's ordinal positions.
 * <p>
 * {@code destination} is always the sending player's own current position/facing for
 * this pass (see {@code GuardCommandScreen#buildMusterBar}) - see {@code MusterOrder}'s
 * javadoc for why the wire format already supports an arbitrary point regardless.
 */
public record IssueMusterOrderC2SPayload(List<Integer> entityIds, Destination destination, String shape)
        implements CustomPacketPayload {
    public static final Type<IssueMusterOrderC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "issue_muster_order"));

    public static final StreamCodec<FriendlyByteBuf, IssueMusterOrderC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.apply(ByteBufCodecs.list(4096)), IssueMusterOrderC2SPayload::entityIds,
            Destination.STREAM_CODEC, IssueMusterOrderC2SPayload::destination,
            ByteBufCodecs.STRING_UTF8, IssueMusterOrderC2SPayload::shape,
            IssueMusterOrderC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
