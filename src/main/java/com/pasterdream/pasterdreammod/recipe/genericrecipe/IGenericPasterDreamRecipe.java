package com.pasterdream.pasterdreammod.recipe.genericrecipe;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.FluidIngredient;
import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Optional;

public interface IGenericPasterDreamRecipe
{
    List<FluidIngredient> getInputFluidIngredients();
    List<ItemIngredient> getInputItemIngredients();
    List<FluidIngredient> getOutputFluidIngredients();
    List<ItemIngredient> getOutputItemIngredients();
    int getRecipeTime();


    boolean matches(List<FluidStack> fluidStacks, List<ItemStack> itemStacks);
    Optional<GenericPasterDreamRecipeMatchResult> matchesWithSlots(List<FluidStack> fluidStacks, List<ItemStack> itemStacks);
}
