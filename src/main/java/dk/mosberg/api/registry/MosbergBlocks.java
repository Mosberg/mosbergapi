package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

public class MosbergBlocks {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergBlocks.class);

    public static final Block CUSTOM_BLOCK = MosbergRegistries.registerBlock("custom_block",
            new Block(AbstractBlock.Settings.create()));

    public static void register() {
        // Blocks are registered when the class is loaded
    }

    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI blocks");
        // Block registration happens when the class is loaded
    }
}
