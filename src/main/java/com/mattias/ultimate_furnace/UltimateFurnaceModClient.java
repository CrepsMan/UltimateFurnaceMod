package com.mattias.ultimate_furnace;

import com.mattias.ultimate_furnace.registry.ModScreenHandlers;
import com.mattias.ultimate_furnace.registry.ModScreens;
import net.fabricmc.api.ClientModInitializer;

public class UltimateFurnaceModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER_GUM, UltimateFurnaceScreenGum::new);
	}
}
