package net.TheIdo1.idos_first_mod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class SkibRenderLayer extends RenderLayer<SkibRenderState, SkibModel> {

    private final SkibModel model;

    public SkibRenderLayer(RenderLayerParent<SkibRenderState, SkibModel> renderer, EntityModelSet entityModelSet) {
        super(renderer);
        this.model = new SkibModel(entityModelSet.bakeLayer(SkibModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, SkibRenderState renderState, float yRot, float xRot) {
        var vc = bufferSource.getBuffer(RenderType.entityCutoutNoCull(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "textures/entity/skib.png")));
        this.getParentModel().renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, -1);
    }
}
