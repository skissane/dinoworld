package com.example.dinoworld;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class DinoWorldClient implements ClientModInitializer {
    private static final Identifier HUD_TEXT_ID = Identifier.fromNamespaceAndPath("dinoworld", "hud_text");

    @Override
    public void onInitializeClient() {
        HudElementRegistry.addLast(HUD_TEXT_ID, this::renderHudText);
    }

    private void renderHudText(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();
        graphics.text(client.font, "DinoWorld", 10, 10, 0xFFFFFFFF, true);
    }
}
