package com.pasterdream.pasterdreammod.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * 眠椰树生成配置（格雷科技椰子树风格，支持顶部叉开多根枝）。
 * <p>
 * 所有参数均可通过数据包的 configured_feature JSON 调整。
 * </p>
 *
 * @param trunkHeight  主干高度（格）
 * @param branchCount  叉开时的分支数量（2 = 两根枝）
 * @param forkChance   叉开概率（0~1，掷中时按 branchCount 分叉，否则单干）
 * @param branchLength 分支高度（格，分叉时生效）
 * @param branchSpread 分支向外倾斜程度（每升高 1 格的水平偏移，分叉时生效）
 * @param armLength    每条分支上树冠星臂的长度（格）
 * @param armCount     每条分支上树冠的星臂数量（沿圆周均布，GT 为 8）
 * @param maxDroop     垂叶高度：臂末端最多下垂格数（GT 为 2）
 * @param centerFill   是否在冠顶中心填充额外叶簇（GT 不填充，为 false）
 */
public record SlumberPalmTreeConfiguration(
        IntProvider trunkHeight,
        int branchCount,
        double forkChance,
        IntProvider branchLength,
        double branchSpread,
        IntProvider armLength,
        int armCount,
        int maxDroop,
        boolean centerFill
) implements FeatureConfiguration {
    public static final Codec<SlumberPalmTreeConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    IntProvider.codec(0, 32).fieldOf("trunk_height").forGetter(SlumberPalmTreeConfiguration::trunkHeight),
                    Codec.intRange(1, 12).fieldOf("branch_count").forGetter(SlumberPalmTreeConfiguration::branchCount),
                    Codec.doubleRange(0.0, 1.0).fieldOf("fork_chance").forGetter(SlumberPalmTreeConfiguration::forkChance),
                    IntProvider.codec(0, 16).fieldOf("branch_length").forGetter(SlumberPalmTreeConfiguration::branchLength),
                    Codec.doubleRange(0.0, 4.0).fieldOf("branch_spread").forGetter(SlumberPalmTreeConfiguration::branchSpread),
                    IntProvider.codec(0, 16).fieldOf("arm_length").forGetter(SlumberPalmTreeConfiguration::armLength),
                    Codec.intRange(2, 24).fieldOf("arm_count").forGetter(SlumberPalmTreeConfiguration::armCount),
                    Codec.intRange(0, 6).fieldOf("max_droop").forGetter(SlumberPalmTreeConfiguration::maxDroop),
                    Codec.BOOL.fieldOf("center_fill").orElse(false).forGetter(SlumberPalmTreeConfiguration::centerFill)
            ).apply(instance, SlumberPalmTreeConfiguration::new)
    );
}