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
 * <p>
 * Phase 3B adds {@code mount}/{@code armor} to {@link Entry} and changes what
 * {@code entityId} means for a guard the server could only find in its durable
 * {@code GuardRoster} cache rather than as a currently-loaded entity: {@code entityId}
 * is {@code -1} ({@link Entry#OFFLINE_ENTITY_ID}) for those, and the client
 * ({@code GuardCommandScreen}) disables anything that would need a live entity
 * (selection, Summon, Patrol toggle) for that row - see {@code GuardRoster}'s own
 * javadoc for why this project stops short of making a roster-only guard actually
 * actionable. This brings {@link Entry} to 6 fields total - confirmed against
 * mappings.dev during Phase 3A that this version's {@code StreamCodec.composite} tops
 * out at 6, so this is the most fields one {@code Entry} can carry without a hand-rolled
 * codec.
 */
public record GuardListS2CPayload(List<Entry> entries) implements CustomPacketPayload {
    /**
     * {@code squadName} is Phase 3A's addition - empty string means "no squad," matching
     * the same blank-means-none convention {@link net.hfstack.rallyguard.network.payload.AssignSquadC2SPayload}
     * uses for its own clear-squad case, so no extra optional-string codec is needed.
     * {@code mount} is Phase 3B's addition, same blank-means-none convention (see
     * {@code GuardOwnership#describeMount}). {@code armor} is also Phase 3B's addition -
     * see {@code GuardRoster.Entry#armor} for its compact wire format, parsed and
     * localized client-side rather than sent as a pre-formatted sentence.
     */
    public record Entry(int entityId, String name, boolean patrolling, String squadName, String mount, String armor) {
        /** Sentinel {@link #entityId} for a guard the server only had a durable
         *  {@code GuardRoster} snapshot for, not a currently-loaded entity (Phase 3B) -
         *  a real Minecraft entity id is never negative, so this can't collide. */
        public static final int OFFLINE_ENTITY_ID = -1;

        public static final StreamCodec<FriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Entry::entityId,
                ByteBufCodecs.STRING_UTF8, Entry::name,
                ByteBufCodecs.BOOL, Entry::patrolling,
                ByteBufCodecs.STRING_UTF8, Entry::squadName,
                ByteBufCodecs.STRING_UTF8, Entry::mount,
                ByteBufCodecs.STRING_UTF8, Entry::armor,
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
