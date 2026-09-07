package com.pasterdream.pasterdreammod.compat.jei.weaponworkshopcraftingtablerecipe;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemIngredient;
import com.pasterdream.pasterdreammod.world.block.weaponworkshop.craftingtable.WeaponWorkshopCraftingTableRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WeaponWorkshopCraftingTableJEIRecipe
{
    private final List<ItemIngredient> inputItemIngredients;
    private final ItemIngredient enhanceStoneItemIngredient;
    private final ItemIngredient outputItemIngredient;

    public WeaponWorkshopCraftingTableJEIRecipe(WeaponWorkshopCraftingTableRecipe recipe)
    {
        this.inputItemIngredients = recipe.getInputItemIngredients() != null ? recipe.getInputItemIngredients() : List.of();
        this.enhanceStoneItemIngredient = recipe.getEnhanceStone() != null ? recipe.getEnhanceStone() : ItemIngredient.of(ItemStack.EMPTY.getItem(), 1);
        this.outputItemIngredient = recipe.getOutputItemIngredients();
    }

    public List<ItemIngredient> getInputItemIngredients()
    {
        return inputItemIngredients;
    }

    public ItemIngredient getEnhanceStoneItemIngredients()
    {
        return enhanceStoneItemIngredient;
    }

    public ItemIngredient getOutputItemIngredients()
    {
        return outputItemIngredient;
    }
}
