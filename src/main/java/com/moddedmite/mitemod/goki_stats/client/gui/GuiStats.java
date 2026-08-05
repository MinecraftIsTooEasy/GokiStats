package com.moddedmite.mitemod.goki_stats.client.gui;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.client.GokiKeyHandler;
import com.moddedmite.mitemod.goki_stats.common.config.GokiConfig;
import com.moddedmite.mitemod.goki_stats.common.network.C2SStatModify;
import com.moddedmite.mitemod.goki_stats.common.network.GokiNetwork;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import net.minecraft.Minecraft;
import net.minecraft.FontRenderer;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;
import net.minecraft.EntityPlayer;
import org.lwjgl.util.vector.Vector2f;

public class GuiStats extends GuiScreen {
    public static final int STATUS_BUTTON_WIDTH = 24;
    public static final int STATUS_BUTTON_HEIGHT = 24;
    public static final int IMAGE_ROWS = 10;
    private static final int[] COLUMNS =
            {4, 4, 5, 4, 5, 4};
    public static float SCALE = 1.0F;

    private EntityPlayer player;
    private int currentColumn = 0;
    private int currentRow = 0;
    private GuiStatTooltip toolTip = null;
    private FontRenderer fontRenderer;

    public GuiStats(EntityPlayer player) {
        this.player = player;
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        int ttx = 0;
        int tty = 0;
        this.toolTip = null;
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, par3);
        for (int i = 0; i < this.buttonList.size(); i++) {
            if ((this.buttonList.get(i) instanceof GuiStatButton)) {
                GuiStatButton button = (GuiStatButton) this.buttonList.get(i);
                if (button.isUnderMouse(mouseX, mouseY)) {
                    this.toolTip = new GuiStatTooltip(StatBase.stats.get(i), this.player);
                    ttx = button.xPosition + 12;
                    tty = button.yPosition - 1;
                    break;
                }
            }
        }
        drawCenteredString(fontRenderer,
                I18n.getString("ui.currentxp.name") + DataHelper.getXPTotal(player) + "xp",
                width / 2,
                this.height - 16,
                0xFFFFFFFF);

        if (this.toolTip != null)
            this.toolTip.draw(ttx, tty, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        for (int stat = 0; stat < StatBase.totalStats; stat++) {
            Vector2f pos = getButton(stat);
            this.buttonList.add(new GuiStatButton(stat, (int) pos.x, (int) pos.y, 24, 24, StatBase.stats.get(stat), this.player));
            this.currentColumn += 1;
            if (this.currentColumn >= COLUMNS[this.currentRow]) {
                this.currentRow += 1;
                this.currentColumn = 0;
            }
            if (this.currentRow >= COLUMNS.length) {
                this.currentRow = (COLUMNS.length - 1);
            }
        }
    }

    private Vector2f getButton(int n) {
        Vector2f vec = new Vector2f();
        int columns = COLUMNS[this.currentRow];
        int x = n % columns;
        int y = this.currentRow;
        int rows = COLUMNS.length;
        float width = columns * 32 * SCALE;
        float height = rows * 36 * SCALE;
        vec.x = (width / columns * x + (this.width - width + 8.0F) / 2.0F);
        vec.y = (height / rows * y + (this.height - height + 12.0F) / 2.0F);
        return vec;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if ((button.id >= 0) && (button.id <= StatBase.totalStats)) {
            if ((button instanceof GuiStatButton)) {
                GuiStatButton statButton = (GuiStatButton) button;
                if (!GuiScreen.isCtrlKeyDown())
                    GokiNetwork.sendToServer(new C2SStatModify(StatBase.stats.indexOf(statButton.stat), 1));
                else // Downgrade
                    GokiNetwork.sendToServer(new C2SStatModify(StatBase.stats.indexOf(statButton.stat), -1));
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char c, int keyCode) {
        super.keyTyped(c, keyCode);
        if (c == 1 || (GokiConfig.keyBindingEnabled && GokiKeyHandler.statsMenu != null && keyCode == GokiKeyHandler.statsMenu.getKeyCode())) {
            mc.displayGuiScreen(null);
        }
    }
}