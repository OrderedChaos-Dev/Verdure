package samebutdifferent.verdure.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import samebutdifferent.verdure.Verdure;
import samebutdifferent.verdure.worldgen.biomemodifier.VerdureBiomeModifier;
import samebutdifferent.verdure.worldgen.biomemodifier.VerdureTreeReplacerBiomeModifier;

public class VerdureBiomeModifierSerializers {
  public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Verdure.MOD_ID);

  public static final RegistryObject<Codec<VerdureBiomeModifier>> VERDURE_BIOME_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("verdure_biome_modifier", () -> VerdureBiomeModifier.CODEC);
  public static final RegistryObject<Codec<VerdureTreeReplacerBiomeModifier>> VERDURE_TREE_REPLACER_BIOME_MODIFIER = BIOME_MODIFIER_SERIALIZERS.register("verdure_tree_replacer_biome_modifier", () -> VerdureTreeReplacerBiomeModifier.CODEC);
}
