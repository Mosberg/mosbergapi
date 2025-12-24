package dk.mosberg.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.api.registry.MosbergBlocks;
import dk.mosberg.api.registry.MosbergEntities;
import dk.mosberg.api.registry.MosbergItemGroups;
import dk.mosberg.api.registry.MosbergItems;
import dk.mosberg.api.registry.MosbergParticles;
import dk.mosberg.api.registry.MosbergSounds;
import net.fabricmc.api.ModInitializer;

public class MosbergApi implements ModInitializer {
	public static final String MOD_ID = "mosbergapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing MosbergAPI");

		// Initialize all registries
		// Order can be important due to dependencies
		// Initialize blocks and items first
		// then entities, particles, and sounds
		// finally item groups
		MosbergBlocks.initialize();
		MosbergItems.initialize();
		MosbergEntities.initialize();
		MosbergParticles.initialize();
		MosbergSounds.initialize();
		MosbergItemGroups.initialize();

		LOGGER.info("MosbergAPI initialized successfully");
	}
}
