package com.pasterdream.pasterdreammod.worldgen.feature;

import com.mojang.serialization.Codec;
import com.pasterdream.pasterdreammod.init.ModBlocks;
import com.pasterdream.pasterdreammod.worldgen.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * 繁茂洞穴粉顶菌巨菇 —— 在锚点附近搜索洞穴地面（空气格 + 下方染梦草/染梦土），
 * 确认有足够垂直空间后调用 PINK_HUGE_MUSHROOM 放置，避免随机高度落不到洞底而无法生成。
 */
public class LushCaveMushroomFeature extends Feature<NoneFeatureConfiguration> {

    public LushCaveMushroomFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        Holder<ConfiguredFeature<?, ?>> mushroom = level.registryAccess()
                .registryOrThrow(Registries.CONFIGURED_FEATURE)
                .getHolderOrThrow(ModConfiguredFeatures.PINK_HUGE_MUSHROOM);
        for (int dy = 0; dy <= 8; dy++) {
            if (tryPlace(level, origin.above(dy), mushroom, context, random)
                    || tryPlace(level, origin.below(dy), mushroom, context, random)) {
                return true;
            }
        }
        return false;
    }

    private boolean tryPlace(WorldGenLevel level, BlockPos pos, Holder<ConfiguredFeature<?, ?>> mushroom,
                             FeaturePlaceContext<NoneFeatureConfiguration> context, RandomSource random) {
        if (!isCaveFloor(level, pos) || !hasMushroomSpace(level, pos)) {
            return false;
        }
        return mushroom.value().place(level, context.chunkGenerator(), random, pos);
    }

    private static boolean isCaveFloor(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).isAir()
                && (level.getBlockState(pos.below()).is(ModBlocks.DYEDREAM_GRASS_BLOCK.get())
                || level.getBlockState(pos.below()).is(ModBlocks.DYEDREAM_DIRT.get()));
    }

    private static boolean hasMushroomSpace(WorldGenLevel level, BlockPos pos) {
        int height = 7;
        for (int dy = 1; dy <= height; dy++) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (!level.getBlockState(pos.offset(dx, dy, dz)).isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}