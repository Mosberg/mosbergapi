package dk.mosberg.api.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Comprehensive configuration manager for MosbergAPI.
 *
 * <p>
 * This manager handles loading, saving, and accessing configuration values with support for
 * multiple configuration files, hot-reloading, and type-safe access.
 *
 * <h2>Configuration Structure</h2>
 * <p>
 * Configurations are stored as JSON files in {@code config/mosbergapi/}:
 * <ul>
 * <li><strong>main.json</strong> - Core API settings</li>
 * <li><strong>registry.json</strong> - Registry-specific settings</li>
 * <li><strong>world.json</strong> - World generation settings</li>
 * <li><strong>entities.json</strong> - Entity behavior settings</li>
 * <li><strong>items.json</strong> - Item behavior settings</li>
 * <li><strong>blocks.json</strong> - Block behavior settings</li>
 * <li><strong>commands.json</strong> - Command permissions and settings</li>
 * </ul>
 *
 * <h2>Features</h2>
 * <ul>
 * <li>Type-safe configuration access with default values</li>
 * <li>Hot-reloading of configurations without server restart</li>
 * <li>Configuration validation and error recovery</li>
 * <li>Change listeners for dynamic updates</li>
 * <li>Per-mod configuration support</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Register a configuration
 * ConfigManager.register("mymod", "custom_config");
 *
 * // Set default values
 * ConfigManager.setDefault("mymod", "custom_config.enabled", true);
 * ConfigManager.setDefault("mymod", "custom_config.max_value", 100);
 *
 * // Get values
 * boolean enabled = ConfigManager.getBoolean("mymod", "custom_config.enabled");
 * int maxValue = ConfigManager.getInt("mymod", "custom_config.max_value");
 *
 * // Listen for changes
 * ConfigManager.addChangeListener("mymod", "custom_config.enabled", value -> {
 *          LOGGER.info("Config changed: {}", value);
 *          });
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_DIR =
            FabricLoader.getInstance().getConfigDir().resolve("mosbergapi");

    private static final Map<String, Map<String, JsonObject>> CONFIGS = new HashMap<>();
    private static final Map<String, Map<String, JsonObject>> DEFAULTS = new HashMap<>();
    private static final Map<String, Map<String, Consumer<Object>>> CHANGE_LISTENERS =
            new HashMap<>();

    // Core configuration keys
    public static final String MAIN_CONFIG = "main";
    public static final String REGISTRY_CONFIG = "registry";
    public static final String WORLD_CONFIG = "world";
    public static final String ENTITIES_CONFIG = "entities";
    public static final String ITEMS_CONFIG = "items";
    public static final String BLOCKS_CONFIG = "blocks";
    public static final String COMMANDS_CONFIG = "commands";

    static {
        try {
            Files.createDirectories(CONFIG_DIR);
            initializeDefaultConfigs();
        } catch (IOException e) {
            LOGGER.error("Failed to create config directory", e);
        }
    }

    private ConfigManager() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Initializes default MosbergAPI configurations.
     */
    private static void initializeDefaultConfigs() {
        // Main configuration
        register("mosbergapi", MAIN_CONFIG);
        setDefault("mosbergapi", "debug_mode", false);
        setDefault("mosbergapi", "log_registry_events", true);
        setDefault("mosbergapi", "enable_auto_save", true);
        setDefault("mosbergapi", "auto_save_interval_ticks", 6000);

        // Registry configuration
        register("mosbergapi", REGISTRY_CONFIG);
        setDefault("mosbergapi", "registry.enable_validation", true);
        setDefault("mosbergapi", "registry.log_registrations", false);
        setDefault("mosbergapi", "registry.freeze_on_ready", true);

        // World configuration
        register("mosbergapi", WORLD_CONFIG);
        setDefault("mosbergapi", "world.enable_custom_gen", true);
        setDefault("mosbergapi", "world.max_structure_distance", 1000);
        setDefault("mosbergapi", "world.custom_biomes_enabled", true);

        // Entity configuration
        register("mosbergapi", ENTITIES_CONFIG);
        setDefault("mosbergapi", "entities.spawn_rates_multiplier", 1.0);
        setDefault("mosbergapi", "entities.enable_custom_ai", true);
        setDefault("mosbergapi", "entities.max_entity_count", 200);
        setDefault("mosbergapi", "entities.despawn_distance", 128);

        // Item configuration
        register("mosbergapi", ITEMS_CONFIG);
        setDefault("mosbergapi", "items.enable_custom_tooltips", true);
        setDefault("mosbergapi", "items.show_durability_bar", true);
        setDefault("mosbergapi", "items.enable_enchantment_glint", true);

        // Block configuration
        register("mosbergapi", BLOCKS_CONFIG);
        setDefault("mosbergapi", "blocks.enable_custom_models", true);
        setDefault("mosbergapi", "blocks.animated_textures", true);
        setDefault("mosbergapi", "blocks.tick_rate_multiplier", 1.0);

        // Commands configuration
        register("mosbergapi", COMMANDS_CONFIG);
        setDefault("mosbergapi", "commands.require_op", true);
        setDefault("mosbergapi", "commands.min_permission_level", 2);
        setDefault("mosbergapi", "commands.enable_client_commands", true);
        setDefault("mosbergapi", "commands.command_cooldown_seconds", 0);

        // Load all default configs
        loadAll("mosbergapi");
    }

    /**
     * Registers a configuration file for a mod.
     *
     * @param modId The mod identifier
     * @param configName The configuration name (without .json extension)
     *
     * @example
     *
     *          <pre>{@code
     * ConfigManager.register("mymod", "custom_config");
     * }</pre>
     */
    public static void register(@NotNull String modId, @NotNull String configName) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");
        if (configName == null)
            throw new NullPointerException("Config name cannot be null");

        CONFIGS.computeIfAbsent(modId, k -> new HashMap<>()).putIfAbsent(configName,
                new JsonObject());
        DEFAULTS.computeIfAbsent(modId, k -> new HashMap<>()).putIfAbsent(configName,
                new JsonObject());
        CHANGE_LISTENERS.computeIfAbsent(modId, k -> new HashMap<>());

        LOGGER.debug("Registered config: {} for mod: {}", configName, modId);
    }

    /**
     * Sets a default value for a configuration key.
     *
     * @param modId The mod identifier
     * @param key The configuration key (dot-separated path)
     * @param value The default value
     *
     * @example
     *
     *          <pre>{@code
     * ConfigManager.setDefault("mymod", "feature.enabled", true);
     * ConfigManager.setDefault("mymod", "feature.max_value", 100);
     * }</pre>
     */
    public static void setDefault(@NotNull String modId, @NotNull String key,
            @NotNull Object value) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");
        if (key == null)
            throw new NullPointerException("Key cannot be null");
        if (value == null)
            throw new NullPointerException("Value cannot be null");

        String[] parts = key.split("\\.", 2);
        String configName = parts.length > 1 ? parts[0] : MAIN_CONFIG;
        String actualKey = parts.length > 1 ? parts[1] : key;

        Map<String, JsonObject> modDefaults = DEFAULTS.get(modId);
        if (modDefaults == null) {
            register(modId, configName);
            modDefaults = DEFAULTS.get(modId);
        }

        JsonObject config = modDefaults.computeIfAbsent(configName, k -> new JsonObject());
        setNestedValue(config, actualKey, value);
    }

    /**
     * Gets a configuration value as a string.
     *
     * @param modId The mod identifier
     * @param key The configuration key
     * @return The configuration value, or empty string if not found
     */
    @NotNull
    public static String getString(@NotNull String modId, @NotNull String key) {
        Object value = getValue(modId, key);
        return value != null ? value.toString() : "";
    }

    /**
     * Gets a configuration value as an integer.
     *
     * @param modId The mod identifier
     * @param key The configuration key
     * @return The configuration value, or 0 if not found
     */
    public static int getInt(@NotNull String modId, @NotNull String key) {
        Object value = getValue(modId, key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    /**
     * Gets a configuration value as a double.
     *
     * @param modId The mod identifier
     * @param key The configuration key
     * @return The configuration value, or 0.0 if not found
     */
    public static double getDouble(@NotNull String modId, @NotNull String key) {
        Object value = getValue(modId, key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }

    /**
     * Gets a configuration value as a boolean.
     *
     * @param modId The mod identifier
     * @param key The configuration key
     * @return The configuration value, or false if not found
     */
    public static boolean getBoolean(@NotNull String modId, @NotNull String key) {
        Object value = getValue(modId, key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }

    /**
     * Sets a configuration value.
     *
     * @param modId The mod identifier
     * @param key The configuration key
     * @param value The new value
     *
     * @example
     *
     *          <pre>{@code
     * ConfigManager.set("mymod", "feature.enabled", true);
     * ConfigManager.save("mymod");
     * }</pre>
     */
    public static void set(@NotNull String modId, @NotNull String key, @NotNull Object value) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");
        if (key == null)
            throw new NullPointerException("Key cannot be null");
        if (value == null)
            throw new NullPointerException("Value cannot be null");

        String[] parts = key.split("\\.", 2);
        String configName = parts.length > 1 ? parts[0] : MAIN_CONFIG;
        String actualKey = parts.length > 1 ? parts[1] : key;

        Map<String, JsonObject> modConfigs = CONFIGS.get(modId);
        if (modConfigs == null) {
            register(modId, configName);
            modConfigs = CONFIGS.get(modId);
        }

        JsonObject config = modConfigs.computeIfAbsent(configName, k -> new JsonObject());
        Object oldValue = getNestedValue(config, actualKey);
        setNestedValue(config, actualKey, value);

        // Trigger change listeners
        if (!value.equals(oldValue)) {
            notifyChangeListeners(modId, key, value);
        }
    }

    /**
     * Adds a change listener for a configuration key.
     *
     * @param modId The mod identifier
     * @param key The configuration key
     * @param listener The listener to invoke when the value changes
     *
     * @example
     *
     *          <pre>{@code
     * ConfigManager.addChangeListener("mymod", "debug_mode", enabled -> {
     *          LOGGER.info("Debug mode: {}", enabled);
     *          });
     * }</pre>
     */
    public static void addChangeListener(@NotNull String modId, @NotNull String key,
            @NotNull Consumer<Object> listener) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");
        if (key == null)
            throw new NullPointerException("Key cannot be null");
        if (listener == null)
            throw new NullPointerException("Listener cannot be null");

        CHANGE_LISTENERS.computeIfAbsent(modId, k -> new HashMap<>()).put(key, listener);
    }

    /**
     * Loads a configuration from disk.
     *
     * @param modId The mod identifier
     * @param configName The configuration name
     * @return true if loaded successfully
     */
    public static boolean load(@NotNull String modId, @NotNull String configName) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");
        if (configName == null)
            throw new NullPointerException("Config name cannot be null");

        Path configFile = CONFIG_DIR.resolve(modId).resolve(configName + ".json");

        try {
            if (!Files.exists(configFile)) {
                // Create default config if it doesn't exist
                save(modId, configName);
                return true;
            }

            String content = Files.readString(configFile);
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();

            CONFIGS.computeIfAbsent(modId, k -> new HashMap<>()).put(configName, json);
            LOGGER.info("Loaded config: {} for mod: {}", configName, modId);
            return true;

        } catch (IOException e) {
            LOGGER.error("Failed to load config: {} for mod: {}", configName, modId, e);
            return false;
        }
    }

    /**
     * Loads all configurations for a mod.
     *
     * @param modId The mod identifier
     * @return true if all configs loaded successfully
     */
    public static boolean loadAll(@NotNull String modId) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");

        Map<String, JsonObject> modConfigs = CONFIGS.get(modId);
        if (modConfigs == null) {
            LOGGER.warn("No configs registered for mod: {}", modId);
            return false;
        }

        boolean success = true;
        for (String configName : modConfigs.keySet()) {
            success &= load(modId, configName);
        }
        return success;
    }

    /**
     * Saves a configuration to disk.
     *
     * @param modId The mod identifier
     * @param configName The configuration name
     * @return true if saved successfully
     */
    public static boolean save(@NotNull String modId, @NotNull String configName) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");
        if (configName == null)
            throw new NullPointerException("Config name cannot be null");

        try {
            Path modConfigDir = CONFIG_DIR.resolve(modId);
            Files.createDirectories(modConfigDir);

            Path configFile = modConfigDir.resolve(configName + ".json");

            JsonObject config = CONFIGS.getOrDefault(modId, new HashMap<>())
                    .getOrDefault(configName, DEFAULTS.getOrDefault(modId, new HashMap<>())
                            .getOrDefault(configName, new JsonObject()));

            String json = GSON.toJson(config);
            Files.writeString(configFile, json);

            LOGGER.info("Saved config: {} for mod: {}", configName, modId);
            return true;

        } catch (IOException e) {
            LOGGER.error("Failed to save config: {} for mod: {}", configName, modId, e);
            return false;
        }
    }

    /**
     * Saves all configurations for a mod.
     *
     * @param modId The mod identifier
     * @return true if all configs saved successfully
     */
    public static boolean saveAll(@NotNull String modId) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");

        Map<String, JsonObject> modConfigs = CONFIGS.get(modId);
        if (modConfigs == null) {
            LOGGER.warn("No configs registered for mod: {}", modId);
            return false;
        }

        boolean success = true;
        for (String configName : modConfigs.keySet()) {
            success &= save(modId, configName);
        }
        return success;
    }

    /**
     * Reloads a configuration from disk.
     *
     * @param modId The mod identifier
     * @param configName The configuration name
     * @return true if reloaded successfully
     */
    public static boolean reload(@NotNull String modId, @NotNull String configName) {
        return load(modId, configName);
    }

    /**
     * Reloads all configurations for a mod.
     *
     * @param modId The mod identifier
     * @return true if all configs reloaded successfully
     */
    public static boolean reloadAll(@NotNull String modId) {
        return loadAll(modId);
    }

    /**
     * Resets a configuration to default values.
     *
     * @param modId The mod identifier
     * @param configName The configuration name
     */
    public static void reset(@NotNull String modId, @NotNull String configName) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");
        if (configName == null)
            throw new NullPointerException("Config name cannot be null");

        JsonObject defaults = DEFAULTS.getOrDefault(modId, new HashMap<>()).getOrDefault(configName,
                new JsonObject());

        CONFIGS.computeIfAbsent(modId, k -> new HashMap<>()).put(configName, defaults.deepCopy());
        save(modId, configName);

        LOGGER.info("Reset config: {} for mod: {}", configName, modId);
    }

    /**
     * Gets the configuration directory path.
     *
     * @return The configuration directory path
     */
    @NotNull
    public static Path getConfigDirectory() {
        return CONFIG_DIR;
    }

    /**
     * Gets the configuration directory for a specific mod.
     *
     * @param modId The mod identifier
     * @return The mod's configuration directory path
     */
    @NotNull
    public static Path getModConfigDirectory(@NotNull String modId) {
        if (modId == null)
            throw new NullPointerException("Mod ID cannot be null");
        return CONFIG_DIR.resolve(modId);
    }

    // Private helper methods

    @Nullable
    private static Object getValue(@NotNull String modId, @NotNull String key) {
        String[] parts = key.split("\\.", 2);
        String configName = parts.length > 1 ? parts[0] : MAIN_CONFIG;
        String actualKey = parts.length > 1 ? parts[1] : key;

        JsonObject config = CONFIGS.getOrDefault(modId, new HashMap<>()).getOrDefault(configName,
                DEFAULTS.getOrDefault(modId, new HashMap<>()).getOrDefault(configName,
                        new JsonObject()));

        Object value = getNestedValue(config, actualKey);
        return value != null ? value : getDefaultValue(modId, configName, actualKey);
    }

    @Nullable
    private static Object getDefaultValue(@NotNull String modId, @NotNull String configName,
            @NotNull String key) {
        JsonObject defaults = DEFAULTS.getOrDefault(modId, new HashMap<>()).getOrDefault(configName,
                new JsonObject());
        return getNestedValue(defaults, key);
    }

    @Nullable
    private static Object getNestedValue(@NotNull JsonObject json, @NotNull String key) {
        String[] parts = key.split("\\.");
        JsonObject current = json;

        for (int i = 0; i < parts.length - 1; i++) {
            if (!current.has(parts[i]) || !current.get(parts[i]).isJsonObject()) {
                return null;
            }
            current = current.getAsJsonObject(parts[i]);
        }

        String finalKey = parts[parts.length - 1];
        if (!current.has(finalKey)) {
            return null;
        }

        var element = current.get(finalKey);
        if (element.isJsonPrimitive()) {
            var primitive = element.getAsJsonPrimitive();
            if (primitive.isBoolean())
                return primitive.getAsBoolean();
            if (primitive.isNumber())
                return primitive.getAsNumber();
            if (primitive.isString())
                return primitive.getAsString();
        }
        return element;
    }

    private static void setNestedValue(@NotNull JsonObject json, @NotNull String key,
            @NotNull Object value) {
        String[] parts = key.split("\\.");
        JsonObject current = json;

        for (int i = 0; i < parts.length - 1; i++) {
            if (!current.has(parts[i]) || !current.get(parts[i]).isJsonObject()) {
                current.add(parts[i], new JsonObject());
            }
            current = current.getAsJsonObject(parts[i]);
        }

        String finalKey = parts[parts.length - 1];
        if (value instanceof Boolean) {
            current.addProperty(finalKey, (Boolean) value);
        } else if (value instanceof Number) {
            current.addProperty(finalKey, (Number) value);
        } else if (value instanceof String) {
            current.addProperty(finalKey, (String) value);
        } else {
            current.addProperty(finalKey, value.toString());
        }
    }

    private static void notifyChangeListeners(@NotNull String modId, @NotNull String key,
            @NotNull Object value) {
        Map<String, Consumer<Object>> listeners = CHANGE_LISTENERS.get(modId);
        if (listeners != null && listeners.containsKey(key)) {
            try {
                listeners.get(key).accept(value);
            } catch (Exception e) {
                LOGGER.error("Error in change listener for key: {}", key, e);
            }
        }
    }
}
