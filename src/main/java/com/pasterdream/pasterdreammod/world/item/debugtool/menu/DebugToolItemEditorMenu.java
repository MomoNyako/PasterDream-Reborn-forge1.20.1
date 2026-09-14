package com.pasterdream.pasterdreammod.world.item.debugtool.menu;

import com.pasterdream.pasterdreammod.helper.abstractcontainermenuwithfluidslot.AbstractContainerMenuWithFluidSlot;
import com.pasterdream.pasterdreammod.helper.abstractcontainermenuwithfluidslot.IFluidContainer;
import com.pasterdream.pasterdreammod.init.ModMenus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class DebugToolItemEditorMenu extends AbstractContainerMenuWithFluidSlot
{
    private final Container editorContainer;

    public DebugToolItemEditorMenu(int id, Inventory playerInventory)
    {
        super(ModMenus.DEBUG_TOOL_ITEM_EDITOR.get(), id);

        for (int col = 0; col < 9; col++)
        {
            addSlot(new Slot(playerInventory, col, 5 + col * 18, 217));
        }

        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 5 + col * 18, 159 + row * 18));
            }
        }

        this.editorContainer = new SimpleContainer(1);
        addSlot(new Slot(editorContainer, 0, 189, 31)
        {
            @Override
            public void set(ItemStack stack)
            {
                super.set(stack);
                for (Consumer<ItemStack> consumer : editorSlotListeners)
                {
                    consumer.accept(stack);
                }
            }
        });


    }

    private final List<Consumer<ItemStack>> editorSlotListeners = new CopyOnWriteArrayList<>();

    public void addEditorSlotListener(Consumer<ItemStack> listener)
    {
        editorSlotListeners.add(listener);
    }

    public void clearEditorSlotListeners()
    {
        editorSlotListeners.clear();
    }

    @Override
    public void removed(Player player)
    {
        super.removed(player);
        if (!player.level().isClientSide)
        {
            clearContainer(editorContainer, player);
        }
    }

    private void clearContainer(Container container, Player player)
    {
        for (int i = 0; i < container.getContainerSize(); i++)
        {
            ItemStack stack = container.removeItemNoUpdate(i);
            if (!stack.isEmpty())
            {
                if (!player.getInventory().add(stack))
                {
                    player.drop(stack, false);
                }
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        Slot slot = slots.get(index);
        if (!slot.hasItem())
        {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        if ((index >= 36))
        {   //从机器移出到背包
            if (!this.moveItemStackTo(stack, 0, 36, false))
            {
                return ItemStack.EMPTY;
            }
        }
        else
            if((index >= 0 && index <= 35))
            {   //从背包移入输入槽
                if (!this.moveItemStackTo(stack, 36, 37, false))
                {
                    return ItemStack.EMPTY;
                }
            }

        if (stack.isEmpty())
        {
            slot.set(ItemStack.EMPTY);
        }
            else
            {
                slot.setChanged();
            }
        return copy;
    }

    @Override
    protected IFluidContainer getFluidContainer()
    {
        return null;
    }

    @Override
    public boolean stillValid(Player player)
    {
        return true;
    }
}
