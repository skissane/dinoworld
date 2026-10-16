package com.example.dinoworld.entity;

import com.example.dinoworld.DinoWorld;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

public final class ModEntities {
    public static final ResourceKey<EntityType<?>> TRICERATOPS_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, DinoWorld.id("triceratops"));

    public static final EntityType<Triceratops> TRICERATOPS = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            TRICERATOPS_KEY,
            FabricEntityType.Builder.createMob(Triceratops::new, MobCategory.CREATURE, mob -> mob
                            .defaultAttributes(Triceratops::createAttributes)
                            .spawnPlacement(SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                                    Animal::checkAnimalSpawnRules))
                    .sized(1.8f, 1.9f)
                    .clientTrackingRange(10)
                    .build(TRICERATOPS_KEY));

    private ModEntities() {
    }

    public static void init() {
        // Small herds in the open grassy biomes.
        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                MobCategory.CREATURE, TRICERATOPS, 6, 2, 4);
    }
}
