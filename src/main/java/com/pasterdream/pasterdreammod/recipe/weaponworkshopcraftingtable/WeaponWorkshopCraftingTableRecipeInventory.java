package com.pasterdream.pasterdreammod.recipe.weaponworkshopcraftingtable;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public record WeaponWorkshopCraftingTableRecipeInventory(List<ItemStack> inputItemStacks, ItemStack enhanceStone, ItemStack outputItemStack){}
