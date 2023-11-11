<h2>**Changelog**</h2>  
Here's what I did  
build.gradle/gradle.properties:
- moved to official mappings (just cause)
- removed cyanide, because I was too lazy to rebuild gradle with it (no feature order cycles anyway)
- added 'idea' plugin, because I use intellij
- Bumped mod version and forge version  

The mod:
- Render layers are set in block models now, so I did that
- BiomeLoadingEvent doesn't exist anymore, so I needed to use Forge's BiomeModifiers
- There are 2 BiomeModifiers: VerdureBiomeModifier and VerdureTreeReplacerBiomeModifier
- VerdureBiomeModifier is for regular features (see Codec on how it is serialized)
- VerdureTreeReplacerBiomeModifier is for replacing tree features (see Codec on how it is serialized)
- I wrote a data generator (BiomeModifierGenerator) to generate the data files.
- Because feature gen is now in datapack format, I mapped config options in VerdureConfig to string keys so the config can still enable/disable features
- The data generator also just uses magic strings to identify placed features. This is kinda sketch but it works
- Biome Categories were removed in 1.19 in favor of tags, and this mod relied heavily on them. I tried to map the old categories to either vanilla/forge.
- This does mean there are might be some discrepancies from the 1.18 version
- For world gen features that didn't really fit a tag (e.g. birch stuff), I just hardcoded the biomes.
- I didn't really take a look at the other data generators but all the stuff it would generate is already in the resources folder so I commented them out in the GatherDataEvent subscriber
- I only tested that the config options work and that things generate

![2023-11-10_18 35 12](https://github.com/OrderedChaos-Dev/Verdure/assets/41054981/690d4e26-7cb0-44db-8da0-4d8e28d68067)  
![2023-11-10_18 35 59](https://github.com/OrderedChaos-Dev/Verdure/assets/41054981/f715c0cd-c2f3-438b-a554-dcf91c476f44)  
![2023-11-10_18 39 13](https://github.com/OrderedChaos-Dev/Verdure/assets/41054981/0427cafc-79f5-4d64-aa47-fb08017305f6)  
![2023-11-10_18 40 41](https://github.com/OrderedChaos-Dev/Verdure/assets/41054981/9d6a9702-51ca-4ef5-a628-42965fd710c3)  
