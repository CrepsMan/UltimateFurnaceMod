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
import net.minecraft.screen.slot.FurnaceFuelSlot;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;

public class UltimateFurnaceScreenHandler extends AbstractFurnaceScreenHandler {
	private final PropertyDelegate customPropertyDelegate;
	private final Inventory inventory;
	private static final int[] UPGRADE_THRESHOLDS = {100, 500, 1000, 5000, 10000, 15000};
	public static final int INGREDIENT_SLOT = 0;
	public static final int RESULT_SLOT = 2;
	public static final int SLOTS_COUNT = 3;
	public static final int PROPERTIES_COUNT = 4;
	private static final int INVENTORY_SLOTS_START = 3;
	private static final int INVENTORY_SLOTS_END = 30;
	private static final int HOTBAR_SLOTS_START = 30;
	private static final int HOTBAR_SLOTS_END = 39;

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

	public UltimateFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER, RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
		this.customPropertyDelegate = propertyDelegate;
		this.addProperties(this.customPropertyDelegate);
		this.slots.clear();
		this.inventory = inventory;

		// Add the slots
		this.addSlot(new Slot(inventory, 0, 56, 17));
		this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, 2, 116, 35));

		int i;
		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}

		this.addProperties(propertyDelegate);
	}

	public ItemStack customTransferStack(PlayerEntity player, int fromIndex) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot = (Slot)this.slots.get(fromIndex);
		if (slot != null && slot.hasStack()) {
			ItemStack itemStack2 = slot.getStack();
			itemStack = itemStack2.copy();
			if (fromIndex == 2) {
				if (!this.insertItem(itemStack2, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onQuickTransfer(itemStack2, itemStack);
			} else if (fromIndex != 1 && fromIndex != 0) {
				if (this.isSmeltable(itemStack2)) {
					if (!this.insertItem(itemStack2, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (fromIndex >= 3 && fromIndex < 30) {
					if (!this.insertItem(itemStack2, 30, 39, false)) {
						return ItemStack.EMPTY;
					}
				} else if (fromIndex >= 30 && fromIndex < 39 && !this.insertItem(itemStack2, 3, 30, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack2.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}

			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTakeItem(player, itemStack2);
		}

		return itemStack;
	}

	@Override
	public int getCookProgress() {
		int cookTime = this.customPropertyDelegate.get(2); // This could cause the issue if index exceeds bounds
		int cookTimeTotal = this.customPropertyDelegate.get(3);

		if (cookTimeTotal == 0) {
			cookTimeTotal = 1; // Avoid division by zero
		}

		return (float) ((double) cookTime / cookTimeTotal * 24); // 24 is the width of the arrow progress indicator
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
		int smeltCount = this.customPropertyDelegate.get(4);
		return Math.min(smeltCount, 15000); // Cap the smelt count at 15000
	}

	public int getCurrentLevel() {
		int smeltCount = getSmeltCount();
		for (int i = UPGRADE_THRESHOLDS.length - 1; i >= 0; i--) {
			if (smeltCount >= UPGRADE_THRESHOLDS[i]) {
				return i + 1; // Returning i+1 to distinguish the base level as 0
			}
		}
		return 0; // Base level with no upgrades
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
