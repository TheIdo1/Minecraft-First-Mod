package net.TheIdo1.idos_first_mod.event;


import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.effect.ModEffects;
import net.TheIdo1.idos_first_mod.entity.ModEntities;
import net.TheIdo1.idos_first_mod.entity.custom.SkibEntity;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.entity.animation.AnimationKeyframeTarget;
import net.neoforged.neoforge.client.entity.animation.AnimationTarget;
import net.neoforged.neoforge.client.event.RegisterJsonAnimationTypesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.joml.Vector3f;

@EventBusSubscriber(modid = IdosFirstMod.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event){
        Level level = event.getLevel();
        if (level.isClientSide()) return;

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (!(stack.getItem() instanceof BlockItem blockItem)) return;
        if (blockItem.getBlock() != Blocks.STONE) return;


        if (!player.hasEffect(ModEffects.HIGH_EFFECT)) return;

        // check if can interact with block
        BlockPos clickedPos = event.getPos();
        BlockState clickedState = level.getBlockState(clickedPos);

        assert event.getFace() != null;

        InteractionResult result = clickedState.useItemOn(stack, level, player, event.getHand(),
                new BlockHitResult(
                        Vec3.atCenterOf(clickedPos),
                        event.getFace(),
                        clickedPos,
                        false
                ));
        InteractionResult result2 = clickedState.useWithoutItem(level, player, new BlockHitResult(
                Vec3.atCenterOf(clickedPos),
                event.getFace(),
                clickedPos,
                false
        ));

        if ((result.consumesAction() || result2.consumesAction()) && !player.isShiftKeyDown()) {
            return;
        }

        // place correctly
        assert event.getFace() != null;
        BlockPos placePos = event.getPos().relative(event.getFace());
        if (!level.getBlockState(placePos).canBeReplaced()) return;

        // cancel original action
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        level.setBlock(placePos, ModBlocks.FIRST_BLOCK.get().defaultBlockState(),
                Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS); // 3 גם מספיק

        if (!player.getAbilities().instabuild) stack.shrink(1);

        // play sound
        level.gameEvent(player, GameEvent.BLOCK_PLACE, placePos);
        level.playSound(null, placePos,
                ModBlocks.FIRST_BLOCK.get().defaultBlockState().getSoundType().getPlaceSound(),
                SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.SKIB.get(), SkibEntity.createAttributes().build());
    }





}
