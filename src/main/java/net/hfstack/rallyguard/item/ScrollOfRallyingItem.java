package net.hfstack.rallyguard.item;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.order.MountDriver;
import net.hfstack.rallyguard.effect.ModEffects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import tallestegg.guardvillagers.common.entities.Guard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Scroll of Rallying, ported from the Forge 1.20.1 donor's {@code use(...)} (shift +
 * right-click toggles a mass "rally": gathers nearby non-patrolling owned guards into a
 * fan formation around the player and sets them following; using it again while active
 * releases them). Two deliberate departures from the Forge/Fabric donors, consistent
 * with this project's established policy (PORTING_PLAN.md section 4.2, applied since
 * Phase 2C):
 * <p>
 * 1. Guard state (following, patrolling) is read/written through Guard Villagers' own
 * typed API ({@code Guard#isFollowing()/setFollowing(boolean)/isPatrolling()}) instead of
 * the donors' {@code saveWithoutId(...)}/{@code load(...)} NBT round-trip, and the dead
 * {@code rallyguard:in_rally} NBT key (already found to be a no-op - PORTING_PLAN.md
 * section 4.2 point 4) is dropped rather than ported forward, same as Phase 2C/2D/2E.
 * <p>
 * 2. The owned-guard lookup uses {@code ServerLevel#getEntities(EntityTypeTest, Predicate)}
 * with a distance check folded into the predicate, instead of {@code getEntitiesOfClass}
 * with an {@code AABB} - the same fix applied in Phase 2E (PORTING_PLAN.md section 17.1)
 * after the Sable mod was found aborting a much larger (world-wide) AABB query. A 100-block
 * box is nowhere near the size that tripped Sable, but reusing the already-proven,
 * bounds-free overload avoids relying on an unverified 3-argument
 * {@code getEntities(EntityTypeTest, AABB, Predicate)} overload that was not checked
 * against this exact Minecraft version.
 * <p>
 * The "active" glint flag is stored on the {@code ItemStack} itself, same idea as the
 * Forge donor's NBT tag, but through 1.21.1's data component API instead of raw NBT
 * ({@code stack.getOrCreateTag()} no longer exists post-1.20.5) - confirmed via
 * mappings.dev: {@code DataComponents.CUSTOM_DATA} + {@code CustomData.update(...)}/
 * {@code stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)}. This is the
 * first use of a data component for custom mod data in this project - a real
 * verification gap like appendHoverText's TooltipContext change in Phase 2B, flagged
 * here in case the build disagrees.
 * <p>
 * Real build failure found and fixed (2026-09-13): {@code LivingEntity#hasEffect}/
 * {@code #removeEffect} and the {@code MobEffectInstance} constructor all take a
 * {@code Holder<MobEffect>} in 1.21.1, not a raw {@code MobEffect} - so
 * {@code ModEffects.RALLY_COMMANDER.get()} (which returns the raw
 * {@code RallyCommanderEffect}) failed to compile at all three call sites. Fixed by
 * passing {@code ModEffects.RALLY_COMMANDER} itself: confirmed via NeoForge's own source
 * that {@code DeferredHolder<R, T extends R> implements Holder<R>, Supplier<T>} - the
 * holder already satisfies a {@code Holder<MobEffect>}-typed parameter directly, no
 * {@code .get()} needed (that's only for when the raw {@code MobEffect} value itself is
 * wanted, as in {@code ModItems}' creative-tab registration).
 * <p>
 * Real in-game behavior report, resolved 2026-09-14: releasing an active rally used to
 * call {@code guard.setFollowing(false)} on every OWNED GUARD CURRENTLY WITHIN
 * {@code RALLY_RANGE} of the player, not just the guards this particular rally had
 * actually gathered. Root cause, confirmed by reading Guard Villagers' real source
 * ({@code Guard.FOLLOWING}, {@code GuardFollowPacket}): {@code Following} is the SAME
 * flag Guard Villagers' own inventory-GUI "follow me" toggle uses - it is not a
 * rally-exclusive concept. So releasing a rally could silently turn off a companion
 * guard's independently-set "follow me" preference even though that guard was never
 * part of the rally (and, symmetrically, {@code RallyFriendlyFireHandler} - which is
 * intentionally keyed off the same {@code isFollowing()} flag, see its own javadoc -
 * would keep protecting a guard from friendly fire the whole time it had "follow me" on,
 * with or without an active rally, which is correct per that handler's stated intent).
 * Fixed by having the rally remember exactly which guard UUIDs it gathered (stored
 * alongside the existing "active" flag in the same {@code CustomData} tag on the scroll
 * itself - see {@code #getRalliedGuardIds(ItemStack)}/{@code #setRalliedGuardIds}) and
 * only clearing {@code Following} on release for guards in that recorded set, looked up
 * by UUID rather than by a fresh distance query - which also fixes the related edge case
 * where a rallied guard that fell more than {@code RALLY_RANGE} behind the player would
 * never have been released at all under the old distance-based release query.
 */
public class ScrollOfRallyingItem extends Item {
    private static final String NBT_ACTIVE = "active";
    private static final String NBT_RALLIED_GUARDS = "RalliedGuards";
    private static final double RALLY_RANGE = 100.0D;
    private static final double RALLY_RANGE_SQ = RALLY_RANGE * RALLY_RANGE;
    private static final double FORMATION_RADIUS = 3.5D;

    public ScrollOfRallyingItem(Properties props) {
        super(props);
    }

    private static boolean isActive(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean(NBT_ACTIVE);
    }

    private static void setActive(ItemStack stack, boolean value) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putBoolean(NBT_ACTIVE, value));
    }

    /**
     * Which guard UUIDs THIS rally gathered - see the class javadoc's "real in-game
     * behavior report" note. Stored the same way {@code SquadRegistry} (Phase 3A) stores
     * its own member lists: a {@code ListTag} of {@code StringTag} UUID strings, since a
     * data component's backing {@code CompoundTag} has no native UUID-list type.
     */
    private static Set<UUID> getRalliedGuardIds(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        Set<UUID> ids = new HashSet<>();
        ListTag list = tag.getList(NBT_RALLIED_GUARDS, Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            ids.add(UUID.fromString(list.getString(i)));
        }
        return ids;
    }

    private static void setRalliedGuardIds(ItemStack stack, Set<UUID> ids) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            ListTag list = new ListTag();
            for (UUID id : ids) {
                list.add(StringTag.valueOf(id.toString()));
            }
            tag.put(NBT_RALLIED_GUARDS, list);
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }
        if (!(user instanceof ServerPlayer player)) {
            return InteractionResultHolder.pass(stack);
        }
        if (!user.isCrouching()) {
            return InteractionResultHolder.pass(stack);
        }

        boolean rallyOn = player.hasEffect(ModEffects.RALLY_COMMANDER);
        ServerLevel serverLevel = player.serverLevel();

        if (rallyOn) {
            player.removeEffect(ModEffects.RALLY_COMMANDER);
            player.displayClientMessage(
                    Component.translatable("alert.rallyguard.scroll_of_rallying.strength_lost")
                            .withStyle(s -> s.withColor(0xFF0000)),
                    false
            );

            // Only release the guards THIS rally actually gathered (see the class
            // javadoc's "real in-game behavior report" note) - looked up by UUID, not by
            // a fresh distance query, so a guard that wandered off past RALLY_RANGE still
            // gets released, and a guard whose "follow me" was set independently through
            // Guard Villagers' own inventory GUI (never part of this rally) is left alone.
            Set<UUID> ralliedIds = getRalliedGuardIds(stack);
            if (!ralliedIds.isEmpty()) {
                List<? extends Guard> rallied = serverLevel.getEntities(EntityTypeTest.forClass(Guard.class),
                        guard -> ralliedIds.contains(guard.getUUID())
                                && GuardOwnership.isOwnedBy(guard, player.getUUID()));
                for (Guard guard : rallied) {
                    guard.setFollowing(false);
                }
            }
            setActive(stack, false);
            setRalliedGuardIds(stack, new HashSet<>());
        } else {
            player.addEffect(new MobEffectInstance(
                    ModEffects.RALLY_COMMANDER, Integer.MAX_VALUE, 0, false, false, true
            ));
            player.displayClientMessage(
                    Component.translatable("alert.rallyguard.scroll_of_rallying.strength_gained")
                            .withStyle(s -> s.withColor(0x00FF00)),
                    false
            );

            List<? extends Guard> candidates = serverLevel.getEntities(EntityTypeTest.forClass(Guard.class),
                    guard -> GuardOwnership.isOwnedBy(guard, player.getUUID())
                            && guard.distanceToSqr(player) <= RALLY_RANGE_SQ);

            List<Guard> joiners = new ArrayList<>();
            for (Guard guard : candidates) {
                if (!guard.isPatrolling()) {
                    joiners.add(guard);
                }
            }

            int total = joiners.size();
            int i = 0;
            Set<UUID> joinerIds = new HashSet<>();
            for (Guard guard : joiners) {
                double angle = (Math.PI / (total + 1)) * (++i);
                double gx = player.getX() + Math.cos(angle) * FORMATION_RADIUS;
                double gz = player.getZ() + Math.sin(angle) * FORMATION_RADIUS;
                rallyGuardToPlayer(guard, player, gx, player.getY(), gz);
                joinerIds.add(guard.getUUID());
            }
            setActive(stack, true);
            setRalliedGuardIds(stack, joinerIds);
        }

        player.getCooldowns().addCooldown(this, 60);
        return InteractionResultHolder.success(stack);
    }

    private static void rallyGuardToPlayer(Guard guard, Player user, double x, double y, double z) {
        // Same reasoning as the mounted-guard fixes in GuardCommandNetworking (summon
        // and patrol-on, PORTING_PLAN.md section 17.2/18): a horse-mounted guard can't be
        // usefully repositioned or driven by its own AI without dismounting first.
        // Phase 3F: except a rider on a drivable mount, which is brought along WITH its
        // mount and then follows you mounted (MountDriver).
        clearPath(guard);
        guard.setDeltaMovement(0.0, 0.0, 0.0);
        MountDriver.relocate(guard, x, y, z);
        guard.setFollowing(true);
        guard.lookAt(user, 30.0F, 30.0F);
    }

    private static void clearPath(Guard guard) {
        guard.setTarget(null);
        guard.setAggressive(false);
        guard.getNavigation().stop();
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isActive(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tips, TooltipFlag flag) {
        tips.add(Component.translatable("tooltip.rallyguard.scroll_of_rallying.tooltip_desc"));
        super.appendHoverText(stack, context, tips, flag);
    }
}
