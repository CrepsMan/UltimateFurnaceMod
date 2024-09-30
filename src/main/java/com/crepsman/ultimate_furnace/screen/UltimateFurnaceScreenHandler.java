package com.crepsman.ultimate_furnace.screen;

import com.crepsman.ultimate_furnace.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;

public class UltimateFurnaceScreenHandler extends AbstractFurnaceScreenHandler {
	private final PropertyDelegate customPropertyDelegate;
	private final Inventory inventory;

	public UltimateFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(3), new PropertyDelegate() {
			private final int[] data = new int[5];

			@Override
			public int get(int index) {
				return data[index];
			}

			@Override
			public void set(int index, int value) {
				data[index] = value;
			}

			@Override
			public int size() {
				return data.length;
			}
		});
	}

	// Override transferSlot to use custom logic
	@Override
	public ItemStack quickTransfer(PlayerEntity player, int index) {
		// Call custom transfer logic here
		return customTransferStack(player, index);
	}

	// Custom transfer logic implementation
	public ItemStack customTransferStack(PlayerEntity player, int index) {
		ItemStack newStack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);

		if (slot != null && slot.hasStack()) {
			ItemStack originalStack = slot.getStack();
			newStack = originalStack.copy();

			// Output slot (index 2) -> Transfer to player inventory/hotbar
			if (index == 2) {
				// Move items from output slot to player inventory (slots 3 to 39)
				if (!this.insertItem(originalStack, 3, 39, true)) {
					return ItemStack.EMPTY;
				}
				slot.onQuickTransfer(originalStack, newStack);
			}
			// Player inventory or hotbar slots -> Transfer to furnace input slot (index 0)
			else if (index != 0) { // (Note: No need to check 'index != 1' as fuel slot is removed)
				// Move items from player inventory/hotbar to furnace input slot (index 0)
				if (this.isSmeltable(originalStack)) {
					if (!this.insertItem(originalStack, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				}
				// Move items from player inventory (slots 3 to 30) to hotbar (slots 30 to 39)
				else if (index >= 3 && index < 30) {
					if (!this.insertItem(originalStack, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				}
				// Move items from hotbar (slots 30 to 39) to player inventory (slots 3 to 30)
				else if (index >= 30 && index < 39) {
					if (!this.insertItem(originalStack, 3, 30, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
			// Input slot (index 0) -> Transfer to player inventory/hotbar
			else if (!this.insertItem(originalStack, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			// If original stack is empty, clear the slot
			if (originalStack.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			// If nothing changed in the stack, return EMPTY
			if (originalStack.getCount() == newStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTakeItem(player, originalStack);
		}

		return newStack;
	}


	public UltimateFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER, RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
		this.customPropertyDelegate = propertyDelegate;
		this.addProperties(this.customPropertyDelegate);
		this.slots.clear();
		this.inventory = inventory;

		// Add the input slot
		this.addSlot(new Slot(inventory, 0, 56, 17)); // Input slot

		// Add the output slot
		this.addSlot(new OutputSlot(inventory, 2, 116, 35)); // Output slot

		int playerInventoryStartX = 8;
		int playerInventoryStartY = 84;
		int hotbarStartY = 142;

		// Add player inventory slots
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlot(new Slot(playerInventory, col + row * 9 + 9, playerInventoryStartX + col * 18, playerInventoryStartY + row * 18));
			}
		}

		// Add player hotbar slots
		for (int col = 0; col < 9; col++) {
			this.addSlot(new Slot(playerInventory, col, playerInventoryStartX + col * 18, hotbarStartY));
		}
	}

	@Override
	public int getCookProgress() {
		int cookTime = this.customPropertyDelegate.get(2); // This could cause the issue if index exceeds bounds
		int cookTimeTotal = this.customPropertyDelegate.get(3);

		if (cookTimeTotal == 0) {
			cookTimeTotal = 1; // Avoid division by zero
		}

		return (int) ((double) cookTime / cookTimeTotal * 24); // 24 is the width of the arrow progress indicator
	}


	public int getFuelProgress() {
		int burnTime = this.customPropertyDelegate.get(0);
		int currentBurnTime = this.customPropertyDelegate.get(1);
		return currentBurnTime != 0 && burnTime != 0 ? burnTime * 13 / currentBurnTime : 0;
	}

	public boolean isBurning() {
		return this.customPropertyDelegate.get(0) > 0;
	}

	public int getSmeltCount() {
		return this.customPropertyDelegate.get(4);
	}



	public static class OutputSlot extends Slot {
		public OutputSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			return false; // Prevent players from placing any items into the output slot
		}
	}

	@Override
	public boolean isFuel(ItemStack stack) {
		// Return false for all items, making nothing act as fuel
		return false;
	}


	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}


}
