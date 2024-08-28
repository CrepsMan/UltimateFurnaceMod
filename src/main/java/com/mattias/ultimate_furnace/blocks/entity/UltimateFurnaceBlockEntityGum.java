package com.mattias.ultimate_furnace.blocks.entity;

import com.mattias.ultimate_furnace.UltimateFurnaceMod;
import com.mattias.ultimate_furnace.blocks.UltimateFurnaceBlockGum;
import com.mattias.ultimate_furnace.registry.ModBlockEntities;
import com.mattias.ultimate_furnace.screen.UltimateFurnaceScreenHandlerGum;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class UltimateFurnaceBlockEntityGum extends AbstractFurnaceBlockEntity {
	private int cookTime = 0;
	private int cookTimeTotal = 300; // Base cook time
	private int nightBurnTime = 6144; // Base burn time at night
	private int currentNightBurnTime = 0;
	private int smeltCount = 0;

	// Upgrade thresholds
	private static final int[] UPGRADE_THRESHOLDS = {300, 650, 1000}; // Example thresholds
	private static final int[] UPGRADE_COOK_TIME = {200, 150, 100}; // Cook time for each upgrade
	private static final int[] UPGRADE_NIGHT_BURN_TIME = {6144, 8192, 10240}; // Night burn time for each upgrade

	public UltimateFurnaceBlockEntityGum(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ULTIMATE_FURNACE_BLOCK_ENTITY_GUM, pos, state, RecipeType.SMELTING);

		// Instead of reassigning propertyDelegate, configure it directly using the existing field
		this.propertyDelegate.set(0, cookTime);
		this.propertyDelegate.set(1, cookTimeTotal);
		this.propertyDelegate.set(2, nightBurnTime);
		this.propertyDelegate.set(3, currentNightBurnTime);
	}

	public static void tick(World world, BlockPos pos, BlockState state, UltimateFurnaceBlockEntityGum entity) {
		if (world.isClient) return;

		UltimateFurnaceMod.LOGGER.debug("UltimateFurnaceBlockEntity: Ticking at " + pos);

		boolean dirty = false;
		boolean isDay = world.isDay();

		// Handle day and night burn time logic
		if (isDay) {
			entity.currentNightBurnTime = entity.getNightBurnTime(); // Use upgraded night burn time
		} else if (entity.currentNightBurnTime > 0) {
			entity.currentNightBurnTime--; // Decrease night burn time at night
		}

		ItemStack input = entity.getInputSlot();
		if (!input.isEmpty()) {
			// Check if the furnace should be burning
			if (entity.isBurning()) {
				Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(input), world);
				if (recipe.isPresent()) {
					entity.cookTime++;
					if (entity.cookTime >= entity.getCookTimeTotal()) {
						entity.smeltItem(recipe.get().getOutput().copy());
						entity.cookTime = 0;
						entity.incrementSmeltCount();
						dirty = true;
					}
				} else {
					entity.cookTime = 0;
				}
			} else {
				entity.cookTime = 0; // Reset cook time if not burning
			}
		} else {
			entity.cookTime = 0; // Reset cook time if no input
		}

		// Determine if the block should be marked as "lit"
		boolean isBurning = entity.isBurning() && !input.isEmpty();
		if (dirty || isBurning != state.get(UltimateFurnaceBlockGum.LIT)) {
			state = state.with(UltimateFurnaceBlockGum.LIT, isBurning);
			world.setBlockState(pos, state, 3);
			entity.markDirty();
		}
	}

	private int getCookTimeTotal() {
		int index = getUpgradeLevel();
		return UPGRADE_COOK_TIME[index];
	}

	private int getNightBurnTime() {
		int index = getUpgradeLevel();
		return UPGRADE_NIGHT_BURN_TIME[index];
	}

	private int getUpgradeLevel() {
		for (int i = UPGRADE_THRESHOLDS.length - 1; i >= 0; i--) {
			if (smeltCount >= UPGRADE_THRESHOLDS[i]) {
				return i;
			}
		}
		return 0; // No upgrades
	}

	public void incrementSmeltCount() {
		this.smeltCount++;
	}

	@Override
	protected Text getContainerName() {
		return Text.translatable("container.ultimate_furnace.furnace_gum");
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

	protected boolean isBurning() {
		return this.world != null && (this.world.isDay() || this.currentNightBurnTime > 0);
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new UltimateFurnaceScreenHandlerGum(syncId, playerInventory, this, this.propertyDelegate);
	}
}
