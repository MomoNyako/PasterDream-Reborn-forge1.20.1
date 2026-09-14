package com.pasterdream.pasterdreammod.helper.stringhelper;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class ListStringFromCompoundTag
{
    public static List<String> indentationLess(CompoundTag NBT, int width)
    {
        if(NBT == null)
        {
            return List.of();
        }

        List<String> lines = new ArrayList<>();
        String[] paragraphs = NBT.toString().split("\n", -1);

        for (String paragraph : paragraphs)
        {
            if (paragraph.isEmpty())
            {
                lines.add("");
            }
            else
                if (Minecraft.getInstance().font.width(paragraph) <= Math.max(8, width))
                {
                    lines.add(paragraph);
                }
                    else
                    {
                        String remaining = paragraph;
                        while (!remaining.isEmpty())
                        {
                            String trimmed = Minecraft.getInstance().font.plainSubstrByWidth(remaining, Math.max(8, width));
                            remaining = remaining.substring(trimmed.length());
                            lines.add(trimmed);
                        }
                    }
        }
        return lines;
    }
}
