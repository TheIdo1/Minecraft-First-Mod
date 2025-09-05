package net.TheIdo1.idos_first_mod.item;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IdosFirstMod.MOD_ID);

    //tabs
    public static final Supplier<CreativeModeTab> FIRST_ITEMS_TAB = CREATIVE_MODE_TAB.register("first_items_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.FIRST.get())).title(Component.translatable("creativetab.idos_first_mod.first_items"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.FIRST);
                        output.accept(ModItems.SECOND);
                    })
                    .build());

    public static final Supplier<CreativeModeTab> FIRST_BLOCKS_TAB = CREATIVE_MODE_TAB.register("first_blocks_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModBlocks.FIRST_BLOCK.get())).title(Component.translatable("creativetab.idos_first_mod.first_blocks"))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(IdosFirstMod.MOD_ID, "first_items_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.FIRST_BLOCK);
                        output.accept(ModBlocks.SECOND_BLOCK);
                        output.accept(ModBlocks.FIRST_ORE);
                        output.accept(ModBlocks.FIRST_DEEPSLATE_ORE);
                    })
                    .build());


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
