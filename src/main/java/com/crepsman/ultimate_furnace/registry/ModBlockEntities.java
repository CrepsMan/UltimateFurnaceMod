package com.crepsman.ultimate_furnace.registry;

import com.crepsman.ultimate_furnace.blocks.entity.UltimateFurnaceBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.crepsman.ultimate_furnace.UltimateFurnaceMod;

public class ModBlockEntities {

	public static final BlockEntityType<UltimateFurnaceBlockEntity> ULTIMATE_FURNACE_BLOCK_ENTITY =
		Registry.register(
			Registry.BLOCK_ENTITY_TYPE,
			new Identifier(UltimateFurnaceMod.MOD_ID, "ultimate_furnace"),
			FabricBlockEntityTypeBuilder.create(UltimateFurnaceBlockEntity::new, ModBlocks.ULTIMATE_FURNACE).build(null)
		);

	public static void registerBlockEntities() {
		// Register all block entities here
	}
}
