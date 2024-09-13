package com.crepsman.ultimate_furnace.registry;

import com.crepsman.ultimate_furnace.screen.UltimateFurnaceScreenHandlerGum;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.crepsman.ultimate_furnace.UltimateFurnaceMod;

public class ModScreenHandlers {

	public static final ScreenHandlerType<UltimateFurnaceScreenHandlerGum> ULTIMATE_FURNACE_SCREEN_HANDLER_GUM =
		Registry.register(
			Registry.SCREEN_HANDLER,
			new Identifier(UltimateFurnaceMod.MOD_ID, "ultimate_furnace_gum"),
			new ScreenHandlerType<>(UltimateFurnaceScreenHandlerGum::new)
		);

	public static void registerScreenHandlers() {

	}
}
