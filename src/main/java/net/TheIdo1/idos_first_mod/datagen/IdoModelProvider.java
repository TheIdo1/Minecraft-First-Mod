package net.TheIdo1.idos_first_mod.datagen;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.block.WowBlock;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;



@EventBusSubscriber(modid = IdosFirstMod.MOD_ID)
public class IdoModelProvider extends net.minecraft.client.data.models.ModelProvider {
    public IdoModelProvider(PackOutput output) {
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




        //Wow Block
        TextureMapping textureMappingBlue = TextureMapping.cube(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/wow_block_blue"));
        ResourceLocation wowBlockBlue = ModelTemplates.CUBE_ALL.create(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/wow_block_blue"), textureMappingBlue, blockModelGenerators.modelOutput);
        TextureMapping textureMappingRed = TextureMapping.cube(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/wow_block_red"));
        ResourceLocation wowBlockRed = ModelTemplates.CUBE_ALL.create(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/wow_block_red"), textureMappingRed, blockModelGenerators.modelOutput);
        TextureMapping textureMappingGreen = TextureMapping.cube(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/wow_block_green"));
        ResourceLocation wowBlockGreen = ModelTemplates.CUBE_ALL.create(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/wow_block_green"), textureMappingGreen, blockModelGenerators.modelOutput);

        blockModelGenerators.blockStateOutput.accept(
                MultiPartGenerator.multiPart(ModBlocks.WOW_BLOCK.get()).with(
                                BlockModelGenerators.condition().term(WowBlock.COLOR, WowBlock.WowColor.RED),
                                BlockModelGenerators.variant(new Variant(wowBlockRed))
                        )
                        .with(
                                BlockModelGenerators.condition().term(WowBlock.COLOR, WowBlock.WowColor.GREEN),
                                BlockModelGenerators.variant(new Variant(wowBlockGreen))
                        )
                        .with(
                                BlockModelGenerators.condition().term(WowBlock.COLOR, WowBlock.WowColor.BLUE),
                                BlockModelGenerators.variant(new Variant(wowBlockBlue))

                )
        );
            // Wow block item
        ItemModel.Unbaked wowBlockItemModel = ItemModelUtils.plainModel(wowBlockBlue);
        itemModelGenerators.itemModelOutput.accept(ModBlocks.WOW_BLOCK.asItem(), wowBlockItemModel);

    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client event){
        event.createProvider(IdoModelProvider::new);
    }
}
