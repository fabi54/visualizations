package org.fabi.visualizations.rapidminer.renderers;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.rapidminer.scatter.RapidMinerDatasetGeneratorObjectAdapter;
import org.fabi.visualizations.scatter_old.ScatterplotVisualizationInitializer;

public class ScatterplotVisualizationRenderer extends VisualizationRenderer {

	@Override
	public String getName() {
		return "Scatterplot";
	}

	@Override
	protected Visualization<?> getVisualization(Object renderable) {
		if (renderable instanceof RapidMinerDatasetGeneratorObjectAdapter) {
			return new ScatterplotVisualizationInitializer().getVisualization(((RapidMinerDatasetGeneratorObjectAdapter) renderable).getDatasetGenerator());
		} else {
			return null;
		}
	}

}
