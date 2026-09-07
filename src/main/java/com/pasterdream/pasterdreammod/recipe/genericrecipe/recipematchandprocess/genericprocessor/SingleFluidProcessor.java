package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericprocessor;


import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.FluidIngredient;
import net.minecraftforge.fluids.FluidStack;

public class SingleFluidProcessor
{
    public static FluidStack processor(FluidStack matchedFluidStack, FluidStack machineFluidStack, boolean isAdd, int maxOutputTankCapacity)
    {
        int matchedFluidCount = matchedFluidStack.getAmount();
        int machineFluidCount = machineFluidStack.getAmount();

        FluidStack processedFluidStack = machineFluidStack.copy();

        if(isAdd)
        {
            if(FluidIngredient.isSameFluidSameTags(matchedFluidStack, machineFluidStack))
            {
                if(machineFluidCount + matchedFluidCount <= maxOutputTankCapacity)
                {
                    processedFluidStack.grow(matchedFluidCount);
                    return processedFluidStack;
                }
                    else
                    {
                        return null;
                    }
            }
                else
                {
                    return null;
                }
        }
            else
            {
                if(FluidIngredient.isSameFluidSameTags(matchedFluidStack, machineFluidStack))
                {
                    if(machineFluidCount >= matchedFluidCount)
                    {
                        processedFluidStack.shrink(matchedFluidCount);
                        return processedFluidStack;
                    }
                        else
                        {
                            return null;
                        }
                }
                    else
                    {
                        return null;
                    }
            }
    }
}
