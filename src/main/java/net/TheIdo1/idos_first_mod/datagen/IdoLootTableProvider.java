package net.TheIdo1.idos_first_mod.datagen;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.Set;


@EventBusSubscriber(modid = IdosFirstMod.MOD_ID)
public class IdoLootTableProvider {
    @SubscribeEvent
    public static void onGatherDataEvent(final GatherDataEvent.Client event){
        event.createProvider((packOutput)-> new LootTableProvider(
                packOutput,
                Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(IdoBlockLootTableSubProvider::new, LootContextParamSets.BLOCK)
                ),
                event.getLookupProvider()
        ));
    }
}
