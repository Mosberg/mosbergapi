# MosbergAPI

A comprehensive Fabric API library for Minecraft 1.21.10 mod development, providing reusable utilities, enhanced registries, and streamlined data generation tools.

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.10-brightgreen.svg)](https://mappings.dev/1.21.10/index.html)
[![Yarn Mappings](https://img.shields.io/badge/Yarn-1.21.10+build.3-blue.svg)](https://maven.fabricmc.net/docs/yarn-1.21.10+build.3/)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-0.138.4%2B1.21.10-orange.svg)](https://maven.fabricmc.net/docs/fabric-api-0.138.3+1.21.10/)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-0.18.3-orange.svg)](https://maven.fabricmc.net/docs/fabric-loader-0.18.3/)
[![Fabric Loom](https://img.shields.io/badge/Fabric%20Loom-1.14.10-orange.svg)](https://maven.fabricmc.net/fabric-loom/fabric-loom.gradle.plugin/1.14.10/)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📖 Overview

MosbergAPI is a foundational library mod designed to simplify Minecraft mod development with Fabric. It provides:

- **Enhanced Registry System** - Simplified registration for blocks, items, entities, sounds, and particles
- **Data Generation Utilities** - Helper classes for recipes, loot tables, and model providers
- **Recipe Utilities** - Advanced recipe helper methods and management tools
- **Component System** - Modern data component support for custom item data
- **Reusable Utilities** - Common helper methods for mod development

## ✨ Features

### Registry Management

- **MosbergBlocks** - Block registration with automatic item blocks
- **MosbergItems** - Item registration with item group support
- **MosbergEntities** - Entity type registration with builder support
- **MosbergItemGroups** - Creative tab management
- **MosbergSounds** - Sound event registration
- **MosbergParticles** - Custom particle registration

### Data Generation

- **MosbergRecipeProvider** - Recipe generation with helper methods
- **MosbergLootTableProvider** - Loot table generation utilities
- **MosbergModelProvider** - Block and item model generation (client-side)

### Utilities

- **RecipeHelper** - Query and filter recipes at runtime
- **Component Registration** - Type-safe data component management
- **Enhanced Registries** - Simplified registration with automatic namespacing

## 🚀 Getting Started

### Requirements

- **Minecraft**: 1.21.10
- **Fabric Loader**: 0.16.10 or higher
- **Fabric API**: 0.111.1+1.21.10 or higher
- **Java**: 21

### Installation

#### For Mod Developers (Dependency)

Add MosbergAPI as a dependency in your `build.gradle`:

```

repositories {
maven {
name = "Moddingx"
url = "https://maven.moddingx.org"
}
}

dependencies {
modImplementation "dk.mosberg:mosbergapi:1.0.0"
}

```

Update your `fabric.mod.json`:

```

{
"depends": {
"mosbergapi": ">=1.0.0"
}
}

```

#### For Players

1. Download and install [Fabric Loader](https://fabricmc.net/use/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download MosbergAPI from [releases](https://github.com/Mosberg/mosbergapi/releases)
4. Place all JARs in your `.minecraft/mods` folder

## 📚 Usage Examples

### Registry System

#### Registering Blocks

```

import dk.mosberg.api.registry.MosbergBlocks;
import net.minecraft.block.Block;

public class MyBlocks {
public static final Block CUSTOM_BLOCK = MosbergBlocks.register(
"custom_block",
new Block(AbstractBlock.Settings.create()
.strength(3.0f)
.requiresTool())
);

    public static void initialize() {
        // Blocks are automatically registered
    }
    }

```

#### Registering Items

```

import dk.mosberg.api.registry.MosbergItems;
import net.minecraft.item.Item;

public class MyItems {
public static final Item CUSTOM_ITEM = MosbergItems.register(
"custom_item",
new Item(new Item.Settings())
);

    public static void initialize() {
        // Items are automatically registered
    }
    }

```

#### Registering Entities

```

import dk.mosberg.api.registry.MosbergEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class MyEntities {
public static final EntityType<CustomEntity> CUSTOM_ENTITY =
MosbergEntities.register("custom_entity",
EntityType.Builder.create(CustomEntity::new, SpawnGroup.CREATURE)
.dimensions(0.6f, 1.8f)
.maxTrackingRange(8)
);

    public static void initialize() {
        // Entities are automatically registered
    }
    }

```

### Data Generation

#### Recipe Provider

```

package com.yourmod.data;

import dk.mosberg.api.data.provider.MosbergRecipeProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class MyRecipeProvider extends MosbergRecipeProvider {

    public MyRecipeProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        return new MyRecipeGenerator(registries, exporter);
    }

    private static class MyRecipeGenerator extends RecipeGenerator {
        public MyRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
            super(registries, exporter);
        }

        @Override
        public void generate() {
            // Shaped recipe
            createShaped(RecipeCategory.MISC, MyItems.CUSTOM_ITEM)
                .pattern("III")
                .pattern("IGI")
                .pattern("III")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GOLD_INGOT)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

            // Smelting recipe
            offerSmelting(exporter,
                List.of(MyItems.RAW_CUSTOM),
                RecipeCategory.MISC,
                MyItems.CUSTOM_INGOT,
                0.7f, 200, "custom_ingot"
            );
        }
    }
    }

```

#### Loot Table Provider

```

package com.yourmod.data;

import dk.mosberg.api.data.provider.MosbergLootTableProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;

public class MyLootTableProvider extends MosbergLootTableProvider {

    public MyLootTableProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, Set.of(), createGenerators(), registries);
    }

    private static List<LootTypeGenerator> createGenerators() {
        return List.of(
            new LootTypeGenerator(
                () -> LootContextTypes.BLOCK,
                MyLootTableProvider::generateBlockLoot
            )
        );
    }

    @Override
    protected List<LootTypeGenerator> createLootTypeGenerators() {
        return createGenerators();
    }

    private static void generateBlockLoot(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> consumer) {
        // Simple block drop
        consumer.accept(
            RegistryKey.of(RegistryKeys.LOOT_TABLE,
                Identifier.of("yourmod", "blocks/custom_block")),
            simpleDrop(MyBlocks.CUSTOM_BLOCK)
        );

        // Ore with fortune
        consumer.accept(
            RegistryKey.of(RegistryKeys.LOOT_TABLE,
                Identifier.of("yourmod", "blocks/custom_ore")),
            oreDrop(MyBlocks.CUSTOM_ORE, Items.DIAMOND)
        );
    }
    }

```

### Recipe Helper Utilities

```

import dk.mosberg.api.util.RecipeHelper;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.world.ServerWorld;

// Get all smelting recipes
List<RecipeEntry<SmeltingRecipe>> smeltingRecipes =
RecipeHelper.getRecipesByType(world, RecipeType.SMELTING);

// Find recipes that produce diamonds
List<RecipeEntry<?>> diamondRecipes =
RecipeHelper.getRecipesForItem(world, new ItemStack(Items.DIAMOND));

// Check if a specific recipe exists
boolean exists = RecipeHelper.hasRecipe(world,
Identifier.of("minecraft", "diamond_block"));

```

### Data Components

```

import dk.mosberg.api.registry.MosbergDataComponents;
import net.minecraft.component.ComponentType;

public class MyComponents {
public static final ComponentType<Integer> CUSTOM_DATA =
MosbergDataComponents.register(
"custom_data",
builder -> builder.codec(Codec.INT)
);

    public static void initialize() {
        // Components are automatically registered
    }
    }

// Usage in items
ItemStack stack = new ItemStack(MyItems.CUSTOM_ITEM);
stack.set(MyComponents.CUSTOM_DATA, 42);
int value = stack.getOrDefault(MyComponents.CUSTOM_DATA, 0);

```

## 🏗️ Project Structure

```

mosbergapi/
├── src/
│ ├── main/
│ │ ├── java/dk/mosberg/api/
│ │ │ ├── MosbergApi.java \# Main mod initializer
│ │ │ ├── data/ \# Data generation
│ │ │ │ ├── provider/
│ │ │ │ │ ├── MosbergRecipeProvider.java
│ │ │ │ │ └── MosbergLootTableProvider.java
│ │ │ │ └── MosbergApiDataGenerator.java
│ │ │ ├── registry/ \# Registry utilities
│ │ │ │ ├── MosbergBlocks.java
│ │ │ │ ├── MosbergItems.java
│ │ │ │ ├── MosbergEntities.java
│ │ │ │ ├── MosbergItemGroups.java
│ │ │ │ ├── MosbergSounds.java
│ │ │ │ ├── MosbergParticles.java
│ │ │ │ ├── MosbergDataComponents.java
│ │ │ │ └── MosbergRegistries.java
│ │ │ └── util/ \# Utilities
│ │ │ └── RecipeHelper.java
│ │ └── resources/
│ │ ├── fabric.mod.json
│ │ ├── mosbergapi.mixins.json
│ │ └── assets/mosbergapi/
│ └── client/
│ └── java/dk/mosberg/api/
│ ├── MosbergApiClient.java \# Client initializer
│ └── data/ \# Client-side data gen
│ └── provider/
│ └── MosbergModelProvider.java
├── build.gradle
├── gradle.properties
├── settings.gradle
└── README.md

```

## 🔧 Configuration

MosbergAPI uses standard Fabric configuration. No additional configuration required for basic usage.

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Setup

```

git clone https://github.com/Mosberg/mosbergapi.git
cd mosbergapi
./gradlew build

```

## 📝 API Documentation

Full API documentation is available at [Wiki](https://github.com/Mosberg/mosbergapi/wiki) (coming soon).

Key classes:

- `MosbergRegistries` - Core registration utilities
- `MosbergRecipeProvider` - Recipe data generation
- `MosbergLootTableProvider` - Loot table generation
- `RecipeHelper` - Runtime recipe utilities

## 🐛 Bug Reports

Found a bug? Please [open an issue](https://github.com/Mosberg/mosbergapi/issues) with:

- Minecraft version
- MosbergAPI version
- Fabric Loader version
- Fabric API version
- Steps to reproduce
- Crash log (if applicable)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Fabric](https://fabricmc.net/) - The modding toolchain
- [Minecraft](https://www.minecraft.net/) - The game we all love
- The Fabric community for excellent documentation and support

## 📞 Contact

- **GitHub**: [@Mosberg](https://github.com/Mosberg)
- **Issues**: [GitHub Issues](https://github.com/Mosberg/mosbergapi/issues)

---

**Note**: This is a library mod and does not add any content to the game on its own. It's designed to be used by other mods as a dependency.

Made with ❤️ by Mosberg for the Minecraft modding community
