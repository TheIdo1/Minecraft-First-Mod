package net.TheIdo1.idos_first_mod.effect;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, IdosFirstMod.MOD_ID);

    public static final Holder<MobEffect> HIGH_EFFECT = MOB_EFFECTS.register("high",
            ()-> new HighEffect(MobEffectCategory.NEUTRAL, 0x1AFF0A)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "high"),
                            -0.15F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID,"high"),
                            -0.1F, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));


    public static void  register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }
}
