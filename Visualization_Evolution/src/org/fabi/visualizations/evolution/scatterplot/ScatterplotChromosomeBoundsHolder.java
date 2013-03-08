package org.fabi.visualizations.evolution.scatterplot;

import org.fabi.visualizations.tools.math.Arrays;

public class ScatterplotChromosomeBoundsHolder {
	
	public double[][] bounds;
	
	public ScatterplotChromosomeBoundsHolder(double[][] inputs) {
		bounds = Arrays.getBasicStats(inputs);
	}
}
