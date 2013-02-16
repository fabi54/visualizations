package org.fabi.visualizations.rapidminer;

import java.util.List;

import org.fabi.visualizations.Visualization;

import com.rapidminer.operator.ResultObjectAdapter;

public class MultipleVisualizationContainer extends ResultObjectAdapter {

	private static final long serialVersionUID = -7767098958741908248L;

	private List<Visualization<?>> visualizations;
	
	public MultipleVisualizationContainer(List<Visualization<?>> visualizations) {
		this.visualizations = visualizations;
	}
	
	public List<Visualization<?>> getVisualizations() {
		return visualizations;
	}
	
}
