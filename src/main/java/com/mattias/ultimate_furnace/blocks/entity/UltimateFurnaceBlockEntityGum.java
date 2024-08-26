package com.mattias.ultimate_furnace.blocks.entity;

import com.mattias.ultimate_furnace.UltimateFurnaceMod;
import com.mattias.ultimate_furnace.blocks.UltimateFurnaceBlockGum;
import com.mattias.ultimate_furnace.registry.ModBlockEntities;
import com.mattias.ultimate_furnace.screen.UltimateFurnaceScreenHandlerGum;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class UltimateFurnaceBlockEntityGum extends AbstractFurnaceBlockEntity {
	private int cookTime = 0;
	private int cookTimeTotal = 200; // Base cook time
	private int nightBurnTime = 6000; // Base burn time at night
	private int currentNightBurnTime = 0;
	private int smeltCount = 0; // Declare smeltCount

	public UltimateFurnaceBlockEntityGum(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ULTIMATE_FURNACE_BLOCK_ENTITY_GUM, pos, state, RecipeType.SMELTING);
	}

	public static void tick(World world, BlockPos pos, BlockState state, UltimateFurnaceBlockEntityGum entity) {
		if (world.isClient) return;

		UltimateFurnaceMod.LOGGER.debug("UltimateFurnaceBlockEntity: Ticking at " + pos);

		boolean dirty = false;
		boolean isDay = world.isDay();

		if (isDay) {
			entity.currentNightBurnTime = entity.nightBurnTime; // Reset night burn time at day
		}

		if (entity.isBurning()) {
			ItemStack input = entity.getInputSlot();
			if (!input.isEmpty()) {
				Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(input), world);
				if (recipe.isPresent()) {
					entity.cookTime++;
					if (entity.cookTime >= entity.cookTimeTotal) {
						entity.smeltItem(recipe.get().getOutput().copy());
						entity.cookTime = 0;
						entity.incrementSmeltCount();
						dirty = true;
					}
				} else {
					entity.cookTime = 0;
				}
			}
		}

		boolean isBurning = entity.isBurning();
		if (dirty) {
			state = state.with(UltimateFurnaceBlockGum.LIT, isBurning);
			world.setBlockState(pos, state, 3);
			entity.markDirty();
		}
	}


	@Override
	protected Text getContainerName() {
		return Text.translatable("container.ultimate_furnace.furnace_gum");
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new UltimateFurnaceScreenHandlerGum(syncId, playerInventory, this, this.propertyDelegate);
	}

	public ItemStack getInputSlot() {
		return this.inventory.get(0); // Assuming inventory[0] is the input slot
	}

	public void smeltItem(ItemStack output) {
		ItemStack resultStack = this.inventory.get(2); // Assuming inventory[2] is the output slot
		if (resultStack.isEmpty()) {
			this.inventory.set(2, output);
		} else if (resultStack.isItemEqual(output)) {
			resultStack.increment(output.getCount());
		}
		this.inventory.get(0).decrement(1); // Decrease input stack
	}

	public void incrementSmeltCount() {
		this.smeltCount++;
	}

	protected boolean isBurning() {

		return this.world != null && (this.world.isDay() || this.currentNightBurnTime > 0);
	}
}
