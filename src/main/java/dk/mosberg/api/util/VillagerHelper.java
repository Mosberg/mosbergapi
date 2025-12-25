package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;

/**
 * Helper class for working with villagers in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for managing villager professions, types (biomes), and
 * villager data in the dynamic registry system.
 *
 * <h2>Villager System</h2>
 * <p>
 * Villagers have two main characteristics:
 * <ul>
 * <li><strong>Profession:</strong> Job type (farmer, librarian, cleric, etc.)</li>
 * <li><strong>Type:</strong> Biome variant (plains, desert, taiga, etc.)</li>
 * <li><strong>Level:</strong> Experience level (1-5: Novice, Apprentice, Journeyman, Expert,
 * Master)</li>
 * <li><strong>Trades:</strong> Available trading offers based on profession and level</li>
 * </ul>
 *
 * <h2>Custom Professions</h2>
 * <p>
 * Custom professions can be created via data packs or registration:
 * <ul>
 * <li>Define workstation block</li>
 * <li>Specify trade offers for each level</li>
 * <li>Set profession-specific behaviors</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Get villager profession
 * VillagerProfession profession = VillagerHelper.getProfession("minecraft:farmer");
 *
 * // Get villager type
 * VillagerType type = VillagerHelper.getType("minecraft:desert");
 *
 * // Set villager profession
 * VillagerHelper.setProfession(villager, VillagerProfession.LIBRARIAN);
 *
 * // Check if profession exists
 * if (VillagerHelper.hasProfession("mosbergapi:alchemist")) {
 *     LOGGER.info("Custom profession registered!");
 *          }
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see VillagerEntity
 * @see VillagerProfession
 * @see VillagerType
 */
