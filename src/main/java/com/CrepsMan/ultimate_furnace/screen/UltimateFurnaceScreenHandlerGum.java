package com.CrepsMan.ultimate_furnace.screen;

import com.CrepsMan.ultimate_furnace.registry.ModScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class UltimateFurnaceScreenHandlerGum extends AbstractFurnaceScreenHandler {
	public UltimateFurnaceScreenHandlerGum(int syncId, PlayerInventory playerInventory) {
		super(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER_GUM, RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory);
	}

	public UltimateFurnaceScreenHandlerGum(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER_GUM, RecipeType.SMELTING, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
	}
}
