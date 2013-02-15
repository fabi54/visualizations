package org.fabi.visualizations.tools.jfree;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.Range;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import org.fabi.visualizations.tools.gui.ColorTools;

/**
 * A custom version of <code>XYBlockRenderer</code>
 * optimalized for classifier output display.
 * Ena
 * 
 * @author Jan Fabian
 *
 */
public class Classifier2DRenderer extends XYBlockRenderer {
	
	private static final long serialVersionUID = -5834041800518968887L;

    protected ColorTools colorTools;

    /** Sets the limit size probability, which will be treated (rendered) as zero probability, ie. by rgb(255,255,255). */
    protected double lowerLimit;
    
    /** A fraction of 0..1 interval which will be covered by the gradient:
     * if 0.25 is lowerLimit and 0.75 is "upperLimit", gradientSpan = 0.5.
     * if 0 is lowerLimit and 1 is "upperLimit", gradientSpan = 1 */
    protected double gradientSpan;
    
    /**
     * If <code>true</code>, color gradient will be used to show the probabilities of all the displayed classes.
     * If <code>false</code>, only the color of the most probable class will be rendered for each area. 
     */
    protected boolean showConfidencies = true;

    /**
     * Width of each block (horizontal resolution of the renderer divided by horizontal resolution of the display).
     */
    protected double xBlockS;
    /**
     * Width of each block (vertical resolution of the renderer divided by vertical resolution of the display).
     */
    protected double yBlockS;

