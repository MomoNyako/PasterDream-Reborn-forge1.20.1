package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericmatcher;

import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.FluidIngredient;
import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.FluidStackWithoutAmount;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class FluidMatcher
{
    public static List<FluidStackWithoutAmount> getInventoryFluidTypes(List<FluidStack> inventoryFluidStacks)
    {
        List<FluidStackWithoutAmount> fluidTypes = new ArrayList<>();
        for(FluidStack fluidStack : inventoryFluidStacks)
        {
            boolean needSkip = false;

            for(FluidStackWithoutAmount fluidStackWithoutAmount : fluidTypes)
            {
                if(FluidStackWithoutAmount.isSame(fluidStackWithoutAmount, new FluidStackWithoutAmount(fluidStack.getFluid(), fluidStack.getTag())))
                {
                    needSkip = true;
                }
            }

            if(!fluidStack.isEmpty() && !needSkip)
            {
                fluidTypes.add(new FluidStackWithoutAmount(fluidStack.getFluid(), fluidStack.getTag()));
            }
        }
        return fluidTypes;
    }

    public static List<FluidStack> matcher(List<FluidIngredient> recipeFluidIngredients, List<FluidStackWithoutAmount> inventoryFluidTypes)
    {
        List<FluidStack> matchedFluidStack = new ArrayList<>();
        List<FluidStackWithoutAmount> remainingInventoryFluidTypes = new ArrayList<>(inventoryFluidTypes);

        for(FluidIngredient fluidIngredient : recipeFluidIngredients)
        {
            if(fluidIngredient.isTag())
            {   //fluidTag匹配
                List<FluidStack> listFluidStackFromTag = fluidIngredient.getListFluidStackFromTag();
                boolean isMatched = false;
                for(FluidStack fluidStack : listFluidStackFromTag)
                {
                    FluidStackWithoutAmount recipeFluidType = new FluidStackWithoutAmount(fluidStack.getFluid(), fluidStack.getTag());
                    FluidStackWithoutAmount needRemovedFluid = recipeFluidType;

                    for(FluidStackWithoutAmount inputFluidType : remainingInventoryFluidTypes)
                    {
                        if (FluidStackWithoutAmount.isSame(recipeFluidType, inputFluidType))
                        {
                            needRemovedFluid = inputFluidType;
                            isMatched = true;
                            matchedFluidStack.add(fluidStack);
                            break;
                        }
                    }

                    if(isMatched)
                    {
                        remainingInventoryFluidTypes.remove(needRemovedFluid);
                        break;
                    }
                }

                if(!isMatched)
                {
                    return List.of();
                }
            }
            else
            {   //流体匹配
                FluidStack fluidStack = fluidIngredient.getFluidStack();
                boolean isMatched = false;
                FluidStackWithoutAmount recipeFluidType = new FluidStackWithoutAmount(fluidStack.getFluid(), fluidStack.getTag());
                FluidStackWithoutAmount needRemovedFluid = recipeFluidType;

                for(FluidStackWithoutAmount inputFluidType : remainingInventoryFluidTypes)
                {
                    if (FluidStackWithoutAmount.isSame(recipeFluidType, inputFluidType))
                    {
                        needRemovedFluid = inputFluidType;
                        isMatched = true;
                        matchedFluidStack.add(fluidStack);
                        break;
                    }
                }

                if(!isMatched)
                {
                    return List.of();
                }
                else
                {
                    remainingInventoryFluidTypes.remove(needRemovedFluid);
                }
            }
        }

        if (remainingInventoryFluidTypes.isEmpty())
        {
            return matchedFluidStack;
        }
        else
        {   //流体输入槽有配方以外的流体
            return List.of();
        }
    }
}
