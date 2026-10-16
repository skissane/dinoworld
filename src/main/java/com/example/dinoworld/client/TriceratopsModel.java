package com.example.dinoworld.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/**
 * A blocky quadruped: barrel body, four legs, a tail, and a head carrying the frill, beak and three horns.
 * The geometry lives in {@code assets/dinoworld/geo/triceratops.geo.json} (edit it in Blockbench); this class
 * animates it, so the bones named in the constructor must keep those names.
 */
public class TriceratopsModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public TriceratopsModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.tail = root.getChild("tail");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        return GeoModelLoader.load("triceratops");
    }

    @Override
    public void setupAnim(LivingEntityRenderState state) {
        super.setupAnim(state);

        // Head follows the look direction, but a heavy frill keeps it within a narrow range.
        this.head.yRot = Mth.clamp(state.yRot, -30.0f, 30.0f) * Mth.DEG_TO_RAD;
        this.head.xRot = Mth.clamp(state.xRot, -15.0f, 20.0f) * Mth.DEG_TO_RAD;

        float pos = state.walkAnimationPos;
        float speed = state.walkAnimationSpeed;
        float swing = Mth.cos(pos * 0.6662f) * 1.2f * speed;
        this.rightHindLeg.xRot = swing;
        this.leftHindLeg.xRot = -swing;
        this.rightFrontLeg.xRot = -swing;
        this.leftFrontLeg.xRot = swing;
        this.tail.yRot = Mth.cos(pos * 0.3331f) * 0.25f * speed;
    }
}
