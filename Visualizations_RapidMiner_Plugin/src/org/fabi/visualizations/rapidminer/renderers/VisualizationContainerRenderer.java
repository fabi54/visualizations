package org.fabi.visualizations.rapidminer.renderers;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.rapidminer.VisualizationContainer;

public class VisualizationContainerRenderer extends VisualizationRenderer {

	@Override
	public String getName() {
		return "Visualization";
	}

	@Override
	protected Visualization<?> getVisualization(Object renderable) {
		if (! (renderable instanceof VisualizationContainer)) {
			return null;
		} else {
			return ((VisualizationContainer) renderable).getVisualization();
		}
	}

}
