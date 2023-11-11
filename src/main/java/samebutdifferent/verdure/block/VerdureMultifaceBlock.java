package samebutdifferent.verdure.block;

import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;

public class VerdureMultifaceBlock extends MultifaceBlock {
  public VerdureMultifaceBlock(Properties props) {
    super(props);
  }

  @Override
  public MultifaceSpreader getSpreader() {
    return null;
  }
}
