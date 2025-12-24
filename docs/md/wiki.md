## Home

### Welcome to Mosberg API Documentation

Mosberg API is a comprehensive Fabric API library for Minecraft 1.21.10+ mod development. This wiki provides complete documentation for all features, utilities, and systems.

**Quick Links:**

- [Getting Started](Getting-Started)
- [Registry System](Registry-System)
- [Utility Helpers](Utility-Helpers)
- [Data Generation](Data-Generation)
- [Examples](Examples)
- [API Reference](API-Reference)

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
    modImplementation "dk.mosberg:Mosberg API:1.0.0"
    include "dk.mosberg:Mosberg API:1.0.0" // Optional: bundle with your mod
}
```

Update `fabric.mod.json`:

```json
{
  "depends": {
    "Mosberg API": ">=1.0.0"
  }
}
```

#### For Players

1. Install [Fabric Loader](https://fabricmc.net/use/) 0.18.3+
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) 0.138.4+
3. Download Mosberg API from [Releases](https://github.com/Mosberg/Mosberg API/releases)
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

### Overview

Mosberg API provides simplified registration for all Minecraft content types through dedicated registry classes.

### MosbergBlocks

**Package:** `dk.mosberg.api.registry`

Simplified block registration with automatic BlockItem creation.

#### Methods

```java
// Register block with automatic BlockItem
public static <T extends Block> T register(String name, T block)

// Register block with optional BlockItem
public static <T extends Block> T register(String name, T block, boolean createItem)

// Register block with custom BlockItem settings
public static <T extends Block> T register(String name, T block, Item.Settings itemSettings)
```

#### Examples

```java
// Simple block with auto BlockItem
public static final Block CUSTOM_BLOCK = MosbergBlocks.register(
    "custom_block",
    new Block(AbstractBlock.Settings.create()
        .strength(3.0f)
        .requiresTool())
);

// Block without BlockItem
public static final Block TECHNICAL_BLOCK = MosbergBlocks.register(
    "technical_block",
    new Block(AbstractBlock.Settings.create()),
    false // No BlockItem
);

// Block with custom BlockItem settings
public static final Block RARE_BLOCK = MosbergBlocks.register(
    "rare_block",
    new Block(AbstractBlock.Settings.create()),
    new Item.Settings().rarity(Rarity.EPIC)
);
```

### MosbergItems

**Package:** `dk.mosberg.api.registry`

Simplified item registration with item group support.

#### Methods

```java
// Register an item
public static <T extends Item> T register(String name, T item)
```

#### Examples

```java
// Basic item
public static final Item CUSTOM_ITEM = MosbergItems.register(
    "custom_item",
    new Item(new Item.Settings())
);

// Tool with durability
public static final Item CUSTOM_SWORD = MosbergItems.register(
    "custom_sword",
    new SwordItem(ToolMaterials.DIAMOND,
        new Item.Settings()
            .maxDamage(500)
            .rarity(Rarity.RARE))
);

// Food item
public static final Item CUSTOM_FOOD = MosbergItems.register(
    "custom_food",
    new Item(new Item.Settings()
        .food(new FoodComponent.Builder()
            .hunger(6)
            .saturationModifier(0.6f)
            .build()))
);
```

### MosbergEntities

**Package:** `dk.mosberg.api.registry`

Entity type registration with builder pattern support.

#### Methods

```java
// Register with EntityType.Builder
public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder)

// Register EntityType directly
public static <T extends Entity> EntityType<T> register(String name, EntityType<T> entityType)

// Create registry key
public static RegistryKey<EntityType<?>> keyOf(String name)
```

#### Examples

```java
// Custom mob entity
public static final EntityType<CustomMob> CUSTOM_MOB =
    MosbergEntities.register("custom_mob",
        EntityType.Builder.create(CustomMob::new, SpawnGroup.MONSTER)
            .dimensions(1.2f, 2.4f)
            .maxTrackingRange(64)
            .trackingTickInterval(3)
    );

