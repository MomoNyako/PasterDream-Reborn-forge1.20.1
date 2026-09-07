package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemIngredient;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SingleItemMatcher
{
    public static ItemStack matchWithNotCompareNBT(ItemIngredient itemIngredient, ItemStack matchItem)
    {
        if(itemIngredient.isTag())
        {
            List<ItemStack> ListItemStackFromTag = itemIngredient.getListItemStackFromTag();
            for(ItemStack itemStack : ListItemStackFromTag)
            {
                if(ItemStack.isSameItem(itemStack, matchItem))
                {
                    return itemStack;
                }
            }
            return ItemStack.EMPTY;
        }
            else
            {
                ItemStack itemStack = itemIngredient.getItemStack();
                if(ItemStack.isSameItem(itemStack, matchItem))
                {
                    return itemStack;
                }
                    else
                    {
                        return ItemStack.EMPTY;
                    }
            }
    }

    public static ItemStack matchWithCompareNBT(ItemIngredient itemIngredient, ItemStack matchItem)
    {
        if(itemIngredient.isTag())
        {
            List<ItemStack> ListItemStackFromTag = itemIngredient.getListItemStackFromTag();
            for(ItemStack itemStack : ListItemStackFromTag)
            {
                if(ItemStack.isSameItemSameTags(itemStack, matchItem))
                {
                    return itemStack;
                }
            }
            return ItemStack.EMPTY;
        }
            else
            {
                ItemStack itemStack = itemIngredient.getItemStack();
                if(ItemStack.isSameItemSameTags(itemStack, matchItem))
                {
                    return itemStack;
                }
                    else
                    {
                        return ItemStack.EMPTY;
                    }
            }
    }


}
