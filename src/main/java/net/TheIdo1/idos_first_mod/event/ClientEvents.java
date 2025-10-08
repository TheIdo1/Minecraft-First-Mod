package net.TheIdo1.idos_first_mod.event;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.effect.ModEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(modid = IdosFirstMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    private static final ResourceLocation HIGH_OVERLAY =
            ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "textures/gui/high_overlay.png");
    private static final ResourceLocation LAYER_ID =
            ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "high_overlay");


    @SubscribeEvent
    public static void registerLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(LAYER_ID, (gui, delta) -> {
            var mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) return;
            var inst = mc.player.getEffect(ModEffects.HIGH_EFFECT);
            if (inst == null) return;

            int w = mc.getWindow().getGuiScaledWidth();
            int h = mc.getWindow().getGuiScaledHeight();


            // שליטת שקיפות (דוגמה: לפי amplifier)
            float alpha = Mth.clamp(0.35f + 0.15f * inst.getAmplifier(), 0f, 1f);
            int a = Mth.clamp((int)(alpha * 255f), 0, 255);
            int tint = (a << 24) | 0xFFFFFF;
            gui.blit(RenderPipelines.GUI_TEXTURED, HIGH_OVERLAY, 0, 0, 0, 0, w, h, 1920, 1080, 1920, 1080,tint);


        });
    }

}
