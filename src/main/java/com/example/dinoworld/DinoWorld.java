package com.example.dinoworld;

import com.example.dinoworld.entity.ModEntities;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class DinoWorld implements ModInitializer {
    public static final String MOD_ID = "dinoworld";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModEntities.init();
        ModItems.init();
    }
}
