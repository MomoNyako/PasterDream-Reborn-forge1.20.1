package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.FluidIngredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class SingleFluidMatcher
{
    public static FluidStack match(FluidIngredient fluidIngredient, FluidStack matchFluid)
    {
        if(fluidIngredient.isTag())
        {
            List<FluidStack> ListFluidStackFromTag = fluidIngredient.getListFluidStackFromTag();
            for(FluidStack fluidStack : ListFluidStackFromTag)
            {
                if(FluidIngredient.isSameFluidSameTags(fluidStack, matchFluid))
                {
                    return fluidStack;
                }
            }
            return FluidStack.EMPTY;
        }
            else
            {
                FluidStack fluidStack = fluidIngredient.getFluidStack();
                if(FluidIngredient.isSameFluidSameTags(fluidStack, matchFluid))
                {
                    return fluidStack;
                }
                    else
                    {
                        return FluidStack.EMPTY;
                    }
            }
    }
}
