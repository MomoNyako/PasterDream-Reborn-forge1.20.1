package com.pasterdream.pasterdreammod.world.item.debugtool.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class NestableWidget extends AbstractWidget
{
    protected final List<AbstractWidget> childrenWidgets = new ArrayList<>();

    public NestableWidget(int x, int y, int width, int height, Component message)
    {
        super(x, y, width, height, message);
    }

    protected void addChildWidget(AbstractWidget childWidget)
    {
        childrenWidgets.add(childWidget);
    }

    protected void clearChildrenWidget()
    {
        childrenWidgets.clear();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        for (AbstractWidget child : childrenWidgets)
        {
            if (child.visible)
            {
                child.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }
    }

    protected void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        //在子类中@Override
    }

    //鼠标点击
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        for (int i = childrenWidgets.size() - 1; i >= 0; i--)
        {
            AbstractWidget child = childrenWidgets.get(i);
            if (child.visible && child.active && child.isMouseOver(mouseX, mouseY))
            {
                if (child.mouseClicked(mouseX, mouseY, button))
                {
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    //鼠标按住拖动
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        for (int i = childrenWidgets.size() - 1; i >= 0; i--)
        {
            AbstractWidget child = childrenWidgets.get(i);
            if (child.visible && child.active)
            {
                if (child.mouseDragged(mouseX, mouseY, button, dragX, dragY))
                {
                    return true;
                }
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    //松开鼠标
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        for (int i = childrenWidgets.size() - 1; i >= 0; i--)
        {
            AbstractWidget child = childrenWidgets.get(i);
            if (child.visible && child.active && child.isMouseOver(mouseX, mouseY))
            {
                if (child.mouseReleased(mouseX, mouseY, button))
                {
                    return true;
                }
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    //鼠标滚轮
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        for (int i = childrenWidgets.size() - 1; i >= 0; i--)
        {
            AbstractWidget child = childrenWidgets.get(i);
            if (child.visible && child.active && child.isMouseOver(mouseX, mouseY))
            {
                if (child.mouseScrolled(mouseX, mouseY, delta))
                {
                    return true;
                }
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    //鼠标移动
    @Override
    public void mouseMoved(double mouseX, double mouseY)
    {
        for (AbstractWidget child : childrenWidgets)
        {
            if (child.visible)
            {
                child.mouseMoved(mouseX, mouseY);
            }
        }
        super.mouseMoved(mouseX, mouseY);
    }

    //键盘点击
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        for (int i = childrenWidgets.size() - 1; i >= 0; i--)
        {
            AbstractWidget child = childrenWidgets.get(i);
            if (child.visible && child.active)
            {
                if (child.keyPressed(keyCode, scanCode, modifiers))
                {
                    return true;
                }
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    //字符输入
    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        for (int i = childrenWidgets.size() - 1; i >= 0; i--)
        {
            AbstractWidget child = childrenWidgets.get(i);
            if (child.visible && child.active)
            {
                if (child.charTyped(codePoint, modifiers))
                {
                    return true;
                }
            }
        }
        return super.charTyped(codePoint, modifiers);
    }

    //无障碍叙述
    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
    {

    }
}
