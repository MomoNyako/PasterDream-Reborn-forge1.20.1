package com.pasterdream.pasterdreammod.recipe.weaponworkshopcraftingtable;

import com.pasterdream.pasterdreammod.helper.enhancestoneattributerelation.AttributeInfo;
import com.pasterdream.pasterdreammod.helper.enhancestoneattributerelation.EnhanceStoneAttributeRelationRegistry;
import com.pasterdream.pasterdreammod.helper.nbthelper.ItemsNBTAdderCalculator;
import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericprocessor.ItemProcessor;
import com.pasterdream.pasterdreammod.recipe.genericrecipe.recipematchandprocess.genericprocessor.SingleItemProcessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WeaponWorkshopCraftingTableRecipeProcessor
{
    public static WeaponWorkshopCraftingTableRecipeInventory processing(WeaponWorkshopCraftingTableRecipeInventory recipeRequire, WeaponWorkshopCraftingTableRecipeInventory machineInventory, RandomSource random, double playerLuckLevel)
    {
        List<ItemStack> matchedInputItemStacks = recipeRequire.inputItemStacks();
        List<ItemStack> machineInputItemStacks = machineInventory.inputItemStacks();
        List<ItemStack> machineInputInventoryProcessResult = ItemProcessor.processor(matchedInputItemStacks, machineInventory.inputItemStacks(), false);
        if(machineInputInventoryProcessResult == null)
        {
            return null;
        }

        ItemStack matchedEnhanceStone = recipeRequire.enhanceStone();
        ItemStack machineEnhanceStone = machineInventory.enhanceStone();
        ItemStack enhanceStoneProcessResult = machineEnhanceStone.copy();
        AttributeInfo enhanceStoneInfo = null;
        if (!matchedEnhanceStone.isEmpty())
        {
            enhanceStoneProcessResult = SingleItemProcessor.processor(matchedEnhanceStone, machineEnhanceStone, false);
            if(enhanceStoneProcessResult == null)
            {
                return null;
            }
            enhanceStoneInfo = EnhanceStoneAttributeRelationRegistry.getInfo(matchedEnhanceStone.getItem());
        }

        List<CompoundTag> inputItemsNBTList = new ArrayList<>();
        for(ItemStack itemStack : machineInputItemStacks)
        {
            inputItemsNBTList.add(itemStack.getTag());
        }

        CompoundTag calculatedNBT = ItemsNBTAdderCalculator.calculate(inputItemsNBTList);

        if(enhanceStoneInfo != null)
        {
            double level = (enhanceStoneInfo.max() - enhanceStoneInfo.min()) * (Math.pow(random.nextDouble(), (1 / (1 + playerLuckLevel)))) + enhanceStoneInfo.min();
            Attribute attributeType = enhanceStoneInfo.attribute();

            if(attributeType == Attributes.ATTACK_DAMAGE)
            {
                double attackBoost = 0;
                if(calculatedNBT.contains("AttackBoost"))
                {
                    attackBoost += calculatedNBT.getDouble("AttackBoost");
                }
                attackBoost += level;
                calculatedNBT.putDouble("AttackBoost", attackBoost);
            }
            else
                if(attributeType == Attributes.LUCK)
                {
                    double luckBoost = 0;
                    if(calculatedNBT.contains("LuckBoost"))
                    {
                        luckBoost += calculatedNBT.getDouble("LuckBoost");
                    }
                    luckBoost += level;
                    calculatedNBT.putDouble("LuckBoost", luckBoost);
                }
        }

        ItemStack matchedOutputItemStack = recipeRequire.outputItemStack();
        ItemStack machineOutputItemStack = machineInventory.outputItemStack();

        matchedOutputItemStack.setTag(calculatedNBT);

        ItemStack machineOutputInventoryProcessResult = SingleItemProcessor.processor(matchedOutputItemStack, machineOutputItemStack, true);
        if(machineOutputInventoryProcessResult != null)
        {
            return new WeaponWorkshopCraftingTableRecipeInventory(machineInputInventoryProcessResult, enhanceStoneProcessResult, machineOutputInventoryProcessResult);
        }
            else
            {
                return null;
            }
    }
}
