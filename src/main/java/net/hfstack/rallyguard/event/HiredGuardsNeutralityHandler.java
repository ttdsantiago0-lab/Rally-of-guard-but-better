package net.hfstack.rallyguard.event;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Phase 2H, part 2: when a player hits a guard that ISN'T theirs (someone else's, or a
 * wild/unowned one), their OWN nearby hired guards should not turn around and start
 * attacking them for it. Ported from the Forge 1.20.1 donor's
 * {@code HiredGuardsNeutralityHandler}.
 * <p>
 * Why this is needed at all (not obvious without reading Guard Villagers' own source -
 * see PORTING_PLAN.md section 17.1/19 for the general policy of verifying against real
 * source rather than guessing): {@code HurtByTargetGoal(this, Guard.class,
 * IronGolem.class).setAlertOthers()} on every {@code Guard} means that when ANY guard is
 * hurt, nearby allied guards get alerted to target whoever did the hurting. Guard
 * Villagers does not scope that alert by owner - it is keyed on team/alliance, not on
 * "is this specifically my owner's business" - so a player attacking an unrelated guard
 * can cause the player's OWN hired guards, standing nearby, to get alerted and target the
 * player (their own owner) as the attacker. This handler proactively clears that target
 * and gives the owner a grace window during which their own guards won't re-acquire them.
 * <p>
 * Forge -&gt; NeoForge translation, verified against the real NeoForge 1.21.1 source tree:
 * <p>
 * 1. {@code net.minecraftforge.event.entity.player.AttackEntityEvent} -&gt;
 * {@code net.neoforged.neoforge.event.entity.player.AttackEntityEvent} (package move
 * only, same shape - see {@code RallyFriendlyFireHandler}'s javadoc for the same note).
 * <p>
 * 2. {@code net.minecraftforge.event.TickEvent.LevelTickEvent} (gated on
 * {@code event.phase == TickEvent.Phase.END}) -&gt;
 * {@code net.neoforged.neoforge.event.tick.LevelTickEvent.Post}, a dedicated end-of-tick
 * event class in NeoForge 1.21.1 (confirmed present in the real NeoForge source tree,
 * alongside a {@code .Pre} sibling) - no phase check needed, using {@code .Post} directly
 * expresses the same "after the level finishes its tick" timing the donor's phase check
 * achieved manually.
 * <p>
 * 3. The donor looked up the guard entity type by {@code ResourceLocation} against
 * {@code ForgeRegistries.ENTITY_TYPES} and swept a hand-built {@code AABB} with
 * {@code getEntitiesOfClass(Entity.class, area, predicate)}, then had to
 * {@code instanceof Mob} each result to call {@code setTarget}/{@code setAggressive}. This
 * project's typed API policy (section 4.2) replaces all of that: {@code Guard.class} is a
 * real compile dependency (no registry lookup needed), and the nearby-guard sweep reuses
 * this project's established bounds-free {@code ServerLevel#getEntities(EntityTypeTest,
 * Predicate)} pattern (first adopted in Phase 2E's Sable-AABB fix, section 17.1, and
 * reused by {@code ScrollOfRallyingItem}) with the distance check folded into the
 * predicate - the results come back already typed as {@code Guard}, so
 * {@code setTarget}/{@code setAggressive}/{@code getNavigation} are called directly with
 * no {@code instanceof Mob} cast needed.
 * <p>
 * The in-memory {@code TRACK} map (entity id + dimension -&gt; owner + remaining ticks) is
 * kept exactly as the donor designed it - a static map is not persisted and does not need
 * to be; a grace window that resets on server restart is the correct behavior here, not a
 * shortcut.
 */
public final class HiredGuardsNeutralityHandler {

    private static final int IGNORE_TICKS = 200; // ~10s at 20 ticks/sec
    private static final double NEUTRALIZE_RADIUS = 24.0D;
    private static final double NEUTRALIZE_RADIUS_SQ = NEUTRALIZE_RADIUS * NEUTRALIZE_RADIUS;

    private static final Map<String, Entry> TRACK = new HashMap<>();

    private record Entry(UUID owner, int ticks) {
    }

    @SubscribeEvent
    public void onPlayerAttackEntity(AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        Player player = event.getEntity();
        Entity target = event.getTarget();

        if (!GuardOwnership.isGuard(target)) return;
        if (GuardOwnership.isOwnedBy(target, player.getUUID())) return; // your own guard - not this handler's job
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        List<? extends Guard> myGuards = serverLevel.getEntities(EntityTypeTest.forClass(Guard.class),
                guard -> GuardOwnership.isOwnedBy(guard, player.getUUID())
                        && guard.distanceToSqr(player) <= NEUTRALIZE_RADIUS_SQ);

        for (Guard guard : myGuards) {
            track(serverLevel, guard.getId(), player.getUUID(), IGNORE_TICKS);
            clearIfTargeting(guard, player.getUUID());
        }
        // Does not cancel the player's own attack on the other guard - only protects the
        // attacker's own hired guards from retaliating, same as the donor.
    }

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
        if (TRACK.isEmpty()) return;

        String dimensionKey = serverLevel.dimension().location().toString();
        List<String> expired = new ArrayList<>();

        for (Map.Entry<String, Entry> trackedEntry : TRACK.entrySet()) {
            String key = trackedEntry.getKey();
            if (!key.startsWith(dimensionKey + "#")) continue;

            Entry value = trackedEntry.getValue();
            int entityId = parseEntityId(key);
            Entity entity = entityId >= 0 ? serverLevel.getEntity(entityId) : null;

            if (!(entity instanceof Guard guard) || !GuardOwnership.isOwnedBy(guard, value.owner())) {
                expired.add(key);
                continue;
            }

            clearIfTargeting(guard, value.owner());

            int remaining = value.ticks() - 1;
            if (remaining <= 0) {
                expired.add(key);
            } else {
                TRACK.put(key, new Entry(value.owner(), remaining));
            }
        }

        expired.forEach(TRACK::remove);
    }

    private static void clearIfTargeting(Guard guard, UUID ownerId) {
        var target = guard.getTarget();
        if (target != null && ownerId.equals(target.getUUID())) {
            guard.setTarget(null);
            guard.setAggressive(false);
            guard.getNavigation().stop();
        }
    }

    private static void track(ServerLevel level, int entityId, UUID owner, int ticks) {
        TRACK.put(key(level, entityId), new Entry(owner, ticks));
    }

    private static String key(ServerLevel level, int entityId) {
        return level.dimension().location().toString() + "#" + entityId;
    }

    private static int parseEntityId(String key) {
        int separator = key.lastIndexOf('#');
        if (separator < 0 || separator + 1 >= key.length()) return -1;
        try {
            return Integer.parseInt(key.substring(separator + 1));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
