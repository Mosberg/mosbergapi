package dk.mosberg.api.client.util;

import dk.mosberg.api.client.registry.MosbergModels;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/**
 * Simplified helper for creating EntityModelLayers.
 * <p>
 * Provides quick access to model layer creation without needing the full MosbergModels API.
 * </p>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class ModelLayersHelper {

    private ModelLayersHelper() {
        throw new UnsupportedOperationException("ModelLayersHelper is a utility class");
    }

    /**
     * Creates a main model layer.
     * <p>
     * Shorthand for {@code new EntityModelLayer(id, "main")}.
     * </p>
     *
     * @param modId Your mod ID
     * @param entityId The entity ID
     * @return EntityModelLayer with "main" layer
     *
     * @example
     *
     *          <pre>{@code
     * public static final EntityModelLayer CUSTOM_MOB =
     *     ModelLayersHelper.main("mymod", "custom_mob");
     * }</pre>
     */
    public static EntityModelLayer main(String modId, String entityId) {
        return MosbergModels.createMainLayer(Identifier.of(modId, entityId));
    }

    /**
     * Creates an inner armor layer.
     *
     * @param modId Your mod ID
     * @param armorId The armor ID
     * @return EntityModelLayer with "inner_armor" layer
     */
    public static EntityModelLayer innerArmor(String modId, String armorId) {
        return MosbergModels.createModelLayer(Identifier.of(modId, armorId), "inner_armor");
    }

    /**
     * Creates an outer armor layer.
     *
     * @param modId Your mod ID
     * @param armorId The armor ID
     * @return EntityModelLayer with "outer_armor" layer
     */
    public static EntityModelLayer outerArmor(String modId, String armorId) {
        return MosbergModels.createModelLayer(Identifier.of(modId, armorId), "outer_armor");
    }

    /**
     * Creates both armor layers at once.
     * <p>
     * Returns an array: [inner, outer]
     * </p>
     *
     * @param modId Your mod ID
     * @param armorId The armor ID
     * @return Array of [inner, outer] EntityModelLayers
     *
     * @example
     *
     *          <pre>{@code
     * EntityModelLayer[] layers = ModelLayersHelper.armor("mymod", "custom_armor");
     * EntityModelLayer inner = layers[0];
     * EntityModelLayer outer = layers[1];
     * }</pre>
     */
    public static EntityModelLayer[] armor(String modId, String armorId) {
        return new EntityModelLayer[] {innerArmor(modId, armorId), outerArmor(modId, armorId)};
    }

    /**
     * Creates a custom layer with specified layer name.
     *
     * @param modId Your mod ID
     * @param entityId The entity ID
     * @param layerName Custom layer name (e.g., "saddle", "clothing")
     * @return EntityModelLayer with custom layer
     *
     * @example
     *
     *          <pre>{@code
     * EntityModelLayer saddle = ModelLayersHelper.custom("mymod", "horse", "saddle");
     * }</pre>
     */
    public static EntityModelLayer custom(String modId, String entityId, String layerName) {
        return MosbergModels.createModelLayer(Identifier.of(modId, entityId), layerName);
    }

    /**
     * Creates a model layer from a full identifier.
     *
     * @param id The full Identifier
     * @param layerName The layer name
     * @return EntityModelLayer
     */
    public static EntityModelLayer of(Identifier id, String layerName) {
        return MosbergModels.createModelLayer(id, layerName);
    }

    /**
     * Creates a main model layer from a full identifier.
     *
     * @param id The full Identifier
     * @return EntityModelLayer with "main" layer
     */
    public static EntityModelLayer of(Identifier id) {
        return MosbergModels.createMainLayer(id);
    }
}
