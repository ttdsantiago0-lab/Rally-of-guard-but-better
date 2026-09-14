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
 * Phase 3B added {@code mount}/{@code armor} to {@link Entry} and an
 * {@code OFFLINE_ENTITY_ID} sentinel for a guard the server could only find in its
 * durable {@code GuardRoster} cache rather than as a currently-loaded entity - see
 * {@code GuardRoster}'s own javadoc for why this project stops short of making a
 * roster-only guard actually actionable.
 * <p>
 * Phase 3C adds {@code stance} - but confirmed during Phase 3A that this version's
 * {@code StreamCodec.composite} tops out at 6 fields, and Phase 3B's {@code mount}/
 * {@code armor} addition had already reached that ceiling (4 base fields + those 2 = 6).
 * Rather than hand-roll a >6-field codec, {@code mount} and {@code armor} are nested into
 * their own two-field {@link Condition} sub-record with its own {@code StreamCodec}, which
 * counts as a SINGLE field from the outer {@code Entry} composite's point of view - this
 * frees a slot for {@code stance} while keeping the outer composite at exactly 6 fields
 * again (entityId, name, patrolling, squadName, condition, stance). This nesting trick is
 * the general escape hatch for this ceiling: any FUTURE field beyond this needs another
 * nesting pass (or its own sub-record), not a flat 7th argument to {@code composite}.
 */
public record GuardListS2CPayload(List<Entry> entries) implements CustomPacketPayload {
    /**
     * {@code squadName} is Phase 3A's addition - empty string means "no squad," matching
     * the same blank-means-none convention {@link net.hfstack.rallyguard.network.payload.AssignSquadC2SPayload}
     * uses for its own clear-squad case, so no extra optional-string codec is needed.
     * {@code condition} (mount + armor) is Phase 3B's addition, nested as of Phase 3C to
     * stay under the 6-field composite ceiling - see the class javadoc. {@code stance} is
     * Phase 3C's addition: the guard's {@code Stance} enum name as a plain string (e.g.
     * {@code "DEFENSIVE"}), parsed and localized client-side, same unlocalized-wire-format
     * philosophy as {@code condition.armor()}.
     */
    public record Entry(int entityId, String name, boolean patrolling, String squadName,
                         Condition condition, String stance) {
        /** Sentinel {@link #entityId} for a guard the server only had a durable
         *  {@code GuardRoster} snapshot for, not a currently-loaded entity (Phase 3B) -
         *  a real Minecraft entity id is never negative. */
        public static final int OFFLINE_ENTITY_ID = -1;

        /**
         * Phase 3B's mount/armor pair, nested (Phase 3C) purely to stay under the
         * 6-field {@code StreamCodec.composite} ceiling on the outer {@link Entry} - see
         * the class javadoc. {@code mount} is "" for "not mounted"; {@code armor} is the
         * compact HEAD,CHEST,LEGS,FEET wire string documented on
         * {@code GuardRoster.Entry#armor}.
         */
        public record Condition(String mount, String armor) {
            public static final StreamCodec<FriendlyByteBuf, Condition> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, Condition::mount,
                    ByteBufCodecs.STRING_UTF8, Condition::armor,
                    Condition::new
            );
        }

        public static final StreamCodec<FriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Entry::entityId,
                ByteBufCodecs.STRING_UTF8, Entry::name,
                ByteBufCodecs.BOOL, Entry::patrolling,
                ByteBufCodecs.STRING_UTF8, Entry::squadName,
                Condition.STREAM_CODEC, Entry::condition,
                ByteBufCodecs.STRING_UTF8, Entry::stance,
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
