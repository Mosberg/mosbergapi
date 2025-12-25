package dk.mosberg.api.client.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * Client-side helper class for working with screens (GUIs) in Minecraft 1.21.10.
 *
 * <p>
 * And screen-related operations.
 *
 * <h2>Screen System</h2>
 * <p>
 * Screens represent all GUI interfaces in Minecraft:
 * </p>
 * <ul>
 * <li><strong>Screen Class:</strong> Base class for all GUI screens</li>
 * <li><strong>DrawContext:</strong> Modern rendering context for GUIs (1.20+)</li>
 * <li><strong>Widgets:</strong> Interactive elements (buttons, text fields, sliders)</li>
 * <li><strong>Tooltips:</strong> Hover text displayed on widgets</li>
 * </ul>
 *
 * <h2>Screen Types</h2>
 * <ul>
 * <li><strong>Handled Screens:</strong> Screens with server-side containers</li>
 * <li><strong>Simple Screens:</strong> Client-only GUIs (menus, settings)</li>
 * <li><strong>HUD:</strong> In-game overlay (health, hunger, hotbar)</li>
 * </ul>
 *
 * <h2>DrawContext (1.20+)</h2>
 * <p>
 * Modern GUI rendering uses DrawContext for all drawing operations:
 * </p>
 * <ul>
 * <li>Automatic matrix stack management</li>
 * <li>Item rendering with tooltips</li>
 * </ul>
 *
 * @example
 *
 *          <pre>{@code
 * // Get current screen
 * Screen current = ScreenHelper.getCurrentScreen();
 *
 * // Open a screen
 * ScreenHelper.open(new CustomScreen());
 *
 * // Close current screen
 * ScreenHelper.close();
 *
 * // Draw texture in screen render method
 * ScreenHelper.drawTexture(context, texture, x, y, width, height);
 *
 * // Draw text
 * ScreenHelper.drawText(context, "Hello!", x, y, 0xFFFFFF);
 * }</pre>
 *
 * @author Mosberg
 * @version 1.0.0
 * @since 1.0.0
 * @see Screen
 * @see DrawContext
 */
