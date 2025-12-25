package dk.mosberg.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dk.mosberg.api.registry.MosbergAttributes;
import dk.mosberg.api.registry.MosbergBlockEntities;
import dk.mosberg.api.registry.MosbergBlocks;
import dk.mosberg.api.registry.MosbergCommands;
import dk.mosberg.api.registry.MosbergDamageTypes;
import dk.mosberg.api.registry.MosbergDataComponents;
import dk.mosberg.api.registry.MosbergEnchantments;
import dk.mosberg.api.registry.MosbergEntities;
import dk.mosberg.api.registry.MosbergFluids;
import dk.mosberg.api.registry.MosbergGameEvents;
import dk.mosberg.api.registry.MosbergItemGroups;
import dk.mosberg.api.registry.MosbergItems;
import dk.mosberg.api.registry.MosbergParticles;
import dk.mosberg.api.registry.MosbergPotions;
import dk.mosberg.api.registry.MosbergRecipes;
import dk.mosberg.api.registry.MosbergRegistries;
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

		// Initialize registries in dependency order
		// Core registries first
		MosbergDataComponents.initialize(); // Data components (needed for items)
		MosbergSounds.initialize(); // Sound events
		MosbergParticles.initialize(); // Particle types
		MosbergGameEvents.initialize(); // Game events

		// Block and fluid registries
		MosbergFluids.initialize(); // Fluids (needed for fluid blocks)
		MosbergBlocks.initialize(); // Blocks
		MosbergBlockEntities.initialize(); // Block entities (needs blocks)

		// Item registries
		MosbergItems.initialize(); // Items
		MosbergItemGroups.initialize(); // Creative tabs (needs items)

		// Effect registries
		MosbergStatusEffects.initialize(); // Status effects
		MosbergPotions.initialize(); // Potions (needs status effects)
		MosbergEnchantments.initialize(); // Enchantments

		// Entity registries
		MosbergAttributes.initialize(); // Entity attributes
		MosbergEntities.initialize(); // Entity types (needs attributes)
		MosbergDamageTypes.initialize(); // Damage types

		// Villager registries
		MosbergVillagers.initialize(); // Villagers (needs blocks for POI)

		// Recipe and screen registries
		MosbergRecipes.initialize(); // Recipe serializers
		MosbergScreenHandlerTypes.initialize(); // Screen handlers

		// World generation
		MosbergWorldGen.initialize(); // World gen features

		// Tag registration
		MosbergTags.initialize(); // Tags

		// Command registration
		MosbergCommands.initialize(); // Commands

		// Central registry (general purpose helper)
		MosbergRegistries.initialize();

		LOGGER.info("MosbergAPI initialized successfully");
	}

}
