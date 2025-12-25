package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * Helper class for working with enchantments in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for managing enchantments on items. Note that
 * enchantments in Minecraft 1.21+ are data-driven through JSON files in the data pack system.
 *
 * <h2>Enchantment System (1.21+)</h2>
 * <p>
 * Enchantments are now defined in JSON files:
 * <ul>
 * <li><strong>Location:</strong> {@code data/<namespace>/enchantment/<name>.json}</li>
 * <li><strong>Registry Keys:</strong> Used to reference enchantments in code</li>
 * <li><strong>Dynamic Registry:</strong> Loaded at runtime from data packs</li>
 * <li><strong>Component-Based:</strong> Stored in item data components</li>
 * </ul>
 *
 * <h2>Enchantment Properties</h2>
 * <ul>
 * <li><strong>Level:</strong> Enchantment power (1-5 typically, can be higher)</li>
 * <li><strong>Rarity:</strong> How rare the enchantment is (common, uncommon, rare, very rare)</li>
 * <li><strong>Compatibility:</strong> Which items can receive the enchantment</li>
 * <li><strong>Conflicts:</strong> Which other enchantments are incompatible</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Get enchantment level
 * int level = EnchantmentHelper.getLevel(stack, ModEnchantments.SOUL_BOUND, registryManager);
 *
 * // Check if item has enchantment
 * if (EnchantmentHelper.hasEnchantment(stack, ModEnchantments.AUTO_SMELT, registryManager)) {
 *     // Item has Auto Smelt enchantment
 *          }
 *
 *          // Get total enchantment level
 *          int total = EnchantmentHelper.getTotalLevel(stack);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see Enchantment
 * @see net.minecraft.enchantment.EnchantmentHelper
 * @see RegistryKey
 */
