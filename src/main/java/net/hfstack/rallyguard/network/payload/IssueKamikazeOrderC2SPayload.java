package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Phase 3E: Kamikaze: the selected (armed) guards plus a target spec string
 * ({@code order.TargetSpec}: NEAREST / ATTACKER / MARKED / PLAYER:&lt;uuid&gt; / NONE).
 * Same shape and fail-soft string convention as {@link IssueOrderC2SPayload}; the server
 * re-resolves the spec itself and never trusts anything else from the client.
 */
public record IssueKamikazeOrderC2SPayload(List<Integer> entityIds, String target) implements CustomPacketPayload {
    public static final Type<IssueKamikazeOrderC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "issue_kamikaze_order"));

    public static final StreamCodec<FriendlyByteBuf, IssueKamikazeOrderC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.apply(ByteBufCodecs.list(4096)), IssueKamikazeOrderC2SPayload::entityIds,
            ByteBufCodecs.STRING_UTF8, IssueKamikazeOrderC2SPayload::target,
            IssueKamikazeOrderC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
