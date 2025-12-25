package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom damage types in MosbergAPI. Register your damage types here using
 * the provided helper methods.
 *
 * <p>
 * Note: In Minecraft 1.21+, damage types are data-driven and registered through JSON files in the
 * data pack system. This registry provides helper methods for creating damage type registry keys
 * that work with the data-driven system.
 *
 * @example
 *
 *          <pre>{@code
 * // Create damage type keys (actual damage types defined in data/mosbergapi/damage_type/)
 * public static final RegistryKey<DamageType> MAGIC = MosbergDamageTypes.of("magic");
 * public static final RegistryKey<DamageType> CURSE = MosbergDamageTypes.of("curse");
 * public static final RegistryKey<DamageType> VOID_DAMAGE = MosbergDamageTypes.of("void_damage");
 *
 * // Usage in code:
 * DamageSource source = new DamageSource(
 *     registries.getOrThrow(RegistryKeys.DAMAGE_TYPE).entryOf(MAGIC),
 *     attacker
 * );
 * entity.damage(world, source, 10.0f);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergDamageTypes {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergDamageTypes.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Creates a registry key for a damage type. The actual damage type definition must exist in
     * data/mosbergapi/damage_type/{name}.json
     *
     * @param name The damage type name (will be prefixed with mod ID)
     * @return The RegistryKey for the damage type
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<DamageType> magic = MosbergDamageTypes.of("magic");
     * // Corresponds to: data/mosbergapi/damage_type/magic.json
     * }</pre>
     */
    public static RegistryKey<DamageType> of(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        LOGGER.debug("Created damage type key: {}", id);
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id);
    }

    /**
     * Creates a damage type identifier for use in data pack JSON files.
     *
     * @param name The damage type name
     * @return The identifier
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = MosbergDamageTypes.id("curse");
     * // Returns: mosbergapi:curse
     * }</pre>
     */
    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    /**
     * Initialize and register all damage types. Call this method from your mod initializer.
     *
     * <p>
     * Note: Damage types in 1.21+ are loaded from data packs. This method exists for consistency
     * with other registry classes but does not register damage types directly. Ensure your damage
     * type JSON files exist in data/mosbergapi/damage_type/
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI damage types (data-driven)");
        // Damage types are loaded from data packs in 1.21+
        // JSON files should be in: data/mosbergapi/damage_type/*.json
    }
}
