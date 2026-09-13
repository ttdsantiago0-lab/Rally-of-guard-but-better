package net.hfstack.rallyguard.item;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.effect.ModEffects;
import net.minecraft.core.component.DataComponents;
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
import java.util.List;

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
 */
public class ScrollOfRallyingItem extends Item {
    private static final String NBT_ACTIVE = "active";
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

        boolean rallyOn = player.hasEffect(ModEffects.RALLY_COMMANDER.get());
        ServerLevel serverLevel = player.serverLevel();

        if (rallyOn) {
            player.removeEffect(ModEffects.RALLY_COMMANDER.get());
            setActive(stack, false);
            player.displayClientMessage(
                    Component.translatable("alert.rallyguard.scroll_of_rallying.strength_lost")
                            .withStyle(s -> s.withColor(0xFF0000)),
                    false
            );

            List<? extends Guard> myGuards = serverLevel.getEntities(EntityTypeTest.forClass(Guard.class),
                    guard -> GuardOwnership.isOwnedBy(guard, player.getUUID())
                            && guard.distanceToSqr(player) <= RALLY_RANGE_SQ);

            for (Guard guard : myGuards) {
                guard.setFollowing(false);
            }
        } else {
            player.addEffect(new MobEffectInstance(
                    ModEffects.RALLY_COMMANDER.get(), Integer.MAX_VALUE, 0, false, false, true
            ));
            setActive(stack, true);
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
            for (Guard guard : joiners) {
                double angle = (Math.PI / (total + 1)) * (++i);
                double gx = player.getX() + Math.cos(angle) * FORMATION_RADIUS;
                double gz = player.getZ() + Math.sin(angle) * FORMATION_RADIUS;
                rallyGuardToPlayer(guard, player, gx, player.getY(), gz);
            }
        }

        player.getCooldowns().addCooldown(this, 60);
        return InteractionResultHolder.success(stack);
    }

    private static void rallyGuardToPlayer(Guard guard, Player user, double x, double y, double z) {
        // Same reasoning as the mounted-guard fixes in GuardCommandNetworking (summon
        // and patrol-on, PORTING_PLAN.md section 17.2/18): a horse-mounted guard can't be
        // usefully repositioned or driven by its own AI without dismounting first.
        if (guard.isPassenger()) {
            guard.stopRiding();
        }
        clearPath(guard);
        guard.setDeltaMovement(0.0, 0.0, 0.0);
        guard.moveTo(x, y, z, guard.getYRot(), guard.getXRot());
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