public class MosbergEnchantmentHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergEnchantmentHelper.class);

    private MosbergEnchantmentHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Gets the level of a specific enchantment on an item stack.
     *
     * @param stack The item stack to check
     * @param enchantmentKey The enchantment registry key
     * @param registryManager The dynamic registry manager
     * @return The enchantment level, or 0 if not present
     * @throws NullPointerException if stack, enchantmentKey, or registryManager is null
     *
     * @example
     *
     *          <pre>{@code
     * int level = MosbergEnchantmentHelper.getLevel(sword, Enchantments.SHARPNESS, registryManager);
     *          LOGGER.info("Sword has Sharpness {}", level);
     * }</pre>
     */
    public static int getLevel(@NotNull ItemStack stack,
            @NotNull RegistryKey<Enchantment> enchantmentKey,
            @NotNull DynamicRegistryManager registryManager) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");
        if (enchantmentKey == null)
            throw new NullPointerException("Enchantment key cannot be null");
        if (registryManager == null)
            throw new NullPointerException("Registry manager cannot be null");

        RegistryEntry<Enchantment> entry = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT)
                .getEntry(enchantmentKey.getValue()).orElse(null);

        if (entry == null)
            return 0;

        return EnchantmentHelper.getLevel(entry, stack);
    }

    /**
     * Checks if an item stack has a specific enchantment.
     *
     * @param stack The item stack to check
     * @param enchantmentKey The enchantment registry key
     * @param registryManager The dynamic registry manager
     * @return true if the item has the enchantment
     *
     * @example
     *
     *          <pre>{@code
     * if (MosbergEnchantmentHelper.hasEnchantment(armor, Enchantments.PROTECTION, registryManager)) {
     *     // Armor has Protection
     *          }
     * }</pre>
     */
    public static boolean hasEnchantment(@NotNull ItemStack stack,
            @NotNull RegistryKey<Enchantment> enchantmentKey,
            @NotNull DynamicRegistryManager registryManager) {
        return getLevel(stack, enchantmentKey, registryManager) > 0;
    }

    /**
     * Gets the total enchantment level on an item (for enchantment cost calculations).
     *
     * @param stack The item stack to check
     * @return The total level of all enchantments
     *
     * @example
     *
     *          <pre>{@code
     * int totalLevel = MosbergEnchantmentHelper.getTotalLevel(stack);
     *          LOGGER.info("Total enchantment power: {}", totalLevel);
     * }</pre>
     */
    public static int getTotalLevel(@NotNull ItemStack stack) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");

        int total = 0;
        var enchantments = EnchantmentHelper.getEnchantments(stack);
        for (var entry : enchantments.getEnchantmentEntries()) {
            total += entry.getIntValue();
        }

        LOGGER.debug("Total enchantment level on {}: {}", stack.getItem(), total);
        return total;
    }

    /**
     * Checks if an item has any enchantments.
     *
     * @param stack The item stack to check
     * @return true if the item has at least one enchantment
     *
     * @example
     *
     *          <pre>{@code
     * if (MosbergEnchantmentHelper.isEnchanted(stack)) {
     *     // Item has enchantments
     *          }
     * }</pre>
     */
    public static boolean isEnchanted(@NotNull ItemStack stack) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");
        return !EnchantmentHelper.getEnchantments(stack).isEmpty();
    }

    /**
     * Creates a registry key for an enchantment.
     *
     * @param id The enchantment identifier
     * @return The registry key for the enchantment
     * @throws NullPointerException if id is null
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<Enchantment> key = MosbergEnchantmentHelper.createKey(
     *     Identifier.of("mosbergapi", "soul_bound"));
     * }</pre>
     */
    @NotNull
    public static RegistryKey<Enchantment> createKey(@NotNull Identifier id) {
        if (id == null)
            throw new NullPointerException("Identifier cannot be null");
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }

    /**
     * Creates a registry key for an enchantment.
     *
     * @param id The enchantment identifier string
     * @return The registry key for the enchantment, or null if id is invalid
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<Enchantment> key = MosbergEnchantmentHelper.createKey("mosbergapi:soul_bound");
     * }</pre>
     */
    @Nullable
    public static RegistryKey<Enchantment> createKey(@NotNull String id) {
        Identifier identifier = Identifier.tryParse(id);
        return identifier != null ? createKey(identifier) : null;
    }

    /**
     * Checks if an enchantment exists in the registry.
     *
     * @param enchantmentKey The enchantment registry key
     * @param registryManager The dynamic registry manager
     * @return true if the enchantment is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (MosbergEnchantmentHelper.exists(ModEnchantments.CUSTOM, registryManager)) {
     *     LOGGER.info("Custom enchantment is registered!");
     *          }
     * }</pre>
     */
    public static boolean exists(@NotNull RegistryKey<Enchantment> enchantmentKey,
            @NotNull DynamicRegistryManager registryManager) {
        if (enchantmentKey == null)
            throw new NullPointerException("Enchantment key cannot be null");
        if (registryManager == null)
            throw new NullPointerException("Registry manager cannot be null");

        return registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).contains(enchantmentKey);
    }

    /**
     * Gets an enchantment registry entry.
     *
     * @param enchantmentKey The enchantment registry key
     * @param registryManager The dynamic registry manager
     * @return The enchantment registry entry, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * RegistryEntry<Enchantment> entry = MosbergEnchantmentHelper.getEntry(
     *     ModEnchantments.SOUL_BOUND, registryManager);
     * if (entry != null) {
     *     // Use the enchantment
     *          }
     * }</pre>
     */
    @Nullable
    public static RegistryEntry<Enchantment> getEntry(
            @NotNull RegistryKey<Enchantment> enchantmentKey,
            @NotNull DynamicRegistryManager registryManager) {
        if (enchantmentKey == null)
            throw new NullPointerException("Enchantment key cannot be null");
        if (registryManager == null)
            throw new NullPointerException("Registry manager cannot be null");

        return registryManager.getOrThrow(RegistryKeys.ENCHANTMENT)
                .getEntry(enchantmentKey.getValue()).orElse(null);
    }

    /**
     * Gets the maximum level for an enchantment.
     *
     * @param enchantmentKey The enchantment registry key
     * @param registryManager The dynamic registry manager
     * @return The maximum level, or 0 if enchantment not found
     *
     * @example
     *
     *          <pre>{@code
     * int maxLevel = MosbergEnchantmentHelper.getMaxLevel(Enchantments.SHARPNESS, registryManager);
     * // Returns 5 for Sharpness
     * }</pre>
     */
    public static int getMaxLevel(@NotNull RegistryKey<Enchantment> enchantmentKey,
            @NotNull DynamicRegistryManager registryManager) {
        RegistryEntry<Enchantment> entry = getEntry(enchantmentKey, registryManager);
        if (entry == null)
            return 0;

        return entry.value().getMaxLevel();
    }

    /**
     * Gets the minimum level for an enchantment (usually 1).
     *
     * @param enchantmentKey The enchantment registry key
     * @param registryManager The dynamic registry manager
     * @return The minimum level, or 0 if enchantment not found
     *
     * @example
     *
     *          <pre>{@code
     * int minLevel = MosbergEnchantmentHelper.getMinLevel(Enchantments.PROTECTION, registryManager);
     * // Returns 1 for most enchantments
     * }</pre>
     */
    public static int getMinLevel(@NotNull RegistryKey<Enchantment> enchantmentKey,
            @NotNull DynamicRegistryManager registryManager) {
        RegistryEntry<Enchantment> entry = getEntry(enchantmentKey, registryManager);
        if (entry == null)
            return 0;

        return entry.value().getMinLevel();
    }

    /**
     * Checks if two enchantments are compatible (can exist on the same item).
     *
     * @param enchantment1 The first enchantment key
     * @param enchantment2 The second enchantment key
     * @param registryManager The dynamic registry manager
     * @return true if the enchantments are compatible
     *
     * @example
     *
     *          <pre>{@code
     * // Check if Sharpness and Smite can coexist (they can't)
     * boolean compatible = MosbergEnchantmentHelper.areCompatible(
     *     Enchantments.SHARPNESS, Enchantments.SMITE, registryManager);
     * // Returns false
     * }</pre>
     */
    public static boolean areCompatible(@NotNull RegistryKey<Enchantment> enchantment1,
            @NotNull RegistryKey<Enchantment> enchantment2,
            @NotNull DynamicRegistryManager registryManager) {
        RegistryEntry<Enchantment> entry1 = getEntry(enchantment1, registryManager);
        RegistryEntry<Enchantment> entry2 = getEntry(enchantment2, registryManager);

        if (entry1 == null || entry2 == null)
            return false;

        return Enchantment.canBeCombined(entry1, entry2);
    }

    /**
     * Gets the enchantment power (anvil cost) for a specific level.
     *
     * @param enchantmentKey The enchantment registry key
     * @param level The enchantment level
     * @param registryManager The dynamic registry manager
     * @return The anvil cost for this enchantment level
     *
     * @example
     *
     *          <pre>{@code
     * int cost = MosbergEnchantmentHelper.getAnvilCost(Enchantments.SHARPNESS, 5, registryManager);
     *          LOGGER.info("Sharpness V costs {} levels", cost);
     * }</pre>
     */
    public static int getAnvilCost(@NotNull RegistryKey<Enchantment> enchantmentKey, int level,
            @NotNull DynamicRegistryManager registryManager) {
        RegistryEntry<Enchantment> entry = getEntry(enchantmentKey, registryManager);
        if (entry == null)
            return 0;

        return entry.value().getAnvilCost();
    }

    /**
     * Removes all enchantments from an item stack.
     *
     * @param stack The item stack to modify
     * @throws NullPointerException if stack is null
     *
     * @example
     *
     *          <pre>{@code
     * MosbergEnchantmentHelper.removeAllEnchantments(stack);
     * // Item now has no enchantments
     * }</pre>
     */
    public static void removeAllEnchantments(@NotNull ItemStack stack) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");

        stack.remove(net.minecraft.component.DataComponentTypes.ENCHANTMENTS);
        LOGGER.debug("Removed all enchantments from {}", stack.getItem());
    }

    /**
     * Gets the number of enchantments on an item stack.
     *
     * @param stack The item stack to check
     * @return The number of enchantments
     *
     * @example
     *
     *          <pre>{@code
     * int count = MosbergEnchantmentHelper.getEnchantmentCount(stack);
     *          LOGGER.info("Item has {} enchantments", count);
     * }</pre>
     */
    public static int getEnchantmentCount(@NotNull ItemStack stack) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");

        return EnchantmentHelper.getEnchantments(stack).getEnchantments().size();
    }

    /**
     * Checks if an item can receive a specific enchantment.
     *
     * @param stack The item stack
     * @param enchantmentKey The enchantment registry key
     * @param registryManager The dynamic registry manager
     * @return true if the item can be enchanted with this enchantment
     *
     * @example
     *
     *          <pre>{@code
     * if (MosbergEnchantmentHelper.canApply(sword, Enchantments.SHARPNESS, registryManager)) {
     *     // Sword can have Sharpness
     *          }
     * }</pre>
     */
    public static boolean canApply(@NotNull ItemStack stack,
            @NotNull RegistryKey<Enchantment> enchantmentKey,
            @NotNull DynamicRegistryManager registryManager) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");

        RegistryEntry<Enchantment> entry = getEntry(enchantmentKey, registryManager);
        if (entry == null)
            return false;

        return entry.value().isAcceptableItem(stack);
    }

    /**
     * Gets all enchantment registry keys from the registry.
     *
     * @param registryManager The dynamic registry manager
     * @return An iterable of all enchantment keys
     *
     * @example
     *
     *          <pre>{@code
     * for (RegistryKey<Enchantment> key : MosbergEnchantmentHelper.getAllKeys(registryManager)) {
     *          LOGGER.info("Enchantment: {}", key.getValue());
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<RegistryKey<Enchantment>> getAllKeys(
            @NotNull DynamicRegistryManager registryManager) {
        if (registryManager == null)
            throw new NullPointerException("Registry manager cannot be null");

        return registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).streamEntries()
                .map(RegistryEntry.Reference::registryKey).toList();
    }

    /**
     * Gets the number of registered enchantments.
     *
     * @param registryManager The dynamic registry manager
     * @return The count of enchantments
     *
     * @example
     *
     *          <pre>{@code
     * int count = MosbergEnchantmentHelper.getEnchantmentTypeCount(registryManager);
     *          LOGGER.info("Total enchantments: {}", count);
     * }</pre>
     */
    public static int getEnchantmentTypeCount(@NotNull DynamicRegistryManager registryManager) {
        if (registryManager == null)
            throw new NullPointerException("Registry manager cannot be null");

        return registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).size();
    }
}
