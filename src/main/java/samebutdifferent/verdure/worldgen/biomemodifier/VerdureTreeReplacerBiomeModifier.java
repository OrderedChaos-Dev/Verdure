package samebutdifferent.verdure.worldgen.biomemodifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.RegistryObject;
import samebutdifferent.verdure.Verdure;
import samebutdifferent.verdure.registry.VerdureBiomeModifierSerializers;
import samebutdifferent.verdure.registry.VerdureConfig;
import samebutdifferent.verdure.util.CodecUtils;

import java.util.List;
import java.util.Optional;

public record VerdureTreeReplacerBiomeModifier(HolderSet<Biome> biomes, Holder<PlacedFeature> originalFeature, Holder<PlacedFeature> replacementFeature, Optional<String> configOption) implements BiomeModifier {

  public static final Codec<VerdureTreeReplacerBiomeModifier> CODEC = RecordCodecBuilder.create(
    builder -> builder.group(
      Biome.LIST_CODEC.fieldOf("biomes").forGetter(VerdureTreeReplacerBiomeModifier::biomes),
      PlacedFeature.CODEC.fieldOf("originalFeature").forGetter(VerdureTreeReplacerBiomeModifier::originalFeature),
      PlacedFeature.CODEC.fieldOf("feature").forGetter(VerdureTreeReplacerBiomeModifier::replacementFeature),
      PrimitiveCodec.STRING.optionalFieldOf("configOption").forGetter(VerdureTreeReplacerBiomeModifier::configOption)
    ).apply(builder, VerdureTreeReplacerBiomeModifier::new));

  @Override
  public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
    if (biomes.contains(biome)) {
      if (configOption.isPresent()) {
        boolean isEnabled = VerdureConfig.GENERATION_SETTINGS.get(configOption.get()).get();
        if (!isEnabled) {
          return;
        }
      }
      if (VerdureConfig.GENERATE_TREE_BRANCHES.get() || VerdureConfig.FALLEN_LEAVES_CHANCE.get() > 0) {
        List<Holder<PlacedFeature>> vegetalFeatures = builder.getGenerationSettings().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);
        boolean replaced = vegetalFeatures.removeIf(placedFeatureSupplier -> CodecUtils.serializeAndCompareFeature(placedFeatureSupplier.value(), originalFeature.value()));
        if (replaced) {
          vegetalFeatures.add(replacementFeature);
          Verdure.LOGGER.debug("Replaced " + originalFeature.unwrapKey().get().location() + " in " + biome.unwrapKey().get().location() + " with " + replacementFeature.unwrapKey().get().location());
        }
      }
    }
  }

  @Override
  public Codec<? extends BiomeModifier> codec() {
    return VerdureBiomeModifierSerializers.VERDURE_TREE_REPLACER_BIOME_MODIFIER.get();
  }
}
