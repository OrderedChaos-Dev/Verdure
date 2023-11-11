package samebutdifferent.verdure.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import samebutdifferent.verdure.Verdure;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Verdure.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
//        generator.addProvider(event.includeServer(), new BlockstateGenerator(generator, event.getExistingFileHelper()));
//        generator.addProvider(event.includeServer(), new ItemModelGenerator(generator, event.getExistingFileHelper()));
//        generator.addProvider(event.includeServer(), new LangGenerator(generator, "en_us"));
//        generator.addProvider(event.includeServer(), new BlockTagGenerator(generator, event.getExistingFileHelper()));
//        generator.addProvider(event.includeServer(), new RecipeGenerator(generator));
//        generator.addProvider(event.includeServer(), new LootTableGenerator(generator));

        RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());
        Map<ResourceLocation, BiomeModifier> featureGenMap = BiomeModifierGenerator.registerFeatures(registryOps);
        JsonCodecProvider<BiomeModifier> jsonCodecProvider = JsonCodecProvider.forDatapackRegistry(generator, event.getExistingFileHelper(), Verdure.MOD_ID, registryOps, ForgeRegistries.Keys.BIOME_MODIFIERS, featureGenMap);
        generator.addProvider(event.includeServer(), jsonCodecProvider);
    }
}
