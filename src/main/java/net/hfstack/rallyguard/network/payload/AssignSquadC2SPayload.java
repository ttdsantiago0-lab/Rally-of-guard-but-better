package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * C2S, Phase 3A: the client asks the server to assign (or clear, when
 * {@code squadName} is blank) a squad for every entity id in {@code entityIds} at once -
 * the Commander's Ledger's multi-select "Assign to squad" action. Ownership of each
 * guard is re-checked server-side in {@code GuardCommandNetworking} exactly like every
 * other action on this channel; nothing here is trusted client input beyond "these are
 * the entity ids the player had checked."
 * <p>
 * {@code entityIds} reuses the same {@code StreamCodec.composite}/{@code
 * ByteBufCodecs.list(...)} shape already proven by {@link GuardListS2CPayload}'s own
 * {@code entries} field, just with a plain {@code ByteBufCodecs.INT} element instead of
 * a record - {@code StreamCodec#apply(...)} works the same way for either.
 */
public record AssignSquadC2SPayload(List<Integer> entityIds, String squadName) implements CustomPacketPayload {
    public static final Type<AssignSquadC2SPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "assign_squad"));

    public static final StreamCodec<FriendlyByteBuf, AssignSquadC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT.apply(ByteBufCodecs.list(4096)), AssignSquadC2SPayload::entityIds,
            ByteBufCodecs.STRING_UTF8, AssignSquadC2SPayload::squadName,
            AssignSquadC2SPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
