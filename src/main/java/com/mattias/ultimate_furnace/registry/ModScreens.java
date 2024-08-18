package com.mattias.ultimate_furnace.registry;

import com.mattias.ultimate_furnace.screen.UltimateFurnaceScreen;
import com.mattias.ultimate_furnace.screen.UltimateFurnaceScreenHandler;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import com.mattias.ultimate_furnace.UltimateFurnaceMod;

public class ModScreens {
	public static void registerClientScreens() {
		UltimateFurnaceMod.LOGGER.info("Registering client screens for " + UltimateFurnaceMod.MOD_ID);
		ScreenRegistry.register(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER, UltimateFurnaceScreen::new);

	}

}
