package com.example.dinoworld;

import com.example.dinoworld.entity.ModEntities;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class ModItems {
    public static final ResourceKey<Item> TRICERATOPS_SPAWN_EGG_KEY =
            ResourceKey.create(Registries.ITEM, DinoWorld.id("triceratops_spawn_egg"));

    public static final Item TRICERATOPS_SPAWN_EGG = Registry.register(
            BuiltInRegistries.ITEM,
            TRICERATOPS_SPAWN_EGG_KEY,
            new SpawnEggItem(new Item.Properties().spawnEgg(ModEntities.TRICERATOPS).setId(TRICERATOPS_SPAWN_EGG_KEY)));

    private ModItems() {
    }

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS)
                .register(output -> output.accept(TRICERATOPS_SPAWN_EGG));
    }
}
