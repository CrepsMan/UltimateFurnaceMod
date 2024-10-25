package com.crepsman.ultimate_furnace.screen;

import com.crepsman.ultimate_furnace.UltimateFurnaceMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.FurnaceRecipeBookScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UltimateFurnaceScreen extends AbstractFurnaceScreen<UltimateFurnaceScreenHandler> {
	private static final Identifier TEXTURE = new Identifier(UltimateFurnaceMod.MOD_ID, "textures/gui/container/ultimate_furnace.png");

	public UltimateFurnaceScreen(UltimateFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, new FurnaceRecipeBookScreen(), inventory, title, TEXTURE);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);

		// Correctly position and scale the cook progress arrow
		int cookProgress = this.handler.getCookProgress();
		if (cookProgress > 0) {
			int progressWidth = (cookProgress * 24) / 100; // Scale the width based on cookProgress (24 is the full width of the arrow)
			this.drawTexture(matrices, this.x + 79, this.y + 34, 176, 14, progressWidth + 1, 16);
		}

		// Correctly position and scale the fire icon
		if (this.handler.isBurning()) {
			int fuelProgress = this.handler.getFuelProgress();
			this.drawTexture(matrices, this.x + 56, this.y + 36 + 12 - fuelProgress, 176, 12 - fuelProgress, 14, fuelProgress + 1);
			this.drawTexture(matrices, this.x + 55, this.y + 52, 176, 31, 18, 5);
		}
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		super.drawForeground(matrices, mouseX, mouseY);

		// Get the smelt count from the handler
		int smeltCount = this.handler.getSmeltCount();

		// Check if the smelt count has reached the maximum threshold
		String smeltCountText = smeltCount >= 15000 ? "Smelted: Max" : "Smelted: " + smeltCount;

		// Draw the smelt count slightly higher to avoid overlapping with inventory text
		this.textRenderer.draw(matrices, smeltCountText, 8, this.backgroundHeight - 104, 4210752); // Adjusted position
	}


}
