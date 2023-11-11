package samebutdifferent.verdure.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;

public class FallenLogFeature extends Feature<NoneFeatureConfiguration> {
    public FallenLogFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BlockState log = this.getBiomeLog(level, origin);
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos.MutableBlockPos mutable = origin.mutable();
        boolean mossy = random.nextBoolean() && level.getBiome(origin).is(BiomeTags.IS_FOREST);

        // check for available space
        for (int i = 0; i < 4; i++) {
            if ((level.getBlockState(mutable).getMaterial().isReplaceable() || level.getBlockState(mutable).getMaterial().equals(Material.DECORATION)) && (level.getBlockState(mutable.below()).is(BlockTags.DIRT) || level.getBlockState(mutable.below()).is(BlockTags.NYLIUM))) {
                mutable.move(direction);
            } else {
                return false;
            }
        }

        mutable.set(origin);

        // place logs
        for (int i = 0; i < 4; i++) {
            level.setBlock(mutable, log.setValue(RotatedPillarBlock.AXIS, direction.getAxis()), 3);
            if (mossy && random.nextBoolean()) {
                level.setBlock(mutable.above(), Blocks.MOSS_CARPET.defaultBlockState(), 3);
            }
            mutable.move(direction);
        }

        return true;
    }

    private BlockState getBiomeLog(WorldGenLevel level, BlockPos pos) {
        Holder<Biome> biome = level.getBiome(pos);
        ResourceLocation resourceLocation = biome.unwrapKey().get().location();
        if (resourceLocation.getPath().contains("birch")) {
            return Blocks.BIRCH_LOG.defaultBlockState();
        } else if (resourceLocation.getPath().equals("dark_forest")) {
            return Blocks.DARK_OAK_LOG.defaultBlockState();
        } else if (resourceLocation.getPath().equals("warped_forest")) {
            return Blocks.WARPED_STEM.defaultBlockState();
        } else if (resourceLocation.getPath().equals("crimson_forest")) {
            return Blocks.CRIMSON_STEM.defaultBlockState();
        } else {
            if (biome.is(BiomeTags.IS_JUNGLE)) {
                return Blocks.JUNGLE_LOG.defaultBlockState();
            } else if (biome.is(BiomeTags.IS_TAIGA)) {
                return Blocks.SPRUCE_LOG.defaultBlockState();
            } else if (biome.is(BiomeTags.IS_SAVANNA)) {
                return Blocks.ACACIA_LOG.defaultBlockState();
            }
            return Blocks.OAK_LOG.defaultBlockState();
        }
    }
}
