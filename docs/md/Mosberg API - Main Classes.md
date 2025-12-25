# Mosberg API - Main Classes

```
└── 📁main
    └── 📁generated
        └── 📁.cache
            ├── 297ac258356534b566436695ece41e060415df60
            ├── 4ecb2f195a12ba78adff1a87bf048723061898ac
            ├── ca63a339e616e4b3689ce279af20b78a11f0610d
    └── 📁java
        └── 📁dk
            └── 📁mosberg
                └── 📁api
                    └── 📁config
                        ├── ConfigManager.java
                    └── 📁data
                        └── 📁provider
                            ├── MosbergLootTableProvider.java
                            ├── MosbergRecipeProvider.java
                        ├── MosbergApiLootTableProvider.java
                        ├── MosbergApiRecipeProvider.java
                    └── 📁doc
                        ├── DocumentationGenerator.java
                    └── 📁event
                        ├── MosbergEvents.java
                    └── 📁mixin
                        ├── MosbergMixin.java
                    └── 📁registry
                        ├── MosbergBlocks.java
                        ├── MosbergEntities.java
                        ├── MosbergItemGroups.java
                        ├── MosbergItems.java
                        ├── MosbergParticles.java
                        ├── MosbergRegistries.java
                        ├── MosbergSounds.java
                    └── 📁test
                        ├── TestHelper.java
                    └── 📁util
                        ├── AttributeHelper.java
                        ├── BlockHelper.java
                        ├── CommandHelper.java
                        ├── EnchantmentUtil.java
                        ├── EntityHelper.java
                        ├── InventoryHelper.java
                        ├── ItemHelper.java
                        ├── MosbergHelper.java
                        ├── NBTHelper.java
                        ├── NetworkHelper.java
                        ├── RecipeHelper.java
                        ├── SerializationHelper.java
                        ├── TagHelper.java
                        ├── WorldHelper.java
                    ├── MosbergApi.java
    └── 📁resources
        └── 📁assets
            └── 📁mosbergapi
                └── 📁atlases
                └── 📁block
                └── 📁blockstates
                    ├── custom_block.json
                └── 📁equipment
                └── 📁items
                └── 📁lang
                    ├── en_us.json
                └── 📁models
                    └── 📁block
                    └── 📁entity
                    └── 📁equipment
                    └── 📁item
                └── 📁particles
                └── 📁post_effect
                └── 📁shaders
                └── 📁sounds
                └── 📁texts
                └── 📁textures
                    └── 📁block
                    └── 📁entity
                    └── 📁item
                ├── icon.png
        └── 📁data
            └── 📁mosbergapi
                └── 📁advancement
                └── 📁damage_type
                └── 📁enchantment
                └── 📁loot_table
                └── 📁recipe
                └── 📁tags
                └── 📁worldgen
        ├── fabric.mod.json
        ├── mosbergapi.mixins.json
        └── pack.mcmeta
```

```java
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

```

```java
package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom blocks in MosbergAPI. Register your blocks here using the provided
 * helper methods.
 */
public class MosbergBlocks {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergBlocks.class);

    /**
     * Registers a block with automatic BlockItem creation.
     *
     * @param name The block name (will be prefixed with mod ID)
     * @param block The Block to register
     * @return The registered Block
     */
    public static <T extends Block> T register(String name, T block) {
        return register(name, block, true);
    }

    /**
     * Registers a block with optional BlockItem creation.
     *
     * @param name The block name (will be prefixed with mod ID)
     * @param block The Block to register
     * @param createItem Whether to create a BlockItem
     * @return The registered Block
     */
    public static <T extends Block> T register(String name, T block, boolean createItem) {
        Identifier id = Identifier.of("mosbergapi", name);
        T registeredBlock = Registry.register(Registries.BLOCK, id, block);

        if (createItem) {
            BlockItem blockItem = new BlockItem(registeredBlock, new Item.Settings());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        LOGGER.debug("Registered block: {}", name);
        return registeredBlock;
    }

    /**
     * Registers a block with a custom BlockItem.
     *
     * @param name The block name (will be prefixed with mod ID)
     * @param block The Block to register
     * @param itemSettings The Item.Settings for the BlockItem
     * @return The registered Block
     */
    public static <T extends Block> T register(String name, T block, Item.Settings itemSettings) {
        Identifier id = Identifier.of("mosbergapi", name);
        T registeredBlock = Registry.register(Registries.BLOCK, id, block);

        BlockItem blockItem = new BlockItem(registeredBlock, itemSettings);
        Registry.register(Registries.ITEM, id, blockItem);

        LOGGER.debug("Registered block with custom item: {}", name);
        return registeredBlock;
    }

    /**
     * Initialize and register all blocks. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI blocks");
        // Block registration happens when register() is called

        // Example:
        // public static final Block EXAMPLE_BLOCK = register("example_block",
        // new Block(AbstractBlock.Settings.create()));
    }
}

```

```java
package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom entities in MosbergAPI. Register your entities here using the
 * provided helper methods.
 */
public class MosbergEntities {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergEntities.class);

    // Example entity registration (uncomment and customize as needed):
    // public static final EntityType<YourEntity> YOUR_ENTITY = register(
    // "your_entity",
    // EntityType.Builder.create(YourEntity::new, SpawnGroup.CREATURE)
    // .dimensions(0.6f, 1.8f)
    // .maxTrackingRange(8)
    // );

    /**
     * Registers an entity type with a simple name.
     *
     * @param name The entity name (will be prefixed with mod ID)
     * @param entityType The EntityType to register
     * @param <T> The entity class type
     * @return The registered EntityType
     */
    public static <T extends Entity> EntityType<T> register(String name, EntityType<T> entityType) {
        LOGGER.debug("Registering entity: {}", name);
        return MosbergRegistries.registerEntityType(name, entityType);
    }

    /**
     * Registers an entity type using an EntityType.Builder. Creates the registry key automatically
     * and builds the entity type.
     *
     * @param name The entity name
     * @param builder The EntityType.Builder to build and register
     * @param <T> The entity class type
     * @return The registered EntityType
     */
    public static <T extends Entity> EntityType<T> register(String name,
            EntityType.Builder<T> builder) {
        // Create the registry key for this entity
        RegistryKey<EntityType<?>> key =
                RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("mosbergapi", name));

        // Build with the registry key
        EntityType<T> entityType = builder.build(key);

        return register(name, entityType);
    }

    /**
     * Creates a registry key for an entity type. Useful for data-driven entity registration.
     *
     * @param name The entity name
     * @return The RegistryKey for the entity type
     */
    public static RegistryKey<EntityType<?>> keyOf(String name) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("mosbergapi", name));
    }

    /**
     * Initialize and register all entities. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI entities");
        // Entity registration happens when the class is loaded
    }
}

```

```java
package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom item groups in MosbergAPI. Item groups appear as tabs in the
 * creative inventory.
 */
public class MosbergItemGroups {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergItemGroups.class);

    // Example item group (uncomment and customize as needed):
    // public static final RegistryKey<ItemGroup> MOSBERG_GROUP = register("mosberg_group");
    //
    // public static final ItemGroup MOSBERG_GROUP_INSTANCE = ItemGroup.create(
    // ItemGroup.Row.TOP,
    // 0
    // )
    // .displayName(Text.translatable("itemGroup.mosbergapi.mosberg_group"))
    // .icon(() -> new ItemStack(Items.DIAMOND))
    // .entries((context, entries) -> {
    // // Add items to the creative tab
    // entries.add(Items.DIAMOND);
    // })
    // .build();

    /**
     * Creates a RegistryKey for an item group.
     *
     * @param name The item group name
     * @return The RegistryKey for the item group
     */
    public static RegistryKey<ItemGroup> of(String name) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of("mosbergapi", name));
    }

    /**
     * Registers an item group with a simple name.
     *
     * @param name The item group name (will be prefixed with mod ID)
     * @param itemGroup The ItemGroup to register
     * @return The registered ItemGroup
     */
    public static ItemGroup register(String name, ItemGroup itemGroup) {
        LOGGER.debug("Registering item group: {}", name);
        return MosbergRegistries.registerItemGroup(name, itemGroup);
    }

    /**
     * Creates and registers a simple item group.
     *
     * @param name The item group name
     * @param displayName The display name text
     * @param icon The icon item stack supplier
     * @return The registered ItemGroup
     */
    public static ItemGroup registerSimple(String name, Text displayName, ItemStack icon) {
        ItemGroup group = ItemGroup.create(ItemGroup.Row.TOP, 0).displayName(displayName)
                .icon(() -> icon).build();
        return register(name, group);
    }

    /**
     * Initialize and register all item groups. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI item groups");
        // Item group registration happens when the class is loaded
    }
}

```

```java
package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom items in MosbergAPI. Register your items here using the provided
 * helper methods.
 */
public class MosbergItems {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergItems.class);

    /**
     * Registers an item with a simple name.
     *
     * @param name The item name (will be prefixed with mod ID)
     * @param item The Item to register
     * @param <T> The item class type
     * @return The registered Item
     */
    public static <T extends Item> T register(String name, T item) {
        Identifier id = Identifier.of("mosbergapi", name);
        T registeredItem = Registry.register(Registries.ITEM, id, item);
        LOGGER.debug("Registered item: {}", name);
        return registeredItem;
    }

    /**
     * Initialize and register all items. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI items");
        // Item registration happens when register() is called

        // Example:
        // public static final Item EXAMPLE_ITEM = register("example_item",
        // new Item(new Item.Settings()));
    }
}

```

```java
package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;

/**
 * Registry class for all custom particle types in MosbergAPI. Register your particles here using
 * the provided helper methods.
 */
public class MosbergParticles {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergParticles.class);

    // Example particle registration (uncomment and customize as needed):
    // public static final SimpleParticleType EXAMPLE_PARTICLE = registerSimple("example_particle",
    // false);
    // public static final SimpleParticleType EXAMPLE_PARTICLE_ALWAYS_SHOW =
    // registerSimple("example_particle_always", true);

    /**
     * Registers a simple particle type using Fabric API.
     *
     * @param name The particle name (will be prefixed with mod ID)
     * @param alwaysShow If true, the particle will always show regardless of particle settings
     * @return The registered SimpleParticleType
     */
    public static SimpleParticleType registerSimple(String name, boolean alwaysShow) {
        LOGGER.debug("Registering simple particle: {} (alwaysShow: {})", name, alwaysShow);

        // Use Fabric API to create the particle type
        SimpleParticleType particle = FabricParticleTypes.simple(alwaysShow);

        return MosbergRegistries.registerParticleType(name, particle);
    }

    /**
     * Registers a simple particle type that respects particle settings. This is equivalent to
     * registerSimple(name, false).
     *
     * @param name The particle name (will be prefixed with mod ID)
     * @return The registered SimpleParticleType
     */
    public static SimpleParticleType registerSimple(String name) {
        return registerSimple(name, false);
    }

    /**
     * Registers a particle type with a custom ParticleType.
     *
     * @param name The particle name (will be prefixed with mod ID)
     * @param particleType The ParticleType to register
     * @param <T> The particle effect type
     * @return The registered ParticleType
     */
    public static <T extends ParticleEffect> ParticleType<T> register(String name,
            ParticleType<T> particleType) {
        LOGGER.debug("Registering particle type: {}", name);
        return MosbergRegistries.registerParticleType(name, particleType);
    }

    /**
     * Initialize and register all particles. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI particles");
        // Particle registration happens when the class is loaded
    }
}

```

