package net.TheIdo1.idos_first_mod.item;

import net.TheIdo1.idos_first_mod.effect.ModEffects;
import net.TheIdo1.idos_first_mod.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class BongItem extends BlockItem {

    public BongItem(Block block, Properties properties) {
        super(block, properties.stacksTo(1));
    }


    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 50;
    }

    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {

        if(!level.isClientSide){
            if(remainingUseTicks==99){
                level.playSound(null, user.getX(), user.getY(), user.getZ(),
                        ModSounds.BONG_LIGHT.get(), SoundSource.PLAYERS, 1.0F, 0.9F);
            }
            if(remainingUseTicks%5==0 && remainingUseTicks>=5){
                float pitch = (float) -(remainingUseTicks * 0.2)+1;
                level.playSound(null, user.getX(), user.getY(), user.getZ(),
                        ModSounds.BONG_RIP.get(), SoundSource.PLAYERS, 1.0F, 0.9F);
            }


            if(remainingUseTicks==5){
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    ModSounds.BONG_RIP.get(), SoundSource.PLAYERS, 1.2F, 1.4F);
            }

            if(remainingUseTicks==3){
                level.playSound(null, user.getX(), user.getY(), user.getZ(),
                        ModSounds.BONG_FINISH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (canUseBong(stack)) {
                player.startUsingItem(hand);
                return InteractionResult.CONSUME;
            }
        }
        return super.use(level, player, hand);
    }

    public static boolean canUseBong(ItemStack stack){
        if (!stack.is(ModItems.BONG_ITEM.get())){
            return false;
        }
        BlockItemStateProperties props = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        String water = props.properties().get("water");
        boolean hasStinky = "true".equals(props.properties().get("has_stinky"));
        return ("clean".equals(water) && hasStinky);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && livingEntity instanceof Player player) {
            BlockItemStateProperties props = stack.getOrDefault(
                    net.minecraft.core.component.DataComponents.BLOCK_STATE,
                    BlockItemStateProperties.EMPTY
            );
            String water = props.properties().get("water");
            boolean hasStinky = "true".equals(props.properties().get("has_stinky"));

            if ("clean".equals(water) && hasStinky) {

                //effect
                player.addEffect(new MobEffectInstance(ModEffects.HIGH_EFFECT, 20 * 90, 0, false, true, true));



                //update block
                Map<String, String> newMap = new HashMap<>(props.properties());
                if (Math.random() < 0.25) {
                    newMap.put("water", "stinky");
                    // cough sound on dirt water
                    level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                            ModSounds.BONG_COUGH.get(), SoundSource.PLAYERS, 0.8F, 1.0F);
                }
                newMap.put("has_stinky", "false");

                if (!player.isCreative()){
                stack.set(net.minecraft.core.component.DataComponents.BLOCK_STATE,
                        new BlockItemStateProperties(newMap));
                }

                //end particles
                exhaleParicles(level, livingEntity, 50);
            }
        }
        return stack;
    }



    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer() == null){
            return super.useOn(context);
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState target = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        BlockPlaceContext placeCtx = new BlockPlaceContext(context);
        BlockPos placePos = target.canBeReplaced(placeCtx)
                ? pos
                : pos.relative(context.getClickedFace());

        boolean waterAtPlace = level.getFluidState(placePos).is(FluidTags.WATER);
        boolean waterAtHit   = level.getFluidState(pos).is(FluidTags.WATER);

        BlockItemStateProperties props = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        Map<String, String> map = new HashMap<>(props.properties());

        String water = map.getOrDefault("water", "empty");   // "empty" | "clean" | "stinky"
        String hasStinky = map.getOrDefault("has_stinky", "false"); // "true" | "false"


        if ("stinky".equals(water) && context.getPlayer().isShiftKeyDown()) {
            if (!level.isClientSide) {
                map.put("water", "empty");
                stack.set(DataComponents.BLOCK_STATE, new BlockItemStateProperties(map));
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BOTTLE_EMPTY, SoundSource.PLAYERS, 0.8F, 1.0F);
            return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        // 2) אם לוחצים על מים והאייטם ריק ממים: למלא מים נקיים
        if ("empty".equals(water) && (waterAtPlace || waterAtHit)) {
            if (!level.isClientSide) {
                map.put("water", "clean");
                stack.set(DataComponents.BLOCK_STATE, new BlockItemStateProperties(map));
            }
            return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        // 3) אם לוחצים על LEAVES: has_stinky = true
        if (target.is(BlockTags.LEAVES)) {
            if (!"true".equals(hasStinky)) {
                if (!level.isClientSide) {
                    map.put("has_stinky", "true");
                    stack.set(DataComponents.BLOCK_STATE, new BlockItemStateProperties(map));
                }
                return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
            }
            // אם כבר true — לא עשינו שינוי, נאפשר התנהגות ברירת מחדל (למשל ניסיון להצבה)
        }

        // אחרת: התנהגות רגילה של BlockItem (ניסיון להציב את הבלוק)
        return super.useOn(context);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        // תמיד מציגים את הפס (גם כשהוא "ריק")
        return true;
    }


    @Override
    public int getBarWidth(ItemStack stack) {
        // רוחב הפס: 0 (ריק) או מלא (13) בהתאם ל-has_stinky
        return hasStinky(stack) ? 13 : 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        // צבע הפס: ירוק כשיש stinky, אפור כשאין (שנה לפי הטעם)
        return hasStinky(stack) ? 0x55FF55 : 0x555555;
    }





    //helpers

    public static void exhaleParicles(Level level, LivingEntity entity, int durationTicks) {
        if (!(level instanceof ServerLevel s)) return; // שרת בלבד

        float pt = 1.0f;
        Vec3 eye  = entity.getEyePosition(pt);
        Vec3 look = entity.getViewVector(pt).normalize();
        // מיקום "פה" – קצת קדימה ומעט מתחת לעיניים
        Vec3 mouth = eye.add(look.scale(1.20)).add(0.0, -0.15, 0.0);

        AreaEffectCloud cloud = new AreaEffectCloud(level, mouth.x, mouth.y, mouth.z);
        cloud.setWaitTime(0);
        cloud.setDuration(durationTicks);          // משך החיים בטיקים (למשל 40–80)
        cloud.setRadius(0.6f);                    // גודל הענן
        cloud.setRadiusPerTick(-0.45f / durationTicks); // דהייה הדרגתית עד 0
        cloud.setCustomParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE);
        cloud.setNoGravity(true);

        // דחיפה עדינה קדימה ולמעלה – כמו נשיפה
        Vec3 push = look.scale(0.03).add(0, 0.01, 0);
        cloud.setDeltaMovement(push);

        s.addFreshEntity(cloud);
    }


    private static boolean hasStinky(ItemStack stack) {
        var props = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        return "true".equalsIgnoreCase(props.properties().getOrDefault("has_stinky", "false"));
    }
}
