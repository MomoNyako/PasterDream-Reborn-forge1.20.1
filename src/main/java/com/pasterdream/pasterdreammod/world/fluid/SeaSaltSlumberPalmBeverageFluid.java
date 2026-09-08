package com.pasterdream.pasterdreammod.world.fluid;

import com.pasterdream.pasterdreammod.init.ModFluids;
import net.minecraftforge.fluids.FluidType;

public class SeaSaltSlumberPalmBeverageFluid extends PasterDreamBaseFluid
{
    @Override
    public FluidType getFluidType()
    {
        return ModFluids.SEA_SALT_SLUMBER_PALM_BEVERAGE_TYPE.get();
    }
}
