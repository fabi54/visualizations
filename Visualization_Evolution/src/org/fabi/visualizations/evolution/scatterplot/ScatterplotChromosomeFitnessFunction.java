package org.fabi.visualizations.evolution.scatterplot;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.tools.math.Arrays;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;
import org.jfree.data.Range;

import static org.fabi.visualizations.evolution.scatterplot.FitnessTools.evaluateInterestingness;
import static org.fabi.visualizations.evolution.scatterplot.FitnessTools.evaluateSimilarity;

public class ScatterplotChromosomeFitnessFunction implements FitnessFunction {

/*****************************************************************************/	
	
	protected ModelSource[] models;
	protected DataSource[] data;
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
		
	public ScatterplotChromosomeFitnessFunction(ModelSource[] models, DataSource data) {
		this.models = models;
		this.data = new DataSource[]{data};
		inputsNr = data.inputsNumber();
		outputsNr = data.outputsNumber();
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
    	ScatterplotVisualization vis = null;
    	try {
    		vis = getVisualization(chrmsm);
    	} catch (ClassCastException ex) {
    		ex.printStackTrace();
    		return Double.NaN;
    	}
    	double[][][] responses = getResponses(vis, getSource(chrmsm));
//    	if (Double.isNaN(evaluateSize(cfg, responses) * evaluateSimilarity(responses) * evaluateInterestingness(responses)))
//    	System.out.println(evaluateSize(cfg, responses) + " " + evaluateSimilarity(responses) + " " + evaluateInterestingness(responses));
//    	double res = evaluateSize(cfg, responses) * evaluateSimilarity(responses) * evaluateInterestingness(responses);
    	double res = Math.tanh(evaluateSize(vis, responses)) * Math.tanh(evaluateSimilarity(responses)) * Math.tanh(evaluateInterestingness(responses));
    	if (Double.isNaN(res) || Double.isInfinite(res)) {
    		System.out.println(evaluateSize(vis, responses) + " " + evaluateSimilarity(responses) + " " + evaluateInterestingness(responses));
    		return 0;
		}
		return res;
    }
    

	protected double evaluateSize(ScatterplotVisualization vis, double[][][] responses) {
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
		return Math.sqrt((vis.getxAxisRangeUpper() - vis.getxAxisRangeLower())
			 * (yAxisRangeUpper - yAxisRangeLower));
	}
	
	protected double[][][] getResponses(ScatterplotVisualization vis, ModelSource[] models) {
		return getModelOutputs(vis, models);
	}
	
	protected ModelSource[] getSource(Chromosome chrmsm) {
		return models;
	}
	
	protected ScatterplotVisualization getVisualization(Chromosome c) {
		ScatterplotVisualization res = (ScatterplotVisualization) c.getPhenotype();
		res.setOutputPrecision(MODEL_OUTPUT_PRECISION);
		return res;
	}

/*****************************************************************************/
	
	// NOTE: copy of ScatterplotVisualization methods [encapsulation of package org.fabi.visualizations.scatter2]
	
	protected double[][] getModelInputs(ScatterplotVisualization vis) {
		double[][] res = null;
		if (vis.getyAxisAttributeIndex() == ScatterplotVisualization.OUTPUT_AXIS) {
				res = getCurveInputs(vis);
		} else {
			res = getHeatMapInputs(vis);
		}
		ReversibleTransformation t = vis.getTransformation();
		if (t == null) {
			return res;
		} else {
			return t.transformBackwards(res);
		}
	}
	
	protected double[][] getHeatMapInputs(ScatterplotVisualization vis) {
		int outputPrecision = vis.getOutputPrecision();
		double[] inputsSetting = vis.getInputsSetting();
		double[][] modelInputs = new double[outputPrecision * outputPrecision][inputsSetting.length];
        double[] steps = new double[2];
        steps[ScatterplotVisualization.X_AXIS] = (vis.getxAxisRangeUpper() - vis.getxAxisRangeLower()) / (double) outputPrecision;
        steps[ScatterplotVisualization.Y_AXIS] = (vis.getyAxisRangeUpper() - vis.getyAxisRangeLower()) / (double) outputPrecision;
        int index = 0;
        for (int i = 0; i < outputPrecision; i++) {
        	double x = vis.getxAxisRangeLower()	+ (0.5 + i) * steps[ScatterplotVisualization.X_AXIS];
            for (int j = 0; j < outputPrecision; j++) {
            	double y = vis.getyAxisRangeLower() + (0.5 + j) * steps[ScatterplotVisualization.Y_AXIS];
            	System.arraycopy(inputsSetting, 0, modelInputs[index], 0, inputsSetting.length);
            	modelInputs[index][vis.getxAxisAttributeIndex()] = x;
            	modelInputs[index][vis.getyAxisAttributeIndex()] = y;
            	index++;
            }
        }
        return modelInputs;
	}
	
	protected double[][] getCurveInputs(ScatterplotVisualization vis) {
		int outputPrecision = vis.getOutputPrecision();
		double[] inputsSetting = vis.getInputsSetting();
		double[][] modelInputs = new double[outputPrecision][inputsSetting.length];
        double step = (vis.getxAxisRangeUpper() - vis.getxAxisRangeLower()) / (double) (outputPrecision - 1);
        for (int i = 0; i < outputPrecision; i++) {
        	double x = vis.getxAxisRangeLower() + i * step;
            System.arraycopy(inputsSetting, 0, modelInputs[i], 0, inputsSetting.length);
            	modelInputs[i][vis.getxAxisAttributeIndex()] = x;
        }
        return modelInputs;
	}
	
	protected double[][][] getModelOutputs(ScatterplotVisualization vis, ModelSource[] models) {
		int modelCnt = models.length;
		double[][][] modelOutputs = new double[modelCnt][][];
		double[][] modelInputs = getModelInputs(vis);
    	for (int i = 0; i < modelCnt; i++) {
	        	modelOutputs[i] = models[i].getModelResponses(modelInputs);
        }
		return modelOutputs;
	}
	
}
