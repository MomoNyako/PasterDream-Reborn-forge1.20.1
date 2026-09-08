package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.FluidIngredient;
import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemIngredient;

import java.util.List;

public interface IGenericRecipe
{
    List<ItemIngredient> getInputItems();
    List<FluidIngredient> getInputFluids();
    List<ItemIngredient> getOutputItems();
    List<FluidIngredient> getOutputFluids();
    int getRecipeTime();
}
