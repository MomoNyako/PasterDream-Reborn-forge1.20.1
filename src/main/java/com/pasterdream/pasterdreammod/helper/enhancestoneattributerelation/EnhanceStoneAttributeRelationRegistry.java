package com.pasterdream.pasterdreammod.helper.enhancestoneattributerelation;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class EnhanceStoneAttributeRelationRegistry
{
    private static final Map<Item, AttributeInfo> ENHANCE_STONE_ATTRIBUTE_RELATION = new HashMap<>();

    public static void register(Item enhanceStoneItem, Attribute attribute, double min, double max)
    {
        if (ENHANCE_STONE_ATTRIBUTE_RELATION.containsKey(enhanceStoneItem))
        {
            throw new IllegalArgumentException("重复的强化石注册关系：" + enhanceStoneItem.toString());
        }
        ENHANCE_STONE_ATTRIBUTE_RELATION.put(enhanceStoneItem, new AttributeInfo(attribute, min, max));
    }

    public static AttributeInfo getInfo(Item enhanceStoneItem)
    {
        return ENHANCE_STONE_ATTRIBUTE_RELATION.get(enhanceStoneItem);
    }
}