// Projectile entity
public static final EntityType<CustomArrow> CUSTOM_ARROW =
    MosbergEntities.register("custom_arrow",
        EntityType.Builder.<CustomArrow>create(CustomArrow::new, SpawnGroup.MISC)
            .dimensions(0.5f, 0.5f)
            .maxTrackingRange(4)
            .trackingTickInterval(20)
    );
```

### MosbergSounds

**Package:** `dk.mosberg.api.registry`

Sound event registration utilities.

#### Methods

```java
// Register sound event
public static SoundEvent register(String name)

// Register with specific identifier
public static SoundEvent register(Identifier id)
```

#### Examples

```java
// Custom sound
public static final SoundEvent CUSTOM_SOUND =
    MosbergSounds.register("custom_sound");

// With namespace
public static final SoundEvent AMBIENT_SOUND =
    MosbergSounds.register(Identifier.of("mymod", "ambient.custom"));
```

**Sound definition** (`assets/mymod/sounds.json`):

```json
{
  "custom_sound": {
    "sounds": ["mymod:custom_sound"]
  }
}
```

### MosbergParticles

**Package:** `dk.mosberg.api.registry`

Particle type registration.

#### Methods

```java
// Register particle type
public static <T extends ParticleType<?>> T register(String name, T particleType)

// Register simple particle
public static DefaultParticleType registerSimple(String name, boolean alwaysShow)
```

#### Examples

```java
// Simple particle
public static final DefaultParticleType CUSTOM_PARTICLE =
    MosbergParticles.registerSimple("custom_particle", false);

// Particle with data
public static final ParticleType<BlockStateParticleEffect> CUSTOM_BLOCK_PARTICLE =
    MosbergParticles.register("custom_block_particle",
        new DefaultParticleType(true));
```

### MosbergItemGroups

**Package:** `dk.mosberg.api.registry`

Creative tab registration and management.

#### Methods

```java
// Register item group
public static ItemGroup register(String name, ItemGroup.Builder builder)

// Register with icon
public static ItemGroup register(String name, ItemStack icon, ItemGroup.EntryCollector entries)
```

#### Examples

```java
public static final ItemGroup CUSTOM_GROUP = MosbergItemGroups.register(
    "custom_group",
    FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModItems.CUSTOM_ITEM))
        .displayName(Text.translatable("itemGroup.mymod.custom"))
        .entries((context, entries) -> {
            entries.add(ModItems.CUSTOM_ITEM);
            entries.add(ModBlocks.CUSTOM_BLOCK);
        })
        .build()
);
```

### MosbergRegistries

**Package:** `dk.mosberg.api.registry`

Low-level registry access for all Minecraft registries (80+ types supported).

#### Supported Registries

- **Blocks & Items**: Block, BlockEntityType, Item, ItemGroup
- **Entities**: EntityType, EntityAttribute, Activity, Memory, Sensor
- **Effects**: StatusEffect, Potion
- **World Gen**: Feature, Carver, Structure, Biome modifiers
- **Recipes**: RecipeType, RecipeSerializer
- **Loot**: LootCondition, LootFunction, LootPool
- **And 60+ more...**

See [MosbergRegistries API](MosbergRegistries-API) for complete list.

---

## Utility Helpers

### Overview

Mosberg API provides 15+ utility helper classes for common modding tasks. All helpers are accessible via `MosbergHelper` or directly.

```java
import dk.mosberg.api.util.MosbergHelper;

// Access helpers through MosbergHelper
MosbergHelper.BLOCK.setBlockState(world, pos, state);
MosbergHelper.ENTITY.teleport(entity, destination);
MosbergHelper.ITEM.createStack(Items.DIAMOND, 64);
```

### BlockHelper

**Package:** `dk.mosberg.api.util`

Block manipulation, state management, and block entity operations.

#### Key Methods

```java
// Block state operations
void setBlockState(World world, BlockPos pos, BlockState state)
void setBlockStateWithoutUpdate(World world, BlockPos pos, BlockState state)
BlockState getBlockState(World world, BlockPos pos)
boolean isAir(World world, BlockPos pos)

// Block breaking
void breakBlock(World world, BlockPos pos, boolean dropItems)
void breakBlockSilently(World world, BlockPos pos)

