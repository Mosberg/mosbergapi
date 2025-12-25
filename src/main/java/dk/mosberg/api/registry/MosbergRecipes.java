package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom recipe types and serializers in MosbergAPI. Register your recipes
 * here using the provided helper methods.
 *
 * @example
 * <pre>{@code
 * public static final RecipeType<AlchemyRecipe> ALCHEMY_TYPE = registerType("alchemy");
 *
 * public static final RecipeSerializer<AlchemyRecipe> ALCHEMY_SERIALIZER = registerSerializer(
 *     "alchemy",
 *     new AlchemyRecipe.Serializer());
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergRecipes {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergRecipes.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers a recipe type with a simple name.
     *
     * @param name The recipe type name (will be prefixed with mod ID)
     * @param <T> The recipe class type
     * @return The registered RecipeType
     *
     * @example
     * <pre>{@code
     * RecipeType<InfusionRecipe> infusion = MosbergRecipes.registerType("infusion");
     * }</pre>
     */
    public static <T extends Recipe<?>> RecipeType<T> registerType(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        RecipeType<T> type = new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        };
        RecipeType<T> registered = Registry.register(Registries.RECIPE_TYPE, id, type);
        LOGGER.debug("Registered recipe type: {}", name);
        return registered;
    }

    /**
     * Registers a recipe serializer with a simple name.
     *
     * @param name The serializer name (will be prefixed with mod ID)
     * @param serializer The RecipeSerializer to register
     * @param <T> The recipe class type
     * @return The registered RecipeSerializer
     *
     * @example
     * <pre>{@code
     * RecipeSerializer<AlchemyRecipe> serializer = MosbergRecipes.registerSerializer(
     *     "alchemy",
     *     new AlchemyRecipe.Serializer());
     * }</pre>
     */
    public static <T extends Recipe<?>> RecipeSerializer<T> registerSerializer(String name,
            RecipeSerializer<T> serializer) {
        Identifier id = Identifier.of(MOD_ID, name);
        RecipeSerializer<T> registered = Registry.register(Registries.RECIPE_SERIALIZER, id, serializer);
        LOGGER.debug("Registered recipe serializer: {}", name);
        return registered;
    }

    /**
     * Registers both a recipe type and its serializer with matching names.
     *
     * @param name The base name for both type and serializer
     * @param serializer The RecipeSerializer to register
     * @param <T> The recipe class type
     * @return A RecipePair containing both registered components
     *
     * @example
     * <pre>{@code
     * RecipePair<CrushingRecipe> crushing = MosbergRecipes.registerPair(
     *     "crushing",
     *     new CrushingRecipe.Serializer());
     * }</pre>
     */
    public static <T extends Recipe<?>> RecipePair<T> registerPair(String name,
            RecipeSerializer<T> serializer) {
        RecipeType<T> type = registerType(name);
        RecipeSerializer<T> registeredSerializer = registerSerializer(name, serializer);
        LOGGER.debug("Registered recipe pair: {} (type & serializer)", name);
        return new RecipePair<>(type, registeredSerializer);
    }

    /**
     * Initialize and register all recipes. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI recipes");
        // Recipe registration happens when register() is called
    }

    /**
     * Record to hold a recipe type and its serializer.
     *
     * @param <T> The recipe type
     */
    public record RecipePair<T extends Recipe<?>>(
        RecipeType<T> type,
        RecipeSerializer<T> serializer
    ) {}
}
