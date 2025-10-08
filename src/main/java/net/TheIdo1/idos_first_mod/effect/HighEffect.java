package net.TheIdo1.idos_first_mod.effect;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HighEffect extends MobEffect {

    public HighEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            //hunger
            player.causeFoodExhaustion(0.002F * (amplifier + 1));

            //regen
            int i = 50 >> amplifier;
            int j = player.getEffect(ModEffects.HIGH_EFFECT).getDuration();
            if(i == 0 || j % i == 0){
                if (entity.getHealth() < entity.getMaxHealth()) {
                    entity.heal(1.0F);
                }
            }


        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        super.onEffectStarted(entity, amplifier);
        //absorption
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(4 * (1 + amplifier))));
    }
}
