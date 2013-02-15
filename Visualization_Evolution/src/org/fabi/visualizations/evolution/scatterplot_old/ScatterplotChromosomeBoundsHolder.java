package org.fabi.visualizations.evolution.scatterplot_old;

import org.fabi.visualizations.tools.math.Arrays;

@Deprecated
public class ScatterplotChromosomeBoundsHolder {
	
	double[][] bounds;
	
	public ScatterplotChromosomeBoundsHolder(double[][] inputs) {
		bounds = Arrays.getBasicStats(inputs);
	}
}
