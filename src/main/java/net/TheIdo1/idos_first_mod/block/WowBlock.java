package net.TheIdo1.idos_first_mod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

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
            WowColor next = switch (blockState.getValue(COLOR)){
                case RED -> WowColor.GREEN;
                case GREEN -> WowColor.BLUE;
                case BLUE -> WowColor.RED;
            };
            level.setBlock(blockPos, blockState.setValue(COLOR, next), Block.UPDATE_ALL);
        }
        level.playSound(null , blockPos, SoundEvents.AMETHYST_CLUSTER_PLACE,SoundSource.BLOCKS);
        return InteractionResult.SUCCESS;
    }

}
