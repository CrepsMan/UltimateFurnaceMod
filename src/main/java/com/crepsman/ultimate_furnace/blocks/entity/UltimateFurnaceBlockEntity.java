package com.crepsman.ultimate_furnace.blocks.entity;

import com.crepsman.ultimate_furnace.UltimateFurnaceMod;
import com.crepsman.ultimate_furnace.blocks.UltimateFurnaceBlock;
import com.crepsman.ultimate_furnace.registry.ModBlockEntities;
import com.crepsman.ultimate_furnace.screen.UltimateFurnaceScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class UltimateFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

	private final PropertyDelegate propertyDelegate;
	private int cookTime = 0;
	private int cookTimeTotal = 300; // Base cook time
	private int nightBurnTime = 6144; // Base burn time at night
	private int currentNightBurnTime = 0;
	private int smeltCount = 0;

	// Upgrade thresholds
	private static final int[] UPGRADE_THRESHOLDS = {300, 650, 1200, 5000, 15000}; // Example thresholds
	private static final int[] UPGRADE_COOK_TIME = {250, 200, 150, 70, 15}; // Cook time for each upgrade
	private static final int[] UPGRADE_NIGHT_BURN_TIME = {6144, 8192, 10240, 10240, 20480}; // Night burn time for each upgrade

	public UltimateFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ULTIMATE_FURNACE_BLOCK_ENTITY, pos, state, RecipeType.SMELTING);

		this.propertyDelegate = new PropertyDelegate() {
			@Override
			public int get(int index) {
				switch (index) {
					case 0:
						return UltimateFurnaceBlockEntity.this.currentNightBurnTime;
					case 1:
						return UltimateFurnaceBlockEntity.this.nightBurnTime;
					case 2:
						return UltimateFurnaceBlockEntity.this.cookTime;
					case 3:
						return UltimateFurnaceBlockEntity.this.cookTimeTotal;
					case 4:
						return UltimateFurnaceBlockEntity.this.smeltCount;
					default:
						return 0;
				}
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0:
						UltimateFurnaceBlockEntity.this.currentNightBurnTime = value;
						break;
					case 1:
						UltimateFurnaceBlockEntity.this.nightBurnTime = value;
						break;
					case 2:
						UltimateFurnaceBlockEntity.this.cookTime = value;
						break;
					case 3:
						UltimateFurnaceBlockEntity.this.cookTimeTotal = value;
						break;
					case 4:
						UltimateFurnaceBlockEntity.this.smeltCount = value;
						break;
				}
			}

			@Override
			public int size() {
				return 5;
			}
		};
	}

	public static void tick(World world, BlockPos pos, BlockState state, UltimateFurnaceBlockEntity entity) {
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
		ItemStack output = entity.inventory.get(2); // Get the output slot

		if (!input.isEmpty() && (output.isEmpty() || output.getCount() < 64)) {
			// Check if the furnace should be smelting
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
			entity.cookTime = 0; // Reset cook time if no input or output slot is full
		}

		// Update PropertyDelegate
		entity.propertyDelegate.set(2, entity.cookTime);
		entity.propertyDelegate.set(3, entity.getCookTimeTotal());

		// Determine if the block should be marked as "lit"
		boolean isBurning = entity.isBurning() && !input.isEmpty();
		if (dirty || isBurning != state.get(UltimateFurnaceBlock.LIT)) {
			state = state.with(UltimateFurnaceBlock.LIT, isBurning);
			world.setBlockState(pos, state, 3);
			entity.markDirty();
		}
	}


	private int getCookTimeTotal() {
		int upgradeLevel = getUpgradeLevel();
		return upgradeLevel == 0 ? this.cookTimeTotal : UPGRADE_COOK_TIME[upgradeLevel - 1];
	}



	private int getNightBurnTime() {
		int index = getUpgradeLevel();
		return UPGRADE_NIGHT_BURN_TIME[index];
	}

	private int getUpgradeLevel() {
		for (int i = UPGRADE_THRESHOLDS.length - 1; i >= 0; i--) {
			if (smeltCount >= UPGRADE_THRESHOLDS[i]) {
				return i + 1; // Returning i+1 to distinguish the base level as 0
			}
		}
		return 0; // Base level with no upgrades
	}


	public void incrementSmeltCount() {
		this.smeltCount++;
		markDirty();
	}


	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("SmeltCount", this.smeltCount);

	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.smeltCount = nbt.getInt("SmeltCount");
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
			// If the output slot is empty, place the new stack directly
			this.inventory.set(2, output.copy());
			this.inventory.get(0).decrement(1); // Decrease input stack since the smelting is successful
		} else if (resultStack.isItemEqual(output)) {
			int newCount = resultStack.getCount() + output.getCount();

			if (newCount <= 64) {
				resultStack.increment(output.getCount());
				this.inventory.get(0).decrement(1); // Decrease input stack since the smelting is successful
			} else if (resultStack.getCount() < 64) {
				// If adding items would exceed 64, only add as many as needed to reach 64
				int itemsToAdd = 64 - resultStack.getCount();
				resultStack.increment(itemsToAdd);
				this.inventory.get(0).decrement(1); // Decrease input stack since the smelting is successful
			}
		}
	}



	protected boolean isBurning() {
		return this.world != null && (this.world.isDay() || this.currentNightBurnTime > 0);
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new UltimateFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}
}
