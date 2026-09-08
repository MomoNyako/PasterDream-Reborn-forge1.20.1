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
 * 浮空流明光球 —— 花海特殊地物。
 * 在锚点下方扫描贴近地表的染梦地面（染梦草方块/染梦泥土），在紧贴地面之上 1~10 格悬浮放置流明光球。
 */
public class FloatingLightBallFeature extends Feature<NoneFeatureConfiguration> {

    public FloatingLightBallFeature(Codec<NoneFeatureConfiguration> codec) {
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
        // 向下扫描最近的染梦地面
        for (int dy = 0; dy <= 8; dy++) {
            BlockPos ground = origin.below(dy);
            if (isDyedreamGround(level.getBlockState(ground))) {
                // 在染梦地面之上 1~10 格悬浮放置光球（贴近地表）
                int hover = 1 + random.nextInt(10);
                BlockPos ballPos = ground.above(hover);
                if (level.getBlockState(ballPos).isAir() && level.getBlockState(ballPos.above()).isAir()) {
                    level.setBlock(ballPos, ModBlocks.LIGHT_BALL.get().defaultBlockState(), 2);
                    return true;
                }
                return false;
            }
            if (!level.getBlockState(ground).isAir() && !isDyedreamGround(level.getBlockState(ground))) {
                return false;
            }
        }
        return false;
    }

    private static boolean isDyedreamGround(BlockState state) {
        return state.is(ModBlocks.DYEDREAM_GRASS_BLOCK.get()) || state.is(ModBlocks.DYEDREAM_DIRT.get());
    }
}
