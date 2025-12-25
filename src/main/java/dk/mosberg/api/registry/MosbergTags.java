package dk.mosberg.api.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * Registry class for all custom tags in MosbergAPI. Provides helper methods for creating tag keys
 * that reference data pack tag files.
 *
 * <p>
 * Tags are data-driven and defined in JSON files under data/mosbergapi/tags/
 *
 * @example
 *
 *          <pre>{@code
 * // Item tags
 * public static final TagKey<Item> CUSTOM_INGOTS = MosbergTags.item("custom_ingots");
 * public static final TagKey<Item> CUSTOM_GEMS = MosbergTags.item("custom_gems");
 *
 * // Block tags
 * public static final TagKey<Block> CUSTOM_ORES = MosbergTags.block("custom_ores");
 * public static final TagKey<Block> MINEABLE_WITH_CUSTOM_TOOL =
 *     MosbergTags.block("mineable/custom_tool");
 *
 * // Usage:
 * if (stack.isIn(CUSTOM_INGOTS)) {
 *     // Do something
 *          }
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class MosbergTags {
    private static final Logger LOGGER = LoggerFactory.getLogger(MosbergTags.class);
    private static final String MOD_ID = "mosbergapi";

    /**
     * Creates a tag key for items. The tag file must exist in data/mosbergapi/tags/item/{name}.json
     *
     * @param name The tag name (will be prefixed with mod ID)
     * @return The TagKey for items
     *
     * @example
     *
     *          <pre>{@code
     * TagKey<Item> customTools = MosbergTags.item("custom_tools");
     * // Corresponds to: data/mosbergapi/tags/item/custom_tools.json
     * }</pre>
     */
    public static TagKey<Item> item(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        LOGGER.debug("Created item tag: {}", id);
        return TagKey.of(RegistryKeys.ITEM, id);
    }

    /**
     * Creates a tag key for blocks. The tag file must exist in
     * data/mosbergapi/tags/block/{name}.json
     *
     * @param name The tag name (will be prefixed with mod ID)
     * @return The TagKey for blocks
     *
     * @example
     *
     *          <pre>{@code
     * TagKey<Block> customOres = MosbergTags.block("custom_ores");
     * // Corresponds to: data/mosbergapi/tags/block/custom_ores.json
     * }</pre>
     */
    public static TagKey<Block> block(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        LOGGER.debug("Created block tag: {}", id);
        return TagKey.of(RegistryKeys.BLOCK, id);
    }

    /**
     * Creates a tag key for entity types. The tag file must exist in
     * data/mosbergapi/tags/entity_type/{name}.json
     *
     * @param name The tag name (will be prefixed with mod ID)
     * @return The TagKey for entity types
     *
     * @example
     *
     *          <pre>{@code
     * TagKey<EntityType<?>> customMobs = MosbergTags.entityType("custom_mobs");
     * // Corresponds to: data/mosbergapi/tags/entity_type/custom_mobs.json
     * }</pre>
     */
    public static TagKey<EntityType<?>> entityType(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        LOGGER.debug("Created entity type tag: {}", id);
        return TagKey.of(RegistryKeys.ENTITY_TYPE, id);
    }

    /**
     * Creates a tag key for fluids. The tag file must exist in
     * data/mosbergapi/tags/fluid/{name}.json
     *
     * @param name The tag name (will be prefixed with mod ID)
     * @return The TagKey for fluids
     *
     * @example
     *
     *          <pre>{@code
     * TagKey<Fluid> customLiquids = MosbergTags.fluid("custom_liquids");
     * // Corresponds to: data/mosbergapi/tags/fluid/custom_liquids.json
     * }</pre>
     */
    public static TagKey<Fluid> fluid(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        LOGGER.debug("Created fluid tag: {}", id);
        return TagKey.of(RegistryKeys.FLUID, id);
    }

    /**
     * Creates a Minecraft tag key for items (using minecraft namespace). Useful for adding to
     * vanilla tags.
     *
     * @param name The tag name
     * @return The TagKey for items with minecraft namespace
     *
     * @example
     *
     *          <pre>{@code
     * TagKey<Item> pickaxes = MosbergTags.minecraftItem("pickaxes");
     * // Returns: minecraft:pickaxes
     * }</pre>
     */
    public static TagKey<Item> minecraftItem(String name) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.ofVanilla(name));
    }

    /**
     * Creates a Minecraft tag key for blocks (using minecraft namespace).
     *
     * @param name The tag name
     * @return The TagKey for blocks with minecraft namespace
     */
    public static TagKey<Block> minecraftBlock(String name) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.ofVanilla(name));
    }

    /**
     * Creates a common tag (c namespace) for items. Used for cross-mod compatibility.
     *
     * @param name The tag name
     * @return The TagKey for items with c namespace
     *
     * @example
     *
     *          <pre>{@code
     * TagKey<Item> ingots = MosbergTags.commonItem("ingots");
     * // Returns: c:ingots
     * }</pre>
     */
    public static TagKey<Item> commonItem(String name) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", name));
    }

    /**
     * Creates a common tag (c namespace) for blocks.
     *
     * @param name The tag name
     * @return The TagKey for blocks with c namespace
     */
    public static TagKey<Block> commonBlock(String name) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of("c", name));
    }

    /**
     * Initialize and create tag references. Call this method from your mod initializer.
     */
    public static void initialize() {
        LOGGER.info("Initializing MosbergAPI tags (data-driven)");
        // Tags are loaded from data packs
        // JSON files should be in: data/mosbergapi/tags/
    }
}
