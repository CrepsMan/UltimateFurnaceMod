package com.crepsman.ultimate_furnace.registry;

import com.crepsman.ultimate_furnace.blocks.entity.UltimateFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import com.crepsman.ultimate_furnace.UltimateFurnaceMod;

public class ModBlockEntities {
	public static final BlockEntityType<UltimateFurnaceBlockEntity> ULTIMATE_FURNACE_BLOCK_ENTITY =
		Registry.register(
			RegistryKeys.BLOCK_ENTITY_TYPE,
			Identifier.tryParse(UltimateFurnaceMod.MOD_ID, "ultimate_furnace"),
			QuiltBlockEntityTypeBuilder.create(UltimateFurnaceBlockEntity::new, ModBlocks.ULTIMATE_FURNACE).build(null)
		);

	public static void registerBlockEntities() {
	}
}
