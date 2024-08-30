package com.CrepsMan.ultimate_furnace.screen;

import com.CrepsMan.ultimate_furnace.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;

public class UltimateFurnaceScreenHandlerGum extends AbstractFurnaceScreenHandler {
	private final PropertyDelegate customPropertyDelegate;

	public UltimateFurnaceScreenHandlerGum(int syncId, PlayerInventory playerInventory) {
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

	public UltimateFurnaceScreenHandlerGum(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER_GUM, RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
		this.customPropertyDelegate = propertyDelegate;
		this.addProperties(this.customPropertyDelegate);
		// Clear existing slots and add only the necessary ones
		this.slots.clear(); // Clear all existing slots

		// Add the input slot
		this.addSlot(new Slot(inventory, 0, 56, 17)); // Input slot

		// Add the result slot
		this.addSlot(new Slot(inventory, 2, 116, 35)); // Output slot

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

	// Get the current cook progress for rendering the cook progress bar
	public int getCookProgress() {
		int cookTime = this.customPropertyDelegate.get(2);
		int cookTimeTotal = this.customPropertyDelegate.get(3);
		return cookTimeTotal != 0 && cookTime != 0 ? cookTime * 24 / cookTimeTotal : 0;
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
}
