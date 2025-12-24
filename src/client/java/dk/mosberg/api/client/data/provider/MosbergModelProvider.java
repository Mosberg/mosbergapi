package dk.mosberg.api.client.data.provider;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.item.Item;

/**
 * Enhanced model provider with helper methods for MosbergAPI
 */
public abstract class MosbergModelProvider extends FabricModelProvider {

    public MosbergModelProvider(FabricDataOutput output) {
        super(output);
    }

    /**
     * Helper method to register a simple cube_all block model
     */
    protected void registerSimpleBlock(BlockStateModelGenerator generator, Block block) {
        generator.registerSimpleCubeAll(block);
    }

    /**
     * Helper method to register a simple generated item model
     */
    protected void registerSimpleItem(ItemModelGenerator generator, Item item) {
        generator.register(item, Models.GENERATED);
    }

    /**
     * Helper method to register a handheld item model (for tools)
     */
    protected void registerHandheldItem(ItemModelGenerator generator, Item item) {
        generator.register(item, Models.HANDHELD);
    }

    /**
     * Helper method to register a block item (uses parent block model)
     */
    protected void registerBlockItem(ItemModelGenerator generator, Block block) {
        generator.register(block.asItem(), Models.GENERATED);
    }
}
