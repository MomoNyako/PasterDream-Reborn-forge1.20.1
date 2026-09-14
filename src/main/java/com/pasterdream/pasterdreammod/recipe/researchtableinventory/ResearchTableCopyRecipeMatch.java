package com.pasterdream.pasterdreammod.recipe.researchtableinventory;

import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher.SingleFluidMatcher;
import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher.SingleItemMatcher;
import com.pasterdream.pasterdreammod.world.block.researchtable.ResearchTableCopyRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;

public class ResearchTableCopyRecipeMatch
{
    public static ResearchTableCopyRecipeInventory matches(ResearchTableCopyRecipeInventory inventory, Collection<ResearchTableCopyRecipe> recipes)
    {
        FluidStack matchedFluidInput;
        ItemStack matchedPen;
        ItemStack matchedSourceBook;
        ItemStack matchedMaterial;

        for(ResearchTableCopyRecipe recipe : recipes)
        {
            matchedFluidInput = SingleFluidMatcher.match(recipe.getFluidInput(), inventory.fluidStack());
            if(matchedFluidInput.isEmpty())
            {
                continue;
            }

            matchedPen = SingleItemMatcher.matchWithNotCompareNBT(recipe.getPen(), inventory.pen());
            if(matchedPen.isEmpty())
            {
                continue;
            }

            matchedSourceBook = SingleItemMatcher.matchWithCompareContainerNBT(recipe.getSourceBook(), inventory.sourceBook());
            if(matchedSourceBook.isEmpty())
            {
                continue;
            }

            matchedMaterial = SingleItemMatcher.matchWithCompareContainerNBT(recipe.getMaterial(), inventory.material());
            if(matchedMaterial.isEmpty())
            {
                continue;
            }

            return new ResearchTableCopyRecipeInventory(matchedFluidInput, matchedPen, matchedSourceBook, matchedMaterial);
        }
        return null;
    }
}
