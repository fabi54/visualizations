package org.fabi.visualizations.scatter.dotsize;

import org.fabi.visualizations.scatter.ScatterplotVisualization;

public abstract class DotSizeModelBase implements DotSizeModel {
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public void init(ScatterplotVisualization visualization) {
		init(visualization.getSource());
	}

}
