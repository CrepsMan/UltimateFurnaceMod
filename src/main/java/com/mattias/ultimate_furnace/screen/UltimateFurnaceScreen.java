/*package com.mattias.ultimate_furnace.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UltimateFurnaceScreen extends HandledScreen<UltimateFurnaceScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("ultimate_furnace", "textures/gui/container/ultimate_furnace.png");

	public UltimateFurnaceScreen(UltimateFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		this.backgroundWidth = 176; // Set background width
		this.backgroundHeight = 166; // Set background height
	}

	@Override
	public void renderBackground(MatrixStack matrices) {
		super.renderBackground(matrices);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		this.client.getTextureManager().bindTexture(TEXTURE);
		int x = (this.width - this.backgroundWidth) / 2;
		int y = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

		// Draw the progress arrow
		if (this.handler.isSmelting()) {
			int progress = this.handler.getSmeltingProgress();
			this.drawTexture(matrices, x + 79, y + 34, 176, 14, progress + 1, 16);
		}

		// Draw the fire indicator
		if (this.handler.hasFuel()) {
			int fuelHeight = this.handler.getFuelHeight();
			this.drawTexture(matrices, x + 56, y + 36 + 12 - fuelHeight, 176, 12 - fuelHeight, 14, fuelHeight + 1);
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
}
/*/
