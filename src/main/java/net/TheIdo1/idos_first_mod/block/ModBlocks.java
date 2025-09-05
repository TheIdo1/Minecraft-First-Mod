package net.TheIdo1.idos_first_mod.block;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IdosFirstMod.MOD_ID);

    public static final DeferredBlock<Block> FIRST_BLOCK = registerBlock("first_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.BAMBOO)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse("idos_first_mod:first_block")))));
    public static final DeferredBlock<Block> SECOND_BLOCK = registerBlock("second_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.ANCIENT_DEBRIS)
                    .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse("idos_first_mod:second_block")))
                    .requiresCorrectToolForDrops().sound(SoundType.STONE)
                    ));
    public static final DeferredBlock<Block> FIRST_ORE = registerBlock("first_ore", ()-> new DropExperienceBlock(UniformInt.of(3,6),
            BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse("idos_first_mod:first_ore")))
                    .requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)
    ));
    public static final DeferredBlock<Block> FIRST_DEEPSLATE_ORE = registerBlock("first_deepslate_ore", ()-> new DropExperienceBlock(UniformInt.of(4,7),
            BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse("idos_first_mod:first_deepslate_ore")))
    ));



    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block){
//        String ID = block.get().getName();
//        ModItems.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties().setId(ResourceKey.create(
//                Registries.ITEM, ResourceLocation.parse()
//        ))));
        ModItems.ITEMS.registerSimpleBlockItem(block, new Item.Properties());
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
