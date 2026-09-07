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
 * 粉顶菌垂帘 —— 1 格宽的竖直悬垂结构，悬挂在染梦繁茂洞穴的洞顶。
 * 由粉顶菌菌盖方块和粉菌光体组成，顶部必须紧贴洞穴天花板（实心方块）。
 */
public class PinkMushroomCurtainFeature extends Feature<NoneFeatureConfiguration> {

    /** 向上搜索天花板的最高距离 */
    private static final int MAX_CEILING_SEARCH = 12;
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 7;
    private static final float SHROOMLIGHT_CHANCE = 0.25F;

    public PinkMushroomCurtainFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        // 向上搜索洞穴天花板（第一个实心方块）
        int ceilingDist = findCeiling(level, origin);
        if (ceilingDist <= 0) {
            return false;
        }

        // 垂帘顶部 = 天花板正下方空气格，确保顶部与天花板接触
        BlockPos top = origin.above(ceilingDist - 1);
        int length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH + 1);

        // 校验垂帘占用的垂直空间全为空气
        for (int dy = 0; dy < length; dy++) {
            if (!level.isEmptyBlock(top.below(dy))) {
                return false;
            }
        }

        BlockState cap = ModBlocks.PINK_MUSHROOM_BLOCK.get().defaultBlockState();
        BlockState shroomlight = ModBlocks.PINK_SHROOMLIGHT.get().defaultBlockState();
        for (int dy = 0; dy < length; dy++) {
            // 贴住天花板的顶部格子用菌盖，其余按概率混合菌光体
            BlockState state = (dy == 0 || random.nextFloat() >= SHROOMLIGHT_CHANCE)
                    ? cap : shroomlight;
            level.setBlock(top.below(dy), state, 2);
        }
        return true;
    }

    /** 返回 origin 上方第一个实心方块的相对距离；origin 不在空气中返回 0，找不到天花板返回 -1。 */
    private static int findCeiling(WorldGenLevel level, BlockPos origin) {
        if (!level.isEmptyBlock(origin)) {
            return 0;
        }
        for (int dy = 1; dy <= MAX_CEILING_SEARCH; dy++) {
            if (!level.isEmptyBlock(origin.above(dy))) {
                return dy;
            }
        }
        return -1;
    }
}