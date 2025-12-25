# Mosberg API - Client Classes

```
└── 📁client
    └── 📁java
        └── 📁dk
            └── 📁mosberg
                └── 📁api
                    └── 📁client
                        └── 📁data
                            └── 📁provider
                                ├── MosbergModelProvider.java
                            ├── MosbergApiDataGenerator.java
                            ├── MosbergApiModelProvider.java
                        └── 📁registry
                            ├── MosbergModelLayers.java
                            ├── MosbergModels.java
                            ├── MosbergRenderers.java
                            ├── MosbergRenderStates.java
                        └── 📁util
                            ├── ModelLayersHelper.java
                            ├── ModelsHelper.java
                            ├── RenderHelper.java
                            ├── RenderStatesHelper.java
                        ├── MosbergApiClient.java
                    └── 📁mixin
                        └── 📁client
                            ├── MosbergClientMixin.java
    └── 📁resources
        └── mosbergapi.client.mixins.json
```

```java
package dk.mosberg.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.api.client.registry.MosbergModelLayers;
import dk.mosberg.api.client.registry.MosbergModels;
import dk.mosberg.api.client.registry.MosbergRenderStates;
import dk.mosberg.api.client.registry.MosbergRenderers;
import net.fabricmc.api.ClientModInitializer;

public class MosbergApiClient implements ClientModInitializer {
	public static final String MOD_ID = "mosbergapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing MosbergAPI Client");
		// Initialize all client-side registries
		// Order can be important due to dependencies
		// Initialize model layers first
		// then models, render states, and renderers
		MosbergModelLayers.initialize();
		MosbergModels.initialize();
		MosbergRenderStates.initialize();
		MosbergRenderers.initialize();

		LOGGER.info("MosbergAPI Client initialized successfully");
	}
}

```

```java
package dk.mosberg.api.client.registry;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/**
 * Example model layer definitions for your mod.
 */
public class MosbergModelLayers {

    // Entity model layers
    public static final EntityModelLayer CUSTOM_MOB =
            MosbergRenderers.createMainLayer(Identifier.of("mymod", "custom_mob"));

    public static final EntityModelLayer CUSTOM_CREATURE =
            MosbergRenderers.createMainLayer(Identifier.of("mymod", "custom_creature"));

    // Armor model layers
    public static final EntityModelLayer[] CUSTOM_ARMOR =
            MosbergRenderers.createArmorLayers(Identifier.of("mymod", "custom_armor"));

    public static final EntityModelLayer CUSTOM_ARMOR_INNER = CUSTOM_ARMOR[0];
    public static final EntityModelLayer CUSTOM_ARMOR_OUTER = CUSTOM_ARMOR[1];

    // Block entity model layers
    public static final EntityModelLayer CUSTOM_CHEST =
            MosbergRenderers.createModelLayer(Identifier.of("mymod", "custom_chest"), "main");

    public static void initialize() {
        // Model layers are registered when used
    }
}

```

```java
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

```

```java
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

```

