package com.pasterdream.pasterdreammod.helper.pasterdreamingredient;

import com.pasterdream.pasterdreammod.helper.nbthelper.NBTContainRelationCalculator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemStackWithoutCount
{
    @Nullable
    private final Item item;
    @Nullable
    private final CompoundTag nbt;

    public ItemStackWithoutCount(@Nullable Item item, @Nullable CompoundTag nbt)
    {
        this.item = item;
        this.nbt = nbt;
    }

    public Item getItem()
    {
        return item;
    }

    public CompoundTag getNbt()
    {
        return nbt;
    }

    public boolean hasNbt()
    {
        return nbt != null;
    }

    public static List<ItemStackWithoutCount> fromListItemStacks(List<ItemStack> itemStackList)
    {
        List<ItemStackWithoutCount> itemStackWithoutCountList = new ArrayList<>();

        for(ItemStack itemStack : itemStackList)
        {
            boolean isHave = false;
            ItemStackWithoutCount converted = new ItemStackWithoutCount(itemStack.getItem(), itemStack.getTag());

            for(ItemStackWithoutCount itemStackWithoutCount : itemStackWithoutCountList)
            {
                if(isSame(itemStackWithoutCount, converted))
                {
                    isHave = true;
                    break;
                }
            }

            if(!isHave && converted.getItem() != Items.AIR)
            {
                itemStackWithoutCountList.add(converted);
            }
        }

        return itemStackWithoutCountList;
    }

    public static boolean isSame(ItemStackWithoutCount itemStackWithoutCount0, ItemStackWithoutCount itemStackWithoutCount1)
    {
        if(!itemStackWithoutCount0.hasNbt() && !itemStackWithoutCount1.hasNbt())
        {
            return isSameItem(itemStackWithoutCount0, itemStackWithoutCount1);
        }
        else
            if (itemStackWithoutCount0.hasNbt() && itemStackWithoutCount1.hasNbt())
            {
                return isSameItem(itemStackWithoutCount0, itemStackWithoutCount1) && itemStackWithoutCount0.getNbt().equals(itemStackWithoutCount1.getNbt());
            }
                else
                {
                    return false;
                }
    }

    public static boolean isSameItem(ItemStackWithoutCount itemStackWithoutCount0, ItemStackWithoutCount itemStackWithoutCount1)
    {
        return itemStackWithoutCount0.getItem() == itemStackWithoutCount1.getItem();
    }

    public static boolean isSameItemAndNBTHasContainRelation(ItemStackWithoutCount itemStackWithoutCount0, ItemStackWithoutCount itemStackWithoutCount1)
    {
        return itemStackWithoutCount0.getItem() == itemStackWithoutCount1.getItem() && NBTContainRelationCalculator.calculator(itemStackWithoutCount0.getNbt(), itemStackWithoutCount1.getNbt());
    }
}
