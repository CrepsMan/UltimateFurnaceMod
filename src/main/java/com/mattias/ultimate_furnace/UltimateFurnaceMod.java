package com.mattias.ultimate_furnace;

import com.mattias.ultimate_furnace.registry.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UltimateFurnaceMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ultimate_furnace");
	public static final String MOD_ID = "ultimate_furnace";
	@Override
	public void onInitialize() {

		LOGGER.info("Initializing Ultimate Furnace Mod");
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		LOGGER.info("Ultimate Furnace Mod Initialization Complete");

	}
}