// Block entity operations
<T extends BlockEntity> Optional<T> getBlockEntity(World world, BlockPos pos, Class<T> type)
void updateBlockEntity(World world, BlockPos pos)

// Block properties
boolean requiresTool(BlockState state)
float getHardness(BlockState state)
boolean isReplaceable(BlockState state)

// Neighbor updates
void notifyNeighbors(World world, BlockPos pos)
List<BlockPos> getNeighbors(BlockPos pos)
```

#### Examples

```java
// Set block without triggering updates
BlockHelper.setBlockStateWithoutUpdate(world, pos,
    Blocks.STONE.getDefaultState());

// Get block entity safely
Optional<ChestBlockEntity> chest =
    BlockHelper.getBlockEntity(world, pos, ChestBlockEntity.class);
chest.ifPresent(c -> c.setStack(0, new ItemStack(Items.DIAMOND)));

// Check block properties
if (BlockHelper.requiresTool(state)) {
    // Block requires proper tool
}

// Get all neighboring positions
List<BlockPos> neighbors = BlockHelper.getNeighbors(pos);
```

### EntityHelper

**Package:** `dk.mosberg.api.util`

Entity spawning, teleportation, damage, AI manipulation.

#### Key Methods

```java
// Spawning
<T extends Entity> T spawn(World world, EntityType<T> type, BlockPos pos)
<T extends Entity> T spawnWithRotation(World world, EntityType<T> type, Vec3d pos, float yaw, float pitch)
boolean canSpawn(World world, EntityType<?> type, BlockPos pos)

// Teleportation
void teleport(Entity entity, Vec3d destination)
void teleport(Entity entity, World targetWorld, Vec3d destination)
void teleportToDimension(Entity entity, RegistryKey<World> dimension, BlockPos pos)

// Health & damage
void heal(LivingEntity entity, float amount)
void damage(Entity entity, DamageSource source, float amount)
void kill(Entity entity)
boolean isAlive(Entity entity)

// Movement & velocity
void setVelocity(Entity entity, Vec3d velocity)
void addVelocity(Entity entity, double x, double y, double z)
void applyKnockback(Entity entity, double strength, Vec3d direction)
void stopMovement(Entity entity)

// AI & targeting
void setTarget(MobEntity mob, LivingEntity target)
Optional<LivingEntity> getTarget(MobEntity mob)
void setAggressive(MobEntity mob, boolean aggressive)

// Effects
void addEffect(LivingEntity entity, StatusEffect effect, int duration, int amplifier)
void removeEffect(LivingEntity entity, StatusEffect effect)
void clearEffects(LivingEntity entity)
```

#### Examples

```java
// Spawn zombie at position
Zombie zombie = EntityHelper.spawn(world, EntityType.ZOMBIE, pos);

// Teleport player to nether
EntityHelper.teleportToDimension(player, World.NETHER, new BlockPos(0, 64, 0));

// Heal entity
EntityHelper.heal(player, 10.0f);

// Apply knockback
EntityHelper.applyKnockback(entity, 2.0, Vec3d.of(direction));

// Add potion effect
EntityHelper.addEffect(player, StatusEffects.SPEED, 200, 1);

// Set mob target
EntityHelper.setTarget(zombie, player);
```

### ItemHelper

**Package:** `dk.mosberg.api.util`

ItemStack creation, modification, enchanting, NBT operations.

#### Key Methods

```java
// Stack creation
ItemStack createStack(Item item, int count)
ItemStack createStack(ItemConvertible item)
ItemStack createEnchantedStack(Item item, Enchantment enchantment, int level)

// Stack operations
boolean isEmpty(ItemStack stack)
int getCount(ItemStack stack)
void setCount(ItemStack stack, int count)
void shrink(ItemStack stack, int amount)
void grow(ItemStack stack, int amount)

// Comparison
boolean areEqual(ItemStack stack1, ItemStack stack2)
boolean areItemsEqual(ItemStack stack1, ItemStack stack2)
boolean canCombine(ItemStack stack1, ItemStack stack2)

