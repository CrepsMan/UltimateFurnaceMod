package com.mattias.ultimate_furnace.blocks.entity;

import com.mattias.ultimate_furnace.registry.ModBlockEntities;
import com.mattias.ultimate_furnace.screen.UltimateFurnaceScreenHandlerGum;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class UltimateFurnaceBlockEntityGum extends AbstractFurnaceBlockEntity {
	public UltimateFurnaceBlockEntityGum(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ULTIMATE_FURNACE_BLOCK_ENTITY_GUM, pos, state, RecipeType.SMELTING);
	}

	protected Text getContainerName() {
		return Text.translatable("container.ultimate_furnace.furnace_gum");
	}

	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new UltimateFurnaceScreenHandlerGum(syncId, playerInventory, this, this.propertyDelegate);
	}
}
