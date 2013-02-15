package org.fabi.visualizations.scatter2.color;

import org.fabi.visualizations.scatter2.ScatterplotVisualization;

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
