package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * C2S, Phase 3D: the client asks the server to run the order named {@code orderId}
 * (an {@code OrderRegistry} key, e.g. {@code "RECALL"}) against every entity id in
 * {@code entityIds} at once - the Commander's Ledger's multi-select "issue order" action,
 * structurally identical to {@link AssignStanceC2SPayload} (same {@code List<Integer>}
 * shape, same per-guard ownership re-check server-side, same "batch acts on whatever was
 * selected" design, same rationale for a String id over an enum ordinal: an unrecognized
 * id fails soft server-side instead of desyncing across versions).
 * <p>
 * Deliberately has NO target field (entity/position) in this first pass - neither of the
 * two orders implemented so far (Recall, Hold) needs one. Per the project's "don't add
 * speculative APIs unless genuinely needed now" rule, a target isn't designed here yet;
 * when an order that needs one is implemented later (Attack, Muster, Kamikaze), the
 * established escape hatch for growing past a flat field list - nesting the extra data
 * into its own sub-record, the same way {@code GuardListS2CPayload.Entry.Condition}
 * already does - is how it will be added, without breaking this payload's existing wire
 * shape for orders that still don't need a target.
 */
public record IssueOrderC2SPayload(List<Integer> entityIds, String orderId) implements CustomPacketPayload {
    public static final Type<IssueOrderC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "issue_order"));

    public static final StreamCodec<FriendlyByteBuf, IssueOrderC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.apply(ByteBufCodecs.list(4096)), IssueOrderC2SPayload::entityIds,
            ByteBufCodecs.STRING_UTF8, IssueOrderC2SPayload::orderId,
            IssueOrderC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
