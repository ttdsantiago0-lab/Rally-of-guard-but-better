package net.hfstack.rallyguard.screen;

import net.hfstack.rallyguard.combat.GuardPack;
import net.hfstack.rallyguard.contract.PackRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * E1-A: the war pack as a plain 6-slot container menu (pack row on top, player inventory
 * below) - opened from the Ledger's ▤ button while the guard is within
 * {@link #REACH} blocks. Same constructor pair as {@link HireGuardMenu}: the client one
 * reads the guard's entity id from the open-menu buffer.
 */
public class PackMenu extends AbstractContainerMenu {

    public static final double REACH = 16.0D;

    private final Container pack;
    private final int guardEntityId;

    public PackMenu(int syncId, Inventory inv, RegistryFriendlyByteBuf data) {
        this(syncId, inv, data.readVarInt(), new SimpleContainer(PackRegistry.SIZE));
    }

    public PackMenu(int syncId, Inventory inv, int guardEntityId, Container pack) {
        super(ModMenus.PACK_MENU.get(), syncId);
        this.pack = pack;
        this.guardEntityId = guardEntityId;
        pack.startOpen(inv.player);
        for (int i = 0; i < PackRegistry.SIZE; i++) {
            addSlot(new Slot(pack, i, 35 + i * 18, 20));
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 58 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inv, col, 8 + col * 18, 116));
        }
    }

    /** Server side: opens {@code guard}'s pack for {@code player}. */
    public static PackMenu server(int syncId, Inventory inv, Entity guard) {
        return new PackMenu(syncId, inv, guard.getId(), GuardPack.of((ServerLevel) guard.level(), guard.getUUID()));
    }

    @Override
    public boolean stillValid(Player player) {
        if (player.level().isClientSide()) return true;
        Entity guard = player.level().getEntity(guardEntityId);
        return guard != null && guard.isAlive() && player.distanceToSqr(guard) <= REACH * REACH;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        pack.stopOpen(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();
        int packEnd = PackRegistry.SIZE;
        if (index < packEnd) {
            if (!moveItemStackTo(stack, packEnd, this.slots.size(), true)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 0, packEnd, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }
}
