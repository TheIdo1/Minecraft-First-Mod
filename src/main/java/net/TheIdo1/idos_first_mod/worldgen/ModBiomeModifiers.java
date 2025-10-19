package net.TheIdo1.idos_first_mod.worldgen;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.entity.ModEntities;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_WEED_BUSH = registerKey("add_weed_bush");

    public static final ResourceKey<BiomeModifier> SPAWN_SKIB = registerKey("spawn_skib");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {

        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_WEED_BUSH, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.PLAINS), biomes.getOrThrow(Biomes.FLOWER_FOREST), biomes.getOrThrow(Biomes.SUNFLOWER_PLAINS)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WEED_BUSH_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));


        WeightedList<MobSpawnSettings.SpawnerData> spawns = WeightedList.<MobSpawnSettings.SpawnerData>builder()
                .add(new MobSpawnSettings.SpawnerData(ModEntities.SKIB.get(), 2, 4), 20)
                .build();

        context.register(SPAWN_SKIB, new BiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.FOREST), biomes.getOrThrow(Biomes.PLAINS), biomes.getOrThrow(Biomes.FLOWER_FOREST), biomes.getOrThrow(Biomes.SUNFLOWER_PLAINS)),
                spawns));

    }


    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, name));
    }
}
