package org.fabi.visualizations.scatter_old;

import java.util.List;

import org.jfree.data.Range;


public abstract class ModelInputGenerator {
	
	public static double[][] generateData(ScatterplotVisualization visualization) {
		List<Double> inputsSetting = visualization.getInputsSetting();
		int outputPrecision = visualization.getOutputPrecision();
		boolean yAxisInput = visualization.isyAxisInput();
		int xAxisIndex = visualization.getxAxisIndex();
		int yAxisIndex = visualization.getyAxisIndex();
		Double[] inputsS = new Double[inputsSetting.size()];
		inputsS = inputsSetting.toArray(inputsS);
		int inputsNr = inputsS.length;
		double[] defaults = new double[inputsNr];
		for (int i = 0; i < inputsS.length; i++) {
			defaults[i] = inputsS[i];
		}
		Range xRange = visualization.getActualXAxisRange();
		Range yRange = visualization.getActualYAxisRange();
		double xStep = xRange.getLength() / outputPrecision;
		double yStep = yRange.getLength() / outputPrecision;
		int xSize = (int) (xRange.getLength() / xStep);
		int ySize = yAxisInput ? (int) (yRange.getLength() / yStep) : 1;
		double x = xRange.getLowerBound() + (xStep / 2);
		double yLower = yRange.getLowerBound() + (yStep / 2);
		double y;
		double[][] inputs = new double[xSize * ySize][inputsNr];
		int xIndex = xAxisIndex;
		int yIndex = yAxisIndex;
		for (int i = 0; i < xSize; i++) {
			y = yLower;
			defaults[xIndex] = x;
			for (int j = 0; j < ySize; j++) {
				if (yAxisInput) {
					defaults[yIndex] = y;
				}
				System.arraycopy(defaults, 0, inputs[ySize * i + j], 0, inputsNr);
				//System.out.println(java.util.Arrays.toString(defaults));
				y += yStep;
			}
			x += xStep;
		}
//		System.out.println("===");
//		for (int i = 0; i < inputs.length; i++) {
//			System.out.println(java.util.Arrays.toString(inputs[i]));
//		}
//		System.out.println("===");
		return inputs;
	}
}