// Enchanting
void addEnchantment(ItemStack stack, Enchantment enchantment, int level)
int getEnchantmentLevel(ItemStack stack, Enchantment enchantment)
void removeEnchantment(ItemStack stack, Enchantment enchantment)
Map<Enchantment, Integer> getEnchantments(ItemStack stack)

// Damage & durability
void damage(ItemStack stack, int amount, LivingEntity entity)
void setDamage(ItemStack stack, int damage)
int getDamage(ItemStack stack)
int getMaxDamage(ItemStack stack)
boolean isDamageable(ItemStack stack)

// NBT operations
void setNbt(ItemStack stack, NbtCompound nbt)
NbtCompound getNbt(ItemStack stack)
boolean hasNbt(ItemStack stack)

// Custom name & lore
void setCustomName(ItemStack stack, Text name)
Text getCustomName(ItemStack stack)
void setLore(ItemStack stack, List<Text> lore)
```

#### Examples

```java
// Create enchanted sword
ItemStack sword = ItemHelper.createEnchantedStack(
    Items.DIAMOND_SWORD, Enchantments.SHARPNESS, 5);

// Add multiple enchantments
ItemHelper.addEnchantment(sword, Enchantments.UNBREAKING, 3);
ItemHelper.addEnchantment(sword, Enchantments.FIRE_ASPECT, 2);

// Check if stacks can combine
if (ItemHelper.canCombine(stack1, stack2)) {
    ItemHelper.grow(stack1, stack2.getCount());
}

// Set custom name and lore
ItemHelper.setCustomName(stack, Text.literal("Legendary Sword"));
ItemHelper.setLore(stack, List.of(
    Text.literal("A powerful weapon"),
    Text.literal("Forged in dragon fire")
));

// Damage item
ItemHelper.damage(sword, 10, player);
```

### InventoryHelper

**Package:** `dk.mosberg.api.util`

Inventory management, item transfer, slot operations.

#### Key Methods

```java
// Item insertion
boolean addItemToInventory(Inventory inventory, ItemStack stack)
int addItemPartially(Inventory inventory, ItemStack stack)
boolean canInsert(Inventory inventory, ItemStack stack)

// Item removal
ItemStack removeItem(Inventory inventory, Item item, int count)
boolean removeStack(Inventory inventory, ItemStack stack)
void clearInventory(Inventory inventory)

// Item queries
int countItems(Inventory inventory, Item item)
boolean hasItem(Inventory inventory, Item item)
boolean hasSpace(Inventory inventory, ItemStack stack)
int getFirstEmptySlot(Inventory inventory)

// Item transfer
boolean transferItems(Inventory source, Inventory destination, int maxAmount)
boolean transferSlot(Inventory source, int sourceSlot, Inventory destination)

// Inventory operations
List<ItemStack> getAllStacks(Inventory inventory)
void sortInventory(Inventory inventory)
```

#### Examples

```java
// Add item to player inventory
if (InventoryHelper.addItemToInventory(player.getInventory(), reward)) {
    player.sendMessage(Text.literal("Received reward!"));
}

// Transfer items between inventories
InventoryHelper.transferItems(chest, player.getInventory(), 64);

// Count specific items
int diamondCount = InventoryHelper.countItems(inventory, Items.DIAMOND);

// Check if player has required items
if (InventoryHelper.countItems(player.getInventory(), Items.IRON_INGOT) >= 10) {
    // Has enough iron
}

// Clear and give starting items
InventoryHelper.clearInventory(player.getInventory());
InventoryHelper.addItemToInventory(player.getInventory(),
    new ItemStack(Items.STONE_SWORD));
```

### WorldHelper

**Package:** `dk.mosberg.api.util`

World manipulation, time/weather control, explosions, chunk operations.

#### Key Methods

```java
// Time operations
void setTime(World world, long time)
void setDayTime(World world)
void setNightTime(World world)
long getTime(World world)
boolean isDay(World world)
boolean isNight(World world)

// Weather operations
void setWeather(ServerWorld world, boolean raining, boolean thundering, int duration)
void clearWeather(ServerWorld world)
void startRain(ServerWorld world, int duration)
void startThunder(ServerWorld world, int duration)

