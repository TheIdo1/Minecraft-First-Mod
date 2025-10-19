package net.TheIdo1.idos_first_mod.entity.goals;

import net.TheIdo1.idos_first_mod.block.BongBlock;
import net.TheIdo1.idos_first_mod.entity.custom.SkibEntity;
import net.TheIdo1.idos_first_mod.item.BongItem;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class FindAndFillWaterGoal extends MoveToBlockGoal {

    private final SkibEntity skib;
    private int fillCooldown;

    public FindAndFillWaterGoal(SkibEntity mob, double speed, int searchRange, int verticalRange) {
        super(mob, speed, searchRange, verticalRange);
        this.skib = mob;
        this.setFlags(java.util.EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // חייבים להחזיק בונג, והמים בבונג צריכים להיות EMPTY (אין מים)
        var held = skib.getMainHandItem();
        if (!held.is(ModItems.BONG_ITEM.get())) return false;
        var state = BongItem.getWaterState(held);
        if (state != BongBlock.WaterState.EMPTY) return false;
        if (fillCooldown > 0) {
            fillCooldown--;
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        // ממשיכים כל עוד לא הגענו ליעד ולא מילאנו מים
        var state = BongItem.getWaterState(skib.getMainHandItem());
        if (state != BongBlock.WaterState.EMPTY) return false;
        return super.canContinueToUse();
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
        // אנחנו מחפשים "מקום לעמוד עליו" שלידו יש מים מקוריים או מים בכלל.
        // pos הוא הבלוק שעליו נעמוד (לכן הוא צריך להיות מוצק בחלק העליון)
        BlockState ground = level.getBlockState(pos);
        if (!ground.isFaceSturdy(level, pos, Direction.UP)) return false;

        // הבלוק מעל צריך להיות פנוי (שנוכל לעמוד/להיכנס)
        if (!level.getBlockState(pos.above()).isAir()) return false;

        // האם יש מים צמודים (אחד מהכיוונים) או ממש מעל משטח העמידה (אם נרצה שהמוב יעמוד במים רדודים)?
        for (BlockPos n : new BlockPos[]{
                pos.north(), pos.south(), pos.east(), pos.west(), pos.above(), pos.above().north(),
                pos.above().south(), pos.above().east(), pos.above().west()
        }) {
            FluidState fs = level.getFluidState(n);
            if (fs.is(FluidTags.WATER)) {
                // אופציונלי: לדרוש water source (נהר/אגם)
                // if (!fs.isSource()) continue;
                return true;
            }
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isReachedTarget()) {
            if (tryFillFromNearbyWater()) {

                if (!skib.level().isClientSide()) {
                    ItemStack held = skib.getMainHandItem();
                    ItemStack updated = held.copyWithCount(held.getCount());     // עותק חדש
                    BongItem.setWaterState(updated, BongBlock.WaterState.WATER); // עדכון על העותק

                    // חשוב: להחליף את הסלוט כדי לאלץ סנכרון ללקוח
                    skib.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, updated);

                    skib.playSound(SoundEvents.BOTTLE_FILL, 1f, 1.2f);
                    skib.swing(InteractionHand.MAIN_HAND);

                    fillCooldown = 200;
                }
            }
        }
    }

    private boolean tryFillFromNearbyWater() {

        BlockPos base = skib.blockPosition();
        LevelReader level = (LevelReader) skib.level();

        for (BlockPos n : BlockPos.betweenClosed(base.offset(-1, -1, -1), base.offset(1, 1, 1))) {
            if (level.getFluidState(n).is(FluidTags.WATER)) {

                return true;
            }
        }
        return false;
    }
}
