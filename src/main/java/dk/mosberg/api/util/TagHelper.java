package dk.mosberg.api.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * Helper for creating and checking tags
 */
public class TagHelper {

    public static TagKey<Block> blockTag(String path) {
        return TagKey.of(RegistryKeys.BLOCK, Identifier.of("mosbergapi", path));
    }

    public static TagKey<Item> itemTag(String path) {
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("mosbergapi", path));
    }

    public static boolean isInTag(Block block, TagKey<Block> tag) {
        return block.getDefaultState().isIn(tag);
    }
}
