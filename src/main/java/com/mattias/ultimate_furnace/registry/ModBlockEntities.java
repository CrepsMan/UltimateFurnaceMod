package com.mattias.ultimate_furnace.registry;

import com.mattias.ultimate_furnace.blocks.entity.UltimateFurnaceBlockEntity;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.mattias.ultimate_furnace.UltimateFurnaceMod;

public class ModBlockEntities {
	public static final BlockEntityType<UltimateFurnaceBlockEntity> ULTIMATE_FURNACE_BLOCK_ENTITY =
		Registry.register(
			Registry.BLOCK_ENTITY_TYPE,
			new Identifier(UltimateFurnaceMod.MOD_ID, "ultimate_furnace"),
			BlockEntityType.Builder.create(UltimateFurnaceBlockEntity::new, ModBlocks.ULTIMATE_FURNACE).build(null)
		);

	public static void registerBlockEntities() {
		UltimateFurnaceMod.LOGGER.info("Registering block entities for " + UltimateFurnaceMod.MOD_ID);
		UltimateFurnaceMod.LOGGER.debug("UltimateFurnaceBlockEntity registered with ID: ultimate_furnace");
	}
}
