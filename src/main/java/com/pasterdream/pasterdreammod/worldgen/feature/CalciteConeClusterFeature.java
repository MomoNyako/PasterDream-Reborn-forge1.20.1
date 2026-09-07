package com.pasterdream.pasterdreammod.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * 方解石锥簇 —— 镜像原版 DripstoneClusterFeature（large_dripstone）：
 * 在一个半径内按中心密度生成成组的钟乳石/石笋，形成大片锥形群落。
 */
public class CalciteConeClusterFeature extends Feature<NoneFeatureConfiguration> {

    public CalciteConeClusterFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        if (!level.getBlockState(origin).isAir()) {
            return false;
        }
        int radius = 2 + random.nextInt(3);
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                int dist = Math.abs(dx) + Math.abs(dz);
                if (dist > radius) {
                    continue;
                }
                double chance = 1.0 - (double) dist / (radius + 1.0);
                if (random.nextDouble() > chance) {
                    continue;
                }
                BlockPos pos = origin.offset(dx, 0, dz);
                for (int dy = 0; dy <= 10; dy++) {
                    BlockPos up = pos.above(dy);
                    if (level.getBlockState(up).isAir() && CalciteConeFeature.isConeSupport(level.getBlockState(up.above()))) {
                        CalciteConeFeature.growCalciteCone(level, up, Direction.DOWN, 1 + random.nextInt(4), false);
                        break;
                    }
                    BlockPos down = pos.below(dy);
                    if (level.getBlockState(down).isAir() && CalciteConeFeature.isConeSupport(level.getBlockState(down.below()))) {
                        CalciteConeFeature.growCalciteCone(level, down, Direction.UP, 1 + random.nextInt(4), false);
                        break;
                    }
                }
            }
        }
        return true;
    }
}