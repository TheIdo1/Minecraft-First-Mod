package net.TheIdo1.idos_first_mod.datagen;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = IdosFirstMod.MOD_ID)
public class IdoBlockTagsProvider extends BlockTagsProvider {

    public IdoBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, IdosFirstMod.MOD_ID);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client event){
        event.createProvider(IdoBlockTagsProvider::new);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.WOW_BLOCK.get(),
                ModBlocks.FIRST_BLOCK.get(),
                ModBlocks.SECOND_BLOCK.get(),
                ModBlocks.FIRST_ORE.get(),
                ModBlocks.FIRST_DEEPSLATE_ORE.get(),
                ModBlocks.BONG_BLOCK.get()
                );

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                ModBlocks.FIRST_DEEPSLATE_ORE.get()
        );

        this.tag(BlockTags.NEEDS_IRON_TOOL).add(
                ModBlocks.FIRST_ORE.get()
        );

        this.tag(BlockTags.NEEDS_STONE_TOOL).add(
                ModBlocks.FIRST_BLOCK.get(),
                ModBlocks.SECOND_BLOCK.get(),
                ModBlocks.WOW_BLOCK.get()
        );
    }
}
