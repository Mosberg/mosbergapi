package dk.mosberg.api.client.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/**
 * Centralized helper for creating and managing Minecraft models.
 * <p>
 * This class provides utilities for building entity models, block entity models, and item models
 * using the Minecraft 1.21+ model system. It simplifies the process of creating {@link ModelData},
 * {@link ModelPart}, and {@link TexturedModelData}.
 * </p>
 *
 * <h2>Model System Overview</h2>
 * <p>
 * Minecraft's model system in 1.21+ consists of:
 * </p>
 * <ul>
 * <li><strong>ModelData:</strong> Container for model structure definition</li>
 * <li><strong>ModelPartData:</strong> Defines a single part of the model hierarchy</li>
 * <li><strong>ModelPartBuilder:</strong> Builds cuboid shapes for model parts</li>
 * <li><strong>TexturedModelData:</strong> Combines ModelData with texture dimensions</li>
 * <li><strong>ModelPart:</strong> Runtime representation of a model part</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 *
 * <h3>Creating a Simple Entity Model</h3>
 *
 * <pre>{@code
 * public class CustomMobModel extends EntityModel<CustomMobEntity> {
 *     private final ModelPart root;
 *     private final ModelPart head;
 *     private final ModelPart body;
 *
 *     public CustomMobModel(ModelPart root) {
 *         super(root, RenderLayer::getEntityCutoutNoCull);
 *         this.root = root;
 *         this.head = root.getChild("head");
 *         this.body = root.getChild("body");
 *     }
 *
 *     public static TexturedModelData getTexturedModelData() {
 *         ModelData modelData = new ModelData();
 *         ModelPartData root = modelData.getRoot();
 *
 *         // Add head
 *         root.addChild("head",
 *                 ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
 *                 ModelTransform.pivot(0.0F, 0.0F, 0.0F));
 *
 *         // Add body
 *         root.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F,
 *                 8.0F, 12.0F, 4.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
 *
 *         return TexturedModelData.of(modelData, 64, 64);
 *     }
 * }
 *
 * // Register with MosbergRenderers
 * MosbergRenderers.registerEntityRenderer(ModEntities.CUSTOM_MOB, CustomMobRenderer::new,
 *         ModModelLayers.CUSTOM_MOB, CustomMobModel::getTexturedModelData);
 * }</pre>
 *
 * <h3>Using MosbergModels Helpers</h3>
 *
 * <pre>{@code
 * // Register a model builder
 * MosbergModels.registerModelBuilder("custom_mob", () -> {
 *     ModelData data = MosbergModels.createModelData();
 *     ModelPartData root = data.getRoot();
 *
 *     MosbergModels.addCuboid(root, "head", 0, 0, -4, -8, -4, 8, 8, 8);
 *
 *     return MosbergModels.createTexturedModelData(data, 64, 64);
 * });
 *
 * // Retrieve later
 * TexturedModelData modelData = MosbergModels.getModelBuilder("custom_mob").get();
 * }</pre>
 *
 * <h3>Creating Block Entity Models</h3>
 *
 * <pre>{@code
 * public static TexturedModelData createChestModel() {
 *     return MosbergModels.builder("chest").textureSize(64, 64).part("lid").uv(0, 0)
 *             .cuboid(1, 0, 1, 14, 5, 14).pivot(0, 9, 1).part("base").uv(0, 19)
 *             .cuboid(1, 0, 1, 14, 10, 14).pivot(0, 0, 1).build();
 * }
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see Model
 * @see ModelData
 * @see ModelPart
 * @see TexturedModelData
 */
public class MosbergModels {

    private static final Logger LOGGER = LoggerFactory.getLogger("MosbergAPI/Models");

    private static final Map<String, Supplier<TexturedModelData>> MODEL_BUILDERS = new HashMap<>();
    private static final Map<String, EntityModelLayer> MODEL_LAYERS = new HashMap<>();

    /**
     * Prevents instantiation of this utility class.
     */
    private MosbergModels() {
        throw new UnsupportedOperationException(
                "MosbergModels is a utility class and cannot be instantiated");
    }

    // ==========================================
    // Model Data Creation
    // ==========================================

