package org.fabi.visualizations.evolution.scatterplot_old;

import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter_old.DatasetGenerator;
import org.fabi.visualizations.scatter_old.ModelInputGenerator;
import org.fabi.visualizations.scatter_old.ScatterplotVisualization;
import org.fabi.visualizations.scatter_old.sources.MultiModelSource;
import org.fabi.visualizations.tools.math.Arrays;
import org.jfree.data.Range;

@Deprecated
public class ScatterplotChromosomeFitnessFunction implements FitnessFunction {

/*****************************************************************************/	
	
	protected MultiModelSource models;
	protected DataSource data;
	protected int inputsNr;
	protected int outputsNr;
		
	protected double lowerOutput;
	protected double outputRange;
	
	protected double[] lowerInputs;
	protected double[] inputsRange; 
	
	protected static int MODEL_OUTPUT_PRECISION = 20;

	public static double SIMILARITY_SIGNIFICANCE = 10;
	public static double SIMILARITY_SIGNIFICANCE2 = 10;
	public static double VARIANCE_SIGNIFICANCE = 1;
	public static double SIZE_SIGNIFICANCE = 0.5;

/*****************************************************************************/	
		
	public ScatterplotChromosomeFitnessFunction(MultiModelSource models, DataSource data) {
		this.models = models;
		this.data = data;
		inputsNr = models.inputsNumber();
		outputsNr = models.outputsNumber();
		if (outputsNr == 1) {
			Range bounds = Arrays.getBounds(data.getOutputDataVectors());
			lowerOutput = bounds.getLowerBound();
			outputRange = bounds.getLength();
		} else {
			lowerOutput = 0;
			outputRange = 1;
		}
		lowerInputs = Arrays.getLowerInputs(data.getInputDataVectors());
		lowerInputs = Arrays.getInputRanges(data.getInputDataVectors());
	}

	public static void setModelOutputPrecision(int precision) {
		MODEL_OUTPUT_PRECISION = precision;
	}
	
/*****************************************************************************/	
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
//    	if (Double.isNaN(evaluateSize(cfg, responses) * evaluateSimilarity(responses) * evaluateInterestingness(responses)))
//    	System.out.println(evaluateSize(cfg, responses) + " " + evaluateSimilarity(responses) + " " + evaluateInterestingness(responses));
//    	double res = evaluateSize(cfg, responses) * evaluateSimilarity(responses) * evaluateInterestingness(responses);
    	double res = Math.tanh(evaluateSize(cfg, responses)) * Math.tanh(evaluateSimilarity(responses)) * Math.tanh(evaluateInterestingness(responses));
    	if (Double.isNaN(res) || Double.isInfinite(res)) {
    		System.out.println(evaluateSize(cfg, responses) + " " + evaluateSimilarity(responses) + " " + evaluateInterestingness(responses));
    		return 0;
		}
		return res;
    }
    

	protected double evaluateSize(VisualizationConfig cfg, double[][][] responses) {
		double yAxisRangeUpper = Double.NEGATIVE_INFINITY, yAxisRangeLower = Double.POSITIVE_INFINITY;
		double[] values = new double[responses.length];
		for (int i = 0; i < responses[0].length; i++) {
			for (int j = 0; j < responses.length; j++) {
				values[j] = responses[j][i][0];
			}
			java.util.Arrays.sort(values);
			yAxisRangeUpper = Math.max(yAxisRangeUpper, values[values.length - 2]);
			yAxisRangeLower = Math.min(yAxisRangeLower, values[1]);
		}
		return Math.sqrt((cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_UPPER)
				- cfg.<Double>getTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_LOWER))
			 * (yAxisRangeUpper - yAxisRangeLower));
	}
	
	protected double evaluateSimilarity(double[][][] responses) {
		double similarity = 0.0;
		double[] values = new double[responses.length];
		for (int i = 0; i < responses[0].length; i++) {
			for (int j = 0; j < responses.length; j++) {
				values[j] = responses[j][i][0];
			}
			java.util.Arrays.sort(values);
			int begin = 1;
			int end = values.length - 2;
			double actSimilarity = 0;
			while (begin != end) {
				actSimilarity += Math.abs(values[end] - values[begin]);
				actSimilarity *= 2;
				if (Math.abs(values[begin + 1] - values[begin])
						> Math.abs(values[end] - values[end - 1])) {
					begin++;
				} else {
					end--;
				}
			}
			similarity += actSimilarity;
		}
		return (responses[0].length / similarity);
	}
	
	protected double evaluateInterestingness(double[][][] responses) {
		double interestingness = 0.0;
		double acc = 0;
		double prevAvg = Double.NaN;
		int cntr = 0;
		double[] values = new double[responses.length];
		for (int i = 0; i < responses[0].length; i++) {
			for (int j = 0; j < responses.length; j++) {
				values[j] = responses[j][i][0];
			}
			java.util.Arrays.sort(values);
			double actAvg = 0.0, actDiff = 0.0;
			for (int j = 1; j < responses.length - 1; j++) {
//			for (int j = 0; j < responses.length; j++) {
				if (!Double.isNaN(values[j])) {
					actAvg += values[j];
				}
			}
			actAvg /= responses.length;
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
			ScatterplotVisualization v = new ScatterplotVisualization(new DatasetGenerator(models.getModel(i), data), cfg);
			v.updateActualAxesRanges();
			responses[i] = models.getModelResponses(i, ModelInputGenerator.generateData(v));
		}
		return responses;
	}
	
	protected MultiModelSource getSource(Chromosome chrmsm) {
		return models;
	}
	
	protected VisualizationConfig getConfig(Chromosome c) {
		VisualizationConfig cfg = (VisualizationConfig) c.getPhenotype();
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_OUTPUT_PRECISION, MODEL_OUTPUT_PRECISION);
		return cfg;
	}

/*****************************************************************************/
}
