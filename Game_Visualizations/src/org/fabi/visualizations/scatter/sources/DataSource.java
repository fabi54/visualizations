package org.fabi.visualizations.scatter.sources;

public interface DataSource {
	/**
	 * @return first index: instance, second index: attributes, inputs first, outputs second
	 */
	public double[][] getInputDataVectors();
	public double[][] getOutputDataVectors();
	public int inputsNumber();
	public int outputsNumber();
	public String getName();
}
