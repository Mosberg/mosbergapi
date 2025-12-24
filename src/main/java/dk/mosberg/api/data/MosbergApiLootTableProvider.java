package dk.mosberg.api.data;

import java.util.concurrent.CompletableFuture;
import dk.mosberg.api.data.provider.MosbergLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;

public class MosbergApiLootTableProvider extends MosbergLootTableProvider {

    public MosbergApiLootTableProvider(FabricDataOutput dataOutput,
            CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        // Example: Simple block drop
        // addSimpleDrop(MosbergBlocks.CUSTOM_BLOCK);

        // Example: Ore with fortune
        // addOreDrop(MosbergBlocks.CUSTOM_ORE, Items.DIAMOND);

        // Example: Silk touch or alternative drop
        // addSilkTouchOrDrop(MosbergBlocks.CUSTOM_GRASS, Items.DIRT);
    }
}
