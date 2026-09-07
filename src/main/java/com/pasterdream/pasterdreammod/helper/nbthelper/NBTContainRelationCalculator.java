package com.pasterdream.pasterdreammod.helper.nbthelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class NBTContainRelationCalculator
{
    public static boolean calculator(CompoundTag targetNBT, CompoundTag requireNBT)
    {
        if(requireNBT == null)
        {
            return true;
        }
        else
            if(targetNBT == null)
            {
                return false;
            }
                else
                {
                    return compoundTagCalculator(targetNBT, requireNBT);
                }
    }

    public static boolean compoundTagCalculator(CompoundTag targetNBT, CompoundTag requireNBT)
    {
        for (String key : requireNBT.getAllKeys())
        {
            Tag target = targetNBT.get(key);
            Tag require = requireNBT.get(key);
            if(require != null && target != null)
            {
                if(require.getType() == target.getType())
                {
                    if(target instanceof CompoundTag)
                    {
                        if(!compoundTagCalculator((CompoundTag)target, (CompoundTag)require))
                        {
                            return false;
                        }
                    }
                    else
                        if(target instanceof ListTag)
                        {
                            if(!listTagCalculator((ListTag)target, (ListTag)require))
                            {
                                return false;
                            }
                        }
                        else
                            if(!target.equals(require))
                            {
                                return false;
                            }
                }
                    else
                    {
                        return false;
                    }
            }
                else
                {
                    return false;
                }
        }
        return true;
    }

    public static boolean listTagCalculator(ListTag targetNBT, ListTag requireNBT)
    {
        if(requireNBT.isEmpty() || requireNBT.getElementType() == Tag.TAG_END)
        {
            return true;
        }
        else
            if(requireNBT.getElementType() == Tag.TAG_COMPOUND)
            {
                for(int i = 0; i < requireNBT.size(); i++)
                {
                    boolean isContained = false;
                    for(int j = 0; j < targetNBT.size(); j++)
                    {
                        if(compoundTagCalculator(targetNBT.getCompound(j), requireNBT.getCompound(i)))
                        {
                            isContained = true;
                            break;
                        }
                    }

                    if(!isContained)
                    {
                        return false;
                    }
                }
                return true;
            }
                else
                {
                    if(targetNBT.size() == requireNBT.size())
                    {
                        for(int i = 0; i < requireNBT.size(); i++)
                        {
                            if(!targetNBT.get(i).equals(requireNBT.get(i)))
                            {
                                return false;
                            }
                        }
                        return true;
                    }
                        else
                        {
                            return false;
                        }
                }
    }
}