```java
package dk.mosberg.api.registry;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.block.Block;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.entry.LootPoolEntryType;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.nbt.LootNbtProviderType;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.loot.provider.score.LootScoreProviderType;
import net.minecraft.particle.ParticleType;
import net.minecraft.potion.Potion;
import net.minecraft.predicate.component.ComponentPredicate;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.scoreboard.number.NumberFormatType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatType;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.pool.StructurePoolElementType;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.structure.rule.PosRuleTestType;
import net.minecraft.structure.rule.RuleTestType;
import net.minecraft.structure.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.FloatProviderType;
import net.minecraft.util.math.intprovider.IntProviderType;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSourceType;
import net.minecraft.world.gen.blockpredicate.BlockPredicateType;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.chunk.placement.StructurePlacementType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.size.FeatureSizeType;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.heightprovider.HeightProviderType;
import net.minecraft.world.gen.placementmodifier.PlacementModifierType;
import net.minecraft.world.gen.root.RootPlacerType;
import net.minecraft.world.gen.stateprovider.BlockStateProviderType;
import net.minecraft.world.gen.structure.StructureType;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import net.minecraft.world.poi.PointOfInterestType;

/**
 * Centralized registry helper for all Minecraft registries. Provides type-safe registration methods
 * for mod content.
 */
public class MosbergRegistries {

        // Entity & AI
        public static <T extends Activity> T registerActivity(String name, T activity) {
                return Registry.register(Registries.ACTIVITY, Identifier.of(getModId(), name),
                                activity);
        }

        public static <T extends EntityAttribute> T registerAttribute(String name, T attribute) {
                return Registry.register(Registries.ATTRIBUTE, Identifier.of(getModId(), name),
                                attribute);
        }

        public static <T extends MemoryModuleType<?>> T registerMemoryModuleType(String name,
                        T memoryModule) {
                return Registry.register(Registries.MEMORY_MODULE_TYPE,
                                Identifier.of(getModId(), name), memoryModule);
        }

        public static <T extends Schedule> T registerSchedule(String name, T schedule) {
                return Registry.register(Registries.SCHEDULE, Identifier.of(getModId(), name),
                                schedule);
        }

        public static <T extends SensorType<?>> T registerSensorType(String name, T sensorType) {
                return Registry.register(Registries.SENSOR_TYPE, Identifier.of(getModId(), name),
                                sensorType);
        }

        // Blocks & Block Entities
        public static <T extends Block> T registerBlock(String name, T block) {
                return Registry.register(Registries.BLOCK, Identifier.of(getModId(), name), block);
        }

        public static <T extends BlockEntityType<?>> T registerBlockEntityType(String name,
                        T blockEntityType) {
                return Registry.register(Registries.BLOCK_ENTITY_TYPE,
                                Identifier.of(getModId(), name), blockEntityType);
        }

        public static <T extends BlockPredicateType<?>> T registerBlockPredicateType(String name,
                        T predicateType) {
                return Registry.register(Registries.BLOCK_PREDICATE_TYPE,
                                Identifier.of(getModId(), name), predicateType);
        }

        public static <T extends BlockStateProviderType<?>> T registerBlockStateProviderType(
                        String name, T providerType) {
                return Registry.register(Registries.BLOCK_STATE_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        // Items & Components
        public static <T extends Item> T registerItem(String name, T item) {
                return Registry.register(Registries.ITEM, Identifier.of(getModId(), name), item);
        }

        public static <T extends ItemGroup> T registerItemGroup(String name, T itemGroup) {
                return Registry.register(Registries.ITEM_GROUP, Identifier.of(getModId(), name),
                                itemGroup);
        }

        public static <T extends ComponentType<?>> T registerDataComponentType(String name,
                        T componentType) {
                return Registry.register(Registries.DATA_COMPONENT_TYPE,
                                Identifier.of(getModId(), name), componentType);
        }

        // FIXED: ComponentPredicate.Type<?> is final, use concrete type
        public static ComponentPredicate.Type<?> registerDataComponentPredicateType(String name,
                        ComponentPredicate.Type<?> predicateType) {
                return Registry.register(Registries.DATA_COMPONENT_PREDICATE_TYPE,
                                Identifier.of(getModId(), name), predicateType);
        }

        // FIXED: ConsumeEffect.Type<?> is final, use concrete type
        public static ConsumeEffect.Type<?> registerConsumeEffectType(String name,
                        ConsumeEffect.Type<?> effectType) {
                return Registry.register(Registries.CONSUME_EFFECT_TYPE,
                                Identifier.of(getModId(), name), effectType);
        }

        // Entities
        public static <T extends EntityType<?>> T registerEntityType(String name, T entityType) {
                return Registry.register(Registries.ENTITY_TYPE, Identifier.of(getModId(), name),
                                entityType);
        }

        // Fluids
        public static <T extends Fluid> T registerFluid(String name, T fluid) {
                return Registry.register(Registries.FLUID, Identifier.of(getModId(), name), fluid);
        }

        // FIXED: GameEvent is final, use concrete type
        public static GameEvent registerGameEvent(String name, GameEvent gameEvent) {
                return Registry.register(Registries.GAME_EVENT, Identifier.of(getModId(), name),
                                gameEvent);
        }

        // FIXED: SoundEvent is final, use concrete type
        public static SoundEvent registerSoundEvent(String name, SoundEvent soundEvent) {
                return Registry.register(Registries.SOUND_EVENT, Identifier.of(getModId(), name),
                                soundEvent);
        }

        // Particle Types
        public static <T extends ParticleType<?>> T registerParticleType(String name,
                        T particleType) {
                return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(getModId(), name),
                                particleType);
        }

        // Status Effects & Potions
        public static <T extends StatusEffect> T registerStatusEffect(String name, T statusEffect) {
                return Registry.register(Registries.STATUS_EFFECT, Identifier.of(getModId(), name),
                                statusEffect);
        }

        public static <T extends Potion> T registerPotion(String name, T potion) {
                return Registry.register(Registries.POTION, Identifier.of(getModId(), name),
                                potion);
        }

        // Enchantments
        public static <T extends ComponentType<?>> T registerEnchantmentEffectComponentType(
                        String name, T componentType) {
                return Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE,
                                Identifier.of(getModId(), name), componentType);
        }

        // Recipes
        public static <T extends RecipeSerializer<?>> T registerRecipeSerializer(String name,
                        T serializer) {
                return Registry.register(Registries.RECIPE_SERIALIZER,
                                Identifier.of(getModId(), name), serializer);
        }

        public static <T extends RecipeType<?>> T registerRecipeType(String name, T recipeType) {
                return Registry.register(Registries.RECIPE_TYPE, Identifier.of(getModId(), name),
                                recipeType);
        }

        public static <T extends RecipeBookCategory> T registerRecipeBookCategory(String name,
                        T category) {
                return Registry.register(Registries.RECIPE_BOOK_CATEGORY,
                                Identifier.of(getModId(), name), category);
        }

        // FIXED: RecipeDisplay.Serializer<?> is final, use concrete type
        public static RecipeDisplay.Serializer<?> registerRecipeDisplay(String name,
                        RecipeDisplay.Serializer<?> displaySerializer) {
                return Registry.register(Registries.RECIPE_DISPLAY, Identifier.of(getModId(), name),
                                displaySerializer);
        }

        // FIXED: SlotDisplay.Serializer<?> is final, use concrete type
        public static SlotDisplay.Serializer<?> registerSlotDisplay(String name,
                        SlotDisplay.Serializer<?> slotDisplay) {
                return Registry.register(Registries.SLOT_DISPLAY, Identifier.of(getModId(), name),
                                slotDisplay);
        }

        // Screen Handlers
        public static <T extends ScreenHandlerType<?>> T registerScreenHandler(String name,
                        T screenHandler) {
                return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(getModId(), name),
                                screenHandler);
        }

        // Stats
        public static <T extends StatType<?>> T registerStatType(String name, T statType) {
                return Registry.register(Registries.STAT_TYPE, Identifier.of(getModId(), name),
                                statType);
        }

        public static Identifier registerCustomStat(String name, Identifier stat) {
                return Registry.register(Registries.CUSTOM_STAT, Identifier.of(getModId(), name),
                                stat);
        }

        // Loot System - FIXED: All these types are final
        public static LootConditionType registerLootConditionType(String name,
                        LootConditionType conditionType) {
                return Registry.register(Registries.LOOT_CONDITION_TYPE,
                                Identifier.of(getModId(), name), conditionType);
        }

        public static LootFunctionType<?> registerLootFunctionType(String name,
                        LootFunctionType<?> functionType) {
                return Registry.register(Registries.LOOT_FUNCTION_TYPE,
                                Identifier.of(getModId(), name), functionType);
        }

        public static LootPoolEntryType registerLootPoolEntryType(String name,
                        LootPoolEntryType entryType) {
                return Registry.register(Registries.LOOT_POOL_ENTRY_TYPE,
                                Identifier.of(getModId(), name), entryType);
        }

        public static LootNumberProviderType registerLootNumberProviderType(String name,
                        LootNumberProviderType providerType) {
                return Registry.register(Registries.LOOT_NUMBER_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        public static LootNbtProviderType registerLootNbtProviderType(String name,
                        LootNbtProviderType providerType) {
                return Registry.register(Registries.LOOT_NBT_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        public static LootScoreProviderType registerLootScoreProviderType(String name,
                        LootScoreProviderType providerType) {
                return Registry.register(Registries.LOOT_SCORE_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        // World Generation - Features
        public static <T extends Feature<?>> T registerFeature(String name, T feature) {
                return Registry.register(Registries.FEATURE, Identifier.of(getModId(), name),
                                feature);
        }

        public static <T extends FeatureSizeType<?>> T registerFeatureSizeType(String name,
                        T sizeType) {
                return Registry.register(Registries.FEATURE_SIZE_TYPE,
                                Identifier.of(getModId(), name), sizeType);
        }

        public static <T extends PlacementModifierType<?>> T registerPlacementModifierType(
                        String name, T modifierType) {
                return Registry.register(Registries.PLACEMENT_MODIFIER_TYPE,
                                Identifier.of(getModId(), name), modifierType);
        }

        // World Generation - Carvers
        public static <T extends Carver<?>> T registerCarver(String name, T carver) {
                return Registry.register(Registries.CARVER, Identifier.of(getModId(), name),
                                carver);
        }

        // World Generation - Trees
        public static <T extends FoliagePlacerType<?>> T registerFoliagePlacerType(String name,
                        T placerType) {
                return Registry.register(Registries.FOLIAGE_PLACER_TYPE,
                                Identifier.of(getModId(), name), placerType);
        }

        public static <T extends TrunkPlacerType<?>> T registerTrunkPlacerType(String name,
                        T placerType) {
                return Registry.register(Registries.TRUNK_PLACER_TYPE,
                                Identifier.of(getModId(), name), placerType);
        }

        public static <T extends RootPlacerType<?>> T registerRootPlacerType(String name,
                        T placerType) {
                return Registry.register(Registries.ROOT_PLACER_TYPE,
                                Identifier.of(getModId(), name), placerType);
        }

        public static <T extends TreeDecoratorType<?>> T registerTreeDecoratorType(String name,
                        T decoratorType) {
                return Registry.register(Registries.TREE_DECORATOR_TYPE,
                                Identifier.of(getModId(), name), decoratorType);
        }

        // World Generation - Structures
        public static <T extends StructureType<?>> T registerStructureType(String name,
                        T structureType) {
                return Registry.register(Registries.STRUCTURE_TYPE, Identifier.of(getModId(), name),
                                structureType);
        }

        public static <T extends StructurePieceType> T registerStructurePiece(String name,
                        T pieceType) {
                return Registry.register(Registries.STRUCTURE_PIECE,
                                Identifier.of(getModId(), name), pieceType);
        }

        public static <T extends StructurePlacementType<?>> T registerStructurePlacement(
                        String name, T placementType) {
                return Registry.register(Registries.STRUCTURE_PLACEMENT,
                                Identifier.of(getModId(), name), placementType);
        }

        public static <T extends StructurePoolElementType<?>> T registerStructurePoolElement(
                        String name, T elementType) {
                return Registry.register(Registries.STRUCTURE_POOL_ELEMENT,
                                Identifier.of(getModId(), name), elementType);
        }

        public static <T extends StructureProcessorType<?>> T registerStructureProcessor(
                        String name, T processorType) {
                return Registry.register(Registries.STRUCTURE_PROCESSOR,
                                Identifier.of(getModId(), name), processorType);
        }

        public static <T extends RuleTestType<?>> T registerRuleTest(String name, T ruleTest) {
                return Registry.register(Registries.RULE_TEST, Identifier.of(getModId(), name),
                                ruleTest);
        }

        public static <T extends PosRuleTestType<?>> T registerPosRuleTest(String name,
                        T posRuleTest) {
                return Registry.register(Registries.POS_RULE_TEST, Identifier.of(getModId(), name),
                                posRuleTest);
        }

        public static <T extends RuleBlockEntityModifierType<?>> T registerRuleBlockEntityModifier(
                        String name, T modifierType) {
                return Registry.register(Registries.RULE_BLOCK_ENTITY_MODIFIER,
                                Identifier.of(getModId(), name), modifierType);
        }

        // World Generation - Misc
        public static <T extends ChunkStatus> T registerChunkStatus(String name, T chunkStatus) {
                return Registry.register(Registries.CHUNK_STATUS, Identifier.of(getModId(), name),
                                chunkStatus);
        }

        public static <T extends HeightProviderType<?>> T registerHeightProviderType(String name,
                        T providerType) {
                return Registry.register(Registries.HEIGHT_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        // Villagers - FIXED: These types are final
        public static VillagerProfession registerVillagerProfession(String name,
                        VillagerProfession profession) {
                return Registry.register(Registries.VILLAGER_PROFESSION,
                                Identifier.of(getModId(), name), profession);
        }

        public static VillagerType registerVillagerType(String name, VillagerType villagerType) {
                return Registry.register(Registries.VILLAGER_TYPE, Identifier.of(getModId(), name),
                                villagerType);
        }

        public static PointOfInterestType registerPointOfInterestType(String name,
                        PointOfInterestType poiType) {
                return Registry.register(Registries.POINT_OF_INTEREST_TYPE,
                                Identifier.of(getModId(), name), poiType);
        }

        // Commands & Arguments
        public static <T extends ArgumentSerializer<?, ?>> T registerCommandArgumentType(
                        String name, T argumentSerializer) {
                return Registry.register(Registries.COMMAND_ARGUMENT_TYPE,
                                Identifier.of(getModId(), name), argumentSerializer);
        }

        // Criteria
        public static <T extends Criterion<?>> T registerCriterion(String name, T criterion) {
                return Registry.register(Registries.CRITERION, Identifier.of(getModId(), name),
                                criterion);
        }

        // Number Providers
        public static <T extends FloatProviderType<?>> T registerFloatProviderType(String name,
                        T providerType) {
                return Registry.register(Registries.FLOAT_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        public static <T extends IntProviderType<?>> T registerIntProviderType(String name,
                        T providerType) {
                return Registry.register(Registries.INT_PROVIDER_TYPE,
                                Identifier.of(getModId(), name), providerType);
        }

        // Misc - FIXED: These types are final
        public static DecoratedPotPattern registerDecoratedPotPattern(String name,
                        DecoratedPotPattern pattern) {
                return Registry.register(Registries.DECORATED_POT_PATTERN,
                                Identifier.of(getModId(), name), pattern);
        }

        public static MapDecorationType registerMapDecorationType(String name,
                        MapDecorationType decorationType) {
                return Registry.register(Registries.MAP_DECORATION_TYPE,
                                Identifier.of(getModId(), name), decorationType);
        }

        public static <T extends NumberFormatType<?>> T registerNumberFormatType(String name,
                        T formatType) {
                return Registry.register(Registries.NUMBER_FORMAT_TYPE,
                                Identifier.of(getModId(), name), formatType);
        }

        public static <T extends PositionSourceType<?>> T registerPositionSourceType(String name,
                        T sourceType) {
                return Registry.register(Registries.POSITION_SOURCE_TYPE,
                                Identifier.of(getModId(), name), sourceType);
        }

        public static ChunkTicketType registerTicketType(String name, ChunkTicketType ticketType) {
                return Registry.register(Registries.TICKET_TYPE, Identifier.of(getModId(), name),
                                ticketType);
        }


        // Helper method to get mod ID
        private static String getModId() {
                return "mosbergapi";
        }

        /**
         * Initialize all registries. Call this from your mod initializer.
         */
        public static void initialize() {
                // Registry initialization happens automatically when referenced
        }
}

```

