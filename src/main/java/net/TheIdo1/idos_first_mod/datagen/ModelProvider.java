package net.TheIdo1.idos_first_mod.datagen;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;



@EventBusSubscriber(modid = IdosFirstMod.MOD_ID)
public class ModelProvider extends net.minecraft.client.data.models.ModelProvider {
    public ModelProvider(PackOutput output) {
        super(output, IdosFirstMod.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators){
        blockModelGenerators.createTrivialCube(ModBlocks.FIRST_BLOCK.get());
        blockModelGenerators.createTrivialCube(ModBlocks.SECOND_BLOCK.get());
        blockModelGenerators.createTrivialCube(ModBlocks.FIRST_ORE.get());
        blockModelGenerators.createTrivialCube(ModBlocks.FIRST_DEEPSLATE_ORE.get());
        itemModelGenerators.generateFlatItem(ModItems.FIRST.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.SECOND.get(), ModelTemplates.FLAT_ITEM);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client event){
        event.createProvider(ModelProvider::new);
    }
}
