package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemIngredient;
import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemStackWithoutCount;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemMatcher
{
    public static List<ItemStackWithoutCount> getInventoryItemTypes(List<ItemStack> inventoryItemStacks)
    {
        List<ItemStackWithoutCount> itemTypes = new ArrayList<>();
        for(ItemStack itemStack : inventoryItemStacks)
        {
            boolean needSkip = false;

            for(ItemStackWithoutCount itemStackWithoutCount : itemTypes)
            {
                if(ItemStackWithoutCount.isSameItem(itemStackWithoutCount, new ItemStackWithoutCount(itemStack.getItem(), itemStack.getTag())))
                {
                    needSkip = true;
                }
            }

            if(!itemStack.isEmpty() && !needSkip)
            {
                itemTypes.add(new ItemStackWithoutCount(itemStack.getItem(), itemStack.getTag()));
            }
        }
        return itemTypes;
    }

    public static List<ItemStack> matcher(List<ItemIngredient> recipeItemIngredients, List<ItemStackWithoutCount> inventoryItemTypes)
    {
        List<ItemStack> matchedItemStack = new ArrayList<>();
        List<ItemStackWithoutCount> remainingInventoryItemTypes = new ArrayList<>(inventoryItemTypes);

        for(ItemIngredient itemIngredient : recipeItemIngredients)
        {   //tag匹配
            if(itemIngredient.isTag())
            {
                List<ItemStack> listItemStackFromTag = itemIngredient.getListItemStackFromTag();
                boolean isMatched = false;
                for(ItemStack itemStack : listItemStackFromTag)
                {
                    ItemStackWithoutCount recipeItemType = new ItemStackWithoutCount(itemStack.getItem(), itemStack.getTag());
                    ItemStackWithoutCount needRemovedItem = recipeItemType;

                    for(ItemStackWithoutCount inputItemType : remainingInventoryItemTypes)
                    {
                        if (ItemStackWithoutCount.isSameItem(recipeItemType, inputItemType))
                        {
                            needRemovedItem = inputItemType;
                            isMatched = true;
                            matchedItemStack.add(itemStack);
                            break;
                        }
                    }

                    if(isMatched)
                    {
                        remainingInventoryItemTypes.remove(needRemovedItem);
                        break;
                    }
                }

                if(!isMatched)
                {
                    return List.of();
                }
            }
                else
                {   //物品匹配
                    ItemStack itemStack = itemIngredient.getItemStack();
                    boolean isMatched = false;
                    ItemStackWithoutCount recipeItemType = new ItemStackWithoutCount(itemStack.getItem(), itemStack.getTag());
                    ItemStackWithoutCount needRemovedItem = recipeItemType;

                    for(ItemStackWithoutCount inputItemType : remainingInventoryItemTypes)
                    {
                        if (ItemStackWithoutCount.isSameItem(recipeItemType, inputItemType))
                        {
                            needRemovedItem = inputItemType;
                            isMatched = true;
                            matchedItemStack.add(itemStack);
                            break;
                        }
                    }

                    if(!isMatched)
                    {
                        return List.of();
                    }
                        else
                        {
                            remainingInventoryItemTypes.remove(needRemovedItem);
                        }
                }
        }

        if (remainingInventoryItemTypes.isEmpty())
        {
            return matchedItemStack;
        }
            else
            {   //物品输入槽有配方以外的物品
                return List.of();
            }
    }
}
