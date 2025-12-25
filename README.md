# 🏗️ MosbergAPI

> A comprehensive **Fabric API library** for Minecraft 1.21.10+ mod development, designed to eliminate boilerplate code and accelerate mod creation with powerful utilities, enhanced registries, and streamlined tooling.

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.10-brightgreen.svg)](https://www.minecraft.net/)
[![Yarn Mappings](https://img.shields.io/badge/Yarn-1.21.10+build.3-blue.svg)](https://maven.fabricmc.net/)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-0.138.4%2B1.21.10-orange.svg)](https://fabricmc.net/)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-0.18.3-orange.svg)](https://fabricmc.net/)
[![Fabric Loom](https://img.shields.io/badge/Fabric%20Loom-1.14.10-orange.svg)](https://github.com/FabricMC/fabric-loom)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

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

- ✅ **Simplifies Registration** - Register blocks, items, entities, sounds, and particles with minimal code
- ✅ **Data Generation** - Auto-generate recipes, loot tables, and models with builders
- ✅ **Utility Helpers** - 15+ helper classes for inventory, entities, blocks, NBT, and more
- ✅ **Client-Side Tools** - Rendering utilities and client-specific functionality
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

**Simplified registration with type-safe builders:**

| Class                 | Purpose                                       | Notable Methods                                                                                     |
| --------------------- | --------------------------------------------- | --------------------------------------------------------------------------------------------------- |
| **MosbergBlocks**     | Block registration with automatic BlockItem   | `register(name, block)`, `register(name, block, createItem)`, `register(name, block, itemSettings)` |
| **MosbergItems**      | Item registration with centralized management | `register(name, item)`                                                                              |
| **MosbergEntities**   | Entity type registration with builder pattern | `register(name, builder)`, `keyOf(name)`                                                            |
| **MosbergItemGroups** | Creative inventory tab management             | `register(name, group)`, `registerSimple(name, displayName, icon)`                                  |
| **MosbergSounds**     | Sound event registration                      | `register(name)`, `registerWithRange(name, range)`                                                  |
| **MosbergParticles**  | Custom particle registration                  | `registerSimple(name)`, `registerSimple(name, alwaysShow)`                                          |
| **MosbergRegistries** | Core registry for 60+ Minecraft registries    | Comprehensive type-safe methods for all content types                                               |

### 🛠️ Utility Helpers (`dk.mosberg.api.util`)

**15+ production-ready helper classes for common operations:**

| Helper                  | Use Cases                                                          |
| ----------------------- | ------------------------------------------------------------------ |
| **AttributeHelper**     | Entity attribute modification, damage tracking, speed adjustments  |
| **BlockHelper**         | Block state queries, neighbor detection, material checks           |
| **CommandHelper**       | Command registration, argument parsing, result messaging           |
| **EnchantmentUtil**     | Enchantment compatibility, level detection, application            |
| **EntityHelper**        | Entity spawning, teleportation, knockback, AI manipulation         |
| **InventoryHelper**     | Item insertion, transfer between inventories, capacity checks      |
| **ItemHelper**          | ItemStack manipulation, NBT reading/writing, durability management |
| **MosbergHelper**       | General utilities (version info, initialization checks)            |
| **NBTHelper**           | NBT data serialization, type-safe reading/writing                  |
| **NetworkHelper**       | Custom packet handling, player-specific networking                 |
| **RecipeHelper**        | Recipe querying at runtime, type filtering                         |
| **SerializationHelper** | GSON-based serialization for configs                               |
| **TagHelper**           | Tag creation and querying for items, blocks, fluids                |
| **WorldHelper**         | World state manipulation, weather control, entity queries          |
| **DamageTypeHelper**    | Custom damage type management, source detection                    |

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
- **MosbergRenderStatesHelper** - Render state utilities

### 🎨 Client-Side Systems (`dk.mosberg.api.client`)

- **MosbergRenderers** - Entity/block renderer registration with humanoid/quadruped templates
- **MosbergModels** - Model layer management and creation
- **MosbergModelLayers** - Pre-defined model layer system
- **RenderHelper** - Custom rendering, overlays, and GL state management
- **ModelHelper** - Texture and animation utilities

### 🔌 Advanced Systems

| System                                             | Purpose                                               |
| -------------------------------------------------- | ----------------------------------------------------- |
| **Event System** (`dk.mosberg.api.event`)          | Custom event framework (BlockMined, PlayerJoin, etc.) |
| **Config Manager** (`dk.mosberg.api.config`)       | JSON-based configuration with auto-generation         |
| **Documentation Generator** (`dk.mosberg.api.doc`) | Auto-generate API documentation                       |
| **Test Framework** (`dk.mosberg.api.test`)         | Helpers for unit testing mods                         |
| **Mixin System**                                   | Server & client-side mixins for core functionality    |

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
  ├── Registry System
  │   └── MosbergRegistries (60+ registry methods)
  ├── Utility Helpers (15+ classes)
  ├── Event System
  ├── Configuration Manager
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

#### Registering an Entity

```java
import dk.mosberg.api.registry.MosbergEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public static final EntityType<CustomEntity> CUSTOM_MOB = MosbergEntities.register(
    "custom_mob",
    EntityType.Builder.create(CustomEntity::new, SpawnGroup.CREATURE)
        .dimensions(0.8f, 1.8f)
        .maxTrackingRange(64)
        .updateIntervalMultiplier(3)
);
```

#### Accessing the Master Registry

```java
import dk.mosberg.api.registry.MosbergRegistries;

// Register anything with a single method
MosbergRegistries.registerStatusEffect("custom_effect", customEffect);
MosbergRegistries.registerPotion("custom_potion", customPotion);
MosbergRegistries.registerEnchantmentEffectComponentType("effect", componentType);
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
```

#### NBT Data

```java
import dk.mosberg.api.util.NBTHelper;
import net.minecraft.nbt.NbtCompound;

NbtCompound nbt = new NbtCompound();

// Type-safe writing
NBTHelper.setString(nbt, "name", "Custom Item");
NBTHelper.setInt(nbt, "level", 5);
NBTHelper.setDouble(nbt, "power", 2.5);

// Type-safe reading
String name = NBTHelper.getString(nbt, "name", "Default");
int level = NBTHelper.getInt(nbt, "level", 0);
```

### Data Generation

#### Creating Recipes

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
                // Shaped crafting
                createShaped(RecipeCategory.TOOLS, ModItems.CUSTOM_SWORD)
                    .pattern("D")
                    .pattern("D")
                    .pattern("S")
                    .input('D', Items.DIAMOND)
                    .input('S', Items.STICK)
                    .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                    .offerTo(exporter);

                // Shapeless crafting
                createShapeless(RecipeCategory.MISC, ModItems.CUSTOM_DUST)
                    .input(Items.DIAMOND)
                    .input(Items.IRON_INGOT)
                    .input(Items.STONE)
                    .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                    .offerTo(exporter);

                // Smelting
                offerSmelting(exporter, List.of(ModBlocks.CUSTOM_ORE),
                    RecipeCategory.MISC, ModItems.CUSTOM_INGOT,
                    1.0f, 200, "custom_ingot");

                // Stonecutting
                createStonecutting(RecipeCategory.BUILDING_BLOCKS,
                    ModBlocks.CUSTOM_STAIRS, ModBlocks.CUSTOM_BLOCK)
                    .criterion(hasItem(ModBlocks.CUSTOM_BLOCK),
                        conditionsFromItem(ModBlocks.CUSTOM_BLOCK))
                    .offerTo(exporter);
            }
        };
    }
}
```

#### Creating Loot Tables

```java
import dk.mosberg.api.data.provider.MosbergLootTableProvider;

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

        // Entity loot table
        addEntityLootTable(ModEntities.CUSTOM_MOB,
            LootTable.builder()
                .pool(LootPool.builder()
                    .rolls(UniformLootNumberProvider.create(0, 2))
                    .with(ItemEntry.builder(Items.DIAMOND))));
    }
}
```

### Configuration

```java
import dk.mosberg.api.config.ConfigManager;

