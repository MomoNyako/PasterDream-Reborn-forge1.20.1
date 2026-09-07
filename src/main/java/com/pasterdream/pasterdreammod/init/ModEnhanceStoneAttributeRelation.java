package com.pasterdream.pasterdreammod.init;

import com.pasterdream.pasterdreammod.helper.enhancestoneattributerelation.EnhanceStoneAttributeRelationRegistry;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ModEnhanceStoneAttributeRelation
{
    public static void registerModEnhanceStoneAttributeRelation()
    {
        EnhanceStoneAttributeRelationRegistry.register(ModItems.ATTACK_ENHANCE_STONE.get().asItem(), Attributes.ATTACK_DAMAGE, 1, 3);
        EnhanceStoneAttributeRelationRegistry.register(ModItems.LUCK_ENHANCE_STONE.get().asItem(), Attributes.LUCK, 1, 3);
    }
}
