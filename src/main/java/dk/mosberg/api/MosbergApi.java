package dk.mosberg.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.api.registry.MosbergAttributes;
import dk.mosberg.api.registry.MosbergBlockEntities;
import dk.mosberg.api.registry.MosbergBlocks;
import dk.mosberg.api.registry.MosbergCommands;
import dk.mosberg.api.registry.MosbergDamageTypes;
import dk.mosberg.api.registry.MosbergEnchantments;
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
import dk.mosberg.api.registry.MosbergTags;
import dk.mosberg.api.registry.MosbergVillagers;
import dk.mosberg.api.registry.MosbergWorldGen;
import net.fabricmc.api.ModInitializer;

public class MosbergApi implements ModInitializer {
	public static final String MOD_ID = "mosbergapi";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing MosbergAPI");

		// Core registries (order matters for dependencies)
		MosbergBlocks.initialize();
		MosbergItems.initialize();
		MosbergFluids.initialize();
		MosbergEntities.initialize();
		MosbergBlockEntities.initialize();

		// Effect & gameplay registries
		MosbergStatusEffects.initialize();
		MosbergPotions.initialize();
		MosbergAttributes.initialize();
		MosbergGameEvents.initialize();

		// Content registries
		MosbergParticles.initialize();
		MosbergSounds.initialize();
		MosbergRecipes.initialize();
		MosbergScreenHandlerTypes.initialize();
		MosbergItemGroups.initialize();

		// Villager & world gen (data-driven)
		MosbergVillagers.initialize();
		MosbergWorldGen.initialize();

		// Data-driven registries (references only)
		MosbergEnchantments.initialize();
		MosbergDamageTypes.initialize();
		MosbergTags.initialize();
		MosbergCommands.initialize();

		LOGGER.info("MosbergAPI initialized successfully");
	}

}
