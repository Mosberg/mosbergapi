package dk.mosberg.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.api.client.registry.MosbergModelLayers;
import dk.mosberg.api.client.registry.MosbergModels;
import dk.mosberg.api.client.registry.MosbergRenderStates;
import dk.mosberg.api.client.registry.MosbergRenderers;
import dk.mosberg.api.client.registry.MosbergScreenHandlers;
import net.fabricmc.api.ClientModInitializer;

public class MosbergApiClient implements ClientModInitializer {
	public static final String MOD_ID = "mosbergapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing MosbergAPI Client");
		// Initialize client registries
		MosbergModelLayers.initialize(); // Model layers (needed first)
		MosbergModels.initialize(); // Entity models
		MosbergRenderStates.initialize(); // Render states (1.21.2+)
		MosbergRenderers.initialize(); // Entity/block entity renderers
		MosbergScreenHandlers.initialize(); // Screen handlers (GUIs)

		LOGGER.info("MosbergAPI Client initialized successfully");
	}
}