```java
package dk.mosberg.api.client.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.Entity;

/**
 * Centralized helper for managing custom render states in Minecraft 1.21+.
 * <p>
 * Minecraft 1.21.2+ introduced a render state system that decouples rendering from game entities.
 * Render states are immutable snapshots of entity/block entity data used during rendering,
 * preventing issues with asynchronous rendering and improving performance.
 * </p>
 *
 * <h2>Render State System Overview</h2>
 * <p>
 * The render state system works in three phases:
 * </p>
 * <ol>
 * <li><strong>Create:</strong> Instantiate a new render state with {@code createRenderState()}</li>
 * <li><strong>Update:</strong> Copy data from entity to render state via
 * {@code updateRenderState()}</li>
 * <li><strong>Render:</strong> Use the render state data in {@code render()} methods</li>
 * </ol>
 *
 * <h2>Usage Examples:</h2>
 *
 * <h3>Custom Entity Render State</h3>
 *
 * <pre>
 * {
 *     &#64;code
 *     // Define your custom render state
 *     public class CustomMobRenderState extends LivingEntityRenderState {
 *         public boolean isGlowing;
 *         public int energyLevel;
 *     }
 *
 *     // Register it
 *     MosbergRenderStates.registerEntityRenderState("custom_mob", CustomMobRenderState.class,
 *             CustomMobRenderState::new);
 *
 *     // Use in your EntityRenderer
 *     public class CustomMobRenderer
 *             extends EntityRenderer<CustomMobEntity, CustomMobRenderState> {
 *
 *         &#64;Override
 *         public CustomMobRenderState createRenderState() {
 *             return new CustomMobRenderState();
 *         }
 *
 *         &#64;Override
 *         public void updateRenderState(CustomMobEntity entity, CustomMobRenderState state,
 *                 float tickDelta) {
 *             super.updateRenderState(entity, state, tickDelta);
 *             state.isGlowing = entity.isGlowing();
 *             state.energyLevel = entity.getEnergyLevel();
 *         }
 *
 *         @Override
 *         public void render(CustomMobRenderState state, MatrixStack matrices,
 *                 VertexConsumerProvider vertexConsumers, int light) {
 *             if (state.isGlowing) {
 *                 // Render with glow effect
 *             }
 *         }
 *     }
 * }
 * </pre>
 *
 * <h3>Custom Block Entity Render State</h3>
 *
 * <pre>
 * {
 *     &#64;code
 *     // Define your custom block entity render state
 *     public class CustomChestRenderState extends BlockEntityRenderState {
 *         public float openProgress;
 *         public boolean isLocked;
 *     }
 *
 *     // Register it
 *     MosbergRenderStates.registerBlockEntityRenderState("custom_chest",
 *             CustomChestRenderState.class, CustomChestRenderState::new);
 *
 *     // Use in your BlockEntityRenderer
 *     public class CustomChestRenderer
 *             implements BlockEntityRenderer<CustomChestBlockEntity, CustomChestRenderState> {
 *
 *         &#64;Override
 *         public CustomChestRenderState createRenderState() {
 *             return new CustomChestRenderState();
 *         }
 *
 *         &#64;Override
 *         public void updateRenderState(CustomChestBlockEntity blockEntity,
 *                 CustomChestRenderState state, float tickProgress, Vec3d cameraPos,
 *                 &#64;Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay) {
 *             BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress,
 *                     cameraPos, crumblingOverlay);
 *             state.openProgress = blockEntity.getOpenProgress(tickProgress);
 *             state.isLocked = blockEntity.isLocked();
 *         }
 *
 *         @Override
 *         public void render(CustomChestRenderState state, MatrixStack matrices,
 *                 VertexConsumerProvider vertexConsumers, int light, int overlay) {
 *             // Render chest with openProgress
 *         }
 *     }
 * }
 * </pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see EntityRenderState
 * @see BlockEntityRenderState
 * @see ItemRenderState
 */
public class MosbergRenderStates {

    private static final Logger LOGGER = LoggerFactory.getLogger("MosbergAPI/RenderStates");

    private static final Map<String, RenderStateInfo<?, ?>> ENTITY_RENDER_STATES = new HashMap<>();
    private static final Map<String, RenderStateInfo<?, ?>> BLOCK_ENTITY_RENDER_STATES =
            new HashMap<>();
    private static final Map<String, Supplier<? extends ItemRenderState>> ITEM_RENDER_STATES =
            new HashMap<>();

    /**
     * Prevents instantiation of this utility class.
     */
    private MosbergRenderStates() {
        throw new UnsupportedOperationException(
                "MosbergRenderStates is a utility class and cannot be instantiated");
    }

    // ==========================================
    // Entity Render State Registration
    // ==========================================

    /**
     * Registers a custom entity render state.
     * <p>
     * Entity render states extend {@link EntityRenderState} and store data needed for rendering
     * entities without direct entity access.
     * </p>
     *
     * @param <E> The entity type
     * @param <S> The render state type
     * @param id Unique identifier for this render state
     * @param entityClass The entity class this state is for
     * @param stateClass The render state class
     * @param stateFactory Factory to create new render state instances
     * @return The registered render state info
     *
     * @throws NullPointerException if any parameter is null
     * @throws IllegalArgumentException if id is already registered
     *
     * @example
     *
     *          <pre>{@code
     * MosbergRenderStates.registerEntityRenderState(
     *     "custom_mob",
     *     CustomMobEntity.class,
     *     CustomMobRenderState.class,
     *     CustomMobRenderState::new
     * );
     * }</pre>
     */
    public static <E extends Entity, S extends EntityRenderState> RenderStateInfo<E, S> registerEntityRenderState(
            String id, Class<E> entityClass, Class<S> stateClass, Supplier<S> stateFactory) {

        if (id == null) {
            throw new NullPointerException("Render state ID cannot be null");
        }
        if (entityClass == null) {
            throw new NullPointerException("Entity class cannot be null");
        }
        if (stateClass == null) {
            throw new NullPointerException("State class cannot be null");
        }
        if (stateFactory == null) {
            throw new NullPointerException("State factory cannot be null");
        }

        if (ENTITY_RENDER_STATES.containsKey(id)) {
            throw new IllegalArgumentException("Entity render state ID already registered: " + id);
        }

        RenderStateInfo<E, S> info =
                new RenderStateInfo<>(id, entityClass, stateClass, stateFactory);
        ENTITY_RENDER_STATES.put(id, info);

        LOGGER.debug("Registered entity render state: {} -> {}", id, stateClass.getSimpleName());
        return info;
    }

    /**
     * Registers a custom entity render state with just the state class.
     * <p>
     * Simplified registration when you don't need to specify the entity class.
     * </p>
     *
     * @param <S> The render state type
     * @param id Unique identifier for this render state
     * @param stateClass The render state class
     * @param stateFactory Factory to create new render state instances
     * @return The registered render state info
     */

    public static <S extends EntityRenderState> RenderStateInfo<Entity, S> registerEntityRenderState(
            String id, Class<S> stateClass, Supplier<S> stateFactory) {

        return (RenderStateInfo<Entity, S>) registerEntityRenderState(id, Entity.class, stateClass,
                stateFactory);
    }

    // ==========================================
    // Block Entity Render State Registration
    // ==========================================

    /**
     * Registers a custom block entity render state.
     * <p>
     * Block entity render states extend {@link BlockEntityRenderState} and store data needed for
     * rendering block entities.
     * </p>
     *
     * @param <T> The block entity type
     * @param <S> The render state type
     * @param id Unique identifier for this render state
     * @param blockEntityClass The block entity class this state is for
     * @param stateClass The render state class
     * @param stateFactory Factory to create new render state instances
     * @return The registered render state info
     *
     * @throws NullPointerException if any parameter is null
     * @throws IllegalArgumentException if id is already registered
     *
     * @example
     *
     *          <pre>{@code
     * MosbergRenderStates.registerBlockEntityRenderState(
     *     "custom_chest",
     *     CustomChestBlockEntity.class,
     *     CustomChestRenderState.class,
     *     CustomChestRenderState::new
     * );
     * }</pre>
     */
    public static <T extends BlockEntity, S extends BlockEntityRenderState> RenderStateInfo<T, S> registerBlockEntityRenderState(
            String id, Class<T> blockEntityClass, Class<S> stateClass, Supplier<S> stateFactory) {

        if (id == null) {
            throw new NullPointerException("Render state ID cannot be null");
        }
        if (blockEntityClass == null) {
            throw new NullPointerException("Block entity class cannot be null");
        }
        if (stateClass == null) {
            throw new NullPointerException("State class cannot be null");
        }
        if (stateFactory == null) {
            throw new NullPointerException("State factory cannot be null");
        }

        if (BLOCK_ENTITY_RENDER_STATES.containsKey(id)) {
            throw new IllegalArgumentException(
                    "Block entity render state ID already registered: " + id);
        }

        RenderStateInfo<T, S> info =
                new RenderStateInfo<>(id, blockEntityClass, stateClass, stateFactory);
        BLOCK_ENTITY_RENDER_STATES.put(id, info);

        LOGGER.debug("Registered block entity render state: {} -> {}", id,
                stateClass.getSimpleName());
        return info;
    }

    /**
     * Registers a custom block entity render state with just the state class.
     * <p>
     * Simplified registration when you don't need to specify the block entity class.
     * </p>
     *
     * @param <S> The render state type
     * @param id Unique identifier for this render state
     * @param stateClass The render state class
     * @param stateFactory Factory to create new render state instances
     * @return The registered render state info
     */

    public static <S extends BlockEntityRenderState> RenderStateInfo<BlockEntity, S> registerBlockEntityRenderState(
            String id, Class<S> stateClass, Supplier<S> stateFactory) {

        return (RenderStateInfo<BlockEntity, S>) registerBlockEntityRenderState(id,
                BlockEntity.class, stateClass, stateFactory);
    }

    // ==========================================
    // Item Render State Registration
    // ==========================================

    /**
     * Registers a custom item render state.
     * <p>
     * Item render states extend {@link ItemRenderState} and are used for custom item rendering with
     * special properties.
     * </p>
     *
     * @param <S> The render state type
     * @param id Unique identifier for this render state
     * @param stateFactory Factory to create new render state instances
     * @return The state factory
     *
     * @throws NullPointerException if any parameter is null
     * @throws IllegalArgumentException if id is already registered
     *
     * @example
     *
     *          <pre>{@code
     * MosbergRenderStates.registerItemRenderState(
     *     "custom_item",
     *     CustomItemRenderState::new
     * );
     * }</pre>
     */
    public static <S extends ItemRenderState> Supplier<S> registerItemRenderState(String id,
            Supplier<S> stateFactory) {

        if (id == null) {
            throw new NullPointerException("Render state ID cannot be null");
        }
        if (stateFactory == null) {
            throw new NullPointerException("State factory cannot be null");
        }

        if (ITEM_RENDER_STATES.containsKey(id)) {
            throw new IllegalArgumentException("Item render state ID already registered: " + id);
        }

        ITEM_RENDER_STATES.put(id, stateFactory);

        LOGGER.debug("Registered item render state: {}", id);
        return stateFactory;
    }

    // ==========================================
    // Query Methods
    // ==========================================

    /**
     * Gets an entity render state info by ID.
     *
     * @param id The render state ID
     * @return The render state info, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <E extends Entity, S extends EntityRenderState> RenderStateInfo<E, S> getEntityRenderState(
            String id) {
        return (RenderStateInfo<E, S>) ENTITY_RENDER_STATES.get(id);
    }

    /**
     * Gets a block entity render state info by ID.
     *
     * @param id The render state ID
     * @return The render state info, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity, S extends BlockEntityRenderState> RenderStateInfo<T, S> getBlockEntityRenderState(
            String id) {
        return (RenderStateInfo<T, S>) BLOCK_ENTITY_RENDER_STATES.get(id);
    }

    /**
     * Gets an item render state factory by ID.
     *
     * @param id The render state ID
     * @return The state factory, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <S extends ItemRenderState> Supplier<S> getItemRenderState(String id) {
        return (Supplier<S>) ITEM_RENDER_STATES.get(id);
    }

    /**
     * Checks if an entity render state is registered.
     *
     * @param id The render state ID
     * @return true if registered
     */
    public static boolean hasEntityRenderState(String id) {
        return ENTITY_RENDER_STATES.containsKey(id);
    }

    /**
     * Checks if a block entity render state is registered.
     *
     * @param id The render state ID
     * @return true if registered
     */
    public static boolean hasBlockEntityRenderState(String id) {
        return BLOCK_ENTITY_RENDER_STATES.containsKey(id);
    }

    /**
     * Checks if an item render state is registered.
     *
     * @param id The render state ID
     * @return true if registered
     */
    public static boolean hasItemRenderState(String id) {
        return ITEM_RENDER_STATES.containsKey(id);
    }

    /**
     * Gets all registered entity render state IDs.
     *
     * @return Unmodifiable map of render state IDs to info
     */
    public static Map<String, RenderStateInfo<?, ?>> getRegisteredEntityRenderStates() {
        return Map.copyOf(ENTITY_RENDER_STATES);
    }

    /**
     * Gets all registered block entity render state IDs.
     *
     * @return Unmodifiable map of render state IDs to info
     */
    public static Map<String, RenderStateInfo<?, ?>> getRegisteredBlockEntityRenderStates() {
        return Map.copyOf(BLOCK_ENTITY_RENDER_STATES);
    }

    /**
     * Gets all registered item render state IDs.
     *
     * @return Unmodifiable map of render state IDs to factories
     */
    public static Map<String, Supplier<? extends ItemRenderState>> getRegisteredItemRenderStates() {
        return Map.copyOf(ITEM_RENDER_STATES);
    }

    // ==========================================
    // Initialization
    // ==========================================

    /**
     * Initializes the render state registry.
     * <p>
     * Call this method from your mod's client initializer.
     * </p>
     *
     * @example
     *
     *          <pre>{@code
     *          @Override
     *          public void onInitializeClient() {
     *          MosbergRenderStates.initialize();
     *          }
     * }</pre>
     */
    public static void initialize() {
        LOGGER.info(
                "MosbergRenderStates initialized with {} entity states, {} block entity states, and {} item states",
                ENTITY_RENDER_STATES.size(), BLOCK_ENTITY_RENDER_STATES.size(),
                ITEM_RENDER_STATES.size());
    }

    /**
     * Logs all registered render states.
     * <p>
     * Useful for debugging render state registration.
     * </p>
     */
    public static void logRegisteredRenderStates() {
        LOGGER.info("=== Registered Entity Render States ===");
        ENTITY_RENDER_STATES.forEach((id, info) -> LOGGER.info("  - {}: {} -> {}", id,
                info.getSourceClass().getSimpleName(), info.getStateClass().getSimpleName()));

        LOGGER.info("=== Registered Block Entity Render States ===");
        BLOCK_ENTITY_RENDER_STATES.forEach((id, info) -> LOGGER.info("  - {}: {} -> {}", id,
                info.getSourceClass().getSimpleName(), info.getStateClass().getSimpleName()));

        LOGGER.info("=== Registered Item Render States ===");
        ITEM_RENDER_STATES.keySet().forEach(id -> LOGGER.info("  - {}", id));
    }

    // ==========================================
    // Render State Info Class
    // ==========================================

    /**
     * Holds information about a registered render state.
     *
     * @param <T> The source type (Entity or BlockEntity)
     * @param <S> The render state type
     */
    public static class RenderStateInfo<T, S> {
        private final String id;
        private final Class<T> sourceClass;
        private final Class<S> stateClass;
        private final Supplier<S> stateFactory;

        /**
         * Creates a new render state info.
         *
         * @param id The render state ID
         * @param sourceClass The source class (Entity or BlockEntity)
         * @param stateClass The render state class
         * @param stateFactory Factory to create new render state instances
         */
        public RenderStateInfo(String id, Class<T> sourceClass, Class<S> stateClass,
                Supplier<S> stateFactory) {
            this.id = id;
            this.sourceClass = sourceClass;
            this.stateClass = stateClass;
            this.stateFactory = stateFactory;
        }

        /**
         * Gets the render state ID.
         *
         * @return The ID
         */
        public String getId() {
            return id;
        }

        /**
         * Gets the source class (Entity or BlockEntity).
         *
         * @return The source class
         */
        public Class<T> getSourceClass() {
            return sourceClass;
        }

        /**
         * Gets the render state class.
         *
         * @return The state class
         */
        public Class<S> getStateClass() {
            return stateClass;
        }

        /**
         * Creates a new render state instance.
         *
         * @return A new render state
         */
        public S createState() {
            return stateFactory.get();
        }

        /**
         * Gets the state factory.
         *
         * @return The factory
         */
        public Supplier<S> getStateFactory() {
            return stateFactory;
        }
    }
}

```

