package net.TheIdo1.idos_first_mod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.entity.custom.SkibEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class SkibRenderer extends LivingEntityRenderer<SkibEntity, SkibRenderState, SkibModel> {

    private final float baseShadow = 0.3f;

    public SkibRenderer(EntityRendererProvider.Context context) {
        super(context, new SkibModel(context.bakeLayer(SkibModel.LAYER_LOCATION)), 0.3f);

        this.addLayer(new SkibRenderLayer(this, context.getModelSet()));

        this.addLayer(new SkibItemRenderLayer(this, Minecraft.getInstance().getItemRenderer()));
    }


    @Override
    public void extractRenderState(SkibEntity entity, SkibRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.ageInTicks = entity.tickCount + partialTicks;
        state.level = entity.level();

        state.itemInMainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        state.mainHand = entity.isLeftHanded() ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        state.isItemInMainHand = !state.itemInMainHand.isEmpty();

        state.itemInOffHand = entity.getItemInHand(InteractionHand.OFF_HAND);
        state.offHand = entity.isLeftHanded() ? HumanoidArm.RIGHT : HumanoidArm.LEFT;
        state.isItemInOffHand = !state.itemInOffHand.isEmpty();


        state.idleState.copyFrom(entity.idleAnimationState);
        state.isIdle = state.idleState.isStarted();


        state.isMoving = state.walkAnimationSpeed >0.05F;

        if(state.isMoving){
            state.idleState.stop();
            if(!state.walkState.isStarted()){
            state.walkState.start((int) state.ageInTicks);
            }
        }

        state.isBongUse = entity.bongUseAnimationState.isStarted();
        state.bongUseState.copyFrom(entity.bongUseAnimationState);

        state.isHeadSpin = entity.headSpinAnimationState.isStarted();
        state.headSpinState.copyFrom(entity.headSpinAnimationState);

        state.isBaby = entity.isBaby();
    }

    @Override
    protected void scale(SkibRenderState renderState, PoseStack poseStack) {
        float s = renderState.isBaby ? 0.5f : 1.0f;
        poseStack.scale(s, s, s);
        this.shadowRadius = baseShadow * s;
    }

    @Override
    public void render(SkibRenderState skibRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(skibRenderState, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(SkibRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "textures/entity/skib.png");
    }

    @Override
    protected boolean shouldShowName(SkibEntity skibEntity, double distance) {
        if (skibEntity.hasCustomName()){
            return super.shouldShowName(skibEntity,distance);
        }
        return false;
    }

    @Override
    public SkibRenderState createRenderState() {
        return new SkibRenderState();
    }




}
