package com.pasterdream.pasterdreammod.network;

import com.pasterdream.pasterdreammod.world.block.weaponworkshop.craftingtable.WeaponWorkshopCraftingTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WeaponWorkshopCraftingTableCraftPacket
{
    private final BlockPos blockPosition;

    public WeaponWorkshopCraftingTableCraftPacket(BlockPos blockPosition)
    {
        this.blockPosition = blockPosition;
    }

    public static void encode(WeaponWorkshopCraftingTableCraftPacket message, FriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(message.blockPosition);
    }

    public static WeaponWorkshopCraftingTableCraftPacket decode(FriendlyByteBuf buffer)
    {
        return new WeaponWorkshopCraftingTableCraftPacket(buffer.readBlockPos());
    }

    public static void handle(WeaponWorkshopCraftingTableCraftPacket message, Supplier<NetworkEvent.Context> context)
    {
        context.get().enqueueWork(() ->
        {
            ServerPlayer player = context.get().getSender();
            if (player != null)
            {
                Level level = player.level();
                BlockEntity blockEntity = level.getBlockEntity(message.blockPosition);
                if (blockEntity instanceof WeaponWorkshopCraftingTableBlockEntity weaponWorkshopCraftingTableBlockEntity)
                {
                    weaponWorkshopCraftingTableBlockEntity.craft(player);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
