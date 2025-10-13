package net.TheIdo1.idos_first_mod.entity.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

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

    public HumanoidArm mainHand;
    public ItemStack itemInMainHand;
    public boolean isItemInMainHand;

    public HumanoidArm offHand;
    public ItemStack itemInOffHand;
    public boolean isItemInOffHand;

    public int entityId;

    public Level level;
}
