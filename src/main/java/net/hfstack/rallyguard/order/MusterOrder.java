package net.hfstack.rallyguard.order;

import net.hfstack.rallyguard.command.CommandStructure;
import net.hfstack.rallyguard.command.SlotQueue;
import net.hfstack.rallyguard.command.SquadDirectives;
import net.hfstack.rallyguard.contract.FormationRegistry;
import net.hfstack.rallyguard.formation.Formation;
import net.hfstack.rallyguard.formation.FormationLayout;
import net.hfstack.rallyguard.formation.FormationShape;
import net.hfstack.rallyguard.formation.UnitLayout;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.List;

/**
 * Phase 3D: "converge into this formation, here" - the first concrete consumer of the
 * {@link Formation} engine. Unlike {@code RecallOrder} (an instant teleport, matching
 * the pre-existing per-guard Summon it generalizes), Muster deliberately uses real
 * pathfinding navigation, per the design brief's own instruction ("normal Minecraft
 * navigation moves each guard toward its assigned position... do NOT build custom
 * pathfinding") - guards visibly walk/run into position rather than snapping there.
 * <p>
 * Unlike every other order so far, Muster needs data beyond "which guards" - an anchor
 * position, a facing, and a shape - so it is deliberately NOT registered in
 * {@link OrderRegistry} as a bare string id: {@code GuardCommandNetworking} constructs
 * a fresh instance of this class per invocation, from
 * {@code IssueMusterOrderC2SPayload}'s fields, and calls {@link #execute} directly.
 * {@link OrderExecutor} itself stays exactly as small as it already was;
 * parameterization happens by constructing the order with its data, not by growing the
 * interface - see {@link OrderExecutor}'s own javadoc.
 * <p>
 * Destination v1 limitation: for this pass, the Ledger always sends the PLAYER's own
 * current position/facing as the anchor (see {@code GuardCommandScreen#buildMusterBar})
 * - there is no in-world "mark a destination elsewhere" interaction yet. The wire
 * format ({@code Destination}'s full x/y/z/facingYaw) already supports an arbitrary
 * point though, so a future destination-marking UI is a client-only addition; nothing
 * here or in the payload needs to change for it.
 * <p>
 * Phase 3E: each slot is also made the guard's Guard Villagers patrol post
 * ({@link GuardPosts}), snapped to standable ground. Before this, the single
 * {@code moveTo} was overridden by the next goal that touched navigation (random stroll,
 * walk back to an OLDER post, follow owner), so formations dissolved within seconds.
 * Now the formation is what Guard Villagers' own walk-back AI returns to after a fight,
 * and the guards stop following the commander (Recall makes them follow again).
 * <p>
 * Every guard's assigned slot is recorded in {@link FormationRegistry} as a side
 * effect, which is what makes {@code ReformOrder} possible later - see that registry's
 * own javadoc for why this doesn't make the Order abstraction itself stateful.
 */
public final class MusterOrder implements OrderExecutor {

    private static final double MUSTER_SPEED = 1.0D;

    private final Vec3 anchor;
    private final float facingYaw;
    private final FormationShape shape;
    /** E3B G1: null = the layout stored for the group's unit (FormationRegistry), else DEFAULT. */
    @javax.annotation.Nullable
    private final FormationLayout layout;

    public MusterOrder(Vec3 anchor, float facingYaw, FormationShape shape) {
        this(anchor, facingYaw, shape, null);
    }

    public MusterOrder(Vec3 anchor, float facingYaw, FormationShape shape, @javax.annotation.Nullable FormationLayout layout) {
        this.anchor = anchor;
        this.facingYaw = facingYaw;
        this.shape = shape;
        this.layout = layout;
    }