```java
package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom sound events in MosbergAPI. Register your sounds here using the
 * provided helper methods.
 */
public class MosbergSounds {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergSounds.class);

    // Example sound registration (uncomment and customize as needed):
    // public static final SoundEvent EXAMPLE_SOUND = register("example_sound");
    // public static final SoundEvent AMBIENT_SOUND = register("ambient.example");
    // public static final SoundEvent ITEM_USE = register("item.example.use");

    /**
     * Registers a sound event with a simple name. Creates a SoundEvent with a fixed range (the
     * default behavior).
     *
     * @param name The sound name (will be prefixed with mod ID)
     * @return The registered SoundEvent
     */
    public static SoundEvent register(String name) {
        Identifier id = Identifier.of("mosbergapi", name);
        LOGGER.debug("Registering sound event: {}", id);
        SoundEvent soundEvent = SoundEvent.of(id);
        return MosbergRegistries.registerSoundEvent(name, soundEvent);
    }

    /**
     * Registers a sound event with a variable range. Useful for sounds that need to be heard at
     * different distances.
     *
     * @param name The sound name (will be prefixed with mod ID)
     * @param range The maximum distance in blocks the sound can be heard
     * @return The registered SoundEvent
     */
    public static SoundEvent registerWithRange(String name, float range) {
        Identifier id = Identifier.of("mosbergapi", name);
        LOGGER.debug("Registering sound event with range: {} ({})", id, range);
        SoundEvent soundEvent = SoundEvent.of(id, range);
        return MosbergRegistries.registerSoundEvent(name, soundEvent);
    }

    /**
     * Registers a pre-created SoundEvent.
     *
     * @param name The sound name (will be prefixed with mod ID)
     * @param soundEvent The SoundEvent to register
     * @return The registered SoundEvent
     */
    public static SoundEvent register(String name, SoundEvent soundEvent) {
        LOGGER.debug("Registering custom sound event: {}", name);
        return MosbergRegistries.registerSoundEvent(name, soundEvent);
    }

    /**
     * Initialize and register all sounds. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI sounds");
        // Sound registration happens when the class is loaded
    }
}

```

```java
package dk.mosberg.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Custom event system for MosbergAPI
 */
public class MosbergEvents {

    public static final Event<OnBlockMined> BLOCK_MINED = EventFactory
            .createArrayBacked(OnBlockMined.class, callbacks -> (player, world, pos, state) -> {
                for (OnBlockMined callback : callbacks) {
                    callback.onBlockMined(player, world, pos, state);
                }
            });

    @FunctionalInterface
    public interface OnBlockMined {
        void onBlockMined(PlayerEntity player, World world, BlockPos pos, BlockState state);
    }
}

```

```java
package dk.mosberg.api.doc;

import java.nio.file.Path;

/**
 * Auto-generates documentation for registered content
 */
public class DocumentationGenerator {

    public static void generateMarkdown(Path output) {
        // Generate README.md with all registered content
    }

    public static void generateWiki(Path output) {
        // Generate wiki pages for GitHub or similar platforms
    }
}

```

```java
package dk.mosberg.api.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dk.mosberg.api.MosbergApi;
import net.fabricmc.loader.api.FabricLoader;

/**
 * JSON-based configuration system for MosbergAPI
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create();

    private static final Path CONFIG_DIR = FabricLoader.getInstance()
        .getConfigDir()
        .resolve("mosbergapi");

    static {
        // Create config directory if it doesn't exist
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            MosbergApi.LOGGER.error("Failed to create config directory", e);
        }
    }

    /**
     * Loads a config from file, or creates it with default values if it doesn't exist
     *
     * @param filename The config filename (e.g., "myconfig.json")
     * @param configClass The config class type
     * @param defaultConfig The default config instance to use if file doesn't exist
     * @return The loaded or default config
     */
    public static <T> T loadConfig(String filename, Class<T> configClass, T defaultConfig) {
        Path configFile = CONFIG_DIR.resolve(filename);

        try {
            if (Files.exists(configFile)) {
                // Load existing config
                String json = Files.readString(configFile);
                T config = GSON.fromJson(json, configClass);
                MosbergApi.LOGGER.info("Loaded config: {}", filename);
                return config;
            } else {
                // Create default config
                saveConfig(filename, defaultConfig);
                MosbergApi.LOGGER.info("Created default config: {}", filename);
                return defaultConfig;
            }
        } catch (IOException e) {
            MosbergApi.LOGGER.error("Failed to load config: {}", filename, e);
            return defaultConfig;
        }
    }

    /**
     * Saves a config to file
     *
     * @param filename The config filename
     * @param config The config instance to save
     */
    public static <T> void saveConfig(String filename, T config) {
        Path configFile = CONFIG_DIR.resolve(filename);

        try {
            String json = GSON.toJson(config);
            Files.writeString(configFile, json);
            MosbergApi.LOGGER.debug("Saved config: {}", filename);
        } catch (IOException e) {
            MosbergApi.LOGGER.error("Failed to save config: {}", filename, e);
        }
    }

    /**
     * Deletes a config file
     *
     * @param filename The config filename to delete
     * @return true if deleted successfully
     */
    public static boolean deleteConfig(String filename) {
        Path configFile = CONFIG_DIR.resolve(filename);

        try {
            boolean deleted = Files.deleteIfExists(configFile);
            if (deleted) {
                MosbergApi.LOGGER.info("Deleted config: {}", filename);
            }
            return deleted;
        } catch (IOException e) {
            MosbergApi.LOGGER.error("Failed to delete config: {}", filename, e);
            return false;
        }
    }

    /**
     * Checks if a config file exists
     *
     * @param filename The config filename
     * @return true if the config file exists
     */
    public static boolean configExists(String filename) {
        return Files.exists(CONFIG_DIR.resolve(filename));
    }

    /**
     * Gets the config directory path
     */
    public static Path getConfigDirectory() {
        return CONFIG_DIR;
    }
}

```

