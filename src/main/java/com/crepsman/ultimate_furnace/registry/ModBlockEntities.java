package com.crepsman.ultimate_furnace.registry;

import com.crepsman.ultimate_furnace.blocks.entity.UltimateFurnaceBlockEntityGum;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.crepsman.ultimate_furnace.UltimateFurnaceMod;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

public class ModBlockEntities {
	public static final BlockEntityType<UltimateFurnaceBlockEntityGum> ULTIMATE_FURNACE_BLOCK_ENTITY_GUM =
		Registry.register(
			Registry.BLOCK_ENTITY_TYPE,
			new Identifier(UltimateFurnaceMod.MOD_ID, "ultimate_furnace_gum"),
			QuiltBlockEntityTypeBuilder.create(UltimateFurnaceBlockEntityGum::new, ModBlocks.ULTIMATE_FURNACE_GUM).build(null)
		);

	public static void registerBlockEntities() {
	}
}