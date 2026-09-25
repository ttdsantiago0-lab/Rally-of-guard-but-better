package net.hfstack.rallyguard.network.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

/**
 * Phase 3D: a world position plus a facing yaw, shared by every order payload that
 * needs a destination ({@code IssueMusterOrderC2SPayload}, {@code
 * IssueRetreatOrderC2SPayload}) - nested rather than inlined so each of those payloads
 * stays comfortably under the known 6-field {@code StreamCodec.composite} ceiling (see
 * {@code GuardListS2CPayload}'s javadoc for that ceiling and this exact nesting escape
 * hatch, already proven compiling there).
 * <p>
 * Deliberately block/degree integer precision, not {@code double}/{@code float}: this
 * project's existing networking layer only has proven, already-compiling codecs for
 * {@code INT}/{@code BOOL}/{@code STRING_UTF8} ({@link GuardListS2CPayload}, {@code
 * AssignStanceC2SPayload}, etc.) - reusing {@code ByteBufCodecs.INT} here avoids
 * introducing an unverified codec type for a feature that doesn't actually need
 * sub-block position or sub-degree facing precision. {@code facingYawDegrees} is a
 * plain int in {@code [0, 360)}; a formation doesn't need finer aim than that.
 */
public record Destination(int x, int y, int z, int facingYawDegrees) {
    public static final StreamCodec<FriendlyByteBuf, Destination> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Destination::x,
            ByteBufCodecs.INT, Destination::y,
            ByteBufCodecs.INT, Destination::z,
            ByteBufCodecs.INT, Destination::facingYawDegrees,
            Destination::new
    );
}
