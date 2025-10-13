package net.TheIdo1.idos_first_mod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;

public class SkibItemRenderLayer extends RenderLayer<SkibRenderState, SkibModel> {

    private final ItemRenderer itemRenderer;

    public SkibItemRenderLayer(RenderLayerParent<SkibRenderState, SkibModel> renderer, ItemRenderer itemRenderer) {
        super(renderer);
        this.itemRenderer = itemRenderer;
    }


    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SkibRenderState renderState, float yRot, float xRot) {

        if (!renderState.itemInMainHand.isEmpty()) {
            poseStack.pushPose();
            this.getParentModel().translateToHand(renderState.mainHand, poseStack);
            // התאמות קטנות לנקודת אחיזה (שנה לפי המודל שלך)
            poseStack.translate(0.0, 0.18, -0.18);
            poseStack.scale( 0.7f, 0.7f, 0.7f);
            poseStack.rotateAround(Axis.XP.rotationDegrees(180.0F),0f,0f,0f);
            poseStack.rotateAround(Axis.YP.rotationDegrees(180.0F),0f,0f,0f);
            poseStack.rotateAround(Axis.XP.rotationDegrees(-45f), 0,0, 0);

            itemRenderer.renderStatic(renderState.itemInMainHand,
                    ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                    packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, renderState.level, renderState.entityId);

            poseStack.popPose();
        }

        if (!renderState.itemInOffHand.isEmpty()) {
            poseStack.pushPose();
            this.getParentModel().translateToHand(renderState.offHand, poseStack);
            // התאמות קטנות לנקודת אחיזה (שנה לפי המודל שלך)
            poseStack.translate(0.0, 0.18, -0.18);
            poseStack.scale( 0.7f, 0.7f, 0.7f);
            poseStack.rotateAround(Axis.XP.rotationDegrees(180.0F),0f,0f,0f);
            poseStack.rotateAround(Axis.YP.rotationDegrees(180.0F),0f,0f,0f);
            poseStack.rotateAround(Axis.XP.rotationDegrees(-45f), 0,0, 0);


            itemRenderer.renderStatic(renderState.itemInOffHand,
                    ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                    packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, renderState.level, renderState.entityId);

            poseStack.popPose();
        }


    }

}
