package org.fabi.visualizations.scatter_old;


public interface VisualizationAttributeSelector {
	/**
	 * The resulting array may contain only a subset of attributes, but contains at least one item.
	 * 
	 * @return ordered attribute indexes from the most important to the least
	 */
	public int[] getAttributesForVisualization(DatasetGenerator source);
}
