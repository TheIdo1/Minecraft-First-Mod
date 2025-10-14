package net.TheIdo1.idos_first_mod.datagen;

import com.google.errorprone.annotations.Var;
import com.mojang.math.Quadrant;
import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.block.BongBlock;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.block.WowBlock;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.multipart.Condition;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.item.properties.select.ItemBlockState;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;


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
        itemModelGenerators.generateFlatItem(ModItems.SKIB_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ModItems.WEED_NUG.get(), ModelTemplates.FLAT_ITEM);




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


        // Bong Block

        ResourceLocation base   = ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/bong");
        ResourceLocation clean  = ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/bong_water_clean");
        ResourceLocation stinky = ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/bong_water_stinky");
        ResourceLocation overlay = ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/bong_stinky_overlay");
        ResourceLocation empty = ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "block/bong_empty");

        MultiPartGenerator mp = MultiPartGenerator.multiPart(ModBlocks.BONG_BLOCK.get())
                // בסיס לפי כיוון
                .with(BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH),
                        BlockModelGenerators.variant(new Variant(base)))
                .with(BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST),
                        BlockModelGenerators.variant(new Variant(base).withYRot(Quadrant.R90)))
                .with(BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH),
                        BlockModelGenerators.variant(new Variant(base).withYRot(Quadrant.R180)))
                .with(BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST),
                        BlockModelGenerators.variant(new Variant(base).withYRot(Quadrant.R270)))
                // מים clean
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                                .term(BongBlock.WATER_STATE, BongBlock.WaterState.WATER),
                        BlockModelGenerators.variant(new Variant(clean)))
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                                .term(BongBlock.WATER_STATE, BongBlock.WaterState.WATER),
                        BlockModelGenerators.variant(new Variant(clean).withYRot(Quadrant.R90)))
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                                .term(BongBlock.WATER_STATE, BongBlock.WaterState.WATER),
                        BlockModelGenerators.variant(new Variant(clean).withYRot(Quadrant.R180)))
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                                .term(BongBlock.WATER_STATE, BongBlock.WaterState.WATER),
                        BlockModelGenerators.variant(new Variant(clean).withYRot(Quadrant.R270)))
                // מים stinky
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                                .term(BongBlock.WATER_STATE, BongBlock.WaterState.BAD_WATER),
                        BlockModelGenerators.variant(new Variant(stinky)))
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                                .term(BongBlock.WATER_STATE, BongBlock.WaterState.BAD_WATER),
                        BlockModelGenerators.variant(new Variant(stinky).withYRot(Quadrant.R90)))
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                                .term(BongBlock.WATER_STATE, BongBlock.WaterState.BAD_WATER),
                        BlockModelGenerators.variant(new Variant(stinky).withYRot(Quadrant.R180)))
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                                .term(BongBlock.WATER_STATE, BongBlock.WaterState.BAD_WATER),
                        BlockModelGenerators.variant(new Variant(stinky).withYRot(Quadrant.R270)))
                // שכבת overlay כש-has_stinky=true
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                                .term(BongBlock.HAS_STINKY, true),
                        BlockModelGenerators.variant(new Variant(overlay)))
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                                .term(BongBlock.HAS_STINKY, true),
                        BlockModelGenerators.variant(new Variant(overlay).withYRot(Quadrant.R90)))
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                                .term(BongBlock.HAS_STINKY, true),
                        BlockModelGenerators.variant(new Variant(overlay).withYRot(Quadrant.R180)))
                .with(BlockModelGenerators.condition()
                                .term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                                .term(BongBlock.HAS_STINKY, true),
                        BlockModelGenerators.variant(new Variant(overlay).withYRot(Quadrant.R270)));

        blockModelGenerators.blockStateOutput.accept(mp);


        // Bong Item
        var baseModel   = new net.minecraft.client.renderer.item.BlockModelWrapper.Unbaked(base, java.util.List.of());

        var hasWater = ItemModelUtils.select(new ItemBlockState(BongBlock.WATER_STATE.getName()),
                ItemModelUtils.plainModel(empty),
                new SelectItemModel.SwitchCase<>(List.of(BongBlock.WaterState.WATER.getSerializedName()), ItemModelUtils.plainModel(clean)),
                new SelectItemModel.SwitchCase<>(List.of(BongBlock.WaterState.BAD_WATER.getSerializedName()), ItemModelUtils.plainModel(stinky))
                );

        var hasStinky = ItemModelUtils.select(new ItemBlockState(BongBlock.HAS_STINKY.getName()),
                ItemModelUtils.plainModel(empty),
                new SelectItemModel.SwitchCase<>(List.of("true"), ItemModelUtils.plainModel(overlay))
                );

        ItemModel.Unbaked bongItemModel3D =  ItemModelUtils.composite(baseModel, hasWater, hasStinky);


        ItemModel.Unbaked bongItemModel2D = ItemModelUtils.select(new ItemBlockState(BongBlock.WATER_STATE.getName()),
                ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(ModItems.BONG_ITEM.get(), ModelTemplates.FLAT_ITEM)),
                new SelectItemModel.SwitchCase<>(List.of(BongBlock.WaterState.WATER.getSerializedName()), ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(ModItems.BONG_ITEM.get(),"_water" ,ModelTemplates.FLAT_ITEM))),
                new SelectItemModel.SwitchCase<>(List.of(BongBlock.WaterState.BAD_WATER.getSerializedName()), ItemModelUtils.plainModel(itemModelGenerators.createFlatItemModel(ModItems.BONG_ITEM.get(), "_water_bad",ModelTemplates.FLAT_ITEM)))
        );


        ItemModel.Unbaked bongItemComplete = ItemModelUtils.select(new DisplayContext(),
                bongItemModel3D,
                new SelectItemModel.SwitchCase<>(List.of(ItemDisplayContext.GUI), bongItemModel2D));


        itemModelGenerators.itemModelOutput.accept(ModItems.BONG_ITEM.get(), bongItemComplete);



        // Weed Bush



        blockModelGenerators.blockStateOutput
                .accept(
                        MultiVariantGenerator.dispatch(ModBlocks.WEED_BUSH.get())
                                .with(PropertyDispatch.initial(BlockStateProperties.AGE_3).generate(
                                        age -> plainVariant(
                                                blockModelGenerators.createSuffixedVariant(ModBlocks.WEED_BUSH.get(), "_stage" + age, ModelTemplates.CROSS.extend().renderType("minecraft:cutout").build(), TextureMapping::cross)
                                                        )
                                                )
                                )
                );


    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent.Client event){
        event.createProvider(IdoModelProvider::new);
    }
}
