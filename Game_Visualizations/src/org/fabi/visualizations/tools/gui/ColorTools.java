
package org.fabi.visualizations.tools.gui;

import java.awt.Color;

public class ColorTools {
    
        public Color getLowerColor() {
            return Color.BLUE;
        }
    
        public Color getUpperColor() {
            return Color.RED;
        }
        
        public Color getColorFromRange(double value) {
            return getPointColor(value, 255);
        }
    
        public Color getPointColor(double value, int alpha) {
        if (Double.isNaN(value))
            return Color.LIGHT_GRAY;
        Color MIN_LEGEND_COLOR = getLowerColor();
        Color MAX_LEGEND_COLOR = getUpperColor();
        float[] minCol = Color.RGBtoHSB(MIN_LEGEND_COLOR.getRed(), MIN_LEGEND_COLOR.getGreen(), MIN_LEGEND_COLOR.getBlue(), null);
        float[] maxCol = Color.RGBtoHSB(MAX_LEGEND_COLOR.getRed(), MAX_LEGEND_COLOR.getGreen(), MAX_LEGEND_COLOR.getBlue(), null);
        //double hColorDiff = 1.0f - 0.68f;
        double hColorDiff = maxCol[0] - minCol[0];
        double sColorDiff = maxCol[1] - minCol[1];
        double bColorDiff = maxCol[2] - minCol[2];

        Color color = new Color(Color.HSBtoRGB((float)(minCol[0] + hColorDiff * value), (float)(minCol[1] + value * sColorDiff), (float)(minCol[2] + value * bColorDiff)));

        if (alpha < 255)
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        return color;
    }
}
