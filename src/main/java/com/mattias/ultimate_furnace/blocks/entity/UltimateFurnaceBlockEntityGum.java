package com.mattias.ultimate_furnace.blocks.entity;

import com.mattias.ultimate_furnace.registry.ModBlockEntities;
import com.mattias.ultimate_furnace.screen.UltimateFurnaceScreenHandlerGum;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Holder;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;
import java.util.Map;

public class UltimateFurnaceBlockEntityGum extends AbstractFurnaceBlockEntity {
	private int storedPower;

	public UltimateFurnaceBlockEntityGum(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ULTIMATE_FURNACE_BLOCK_ENTITY_GUM, pos, state, RecipeType.SMELTING);
	}

	protected Text getContainerName() {
		return Text.translatable("container.ultimate_furnace.furnace_gum");
	}

	private static boolean isNonFlammableWood(Item item) {
		return item.getBuiltInRegistryHolder().isIn(ItemTags.NON_FLAMMABLE_WOOD);
	}

	private static void addFuel(Map<Item, Integer> fuelTimes, TagKey<Item> tag, int fuelTime) {
		Iterator var3 = Registry.ITEM.getTagOrEmpty(tag).iterator();

		while(var3.hasNext()) {
			Holder<Item> holder = (Holder)var3.next();
			if (!isNonFlammableWood((Item)holder.value())) {
				fuelTimes.put((Item)holder.value(), fuelTime);
			}
		}

	}

	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new UltimateFurnaceScreenHandlerGum(syncId, playerInventory, this, this.propertyDelegate);
	}
}
