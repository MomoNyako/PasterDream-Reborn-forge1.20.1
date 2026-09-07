package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.FluidIngredient;
import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.FluidStackWithoutAmount;
import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemIngredient;
import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemStackWithoutCount;
import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher.FluidMatcher;
import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher.ItemMatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GenericRecipeMatcher
{
    @Nullable
    public static <T extends IGenericRecipe> GenericRecipeInventory match(List<ItemStack> inputItems, List<FluidStack> inputFluids, Collection<T> recipes)
    {
        boolean isEmpty = true;
        for(ItemStack itemStack : inputItems)
        {
            if(itemStack != ItemStack.EMPTY)
            {
                isEmpty = false;
                break;
            }
        }

        if(isEmpty)
        {
            for(FluidStack fluidStack : inputFluids)
            {
                if(fluidStack != FluidStack.EMPTY)
                {
                    isEmpty = false;
                    break;
                }
            }
        }

        if(isEmpty)
        {
            return null;
        }

        List<ItemStack> matchedInputItemStacks = new ArrayList<>();
        List<FluidStack> matchedInputFluidStacks = new ArrayList<>();
        List<ItemStack> matchedOutputItemStacks = new ArrayList<>();
        List<FluidStack> matchedOutputFluidStacks = new ArrayList<>();
        int recipeTime = 0;

        List<ItemStackWithoutCount> inputInventoryItemTypes = ItemStackWithoutCount.fromListItemStacks(inputItems);
        List<FluidStackWithoutAmount> inputInventoryFluidTypes = FluidStackWithoutAmount.fromListFluidStack(inputFluids);

        boolean isMatched = false;

        for(T recipe : recipes)
        {
            matchedInputItemStacks = ItemMatcher.matcher(recipe.getInputItems(), inputInventoryItemTypes);

            if(matchedInputItemStacks.isEmpty() && !recipe.getInputItems().isEmpty())
            {
                continue;
            }

            matchedInputFluidStacks = FluidMatcher.matcher(recipe.getInputFluids(), inputInventoryFluidTypes);

            if(matchedInputFluidStacks.isEmpty() && !recipe.getInputFluids().isEmpty())
            {
                continue;
            }

            isMatched = true;

            for(ItemIngredient itemIngredient : recipe.getOutputItems())
            {
                matchedOutputItemStacks.add(itemIngredient.getItemStack());
            }

            for(FluidIngredient fluidIngredient : recipe.getOutputFluids())
            {
                matchedOutputFluidStacks.add(fluidIngredient.getFluidStack());
            }

            recipeTime = recipe.getRecipeTime();
            break;
        }

        if(isMatched)
        {
            return new GenericRecipeInventory(matchedInputItemStacks, matchedInputFluidStacks, matchedOutputItemStacks, matchedOutputFluidStacks, recipeTime, 2147483647);
        }
            else
            {
                return null;
            }
    }
}