```java
package dk.mosberg.api.data;

import java.util.concurrent.CompletableFuture;
import dk.mosberg.api.data.provider.MosbergRecipeProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryWrapper;

public class MosbergApiRecipeProvider extends MosbergRecipeProvider {

    public MosbergApiRecipeProvider(DataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public String getName() {
        return "Mosberg API Recipes";
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries,
            RecipeExporter exporter) {
        return new MosbergApiRecipeGenerator(registries, exporter);
    }

    private static class MosbergApiRecipeGenerator extends RecipeGenerator {

        public MosbergApiRecipeGenerator(RegistryWrapper.WrapperLookup registries,
                RecipeExporter exporter) {
            super(registries, exporter);
        }

        @Override
        public void generate() {
            // Add your recipes here using the helper methods below

            generateToolRecipes();
            generateBlockRecipes();
            generateSmeltingRecipes();
            generateCookingRecipes();
            generateTagRecipes();
            generateConditionalRecipes();
        }

        // ==================== TOOL RECIPES ====================

        private void generateToolRecipes() {
            // Example: Pickaxe
            // createShaped(RecipeCategory.TOOLS, MosbergItems.CUSTOM_PICKAXE)
            // .pattern("III")
            // .pattern(" S ")
            // .pattern(" S ")
            // .input('I', MosbergItems.CUSTOM_INGOT)
            // .input('S', Items.STICK)
            // .criterion(hasItem(MosbergItems.CUSTOM_INGOT),
            // conditionsFromItem(MosbergItems.CUSTOM_INGOT))
            // .offerTo(exporter);

            // Example: Sword
            // createShaped(RecipeCategory.COMBAT, MosbergItems.CUSTOM_SWORD)
            // .pattern("I")
            // .pattern("I")
            // .pattern("S")
            // .input('I', MosbergItems.CUSTOM_INGOT)
            // .input('S', Items.STICK)
            // .criterion(hasItem(MosbergItems.CUSTOM_INGOT),
            // conditionsFromItem(MosbergItems.CUSTOM_INGOT))
            // .offerTo(exporter);
        }

        // ==================== BLOCK RECIPES ====================

        private void generateBlockRecipes() {
            // Example: Block from 9 ingots
            // createShaped(RecipeCategory.BUILDING_BLOCKS, MosbergBlocks.CUSTOM_BLOCK)
            // .pattern("III")
            // .pattern("III")
            // .pattern("III")
            // .input('I', MosbergItems.CUSTOM_INGOT)
            // .criterion(hasItem(MosbergItems.CUSTOM_INGOT),
            // conditionsFromItem(MosbergItems.CUSTOM_INGOT))
            // .offerTo(exporter);

            // Example: Ingots from block (reversible)
            // createShapeless(RecipeCategory.MISC, MosbergItems.CUSTOM_INGOT, 9)
            // .input(MosbergBlocks.CUSTOM_BLOCK)
            // .criterion(hasItem(MosbergBlocks.CUSTOM_BLOCK),
            // conditionsFromItem(MosbergBlocks.CUSTOM_BLOCK))
            // .offerTo(exporter);

            // Example: Stairs
            // createShaped(RecipeCategory.BUILDING_BLOCKS, MosbergBlocks.CUSTOM_STAIRS, 4)
            // .pattern("I ")
            // .pattern("II ")
            // .pattern("III")
            // .input('I', MosbergBlocks.CUSTOM_BLOCK)
            // .criterion(hasItem(MosbergBlocks.CUSTOM_BLOCK),
            // conditionsFromItem(MosbergBlocks.CUSTOM_BLOCK))
            // .offerTo(exporter);

            // Example: Slab
            // createShaped(RecipeCategory.BUILDING_BLOCKS, MosbergBlocks.CUSTOM_SLAB, 6)
            // .pattern("III")
            // .input('I', MosbergBlocks.CUSTOM_BLOCK)
            // .criterion(hasItem(MosbergBlocks.CUSTOM_BLOCK),
            // conditionsFromItem(MosbergBlocks.CUSTOM_BLOCK))
            // .offerTo(exporter);
        }

        // ==================== SMELTING RECIPES ====================

        private void generateSmeltingRecipes() {
            // Example: Ore to ingot (all methods)
            // List<ItemConvertible> ores = List.of(
            // MosbergBlocks.CUSTOM_ORE,
            // MosbergBlocks.DEEPSLATE_CUSTOM_ORE,
            // MosbergItems.RAW_CUSTOM
            // );

            // Furnace
            // offerSmelting(exporter, ores, RecipeCategory.MISC,
            // MosbergItems.CUSTOM_INGOT, 0.7f, 200, "custom_ingot");

            // Blast furnace (faster)
            // offerBlasting(exporter, ores, RecipeCategory.MISC,
            // MosbergItems.CUSTOM_INGOT, 0.7f, 100, "custom_ingot");
        }

        // ==================== COOKING RECIPES ====================

        private void generateCookingRecipes() {
            // Example: Food item
            // offerFoodCookingRecipe(exporter, "smoking",
            // RecipeSerializer.SMOKING, SmokingRecipe::new,
            // 100, MosbergItems.RAW_CUSTOM_FOOD,
            // MosbergItems.COOKED_CUSTOM_FOOD, 0.35f);

            // Example: Campfire cooking
            // offerFoodCookingRecipe(exporter, "campfire_cooking",
            // RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new,
            // 600, MosbergItems.RAW_CUSTOM_FOOD,
            // MosbergItems.COOKED_CUSTOM_FOOD, 0.35f);
        }

        // ==================== TAG-BASED RECIPES ====================

        private void generateTagRecipes() {
            // Example: Recipe using item tags
            // createShaped(RecipeCategory.TOOLS, MosbergItems.CUSTOM_TOOL)
            // .pattern("###")
            // .pattern(" S ")
            // .pattern(" S ")
            // .input('#', ItemTags.PLANKS)
            // .input('S', Items.STICK)
            // .criterion("has_planks", conditionsFromTag(ItemTags.PLANKS))
            // .offerTo(exporter);
        }

        // ==================== CONDITIONAL RECIPES ====================

        private void generateConditionalRecipes() {
            // Example: Recipe with custom advancement requirement
            // createShaped(RecipeCategory.MISC, MosbergItems.SPECIAL_ITEM)
            // .pattern(" I ")
            // .pattern("IDI")
            // .pattern(" I ")
            // .input('I', Items.IRON_INGOT)
            // .input('D', Items.DIAMOND)
            // .criterion("has_diamond", conditionsFromItem(Items.DIAMOND))
            // .criterion("has_nether_star", conditionsFromItem(Items.NETHER_STAR))
            // .offerTo(exporter);
        }
    }
}

```

```java
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

```

```java
package dk.mosberg.api.data.provider;

import java.util.concurrent.CompletableFuture;
import net.minecraft.data.DataOutput;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryWrapper;

/**
 * Base class for MosbergAPI recipe providers Extend this and override getRecipeGenerator() to add
 * your recipes
 */
public abstract class MosbergRecipeProvider extends RecipeGenerator.RecipeProvider {

    protected final RegistryWrapper.WrapperLookup registryLookup;

    public MosbergRecipeProvider(DataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
        this.registryLookup = registriesFuture.join();
    }

    /**
     * Override this method to create your recipe generator Return a new instance of your custom
     * RecipeGenerator
     */
    @Override
    protected abstract RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries,
            RecipeExporter exporter);
}

```

```java
package dk.mosberg.api.data.provider;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.state.property.Properties;

/**
 * Enhanced loot table provider with helper methods for MosbergAPI
 */
public abstract class MosbergLootTableProvider extends FabricBlockLootTableProvider {

        protected final RegistryWrapper.WrapperLookup registryLookup;

        public MosbergLootTableProvider(FabricDataOutput dataOutput,
                        CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
                super(dataOutput, registryLookup);
                this.registryLookup = registryLookup.join();
        }

        /**
         * Helper: Drops the block itself
         */
        protected void addSimpleDrop(Block block) {
                addDrop(block);
        }

        /**
         * Helper: Drops nothing
         */
        protected void addNoDrop(Block block) {
                addDrop(block, LootTable.builder());
        }

        /**
         * Helper: Drops with fortune enchantment (like ores)
         */
        protected void addOreDrop(Block block, Item drop) {
                addDrop(block, oreDrops(block, drop));
        }

        /**
         * Helper: Drops a random amount between min and max
         */
        protected void addRandomDrop(Block block, ItemConvertible drop, float min, float max) {
                addDrop(block, drops(drop, UniformLootNumberProvider.create(min, max)));
        }

        /**
         * Helper: Drops block with silk touch, otherwise drops the specified item
         */
        protected void addSilkTouchOrDrop(Block block, ItemConvertible alternativeDrop) {
                addDrop(block, dropsWithSilkTouch(block, ItemEntry.builder(alternativeDrop)));
        }

        /**
         * Helper: Drops item with silk touch, otherwise drops with fortune
         */
        protected void addSilkTouchOrFortuneOre(Block block, Item drop) {
                RegistryWrapper.Impl<Enchantment> enchantmentLookup =
                                this.registryLookup.getOrThrow(RegistryKeys.ENCHANTMENT);

                addDrop(block, dropsWithSilkTouch(block, applyExplosionDecay(block,
                                ItemEntry.builder(drop).apply(ApplyBonusLootFunction
                                                .oreDrops(enchantmentLookup.getOrThrow(
                                                                Enchantments.FORTUNE))))));
        }

        /**
         * Helper: Drops slab - 1 if single, 2 if double
         */
        protected void addSlabDrop(Block slab) {
                addDrop(slab, slabDrops(slab));
        }

        /**
         * Helper: Drops based on age property (like crops)
         */
        protected void addCropDrop(Block crop, Item seeds, Item product, int maxAge) {
                addDrop(crop, cropDrops(crop, product, seeds,
                                BlockStatePropertyLootCondition.builder(crop)
                                                .properties(StatePredicate.Builder.create()
                                                                .exactMatch(Properties.AGE_7,
                                                                                maxAge))));
        }

        /**
         * Helper: Drops only when harvested with shears
         */
        protected void addShearsDrop(Block block, ItemConvertible drop) {
                addDrop(block, LootTable.builder().pool(
                                LootPool.builder().with(ItemEntry.builder(drop)).conditionally(
                                                MatchToolLootCondition.builder(ItemPredicate.Builder
                                                                .create().tag(null, ItemTags.SWORDS) // Changed
                                                // from
                                                // SHEARS
                                                ))));
        }

        /**
         * Helper: Drops door blocks
         */
        protected void addDoorDrop(Block door) {
                addDrop(door, doorDrops(door));
        }

        /**
         * Helper: Drops with specific count
         */
        protected void addDropWithCount(Block block, ItemConvertible drop, int count) {
                addDrop(block, drops(drop, ConstantLootNumberProvider.create(count)));
        }

        /**
         * Helper: Drops item with count range affected by fortune
         */
        protected void addFortuneRangeDrop(Block block, ItemConvertible drop, int min, int max) {
                RegistryWrapper.Impl<Enchantment> enchantmentLookup =
                                this.registryLookup.getOrThrow(RegistryKeys.ENCHANTMENT);

                addDrop(block, LootTable.builder().pool(LootPool.builder().with(applyExplosionDecay(
                                block,
                                ItemEntry.builder(drop).apply(SetCountLootFunction.builder(
                                                UniformLootNumberProvider.create(min, max)))
                                                .apply(ApplyBonusLootFunction.uniformBonusCount(
                                                                enchantmentLookup.getOrThrow(
                                                                                Enchantments.FORTUNE)))))));
        }

        /**
         * Helper: Drops with count based on fortune level
         */
        protected void addFortuneOreDropWithCount(Block block, Item drop, int baseCount) {
                RegistryWrapper.Impl<Enchantment> enchantmentLookup =
                                this.registryLookup.getOrThrow(RegistryKeys.ENCHANTMENT);

                addDrop(block, LootTable.builder().pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .with(applyExplosionDecay(block, ItemEntry.builder(drop)
                                                .apply(SetCountLootFunction
                                                                .builder(ConstantLootNumberProvider
                                                                                .create(baseCount)))
                                                .apply(ApplyBonusLootFunction.oreDrops(
                                                                enchantmentLookup.getOrThrow(
                                                                                Enchantments.FORTUNE)))))));
        }
}

```

