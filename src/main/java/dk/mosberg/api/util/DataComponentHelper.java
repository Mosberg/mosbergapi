package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

/**
 * Helper class for working with data components in Minecraft 1.21.10+.
 *
 * <p>
 * Data components replaced NBT in Minecraft 1.21+ for storing item and block data. They provide
 * type-safe, codec-based serialization with better performance and validation.
 *
 * <h2>Component System Benefits</h2>
 * <ul>
 * <li><strong>Type Safety:</strong> Components have defined types, preventing data corruption</li>
 * <li><strong>Performance:</strong> More efficient serialization than NBT</li>
 * <li><strong>Validation:</strong> Codecs enforce data structure constraints</li>
 * <li><strong>Network Efficiency:</strong> Optimized packet codecs reduce bandwidth</li>
 * </ul>
 *
 * <h2>Common Component Types</h2>
 * <ul>
 * <li>{@link DataComponentTypes#CUSTOM_NAME} - Custom item names</li>
 * <li>{@link DataComponentTypes#LORE} - Item lore text</li>
 * <li>{@link DataComponentTypes#DAMAGE} - Item durability</li>
 * <li>{@link DataComponentTypes#ENCHANTMENTS} - Enchantments list</li>
 * <li>{@link DataComponentTypes#CUSTOM_DATA} - Custom NBT data (legacy compatibility)</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Set a component value
 * DataComponentHelper.set(stack, ModComponents.MANA, 100);
 *
 * // Get a component value
 * int mana = DataComponentHelper.get(stack, ModComponents.MANA, 0);
 *
 * // Check if component exists
 * if (DataComponentHelper.has(stack, ModComponents.CHARGE)) {
 *     int charge = stack.get(ModComponents.CHARGE);
 *          }
 *
 *          // Remove a component
 *          DataComponentHelper.remove(stack, ModComponents.MANA);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see ComponentType
 * @see ItemStack
 * @see net.minecraft.component.DataComponentTypes
 */
