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

	public UltimateFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER, RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
		this.customPropertyDelegate = propertyDelegate;
		this.addProperties(this.customPropertyDelegate);
		// Clear existing slots and add only the necessary ones
		this.slots.clear(); // Clear all existing slots
		this.inventory = inventory;

		// Add the input slot
		this.addSlot(new Slot(inventory, 0, 56, 17)); // Input slot

		// Add the result slot
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

	public int getCookProgress() {
		int cookTime = this.customPropertyDelegate.get(2);
		int cookTimeTotal = this.customPropertyDelegate.get(3);

		if (cookTimeTotal == 0) {
			cookTimeTotal = 1; // Avoid division by zero
		}

		// Return progress based on the total cook time
		return (int)((double) cookTime / cookTimeTotal * 24); // 24 is the width of the arrow progress indicator
	}



	// Get the current fuel progress for rendering the flame icon
	public int getFuelProgress() {
		int burnTime = this.customPropertyDelegate.get(0);
		int currentBurnTime = this.customPropertyDelegate.get(1);
		return currentBurnTime != 0 && burnTime != 0 ? burnTime * 13 / currentBurnTime : 0;
	}

	// Get whether the furnace is currently burning
	public boolean isBurning() {
		return this.customPropertyDelegate.get(0) > 0;
	}

	// Get the smelt count to be displayed in the GUI
	public int getSmeltCount() {
		return this.customPropertyDelegate.get(4);
	}

	// Create a custom slot class for the output slot
	public static class OutputSlot extends Slot {
		public OutputSlot(Inventory inventory, int index, int x, int y) {
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack) {
			// Prevent players from placing any items into the output slot
			return false;
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}
}
