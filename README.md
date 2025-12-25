# 🏗️ MosbergAPI

> A comprehensive **Fabric API library** for Minecraft 1.21.10+ mod development, designed to eliminate boilerplate code and accelerate mod creation with powerful utilities, enhanced registries, and streamlined tooling.

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

**MosbergAPI** is a foundational library mod that provides developers with battle-tested utilities, intelligent registry systems, and powerful tooling to accelerate Minecraft mod development. It abstracts away common boilerplate, handles edge cases, and follows modern Fabric best practices.

### What It Does

- ✅ **Simplifies Registration** - Register blocks, items, entities, sounds, particles, enchantments, and more with minimal code
- ✅ **Data Generation** - Auto-generate recipes, loot tables, and models with fluent builders
- ✅ **27+ Utility Helpers** - Production-ready helpers for inventory, entities, blocks, NBT, particles, and more
- ✅ **Command System** - Built-in command framework with 8+ pre-made commands
- ✅ **Client-Side Tools** - Rendering, screen handling, texture, and model utilities
- ✅ **Event System** - Custom events for mod interactions
- ✅ **Configuration** - JSON-based config management out of the box
- ✅ **Type Safety** - Modern Java with records, pattern matching, and null safety annotations
- ✅ **Comprehensive Logging** - SLF4J integration for debugging

### What It's NOT

❌ MosbergAPI adds **no content** to Minecraft by itself  
❌ It's not a gameplay mod—it's a **developer library**  
❌ Not required for vanilla Minecraft—only for mods using it

---

## ✨ Key Features

### 🔧 Registry System (`dk.mosberg.api.registry`)

**Simplified registration with type-safe builders for 20+ content types:**

| Class | Purpose |
|-------|---------|
| **MosbergBlocks** | Block registration with automatic BlockItem creation |
| **MosbergItems** | Item registration with centralized management |
| **MosbergEntities** | Entity type registration with builder pattern |
| **MosbergItemGroups** | Creative inventory tab management |
| **MosbergSounds** | Sound event registration and querying |
| **MosbergParticles** | Custom particle effect registration |
| **MosbergBlockEntities** | Block entity type registration |
| **MosbergFluidS** | Fluid registration (water, lava, custom) |
| **MosbergEnchantments** | Custom enchantment registration |
| **MosbergPotions** | Potion effect registration |
| **MosbergStatusEffects** | Status effect registration |
| **MosbergDamageTypes** | Custom damage type registration (1.21+) |
| **MosbergDataComponents** | Data component type registration |
| **MosbergGameEvents** | Game event registration |
| **MosbergAttributes** | Entity attribute registration |
| **MosbergTags** | Tag creation and management |
| **MosbergVillagers** | Villager profession and type registration |
| **MosbergWorldGen** | World generation feature registration |
| **MosbergScreenHandlerTypes** | GUI screen handler registration |
| **MosbergRecipes** | Recipe type and serializer registration |
| **MosbergRegistries** | Master registry for 60+ Minecraft registries |

### 🛠️ Utility Helpers (`dk.mosberg.api.util`)

**27+ production-ready helper classes for common operations:**

#### Core Utilities

| Helper | Purpose |
|--------|---------|
| **BlockHelper** | Block state queries, neighbor detection, material checks |
| **EntityHelper** | Entity spawning, teleportation, knockback, AI manipulation |
| **ItemHelper** | ItemStack manipulation, NBT reading/writing, durability |
| **InventoryHelper** | Item insertion, transfer between inventories, capacity checks |
| **WorldHelper** | World state manipulation, weather, entity queries, explosions |
| **NBTHelper** | NBT data serialization, type-safe reading/writing |

#### Attributes & Effects

| Helper | Purpose |
|--------|---------|
| **AttributeHelper** | Entity attribute modification, damage tracking, speed |
| **EnchantmentUtil** | Enchantment compatibility, level detection, application |
| **StatusEffectHelper** | Status effect application and management |
| **PotionHelper** | Potion creation, brewing, effect application |
| **DamageTypeHelper** | Custom damage type management, source detection |

#### Items & Data

| Helper | Purpose |
|--------|---------|
| **DataComponentHelper** | Data component manipulation and queries |
| **TagHelper** | Tag creation and querying for items, blocks, fluids |
| **RecipeHelper** | Recipe querying at runtime, type filtering |
| **ItemGroupHelper** | Item group management and customization |

