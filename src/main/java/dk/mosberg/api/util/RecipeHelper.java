package dk.mosberg.api.util;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

/**
 * Helper for recipe operations in Minecraft 1.21.10+
 */
public class RecipeHelper {

    /**
     * Gets a recipe by its identifier
     */
    public static Optional<RecipeEntry<?>> getRecipe(ServerWorld world, Identifier id) {
        RegistryKey<Recipe<?>> key = RegistryKey.of(RegistryKeys.RECIPE, id);
        return world.getRecipeManager().get(key);
    }

    /**
     * Gets a recipe by its registry key
     */
    public static Optional<RecipeEntry<?>> getRecipe(ServerWorld world,
            RegistryKey<Recipe<?>> key) {
        return world.getRecipeManager().get(key);
    }

    /**
     * Checks if a recipe exists
     */
    public static boolean hasRecipe(ServerWorld world, Identifier id) {
        return getRecipe(world, id).isPresent();
    }

    /**
     * Gets all recipes
     */
    public static Collection<RecipeEntry<?>> getAllRecipes(ServerWorld world) {
        return world.getRecipeManager().values();
    }

    /**
     * Gets all recipes of a specific type FIXED: Filter recipes manually since listAllOfType()
     * doesn't exist in 1.21.10
     */
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<RecipeEntry<T>> getRecipesByType(ServerWorld world,
            RecipeType<T> type) {
        return world.getRecipeManager().values().stream()
                .filter(entry -> entry.value().getType() == type)
                .map(entry -> (RecipeEntry<T>) entry).collect(Collectors.toList());
    }

    /**
     * Gets all recipe identifiers of a specific type
     */
    public static <T extends Recipe<?>> List<Identifier> getRecipeIdsByType(ServerWorld world,
            RecipeType<T> type) {
        return getRecipesByType(world, type).stream().map(entry -> entry.id().getValue())
                .collect(Collectors.toList());
    }

    /**
     * Filters recipes by a custom predicate
     */
    public static List<RecipeEntry<?>> filterRecipes(ServerWorld world,
            java.util.function.Predicate<RecipeEntry<?>> predicate) {
        return world.getRecipeManager().values().stream().filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Creates a registry key for a recipe
     */
    public static RegistryKey<Recipe<?>> createKey(Identifier id) {
        return RegistryKey.of(RegistryKeys.RECIPE, id);
    }

    /**
     * Creates a registry key for a recipe with mod namespace
     */
    public static RegistryKey<Recipe<?>> createKey(String modId, String path) {
        return createKey(Identifier.of(modId, path));
    }

    /**
     * Gets the output item from a recipe FIXED: Use value().getResult() which returns ItemStack
     * directly in 1.21.10
     */
    public static Optional<ItemStack> getRecipeOutput(RecipeEntry<?> entry, ServerWorld world) {
        try {
            Recipe<?> recipe = entry.value();
            if (hasGetResult(recipe)) {
                ItemStack result = invokeGetResult(recipe, world.getRegistryManager());
                return Optional.of(result);
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Helper to check if getResult(DynamicRegistryManager) exists
    private static boolean hasGetResult(Recipe<?> recipe) {
        try {
            recipe.getClass().getMethod("getResult",
                    net.minecraft.registry.DynamicRegistryManager.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    // Helper to invoke getResult reflectively
    private static ItemStack invokeGetResult(Recipe<?> recipe,
            net.minecraft.registry.DynamicRegistryManager drm) {
        try {
            java.lang.reflect.Method m = recipe.getClass().getMethod("getResult",
                    net.minecraft.registry.DynamicRegistryManager.class);
            return (ItemStack) m.invoke(recipe, drm);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }

    /**
     * Gets all recipe outputs of a specific type
     */
    public static <T extends Recipe<?>> List<ItemStack> getRecipeOutputsByType(ServerWorld world,
            RecipeType<T> type) {
        return getRecipesByType(world, type).stream().map(entry -> {
            Recipe<?> recipe = entry.value();
            if (hasGetResult(recipe)) {
                return invokeGetResult(recipe, world.getRegistryManager());
            }
            return ItemStack.EMPTY;
        }).filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    /**
     * Finds recipes that produce a specific item
     */
    public static List<RecipeEntry<?>> getRecipesForItem(ServerWorld world, ItemStack targetItem) {
        return world.getRecipeManager().values().stream().filter(entry -> {
            Recipe<?> recipe = entry.value();
            if (hasGetResult(recipe)) {
                ItemStack result = invokeGetResult(recipe, world.getRegistryManager());
                return ItemStack.areItemsEqual(result, targetItem);
            }
            return false;
        }).collect(Collectors.toList());
    }

    /**
     * Checks if a recipe can be crafted with the given inventory
     */
    public static boolean canCraft(RecipeEntry<?> entry,
            net.minecraft.inventory.Inventory inventory, ServerWorld world) {
        Recipe<?> recipe = entry.value();
        if (recipe instanceof Recipe<?>) {
            try {
                // Attempt to call matches if possible
                java.lang.reflect.Method matchesMethod = recipe.getClass().getMethod("matches",
                        net.minecraft.inventory.Inventory.class, net.minecraft.world.World.class);
                Boolean result = (Boolean) matchesMethod.invoke(recipe, inventory,
                        (net.minecraft.world.World) world);
                return result != null && result;
            } catch (Exception e) {
                // Cannot invoke matches, cannot craft
                return false;
            }
        }
        return false;
    }
}
