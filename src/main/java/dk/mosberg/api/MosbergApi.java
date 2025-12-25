package dk.mosberg.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.api.registry.MosbergAttributes;
import dk.mosberg.api.registry.MosbergBlockEntities;
import dk.mosberg.api.registry.MosbergBlocks;
import dk.mosberg.api.registry.MosbergEntities;
import dk.mosberg.api.registry.MosbergFluids;
import dk.mosberg.api.registry.MosbergGameEvents;
import dk.mosberg.api.registry.MosbergItemGroups;
import dk.mosberg.api.registry.MosbergItems;
import dk.mosberg.api.registry.MosbergParticles;
import dk.mosberg.api.registry.MosbergPotions;
import dk.mosberg.api.registry.MosbergRecipes;
import dk.mosberg.api.registry.MosbergScreenHandlerTypes;
import dk.mosberg.api.registry.MosbergSounds;
import dk.mosberg.api.registry.MosbergStatusEffects;
import net.fabricmc.api.ModInitializer;

public class MosbergApi implements ModInitializer {
	public static final String MOD_ID = "mosbergapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing MosbergAPI");

		// Core registries
		MosbergBlocks.initialize();
		MosbergItems.initialize();
		MosbergEntities.initialize();
		MosbergBlockEntities.initialize();

		// Effect registries
		MosbergStatusEffects.initialize();
		MosbergPotions.initialize();

		// Resource registries
		MosbergFluids.initialize();
		MosbergParticles.initialize();
		MosbergSounds.initialize();
		MosbergGameEvents.initialize();

		// Gameplay registries
		MosbergAttributes.initialize();
		MosbergRecipes.initialize();
		MosbergScreenHandlerTypes.initialize();
		MosbergItemGroups.initialize();

		LOGGER.info("MosbergAPI initialized successfully");
	}

}
