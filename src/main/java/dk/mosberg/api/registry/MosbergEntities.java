package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom entities in MosbergAPI. Register your entities here using the
 * provided helper methods.
 */
public class MosbergEntities {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergEntities.class);

    // Example entity registration (uncomment and customize as needed):
    // public static final EntityType<YourEntity> YOUR_ENTITY = register(
    // "your_entity",
    // EntityType.Builder.create(YourEntity::new, SpawnGroup.CREATURE)
    // .dimensions(0.6f, 1.8f)
    // .maxTrackingRange(8)
    // );

    /**
     * Registers an entity type with a simple name.
     *
     * @param name The entity name (will be prefixed with mod ID)
     * @param entityType The EntityType to register
     * @param <T> The entity class type
     * @return The registered EntityType
     */
    public static <T extends Entity> EntityType<T> register(String name, EntityType<T> entityType) {
        LOGGER.debug("Registering entity: {}", name);
        return MosbergRegistries.registerEntityType(name, entityType);
    }

    /**
     * Registers an entity type using an EntityType.Builder. Creates the registry key automatically
     * and builds the entity type.
     *
     * @param name The entity name
     * @param builder The EntityType.Builder to build and register
     * @param <T> The entity class type
     * @return The registered EntityType
     */
    public static <T extends Entity> EntityType<T> register(String name,
            EntityType.Builder<T> builder) {
        // Create the registry key for this entity
        RegistryKey<EntityType<?>> key =
                RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("mosbergapi", name));

        // Build with the registry key
        EntityType<T> entityType = builder.build(key);

        return register(name, entityType);
    }

    /**
     * Creates a registry key for an entity type. Useful for data-driven entity registration.
     *
     * @param name The entity name
     * @return The RegistryKey for the entity type
     */
    public static RegistryKey<EntityType<?>> keyOf(String name) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("mosbergapi", name));
    }

    /**
     * Initialize and register all entities. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI entities");
        // Entity registration happens when the class is loaded
    }
}