    /**
     * Improves the opacity of the color to match the probability
     * (the more probable the more opaque).
     */
    protected Color getColor(Color color, double probability) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = (int) (color.getAlpha() * probability);
        return new Color(r, g, b, a);
    }

    public Classifier2DRenderer(int seriesCount) {
        super();
        setLowerLimit(0);
        setUpperLimit(1);
		colorTools = new ColorTools();
		this.seriesCount = seriesCount;
    }
    
    protected int seriesCount;

    public void setColorProvider(ColorTools provider) {
        this.colorTools = provider;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }
    
    public void setShowConfidencies(boolean show) {
    	showConfidencies = show;
    }
    
    public boolean isShowConfidencies() {
    	return showConfidencies;
    }

    public void setLowerLimit(double limit) {
    	double upper = getUpperLimit();
        this.lowerLimit = limit;
        setUpperLimit(upper);
    }

    public double getUpperLimit() {
        return lowerLimit + 1 / gradientSpan;
    }

    public void setUpperLimit(double limit) {
        if (limit < lowerLimit) throw new IllegalArgumentException();
        gradientSpan = 1 / (limit - lowerLimit);
    }

    /**
     * Initializes the renderer. The dataset should contain <code>xprecision * yprecision</code>
     * items in each series.
     * (The initialization avoids setting renderer precision each time a item is rendered
     * and counting the precision from a dataset).
     * 
     * @param dataset dataset which will be rendered
     * @param xprecision desired horizontal resolution of the renderer
     * @param yprecision desired vertical resolution of the renderer
     * @return <code>true</code> for success, <code>false</code> otherwise
     */
    public boolean init(XYDataset dataset, int xprecision, int yprecision) {
        xBlockS = 0;
        yBlockS = 0;
        for (int i = 1; i < dataset.getSeriesCount(); i++) {
            if (xprecision * yprecision != dataset.getItemCount(i)) {
                return false;
            }
        }
        Range domainR = DatasetUtilities.findDomainBounds(dataset);
        Range rangeR = DatasetUtilities.findRangeBounds(dataset);
        if (domainR != null) xBlockS = domainR.getLength() / xprecision;
        if (rangeR != null) yBlockS = rangeR.getLength() / yprecision;
        return true;
    }
    
    /**
     * Initializes the renderer according to the current setup of the model
     * (precision of rendering). The displayed dataset should be equal
     * with the object passed to this method.
     * 
     * @param dataset adapter providing the <code>XYZDataset</code> interface
     */
    public void init(double xStep, double yStep) {
        xBlockS = xStep;
        yBlockS = yStep;
    }

    @Override
    public void drawItem(Graphics2D g2, XYItemRendererState state,
                 Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
                 ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
                 int series, int item, CrosshairState crosshairState, int pass) {
    	
             if (series != 0) {
            	 return; // only first series will be drawn and will contain other series' corresponding item as well
             }
             int seriesCnt = dataset.getSeriesCount();
             double xx0, yy0, xx1, yy1;
             if (domainAxis instanceof SymbolAxis) {
            	 double xval = dataset.getXValue(series, item);
            	 double x = domainAxis.valueToJava2D(xval, dataArea,
                         plot.getDomainAxisEdge());
            	 xx0 = domainAxis.valueToJava2D(xval - 1, dataArea,
                         plot.getDomainAxisEdge());
            	 xx1 = domainAxis.valueToJava2D(xval + 1, dataArea,
                         plot.getDomainAxisEdge());
            	 xx0 = x - Math.abs((x - xx0) / 2);
            	 xx1 = x + Math.abs((x - xx1) / 2);
             } else {
                 double x = dataset.getXValue(series, item) - (xBlockS / 2);
                 xx0 = domainAxis.valueToJava2D(x, dataArea,
                         plot.getDomainAxisEdge());
                 xx1 = domainAxis.valueToJava2D(x + xBlockS, dataArea, plot.getDomainAxisEdge())/* + 2*/;
             }
             if (rangeAxis instanceof SymbolAxis) {
            	 double yval = dataset.getYValue(series, item);
                 double y = rangeAxis.valueToJava2D(yval, dataArea,
                         plot.getRangeAxisEdge());
            	 yy0 = rangeAxis.valueToJava2D(yval - 1, dataArea,
                         plot.getRangeAxisEdge());
            	 yy1 = rangeAxis.valueToJava2D(yval + 1, dataArea,
                         plot.getRangeAxisEdge());
            	 yy0 = y - Math.abs((y - yy0) / 2);
            	 yy1 = y + Math.abs((y - yy1) / 2);
             } else {
                 double y = dataset.getYValue(series, item) - yBlockS / 2;
                 yy0 = rangeAxis.valueToJava2D(y, dataArea,
                         plot.getRangeAxisEdge());
                 yy1 = rangeAxis.valueToJava2D(y + yBlockS, dataArea, plot.getRangeAxisEdge())/* - 2*/;
             }
             double[] z = new double[seriesCnt];
             Color clr = Color.white;
             if (dataset instanceof XYZDataset) {
            	 for (int i = 0; i < seriesCnt; i++) {
            		 z[i] = ((XYZDataset) dataset).getZValue(i, item);
            	 }
             }
             if (showConfidencies) {
	             Color[] clrs = new Color[seriesCnt + 1];
                 for (int i = 0; i < dataset.getSeriesCount(); i++) {
                      if (z[i] <= lowerLimit) {
                    	  z[i] = 0;
                      }
                     else if (z[i] >= lowerLimit + 1 / gradientSpan) {
                    	 z[i] = 1;
                     }
                 	 clrs[i] = getColor(getSeriesColor(i), z[i]);
                 }
	             clrs[clrs.length - 1] = Color.WHITE;
	             clr = ColorHelper.mixColors(clrs);
             } else {       
            	 int maxIndex = 0;
            	 for (int i = 0; i < seriesCnt; i++) {
            		 if (z[i] > z[maxIndex]) {
            			 maxIndex = i;
            		 }
            	 }
            	 if (z[maxIndex] > lowerLimit) {
		             Color[] clrs = {getSeriesColor(series), Color.WHITE};
	            	 clr = ColorHelper.mixColors(clrs);
            	 } else {
            		 clr = Color.WHITE;
            	 }
             }
             Rectangle2D block;
             PlotOrientation orientation = plot.getOrientation();
             if (orientation.equals(PlotOrientation.HORIZONTAL)) {
                 block = new Rectangle2D.Double(Math.min(yy0, yy1),
                         Math.min(xx0, xx1), Math.abs(yy1 - yy0),
                         Math.abs(xx0 - xx1));
             }
             else {
                 block = new Rectangle2D.Double(Math.min(xx0, xx1),
                         Math.min(yy0, yy1), Math.abs(xx1 - xx0),
                         Math.abs(yy1 - yy0));
             }
             g2.setPaint(clr);
             g2.fill(block);
             g2.setStroke(new BasicStroke(1.0f));
             g2.draw(block);

             EntityCollection entities = state.getEntityCollection();
             if (entities != null) {
                 addEntity(entities, block, dataset, series, item, 0.0, 0.0);
             }

         }
    
    public Color getSeriesColor(int series) {
        //System.out.println("MODELSERIES " + series + " - " + colorTools.getColorFromRange((double) series / (double) (seriesCount - 1)));
        return colorTools.getColorFromRange((double) series / (double) (seriesCount - 1));
    	/*if (super.getSeriesPaint(series) instanceof Color) return (Color) super.getSeriesPaint(series);
    	return new Color(255,255,255);*/
    }
    
    /**
     * Returns empty collection.
     */
    @Override
    public LegendItemCollection getLegendItems() {
    	return new LegendItemCollection();
    }
    
    /**
     * Returns <code>null</code>.
     */
    @Override
    public LegendItem getLegendItem(int datasetIndex, int series) {
    	return null;
    }
}
