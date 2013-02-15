package org.fabi.visualizations.scatter.sources;

public interface ModelSource {
	/**
	 * @return first index: instance, second index: output attribute
	 */
	public double[][] getModelResponses(double[][] inputs);
	public int inputsNumber();
	public int outputsNumber();
	public String getName();
}
