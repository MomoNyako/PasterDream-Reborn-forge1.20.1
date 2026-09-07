package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericprocessor;

import net.minecraft.world.item.ItemStack;

public class SingleItemProcessor
{
    public static ItemStack processor(ItemStack matchedItemStack, ItemStack machineItemStack, boolean isAdd)
    {
        int matchedItemCount = matchedItemStack.getCount();
        int machineItemCount = machineItemStack.getCount();

        ItemStack processedItemStack = machineItemStack.copy();

        if(isAdd)
        {
            if(ItemStack.isSameItemSameTags(matchedItemStack, machineItemStack))
            {
                int itemMaxStackSize = matchedItemStack.getMaxStackSize();
                if(machineItemCount + matchedItemCount <= itemMaxStackSize)
                {
                    processedItemStack.grow(matchedItemCount);
                    return processedItemStack;
                }
                    else
                    {
                        return null;
                    }
            }
            else
                if(machineItemStack.isEmpty())
                {
                    return matchedItemStack.copy();
                }
                    else
                    {
                        return null;
                    }
        }
            else
            {
                if(ItemStack.isSameItem(matchedItemStack, machineItemStack))
                {
                    if(machineItemCount >= matchedItemCount)
                    {
                        processedItemStack.shrink(matchedItemCount);
                        return processedItemStack;
                    }
                        else
                        {
                            return null;
                        }
                }
                    else
                    {
                        return null;
                    }
            }
    }
}
