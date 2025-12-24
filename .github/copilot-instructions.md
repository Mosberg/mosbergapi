# Copilot Instructions for MosbergAPI (Remotely Indexed)

> **Remote Index:** https://github.com/Mosberg/mosbergapi

## Home

### Welcome to Mosberg API Documentation

Mosberg API is a comprehensive Fabric API library for Minecraft 1.21.10+ mod development. This guide provides actionable, code-focused documentation for all features, utilities, and systems.

**Quick Links:**

- [Getting Started](https://github.com/Mosberg/mosbergapi/wiki/Getting-Started)
- [Registry System](https://github.com/Mosberg/mosbergapi/wiki/Registry-System)
- [Utility Helpers](https://github.com/Mosberg/mosbergapi/wiki/Utility-Helpers)
- [Data Generation](https://github.com/Mosberg/mosbergapi/wiki/Data-Generation)
- [Examples](https://github.com/Mosberg/mosbergapi/wiki/Examples)
- [API Reference](https://github.com/Mosberg/mosbergapi/wiki/API-Reference)

---

## Getting Started

### Installation

#### For Mod Developers

Add Mosberg API to your `build.gradle`:

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

#### For Players

1. Install [Fabric Loader](https://fabricmc.net/use/) 0.18.3+
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) 0.138.4+
3. Download Mosberg API from [Releases](https://github.com/Mosberg/mosbergapi/releases)
4. Place all JARs in `.minecraft/mods/`

### First Mod with Mosberg API

```java
package com.example.mymod;

import dk.mosberg.api.registry.MosbergBlocks;
import dk.mosberg.api.registry.MosbergItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.Item;

public class MyMod implements ModInitializer {
	public static final String MOD_ID = "mymod";

	// Register blocks
	public static final Block EXAMPLE_BLOCK = MosbergBlocks.register(
		"example_block",
		new Block(AbstractBlock.Settings.create().strength(3.0f))
	);

	// Register items
	public static final Item EXAMPLE_ITEM = MosbergItems.register(
		"example_item",
		new Item(new Item.Settings())
	);

	@Override
	public void onInitialize() {
		// Blocks and items are automatically registered!
	}
}
```

---

## Registry System

See [README.md](../README.md) and [Wiki](https://github.com/Mosberg/mosbergapi/wiki) for full details. Key registry classes:

- `MosbergBlocks` — Block registration (auto BlockItem)
- `MosbergItems` — Item registration
- `MosbergEntities` — EntityType registration (builder pattern)
- `MosbergSounds` — Sound event registration
- `MosbergParticles` — Particle type registration
- `MosbergItemGroups` — Creative tab registration
- `MosbergRegistries` — Low-level registry access

All registration is static and automatic. Example:

```java
public static final Block CUSTOM_BLOCK = MosbergBlocks.register(
	"custom_block",
	new Block(AbstractBlock.Settings.create().strength(3.0f).requiresTool())
);
```

## Utility Helpers

All helpers are in `dk.mosberg.api.util` and accessible via `MosbergHelper`. Examples:

```java
MosbergHelper.BLOCK.setBlockState(world, pos, state);
MosbergHelper.ENTITY.teleport(entity, destination);
MosbergHelper.ITEM.createStack(Items.DIAMOND, 64);
```

See [README.md](../README.md) for full helper class and method list.

## Data Generation

Extend `MosbergRecipeProvider`, `MosbergLootTableProvider`, or `MosbergModelProvider` for recipes, loot tables, and models. Register providers in your data generator entrypoint. Run with `./gradlew runDatagen`.

## Examples

- Register a block: `MosbergBlocks.register("my_block", new Block(...))`
- Add a recipe: extend `MosbergRecipeProvider` and implement `generate()`
- Use helpers: `EntityHelper.teleport(entity, pos)`, `InventoryHelper.addItemToInventory(inv, stack)`

## API Reference

See [Wiki](https://github.com/Mosberg/mosbergapi/wiki) and [README.md](https://github.com/Mosberg/mosbergapi/blob/main/README.md) for detailed API docs and class lists.

## FAQ

**Q: Is Mosberg API required for players?**
A: Only if you're using mods that depend on it. Mosberg API is a library mod.

**Q: Does Mosberg API add content to the game?**
A: No, it's purely a development library. It doesn't add blocks, items, or entities on its own.

**Q: How do I include Mosberg API with my mod?**
A: Use `include` in your build.gradle:

```gradle
dependencies {
	modImplementation "dk.mosberg:mosbergapi:1.0.0"
	include "dk.mosberg:mosbergapi:1.0.0"
}
```

**Q: Do I need to call `initialize()` methods?**
A: No! Mosberg API automatically registers everything when your static fields are loaded.

**Q: Can I use Mosberg API with other mod loaders?**
A: No, it's specifically designed for Fabric/Quilt.

**Q: How do I update to a new version?**
A: Change the version number in build.gradle and run `./gradlew build --refresh-dependencies`.

**Q: My blocks aren't registering**
A: Make sure your registry classes are loaded during mod initialization:

```java
@Override
public void onInitialize() {
	ModBlocks.class.getName(); // Force class load
}
```

**Q: Data generation isn't working**
A: Check that you've added the data generator entrypoint in fabric.mod.json.

**Q: Getting "Block id not set" error**
A: You must register blocks immediately when creating them, not store as static final fields before registration.

## Contributing

See the [Contributing Guide](https://github.com/Mosberg/mosbergapi/blob/main/CONTRIBUTING.md) for code style, PR process, and testing requirements.

## Changelog

See [CHANGELOG.md](https://github.com/Mosberg/mosbergapi/blob/main/CHANGELOG.md) for version history.

## License

Mosberg API is licensed under the MIT License. See [LICENSE](https://github.com/Mosberg/mosbergapi/blob/main/LICENSE).
