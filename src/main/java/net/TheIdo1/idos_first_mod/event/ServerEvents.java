package net.TheIdo1.idos_first_mod.event;


import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

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

}