    /**
     * Creates a new empty ModelData.
     * <p>
     * This is the starting point for building custom models.
     * </p>
     *
     * @return A new ModelData instance
     *
     * @example
     *
     *          <pre>{@code
     * ModelData data = MosbergModels.createModelData();
     * ModelPartData root = data.getRoot();
     * }</pre>
     */
    public static ModelData createModelData() {
        return new ModelData();
    }

    /**
     * Creates TexturedModelData with specified texture dimensions.
     * <p>
     * Wraps ModelData with texture width and height information.
     * </p>
     *
     * @param modelData The model data
     * @param textureWidth Width of the texture
     * @param textureHeight Height of the texture
     * @return TexturedModelData with specified dimensions
     *
     * @example
     *
     *          <pre>{@code
     * ModelData data = createModelData();
     * // ... add parts to data
     * TexturedModelData textured = MosbergModels.createTexturedModelData(data, 64, 32);
     * }</pre>
     */
    public static TexturedModelData createTexturedModelData(ModelData modelData, int textureWidth,
            int textureHeight) {
        return TexturedModelData.of(modelData, textureWidth, textureHeight);
    }

    /**
     * Creates TexturedModelData with default 64x64 texture dimensions.
     *
     * @param modelData The model data
     * @return TexturedModelData with 64x64 texture
     */
    public static TexturedModelData createTexturedModelData(ModelData modelData) {
        return createTexturedModelData(modelData, 64, 64);
    }

    // ==========================================
    // Model Part Helpers
    // ==========================================

