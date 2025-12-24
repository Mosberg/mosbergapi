package dk.mosberg.api.data;

import java.util.concurrent.CompletableFuture;
import dk.mosberg.api.data.provider.MosbergRecipeProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryWrapper;

public class MosbergApiRecipeProvider extends MosbergRecipeProvider {

    public MosbergApiRecipeProvider(DataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Mosberg API Recipes";
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries,
            RecipeExporter exporter) {
        return new MosbergApiRecipeGenerator(registries, exporter);
    }

    private static class MosbergApiRecipeGenerator extends RecipeGenerator {

        public MosbergApiRecipeGenerator(RegistryWrapper.WrapperLookup registries,
                RecipeExporter exporter) {
            super(registries, exporter);
        }

        @Override
        public void generate() {
            // Add your recipes here using the helper methods below

            generateToolRecipes();
            generateBlockRecipes();
            generateSmeltingRecipes();
            generateCookingRecipes();
            generateTagRecipes();
            generateConditionalRecipes();
        }

        // ==================== TOOL RECIPES ====================

        private void generateToolRecipes() {
            // Example: Pickaxe
            // createShaped(RecipeCategory.TOOLS, MosbergItems.CUSTOM_PICKAXE)
            // .pattern("III")
            // .pattern(" S ")
            // .pattern(" S ")
            // .input('I', MosbergItems.CUSTOM_INGOT)
            // .input('S', Items.STICK)
            // .criterion(hasItem(MosbergItems.CUSTOM_INGOT),
            // conditionsFromItem(MosbergItems.CUSTOM_INGOT))
            // .offerTo(exporter);

            // Example: Sword
            // createShaped(RecipeCategory.COMBAT, MosbergItems.CUSTOM_SWORD)
            // .pattern("I")
            // .pattern("I")
            // .pattern("S")
            // .input('I', MosbergItems.CUSTOM_INGOT)
            // .input('S', Items.STICK)
            // .criterion(hasItem(MosbergItems.CUSTOM_INGOT),
            // conditionsFromItem(MosbergItems.CUSTOM_INGOT))
            // .offerTo(exporter);
        }

        // ==================== BLOCK RECIPES ====================

        private void generateBlockRecipes() {
            // Example: Block from 9 ingots
            // createShaped(RecipeCategory.BUILDING_BLOCKS, MosbergBlocks.CUSTOM_BLOCK)
            // .pattern("III")
            // .pattern("III")
            // .pattern("III")
            // .input('I', MosbergItems.CUSTOM_INGOT)
            // .criterion(hasItem(MosbergItems.CUSTOM_INGOT),
            // conditionsFromItem(MosbergItems.CUSTOM_INGOT))
            // .offerTo(exporter);

            // Example: Ingots from block (reversible)
            // createShapeless(RecipeCategory.MISC, MosbergItems.CUSTOM_INGOT, 9)
            // .input(MosbergBlocks.CUSTOM_BLOCK)
            // .criterion(hasItem(MosbergBlocks.CUSTOM_BLOCK),
            // conditionsFromItem(MosbergBlocks.CUSTOM_BLOCK))
            // .offerTo(exporter);

            // Example: Stairs
            // createShaped(RecipeCategory.BUILDING_BLOCKS, MosbergBlocks.CUSTOM_STAIRS, 4)
            // .pattern("I ")
            // .pattern("II ")
            // .pattern("III")
            // .input('I', MosbergBlocks.CUSTOM_BLOCK)
            // .criterion(hasItem(MosbergBlocks.CUSTOM_BLOCK),
            // conditionsFromItem(MosbergBlocks.CUSTOM_BLOCK))
            // .offerTo(exporter);

            // Example: Slab
            // createShaped(RecipeCategory.BUILDING_BLOCKS, MosbergBlocks.CUSTOM_SLAB, 6)
            // .pattern("III")
            // .input('I', MosbergBlocks.CUSTOM_BLOCK)
            // .criterion(hasItem(MosbergBlocks.CUSTOM_BLOCK),
            // conditionsFromItem(MosbergBlocks.CUSTOM_BLOCK))
            // .offerTo(exporter);
        }

        // ==================== SMELTING RECIPES ====================

        private void generateSmeltingRecipes() {
            // Example: Ore to ingot (all methods)
            // List<ItemConvertible> ores = List.of(
            // MosbergBlocks.CUSTOM_ORE,
            // MosbergBlocks.DEEPSLATE_CUSTOM_ORE,
            // MosbergItems.RAW_CUSTOM
            // );

            // Furnace
            // offerSmelting(exporter, ores, RecipeCategory.MISC,
            // MosbergItems.CUSTOM_INGOT, 0.7f, 200, "custom_ingot");

            // Blast furnace (faster)
            // offerBlasting(exporter, ores, RecipeCategory.MISC,
            // MosbergItems.CUSTOM_INGOT, 0.7f, 100, "custom_ingot");
        }

        // ==================== COOKING RECIPES ====================

        private void generateCookingRecipes() {
            // Example: Food item
            // offerFoodCookingRecipe(exporter, "smoking",
            // RecipeSerializer.SMOKING, SmokingRecipe::new,
            // 100, MosbergItems.RAW_CUSTOM_FOOD,
            // MosbergItems.COOKED_CUSTOM_FOOD, 0.35f);

            // Example: Campfire cooking
            // offerFoodCookingRecipe(exporter, "campfire_cooking",
            // RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new,
            // 600, MosbergItems.RAW_CUSTOM_FOOD,
            // MosbergItems.COOKED_CUSTOM_FOOD, 0.35f);
        }

        // ==================== TAG-BASED RECIPES ====================

        private void generateTagRecipes() {
            // Example: Recipe using item tags
            // createShaped(RecipeCategory.TOOLS, MosbergItems.CUSTOM_TOOL)
            // .pattern("###")
            // .pattern(" S ")
            // .pattern(" S ")
            // .input('#', ItemTags.PLANKS)
            // .input('S', Items.STICK)
            // .criterion("has_planks", conditionsFromTag(ItemTags.PLANKS))
            // .offerTo(exporter);
        }

        // ==================== CONDITIONAL RECIPES ====================

        private void generateConditionalRecipes() {
            // Example: Recipe with custom advancement requirement
            // createShaped(RecipeCategory.MISC, MosbergItems.SPECIAL_ITEM)
            // .pattern(" I ")
            // .pattern("IDI")
            // .pattern(" I ")
            // .input('I', Items.IRON_INGOT)
            // .input('D', Items.DIAMOND)
            // .criterion("has_diamond", conditionsFromItem(Items.DIAMOND))
            // .criterion("has_nether_star", conditionsFromItem(Items.NETHER_STAR))
            // .offerTo(exporter);
        }
    }
}
