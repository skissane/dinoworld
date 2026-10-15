package com.example.dinoworld;

import com.example.dinoworld.client.TriceratopsModel;
import com.example.dinoworld.client.TriceratopsRenderer;
import com.example.dinoworld.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;

public class DinoWorldClient implements ClientModInitializer {
    public static final ModelLayerLocation TRICERATOPS_LAYER = new ModelLayerLocation(DinoWorld.id("triceratops"), "main");

    private static final Identifier HUD_TEXT_ID = DinoWorld.id("hud_text");
    private static final String HUD_TEXT = "DinoWorld " + FabricLoader.getInstance()
            .getModContainer(DinoWorld.MOD_ID)
            .map(mod -> mod.getMetadata().getVersion().getFriendlyString())
            .orElse("unknown");

    @Override
    public void onInitializeClient() {
        HudElementRegistry.addLast(HUD_TEXT_ID, this::renderHudText);

        ModelLayerRegistry.registerModelLayer(TRICERATOPS_LAYER, TriceratopsModel::createBodyLayer);
        EntityRenderers.register(ModEntities.TRICERATOPS, TriceratopsRenderer::new);
    }

    private void renderHudText(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        graphics.text(client.font, HUD_TEXT, 10, 10, 0xFFFFFFFF, true);
    }
}
