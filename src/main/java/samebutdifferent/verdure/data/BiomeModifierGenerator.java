package samebutdifferent.verdure.data;

import com.google.gson.JsonElement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import samebutdifferent.verdure.Verdure;
import samebutdifferent.verdure.worldgen.biomemodifier.VerdureBiomeModifier;
import samebutdifferent.verdure.worldgen.biomemodifier.VerdureTreeReplacerBiomeModifier;

import javax.annotation.Nullable;
import java.util.*;

public class BiomeModifierGenerator {

  private static final Map<ResourceLocation, BiomeModifier> biomeModifierMap = new HashMap<>();

  public static Map<ResourceLocation, BiomeModifier> registerFeatures(RegistryOps<JsonElement> registryOps) {

    Registry<Biome> biomeRegistry = registryOps.registry(Registry.BIOME_REGISTRY).get();
    Registry<PlacedFeature> placedFeatureRegistry = registryOps.registry(Registry.PLACED_FEATURE_REGISTRY).get();

    replaceTree(placedFeatureRegistry, direct(biomeRegistry, Biomes.FOREST), "trees_birch_and_oak");
    replaceTree(placedFeatureRegistry, direct(biomeRegistry, Biomes.FLOWER_FOREST), "trees_flower_forest");
    replaceTree(placedFeatureRegistry, direct(biomeRegistry, Biomes.BIRCH_FOREST), "trees_birch");
    replaceTree(placedFeatureRegistry, direct(biomeRegistry, Biomes.OLD_GROWTH_BIRCH_FOREST), "birch_tall");
    replaceTree(placedFeatureRegistry, direct(biomeRegistry, Biomes.DARK_FOREST), "dark_forest_vegetation", "generate_dark_oak_humus");
    replaceTree(placedFeatureRegistry, direct(biomeRegistry, Biomes.SPARSE_JUNGLE), "trees_sparse_jungle");
    replaceTree(placedFeatureRegistry,fromTag(biomeRegistry, BiomeTags.IS_JUNGLE), "trees_jungle");
    replaceTree(placedFeatureRegistry,fromTag(biomeRegistry, BiomeTags.IS_TAIGA), "trees_taiga");
    replaceTree(placedFeatureRegistry, fromTag(biomeRegistry, BiomeTags.IS_SAVANNA), "trees_savanna");
    replaceTree(placedFeatureRegistry, direct(biomeRegistry, Biomes.WINDSWEPT_SAVANNA), "trees_windswept_savanna");

    //birch daisies
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(), List.of(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.FOREST)), GenerationStep.Decoration.VEGETAL_DECORATION, "birch_daisies", "generate_daisy_tree");

    //oak_daisies
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(), List.of(Biomes.FOREST)), GenerationStep.Decoration.VEGETAL_DECORATION, "oak_daisies", "generate_daisy_tree");

    //daisies patch
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(), List.of(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.FLOWER_FOREST)), GenerationStep.Decoration.VEGETAL_DECORATION, "patch_wildflowers", "generate_daisies_patch");

    //oak_hollow
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(), List.of(Biomes.FOREST, Biomes.FLOWER_FOREST)), GenerationStep.Decoration.VEGETAL_DECORATION, "oak_hollow", "generate_oak_hollow");

    //fancy oak hollow
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(), List.of(Biomes.FOREST, Biomes.FLOWER_FOREST)), GenerationStep.Decoration.VEGETAL_DECORATION, "fancy_oak_hollow", "generate_oak_hollow");

    //boulder
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_FOREST), List.of()), GenerationStep.Decoration.LOCAL_MODIFICATIONS, "boulder_stone", "generate_boulder");
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_RIVER, BiomeTags.IS_JUNGLE), List.of()), GenerationStep.Decoration.LOCAL_MODIFICATIONS, "boulder_slate", "generate_boulder");
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(), List.of(Biomes.TAIGA, Biomes.SNOWY_TAIGA)), GenerationStep.Decoration.LOCAL_MODIFICATIONS, "boulder_diorite", "generate_boulder");
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_SAVANNA), List.of()), GenerationStep.Decoration.LOCAL_MODIFICATIONS, "boulder_granite", "generate_boulder");
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_BEACH), List.of()), GenerationStep.Decoration.LOCAL_MODIFICATIONS, "boulder_andesite", "generate_boulder");

    //smooth dirt patch
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA, BiomeTags.IS_SAVANNA), List.of()), GenerationStep.Decoration.UNDERGROUND_ORES, "smooth_dirt_patch", "generate_smooth_dirt_patch");
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(Tags.Biomes.IS_SWAMP), List.of()), GenerationStep.Decoration.UNDERGROUND_ORES, "smooth_dirt_patch_swamp", "generate_smooth_dirt_patch");

    //fallen log
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "fallen_log", "generate_fallen_log");
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_SAVANNA), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "fallen_log_savanna", "generate_fallen_log");
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(), List.of(Biomes.CRIMSON_FOREST, Biomes.WARPED_FOREST)), GenerationStep.Decoration.VEGETAL_DECORATION, "fallen_log_nether", "generate_fallen_log_nether");

    //mushroom shelf
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_FOREST, BiomeTags.IS_TAIGA, Tags.Biomes.IS_SWAMP), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "mushroom_shelf", "generate_mushroom_shelf_surface");
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_OVERWORLD), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "underground_mushroom_shelf", "generate_mushroom_shelf_underground");

    //humus patch
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_JUNGLE), List.of()), GenerationStep.Decoration.UNDERGROUND_ORES, "humus_patch", "generate_humus_patch");

    //hanging moss
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(Tags.Biomes.IS_CAVE), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "hanging_moss", "generate_hanging_moss");

    //clover patch
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA, BiomeTags.IS_SAVANNA, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_SWAMP), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "patch_clover", "generate_clover_patch");

    //pebbles
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA, BiomeTags.IS_SAVANNA, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_SWAMP, BiomeTags.IS_BEACH), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "pebbles", "generate_pebbles");

    //rock
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA, BiomeTags.IS_SAVANNA, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_SWAMP, BiomeTags.IS_BEACH), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "rock", "generate_rock");

    //daisies patch
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_SWAMP), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "patch_daisies", "generate_daisies_patch");

    //blue daisies patch
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_SWAMP), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "patch_daisies_blue", "generate_daisies_patch");

    //pink daisies patch
    addFeature(placedFeatureRegistry, makeList(biomeRegistry, List.of(BiomeTags.IS_FOREST, BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_SWAMP), List.of()), GenerationStep.Decoration.VEGETAL_DECORATION, "patch_daisies_pink", "generate_daisies_patch");

    return biomeModifierMap;
  }

  private static HolderSet<Biome> direct(Registry<Biome> biomeRegistry, ResourceKey<Biome>... biomeResourceKeys) {
    List<Holder<Biome>> biomes = Arrays.stream(biomeResourceKeys).map((key) -> biomeRegistry.getHolder(key).get()).toList();
    return HolderSet.direct(biomes);
  }

  private static HolderSet<Biome> fromTag(Registry<Biome> biomeRegistry, TagKey<Biome> tag) {
    return new HolderSet.Named<>(biomeRegistry, tag);
  }

  private static void replaceTree(Registry<PlacedFeature> placedFeatureRegistry, HolderSet<Biome> biomes, String featureName, @Nullable String configOption) {
    Holder<PlacedFeature> originalFeature = placedFeatureRegistry.getHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation("minecraft", featureName)));
    Holder<PlacedFeature> replacementFeature = placedFeatureRegistry.getHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(Verdure.MOD_ID, featureName)));
    VerdureTreeReplacerBiomeModifier modifier = new VerdureTreeReplacerBiomeModifier(biomes, originalFeature, replacementFeature, Optional.ofNullable(configOption));
    ResourceLocation location = new ResourceLocation(Verdure.MOD_ID, "replace_" + featureName);
    biomeModifierMap.put(location, modifier);
  }

  private static void replaceTree(Registry<PlacedFeature> placedFeatureRegistry, HolderSet<Biome> biomes, String featureName) {
    replaceTree(placedFeatureRegistry, biomes, featureName, null);
  }

  private static void addFeature(Registry<PlacedFeature> placedFeatureRegistry, List<HolderSet<Biome>> biomes, GenerationStep.Decoration step, String featureName, @Nullable String configOption) {
    ResourceLocation location = new ResourceLocation(Verdure.MOD_ID, featureName);
    Holder<PlacedFeature> feature = placedFeatureRegistry.getHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, location));
    VerdureBiomeModifier modifier = new VerdureBiomeModifier(biomes, feature, step, Optional.ofNullable(configOption));
    biomeModifierMap.put(location, modifier);
  }

  private static List<HolderSet<Biome>> makeList(Registry<Biome> biomeRegistry, List<TagKey<Biome>> biomeTags, List<ResourceKey<Biome>> biomeKeys) {
    List<Holder<Biome>> biomes = biomeKeys.stream().map((key) -> biomeRegistry.getHolder(key).get()).toList();
    HolderSet<Biome> biomeSet = HolderSet.direct(biomes);

    List<HolderSet<Biome>> biomesSet = new ArrayList<>(biomeTags.stream().map(tag -> new HolderSet.Named<>(biomeRegistry, tag)).toList());
    biomesSet.add(biomeSet);

    return biomesSet;
  }
}
