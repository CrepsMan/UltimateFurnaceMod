/*package com.mattias.ultimate_furnace.blocks;

import com.mattias.ultimate_furnace.UltimateFurnaceMod;
import com.mattias.ultimate_furnace.blocks.entity.UltimateFurnaceBlockEntity;
import com.mattias.ultimate_furnace.registry.ModBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class UltimateFurnaceBlock extends Block implements BlockEntityProvider {

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
	public static final BooleanProperty LIT = BooleanProperty.of("lit");
	public static final BooleanProperty DAY_MODE = BooleanProperty.of("day_mode");

	public UltimateFurnaceBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState()
			.with(LIT, false)
			.with(DAY_MODE, true)
		);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof UltimateFurnaceBlockEntity) {
				player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onBlockAdded(state, world, pos, oldState, notify);
		if (!world.isClient) {
			scheduleNextTick(world, pos);
		}
	}

	private void scheduleNextTick(World world, BlockPos pos) {
		if (!world.isClient) {
			world.scheduleBlockTick(pos, this, 200, TickPriority.NORMAL);
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, RandomGenerator random) {
		boolean isDay = world.getTimeOfDay() % 24000 < 12000;
		world.setBlockState(pos, state.with(DAY_MODE, isDay), 3);

		// Schedule the next tick
		scheduleNextTick(world, pos);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new UltimateFurnaceBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (!world.isClient && type == ModBlockEntities.ULTIMATE_FURNACE_BLOCK_ENTITY) {
			return (world1, pos, state1, blockEntity) -> {
				if (blockEntity instanceof UltimateFurnaceBlockEntity) {
					UltimateFurnaceBlockEntity.tick(world1, pos, state1, (UltimateFurnaceBlockEntity) blockEntity);
				}
			};
		}
		return null;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LIT, DAY_MODE, FACING);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		boolean isDay = ((World) world).getTimeOfDay() % 24000 < 12000;
		return state.with(UltimateFurnaceBlock.DAY_MODE, isDay);
	}
}
/*/
