package com.pasterdream.pasterdreammod.recipe.weaponworkshopcraftingtable;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemIngredient;

import java.util.List;

public interface IWeaponWorkshopCraftingTableRecipe
{
    List<ItemIngredient> getInputItemIngredients();
    ItemIngredient getEnhanceStone();
    ItemIngredient getOutputItemIngredients();
}
