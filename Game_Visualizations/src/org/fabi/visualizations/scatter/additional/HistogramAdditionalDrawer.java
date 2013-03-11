package org.fabi.visualizations.scatter.additional;

import java.awt.Color;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.math.plot.render.AbstractDrawer;

public class HistogramAdditionalDrawer implements AdditionalDrawer {

	protected double[][] values;
	protected double width;
	protected String name;
	protected Color c;
	protected ScatterplotVisualization visualization;
	protected double relativeHeight;
	
	protected double max;
	
	public HistogramAdditionalDrawer(ScatterplotVisualization visualization, String name, Color c, double[][] values, double width, double relativeHeight) {
		this.values = values;
		this.name = name;
		this.c = c;
		this.width = width;
		this.visualization = visualization;
		this.relativeHeight = relativeHeight;
		
		max = 0;
		
		for (int i = 0; i < values.length; i++) { // TODO inefficient maximum computation
			if (values[i][1] > 0) {
				max = Math.max(values[i][1], max);
			}
			if (max == 0) {
				max = 1.0;
			}
		}
	}
	
	@Override
	public void draw(AbstractDrawer draw) {
		visualization.getVisualizationAsComponent();
		double[][] bounds = visualization.getActualAxesBounds();
		double axis = bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.LOWER_BOUND];
		double coef = ((bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.UPPER_BOUND]
				- bounds[ScatterplotVisualization.Y_AXIS][ScatterplotVisualization.LOWER_BOUND]) * relativeHeight / max);
		draw.setColor(c);
		for (int i = 0; i < values.length; i++) {
			draw.drawPolygon(new double[]{values[i][0] - (width / 2.0), axis},
							 new double[]{values[i][0] + (width / 2.0), axis},
							 new double[]{values[i][0] + (width / 2.0), axis + coef * values[i][1]},
							 new double[]{values[i][0] - (width / 2.0), axis + coef * values[i][1]});
			draw.fillPolygon(0.33f, new double[]{values[i][0] - (width / 2.0), axis},
					 new double[]{values[i][0] + (width / 2.0), axis},
					 new double[]{values[i][0] + (width / 2.0), axis + coef * values[i][1]},
					 new double[]{values[i][0] - (width / 2.0), axis + coef * values[i][1]});
		}
	}

}