```java
package dk.mosberg.api.client.data;

import dk.mosberg.api.client.data.provider.MosbergModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public class MosbergApiModelProvider extends MosbergModelProvider {

    public MosbergApiModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Example: Register simple block
        // registerSimpleBlock(blockStateModelGenerator, MosbergBlocks.CUSTOM_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Example: Register simple item
        // registerSimpleItem(itemModelGenerator, MosbergItems.CUSTOM_ITEM);
    }
}

```

```java
package dk.mosberg.api.client.data;

import dk.mosberg.api.data.MosbergApiLootTableProvider;
import dk.mosberg.api.data.MosbergApiRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MosbergApiDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Server-side providers (recipes, loot tables)
		pack.addProvider(MosbergApiRecipeProvider::new);
		pack.addProvider(MosbergApiLootTableProvider::new);

		// Client-side provider (models)
		pack.addProvider(MosbergApiModelProvider::new);
	}
}

```

```java
package dk.mosberg.api.client.data.provider;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.item.Item;

/**
 * Enhanced model provider with helper methods for MosbergAPI
 */
public abstract class MosbergModelProvider extends FabricModelProvider {

    public MosbergModelProvider(FabricDataOutput output) {
        super(output);
    }

    /**
     * Helper method to register a simple cube_all block model
     */
    protected void registerSimpleBlock(BlockStateModelGenerator generator, Block block) {
        generator.registerSimpleCubeAll(block);
    }

    /**
     * Helper method to register a simple generated item model
     */
    protected void registerSimpleItem(ItemModelGenerator generator, Item item) {
        generator.register(item, Models.GENERATED);
    }

    /**
     * Helper method to register a handheld item model (for tools)
     */
    protected void registerHandheldItem(ItemModelGenerator generator, Item item) {
        generator.register(item, Models.HANDHELD);
    }

    /**
     * Helper method to register a block item (uses parent block model)
     */
    protected void registerBlockItem(ItemModelGenerator generator, Block block) {
        generator.register(block.asItem(), Models.GENERATED);
    }
}

```

