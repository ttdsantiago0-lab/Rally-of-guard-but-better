package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Phase 3H: a commander hotkey was pressed ({@code key} = {@code "ORDER"} or
 * {@code "FIRE"}), with whether the player was sneaking. The client sends nothing else:
 * the server does its own ray cast from the player's eyes and decides what the key means
 * (see {@code order.CommanderHotkeys}), so a modified client can't point at things it
 * can't see. String ids, fail-soft.
 */
public record CommandKeyC2SPayload(String key, boolean sneaking) implements CustomPacketPayload {
    public static final Type<CommandKeyC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "command_key"));

    public static final StreamCodec<FriendlyByteBuf, CommandKeyC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, CommandKeyC2SPayload::key,
            ByteBufCodecs.BOOL, CommandKeyC2SPayload::sneaking,
            CommandKeyC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
