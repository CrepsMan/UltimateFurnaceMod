package com.crepsman.ultimate_furnace.screen;

import com.crepsman.ultimate_furnace.UltimateFurnaceMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.FurnaceRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UltimateFurnaceScreen extends AbstractFurnaceScreen<UltimateFurnaceScreenHandler> {
	private static final Identifier TEXTURE = Identifier.tryParse(UltimateFurnaceMod.MOD_ID, "textures/gui/container/ultimate_furnace.png");

	// Additional Identifiers as required by the updated constructor
	private static final Identifier RECIPE_BOOK_TEXTURE = Identifier.tryParse("minecraft", "textures/gui/recipe_book.png");
	private static final Identifier FUEL_BACKGROUND_TEXTURE = Identifier.tryParse("minecraft", "textures/gui/fuel_background.png");

	public UltimateFurnaceScreen(UltimateFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, new FurnaceRecipeBookScreen(), inventory, title, TEXTURE, RECIPE_BOOK_TEXTURE, FUEL_BACKGROUND_TEXTURE);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context, mouseX, mouseY, delta); // Updated to include all required parameters
		super.render(context, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

		if (this.handler.isBurning() && this.handler.getCookProgress() > 0) {
			int cookProgress = (int) this.handler.getCookProgress(); // Explicitly cast to int
			int progressWidth = (cookProgress * 24) / 100;
			context.drawTexture(TEXTURE, this.x + 79, this.y + 34, 176, 14, progressWidth + 1, 16);
		}

		if (this.handler.isBurning()) {
			int fuelProgress = (int) this.handler.getFuelProgress(); // Explicitly cast to int
			context.drawTexture(TEXTURE, this.x + 56, this.y + 36 + 12 - fuelProgress, 176, 12 - fuelProgress, 14, fuelProgress + 1);
			context.drawTexture(TEXTURE, this.x + 55, this.y + 52, 176, 31, 18, 5);
		}
	}

	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
		super.drawForeground(context, mouseX, mouseY);

		int smeltCount = (int) this.handler.getSmeltCount(); // Explicitly cast to int
		String smeltCountText = smeltCount >= 15000 ? "Smelted: Max" : "Smelted: " + smeltCount;
		context.drawText(this.textRenderer, smeltCountText, 8, this.backgroundHeight - 104, 4210752, false);
	}
}