    /**
     * E3B G1: the layout the group forms with - the given one, else the one stored for the
     * first guard's squad/unit, else DEFAULT; always DEFAULT when {@code groups.layoutEnabled}
     * is off.
     */
    public static FormationLayout layoutFor(ServerLevel level, List<Guard> guards, @javax.annotation.Nullable FormationLayout given) {
        if (!net.hfstack.rallyguard.config.RallyConfig.on(net.hfstack.rallyguard.config.RallyConfig.LAYOUT_ENABLED)) {
            return FormationLayout.DEFAULT;
        }
        if (given != null) return given;
        if (guards.isEmpty()) return FormationLayout.DEFAULT;
        Guard first = guards.get(0);
        java.util.UUID owner = net.hfstack.rallyguard.contract.GuardOwnership.getOwner(first);
        java.util.Optional<String> path = net.hfstack.rallyguard.contract.SquadRegistry.get(level).pathOf(first.getUUID());
        if (owner == null || path.isEmpty()) return FormationLayout.DEFAULT;
        return FormationRegistry.get(level).unitLayout(owner, path.get());
    }

    /**
     * E2A block 1: the group is split into subunits by command capacity
     * ({@link CommandStructure#partition}); each subunit forms the shape around its own
     * anchor ({@link UnitLayout}), gets one pushed {@link SquadDirectives.Directive}
     * naming its captain, and its slots are recorded at once but handed out over the next
     * ticks ({@link SlotQueue}) instead of 200 posts and paths on one tick. Slot 0 of each
     * subunit (its front) goes to its captain.
     */
    @Override
    public void execute(ServerPlayer player, List<Guard> guards) {
        ServerLevel level = player.serverLevel();
        Vec3 anchor = GuardPosts.groundAnchor(level, this.anchor);
        FormationLayout chosen = layoutFor(level, guards, layout);
        Formation formation = new Formation(shape, Formation.DEFAULT_SPACING, chosen);
        FormationRegistry registry = FormationRegistry.get(level);

        net.hfstack.rallyguard.force.UnitOrders.notePlayerOrder(guards); // E3A A2: player's order (precedence)
        List<CommandStructure.Subunit> units = CommandStructure.partition(guards);
        int[] sizes = new int[units.size()];
        for (int k = 0; k < sizes.length; k++) sizes[k] = units.get(k).members().size();
        List<Vec3> unitAnchors = UnitLayout.anchors(shape, chosen.spacing(Formation.DEFAULT_SPACING), anchor, facingYaw, sizes);
        long now = level.getGameTime();

        for (int k = 0; k < units.size(); k++) {
            CommandStructure.Subunit unit = units.get(k);
            Vec3 unitAnchor = k == 0 ? anchor : GuardPosts.groundAnchor(level, unitAnchors.get(k));
            List<Guard> members = unit.members();
            List<Vec3> positions = formation.positions(unitAnchor, facingYaw, members.size());
            SquadDirectives.push(members, new SquadDirectives.Directive(SquadDirectives.Kind.MUSTER, unitAnchor,
                    facingYaw, unit.captain().getUUID(), now, k, units.size()));
            // E3B G1: with a layout, the toughest guards (armor, shield, melee) take the most exposed
            // slots (front, loaded zone) and shooters the rear ones; the default keeps slot 0 = captain.
            List<Guard> ordered = new java.util.ArrayList<>(members);
            if (!chosen.isDefault()) {
                ordered.sort(java.util.Comparator.comparingDouble((Guard g) -> -net.hfstack.rallyguard.command.GroupProfile.toughness(g))
                        .thenComparing(Guard::getUUID));
            }
            List<Integer> slotOrder = formation.exposureOrder(members.size());
            for (int i = 0; i < ordered.size(); i++) {
                Guard guard = ordered.get(i);
                int slot = slotOrder.get(i);
                // Phase 3E: the slot becomes the guard's Guard Villagers post (dismount +
                // stop following included), so the formation HOLDS - see GuardPosts.
                registry.setSlot(guard.getUUID(), new FormationRegistry.Slot(
                        unitAnchor, facingYaw, shape, Formation.DEFAULT_SPACING, slot, members.size(), chosen));
                SlotQueue.enqueue(guard, positions.get(slot), MUSTER_SPEED);
            }
        }
    }
}
