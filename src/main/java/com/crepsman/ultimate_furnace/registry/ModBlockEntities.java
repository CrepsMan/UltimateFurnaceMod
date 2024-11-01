package com.crepsman.ultimate_furnace.registry;

import com.crepsman.ultimate_furnace.blocks.entity.UltimateFurnaceBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import com.crepsman.ultimate_furnace.UltimateFurnaceMod;

import static com.crepsman.ultimate_furnace.UltimateFurnaceMod.MOD_ID;

public class ModBlockEntities {

	public static final BlockEntityType<UltimateFurnaceBlockEntity> ULTIMATE_FURNACE_BLOCK_ENTITY =
		Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.tryParse(MOD_ID, "ultimate_furnace_block_entity"), ULTIMATE_FURNACE_BLOCK_ENTITY);


	public static void registerBlockEntities() {
		// Register all block entities here
	}
}
