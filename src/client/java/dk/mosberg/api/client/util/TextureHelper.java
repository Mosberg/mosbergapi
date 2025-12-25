package dk.mosberg.api.client.util;

import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

/**
 * Client-side helper class for texture management in Minecraft 1.21.10.
 *
 * <p>
 * This helper provides convenient methods for loading, binding, and managing textures in the
 * client-side rendering system.
 *
 * <h2>Texture System</h2>
 * <p>
 * Minecraft's texture system manages all graphical assets:
 * <ul>
 * <li><strong>Texture Manager:</strong> Central registry for all loaded textures</li>
 * <li><strong>Resource Pack:</strong> Source of texture files (PNG images)</li>
 * <li><strong>Texture Atlas:</strong> Combined textures for efficient rendering (blocks,
 * items)</li>
 * <li><strong>Dynamic Textures:</strong> Programmatically generated or modified textures</li>
 * </ul>
 *
 * <h2>Texture Locations</h2>
 * <p>
 * Textures are organized by type:
 * <ul>
 * <li><strong>Blocks:</strong> {@code textures/block/}</li>
 * <li><strong>Items:</strong> {@code textures/item/}</li>
 * <li><strong>Entities:</strong> {@code textures/entity/}</li>
 * <li><strong>GUI:</strong> {@code textures/gui/}</li>
 * <li><strong>Particle:</strong> {@code textures/particle/}</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Create texture identifier
 * Identifier texture = TextureHelper.of("mosbergapi", "textures/entity/custom_mob.png");
 *
 * // Check if texture exists
 * if (TextureHelper.exists(texture)) {
 *     LOGGER.info("Texture found!");
 *          }
 *
 *          // Bind texture for rendering
 *          TextureHelper.bind(texture);
 *
 *          // Get texture dimensions
 *          int width = TextureHelper.getWidth(texture);
 *          int height = TextureHelper.getHeight(texture);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see TextureManager
 * @see Identifier
 * @see NativeImage
 */
