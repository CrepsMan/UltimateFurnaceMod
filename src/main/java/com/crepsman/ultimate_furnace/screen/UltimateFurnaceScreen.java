package com.crepsman.ultimate_furnace.screen;

import com.crepsman.ultimate_furnace.UltimateFurnaceMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.FurnaceRecipeBookScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UltimateFurnaceScreen extends AbstractFurnaceScreen<UltimateFurnaceScreenHandler> {
	private static final Identifier TEXTURE = new Identifier(UltimateFurnaceMod.MOD_ID, "textures/gui/container/ultimate_furnace.png");
	private static final int[] UPGRADE_THRESHOLDS = {100, 500, 1000, 5000, 10000, 15000};

	public UltimateFurnaceScreen(UltimateFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, new FurnaceRecipeBookScreen(), inventory, title, TEXTURE);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);

		// Draw the XP bar fill, scaled to the smelt progress percentage within the level
		int smeltCount = this.handler.getSmeltCount();
		int currentLevel = this.handler.getCurrentLevel();
		int maxSmeltCount = currentLevel < UPGRADE_THRESHOLDS.length ? UPGRADE_THRESHOLDS[currentLevel] : UPGRADE_THRESHOLDS[UPGRADE_THRESHOLDS.length - 1];

		if (maxSmeltCount > 0) {
			// Calculate the fill width based on percentage progress within the level
			double progressPercentage = (double) smeltCount / maxSmeltCount;
			int barWidth = (int) (progressPercentage * 161);

			// Ensure the bar width is within bounds
			barWidth = Math.min(barWidth, 161);
			barWidth = Math.max(barWidth, 0);

			// Set default shader color and draw XP bar
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexture(matrices, this.x + 7, this.y + 65, 0, 176, barWidth, 5);
		}
	}

	@Override
	protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
		super.drawForeground(matrices, mouseX, mouseY);

		// Get the smelt count from the handler
		int smeltCount = this.handler.getSmeltCount();
		int currentLevel = this.handler.getCurrentLevel();
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		super.drawMouseoverTooltip(matrices, x, y);

		// Show tooltip for the XP bar
		if (x >= this.x + 7 && x <= this.x + 107 && y >= this.y + 65 && y <= this.y + 70) {
			int smeltCount = this.handler.getSmeltCount();
			int currentLevel = this.handler.getCurrentLevel();
			int maxSmeltCount = currentLevel < UPGRADE_THRESHOLDS.length ? UPGRADE_THRESHOLDS[currentLevel] : UPGRADE_THRESHOLDS[UPGRADE_THRESHOLDS.length - 1];
			float progress = (float) smeltCount / maxSmeltCount * 100;

			String tooltipText = currentLevel >= UPGRADE_THRESHOLDS.length
					? "Max Level"
					: String.format("Smelted: %d/%d (%.2f%%)", smeltCount, maxSmeltCount, progress);

			this.renderTooltip(matrices, Text.of(tooltipText), x, y);
		}
	}
}
