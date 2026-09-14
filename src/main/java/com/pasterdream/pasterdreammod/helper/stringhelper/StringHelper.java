package com.pasterdream.pasterdreammod.helper.stringhelper;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class StringHelper
{
    public static List<String> ListStringFromString(String string, int width)
    {
        if(string != null && !string.isEmpty())
        {
            List<String> stringBuilderList = new ArrayList<>();
            StringBuilder remaining = new StringBuilder(string);
            StringBuilder trimmedBuffer;

            while(!remaining.isEmpty())
            {
                trimmedBuffer = new StringBuilder(Minecraft.getInstance().font.plainSubstrByWidth(remaining.toString(), Math.max(8, width)));
                if (trimmedBuffer.isEmpty())
                {
                    break;
                }
                stringBuilderList.add(trimmedBuffer.toString());
                remaining.delete(0, trimmedBuffer.length());
            }
            return stringBuilderList;
        }
            else
            {
                return List.of("");
            }
    }
}
