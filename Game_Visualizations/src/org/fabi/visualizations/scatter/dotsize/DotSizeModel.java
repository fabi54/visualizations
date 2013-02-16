package org.fabi.visualizations.scatter.dotsize;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;

public interface DotSizeModel {

	public void init(ScatterplotVisualization visualization);

	public void init(ScatterplotSource source);
	
	// TODO? inputs before transformation
	// TODO? instance index in dataset
	// TODO yAxisOutput
	/**
	 * 
	 * Note for parameter outputsIndices: If output is plotted on some axis,
	 * data for each output is plotted separately, therefore outputsIndices contains only one value always.
	 * If all axes correspond to input attributes, each data instance is plotted only once, therefore outputsIndices
	 * contains all active outputs (equal to visualization visible outputs).
	 * 
	 * @param inputs transformed inputs
	 * @param outputs outputs
	 * @param index dataset index
	 * @param inputsIndices sorted array of used input attributes
	 * @param outputsIndices sorted array of used output attributes
	 */
	public int getSize(double[] inputs, double[] outputs, int index, int[] inputsIndices, int[] outputsIndices);
	
	public String getName();
}