public class ModConfig {
    public static final ConfigManager CONFIG = new ConfigManager("yourmod");

    public static int SPAWN_RATE = 10;
    public static boolean ENABLE_FEATURE = true;
    public static String CUSTOM_MESSAGE = "Hello!";

    public static void load() {
        SPAWN_RATE = CONFIG.getInt("spawn_rate", 10);
        ENABLE_FEATURE = CONFIG.getBoolean("enable_feature", true);
        CUSTOM_MESSAGE = CONFIG.getString("custom_message", "Hello!");

        CONFIG.save();
    }
}
```

### Custom Events

```java
import dk.mosberg.api.event.MosbergEvents;

public class ModEvents {
    public static void register() {
        MosbergEvents.BLOCK_MINED.register((player, world, pos, state) -> {
            if (state.isOf(Blocks.DIAMOND_ORE)) {
                player.sendMessage(Text.literal("You mined diamonds!"), false);
            }
        });
    }
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
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvent;

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

    public static void initialize() {
        // Called from main initializer
    }
}

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

    public static void initialize() {
        // Called from main initializer
    }
}

public class ModSounds {
    public static final SoundEvent RUBY_BREAK = MosbergSounds.register("block.ruby.break");
    public static final SoundEvent RUBY_STEP = MosbergSounds.register("block.ruby.step");
    public static final SoundEvent RUBY_PLACE = MosbergSounds.register("block.ruby.place");