public class VillagerHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillagerHelper.class);

    private VillagerHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Gets a villager profession by its identifier.
     *
     * @param id The identifier of the profession
     * @return The villager profession, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * VillagerProfession profession = VillagerHelper.getProfession("minecraft:farmer");
     * }</pre>
     */
    @Nullable
    public static VillagerProfession getProfession(@NotNull String id) {
        return getProfession(Identifier.tryParse(id));
    }

    /**
     * Gets a villager profession by its identifier.
     *
     * @param id The identifier of the profession
     * @return The villager profession, or null if not found
     */
    @Nullable
    public static VillagerProfession getProfession(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.VILLAGER_PROFESSION.get(id);
    }

    /**
     * Gets a villager type by its identifier.
     *
     * @param id The identifier of the type
     * @return The villager type, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * VillagerType type = VillagerHelper.getType("minecraft:desert");
     * }</pre>
     */
    @Nullable
    public static VillagerType getType(@NotNull String id) {
        return getType(Identifier.tryParse(id));
    }

    /**
     * Gets a villager type by its identifier.
     *
     * @param id The identifier of the type
     * @return The villager type, or null if not found
     */
    @Nullable
    public static VillagerType getType(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.VILLAGER_TYPE.get(id);
    }

    /**
     * Checks if a villager profession exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the profession is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (VillagerHelper.hasProfession("mosbergapi:alchemist")) {
     *     LOGGER.info("Alchemist profession is registered!");
     *          }
     * }</pre>
     */
    public static boolean hasProfession(@NotNull String id) {
        return hasProfession(Identifier.tryParse(id));
    }

    /**
     * Checks if a villager profession exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the profession is registered
     */
    public static boolean hasProfession(@Nullable Identifier id) {
        return id != null && Registries.VILLAGER_PROFESSION.containsId(id);
    }

    /**
     * Checks if a villager type exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the type is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (VillagerHelper.hasType("minecraft:swamp")) {
     *     LOGGER.info("Swamp villager type exists!");
     *          }
     * }</pre>
     */
    public static boolean hasType(@NotNull String id) {
        return hasType(Identifier.tryParse(id));
    }

    /**
     * Checks if a villager type exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the type is registered
     */
    public static boolean hasType(@Nullable Identifier id) {
        return id != null && Registries.VILLAGER_TYPE.containsId(id);
    }

    /**
     * Gets the identifier of a villager profession.
     *
     * @param profession The villager profession
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = VillagerHelper.getProfessionId(VillagerProfession.FARMER);
     * // Returns: minecraft:farmer
     * }</pre>
     */
    @Nullable
    public static Identifier getProfessionId(@NotNull VillagerProfession profession) {
        if (profession == null)
            throw new NullPointerException("Profession cannot be null");
        return Registries.VILLAGER_PROFESSION.getId(profession);
    }

    /**
     * Gets the identifier of a villager type.
     *
     * @param type The villager type
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = VillagerHelper.getTypeId(VillagerType.DESERT);
     * // Returns: minecraft:desert
     * }</pre>
     */
    @Nullable
    public static Identifier getTypeId(@NotNull VillagerType type) {
        if (type == null)
            throw new NullPointerException("Type cannot be null");
        return Registries.VILLAGER_TYPE.getId(type);
    }

    /**
     * Sets the level of a villager entity.
     *
     * @param villager The villager entity
     * @param level The level to set (1-5)
     * @throws NullPointerException if villager is null
     * @throws IllegalArgumentException if level is not between 1 and 5
     *
     * @example
     *
     *          <pre>{@code
     * VillagerHelper.setLevel(villager, 5);
     * // Villager is now level 5 (Master)
     * }</pre>
     */
    public static void setLevel(@NotNull VillagerEntity villager, int level) {
        if (villager == null)
            throw new NullPointerException("Villager cannot be null");
        if (level < 1 || level > 5) {
            throw new IllegalArgumentException("Level must be between 1 and 5, got: " + level);
        }

        villager.setVillagerData(villager.getVillagerData().withLevel(level));
        LOGGER.debug("Set villager level to: {}", level);
    }

    /**
     * Gets the profession of a villager entity.
     *
     * @param villager The villager entity
     * @return The villager's profession
     * @throws NullPointerException if villager is null
     *
     * @example
     *
     *          <pre>{@code
     * VillagerProfession profession = VillagerHelper.getVillagerProfession(villager);
     *          LOGGER.info("Villager profession: {}", VillagerHelper.getProfessionId(profession));
     * }</pre>
     */
    @NotNull
    public static VillagerProfession getVillagerProfession(@NotNull VillagerEntity villager) {
        if (villager == null)
            throw new NullPointerException("Villager cannot be null");
        return villager.getVillagerData().profession().value();
    }

    /**
     * Gets the type of a villager entity.
     *
     * @param villager The villager entity
     * @return The villager's type
     * @throws NullPointerException if villager is null
     *
     * @example
     *
     *          <pre>{@code
     * VillagerType type = VillagerHelper.getVillagerType(villager);
     *          LOGGER.info("Villager type: {}", VillagerHelper.getTypeId(type));
     * }</pre>
     */
    @NotNull
    public static VillagerType getVillagerType(@NotNull VillagerEntity villager) {
        if (villager == null)
            throw new NullPointerException("Villager cannot be null");
        return villager.getVillagerData().type().value();
    }

    /**
     * Gets the level of a villager entity.
     *
     * @param villager The villager entity
     * @return The villager's level (1-5)
     * @throws NullPointerException if villager is null
     *
     * @example
     *
     *          <pre>{@code
     * int level = VillagerHelper.getVillagerLevel(villager);
     *          LOGGER.info("Villager is level {}", level);
     * }</pre>
     */
    public static int getVillagerLevel(@NotNull VillagerEntity villager) {
        if (villager == null)
            throw new NullPointerException("Villager cannot be null");
        return villager.getVillagerData().level();
    }

    /**
     * Checks if a villager has a specific profession.
     *
     * @param villager The villager entity
     * @param profession The profession to check
     * @return true if the villager has this profession
     * @throws NullPointerException if villager or profession is null
     *
     * @example
     *
     *          <pre>{@code
     * if (VillagerHelper.hasProfession(villager, VillagerProfession.FARMER)) {
     *     LOGGER.info("This is a farmer villager");
     *          }
     * }</pre>
     */
    public static boolean hasVillagerProfession(@NotNull VillagerEntity villager,
            @NotNull VillagerProfession profession) {
        if (profession == null)
            throw new NullPointerException("Profession cannot be null");
        return getVillagerProfession(villager).equals(profession);
    }



    /**
     * Increases a villager's level by one.
     *
     * @param villager The villager entity
     * @return true if the level was increased (false if already max level)
     * @throws NullPointerException if villager is null
     *
     * @example
     *
     *          <pre>{@code
     * if (VillagerHelper.levelUp(villager)) {
     *     LOGGER.info("Villager leveled up!");
     *          } else {
     *          LOGGER.info("Villager is already max level");
     *          }
     * }</pre>
     */
    public static boolean levelUp(@NotNull VillagerEntity villager) {
        int currentLevel = getVillagerLevel(villager);
        if (currentLevel >= 5)
            return false;

        setLevel(villager, currentLevel + 1);
        return true;
    }

    /**
     * Gets all registered villager professions.
     *
     * @return An iterable of all villager professions
     *
     * @example
     *
     *          <pre>{@code
     * for (VillagerProfession profession : VillagerHelper.getAllProfessions()) {
     *          LOGGER.info("Profession: {}", VillagerHelper.getProfessionId(profession));
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<VillagerProfession> getAllProfessions() {
        return Registries.VILLAGER_PROFESSION;
    }

    /**
     * Gets all registered villager types.
     *
     * @return An iterable of all villager types
     *
     * @example
     *
     *          <pre>{@code
     * for (VillagerType type : VillagerHelper.getAllTypes()) {
     *          LOGGER.info("Type: {}", VillagerHelper.getTypeId(type));
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<VillagerType> getAllTypes() {
        return Registries.VILLAGER_TYPE;
    }

    /**
     * Gets the number of registered villager professions.
     *
     * @return The count of professions
     *
     * @example
     *
     *          <pre>{@code
     * int count = VillagerHelper.getProfessionCount();
     *          LOGGER.info("Total professions: {}", count);
     * }</pre>
     */
    public static int getProfessionCount() {
        return Registries.VILLAGER_PROFESSION.size();
    }

    /**
     * Gets the number of registered villager types.
     *
     * @return The count of types
     *
     * @example
     *
     *          <pre>{@code
     * int count = VillagerHelper.getTypeCount();
     *          LOGGER.info("Total villager types: {}", count);
     * }</pre>
     */
    public static int getTypeCount() {
        return Registries.VILLAGER_TYPE.size();
    }

    /**
     * Gets the experience needed to reach a specific level.
     *
     * @param level The target level (1-5)
     * @return The total experience needed
     * @throws IllegalArgumentException if level is not between 1 and 5
     *
     * @example
     *
     *          <pre>{@code
     * int exp = VillagerHelper.getExperienceForLevel(5);
     *          LOGGER.info("Master level requires {} experience", exp);
     * }</pre>
     */
    public static int getExperienceForLevel(int level) {
        if (level < 1 || level > 5) {
            throw new IllegalArgumentException("Level must be between 1 and 5, got: " + level);
        }

        // Experience thresholds for each level
        return switch (level) {
            case 1 -> 0; // Novice
            case 2 -> 10; // Apprentice
            case 3 -> 70; // Journeyman
            case 4 -> 150; // Expert
            case 5 -> 250; // Master
            default -> 0;
        };
    }

    /**
     * Gets the name of a villager level.
     *
     * @param level The level (1-5)
     * @return The level name
     * @throws IllegalArgumentException if level is not between 1 and 5
     *
     * @example
     *
     *          <pre>{@code
     * String name = VillagerHelper.getLevelName(5);
     * // Returns: "Master"
     * }</pre>
     */
    @NotNull
    public static String getLevelName(int level) {
        if (level < 1 || level > 5) {
            throw new IllegalArgumentException("Level must be between 1 and 5, got: " + level);
        }

        return switch (level) {
            case 1 -> "Novice";
            case 2 -> "Apprentice";
            case 3 -> "Journeyman";
            case 4 -> "Expert";
            case 5 -> "Master";
            default -> "Unknown";
        };
    }
}
