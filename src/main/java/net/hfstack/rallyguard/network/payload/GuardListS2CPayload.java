package net.hfstack.rallyguard.network.payload;

import net.hfstack.rallyguard.RallyOfTheGuard;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * S2C: the server's answer to {@link OpenGuardCommandC2SPayload} - every guard the
 * requesting player owns, by entity id/name/current patrol state, for the Commander's
 * Ledger GUI (Phase 2E) to list.
 * <p>
 * {@code entries}' element codec is turned into a {@code StreamCodec<FriendlyByteBuf,
 * List<Entry>>} via {@code Entry.STREAM_CODEC.apply(ByteBufCodecs.list(maxSize))} -
 * confirmed via the official NeoForged 1.21.x networking docs, since Guard Villagers
 * itself never sends a list over the network. The Fabric port hand-rolled this same loop
 * with a raw anonymous {@code PacketCodec<>() { ... }}; NeoForge's {@code StreamCodec}
 * already has the list-of-records case built in, so no anonymous class is needed here.
 */
public record GuardListS2CPayload(List<Entry> entries) implements CustomPacketPayload {
    /**
     * {@code squadName} is Phase 3A's addition - empty string means "no squad," matching
     * the same blank-means-none convention {@link net.hfstack.rallyguard.network.payload.AssignSquadC2SPayload}
     * uses for its own clear-squad case, so no extra optional-string codec is needed.
     */
    public record Entry(int entityId, String name, boolean patrolling, String squadName) {
        public static final StreamCodec<FriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Entry::entityId,
                ByteBufCodecs.STRING_UTF8, Entry::name,
                ByteBufCodecs.BOOL, Entry::patrolling,
                ByteBufCodecs.STRING_UTF8, Entry::squadName,
                Entry::new
        );
    }

    public static final Type<GuardListS2CPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "guard_list"));

    public static final StreamCodec<FriendlyByteBuf, GuardListS2CPayload> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list(4096)), GuardListS2CPayload::entries,
            GuardListS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
