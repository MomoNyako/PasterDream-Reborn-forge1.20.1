package com.pasterdream.pasterdreammod.worldgen.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pasterdream.pasterdreammod.init.ModStructurePlacementTypes;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

/**
 * 固定区块结构放置：仅在指定的区块坐标 (chunkX, chunkZ) 生成结构。
 * 用于让「染梦世界树」随世界生成在固定坐标 (2002, 1128) 生成，
 * 而非在区块与地物全部生成后于运行时强行放置（后者会破坏已生成的花草产生掉落物）。
 */
public class FixedChunkStructurePlacement extends StructurePlacement {

    public static final Codec<FixedChunkStructurePlacement> CODEC =
            RecordCodecBuilder.<FixedChunkStructurePlacement>mapCodec(instance ->
                    placementCodec(instance).and(
                            instance.group(
                                    Codec.INT.fieldOf("chunk_x").forGetter(p -> p.chunkX),
                                    Codec.INT.fieldOf("chunk_z").forGetter(p -> p.chunkZ)
                            )
                    ).apply(instance, FixedChunkStructurePlacement::new)
            ).codec();

    private final int chunkX;
    private final int chunkZ;

    public FixedChunkStructurePlacement(Vec3i locateOffset,
                                        StructurePlacement.FrequencyReductionMethod frequencyReductionMethod,
                                        float frequency,
                                        int salt,
                                        Optional<StructurePlacement.ExclusionZone> exclusionZone,
                                        int chunkX,
                                        int chunkZ) {
        super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone);
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState state, int x, int z) {
        return x == this.chunkX && z == this.chunkZ;
    }

    @Override
    public StructurePlacementType<?> type() {
        return ModStructurePlacementTypes.FIXED_CHUNK.get();
    }
}