#### Game Mechanics

| Helper | Purpose |
|--------|---------|
| **CommandHelper** | Command registration, argument parsing, result messaging |
| **GameEventHelper** | Game event creation and triggering |
| **VillagerHelper** | Villager trade management, profession changes |
| **FluidHelper** | Fluid placement, flowing, bucket operations |
| **ParticleHelper** | Particle spawning with custom effects |
| **SoundHelper** | Sound playing, volume, pitch control |

#### Utilities

| Helper | Purpose |
|--------|---------|
| **MosbergHelper** | General utilities (version info, initialization checks) |
| **SerializationHelper** | GSON-based serialization for configs |
| **NetworkHelper** | Custom packet handling, player-specific networking |
| **MosbergEnchantmentHelper** | Advanced enchantment utilities |

### 🎮 Command System (`dk.mosberg.api.command`)

**8+ pre-made commands with framework for custom commands:**

| Command | Purpose | Usage |
|---------|---------|-------|
| **ItemCommand** | ItemStack inspection and manipulation | `/item <subcommand>` |
| **EntityCommand** | Entity spawning and management | `/entity <subcommand>` |
| **BlockCommand** | Block state queries and modification | `/block <subcommand>` |
| **WorldCommand** | World manipulation (weather, time) | `/world <subcommand>` |
| **ConfigCommand** | Configuration management | `/config <subcommand>` |
| **DebugCommand** | Debugging utilities | `/debug <subcommand>` |
| **RegistryCommand** | Registry inspection | `/registry <subcommand>` |
| **HelpCommand** | Command documentation | `/help` |

### 📊 Data Generation (`dk.mosberg.api.data`)

**Compile-time generation of recipes, loot tables, and models:**

#### Server-Side (`dk.mosberg.api.data.provider`)

- **MosbergRecipeProvider** - Fluent builders for crafting, smelting, stonecutting
- **MosbergLootTableProvider** - Block, entity, and chest loot generation
- **MosbergApiRecipeProvider** - Built-in examples and templates
- **MosbergApiLootTableProvider** - Pre-built loot table generators

#### Client-Side (`dk.mosberg.api.client.data`)

- **MosbergModelProvider** - 1.21+ block and item model generation
- **MosbergApiModelProvider** - Model template examples
- **MosbergApiDataGenerator** - Coordinated data generation

### 🎨 Client-Side Systems (`dk.mosberg.api.client`)

#### Registries

- **MosbergRenderers** - Entity/block renderer registration with humanoid/quadruped templates
- **MosbergModels** - Model layer management and creation
- **MosbergModelLayers** - Pre-defined model layer system
- **MosbergRenderStates** - 1.21+ render state system utilities
- **MosbergScreenHandlers** - GUI screen handler registration

#### Utilities (6 helpers)

| Helper | Purpose |
|--------|---------|
| **RenderHelper** | Custom rendering, overlays, GL state management |
| **ModelHelper** | Model creation, texture, animation utilities |
| **ModelLayersHelper** | Model layer utilities and templates |
| **RenderStatesHelper** | 1.21+ render state manipulation |
| **ScreenHelper** | GUI widget management, event handling |
| **TextureHelper** | Texture loading, binding, manipulation |
| **ScreenHandlerHelper** | Screen handler utilities and customization |

### 🔌 Advanced Systems

| System | Purpose |
|--------|---------|
| **Event System** (`dk.mosberg.api.event`) | Custom event framework (BlockMined, PlayerJoin, etc.) |
| **Config Manager** (`dk.mosberg.api.config`) | JSON-based configuration with auto-generation |
| **Documentation Generator** (`dk.mosberg.api.doc`) | Auto-generate API documentation |
| **Test Framework** (`dk.mosberg.api.test`) | Helpers for unit testing mods |
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
  }
}
```

#### 3. Create Your Mod Class

```java
package com.yourname;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ModInitializer;

