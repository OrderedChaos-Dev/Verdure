package samebutdifferent.verdure.worldgen.treedecorator;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import samebutdifferent.verdure.Verdure;
import samebutdifferent.verdure.block.HollowLogBlock;
import samebutdifferent.verdure.registry.VerdureBlocks;
import samebutdifferent.verdure.registry.VerdureTreeDecoratorTypes;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class HollowLogDecorator extends TreeDecorator {
    public static final Codec<HollowLogDecorator> CODEC = Codec.unit(() -> HollowLogDecorator.INSTANCE);
    public static final HollowLogDecorator INSTANCE = new HollowLogDecorator();

    @Override
    protected TreeDecoratorType<?> type() {
        return VerdureTreeDecoratorTypes.HOLLOW_LOG.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        List<BlockPos> pLogPositions = context.logs();
        RandomSource pRandom = context.random();
        LevelSimulatedReader pLevel = context.level();
        BlockPos pos = pLogPositions.get(2);
        Direction direction = Direction.from2DDataValue(pRandom.nextInt(4));

        context.setBlock(pos, VerdureBlocks.HOLLOW_LOG.get().defaultBlockState().setValue(HollowLogBlock.FACING, direction));

        RandomizableContainerBlockEntity.setLootTable((BlockGetter) pLevel, pRandom, pos, new ResourceLocation(Verdure.MOD_ID, "chests/hollow_log"));
    }
}
