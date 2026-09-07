package com.pasterdream.pasterdreammod.worldgen.feature;

import com.mojang.serialization.Codec;
import com.pasterdream.pasterdreammod.init.ModBlocks;
import com.pasterdream.pasterdreammod.world.block.CalciteConeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Consumer;

public class CalciteConeFeature extends Feature<NoneFeatureConfiguration> {

    public CalciteConeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        // 在锚点上下搜索最近洞壁（空气格 + 方解石支撑），保证尝试大多能落到洞穴表面生成锥
        for (int dy = 0; dy <= 8; dy++) {
            BlockPos up = origin.above(dy);
            if (level.getBlockState(up).isAir() && isConeSupport(level.getBlockState(up.above()))) {
                growCalciteCone(level, up, Direction.DOWN, 1 + random.nextInt(3), false);
                return true;
            }
            BlockPos down = origin.below(dy);
            if (level.getBlockState(down).isAir() && isConeSupport(level.getBlockState(down.below()))) {
                growCalciteCone(level, down, Direction.UP, 1 + random.nextInt(3), false);
                return true;
            }
        }
        return false;
    }

    static boolean isConeSupport(BlockState state) {
        return state.is(Blocks.CALCITE) || state.is(ModBlocks.CALCITE_CONE.get());
    }

    static void growCalciteCone(LevelAccessor level, BlockPos pos, Direction direction, int length, boolean tipMerge) {
        // 先按目标长度扫描可用空气，截断到贴合洞高，避免柱体末端顶到实心导致厚度重算级联变细
        int fit = 0;
        BlockPos.MutableBlockPos scan = pos.mutable();
        while (fit < length && level.getBlockState(scan).isAir()) {
            fit++;
            scan.move(direction);
        }
        if (fit == 0) {
            return;
        }
        final int finalLength = fit;
        BlockPos.MutableBlockPos mutable = pos.mutable();
        buildBaseToTipColumn(direction, finalLength, tipMerge, state -> {
            level.setBlock(mutable, state, 2);
            mutable.move(direction);
        });
    }

    private static void buildBaseToTipColumn(Direction direction, int length, boolean tipMerge, Consumer<BlockState> consumer) {
        if (length >= 3) {
            consumer.accept(createCone(direction, DripstoneThickness.BASE));
            for (int i = 0; i < length - 3; i++) {
                consumer.accept(createCone(direction, DripstoneThickness.MIDDLE));
            }
        }
        if (length >= 2) {
            consumer.accept(createCone(direction, DripstoneThickness.FRUSTUM));
        }
        if (length >= 1) {
            consumer.accept(createCone(direction, tipMerge ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
        }
    }

    private static BlockState createCone(Direction direction, DripstoneThickness thickness) {
        return ModBlocks.CALCITE_CONE.get().defaultBlockState()
                .setValue(CalciteConeBlock.TIP_DIRECTION, direction)
                .setValue(CalciteConeBlock.THICKNESS, thickness);
    }
}