public class YourMod implements ModInitializer {
    public static final String MOD_ID = "yourmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing YourMod with MosbergAPI!");
        // Your registration code here
    }
}
```

---

## 🏛️ Architecture

### Design Principles

1. **Separation of Concerns** - Clear package organization by functionality
2. **Type Safety** - Modern Java (records, sealed classes, pattern matching)
3. **Fluent APIs** - Builder patterns for complex objects
4. **Null Safety** - `@Nullable` and `@NotNull` annotations throughout
5. **Comprehensive Logging** - SLF4J with appropriate log levels
6. **Zero Boilerplate** - Convention over configuration

### Dependency Graph

```
MosbergApi (Mod Initializer)
  ├── Registry System (20+ registries)
  │   └── MosbergRegistries (60+ registry methods)
  ├── Utility Helpers (27+ classes)
  ├── Command System (8+ commands)
  ├── Event System
  ├── Configuration Manager
  ├── Client Systems
  │   ├── Renderers (entity, block)
  │   ├── Models (creation, layers)
  │   └── Client Utilities (6+ helpers)
  └── Data Generators
      ├── Recipes
      └── Loot Tables
```

---

## 📚 API Reference

### Registry Operations

#### Registering a Block

```java
import dk.mosberg.api.registry.MosbergBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock;

// Automatic BlockItem creation
public static final Block CUSTOM_BLOCK = MosbergBlocks.register(
    "custom_block",
    new Block(AbstractBlock.Settings.create()
        .strength(2.0f)
        .requiresTool())
);

// No BlockItem
public static final Block DECORATIVE_BLOCK = MosbergBlocks.register(
    "decorative",
    new Block(AbstractBlock.Settings.create()),
    false
);

// Custom item settings
public static final Block SPECIAL_BLOCK = MosbergBlocks.register(
    "special",
    new Block(AbstractBlock.Settings.create()),
    new Item.Settings().rarity(Rarity.RARE)
);
```

#### Registering Enchantments

```java
import dk.mosberg.api.registry.MosbergEnchantments;

public static final Enchantment CUSTOM_ENCHANTMENT = MosbergEnchantments.register(
    "custom_enchantment",
    Enchantment.create(
        Text.translatable("enchantment.yourmod.custom"),
        Effects.ATTACK_DAMAGE,
        1,
        4,
        Enchantment.constantCost(10),
        Enchantment.constantCost(60)
    )
);
```

#### Registering Fluids

```java
import dk.mosberg.api.registry.MosbergFluids;

public static final FlowableFluid CUSTOM_FLUID = MosbergFluids.register(
    "custom_fluid",
    new FlowableFluid.Flowing(
        CUSTOM_FLUID_SETTINGS
    )
);
```

### Utility Helper Usage

#### Inventory Management

```java
import dk.mosberg.api.util.InventoryHelper;

// Add item to inventory (returns leftovers)
ItemStack leftovers = InventoryHelper.addItemToInventory(inventory, new ItemStack(Items.DIAMOND, 64));

// Transfer between inventories
int transferred = InventoryHelper.transferItems(sourceInv, destInv, 32);

// Check capacity
boolean hasSpace = InventoryHelper.hasSpace(inventory, new ItemStack(Items.IRON_INGOT));

// Count items
int count = InventoryHelper.countItems(inventory, Items.GOLD_ORE);
```

#### Command Registration

```java
import dk.mosberg.api.command.MosbergCommands;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            MosbergCommands.register(dispatcher, "yourcommand", 
                ctx -> {
                    ctx.getSource().sendFeedback(
                        () -> Text.literal("Hello!"),
                        false
                    );
                    return 1;
                }
            );
        });
    }
}
```

#### Client Rendering

```java
import dk.mosberg.api.client.registry.MosbergRenderers;
import dk.mosberg.api.client.util.RenderHelper;

// Register entity renderer
MosbergRenderers.registerEntityRenderer(
    ModEntities.CUSTOM_MOB,
    CustomMobRenderer::new,
    ModModelLayers.CUSTOM_MOB,
    CustomMobModel::getTexturedModelData
);

// Use render helper
RenderHelper.drawRect(matrices, x, y, width, height, color);
RenderHelper.drawString(matrices, text, x, y, textColor);
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

public class MyMod implements ModInitializer {
    public static final String MOD_ID = "mymod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing MyMod!");
        
        // Register all content
        ModBlocks.initialize();
        ModItems.initialize();
        ModEntities.initialize();
        ModEnchantments.initialize();
        ModSounds.initialize();
        
        // Register events
        ModEvents.register();
        
        // Load config
        ModConfig.load();
        
        LOGGER.info("MyMod initialized successfully!");
    }
}