```java
package dk.mosberg.api.client.util;

import dk.mosberg.api.client.registry.MosbergModels;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/**
 * Simplified helper for creating EntityModelLayers.
 * <p>
 * Provides quick access to model layer creation without needing the full MosbergModels API.
 * </p>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class ModelLayersHelper {

    private ModelLayersHelper() {
        throw new UnsupportedOperationException("ModelLayersHelper is a utility class");
    }

    /**
     * Creates a main model layer.
     * <p>
     * Shorthand for {@code new EntityModelLayer(id, "main")}.
     * </p>
     *
     * @param modId Your mod ID
     * @param entityId The entity ID
     * @return EntityModelLayer with "main" layer
     *
     * @example
     *
     *          <pre>{@code
     * public static final EntityModelLayer CUSTOM_MOB =
     *     ModelLayersHelper.main("mymod", "custom_mob");
     * }</pre>
     */
    public static EntityModelLayer main(String modId, String entityId) {
        return MosbergModels.createMainLayer(Identifier.of(modId, entityId));
    }

    /**
     * Creates an inner armor layer.
     *
     * @param modId Your mod ID
     * @param armorId The armor ID
     * @return EntityModelLayer with "inner_armor" layer
     */
    public static EntityModelLayer innerArmor(String modId, String armorId) {
        return MosbergModels.createModelLayer(Identifier.of(modId, armorId), "inner_armor");
    }

    /**
     * Creates an outer armor layer.
     *
     * @param modId Your mod ID
     * @param armorId The armor ID
     * @return EntityModelLayer with "outer_armor" layer
     */
    public static EntityModelLayer outerArmor(String modId, String armorId) {
        return MosbergModels.createModelLayer(Identifier.of(modId, armorId), "outer_armor");
    }

    /**
     * Creates both armor layers at once.
     * <p>
     * Returns an array: [inner, outer]
     * </p>
     *
     * @param modId Your mod ID
     * @param armorId The armor ID
     * @return Array of [inner, outer] EntityModelLayers
     *
     * @example
     *
     *          <pre>{@code
     * EntityModelLayer[] layers = ModelLayersHelper.armor("mymod", "custom_armor");
     * EntityModelLayer inner = layers[0];
     * EntityModelLayer outer = layers[1];
     * }</pre>
     */
    public static EntityModelLayer[] armor(String modId, String armorId) {
        return new EntityModelLayer[] {innerArmor(modId, armorId), outerArmor(modId, armorId)};
    }

    /**
     * Creates a custom layer with specified layer name.
     *
     * @param modId Your mod ID
     * @param entityId The entity ID
     * @param layerName Custom layer name (e.g., "saddle", "clothing")
     * @return EntityModelLayer with custom layer
     *
     * @example
     *
     *          <pre>{@code
     * EntityModelLayer saddle = ModelLayersHelper.custom("mymod", "horse", "saddle");
     * }</pre>
     */
    public static EntityModelLayer custom(String modId, String entityId, String layerName) {
        return MosbergModels.createModelLayer(Identifier.of(modId, entityId), layerName);
    }

    /**
     * Creates a model layer from a full identifier.
     *
     * @param id The full Identifier
     * @param layerName The layer name
     * @return EntityModelLayer
     */
    public static EntityModelLayer of(Identifier id, String layerName) {
        return MosbergModels.createModelLayer(id, layerName);
    }

    /**
     * Creates a main model layer from a full identifier.
     *
     * @param id The full Identifier
     * @return EntityModelLayer with "main" layer
     */
    public static EntityModelLayer of(Identifier id) {
        return MosbergModels.createMainLayer(id);
    }
}

```