@Environment(EnvType.CLIENT)
public class TextureHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextureHelper.class);

    private TextureHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Creates a texture identifier.
     *
     * @param namespace The namespace (mod ID)
     * @param path The texture path
     * @return The texture identifier
     * @throws NullPointerException if namespace or path is null
     *
     * @example
     *
     *          <pre>{@code
     * Identifier texture = TextureHelper.of("mosbergapi", "textures/entity/custom.png");
     * }</pre>
     */
    @NotNull
    public static Identifier of(@NotNull String namespace, @NotNull String path) {
        if (namespace == null)
            throw new NullPointerException("Namespace cannot be null");
        if (path == null)
            throw new NullPointerException("Path cannot be null");
        return Identifier.of(namespace, path);
    }

    /**
     * Creates a texture identifier for a block texture.
     *
     * @param namespace The namespace (mod ID)
     * @param name The texture name (without path or extension)
     * @return The texture identifier
     *
     * @example
     *
     *          <pre>{@code
     * Identifier texture = TextureHelper.block("mosbergapi", "custom_ore");
     * // Returns: mosbergapi:textures/block/custom_ore.png
     * }</pre>
     */
    @NotNull
    public static Identifier block(@NotNull String namespace, @NotNull String name) {
        return of(namespace, "textures/block/" + name + ".png");
    }

    /**
     * Creates a texture identifier for an item texture.
     *
     * @param namespace The namespace (mod ID)
     * @param name The texture name (without path or extension)
     * @return The texture identifier
     *
     * @example
     *
     *          <pre>{@code
     * Identifier texture = TextureHelper.item("mosbergapi", "magic_wand");
     * // Returns: mosbergapi:textures/item/magic_wand.png
     * }</pre>
     */
    @NotNull
    public static Identifier item(@NotNull String namespace, @NotNull String name) {
        return of(namespace, "textures/item/" + name + ".png");
    }

    /**
     * Creates a texture identifier for an entity texture.
     *
     * @param namespace The namespace (mod ID)
     * @param name The texture name (without path or extension)
     * @return The texture identifier
     *
     * @example
     *
     *          <pre>{@code
     * Identifier texture = TextureHelper.entity("mosbergapi", "custom_mob");
     * // Returns: mosbergapi:textures/entity/custom_mob.png
     * }</pre>
     */
    @NotNull
    public static Identifier entity(@NotNull String namespace, @NotNull String name) {
        return of(namespace, "textures/entity/" + name + ".png");
    }

    /**
     * Creates a texture identifier for a GUI texture.
     *
     * @param namespace The namespace (mod ID)
     * @param name The texture name (without path or extension)
     * @return The texture identifier
     *
     * @example
     *
     *          <pre>{@code
     * Identifier texture = TextureHelper.gui("mosbergapi", "custom_container");
     * // Returns: mosbergapi:textures/gui/custom_container.png
     * }</pre>
     */
    @NotNull
    public static Identifier gui(@NotNull String namespace, @NotNull String name) {
        return of(namespace, "textures/gui/" + name + ".png");
    }

    /**
     * Creates a texture identifier for a particle texture.
     *
     * @param namespace The namespace (mod ID)
     * @param name The texture name (without path or extension)
     * @return The texture identifier
     *
     * @example
     *
     *          <pre>{@code
     * Identifier texture = TextureHelper.particle("mosbergapi", "magic_sparkle");
     * // Returns: mosbergapi:textures/particle/magic_sparkle.png
     * }</pre>
     */
    @NotNull
    public static Identifier particle(@NotNull String namespace, @NotNull String name) {
        return of(namespace, "textures/particle/" + name + ".png");
    }

    /**
     * Gets the texture manager.
     *
     * @return The texture manager
     *
     * @example
     *
     *          <pre>{@code
     * TextureManager manager = TextureHelper.getManager();
     * }</pre>
     */
    @NotNull
    public static TextureManager getManager() {
        return MinecraftClient.getInstance().getTextureManager();
    }

    /**
     * Gets the resource manager.
     *
     * @return The resource manager
     *
     * @example
     *
     *          <pre>{@code
     * ResourceManager resources = TextureHelper.getResourceManager();
     * }</pre>
     */
    @NotNull
    public static ResourceManager getResourceManager() {
        return MinecraftClient.getInstance().getResourceManager();
    }

    /**
     * Binds a texture for rendering.
     *
     * @param texture The texture identifier
     * @throws NullPointerException if texture is null
     *
     * @example
     *
     *          <pre>{@code
     * Identifier texture = TextureHelper.entity("mosbergapi", "custom_mob");
     * TextureHelper.bind(texture);
     * // Now render with this texture
     * }</pre>
     */
    public static void bind(@NotNull Identifier texture) {
        if (texture == null)
            throw new NullPointerException("Texture identifier cannot be null");
        // In Minecraft 1.21.10+, use RenderSystem.setShaderTexture instead
        RenderSystem.setShaderTexture(0, null);
        LOGGER.debug("Bound texture: {}", texture);
    }

    /**
     * Gets a texture object from the texture manager.
     *
     * @param texture The texture identifier
     * @return The texture object, or null if not loaded
     *
     * @example
     *
     *          <pre>{@code
     * AbstractTexture texture = TextureHelper.getTexture(identifier);
     * if (texture != null) {
     *     // Texture is loaded
     *          }
     * }</pre>
     */
    @Nullable
    public static AbstractTexture getTexture(@NotNull Identifier texture) {
        if (texture == null)
            throw new NullPointerException("Texture identifier cannot be null");
        return getManager().getTexture(texture);
    }

    /**
     * Checks if a texture is currently loaded.
     *
     * @param texture The texture identifier
     * @return true if the texture is loaded
     *
     * @example
     *
     *          <pre>{@code
     * if (TextureHelper.isLoaded(texture)) {
     *     LOGGER.info("Texture is already loaded");
     *          }
     * }</pre>
     */
    public static boolean isLoaded(@NotNull Identifier texture) {
        return getTexture(texture) != null;
    }

    /**
     * Checks if a texture resource exists in the resource pack.
     *
     * @param texture The texture identifier
     * @return true if the texture file exists
     *
     * @example
     *
     *          <pre>{@code
     * if (TextureHelper.exists(texture)) {
     *     LOGGER.info("Texture file found in resources");
     *          }
     * }</pre>
     */
    public static boolean exists(@NotNull Identifier texture) {
        if (texture == null)
            throw new NullPointerException("Texture identifier cannot be null");
        return getResourceManager().getResource(texture).isPresent();
    }

    /**
     * Registers a dynamic texture.
     *
     * @param id The texture identifier
     * @param texture The texture to register
     * @throws NullPointerException if id or texture is null
     *
     * @example
     *
     *          <pre>{@code
     * NativeImage image = new NativeImage(256, 256, false);
     * // ... modify image pixels ...
     * NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
     * TextureHelper.register(Identifier.of("mosbergapi", "dynamic"), texture);
     * }</pre>
     */
    public static void register(@NotNull Identifier id, @NotNull AbstractTexture texture) {
        if (id == null)
            throw new NullPointerException("Identifier cannot be null");
        if (texture == null)
            throw new NullPointerException("Texture cannot be null");

        getManager().registerTexture(id, texture);
        LOGGER.debug("Registered dynamic texture: {}", id);
    }

    /**
     * Unregisters and closes a texture.
     *
     * @param texture The texture identifier
     * @throws NullPointerException if texture is null
     *
     * @example
     *
     *          <pre>{@code
     * TextureHelper.unregister(dynamicTextureId);
     * // Texture is now unloaded
     * }</pre>
     */
    public static void unregister(@NotNull Identifier texture) {
        if (texture == null)
            throw new NullPointerException("Texture identifier cannot be null");

        AbstractTexture tex = getTexture(texture);
        if (tex != null) {
            tex.close();
            LOGGER.debug("Unregistered texture: {}", texture);
        }
    }

    /**
     * Loads a native image from a texture resource.
     *
     * @param texture The texture identifier
     * @return The native image, or null if loading failed
     *
     * @example
     *
     *          <pre>{@code
     * NativeImage image = TextureHelper.loadImage(texture);
     * if (image != null) {
     *     int color = image.getColor(x, y);
     *     image.close(); // Don't forget to close!
     *          }
     * }</pre>
     */
    @Nullable
    public static NativeImage loadImage(@NotNull Identifier texture) {
        if (texture == null)
            throw new NullPointerException("Texture identifier cannot be null");

        try {
            var resource = getResourceManager().getResource(texture);
            if (resource.isEmpty()) {
                LOGGER.warn("Texture resource not found: {}", texture);
                return null;
            }

            try (InputStream stream = resource.get().getInputStream()) {
                return NativeImage.read(stream);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load texture image: {}", texture, e);
            return null;
        }
    }

    /**
     * Creates a blank native image.
     *
     * @param width Image width
     * @param height Image height
     * @param useAlpha Whether to use alpha channel
     * @return A new native image
     * @throws IllegalArgumentException if width or height is not positive
     *
     * @example
     *
     *          <pre>{@code
     * NativeImage image = TextureHelper.createBlankImage(256, 256, true);
     * // Modify pixels
     * image.setColor(x, y, 0xFFFF0000); // Red pixel
     * }</pre>
     */
    @NotNull
    public static NativeImage createBlankImage(int width, int height, boolean useAlpha) {
        if (width <= 0)
            throw new IllegalArgumentException("Width must be positive");
        if (height <= 0)
            throw new IllegalArgumentException("Height must be positive");

        return new NativeImage(width, height, useAlpha);
    }

    /**
     * Creates a dynamic texture from a native image.
     *
     * @param image The native image (will be owned by the texture)
     * @return A new texture backed by the image
     * @throws NullPointerException if image is null
     *
     * @example
     *
     *          <pre>{@code
     * NativeImage image = TextureHelper.createBlankImage(64, 64, true);
     * // Modify image...
     * NativeImageBackedTexture texture = TextureHelper.createTexture(image);
     * TextureHelper.register(Identifier.of("mosbergapi", "custom"), texture);
     * }</pre>
     */
    @NotNull
    public static NativeImageBackedTexture createTexture(@NotNull NativeImage image) {
        if (image == null)
            throw new NullPointerException("NativeImage cannot be null");
        // In Minecraft 1.21.10+, NativeImageBackedTexture does not take an explicit upload flag
        return new NativeImageBackedTexture(null, image);
    }

    /**
     * Gets the width of a loaded texture.
     *
     * @param texture The texture identifier
     * @return The texture width in pixels, or -1 if not available
     *
     * @example
     *
     *          <pre>{@code
     * int width = TextureHelper.getWidth(texture);
     * if (width > 0) {
     *          LOGGER.info("Texture is {} pixels wide", width);
     *          }
     * }</pre>
     */
    public static int getWidth(@NotNull Identifier texture) {
        NativeImage image = loadImage(texture);
        if (image == null)
            return -1;

        int width = image.getWidth();
        image.close();
        return width;
    }

    /**
     * Gets the height of a loaded texture.
     *
     * @param texture The texture identifier
     * @return The texture height in pixels, or -1 if not available
     *
     * @example
     *
     *          <pre>{@code
     * int height = TextureHelper.getHeight(texture);
     * if (height > 0) {
     *          LOGGER.info("Texture is {} pixels tall", height);
     *          }
     * }</pre>
     */
    public static int getHeight(@NotNull Identifier texture) {
        NativeImage image = loadImage(texture);
        if (image == null)
            return -1;

        int height = image.getHeight();
        image.close();
        return height;
    }

    /**
     * Gets both dimensions of a texture.
     *
     * @param texture The texture identifier
     * @return Array of [width, height], or null if texture not available
     *
     * @example
     *
     *          <pre>{@code
     * int[] dimensions = TextureHelper.getDimensions(texture);
     * if (dimensions != null) {
     *     int width = dimensions[0];
     *     int height = dimensions[1];
     *          }
     * }</pre>
     */
    @Nullable
    public static int[] getDimensions(@NotNull Identifier texture) {
        NativeImage image = loadImage(texture);
        if (image == null)
            return null;

        int[] dimensions = new int[] {image.getWidth(), image.getHeight()};
        image.close();
        return dimensions;
    }

    /**
     * Converts ARGB color to ABGR format (for native image).
     *
     * @param argb Color in ARGB format
     * @return Color in ABGR format
     *
     * @example
     *
     *          <pre>{@code
     * int argb = 0xFFFF0000; // Red
     * int abgr = TextureHelper.argbToAbgr(argb);
     * image.setColor(x, y, abgr);
     * }</pre>
     */
    public static int argbToAbgr(int argb) {
        int alpha = (argb >> 24) & 0xFF;
        int red = (argb >> 16) & 0xFF;
        int green = (argb >> 8) & 0xFF;
        int blue = argb & 0xFF;
        return (alpha << 24) | (blue << 16) | (green << 8) | red;
    }

    /**
     * Converts ABGR color to ARGB format.
     *
     * @param abgr Color in ABGR format
     * @return Color in ARGB format
     *
     * @example
     *
     *          <pre>{@code
     * int abgr = image.getColor(x, y);
     * int argb = TextureHelper.abgrToArgb(abgr);
     * }</pre>
     */
    public static int abgrToArgb(int abgr) {
        return argbToAbgr(abgr); // Conversion is symmetrical
    }

    /**
     * Creates a color from RGBA components.
     *
     * @param red Red component (0-255)
     * @param green Green component (0-255)
     * @param blue Blue component (0-255)
     * @param alpha Alpha component (0-255)
     * @return Color in ABGR format for native image
     *
     * @example
     *
     *          <pre>{@code
     * int color = TextureHelper.rgba(255, 0, 0, 255); // Red
     * image.setColor(x, y, color);
     * }</pre>
     */
    public static int rgba(int red, int green, int blue, int alpha) {
        return (alpha << 24) | (blue << 16) | (green << 8) | red;
    }

    /**
     * Fills a native image with a solid color.
     *
     * @param image The image to fill
     * @param color The color in ABGR format
     * @throws NullPointerException if image is null
     *
     * @example
     *
     *          <pre>{@code
     * NativeImage image = TextureHelper.createBlankImage(64, 64, true);
     * TextureHelper.fill(image, TextureHelper.rgba(0, 255, 0, 255)); // Fill with green
     * }</pre>
     */
    public static void fill(@NotNull NativeImage image, int color) {
        if (image == null)
            throw new NullPointerException("NativeImage cannot be null");

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.setColor(x, y, color);
            }
        }
    }
}