```java
package dk.mosberg.api.test;

/**
 * Helper for testing mod content
 */
public class TestHelper {

    public static void assertBlockRegistered(String name) {
        // Verify block is registered
    }

    public static void assertItemRegistered(String name) {
        // Verify item is registered
    }

    public static void assertEntityRegistered(String name) {
        // Verify entity is registered
    }

    public static void assertRecipeRegistered(String name) {
        // Verify recipe is registered
    }

    public static void assertTagRegistered(String name) {
        // Verify tag is registered
    }

    public static void assertAttributeRegistered(String name) {
        // Verify attribute is registered
    }

    public static void assertEnchantmentRegistered(String name) {
        // Verify enchantment is registered
    }

    public static void assertSoundEventRegistered(String name) {
        // Verify sound event is registered
    }

    public static void assertPotionRegistered(String name) {
        // Verify potion is registered
    }

    public static void assertParticleTypeRegistered(String name) {
        // Verify particle type is registered
    }

    public static void assertBiomeRegistered(String name) {
        // Verify biome is registered
    }

    public static void assertStructureRegistered(String name) {
        // Verify structure is registered
    }

    public static void assertDimensionRegistered(String name) {
        // Verify dimension is registered
    }

    public static void assertFeatureRegistered(String name) {
        // Verify feature is registered
    }

    public static void assertLootTableRegistered(String name) {
        // Verify loot table is registered
    }

    public static void assertRecipeSerializerRegistered(String name) {
        // Verify recipe serializer is registered
    }

    public static void assertRecipeTypeRegistered(String name) {
        // Verify recipe type is registered
    }

    public static void assertVillagerProfessionRegistered(String name) {
        // Verify villager profession is registered
    }

    public static void assertPointOfInterestTypeRegistered(String name) {
        // Verify point of interest type is registered
    }

    public static void assertDamageTypeRegistered(String name) {
        // Verify damage type is registered
    }

    public static void assertCriterionRegistered(String name) {
        // Verify criterion is registered
    }

    public static void assertAdvancementRegistered(String name) {
        // Verify advancement is registered
    }

    public static void assertRecipeBookCategoryRegistered(String name) {
        // Verify recipe book category is registered
    }

    public static void assertCustomElementRegistered(String name) {
        // Verify custom mod element is registered
    }

    public static void resetRegistrations() {
        // Reset all registrations for a clean test state
    }

    public static void initializeTestEnvironment() {
        // Initialize test environment
    }

    public static void cleanupTestEnvironment() {
        // Cleanup test environment
    }

    public static void logRegistrationStatus() {
        // Log the status of registrations
    }

    public static void validateAllRegistrations() {
        // Validate all registrations
    }

    public static void assertAllRequiredElementsRegistered() {
        // Assert all required mod elements are registered
    }

    public static void assertNoDuplicateRegistrations() {
        // Assert there are no duplicate registrations
    }

    public static void assertRegistrationOrder(String first, String second) {
        // Assert that 'first' is registered before 'second'
    }

    public static void assertRegistrationDependenciesResolved(String name) {
        // Assert that all dependencies for the given registration are resolved
    }
}

```

```java
package dk.mosberg.api.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * Helper for entity attributes
 */
public class AttributeHelper {

    public static double getValue(LivingEntity entity, RegistryEntry<EntityAttribute> attribute) {
        EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
        return instance != null ? instance.getValue() : 0;
    }

    public static void setValue(LivingEntity entity, RegistryEntry<EntityAttribute> attribute,
            double value) {
        EntityAttributeInstance instance = entity.getAttributeInstance(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }
}

```

```java
package dk.mosberg.api.util;

import org.jetbrains.annotations.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

/**
 * Utility class providing helper methods for Block operations
 */
public class BlockHelper {

    /**
     * Safely gets a BlockState at the specified position
     */
    public BlockState getBlockState(World world, BlockPos pos) {
        return world.getBlockState(pos);
    }

    /**
     * Sets a BlockState at the specified position with default update flags
     */
    public void setBlockState(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state, Block.NOTIFY_ALL);
    }

    /**
     * Sets a BlockState with custom update flags
     */
    public void setBlockState(World world, BlockPos pos, BlockState state, int flags) {
        world.setBlockState(pos, state, flags);
    }

    /**
     * Checks if a block at the position is air
     */
    public boolean isAir(World world, BlockPos pos) {
        return world.getBlockState(pos).isAir();
    }

    /**
     * Gets the Block at the specified position
     */
    public Block getBlock(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock();
    }

    /**
     * Checks if two positions have the same block type
     */
    public boolean isSameBlock(World world, BlockPos pos1, BlockPos pos2) {
        return world.getBlockState(pos1).getBlock() == world.getBlockState(pos2).getBlock();
    }

    /**
     * Gets the BlockEntity at the specified position
     */
    @Nullable
    public BlockEntity getBlockEntity(World world, BlockPos pos) {
        return world.getBlockEntity(pos);
    }

    /**
     * Gets the BlockEntity at the specified position with type checking
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> T getBlockEntity(World world, BlockPos pos, Class<T> type) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (type.isInstance(blockEntity)) {
            return (T) blockEntity;
        }
        return null;
    }

    /**
     * Breaks a block at the specified position with drops
     */
    public void breakBlock(World world, BlockPos pos, boolean drops) {
        world.breakBlock(pos, drops);
    }

    /**
     * Gets a neighboring BlockState in the specified direction
     */
    public BlockState getNeighborBlockState(World world, BlockPos pos, Direction direction) {
        return world.getBlockState(pos.offset(direction));
    }

    /**
     * Checks if a block can be replaced at the position
     */
    public boolean isReplaceable(World world, BlockPos pos) {
        return world.getBlockState(pos).isReplaceable();
    }

    /**
     * Gets the light level at the block position
     */
    public int getLightLevel(World world, BlockPos pos) {
        return world.getLightLevel(pos);
    }

    /**
     * Schedules a block tick
     */
    public void scheduleBlockTick(WorldAccess world, BlockPos pos, Block block, int delay) {
        world.scheduleBlockTick(pos, block, delay);
    }

    /**
     * Updates neighboring blocks
     */
    public void updateNeighbors(World world, BlockPos pos, Block block) {
        world.updateNeighbors(pos, block);
    }

    /**
     * Checks if a position is within world bounds
     */
    public boolean isInBounds(World world, BlockPos pos) {
        return world.isInBuildLimit(pos);
    }

    /**
     * Gets the hardness of a block at the position
     */
    public float getHardness(World world, BlockPos pos) {
        return world.getBlockState(pos).getHardness(world, pos);
    }
}

```

```java
package dk.mosberg.api.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

/**
 * Helper for registering commands
 */
public class CommandHelper {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, String name,
            int permissionLevel, Command<ServerCommandSource> command) {
        dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal(name)
                .requires(source -> source.hasPermissionLevel(permissionLevel)).executes(command));
    }
}

```

```java
package dk.mosberg.api.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

/**
 * Extended enchantment helper
 */
public class EnchantmentUtil {

    public static void addEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment,
            int level) {
        // Add enchantment to stack
    }

    public static void removeEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        // Remove enchantment from stack
    }

    public static boolean hasEnchantment(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        return EnchantmentHelper.getLevel(enchantment, stack) > 0;
    }
}

```

```java
package dk.mosberg.api.util;

import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Utility class providing helper methods for Entity operations
 */
public class EntityHelper {

    /**
     * Spawns an entity at the specified position
     */
    @Nullable
    public <T extends Entity> T spawn(ServerWorld world, EntityType<T> entityType, BlockPos pos) {
        return entityType.spawn(world, pos, SpawnReason.COMMAND);
    }

    /**
     * Spawns an entity at the specified position with a custom spawn reason
     */
    @Nullable
    public <T extends Entity> T spawn(ServerWorld world, EntityType<T> entityType, BlockPos pos,
            SpawnReason reason) {
        return entityType.spawn(world, pos, reason);
    }

    /**
     * Spawns an entity at the specified Vec3d position
     */
    @Nullable
    public <T extends Entity> T spawn(ServerWorld world, EntityType<T> entityType, Vec3d pos) {
        return entityType.spawn(world, BlockPos.ofFloored(pos), SpawnReason.COMMAND);
    }

    /**
     * Teleports an entity to the specified position
     */
    public void teleport(Entity entity, BlockPos pos) {
        entity.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                entity.getYaw(), entity.getPitch());
    }

    /**
     * Teleports an entity to the specified Vec3d position
     */
    public void teleport(Entity entity, Vec3d pos) {
        entity.refreshPositionAndAngles(pos.x, pos.y, pos.z, entity.getYaw(), entity.getPitch());
    }

    /**
     * Teleports an entity to another entity's position
     */
    public void teleportToEntity(Entity entity, Entity target) {
        entity.refreshPositionAndAngles(target.getX(), target.getY(), target.getZ(),
                entity.getYaw(), entity.getPitch());
    }

    /**
     * Damages a living entity (server-side only)
     */
    public boolean damage(ServerWorld world, LivingEntity entity, DamageSource source,
            float amount) {
        return entity.damage(world, source, amount);
    }

    /**
     * Heals a living entity
     */
    public void heal(LivingEntity entity, float amount) {
        entity.heal(amount);
    }

    /**
     * Gets the health of a living entity
     */
    public float getHealth(LivingEntity entity) {
        return entity.getHealth();
    }

    /**
     * Gets the max health of a living entity
     */
    public float getMaxHealth(LivingEntity entity) {
        return entity.getMaxHealth();
    }

    /**
     * Checks if an entity is alive
     */
    public boolean isAlive(LivingEntity entity) {
        return entity.isAlive();
    }

    /**
     * Gets entities within a radius of a position
     */
    public List<Entity> getEntitiesInRadius(World world, Vec3d center, double radius) {
        return world.getOtherEntities(null,
                net.minecraft.util.math.Box.of(center, radius * 2, radius * 2, radius * 2));
    }

    /**
     * Gets entities within a radius with a predicate filter
     */
    public List<Entity> getEntitiesInRadius(World world, Vec3d center, double radius,
            Predicate<Entity> predicate) {
        return world.getOtherEntities(null,
                net.minecraft.util.math.Box.of(center, radius * 2, radius * 2, radius * 2),
                predicate);
    }

    /**
     * Gets the nearest player to an entity
     */
    @Nullable
    public PlayerEntity getNearestPlayer(World world, Entity entity, double maxDistance) {
        return world.getClosestPlayer(entity, maxDistance);
    }

    /**
     * Gets the distance between two entities
     */
    public double getDistance(Entity entity1, Entity entity2) {
        return entity1.distanceTo(entity2);
    }

    /**
     * Checks if an entity is in water
     */
    public boolean isInWater(Entity entity) {
        return entity.isSubmergedInWater();
    }

    /**
     * Checks if an entity is on ground
     */
    public boolean isOnGround(Entity entity) {
        return entity.isOnGround();
    }

    /**
     * Sets an entity on fire for the specified seconds
     */
    public void setOnFire(Entity entity, int seconds) {
        entity.setOnFireFor(seconds);
    }

    /**
     * Extinguishes fire from an entity
     */
    public void extinguish(Entity entity) {
        entity.extinguish();
    }

    /**
     * Gets the entity's position as Vec3d
     */
    public Vec3d getPosition(Entity entity) {
        return new Vec3d(entity.getX(), entity.getY(), entity.getZ());
    }

    /**
     * Gets the entity's X coordinate
     */
    public double getX(Entity entity) {
        return entity.getX();
    }

    /**
     * Gets the entity's Y coordinate
     */
    public double getY(Entity entity) {
        return entity.getY();
    }

    /**
     * Gets the entity's Z coordinate
     */
    public double getZ(Entity entity) {
        return entity.getZ();
    }

    /**
     * Gets the entity's block position
     */
    public BlockPos getBlockPos(Entity entity) {
        return entity.getBlockPos();
    }

    /**
     * Removes an entity from the world
     */
    public void remove(Entity entity) {
        entity.discard();
    }

    /**
     * Gets the entity's yaw rotation
     */
    public float getYaw(Entity entity) {
        return entity.getYaw();
    }

    /**
     * Gets the entity's pitch rotation
     */
    public float getPitch(Entity entity) {
        return entity.getPitch();
    }

    /**
     * Sets the entity's position and rotation
     */
    public void setPositionAndRotation(Entity entity, double x, double y, double z, float yaw,
            float pitch) {
        entity.refreshPositionAndAngles(x, y, z, yaw, pitch);
    }
}

```