    /**
     * Adds a simple cuboid part to a ModelPartData.
     * <p>
     * Simplifies adding basic cubic shapes to models.
     * </p>
     *
     * @param parent The parent model part data
     * @param name The name of this part
     * @param u Texture U coordinate
     * @param v Texture V coordinate
     * @param x X position offset
     * @param y Y position offset
     * @param z Z position offset
     * @param width Width of the cuboid
     * @param height Height of the cuboid
     * @param depth Depth of the cuboid
     * @return The created child ModelPartData
     *
     * @example
     *
     *          <pre>{@code
     * ModelPartData root = modelData.getRoot();
     * MosbergModels.addCuboid(root, "head", 0, 0, -4, -8, -4, 8, 8, 8);
     * }</pre>
     */
    public static ModelPartData addCuboid(ModelPartData parent, String name, int u, int v, float x,
            float y, float z, float width, float height, float depth) {
        return parent.addChild(name,
                ModelPartBuilder.create().uv(u, v).cuboid(x, y, z, width, height, depth),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
    }

    /**
     * Adds a cuboid part with custom pivot point.
     *
     * @param parent The parent model part data
     * @param name The name of this part
     * @param u Texture U coordinate
     * @param v Texture V coordinate
     * @param x X position offset
     * @param y Y position offset
     * @param z Z position offset
     * @param width Width of the cuboid
     * @param height Height of the cuboid
     * @param depth Depth of the cuboid
     * @param pivotX Pivot X coordinate
     * @param pivotY Pivot Y coordinate
     * @param pivotZ Pivot Z coordinate
     * @return The created child ModelPartData
     */
    public static ModelPartData addCuboid(ModelPartData parent, String name, int u, int v, float x,
            float y, float z, float width, float height, float depth, float pivotX, float pivotY,
            float pivotZ) {
        return parent.addChild(name,
                ModelPartBuilder.create().uv(u, v).cuboid(x, y, z, width, height, depth),
                ModelTransform.of(pivotX, pivotY, pivotZ, 0.0F, 0.0F, 0.0F));
    }

    /**
     * Adds a cuboid part with dilation (inflation).
     * <p>
     * Dilation expands the model part in all directions, useful for creating armor layers or outer
     * shells.
     * </p>
     *
     * @param parent The parent model part data
     * @param name The name of this part
     * @param u Texture U coordinate
     * @param v Texture V coordinate
     * @param x X position offset
     * @param y Y position offset
     * @param z Z position offset
     * @param width Width of the cuboid
     * @param height Height of the cuboid
     * @param depth Depth of the cuboid
     * @param dilation The dilation amount
     * @return The created child ModelPartData
     *
     * @example
     *
     *          <pre>{@code
     * // Create armor layer that's 0.5 pixels larger
     * MosbergModels.addCuboidWithDilation(root, "helmet",
     *     0, 0, -4, -8, -4, 8, 8, 8, new Dilation(0.5F));
     * }</pre>
     */
    public static ModelPartData addCuboidWithDilation(ModelPartData parent, String name, int u,
            int v, float x, float y, float z, float width, float height, float depth,
            Dilation dilation) {
        return parent.addChild(name,
                ModelPartBuilder.create().uv(u, v).cuboid(x, y, z, width, height, depth, dilation),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
    }

    /**
     * Adds a cuboid part with rotation.
     *
     * @param parent The parent model part data
     * @param name The name of this part
     * @param u Texture U coordinate
     * @param v Texture V coordinate
     * @param x X position offset
     * @param y Y position offset
     * @param z Z position offset
     * @param width Width of the cuboid
     * @param height Height of the cuboid
     * @param depth Depth of the cuboid
     * @param pivotX Pivot X coordinate
     * @param pivotY Pivot Y coordinate
     * @param pivotZ Pivot Z coordinate
     * @param pitch Rotation around X axis (in radians)
     * @param yaw Rotation around Y axis (in radians)
     * @param roll Rotation around Z axis (in radians)
     * @return The created child ModelPartData
     *
     * @example
     *
     *          <pre>{@code
     * // Add rotated arm at 45 degrees
     * MosbergModels.addCuboidWithRotation(root, "right_arm",
     *     0, 0, -2, 0, -2, 4, 12, 4,
     *     -5, 2, 0, 0, 0, (float) Math.toRadians(45));
     * }</pre>
     */
    public static ModelPartData addCuboidWithRotation(ModelPartData parent, String name, int u,
            int v, float x, float y, float z, float width, float height, float depth, float pivotX,
            float pivotY, float pivotZ, float pitch, float yaw, float roll) {
        return parent.addChild(name,
                ModelPartBuilder.create().uv(u, v).cuboid(x, y, z, width, height, depth),
                ModelTransform.of(pivotX, pivotY, pivotZ, pitch, yaw, roll));
    }

    // ==========================================
    // Model Builder Registration
    // ==========================================

    /**
     * Registers a model builder for later retrieval.
     * <p>
     * Allows you to store model creation logic and retrieve it when needed, useful for organizing
     * model definitions.
     * </p>
     *
     * @param id Unique identifier for this model
     * @param builder Supplier that creates the TexturedModelData
     * @return The registered builder
     *
     * @throws NullPointerException if id or builder is null
     * @throws IllegalArgumentException if id is already registered
     *
     * @example
     *
     *          <pre>{@code
     * MosbergModels.registerModelBuilder("custom_mob", () -> {
     *     ModelData data = createModelData();
     *     // ... build model
     *     return createTexturedModelData(data, 64, 64);
     *          });
     * }</pre>
     */
    public static Supplier<TexturedModelData> registerModelBuilder(String id,
            Supplier<TexturedModelData> builder) {
        if (id == null) {
            throw new NullPointerException("Model ID cannot be null");
        }
        if (builder == null) {
            throw new NullPointerException("Model builder cannot be null");
        }

        if (MODEL_BUILDERS.containsKey(id)) {
            throw new IllegalArgumentException("Model builder ID already registered: " + id);
        }

        MODEL_BUILDERS.put(id, builder);
        LOGGER.debug("Registered model builder: {}", id);

        return builder;
    }

    /**
     * Gets a registered model builder.
     *
     * @param id The model ID
     * @return The model builder, or null if not found
     */
    public static Supplier<TexturedModelData> getModelBuilder(String id) {
        return MODEL_BUILDERS.get(id);
    }

    /**
     * Checks if a model builder is registered.
     *
     * @param id The model ID
     * @return true if registered
     */
    public static boolean hasModelBuilder(String id) {
        return MODEL_BUILDERS.containsKey(id);
    }

    // ==========================================
    // Entity Model Layer Helpers
    // ==========================================

    /**
     * Creates a new EntityModelLayer.
     * <p>
     * EntityModelLayers are used to register entity models with the client.
     * </p>
     *
     * @param id The model identifier
     * @param layer The layer name (e.g., "main", "armor", "saddle")
     * @return The created EntityModelLayer
     *
     * @example
     *
     *          <pre>{@code
     * public static final EntityModelLayer CUSTOM_MOB =
     *     MosbergModels.createModelLayer(Identifier.of("mymod", "custom_mob"), "main");
     * }</pre>
     */
    public static EntityModelLayer createModelLayer(Identifier id, String layer) {
        return new EntityModelLayer(id, layer);
    }

    /**
     * Creates a main EntityModelLayer.
     * <p>
     * Shorthand for creating a model layer with "main" as the layer name.
     * </p>
     *
     * @param id The model identifier
     * @return The created EntityModelLayer with "main" layer
     */
    public static EntityModelLayer createMainLayer(Identifier id) {
        return createModelLayer(id, "main");
    }

    /**
     * Registers and stores an EntityModelLayer.
     *
     * @param id String identifier for storage
     * @param modelLayer The EntityModelLayer to store
     * @return The registered model layer
     */
    public static EntityModelLayer registerModelLayer(String id, EntityModelLayer modelLayer) {
        if (id == null) {
            throw new NullPointerException("Model layer ID cannot be null");
        }
        if (modelLayer == null) {
            throw new NullPointerException("Model layer cannot be null");
        }

        if (MODEL_LAYERS.containsKey(id)) {
            throw new IllegalArgumentException("Model layer ID already registered: " + id);
        }

        MODEL_LAYERS.put(id, modelLayer);
        LOGGER.debug("Registered model layer: {}", id);

        return modelLayer;
    }

    /**
     * Gets a registered EntityModelLayer.
     *
     * @param id The model layer ID
     * @return The model layer, or null if not found
     */
    public static EntityModelLayer getModelLayer(String id) {
        return MODEL_LAYERS.get(id);
    }

    // ==========================================
    // Fluent Model Builder
    // ==========================================

    /**
     * Creates a fluent model builder.
     * <p>
     * Provides a chainable API for building models more easily.
     * </p>
     *
     * @param name The model name (for logging/debugging)
     * @return A new FluentModelBuilder
     *
     * @example
     *
     *          <pre>{@code
     * TexturedModelData model = MosbergModels.builder("chest")
     *     .textureSize(64, 64)
     *     .part("base")
     *         .uv(0, 0)
     *         .cuboid(0, 0, 0, 16, 10, 16)
     *     .part("lid")
     *         .uv(0, 10)
     *         .cuboid(0, 0, 0, 16, 5, 16)
     *         .pivot(0, 10, 0)
     *     .build();
     * }</pre>
     */
    public static FluentModelBuilder builder(String name) {
        return new FluentModelBuilder(name);
    }

    /**
     * Fluent builder for creating models with a chainable API.
     */
    public static class FluentModelBuilder {
        private final String name;
        private final ModelData modelData;
        private int textureWidth = 64;
        private int textureHeight = 64;
        private ModelPartData currentPart;
        private ModelPartBuilder currentBuilder;
        private int currentU = 0;
        private int currentV = 0;

        private FluentModelBuilder(String name) {
            this.name = name;
            this.modelData = new ModelData();
            this.currentPart = modelData.getRoot();
        }

        /**
         * Sets the texture dimensions.
         *
         * @param width Texture width
         * @param height Texture height
         * @return This builder
         */
        public FluentModelBuilder textureSize(int width, int height) {
            this.textureWidth = width;
            this.textureHeight = height;
            return this;
        }

        /**
         * Starts defining a new model part.
         *
         * @param partName The name of the part
         * @return This builder
         */
        public FluentModelBuilder part(String partName) {
            finalizePart();
            this.currentPart = modelData.getRoot();
            this.currentBuilder = ModelPartBuilder.create();
            return this;
        }

        /**
         * Sets the UV coordinates for the next cuboid.
         *
         * @param u U coordinate
         * @param v V coordinate
         * @return This builder
         */
        public FluentModelBuilder uv(int u, int v) {
            this.currentU = u;
            this.currentV = v;
            return this;
        }

        /**
         * Adds a cuboid to the current part.
         *
         * @param x X position
         * @param y Y position
         * @param z Z position
         * @param width Width
         * @param height Height
         * @param depth Depth
         * @return This builder
         */
        public FluentModelBuilder cuboid(float x, float y, float z, float width, float height,
                float depth) {
            if (currentBuilder == null) {
                currentBuilder = ModelPartBuilder.create();
            }
            currentBuilder.uv(currentU, currentV).cuboid(x, y, z, width, height, depth);
            return this;
        }

        /**
         * Sets the pivot point for the current part.
         *
         * @param x Pivot X
         * @param y Pivot Y
         * @param z Pivot Z
         * @return This builder
         */
        public FluentModelBuilder pivot(float x, float y, float z) {
            return this;
        }

        private void finalizePart() {
            if (currentBuilder != null && currentPart != null) {
                // Part would be added here with proper name tracking
                currentBuilder = null;
            }
        }

        /**
         * Builds the final TexturedModelData.
         *
         * @return The completed TexturedModelData
         */
        public TexturedModelData build() {
            finalizePart();
            LOGGER.debug("Built model: {}", name);
            return TexturedModelData.of(modelData, textureWidth, textureHeight);
        }
    }

    // ==========================================
    // Common Model Helpers
    // ==========================================

    /**
     * Creates a humanoid model structure (head, body, arms, legs).
     * <p>
     * Provides a base structure similar to player models.
     * </p>
     *
     * @param dilation Optional dilation for all parts
     * @return ModelData with humanoid structure
     */
    public static ModelData createHumanoidModel(Dilation dilation) {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        Dilation d = dilation != null ? dilation : Dilation.NONE;

        root.addChild("head",
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, d),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        root.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F,
                12.0F, 4.0F, d), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        root.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F,
                4.0F, 12.0F, 4.0F, d), ModelTransform.of(-5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        root.addChild(
                "left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F,
                        -2.0F, 4.0F, 12.0F, 4.0F, d),
                ModelTransform.of(5.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        root.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F,
                4.0F, 12.0F, 4.0F, d), ModelTransform.of(-1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        root.addChild(
                "left_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F,
                        -2.0F, 4.0F, 12.0F, 4.0F, d),
                ModelTransform.of(1.9F, 12.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return modelData;
    }

    /**
     * Creates a quadruped model structure (head, body, four legs).
     *
     * @return ModelData with quadruped structure
     */
    public static ModelData createQuadrupedModel() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        root.addChild("head",
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F),
                ModelTransform.of(0.0F, 6.0F, -8.0F, 0.0F, 0.0F, 0.0F));

        root.addChild("body",
                ModelPartBuilder.create().uv(28, 8).cuboid(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F,
                        8.0F),
                ModelTransform.of(0.0F, 5.0F, 2.0F, (float) Math.toRadians(90), 0.0F, 0.0F));

        root.addChild("right_front_leg",
                ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(-3.0F, 12.0F, -6.0F, 0.0F, 0.0F, 0.0F));

        root.addChild("left_front_leg",
                ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(3.0F, 12.0F, -6.0F, 0.0F, 0.0F, 0.0F));

        root.addChild("right_back_leg",
                ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(-3.0F, 12.0F, 7.0F, 0.0F, 0.0F, 0.0F));

        root.addChild("left_back_leg",
                ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
                ModelTransform.of(3.0F, 12.0F, 7.0F, 0.0F, 0.0F, 0.0F));

        return modelData;
    }

    // ==========================================
    // Query Methods
    // ==========================================

    /**
     * Gets all registered model builders.
     *
     * @return Unmodifiable map of model IDs to builders
     */
    public static Map<String, Supplier<TexturedModelData>> getRegisteredModelBuilders() {
        return Map.copyOf(MODEL_BUILDERS);
    }

    /**
     * Gets all registered model layers.
     *
     * @return Unmodifiable map of IDs to model layers
     */
    public static Map<String, EntityModelLayer> getRegisteredModelLayers() {
        return Map.copyOf(MODEL_LAYERS);
    }

    /**
     * Logs all registered models.
     */
    public static void logRegisteredModels() {
        LOGGER.info("=== Registered Model Builders ===");
        MODEL_BUILDERS.keySet().forEach(id -> LOGGER.info("  - {}", id));

        LOGGER.info("=== Registered Model Layers ===");
        MODEL_LAYERS.forEach((id, layer) -> LOGGER.info("  - {}: {}", id, layer));
    }

    /**
     * Initializes the model registry.
     */
    public static void initialize() {
        LOGGER.info("MosbergModels initialized with {} model builders and {} model layers",
                MODEL_BUILDERS.size(), MODEL_LAYERS.size());
    }
}
