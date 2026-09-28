package net.hfstack.rallyguard.item;

import net.hfstack.rallyguard.client.ClientHooks;
import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.order.CommanderMarks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Commander's Ledger - opens the Commander's Ledger command panel when used.
 * <p>
 * Phase 2E: ports the Forge 1.20.1 {@code use(...)} behavior - request the guard list
 * from the server (which responds with {@code GuardListS2CPayload} and opens
 * {@link net.hfstack.rallyguard.screen.GuardCommandScreen} - see
 * {@code GuardCommandNetworking#handleGuardList}). Reaches the client-only send through
 * {@link ClientHooks#requestOpenGuardPanel()} rather than calling networking code
 * directly from this common-side class - see that class's javadoc for why a direct
 * guarded call (no reflection, unlike the Fabric donor) is safe here.
 * <p>
 * Phase 3E: the Ledger is also the commander's pointer ({@link CommanderMarks}), which
 * is how the panel - a GUI with no view of the world - gets explicit targets and
 * destinations without any client raycast:
 * <ul>
 *     <li><b>Sneak + right-click a creature or player</b>: mark it. It then appears as the
 *     "Marked" option of the panel's target selector.</li>
 *     <li><b>Sneak + right-click a block</b>: mark the spot in front of that face (the top
 *     of a wall, a gate, a hill) and the direction you are facing. While a position is
 *     marked, Muster and Retreat form up THERE, facing that way, instead of on you.</li>
 *     <li><b>Sneak + right-click the air</b>: clear both marks.</li>
 *     <li>Plain right-click: open the panel, as before.</li>
 * </ul>
 * Feedback (sound, particles, action-bar text) is sent to the commander ONLY, so
 * marking an enemy player doesn't warn them.
 */
public class CommandersLedgerItem extends Item {
    public CommandersLedgerItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (user.isSecondaryUseActive()) {
            if (user instanceof ServerPlayer player) {
                CommanderMarks.clearMarks(player.getUUID());
                player.displayClientMessage(Component.translatable("message.rallyguard.marks_cleared"), true);
                player.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 0.8F, 0.8F);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        if (level.isClientSide()) {
            ClientHooks.requestOpenGuardPanel();
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player user = context.getPlayer();
        if (user == null || !user.isSecondaryUseActive()) {
            return InteractionResult.PASS; // plain right-click on a block still opens the panel via use()
        }
        if (user instanceof ServerPlayer player) {
            BlockPos spot = context.getClickedPos().relative(context.getClickedFace());
            CommanderMarks.markPosition(player.getUUID(), player.serverLevel().dimension(), spot, player.getYRot());
            player.displayClientMessage(Component.translatable("message.rallyguard.position_marked",
                    spot.getX(), spot.getY(), spot.getZ()), true);
            player.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0F, 1.2F);
            player.serverLevel().sendParticles(player, ParticleTypes.HAPPY_VILLAGER, true,
                    spot.getX() + 0.5D, spot.getY() + 0.2D, spot.getZ() + 0.5D, 12, 0.3D, 0.1D, 0.3D, 0.0D);
        }
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity target, InteractionHand hand) {
        if (!user.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        if (GuardOwnership.isOwnedBy(target, user.getUUID())) {
            return InteractionResult.PASS; // your own guards are commanded, not targeted
        }
        if (user instanceof ServerPlayer player) {
            CommanderMarks.markEntity(player.getUUID(), target);
            player.displayClientMessage(Component.translatable("message.rallyguard.target_marked",
                    target.getName().getString()), true);
            player.playNotifySound(SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0F, 0.6F);
            player.serverLevel().sendParticles(player, ParticleTypes.ANGRY_VILLAGER, true,
                    target.getX(), target.getY() + target.getBbHeight() + 0.3D, target.getZ(), 3, 0.2D, 0.1D, 0.2D, 0.0D);
        }
        return InteractionResult.sidedSuccess(user.level().isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tips, TooltipFlag flag) {
        tips.add(Component.translatable("item.rallyguard.commanders_ledger.tooltip"));
        tips.add(Component.translatable("item.rallyguard.commanders_ledger.tooltip_mark"));
        super.appendHoverText(stack, context, tips, flag);
    }
}
