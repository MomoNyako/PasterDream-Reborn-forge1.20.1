package com.pasterdream.pasterdreammod.recipe.dreamaccumulatorrecipe;

import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher.SingleItemMatcher;
import com.pasterdream.pasterdreammod.world.block.dreamaccumulator.DreamAccumulatorRecipe;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

import java.util.Collection;

public class DreamAccumulatorRecipeMatch
{
    public static DreamAccumulatorRecipeMatchedData matches(DreamAccumulatorRecipeMatchedData inventory, Collection<DreamAccumulatorRecipe> recipes)
    {
        boolean isHaveCatalyst = inventory.catalyst() != ItemStack.EMPTY;
        for(DreamAccumulatorRecipe recipe : recipes)
        {
            Holder<Biome> biome = MatchSingleBiome.Match(recipe.getBiomeTags(), inventory.biome());
            if(biome == null)
            {
                continue;
            }

            ItemStack catalyst = SingleItemMatcher.matchWithNotCompareNBT(recipe.getCatalyst(), inventory.catalyst());
            if(isHaveCatalyst && catalyst == null)
            {
                continue;
            }

            ItemStack output;
            if(recipe.getOutput() != null)
            {
                output = recipe.getOutput();
            }
                else
                {
                    continue;
                }

            return new DreamAccumulatorRecipeMatchedData(biome, recipe.getTime(), catalyst, recipe.getCatalystMultiplier(), output);
        }
        return null;
    }
}
