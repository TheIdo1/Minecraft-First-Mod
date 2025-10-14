package net.TheIdo1.idos_first_mod.item;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.block.BongBlock;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.entity.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.checkerframework.checker.index.qual.PolyUpperBound;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IdosFirstMod.MOD_ID);

    public static final DeferredItem<Item> FIRST = ITEMS.registerItem("first",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> SECOND = ITEMS.registerItem("second",
            Item::new, new Item.Properties());


    public static final DeferredItem<BlockItem> BONG_ITEM = ITEMS.register("bong", ()-> new BongItem(
            ModBlocks.BONG_BLOCK.get(), new Item.Properties().stacksTo(1)
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse("idos_first_mod:bong")))));

    public static final DeferredItem<Item> SKIB_SPAWN_EGG = ITEMS.register("skib_spawn_egg", ()-> new SpawnEggItem(ModEntities.SKIB.get(), new Item.Properties()
            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse("idos_first_mod:skib_spawn_egg")))));


    public static final DeferredItem<Item> WEED_NUG = ITEMS.register("weed_nug", () -> new BlockItem(ModBlocks.WEED_BUSH.get(),
            new Item.Properties().useItemDescriptionPrefix().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse("idos_first_mod:weed_nug")))));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
