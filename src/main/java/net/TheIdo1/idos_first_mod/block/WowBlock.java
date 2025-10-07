package net.TheIdo1.idos_first_mod.block;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.enums.BubbleColumnDirection;

public class WowBlock extends Block {

    public enum WowColor implements StringRepresentable {
        RED("red"), GREEN("green"), BLUE("blue");
        private final String name;
        WowColor(String name) { this.name = name; }
        @Override public String getSerializedName() { return name; }
        @Override public String toString() { return name; }
    }

    public static final EnumProperty<WowColor> COLOR = EnumProperty.create("color", WowColor.class);

    public WowBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(COLOR, WowColor.BLUE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(COLOR, WowColor.BLUE);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult
    ) {
        if(!level.isClientSide()){
            BlockState newState = blockState.cycle(COLOR);
            level.setBlock(blockPos, newState, Block.UPDATE_ALL);
            level.scheduleTick(blockPos, this, 20);
            level.playSound(null , blockPos, SoundEvents.AMETHYST_CLUSTER_PLACE, SoundSource.BLOCKS);
        }
        return InteractionResult.SUCCESS;
    }


    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof Player player){
            Vec3 pVec = player.getDeltaMovement();
            player.displayClientMessage(Component.literal(pVec.toString()),true);
        }
        if(state.getValue(COLOR) == WowColor.RED){
            if (!entity.isSteppingCarefully() && entity instanceof LivingEntity) {
                entity.hurt(level.damageSources().hotFloor(), 1.0F);
            }
        } else if (state.getValue(COLOR) == WowColor.GREEN) {
            if (!entity.isSteppingCarefully() && entity instanceof LivingEntity && entity.onGround()){
                ((LivingEntity) entity).jumpFromGround();
                entity.resetFallDistance();
            }
        } else if (state.getValue(COLOR) == WowColor.BLUE) {
                entity.resetFallDistance();
        }

        super.stepOn(level, pos, state, entity);
    }


    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, 20);
    }


    @Override
    protected BlockState updateShape(
            BlockState blockState,
            LevelReader levelReader,
            ScheduledTickAccess scheduledTickAccess,
            BlockPos pos,
            Direction direction,
            BlockPos pos1,
            BlockState blockState1,
            RandomSource random
    ) {
        if (direction == Direction.UP && blockState1.is(Blocks.WATER)) {
            scheduledTickAccess.scheduleTick(pos, this, 20);
        }
        return super.updateShape(blockState, levelReader, scheduledTickAccess, pos, direction, pos1, blockState1, random);
    }


    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BubbleColumnBlock.updateColumn(level, pos.above(), state);
    }

    @Override
    public BubbleColumnDirection getBubbleColumnDirection(BlockState state) {
        if (state.getValue(COLOR) == WowColor.GREEN) {
            return BubbleColumnDirection.UPWARD;
        } else if (state.getValue(COLOR) == WowColor.RED) {
            return BubbleColumnDirection.DOWNWARD;
        } else {
            return BubbleColumnDirection.NONE;
        }
    }
}