// Explosions
void explode(World world, Vec3d pos, float power, boolean createFire)
void explode(World world, Entity source, Vec3d pos, float power, Explosion.DestructionType destructionType)

// Entity queries
List<Entity> getEntitiesInRadius(World world, Vec3d center, double radius)
<T extends Entity> List<T> getEntitiesOfType(World world, Class<T> type, Box box)
Entity getNearestEntity(World world, Vec3d pos, double range)
<T extends Entity> T getNearestEntityOfType(World world, Class<T> type, Vec3d pos, double range)

// Block operations
void setBlockWithoutUpdate(World world, BlockPos pos, BlockState state)
void removeBlock(World world, BlockPos pos)
void placeBlock(World world, BlockPos pos, BlockState state, int flags)

// Chunk operations
boolean isChunkLoaded(World world, ChunkPos pos)
void loadChunk(ServerWorld world, ChunkPos pos)
void unloadChunk(ServerWorld world, ChunkPos pos)
```

#### Examples

```java
// Set time to day
WorldHelper.setDayTime(world);

// Start rain for 10 minutes (12000 ticks)
WorldHelper.startRain((ServerWorld) world, 12000);

// Create explosion without fire
WorldHelper.explode(world, pos.toCenterPos(), 4.0f, false);

// Get all players nearby
List<PlayerEntity> nearbyPlayers = WorldHelper.getEntitiesOfType(
    world, PlayerEntity.class,
    Box.of(pos.toCenterPos(), 10, 10, 10));

// Find nearest zombie
Zombie nearest = WorldHelper.getNearestEntityOfType(
    world, Zombie.class, pos.toCenterPos(), 32.0);
```

### RecipeHelper

**Package:** `dk.mosberg.api.util`

Runtime recipe queries, filtering, and validation.

#### Key Methods

```java
// Recipe queries
List<RecipeEntry<?>> getAllRecipes(World world)
<T extends Recipe<?>> List<RecipeEntry<T>> getRecipesByType(World world, RecipeType<T> type)
List<RecipeEntry<?>> getRecipesForItem(World world, ItemStack output)
Optional<RecipeEntry<?>> getRecipe(World world, Identifier id)

// Recipe validation
boolean hasRecipe(World world, Identifier id)
boolean canCraft(World world, Recipe<?> recipe, Inventory inventory)
ItemStack getRecipeOutput(Recipe<?> recipe, RegistryWrapper.WrapperLookup registries)

// Recipe filtering
List<RecipeEntry<?>> filterByIngredient(World world, Item ingredient)
List<RecipeEntry<?>> filterByResult(World world, Item result)
```

#### Examples

```java
// Get all smelting recipes
List<RecipeEntry<SmeltingRecipe>> smeltingRecipes =
    RecipeHelper.getRecipesByType(world, RecipeType.SMELTING);

// Find recipes that produce diamonds
List<RecipeEntry<?>> diamondRecipes =
    RecipeHelper.getRecipesForItem(world, new ItemStack(Items.DIAMOND));

// Check if player can craft something
if (RecipeHelper.canCraft(world, recipe, player.getInventory())) {
    // Player has required ingredients
}

// Find recipes using iron
List<RecipeEntry<?>> ironRecipes =
    RecipeHelper.filterByIngredient(world, Items.IRON_INGOT);
```

### Additional Helpers

- **AttributeHelper** - Entity attribute modification
- **CommandHelper** - Command registration and parsing
- **EnchantmentUtil** - Enchantment operations
- **NBTHelper** - NBT read/write operations
- **NetworkHelper** - Packet handling
- **SerializationHelper** - Data serialization
- **TagHelper** - Tag management

See [Complete Utility Reference](Utility-Reference) for all methods.

---

## Data Generation

### Overview

Mosberg API provides enhanced data generation classes for recipes, loot tables, and models.

### MosbergRecipeProvider

**Package:** `dk.mosberg.api.data.provider`

Enhanced recipe generation with helper methods.

#### Usage

```java
package com.yourmod.data;