public class DataComponentHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataComponentHelper.class);

    private DataComponentHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Sets a component value on an item stack.
     *
     * @param <T> The component value type
     * @param stack The item stack to modify
     * @param type The component type
     * @param value The value to set
     * @throws NullPointerException if stack, type, or value is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
     * DataComponentHelper.set(stack, ModComponents.MANA, 150);
     * }</pre>
     */
    public static <T> void set(@NotNull ItemStack stack, @NotNull ComponentType<T> type,
            @NotNull T value) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");
        if (type == null)
            throw new NullPointerException("ComponentType cannot be null");
        if (value == null)
            throw new NullPointerException("Component value cannot be null");

        stack.set(type, value);
        LOGGER.debug("Set component {} on {} to {}", getId(type), stack.getItem(), value);
    }

    /**
     * Gets a component value from an item stack.
     *
     * @param <T> The component value type
     * @param stack The item stack to read from
     * @param type The component type
     * @return The component value, or null if not present
     *
     * @example
     *
     *          <pre>{@code
     * Integer mana = DataComponentHelper.get(stack, ModComponents.MANA);
     * if (mana != null) {
     *          LOGGER.info("Item has {} mana", mana);
     *          }
     * }</pre>
     */
    @Nullable
    public static <T> T get(@NotNull ItemStack stack, @NotNull ComponentType<T> type) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");
        if (type == null)
            throw new NullPointerException("ComponentType cannot be null");

        return stack.get(type);
    }

    /**
     * Gets a component value with a default fallback.
     *
     * @param <T> The component value type
     * @param stack The item stack to read from
     * @param type The component type
     * @param defaultValue The default value if component is not present
     * @return The component value, or defaultValue if not present
     *
     * @example
     *
     *          <pre>{@code
     * int mana = DataComponentHelper.get(stack, ModComponents.MANA, 0);
     * // Returns 0 if component is not set
     * }</pre>
     */
    @NotNull
    public static <T> T get(@NotNull ItemStack stack, @NotNull ComponentType<T> type,
            @NotNull T defaultValue) {
        T value = get(stack, type);
        return value != null ? value : defaultValue;
    }

    /**
     * Checks if an item stack has a specific component.
     *
     * @param stack The item stack to check
     * @param type The component type
     * @return true if the component is present
     *
     * @example
     *
     *          <pre>{@code
     * if (DataComponentHelper.has(stack, ModComponents.CHARGE)) {
     *     // Item is charged
     *          }
     * }</pre>
     */
    public static boolean has(@NotNull ItemStack stack, @NotNull ComponentType<?> type) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");
        if (type == null)
            throw new NullPointerException("ComponentType cannot be null");

        return stack.contains(type);
    }

    /**
     * Removes a component from an item stack.
     *
     * @param stack The item stack to modify
     * @param type The component type to remove
     * @throws NullPointerException if stack or type is null
     *
     * @example
     *
     *          <pre>{@code
     * DataComponentHelper.remove(stack, ModComponents.MANA);
     * // Component is now removed from the stack
     * }</pre>
     */
    public static void remove(@NotNull ItemStack stack, @NotNull ComponentType<?> type) {
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");
        if (type == null)
            throw new NullPointerException("ComponentType cannot be null");

        stack.remove(type);
        LOGGER.debug("Removed component {} from {}", getId(type), stack.getItem());
    }

    /**
     * Copies all components from one item stack to another.
     *
     * @param source The source item stack
     * @param target The target item stack
     * @throws NullPointerException if source or target is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack copy = new ItemStack(source.getItem());
     * DataComponentHelper.copyComponents(source, copy);
     * // copy now has all the same components as source
     * }</pre>
     */
    public static void copyComponents(@NotNull ItemStack source, @NotNull ItemStack target) {
        if (source == null)
            throw new NullPointerException("Source ItemStack cannot be null");
        if (target == null)
            throw new NullPointerException("Target ItemStack cannot be null");

        target.applyComponentsFrom(source.getComponents());
        LOGGER.debug("Copied components from {} to {}", source.getItem(), target.getItem());
    }

    /**
     * Gets a component type by its identifier.
     *
     * @param id The identifier of the component type
     * @return The component type, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * ComponentType<?> type = DataComponentHelper.getComponentType("mosbergapi:mana");
     * }</pre>
     */
    @Nullable
    public static ComponentType<?> getComponentType(@NotNull String id) {
        return getComponentType(Identifier.tryParse(id));
    }

    /**
     * Gets a component type by its identifier.
     *
     * @param id The identifier of the component type
     * @return The component type, or null if not found
     */
    @Nullable
    public static ComponentType<?> getComponentType(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.DATA_COMPONENT_TYPE.get(id);
    }

    /**
     * Checks if a component type exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the component type is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (DataComponentHelper.hasComponentType("mosbergapi:custom_data")) {
     *     LOGGER.info("Custom data component is registered!");
     *          }
     * }</pre>
     */
    public static boolean hasComponentType(@NotNull String id) {
        return hasComponentType(Identifier.tryParse(id));
    }

    /**
     * Checks if a component type exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the component type is registered
     */
    public static boolean hasComponentType(@Nullable Identifier id) {
        return id != null && Registries.DATA_COMPONENT_TYPE.containsId(id);
    }

    /**
     * Gets the identifier of a component type.
     *
     * @param type The component type
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = DataComponentHelper.getId(DataComponentTypes.CUSTOM_NAME);
     * // Returns: minecraft:custom_name
     * }</pre>
     */
    @Nullable
    public static Identifier getId(@NotNull ComponentType<?> type) {
        if (type == null)
            throw new NullPointerException("ComponentType cannot be null");
        return Registries.DATA_COMPONENT_TYPE.getId(type);
    }

    /**
     * Increments an integer component value.
     *
     * @param stack The item stack to modify
     * @param type The integer component type
     * @param amount The amount to add (can be negative)
     * @return The new value after incrementing
     * @throws IllegalArgumentException if component type is not Integer
     *
     * @example
     *
     *          <pre>{@code
     * int newCharge = DataComponentHelper.increment(stack, ModComponents.CHARGE, 10);
     *          LOGGER.info("New charge: {}", newCharge);
     * }</pre>
     */
    public static int increment(@NotNull ItemStack stack, @NotNull ComponentType<Integer> type,
            int amount) {
        int current = get(stack, type, 0);
        int newValue = current + amount;
        set(stack, type, newValue);
        return newValue;
    }

    /**
     * Decrements an integer component value.
     *
     * @param stack The item stack to modify
     * @param type The integer component type
     * @param amount The amount to subtract
     * @return The new value after decrementing
     *
     * @example
     *
     *          <pre>{@code
     * int remaining = DataComponentHelper.decrement(stack, ModComponents.USES, 1);
     * if (remaining <= 0) {
     *     stack.decrement(1); // Break the item
     *          }
     * }</pre>
     */
    public static int decrement(@NotNull ItemStack stack, @NotNull ComponentType<Integer> type,
            int amount) {
        return increment(stack, type, -amount);
    }

    /**
     * Gets all registered component types.
     *
     * @return An iterable of all component types
     *
     * @example
     *
     *          <pre>{@code
     * for (ComponentType<?> type : DataComponentHelper.getAllComponentTypes()) {
     *          LOGGER.info("Component: {}", DataComponentHelper.getId(type));
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<ComponentType<?>> getAllComponentTypes() {
        return Registries.DATA_COMPONENT_TYPE;
    }

    /**
     * Gets the number of registered component types.
     *
     * @return The count of component types
     *
     * @example
     *
     *          <pre>{@code
     * int count = DataComponentHelper.getComponentTypeCount();
     *          LOGGER.info("Total component types: {}", count);
     * }</pre>
     */
    public static int getComponentTypeCount() {
        return Registries.DATA_COMPONENT_TYPE.size();
    }
}
