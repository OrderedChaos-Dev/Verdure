package samebutdifferent.verdure.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PackedDirtBlock extends Block {

  public PackedDirtBlock(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

    ItemStack itemstack = player.getItemInHand(hand);
    if(PotionUtils.getPotion(itemstack) == Potions.WATER) {
      player.setItemInHand(hand, ItemUtils.createFilledResult(itemstack, player, new ItemStack(Items.GLASS_BOTTLE)));
      level.playSound((Player)null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.PLAYERS, 1.0F, 1.0F);
      level.playSound((Player)null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
      if(!level.isClientSide) {
        ServerLevel serverlevel = (ServerLevel)level;
        for(int i = 0; i < 5; ++i) {
          serverlevel.sendParticles(ParticleTypes.SPLASH, (double)pos.getX() + level.random.nextDouble(), (double)(pos.getY() + 1), (double)pos.getZ() + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
        }
      }
      level.setBlockAndUpdate(pos, Blocks.PACKED_MUD.defaultBlockState());
    }



    return InteractionResult.sidedSuccess(level.isClientSide);
  }
}