import dk.mosberg.api.data.provider.MosbergRecipeProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.recipe.*;
import net.minecraft.registry.RegistryWrapper;
import java.util.concurrent.CompletableFuture;

public class ModRecipes extends MosbergRecipeProvider {
    public ModRecipes(DataOutput output,
                     CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(
            RegistryWrapper.WrapperLookup registries,
            RecipeExporter exporter) {
        return new ModRecipeGenerator(registries, exporter);
    }

    private static class ModRecipeGenerator extends RecipeGenerator {
        public ModRecipeGenerator(RegistryWrapper.WrapperLookup registries,
                                 RecipeExporter exporter) {
            super(registries, exporter);
        }

        @Override
        public void generate() {
            // Your recipes here
        }
    }
}
```

#### Recipe Types

```java
// Shaped crafting
createShaped(RecipeCategory.TOOLS, ModItems.CUSTOM_TOOL)
    .pattern(" D ")
    .pattern(" S ")
    .pattern(" S ")
    .input('D', Items.DIAMOND)
    .input('S', Items.STICK)
    .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
    .offerTo(exporter);

// Shapeless crafting
createShapeless(RecipeCategory.MISC, Items.PURPLE_DYE)
    .input(Items.RED_DYE)
    .input(Items.BLUE_DYE)
    .criterion(hasItem(Items.RED_DYE), conditionsFromItem(Items.RED_DYE))
    .offerTo(exporter);

// Smelting
offerSmelting(exporter, List.of(ModBlocks.CUSTOM_ORE),
    RecipeCategory.MISC, ModItems.CUSTOM_INGOT,
    1.0f, 200, "custom_ingot");

// Blasting (faster smelting)
offerBlasting(exporter, List.of(ModBlocks.CUSTOM_ORE),
    RecipeCategory.MISC, ModItems.CUSTOM_INGOT,
    1.0f, 100, "custom_ingot_from_blasting");

// Stonecutting
createStonecutting(RecipeCategory.BUILDING_BLOCKS,
    ModBlocks.CUSTOM_STAIRS, ModBlocks.CUSTOM_BLOCK)
    .criterion(hasItem(ModBlocks.CUSTOM_BLOCK),
        conditionsFromItem(ModBlocks.CUSTOM_BLOCK))
    .offerTo(exporter);

// Smithing (transformation)
createSmithing(RecipeCategory.EQUIPMENT,
    Items.DIAMOND_SWORD,
    Items.NETHERITE_INGOT,
    Items.NETHERITE_SWORD)
    .criterion(hasItem(Items.NETHERITE_INGOT),
        conditionsFromItem(Items.NETHERITE_INGOT))
    .offerTo(exporter);
```

### MosbergLootTableProvider

**Package:** `dk.mosberg.api.data.provider`

Enhanced loot table generation with helper methods.[1]

#### Usage

```java
package com.yourmod.data;

import dk.mosberg.api.data.provider.MosbergLootTableProvider;

public class ModLootTables extends MosbergLootTableProvider {
    public ModLootTables(FabricDataOutput output,
                        CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    public void generate() {
        // Simple block drop
        addSimpleDrop(ModBlocks.CUSTOM_BLOCK);

        // Ore with fortune
        addOreDrop(ModBlocks.CUSTOM_ORE, ModItems.CUSTOM_GEM);

        // Slab drops
        addSlabDrop(ModBlocks.CUSTOM_SLAB);

        // Door drops
        addDoorDrop(ModBlocks.CUSTOM_DOOR);

        // Random amount
        addRandomDrop(ModBlocks.GRAVEL_ORE, ModItems.CUSTOM_GEM, 1, 4);
    }
}
```

#### Loot Table Helpers

```java
// Simple block drop (drops itself)
addSimpleDrop(Block block)

// No drop (for technical blocks)
addNoDrop(Block block)

// Ore with fortune enchantment
addOreDrop(Block block, Item drop)

// Silk touch only
addSilkTouchDrop(Block block)

// Silk touch or alternative
addSilkTouchOrDrop(Block block, ItemConvertible alternativeDrop)

// Random amount (min-max)
addRandomDrop(Block block, ItemConvertible drop, float min, float max)

// With shears only
addShearsDrop(Block block, ItemConvertible drop)

// Crop drops (age-based)
addCropDrop(Block crop, Item seeds, Item product, int maxAge)

// Slab drops (1 if single, 2 if double)
addSlabDrop(Block slab)

// Door drops (handles upper/lower)
addDoorDrop(Block door)

// Fortune with specific count range
addFortuneRangeDrop(Block block, ItemConvertible drop, int min, int max)
```

### MosbergModelProvider

**Package:** `dk.mosberg.api.client.data.provider`

Model generation for blocks and items (1.21+ format).

#### Usage

```java
package com.yourmod.client.data;

import dk.mosberg.api.client.data.provider.MosbergModelProvider;

public class ModModels extends MosbergModelProvider {
    public ModModels(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        // Simple cube_all block
        registerSimpleBlock(generator, ModBlocks.CUSTOM_BLOCK);

        // Complex block with custom model
        generator.registerCubeAllModelTexturePool(ModBlocks.STONE_VARIANT);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        // Simple item
        registerSimpleItem(generator, ModItems.CUSTOM_ITEM);

        // Handheld tool
        registerHandheldItem(generator, ModItems.CUSTOM_TOOL);

        // Block item (uses parent block model)
        registerBlockItem(generator, ModBlocks.CUSTOM_BLOCK);
    }
}
```

### Running Data Generation

Add to your data generator:

```java
package com.yourmod.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ModRecipes::new);
        pack.addProvider(ModLootTables::new);
        pack.addProvider(ModModels::new);
    }
}
```

Update `fabric.mod.json`:

```json
{
  "entrypoints": {
    "fabric-datagen": ["com.yourmod.data.ModDataGenerator"]
  }
}
```

Run data generation:

```bash
./gradlew runDatagen
```

Generated files appear in `src/generated/resources/`.

---

## Examples

### Complete Block Example

```java
// 1. Create block class
public class CustomBlock extends Block {
    public CustomBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                             PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            player.sendMessage(Text.literal("Block clicked!"));
        }
        return ActionResult.SUCCESS;
    }
}

