package com.pasterdream.pasterdreammod.world.item.debugtool.generichandler;

import net.minecraft.network.chat.Component;

@FunctionalInterface
public interface NbtSaveHandler
{
    Component trySave(String text);
}
