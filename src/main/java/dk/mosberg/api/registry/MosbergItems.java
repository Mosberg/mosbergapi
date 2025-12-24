package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom items in MosbergAPI. Register your items here using the provided
 * helper methods.
 */
public class MosbergItems {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergItems.class);

    /**
     * Registers an item with a simple name.
     *
     * @param name The item name (will be prefixed with mod ID)
     * @param item The Item to register
     * @param <T> The item class type
     * @return The registered Item
     */
    public static <T extends Item> T register(String name, T item) {
        Identifier id = Identifier.of("mosbergapi", name);
        T registeredItem = Registry.register(Registries.ITEM, id, item);
        LOGGER.debug("Registered item: {}", name);
        return registeredItem;
    }

    /**
     * Initialize and register all items. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI items");
        // Item registration happens when register() is called

        // Example:
        // public static final Item EXAMPLE_ITEM = register("example_item",
        // new Item(new Item.Settings()));
    }
}