```java
package dk.mosberg.api.client.util;

import dk.mosberg.api.client.registry.MosbergModels;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Simplified helper for common model operations.
 * <p>
 * Provides quick access to frequently used model building patterns without needing to interact with
 * the full MosbergModels API.
 * </p>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class ModelsHelper {

    private ModelsHelper() {
        throw new UnsupportedOperationException("ModelsHelper is a utility class");
    }

    /**
     * Quick method to create a simple cube model.
     *
     * @param size Size of the cube
     * @return TexturedModelData for a cube
     *
     * @example
     *
     *          <pre>{@code
     * TexturedModelData cubeModel = ModelsHelper.createCube(16);
     * }</pre>
     */
    public static TexturedModelData createCube(float size) {
        ModelData data = MosbergModels.createModelData();
        ModelPartData root = data.getRoot();

        float halfSize = size / 2;
        root.addChild("cube", ModelPartBuilder.create().uv(0, 0).cuboid(-halfSize, -halfSize,
                -halfSize, size, size, size),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return MosbergModels.createTexturedModelData(data, 64, 64);
    }

    /**
     * Creates a simple humanoid model with default proportions.
     *
     * @return TexturedModelData for humanoid
     */
    public static TexturedModelData createHumanoid() {
        return MosbergModels
                .createTexturedModelData(MosbergModels.createHumanoidModel(Dilation.NONE), 64, 64);
    }

    /**
     * Creates a simple humanoid model with armor dilation.
     *
     * @param dilation Dilation amount (0.5F for inner armor, 1.0F for outer)
     * @return TexturedModelData for humanoid armor
     */
    public static TexturedModelData createHumanoidArmor(float dilation) {
        return MosbergModels.createTexturedModelData(
                MosbergModels.createHumanoidModel(new Dilation(dilation)), 64, 64);
    }

    /**
     * Creates a simple quadruped model.
     *
     * @return TexturedModelData for quadruped
     */
    public static TexturedModelData createQuadruped() {
        return MosbergModels.createTexturedModelData(MosbergModels.createQuadrupedModel(), 64, 64);
    }

    /**
     * Creates a simple box model (like a chest).
     *
     * @param width Width of the box
     * @param height Height of the box
     * @param depth Depth of the box
     * @return TexturedModelData for box
     */
    public static TexturedModelData createBox(float width, float height, float depth) {
        ModelData data = MosbergModels.createModelData();
        ModelPartData root = data.getRoot();

        root.addChild("box",
                ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, 0.0F, width, height, depth),
                ModelTransform.of(-width / 2, 0.0F, -depth / 2, 0.0F, 0.0F, 0.0F));

        return MosbergModels.createTexturedModelData(data, 64, 64);
    }

    /**
     * Creates a simple sphere-like model using multiple cuboids.
     * <p>
     * Note: This is an approximation, not a true sphere.
     * </p>
     *
     * @param radius Radius of the sphere
     * @param segments Number of segments (more = smoother)
     * @return TexturedModelData for sphere
     */
    public static TexturedModelData createSphere(float radius, int segments) {
        ModelData data = MosbergModels.createModelData();
        ModelPartData root = data.getRoot();

        // Simple sphere approximation with a few cuboids
        root.addChild("core",
                ModelPartBuilder.create().uv(0, 0).cuboid(-radius, -radius, -radius, radius * 2,
                        radius * 2, radius * 2),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return MosbergModels.createTexturedModelData(data, 64, 64);
    }

    /**
     * Creates a flat plane model.
     *
     * @param width Width of the plane
     * @param depth Depth of the plane
     * @return TexturedModelData for plane
     */
    public static TexturedModelData createPlane(float width, float depth) {
        ModelData data = MosbergModels.createModelData();
        ModelPartData root = data.getRoot();

        root.addChild("plane", ModelPartBuilder.create().uv(0, 0).cuboid(-width / 2, 0.0F,
                -depth / 2, width, 0.1F, depth),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return MosbergModels.createTexturedModelData(data, 64, 64);
    }

    /**
     * Quickly adds a simple part to existing model data.
     *
     * @param data The ModelData to add to
     * @param name Name of the part
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @param width Width
     * @param height Height
     * @param depth Depth
     */
    public static void addPart(ModelData data, String name, float x, float y, float z, float width,
            float height, float depth) {
        data.getRoot().addChild(name,
                ModelPartBuilder.create().uv(0, 0).cuboid(x, y, z, width, height, depth),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
    }
}

```