// 2. Register block
public static final Block CUSTOM_BLOCK = MosbergBlocks.register(
    "custom_block",
    new CustomBlock(AbstractBlock.Settings.create()
        .strength(3.0f)
        .requiresTool()
        .sounds(BlockSoundGroup.STONE))
);

// 3. Generate loot table
public class ModLootTables extends MosbergLootTableProvider {
    @Override
    public void generate() {
        addSimpleDrop(ModBlocks.CUSTOM_BLOCK);
    }
}

// 4. Generate model
public class ModModels extends MosbergModelProvider {
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        registerSimpleBlock(generator, ModBlocks.CUSTOM_BLOCK);
    }
}

// 5. Add texture: assets/mymod/textures/block/custom_block.png
// 6. Add translation: assets/mymod/lang/en_us.json
{
  "block.mymod.custom_block": "Custom Block"
}
```

### Complete Entity Example

```java
// 1. Create entity class
public class CustomMob extends HostileEntity {
    public CustomMob(EntityType<? extends HostileEntity> type, World world) {
        super(type, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));

        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
            .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
    }
}

// 2. Register entity
public static final EntityType<CustomMob> CUSTOM_MOB =
    MosbergEntities.register("custom_mob",
        EntityType.Builder.create(CustomMob::new, SpawnGroup.MONSTER)
            .dimensions(0.6f, 1.95f)
            .maxTrackingRange(64)
    );

// 3. Register attributes
FabricDefaultAttributeRegistry.register(CUSTOM_MOB, CustomMob.createAttributes());

// 4. Add spawn egg item
public static final Item CUSTOM_MOB_SPAWN_EGG = MosbergItems.register(
    "custom_mob_spawn_egg",
    new SpawnEggItem(CUSTOM_MOB, 0x123456, 0x789ABC, new Item.Settings())
);
```

### Configuration Example

```java
import dk.mosberg.api.config.ConfigManager;