// Content registration
package com.example.mymod.content;

import dk.mosberg.api.registry.*;

public class ModBlocks {
    public static final Block RUBY_BLOCK = MosbergBlocks.register(
        "ruby_block",
        new Block(AbstractBlock.Settings.copy(Blocks.DIAMOND_BLOCK))
    );

    public static void initialize() {
        // Called from main initializer
    }
}

public class ModEnchantments {
    public static final Enchantment RUBY_SHARPNESS = MosbergEnchantments.register(
        "ruby_sharpness",
        Enchantment.create(
            Text.translatable("enchantment.mymod.ruby_sharpness"),
            Effects.ATTACK_DAMAGE,
            2,
            5,
            Enchantment.constantCost(10),
            Enchantment.constantCost(60)
        )
    );

    public static void initialize() {
        // Called from main initializer
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
    │   │   ├── 📄 MosbergApi.java       # Main mod initializer
    │   │   │
    │   │   ├── 📁 command/              # Command system
    │   │   │   ├── 📁 commands/         # Pre-made commands
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
    │   │   │   └── 📄 ConfigManager.java
    │   │   │
    │   │   ├── 📁 data/                 # Data generation
    │   │   │   ├── 📁 provider/
    │   │   │   │   ├── 📄 MosbergRecipeProvider.java
    │   │   │   │   └── 📄 MosbergLootTableProvider.java
    │   │   │   ├── 📄 MosbergApiRecipeProvider.java
    │   │   │   └── 📄 MosbergApiLootTableProvider.java
    │   │   │
    │   │   ├── 📁 doc/
    │   │   │   └── 📄 DocumentationGenerator.java
    │   │   │
    │   │   ├── 📁 event/
    │   │   │   └── 📄 MosbergEvents.java
    │   │   │
    │   │   ├── 📁 mixin/
    │   │   │   └── 📄 MosbergMixin.java
    │   │   │
    │   │   ├── 📁 registry/             # Registry helpers (20+ classes)
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
    │   │   │   ├── 📄 MosbergRegistries.java
    │   │   │   ├── 📄 MosbergScreenHandlerTypes.java
    │   │   │   ├── 📄 MosbergSounds.java
    │   │   │   ├── 📄 MosbergStatusEffects.java
    │   │   │   ├── 📄 MosbergTags.java
    │   │   │   ├── 📄 MosbergVillagers.java
    │   │   │   └── 📄 MosbergWorldGen.java
    │   │   │
    │   │   ├── 📁 test/
    │   │   │   └── 📄 TestHelper.java
    │   │   │
    │   │   └── 📁 util/                 # Utility helpers (27 classes)
    │   │       ├── 📄 AttributeHelper.java
    │   │       ├── 📄 BlockHelper.java
    │   │       ├── 📄 CommandHelper.java
    │   │       ├── 📄 DamageTypeHelper.java
    │   │       ├── 📄 DataComponentHelper.java
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
    │       ├── 📄 fabric.mod.json
    │       ├── 📄 mosbergapi.mixins.json
    │       ├── 📄 pack.mcmeta
    │       │
    │       ├── 📁 assets/mosbergapi/
    │       │   ├── 📁 atlases/
    │       │   ├── 📁 block/
    │       │   ├── 📁 blockstates/
    │       │   ├── 📁 equipment/
    │       │   ├── 📁 items/
    │       │   ├── 📁 lang/
    │       │   │   └── 📄 en_us.json
    │       │   ├── 📁 models/
    │       │   │   ├── 📁 block/
    │       │   │   ├── 📁 entity/
    │       │   │   ├── 📁 equipment/
    │       │   │   └── 📁 item/
    │       │   ├── 📁 particles/
    │       │   ├── 📁 post_effect/
    │       │   ├── 📁 shaders/
    │       │   ├── 📁 sounds/
    │       │   ├── 📁 texts/
    │       │   ├── 📁 textures/
    │       │   │   ├── 📁 block/
    │       │   │   ├── 📁 entity/
    │       │   │   └── 📁 item/
    │       │   └── 📄 icon.png
    │       │
    │       └── 📁 data/mosbergapi/
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
        │   ├── 📄 MosbergApiClient.java
        │   │
        │   ├── 📁 data/                 # Client data generation
        │   │   ├── 📁 provider/
        │   │   │   └── 📄 MosbergModelProvider.java
        │   │   ├── 📄 MosbergApiDataGenerator.java
        │   │   └── 📄 MosbergApiModelProvider.java
        │   │
        │   ├── 📁 mixin/client/
        │   │   └── 📄 MosbergClientMixin.java
        │   │
        │   ├── 📁 registry/              # Client registries
        │   │   ├── 📄 MosbergModelLayers.java
        │   │   ├── 📄 MosbergModels.java
        │   │   ├── 📄 MosbergRenderers.java
        │   │   ├── 📄 MosbergRenderStates.java
        │   │   └── 📄 MosbergScreenHandlers.java
        │   │
        │   └── 📁 util/                 # Client utilities (7 helpers)
        │       ├── 📄 ModelHelper.java
        │       ├── 📄 ModelLayersHelper.java
        │       ├── 📄 RenderHelper.java
        │       ├── 📄 RenderStatesHelper.java
        │       ├── 📄 ScreenHandler Helper.java
        │       ├── 📄 ScreenHelper.java
        │       └── 📄 TextureHelper.java
        │
        └── 📁 resources/
            └── 📄 mosbergapi.client.mixins.json
```

### Summary Statistics

- **20+ Registry Classes** - Comprehensive content registration
- **27 Utility Helpers** - Production-ready functionality
- **8 Pre-made Commands** - Command framework with examples
- **7 Client-Side Helpers** - Rendering and UI utilities
- **60+ Registry Methods** - Master registry support
- **Modular Design** - Easy to use, easy to extend

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

# Format code
./gradlew spotlessApply

# Run tests
./gradlew test
```

### Build Outputs

After building, find outputs in `build/libs/`:

| JAR | Purpose |
|-----|---------|
| `mosbergapi-1.0.0.jar` | Production mod JAR |
| `mosbergapi-1.0.0-sources.jar` | Source code (for developers) |
| `mosbergapi-1.0.0-javadoc.jar` | API documentation |

---

## 🤝 Contributing

### Code Style Guidelines

1. **Formatting**
   - 4 spaces per indent (no tabs)
   - 120 character line limit
   - Format with `spotlessApply` before commit

2. **Naming**
   - Classes: `PascalCase` (e.g., `EntityHelper`)
   - Methods: `camelCase` (e.g., `teleportEntity`)
   - Constants: `UPPER_SNAKE_CASE` (e.g., `MOD_ID`)
   - Private fields: `camelCase` with `private` modifier

3. **Documentation**
   - Public API methods require JavaDoc
   - Use `@param`, `@return`, `@throws` tags
   - Include `@since` version tag
   - Add `@author "Mosberg"` for consistency
   - Include `@example` tags for complex methods

4. **Type Safety**
   - Use `@Nullable` and `@NotNull` annotations
   - Validate parameters with clear error messages
   - Use sealed classes and records where appropriate
   - Enable strict null checking

### Contribution Steps

1. **Fork & Clone**
   ```bash
   git clone https://github.com/YOUR_USERNAME/mosbergapi.git
   cd mosbergapi
   ```

2. **Create Feature Branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Make Changes**
   - Follow code style guidelines
   - Add tests if applicable
   - Update documentation

4. **Format & Test**
   ```bash
   ./gradlew spotlessApply    # Format code
   ./gradlew test             # Run tests
   ./gradlew build            # Full build
   ```

5. **Commit with Clear Message**
   ```bash
   git commit -m "Add amazing feature

   Description of what the feature does and why it was added.
   - Point 1
   - Point 2
   - Fixes #123"
   ```

6. **Push & Create Pull Request**
   ```bash
   git push origin feature/amazing-feature
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

#### Issue: "Mixin not applied"

**Solution**: Verify `fabric.mod.json`:
```json
{
  "mixins": ["mosbergapi.mixins.json"]
}
```

#### Issue: "Command not registered"

**Solution**: Register commands in `ModInitializer.onInitialize()`:
```java
CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
    MosbergCommands.registerAllCommands(dispatcher);
});
```

#### Issue: "Data generation not running"

**Solution**: Ensure your `DataGeneratorEntrypoint` is registered in `fabric.mod.json`:
```json
{
  "entrypoints": {
    "fabric-datagen": ["com.yourmod.datagen.ModDataGen"]
  }
}
```

### Getting Help

- **Issues**: [GitHub Issues](https://github.com/Mosberg/mosbergapi/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Mosberg/mosbergapi/discussions)
- **Wiki**: [GitHub Wiki](https://github.com/Mosberg/mosbergapi/wiki)
- **Discord**: Join the Fabric community (link in repo)

---

## 📊 Project Status

| Component | Status | Details |
|-----------|--------|---------|
| **Core Registry (20+)** | ✅ Stable | All registries working perfectly |
| **Utility Helpers (27)** | ✅ Stable | Production-ready functionality |
| **Command System (8)** | ✅ Stable | Pre-made commands with framework |
| **Data Generation** | ✅ Stable | Recipes, loot, models |
| **Event System** | ✅ Stable | BlockMined, PlayerJoin events |
| **Client Rendering** | ✅ Stable | Entity and block rendering |
| **Config Manager** | ✅ Stable | JSON-based configuration |
| **Client Utilities (7)** | ✅ Stable | Render, screen, model helpers |
| **Testing Framework** | 🚧 Beta | Unit testing utilities |
| **GUI Utilities** | 📋 Planned | Advanced widget system |
| **Networking Framework** | 📋 Planned | Advanced packet handling |

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
- **Yarn Contributors** - Mappings
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

---

## 📞 Contact & Social

- **Author**: [@Mosberg](https://github.com/Mosberg)
- **Repository**: [github.com/Mosberg/mosbergapi](https://github.com/Mosberg/mosbergapi)
- **Bug Reports**: [Issues](https://github.com/Mosberg/mosbergapi/issues)
- **Feature Requests**: [Discussions](https://github.com/Mosberg/mosbergapi/discussions)
- **Documentation**: [Wiki](https://github.com/Mosberg/mosbergapi/wiki)

---

## 📝 Version History

| Version | Minecraft | Status | Notes |
|---------|-----------|--------|-------|
| **1.0.0** | 1.21.10 | ✅ Released | Initial stable release with 20+ registries, 27 helpers, command system |
| 0.5.0 | 1.21.1 | ✅ Released | Beta test release |

*See [CHANGELOG.md](CHANGELOG.md) for detailed version history.*

---

## 🎓 Learning Resources

### Documentation

- [Minecraft Wiki](https://minecraft.wiki) - Game mechanics
- [Fabric Wiki](https://wiki.fabricmc.net) - Fabric concepts
- [Yarn Mappings](https://mappings.dev) - Class/method names
- [Fabric API Javadoc](https://maven.fabricmc.net) - API docs

### Community

- [Fabric Discord](https://discord.gg/v6v4pMv) - Official Discord
- [Minecraft Modding Forums](https://www.minecraftforum.net) - Community
- [GitHub Discussions](https://github.com/Mosberg/mosbergapi/discussions) - This project

---

## ⚡ Quick Reference

### Common Commands

```bash
# Build mod
./gradlew build

# Run client
./gradlew runClient

# Generate data
./gradlew runDatagen

# Format code
./gradlew spotlessApply

# Create release
./gradlew build --scan
```

### Common Imports

```java
// Registry imports
import dk.mosberg.api.registry.*;

// Utility imports
import dk.mosberg.api.util.*;

// Command imports
import dk.mosberg.api.command.*;

// Data generation imports
import dk.mosberg.api.data.provider.*;

// Event imports
import dk.mosberg.api.event.*;

// Client imports
import dk.mosberg.api.client.registry.*;
import dk.mosberg.api.client.util.*;

// Config imports
import dk.mosberg.api.config.*;
```

---

## 🚀 Next Steps

1. **Explore Examples** - Check out the [examples](examples/) directory
2. **Read the Wiki** - Detailed guides on every component
3. **Ask Questions** - Use [Discussions](https://github.com/Mosberg/mosbergapi/discussions) for help
4. **Contribute** - Submit PRs for improvements
5. **Share Your Mods** - Built something cool? Show us!

---

**MosbergAPI** - *Accelerating Minecraft mod development, one helper at a time.*

*Made with ❤️ for the Minecraft modding community*