    public static void initialize() {
        // Called from main initializer
    }
}

// Event handlers
package com.example.mymod.event;

import dk.mosberg.api.event.MosbergEvents;

public class ModEvents {
    public static void register() {
        MosbergEvents.BLOCK_MINED.register((player, world, pos, state) -> {
            if (state.isOf(ModBlocks.RUBY_ORE)) {
                // Bonus drops for ruby ore
                ItemStack drop = new ItemStack(ModItems.RUBY, 2);
                ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), drop);
            }
        });
    }
}

// Data generation
package com.example.mymod.datagen;

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
                // Ruby sword recipe
                createShaped(RecipeCategory.TOOLS, ModItems.RUBY_SWORD)
                    .pattern(" R ")
                    .pattern(" R ")
                    .pattern(" S ")
                    .input('R', ModItems.RUBY)
                    .input('S', Items.STICK)
                    .criterion(hasItem(ModItems.RUBY), conditionsFromItem(ModItems.RUBY))
                    .offerTo(exporter);

                // Smelting ruby ore
                offerSmelting(exporter, List.of(ModBlocks.RUBY_ORE),
                    RecipeCategory.MISC, ModItems.RUBY,
                    0.8f, 200, "ruby");
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
    │   │   ├── 📁 registry/                        # Registry helpers
    │   │   │   ├── 📄 MosbergBlocks.java
    │   │   │   ├── 📄 MosbergItems.java
    │   │   │   ├── 📄 MosbergEntities.java
    │   │   │   ├── 📄 MosbergItemGroups.java
    │   │   │   ├── 📄 MosbergSounds.java
    │   │   │   ├── 📄 MosbergParticles.java
    │   │   │   ├── 📄 MosbergRecipes.java
    │   │   │   ├── 📄 MosbergAttributes.java
    │   │   │   ├── 📄 MosbergGameEvents.java
    │   │   │   ├── 📄 MosbergDataComponents.java
    │   │   │   ├── 📄 MosbergDamageTypes.java
    │   │   │   └── 📄 MosbergRegistries.java        # Master registry
    │   │   │
    │   │   ├── 📁 test/
    │   │   │   └── 📄 TestHelper.java               # Testing utilities
    │   │   │
    │   │   └── 📁 util/                             # Utility helpers (15+)
    │   │       ├── 📄 AttributeHelper.java
    │   │       ├── 📄 BlockHelper.java
    │   │       ├── 📄 CommandHelper.java
    │   │       ├── 📄 DamageTypeHelper.java
    │   │       ├── 📄 EnchantmentUtil.java
    │   │       ├── 📄 EntityHelper.java
    │   │       ├── 📄 InventoryHelper.java
    │   │       ├── 📄 ItemHelper.java
    │   │       ├── 📄 MosbergHelper.java
    │   │       ├── 📄 ModelHelper.java
    │   │       ├── 📄 NBTHelper.java
    │   │       ├── 📄 NetworkHelper.java
    │   │       ├── 📄 RecipeHelper.java
    │   │       ├── 📄 SerializationHelper.java
    │   │       ├── 📄 TagHelper.java
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
    │       │   ├── 📁 lang/
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
    │       │       ├── 📁 item/
    │       │       └── 📁 entity/
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
        │   │   ├── 📄 MosbergApiModelProvider.java
        │   │   └── 📄 MosbergApiDataGenerator.java
        │   │
        │   ├── 📁 mixin/client/
        │   │   └── 📄 MosbergClientMixin.java
        │   │
        │   ├── 📁 registry/                         # Client registries
        │   │   ├── 📄 MosbergModelLayers.java
        │   │   ├── 📄 MosbergRenderers.java
        │   │   └── 📄 MosbergModels.java
        │   │
        │   ├── 📁 util/                             # Client utilities
        │   │   ├── 📄 RenderHelper.java
        │   │   └── 📄 ModelHelper.java
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
│   ├── block/               # Block models
│   ├── item/                # Item models (JSON)
│   ├── entity/              # Entity/mob models
│   └── equipment/           # Armor models
├── particles/               # Particle definitions
├── post_effect/             # Post-processing shaders
├── shaders/                 # GLSL shader files
├── sounds/                  # Sound effects (.ogg)
├── texts/                   # Text files (credits)
└── textures/
    ├── block/               # Block textures (PNG)
    ├── entity/              # Entity textures (PNG)
    └── item/                # Item textures (PNG)
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