@Environment(EnvType.CLIENT)
public class ScreenHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenHelper.class);

    private ScreenHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Gets the currently open screen.
     *
     * @return The current screen, or null if no screen is open
     *
     * @example
     *
     *          <pre>{@code
     * Screen current = ScreenHelper.getCurrentScreen();
     * if (current != null) {
     *
     *          LOGGER.info("Screen: {}", current.getTitle().getString());
     *          }
     * }</pre>
     */
    @Nullable
    public static Screen getCurrentScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    /**
     * Opens a screen.
     *
     * @param screen The screen to open
     *
     * @example
     *
     *          <pre>{@code
     * ScreenHelper.open(new CustomConfigScreen());
     * }</pre>
     */
    public static void open(@Nullable Screen screen) {
        MinecraftClient.getInstance().setScreen(screen);
        if (screen != null) {
            LOGGER.debug("Opened screen: {}", screen.getTitle().getString());
        }
    }

    /**
     * Closes the current screen.
     *
     * @example
     *
     *          <pre>{@code
     * ScreenHelper.close();
     * // Returns to game or previous screen
     * }</pre>
     */
    public static void close() {
        open(null);
        LOGGER.debug("Closed current screen");
    }

    /**
     * Checks if a screen is currently open.
     *
     * @return true if any screen is open
     *
     * @example
     *
     *          <pre>{@code
     * if (ScreenHelper.isScreenOpen()) {
     *     LOGGER.info("A GUI is currently open");
     *          }
     * }</pre>
     */
    public static boolean isScreenOpen() {
        return getCurrentScreen() != null;
    }

    /**
     * Checks if a specific screen type is currently open.
     *
     * @param screenClass The screen class to check
     * @return true if a screen of this type is open
     *
     * @example
     *
     *          <pre>{@code
     * if (ScreenHelper.isScreenOpen(InventoryScreen.class)) {
     *     LOGGER.info("Inventory is open");
     *          }
     * }</pre>
     */
    public static boolean isScreenOpen(@NotNull Class<? extends Screen> screenClass) {
        if (screenClass == null)
            throw new NullPointerException("Screen class cannot be null");
        Screen current = getCurrentScreen();
        return current != null && screenClass.isInstance(current);
    }

    /**
     * Gets the title of the current screen.
     *
     * @return The screen title, or null if no screen is open
     *
     * @example
     *
     *          <pre>{@code
     * Text title = ScreenHelper.getTitle();
     * if (title != null) {
     *          LOGGER.info("Screen title: {}", title.getString());
     *          }
     * }</pre>
     */
    @Nullable
    public static Text getTitle() {
        Screen screen = getCurrentScreen();
        return screen != null ? screen.getTitle() : null;
    }


    /**
     * Draws text on the screen.
     *
     * @param context The draw context
     * @param text The text to draw
     * @param x Screen X position
     * @param y Screen Y position
     * @param color Text color (ARGB format)
     * @throws NullPointerException if context or text is null
     *
     * @example
     *
     *          <pre>{@code
     * ScreenHelper.drawText(context, "Hello World!", 10, 10, 0xFFFFFF);
     * }</pre>
     */
    public static void drawText(@NotNull DrawContext context, @NotNull String text, int x, int y,
            int color) {
        if (context == null)
            throw new NullPointerException("DrawContext cannot be null");
        if (text == null)
            throw new NullPointerException("Text cannot be null");

        context.drawText(MinecraftClient.getInstance().textRenderer, text, x, y, color, false);
    }

    /**
     * Draws centered text on the screen.
     *
     * @param context The draw context
     * @param text The text to draw
     * @param centerX Center X position
     * @param y Screen Y position
     * @param color Text color (ARGB format)
     * @throws NullPointerException if context or text is null
     *
     * @example
     *
     *          <pre>{@code
     * int screenWidth = ScreenHelper.getScaledWidth();
     * ScreenHelper.drawCenteredText(context, "Centered!", screenWidth / 2, 10, 0xFFFFFF);
     * }</pre>
     */
    public static void drawCenteredText(@NotNull DrawContext context, @NotNull String text,
            int centerX, int y, int color) {
        if (context == null)
            throw new NullPointerException("DrawContext cannot be null");
        if (text == null)
            throw new NullPointerException("Text cannot be null");

        var textRenderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = textRenderer.getWidth(text);
        context.drawText(textRenderer, text, centerX - textWidth / 2, y, color, false);
    }

    /**
     * Draws a colored rectangle.
     *
     * @param context The draw context
     * @param x1 Left X position
     * @param y1 Top Y position
     * @param x2 Right X position
     * @param y2 Bottom Y position
     * @param color Fill color (ARGB format)
     * @throws NullPointerException if context is null
     *
     * @example
     *
     *          <pre>{@code
     * // Draw a semi-transparent black background
     * ScreenHelper.drawRectangle(context, 10, 10, 100, 50, 0x80000000);
     * }</pre>
     */
    public static void drawRectangle(@NotNull DrawContext context, int x1, int y1, int x2, int y2,
            int color) {
        if (context == null)
            throw new NullPointerException("DrawContext cannot be null");
        context.fill(x1, y1, x2, y2, color);
    }

    /**
     * Draws a rectangle outline.
     *
     * @param context The draw context
     * @param x Screen X position
     * @param y Screen Y position
     * @param width Rectangle width
     * @param height Rectangle height
     * @param color Border color (ARGB format)
     * @param thickness Border thickness
     * @throws NullPointerException if context is null
     *
     * @example
     *
     *          <pre>{@code
     * // Draw a white border around an area
     * ScreenHelper.drawBorder(context, 10, 10, 100, 50, 0xFFFFFFFF, 2);
     * }</pre>
     */


    public static void drawBorder(@NotNull DrawContext context, int x, int y, int width, int height,
            int color, int thickness) {
        if (context == null)
            throw new NullPointerException("DrawContext cannot be null");

        // Top
        context.fill(x, y, x + width, y + thickness, color);
        // Bottom
        context.fill(x, y + height - thickness, x + width, y + height, color);
        // Left
        context.fill(x, y, x + thickness, y + height, color);
        // Right
        context.fill(x + width - thickness, y, x + width, y + height, color);
    }

    /**
     * Gets the text renderer.
     *
     * @return The text renderer
     *
     * @example
     *
     *          <pre>{@code
     * var textRenderer = ScreenHelper.getTextRenderer();
     * int textWidth = textRenderer.getWidth("Hello");
     * }</pre>
     */
    @NotNull
    public static net.minecraft.client.font.TextRenderer getTextRenderer() {
        return MinecraftClient.getInstance().textRenderer;
    }

    /**
     * Gets the width of text as it would be rendered.
     *
     * @param text The text to measure
     * @return The width in pixels
     * @throws NullPointerException if text is null
     *
     * @example
     *
     *          <pre>{@code
     * int width = ScreenHelper.getTextWidth("Hello World!");
     *          LOGGER.info("Text is {} pixels wide", width);
     * }</pre>
     */
    public static int getTextWidth(@NotNull String text) {
        if (text == null)
            throw new NullPointerException("Text cannot be null");
        return getTextRenderer().getWidth(text);
    }

    /**
     * Gets the height of the font.
     *
     * @return The font height in pixels
     *
     * @example
     *
     *          <pre>{@code
     * int height = ScreenHelper.getTextHeight();
     * // Usually returns 9 for default font
     * }</pre>
     */
    public static int getTextHeight() {
        return getTextRenderer().fontHeight;
    }

    /**
     * Gets the scaled window width (GUI coordinates).
     *
     * @return The scaled width
     *
     * @example
     *
     *          <pre>{@code
     * int width = ScreenHelper.getScaledWidth();
     * int centerX = width / 2;
     * }</pre>
     */
    public static int getScaledWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    /**
     * Gets the scaled window height (GUI coordinates).
     *
     *
     * @return The scaled height
     *
     * @example
     *
     *          <pre>{@code
     * int height = ScreenHelper.getScaledHeight();
     * int centerY = height / 2;
     * }</pre>
     */
    public static int getScaledHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }


}
