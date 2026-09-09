package com.pasterdream.pasterdreammod.worldgen.feature;

import com.pasterdream.pasterdreammod.init.ModBlocks;
import com.pasterdream.pasterdreammod.init.ModTreeDecoratorTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LightBallTreeDecorator extends TreeDecorator {

    public static final LightBallTreeDecorator INSTANCE = new LightBallTreeDecorator();

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        if (random.nextFloat() > 0.2F) return;

        Set<BlockPos> candidates = new LinkedHashSet<>();
        for (BlockPos leaf : context.leaves()) {
            BlockPos below = leaf.below();
            if (context.isAir(below)) {
                candidates.add(below);
            }
        }
        if (candidates.isEmpty()) return;

        List<BlockPos> positions = new ArrayList<>(candidates);
        int count = 1 + random.nextInt(2);
        for (int i = 0; i < count && !positions.isEmpty(); i++) {
            BlockPos target = positions.remove(random.nextInt(positions.size()));
            context.setBlock(target, ModBlocks.LIGHT_BALL.get().defaultBlockState());
        }
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecoratorTypes.LIGHT_BALL.get();
    }
}