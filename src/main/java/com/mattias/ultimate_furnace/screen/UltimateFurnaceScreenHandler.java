/*package com.mattias.ultimate_furnace.screen;

import com.mattias.ultimate_furnace.blocks.entity.UltimateFurnaceBlockEntity;
import com.mattias.ultimate_furnace.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class UltimateFurnaceScreenHandler extends ScreenHandler {
	private final Inventory inventory;
	private final PropertyDelegate propertyDelegate;

	public UltimateFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, UltimateFurnaceBlockEntity blockEntity, PropertyDelegate propertyDelegate) {
		super(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER, syncId);
		this.inventory = blockEntity;
		this.propertyDelegate = propertyDelegate;

		// Add slots to the screen handler
		this.addSlot(new Slot(inventory, 0, 56, 17)); // Input slot
		this.addSlot(new Slot(inventory, 1, 116, 35)); // Output slot

		// Add player inventory slots
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		// Add player hotbar slots
		for (int i = 0; i < 9; i++) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

		this.addProperties(propertyDelegate);
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack quickTransfer(PlayerEntity player, int index) {
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasStack()) {
			ItemStack originalStack = slot.getStack();
			newStack = originalStack.copy();

			if (index < this.inventory.size()) { // If it's a furnace slot
				if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
				return ItemStack.EMPTY;
			}

			if (originalStack.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
		}

		return newStack;
	}

	public boolean isSmelting() {
		return this.propertyDelegate.get(0) > 0;
	}

	public int getSmeltingProgress() {
		int i = this.propertyDelegate.get(0);
		int j = this.propertyDelegate.get(1);
		return j != 0 && i != 0 ? i * 24 / j : 0;
	}

	public boolean hasFuel() {
		return this.propertyDelegate.get(2) > 0;
	}

	public int getFuelHeight() {
		int i = this.propertyDelegate.get(2);
		int j = this.propertyDelegate.get(3);
		return j != 0 && i != 0 ? i * 13 / j : 0;
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		super.onContentChanged(inventory);
	}

	public UltimateFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
		this(syncId, playerInventory, (UltimateFurnaceBlockEntity) playerInventory.player.world.getBlockEntity(buf.readBlockPos()), new ArrayPropertyDelegate(4));
	}
}
/*/
