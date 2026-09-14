package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * C2S, Phase 3C: the client asks the server to set every entity id in {@code entityIds}
 * to {@code stance} (a {@code Stance} enum name, e.g. {@code "AGGRESSIVE"}) at once - the
 * Commander's Ledger's multi-select "Set stance" action, structurally identical to
 * {@link AssignSquadC2SPayload} (same {@code List<Integer>} shape, same per-guard
 * ownership re-check server-side, same "batch acts on whatever was selected" design).
 * A string rather than an ordinal/byte is used for {@code stance} so an unrecognized
 * value (an older/newer client, or a future Kamikaze addition talking to an older server)
 * fails soft server-side (falls back to a safe default) instead of desyncing an enum's
 * ordinal positions across versions.
 */
public record AssignStanceC2SPayload(List<Integer> entityIds, String stance) implements CustomPacketPayload {
    public static final Type<AssignStanceC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "assign_stance"));

    public static final StreamCodec<FriendlyByteBuf, AssignStanceC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.apply(ByteBufCodecs.list(4096)), AssignStanceC2SPayload::entityIds,
            ByteBufCodecs.STRING_UTF8, AssignStanceC2SPayload::stance,
            AssignStanceC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
