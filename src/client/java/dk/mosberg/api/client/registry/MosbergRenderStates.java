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
