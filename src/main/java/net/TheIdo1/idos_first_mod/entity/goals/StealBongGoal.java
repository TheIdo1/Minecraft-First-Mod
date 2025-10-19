package net.TheIdo1.idos_first_mod.entity.goals;

import net.TheIdo1.idos_first_mod.block.BongBlock;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.entity.custom.SkibEntity;
import net.TheIdo1.idos_first_mod.item.BongItem;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class StealBongGoal extends MoveToBlockGoal {

    private final SkibEntity skib;
    private int stealCooldown;

    public StealBongGoal(SkibEntity mob, double speedModifier, int searchRange, int verticalSearchRange) {
        super(mob, speedModifier, searchRange, verticalSearchRange);
        this.skib = mob;
    }

    @Override
    public boolean canUse() {
        if (!skib.getMainHandItem().isEmpty()) return false;
        if (stealCooldown > 0) {
            stealCooldown--;
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (!skib.getMainHandItem().isEmpty()) return false;
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
            if (bushState.is(ModBlocks.BONG_BLOCK.get())) {
                return true;

            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isReachedTarget()) return;

        if (!skib.level().isClientSide()) {

            BlockPos standPos = this.blockPos;
            Level level = skib.level();

            BlockPos bongPos = null;
            for (var dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
                BlockPos p = standPos.relative(dir);
                if (level.getBlockState(p).is(ModBlocks.BONG_BLOCK.get())) {
                    bongPos = p;
                    break;
                }
            }

            if (bongPos == null) {
                return;
            }

            var bongState = level.getBlockState(bongPos);

            // בנה ItemStack של הבונג עם ה־block_state מהבלוק בעולם
            ItemStack stack = new ItemStack(
                    ModBlocks.BONG_BLOCK.get().asItem()
            );

            // שלוף תכונות מה־BlockState והעבר ל־DataComponents.BLOCK_STATE של האייטם
            String waterSerialized = bongState.getValue(BongBlock.WATER_STATE).getSerializedName();
            String hasStinkyStr   = String.valueOf(bongState.getValue(BongBlock.HAS_STINKY));

            java.util.Map<String, String> map = java.util.Map.of(
                    "water", waterSerialized,
                    "has_stinky", hasStinkyStr
            );

            stack.set(
                    net.minecraft.core.component.DataComponents.BLOCK_STATE,
                    new net.minecraft.world.item.component.BlockItemStateProperties(map)
            );

            // שים ביד של ה־skib (MAINHAND ריק לפי canUse)
            skib.setItemSlot(EquipmentSlot.MAINHAND, stack);


            level.setBlock(bongPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),
                    3);

            skib.playSound(net.minecraft.sounds.SoundEvents.ITEM_PICKUP, 1.0f, 1.2f);
            skib.swing(InteractionHand.MAIN_HAND);


            skib.getNavigation().stop();
            stealCooldown = 200;
            skib.playStealSound();
        }
    }
}
