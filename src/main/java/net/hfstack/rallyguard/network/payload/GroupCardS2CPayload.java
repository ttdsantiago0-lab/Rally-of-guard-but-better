package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * E3B G4: the group card of the commander's current selection, as one "|"-separated line
 * ({@code command.GroupCards#card}): identity, consistency, second archetype, morale, cohesion,
 * warnings, dominant archetype, size, instruction, density, load, morale state and its main
 * cause. "" = no card (fewer than 2 guards selected).
 */
public record GroupCardS2CPayload(String card) implements CustomPacketPayload {
    public static final int MAX = 2048;
    public static final Type<GroupCardS2CPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "group_card"));

    public static final StreamCodec<FriendlyByteBuf, GroupCardS2CPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(MAX), GroupCardS2CPayload::card,
            GroupCardS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
