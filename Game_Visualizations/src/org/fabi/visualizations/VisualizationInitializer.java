package org.fabi.visualizations;

public interface VisualizationInitializer<T extends Visualization<?>> {
	public T initialize(T orig);
}