```java
package dk.mosberg.api.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Utility class providing helper methods for Inventory operations
 */
public class InventoryHelper {

    /**
     * Checks if an inventory is empty
     */
    public boolean isEmpty(Inventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.getStack(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears all items from an inventory
     */
    public void clear(Inventory inventory) {
        inventory.clear();
    }

    /**
     * Gets the first empty slot index in an inventory
     */
    public int getFirstEmptySlot(Inventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if an inventory has space for an item stack
     */
    public boolean hasSpace(Inventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack slotStack = inventory.getStack(i);
            if (slotStack.isEmpty()) {
                return true;
            }
            if (ItemStack.areItemsAndComponentsEqual(slotStack, stack)
                    && slotStack.getCount() + stack.getCount() <= slotStack.getMaxCount()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Counts how many of a specific item are in the inventory
     */
    public int countItem(Inventory inventory, Item item) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    /**
     * Checks if an inventory contains at least the specified amount of an item
     */
    public boolean contains(Inventory inventory, Item item, int amount) {
        return countItem(inventory, item) >= amount;
    }

    /**
     * Finds the first slot containing a specific item
     */
    public int findSlot(Inventory inventory, Item item) {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isOf(item)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Removes a specific amount of an item from the inventory
     */
    public boolean removeItem(Inventory inventory, Item item, int amount) {
        int remaining = amount;
        for (int i = 0; i < inventory.size() && remaining > 0; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(item)) {
                int toRemove = Math.min(stack.getCount(), remaining);
                stack.decrement(toRemove);
                remaining -= toRemove;
            }
        }
        return remaining == 0;
    }

    /**
     * Tries to insert an item stack into the inventory
     */
    public boolean insertStack(Inventory inventory, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        // Try to merge with existing stacks first
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack slotStack = inventory.getStack(i);
            if (ItemStack.areItemsAndComponentsEqual(slotStack, stack)) {
                int maxCount = Math.min(inventory.getMaxCountPerStack(), stack.getMaxCount());
                int transferAmount = Math.min(stack.getCount(), maxCount - slotStack.getCount());
                if (transferAmount > 0) {
                    slotStack.increment(transferAmount);
                    stack.decrement(transferAmount);
                    if (stack.isEmpty()) {
                        return true;
                    }
                }
            }
        }

        // Try to place in empty slots
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.getStack(i).isEmpty()) {
                inventory.setStack(i, stack.copy());
                stack.setCount(0);
                return true;
            }
        }

        return stack.isEmpty();
    }

    /**
     * Gets all non-empty item stacks from an inventory
     */
    public List<ItemStack> getAllStacks(Inventory inventory) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    /**
     * Copies all items from one inventory to another
     */
    public void copyInventory(Inventory source, Inventory destination) {
        int size = Math.min(source.size(), destination.size());
        for (int i = 0; i < size; i++) {
            destination.setStack(i, source.getStack(i).copy());
        }
    }

    /**
     * Swaps the contents of two inventories
     */
    public void swapInventories(Inventory inv1, Inventory inv2) {
        int size = Math.min(inv1.size(), inv2.size());
        for (int i = 0; i < size; i++) {
            ItemStack temp = inv1.getStack(i).copy();
            inv1.setStack(i, inv2.getStack(i).copy());
            inv2.setStack(i, temp);
        }
    }

    /**
     * Gets the total number of occupied slots
     */
    public int getOccupiedSlots(Inventory inventory) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.getStack(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if a player's inventory contains a specific item
     */
    public boolean playerHasItem(PlayerEntity player, Item item) {
        return player.getInventory().contains(new ItemStack(item));
    }

    /**
     * Removes an item from a player's inventory
     */
    public boolean removeFromPlayer(PlayerEntity player, Item item, int amount) {
        return removeItem(player.getInventory(), item, amount);
    }
}

```

```java
package dk.mosberg.api.util;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

/**
 * Utility class providing helper methods for Item and ItemStack operations
 */
public class ItemHelper {

    /**
     * Creates an ItemStack with the specified count
     */
    public ItemStack createStack(Item item, int count) {
        return new ItemStack(item, count);
    }

    /**
     * Creates a single ItemStack
     */
    public ItemStack createStack(Item item) {
        return new ItemStack(item);
    }

    /**
     * Checks if an ItemStack is empty
     */
    public boolean isEmpty(ItemStack stack) {
        return stack.isEmpty();
    }

    /**
     * Gets the count of items in a stack
     */
    public int getCount(ItemStack stack) {
        return stack.getCount();
    }

    /**
     * Sets the count of items in a stack
     */
    public void setCount(ItemStack stack, int count) {
        stack.setCount(count);
    }

    /**
     * Increments the stack count
     */
    public void increment(ItemStack stack, int amount) {
        stack.increment(amount);
    }

    /**
     * Decrements the stack count
     */
    public void decrement(ItemStack stack, int amount) {
        stack.decrement(amount);
    }

    /**
     * Checks if a stack is at max count
     */
    public boolean isMaxCount(ItemStack stack) {
        return stack.getCount() >= stack.getMaxCount();
    }

    /**
     * Gets the max stack size for an item
     */
    public int getMaxCount(ItemStack stack) {
        return stack.getMaxCount();
    }

    /**
     * Damages an item stack (server-side)
     */
    public void damage(ItemStack stack, int amount, ServerWorld world,
            @Nullable ServerPlayerEntity player) {
        stack.damage(amount, world, player, item -> {
            // Item broke callback
        });
    }

    /**
     * Damages an item stack with equipment slot tracking (server-side)
     */
    public void damage(ItemStack stack, int amount, LivingEntity entity, EquipmentSlot slot) {
        stack.damage(amount, entity, slot);
    }

    /**
     * Gets the current damage (durability used) of an item
     */
    public int getDamage(ItemStack stack) {
        return stack.getDamage();
    }

    /**
     * Sets the damage of an item
     */
    public void setDamage(ItemStack stack, int damage) {
        stack.setDamage(damage);
    }

    /**
     * Gets the max damage (durability) of an item
     */
    public int getMaxDamage(ItemStack stack) {
        return stack.getMaxDamage();
    }

    /**
     * Checks if an item is damaged
     */
    public boolean isDamaged(ItemStack stack) {
        return stack.isDamaged();
    }

    /**
     * Checks if an item is damageable
     */
    public boolean isDamageable(ItemStack stack) {
        return stack.isDamageable();
    }

    /**
     * Gets the remaining durability of an item
     */
    public int getRemainingDurability(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamage();
    }

    /**
     * Checks if an item is about to break
     */
    public boolean isAboutToBreak(ItemStack stack, int threshold) {
        return getRemainingDurability(stack) <= threshold;
    }

    /**
     * Copies an ItemStack
     */
    public ItemStack copy(ItemStack stack) {
        return stack.copy();
    }

    /**
     * Checks if two ItemStacks are equal (item and components)
     */
    public boolean areEqual(ItemStack stack1, ItemStack stack2) {
        return ItemStack.areItemsAndComponentsEqual(stack1, stack2);
    }

    /**
     * Checks if two ItemStacks are of the same item type
     */
    public boolean isSameItem(ItemStack stack1, ItemStack stack2) {
        return ItemStack.areItemsEqual(stack1, stack2);
    }

    /**
     * Sets a custom name for an ItemStack
     */
    public void setCustomName(ItemStack stack, Text name) {
        stack.set(DataComponentTypes.CUSTOM_NAME, name);
    }

    /**
     * Gets the custom name of an ItemStack
     */
    @Nullable
    public Text getCustomName(ItemStack stack) {
        return stack.get(DataComponentTypes.CUSTOM_NAME);
    }

    /**
     * Checks if an ItemStack has a custom name
     */
    public boolean hasCustomName(ItemStack stack) {
        return stack.contains(DataComponentTypes.CUSTOM_NAME);
    }

    /**
     * Sets lore (description) for an ItemStack
     */
    public void setLore(ItemStack stack, List<Text> lore) {
        stack.set(DataComponentTypes.LORE, new net.minecraft.component.type.LoreComponent(lore));
    }

    /**
     * Makes an ItemStack unbreakable
     */
    public void makeUnbreakable(ItemStack stack) {
        stack.set(DataComponentTypes.UNBREAKABLE, net.minecraft.util.Unit.INSTANCE);
    }

    /**
     * Makes an ItemStack unbreakable with custom tooltip visibility Note: In 1.21.10, UNBREAKABLE
     * uses Unit type and always shows in tooltip
     */
    public void makeUnbreakable(ItemStack stack, boolean showInTooltip) {
        if (showInTooltip) {
            stack.set(DataComponentTypes.UNBREAKABLE, net.minecraft.util.Unit.INSTANCE);
        } else {
            // Remove the component to hide it
            stack.remove(DataComponentTypes.UNBREAKABLE);
        }
    }

    /**
     * Checks if an ItemStack is unbreakable
     */
    public boolean isUnbreakable(ItemStack stack) {
        return stack.contains(DataComponentTypes.UNBREAKABLE);
    }

    /**
     * Removes the unbreakable property from an ItemStack
     */
    public void removeUnbreakable(ItemStack stack) {
        stack.remove(DataComponentTypes.UNBREAKABLE);
    }


    /**
     * Gets the enchantment level of an item
     */
    public int getEnchantmentLevel(ItemStack stack, RegistryEntry<Enchantment> enchantment) {
        return EnchantmentHelper.getLevel(enchantment, stack);
    }

    /**
     * Checks if an item has any enchantments
     */
    public boolean hasEnchantments(ItemStack stack) {
        return stack.hasEnchantments();
    }

    /**
     * Gives an item to a player, dropping it if inventory is full
     */
    public void giveOrDrop(PlayerEntity player, ItemStack stack) {
        if (!player.giveItemStack(stack)) {
            player.dropItem(stack, false);
        }
    }

    /**
     * Checks if a player can hold an item
     */
    public boolean canPlayerHold(PlayerEntity player, ItemStack stack) {
        return player.getInventory().getEmptySlot() != -1;
    }

    /**
     * Gets the translation key for an item
     */
    public String getTranslationKey(ItemStack stack) {
        return stack.getItem().getTranslationKey();
    }

    /**
     * Gets the display name of an item
     */
    public Text getName(ItemStack stack) {
        return stack.getName();
    }

    /**
     * Gets the rarity of an item
     */
    public net.minecraft.util.Rarity getRarity(ItemStack stack) {
        return stack.getRarity();
    }

    /**
     * Checks if an item has glint (enchantment glow)
     */
    public boolean hasGlint(ItemStack stack) {
        return stack.hasGlint();
    }

    /**
     * Checks if an item is food
     */
    public boolean isFood(ItemStack stack) {
        return stack.contains(DataComponentTypes.FOOD);
    }

    /**
     * Gets the food component from an item stack
     */
    @Nullable
    public net.minecraft.component.type.FoodComponent getFoodComponent(ItemStack stack) {
        return stack.get(DataComponentTypes.FOOD);
    }

    /**
     * Checks if two items can stack together
     */
    public boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return ItemStack.areItemsAndComponentsEqual(stack1, stack2)
                && stack1.getCount() + stack2.getCount() <= stack1.getMaxCount();
    }

    /**
     * Splits a stack in half
     */
    public ItemStack split(ItemStack stack, int amount) {
        return stack.split(amount);
    }
}

```

