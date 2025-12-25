package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;

/**
 * Registry class for world generation features in MosbergAPI. This provides helper methods for
 * creating registry keys for configured and placed features.
 *
 * <p>
 * Note: In Minecraft 1.21+, world generation features are data-driven through JSON files. This
 * class provides utilities for referencing those features in code.
 *
 * @example
 *
 *          <pre>{@code
 * // Create feature keys (actual features defined in worldgen/configured_feature/ and worldgen/placed_feature/)
 * public static final RegistryKey<ConfiguredFeature<?, ?>> CUSTOM_ORE_CONFIGURED =
 *     MosbergWorldGen.configuredFeature("custom_ore");
 *
 * public static final RegistryKey<PlacedFeature> CUSTOM_ORE_PLACED =
 *     MosbergWorldGen.placedFeature("custom_ore");
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergWorldGen {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergWorldGen.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Creates a registry key for a configured feature. The actual feature must be defined in
     * data/mosbergapi/worldgen/configured_feature/{name}.json
     *
     * @param name The feature name (will be prefixed with mod ID)
     * @return The RegistryKey for the configured feature
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<ConfiguredFeature<?, ?>> customOre =
     *     MosbergWorldGen.configuredFeature("custom_ore");
     * // Corresponds to: data/mosbergapi/worldgen/configured_feature/custom_ore.json
     * }</pre>
     */
    public static RegistryKey<ConfiguredFeature<?, ?>> configuredFeature(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        LOGGER.debug("Created configured feature key: {}", id);
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, id);
    }

    /**
     * Creates a registry key for a placed feature. The actual feature must be defined in
     * data/mosbergapi/worldgen/placed_feature/{name}.json
     *
     * @param name The feature name (will be prefixed with mod ID)
     * @return The RegistryKey for the placed feature
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<PlacedFeature> customOre = MosbergWorldGen.placedFeature("custom_ore");
     * // Corresponds to: data/mosbergapi/worldgen/placed_feature/custom_ore.json
     * }</pre>
     */
    public static RegistryKey<PlacedFeature> placedFeature(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        LOGGER.debug("Created placed feature key: {}", id);
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, id);
    }

    /**
     * Creates an identifier for worldgen features.
     *
     * @param name The feature name
     * @return The identifier
     */
    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    /**
     * Initialize worldgen features. Call this method from your mod initializer.
     *
     * <p>
     * Note: World generation in 1.21+ is data-driven. This method exists for consistency but
     * features are loaded from JSON files in the worldgen directory.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI world generation features (data-driven)");
        // Features are loaded from data packs
        // JSON files should be in: data/mosbergapi/worldgen/
    }
}
