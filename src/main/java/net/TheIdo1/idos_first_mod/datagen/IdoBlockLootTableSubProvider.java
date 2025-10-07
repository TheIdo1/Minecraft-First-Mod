package net.TheIdo1.idos_first_mod.datagen;


import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;

public class IdoBlockLootTableSubProvider extends BlockLootSubProvider {
    protected IdoBlockLootTableSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(e -> (Block) e.value())
                .toList();
    }



    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.FIRST_BLOCK.get());
        this.dropSelf(ModBlocks.SECOND_BLOCK.get());
        this.dropSelf(ModBlocks.WOW_BLOCK.get());

        this.add(ModBlocks.FIRST_ORE.get(), createCustomOreDrops(ModBlocks.FIRST_ORE.get(), ModItems.FIRST.get(), 2f, 5f));
        this.add(ModBlocks.FIRST_DEEPSLATE_ORE.get(), createCustomOreDrops(ModBlocks.FIRST_DEEPSLATE_ORE.get(), ModItems.FIRST.get(),3f ,6f));

        this.dropWhenSilkTouch(ModBlocks.BONG_BLOCK.get());

    }


    protected LootTable.Builder createCustomOreDrops(Block block,Item item ,float min, float max) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(
                block,
                (LootPoolEntryContainer.Builder<?>)this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(item)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }
}
