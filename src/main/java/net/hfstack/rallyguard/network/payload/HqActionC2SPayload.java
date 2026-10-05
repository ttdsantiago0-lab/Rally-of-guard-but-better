package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * E1-C: HQ management from the Ledger. {@code action}: CREATE (argument = name; at the
 * commander's position), RENAME (argument = name), DELETE, SELECT (the HQ a sneak-commanded
 * banner links to; empty hqId = none), ASSIGN (argument = squad name). String ids, fail-soft.
 */
public record HqActionC2SPayload(String action, String hqId, String argument) implements CustomPacketPayload {
    public static final Type<HqActionC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "hq_action"));

    public static final StreamCodec<FriendlyByteBuf, HqActionC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, HqActionC2SPayload::action,
            ByteBufCodecs.STRING_UTF8, HqActionC2SPayload::hqId,
            ByteBufCodecs.STRING_UTF8, HqActionC2SPayload::argument,
            HqActionC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
