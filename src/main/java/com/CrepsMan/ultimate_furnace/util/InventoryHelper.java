package com.CrepsMan.ultimate_furnace.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class InventoryHelper {

	/**
	 * Removes up to a specified number of items from an inventory slot.
	 *
	 * @param items the list of item stacks
	 * @param slot the slot to remove from
	 * @param amount the maximum number of items to remove
	 * @return the removed stack
	 */
	public static ItemStack removeStack(DefaultedList<ItemStack> items, int slot, int amount) {
		ItemStack stack = items.get(slot);
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			return stack.split(amount);
		}
	}

	/**
	 * Removes all items from an inventory slot.
	 *
	 * @param items the list of item stacks
	 * @param slot the slot to remove from
	 * @return the removed stack
	 */
	public static ItemStack removeStack(DefaultedList<ItemStack> items, int slot) {
		ItemStack stack = items.get(slot);
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			items.set(slot, ItemStack.EMPTY);
			return stack;
		}
	}

	/**
	 * Calculate the comparator output based on how full the inventory is.
	 *
	 * @param inventory the inventory
	 * @return the comparator output
	 */
	public static int calculateComparatorOutput(Inventory inventory) {
		int nonEmptySlots = 0;
		float fillRatio = 0.0F;

		for (int i = 0; i < inventory.size(); i++) {
			ItemStack stack = inventory.getStack(i);
			if (!stack.isEmpty()) {
				fillRatio += (float) stack.getCount() / (float) Math.min(inventory.getMaxCountPerStack(), stack.getMaxCount());
				nonEmptySlots++;
			}
		}

		fillRatio /= (float) inventory.size();
		return Math.round(fillRatio * 14.0F) + (nonEmptySlots > 0 ? 1 : 0);
	}
}
