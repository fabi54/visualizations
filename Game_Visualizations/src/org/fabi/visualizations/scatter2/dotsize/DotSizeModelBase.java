package org.fabi.visualizations.scatter2.dotsize;

import org.fabi.visualizations.scatter2.ScatterplotVisualization;

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
