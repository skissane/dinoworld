package com.example.dinoworld.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

/**
 * A blocky quadruped: barrel body, four legs, a tail, and a head carrying the frill, beak and three horns.
 * Model space has y pointing down with the ground at y = 24.
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

    // Texture layout (128x64): see the region comments; each cube's UV block is 2*(d+w) by d+h.
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-8.0f, 0.0f, -12.0f, 16.0f, 14.0f, 24.0f),
                PartPose.offset(0.0f, 0.0f, 2.0f));

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(80, 0).addBox(-6.0f, -5.0f, -12.0f, 12.0f, 10.0f, 12.0f),
                PartPose.offset(0.0f, 6.0f, -10.0f));
        head.addOrReplaceChild("frill",
                CubeListBuilder.create().texOffs(80, 22).addBox(-11.0f, -14.0f, 0.0f, 22.0f, 16.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, -1.0f, -2.0f, 0.4f, 0.0f, 0.0f));
        PartDefinition beak = head.addOrReplaceChild("beak",
                CubeListBuilder.create().texOffs(60, 40).addBox(-3.0f, 0.0f, -5.0f, 6.0f, 6.0f, 5.0f),
                PartPose.offset(0.0f, -1.0f, -12.0f));
        beak.addOrReplaceChild("nose_horn",
                CubeListBuilder.create().texOffs(108, 42).addBox(-1.0f, -4.0f, -1.0f, 2.0f, 4.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, -3.0f, -0.3f, 0.0f, 0.0f));
        head.addOrReplaceChild("right_brow_horn",
                CubeListBuilder.create().texOffs(84, 42).addBox(-1.0f, -1.0f, -9.0f, 2.0f, 2.0f, 9.0f),
                PartPose.offsetAndRotation(-3.5f, -4.0f, -9.0f, -0.4f, 0.0f, 0.0f));
        head.addOrReplaceChild("left_brow_horn",
                CubeListBuilder.create().texOffs(84, 42).mirror().addBox(-1.0f, -1.0f, -9.0f, 2.0f, 2.0f, 9.0f),
                PartPose.offsetAndRotation(3.5f, -4.0f, -9.0f, -0.4f, 0.0f, 0.0f));

        root.addOrReplaceChild("tail",
                CubeListBuilder.create().texOffs(24, 38).addBox(-3.0f, 0.0f, 0.0f, 6.0f, 6.0f, 12.0f),
                PartPose.offsetAndRotation(0.0f, 3.0f, 14.0f, -0.3f, 0.0f, 0.0f));

        CubeListBuilder leg = CubeListBuilder.create().texOffs(0, 38).addBox(-3.0f, 0.0f, -3.0f, 6.0f, 10.0f, 6.0f);
        CubeListBuilder mirroredLeg = CubeListBuilder.create().texOffs(0, 38).mirror().addBox(-3.0f, 0.0f, -3.0f, 6.0f, 10.0f, 6.0f);
        root.addOrReplaceChild("right_hind_leg", leg, PartPose.offset(-5.0f, 14.0f, 10.0f));
        root.addOrReplaceChild("left_hind_leg", mirroredLeg, PartPose.offset(5.0f, 14.0f, 10.0f));
        root.addOrReplaceChild("right_front_leg", leg, PartPose.offset(-5.0f, 14.0f, -6.0f));
        root.addOrReplaceChild("left_front_leg", mirroredLeg, PartPose.offset(5.0f, 14.0f, -6.0f));

        return LayerDefinition.create(mesh, 128, 64);
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
