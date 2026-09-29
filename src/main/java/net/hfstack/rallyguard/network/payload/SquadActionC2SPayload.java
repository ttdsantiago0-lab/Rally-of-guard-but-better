package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Phase 3G: squad-level management from the Ledger's Squads tab - an action on a whole
 * squad by NAME, not on a list of loaded entities, so it also covers members that are
 * far away and unloaded. {@code action}: {@code RENAME} ({@code argument} = the new
 * name) or {@code DISBAND}. String ids, fail-soft, same convention as every other payload.
 */
public record SquadActionC2SPayload(String squadName, String action, String argument) implements CustomPacketPayload {
    public static final Type<SquadActionC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "squad_action"));

    public static final StreamCodec<FriendlyByteBuf, SquadActionC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SquadActionC2SPayload::squadName,
            ByteBufCodecs.STRING_UTF8, SquadActionC2SPayload::action,
            ByteBufCodecs.STRING_UTF8, SquadActionC2SPayload::argument,
            SquadActionC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
