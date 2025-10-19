package net.TheIdo1.idos_first_mod.block;

import com.google.gson.Gson;
import com.mojang.serialization.MapCodec;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;

public class BongBlock extends HorizontalDirectionalBlock implements LiquidBlockContainer {

    public static final MapCodec<BongBlock> CODEC = simpleCodec(BongBlock::new);

    // --- Hitbox from JSON (assets/idos_first_mod/models/block/bong_hitbox.json) ---
    private static final VoxelShape SHAPE_NORTH = loadShapeFromJson(
            "/assets/idos_first_mod/hitboxes/bong_hitbox.json"
    );
    private static final Map<Direction, VoxelShape> SHAPES_BY_FACING = makeRotations(SHAPE_NORTH);

    public static final EnumProperty<BongBlock.WaterState> WATER_STATE = EnumProperty.create("water", BongBlock.WaterState.class);

    public static final BooleanProperty HAS_STINKY = BooleanProperty.create("has_stinky");
    private static final Logger log = LoggerFactory.getLogger(BongBlock.class);

    public BongBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATER_STATE, WaterState.EMPTY)
                .setValue(HAS_STINKY, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATER_STATE, HAS_STINKY);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if(stack.getItem() == Items.WATER_BUCKET && state.getValue(WATER_STATE) == WaterState.EMPTY){
            if(!level.isClientSide()){
                BlockState newBlockstate = state.setValue(WATER_STATE, WaterState.WATER);
                level.setBlock(pos, newBlockstate, UPDATE_ALL);
                if(!player.isCreative()){
                    if(stack.getCount()==1){
                        player.setItemInHand(hand, new ItemStack(Items.BUCKET,1));
                    } else {
                        stack.shrink(1);
                        ItemStack empty = new ItemStack(Items.BUCKET);
                        if (!player.addItem(empty)) {
                            player.drop(empty, false);
                        }
                    }
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        if(stack.getItem() == Items.BUCKET && state.getValue(WATER_STATE) != WaterState.EMPTY){
            if(!level.isClientSide()){
                BlockState newBlockstate = state.setValue(WATER_STATE, WaterState.EMPTY);
                level.setBlock(pos, newBlockstate, UPDATE_ALL);
                if(!player.isCreative()){
                    boolean clean = (state.getValue(WATER_STATE) == WaterState.WATER);
                    ItemStack newStack;
                    if (clean){
                        newStack = new ItemStack(Items.WATER_BUCKET,1);
                    } else {
                        newStack = new ItemStack(Items.BUCKET,1);
                    }
                    player.setItemInHand(hand, newStack);
                }
                level.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        if(stack.getItem() == ModItems.WEED_NUG.get() && !state.getValue(HAS_STINKY)){
            if(!level.isClientSide()){
                BlockState newBlockstate = state.setValue(HAS_STINKY, true);
                level.setBlock(pos, newBlockstate, UPDATE_ALL);
                if(!player.isCreative()){
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.COMPOSTER_FILL_SUCCESS, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        if (player.getMainHandItem().is(Items.AIR)) {

            ItemStack stack1 = new ItemStack(ModBlocks.BONG_BLOCK.get().asItem());

            stack1.set(
                    DataComponents.BLOCK_STATE,
                    new net.minecraft.world.item.component.BlockItemStateProperties(
                            java.util.Map.of(
                                    "water", state.getValue(BongBlock.WATER_STATE).getSerializedName(),
                                    "has_stinky", String.valueOf(state.getValue(BongBlock.HAS_STINKY))
                            )
                    )
            );

            if (player.getMainHandItem().isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, stack1);
            } else if (!player.addItem(stack1)) {
                player.drop(stack1, false);
            }

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_ALL);

            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }


    //Cant be destroyed with a bucket
    @Override
    protected boolean canBeReplaced(BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity owner, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }

    // ----------------- shapes -----------------

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPES_BY_FACING.getOrDefault(state.getValue(FACING), SHAPE_NORTH);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPES_BY_FACING.getOrDefault(state.getValue(FACING), SHAPE_NORTH);
    }

    // ----------------- helpers -----------------

    private static VoxelShape loadShapeFromJson(String resourcePath) {
        try (var in = BongBlock.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalStateException("Hitbox JSON not found at " + resourcePath);
            }
            try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                // הפורמט הוא מערך של קופסאות: [ [minX,minY,minZ,maxX,maxY,maxZ], ... ] כשהכול בטווח 0..1
                double[][] boxes01 = new Gson().fromJson(reader, double[][].class);
                VoxelShape shape = Shapes.empty();
                if (boxes01 != null) {
                    for (double[] b : boxes01) {
                        if (b != null && b.length == 6) {
                            shape = Shapes.or(shape, Shapes.box(b[0], b[1], b[2], b[3], b[4], b[5]));
                        }
                    }
                }
                return shape.optimize();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load/parse hitbox JSON: " + resourcePath, e);
        }
    }

    private static Map<Direction, VoxelShape> makeRotations(VoxelShape north) {
        Map<Direction, VoxelShape> m = new EnumMap<>(Direction.class);
        m.put(Direction.NORTH, north);
        m.put(Direction.EAST,  rotateY(north, 1));
        m.put(Direction.SOUTH, rotateY(north, 2));
        m.put(Direction.WEST,  rotateY(north, 3));
        return m;
    }

    // סיבוב סביב ציר Y בקפיצות של 90°; הקואורדינטות כאן בנורמליזציה 0..1 (כי השתמשנו ב-Shapes.box)
    private static VoxelShape rotateY(VoxelShape shape, int quartersCW) {
        quartersCW = (quartersCW % 4 + 4) % 4;
        if (quartersCW == 0) return shape;

        VoxelShape[] buf = new VoxelShape[]{shape, Shapes.empty()};
        for (int r = 0; r < quartersCW; r++) {
            final VoxelShape src = buf[0];
            buf[1] = Shapes.empty();
            src.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                // סיבוב עם (x,z) -> (1 - z, x)
                double nMinX = 1.0 - maxZ;
                double nMinZ = minX;
                double nMaxX = 1.0 - minZ;
                double nMaxZ = maxX;
                buf[1] = Shapes.or(buf[1], Shapes.box(nMinX, minY, nMinZ, nMaxX, maxY, nMaxZ));
            });
            buf[0] = buf[1].optimize();
            buf[1] = Shapes.empty();
        }
        return buf[0];
    }

    //Make it light transparent
    @Override
    protected boolean propagatesSkylightDown(BlockState state) {
        return true;
    }

    @Override
    protected int getLightBlock(BlockState state) {
        return 0;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    public enum WaterState implements StringRepresentable {
        WATER("clean"), BAD_WATER("stinky"), EMPTY("empty");
        private final String name;
        WaterState(String name) { this.name = name; }
        @Override public String getSerializedName() { return name; }
        @Override public String toString() { return name; }

        public static @org.jetbrains.annotations.Nullable WaterState fromSerialized(String s) {
            if (s == null) return null;
            for (WaterState ws : values()) {
                if (ws.getSerializedName().equalsIgnoreCase(s) || ws.name().equalsIgnoreCase(s)) {
                    return ws;
                }
            }
            return null;
        }
    }
}
