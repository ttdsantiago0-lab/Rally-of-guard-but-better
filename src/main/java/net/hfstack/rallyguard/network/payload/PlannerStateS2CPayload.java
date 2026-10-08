package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** E3A A4: the Battle Planner's state (forces, units, HQs, known enemies, plans, warnings) as JSON; opens or refreshes it. */
public record PlannerStateS2CPayload(String json, boolean open) implements CustomPacketPayload {
    public static final int MAX_JSON = 262144;
    public static final Type<PlannerStateS2CPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "planner_state"));

    public static final StreamCodec<FriendlyByteBuf, PlannerStateS2CPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(MAX_JSON), PlannerStateS2CPayload::json,
            ByteBufCodecs.BOOL, PlannerStateS2CPayload::open,
            PlannerStateS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
