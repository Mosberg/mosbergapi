package dk.mosberg.api.client.registry;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/**
 * Example model layer definitions for your mod.
 */
public class MosbergModelLayers {

    // Entity model layers
    public static final EntityModelLayer CUSTOM_MOB =
            MosbergRenderers.createMainLayer(Identifier.of("mymod", "custom_mob"));

    public static final EntityModelLayer CUSTOM_CREATURE =
            MosbergRenderers.createMainLayer(Identifier.of("mymod", "custom_creature"));

    // Armor model layers
    public static final EntityModelLayer[] CUSTOM_ARMOR =
            MosbergRenderers.createArmorLayers(Identifier.of("mymod", "custom_armor"));

    public static final EntityModelLayer CUSTOM_ARMOR_INNER = CUSTOM_ARMOR[0];
    public static final EntityModelLayer CUSTOM_ARMOR_OUTER = CUSTOM_ARMOR[1];

    // Block entity model layers
    public static final EntityModelLayer CUSTOM_CHEST =
            MosbergRenderers.createModelLayer(Identifier.of("mymod", "custom_chest"), "main");

    public static void initialize() {
        // Model layers are registered when used
    }
}