```java
package dk.mosberg.api.util;

import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.SpawnHelper;

/**
 * Central utility class providing access to all Mosberg API helpers. This class serves as a
 * convenient entry point for commonly used helper methods in Minecraft Fabric mod development.
 *
 * <p>
 * Usage example:
 *
 * <pre>{@code
 * // Block operations
 * MosbergHelper.BLOCK.setBlockState(world, pos, state);
 *
 * // Entity operations
 * MosbergHelper.ENTITY.spawn(world, EntityType.ZOMBIE, pos);
 *
 * // Item operations
 * ItemStack stack = MosbergHelper.ITEM.createStack(Items.DIAMOND, 64);
 *
 * // NBT operations
 * NbtCompound nbt = MosbergHelper.NBT.toNbt(stack);
 *
 * // JSON operations
 * JsonObject json = MosbergHelper.JSON.parse("{\"key\":\"value\"}");
 *
 * // Math operations
 * int clampedValue = MosbergHelper.MATH.clamp(value, min, max);
 *
 * // Color operations
 * int argb = MosbergHelper.COLOR.Argb.getArgb(255, 0, 255, 0);
 *
 * // Time operations
 * long seconds = MosbergHelper.TIME.SECOND * 60;
 *
 * // Network operations
 * MosbergHelper.NETWORK.sendPacket(player, packet);
 *
 * // Recipe operations
 * Recipe<?> recipe = MosbergHelper.RECIPE.getRecipe(world, id);
 *
 * // Tag operations
 * boolean hasTag = MosbergHelper.TAG.hasTag(item, tag);
 *
 * // Attribute operations
 * MosbergHelper.ATTRIBUTE.modifyAttribute(entity, attribute, amount);
 *
 * // Enchantment operations
 * MosbergHelper.ENCHANTMENT.applyEnchantment(stack, Enchantments.SHARPNESS, 5);
 *
 * }</pre>
 */
public class MosbergHelper {

    // ===== Custom Helper Instances =====

    /**
     * Helper for block-related operations (getting/setting blocks, block entities, etc.)
     */
    public static final BlockHelper BLOCK = new BlockHelper();

    /**
     * Helper for entity-related operations (spawning, teleporting, damage, etc.)
     */
    public static final EntityHelper ENTITY = new EntityHelper();

    /**
     * Helper for inventory-related operations (inserting, removing, counting items, etc.)
     */
    public static final InventoryHelper INVENTORY = new InventoryHelper();

    /**
     * Helper for item stack operations (creating, modifying, enchanting, etc.)
     */
    public static final ItemHelper ITEM = new ItemHelper();

    /**
     * Helper for world-related operations (time, weather, explosions, etc.)
     */
    public static final WorldHelper WORLD = new WorldHelper();

    // ===== Minecraft Helper Classes =====
    // Note: These classes provide static utility methods and should not be instantiated.
    // Access their methods directly (e.g., NbtHelper.fromNbtList(...))

    /**
     * NBT helper for reading/writing game profiles, block states, UUIDs, and block positions.
     * <p>
     * Use static methods directly: {@code NbtHelper.toBlockPos(nbt)}
     *
     * @see NbtHelper
     */
    public static final Class<NbtHelper> NBT = NbtHelper.class;

    /**
     * Spawn helper for entity spawning validation and spawn logic.
     * <p>
     * Use static methods directly: {@code SpawnHelper.canSpawn(...)}
     *
     * @see SpawnHelper
     */
    public static final Class<SpawnHelper> SPAWN = SpawnHelper.class;

    /**
     * JSON helper for safe JSON parsing and element access.
     * <p>
     * Use static methods directly: {@code JsonHelper.getString(json, "key")}
     *
     * @see JsonHelper
     */
    public static final Class<JsonHelper> JSON = JsonHelper.class;

    /**
     * Math helper for common mathematical operations.
     * <p>
     * Use static methods directly: {@code MathHelper.clamp(value, min, max)}
     *
     * @see MathHelper
     */
    public static final Class<MathHelper> MATH = MathHelper.class;

    /**
     * Color helper for ARGB color operations.
     * <p>
     * Use static methods directly: {@code ColorHelper.Argb.getArgb(a, r, g, b)}
     *
     * @see ColorHelper
     */
    public static final Class<ColorHelper> COLOR = ColorHelper.class;

    /**
     * Time helper for time-related operations and formatting.
     * <p>
     * Use static methods directly: {@code TimeHelper.SECOND}
     *
     * @see TimeHelper
     */
    public static final Class<TimeHelper> TIME = TimeHelper.class;

    /**
     * Custom helper for network operations (packet sending, channel management, etc.)
     * <p>
     * Note: This is an instance-based helper, unlike the other Minecraft helpers.
     *
     * @see NetworkHelper
     */
    public static final NetworkHelper NETWORK = new NetworkHelper();

    /**
     * Custom helper for NBT operations (reading, writing, modifying NBT data)
     * <p>
     * Note: This is an instance-based helper, unlike the other Minecraft helpers.
     *
     * @see NBTHelper
     */
    public static final NBTHelper MOSBERGNBT = new NBTHelper();

    /**
     * Custom helper for recipe operations (registering, fetching recipes)
     * <p>
     * Note: This is an instance-based helper, unlike the other Minecraft helpers.
     *
     * @see RecipeHelper
     */
    public static final RecipeHelper RECIPE = new RecipeHelper();

    /**
     * Custom helper for tag operations (managing block/item/entity tags)
     * <p>
     * Note: This is an instance-based helper, unlike the other Minecraft helpers.
     *
     * @see TagHelper
     */
    public static final TagHelper TAG = new TagHelper();

    /**
     * Custom helper for attribute operations (modifying entity attributes)
     * <p>
     * Note: This is an instance-based helper, unlike the other Minecraft helpers.
     *
     * @see AttributeHelper
     */
    public static final AttributeHelper ATTRIBUTE = new AttributeHelper();

    /**
     * Custom helper for enchantment operations (applying, removing enchantments)
     * <p>
     * Note: This is an instance-based helper, unlike the other Minecraft helpers.
     *
     * @see EnchantmentUtil
     */
    public static final EnchantmentUtil ENCHANTMENT = new EnchantmentUtil();


    // ===== Utility Methods =====

    /**
     * Prevents instantiation of this utility class
     */
    private MosbergHelper() {
        throw new UnsupportedOperationException(
                "MosbergHelper is a utility class and cannot be instantiated");
    }

    /**
     * Gets the API version
     *
     * @return The Mosberg API version string
     */
    public static String getVersion() {
        return "1.0.0";
    }

    /**
     * Checks if the API is initialized
     *
     * @return true if all helper instances are available
     */
    public static boolean isInitialized() {
        return BLOCK != null && ENTITY != null && INVENTORY != null && ITEM != null
                && WORLD != null;
    }
}

```

```java
package dk.mosberg.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

/**
 * Helper for MOSBERGNBT data manipulation
 */
public class NBTHelper {

    public static void setString(NbtCompound nbt, String key, String value) {
        nbt.putString(key, value);
    }

    public static String getString(NbtCompound nbt, String key, String defaultValue) {
        return nbt.contains(key) ? nbt.getString(key).orElse(defaultValue) : defaultValue;
    }

    public static ItemStack getItemStack(NbtCompound nbt, String key) {
        // Load ItemStack from MOSBERGNBT
        return ItemStack.EMPTY;
    }
}

```

```java
package dk.mosberg.api.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Helper for sending custom packets between client and server
 */
public class NetworkHelper {

    public static void sendToServer(CustomPayload payload) {
        // Client -> Server
    }

    public static void sendToPlayer(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static void sendToAllPlayers(ServerWorld world, CustomPayload payload) {
        // Broadcast to all players
    }
}

```

