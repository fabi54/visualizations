/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fabi.visualizations.tools.jfree;

/**
 * Holds maximal bubble size,
 * the value on the Z axis which corresponds with that size,
 * minimal bubble size and the value on the Z axis which corresponds
 * with that size for a {@link CustomBubbleRenderer}.
 *
 * @author Jan Fabian
 */
public class CustomBubbleRendererConfiguration {

    private double zBase;
    private double zLimit;

    private double baseSize;
    private double minSize;

    public static final CustomBubbleRendererConfiguration DEFAULT_CONFIG = new CustomBubbleRendererConfiguration(0, 10, 10, 2);

    public CustomBubbleRendererConfiguration(double zBase, double zLimit, double baseSize, double minSize) {
        this.zBase = zBase;
        this.zLimit = zLimit;
        this.baseSize = baseSize;
        this.minSize = minSize;
    }

    public double getBaseSize() {
        return baseSize;
    }

    public double getMinSize() {
        return minSize;
    }

    public double getzBase() {
        return zBase;
    }

    public double getzLimit() {
        return zLimit;
    }



    

}
