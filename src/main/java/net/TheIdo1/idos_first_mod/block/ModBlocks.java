package net.TheIdo1.idos_first_mod.block;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IdosFirstMod.MOD_ID);

    public static final DeferredBlock<Block> FIRST_BLOCK = registerBlock("first_block",
            BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.ANVIL));
    public static final DeferredBlock<Block> SECOND_BLOCK = registerBlock("second_block",
            BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.ANCIENT_DEBRIS));




    private static <T extends Block> DeferredBlock<T> registerBlock(String name, BlockBehaviour.Properties props){
        DeferredBlock<T> toReturn = (DeferredBlock<T>) BLOCKS.registerSimpleBlock(name, props);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
//        ModItems.ITEMS.register(name, ()-> new BlockItem(block.get(),
//                new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse("idos_first_mod:first_block")))));
        ModItems.ITEMS.registerSimpleBlockItem(block, new Item.Properties());
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
