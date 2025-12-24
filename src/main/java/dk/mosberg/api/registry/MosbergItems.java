package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.item.Item;

public class MosbergItems {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergItems.class);

    public static final Item CUSTOM_ITEM =
            MosbergRegistries.registerItem("custom_item", new Item(new Item.Settings()));

    public static void register() {
        // Items are registered when the class is loaded
    }

    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI items");
        // Item registration happens when the class is loaded
    }
}
