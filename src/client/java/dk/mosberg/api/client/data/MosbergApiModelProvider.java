package dk.mosberg.api.client.data;

import dk.mosberg.api.client.data.provider.MosbergModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;

public class MosbergApiModelProvider extends MosbergModelProvider {

    public MosbergApiModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Example: Register simple block
        // registerSimpleBlock(blockStateModelGenerator, MosbergBlocks.CUSTOM_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Example: Register simple item
        // registerSimpleItem(itemModelGenerator, MosbergItems.CUSTOM_ITEM);
    }
}
