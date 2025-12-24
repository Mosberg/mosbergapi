package dk.mosberg.api.client.registry;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;

/**
 * Centralized registry helper for entity and block entity renderers.
 * <p>
 * Provides type-safe registration methods for client-side rendering in Minecraft Fabric mods. This
 * class handles entity renderers, block entity renderers, and entity model layers.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 *
 * <h3>Entity Renderer Registration</h3>
 *
 * <pre>{@code
 * // Register entity renderer
 * MosbergRenderers.registerEntityRenderer(ModEntities.CUSTOM_MOB, CustomMobRenderer::new);
 *
 * // Register with model layer
 * MosbergRenderers.registerEntityRenderer(ModEntities.CUSTOM_MOB, CustomMobRenderer::new,
 *         ModModelLayers.CUSTOM_MOB, CustomMobModel::getTexturedModelData);
 * }</pre>
 *
 * <h3>Block Entity Renderer Registration</h3>
 *
 * <pre>{@code
 * // Register block entity renderer
 * MosbergRenderers.registerBlockEntityRenderer(ModBlockEntities.CUSTOM_CHEST,
 *         CustomChestRenderer::new);
 * }</pre>
 *
 * <h3>Model Layer Registration</h3>
 *
 * <pre>{@code
 * // Register model layer
 * MosbergRenderers.registerModelLayer(ModModelLayers.CUSTOM_ARMOR,
 *         CustomArmorModel::getTexturedModelData);
 *
 * // Register with inner and outer layers
 * MosbergRenderers.registerArmorModelLayers(ModModelLayers.CUSTOM_ARMOR_INNER,
 *         ModModelLayers.CUSTOM_ARMOR_OUTER, CustomArmorModel::getTexturedModelData);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergRenderers {

    private static final Logger LOGGER = LoggerFactory.getLogger("MosbergAPI/Renderers");
    private static final Map<EntityType<?>, String> REGISTERED_ENTITY_RENDERERS = new HashMap<>();
    private static final Map<BlockEntityType<?>, String> REGISTERED_BLOCK_ENTITY_RENDERERS =
            new HashMap<>();
    private static final Map<EntityModelLayer, EntityModelLayerRegistry.TexturedModelDataProvider> REGISTERED_MODEL_LAYERS =
            new HashMap<>();

    /**
     * Prevents instantiation of this utility class.
     */
    private MosbergRenderers() {
        throw new UnsupportedOperationException(
                "MosbergRenderers is a utility class and cannot be instantiated");
    }

    // ==========================================
    // Entity Renderer Registration
    // ==========================================

    /**
     * Registers an entity renderer.
     * <p>
     * This method associates an entity type with its renderer factory, allowing custom rendering
     * logic for entities.
     * </p>
     *
     * @param <T> The entity type
     * @param entityType The entity type to register a renderer for
     * @param rendererFactory The factory that creates the renderer
     * @return The entity type for method chaining
     *
     * @throws NullPointerException if entityType or rendererFactory is null
     *
     * @example
     *
     *          <pre>{@code
     * MosbergRenderers.registerEntityRenderer(
     *     ModEntities.ZOMBIE_VARIANT,
     *     ZombieVariantRenderer::new
     * );
     * }</pre>
     */
    public static <T extends Entity> EntityType<T> registerEntityRenderer(EntityType<T> entityType,
            EntityRendererFactory<T> rendererFactory) {

        if (entityType == null) {
            throw new NullPointerException("Entity type cannot be null");
        }
        if (rendererFactory == null) {
            throw new NullPointerException("Renderer factory cannot be null");
        }

        EntityRendererFactories.register(entityType, rendererFactory);
        REGISTERED_ENTITY_RENDERERS.put(entityType, rendererFactory.getClass().getName());

        LOGGER.debug("Registered entity renderer for: {}", entityType);
        return entityType;
    }

    /**
     * Registers an entity renderer with a model layer.
     * <p>
     * This method registers both the entity renderer and its associated model layer, streamlining
     * the process of setting up entities with custom models.
     * </p>
     *
     * @param <T> The entity type
     * @param entityType The entity type to register
     * @param rendererFactory The renderer factory
     * @param modelLayer The model layer for this entity
     * @param modelDataProvider Provider for the textured model data
     * @return The entity type for method chaining
     *
     * @example
     *
     *          <pre>{@code
     * MosbergRenderers.registerEntityRenderer(
     *     ModEntities.CUSTOM_CREATURE,
     *     CustomCreatureRenderer::new,
     *     ModModelLayers.CUSTOM_CREATURE,
     *     CustomCreatureModel::getTexturedModelData
     * );
     * }</pre>
     */
    public static <T extends Entity> EntityType<T> registerEntityRenderer(EntityType<T> entityType,
            EntityRendererFactory<T> rendererFactory, EntityModelLayer modelLayer,
            EntityModelLayerRegistry.TexturedModelDataProvider modelDataProvider) {

        registerEntityRenderer(entityType, rendererFactory);
        registerModelLayer(modelLayer, modelDataProvider);

        return entityType;
    }

    /**
     * Registers a living entity renderer with main and armor model layers.
     * <p>
     * Useful for entities that have both a main model and armor layers.
     * </p>
     *
     * @param <T> The entity type
     * @param entityType The entity type
     * @param rendererFactory The renderer factory
     * @param mainLayer The main model layer
     * @param mainModelData Provider for main model data
     * @param armorInnerLayer The inner armor layer
     * @param armorOuterLayer The outer armor layer
     * @param armorModelData Provider for armor model data
     * @return The entity type for method chaining
     */
    public static <T extends Entity> EntityType<T> registerLivingEntityRenderer(
            EntityType<T> entityType, EntityRendererFactory<T> rendererFactory,
            EntityModelLayer mainLayer,
            EntityModelLayerRegistry.TexturedModelDataProvider mainModelData,
            EntityModelLayer armorInnerLayer, EntityModelLayer armorOuterLayer,
            EntityModelLayerRegistry.TexturedModelDataProvider armorModelData) {

        registerEntityRenderer(entityType, rendererFactory);
        registerModelLayer(mainLayer, mainModelData);
        registerArmorModelLayers(armorInnerLayer, armorOuterLayer, armorModelData);

        return entityType;
    }

    // ==========================================
    // Block Entity Renderer Registration
    // ==========================================

    /**
     * Registers a block entity renderer.
     * <p>
     * Associates a block entity type with its renderer, enabling custom rendering for block
     * entities like chests, signs, or custom machines.
     * </p>
     *
     * <p>
     * <strong>Note:</strong> This method uses raw types internally to work around the two-parameter
     * BlockEntityRendererFactory interface. The renderer factory you provide should match the
     * signature expected by Minecraft's rendering system.
     * </p>
     *
     * @param <T> The block entity type
     * @param blockEntityType The block entity type
     * @param rendererFactory The renderer factory (lambda or method reference)
     * @return The block entity type for method chaining
     *
     * @throws NullPointerException if blockEntityType or rendererFactory is null
     *
     * @example
     *
     *          <pre>{@code
     * MosbergRenderers.registerBlockEntityRenderer(
     *     ModBlockEntities.CUSTOM_CHEST,
     *     CustomChestRenderer::new
     * );
     * }</pre>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityRenderer(
            BlockEntityType<T> blockEntityType, BlockEntityRendererFactory rendererFactory) {

        if (blockEntityType == null) {
            throw new NullPointerException("Block entity type cannot be null");
        }
        if (rendererFactory == null) {
            throw new NullPointerException("Renderer factory cannot be null");
        }

        BlockEntityRendererFactories.register(blockEntityType, rendererFactory);
        REGISTERED_BLOCK_ENTITY_RENDERERS.put(blockEntityType,
                rendererFactory.getClass().getName());

        LOGGER.debug("Registered block entity renderer for: {}", blockEntityType);
        return blockEntityType;
    }

    /**
     * Registers a block entity renderer with a model layer.
     * <p>
     * Combines block entity renderer registration with model layer setup.
     * </p>
     *
     * @param <T> The block entity type
     * @param blockEntityType The block entity type
     * @param rendererFactory The renderer factory
     * @param modelLayer The model layer
     * @param modelDataProvider Provider for model data
     * @return The block entity type for method chaining
     */
    @SuppressWarnings("rawtypes")
    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityRenderer(
            BlockEntityType<T> blockEntityType, BlockEntityRendererFactory rendererFactory,
            EntityModelLayer modelLayer,
            EntityModelLayerRegistry.TexturedModelDataProvider modelDataProvider) {

        registerBlockEntityRenderer(blockEntityType, rendererFactory);
        registerModelLayer(modelLayer, modelDataProvider);

        return blockEntityType;
    }

    // ==========================================
    // Model Layer Registration
    // ==========================================

    /**
     * Registers an entity model layer.
     * <p>
     * Model layers define the visual appearance of entities and are loaded during the client
     * initialization phase.
     * </p>
     *
     * @param modelLayer The model layer identifier
     * @param modelDataProvider Provider that supplies the textured model data
     * @return The model layer for method chaining
     *
     * @throws NullPointerException if modelLayer or modelDataProvider is null
     *
     * @example
     *
     *          <pre>{@code
     * MosbergRenderers.registerModelLayer(
     *     new EntityModelLayer(Identifier.of("mymod", "custom_entity"), "main"),
     *     CustomEntityModel::getTexturedModelData
     * );
     * }</pre>
     */
    public static EntityModelLayer registerModelLayer(EntityModelLayer modelLayer,
            EntityModelLayerRegistry.TexturedModelDataProvider modelDataProvider) {

        if (modelLayer == null) {
            throw new NullPointerException("Model layer cannot be null");
        }
        if (modelDataProvider == null) {
            throw new NullPointerException("Model data provider cannot be null");
        }

        EntityModelLayerRegistry.registerModelLayer(modelLayer, modelDataProvider);
        REGISTERED_MODEL_LAYERS.put(modelLayer, modelDataProvider);

        LOGGER.debug("Registered model layer: {}", modelLayer);
        return modelLayer;
    }

    /**
     * Registers inner and outer armor model layers.
     * <p>
     * Armor models typically require two layers: inner (for leggings) and outer (for helmet,
     * chestplate, and boots).
     * </p>
     *
     * @param innerLayer The inner armor layer
     * @param outerLayer The outer armor layer
     * @param modelDataProvider Provider for the armor model data
     *
     * @example
     *
     *          <pre>{@code
     * MosbergRenderers.registerArmorModelLayers(
     *     ModModelLayers.CUSTOM_ARMOR_INNER,
     *     ModModelLayers.CUSTOM_ARMOR_OUTER,
     *     CustomArmorModel::getTexturedModelData
     * );
     * }</pre>
     */
    public static void registerArmorModelLayers(EntityModelLayer innerLayer,
            EntityModelLayer outerLayer,
            EntityModelLayerRegistry.TexturedModelDataProvider modelDataProvider) {

        registerModelLayer(innerLayer, modelDataProvider);
        registerModelLayer(outerLayer, modelDataProvider);

        LOGGER.debug("Registered armor model layers: {} (inner), {} (outer)", innerLayer,
                outerLayer);
    }

    /**
     * Creates a standard entity model layer.
     * <p>
     * Helper method to create entity model layers with consistent naming.
     * </p>
     *
     * @param id The identifier for the entity
     * @param layer The layer name (e.g., "main", "armor", "saddle")
     * @return The created entity model layer
     *
     * @example
     *
     *          <pre>{@code
     * EntityModelLayer layer = MosbergRenderers.createModelLayer(
     *     Identifier.of("mymod", "custom_entity"),
     *     "main"
     * );
     * }</pre>
     */
    public static EntityModelLayer createModelLayer(Identifier id, String layer) {
        return new EntityModelLayer(id, layer);
    }

    /**
     * Creates a main entity model layer.
     * <p>
     * Shorthand for creating a model layer with the "main" layer name.
     * </p>
     *
     * @param id The entity identifier
     * @return The created entity model layer
     */
    public static EntityModelLayer createMainLayer(Identifier id) {
        return createModelLayer(id, "main");
    }

    /**
     * Creates inner and outer armor model layers.
     * <p>
     * Returns an array with [innerLayer, outerLayer].
     * </p>
     *
     * @param id The armor identifier
     * @return Array containing inner and outer layers
     */
    public static EntityModelLayer[] createArmorLayers(Identifier id) {
        return new EntityModelLayer[] {createModelLayer(id, "inner_armor"),
                createModelLayer(id, "outer_armor")};
    }

    // ==========================================
    // Query Methods
    // ==========================================

    /**
     * Checks if an entity renderer is registered.
     *
     * @param entityType The entity type to check
     * @return true if a renderer is registered
     */
    public static boolean hasEntityRenderer(EntityType<?> entityType) {
        return REGISTERED_ENTITY_RENDERERS.containsKey(entityType);
    }

    /**
     * Checks if a block entity renderer is registered.
     *
     * @param blockEntityType The block entity type to check
     * @return true if a renderer is registered
     */
    public static boolean hasBlockEntityRenderer(BlockEntityType<?> blockEntityType) {
        return REGISTERED_BLOCK_ENTITY_RENDERERS.containsKey(blockEntityType);
    }

    /**
     * Checks if a model layer is registered.
     *
     * @param modelLayer The model layer to check
     * @return true if the model layer is registered
     */
    public static boolean hasModelLayer(EntityModelLayer modelLayer) {
        return REGISTERED_MODEL_LAYERS.containsKey(modelLayer);
    }

    /**
     * Gets the number of registered entity renderers.
     *
     * @return Count of registered entity renderers
     */
    public static int getEntityRendererCount() {
        return REGISTERED_ENTITY_RENDERERS.size();
    }

    /**
     * Gets the number of registered block entity renderers.
     *
     * @return Count of registered block entity renderers
     */
    public static int getBlockEntityRendererCount() {
        return REGISTERED_BLOCK_ENTITY_RENDERERS.size();
    }

    /**
     * Gets the number of registered model layers.
     *
     * @return Count of registered model layers
     */
    public static int getModelLayerCount() {
        return REGISTERED_MODEL_LAYERS.size();
    }

    /**
     * Gets all registered entity types with renderers.
     *
     * @return Unmodifiable map of registered entity renderer names
     */
    public static Map<EntityType<?>, String> getRegisteredEntityRenderers() {
        return Map.copyOf(REGISTERED_ENTITY_RENDERERS);
    }

    /**
     * Gets all registered block entity types with renderers.
     *
     * @return Unmodifiable map of registered block entity renderer names
     */
    public static Map<BlockEntityType<?>, String> getRegisteredBlockEntityRenderers() {
        return Map.copyOf(REGISTERED_BLOCK_ENTITY_RENDERERS);
    }

    /**
     * Gets all registered model layers.
     *
     * @return Unmodifiable map of registered model layers
     */
    public static Map<EntityModelLayer, EntityModelLayerRegistry.TexturedModelDataProvider> getRegisteredModelLayers() {
        return Map.copyOf(REGISTERED_MODEL_LAYERS);
    }

    // ==========================================
    // Initialization
    // ==========================================

    /**
     * Initializes the renderer registry.
     * <p>
     * Call this method from your mod's client initializer to ensure all renderers are properly
     * registered.
     * </p>
     *
     * @example
     *
     *          <pre>{@code
     *          @Override
     *          public void onInitializeClient() {
     *          MosbergRenderers.initialize();
     *          }
     * }</pre>
     */
    public static void initialize() {
        LOGGER.info(
                "MosbergRenderers initialized with {} entity renderers, {} block entity renderers, and {} model layers",
                REGISTERED_ENTITY_RENDERERS.size(), REGISTERED_BLOCK_ENTITY_RENDERERS.size(),
                REGISTERED_MODEL_LAYERS.size());
    }

    /**
     * Logs all registered renderers and model layers.
     * <p>
     * Useful for debugging renderer registration.
     * </p>
     */
    public static void logRegisteredRenderers() {
        LOGGER.info("=== Registered Entity Renderers ===");
        REGISTERED_ENTITY_RENDERERS
                .forEach((type, factory) -> LOGGER.info("  - {}: {}", type, factory));

        LOGGER.info("=== Registered Block Entity Renderers ===");
        REGISTERED_BLOCK_ENTITY_RENDERERS
                .forEach((type, factory) -> LOGGER.info("  - {}: {}", type, factory));

        LOGGER.info("=== Registered Model Layers ===");
        REGISTERED_MODEL_LAYERS.keySet().forEach(layer -> LOGGER.info("  - {}", layer));
    }
}
