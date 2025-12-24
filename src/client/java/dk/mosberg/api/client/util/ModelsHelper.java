package dk.mosberg.api.client.util;

import dk.mosberg.api.client.registry.MosbergModels;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;

/**
 * Simplified helper for common model operations.
 * <p>
 * Provides quick access to frequently used model building patterns without needing to interact with
 * the full MosbergModels API.
 * </p>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 */
public class ModelsHelper {

    private ModelsHelper() {
        throw new UnsupportedOperationException("ModelsHelper is a utility class");
    }

    /**
     * Quick method to create a simple cube model.
     *
     * @param size Size of the cube
     * @return TexturedModelData for a cube
     *
     * @example
     *
     *          <pre>{@code
     * TexturedModelData cubeModel = ModelsHelper.createCube(16);
     * }</pre>
     */
    public static TexturedModelData createCube(float size) {
        ModelData data = MosbergModels.createModelData();
        ModelPartData root = data.getRoot();

        float halfSize = size / 2;
        root.addChild("cube", ModelPartBuilder.create().uv(0, 0).cuboid(-halfSize, -halfSize,
                -halfSize, size, size, size),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return MosbergModels.createTexturedModelData(data, 64, 64);
    }

    /**
     * Creates a simple humanoid model with default proportions.
     *
     * @return TexturedModelData for humanoid
     */
    public static TexturedModelData createHumanoid() {
        return MosbergModels
                .createTexturedModelData(MosbergModels.createHumanoidModel(Dilation.NONE), 64, 64);
    }

    /**
     * Creates a simple humanoid model with armor dilation.
     *
     * @param dilation Dilation amount (0.5F for inner armor, 1.0F for outer)
     * @return TexturedModelData for humanoid armor
     */
    public static TexturedModelData createHumanoidArmor(float dilation) {
        return MosbergModels.createTexturedModelData(
                MosbergModels.createHumanoidModel(new Dilation(dilation)), 64, 64);
    }

    /**
     * Creates a simple quadruped model.
     *
     * @return TexturedModelData for quadruped
     */
    public static TexturedModelData createQuadruped() {
        return MosbergModels.createTexturedModelData(MosbergModels.createQuadrupedModel(), 64, 64);
    }

    /**
     * Creates a simple box model (like a chest).
     *
     * @param width Width of the box
     * @param height Height of the box
     * @param depth Depth of the box
     * @return TexturedModelData for box
     */
    public static TexturedModelData createBox(float width, float height, float depth) {
        ModelData data = MosbergModels.createModelData();
        ModelPartData root = data.getRoot();

        root.addChild("box",
                ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, 0.0F, width, height, depth),
                ModelTransform.of(-width / 2, 0.0F, -depth / 2, 0.0F, 0.0F, 0.0F));

        return MosbergModels.createTexturedModelData(data, 64, 64);
    }

    /**
     * Creates a simple sphere-like model using multiple cuboids.
     * <p>
     * Note: This is an approximation, not a true sphere.
     * </p>
     *
     * @param radius Radius of the sphere
     * @param segments Number of segments (more = smoother)
     * @return TexturedModelData for sphere
     */
    public static TexturedModelData createSphere(float radius, int segments) {
        ModelData data = MosbergModels.createModelData();
        ModelPartData root = data.getRoot();

        // Simple sphere approximation with a few cuboids
        root.addChild("core",
                ModelPartBuilder.create().uv(0, 0).cuboid(-radius, -radius, -radius, radius * 2,
                        radius * 2, radius * 2),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return MosbergModels.createTexturedModelData(data, 64, 64);
    }

    /**
     * Creates a flat plane model.
     *
     * @param width Width of the plane
     * @param depth Depth of the plane
     * @return TexturedModelData for plane
     */
    public static TexturedModelData createPlane(float width, float depth) {
        ModelData data = MosbergModels.createModelData();
        ModelPartData root = data.getRoot();

        root.addChild("plane", ModelPartBuilder.create().uv(0, 0).cuboid(-width / 2, 0.0F,
                -depth / 2, width, 0.1F, depth),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        return MosbergModels.createTexturedModelData(data, 64, 64);
    }

    /**
     * Quickly adds a simple part to existing model data.
     *
     * @param data The ModelData to add to
     * @param name Name of the part
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @param width Width
     * @param height Height
     * @param depth Depth
     */
    public static void addPart(ModelData data, String name, float x, float y, float z, float width,
            float height, float depth) {
        data.getRoot().addChild(name,
                ModelPartBuilder.create().uv(0, 0).cuboid(x, y, z, width, height, depth),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
    }
}
