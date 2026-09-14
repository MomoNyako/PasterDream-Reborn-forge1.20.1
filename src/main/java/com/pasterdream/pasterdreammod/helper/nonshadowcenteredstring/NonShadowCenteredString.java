package com.pasterdream.pasterdreammod.helper.nonshadowcenteredstring;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class NonShadowCenteredString
{
    public static void drawCenteredStringWithOutShadow(GuiGraphics guiGraphics, int x, int y, String string, int color)
    {
        int width = Minecraft.getInstance().font.width(string);
        int height = Minecraft.getInstance().font.lineHeight;
        guiGraphics.drawString(Minecraft.getInstance().font, string, x - width / 2, y - height / 2, color, false);
    }
}
