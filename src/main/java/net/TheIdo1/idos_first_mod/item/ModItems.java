package net.TheIdo1.idos_first_mod.item;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IdosFirstMod.MOD_ID);

    public static final DeferredItem<Item> FIRST = ITEMS.registerItem("first",
            Item::new, new Item.Properties());

    public static final DeferredItem<Item> SECOND = ITEMS.registerItem("second",
            Item::new, new Item.Properties());

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
