package com.pasterdream.pasterdreammod.world.block.weaponworkshop.craftingtable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pasterdream.pasterdreammod.helper.pasterdreamingredient.ItemIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WeaponWorkshopCraftingTableRecipeSerializer implements RecipeSerializer<WeaponWorkshopCraftingTableRecipe>
{
    @Override
    public WeaponWorkshopCraftingTableRecipe fromJson(ResourceLocation id, JsonObject json)
    {
        List<ItemIngredient> inputItemIngredients = new ArrayList<>();
        if (json.has("itemInputs"))
        {
            JsonArray array = json.getAsJsonArray("itemInputs");
            for (var jsonElement : array)
            {
                inputItemIngredients.add(ItemIngredient.fromJson(jsonElement));
            }
        }

        ItemIngredient enhanceStone = ItemIngredient.fromJson(json.getAsJsonArray("enhanceStone").get(0).getAsJsonObject());
        ItemIngredient outputItemIngredient = ItemIngredient.fromJson(json.getAsJsonArray("itemOutputs").get(0).getAsJsonObject());

        return new WeaponWorkshopCraftingTableRecipe(id, inputItemIngredients, enhanceStone, outputItemIngredient);
    }

    @Override
    public @Nullable WeaponWorkshopCraftingTableRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer)
    {
        int inputItemCount = buffer.readVarInt();
        List<ItemIngredient> inputItems = new ArrayList<>();
        for (int i = 0; i < inputItemCount; i++)
        {
            inputItems.add(ItemIngredient.fromNetwork(buffer));
        }

        ItemIngredient enhanceStone = ItemIngredient.fromNetwork(buffer);
        ItemIngredient outputItems = ItemIngredient.fromNetwork(buffer);

        return new WeaponWorkshopCraftingTableRecipe(id, inputItems, enhanceStone, outputItems);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, WeaponWorkshopCraftingTableRecipe recipe)
    {
        buffer.writeVarInt(recipe.getInputItemIngredients().size());
        for (ItemIngredient itemIngredient : recipe.getInputItemIngredients())
        {
            itemIngredient.toNetwork(buffer);
        }

        recipe.getEnhanceStone().toNetwork(buffer);
        recipe.getOutputItemIngredients().toNetwork(buffer);
    }
}
