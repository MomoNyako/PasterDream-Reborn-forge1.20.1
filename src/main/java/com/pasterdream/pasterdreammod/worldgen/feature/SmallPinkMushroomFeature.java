package com.pasterdream.pasterdreammod.worldgen.feature;

import com.mojang.serialization.Codec;
import com.pasterdream.pasterdreammod.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * 小粉顶菌平菇 —— 3~6 格高（茎 2~5 + 菌盖 1），菌盖平盘直径 3 或 5，
 * 菌盖方块按概率替换为粉菌光体。仅在染梦繁茂洞穴的染梦草地面上生成。
 */
public class SmallPinkMushroomFeature extends Feature<NoneFeatureConfiguration> {

    private static final float SHROOMLIGHT_CHANCE = 0.3F;

    public SmallPinkMushroomFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        for (int dy = 0; dy <= 8; dy++) {
            if (tryPlace(level, origin.above(dy), random)) {
                return true;
            }
            if (tryPlace(level, origin.below(dy), random)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryPlace(WorldGenLevel level, BlockPos pos, RandomSource random) {
        if (!isCaveFloor(level, pos)) {
            return false;
        }
        int stemHeight = 2 + random.nextInt(4);
        int radius = 1 + random.nextInt(2);
        for (int dy = 1; dy <= stemHeight; dy++) {
            if (!level.getBlockState(pos.above(dy)).isAir()) {
                return false;
            }
        }
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (!level.getBlockState(pos.offset(dx, stemHeight, dz)).isAir()) {
                    return false;
                }
            }
        }
        BlockState stem = ModBlocks.PINK_MUSHROOM_STEM.get().defaultBlockState();
        for (int dy = 0; dy < stemHeight; dy++) {
            level.setBlock(pos.above(dy), stem, 2);
        }
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (radius == 2 && Math.abs(dx) + Math.abs(dz) > 3) {
                    continue;
                }
                BlockState cap = random.nextFloat() < SHROOMLIGHT_CHANCE
                        ? ModBlocks.PINK_SHROOMLIGHT.get().defaultBlockState()
                        : ModBlocks.PINK_MUSHROOM_BLOCK.get().defaultBlockState();
                level.setBlock(pos.offset(dx, stemHeight, dz), cap, 2);
            }
        }
        return true;
    }

    private static boolean isCaveFloor(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).isAir()
                && (level.getBlockState(pos.below()).is(ModBlocks.DYEDREAM_GRASS_BLOCK.get())
                || level.getBlockState(pos.below()).is(ModBlocks.DYEDREAM_DIRT.get()));
    }
}