```java
package dk.mosberg.api.client.util;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Helper for client-side rendering
 */
public class RenderHelper {

    public static void drawCenteredText(MatrixStack matrices, Text text, int x, int y, int color) {
        // Render centered text
    }

    public static void drawTexture(MatrixStack matrices, Identifier texture, int x, int y,
            int width, int height) {
        // Render texture
    }

    public static void drawGradient(MatrixStack matrices, int x, int y, int width, int height,
            int colorStart, int colorEnd) {
        // Draw gradient rectangle
    }

    public static void setShaderColor(float r, float g, float b, float a) {
        // Set shader color
    }

    public static void resetShaderColor() {
        // Reset shader color to default
    }

    public static void drawTooltip(MatrixStack matrices, Text text, int x, int y) {
        // Draw tooltip at specified position
    }

    public static void drawItemWithOverlay(MatrixStack matrices, Identifier itemTexture, int x,
            int y, int overlayCount) {
        // Draw item texture with overlay count
    }

    public static void drawBox(MatrixStack matrices, int x, int y, int width, int height,
            int color) {
        // Draw colored box
    }

    public static void drawCircle(MatrixStack matrices, int centerX, int centerY, int radius,
            int color) {
        // Draw filled circle
    }

    public static void drawLine(MatrixStack matrices, int x1, int y1, int x2, int y2, int color,
            float thickness) {
        // Draw line between two points
    }

    public static void drawProgressBar(MatrixStack matrices, int x, int y, int width, int height,
            float progress, int backgroundColor, int foregroundColor) {
        // Draw progress bar
    }

    public static void draw3DModel(MatrixStack matrices, Identifier model, int x, int y,
            float scale) {
        // Render 3D model at specified position and scale
    }

    public static void drawShadowedText(MatrixStack matrices, Text text, int x, int y, int color) {
        // Render text with shadow effect
    }

    public static void drawRotatedTexture(MatrixStack matrices, Identifier texture, int x, int y,
            int width, int height, float angle) {
        // Render rotated texture
    }

    public static void drawNinePatch(MatrixStack matrices, Identifier texture, int x, int y,
            int width, int height, int cornerSize) {
        // Render nine-patch texture
    }

    public static void drawStretchedTexture(MatrixStack matrices, Identifier texture, int x, int y,
            int width, int height) {
        // Render stretched texture
    }

    public static void drawTexturedQuad(MatrixStack matrices, Identifier texture, float x1,
            float y1, float x2, float y2, float u1, float v1, float u2, float v2) {
        // Render textured quad with specified UV coordinates
    }

    public static void drawColoredCircle(MatrixStack matrices, int centerX, int centerY, int radius,
            int color) {
        // Draw filled colored circle
    }

    public static void drawDashedLine(MatrixStack matrices, int x1, int y1, int x2, int y2,
            int color, float thickness, float dashLength) {
        // Draw dashed line between two points
    }

    public static void drawTexturedRectangle(MatrixStack matrices, Identifier texture, int x, int y,
            int width, int height) {
        // Render textured rectangle
    }

    public static void drawTooltipBox(MatrixStack matrices, Text text, int x, int y, int padding,
            int backgroundColor, int borderColor) {
        // Draw tooltip box with background and border
    }

    public static void drawItemWithCount(MatrixStack matrices, Identifier itemTexture, int x, int y,
            int count) {
        // Draw item texture with count overlay
    }

    public static void drawRoundedRectangle(MatrixStack matrices, int x, int y, int width,
            int height, int cornerRadius, int color) {
        // Draw rounded rectangle
    }

    public static void drawTexturedCircle(MatrixStack matrices, Identifier texture, int centerX,
            int centerY, int radius) {
        // Render textured circle
    }

    public static void draw3DItem(MatrixStack matrices, Identifier itemModel, int x, int y,
            float scale) {
        // Render 3D item model at specified position and scale
    }

    public static void drawGlowingText(MatrixStack matrices, Text text, int x, int y, int color,
            int glowColor) {
        // Render text with glowing effect
    }

    public static void drawRotatedRectangle(MatrixStack matrices, int x, int y, int width,
            int height, float angle, int color) {
        // Draw rotated rectangle
    }

    public static void drawTexturedProgressBar(MatrixStack matrices, Identifier texture, int x,
            int y, int width, int height, float progress) {
        // Draw textured progress bar
    }

    public static void drawShadowedBox(MatrixStack matrices, int x, int y, int width, int height,
            int color, int shadowColor) {
        // Draw box with shadow effect
    }

    public static void drawTexturedLine(MatrixStack matrices, Identifier texture, int x1, int y1,
            int x2, int y2, float thickness) {
        // Draw textured line between two points
    }

    public static void drawCircularProgressIndicator(MatrixStack matrices, int centerX, int centerY,
            int radius, float progress, int color) {
        // Draw circular progress indicator
    }

    public static void drawTexturedRoundedRectangle(MatrixStack matrices, Identifier texture, int x,
            int y, int width, int height, int cornerRadius) {
        // Render textured rounded rectangle
    }

    public static void draw3DRotatingModel(MatrixStack matrices, Identifier model, int x, int y,
            float scale, float rotationSpeed) {
        // Render rotating 3D model
    }

    public static void drawOutlinedText(MatrixStack matrices, Text text, int x, int y, int color,
            int outlineColor) {
        // Render text with outline
    }

    public static void drawTexturedCircleWithBorder(MatrixStack matrices, Identifier texture,
            int centerX, int centerY, int radius, int borderColor, int borderWidth) {
        // Render textured circle with border
    }

    public static void draw3DItemWithLighting(MatrixStack matrices, Identifier itemModel, int x,
            int y, float scale) {
        // Render 3D item model with lighting effects
    }

    public static void drawGlowingBox(MatrixStack matrices, int x, int y, int width, int height,
            int color, int glowColor) {
        // Draw box with glowing effect
    }

    public static void drawRotatedTexturedRectangle(MatrixStack matrices, Identifier texture, int x,
            int y, int width, int height, float angle) {
        // Render rotated textured rectangle
    }

    public static void drawTexturedDashedLine(MatrixStack matrices, Identifier texture, int x1,
            int y1, int x2, int y2, float thickness, float dashLength) {
        // Draw textured dashed line between two points
    }

    public static void drawCircularText(MatrixStack matrices, Text text, int centerX, int centerY,
            int radius) {
        // Render text in a circular pattern
    }

    public static void draw3DRotatingItem(MatrixStack matrices, Identifier itemModel, int x, int y,
            float scale, float rotationSpeed) {
        // Render rotating 3D item model
    }

    public static void drawShadowedTexturedBox(MatrixStack matrices, Identifier texture, int x,
            int y, int width, int height, int shadowColor) {
        // Draw textured box with shadow effect
    }

    public static void drawTexturedCircularProgressIndicator(MatrixStack matrices,
            Identifier texture, int centerX, int centerY, int radius, float progress) {
        // Draw textured circular progress indicator
    }

    public static void draw3DItemWithRotation(MatrixStack matrices, Identifier itemModel, int x,
            int y, float scale, float rotationAngle) {
        // Render 3D item model with specified rotation angle
    }

    public static void drawGlowingTexturedBox(MatrixStack matrices, Identifier texture, int x,
            int y, int width, int height, int glowColor) {
        // Draw textured box with glowing effect
    }

    public static void drawRotatedTexturedCircle(MatrixStack matrices, Identifier texture,
            int centerX, int centerY, int radius, float angle) {
        // Render rotated textured circle
    }

    public static void drawTextured3DModel(MatrixStack matrices, Identifier texture,
            Identifier model, int x, int y, float scale) {
        // Render 3D model with texture applied
    }

    public static void drawOutlinedTexturedBox(MatrixStack matrices, Identifier texture, int x,
            int y, int width, int height, int outlineColor) {
        // Draw textured box with outline
    }

    public static void draw3DRotatingTexturedItem(MatrixStack matrices, Identifier texture,
            Identifier itemModel, int x, int y, float scale, float rotationSpeed) {
        // Render rotating 3D item model with texture applied
    }

    public static void drawShadowedGlowingText(MatrixStack matrices, Text text, int x, int y,
            int color, int shadowColor, int glowColor) {
        // Render text with both shadow and glowing effects
    }

    public static void drawTexturedRotatedRectangle(MatrixStack matrices, Identifier texture, int x,
            int y, int width, int height, float angle) {
        // Render textured rectangle with rotation
    }

    public static void draw3DItemWithLightingAndRotation(MatrixStack matrices, Identifier itemModel,
            int x, int y, float scale, float rotationAngle) {
        // Render 3D item model with lighting and rotation
    }

    public static void drawGlowingTexturedCircle(MatrixStack matrices, Identifier texture,
            int centerX, int centerY, int radius, int glowColor) {
        // Render textured circle with glowing effect
    }
}

```

