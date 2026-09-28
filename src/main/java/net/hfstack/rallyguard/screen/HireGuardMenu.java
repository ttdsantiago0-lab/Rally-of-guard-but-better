package net.hfstack.rallyguard.screen;

import net.hfstack.rallyguard.contract.GuardOwnership;
import net.hfstack.rallyguard.contract.GuardRoster;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Ported verbatim from the Forge 1.20.1 {@code HireGuardMenu}, except ownership is now set
 * via {@link GuardOwnership#setOwner} (typed Guard Villagers API) instead of an NBT
 * round-trip - see {@link GuardOwnership} for the full rationale.
 * <p>
 * The hire button click reaches this class entirely through vanilla's own container-button
 * mechanism ({@code AbstractContainerMenu#clickMenuButton}, driven by the client's built-in
 * {@code ServerboundContainerButtonClickPacket}) - there is no Rally of the Guard custom
 * packet involved here, so this is genuinely Phase 2C, not Phase 2D (networking).
 * <p>
 * Forge -&gt; NeoForge: the client-side factory constructor's buffer parameter type changes
 * from {@code FriendlyByteBuf} to {@code RegistryFriendlyByteBuf}, matching the exact
 * {@code IContainerFactory<T>.create(int, Inventory, RegistryFriendlyByteBuf)} signature
 * used by {@link ModMenus} (verified against the real NeoForge 1.21.1
 * {@code IContainerFactory} javadoc).
 */
public class HireGuardMenu extends AbstractContainerMenu {
    private final int guardEntityId; // -1 on the client (unused there)
    private final Inventory playerInventory;

    // Client-side factory constructor (IContainerFactory): (windowId, inv, data)
    public HireGuardMenu(int syncId, Inventory inv, RegistryFriendlyByteBuf data) {
        this(syncId, inv, data.readVarInt());
    }

    // Server-side constructor
    public HireGuardMenu(int syncId, Inventory inv, int guardEntityId) {
        super(ModMenus.HIRE_MENU.get(), syncId);
        this.playerInventory = inv;
        this.guardEntityId = guardEntityId;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    /**
     * id == 0 => "Hire" button
     */
    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (!(player instanceof ServerPlayer sp)) return false;
        if (id != 0) return false;

        ServerLevel level = sp.serverLevel();
        Entity guard = level.getEntity(this.guardEntityId);

        // Guard no longer exists -> close
        if (guard == null || !GuardOwnership.isGuard(guard)) {
            sp.closeContainer();
            return true;
        }

        // Already owned -> close and notify
        if (GuardOwnership.hasOwner(guard)) {
            sp.closeContainer();
            sp.displayClientMessage(Component.translatable("gui.rallyguard.hire.already_owned"), true);
            return true;
        }

        final int COST = 3;
        int emeralds = 0;

        // Count emeralds
        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            ItemStack s = playerInventory.getItem(i);
            if (s.is(Items.EMERALD)) emeralds += s.getCount();
        }

        // Not enough -> close and notify (overlay)
        if (emeralds < COST) {
            sp.closeContainer();
            sp.displayClientMessage(Component.translatable("gui.rallyguard.hire.not_enough"), true);
            return true;
        }

        // Deduct cost
        int remaining = COST;
        for (int i = 0; i < playerInventory.getContainerSize() && remaining > 0; i++) {
            ItemStack s = playerInventory.getItem(i);
            if (s.is(Items.EMERALD)) {
                int take = Math.min(remaining, s.getCount());
                s.shrink(take);
                remaining -= take;
            }
        }

        // Set owner (applies gold name + "reporting for duty" chat message)
        GuardOwnership.setOwner(guard, sp);

        // Phase 3B: seed the durable roster immediately at hire time, rather than
        // waiting for the first time this owner happens to open the Ledger while this
        // guard is loaded (GuardCommandNetworking#sendGuardList opportunistically
        // refreshes it too - see GuardRoster's class javadoc for why "opportunistic,
        // no background scan" is an accepted trade-off here). Without this seed call, a
        // guard hired and then immediately unloaded (player walks away right after
        // hiring) would be invisible on the Ledger until it's loaded again at least
        // once - a real, if narrow, gap this closes.
        GuardRoster.get(level).recordSnapshot(guard.getUUID(), sp.getUUID(), guard.getName().getString(),
                false, GuardOwnership.describeMount(guard),
                GuardOwnership.describeArmor((LivingEntity) guard), level.getGameTime(),
                level.dimension().location().toString(), guard.blockPosition());

        // Success (overlay)
        sp.displayClientMessage(Component.translatable("gui.rallyguard.hire.success"), true);

        sp.closeContainer();
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }
}
