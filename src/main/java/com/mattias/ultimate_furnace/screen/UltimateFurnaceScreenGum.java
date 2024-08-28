package com.mattias.ultimate_furnace.screen;

import com.mattias.ultimate_furnace.UltimateFurnaceMod;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.FurnaceRecipeBookScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UltimateFurnaceScreenGum extends AbstractFurnaceScreen<UltimateFurnaceScreenHandlerGum> {
	private static final Identifier TEXTURE = new Identifier(UltimateFurnaceMod.MOD_ID, "textures/gui/container/ultimate_furnace.png");

	public UltimateFurnaceScreenGum(UltimateFurnaceScreenHandlerGum handler, PlayerInventory inventory, Text title) {
		super(handler, new FurnaceRecipeBookScreen(), inventory, title, TEXTURE);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);

		int cookProgress = this.handler.getCookProgress();
		this.drawTexture(matrices, 79, 34, 176, 14, cookProgress + 1, 16);

		if (this.handler.isBurning()) {
			int fuelProgress = this.handler.getFuelProgress();
			this.drawTexture(matrices, 56, 53, 176, 0, 14, 14);
		}
	}
}
