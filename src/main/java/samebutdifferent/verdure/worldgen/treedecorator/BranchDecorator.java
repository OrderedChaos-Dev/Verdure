package samebutdifferent.verdure.worldgen.treedecorator;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import samebutdifferent.verdure.registry.VerdureConfig;
import samebutdifferent.verdure.registry.VerdureTreeDecoratorTypes;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class BranchDecorator extends TreeDecorator {
    public static final Codec<BranchDecorator> CODEC = BlockState.CODEC.fieldOf("branch").xmap(BranchDecorator::new, (decorator) -> decorator.state).codec();
    private final BlockState state;

    public BranchDecorator(BlockState state) {
        this.state = state;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return VerdureTreeDecoratorTypes.BRANCH.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        List<BlockPos> pLeafPositions = context.leaves();
        List<BlockPos> pLogPositions = context.logs();
        RandomSource pRandom = context.random();
        LevelSimulatedReader pLevel = context.level();

        if (!pLeafPositions.isEmpty() && VerdureConfig.GENERATE_TREE_BRANCHES.get()) {
            int lowestLeafY = pLeafPositions.get(0).getY();
            List<BlockPos> exposedLogs = pLogPositions.stream().filter(blockPos -> blockPos.getY() < lowestLeafY).toList();
            Direction zRand = pRandom.nextBoolean() ? Direction.NORTH : Direction.SOUTH;
            Direction xRand = pRandom.nextBoolean() ? Direction.EAST : Direction.WEST;
            if (exposedLogs.size() > 2) {
                BlockPos posFirst = exposedLogs.get(exposedLogs.size() - 1);
                if (context.isAir(posFirst.relative(xRand))) {
                    context.setBlock(posFirst.relative(xRand), this.state.setValue(HorizontalDirectionalBlock.FACING, xRand));
                }
                if (exposedLogs.size() > 3) {
                    BlockPos posSecond = exposedLogs.get(exposedLogs.size() - 2);
                    if (context.isAir(posSecond.relative(zRand))) {
                        context.setBlock(posSecond.relative(zRand), this.state.setValue(HorizontalDirectionalBlock.FACING, zRand));
                    }
                }
            }
        }
    }
}
