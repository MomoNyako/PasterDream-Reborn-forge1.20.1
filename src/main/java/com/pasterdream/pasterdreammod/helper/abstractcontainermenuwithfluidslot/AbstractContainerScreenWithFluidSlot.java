package com.pasterdream.pasterdreammod.helper.abstractcontainermenuwithfluidslot;

import com.pasterdream.pasterdreammod.helper.renderhelper.RendFluidSlot;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractContainerScreenWithFluidSlot<T extends AbstractContainerMenuWithFluidSlot> extends AbstractContainerScreen<T>
{
    private static final long FLUID_SLOT_HINT_DURATION_MS = 3000L;
    private long fluidSlotHintEndTime = 0L;

    public AbstractContainerScreenWithFluidSlot(T menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        for (FluidSlot slot : menu.getFluidSlots())
        {
            if (isHovering(slot.x, slot.y, 18, 18, mouseX, mouseY) && slot.getFluid().isEmpty())
            {
                this.fluidSlotHintEndTime = Util.getMillis() + FLUID_SLOT_HINT_DURATION_MS;
                break;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {
        for (FluidSlot slot : menu.getFluidSlots())
        {
            RendFluidSlot.rendFluidSlot(guiGraphics, slot, leftPos + slot.x,  topPos + slot.y);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        for (FluidSlot slot : menu.getFluidSlots())
        {
            if (isHovering(slot.x, slot.y, 18, 18, mouseX, mouseY))
            {
                guiGraphics.fill(leftPos + slot.x + 1,  topPos + slot.y + 1, leftPos + slot.x + 17, topPos + slot.y + 17, 0x7FFFFFFF);
                break;
            }
        }

        if (Util.getMillis() < fluidSlotHintEndTime)
        {
            Component hint = Component.translatable("gui.pasterdream.fluid_slot.hint");
            guiGraphics.drawString(font, hint, (this.width - font.width(hint)) / 2, 4, 0xFFFFFF, true);
        }

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        for (FluidSlot slot : menu.getFluidSlots())
        {
            if (isHovering(slot.x, slot.y, 18, 18, mouseX, mouseY) && !slot.getFluid().isEmpty())
            {
                FluidStack fluidStack = slot.getFluid();
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(fluidStack.getDisplayName());
                ResourceLocation fluidId = BuiltInRegistries.FLUID.getKey(fluidStack.getFluid());
                tooltip.add(Component.literal("§8" + fluidId));
                tooltip.add(Component.literal("§7" + fluidStack.getAmount() + " mB"));
                String modId = fluidId.getNamespace();
                String modName = net.minecraftforge.fml.ModList.get().getModContainerById(modId).map(mod -> mod.getModInfo().getDisplayName()).orElse(modId);
                tooltip.add(Component.literal("§9§o" + modName));
                guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltip, Optional.empty(), mouseX, mouseY);
                break;
            }
        }
    }
}
