package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * C2S: the client asks the server to run an action (summon / toggle patrol - see
 * {@link net.hfstack.rallyguard.network.NetworkConstants}) on a specific owned guard,
 * identified by its runtime entity id. Field codec shape ({@code ByteBufCodecs.INT} x2,
 * {@code StreamCodec.composite}) matches Guard Villagers' own real
 * {@code GuardFollowPacket}/{@code GuardSetPatrolPosPacket} exactly (verified source,
 * not guessed).
 */
public record GuardActionC2SPayload(int entityId, int action) implements CustomPacketPayload {
    public static final Type<GuardActionC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "guard_action"));

    public static final StreamCodec<FriendlyByteBuf, GuardActionC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, GuardActionC2SPayload::entityId,
            ByteBufCodecs.INT, GuardActionC2SPayload::action,
            GuardActionC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
