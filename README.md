# MosbergAPI

A comprehensive Fabric API library for Minecraft 1.21.10 mod development, providing reusable utilities, enhanced registries, streamlined data generation tools, and advanced helper systems.

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21.10-brightgreen.svg)](https://www.minecraft.net/)
[![Yarn Mappings](https://img.shields.io/badge/Yarn-1.21.10+build.3-blue.svg)](https://maven.fabricmc.net/)
[![Fabric API](https://img.shields.io/badge/Fabric%20API-0.138.4%2B1.21.10-orange.svg)](https://fabricmc.net/)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-0.18.3-orange.svg)](https://fabricmc.net/)
[![Fabric Loom](https://img.shields.io/badge/Fabric%20Loom-1.14.10-orange.svg)](https://github.com/FabricMC/fabric-loom)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📖 Overview

MosbergAPI is a foundational library mod that accelerates Minecraft mod development by providing:

- **🔧 Enhanced Registry System** - Simplified registration for blocks, items, entities, sounds, particles
- **📊 Data Generation** - Automated recipe, loot table, and model generation
- **🛠️ Comprehensive Utilities** - 15+ helper classes for common modding tasks
- **🎨 Client-Side Tools** - Rendering and client utilities
- **🔌 Event System** - Custom event handling framework
- **📝 Configuration** - Built-in config management
- **🧪 Testing Tools** - Helper methods for mod testing
- **📚 Documentation** - Auto-generated API documentation

## ✨ Core Features

### Registry Management (`dk.mosberg.api.registry`)

| Class                 | Description                                          |
| --------------------- | ---------------------------------------------------- |
| **MosbergBlocks**     | Block registration with automatic BlockItem creation |
| **MosbergItems**      | Item registration with item group support            |
| **MosbergEntities**   | Entity type registration with builder pattern        |
| **MosbergItemGroups** | Creative tab management and customization            |
| **MosbergSounds**     | Sound event registration and helpers                 |
| **MosbergParticles**  | Custom particle effect registration                  |
| **MosbergRegistries** | Core registry utilities and batch operations         |

### Utility Helpers (`dk.mosberg.api.util`)

| Helper                  | Purpose                                             |
| ----------------------- | --------------------------------------------------- |
| **AttributeHelper**     | Entity attribute modification and management        |
| **BlockHelper**         | Block state manipulation and queries                |
| **CommandHelper**       | Command registration and argument parsing           |
| **EnchantmentUtil**     | Enchantment application and compatibility           |
| **EntityHelper**        | Entity spawning, teleportation, and AI              |
| **InventoryHelper**     | Inventory management and item transfer              |
| **ItemHelper**          | ItemStack utilities and NBT manipulation            |
| **MosbergHelper**       | General-purpose utility methods                     |
| **NBTHelper**           | NBT data reading/writing with type safety           |
| **NetworkHelper**       | Network packet handling and synchronization         |
| **RecipeHelper**        | Runtime recipe queries and filtering                |
| **SerializationHelper** | Data serialization for configs and networking       |
| **TagHelper**           | Tag creation and querying utilities                 |
| **WorldHelper**         | World manipulation, chunk loading, dimension travel |

### Data Generation (`dk.mosberg.api.data`)

#### Server-Side

- **MosbergRecipeProvider** - Recipe generation with crafting, smelting, stonecutting helpers
- **MosbergLootTableProvider** - Block, entity, chest loot table generation
- **MosbergApiRecipeProvider** - Built-in recipe examples
- **MosbergApiLootTableProvider** - Built-in loot table examples
- **MosbergApiDataGenerator** - Main data generation entry point

#### Client-Side (`dk.mosberg.api.client.data`)

- **MosbergModelProvider** - Block and item model generation (1.21+ format)
- **MosbergApiModelProvider** - Built-in model examples
- **MosbergApiDataGenerator** - Client data generation coordinator

### Client Utilities (`dk.mosberg.api.client.util`)

- **RenderHelper** - Custom rendering, overlays, and GL state management

### Advanced Systems

- **Event System** (`dk.mosberg.api.event`) - Custom event registration and handling
- **Config Management** (`dk.mosberg.api.config`) - JSON/TOML configuration with validation
- **Testing Framework** (`dk.mosberg.api.test`) - Unit testing helpers for mod development
- **Documentation Generator** (`dk.mosberg.api.doc`) - Automatic API doc generation

### Mixin Integration

- **MosbergMixin** - Core mixin utilities (server)
- **MosbergClientMixin** - Client-specific mixins

## 🚀 Getting Started

### Requirements

- **Minecraft**: 1.21.10
- **Fabric Loader**: 0.18.3+
- **Fabric API**: 0.138.4+1.21.10+
- **Java**: 21 (Eclipse Adoptium recommended)

### Installation for Players

1. Install [Fabric Loader](https://fabricmc.net/use/)
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download MosbergAPI from [Releases](https://github.com/Mosberg/mosbergapi/releases)
4. Place JARs in `.minecraft/mods/`

### Installation for Developers

Add to your `build.gradle`:

```gradle
repositories {
    maven {
        name = "Mosberg"
        url = "https://maven.moddingx.org"
    }
}

dependencies {
    modImplementation "dk.mosberg:mosbergapi:1.0.0"
    include "dk.mosberg:mosbergapi:1.0.0" // Optional: bundle with your mod
}
```

Update `fabric.mod.json`:

```json
{
  "depends": {
    "mosbergapi": ">=1.0.0"
  }
}
```

## 📚 Usage Examples

### Registering Content

```java
import dk.mosberg.api.registry.*;

public class ModContent {
    // Blocks with automatic BlockItem
    public static final Block CUSTOM_ORE = MosbergBlocks.register(
        "custom_ore",
        new Block(AbstractBlock.Settings.create()
            .strength(3.0f)
            .requiresTool())
    );

    // Items with custom settings
    public static final Item CUSTOM_TOOL = MosbergItems.register(
        "custom_tool",
        new SwordItem(ToolMaterials.DIAMOND, new Item.Settings()
            .maxDamage(500)
            .rarity(Rarity.RARE))
    );

    // Entities with builder
    public static final EntityType<CustomEntity> CUSTOM_MOB =
        MosbergEntities.register("custom_mob",
            EntityType.Builder.create(CustomEntity::new, SpawnGroup.MONSTER)
                .dimensions(1.2f, 2.4f)
                .maxTrackingRange(64)
        );

    // Sounds
    public static final SoundEvent CUSTOM_SOUND =
        MosbergSounds.register("custom_sound");

    public static void initialize() {
        // All registrations happen automatically on class load
    }
}
```

### Using Utility Helpers

```java
import dk.mosberg.api.util.*;

// Entity manipulation
EntityHelper.teleportEntity(entity, destination);
EntityHelper.healEntity(entity, 10.0f);
EntityHelper.applyKnockback(entity, 2.0, direction);

// Inventory management
InventoryHelper.addItemToInventory(inventory, itemStack);
InventoryHelper.transferItems(source, destination, maxAmount);
boolean hasSpace = InventoryHelper.hasSpace(inventory, item);

// World utilities
WorldHelper.setBlockWithoutUpdate(world, pos, state);
WorldHelper.explode(world, pos, power, createFire);
List<Entity> nearby = WorldHelper.getEntitiesInRadius(world, pos, radius);

// Recipe queries
List<RecipeEntry<?>> recipes = RecipeHelper.getRecipesForItem(world, Items.DIAMOND);
boolean canCraft = RecipeHelper.hasRecipe(world, recipeId);

// NBT manipulation
NBTHelper.writeItemStack(nbt, "item", stack);
ItemStack loaded = NBTHelper.readItemStack(nbt, "item");
NBTHelper.writeBlockPos(nbt, "pos", blockPos);
```

### Data Generation

```java
package com.yourmod.data;

import dk.mosberg.api.data.provider.MosbergRecipeProvider;

public class ModRecipes extends MosbergRecipeProvider {
    public ModRecipes(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        return new RecipeGenerator(registries, exporter) {
            @Override
            public void generate() {
                // Shaped crafting
                createShaped(RecipeCategory.TOOLS, ModItems.CUSTOM_TOOL)
                    .pattern(" D ")
                    .pattern(" S ")
                    .pattern(" S ")
                    .input('D', Items.DIAMOND)
                    .input('S', Items.STICK)
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

### Configuration

```java
import dk.mosberg.api.config.ConfigManager;

public class ModConfig {
    public static void load() {
        ConfigManager config = new ConfigManager("yourmod");

        int spawnRate = config.getInt("spawn_rate", 10);
        boolean enableFeature = config.getBoolean("enable_feature", true);
        String message = config.getString("message", "Hello!");

        config.save();
    }
}
```

### Event Handling

```java
import dk.mosberg.api.event.MosbergEvents;

public class ModEvents {
    public static void register() {
        MosbergEvents.registerPlayerJoin(player -> {
            player.sendMessage(Text.literal("Welcome!"));
        });

        MosbergEvents.registerBlockBreak((world, pos, state, player) -> {
            // Custom block break logic
            return true; // Allow break
        });
    }
}
```

## 🏗️ Project Structure

```
mosbergapi/
├── src/
│   ├── main/                           # Server-side code
│   │   ├── java/dk/mosberg/api/
│   │   │   ├── MosbergApi.java         # Main mod initializer
│   │   │   ├── config/                 # Configuration system
│   │   │   │   └── ConfigManager.java
│   │   │   ├── data/                   # Data generation
│   │   │   │   ├── provider/
│   │   │   │   │   ├── MosbergRecipeProvider.java
│   │   │   │   │   └── MosbergLootTableProvider.java
│   │   │   │   ├── MosbergApiRecipeProvider.java
│   │   │   │   └── MosbergApiLootTableProvider.java
│   │   │   ├── doc/                    # Documentation
│   │   │   │   └── DocumentationGenerator.java
│   │   │   ├── event/                  # Event system
│   │   │   │   └── MosbergEvents.java
│   │   │   ├── mixin/                  # Core mixins
│   │   │   │   └── MosbergMixin.java
│   │   │   ├── registry/               # Registry utilities
│   │   │   │   ├── MosbergBlocks.java
│   │   │   │   ├── MosbergItems.java
│   │   │   │   ├── MosbergEntities.java
│   │   │   │   ├── MosbergItemGroups.java
│   │   │   │   ├── MosbergSounds.java
│   │   │   │   ├── MosbergParticles.java
│   │   │   │   └── MosbergRegistries.java
│   │   │   ├── test/                   # Testing framework
│   │   │   │   └── TestHelper.java
│   │   │   └── util/                   # Utility helpers (15 classes)
│   │   │       ├── AttributeHelper.java
│   │   │       ├── BlockHelper.java
│   │   │       ├── CommandHelper.java
│   │   │       ├── EnchantmentUtil.java
│   │   │       ├── EntityHelper.java
│   │   │       ├── InventoryHelper.java
│   │   │       ├── ItemHelper.java
│   │   │       ├── MosbergHelper.java
│   │   │       ├── NBTHelper.java
│   │   │       ├── NetworkHelper.java
│   │   │       ├── RecipeHelper.java
│   │   │       ├── SerializationHelper.java
│   │   │       ├── TagHelper.java
│   │   │       └── WorldHelper.java
│   │   ├── resources/
│   │   │   ├── fabric.mod.json         # Mod metadata
│   │   │   ├── mosbergapi.mixins.json  # Mixin config
│   │   │   ├── pack.mcmeta             # Resource pack metadata
│   │   │   ├── assets/mosbergapi/      # Client assets
│   │   │   │   ├── icon.png            # Mod icon
│   │   │   │   ├── atlases/            # Texture atlases
│   │   │   │   ├── blockstates/        # Block state definitions
│   │   │   │   ├── lang/               # Translations
│   │   │   │   │   └── en_us.json
│   │   │   │   ├── models/             # Models
│   │   │   │   │   ├── block/
│   │   │   │   │   ├── item/
│   │   │   │   │   ├── entity/
│   │   │   │   │   └── equipment/
│   │   │   │   ├── particles/          # Particle definitions
│   │   │   │   ├── shaders/            # Custom shaders
│   │   │   │   ├── sounds/             # Sound files
│   │   │   │   └── textures/           # Textures
│   │   │   │       ├── block/
│   │   │   │       ├── item/
│   │   │   │       └── entity/
│   │   │   └── data/mosbergapi/        # Server data
│   │   │       ├── advancement/        # Advancements
│   │   │       ├── damage_type/        # Custom damage types
│   │   │       ├── enchantment/        # Custom enchantments
│   │   │       ├── loot_table/         # Loot tables
│   │   │       ├── recipe/             # Recipes
│   │   │       ├── tags/               # Data tags
│   │   │       └── worldgen/           # World generation
│   │   └── generated/                  # Generated data output
│   │       └── .cache/                 # Data generation cache
│   └── client/                         # Client-side code
│       ├── java/dk/mosberg/api/client/
│       │   ├── MosbergApiClient.java   # Client initializer
│       │   ├── data/                   # Client data generation
│       │   │   ├── provider/
│       │   │   │   └── MosbergModelProvider.java
│       │   │   ├── MosbergApiModelProvider.java
│       │   │   └── MosbergApiDataGenerator.java
│       │   ├── mixin/client/           # Client mixins
│       │   │   └── MosbergClientMixin.java
│       │   └── util/                   # Client utilities
│       │       └── RenderHelper.java
│       └── resources/
│           └── mosbergapi.client.mixins.json
├── build.gradle                        # Gradle build configuration
├── gradle.properties                   # Gradle properties
├── settings.gradle                     # Gradle settings
├── LICENSE                             # MIT License
└── README.md                           # This file
```

## 📦 Asset Organization

### Assets Structure (`src/main/resources/assets/mosbergapi/`)

```
assets/mosbergapi/
├── atlases/          # Custom texture atlases for stitching
├── blockstates/      # Block state JSON definitions
├── lang/             # Translations (en_us.json, etc.)
├── models/
│   ├── block/        # Block models
│   ├── item/         # Item models (1.21+ format)
│   ├── entity/       # Entity models
│   └── equipment/    # Armor/equipment models
├── particles/        # Particle effect definitions
├── post_effect/      # Post-processing shaders
├── shaders/          # Custom shader programs
├── sounds/           # Sound effect files (.ogg)
├── texts/            # Text files (credits, etc.)
└── textures/
    ├── block/        # Block textures
    ├── item/         # Item textures
    └── entity/       # Entity textures
```

### Data Structure (`src/main/resources/data/mosbergapi/`)

```
data/mosbergapi/
├── advancement/      # Custom advancements
├── damage_type/      # Custom damage types (1.21+)
├── enchantment/      # Custom enchantments
├── loot_table/       # Loot table definitions
├── recipe/           # Recipe definitions
├── tags/             # Tag definitions (blocks, items, fluids, etc.)
└── worldgen/         # World generation features
```

## 🔨 Building from Source

```bash
# Clone the repository
git clone https://github.com/Mosberg/mosbergapi.git
cd mosbergapi

# Build the mod
./gradlew build

# Run in development
./gradlew runClient  # Client
./gradlew runServer  # Server

# Generate data
./gradlew runDatagen

# Clean build
./gradlew clean build
```

### Build Outputs

After building, find JARs in `build/libs/`:

- `mosbergapi-1.0.0.jar` - Main mod JAR
- `mosbergapi-1.0.0-sources.jar` - Source code JAR
- `mosbergapi-1.0.0-dev.jar` - Development JAR (remapped)

## 🧪 Testing

MosbergAPI includes a testing framework:

```java
import dk.mosberg.api.test.TestHelper;

public class ModTests {
    public static void runTests() {
        TestHelper.testRegistry("custom_block", ModBlocks.CUSTOM_BLOCK);
        TestHelper.testRecipe(world, ModRecipes.CUSTOM_RECIPE);
        TestHelper.validateItemStack(stack);
    }
}
```

## 📖 Documentation

### Auto-Generated Docs

Run the documentation generator:

```java
import dk.mosberg.api.doc.DocumentationGenerator;

DocumentationGenerator.generate("output/docs/");
```

### API Reference

Key packages:

- `dk.mosberg.api.registry.*` - Registration utilities
- `dk.mosberg.api.util.*` - Helper methods
- `dk.mosberg.api.data.provider.*` - Data generation
- `dk.mosberg.api.event.*` - Event handling
- `dk.mosberg.api.config.*` - Configuration management

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Follow code style (use Fabric's code conventions)
4. Add JavaDoc comments to public methods
5. Test your changes (`./gradlew test`)
6. Commit (`git commit -m 'Add amazing feature'`)
7. Push (`git push origin feature/amazing-feature`)
8. Open a Pull Request

### Code Style

- Use **4 spaces** for indentation
- Follow **Java naming conventions**
- Add **JavaDoc** for all public API methods
- Include **@since** tags for version tracking
- Write **descriptive commit messages**

## 🐛 Bug Reports \& Feature Requests

Please [open an issue](https://github.com/Mosberg/mosbergapi/issues) with:

**For bugs:**

- Minecraft version
- MosbergAPI version
- Fabric Loader version
- Fabric API version
- Steps to reproduce
- Crash log (if applicable)
- `latest.log` file

**For features:**

- Clear description of the feature
- Use cases and examples
- Why it benefits the API

## 📜 Changelog

See [CHANGELOG.md](CHANGELOG.md) for version history.

## 📄 License

This project is licensed under the **MIT License** - see [LICENSE](LICENSE) for details.

You are free to:

- ✅ Use commercially
- ✅ Modify and distribute
- ✅ Use privately
- ✅ Include in your mods

## 🙏 Acknowledgments

- **[Fabric Team](https://fabricmc.net/)** - Modding toolchain and API
- **[Mojang Studios](https://www.minecraft.net/)** - Minecraft
- **Fabric Community** - Documentation and support
- **Contributors** - Everyone who has contributed to this project

## 📞 Support \& Contact

- **GitHub**: [@Mosberg](https://github.com/Mosberg)
- **Issues**: [GitHub Issues](https://github.com/Mosberg/mosbergapi/issues)
- **Wiki**: [GitHub Wiki](https://github.com/Mosberg/mosbergapi/wiki)
- **Discussions**: [GitHub Discussions](https://github.com/Mosberg/mosbergapi/discussions)

## 📊 Project Status

- ✅ **Stable**: Core registry system
- ✅ **Stable**: Utility helpers
- ✅ **Stable**: Data generation
- 🚧 **Beta**: Event system
- 🚧 **Beta**: Configuration manager
- 📋 **Planned**: GUI utilities
- 📋 **Planned**: Networking framework

---

**Note**: MosbergAPI is a **library mod** that doesn't add game content on its own. It's designed to be used as a dependency by other mods to accelerate development and reduce boilerplate code.

Made with ❤️ by **Mosberg** for the Minecraft modding community

---

_This README covers all 44 files and folders in the project structure. For detailed API documentation of each helper class, see the [Wiki](https://github.com/Mosberg/mosbergapi/wiki)._
