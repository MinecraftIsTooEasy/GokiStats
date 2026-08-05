package com.moddedmite.mitemod.goki_stats.client.gui;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import com.moddedmite.mitemod.goki_stats.common.utils.Reference;
import net.minecraft.Minecraft;
import net.minecraft.FontRenderer;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.EntityPlayer;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class GuiStatButton extends GuiButton {
    public StatBase stat;
    public EntityPlayer player;

    public static final int INACTIVE_X = 0;
    public static final int ACTIVATED_X = 24;
    public static final int DISABLED_X = 48;
    public static final int MAXIMUM_X = 72;

    public GuiStatButton(int id, int x, int y, int width, int height, StatBase stat, EntityPlayer player) {
        super(id, x, y, width, height, "");
        this.stat = stat;
        this.player = player;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY) {
        if (this.drawButton) {
            int iconY = 24 * (this.stat.imageID % 10);
            int level = DataHelper.getPlayerStatLevel(this.player, this.stat);
            int cost = this.stat.getCost(level);
            int playerXP = DataHelper.getXPTotal(this.player);

            FontRenderer fontrenderer = mc.fontRenderer;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean hovered = isUnderMouse(mouseX, mouseY);
            int hoverState = getHoverState(hovered);

            int iconX = INACTIVE_X;
            if (hoverState == 2) { // Hovering
                iconX = ACTIVATED_X;
            }
            if (playerXP < cost) {
                iconX = INACTIVE_X;
            }
            if (level >= this.stat.getLimit()) {
                iconX = MAXIMUM_X;
            }
            if (GuiScreen.isCtrlKeyDown()) {
                if (DataHelper.canPlayerRevertStat(player, this.stat))
                    iconX = ACTIVATED_X;
                else
                    iconX = DISABLED_X;
            }
            if (!stat.enabled)
                iconX = DISABLED_X;

            String message = level + "";
            if (!this.stat.enabled) {
                iconX = 48;
                message = "X";
            }

            int messageColor = 16777215;
            if (level >= this.stat.getLimit()) {
                message = "*" + level + "*";
                messageColor = 16763904;
            }

            iconX += this.stat.imageID % 20 / 10 * 24 * 4;

            if (this.stat.imageID >= 20) {
                mc.getTextureManager().bindTexture(Reference.RPG_ICON_2_TEXTURE_LOCATION);
            } else {
                mc.getTextureManager().bindTexture(Reference.RPG_ICON_TEXTURE_LOCATION);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(this.xPosition, this.yPosition, 0.0F);
            GL11.glScalef(GuiStats.SCALE, GuiStats.SCALE, 0.0F);
            drawTexturedModalRect(0, 0, iconX, iconY, this.width, this.height);

            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslatef(this.xPosition, this.yPosition, 0.0F);
            drawCenteredString(fontrenderer,
                    message,
                    (int) (this.width / 2 * GuiStats.SCALE),
                    (int) (this.height * GuiStats.SCALE) + 2,
                    messageColor);
            GL11.glPopMatrix();
        }
    }

    public boolean isUnderMouse(int mouseX, int mouseY) {
        return (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width * GuiStats.SCALE) && (mouseY < this.yPosition + this.height * GuiStats.SCALE);
    }

    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int mouseX, int mouseY) {
        return (this.enabled) && (this.drawButton) && (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < this.xPosition + this.width * GuiStats.SCALE) && (mouseY < this.yPosition + this.height * GuiStats.SCALE);
    }

    public String getHoverMessage(int which) {
        if (which == 0) {
            return this.stat.getLocalizedName() + " L" + DataHelper.getPlayerStatLevel(this.player,
                    this.stat);
        }

        return this.stat.getLocalizedDescription(this.player);
    }
}
