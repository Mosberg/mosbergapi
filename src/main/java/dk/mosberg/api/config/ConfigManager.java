package dk.mosberg.api.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.mosberg.api.MosbergApi;
import net.fabricmc.loader.api.FabricLoader;

/**
 * JSON-based configuration system for MosbergAPI
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create();

    private static final Path CONFIG_DIR = FabricLoader.getInstance()
        .getConfigDir()
        .resolve("mosbergapi");

    static {
        // Create config directory if it doesn't exist
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            MosbergApi.LOGGER.error("Failed to create config directory", e);
        }
    }

    /**
     * Loads a config from file, or creates it with default values if it doesn't exist
     *
     * @param filename The config filename (e.g., "myconfig.json")
     * @param configClass The config class type
     * @param defaultConfig The default config instance to use if file doesn't exist
     * @return The loaded or default config
     */
    public static <T> T loadConfig(String filename, Class<T> configClass, T defaultConfig) {
        Path configFile = CONFIG_DIR.resolve(filename);

        try {
            if (Files.exists(configFile)) {
                // Load existing config
                String json = Files.readString(configFile);
                T config = GSON.fromJson(json, configClass);
                MosbergApi.LOGGER.info("Loaded config: {}", filename);
                return config;
            } else {
                // Create default config
                saveConfig(filename, defaultConfig);
                MosbergApi.LOGGER.info("Created default config: {}", filename);
                return defaultConfig;
            }
        } catch (IOException e) {
            MosbergApi.LOGGER.error("Failed to load config: {}", filename, e);
            return defaultConfig;
        }
    }

    /**
     * Saves a config to file
     *
     * @param filename The config filename
     * @param config The config instance to save
     */
    public static <T> void saveConfig(String filename, T config) {
        Path configFile = CONFIG_DIR.resolve(filename);

        try {
            String json = GSON.toJson(config);
            Files.writeString(configFile, json);
            MosbergApi.LOGGER.debug("Saved config: {}", filename);
        } catch (IOException e) {
            MosbergApi.LOGGER.error("Failed to save config: {}", filename, e);
        }
    }

    /**
     * Deletes a config file
     *
     * @param filename The config filename to delete
     * @return true if deleted successfully
     */
    public static boolean deleteConfig(String filename) {
        Path configFile = CONFIG_DIR.resolve(filename);

        try {
            boolean deleted = Files.deleteIfExists(configFile);
            if (deleted) {
                MosbergApi.LOGGER.info("Deleted config: {}", filename);
            }
            return deleted;
        } catch (IOException e) {
            MosbergApi.LOGGER.error("Failed to delete config: {}", filename, e);
            return false;
        }
    }

    /**
     * Checks if a config file exists
     *
     * @param filename The config filename
     * @return true if the config file exists
     */
    public static boolean configExists(String filename) {
        return Files.exists(CONFIG_DIR.resolve(filename));
    }

    /**
     * Gets the config directory path
     */
    public static Path getConfigDirectory() {
        return CONFIG_DIR;
    }
}
