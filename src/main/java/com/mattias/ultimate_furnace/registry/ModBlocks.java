package com.mattias.ultimate_furnace.registry;

import com.mattias.ultimate_furnace.blocks.UltimateFurnaceBlockGum;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.mattias.ultimate_furnace.UltimateFurnaceMod;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

public class ModBlocks {
	public static final Block ULTIMATE_FURNACE_GUM = registerBlock("ultimate_furnace_gum", new UltimateFurnaceBlockGum(QuiltBlockSettings.of(Material.STONE).strength(4.0f)));

	private static Block registerBlock(String name, Block block) {
		registerBlockItem(name, block);
		return Registry.register(Registry.BLOCK, new Identifier(UltimateFurnaceMod.MOD_ID, name), block);
	}

	private static Item registerBlockItem(String name, Block block) {
		return Registry.register(Registry.ITEM, new Identifier(UltimateFurnaceMod.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
	}

	public static void registerModBlocks() {
		UltimateFurnaceMod.LOGGER.info("Registering blocks for " + UltimateFurnaceMod.MOD_ID);
	}
}
