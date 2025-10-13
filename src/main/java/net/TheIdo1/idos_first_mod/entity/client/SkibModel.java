package net.TheIdo1.idos_first_mod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.neoforge.client.entity.animation.json.AnimationHolder;

public class SkibModel extends EntityModel<SkibRenderState> implements ArmedModel {

    public static final AnimationHolder HEAD_SPIN_ANIMATION =
            Model.getAnimation(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "skib/head_spin"));

    public static final AnimationHolder WALK_ANIMATION =
            Model.getAnimation(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "skib/walk"));

    public static final AnimationHolder IDLE_ANIMATION =
            Model.getAnimation(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "skib/idle"));

    public static final AnimationHolder BONG_USE_ANIMATION =
            Model.getAnimation(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "skib/bong_use"));

    private static final float DEG2RAD = (float) Math.PI / 180f;


    private final KeyframeAnimation headSpin;
    private final KeyframeAnimation walk;
    private final KeyframeAnimation idle;
    private final KeyframeAnimation bongUse;


    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "skib"), "main");
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart LeftArm;
    private final ModelPart RightArm;
    private final ModelPart Body;
    private final ModelPart Head;
    private final ModelPart Hat;

    public SkibModel(ModelPart root) {
        super(root);
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.LeftArm = root.getChild("LeftArm");
        this.RightArm = root.getChild("RightArm");
        this.Body = root.getChild("Body");
        this.Head = root.getChild("Head");
        this.Hat = root.getChild("Hat");

        this.headSpin = HEAD_SPIN_ANIMATION.get().bake(root);
        this.walk = WALK_ANIMATION.get().bake(root);
        this.idle = IDLE_ANIMATION.get().bake(root);
        this.bongUse = BONG_USE_ANIMATION.get().bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(14, 13).addBox(-2.0F, 0.0F, -2.0F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 1.0F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 15).addBox(0.0F, 0.0F, -2.0F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 1.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(10, 22).addBox(-1.0F, -2.0F, -1.5F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 14.0F, 0.5F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(20, 22).addBox(-1.0F, -2.0F, -1.5F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 14.0F, 0.5F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 6).addBox(1.0F, -6.0F, -1.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 18.0F, 0.0F));

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(14, 6).addBox(-2.0F, -2.0F, -1.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.5F));

        PartDefinition Hat = partdefinition.addOrReplaceChild("Hat", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -3.25F, -2.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 0).addBox(-1.5F, -4.25F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 0.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.Head.yRot = headYaw * ((float)Math.PI / 180f);
        this.Head.xRot = headPitch *  ((float)Math.PI / 180f);
        this.Hat.yRot = headYaw * ((float)Math.PI / 180f);
        this.Hat.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    private void applyMainHandHoldingItemRotation(){
        this.RightArm.xRot = -40f * DEG2RAD + 3f * DEG2RAD;
        this.RightArm.yRot = 5f * DEG2RAD;
    }

    private void applyOffHandHoldingItemRotation(){
        this.LeftArm.xRot = -40f * DEG2RAD + 3f * DEG2RAD;
        this.LeftArm.yRot = -5f * DEG2RAD;
    }


    @Override
    public void setupAnim(SkibRenderState renderState) {
        //reset pose
        super.setupAnim(renderState);
        this.root().getAllParts().forEach(ModelPart::resetPose);

        boolean doApplyHeadRotation = true;


        if(renderState.isMoving){
            this.walk.apply(renderState.walkState, renderState.ageInTicks, 4);
        }

        if (renderState.isIdle){
            this.idle.apply(renderState.idleState, renderState.ageInTicks);
        }

        if (renderState.isBongUse){
            renderState.idleState.stop();
            doApplyHeadRotation = false;
            this.bongUse.apply(renderState.bongUseState, renderState.ageInTicks);
        }

        if (renderState.isHeadSpin){
            renderState.idleState.stop();
            doApplyHeadRotation = false;
            this.headSpin.apply(renderState.headSpinState, renderState.ageInTicks);
        }

        if(renderState.isItemInMainHand){
            this.applyMainHandHoldingItemRotation();
        }
        if(renderState.isItemInOffHand){
            this.applyOffHandHoldingItemRotation();
        }

        if(doApplyHeadRotation){
        this.applyHeadRotation(renderState.xRot, renderState.yRot);
        }


    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack poseStack) {
        // אם יש לך parent hierarchy (Body -> Arm), תתרגם דרכם.
        // אצלך הזרועות ילדים של ה-root, אז:
        this.root().translateAndRotate(poseStack);

        ModelPart armPart = (side == HumanoidArm.RIGHT) ? this.RightArm : this.LeftArm;

        armPart.translateAndRotate(poseStack);


    }

}
