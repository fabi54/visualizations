package org.fabi.visualizations.scatter_old;

import org.fabi.visualizations.tools.math.Statistics;

@Deprecated
public class VisualizationAttributeSelectorCorrelation implements VisualizationAttributeSelector {

	protected double OUTPUT_CORRELATION_WEIGHT = 1.0;
	protected double INPUT_CORRELATION_WEIGHT = 1.0;
	
	protected static VisualizationAttributeSelectorCorrelation instance = null;
	
	protected VisualizationAttributeSelectorCorrelation() { }
	
	public static VisualizationAttributeSelectorCorrelation getInstance() {
		if (instance == null) {
			instance = new VisualizationAttributeSelectorCorrelation();
		}
		return instance;
	}
	
	protected double getCorr(double[][] inputs, double[][] outputs, int[] previouslySelected, int index, int iteration) {
		double corr = 0;
		for (int i = 0; i < outputs[0].length; i++) {
			corr += Math.abs(Statistics.corr(inputs, index, outputs, i));
		}
		corr *= iteration;
		double corr2 = 0;
		for (int i = 0; i < iteration; i++) {
			corr2 = Math.abs(Statistics.corr(inputs, index, inputs, previouslySelected[i]));
		}
		corr2 *= outputs[0].length;
		return (OUTPUT_CORRELATION_WEIGHT * corr) - (INPUT_CORRELATION_WEIGHT * corr2);
	}
	
	@Override
	public int[] getAttributesForVisualization(DatasetGenerator source) {
		double[][] inputs = source.dataSource.getInputDataVectors();
		double[][] outputs = source.dataSource.getOutputDataVectors();
		if (inputs.length == 0 || outputs.length == 0) {
			return new int[1];
		}
		int inputsNr = source.getInputsNumber();
		double[] corr = new double[inputsNr];
		int[] result = new int[inputsNr]; // list of previously selcted attributes
		for (int i = 0; i < inputsNr; i++) {
			result[i] = -1;
		}
		for (int iteration = 0; iteration < inputsNr; iteration++) { // each iteration selects one attribute
			for (int i = 0; i < inputsNr; i++) {
				boolean ignore = false;
				for (int ignoreIndex = 0; ignoreIndex < inputsNr; ignoreIndex++) {
					if (i == result[ignoreIndex]) { // i has been selected already
						ignore = true;
						i = inputsNr;
					}
				}
				if (!ignore) {
					corr[i] = getCorr(inputs, outputs, result, i, iteration);
					if (result[iteration] < 0 || corr[result[iteration]] < corr[i]) {
						result[iteration] = i;
					}
				}
			}
		}
		return result;
	}

}
