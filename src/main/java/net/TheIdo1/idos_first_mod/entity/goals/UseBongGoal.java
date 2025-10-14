package net.TheIdo1.idos_first_mod.entity.goals;

import net.TheIdo1.idos_first_mod.entity.custom.SkibEntity;
import net.TheIdo1.idos_first_mod.item.BongItem;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.TheIdo1.idos_first_mod.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class UseBongGoal extends Goal {
    private final SkibEntity mob;

    private int useBongAnim;

    private final SoundEvent bongLightSound = ModSounds.BONG_LIGHT.get();
    private final SoundEvent bongRipSound = ModSounds.BONG_RIP.get();
    private final SoundEvent bongFinishSound = ModSounds.BONG_FINISH.get();
    private final SoundEvent coughSound = ModSounds.BONG_COUGH.get();

    public UseBongGoal(SkibEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return BongItem.canUseBong(this.mob.getMainHandItem());
    }

    @Override
    public boolean canContinueToUse() {
        return useBongAnim >= 0;
    }


    @Override
    public void start() {
        this.useBongAnim = 20;
        this.mob.startUsingItem(InteractionHand.MAIN_HAND);
        mob.setUsingBong(true);
        this.mob.playSound(this.bongLightSound, 0.7F, 1.0F);
    }

    @Override
    public void stop() {
        this.mob.stopUsingItem();

        ItemStack item = this.mob.getMainHandItem();
        BlockItemStateProperties props = item.getOrDefault(
                net.minecraft.core.component.DataComponents.BLOCK_STATE,
                BlockItemStateProperties.EMPTY
        );

        Map<String, String> newMap = new HashMap<>(props.properties());


        ItemStack bongWaterNoStinky = new ItemStack(ModItems.BONG_ITEM.asItem());
        bongWaterNoStinky.set(net.minecraft.core.component.DataComponents.BLOCK_STATE, new BlockItemStateProperties(newMap));
        newMap.put("water", "clean");
        newMap.put("has_stinky", "false");

        this.mob.setItemSlot(EquipmentSlot.MAINHAND, bongWaterNoStinky);
        this.mob.playSound(this.coughSound, 0.55F, this.mob.getRandom().nextFloat() * 0.2F + 0.9F);

        BongItem.exhaleParicles(mob.level(), mob, 50);
        mob.setUsingBong(false);

    }


    @Override
    public void tick() {
        if (--useBongAnim == 3) {
            this.mob.playSound(ModSounds.BONG_FINISH.get(), 0.7F, 1);
        }
    }
}
