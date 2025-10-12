package net.TheIdo1.idos_first_mod.entity.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class SkibRenderState extends LivingEntityRenderState {
    public float ageInTicks;

    public final AnimationState headSpinState = new AnimationState();
    public final AnimationState walkState = new AnimationState();
    public final AnimationState idleState = new AnimationState();
    public final AnimationState bongUseState = new AnimationState();

    public boolean isMoving;
    public boolean isIdle;
    public boolean isHeadSpin;
    public boolean isBongUse;


}
