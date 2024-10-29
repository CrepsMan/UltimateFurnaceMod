package com.crepsman.ultimate_furnace.blocks;

import com.crepsman.ultimate_furnace.blocks.entity.UltimateFurnaceBlockEntity;
import com.crepsman.ultimate_furnace.registry.ModBlockEntities;
import com.crepsman.ultimate_furnace.util.ModProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.GaussianGenerator;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.random.RandomGenerator;

public class UltimateFurnaceBlock extends AbstractFurnaceBlock {
	public static final BooleanProperty DAY_MODE;

	public UltimateFurnaceBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(LIT, false)
				.with(DAY_MODE, false)
		);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new UltimateFurnaceBlockEntity(pos, state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIT, DAY_MODE, FACING);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(type, ModBlockEntities.ULTIMATE_FURNACE_BLOCK_ENTITY, UltimateFurnaceBlockEntity::tick);
	}

	@Override
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof UltimateFurnaceBlockEntity) {
			player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
			player.incrementStat(Stats.INTERACT_WITH_FURNACE);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		if (!world.isClient) {
			this.openScreen(world, pos, player);
			return ActionResult.CONSUME;
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (!world.isClient) {
			updateDayMode(world, state, pos);
		}
	}

	public void updateDayMode(World world, BlockState state, BlockPos pos) {
		boolean isDay = world.getTimeOfDay() % 24000 < 12000;

		if (state.get(DAY_MODE) != isDay) {
			world.setBlockState(pos, state.with(DAY_MODE, isDay), 3);
		}

		scheduleNextTick(world, pos);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, GaussianGenerator random) {
		boolean isDay = world.getTimeOfDay() % 24000 < 12000;

		if (state.get(DAY_MODE) != isDay) {
			world.setBlockState(pos, state.with(DAY_MODE, isDay), 3);
		}

		scheduleNextTick(world, pos);
	}

	private void scheduleNextTick(World world, BlockPos pos) {
		if (!world.isClient) {
			world.scheduleBlockTick(pos, this, 200, net.minecraft.world.tick.TickPriority.NORMAL);
		}
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		if (state.get(LIT)) {
			double x = pos.getX() + 0.5;
			double y = pos.getY() + 0.15;
			double z = pos.getZ() + 0.5;
			if (random.nextDouble() < 0.1) {
				world.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = state.get(FACING);
			double offsetX = direction.getAxis() == Direction.Axis.X ? direction.getOffsetX() * 0.52 : (random.nextDouble() * 0.6 - 0.3);
			double offsetZ = direction.getAxis() == Direction.Axis.Z ? direction.getOffsetZ() * 0.52 : (random.nextDouble() * 0.6 - 0.3);

			world.addParticle(ParticleTypes.SMOKE, x + offsetX, y + (random.nextDouble() * 6.0 / 16.0), z + offsetZ, 0.0, 0.0, 0.0);
			world.addParticle(ParticleTypes.FLAME, x + offsetX, y + (random.nextDouble() * 6.0 / 16.0), z + offsetZ, 0.0, 0.0, 0.0);
		}
	}

	static {
		DAY_MODE = ModProperties.DAY_MODE;
	}
}