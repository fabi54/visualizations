package org.fabi.visualizations.tools.jfree;

import java.awt.Color;

/**
 * Provides methods for color manipulations.
 * 
 * @author Jan Fabian
 *
 */
public abstract class ColorHelper {
	
	/**
	 * Mixes the passed colors.
	 * 
	 * @param colors array containing colors to be mixed.
	   Rate of each color must be specified by its alpha channel.
	 * @return resulting color
	 */
    public static Color mixColors(Color[] colors) {
        int ra = 0, ga = 0, ba = 0, b = 0;
        for (Color c : colors) {
            ra += c.getRed() * c.getAlpha();
            ga += c.getGreen() * c.getAlpha();
            ba += c.getBlue() * c.getAlpha();
            b += c.getAlpha();
        }
        if (b == 0) {
        	// when result is nothing, returns white
        	return Color.WHITE;
        }
        return new Color(ra / b, ga / b, ba / b);
    }
}
