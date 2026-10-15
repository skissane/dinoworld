package com.example.dinoworld.client;

import com.example.dinoworld.DinoWorld;
import com.example.dinoworld.DinoWorldClient;
import com.example.dinoworld.entity.Triceratops;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class TriceratopsRenderer extends MobRenderer<Triceratops, LivingEntityRenderState, TriceratopsModel> {
    private static final Identifier TEXTURE = DinoWorld.id("textures/entity/triceratops.png");

    public TriceratopsRenderer(EntityRendererProvider.Context context) {
        super(context, new TriceratopsModel(context.bakeLayer(DinoWorldClient.TRICERATOPS_LAYER)), 1.0f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    protected void scale(LivingEntityRenderState state, PoseStack poseStack) {
        if (state.isBaby) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        }
    }
}
