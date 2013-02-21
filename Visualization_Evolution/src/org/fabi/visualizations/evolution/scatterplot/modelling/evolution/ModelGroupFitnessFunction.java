package org.fabi.visualizations.evolution.scatterplot.modelling.evolution;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.scatterplot.FitnessTools;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;

import configuration.CfgTemplate;

/*
 * 2013-02-17 19:56 changed line: "res = Math.tanh(res + 1.0);" -> "res = Math.tanh(2 * res + 1.0);"
 */

public class ModelGroupFitnessFunction implements FitnessFunction {

	int PRECISION_OUTSIDE_DATA = 20;
	
	protected DataSource data;
	
	public ModelGroupFitnessFunction(DataSource data) {
		this.data = data;
	}
	
	@Override
	public double getFitness(Chromosome c) {
		if (!(c instanceof ModelGroupChromosome)) {
			return Double.NaN;
		}
		CfgTemplate[] templates = (CfgTemplate[]) c.getPhenotype();
		ModelSource[] models = new ModelSource[templates.length];
		for (int i = 0; i < templates.length; i++) {
			models[i] = ModGenTools.learnRegressionModel(templates[i], data);
		}
		double res = 1.0;
		
		// precision on data:
		for (int i = 0; i < models.length; i++) {
			res *= meanSquareError(models[i], data);
		}
		res = Math.tanh(2 * res + 1.0);
		
		// difference outside data:
		// TODO improve "area outside data" detection
		double[][] bounds = org.fabi.visualizations.tools.math.Arrays.getBasicStats(data.getInputDataVectors());
		double[][] inputs = new double[PRECISION_OUTSIDE_DATA][1];
		int i = 0;
		double incr = bounds[org.fabi.visualizations.tools.math.Arrays.RANGE][0] * 2.0 / (double) PRECISION_OUTSIDE_DATA;
		double val = bounds[org.fabi.visualizations.tools.math.Arrays.LOWER_BOUND][0];
		for (; i < inputs.length / 2; i++) {
			val -= incr;
			inputs[i][0] = val;
		}
		val = bounds[org.fabi.visualizations.tools.math.Arrays.UPPER_BOUND][0];
		for (; i < inputs.length / 2; i++) {
			val += incr;
			inputs[i][0] = val;
		}
		double[][][] responses = new double[models.length][][];
		for (int j = 0; j < responses.length; j++) {
			responses[j] = models[j].getModelResponses(inputs);
		}
		res *= Math.tanh(FitnessTools.evaluateSimilarity(responses) + 1.0);
		res = 1 / res;
		
		return res;
	}
	
	// regression version
	static double meanSquareError(ModelSource model, DataSource data) {
		double res = 0.0;
		double[][] predicted = model.getModelResponses(data.getInputDataVectors());
		double[][] labeled = data.getOutputDataVectors();
		for (int i = 0; i < predicted.length; i++) {
			res += (Math.sqrt(Math.abs(Math.pow(predicted[i][0], 2) - Math.pow(labeled[i][0],2))));
		}
		return res / labeled.length;
	}
	
}