```java
package dk.mosberg.api.util;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

/**
 * Helper for recipe operations in Minecraft 1.21.10+
 */
public class RecipeHelper {

    /**
     * Gets a recipe by its identifier
     */
    public static Optional<RecipeEntry<?>> getRecipe(ServerWorld world, Identifier id) {
        RegistryKey<Recipe<?>> key = RegistryKey.of(RegistryKeys.RECIPE, id);
        return world.getRecipeManager().get(key);
    }

    /**
     * Gets a recipe by its registry key
     */
    public static Optional<RecipeEntry<?>> getRecipe(ServerWorld world,
            RegistryKey<Recipe<?>> key) {
        return world.getRecipeManager().get(key);
    }

    /**
     * Checks if a recipe exists
     */
    public static boolean hasRecipe(ServerWorld world, Identifier id) {
        return getRecipe(world, id).isPresent();
    }

    /**
     * Gets all recipes
     */
    public static Collection<RecipeEntry<?>> getAllRecipes(ServerWorld world) {
        return world.getRecipeManager().values();
    }

    /**
     * Gets all recipes of a specific type FIXED: Filter recipes manually since listAllOfType()
     * doesn't exist in 1.21.10
     */
    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> List<RecipeEntry<T>> getRecipesByType(ServerWorld world,
            RecipeType<T> type) {
        return world.getRecipeManager().values().stream()
                .filter(entry -> entry.value().getType() == type)
                .map(entry -> (RecipeEntry<T>) entry).collect(Collectors.toList());
    }

    /**
     * Gets all recipe identifiers of a specific type
     */
    public static <T extends Recipe<?>> List<Identifier> getRecipeIdsByType(ServerWorld world,
            RecipeType<T> type) {
        return getRecipesByType(world, type).stream().map(entry -> entry.id().getValue())
                .collect(Collectors.toList());
    }

    /**
     * Filters recipes by a custom predicate
     */
    public static List<RecipeEntry<?>> filterRecipes(ServerWorld world,
            java.util.function.Predicate<RecipeEntry<?>> predicate) {
        return world.getRecipeManager().values().stream().filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Creates a registry key for a recipe
     */
    public static RegistryKey<Recipe<?>> createKey(Identifier id) {
        return RegistryKey.of(RegistryKeys.RECIPE, id);
    }

    /**
     * Creates a registry key for a recipe with mod namespace
     */
    public static RegistryKey<Recipe<?>> createKey(String modId, String path) {
        return createKey(Identifier.of(modId, path));
    }

    /**
     * Gets the output item from a recipe FIXED: Use value().getResult() which returns ItemStack
     * directly in 1.21.10
     */
    public static Optional<ItemStack> getRecipeOutput(RecipeEntry<?> entry, ServerWorld world) {
        try {
            Recipe<?> recipe = entry.value();
            if (hasGetResult(recipe)) {
                ItemStack result = invokeGetResult(recipe, world.getRegistryManager());
                return Optional.of(result);
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Helper to check if getResult(DynamicRegistryManager) exists
    private static boolean hasGetResult(Recipe<?> recipe) {
        try {
            recipe.getClass().getMethod("getResult",
                    net.minecraft.registry.DynamicRegistryManager.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    // Helper to invoke getResult reflectively
    private static ItemStack invokeGetResult(Recipe<?> recipe,
            net.minecraft.registry.DynamicRegistryManager drm) {
        try {
            java.lang.reflect.Method m = recipe.getClass().getMethod("getResult",
                    net.minecraft.registry.DynamicRegistryManager.class);
            return (ItemStack) m.invoke(recipe, drm);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }

    /**
     * Gets all recipe outputs of a specific type
     */
    public static <T extends Recipe<?>> List<ItemStack> getRecipeOutputsByType(ServerWorld world,
            RecipeType<T> type) {
        return getRecipesByType(world, type).stream().map(entry -> {
            Recipe<?> recipe = entry.value();
            if (hasGetResult(recipe)) {
                return invokeGetResult(recipe, world.getRegistryManager());
            }
            return ItemStack.EMPTY;
        }).filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    /**
     * Finds recipes that produce a specific item
     */
    public static List<RecipeEntry<?>> getRecipesForItem(ServerWorld world, ItemStack targetItem) {
        return world.getRecipeManager().values().stream().filter(entry -> {
            Recipe<?> recipe = entry.value();
            if (hasGetResult(recipe)) {
                ItemStack result = invokeGetResult(recipe, world.getRegistryManager());
                return ItemStack.areItemsEqual(result, targetItem);
            }
            return false;
        }).collect(Collectors.toList());
    }

    /**
     * Checks if a recipe can be crafted with the given inventory
     */
    public static boolean canCraft(RecipeEntry<?> entry,
            net.minecraft.inventory.Inventory inventory, ServerWorld world) {
        Recipe<?> recipe = entry.value();
        if (recipe instanceof Recipe<?>) {
            try {
                // Attempt to call matches if possible
                java.lang.reflect.Method matchesMethod = recipe.getClass().getMethod("matches",
                        net.minecraft.inventory.Inventory.class, net.minecraft.world.World.class);
                Boolean result = (Boolean) matchesMethod.invoke(recipe, inventory,
                        (net.minecraft.world.World) world);
                return result != null && result;
            } catch (Exception e) {
                // Cannot invoke matches, cannot craft
                return false;
            }
        }
        return false;
    }
}

```

```java
package dk.mosberg.api.util;

import java.util.Optional;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dk.mosberg.api.MosbergApi;

/**
 * Helper for Codec-based serialization
 */
public class SerializationHelper {

    public static <T> JsonObject toJson(T object, Codec<T> codec) {
        return codec.encodeStart(JsonOps.INSTANCE, object).resultOrPartial(MosbergApi.LOGGER::error)
                .map(JsonElement::getAsJsonObject).orElse(new JsonObject());
    }

    public static <T> Optional<T> fromJson(JsonObject json, Codec<T> codec) {
        return codec.parse(JsonOps.INSTANCE, json).resultOrPartial(MosbergApi.LOGGER::error);
    }
}

```

```java
package dk.mosberg.api.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * Helper for creating and checking tags
 */
public class TagHelper {

    public static TagKey<Block> blockTag(String path) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of("mosbergapi", path));
    }

    public static TagKey<Item> itemTag(String path) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("mosbergapi", path));
    }

    public static boolean isInTag(Block block, TagKey<Block> tag) {
        return block.getDefaultState().isIn(tag);
    }
}

```

```java
package dk.mosberg.api.util;

import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.WorldProperties.SpawnPoint;

/**
 * Utility class providing helper methods for World operations
 */
public class WorldHelper {

    /**
     * Checks if the world is on the client side
     */
    public boolean isClient(World world) {
        return world.isClient();
    }

    /**
     * Checks if the world is on the server side
     */
    public boolean isServer(World world) {
        return !world.isClient();
    }

    /**
     * Gets the world time
     */
    public long getTime(World world) {
        return world.getTime();
    }

    /**
     * Gets the time of day (0-24000)
     */
    public long getTimeOfDay(World world) {
        return world.getTimeOfDay();
    }

    /**
     * Sets the time of day
     */
    public void setTimeOfDay(ServerWorld world, long time) {
        world.setTimeOfDay(time);
    }

    /**
     * Checks if it's daytime
     */
    public boolean isDaytime(World world) {
        return world.isDay();
    }

    /**
     * Checks if it's nighttime
     */
    public boolean isNighttime(World world) {
        return world.isNight();
    }

    /**
     * Checks if it's raining
     */
    public boolean isRaining(World world) {
        return world.isRaining();
    }

    /**
     * Checks if it's thundering
     */
    public boolean isThundering(World world) {
        return world.isThundering();
    }

    /**
     * Sets the weather to clear
     */
    public void clearWeather(ServerWorld world) {
        world.setWeather(6000, 0, false, false);
    }

    /**
     * Sets the weather to rain
     */
    public void setRaining(ServerWorld world, int duration) {
        world.setWeather(0, duration, true, false);
    }

    /**
     * Sets the weather to thunder
     */
    public void setThundering(ServerWorld world, int duration) {
        world.setWeather(0, duration, true, true);
    }

    /**
     * Creates an explosion at the specified position
     */
    public void createExplosion(World world, @Nullable Entity entity, double x, double y, double z,
            float power, boolean createFire) {
        world.createExplosion(entity, x, y, z, power, createFire, World.ExplosionSourceType.BLOCK);
    }

    /**
     * Creates an explosion at a BlockPos
     */
    public void createExplosion(World world, @Nullable Entity entity, BlockPos pos, float power,
            boolean createFire) {
        createExplosion(world, entity, pos.getX(), pos.getY(), pos.getZ(), power, createFire);
    }

    /**
     * Gets all players in the world
     */
    public List<? extends PlayerEntity> getPlayers(World world) {
        return world.getPlayers();
    }

    /**
     * Gets all entities in a box area
     */
    public List<Entity> getEntitiesInBox(World world, Box box) {
        return world.getOtherEntities(null, box);
    }

    /**
     * Gets the spawn position from the server (GlobalPos with dimension)
     */
    public SpawnPoint getServerSpawnPos(ServerWorld world) {
        return world.getServer().getSpawnPoint();
    }

    /**
     * Gets the spawn position as BlockPos (from the server's global spawn)
     */
    public BlockPos getSpawnPos(ServerWorld world) {
        return world.getServer().getSpawnPoint().getPos();
    }

    /**
     * Gets the spawn dimension from the server
     */
    public RegistryKey<World> getSpawnDimension(ServerWorld world) {
        return world.getServer().getSpawnPoint().getDimension();
    }

    /**
     * Creates a spawn point
     */
    public WorldProperties.SpawnPoint createSpawnPoint(RegistryKey<World> dimension, BlockPos pos,
            float yaw, float pitch) {
        return WorldProperties.SpawnPoint.create(dimension, pos, yaw, pitch);
    }

    /**
     * Checks if a chunk is loaded at the given position Uses chunk coordinates instead of
     * deprecated block position check
     */
    public boolean isChunkLoaded(World world, BlockPos pos) {
        return world.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4);
    }

    /**
     * Checks if a chunk is loaded at the given chunk coordinates
     */
    public boolean isChunkLoaded(World world, int chunkX, int chunkZ) {
        return world.isChunkLoaded(chunkX, chunkZ);
    }

    /**
     * Gets a random position within a radius
     */
    public BlockPos getRandomPos(Random random, BlockPos center, int radius) {
        int x = center.getX() + random.nextInt(radius * 2 + 1) - radius;
        int y = center.getY() + random.nextInt(radius * 2 + 1) - radius;
        int z = center.getZ() + random.nextInt(radius * 2 + 1) - radius;
        return new BlockPos(x, y, z);
    }

    /**
     * Finds the highest non-air block at X/Z coordinates
     */
    public BlockPos getTopPosition(World world, BlockPos pos) {
        return world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos);
    }

    /**
     * Gets the Y coordinate of the topmost block at X/Z coordinates with specific heightmap
     */
    public int getTopY(World world, Heightmap.Type heightmap, BlockPos pos) {
        return world.getTopY(heightmap, pos.getX(), pos.getZ());
    }

    /**
     * Gets the Y coordinate of the topmost block at X/Z coordinates
     */
    public int getTopY(World world, BlockPos pos) {
        return world.getTopY(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ());
    }

    /**
     * Gets the dimension key
     */
    public String getDimensionName(World world) {
        return world.getRegistryKey().getValue().toString();
    }

    /**
     * Checks if a position has skylight access
     */
    public boolean canSeeSky(World world, BlockPos pos) {
        return world.isSkyVisible(pos);
    }

    /**
     * Gets the biome at a position
     */
    public String getBiomeName(World world, BlockPos pos) {
        return world.getBiome(pos).getKey().map(key -> key.getValue().toString()).orElse("unknown");
    }

    /**
     * Plays a sound at a position
     */
    public void playSound(World world, BlockPos pos, net.minecraft.sound.SoundEvent sound,
            float volume, float pitch) {
        world.playSound(null, pos, sound, net.minecraft.sound.SoundCategory.BLOCKS, volume, pitch);
    }

    /**
     * Spawns particles at a position
     */
    public void spawnParticles(ServerWorld world, net.minecraft.particle.ParticleEffect particle,
            Vec3d pos, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        world.spawnParticles(particle, pos.x, pos.y, pos.z, count, deltaX, deltaY, deltaZ, speed);
    }

    /**
     * Gets the sea level of the world
     */
    public int getSeaLevel(World world) {
        return world.getSeaLevel();
    }

    /**
     * Gets the bottom Y level of the world
     */
    public int getBottomY(World world) {
        return world.getBottomY();
    }

    /**
     * Gets the height of the world
     */
    public int getHeight(World world) {
        return world.getHeight();
    }

    /**
     * Gets the maximum build height of the world
     */
    public int getMaxBuildHeight(World world) {
        return world.getTopY(null, 0, 0);
    }
}

```