package com.mattias.ultimate_furnace.blocks.entity;

import com.mattias.ultimate_furnace.UltimateFurnaceMod;
import com.mattias.ultimate_furnace.blocks.UltimateFurnaceBlock;
import com.mattias.ultimate_furnace.registry.ModBlockEntities;
import com.mattias.ultimate_furnace.screen.UltimateFurnaceScreenHandler;
import com.mattias.ultimate_furnace.util.ImplementedInventory;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UltimateFurnaceBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory {
	private final PropertyDelegate propertyDelegate;
	private DefaultedList<ItemStack> items = DefaultedList.ofSize(3, ItemStack.EMPTY);
	private int tickCounter = 0;
	private int cookTime = 0;
	private int cookTimeTotal = 200; // Base cook time
	private boolean dayPowered = false;
	private int nightBurnTime = 2000; // Base burn time at night
	private int currentNightBurnTime = 0;
	private int smeltCount = 0; // Declare smeltCount
	private int upgradeLevel = 0; // Declare upgradeLevel

	public UltimateFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.ULTIMATE_FURNACE_BLOCK_ENTITY, pos, state);
		this.propertyDelegate = new ArrayPropertyDelegate(4);
	}

	public static void tick(World world, BlockPos pos, BlockState state, UltimateFurnaceBlockEntity entity) {
		if (world.isClient) return;

		UltimateFurnaceMod.LOGGER.debug("UltimateFurnaceBlockEntity: Ticking at " + pos);

		boolean dirty = false;
		boolean isDay = world.isDay();

		if (isDay) {
			entity.dayPowered = true;
			entity.currentNightBurnTime = entity.nightBurnTime; // Reset night burn time at day
		} else {
			entity.dayPowered = false;
		}

		boolean wasBurning = entity.isBurning();

		if (entity.dayPowered || entity.currentNightBurnTime > 0) {
			if (!entity.dayPowered && entity.currentNightBurnTime > 0) {
				entity.currentNightBurnTime--;
			}

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

			boolean isBurning = entity.isBurning();
			if (wasBurning != isBurning) {
				dirty = true;
				state = state.with(UltimateFurnaceBlock.LIT, isBurning);
				world.setBlockState(pos, state, 3);
			}
		}

		if (dirty) {
			UltimateFurnaceMod.LOGGER.debug("UltimateFurnaceBlockEntity: Marking entity dirty at " + pos);
			entity.markDirty();
		}
	}

	private ItemStack getInputSlot() {
		return this.items.get(0);
	}

	private ItemStack getOutputSlot() {
		return this.items.get(1);
	}

	private void setOutputSlot(ItemStack stack) {
		this.items.set(1, stack);
	}

	private void smeltItem(ItemStack output) {
		ItemStack result = this.getOutputSlot();
		if (result.isEmpty()) {
			this.setOutputSlot(output.copy());
		} else if (result.getItem() == output.getItem()) {
			result.increment(output.getCount());
		}

		this.getInputSlot().decrement(1);
	}

	private boolean isBurning() {
		return this.dayPowered || this.currentNightBurnTime > 0;
	}

	private void incrementSmeltCount() {
		this.smeltCount++;
		if (this.smeltCount % 500 == 0) {
			this.upgradeFurnace();
		}
	}

	private void upgradeFurnace() {
		this.upgradeLevel++;
		this.cookTimeTotal = Math.max(50, this.cookTimeTotal - 20); // Faster smelting
		this.nightBurnTime += 200; // Longer burn time at night
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		Inventories.writeNbt(nbt, this.items);
		nbt.putInt("SmeltCount", this.smeltCount);
		nbt.putInt("UpgradeLevel", this.upgradeLevel);
		nbt.putInt("CookTime", this.cookTime);
		nbt.putInt("CookTimeTotal", this.cookTimeTotal);
		nbt.putInt("CurrentNightBurnTime", this.currentNightBurnTime);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, this.items);
		this.smeltCount = nbt.getInt("SmeltCount");
		this.upgradeLevel = nbt.getInt("UpgradeLevel");
		this.cookTime = nbt.getInt("CookTime");
		this.cookTimeTotal = nbt.getInt("CookTimeTotal");
		this.currentNightBurnTime = nbt.getInt("CurrentNightBurnTime");
	}

	@Override
	public Text getDisplayName() {
		return Text.translatable("container.ultimate_furnace");
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new UltimateFurnaceScreenHandler(syncId, inv, this, this.propertyDelegate);
	}

}
