package dk.mosberg.api.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Helper class for working with item groups (creative tabs) in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for managing creative inventory tabs, adding items to
 * groups, and working with the item group system.
 *
 * <h2>Item Group System</h2>
 * <p>
 * Item groups organize items in the creative inventory:
 * <ul>
 * <li><strong>Display Name:</strong> Translatable text shown in the UI</li>
 * <li><strong>Icon:</strong> Item stack displayed as the tab icon</li>
 * <li><strong>Row:</strong> TOP or BOTTOM row in creative menu</li>
 * <li><strong>Type:</strong> CATEGORY (functional) or SEARCH (all items)</li>
 * <li><strong>Entries:</strong> Items displayed in the tab</li>
 * </ul>
 *
 * <h2>Custom Item Groups</h2>
 * <p>
 * Creating custom creative tabs allows organizing mod items:
 * <ul>
 * <li>Register with {@link net.minecraft.registry.Registries#ITEM_GROUP}</li>
 * <li>Add translation key to language files</li>
 * <li>Populate with mod items in the entries consumer</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Get item group by key
 * ItemGroup group = ItemGroupHelper.getItemGroup(ItemGroups.BUILDING_BLOCKS);
 *
 * // Get item group by ID
 * ItemGroup group = ItemGroupHelper.getItemGroup("minecraft:building_blocks");
 *
 * // Check if item group exists
 * if (ItemGroupHelper.exists("mosbergapi:custom_items")) {
 *     LOGGER.info("Custom item group exists!");
 *          }
 *
 *          // Get display name
 *          Text name = ItemGroupHelper.getDisplayName(group);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see ItemGroup
 * @see RegistryKey
 */
public class ItemGroupHelper {


    private ItemGroupHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Gets an item group by its registry key.
     *
     * @param key The item group registry key
     * @return The item group, or null if not found
     * @throws NullPointerException if key is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemGroup group = ItemGroupHelper.getItemGroup(ItemGroups.COMBAT);
     * if (group != null) {
     *     LOGGER.info("Found combat tab");
     *          }
     * }</pre>
     */
    @Nullable
    public static ItemGroup getItemGroup(@NotNull RegistryKey<ItemGroup> key) {
        if (key == null)
            throw new NullPointerException("Registry key cannot be null");

        return Registries.ITEM_GROUP.get(key);
    }

    /**
     * Gets an item group by its identifier.
     *
     * @param id The item group identifier
     * @return The item group, or null if not found
     *
     * @example
     *
     *          <pre>{@code
     * ItemGroup group = ItemGroupHelper.getItemGroup("minecraft:redstone");
     * }</pre>
     */
    @Nullable
    public static ItemGroup getItemGroup(@NotNull String id) {
        return getItemGroup(Identifier.tryParse(id));
    }

    /**
     * Gets an item group by its identifier.
     *
     * @param id The item group identifier
     * @return The item group, or null if not found
     */
    @Nullable
    public static ItemGroup getItemGroup(@Nullable Identifier id) {
        if (id == null)
            return null;
        return Registries.ITEM_GROUP.get(id);
    }

    /**
     * Creates a registry key for an item group.
     *
     * @param id The item group identifier
     * @return The registry key for the item group
     * @throws NullPointerException if id is null
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<ItemGroup> key = ItemGroupHelper.createKey(
     *     Identifier.of("mosbergapi", "custom_items"));
     * }</pre>
     */
    @NotNull
    public static RegistryKey<ItemGroup> createKey(@NotNull Identifier id) {
        if (id == null)
            throw new NullPointerException("Identifier cannot be null");
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, id);
    }

    /**
     * Creates a registry key for an item group.
     *
     * @param id The item group identifier string
     * @return The registry key for the item group, or null if id is invalid
     *
     * @example
     *
     *          <pre>{@code
     * RegistryKey<ItemGroup> key = ItemGroupHelper.createKey("mosbergapi:magic_items");
     * }</pre>
     */
    @Nullable
    public static RegistryKey<ItemGroup> createKey(@NotNull String id) {
        Identifier identifier = Identifier.tryParse(id);
        return identifier != null ? createKey(identifier) : null;
    }

    /**
     * Checks if an item group exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the item group is registered
     *
     * @example
     *
     *          <pre>{@code
     * if (ItemGroupHelper.exists("mosbergapi:custom_items")) {
     *     LOGGER.info("Custom item group is registered!");
     *          }
     * }</pre>
     */
    public static boolean exists(@NotNull String id) {
        return exists(Identifier.tryParse(id));
    }

    /**
     * Checks if an item group exists in the registry.
     *
     * @param id The identifier to check
     * @return true if the item group is registered
     */
    public static boolean exists(@Nullable Identifier id) {
        return id != null && Registries.ITEM_GROUP.containsId(id);
    }

    /**
     * Checks if an item group exists by registry key.
     *
     * @param key The registry key to check
     * @return true if the item group is registered
     */
    public static boolean exists(@NotNull RegistryKey<ItemGroup> key) {
        if (key == null)
            throw new NullPointerException("Registry key cannot be null");
        return Registries.ITEM_GROUP.contains(key);
    }

    /**
     * Gets the identifier of an item group.
     *
     * @param group The item group
     * @return The identifier, or null if not registered
     *
     * @example
     *
     *          <pre>{@code
     * Identifier id = ItemGroupHelper.getId(group);
     * // Returns: minecraft:building_blocks (for example)
     * }</pre>
     */
    @Nullable
    public static Identifier getId(@NotNull ItemGroup group) {
        if (group == null)
            throw new NullPointerException("ItemGroup cannot be null");
        return Registries.ITEM_GROUP.getId(group);
    }

    /**
     * Gets the display name of an item group.
     *
     * @param group The item group
     * @return The display name text
     * @throws NullPointerException if group is null
     *
     * @example
     *
     *          <pre>{@code
     * Text name = ItemGroupHelper.getDisplayName(group);
     *          LOGGER.info("Group name: {}", name.getString());
     * }</pre>
     */
    @NotNull
    public static Text getDisplayName(@NotNull ItemGroup group) {
        if (group == null)
            throw new NullPointerException("ItemGroup cannot be null");
        return group.getDisplayName();
    }

    /**
     * Gets the icon item stack of an item group.
     *
     * @param group The item group
     * @return The icon item stack
     * @throws NullPointerException if group is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemStack icon = ItemGroupHelper.getIcon(group);
     *          LOGGER.info("Tab icon: {}", icon.getItem());
     * }</pre>
     */
    @NotNull
    public static ItemStack getIcon(@NotNull ItemGroup group) {
        if (group == null)
            throw new NullPointerException("ItemGroup cannot be null");
        return group.getIcon();
    }

    /**
     * Gets the row of an item group (TOP or BOTTOM).
     *
     * @param group The item group
     * @return The row
     * @throws NullPointerException if group is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemGroup.Row row = ItemGroupHelper.getRow(group);
     * if (row == ItemGroup.Row.TOP) {
     *     LOGGER.info("Group is in top row");
     *          }
     * }</pre>
     */
    @NotNull
    public static ItemGroup.Row getRow(@NotNull ItemGroup group) {
        if (group == null)
            throw new NullPointerException("ItemGroup cannot be null");
        return group.getRow();
    }

    /**
     * Gets the column index of an item group within its row.
     *
     * @param group The item group
     * @return The column index
     * @throws NullPointerException if group is null
     *
     * @example
     *
     *          <pre>{@code
     * int column = ItemGroupHelper.getColumn(group);
     *          LOGGER.info("Group is at column {}", column);
     * }</pre>
     */
    public static int getColumn(@NotNull ItemGroup group) {
        if (group == null)
            throw new NullPointerException("ItemGroup cannot be null");
        return group.getColumn();
    }

    /**
     * Gets the type of an item group.
     *
     * @param group The item group
     * @return The item group type
     * @throws NullPointerException if group is null
     *
     * @example
     *
     *          <pre>{@code
     * ItemGroup.Type type = ItemGroupHelper.getType(group);
     * if (type == ItemGroup.Type.CATEGORY) {
     *     LOGGER.info("This is a category tab");
     *          }
     * }</pre>
     */
    @NotNull
    public static ItemGroup.Type getType(@NotNull ItemGroup group) {
        if (group == null)
            throw new NullPointerException("ItemGroup cannot be null");
        return group.getType();
    }

    /**
     * Checks if an item group is a search tab.
     *
     * @param group The item group
     * @return true if the group is a search tab
     *
     * @example
     *
     *          <pre>{@code
     * if (ItemGroupHelper.isSearchGroup(group)) {
     *     LOGGER.info("This is the search tab");
     *          }
     * }</pre>
     */
    public static boolean isSearchGroup(@NotNull ItemGroup group) {
        return getType(group) == ItemGroup.Type.SEARCH;
    }

    /**
     * Checks if an item group is a category tab.
     *
     * @param group The item group
     * @return true if the group is a category tab
     *
     * @example
     *
     *          <pre>{@code
     * if (ItemGroupHelper.isCategoryGroup(group)) {
     *     // This is a normal category tab
     *          }
     * }</pre>
     */
    public static boolean isCategoryGroup(@NotNull ItemGroup group) {
        return getType(group) == ItemGroup.Type.CATEGORY;
    }

    /**
     * Checks if an item group is in the top row.
     *
     * @param group The item group
     * @return true if the group is in the top row
     *
     * @example
     *
     *          <pre>{@code
     * if (ItemGroupHelper.isTopRow(group)) {
     *     LOGGER.info("Group is in top row");
     *          }
     * }</pre>
     */
    public static boolean isTopRow(@NotNull ItemGroup group) {
        return getRow(group) == ItemGroup.Row.TOP;
    }

    /**
     * Checks if an item group is in the bottom row.
     *
     * @param group The item group
     * @return true if the group is in the bottom row
     *
     * @example
     *
     *          <pre>{@code
     * if (ItemGroupHelper.isBottomRow(group)) {
     *     LOGGER.info("Group is in bottom row");
     *          }
     * }</pre>
     */
    public static boolean isBottomRow(@NotNull ItemGroup group) {
        return getRow(group) == ItemGroup.Row.BOTTOM;
    }

    /**
     * Checks if an item group contains a specific item.
     *
     * @param group The item group
     * @param item The item to check for
     * @return true if the group contains the item
     * @throws NullPointerException if group or item is null
     *
     * @example
     *
     *          <pre>{@code
     * if (ItemGroupHelper.contains(group, Items.DIAMOND_SWORD)) {
     *     LOGGER.info("Group contains diamond sword");
     *          }
     * }</pre>
     */
    public static boolean contains(@NotNull ItemGroup group, @NotNull Item item) {
        if (group == null)
            throw new NullPointerException("ItemGroup cannot be null");
        if (item == null)
            throw new NullPointerException("Item cannot be null");

        return group.contains(new ItemStack(item));
    }

    /**
     * Checks if an item group contains a specific item stack.
     *
     * @param group The item group
     * @param stack The item stack to check for
     * @return true if the group contains the item stack
     * @throws NullPointerException if group or stack is null
     *
     * @example
     *
     *          <pre>{@code
     * if (ItemGroupHelper.contains(group, stack)) {
     *     LOGGER.info("Group contains this item");
     *          }
     * }</pre>
     */
    public static boolean contains(@NotNull ItemGroup group, @NotNull ItemStack stack) {
        if (group == null)
            throw new NullPointerException("ItemGroup cannot be null");
        if (stack == null)
            throw new NullPointerException("ItemStack cannot be null");

        return group.contains(stack);
    }

    /**
     * Gets all registered item groups.
     *
     * @return An iterable of all item groups
     *
     * @example
     *
     *          <pre>{@code
     * for (ItemGroup group : ItemGroupHelper.getAllGroups()) {
     *          LOGGER.info("Group: {} - {}", ItemGroupHelper.getId(group),
     *          ItemGroupHelper.getDisplayName(group).getString());
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<ItemGroup> getAllGroups() {
        return Registries.ITEM_GROUP;
    }

    /**
     * Gets all item group registry keys.
     *
     * @return An iterable of all item group keys
     *
     * @example
     *
     *          <pre>{@code
     * for (RegistryKey<ItemGroup> key : ItemGroupHelper.getAllKeys()) {
     *          LOGGER.info("Item group key: {}", key.getValue());
     *          }
     * }</pre>
     */
    @NotNull
    public static Iterable<RegistryKey<ItemGroup>> getAllKeys() {
        return Registries.ITEM_GROUP.getKeys();
    }

    /**
     * Gets the number of registered item groups.
     *
     * @return The count of item groups
     *
     * @example
     *
     *          <pre>{@code
     * int count = ItemGroupHelper.getGroupCount();
     *          LOGGER.info("Total item groups: {}", count);
     * }</pre>
     */
    public static int getGroupCount() {
        return Registries.ITEM_GROUP.size();
    }

    /**
     * Checks if an item group is special (hotbar, inventory, or search).
     *
     * @param group The item group
     * @return true if the group is special
     *
     * @example
     *
     *          <pre>{@code
     * if (ItemGroupHelper.isSpecial(group)) {
     *     // This is a special system tab
     *          }
     * }</pre>
     */
    public static boolean isSpecial(@NotNull ItemGroup group) {
        if (group == null)
            throw new NullPointerException("ItemGroup cannot be null");
        return group.isSpecial();
    }
}
