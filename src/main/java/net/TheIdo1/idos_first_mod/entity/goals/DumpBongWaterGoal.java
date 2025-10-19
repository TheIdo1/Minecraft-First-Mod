package net.TheIdo1.idos_first_mod.entity.goals;

import net.TheIdo1.idos_first_mod.block.BongBlock;
import net.TheIdo1.idos_first_mod.entity.custom.SkibEntity;
import net.TheIdo1.idos_first_mod.item.BongItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;

public class DumpBongWaterGoal extends Goal {

    private final SkibEntity entity;


    public DumpBongWaterGoal(SkibEntity entity){
        this.entity = entity;
    }


    @Override
    public boolean canUse() {
        return BongItem.getWaterState(entity.getMainHandItem()) == BongBlock.WaterState.BAD_WATER;
    }

    @Override
    public void start() {
        BongItem.setWaterState(entity.getMainHandItem(), BongBlock.WaterState.EMPTY);
        entity.playSound(SoundEvents.BOTTLE_EMPTY, 0.8f, 1.2f);
        entity.swing(InteractionHand.MAIN_HAND);
    }


}
