package com.crepsman.ultimate_furnace.registry;

import com.crepsman.ultimate_furnace.screen.UltimateFurnaceScreenHandler;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.crepsman.ultimate_furnace.UltimateFurnaceMod;

public class ModScreenHandlers {

	public static final ScreenHandlerType<UltimateFurnaceScreenHandler> ULTIMATE_FURNACE_SCREEN_HANDLER =
		Registry.register(
			RegistryKeys.SCREEN_HANDLER,
			Identifier.tryParse(UltimateFurnaceMod.MOD_ID, "ultimate_furnace"),
			new ScreenHandlerType<>(UltimateFurnaceScreenHandler::new)
		);

	public static void registerScreenHandlers() {

	}
}
