package com.pasterdream.pasterdreammod.recipe.weaponworkshopcraftingtable;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemStackWithoutCount;
import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher.SingleItemMatcher;
import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher.ItemMatcher;
import com.pasterdream.pasterdreammod.world.block.weaponworkshop.craftingtable.WeaponWorkshopCraftingTableRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WeaponWorkshopCraftingTableRecipeMatcher
{
    public static WeaponWorkshopCraftingTableRecipeInventory matches(WeaponWorkshopCraftingTableRecipeInventory inventory, Collection<WeaponWorkshopCraftingTableRecipe> recipes)
    {
        List<ItemStack> matchedInputItemStacks = new ArrayList<>();
        ItemStack MatchedEnhanceStone = ItemStack.EMPTY;
        ItemStack MatchedOutputItemStack = ItemStack.EMPTY;

        boolean isMatched = false;

        List<ItemStack> machineInputItemStack = inventory.inputItemStacks();
        List<ItemStackWithoutCount> inputInventoryItemTypes = ItemMatcher.getInventoryItemTypes(machineInputItemStack);

        for(WeaponWorkshopCraftingTableRecipe recipe : recipes)
        {
            matchedInputItemStacks = ItemMatcher.matcher(recipe.getInputItemIngredients(), inputInventoryItemTypes);

            if(!matchedInputItemStacks.isEmpty())
            {
                isMatched = true;
                MatchedEnhanceStone = SingleItemMatcher.matchWithNotCompareNBT(recipe.getEnhanceStone(), inventory.enhanceStone());
                MatchedOutputItemStack = recipe.getOutputItemIngredients().getItemStack();
                break;
            }
        }

        if (isMatched)
        {
            return new WeaponWorkshopCraftingTableRecipeInventory(matchedInputItemStacks, MatchedEnhanceStone, MatchedOutputItemStack);
        }
            else
            {
                return null;
            }
    }
}
