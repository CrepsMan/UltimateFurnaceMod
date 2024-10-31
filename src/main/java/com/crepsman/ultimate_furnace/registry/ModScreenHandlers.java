package com.CrepsMan.ultimate_furnace.registry;

import com.CrepsMan.ultimate_furnace.screen.UltimateFurnaceScreenHandlerGum;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.CrepsMan.ultimate_furnace.UltimateFurnaceMod;

public class ModScreenHandlers {

	public static final ScreenHandlerType<UltimateFurnaceScreenHandlerGum> ULTIMATE_FURNACE_SCREEN_HANDLER_GUM =
		Registry.register(
			Registry.SCREEN_HANDLER,
			new Identifier(UltimateFurnaceMod.MOD_ID, "ultimate_furnace_gum"),
			new ScreenHandlerType<>(UltimateFurnaceScreenHandlerGum::new)
		);

	public static void registerScreenHandlers() {
		UltimateFurnaceMod.LOGGER.info("Registering screen handlers for " + UltimateFurnaceMod.MOD_ID);
		UltimateFurnaceMod.LOGGER.debug("UltimateFurnaceScreenHandler registered with ID: ultimate_furnace");
	}
}
