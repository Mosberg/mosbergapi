# 🏗️ MosbergAPI

> A comprehensive **Fabric API library** for Minecraft 1.21.10+ mod development, designed to eliminate boilerplate code and accelerate mod creation with powerful utilities, enhanced registries, streamlined commands, and advanced client-side systems.

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.10-brightgreen?style=flat-square)](https://www.minecraft.net/)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-0.138.4+-orange?style=flat-square)](https://fabricmc.net/)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-0.18.3+-orange?style=flat-square)](https://fabricmc.net/)
[![Java](https://img.shields.io/badge/Java-21-blue?style=flat-square)](https://adoptium.net/)
[![Yarn Mappings](https://img.shields.io/badge/Yarn-1.21.10+build.3-blue?style=flat-square)](https://maven.fabricmc.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)](LICENSE)

---

## 📋 Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Quick Start](#quick-start)
- [Architecture](#architecture)
- [API Reference](#api-reference)
- [Usage Examples](#usage-examples)
- [Project Structure](#project-structure)
- [Building & Development](#building--development)
- [Contributing](#contributing)
- [Support & Troubleshooting](#support--troubleshooting)
- [License](#license)

---

## 🎯 Overview

**MosbergAPI** is a foundational library mod that provides developers with battle-tested utilities, intelligent registry systems, powerful command framework, and advanced client-side tooling to accelerate Minecraft mod development. It abstracts away common boilerplate, handles edge cases, and follows modern Fabric best practices.

### What It Does

- ✅ **Simplifies Registration** - Register 16+ content types (blocks, items, entities, sounds, particles, fluids, enchantments, and more) with minimal code
- ✅ **Command System** - Built-in command templates for common operations (blocks, items, entities, world, debug)
- ✅ **Data Generation** - Auto-generate recipes, loot tables, and models with fluent builders
- ✅ **Utility Helpers** - 25+ production-ready helper classes for inventory, entities, blocks, NBT, particles, and more
- ✅ **Client-Side Tools** - Advanced rendering, screen handler management, model layers, texture utilities, and render states
- ✅ **Screen Management** - Screen handler registration and custom screen utilities
- ✅ **Event System** - Custom events for mod interactions
- ✅ **Configuration** - JSON-based config management out of the box
- ✅ **Type Safety** - Modern Java 21 with records, pattern matching, sealed classes, and null safety annotations
- ✅ **Comprehensive Logging** - SLF4J integration with appropriate debug levels

### What It's NOT

❌ MosbergAPI adds **no content** to Minecraft by itself  
❌ It's not a gameplay mod—it's a **developer library**  
❌ Not required for vanilla Minecraft—only for mods using it

---

## ✨ Key Features

### 🔧 Registry System (`dk.mosberg.api.registry`)

**16+ type-safe registry classes with automatic registration and builder patterns:**

| Class | Purpose | Key Methods |
|-------|---------|------------|
| **MosbergBlocks** | Block registration with automatic BlockItem | `register(name, block)`, `register(name, block, createItem)`, `register(name, block, itemSettings)` |
| **MosbergItems** | Item registration with centralized management | `register(name, item)`, `getItem(name)` |
| **MosbergEntities** | Entity type registration with builder pattern | `register(name, builder)`, `keyOf(name)`, `getEntityType(name)` |
| **MosbergItemGroups** | Creative inventory tab management | `register(name, group)`, `registerSimple(name, displayName, icon)` |
| **MosbergSounds** | Sound event registration | `register(name)`, `registerWithRange(name, range)` |
| **MosbergParticles** | Custom particle registration | `registerSimple(name)`, `registerSimple(name, alwaysShow)` |
| **MosbergFluids** | Fluid type registration with flowing variants | `register(name, fluid)`, `registerFlowing(name, still, flowing)` |
| **MosbergEnchantments** | Enchantment registration with configuration | `register(name, enchantment)`, `registerWithDescription(name, enchantment, description)` |
| **MosbergStatusEffects** | Status effect registration | `register(name, effect)`, `getEffect(name)` |
| **MosbergPotions** | Potion registration with brewing support | `register(name, potion)` |
| **MosbergBlockEntities** | Block entity type registration | `register(name, type)`, `registerWithRenderer(name, type, rendererFactory)` |
| **MosbergDataComponents** | Data component type registration (1.21+) | `register(name, component)` |
| **MosbergDamageTypes** | Custom damage type registration | `register(name, damageType)` |
| **MosbergGameEvents** | Game event registration | `register(name, event)` |
| **MosbergScreenHandlerTypes** | Screen handler registration | `register(name, type)`, `registerWithGui(name, type, screenFactory)` |
| **MosbergVillagers** | Villager profession & POI registration | `registerProfession(name, profession)`, `registerPOI(name, poi)` |
| **MosbergWorldGen** | World generation registry management | `registerFeature(name, feature)`, `registerBiome(name, biome)` |
| **MosbergTags** | Tag creation and management | `createBlockTag(name)`, `createItemTag(name)`, `createFluidTag(name)` |
| **MosbergRegistries** | Master registry for 60+ Minecraft types | Comprehensive type-safe methods for all content |

### ⌨️ Command System (`dk.mosberg.api.command`)

**Pre-built command templates and registration utilities:**

| Class | Purpose | Features |
|-------|---------|----------|
| **MosbergCommands** | Command registration manager | Central registration point for all commands |
| **BlockCommand** | Block inspection & manipulation | Query state, modify properties, validate placement |
| **ItemCommand** | Item management utilities | Inspect stacks, modify NBT, check attributes |
| **EntityCommand** | Entity queries & manipulation | Spawn, teleport, modify attributes, AI control |
| **WorldCommand** | World state management | Weather, time, entity queries, explosion creation |
| **DebugCommand** | Development & debugging tools | Performance profiling, entity spawning, block analysis |
| **ConfigCommand** | Runtime configuration management | Reload configs, list settings, modify values |
| **RegistryCommand** | Registry introspection | List registered items, blocks, entities, inspect properties |
| **HelpCommand** | In-game command documentation | List available commands with usage information |

### 🛠️ Utility Helpers (`dk.mosberg.api.util`)

**25+ production-ready helper classes for common operations:**

| Helper | Use Cases |
|--------|-----------|
| **AttributeHelper** | Entity attribute modification, damage tracking, speed adjustments, health manipulation |
| **BlockHelper** | Block state queries, neighbor detection, material checks, waterlogging detection |
| **CommandHelper** | Command registration, argument parsing, result messaging, feedback |
| **DataComponentHelper** | Data component management (1.21+), component modification |
| **DamageTypeHelper** | Custom damage type management, source detection, type filtering |
| **EnchantmentUtil** | Enchantment compatibility, level detection, application, removal |
| **EntityHelper** | Entity spawning, teleportation, knockback, AI manipulation, attribute modification |
| **FluidHelper** | Fluid state queries, flow detection, viscosity checks |
| **GameEventHelper** | Game event dispatching and listening |
| **InventoryHelper** | Item insertion, transfer between inventories, capacity checks, sorting |
| **ItemGroupHelper** | Creative tab management and entry registration |
| **ItemHelper** | ItemStack manipulation, NBT reading/writing, durability management, enchantment handling |
| **MosbergEnchantmentHelper** | Advanced enchantment operations, custom effects |
| **MosbergHelper** | General utilities (version info, initialization checks) |
| **NBTHelper** | NBT data serialization, type-safe reading/writing, deep copying |
| **NetworkHelper** | Custom packet handling, player-specific networking, sync |
| **ParticleHelper** | Particle spawning, custom effects, animation management |
| **PotionHelper** | Potion effect application, duration management, amplifier control |
| **RecipeHelper** | Recipe querying at runtime, type filtering, ingredient matching |
| **SerializationHelper** | GSON-based serialization for configs, JSON conversion |
| **SoundHelper** | Sound event playing, custom sounds, positioning |
| **StatusEffectHelper** | Status effect application, removal, duration control |
| **TagHelper** | Tag creation and querying for items, blocks, fluids, entities |
| **VillagerHelper** | Villager profession management, trade modification |
| **WorldHelper** | World state manipulation, weather control, entity queries, explosion creation |

### 🎨 Client-Side Systems (`dk.mosberg.api.client`)

#### Rendering & Model Management

| Class | Purpose | Features |
|-------|---------|----------|
| **MosbergRenderers** | Entity/block renderer registration | Humanoid/quadruped templates, custom renderers |
| **MosbergModels** | Model layer and entity model management | Layer registration, model creation utilities |
| **MosbergModelLayers** | Pre-defined model layer system | Consistent layer naming and organization |
| **MosbergRenderStates** | 1.21+ render state utilities | Advanced rendering configuration |
| **RenderHelper** | Custom rendering utilities | GL state management, overlay rendering, transformations |
| **ModelHelper** | Model texture and animation utilities | Texture binding, animation frame calculation |
| **TextureHelper** | Texture loading and management | Dynamic texture loading, atlas management |

#### Screen & UI Management

| Class | Purpose | Features |
|-------|---------|----------|
| **MosbergScreenHandlerTypes** | Screen handler registration | Type-safe handler registration with screen factory |
| **ScreenHandlerHelper** | Handler utility operations | Slot manipulation, data sync, inventory interaction |
| **ScreenHelper** | Custom screen creation utilities | Button/widget factories, event handling, rendering |

#### Render State System (1.21+)

The render state system provides fine-grained control over entity rendering:

```java
// EntityRenderState - Per-frame render state snapshot
EntityRenderState state = new EntityRenderState();
state.x = entity.getX();
state.y = entity.getY();
state.z = entity.getZ();
state.yRot = entity.getYaw();
state.xRot = entity.getPitch();

// BlockEntityRenderState - Block entity rendering state
BlockEntityRenderState blockState = new BlockEntityRenderState();
blockState.setBlockPos(pos);
blockState.setBlockState(state);
```

### 📊 Data Generation (`dk.mosberg.api.data`)

**Compile-time generation of recipes, loot tables, and models with fluent builders:**

#### Server-Side Data Providers

- **MosbergRecipeProvider** - Shaped/shapeless crafting, smelting, stonecutting, smoking, campfire cooking
- **MosbergLootTableProvider** - Block, entity, and chest loot table generation with conditions
- **MosbergApiRecipeProvider** - Built-in examples and recipe templates
- **MosbergApiLootTableProvider** - Pre-built loot table generators

#### Client-Side Data Providers

- **MosbergModelProvider** - 1.21+ block and item model generation with automatic variants
- **MosbergApiModelProvider** - Model template examples and best practices
- **MosbergRenderStatesHelper** - Render state data generation

### 🔌 Advanced Systems

| System | Purpose |
|--------|---------|
| **Event System** (`dk.mosberg.api.event`) | Custom event framework (BlockMined, PlayerJoin, EntitySpawn, etc.) |
| **Config Manager** (`dk.mosberg.api.config`) | JSON-based configuration with auto-generation and reload |
| **Documentation Generator** (`dk.mosberg.api.doc`) | Auto-generate API documentation from code |
| **Test Framework** (`dk.mosberg.api.test`) | Helpers for unit testing mods with test utilities |
| **Mixin System** | Server & client-side mixins for core functionality |

---

## 🚀 Quick Start

### For Players

1. **Install Fabric**
   ```bash
   # Download Fabric Installer from https://fabricmc.net/use/
   java -jar fabric-installer.jar client
   ```

2. **Install Dependencies**
   - Download [Fabric API](https://modrinth.com/mod/fabric-api) (required)
   - Download [MosbergAPI](https://github.com/Mosberg/mosbergapi/releases)

3. **Place in Mods Folder**
   ```
   .minecraft/mods/
   ├── fabric-api-0.138.4+1.21.10.jar
   └── mosbergapi-1.0.0.jar
   ```

### For Developers

#### 1. Add to Gradle

```gradle
repositories {
    maven {
        name = "Mosberg"
        url = "https://maven.moddingx.org"
    }
}

dependencies {
    modImplementation "dk.mosberg:mosbergapi:1.0.0"
    // Optional: bundle with your mod
    include "dk.mosberg:mosbergapi:1.0.0"
}
```

#### 2. Update fabric.mod.json

```json
{
  "schemaVersion": 1,
  "id": "yourmod",
  "version": "1.0.0",
  "name": "Your Mod",
  "environment": "*",
  "depends": {
    "fabricloader": ">=0.18.3",
    "fabric": "*",
    "minecraft": "1.21.10",
    "mosbergapi": ">=1.0.0"
  },
  "entrypoints": {
    "main": ["com.yourname.YourMod"],
    "client": ["com.yourname.YourModClient"]
  },
  "mixins": ["yourmod.mixins.json"],
  "custom": {
    "modmenu": {
      "links": {
        "modpage": "https://modrinth.com/mod/yourmod"
      }
    }
  }
}
```

#### 3. Create Your Mod Class

```java
package com.yourname;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;

/**
 * Main mod initializer for YourMod.
 * 
 * @author YourName
 * @version 1.0.0
 */
public class YourMod implements ModInitializer {
    public static final String MOD_ID = "yourmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing YourMod with MosbergAPI!");
        
        // Register content
        ModBlocks.initialize();
        ModItems.initialize();
        ModEntities.initialize();
        
        // Register events
        ModEvents.register();
        
        // Load configuration
        ModConfig.load();
        
        // Register commands
        ModCommands.register();
        
        LOGGER.info("YourMod initialized successfully!");
    }
}
```

---

## 🏛️ Architecture

### Design Principles

1. **Separation of Concerns** - Clear package organization by functionality (registry, util, command, client)
2. **Type Safety** - Modern Java 21 (records, sealed classes, pattern matching, text blocks)
3. **Fluent APIs** - Builder patterns for complex objects (entity registration, recipe generation)
4. **Null Safety** - `@Nullable` and `@NotNull` annotations throughout
5. **Comprehensive Logging** - SLF4J with appropriate log levels (DEBUG, INFO, WARN, ERROR)
6. **Zero Boilerplate** - Convention over configuration with sensible defaults
7. **Client-Server Separation** - Clear boundaries between client-only and server-only code

### Dependency Graph

```
MosbergApi (Mod Initializer)
  ├── Registry System (16 classes)
  │   └── MosbergRegistries (Master registry)
  ├── Utility Helpers (25 classes)
  │   ├── Inventory/NBT operations
  │   ├── Entity manipulation
  │   └── World state management
  ├── Command System (8 templates)
  │   └── MosbergCommands (Manager)
  ├── Client Systems
  │   ├── Rendering (MosbergRenderers)
  │   ├── Models (MosbergModels)
  │   ├── Screen Handlers
  │   └── Utilities (7 helpers)
  ├── Event System
  ├── Configuration Manager
  └── Data Generators
      ├── Recipes
      ├── Loot Tables
      └── Models
```

---

## 📚 API Reference

### Registry Operations

#### Registering a Block

```java
import dk.mosberg.api.registry.MosbergBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock;

/**
 * Register a basic block with automatic item creation.
 */
public static final Block CUSTOM_BLOCK = MosbergBlocks.register(
    "custom_block",
    new Block(AbstractBlock.Settings.create()
        .strength(2.0f)
        .requiresTool())
);

/**
 * Register block without item.
 */
public static final Block DECORATIVE_BLOCK = MosbergBlocks.register(
    "decorative",
    new Block(AbstractBlock.Settings.create()),
    false  // Don't create item
);

/**
 * Register block with custom item settings.
 */
public static final Block SPECIAL_BLOCK = MosbergBlocks.register(
    "special",
    new Block(AbstractBlock.Settings.create()),
    new Item.Settings().rarity(Rarity.RARE)
);
```

#### Registering an Entity

```java
import dk.mosberg.api.registry.MosbergEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

/**
 * Register a custom entity type with builder pattern.
 */
public static final EntityType<CustomEntity> CUSTOM_MOB = MosbergEntities.register(
    "custom_mob",
    EntityType.Builder.create(CustomEntity::new, SpawnGroup.CREATURE)
        .dimensions(0.8f, 1.8f)
        .maxTrackingRange(64)
        .updateIntervalMultiplier(3)
);
```

#### Registering an Enchantment

```java
import dk.mosberg.api.registry.MosbergEnchantments;
import net.minecraft.enchantment.Enchantment;

/**
 * Register a custom enchantment.
 */
public static final Enchantment CUSTOM_ENCHANTMENT = MosbergEnchantments.register(
    "custom_enchantment",
    new Enchantment(
        Enchantment.definition(
            Items.DIAMOND_SWORD,
            2,      // minimum cost
            5,      // maximum cost
            Enchantment.Weight.UNCOMMON,
            EquipmentSlot.HAND
        )
    )
);
```

#### Registering a Fluid

```java
import dk.mosberg.api.registry.MosbergFluids;
import net.minecraft.fluid.Fluid;

/**
 * Register a custom fluid with flowing variant.
 */
public static final Fluid CUSTOM_FLUID = MosbergFluids.registerFlowing(
    "custom_fluid",
    stillFluid,
    flowingFluid
);
```

#### Using the Master Registry

```java
import dk.mosberg.api.registry.MosbergRegistries;

// Register any type with type-safe methods
MosbergRegistries.registerStatusEffect("custom_effect", customEffect);
MosbergRegistries.registerPotion("custom_potion", customPotion);
MosbergRegistries.registerGameEvent("custom_event", gameEvent);
MosbergRegistries.registerDamageType("custom_damage", damageType);
```

### Utility Helper Usage

#### Inventory Management

```java
import dk.mosberg.api.util.InventoryHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

// Add item to inventory (returns leftovers)
ItemStack leftovers = InventoryHelper.addItemToInventory(inventory, new ItemStack(Items.DIAMOND, 64));

// Transfer between inventories
int transferred = InventoryHelper.transferItems(sourceInv, destInv, 32);

// Check capacity
boolean hasSpace = InventoryHelper.hasSpace(inventory, new ItemStack(Items.IRON_INGOT));

// Count items
int count = InventoryHelper.countItems(inventory, Items.GOLD_ORE);

// Find item in inventory
int slot = InventoryHelper.findItem(inventory, Items.DIAMOND);
```

#### Entity Manipulation

```java
import dk.mosberg.api.util.EntityHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

// Teleport entity
EntityHelper.teleportEntity(entity, world, new Vec3d(100, 64, 200));

// Apply knockback
EntityHelper.applyKnockback(entity, 1.5, direction);

// Heal entity
EntityHelper.healEntity(entity, 5.0f);

// Apply attribute modifier
EntityHelper.addAttributeModifier(entity, EntityAttributes.GENERIC_SPEED, 0.5);

// Modify AI behavior
EntityHelper.disableAI(mob);
```

#### World Operations

```java
import dk.mosberg.api.util.WorldHelper;

// Check time of day
if (WorldHelper.isDaytime(world)) {
    // Only during day
}

// Set weather
WorldHelper.setRaining(serverWorld, 6000); // 5 minutes

// Create explosion
WorldHelper.createExplosion(world, null, pos, 5.0f, true);

// Get nearby entities
List<Entity> nearby = WorldHelper.getEntitiesInBox(world, new Box(pos, pos.add(16, 16, 16)));

// Set block state safely
WorldHelper.setBlockState(world, pos, state);
```

#### NBT Data Operations

```java
import dk.mosberg.api.util.NBTHelper;
import net.minecraft.nbt.NbtCompound;

NbtCompound nbt = new NbtCompound();

// Type-safe writing
NBTHelper.setString(nbt, "name", "Custom Item");
NBTHelper.setInt(nbt, "level", 5);
NBTHelper.setDouble(nbt, "power", 2.5);
NBTHelper.setBoolean(nbt, "enchanted", true);

// Type-safe reading with defaults
String name = NBTHelper.getString(nbt, "name", "Default");
int level = NBTHelper.getInt(nbt, "level", 0);
double power = NBTHelper.getDouble(nbt, "power", 1.0);

// Deep copy NBT
NbtCompound copy = NBTHelper.deepCopy(nbt);
```

#### Particle Effects

```java
import dk.mosberg.api.util.ParticleHelper;
import net.minecraft.particle.ParticleTypes;

// Spawn simple particles
ParticleHelper.spawnParticles(world, ParticleTypes.FLAME, pos, 10, 0.5);

// Spawn with velocity
ParticleHelper.spawnParticlesWithVelocity(world, ParticleTypes.EXPLOSION, 
    pos, 5, new Vec3d(0.1, 0.1, 0.1));
```

### Command Registration

#### Using Built-in Commands

```java
import dk.mosberg.api.command.MosbergCommands;

public class ModCommands {
    public static void register() {
        // Register all built-in commands
        MosbergCommands.registerAll();
        
        // Or register individually
        MosbergCommands.registerBlockCommand();
        MosbergCommands.registerItemCommand();
        MosbergCommands.registerEntityCommand();
        MosbergCommands.registerWorldCommand();
    }
}
```

#### Creating Custom Commands

```java
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class CustomCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                LiteralArgumentBuilder.literal("custommodd")
                    .executes(context -> {
                        context.getSource().sendFeedback(
                            () -> Text.literal("Custom command executed!"),
                            false
                        );
                        return 1;
                    })
            );
        });
    }
}
```

### Data Generation

#### Creating Recipes with Fluent API

```java
import dk.mosberg.api.data.provider.MosbergRecipeProvider;
import net.minecraft.data.recipe.RecipeGenerator;

public class ModRecipes extends MosbergRecipeProvider {
    public ModRecipes(DataOutput output, 
                      CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(
            RegistryWrapper.WrapperLookup registries, 
            RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                // Shaped crafting recipe
                createShaped(RecipeCategory.TOOLS, ModItems.CUSTOM_SWORD)
                    .pattern("D")
                    .pattern("D")
                    .pattern("S")
                    .input('D', Items.DIAMOND)
                    .input('S', Items.STICK)
                    .criterion(hasItem(Items.DIAMOND), 
                        conditionsFromItem(Items.DIAMOND))
                    .offerTo(exporter);

                // Shapeless crafting recipe
                createShapeless(RecipeCategory.MISC, ModItems.CUSTOM_DUST)
                    .input(Items.DIAMOND)
                    .input(Items.IRON_INGOT)
                    .input(Items.STONE)
                    .criterion(hasItem(Items.DIAMOND), 
                        conditionsFromItem(Items.DIAMOND))
                    .offerTo(exporter);

                // Smelting recipe
                offerSmelting(exporter, List.of(ModBlocks.CUSTOM_ORE),
                    RecipeCategory.MISC, ModItems.CUSTOM_INGOT,
                    1.0f, 200, "custom_ingot");

                // Stonecutting recipe
                createStonecutting(RecipeCategory.BUILDING_BLOCKS,
                    ModBlocks.CUSTOM_STAIRS, ModBlocks.CUSTOM_BLOCK)
                    .criterion(hasItem(ModBlocks.CUSTOM_BLOCK),
                        conditionsFromItem(ModBlocks.CUSTOM_BLOCK))
                    .offerTo(exporter);

                // Smoking recipe
                offerSmokingRecipe(exporter, List.of(ModBlocks.CUSTOM_FOOD),
                    RecipeCategory.FOOD, ModItems.COOKED_CUSTOM,
                    0.5f, 100, "cooked_custom");
            }
        };
    }
}
```

#### Creating Loot Tables

```java
import dk.mosberg.api.data.provider.MosbergLootTableProvider;
import net.minecraft.loot.*;

public class ModLootTables extends MosbergLootTableProvider {
    @Override
    public void generate() {
        // Block loot table
        addBlockLootTable(ModBlocks.CUSTOM_ORE, 
            LootTable.builder()
                .pool(LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .with(ItemEntry.builder(ModItems.CUSTOM_INGOT)
                        .apply(SetCountLootFunction.builder(
                            ConstantLootNumberProvider.create(1))))));

        // Entity loot table with conditions
        addEntityLootTable(ModEntities.CUSTOM_MOB,
            LootTable.builder()
                .pool(LootPool.builder()
                    .rolls(UniformLootNumberProvider.create(0, 2))
                    .with(ItemEntry.builder(Items.DIAMOND)
                        .conditionally(
                            KilledByPlayerLootCondition.builder()))));

        // Chest loot table
        addChestLootTable("custom_chest",
            LootTable.builder()
                .pool(LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(3))
                    .with(ItemEntry.builder(Items.EMERALD))));
    }
}
```

#### Generating Models

```java
import dk.mosberg.api.client.data.provider.MosbergModelProvider;
import net.minecraft.data.client.BlockStateSupplier;

public class ModModels extends MosbergModelProvider {
    @Override
    public void generate() {
        // Simple block model
        registerSimpleBlockModel(ModBlocks.CUSTOM_BLOCK, 
            "textures/block/custom_block");

        // Block with variants
        registerMultiVariantBlock(ModBlocks.CUSTOM_WOOD,
            new String[]{"oak", "birch", "spruce"},
            "textures/block/custom_wood");

        // Item model
        registerSimpleItemModel(ModItems.CUSTOM_GEM,
            "textures/item/custom_gem");
    }
}
```

### Configuration Management

```java
import dk.mosberg.api.config.ConfigManager;

public class ModConfig {
    public static final ConfigManager CONFIG = new ConfigManager("yourmod");

    public static int SPAWN_RATE = 10;
    public static boolean ENABLE_FEATURE = true;
    public static String CUSTOM_MESSAGE = "Hello!";
    public static double DAMAGE_MULTIPLIER = 1.5;

    public static void load() {
        SPAWN_RATE = CONFIG.getInt("spawn_rate", 10);
        ENABLE_FEATURE = CONFIG.getBoolean("enable_feature", true);
        CUSTOM_MESSAGE = CONFIG.getString("custom_message", "Hello!");
        DAMAGE_MULTIPLIER = CONFIG.getDouble("damage_multiplier", 1.5);

        CONFIG.save();
    }

    public static void reload() {
        CONFIG.reload();
        load();
    }
}
```

### Custom Events

```java
import dk.mosberg.api.event.MosbergEvents;

public class ModEvents {
    public static void register() {
        // Block mined event
        MosbergEvents.BLOCK_MINED.register((player, world, pos, state) -> {
            if (state.isOf(Blocks.DIAMOND_ORE)) {
                player.sendMessage(Text.literal("You mined diamonds!"), false);
            }
        });

        // Player join event
        MosbergEvents.PLAYER_JOINED.register((player) -> {
            LOGGER.info("Player {} joined", player.getName().getString());
        });

        // Entity spawn event
        MosbergEvents.ENTITY_SPAWNED.register((entity, world) -> {
            if (entity instanceof HostileEntity) {
                LOGGER.debug("Hostile entity spawned: {}", entity.getType());
            }
        });
    }
}
```

### Client-Side Rendering

#### Entity Renderer Registration

```java
import dk.mosberg.api.client.registry.MosbergRenderers;
import dk.mosberg.api.client.registry.MosbergModelLayers;

public class ModClientSetup {
    public static void registerRenderers() {
        // Register entity renderer with model layer
        MosbergRenderers.registerEntityRenderer(
            ModEntities.CUSTOM_MOB,
            CustomMobRenderer::new,
            ModModelLayers.CUSTOM_MOB,
            CustomMobModel::getTexturedModelData
        );

        // Register block entity renderer
        MosbergRenderers.registerBlockEntityRenderer(
            ModBlockEntities.CUSTOM_ENTITY,
            CustomBlockEntityRenderer::new
        );
    }
}
```

#### Screen Handler Registration

```java
import dk.mosberg.api.client.registry.MosbergScreenHandlerTypes;
import dk.mosberg.api.registry.MosbergScreenHandlerTypes as ServerRegistry;

// Server-side
public static final ScreenHandlerType<CustomHandler> CUSTOM_HANDLER =
    ServerRegistry.register("custom_handler",
        (syncId, inventory) -> new CustomHandler(syncId, inventory)
    );

// Client-side
public static void registerScreens() {
    ScreenRegistry.register(ModScreenHandlers.CUSTOM_HANDLER,
        CustomScreen::new);
}
```

---

## 💡 Usage Examples

### Complete Mod Example

```java
// Main mod initializer
package com.example.mymod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for MyMod.
 * 
 * @author YourName
 * @version 1.0.0
 */
public class MyMod implements ModInitializer {
    public static final String MOD_ID = "mymod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing MyMod with MosbergAPI!");
        
        // Register all content
        ModBlocks.initialize();
        ModItems.initialize();
        ModEntities.initialize();
        ModSounds.initialize();
        ModEnchantments.initialize();
        
        // Register events and commands
        ModEvents.register();
        ModCommands.register();
        
        // Load configuration
        ModConfig.load();
        
        LOGGER.info("MyMod initialized successfully!");
    }
}

// Content registration
package com.example.mymod.content;

import dk.mosberg.api.registry.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;

/**
 * Block registration for MyMod.
 */
public class ModBlocks {
    public static final Block RUBY_BLOCK = MosbergBlocks.register(
        "ruby_block",
        new Block(AbstractBlock.Settings.copy(Blocks.DIAMOND_BLOCK))
    );

    public static final Block RUBY_ORE = MosbergBlocks.register(
        "ruby_ore",
        new OreBlock(Blocks.DEEPSLATE_DIAMOND_ORE, 
            AbstractBlock.Settings.copy(Blocks.DEEPSLATE_DIAMOND_ORE))
    );

    public static final Block RUBY_DEEPSLATE_ORE = MosbergBlocks.register(
        "ruby_deepslate_ore",
        new OreBlock(Blocks.DEEPSLATE,
            AbstractBlock.Settings.copy(Blocks.DEEPSLATE_DIAMOND_ORE)
                .strength(4.5f))
    );

    public static void initialize() {
        // Called from main initializer
    }
}

/**
 * Item registration for MyMod.
 */
public class ModItems {
    public static final Item RUBY = MosbergItems.register(
        "ruby",
        new Item(new Item.Settings())
    );

    public static final Item RUBY_SWORD = MosbergItems.register(
        "ruby_sword",
        new SwordItem(ToolMaterials.DIAMOND,
            new Item.Settings()
                .maxDamage(1500)
                .rarity(Rarity.RARE))
    );

    public static final Item RUBY_PICKAXE = MosbergItems.register(
        "ruby_pickaxe",
        new PickaxeItem(ToolMaterials.DIAMOND,
            new Item.Settings()
                .maxDamage(1500)
                .rarity(Rarity.RARE))
    );

    public static void initialize() {
        // Called from main initializer
    }
}

/**
 * Sound registration for MyMod.
 */
public class ModSounds {
    public static final SoundEvent RUBY_BREAK = MosbergSounds.register("block.ruby.break");
    public static final SoundEvent RUBY_STEP = MosbergSounds.register("block.ruby.step");
    public static final SoundEvent RUBY_PLACE = MosbergSounds.register("block.ruby.place");
    public static final SoundEvent RUBY_HIT = MosbergSounds.register("item.ruby.hit");

    public static void initialize() {
        // Called from main initializer
    }
}

/**
 * Enchantment registration for MyMod.
 */
public class ModEnchantments {
    public static final Enchantment RUBY_SHARPNESS = MosbergEnchantments.register(
        "ruby_sharpness",
        new Enchantment(
            Enchantment.definition(
                ItemTags.SWORD_ENCHANTABLE,
                2,
                5,
                Enchantment.Weight.UNCOMMON,
                EquipmentSlot.MAINHAND
            )
        )
    );

    public static void initialize() {
        // Called from main initializer
    }
}

// Event handlers
package com.example.mymod.event;

import dk.mosberg.api.event.MosbergEvents;

/**
 * Custom event handlers for MyMod.
 */
public class ModEvents {
    public static void register() {
        MosbergEvents.BLOCK_MINED.register((player, world, pos, state) -> {
            if (state.isOf(ModBlocks.RUBY_ORE)) {
                // Bonus drops for ruby ore
                ItemStack drop = new ItemStack(ModItems.RUBY, 2);
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), drop);
                
                // Play sound
                world.playSound(player, pos, ModSounds.RUBY_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        });

        MosbergEvents.ENTITY_SPAWNED.register((entity, world) -> {
            if (entity instanceof Creeper creeper) {
                MyMod.LOGGER.debug("Creeper spawned at: {}", entity.getPos());
            }
        });
    }
}

// Command registration
package com.example.mymod.command;

import dk.mosberg.api.command.MosbergCommands;

/**
 * Command setup for MyMod.
 */
public class ModCommands {
    public static void register() {
        // Register built-in commands
        MosbergCommands.registerAll();
    }
}

// Data generation
package com.example.mymod.datagen;

import dk.mosberg.api.data.provider.MosbergRecipeProvider;
import net.minecraft.data.recipe.RecipeGenerator;

/**
 * Recipe data generation for MyMod.
 */
public class ModRecipes extends MosbergRecipeProvider {
    public ModRecipes(DataOutput output, 
                      CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(
            RegistryWrapper.WrapperLookup registries, 
            RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                // Ruby sword recipe
                createShaped(RecipeCategory.TOOLS, ModItems.RUBY_SWORD)
                    .pattern(" R ")
                    .pattern(" R ")
                    .pattern(" S ")
                    .input('R', ModItems.RUBY)
                    .input('S', Items.STICK)
                    .criterion(hasItem(ModItems.RUBY), conditionsFromItem(ModItems.RUBY))
                    .offerTo(exporter);

                // Ruby pickaxe recipe
                createShaped(RecipeCategory.TOOLS, ModItems.RUBY_PICKAXE)
                    .pattern("RRR")
                    .pattern(" S ")
                    .pattern(" S ")
                    .input('R', ModItems.RUBY)
                    .input('S', Items.STICK)
                    .criterion(hasItem(ModItems.RUBY), conditionsFromItem(ModItems.RUBY))
                    .offerTo(exporter);

                // Smelting ruby ore
                offerSmelting(exporter, List.of(ModBlocks.RUBY_ORE),
                    RecipeCategory.MISC, ModItems.RUBY,
                    0.8f, 200, "ruby");

                // Smelting deepslate ruby ore
                offerSmelting(exporter, List.of(ModBlocks.RUBY_DEEPSLATE_ORE),
                    RecipeCategory.MISC, ModItems.RUBY,
                    0.8f, 250, "ruby_from_deepslate");
            }
        };
    }
}
```

---

## 📁 Project Structure

### Complete Directory Tree

```
mosbergapi/
│
├── 📄 build.gradle                      # Gradle build configuration
├── 📄 gradle.properties                 # Build properties
├── 📄 settings.gradle                   # Gradle settings
├── 📄 LICENSE                           # MIT License
├── 📄 README.md                         # This file
│
└── 📁 src/
    │
    ├── 📁 main/                         # Server-side code
    │   │
    │   ├── 📁 java/dk/mosberg/api/
    │   │   │
    │   │   ├── 📄 MosbergApi.java                   # Main mod initializer
    │   │   │
    │   │   ├── 📁 command/                         # Command system
    │   │   │   ├── 📁 commands/
    │   │   │   │   ├── 📄 BlockCommand.java
    │   │   │   │   ├── 📄 ConfigCommand.java
    │   │   │   │   ├── 📄 DebugCommand.java
    │   │   │   │   ├── 📄 EntityCommand.java
    │   │   │   │   ├── 📄 HelpCommand.java
    │   │   │   │   ├── 📄 ItemCommand.java
    │   │   │   │   ├── 📄 RegistryCommand.java
    │   │   │   │   └── 📄 WorldCommand.java
    │   │   │   ├── 📄 MosbergCommand.java
    │   │   │   └── 📄 MosbergCommands.java
    │   │   │
    │   │   ├── 📁 config/
    │   │   │   └── 📄 ConfigManager.java           # JSON config system
    │   │   │
    │   │   ├── 📁 data/                            # Data generation
    │   │   │   ├── 📁 provider/
    │   │   │   │   ├── 📄 MosbergRecipeProvider.java
    │   │   │   │   └── 📄 MosbergLootTableProvider.java
    │   │   │   ├── 📄 MosbergApiRecipeProvider.java
    │   │   │   └── 📄 MosbergApiLootTableProvider.java
    │   │   │
    │   │   ├── 📁 doc/
    │   │   │   └── 📄 DocumentationGenerator.java   # Auto-docs
    │   │   │
    │   │   ├── 📁 event/
    │   │   │   └── 📄 MosbergEvents.java            # Custom events
    │   │   │
    │   │   ├── 📁 mixin/
    │   │   │   └── 📄 MosbergMixin.java             # Server mixins
    │   │   │
    │   │   ├── 📁 registry/                        # Registry helpers (16 classes)
    │   │   │   ├── 📄 MosbergAttributes.java
    │   │   │   ├── 📄 MosbergBlockEntities.java
    │   │   │   ├── 📄 MosbergBlocks.java
    │   │   │   ├── 📄 MosbergDamageTypes.java
    │   │   │   ├── 📄 MosbergDataComponents.java
    │   │   │   ├── 📄 MosbergEnchantments.java
    │   │   │   ├── 📄 MosbergEntities.java
    │   │   │   ├── 📄 MosbergFluids.java
    │   │   │   ├── 📄 MosbergGameEvents.java
    │   │   │   ├── 📄 MosbergItemGroups.java
    │   │   │   ├── 📄 MosbergItems.java
    │   │   │   ├── 📄 MosbergParticles.java
    │   │   │   ├── 📄 MosbergPotions.java
    │   │   │   ├── 📄 MosbergRecipes.java
    │   │   │   ├── 📄 MosbergRegistries.java        # Master registry
    │   │   │   ├── 📄 MosbergScreenHandlerTypes.java
    │   │   │   ├── 📄 MosbergSounds.java
    │   │   │   ├── 📄 MosbergStatusEffects.java
    │   │   │   ├── 📄 MosbergTags.java
    │   │   │   ├── 📄 MosbergVillagers.java
    │   │   │   └── 📄 MosbergWorldGen.java
    │   │   │
    │   │   ├── 📁 test/
    │   │   │   └── 📄 TestHelper.java               # Testing utilities
    │   │   │
    │   │   └── 📁 util/                             # Utility helpers (25+)
    │   │       ├── 📄 AttributeHelper.java
    │   │       ├── 📄 BlockHelper.java
    │   │       ├── 📄 CommandHelper.java
    │   │       ├── 📄 DataComponentHelper.java
    │   │       ├── 📄 DamageTypeHelper.java
    │   │       ├── 📄 EnchantmentUtil.java
    │   │       ├── 📄 EntityHelper.java
    │   │       ├── 📄 FluidHelper.java
    │   │       ├── 📄 GameEventHelper.java
    │   │       ├── 📄 InventoryHelper.java
    │   │       ├── 📄 ItemGroupHelper.java
    │   │       ├── 📄 ItemHelper.java
    │   │       ├── 📄 MosbergEnchantmentHelper.java
    │   │       ├── 📄 MosbergHelper.java
    │   │       ├── 📄 NBTHelper.java
    │   │       ├── 📄 NetworkHelper.java
    │   │       ├── 📄 ParticleHelper.java
    │   │       ├── 📄 PotionHelper.java
    │   │       ├── 📄 RecipeHelper.java
    │   │       ├── 📄 SerializationHelper.java
    │   │       ├── 📄 SoundHelper.java
    │   │       ├── 📄 StatusEffectHelper.java
    │   │       ├── 📄 TagHelper.java
    │   │       ├── 📄 VillagerHelper.java
    │   │       └── 📄 WorldHelper.java
    │   │
    │   └── 📁 resources/
    │       ├── 📄 fabric.mod.json                   # Mod metadata
    │       ├── 📄 mosbergapi.mixins.json            # Mixin config
    │       ├── 📄 pack.mcmeta                       # Pack metadata
    │       │
    │       ├── 📁 assets/mosbergapi/                # Client assets
    │       │   ├── 📄 icon.png                      # Mod icon (64x64)
    │       │   ├── 📁 atlases/                      # Texture atlases
    │       │   ├── 📁 blockstates/                  # Block state JSONs
    │       │   │   └── 📄 custom_block.json
    │       │   ├── 📁 equipment/                    # Armor/equipment models
    │       │   ├── 📁 lang/                         # Localization
    │       │   │   └── 📄 en_us.json
    │       │   ├── 📁 models/
    │       │   │   ├── 📁 block/
    │       │   │   ├── 📁 item/
    │       │   │   ├── 📁 entity/
    │       │   │   └── 📁 equipment/
    │       │   ├── 📁 particles/
    │       │   ├── 📁 post_effect/
    │       │   ├── 📁 shaders/
    │       │   ├── 📁 sounds/
    │       │   ├── 📁 texts/
    │       │   └── 📁 textures/
    │       │       ├── 📁 block/
    │       │       ├── 📁 entity/
    │       │       └── 📁 item/
    │       │
    │       └── 📁 data/mosbergapi/                  # Server data
    │           ├── 📁 advancement/
    │           ├── 📁 damage_type/
    │           ├── 📁 enchantment/
    │           ├── 📁 loot_table/
    │           ├── 📁 recipe/
    │           ├── 📁 tags/
    │           └── 📁 worldgen/
    │
    └── 📁 client/                       # Client-side code
        │
        ├── 📁 java/dk/mosberg/api/client/
        │   │
        │   ├── 📄 MosbergApiClient.java             # Client initializer
        │   │
        │   ├── 📁 data/                             # Client data gen
        │   │   ├── 📁 provider/
        │   │   │   └── 📄 MosbergModelProvider.java
        │   │   ├── 📄 MosbergApiDataGenerator.java
        │   │   └── 📄 MosbergApiModelProvider.java
        │   │
        │   ├── 📁 mixin/client/
        │   │   └── 📄 MosbergClientMixin.java
        │   │
        │   ├── 📁 registry/                         # Client registries
        │   │   ├── 📄 MosbergModelLayers.java
        │   │   ├── 📄 MosbergModels.java
        │   │   ├── 📄 MosbergRenderers.java
        │   │   ├── 📄 MosbergRenderStates.java
        │   │   └── 📄 MosbergScreenHandlers.java
        │   │
        │   ├── 📁 util/                             # Client utilities (7)
        │   │   ├── 📄 ModelHelper.java
        │   │   ├── 📄 ModelLayersHelper.java
        │   │   ├── 📄 RenderHelper.java
        │   │   ├── 📄 RenderStatesHelper.java
        │   │   ├── 📄 ScreenHandlerHelper.java
        │   │   ├── 📄 ScreenHelper.java
        │   │   └── 📄 TextureHelper.java
        │   │
        │   └── 📁 event/
        │       └── 📄 MosbergClientEvents.java
        │
        └── 📁 resources/
            └── 📄 mosbergapi.client.mixins.json
```

### Asset Organization

#### Client Assets (`src/main/resources/assets/mosbergapi/`)

```
assets/mosbergapi/
├── atlases/                 # Texture atlas definitions
├── blockstates/             # Block state JSON files
│   └── custom_block.json
├── equipment/               # Armor/equipment models
├── lang/                    # Localization files
│   └── en_us.json           # English strings
├── models/
│   ├── block/               # Block models (JSON)
│   ├── item/                # Item models (JSON)
│   ├── entity/              # Entity/mob models
│   └── equipment/           # Armor models
├── particles/               # Particle definitions
├── post_effect/             # Post-processing shaders
├── shaders/                 # GLSL shader files
├── sounds/                  # Sound effects (.ogg)
├── texts/                   # Text files (credits)
├── textures/
│   ├── block/               # Block textures (PNG)
│   ├── entity/              # Entity textures (PNG)
│   └── item/                # Item textures (PNG)
└── icon.png                 # Mod icon (64x64 recommended)
```

#### Server Data (`src/main/resources/data/mosbergapi/`)

```
data/mosbergapi/
├── advancement/             # Advancement definitions
├── damage_type/             # Custom damage types (1.21+)
├── enchantment/             # Enchantment definitions
├── loot_table/              # Loot table definitions
├── recipe/                  # Recipe definitions
├── tags/                    # Tag definitions
│   ├── blocks/
│   ├── items/
│   ├── fluids/
│   └── entity_types/
└── worldgen/                # World generation
    ├── biome/
    ├── feature/
    ├── configured_feature/
    └── placed_feature/
```

---

## 🔨 Building & Development

### System Requirements

- **Minecraft**: 1.21.10
- **Java**: 21+ (Eclipse Adoptium recommended)
- **Gradle**: 8.x (bundled via wrapper)
- **Fabric Loader**: 0.18.3+
- **Fabric API**: 0.138.4+1.21.10

### Building from Source

```bash
# Clone repository
git clone https://github.com/Mosberg/mosbergapi.git
cd mosbergapi

# Build JAR
./gradlew build

# Clean build
./gradlew clean build
```

### Development Workflow

```bash
# Run game client for testing
./gradlew runClient

# Run game server for testing
./gradlew runServer

# Generate Javadoc
./gradlew javadoc

# Generate data (recipes, models, etc.)
./gradlew runDatagen

# Format code with spotless
./gradlew spotlessApply

# Run tests
./gradlew test

# Build and publish locally
./gradlew publishToMavenLocal
```

### Build Outputs

After building, find outputs in `build/libs/`:

| JAR | Purpose |
|-----|---------|
| `mosbergapi-1.0.0.jar` | Production mod JAR |
| `mosbergapi-1.0.0-sources.jar` | Source code (for developers) |
| `mosbergapi-1.0.0-javadoc.jar` | API documentation |

### IDE Setup

#### IntelliJ IDEA

1. Import as Gradle project
2. Enable annotation processing: `Settings > Compiler > Annotation Processors > Enable`
3. Mark `src/main/java` and `src/client/java` as sources
4. Mark `src/main/resources` and `src/client/resources` as resources

#### Eclipse

1. Import as existing Gradle project
2. Run `./gradlew eclipse`
3. Refresh project in Eclipse

---

## 🤝 Contributing

### Code Style Guidelines

1. **Formatting**
   - 4 spaces per indent (no tabs)
   - 120 character line limit
   - Format with `spotlessApply` before commit
   - Use Unix line endings (LF)

2. **Naming Conventions**
   - Classes: `PascalCase` (e.g., `EntityHelper`)
   - Methods: `camelCase` (e.g., `teleportEntity`)
   - Constants: `UPPER_SNAKE_CASE` (e.g., `MOD_ID`)
   - Private fields: `camelCase` with `private` modifier
   - Parameters: `camelCase`

3. **Documentation**
   - **Public API**: Requires comprehensive JavaDoc
   - **Method tags**:
     - `@param` for each parameter
     - `@return` for return value
     - `@throws` for exceptions
     - `@since` for version introduced
     - `@author "Mosberg"` for consistency
     - `@example` for complex methods showing usage
   - **Class-level**: Explain purpose, usage patterns, and provide example
   - **Inline comments**: Explain "why", not "what"

   ```java
   /**
    * Teleports an entity to a specific location.
    * 
    * @param entity The entity to teleport (non-null)
    * @param world The destination world (non-null)
    * @param position The destination coordinates (non-null)
    * @throws IllegalArgumentException if entity or world is null
    * 
    * @example
    * <pre>
    * EntityHelper.teleportEntity(player, world, new Vec3d(100, 64, 200));
    * </pre>
    * 
    * @since 1.0.0
    * @author Mosberg
    */
   public static void teleportEntity(
       @NotNull Entity entity,
       @NotNull World world,
       @NotNull Vec3d position
   ) { ... }
   ```

4. **Type Safety**
   - Use `@Nullable` and `@NotNull` annotations
   - Validate parameters with clear error messages
   - Use sealed classes and records where appropriate
   - Enable strict null checking in IDE

5. **Exception Handling**
   - Use specific exception types:
     - `IllegalArgumentException` - Invalid parameter
     - `IllegalStateException` - Invalid operation state
     - `NullPointerException` - Null when not allowed
   - Include descriptive messages:
     ```java
     if (entity == null) {
         throw new NullPointerException("Entity cannot be null");
     }
     ```

6. **Logging**
   - Use SLF4J logger: `LoggerFactory.getLogger(ClassName.class)`
   - Appropriate levels:
     - **DEBUG**: Detailed flow information
     - **INFO**: Important state changes
     - **WARN**: Potentially problematic situations
     - **ERROR**: Error events with stack traces

### Contribution Steps

1. **Fork & Clone**
   ```bash
   git clone https://github.com/YOUR_USERNAME/mosbergapi.git
   cd mosbergapi
   ```

2. **Create Feature Branch**
   ```bash
   git checkout -b feature/amazing-feature
   git checkout -b fix/critical-bug
   ```

3. **Make Changes**
   - Follow code style guidelines
   - Add tests if applicable
   - Update documentation for public API
   - Update this README if adding new features

4. **Format & Test**
   ```bash
   ./gradlew spotlessApply    # Format code
   ./gradlew test             # Run tests
   ./gradlew build            # Full build
   ```

5. **Commit with Clear Message**
   ```bash
   git commit -m "feat: Add amazing feature

   Description of what the feature does and why it was added.
   - Adds X functionality
   - Improves performance by Y%
   - Fixes issue with Z

   Fixes #123"
   ```

6. **Push & Create Pull Request**
   ```bash
   git push origin feature/amazing-feature
   # Create PR on GitHub
   ```

### Commit Message Format

- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation changes
- `style:` - Code style changes (formatting)
- `refactor:` - Code refactoring
- `perf:` - Performance improvements
- `test:` - Test additions/changes
- `chore:` - Build/dependency changes

### Issue Templates

#### Bug Report

```markdown
**Describe the Bug**
Clear description of what happened.

**Minecraft Version**: 1.21.10
**MosbergAPI Version**: 1.0.0
**Fabric Loader Version**: 0.18.3
**Fabric API Version**: 0.138.4+1.21.10
**Java Version**: 21

**Steps to Reproduce**
1. ...
2. ...
3. ...

**Expected Behavior**
What should happen instead.

**Actual Behavior**
What actually happened.

**Error Log**
\`\`\`logs
Paste relevant logs here
\`\`\`

**Additional Context**
Any other relevant information.
```

#### Feature Request

```markdown
**Describe the Feature**
Clear description of the requested feature.

**Use Cases**
Why would this feature be useful?

**Proposed Implementation**
How should this feature work?

**Alternative Solutions**
Other ways to solve this problem?

**Additional Context**
Any other relevant information.
```

---

## 🐛 Support & Troubleshooting

### Common Issues

#### Issue: "Cannot find symbol: class MosbergBlocks"

**Solution**: Ensure MosbergAPI is added to `build.gradle`:
```gradle
dependencies {
    modImplementation "dk.mosberg:mosbergapi:1.0.0"
}
```

Also verify the dependency is downloaded:
```bash
./gradlew --refresh-dependencies build
```

#### Issue: "Mixin not applied"

**Solution**: Verify `fabric.mod.json` includes mixin config:
```json
{
  "mixins": ["yourmod.mixins.json"]
}
```

And check your `yourmod.mixins.json` exists and is formatted correctly:
```json
{
  "required": true,
  "minVersion": "0.8",
  "package": "com.yourname.mixin",
  "compatibilityLevel": "JAVA_21",
  "mixins": ["MixinClassName"],
  "client": ["ClientMixinClassName"]
}
```

#### Issue: "Config not loading"

**Solution**: Call `ConfigManager.load()` during initialization:
```java
@Override
public void onInitialize() {
    // Load config BEFORE registering other content
    ModConfig.load();
    
    // Then register other systems
    ModBlocks.initialize();
    ModItems.initialize();
}
```

#### Issue: "Data generation not running"

**Solution**: 
1. Register data generator in `fabric.mod.json`:
```json
{
  "entrypoints": {
    "fabric-datagen": ["com.yourmod.datagen.ModDataGen"]
  }
}
```

2. Implement `DataGeneratorEntrypoint`:
```java
public class ModDataGen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(DataGeneratorContext context) {
        context.addProvider(ModRecipes::new);
        context.addProvider(ModLootTables::new);
        context.addProvider(ModModels::new);
    }
}
```

3. Run: `./gradlew runDatagen`

#### Issue: "Client-side rendering not working"

**Solution**: Ensure you're calling registration in client initializer:
```java
@Environment(EnvType.CLIENT)
public class YourModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register renderers ONLY on client side
        ModClientSetup.registerRenderers();
        ModClientSetup.registerScreens();
    }
}
```

#### Issue: "Command not found in-game"

**Solution**: Verify command registration:
```java
public static void registerCommands() {
    CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) -> {
        // Register command here
    });
}

// Call in main initializer
@Override
public void onInitialize() {
    ModCommands.registerCommands();
}
```

#### Issue: "Gradle build fails with Java version error"

**Solution**: Set Java version in `gradle.properties`:
```properties
java_version = 21
```

And verify `build.gradle` targets Java 21:
```gradle
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
```

### Debug Logging

Enable debug logs by setting log level in run configuration:

```bash
# VM Options
-Dlog4j.configurationFile=log4j-debug.xml
-Dlogger.mymod=DEBUG
```

Or configure in IDE run configuration:
- IntelliJ: Run > Edit Configurations > VM Options
- Eclipse: Run > Run Configurations > Arguments > VM Arguments

### Performance Tips

1. **Cache expensive operations**:
   ```java
   private static final float CACHED_VALUE = calculateExpensive();
   ```

2. **Avoid allocations in hot paths**:
   ```java
   // Bad: allocates Vec3d every frame
   public void update() {
       Vec3d pos = new Vec3d(x, y, z); // avoid this
   }
   
   // Good: reuse or calculate in-place
   private final Vec3d pos = new Vec3d(0, 0, 0);
   public void update() {
       pos.set(x, y, z);
   }
   ```

3. **Use appropriate collections**:
   - `HashMap<K, V>` for fast lookups
   - `ArrayList<T>` for iteration
   - `HashSet<T>` for existence checks
   - `LinkedHashMap<K, V>` for ordered iteration

### Getting Help

- **Issues**: [GitHub Issues](https://github.com/Mosberg/mosbergapi/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Mosberg/mosbergapi/discussions)
- **Wiki**: [GitHub Wiki](https://github.com/Mosberg/mosbergapi/wiki)
- **Discord**: [Fabric Discord](https://discord.gg/v6v4pMv) - #modding-help

---

## 📊 Project Status

| Component | Status | Details |
|-----------|--------|---------|
| **Core Registry** | ✅ Stable | 16 registry classes, 60+ types |
| **Utility Helpers** | ✅ Stable | 25+ helpers production-ready |
| **Data Generation** | ✅ Stable | Recipes, loot, models with fluent API |
| **Command System** | ✅ Stable | 8 command templates + registration |
| **Event System** | ✅ Stable | Custom events with callbacks |
| **Config Manager** | ✅ Stable | JSON-based configuration |
| **Client Rendering** | ✅ Stable | Entity, block, screen handlers |
| **Client Screen Management** | ✅ Stable | Handler registration, UI utilities |
| **Render States (1.21+)** | ✅ Stable | Entity and block entity rendering |
| **Testing Framework** | 🚧 Beta | Unit testing utilities |
| **GUI Widgets** | 📋 Planned | Button, label, text box widgets |
| **Advanced Networking** | 📋 Planned | Packet framework |
| **Animation System** | 📋 Planned | Entity animation helpers |

---

## 📜 License

This project is licensed under the **MIT License**. See [LICENSE](LICENSE) for details.

You are free to:

✅ Use MosbergAPI in commercial projects  
✅ Modify and distribute the source code  
✅ Include in mods without attribution (but appreciated)  
✅ Use for personal or educational purposes  

The only requirement is to include the original license and copyright notice.

---

## 🙏 Credits & Acknowledgments

### Key Contributors

- **Mosberg** - Original author and maintainer
- **Fabric Team** - Excellent modding tools and API
- **Yarn Contributors** - Readable source mappings
- **Minecraft/Mojang Studios** - The game itself
- **Community** - Bug reports, suggestions, and support

### Technologies Used

- [Fabric Loader](https://fabricmc.net/) - Modern modding framework
- [Fabric API](https://github.com/FabricMC/fabric) - Essential hooks and utilities
- [Yarn Mappings](https://github.com/FabricMC/yarn) - Readable source names
- [Gradle Loom](https://github.com/FabricMC/fabric-loom) - Build automation
- [SLF4J](https://www.slf4j.org/) - Logging framework
- [GSON](https://github.com/google/gson) - JSON serialization
- [JUnit 5](https://junit.org/) - Testing framework
- [JetBrains Annotations](https://www.jetbrains.com/help/idea/annotating-source-code.html) - Null safety

---

## 📞 Contact & Social

- **Author**: [@Mosberg](https://github.com/Mosberg)
- **Repository**: [github.com/Mosberg/mosbergapi](https://github.com/Mosberg/mosbergapi)
- **Bug Reports**: [Issues](https://github.com/Mosberg/mosbergapi/issues)
- **Feature Requests**: [Discussions](https://github.com/Mosberg/mosbergapi/discussions)
- **Documentation**: [Wiki](https://github.com/Mosberg/mosbergapi/wiki)
- **Discord**: [Fabric Community](https://discord.gg/v6v4pMv)

---

## 📝 Version History

| Version | Minecraft | Status | Notes |
|---------|-----------|--------|-------|
| **1.0.0** | 1.21.10 | ✅ Released | Initial stable release with complete feature set |
| 0.9.0 | 1.21.10 | ✅ Released | Beta with all registries and helpers |
| 0.5.0 | 1.21.1 | ✅ Released | Early beta release |

*See [CHANGELOG.md](CHANGELOG.md) for detailed version history and migration guides.*

---

## 🎓 Learning Resources

### Official Documentation

- [Minecraft Wiki](https://minecraft.wiki) - Game mechanics and content
- [Fabric Wiki](https://wiki.fabricmc.net) - Fabric framework concepts
- [Yarn Mappings](https://mappings.dev) - Class and method names
- [Fabric API Javadoc](https://maven.fabricmc.net) - API documentation
- [Minecraft Modding Forums](https://www.minecraftforum.net) - Community resources

### Tutorials & Guides

- [Fabric Official Tutorial](https://wiki.fabricmc.net/tutorial:setup) - Getting started
- [Rendering Guide](https://docs.fabricmc.net/develop/rendering) - Rendering systems
- [Packet Guide](https://docs.fabricmc.net/develop/networking) - Custom networking
- [Data Generation Guide](https://docs.fabricmc.net/develop/datagen) - Datagen setup

### Community

- [Fabric Discord](https://discord.gg/v6v4pMv) - Official server
- [Fabric GitHub Discussions](https://github.com/FabricMC/fabric/discussions) - Questions & answers
- [Minecraft Modding Subreddit](https://www.reddit.com/r/fabricmc/) - Community discussion

---

## ⚡ Quick Reference

### Common Commands

```bash
# Build mod
./gradlew build

# Run client for testing
./gradlew runClient

# Run server for testing
./gradlew runServer

# Generate data (recipes, models, loot)
./gradlew runDatagen

# Format code with spotless
./gradlew spotlessApply

# Generate Javadoc
./gradlew javadoc

# Run tests
./gradlew test

# Clean build directory
./gradlew clean

# Create release build
./gradlew build --scan
```

### Common Imports

```java
// Registry classes
import dk.mosberg.api.registry.*;

// Utility helpers
import dk.mosberg.api.util.*;

// Command system
import dk.mosberg.api.command.*;

// Data generation
import dk.mosberg.api.data.provider.*;

// Events
import dk.mosberg.api.event.*;

// Configuration
import dk.mosberg.api.config.*;

// Client-side (only in client code)
import dk.mosberg.api.client.registry.*;
import dk.mosberg.api.client.util.*;
```

### Common Annotations

```java
@Nullable               // Value can be null
@NotNull                // Value must not be null
@Override               // Method overrides parent
@FunctionalInterface    // Single abstract method
@Deprecated(since="1.0.0")  // No longer recommended
@Environment(EnvType.CLIENT) // Client-side only
```

### Gradle Properties

```gradle
# In gradle.properties
minecraft_version = 1.21.10
yarn_mappings = 1.21.10+build.3
fabric_loader_version = 0.18.3
fabric_version = 0.138.4
java_version = 21
```

---

## 🚀 Next Steps

1. **Explore Examples** - Check out the [examples](examples/) directory for complete mod implementations
2. **Read the Wiki** - Detailed guides on every component at [GitHub Wiki](https://github.com/Mosberg/mosbergapi/wiki)
3. **Ask Questions** - Use [Discussions](https://github.com/Mosberg/mosbergapi/discussions) for help and questions
4. **Contribute** - Submit PRs for improvements, fixes, or new features
5. **Share Your Mods** - Built something cool using MosbergAPI? Show us in discussions!

---

## 🎯 Development Philosophy

MosbergAPI is built on these core principles:

- **Developer Experience First**: Every API should be intuitive and require minimal boilerplate
- **Safety by Default**: Type-safe APIs, null annotations, and compile-time validation
- **Comprehensive Documentation**: Every public method has clear Javadoc with examples
- **Modern Java**: Leverage Java 21 features for cleaner, more efficient code
- **Best Practices**: Follow Minecraft and Fabric conventions throughout
- **Community Driven**: Open to feedback and contributions from modders

---

**MosbergAPI** - Accelerating Minecraft mod development, one helper at a time.

*Made with ❤️ by Mosberg for the Minecraft modding community*
