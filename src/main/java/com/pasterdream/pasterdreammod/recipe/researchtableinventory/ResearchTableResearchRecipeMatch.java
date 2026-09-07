package com.pasterdream.pasterdreammod.recipe.researchtableinventory;

import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher.SingleFluidMatcher;
import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher.SingleItemMatcher;
import com.pasterdream.pasterdreammod.world.block.researchtable.ResearchTableResearchRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;

public class ResearchTableResearchRecipeMatch
{
    public static ResearchTableResearchRecipeInventory matches(ResearchTableResearchRecipeInventory inventory, Collection<ResearchTableResearchRecipe> recipes)
    {
        FluidStack matchedFluidInput;
        ItemStack matchedPen;
        ItemStack matchedUnresolvedNote;
        ItemStack matchedOutput;

        for(ResearchTableResearchRecipe recipe : recipes)
        {
            matchedFluidInput = SingleFluidMatcher.match(recipe.getFluidInput(), inventory.fluidStack());
            if(matchedFluidInput == null)
            {
                continue;
            }

            matchedPen = SingleItemMatcher.matchWithNotCompareNBT(recipe.getPen(), inventory.pen());
            if(matchedPen == null)
            {
                continue;
            }

            matchedUnresolvedNote = SingleItemMatcher.matchWithNotCompareNBT(recipe.getUnresolvedNote(), inventory.unresolvedNote());
            if(matchedUnresolvedNote == null)
            {
                continue;
            }

            matchedOutput = recipe.getOutput().getItemStack();

            return new ResearchTableResearchRecipeInventory(matchedFluidInput, matchedPen, matchedUnresolvedNote, matchedOutput);
        }
        return null;

    }
}
