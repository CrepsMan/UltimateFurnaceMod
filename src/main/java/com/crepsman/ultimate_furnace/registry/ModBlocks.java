package com.crepsman.ultimate_furnace.registry;

import com.crepsman.ultimate_furnace.blocks.UltimateFurnaceBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.state.property.Properties;
import com.crepsman.ultimate_furnace.UltimateFurnaceMod;
import org.jetbrains.annotations.Nullable;

import java.util.function.ToIntFunction;

public class ModBlocks {

	public static final Block ULTIMATE_FURNACE = registerBlock(
		"ultimate_furnace",
		new UltimateFurnaceBlock(FabricBlockSettings.create().requiresTool().strength(3.5F).luminance(createLightLevelFromLitBlockState(13))),
		ItemGroup.DECORATIONS
	);

	private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
		return (state) -> state.get(Properties.LIT) ? litLevel : 0;
	}

	private static Block registerBlock(String name, Block block, @Nullable ItemGroup group) {
		registerBlockItem(name, block, group);
		return Registry.register(Registry.BLOCK, new Identifier(UltimateFurnaceMod.MOD_ID, name), block);
	}

	private static Item registerBlockItem(String name, Block block, @Nullable ItemGroup group) {
		return Registry.register(
			Registry.ITEM,
			new Identifier(UltimateFurnaceMod.MOD_ID, name),
			new BlockItem(block, new Item.Settings().group(group))
		);
	}

	public static void registerModBlocks() {
		// Register all blocks here
	}
}
