package samebutdifferent.verdure.worldgen.biomemodifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import samebutdifferent.verdure.registry.VerdureBiomeModifierSerializers;
import samebutdifferent.verdure.registry.VerdureConfig;

import java.util.List;
import java.util.Optional;

public record VerdureBiomeModifier(List<HolderSet<Biome>> biomes, Holder<PlacedFeature> feature, GenerationStep.Decoration generationStep, Optional<String> configOption) implements BiomeModifier {

  public static final Codec<VerdureBiomeModifier> CODEC = RecordCodecBuilder.create(
    builder -> builder.group(
      Biome.LIST_CODEC.listOf().fieldOf("biomes").forGetter(VerdureBiomeModifier::biomes),
      PlacedFeature.CODEC.fieldOf("feature").forGetter(VerdureBiomeModifier::feature),
      GenerationStep.Decoration.CODEC.fieldOf("generationStep").forGetter(VerdureBiomeModifier::generationStep),
      PrimitiveCodec.STRING.optionalFieldOf("configOption").forGetter(VerdureBiomeModifier::configOption)
    ).apply(builder, VerdureBiomeModifier::new));

  @Override
  public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
    if(phase == Phase.ADD) {
      boolean flag = false;
      for(HolderSet<Biome> set : biomes) {
        if(set.contains(biome)) {
          flag = true;
          break;
        }
      }
      if (configOption.isPresent()) {
        boolean isEnabled = VerdureConfig.GENERATION_SETTINGS.get(configOption.get()).get();
        if (!isEnabled) {
          return;
        }
      }
      if (flag) {
        builder.getGenerationSettings().addFeature(generationStep, feature);
      }
    }
  }

  @Override
  public Codec<? extends BiomeModifier> codec() {
    return VerdureBiomeModifierSerializers.VERDURE_BIOME_MODIFIER.get();
  }
}
