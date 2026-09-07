package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericprocessor;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.FluidIngredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FluidProcessor
{
    public static List<FluidStack> processor(List<FluidStack> matchedFluidStacks, List<FluidStack> machineFluidStacks, Boolean isAdd, int maxOutputTankCapacity)
    {
        List<FluidStack> remainingFluidStacks = new ArrayList<>(matchedFluidStacks);
        List<FluidStack> processedFluidStacks = new ArrayList<>(machineFluidStacks);

        for(int i = 0; i < matchedFluidStacks.size(); i++)
        {
            for(int j = 0; j < machineFluidStacks.size(); j++)
            {
                FluidStack matchedFluidStack = remainingFluidStacks.get(i);
                int matchedFluidAmount = matchedFluidStack.getAmount();

                FluidStack machineFluidStack = processedFluidStacks.get(j);
                int machineFluidAmount = machineFluidStack.getAmount();

                if(isAdd)
                {
                    if(FluidIngredient.isSameFluidSameTags(matchedFluidStack, machineFluidStack) || machineFluidStack.isEmpty())
                    {
                        if(machineFluidAmount + matchedFluidAmount <= maxOutputTankCapacity)
                        {
                            machineFluidStack.grow(matchedFluidAmount);
                            remainingFluidStacks.set(i, FluidStack.EMPTY);
                            break;
                        }
                            else
                            {
                                machineFluidStack.setAmount(maxOutputTankCapacity);
                                matchedFluidStack.shrink(maxOutputTankCapacity - machineFluidAmount);
                            }
                    }
                }
                    else
                    {
                        if(FluidIngredient.isSameFluidSameTags(matchedFluidStack, machineFluidStack))
                        {
                            if(machineFluidAmount >= matchedFluidAmount)
                            {
                                machineFluidStack.shrink(matchedFluidAmount);
                                remainingFluidStacks.set(i, FluidStack.EMPTY);
                                break;
                            }
                                else
                                {
                                    processedFluidStacks.set(j, FluidStack.EMPTY);
                                    matchedFluidStack.shrink(machineFluidAmount);
                                }
                        }
                    }
            }

            if (!remainingFluidStacks.get(i).isEmpty())
            {
                return null;
            }
        }

        return processedFluidStacks;
    }
}
