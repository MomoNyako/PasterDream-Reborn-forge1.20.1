package com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public record GenericRecipeInventory(List<ItemStack> inputItemStacks, List<FluidStack> inputFluidStacks, List<ItemStack> outputItemStacks, List<FluidStack> outputFluidStacks, int recipeTime, int FluidSlotMaxStackSize){}
