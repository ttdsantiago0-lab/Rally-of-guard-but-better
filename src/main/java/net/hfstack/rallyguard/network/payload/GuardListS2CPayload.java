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
 * <p>
 * Phase 3E applies exactly that escape hatch: the 6th field became a {@link
 * Entry.Status} sub-record (stance + hold fire + designated target name), and the
 * payload itself gained the commander's target options (what the Ledger's target
 * selector can cycle through - ids only mean something to the server's {@code
 * TargetSpec}) and the current Muster/Retreat anchor ("" = the commander's own position,
 * otherwise the position marked with the Ledger, as "x y z").
 */
public record GuardListS2CPayload(List<Entry> entries, List<TargetOption> targets, Extras extras,
                                  List<Integer> selectedIds, List<SquadInfo> squads, int omitted)
        implements CustomPacketPayload {

    /** E1-C: one HQ of the commander ({@code pos} "x y z", {@code squads} = the squads using it). */
    public record HqInfo(String id, String name, String pos, String dimension, String squads) {
        public static final StreamCodec<FriendlyByteBuf, HqInfo> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, HqInfo::id,
                ByteBufCodecs.STRING_UTF8, HqInfo::name,
                ByteBufCodecs.STRING_UTF8, HqInfo::pos,
                ByteBufCodecs.STRING_UTF8, HqInfo::dimension,
                ByteBufCodecs.STRING_UTF8, HqInfo::squads,
                HqInfo::new
        );
    }

    /** E1: the marked anchor ("" = none), the commander's HQs and the HQ picked in the
     *  Ledger ("" = none) - nested to stay under the 6-field composite ceiling. */
    public record Extras(String anchor, List<HqInfo> hqs, String selectedHq, int batch, int batches) {
        // E1-E: batch / batches = which server batch of rows this is (armies bigger than
        // one batch of ledgerPageSize rows).
        public static final StreamCodec<FriendlyByteBuf, Extras> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, Extras::anchor,
                HqInfo.STREAM_CODEC.apply(ByteBufCodecs.list(1024)), Extras::hqs,
                ByteBufCodecs.STRING_UTF8, Extras::selectedHq,
                ByteBufCodecs.VAR_INT, Extras::batch,
                ByteBufCodecs.VAR_INT, Extras::batches,
                Extras::new
        );
    }

    /** Most rows one Ledger list carries (nearest first). Phase 3H.1 scale: the list used
     *  to hold every guard, and past 4096 the codec refused to encode it - the Ledger
     *  would have broken the connection for an army that big. The rest is counted in
     *  {@link #omitted}; squad totals ({@link #squads}) still cover everyone. */
    public static final int MAX_ROWS = 2000;

    /** Phase 3H.1: one squad or "Squad/Unit" with totals computed server-side over the
     *  WHOLE army (so they stay right even when rows are omitted). {@code hq} is
     *  "x y z" or "". */
    public record SquadInfo(String path, int total, int online, int nearest, String hq) {
        public static final StreamCodec<FriendlyByteBuf, SquadInfo> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, SquadInfo::path,
                ByteBufCodecs.VAR_INT, SquadInfo::total,
                ByteBufCodecs.VAR_INT, SquadInfo::online,
                ByteBufCodecs.VAR_INT, SquadInfo::nearest,
                ByteBufCodecs.STRING_UTF8, SquadInfo::hq,
                SquadInfo::new
        );
    }
    // Phase 3H: selectedIds = the commander's current selection (server-side since 3H, so
    // the in-world sneak-select, the hotkeys and the Ledger all share one selection).

    /** One entry of the Ledger's target selector: a {@code TargetSpec} id plus the
     *  entity/player name the client shows next to its localized label ("" if none). */
    public record TargetOption(String id, String name) {
        public static final StreamCodec<FriendlyByteBuf, TargetOption> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, TargetOption::id,
                ByteBufCodecs.STRING_UTF8, TargetOption::name,
                TargetOption::new
        );
    }

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
                         Condition condition, Status status) {
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
        public record Condition(String mount, String armor, String guardId, String fireMode, String pack,
                                String command) {
            // Phase 3H: guardId (the guard's UUID) rides along here - the only identity an
            // unloaded (offline) row has, needed to Forget or Rename it.
            // E1: fireMode (combat.FireMode name) and pack ("used/6 slots:ammo items", "" = unknown).
            public static final StreamCodec<FriendlyByteBuf, Condition> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, Condition::mount,
                    ByteBufCodecs.STRING_UTF8, Condition::armor,
                    ByteBufCodecs.STRING_UTF8, Condition::guardId,
                    ByteBufCodecs.STRING_UTF8, Condition::fireMode,
                    ByteBufCodecs.STRING_UTF8, Condition::pack,
                    // E2A: command ("rank|captain 0/1|configured rank name", "" = unknown).
                    ByteBufCodecs.STRING_UTF8, Condition::command,
                    Condition::new
            );
        }

        /**
         * Phase 3E: the guard's standing orders - {@code stance} (Phase 3C's field, moved
         * in here), whether it is under HOLD FIRE, and the display name of its designated
         * target ("" = none) - plus where it is relative to the commander: {@code
         * distance} in blocks (-1 = unknown) and {@code dimension} ("" = same dimension as
         * the commander, otherwise that dimension's id). Nested for the same 6-field
         * reason as {@link Condition}.
         */
        public record Status(String stance, boolean holdFire, String target, int distance, String dimension,
                             String ward) {
            // Phase 3H: ward = display name of the entity this guard DEFENDS ("" = none).
            // This record is now at the 6-field composite ceiling.
            public static final StreamCodec<FriendlyByteBuf, Status> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, Status::stance,
                    ByteBufCodecs.BOOL, Status::holdFire,
                    ByteBufCodecs.STRING_UTF8, Status::target,
                    ByteBufCodecs.INT, Status::distance,
                    ByteBufCodecs.STRING_UTF8, Status::dimension,
                    ByteBufCodecs.STRING_UTF8, Status::ward,
                    Status::new
            );
        }

        public static final StreamCodec<FriendlyByteBuf, Entry> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT, Entry::entityId,
                ByteBufCodecs.STRING_UTF8, Entry::name,
                ByteBufCodecs.BOOL, Entry::patrolling,
                ByteBufCodecs.STRING_UTF8, Entry::squadName,
                Condition.STREAM_CODEC, Entry::condition,
                Status.STREAM_CODEC, Entry::status,
                Entry::new
        );
    }

    public static final Type<GuardListS2CPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RallyOfTheGuard.MOD_ID, "guard_list"));

    public static final StreamCodec<FriendlyByteBuf, GuardListS2CPayload> STREAM_CODEC = StreamCodec.composite(
            Entry.STREAM_CODEC.apply(ByteBufCodecs.list(MAX_ROWS)), GuardListS2CPayload::entries,
            TargetOption.STREAM_CODEC.apply(ByteBufCodecs.list(64)), GuardListS2CPayload::targets,
            Extras.STREAM_CODEC, GuardListS2CPayload::extras,
            ByteBufCodecs.INT.apply(ByteBufCodecs.list(MAX_ROWS)), GuardListS2CPayload::selectedIds,
            SquadInfo.STREAM_CODEC.apply(ByteBufCodecs.list(4096)), GuardListS2CPayload::squads,
            ByteBufCodecs.VAR_INT, GuardListS2CPayload::omitted,
            GuardListS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
