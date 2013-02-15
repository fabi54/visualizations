/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fabi.visualizations.evolution.fitness;

import java.util.Arrays;

import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.fitness.mockchromosomes.MockVisualizationChromosome;
import org.fabi.visualizations.scatter.sources.MultiModelSource;
import org.fabi.visualizations.scatter_old.DatasetGenerator;
import org.fabi.visualizations.scatter_old.ModelInputGenerator;
import org.fabi.visualizations.scatter_old.ScatterplotVisualization;

public class VisualizationFitnessFunction2 implements FitnessFunction {
	
	protected static int MODEL_OUTPUT_PRECISION = 50;
	
	//protected double[] range;
	//protected double[] lower;
	
	public VisualizationFitnessFunction2() {
		
	}
	
	protected double normalize(double orig, int attr) {
		// return (orig - lower[attr]) / range[attr];
		return orig;
	}
	
    @Override
    public double getFitness(Chromosome chrmsm) {
    	VisualizationConfig cfg = null;
    	try {
    		cfg = getConfig(chrmsm);
    	} catch (ClassCastException ex) {
    		ex.printStackTrace();
    		return Double.NaN;
    	}
    	double[][][] responses = getResponses(cfg, getSource(chrmsm));
//    	System.out.println(evaluateSize(cfg, responses) + " " + evaluateSimilarity(cfg, responses) + " " + evaluateInterestingness(cfg, responses));
    	return evaluateSize(cfg, responses) * evaluateSimilarity(cfg, responses) * evaluateInterestingness(cfg, responses);
    }
    

	protected double evaluateSize(VisualizationConfig cfg, double[][][] responses) {
		double yAxisRangeUpper = Double.NEGATIVE_INFINITY, yAxisRangeLower = Double.POSITIVE_INFINITY;
		double[] actResponses = new double[responses.length];
		for (int i = 0; i < responses[0].length; i++) {
			for (int j = 0; j < responses.length; j++) {
				actResponses[j] = responses[j][i][0];
			}
			Arrays.sort(actResponses);
			yAxisRangeUpper = Math.max(yAxisRangeUpper, actResponses[actResponses.length - 2]);
			yAxisRangeLower = Math.min(yAxisRangeLower, actResponses[1]);
		}
		return Math.sqrt(normalize(cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_UPPER)
				- cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_LOWER),
				cfg.<Integer>getTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_ATTRIBUTE_INDEX))
			 *	normalize(yAxisRangeUpper - yAxisRangeLower,
					cfg.<Integer>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_ATTRIBUTE_INDEX)));
	}
	
	protected double evaluateSimilarity(VisualizationConfig cfg, double[][][] responses) {
		double similarity = 0.0;
		double[] values = new double[responses.length];
//		double lower = cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_LOWER);
//		double range = cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_UPPER)
//				- cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_LOWER);
		//double lower = 0, range = 1;
		for (int i = 0; i < responses[0].length; i++) {
			for (int j = 0; j < responses.length; j++) {
				values[j] = responses[j][i][0];
			}
			Arrays.sort(values);
//			int begin = 1; // omit one
//			int end = values.length - 2; // omit one
//			double actSimilarity = 0;
//			while (begin != end) {
//				actSimilarity += Math.abs(values[end] - values[begin]);
//				actSimilarity *= 2;
//				if (Math.abs(values[begin + 1] - values[begin])
//						> Math.abs(values[end] - values[end - 1])) {
//					begin++;
//				} else {
//					end--;
//				}
//			}
//			similarity += actSimilarity;
			double actSimilarity = Math.abs(values[values.length - 2] - values[1]);
			similarity += normalize(actSimilarity, cfg.<Integer>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_ATTRIBUTE_INDEX));//(actSimilarity - lower) / range;
		}
		return (responses[0].length / similarity);
	}
	
	protected double evaluateInterestingness(VisualizationConfig cfg, double[][][] responses) {
		double interestingness = 0.0;
		double acc = 0;
		double prevAvg = Double.NaN;
//		double lower = cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_LOWER);
//		double range = cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_UPPER)
//				- cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_LOWER);
		double lower = 0, range = 1;
		int cntr = 0;
		double[] actResponses = new double[responses.length];
		for (int i = 0; i < responses[0].length; i++) {
			double actAvg = 0.0, actDiff = 0.0;
			for (int j = 0; j < responses.length; j++) {
				actResponses[j] = responses[j][i][0];
			}
			Arrays.sort(actResponses);
			for (int j = 1; j < actResponses.length - 1; j++) {
				//if (!Double.isNaN(responses[j][i][0])) {
					actAvg += normalize(actResponses[j], cfg.<Integer>getTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_ATTRIBUTE_INDEX));//(actResponses[j] - lower) / range;
				//}
			}
			actAvg /= (actResponses.length - 2);
			if (!Double.isNaN(prevAvg)) {
				actDiff = prevAvg - actAvg; 
				if (acc == 0 || Math.signum(acc) == Math.signum(actDiff)) {
					acc += actDiff;
					cntr++;
				} else {
					interestingness += cntr * Math.abs(acc);
					acc = actDiff;
					cntr = 1;
				}
			}
			prevAvg = actAvg;
		}
		if (cntr > 0) {
			interestingness += cntr * Math.abs(acc);
		}
		return interestingness / (responses[0].length);
	}
	
	protected double[][][] getResponses(VisualizationConfig cfg, MultiModelSource models) {
		double[][][] responses = new double[models.getModelCount()][][];
		for (int i = 0; i < models.getModelCount(); i++) {
			ScatterplotVisualization v = new ScatterplotVisualization(new DatasetGenerator(models.getModel(i), null), cfg);
			v.updateActualAxesRanges();
			responses[i] = models.getModelResponses(i, ModelInputGenerator.generateData(v));
		}
		return responses;
	}
	
	protected MultiModelSource getSource(Chromosome chrmsm) {
		return (MockVisualizationChromosome) chrmsm;
	}
	
	protected VisualizationConfig getConfig(Chromosome c) {
		VisualizationConfig cfg = (VisualizationConfig) c.getPhenotype();
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_OUTPUT_PRECISION, MODEL_OUTPUT_PRECISION);
		return cfg;
	}
	
    
}
