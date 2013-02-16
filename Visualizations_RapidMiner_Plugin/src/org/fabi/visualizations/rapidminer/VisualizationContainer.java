package org.fabi.visualizations.rapidminer;

import org.fabi.visualizations.Visualization;

import com.rapidminer.operator.ResultObjectAdapter;

public class VisualizationContainer extends ResultObjectAdapter {

	private static final long serialVersionUID = -7767098958741908248L;

	private Visualization<?> visualization;
	
	public VisualizationContainer(Visualization<?> visualization) {
		this.visualization = visualization;
	}
	
	public Visualization<?> getVisualization() {
		return visualization;
	}
	
}
