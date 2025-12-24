package dk.mosberg.api.client.data;

import dk.mosberg.api.data.MosbergApiLootTableProvider;
import dk.mosberg.api.data.MosbergApiRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MosbergApiDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		// Server-side providers (recipes, loot tables)
		pack.addProvider(MosbergApiRecipeProvider::new);
		pack.addProvider(MosbergApiLootTableProvider::new);

		// Client-side provider (models)
		pack.addProvider(MosbergApiModelProvider::new);
	}
}
