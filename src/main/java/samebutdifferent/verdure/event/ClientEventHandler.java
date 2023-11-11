package samebutdifferent.verdure.event;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import samebutdifferent.verdure.Verdure;
import samebutdifferent.verdure.registry.VerdureBlocks;

@Mod.EventBusSubscriber(modid = Verdure.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.getBlockColors().register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null ? BiomeColors.getAverageFoliageColor(pLevel, pPos) : FoliageColor.getDefaultColor(),
                VerdureBlocks.FALLEN_OAK_LEAVES.get(), VerdureBlocks.FALLEN_JUNGLE_LEAVES.get(), VerdureBlocks.FALLEN_ACACIA_LEAVES.get(), VerdureBlocks.FALLEN_DARK_OAK_LEAVES.get(),
                VerdureBlocks.OAK_BRANCH.get());
        event.getBlockColors().register((pState, pLevel, pPos, pTintIndex) -> FoliageColor.getEvergreenColor(), VerdureBlocks.FALLEN_SPRUCE_LEAVES.get());
        event.getBlockColors().register((pState, pLevel, pPos, pTintIndex) -> FoliageColor.getBirchColor(), VerdureBlocks.FALLEN_BIRCH_LEAVES.get(), VerdureBlocks.BIRCH_BRANCH.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.getItemColors().register((pStack, pTintIndex) -> {
            BlockState blockstate = ((BlockItem)pStack.getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(blockstate, null, null, pTintIndex);
        }, VerdureBlocks.FALLEN_OAK_LEAVES.get(), VerdureBlocks.FALLEN_JUNGLE_LEAVES.get(), VerdureBlocks.FALLEN_ACACIA_LEAVES.get(), VerdureBlocks.FALLEN_DARK_OAK_LEAVES.get(), VerdureBlocks.FALLEN_SPRUCE_LEAVES.get(), VerdureBlocks.FALLEN_BIRCH_LEAVES.get());
    }
}
