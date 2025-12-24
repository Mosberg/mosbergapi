package dk.mosberg.api.client.util;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Helper for client-side rendering
 */
public class RenderHelper {

    public static void drawCenteredText(MatrixStack matrices, Text text, int x, int y, int color) {
        // Render centered text
    }

    public static void drawTexture(MatrixStack matrices, Identifier texture, int x, int y,
            int width, int height) {
        // Render texture
    }

    public static void drawGradient(MatrixStack matrices, int x, int y, int width, int height,
            int colorStart, int colorEnd) {
        // Draw gradient rectangle
    }
}
