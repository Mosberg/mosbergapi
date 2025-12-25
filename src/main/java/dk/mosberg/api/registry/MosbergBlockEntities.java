package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom block entities in MosbergAPI. Register your block entities here
 * using the provided helper methods.
 *
 * @example
 * 
 *          <pre>{@code
 * public static final BlockEntityType<CustomBlockEntity> CUSTOM_BE = register("custom_be",
 *     FabricBlockEntityTypeBuilder.create(CustomBlockEntity::new, MosbergBlocks.CUSTOM_BLOCK)
 *         .build());
 *
 * public static final BlockEntityType<StorageBlockEntity> STORAGE_BE = register("storage_be",
 *     CustomBlockEntity::new,
 *     MosbergBlocks.CHEST_BLOCK,
 *     MosbergBlocks.BARREL_BLOCK);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergBlockEntities {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergBlockEntities.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers a block entity type with a simple name.
     *
     * @param name The block entity name (will be prefixed with mod ID)
     * @param blockEntityType The BlockEntityType to register
     * @param <T> The block entity class type
     * @return The registered BlockEntityType
     *
     * @example
     * 
     *          <pre>{@code
     * BlockEntityType<FurnaceBlockEntity> type = MosbergBlockEntities.register("custom_furnace",
     *     FabricBlockEntityTypeBuilder.create(FurnaceBlockEntity::new, MosbergBlocks.FURNACE)
     *         .build());
     * }</pre>
     */
    public static <T extends BlockEntity> BlockEntityType<T> register(String name,
            BlockEntityType<T> blockEntityType) {
        Identifier id = Identifier.of(MOD_ID, name);
        BlockEntityType<T> registered =
                Registry.register(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType);
        LOGGER.debug("Registered block entity type: {}", name);
        return registered;
    }

    /**
     * Registers a block entity type using a factory and supported blocks.
     *
     * @param name The block entity name
     * @param factory The BlockEntity factory
     * @param blocks The blocks that use this block entity
     * @param <T> The block entity class type
     * @return The registered BlockEntityType
     *
     * @example
     * 
     *          <pre>{@code
     * BlockEntityType<ChestBlockEntity> chest = MosbergBlockEntities.register("custom_chest",
     *     ChestBlockEntity::new,
     *     MosbergBlocks.OAK_CHEST,
     *     MosbergBlocks.BIRCH_CHEST);
     * }</pre>
     */
    public static <T extends BlockEntity> BlockEntityType<T> register(String name,
            FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory, blocks).build();
        return register(name, type);
    }

    /**
     * Registers a block entity type with a single block.
     *
     * @param name The block entity name
     * @param factory The BlockEntity factory
     * @param block The block that uses this block entity
     * @param <T> The block entity class type
     * @return The registered BlockEntityType
     *
     * @example
     * 
     *          <pre>{@code
     * BlockEntityType<BeaconBlockEntity> beacon = MosbergBlockEntities.registerSingle(
     *     "custom_beacon",
     *     BeaconBlockEntity::new,
     *     MosbergBlocks.BEACON);
     * }</pre>
     */
    public static <T extends BlockEntity> BlockEntityType<T> registerSingle(String name,
            FabricBlockEntityTypeBuilder.Factory<T> factory, Block block) {
        return register(name, factory, block);
    }

    /**
     * Initialize and register all block entities. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI block entities");
        // Block entity registration happens when register() is called
    }
}
