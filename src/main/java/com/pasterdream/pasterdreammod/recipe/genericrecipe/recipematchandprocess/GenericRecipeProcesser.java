package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess;

import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericprocessor.FluidProcessor;
import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericprocessor.ItemProcessor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class GenericRecipeProcesser
{
    public static GenericRecipeInventory processing(GenericRecipeInventory recipeRequire, GenericRecipeInventory machineInventory)
    {
        List<ItemStack> matchedInputItemStacks = recipeRequire.inputItemStacks();
        List<ItemStack> machineInputItemStacks = machineInventory.inputItemStacks();
        List<ItemStack> machineInputItemStacksProcessResult = ItemProcessor.processor(matchedInputItemStacks, machineInputItemStacks, false);
        if(machineInputItemStacksProcessResult == null)
        {
            return null;
        }

        List<FluidStack> matchedInputFluidStacks = recipeRequire.inputFluidStacks();
        List<FluidStack> machineInputFluidStacks = machineInventory.inputFluidStacks();
        List<FluidStack> machineInputFluidStacksProcessResult = FluidProcessor.processor(matchedInputFluidStacks, machineInputFluidStacks, false, 2147483647);
        if(machineInputFluidStacksProcessResult == null)
        {
            return null;
        }

        List<ItemStack> matchedOutputItemStacks = recipeRequire.outputItemStacks();
        List<ItemStack> machineOutputItemStacks = machineInventory.outputItemStacks();
        List<ItemStack> machineOutputItemStacksProcessResult = ItemProcessor.processor(matchedOutputItemStacks, machineOutputItemStacks, true);
        if(machineOutputItemStacksProcessResult == null)
        {
            return null;
        }

        List<FluidStack> matchedOutputFluidStacks = recipeRequire.outputFluidStacks();
        List<FluidStack> machineOutputFluidStacks = machineInventory.outputFluidStacks();
        System.out.println("14");
        List<FluidStack> machineOutputFluidStacksProcessResult = FluidProcessor.processor(matchedOutputFluidStacks, machineOutputFluidStacks, true, machineInventory.FluidSlotMaxStackSize());
        System.out.println("15");
        if(machineOutputFluidStacksProcessResult == null)
        {
            System.out.println("machineOutputFluidStacksProcessResult == null");
            return null;
        }

        System.out.println("执行了return new GenericRecipeInventory()");
        return new GenericRecipeInventory(machineInputItemStacksProcessResult, machineInputFluidStacksProcessResult, machineOutputItemStacksProcessResult, machineOutputFluidStacksProcessResult, recipeRequire.recipeTime(), 2147483647);
    }
}
