package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom enchantments in MosbergAPI. Register your enchantments here using
 * the provided helper methods.
 *
 * <p>
 * Note: In Minecraft 1.21+, enchantments are data-driven and registered through JSON files in the
 * data pack system. This registry provides helper methods for creating enchantment registry keys
 * and references that work with the data-driven system.
 *
 * @example
 *
 *          <pre>{@code
 * // Create enchantment keys (actual enchantments defined in data/mosbergapi/enchantment/)
 * public static final RegistryKey<Enchantment> SOUL_BOUND = MosbergEnchantments.of("soul_bound");
 * public static final RegistryKey<Enchantment> AUTO_SMELT = MosbergEnchantments.of("auto_smelt");
 * public static final RegistryKey<Enchantment> TELEKINESIS = MosbergEnchantments.of("telekinesis");
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergEnchantments {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergEnchantments.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Creates a registry key for an enchantment. The actual enchantment definition must exist in
     * data/mosbergapi/enchantment/{name}.json
     *
     * @param name The enchantment name (will be prefixed with mod ID)
     * @return The RegistryKey for the enchantment
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<Enchantment> soulbound = MosbergEnchantments.of("soul_bound");
     * // Corresponds to: data/mosbergapi/enchantment/soul_bound.json
     * }</pre>
     */
    public static RegistryKey<Enchantment> of(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        LOGGER.debug("Created enchantment key: {}", id);
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }

    /**
     * Creates an enchantment identifier for use in data pack JSON files.
     *
     * @param name The enchantment name
     * @return The identifier
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = MosbergEnchantments.id("soul_bound");
     * // Returns: mosbergapi:soul_bound
     * }</pre>
     */
    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    /**
     * Initialize and register all enchantments. Call this method from your mod initializer.
     *
     * <p>
     * Note: Enchantments in 1.21+ are loaded from data packs. This method exists for consistency
     * with other registry classes but does not register enchantments directly. Ensure your
     * enchantment JSON files exist in data/mosbergapi/enchantment/
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI enchantments (data-driven)");
        // Enchantments are loaded from data packs in 1.21+
        // JSON files should be in: data/mosbergapi/enchantment/*.json
    }
}
