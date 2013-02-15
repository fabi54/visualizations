/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fabi.visualizations.tools.jfree;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import org.fabi.visualizations.tools.gui.ColorTools;

/**
 * Custom configurable implementation of {@link XYBubbleRenderer}.
 *
 * @author Jan Fabian
 */
public class CustomBubbleRenderer extends XYBubbleRenderer {

	private static final long serialVersionUID = -7341702404843887311L;
	
	CustomBubbleRendererConfiguration configuration;
        protected ColorTools colorTools;
    
    private Item highlight;
    protected int seriesCount;
    
    public class Item {
    	public int series;
    	public int item;
    	
    	public Item(int series, int item ){
    		this.series = series;
    		this.item = item;
    	}
    }
    
    public void highlightItem(int series, int item) {
    	highlight = new Item(series, item);
    }
    
    public void setNullHighlightedItem() {
    	highlight = null;
    }
    
    public Item getHighlitedItem() {
    	return highlight;
    }

    public CustomBubbleRenderer(int seriesCount) {
        super(SCALE_ON_BOTH_AXES);
        configuration = CustomBubbleRendererConfiguration.DEFAULT_CONFIG;
        setNullHighlightedItem();
        colorTools = new ColorTools();
        this.seriesCount = seriesCount;
    }

    public void autoset(XYDataset dataset, double baseSize) { 
    	if (dataset instanceof XYZDataset) {
	        double minz = Double.MAX_VALUE;
	        double maxz = - Double.MAX_VALUE;
	        for (int series = 0; series < dataset.getSeriesCount(); series++) {
	            for (int item = 0; item < dataset.getItemCount(series); item++) {
	                double z = ((XYZDataset) dataset).getZValue(series, item);
	                minz = Math.min(minz, z);
	                maxz = Math.max(maxz, z);
	            }
	        }
	        configuration = new CustomBubbleRendererConfiguration(minz, maxz, 
	                baseSize, 2);
    	} // TODO else
		if (dataset.getSeriesCount() == 1) {
			Paint color = Color.red;
			setSeriesPaint(0, color);
			setSeriesFillPaint(0, color);
		} else {
			for (int j = 0; j < dataset.getSeriesCount(); j++) {
				ColorTools colorProvider = new ColorTools();
				Paint color = colorProvider.getColorFromRange((double) j
							/ (double) (dataset.getSeriesCount() - 1));
				setSeriesPaint(j, color);
				setSeriesFillPaint(j, color);
			}
		}
    }

    public void setConfiguration(CustomBubbleRendererConfiguration configuration) {
        this.configuration = configuration;
    }

    public CustomBubbleRendererConfiguration getConfiguration() {
        return configuration;
    }
    
    public Color getSeriesColor(int series) {
        //System.out.println("DATASERIES " + series + " - " + colorTools.getColorFromRange((double) series / (double) (seriesCount - 1)));
        return colorTools.getColorFromRange((double) series / (double) (seriesCount - 1));
    	/*if (super.getSeriesPaint(series) instanceof Color) return (Color) super.getSeriesPaint(series);
    	return new Color(255,255,255);*/
    }
    
   @Override
   public Paint getItemOutlinePaint(int series, int item) {
       if (isHighlighted(series, item)) {
      	 return Color.YELLOW;
       }
	   return super.getItemOutlinePaint(series, item);
   }
    
   @Override
   public Stroke getItemOutlineStroke(int series, int item) {
       if (isHighlighted(series, item)) {
      	 return new BasicStroke(2);
       }
	   return super.getItemOutlineStroke(series, item);
   }
   
   private boolean isHighlighted(int series, int item) {
	   return (highlight != null && highlight.series == series && highlight.item == item);
   }
   
    @Override
   public void drawItem(Graphics2D  g2, XYItemRendererState state,
             Rectangle2D  dataArea, PlotRenderingInfo info, XYPlot plot,
             ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
             int series, int item, CrosshairState crosshairState, int pass) {    	
    	
        PlotOrientation orientation = plot.getOrientation();

        double x = dataset.getXValue(series, item);
        double y = dataset.getYValue(series, item);
        double z = 0;

        double stepSize = Math.abs((configuration.getBaseSize() - configuration.getMinSize()) / configuration.getzLimit());
        if (configuration.getzLimit() == 0) {
        	stepSize = 0;
        }
        
        if (dataset instanceof XYZDataset) {
            XYZDataset xyzData = (XYZDataset) dataset;
            z = xyzData.getZValue(series, item);
        }
        if (configuration.getzLimit() > 0) {
            if (z < configuration.getzBase() - configuration.getzLimit() || z > configuration.getzBase() + configuration.getzLimit()) return;
        }

        if (!Double.isNaN(z)) {
            RectangleEdge domainAxisLocation = plot.getDomainAxisEdge();
            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();
            double transX = domainAxis.valueToJava2D(x, dataArea,
                domainAxisLocation);
            double transY = rangeAxis.valueToJava2D(y, dataArea,
                rangeAxisLocation);

             double size = Math.abs(configuration.getBaseSize() - Math.abs(Math.abs(z - configuration.getzBase()) * stepSize));
             Ellipse2D  circle = null;
             if (orientation == PlotOrientation.VERTICAL) {
                 circle = new Ellipse2D.Double (transX - size / 2.0,
                         transY - size / 2.0, size, size);
             }
             else if (orientation == PlotOrientation.HORIZONTAL) {
                 circle = new Ellipse2D.Double (transY - size / 2.0,
                         transX - size / 2.0, size, size);
             }
             g2.setPaint(getSeriesColor(series));
             g2.fill(circle);
             g2.setStroke(getItemOutlineStroke(series, item));
             g2.setPaint(getItemOutlinePaint(series, item));
             g2.draw(circle);

             if (isItemLabelVisible(series, item)) {
                 if (orientation == PlotOrientation.VERTICAL) {
                     drawItemLabel(g2, orientation, dataset, series, item,
                             transX, transY, false);
                 }
                 else if (orientation == PlotOrientation.HORIZONTAL) {
                     drawItemLabel(g2, orientation, dataset, series, item,
                             transY, transX, false);
                 }
             }

             // setup for collecting optional entity info...
             EntityCollection entities = null;
             if (info != null) {
                 entities = info.getOwner().getEntityCollection();
             }

             // add an entity for the item...
             if (entities != null) {
                 String  tip = null;
                 XYToolTipGenerator generator
                     = getToolTipGenerator(series, item);
                 if (generator != null) {
                     tip = generator.generateToolTip(dataset, series, item);
                 }
                 String  url = null;
                 if (getURLGenerator() != null) {
                     url = getURLGenerator().generateURL(dataset, series, item);
                 }
                 XYItemEntity entity = new XYItemEntity(circle, dataset, series,
                         item, tip, url);
                 entities.add(entity);
             }
         }

     }
    
    public Paint getItemPaint(int series, int item) {
    	return getSeriesColor(series);
    }

}
