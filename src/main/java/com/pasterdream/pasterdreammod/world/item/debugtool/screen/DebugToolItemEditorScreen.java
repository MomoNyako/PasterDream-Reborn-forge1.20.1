package com.pasterdream.pasterdreammod.world.item.debugtool.screen;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.pasterdream.pasterdreammod.helper.abstractcontainermenuwithfluidslot.AbstractContainerScreenWithFluidSlot;
import com.pasterdream.pasterdreammod.helper.renderhelper.GUIBackGroundRender;
import com.pasterdream.pasterdreammod.helper.stringhelper.ListStringFromCompoundTag;
import com.pasterdream.pasterdreammod.helper.stringhelper.StringHelper;
import com.pasterdream.pasterdreammod.init.ModNetwork;
import com.pasterdream.pasterdreammod.network.menu.SetSlotNbtPacket;
import com.pasterdream.pasterdreammod.world.item.debugtool.menu.DebugToolItemEditorMenu;
import com.pasterdream.pasterdreammod.world.item.debugtool.widget.NBTPreviewWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DebugToolItemEditorScreen extends AbstractContainerScreenWithFluidSlot<DebugToolItemEditorMenu>
{
    private NBTPreviewWidget nbtPreviewWidget;

    public DebugToolItemEditorScreen(DebugToolItemEditorMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init()
    {
        imageWidth = width;
        imageHeight = height;
        super.init();

        for (Slot slot : menu.slots)
        {
            int index = slot.index;
            if(index >= 0 && index <= 8)
            {
                slot.x = width / 2 - 80 + 18 * index;
                slot.y = height - 21;
            }
            else
                if(index >= 9 && index <= 35)
                {
                    slot.x = width / 2 - 80 + 18 * ((index - 9) % 9);
                    slot.y = height - 79 + (index - 9) / 9 * 18;
                }
                else
                    if(index == 36)
                    {
                        slot.x = width / 2 - 51;
                        slot.y = height / 4 - 29;
                    }
        }

        CompoundTag NBT = menu.getSlot(36).getItem().getTag();
        nbtPreviewWidget = new NBTPreviewWidget(5, 5, width / 2 - 95, height - 26, StringHelper.ListStringFromString(NBT == null ? "" : NBT.toString(), width / 2 - 104));
        menu.addEditorSlotListener(itemStack ->
        {
            CompoundTag nbt = itemStack == null ? null : itemStack.getTag();
            nbtPreviewWidget.setListString(StringHelper.ListStringFromString(nbt == null ? "" : nbt.toString(), width / 2 - 104));
        });

        addRenderableWidget(nbtPreviewWidget);

        Button NBTEditorButton = Button.builder(Component.translatable("button.pasterdream.编辑NBT"), button ->
        {
            CompoundTag nbt = menu.getSlot(36).getItem().getTag();
            Minecraft.getInstance().setScreen(new DebugToolNBTEditorScreen(this, nbt == null ? "" : nbt.toString(), savedText ->
            {
                CompoundTag parsed;
                try
                {
                    parsed = savedText.isBlank() ? null : TagParser.parseTag(savedText);
                }
                    catch (CommandSyntaxException e)
                    {
                        return Component.translatable("error.pasterdream.invalid_nbt", e.getMessage());
                    }

                ModNetwork.CHANNEL.sendToServer(new SetSlotNbtPacket(36, parsed));
                return null;
            }));
        }).pos(5, height - 21).size(width / 2 - 95, 16).build();

        addRenderableWidget(NBTEditorButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {
        GUIBackGroundRender.rendMinecraftGUIBackground(guiGraphics, width / 2 - 85, 0, 85, height / 2 - 42);
        GUIBackGroundRender.rendMinecraftGUIBackground(guiGraphics, width / 2, 0, 85, height / 2 - 42);
        GUIBackGroundRender.rendMinecraftGUIBackground(guiGraphics, width / 2 - 85, height / 2 - 42, 85, height / 2 - 42);
        GUIBackGroundRender.rendMinecraftGUIBackground(guiGraphics, width / 2, height / 2 - 42, 85, height / 2 - 42);

        GUIBackGroundRender.rendPasterDreamInventoryGUI(guiGraphics, width / 2 - 85, height - 84);

        GUIBackGroundRender.rendMinecraftGUIBackground(guiGraphics, 0, 0, width / 2 - 85, height);
        GUIBackGroundRender.rendMinecraftGUIBackground(guiGraphics, width / 2 + 85, 0, width / 2 - 85, height);

        GUIBackGroundRender.rendMinecraftSingleSlot(guiGraphics, width / 2 - 52, height / 4 - 30);

        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        if (nbtPreviewWidget != null && button == 0 && nbtPreviewWidget.mouseDragged(mouseX, mouseY, button, dragX, dragY))
        {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {

    }
}
