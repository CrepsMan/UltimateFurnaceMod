package com.crepsman.ultimate_furnace.registry;

import com.crepsman.ultimate_furnace.blocks.UltimateFurnaceBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.crepsman.ultimate_furnace.UltimateFurnaceMod;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import java.util.function.ToIntFunction;

public class ModBlocks {

	public static final Block ULTIMATE_FURNACE = registerBlock("ultimate_furnace", new UltimateFurnaceBlock(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3.5F).luminance(createLightLevelFromLitBlockState(13))), ItemGroup.DECORATIONS);



	private static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
		return (state) -> {
			return (Boolean)state.get(Properties.LIT) ? litLevel : 0;
		};
	}

	private static Block registerBlock(String name, Block block, @Nullable ItemGroup group) {
		registerBlockItem(name, block, group);
		return Registry.register(Registry.BLOCK, new Identifier(UltimateFurnaceMod.MOD_ID, name), block);
	}

	private static Item registerBlockItem(String name, Block block, @Nullable ItemGroup group) {
		return Registry.register(Registry.ITEM, new Identifier(UltimateFurnaceMod.MOD_ID, name), new BlockItem(block, new FabricItemSettings().group(group)));
	}

	public static void registerModBlocks() {

	}

}