public class ModConfig {
    private static ConfigManager config;

    // Config values
    public static int mobSpawnRate;
    public static boolean enableFeature;
    public static double dropChance;

    public static void load() {
        config = new ConfigManager("mymod");

        mobSpawnRate = config.getInt("mob_spawn_rate", 10);
        enableFeature = config.getBoolean("enable_feature", true);
        dropChance = config.getDouble("drop_chance", 0.25);

        save();
    }

    public static void save() {
        config.setInt("mob_spawn_rate", mobSpawnRate);
        config.setBoolean("enable_feature", enableFeature);
        config.setDouble("drop_chance", dropChance);
        config.save();
    }
}
```

---

## API Reference

### Complete Class Index

#### Registry Classes (`dk.mosberg.api.registry`)

- [MosbergBlocks](API-MosbergBlocks)
- [MosbergItems](API-MosbergItems)
- [MosbergEntities](API-MosbergEntities)
- [MosbergSounds](API-MosbergSounds)
- [MosbergParticles](API-MosbergParticles)
- [MosbergItemGroups](API-MosbergItemGroups)
- [MosbergRegistries](API-MosbergRegistries)

#### Utility Classes (`dk.mosberg.api.util`)

- [AttributeHelper](API-AttributeHelper)
- [BlockHelper](API-BlockHelper)
- [CommandHelper](API-CommandHelper)
- [EnchantmentUtil](API-EnchantmentUtil)
- [EntityHelper](API-EntityHelper)
- [InventoryHelper](API-InventoryHelper)
- [ItemHelper](API-ItemHelper)
- [MosbergHelper](API-MosbergHelper)
- [NBTHelper](API-NBTHelper)
- [NetworkHelper](API-NetworkHelper)
- [RecipeHelper](API-RecipeHelper)
- [SerializationHelper](API-SerializationHelper)
- [TagHelper](API-TagHelper)
- [WorldHelper](API-WorldHelper)

#### Data Generation (`dk.mosberg.api.data.provider`)

- [MosbergRecipeProvider](API-MosbergRecipeProvider)
- [MosbergLootTableProvider](API-MosbergLootTableProvider)

#### Client (`dk.mosberg.api.client`)

- [MosbergModelProvider](API-MosbergModelProvider)
- [RenderHelper](API-RenderHelper)

#### Systems

- [ConfigManager](API-ConfigManager)
- [MosbergEvents](API-MosbergEvents)
- [TestHelper](API-TestHelper)
- [DocumentationGenerator](API-DocumentationGenerator)

---

## FAQ

### General Questions

**Q: Is Mosberg API required for players?**
A: Only if you're using mods that depend on it. Mosberg API is a library mod.

**Q: Does Mosberg API add content to the game?**
A: No, it's purely a development library. It doesn't add blocks, items, or entities on its own.

**Q: Can I use Mosberg API in my mod?**
A: Yes! It's licensed under MIT. Just add it as a dependency.

### Development Questions

**Q: How do I include Mosberg API with my mod?**
A: Use `include` in your build.gradle:

```gradle
dependencies {
    modImplementation "dk.mosberg:Mosberg API:1.0.0"
    include "dk.mosberg:Mosberg API:1.0.0"
}
```

**Q: Do I need to call `initialize()` methods?**
A: No! Mosberg API automatically registers everything when your static fields are loaded.

**Q: Can I use Mosberg API with other mod loaders?**
A: No, it's specifically designed for Fabric/Quilt.

**Q: How do I update to a new version?**
A: Change the version number in build.gradle and run `./gradlew build --refresh-dependencies`.

### Troubleshooting

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

---

## Contributing

See the [Contributing Guide](Contributing) for information on:

- Code style guidelines
- Pull request process
- Testing requirements
- Documentation standards

---

## Changelog

See [CHANGELOG.md](https://github.com/Mosberg/Mosberg API/blob/main/CHANGELOG.md) for version history.

---

## License

Mosberg API is licensed under the MIT License. See [LICENSE](https://github.com/Mosberg/Mosberg API/blob/main/LICENSE).

---
