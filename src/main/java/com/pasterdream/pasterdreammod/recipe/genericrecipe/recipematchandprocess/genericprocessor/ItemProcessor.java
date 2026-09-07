package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericprocessor;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemProcessor
{
    public static List<ItemStack> processor(List<ItemStack> matchedItemStacks, List<ItemStack> machineItemStacks, Boolean isAdd)
    {
        List<ItemStack> remainingItemStacks = new ArrayList<>(matchedItemStacks);
        List<ItemStack> processedItemStacks = new ArrayList<>(machineItemStacks);

        for(int i = 0; i < matchedItemStacks.size(); i++)
        {
            for(int j = 0; j < machineItemStacks.size(); j++)
            {
                ItemStack matchedItemStack = remainingItemStacks.get(i);
                int matchedItemCount = matchedItemStack.getCount();

                ItemStack machineItemStack = processedItemStacks.get(j);
                int machineItemCount = machineItemStack.getCount();

                if(isAdd)
                {
                    if(ItemStack.isSameItemSameTags(matchedItemStack, machineItemStack) || machineItemStack.isEmpty())
                    {
                        int itemMaxStackSize = matchedItemStack.getMaxStackSize();
                        if(machineItemCount + matchedItemCount <= itemMaxStackSize)
                        {
                            machineItemStack.grow(matchedItemCount);
                            remainingItemStacks.set(i, ItemStack.EMPTY);
                            break;
                        }
                            else
                            {
                                machineItemStack.setCount(itemMaxStackSize);
                                matchedItemStack.shrink(itemMaxStackSize - machineItemCount);
                            }
                    }
                }
                    else
                    {
                        if(ItemStack.isSameItem(matchedItemStack, machineItemStack))
                        {
                            if(machineItemCount >= matchedItemCount)
                            {
                                machineItemStack.shrink(matchedItemCount);
                                remainingItemStacks.set(i, ItemStack.EMPTY);
                                break;
                            }
                                else
                                {
                                    processedItemStacks.set(j, ItemStack.EMPTY);
                                    matchedItemStack.shrink(machineItemCount);
                                }
                        }
                    }
            }

            if (!remainingItemStacks.get(i).isEmpty())
            {
                return null;
            }
        }

        return processedItemStacks;
    }
}
