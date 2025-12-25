package dk.mosberg.api.client.registry;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/**
 * Central registry for entity model layers in MosbergAPI.
 *
 * <p>
 * Model layers define the visual structure of entities, armor, and other rendered objects in
 * Minecraft. Each layer consists of an {@link Identifier} and a layer name (e.g., "main",
 * "inner_armor").
 *
 * <h2>Model Layer Types</h2>
 * <ul>
 * <li><strong>Main Layer:</strong> Primary entity model (layer name: "main")</li>
 * <li><strong>Armor Layers:</strong> Two layers for armor rendering:
 * <ul>
 * <li>Inner armor (leggings) - layer name: "inner_armor"</li>
 * <li>Outer armor (helmet, chestplate, boots) - layer name: "outer_armor"</li>
 * </ul>
 * </li>
 * <li><strong>Custom Layers:</strong> Additional layers for special rendering (saddles,
 * decorations, etc.)</li>
 * </ul>
 *
 * <h2>Integration with MosbergRenderers</h2>
 * <p>
 * Model layers defined here should be registered with {@link MosbergRenderers} in your client
 * initializer. The renderer associates model layers with
 * {@link net.minecraft.client.model.TexturedModelData}.
 *
 * @example
 *
 *          <pre>{@code
 * public class ModModelLayers {
 *     // Entity model layer
 *     public static final EntityModelLayer CUSTOM_MOB =
 *         MosbergRenderers.createMainLayer(Identifier.of("mymod", "custom_mob"));
 *
 *     // Armor model layers
 *     public static final EntityModelLayer[] RUBY_ARMOR =
 *         MosbergRenderers.createArmorLayers(Identifier.of("mymod", "ruby_armor"));
 *     public static final EntityModelLayer RUBY_ARMOR_INNER = RUBY_ARMOR[0];
 *     public static final EntityModelLayer RUBY_ARMOR_OUTER = RUBY_ARMOR[1];
 *
 *     // Block entity model layer
 *     public static final EntityModelLayer MAGIC_CHEST =
 *         MosbergRenderers.createModelLayer(Identifier.of("mymod", "magic_chest"), "main");
 *
 *     public static void initialize() {
 *         // Model layers are automatically registered when renderers are registered
 *          }
 *          }
 *
 *          // In your client initializer:
 *          MosbergRenderers.registerEntityRenderer(ModEntities.CUSTOM_MOB, CustomMobRenderer::new,
 *          ModModelLayers.CUSTOM_MOB, CustomMobModel::getTexturedModelData);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see MosbergRenderers
 * @see EntityModelLayer
 * @see net.minecraft.client.model.TexturedModelData
 */
public class MosbergModelLayers {

        /**
         * Example entity model layer for a custom mob.
         *
         * <p>
         * This demonstrates the standard pattern for defining entity model layers. Replace "mymod"
         * with your mod ID and "custom_mob" with your entity name.
         */
        public static final EntityModelLayer CUSTOM_MOB =
                        MosbergRenderers.createMainLayer(Identifier.of("mymod", "custom_mob"));

        /**
         * Example entity model layer for a custom creature.
         *
         * <p>
         * Multiple entities can have separate model layers, allowing different visual structures.
         */
        public static final EntityModelLayer CUSTOM_CREATURE =
                        MosbergRenderers.createMainLayer(Identifier.of("mymod", "custom_creature"));

        /**
         * Example armor model layers for custom armor.
         *
         * <p>
         * Armor requires two layers: inner (index 0) for leggings, outer (index 1) for helmet,
         * chestplate, and boots. Use {@link MosbergRenderers#createArmorLayers(Identifier)} to
         * create both layers at once.
         */
        public static final EntityModelLayer[] CUSTOM_ARMOR =
                        MosbergRenderers.createArmorLayers(Identifier.of("mymod", "custom_armor"));

        /**
         * Inner armor layer for custom armor (leggings).
         *
         * <p>
         * Extracted from {@link #CUSTOM_ARMOR} array for convenience.
         */
        public static final EntityModelLayer CUSTOM_ARMOR_INNER = CUSTOM_ARMOR[0];

        /**
         * Outer armor layer for custom armor (helmet, chestplate, boots).
         *
         * <p>
         * Extracted from {@link #CUSTOM_ARMOR} array for convenience.
         */
        public static final EntityModelLayer CUSTOM_ARMOR_OUTER = CUSTOM_ARMOR[1];

        /**
         * Example block entity model layer for a custom chest.
         *
         * <p>
         * Block entities (like chests, signs, etc.) can have custom models registered using model
         * layers. The layer name "main" is standard for the primary model.
         */
        public static final EntityModelLayer CUSTOM_CHEST = MosbergRenderers
                        .createModelLayer(Identifier.of("mymod", "custom_chest"), "main");

        /**
         * Initialize model layers.
         *
         * <p>
         * Model layers are automatically registered when entity/block entity renderers are
         * registered via {@link MosbergRenderers}. This method exists for consistency but performs
         * no actual registration.
         *
         * <p>
         * <strong>Important:</strong> Ensure this class is loaded before registering renderers in
         * your client initializer.
         *
         * @example
         *
         *          <pre>{@code
         *          @Override
         *          public void onInitializeClient() {
         *          ModModelLayers.initialize();
         *          // Now register renderers that reference these model layers
         *          }
         * }</pre>
         */
        public static void initialize() {
                // Model layers are registered when used by renderers
                // This method ensures the class is loaded
        }
}
