package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** E3A A4: one Battle Planner action (refresh, select, doctrine, role, op, execute, cancel, save, load, delete) + JSON args. */
public record PlannerActionC2SPayload(String action, String json) implements CustomPacketPayload {
    public static final Type<PlannerActionC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "planner_action"));

    public static final StreamCodec<FriendlyByteBuf, PlannerActionC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(64), PlannerActionC2SPayload::action,
            ByteBufCodecs.stringUtf8(32768), PlannerActionC2SPayload::json,
            PlannerActionC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
