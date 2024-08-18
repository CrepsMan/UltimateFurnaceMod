package com.mattias.ultimate_furnace.registry;

import com.mattias.ultimate_furnace.screen.UltimateFurnaceScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.mattias.ultimate_furnace.UltimateFurnaceMod;

public class ModScreenHandlers {
	public static final ExtendedScreenHandlerType<UltimateFurnaceScreenHandler> ULTIMATE_FURNACE_SCREEN_HANDLER =
		Registry.register(
			Registry.SCREEN_HANDLER,
			new Identifier(UltimateFurnaceMod.MOD_ID, "ultimate_furnace"),
			new ExtendedScreenHandlerType<>(UltimateFurnaceScreenHandler::new)
		);

	public static void registerScreenHandlers() {
		UltimateFurnaceMod.LOGGER.info("Registering screen handlers for " + UltimateFurnaceMod.MOD_ID);
		UltimateFurnaceMod.LOGGER.debug("UltimateFurnaceScreenHandler registered with ID: ultimate_furnace");
	}
}
