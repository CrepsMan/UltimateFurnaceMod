package com.mattias.ultimate_furnace.screen;

import com.mattias.ultimate_furnace.UltimateFurnaceMod;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.FurnaceRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UltimateFurnaceScreenGum extends AbstractFurnaceScreen<UltimateFurnaceScreenHandlerGum> {
	private static final Identifier TEXTURE = new Identifier(UltimateFurnaceMod.MOD_ID, "textures/gui/container/ultimate_furnace.png");

    public UltimateFurnaceScreenGum(UltimateFurnaceScreenHandlerGum handler, PlayerInventory inventory, Text title) {
		super(handler, new FurnaceRecipeBookScreen(), inventory, title, TEXTURE);
	}
}
