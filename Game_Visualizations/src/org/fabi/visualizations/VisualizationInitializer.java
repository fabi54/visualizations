package org.fabi.visualizations;

import org.fabi.visualizations.config.VisualizationConfig;

@Deprecated
public interface VisualizationInitializer<S,V extends Visualization<S>> {
	
	public VisualizationConfig getConfig(S source);
	
	public V getVisualization(S source);
}
