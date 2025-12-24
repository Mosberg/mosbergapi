package dk.mosberg.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.api.client.registry.MosbergModelLayers;
import dk.mosberg.api.client.registry.MosbergModels;
import dk.mosberg.api.client.registry.MosbergRenderStates;
import dk.mosberg.api.client.registry.MosbergRenderers;
import net.fabricmc.api.ClientModInitializer;

public class MosbergApiClient implements ClientModInitializer {
	public static final String MOD_ID = "mosbergapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Initializing MosbergAPI Client");
		// Initialize all client-side registries
		// Order can be important due to dependencies
		// Initialize model layers first
		// then models, render states, and renderers
		MosbergModelLayers.initialize();
		MosbergModels.initialize();
		MosbergRenderStates.initialize();
		MosbergRenderers.initialize();

		LOGGER.info("MosbergAPI Client initialized successfully");
	}
}
