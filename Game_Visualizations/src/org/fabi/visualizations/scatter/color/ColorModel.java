package org.fabi.visualizations.scatter.color;

import java.awt.Color;

import javax.swing.JComponent;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;

public interface ColorModel {
	public static final boolean DATA = true;
	public static final boolean MODEL = false;
	
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
	 * @param data true for data series, false for model series 
	 * @param index dataset or model index
	 * @param inputsIndices sorted array of used input attributes
	 * @param outputsIndices sorted array of used output attributes
	 */
	public Color getColor(double[] inputs, double[] outputs, boolean data, int index, int[] inputsIndices, int[] outputsIndices);
	
	public String getName();

	// TODO? separate GUI
	public JComponent getControls();
}
