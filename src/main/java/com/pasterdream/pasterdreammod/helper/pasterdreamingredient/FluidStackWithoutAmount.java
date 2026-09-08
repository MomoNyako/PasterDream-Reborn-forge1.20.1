package com.pasterdream.pasterdreammod.helper.pasterdreamingredient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FluidStackWithoutAmount
{
    @Nullable
    private final Fluid fluid;
    @Nullable
    private final CompoundTag nbt;

    public FluidStackWithoutAmount(@Nullable Fluid fluid, @Nullable CompoundTag nbt)
    {
        this.fluid = fluid;
        this.nbt = nbt;
    }

    public Fluid getFluid()
    {
        return fluid;
    }

    public CompoundTag getNbt()
    {
        return nbt;
    }

    public boolean hasNbt()
    {
        return nbt != null;
    }

    public static List<FluidStackWithoutAmount> fromListFluidStack(List<FluidStack> fluidStackList)
    {
        List<FluidStackWithoutAmount> fluidStackWithoutAmountList = new ArrayList<>();

        for(FluidStack fluidStack : fluidStackList)
        {
            boolean isHave = false;
            FluidStackWithoutAmount converted = new FluidStackWithoutAmount(fluidStack.getFluid(), fluidStack.getTag());

            for(FluidStackWithoutAmount fluidStackWithoutAmount : fluidStackWithoutAmountList)
            {
                if(isSame(fluidStackWithoutAmount, converted))
                {
                    isHave = true;
                    break;
                }
            }

            if(!isHave && converted.getFluid() != Fluids.EMPTY)
            {
                fluidStackWithoutAmountList.add(converted);
            }
        }

        return fluidStackWithoutAmountList;
    }

    public static boolean isSame(FluidStackWithoutAmount fluidStackWithoutAmount0, FluidStackWithoutAmount fluidStackWithoutAmount1)
    {
        if(!fluidStackWithoutAmount0.hasNbt() && !fluidStackWithoutAmount1.hasNbt())
        {
            return fluidStackWithoutAmount0.getFluid() == fluidStackWithoutAmount1.getFluid();
        }
        else
            if(fluidStackWithoutAmount0.hasNbt() && fluidStackWithoutAmount1.hasNbt())
            {
                return fluidStackWithoutAmount0.getFluid() == fluidStackWithoutAmount1.getFluid() && fluidStackWithoutAmount0.getNbt().equals(fluidStackWithoutAmount1.getNbt());
            }
                else
                {
                    return false;
                }
    }
}
