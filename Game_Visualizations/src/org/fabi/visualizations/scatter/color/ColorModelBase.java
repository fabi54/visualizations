package org.fabi.visualizations.scatter.color;

import org.fabi.visualizations.scatter.ScatterplotVisualization;

public abstract class ColorModelBase implements ColorModel {
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public void init(ScatterplotVisualization visualization) {
		init(visualization.getSource());
	}

}
