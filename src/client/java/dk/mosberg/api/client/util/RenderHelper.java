package dk.mosberg.api.client.util;

import org.jetbrains.annotations.NotNull;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

/**
 * Client-side helper class for rendering operations in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for common rendering tasks, matrix transformations, and
 * working with the render system.
 *
 * <h2>Rendering System</h2>
 * <p>
 * Modern Minecraft rendering uses several key components:
 * <ul>
 * <li><strong>MatrixStack:</strong> Transformation matrices (translate, rotate, scale)</li>
 * <li><strong>VertexConsumer:</strong> Builds geometry for rendering</li>
 * <li><strong>RenderLayer:</strong> Defines how geometry is rendered (texture, transparency)</li>
 * <li><strong>Tessellator:</strong> Batches and submits vertices to GPU</li>
 * </ul>
 *
 * <h2>Render Phases</h2>
 * <p>
 * Rendering happens in specific phases:
 * <ul>
 * <li><strong>Solid:</strong> Opaque geometry (blocks, entities)</li>
 * <li><strong>Cutout:</strong> Textures with alpha testing (grass, leaves)</li>
 * <li><strong>Translucent:</strong> Semi-transparent (water, glass, particles)</li>
 * <li><strong>Overlay:</strong> UI elements, outlines, effects</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Push matrix for isolated transformation
 * RenderHelper.push(matrices);
 * RenderHelper.translate(matrices, 0, 1, 0);
 * RenderHelper.scale(matrices, 2.0f);
 * // ... render something ...
 * RenderHelper.pop(matrices);
 *
 * // Rotate around axis
 * RenderHelper.rotateY(matrices, 90.0f);
 *
 * // Get current render tick
 * float delta = RenderHelper.getTickDelta();
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see MatrixStack
 * @see VertexConsumer
 * @see RenderLayer
 */
@Environment(EnvType.CLIENT)
public class RenderHelper {

    private RenderHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Pushes the current matrix state onto the stack.
     *
     * <p>
     * Always pair with {@link #pop(MatrixStack)} to avoid stack corruption.
     *
     * @param matrices The matrix stack
     * @throws NullPointerException if matrices is null
     *
     * @example
     *
     *          <pre>{@code
     * RenderHelper.push(matrices);
     * // Apply transformations
     * RenderHelper.translate(matrices, x, y, z);
     * // Render something
     * RenderHelper.pop(matrices);
     * }</pre>
     */
    public static void push(@NotNull MatrixStack matrices) {
        if (matrices == null)
            throw new NullPointerException("MatrixStack cannot be null");
        matrices.push();
    }

    /**
     * Pops the matrix state from the stack.
     *
     * @param matrices The matrix stack
     * @throws NullPointerException if matrices is null
     *
     * @example
     *
     *          <pre>{@code
     * RenderHelper.push(matrices);
     * // ... transformations and rendering ...
     * RenderHelper.pop(matrices);
     * }</pre>
     */
    public static void pop(@NotNull MatrixStack matrices) {
        if (matrices == null)
            throw new NullPointerException("MatrixStack cannot be null");
        matrices.pop();
    }

    /**
     * Translates the matrix stack.
     *
     * @param matrices The matrix stack
     * @param x X offset
     * @param y Y offset
     * @param z Z offset
     * @throws NullPointerException if matrices is null
     *
     * @example
     *
     *          <pre>{@code
     * // Move rendering position up by 1 block
     * RenderHelper.translate(matrices, 0, 1, 0);
     * }</pre>
     */
    public static void translate(@NotNull MatrixStack matrices, double x, double y, double z) {
        if (matrices == null)
            throw new NullPointerException("MatrixStack cannot be null");
        matrices.translate(x, y, z);
    }

    /**
     * Translates the matrix stack using float values.
     *
     * @param matrices The matrix stack
     * @param x X offset
     * @param y Y offset
     * @param z Z offset
     */
    public static void translate(@NotNull MatrixStack matrices, float x, float y, float z) {
        translate(matrices, (double) x, (double) y, (double) z);
    }

    /**
     * Scales the matrix stack uniformly.
     *
     * @param matrices The matrix stack
     * @param scale Scale factor (1.0 = normal size)
     * @throws NullPointerException if matrices is null
     *
     * @example
     *
     *          <pre>{@code
     * // Render at double size
     * RenderHelper.scale(matrices, 2.0f);
     * }</pre>
     */
    public static void scale(@NotNull MatrixStack matrices, float scale) {
        scale(matrices, scale, scale, scale);
    }

    /**
     * Scales the matrix stack with different factors per axis.
     *
     * @param matrices The matrix stack
     * @param x X scale factor
     * @param y Y scale factor
     * @param z Z scale factor
     * @throws NullPointerException if matrices is null
     *
     * @example
     *
     *          <pre>{@code
     * // Stretch vertically
     * RenderHelper.scale(matrices, 1.0f, 2.0f, 1.0f);
     * }</pre>
     */
    public static void scale(@NotNull MatrixStack matrices, float x, float y, float z) {
        if (matrices == null)
            throw new NullPointerException("MatrixStack cannot be null");
        matrices.scale(x, y, z);
    }

    /**
     * Rotates around the X axis (pitch).
     *
     * @param matrices The matrix stack
     * @param degrees Rotation angle in degrees
     * @throws NullPointerException if matrices is null
     *
     * @example
     *
     *          <pre>{@code
     * // Rotate 90 degrees around X axis
     * RenderHelper.rotateX(matrices, 90.0f);
     * }</pre>
     */
    public static void rotateX(@NotNull MatrixStack matrices, float degrees) {
        if (matrices == null)
            throw new NullPointerException("MatrixStack cannot be null");
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(degrees));
    }

