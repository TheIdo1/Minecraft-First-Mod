package net.TheIdo1.idos_first_mod.entity.goals;

import net.TheIdo1.idos_first_mod.block.BongBlock;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.entity.custom.SkibEntity;
import net.TheIdo1.idos_first_mod.item.BongItem;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class GetWeedBongGoal extends MoveToBlockGoal {

    private final SkibEntity skib;
    private int getCooldown;


    public GetWeedBongGoal(SkibEntity mob, double speedModifier, int searchRange, int verticalSearchRange) {
        super(mob, speedModifier, searchRange, verticalSearchRange);
        this.skib = mob;
    }


    public boolean canUse() {
        var held = skib.getMainHandItem();
        if (!held.is(ModItems.BONG_ITEM.get())) return false;
        var state = BongItem.getStinkyState(held);
        if (state) return false;
        if (getCooldown > 0) {
            getCooldown--;
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        // ממשיכים כל עוד לא הגענו ליעד ולא מילאנו מים
        var state = BongItem.getStinkyState(skib.getMainHandItem());
        if (state) return false;
        return super.canContinueToUse();
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {

        var ground = level.getBlockState(pos);

        if (!ground.isFaceSturdy(level, pos, net.minecraft.core.Direction.UP)) return false;
        if (!level.getBlockState(pos.above()).isAir()) return false;

        for (var dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
            BlockPos bushPos = pos.relative(dir);
            var bushState = level.getBlockState(bushPos);
            if (bushState.is(ModBlocks.WEED_BUSH.get())) {
                 if (bushState.hasProperty(BlockStateProperties.AGE_3)) { return true; }

            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isReachedTarget()) {
            if (tryFillWeed()) {
                if (!skib.level().isClientSide()) {
                    ItemStack held = skib.getMainHandItem();
                    ItemStack updated = held.copyWithCount(held.getCount());
                    BongItem.setStinkyState(updated, true);


                    skib.setItemSlot(net.minecraft.world.entity.EquipmentSlot.MAINHAND, updated);

                    skib.playSound(SoundEvents.COMPOSTER_FILL_SUCCESS, 1f, 1.2f);
                    skib.swing(InteractionHand.MAIN_HAND);

                    getCooldown = 200;
                }
            }
        }
    }

    private boolean tryFillWeed() {
        BlockPos standPos = this.blockPos;
        LevelReader level = (LevelReader) skib.level();

        for (var dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
            BlockPos bushPos = standPos.relative(dir);
            if (level.getBlockState(bushPos).is(ModBlocks.WEED_BUSH.get())) {
                return true; // כאן אפשר גם לעדכן את ה-Bush (להוריד גיל וכו') אם תרצה
            }
        }
        return false;
    }
}