# Format code
./gradlew spotlessApply

# Run tests
./gradlew test
```

### Build Outputs

After building, find outputs in `build/libs/`:

| JAR                            | Purpose                      |
| ------------------------------ | ---------------------------- |
| `mosbergapi-1.0.0.jar`         | Production mod JAR           |
| `mosbergapi-1.0.0-sources.jar` | Source code (for developers) |
| `mosbergapi-1.0.0-javadoc.jar` | API documentation            |

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

### Issue Templates

#### Bug Report

````markdown
**Describe the Bug**
Clear description of what happened.

**Minecraft Version**: 1.21.10
**MosbergAPI Version**: 1.0.0
**Fabric Loader Version**: 0.18.3
**Fabric API Version**: 0.138.4+1.21.10

**Steps to Reproduce**

1. ...
2. ...
3. ...

**Expected Behavior**
What should happen instead.

**Actual Behavior**
What actually happened.

**Error Log**

```logs
Paste relevant logs here
```
````

````

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
````

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

#### Issue: "Config not loading"

**Solution**: Call `ConfigManager.load()` during `ModInitializer.onInitialize()`:

```java
@Override
public void onInitialize() {
    ModConfig.load();  // Before other registration
}
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

### Debug Logging

Enable debug logs by setting log level in your IDE run configuration:

```bash
# VM Options
-Dlog4j.configurationFile=log4j-debug.xml
```

Or in `fabric.mod.json`:

```json
{
  "custom": {
    "modsDir": "mods"
  }
}
```

---

## 📊 Project Status

| Component                | Status     | Details                         |
| ------------------------ | ---------- | ------------------------------- |
| **Core Registry**        | ✅ Stable  | All 60+ registries working      |
| **Utility Helpers**      | ✅ Stable  | 15+ helpers production-ready    |
| **Data Generation**      | ✅ Stable  | Recipes, loot, models           |
| **Event System**         | ✅ Stable  | BlockMined, PlayerJoin events   |
| **Config Manager**       | ✅ Stable  | JSON-based configuration        |
| **Client Rendering**     | ✅ Stable  | Entity and block rendering      |
| **Testing Framework**    | 🚧 Beta    | Unit testing utilities          |
| **GUI Utilities**        | 📋 Planned | Button, label, text box widgets |
| **Networking Framework** | 📋 Planned | Advanced packet handling        |

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

| Version   | Minecraft | Status      | Notes                  |
| --------- | --------- | ----------- | ---------------------- |
| **1.0.0** | 1.21.10   | ✅ Released | Initial stable release |
| 0.5.0     | 1.21.1    | ✅ Released | Beta test release      |

_See [CHANGELOG.md](CHANGELOG.md) for detailed version history._

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

// Data generation imports
import dk.mosberg.api.data.provider.*;

// Event imports
import dk.mosberg.api.event.*;

// Config imports
import dk.mosberg.api.config.*;
```

### Annotation Reference

```java
@Nullable           // This value can be null
@NotNull            // This value must not be null
@Override           // Method overrides parent
@FunctionalInterface // Interface has single method
@Deprecated(since="1.0.0") // No longer recommended
```

---

## 🚀 Next Steps

1. **Explore Examples** - Check out the [examples](examples/) directory
2. **Read the Wiki** - Detailed guides on every component
3. **Ask Questions** - Use [Discussions](https://github.com/Mosberg/mosbergapi/discussions) for help
4. **Contribute** - Submit PRs for improvements
5. **Share Your Mods** - Built something cool? Show us!

---

**MosbergAPI** - Accelerating Minecraft mod development, one helper at a time.

_Made with ❤️ by Mosberg for the Minecraft modding community_
