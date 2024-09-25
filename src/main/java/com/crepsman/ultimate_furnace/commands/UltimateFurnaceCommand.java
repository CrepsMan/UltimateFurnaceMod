package com.crepsman.ultimate_furnace.commands;

import com.crepsman.ultimate_furnace.blocks.UltimateFurnaceBlock;
import com.crepsman.ultimate_furnace.blocks.entity.UltimateFurnaceBlockEntity;
import com.crepsman.ultimate_furnace.registry.ModBlockEntities;
import com.crepsman.ultimate_furnace.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class UltimateFurnaceCommand {

	private static int execute(ServerCommandSource source, BlockPos pos, Mode mode) {
		Block block = source.getWorld().getBlockState(pos).getBlock();
		if(block == ModBlocks.ULTIMATE_FURNACE){
			UltimateFurnaceBlockEntity entity = source.getWorld().getBlockEntity(pos, ModBlockEntities.ULTIMATE_FURNACE_BLOCK_ENTITY).get();
			source.sendFeedback(Text.translatable("commands.ultimate_furnace.ultimate_furnace.success", entity.getSmeltCount()), true);
		} else {
			source.sendFeedback(Text.translatable("commands.ultimate_furnace.ultimate_furnace.fail", block.getName()), false);
		}
		return 0;
	}


	public enum Mode{
		GET{

		},
		SET{
			
		};
	}
}
