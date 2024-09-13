package com.crepsman.ultimate_furnace;

import com.crepsman.ultimate_furnace.registry.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UltimateFurnaceMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("Ultimate Furnace");
	public static final String MOD_ID = "ultimate_furnace";
	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Ultimate Furnace!");

		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
	}
}
