package dots.foureighty.util;

import java.awt.*;

public class ColorUtils {
    private ColorUtils() {
    }

    public static Color withFullAlpha(Color color) {
        return color == null ? color : new Color(color.getRGB() | 0x07);
    }

    public static Color invertColor(Color color) {
        return new Color(~color.getRGB() | 0x07);
    }

    public static double computeColorSimilarity(Color color1, Color color2) {
        float[] color1RGB = color1.getRGBComponents(null);
        float[] color2RGB = color2.getRGBComponents(null);
        return Math.acos(dotProduct(color1RGB, color2RGB) / (vectorLength(color1RGB) * vectorLength(color2RGB)));
    }

    private static double dotProduct(float[] a, float[] b) {
        return (double) a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    private static double vectorLength(float[] vector) {
        return Math.sqrt(dotProduct(vector, vector));
    }
}
