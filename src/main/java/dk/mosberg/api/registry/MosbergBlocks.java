package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom blocks in MosbergAPI. Register your blocks here using the provided
 * helper methods.
 */
public class MosbergBlocks {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergBlocks.class);

    /**
     * Registers a block with automatic BlockItem creation.
     *
     * @param name The block name (will be prefixed with mod ID)
     * @param block The Block to register
     * @return The registered Block
     */
    public static <T extends Block> T register(String name, T block) {
        return register(name, block, true);
    }

    /**
     * Registers a block with optional BlockItem creation.
     *
     * @param name The block name (will be prefixed with mod ID)
     * @param block The Block to register
     * @param createItem Whether to create a BlockItem
     * @return The registered Block
     */
    public static <T extends Block> T register(String name, T block, boolean createItem) {
        Identifier id = Identifier.of("mosbergapi", name);
        T registeredBlock = Registry.register(Registries.BLOCK, id, block);

        if (createItem) {
            BlockItem blockItem = new BlockItem(registeredBlock, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        LOGGER.debug("Registered block: {}", name);
        return registeredBlock;
    }

    /**
     * Registers a block with a custom BlockItem.
     *
     * @param name The block name (will be prefixed with mod ID)
     * @param block The Block to register
     * @param itemSettings The Item.Settings for the BlockItem
     * @return The registered Block
     */
    public static <T extends Block> T register(String name, T block, Item.Settings itemSettings) {
        Identifier id = Identifier.of("mosbergapi", name);
        T registeredBlock = Registry.register(Registries.BLOCK, id, block);

        BlockItem blockItem = new BlockItem(registeredBlock, itemSettings);
        Registry.register(Registries.ITEM, id, blockItem);

        LOGGER.debug("Registered block with custom item: {}", name);
        return registeredBlock;
    }

    /**
     * Initialize and register all blocks. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI blocks");
        // Block registration happens when register() is called

        // Example:
        // public static final Block EXAMPLE_BLOCK = register("example_block",
        // new Block(AbstractBlock.Settings.create()));
    }
}
