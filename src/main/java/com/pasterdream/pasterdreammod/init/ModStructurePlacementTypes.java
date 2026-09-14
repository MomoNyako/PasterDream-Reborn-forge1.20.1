package com.pasterdream.pasterdreammod.init;

import com.pasterdream.pasterdreammod.PasterDreamMod;
import com.pasterdream.pasterdreammod.worldgen.structures.FixedChunkStructurePlacement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructurePlacementTypes {

    private static final DeferredRegister<StructurePlacementType<?>> PLACEMENT_TYPES =
            DeferredRegister.create(BuiltInRegistries.STRUCTURE_PLACEMENT.key(), PasterDreamMod.MOD_ID);

    public static final RegistryObject<StructurePlacementType<FixedChunkStructurePlacement>> FIXED_CHUNK =
            PLACEMENT_TYPES.register("fixed_chunk", ModStructurePlacementTypes::fixedChunk);

    private static StructurePlacementType<FixedChunkStructurePlacement> fixedChunk() {
        return () -> FixedChunkStructurePlacement.CODEC;
    }

    public static void register(IEventBus eventBus) {
        PLACEMENT_TYPES.register(eventBus);
    }
}
