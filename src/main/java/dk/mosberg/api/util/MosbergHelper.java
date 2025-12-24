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