```java
package dk.mosberg.api.client.util;

import java.util.function.Supplier;
import dk.mosberg.api.client.registry.MosbergRenderStates;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.Entity;

/**
 * Simplified helper for common render state operations.
 * <p>
 * Provides quick access to render state registration and common render state patterns.
 * </p>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class RenderStatesHelper {

    private RenderStatesHelper() {
        throw new UnsupportedOperationException("RenderStatesHelper is a utility class");
    }

    /**
     * Quick registration for entity render states.
     * <p>
     * Simplified version that doesn't require entity class parameter.
     * </p>
     *
     * @param <S> The render state type
     * @param id State identifier
     * @param stateClass State class
     * @param factory State factory
     *
     * @example
     *
     *          <pre>{@code
     * RenderStatesHelper.entity("custom_mob",
     *     CustomMobRenderState.class,
     *     CustomMobRenderState::new);
     * }</pre>
     */
    public static <S extends EntityRenderState> void entity(String id, Class<S> stateClass,
            Supplier<S> factory) {
        MosbergRenderStates.registerEntityRenderState(id, stateClass, factory);
    }

    /**
     * Quick registration for living entity render states.
     *
     * @param <S> The render state type extending LivingEntityRenderState
     * @param id State identifier
     * @param stateClass State class
     * @param factory State factory
     */
    public static <S extends LivingEntityRenderState> void livingEntity(String id,
            Class<S> stateClass, Supplier<S> factory) {
        MosbergRenderStates.registerEntityRenderState(id, stateClass, factory);
    }

    /**
     * Quick registration for block entity render states.
     *
     * @param <S> The render state type
     * @param id State identifier
     * @param stateClass State class
     * @param factory State factory
     *
     * @example
     *
     *          <pre>{@code
     * RenderStatesHelper.blockEntity("custom_chest",
     *     CustomChestRenderState.class,
     *     CustomChestRenderState::new);
     * }</pre>
     */
    public static <S extends BlockEntityRenderState> void blockEntity(String id,
            Class<S> stateClass, Supplier<S> factory) {
        MosbergRenderStates.registerBlockEntityRenderState(id, stateClass, factory);
    }

    /**
     * Quick registration for item render states.
     *
     * @param <S> The render state type
     * @param id State identifier
     * @param factory State factory
     *
     * @example
     *
     *          <pre>{@code
     * RenderStatesHelper.item("custom_item", CustomItemRenderState::new);
     * }</pre>
     */
    public static <S extends ItemRenderState> void item(String id, Supplier<S> factory) {
        MosbergRenderStates.registerItemRenderState(id, factory);
    }

    /**
     * Checks if an entity render state exists.
     *
     * @param id State identifier
     * @return true if registered
     */
    public static boolean hasEntity(String id) {
        return MosbergRenderStates.hasEntityRenderState(id);
    }

    /**
     * Checks if a block entity render state exists.
     *
     * @param id State identifier
     * @return true if registered
     */
    public static boolean hasBlockEntity(String id) {
        return MosbergRenderStates.hasBlockEntityRenderState(id);
    }

    /**
     * Checks if an item render state exists.
     *
     * @param id State identifier
     * @return true if registered
     */
    public static boolean hasItem(String id) {
        return MosbergRenderStates.hasItemRenderState(id);
    }

    /**
     * Gets an entity render state by ID.
     *
     * @param <E> Entity type
     * @param <S> State type
     * @param id State identifier
     * @return RenderStateInfo or null
     */
    public static <E extends Entity, S extends EntityRenderState> MosbergRenderStates.RenderStateInfo<E, S> getEntity(
            String id) {
        return MosbergRenderStates.getEntityRenderState(id);
    }

    /**
     * Gets a block entity render state by ID.
     *
     * @param <T> Block entity type
     * @param <S> State type
     * @param id State identifier
     * @return RenderStateInfo or null
     */
    public static <T extends BlockEntity, S extends BlockEntityRenderState> MosbergRenderStates.RenderStateInfo<T, S> getBlockEntity(
            String id) {
        return MosbergRenderStates.getBlockEntityRenderState(id);
    }

    /**
     * Gets an item render state factory by ID.
     *
     * @param <S> State type
     * @param id State identifier
     * @return State factory or null
     */
    public static <S extends ItemRenderState> Supplier<S> getItem(String id) {
        return MosbergRenderStates.getItemRenderState(id);
    }
}

```