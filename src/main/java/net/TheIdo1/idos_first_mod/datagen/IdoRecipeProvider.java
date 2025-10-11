package net.TheIdo1.idos_first_mod.datagen;

import net.TheIdo1.idos_first_mod.IdosFirstMod;
import net.TheIdo1.idos_first_mod.block.ModBlocks;
import net.TheIdo1.idos_first_mod.item.BongItem;
import net.TheIdo1.idos_first_mod.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = IdosFirstMod.MOD_ID)
public class IdoRecipeProvider extends RecipeProvider {

    public IdoRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {
        List<ItemLike> FIRST_ORES = List.of(
                ModBlocks.FIRST_ORE, ModBlocks.FIRST_DEEPSLATE_ORE
        );

        shaped(RecipeCategory.MISC, ModItems.BONG_ITEM.get(), 1)
                .pattern(" B ")
                .pattern("G G")
                .pattern(" GN")
                .define('G', Items.GLASS)
                .define('B', Items.GLASS_BOTTLE)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy("has_glass", has(Items.GLASS)).save(output);


        shapeless(RecipeCategory.MISC, ModItems.FIRST.get(), 9)
                .requires(ModBlocks.FIRST_BLOCK.get()).unlockedBy("has_first_block", has(ModBlocks.FIRST_BLOCK))
                .save(output, "idos_first_mod:first_from_first_block");

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.FIRST_BLOCK.get(), 1)
                .requires(ModItems.FIRST.get(), 9).unlockedBy("has_first", has(ModItems.FIRST))
                .save(output, "idos_first_mod:first_block_from_first");


        shapeless(RecipeCategory.MISC, ModItems.SECOND.get(), 9)
                .requires(ModBlocks.SECOND_BLOCK.get()).unlockedBy("has_second_block", has(ModBlocks.SECOND_BLOCK))
                .save(output, "idos_first_mod:second_from_second_block");

        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SECOND_BLOCK.get(), 1)
                .requires(ModItems.SECOND.get(), 9).unlockedBy("has_second", has(ModItems.SECOND))
                .save(output, "idos_first_mod:second_block_from_second");


        oreSmelting(output, List.of(ModBlocks.FIRST_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.SECOND_BLOCK.get(), 0.25f, 200, "second_block");
        oreSmelting(output, List.of(ModItems.FIRST.get()), RecipeCategory.MISC, ModItems.SECOND.get(), 0.25f, 200, "second");
        oreSmelting(output, FIRST_ORES, RecipeCategory.MISC, ModItems.SECOND.get(), 0.25f, 200, "first");
        oreBlasting(output, List.of(ModBlocks.FIRST_BLOCK.get()), RecipeCategory.BUILDING_BLOCKS, ModBlocks.SECOND_BLOCK.get(), 0.25f, 100, "second_block");
        oreBlasting(output, List.of(ModItems.FIRST.get()), RecipeCategory.MISC, ModItems.SECOND.get(), 0.25f, 100, "second");
        oreBlasting(output, FIRST_ORES, RecipeCategory.MISC, ModItems.SECOND.get(), 0.25f, 100, "first");

    }

    protected void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, IdosFirstMod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }



    public static class Runner extends RecipeProvider.Runner{

        protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new IdoRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "";
        }
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event){
        event.createProvider(IdoRecipeProvider.Runner::new);
    }

}
