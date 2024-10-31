package com.CrepsMan.ultimate_furnace;

import com.CrepsMan.ultimate_furnace.registry.ModScreenHandlers;
import com.CrepsMan.ultimate_furnace.screen.UltimateFurnaceScreenGum;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class UltimateFurnaceModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(ModScreenHandlers.ULTIMATE_FURNACE_SCREEN_HANDLER_GUM, UltimateFurnaceScreenGum::new);
	}
}
