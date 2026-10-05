package net.hfstack.rallyguard.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/** E1-A: minimal screen for {@link PackMenu} - a dark panel with slot wells, no textures. */
public class PackScreen extends AbstractContainerScreen<PackMenu> {

    public PackScreen(PackMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 140;
        this.inventoryLabelY = 47;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        super.render(g, mouseX, mouseY, delta);
        this.renderTooltip(g, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics g, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        g.fill(x, y, x + this.imageWidth, y + this.imageHeight, 0xF0202020);
        g.fill(x, y, x + this.imageWidth, y + 1, 0xFFAAAAAA);
        g.fill(x, y + this.imageHeight - 1, x + this.imageWidth, y + this.imageHeight, 0xFFAAAAAA);
        g.fill(x, y, x + 1, y + this.imageHeight, 0xFFAAAAAA);
        g.fill(x + this.imageWidth - 1, y, x + this.imageWidth, y + this.imageHeight, 0xFFAAAAAA);
        for (Slot slot : this.menu.slots) {
            int sx = x + slot.x - 1;
            int sy = y + slot.y - 1;
            g.fill(sx, sy, sx + 18, sy + 18, 0xFF555555);
            g.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
        }
    }
}
