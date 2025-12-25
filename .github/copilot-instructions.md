# MosbergAPI Copilot Instructions

## Overview

MosbergAPI is a Fabric API library for Minecraft 1.21.10+ mod development. This guide provides clear, actionable steps for using and contributing to MosbergAPI.

---

# MosbergAPI Copilot Instructions

## Overview

MosbergAPI is a Fabric API library for Minecraft 1.21.10+ mod development. This guide provides clear, actionable steps for using and contributing to MosbergAPI.

---

## Quick Links

- [Getting Started](https://github.com/Mosberg/mosbergapi/wiki/Getting-Started)
- [Registry System](https://github.com/Mosberg/mosbergapi/wiki/Registry-System)
- [Utility Helpers](https://github.com/Mosberg/mosbergapi/wiki/Utility-Helpers)
- [Data Generation](https://github.com/Mosberg/mosbergapi/wiki/Data-Generation)
- [Examples](https://github.com/Mosberg/mosbergapi/wiki/Examples)
- [API Reference](https://github.com/Mosberg/mosbergapi/wiki/API-Reference)

---

## Installation

### For Mod Developers

1. Add to your `build.gradle`:
   ```gradle
   repositories {
       maven { name = "Mosberg"; url = "https://maven.moddingx.org" }
   }
   dependencies {
       modImplementation "dk.mosberg:mosbergapi:1.0.0"
       include "dk.mosberg:mosbergapi:1.0.0" // Optional: bundle with your mod
   }
   ```
2. Add to `fabric.mod.json`:
   ```json
   {
     "depends": { "mosbergapi": ">=1.0.0" }
   }
   ```

### For Players

1. Install [Fabric Loader](https://fabricmc.net/use/) 0.18.3+
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) 0.138.4+
3. Download MosbergAPI from [Releases](https://github.com/Mosberg/mosbergapi/releases)
4. Place all JARs in `.minecraft/mods/`

---

## Quickstart Example

```java
import dk.mosberg.api.registry.MosbergBlocks;
import dk.mosberg.api.registry.MosbergItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.Item;

public class MyMod implements ModInitializer {
    public static final Block EXAMPLE_BLOCK = MosbergBlocks.register(
        "example_block", new Block(AbstractBlock.Settings.create().strength(3.0f))
    );
    public static final Item EXAMPLE_ITEM = MosbergItems.register(
        "example_item", new Item(new Item.Settings())
    );
    @Override
    public void onInitialize() {
        // Registration is automatic
    }
}
```

---

## Registry System

All registry classes are static and auto-registering:

- `MosbergBlocks` — Blocks (auto BlockItem)
- `MosbergItems` — Items
- `MosbergEntities` — EntityTypes (builder pattern)
- `MosbergSounds` — Sound events
- `MosbergParticles` — Particle types
- `MosbergItemGroups` — Creative tabs
- `MosbergRegistries` — Low-level registry access

**Example:**

```java
public static final Block CUSTOM_BLOCK = MosbergBlocks.register(
    "custom_block", new Block(AbstractBlock.Settings.create().strength(3.0f).requiresTool())
);
```

---

## Utility Helpers

Helpers are in `dk.mosberg.api.util` and accessible via `MosbergHelper`:

```java
MosbergHelper.BLOCK.setBlockState(world, pos, state);
MosbergHelper.ENTITY.teleport(entity, destination);
MosbergHelper.ITEM.createStack(Items.DIAMOND, 64);
```

See the [README.md](https://raw.githubusercontent.com/Mosberg/mosbergapi/main/README.md) for the full helper class/method list.

---

## Data Generation

To generate recipes, loot tables, or models:

1. Extend `MosbergRecipeProvider`, `MosbergLootTableProvider`, or `MosbergModelProvider`.
2. Register your providers in the data generator entrypoint.
3. Run:
   ```
   ./gradlew runDatagen
   ```

---

## Common Examples

- Register a block: `MosbergBlocks.register("my_block", new Block(...))`
- Add a recipe: extend `MosbergRecipeProvider` and implement `generate()`
- Use helpers: `EntityHelper.teleport(entity, pos)`, `InventoryHelper.addItemToInventory(inv, stack)`

---

## FAQ

**Q: Is MosbergAPI required for players?**
A: Only if using mods that depend on it. It's a library mod.

**Q: Does MosbergAPI add content?**
A: No, it provides APIs only.

**Q: How do I bundle MosbergAPI with my mod?**
A: Use `include` in your `build.gradle` dependencies.

**Q: Do I need to call `initialize()`?**
A: No, registration is automatic on static field load.

**Q: Can I use MosbergAPI with other mod loaders?**
A: No, Fabric/Quilt only.

**Q: How do I update MosbergAPI?**
A: Change the version in `build.gradle` and run `./gradlew build --refresh-dependencies`.

**Q: Blocks not registering?**
A: Ensure registry classes are loaded in your mod initializer:

```java
@Override
public void onInitialize() {
    ModBlocks.class.getName(); // Force class load
}
```

**Q: Data generation not working?**
A: Add the data generator entrypoint in `fabric.mod.json`.

**Q: "Block id not set" error?**
A: Register blocks immediately when creating them, not as static finals before registration.

---

## Contributing

See [CONTRIBUTING.md](https://raw.githubusercontent.com/Mosberg/mosbergapi/main/CONTRIBUTING.md) for code style, PR process, and testing guidelines.

## Changelog

See [CHANGELOG.md](https://raw.githubusercontent.com/Mosberg/mosbergapi/main/CHANGELOG.md) for version history.

## License

MosbergAPI is MIT licensed. See [LICENSE](https://github.com/Mosberg/mosbergapi/blob/main/LICENSE).
