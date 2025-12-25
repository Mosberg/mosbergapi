package dk.mosberg.api.registry;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom items in MosbergAPI.
 *
 * <p>
 * This class provides simplified registration methods for items in Minecraft 1.21.10. All items
 * registered through this class are automatically assigned the "mosbergapi" namespace and logged
 * for debugging purposes.
 *
 * <h2>Registration Pattern</h2>
 * <p>
 * Items should be registered as static final fields that are initialized when the class is loaded.
 * This ensures items are registered before they are referenced.
 *
 * <h2>Item Settings</h2>
 * <p>
 * Use {@link net.minecraft.item.Item.Settings} to configure item properties:
 * <ul>
 * <li>Durability: {@code .maxDamage(durability)}</li>
 * <li>Stack size: {@code .maxCount(stackSize)}</li>
 * <li>Rarity: {@code .rarity(Rarity.RARE)}</li>
 * <li>Food component: {@code .food(foodComponent)}</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * public class ModItems {
 *     // Basic item
 *     public static final Item COPPER_INGOT = MosbergItems.register("copper_ingot",
 *         new Item(new Item.Settings()));
 *
 *     // Tool with durability
 *     public static final SwordItem RUBY_SWORD = MosbergItems.register("ruby_sword",
 *         new SwordItem(ToolMaterials.IRON, new Item.Settings()
 *             .maxDamage(500)
 *             .rarity(Rarity.RARE)));
 *
 *     // Food item
 *     public static final Item GOLDEN_APPLE = MosbergItems.register("golden_apple",
 *         new Item(new Item.Settings()
 *             .food(FoodComponents.GOLDEN_APPLE)
 *             .rarity(Rarity.EPIC)));
 *
 *     public static void initialize() {
 *         MosbergItems.initialize();
 *          }
 *          }
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see Item
 * @see net.minecraft.item.Item.Settings
 * @see MosbergItemGroups
 */
public class MosbergItems {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergItems.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Registers an item with a simple name.
     *
     * <p>
     * The item identifier is automatically prefixed with the mod ID ("mosbergapi:"). Registration
     * is logged at DEBUG level for troubleshooting.
     *
     * @param <T> The item class type (Item or subclass)
     * @param name The item name (e.g., "copper_ingot" becomes "mosbergapi:copper_ingot")
     * @param item The Item instance to register
     * @return The registered Item instance
     * @throws NullPointerException if name or item is null
     *
     * @example
     *
     *          <pre>{@code
     * // Register basic item
     * Item copperIngot = MosbergItems.register("copper_ingot",
     *     new Item(new Item.Settings()));
     *
     * // Register tool item
     * SwordItem rubySword = MosbergItems.register("ruby_sword",
     *     new SwordItem(ModToolMaterials.RUBY, new Item.Settings()));
     * }</pre>
     */
    public static <T extends Item> @NotNull T register(@NotNull String name, @NotNull T item) {
        if (name == null) {
            throw new NullPointerException("Item name cannot be null");
        }
        if (item == null) {
            throw new NullPointerException("Item cannot be null");
        }

        Identifier id = Identifier.of(MOD_ID, name);
        T registeredItem = Registry.register(Registries.ITEM, id, item);
        LOGGER.debug("Registered item: {} (class: {})", id, item.getClass().getSimpleName());
        return registeredItem;
    }

    /**
     * Initialize and register all items.
     *
     * <p>
     * Call this method from your mod initializer to trigger static initialization of item fields.
     * This ensures all items are registered before the game attempts to load them.
     *
     * <p>
     * <strong>Note:</strong> Item registration happens when the {@code register()} method is
     * called, typically during static field initialization. This method exists for consistency with
     * other registry classes.
     *
     * @example
     *
     *          <pre>{@code
     *          @Override
     *          public void onInitialize() {
     *          MosbergItems.initialize();
     *          // Items are now registered and ready to use
     *          }
     * }</pre>
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI items");
        // Item registration happens when register() is called
        // This method ensures the class is loaded
    }
}
