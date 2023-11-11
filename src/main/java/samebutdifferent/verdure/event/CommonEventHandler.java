package samebutdifferent.verdure.event;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStartedEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import samebutdifferent.verdure.Verdure;
import samebutdifferent.verdure.block.WallRootsBlock;
import samebutdifferent.verdure.registry.VerdureBlocks;
import samebutdifferent.verdure.registry.VerdureConfig;
import samebutdifferent.verdure.registry.VerdurePlacedFeatures;
import samebutdifferent.verdure.util.CodecUtils;

@Mod.EventBusSubscriber(modid = Verdure.MOD_ID)
public class CommonEventHandler {

//    @SubscribeEvent
//    public static void onBiomeLoad(BiomeLoadingEvent event) {
//        BiomeGenerationSettingsBuilder builder = event.getGeneration();
//        ResourceLocation location = event.getName();
//
//        if (location != null) {
//            switch (event.getCategory()) {

//                case NETHER -> {
//                    if (location.equals(Biomes.WARPED_FOREST.location()) || location.equals(Biomes.CRIMSON_FOREST.location())) {
//                        addFeature(builder, GenerationStep.Decoration.VEGETAL_DECORATION, VerdurePlacedFeatures.FALLEN_LOG_NETHER, VerdureConfig.GENERATE_FALLEN_LOG_NETHER.get());
//                    }
//                }
//            }
//        }
//        addFeature(builder, GenerationStep.Decoration.VEGETAL_DECORATION, VerdurePlacedFeatures.UNDERGROUND_MUSHROOM_SHELF, VerdureConfig.GENERATE_MUSHROOM_SHELF_UNDERGROUND.get());
//    }

    private static void addFeature(BiomeGenerationSettingsBuilder builder, GenerationStep.Decoration decorationStage, RegistryObject<PlacedFeature> feature, boolean config) {
        if (config) {
            builder.addFeature(decorationStage, feature.getHolder().orElseThrow());
        }
    }

    private static void addGrassyAreaFeatures(BiomeGenerationSettingsBuilder builder) {
        addFeature(builder, GenerationStep.Decoration.VEGETAL_DECORATION, VerdurePlacedFeatures.PEBBLES, VerdureConfig.GENERATE_PEBBLES.get());
        addFeature(builder, GenerationStep.Decoration.VEGETAL_DECORATION, VerdurePlacedFeatures.ROCK, VerdureConfig.GENERATE_ROCK.get());
        addFeature(builder, GenerationStep.Decoration.VEGETAL_DECORATION, VerdurePlacedFeatures.PATCH_DAISIES, VerdureConfig.GENERATE_DAISIES_PATCH.get());
        addFeature(builder, GenerationStep.Decoration.VEGETAL_DECORATION, VerdurePlacedFeatures.PATCH_DAISIES_BLUE, VerdureConfig.GENERATE_DAISIES_PATCH.get());
        addFeature(builder, GenerationStep.Decoration.VEGETAL_DECORATION, VerdurePlacedFeatures.PATCH_DAISIES_PINK, VerdureConfig.GENERATE_DAISIES_PATCH.get());
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        CodecUtils.clearCache();
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = player.level;
        ItemStack stack = event.getItemStack();
        if (WallRootsBlock.canAttachTo(level, event.getPos(), event.getFace())) {
            if (event.getFace().getAxis().getPlane() == Direction.Plane.HORIZONTAL) {
                BlockPos pos = event.getHitVec().getBlockPos().relative(event.getFace());
                if (level.isEmptyBlock(pos)) {
                    if (stack.is(Items.BROWN_MUSHROOM)) {
                        placeBlock(event, player, level, pos, stack, VerdureBlocks.BROWN_MUSHROOM_SHELF.get());
                    } else if (stack.is(Items.RED_MUSHROOM)) {
                        placeBlock(event, player, level, pos, stack, VerdureBlocks.RED_MUSHROOM_SHELF.get());
                    } else if (stack.is(Items.HANGING_ROOTS)) {
                        placeBlock(event, player, level, pos, stack, VerdureBlocks.WALL_ROOTS.get());
                    }
                }
            }
        }
    }

    private static void placeBlock(PlayerInteractEvent.RightClickBlock event, Player player, Level level, BlockPos pos, ItemStack stack, Block block) {
        BlockState state = block.getStateForPlacement(new BlockPlaceContext(player, event.getHand(), stack, event.getHitVec()));
        if (state != null && block.canSurvive(state, level, pos)) {
            level.setBlockAndUpdate(pos, state);
            player.swing(event.getHand());
            block.defaultBlockState().getBlock().setPlacedBy(level, pos, state, player, stack);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, stack);
            }
            level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
            SoundType soundtype = state.getSoundType(level, pos, player);
            level.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getToolAction().equals(ToolActions.HOE_TILL)) {
            if (event.getState().is(VerdureBlocks.SMOOTH_DIRT.get())) {
                event.setFinalState(Blocks.FARMLAND.defaultBlockState());
            }
        }
    }
}
