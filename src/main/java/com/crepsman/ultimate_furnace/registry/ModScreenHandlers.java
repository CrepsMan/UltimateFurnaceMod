package com.crepsman.ultimate_furnace.registry;

import com.crepsman.ultimate_furnace.screen.UltimateFurnaceScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.crepsman.ultimate_furnace.UltimateFurnaceMod;

import static com.crepsman.ultimate_furnace.UltimateFurnaceMod.MOD_ID;

public class ModScreenHandlers {

	public static final ScreenHandlerType<UltimateFurnaceScreenHandler> ULTIMATE_FURNACE_SCREEN_HANDLER =
		Registry.register(Registries.SCREEN_HANDLER, Identifier.tryParse(MOD_ID, "ultimate_furnace_screen"), new ScreenHandlerType<>(UltimateFurnaceScreenHandler::new));


	public static void registerScreenHandlers() {
		// Register screen handlers here
	}
}