    /**
     * Rotates around the Y axis (yaw).
     *
     * @param matrices The matrix stack
     * @param degrees Rotation angle in degrees
     * @throws NullPointerException if matrices is null
     *
     * @example
     *
     *          <pre>{@code
     * // Rotate 180 degrees around Y axis
     * RenderHelper.rotateY(matrices, 180.0f);
     * }</pre>
     */
    public static void rotateY(@NotNull MatrixStack matrices, float degrees) {
        if (matrices == null)
            throw new NullPointerException("MatrixStack cannot be null");
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(degrees));
    }

    /**
     * Rotates around the Z axis (roll).
     *
     * @param matrices The matrix stack
     * @param degrees Rotation angle in degrees
     * @throws NullPointerException if matrices is null
     *
     * @example
     *
     *          <pre>{@code
     * // Rotate 45 degrees around Z axis
     * RenderHelper.rotateZ(matrices, 45.0f);
     * }</pre>
     */
    public static void rotateZ(@NotNull MatrixStack matrices, float degrees) {
        if (matrices == null)
            throw new NullPointerException("MatrixStack cannot be null");
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(degrees));
    }



    /**
     * Gets the Minecraft client instance.
     *
     * @return The Minecraft client
     *
     * @example
     *
     *          <pre>{@code
     * MinecraftClient client = RenderHelper.getClient();
     * }</pre>
     */
    @NotNull
    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    /**
     * Checks if the game is currently rendering.
     *
     * @return true if on render thread
     *
     * @example
     *
     *          <pre>{@code
     * if (RenderHelper.isOnRenderThread()) {
     *     // Safe to perform render operations
     *          }
     * }</pre>
     */
    public static boolean isOnRenderThread() {
        return getClient().isOnThread();
    }

    /**
     * Gets the window width in pixels.
     *
     * @return The window width
     *
     * @example
     *
     *          <pre>{@code
     * int width = RenderHelper.getWindowWidth();
     * int centerX = width / 2;
     * }</pre>
     */
    public static int getWindowWidth() {
        return getClient().getWindow().getWidth();
    }

    /**
     * Gets the window height in pixels.
     *
     * @return The window height
     *
     * @example
     *
     *          <pre>{@code
     * int height = RenderHelper.getWindowHeight();
     * int centerY = height / 2;
     * }</pre>
     */
    public static int getWindowHeight() {
        return getClient().getWindow().getHeight();
    }

    /**
     * Gets the scaled window width (GUI coordinates).
     *
     * @return The scaled width
     *
     * @example
     *
     *          <pre>{@code
     * int scaledWidth = RenderHelper.getScaledWidth();
     * // Use for GUI positioning
     * }</pre>
     */
    public static int getScaledWidth() {
        return getClient().getWindow().getScaledWidth();
    }

    /**
     * Gets the scaled window height (GUI coordinates).
     *
     * @return The scaled height
     *
     * @example
     *
     *          <pre>{@code
     * int scaledHeight = RenderHelper.getScaledHeight();
     * // Use for GUI positioning
     * }</pre>
     */
    public static int getScaledHeight() {
        return getClient().getWindow().getScaledHeight();
    }

    /**
     * Gets the GUI scale factor.
     *
     * @return The GUI scale (1, 2, 3, or 4 typically)
     *
     * @example
     *
     *          <pre>{@code
     * double scale = RenderHelper.getGuiScale();
     *          LOGGER.info("Current GUI scale: {}", scale);
     * }</pre>
     */
    public static double getGuiScale() {
        return getClient().getWindow().getScaleFactor();
    }

    /**
     * Gets the current frames per second.
     *
     * @return The current FPS
     *
     * @example
     *
     *          <pre>{@code
     * int fps = RenderHelper.getFPS();
     *          LOGGER.debug("Running at {} FPS", fps);
     * }</pre>
     */
    public static int getFPS() {
        return getClient().getCurrentFps();
    }

    /**
     * Checks if the game is paused.
     *
     * @return true if the game is paused
     *
     * @example
     *
     *          <pre>{@code
     * if (RenderHelper.isPaused()) {
     *     // Don't animate
     *          }
     * }</pre>
     */
    public static boolean isPaused() {
        return getClient().isPaused();
    }

    /**
     * Gets the texture manager.
     *
     * @return The texture manager
     *
     * @example
     *
     *          <pre>{@code
     * TextureManager textureManager = RenderHelper.getTextureManager();
     * }</pre>
     */
    @NotNull
    public static TextureManager getTextureManager() {
        return getClient().getTextureManager();
    }


}
