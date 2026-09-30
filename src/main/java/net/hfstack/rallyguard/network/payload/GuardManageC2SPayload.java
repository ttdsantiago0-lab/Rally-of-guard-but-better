package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Phase 3H: per-guard management by the guard's UUID (so it also works for a guard that
 * isn't loaded): {@code FORGET} (drop a missing/dead guard's row from the Ledger) or
 * {@code RENAME} ({@code argument} = the new name). String ids, fail-soft.
 */
public record GuardManageC2SPayload(String guardId, String action, String argument) implements CustomPacketPayload {
    public static final Type<GuardManageC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "guard_manage"));

    public static final StreamCodec<FriendlyByteBuf, GuardManageC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, GuardManageC2SPayload::guardId,
            ByteBufCodecs.STRING_UTF8, GuardManageC2SPayload::action,
            ByteBufCodecs.STRING_UTF8, GuardManageC2SPayload::argument,
            GuardManageC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
