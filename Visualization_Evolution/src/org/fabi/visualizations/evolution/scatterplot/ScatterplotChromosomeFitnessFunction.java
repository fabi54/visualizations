package org.fabi.visualizations.evolution.scatterplot;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.tools.math.Arrays;
import org.fabi.visualizations.tools.transformation.ReversibleTransformation;
import org.jfree.data.Range;


/*
* 2013-02-28 23:31: similarity evaluation changed to relative
* 2013-03-01 0:00:  significance bug fixed
* 2013-03-01 0:45:  default similarity significance: 10 -> 1
* 2013-03-01 0:46:  similarity evaluation reverted to non-relative
* 2013-03-01 0:47:  default significances changed: (sim var siz) 1 0.5 1
* 2013-03-01 0:49:  default significances changed: (sim var siz) 1 0.1 1
* 2013-03-01 0:50:  default significances changed: (sim var siz) 1 1 1
* 2013-03-01 23:08: change to local similarity+significance evaluation
* ... (see notes in date.txt)
* 2013-03-03 18:40: significance = abs(significance), similarity = 1 - similarity
* 2013-03-03 18:41: significance = abs(significance), similarity = 20 - similarity
* 2013-03-03 18:41: significance = abs(significance), similarity = 15 - similarity
* 
* 2013-03-08 15:46: LocalFitnessFunction <-> ScatterplotChromosomeFitnessFunction (this = further LocalFitnessFunction)
* 2013-03-08 20:47: basically tuned evaluateLocalAt function
* 2013-03-08 21:25: Math.tanh removed
* 2013-03-08 22:52: new evaluateLocalAt version
* 2013-03-08 23:07: evaluateLocalAt with subtractive similarity
* 2013-03-09 10:05: removed yAxis from size evaluation
*/

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
	
	public static int MODEL_OUTPUT_PRECISION = 20;

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
//    	double res = evaluateSize(vis, responses) * evaluateSimilarity(responses) * evaluateInterestingness(responses);
//		return res;
    	return evaluateGlobal(responses, vis) * evaluateLocal(responses, vis);
    }

    public static double evaluateGlobal(double[][][] responses, ScatterplotVisualization vis) {
    	return evaluateSize(vis, responses);
    }
    
    public static double evaluateLocal(double[][][] responses, ScatterplotVisualization vis) {
    	double res = 0;
    	int length = responses[0].length;
    	for (int i = 0; i < length; i++) {
    		res += evaluateLocalAt(responses, i, vis);
    	}
    	return res / length;
    }
    

	
    public static double evaluateLocalAt(double[][][] responses, int i, ScatterplotVisualization vis) {
		if (i == 0) { // TODO
			return 0.0;
		}
    	
    	double res = 0.0;
		double[] values = new double[responses.length];
		double[] prevValues = new double[responses.length];
		for (int j = 0; j < responses.length; j++) {
			prevValues[j] = responses[j][i - 1][0];
			values[j] = responses[j][i][0];
		}
		java.util.Arrays.sort(values);
		java.util.Arrays.sort(prevValues);
		
		// interestingness
		double prevAvg = 0.0;
		for (int j = 1; j < responses.length - 1; j++) {
			if (!Double.isNaN(values[j])) {
				res += values[j];
				prevAvg += prevValues[j];
			}
		}
		res /= responses.length;
		prevAvg /= responses.length;

		res -= prevAvg;
		res = Math.abs(res);
		res *= MODEL_OUTPUT_PRECISION;
		
		// similarity
		double similarity = 0.0;
    	for (int j = 0; j < responses.length; j++) {
			values[j] = responses[j][i][0];
		}
		java.util.Arrays.sort(values);
		int begin = 1;
		int end = values.length - 2;
		double div = 1;
		while (begin != end) {
			similarity += Math.abs(values[end] - values[begin]);
			similarity *= 2;
			if (Math.abs(values[begin + 1] - values[begin])
					> Math.abs(values[end] - values[end - 1])) {
				begin++;
			} else {
				end--;
			}
			div *= 2;
		}
		similarity /= div;
//		similarity = 0.1 - similarity;
		similarity = BASE - Math.exp(similarity);
//		if (similarity < 0) {
//			similarity = Math.pow(similarity, 5);
//		}
//		similarity = Math.pow(CONST2 / (CONST + similarity) + CONST3, CONST4);
//		if (similarity < 0) {
//			similarity = Math.pow(similarity, CONST5);
//		}
//		System.out.println("I: "  + res + " S: " + similarity + " -> " + res * similarity);
		res *= similarity;
		if (Double.isNaN(res)) { // added 2013-03-09 12:08
			return Double.NEGATIVE_INFINITY;
		} else {
			return res;
		}
    }
	
	public static void adjustFitness(DataSource data, ModelSource[] models) {
		double[][] inputs = data.getInputDataVectors();
		double[][][] responses = new double[models.length][][];
		for (int i = 0; i < responses.length; i++) {
			responses[i] = models[i].getModelResponses(inputs);
		}
		double[] similarity = new double[inputs.length];
		for (int i = 0; i < responses[0].length; i++) {
			double[] values = new double[responses.length];
			java.util.Arrays.sort(values);
			similarity[i] = 0.0;
	    	for (int j = 0; j < responses.length; j++) {
				values[j] = responses[j][i][0];
			}
			java.util.Arrays.sort(values);
			int begin = 1;
			int end = values.length - 2;
			double div = 1;
			while (begin != end) {
				similarity[i] += Math.abs(values[end] - values[begin]);
				similarity[i] *= 2;
				if (Math.abs(values[begin + 1] - values[begin])
						> Math.abs(values[end] - values[end - 1])) {
					begin++;
				} else {
					end--;
				}
				div *= 2;
			}
			similarity[i] /= div;
			similarity[i] = Math.exp(similarity[i]);
		}
//		double avg = 0.0, var = 0.0;
//		for (int i = 0; i < similarity.length; i++) {
//			avg += similarity[i];
//		}
//		avg /= similarity.length;
//		for (int i = 0; i < similarity.length; i++) {
//			var += Math.pow(similarity[i] - avg, 2.0);
//		}
//		var /= similarity.length;
//		org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.BASE = avg + var;
//		double max = Double.NEGATIVE_INFINITY;
//		for (int i = 0; i < similarity.length; i++) {
//			if (!Double.isInfinite(similarity[i]) && !Double.isNaN(similarity[i])) {
//				max = Math.max(max, similarity[i]);
//			}
//		}
		java.util.Arrays.sort(similarity);
		org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.BASE = similarity[similarity.length  * 3 / 4];
		System.out.println("org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.BASE = " + org.fabi.visualizations.evolution.scatterplot.ScatterplotChromosomeFitnessFunction.BASE);
	}
    
    public static double BASE = 1.2;
	
//    public static double evaluateLocalAt(double[][][] responses, int i, ScatterplotVisualization vis) {
//		if (i == 0) { // TODO
//			return 0.0;
//		}
//    	
//    	double res = 0.0;
//		double[] values = new double[responses.length];
//		double[] prevValues = new double[responses.length];
//		for (int j = 0; j < responses.length; j++) {
//			prevValues[j] = responses[j][i - 1][0];
//			values[j] = responses[j][i][0];
//		}
//		java.util.Arrays.sort(values);
//		java.util.Arrays.sort(prevValues);
//		
//		// interestingness
//		double prevAvg = 0.0;
//		for (int j = 1; j < responses.length - 1; j++) {
//			if (!Double.isNaN(values[j])) {
//				res += values[j];
//				prevAvg += prevValues[j];
//			}
//		}
//		res /= responses.length;
//		prevAvg /= responses.length;
//
//		res -= prevAvg;
//		res = Math.abs(res);
//		
//		// similarity
//		double similarity = 0.0;
//    	for (int j = 0; j < responses.length; j++) {
//			values[j] = responses[j][i][0];
//		}
//		java.util.Arrays.sort(values);
//		int begin = 1;
//		int end = values.length - 2;
//		double div = 1;
//		while (begin != end) {
//			similarity += Math.abs(values[end] - values[begin]);
//			similarity *= 2;
//			if (Math.abs(values[begin + 1] - values[begin])
//					> Math.abs(values[end] - values[end - 1])) {
//				begin++;
//			} else {
//				end--;
//			}
//			div *= 2;
//		}
//		similarity /= div;
//		similarity = Math.pow(CONST2 / (CONST + similarity) + CONST3, CONST4);
//		if (similarity < 0) {
//			similarity = Math.pow(similarity, CONST5);
//		}
//		res *= similarity;
//		return res;
//    }
//    
//    protected static double CONST = 0.25;
//    protected static double CONST2 = 3;
//    protected static double CONST3 = -2.8;
//    protected static double CONST4 = 1;
//    protected static double CONST5 = 3;
    
//    public static double evaluateLocalAt(double[][][] responses, int i, ScatterplotVisualization vis) {
//		if (i == 0) { // TODO
//			return 0.0;
//		}
//    	
//    	double res = 0.0;
//		double[] values = new double[responses.length];
//		
//		// interestingness
//		double[] prevValues = new double[responses.length];
//		for (int j = 0; j < responses.length; j++) {
//			prevValues[j] = responses[j][i - 1][0];
//			values[j] = responses[j][i][0];
//		}
//		java.util.Arrays.sort(values);
//		java.util.Arrays.sort(prevValues);
//		double prevAvg = 0.0;
//		for (int j = 1; j < responses.length - 1; j++) {
//			if (!Double.isNaN(values[j])) {
//				res += values[j];
//				prevAvg += prevValues[j];
//			}
//		}
//		res /= responses.length;
//		prevAvg /= responses.length;
//
//		res -= prevAvg;
//		res = Math.abs(res);
////		res -= 0.1; // ................................................ !!!!!! MAGIC NUMBER
//		
//		// similarity
//		double similarity = 0.0;
//    	for (int j = 0; j < responses.length; j++) {
//			values[j] = responses[j][i][0];
//		}
//		java.util.Arrays.sort(values);
//		int begin = 1;
//		int end = values.length - 2;
//		while (begin != end) {
//			similarity += Math.abs(values[end] - values[begin]);
//			similarity *= 2;
//			if (Math.abs(values[begin + 1] - values[begin])
//					> Math.abs(values[end] - values[end - 1])) {
//				begin++;
//			} else {
//				end--;
//			}
//		}
//		similarity = 15 - similarity;
////		similarity = 1 / (1 + similarity);
////		if (similarity < 0.5) { // ................................................ !!!!!! MAGIC NUMBER
////			similarity = -similarity;
////		}
//		res *= similarity;
//		return res;
//    }
//    
//    public static double evaluateLocalAt(double[][][] responses, int i, ScatterplotVisualization vis) {
//		if (i == 0) { // TODO
//			return 0.0;
//		}
//    	
//    	double res = 0.0;
//		double[] values = new double[responses.length];
//		
//		// interestingness
//		double[] prevValues = new double[responses.length];
//		for (int j = 0; j < responses.length; j++) {
//			prevValues[j] = responses[j][i - 1][0];
//		}
//		java.util.Arrays.sort(prevValues);
//		double prevAvg = 0.0;
//		for (int j = 1; j < responses.length - 1; j++) {
//			if (!Double.isNaN(values[j])) {
//				res += values[j];
//				prevAvg += prevValues[j];
//			}
//		}
//		res /= responses.length;
//		prevAvg /= responses.length;
//
//		res -= prevAvg;
//		res = Math.abs(res);
////		res -= 0.1; // ................................................ !!!!!! MAGIC NUMBER
//		
//		// similarity
//		double similarity = 0.0;
//    	for (int j = 0; j < responses.length; j++) {
//			values[j] = responses[j][i][0];
//		}
//		java.util.Arrays.sort(values);
//		int begin = 1;
//		int end = values.length - 2;
//		while (begin != end) {
//			similarity += Math.abs(values[end] - values[begin]);
//			similarity *= 2;
//			if (Math.abs(values[begin + 1] - values[begin])
//					> Math.abs(values[end] - values[end - 1])) {
//				begin++;
//			} else {
//				end--;
//			}
//		}
//		similarity = 15 - similarity;
////		similarity = 1 / similarity;
////		if (similarity < 0.5) { // ................................................ !!!!!! MAGIC NUMBER
////			similarity = -similarity;
////		}
//		res *= similarity;
//		return res;
//    }

	protected static double evaluateSize(ScatterplotVisualization vis, double[][][] responses) {
//		double yAxisRangeUpper = Double.NEGATIVE_INFINITY, yAxisRangeLower = Double.POSITIVE_INFINITY;
//		double[] values = new double[responses.length];
//		for (int i = 0; i < responses[0].length; i++) {
//			for (int j = 0; j < responses.length; j++) {
//				values[j] = responses[j][i][0];
//			}
//			java.util.Arrays.sort(values);
//			yAxisRangeUpper = Math.max(yAxisRangeUpper, values[values.length - 2]);
//			yAxisRangeLower = Math.min(yAxisRangeLower, values[1]);
//		}
		return Math.sqrt((vis.getxAxisRangeUpper() - vis.getxAxisRangeLower())
			/* * (yAxisRangeUpper - yAxisRangeLower)*/);
	}
	
	public static double[][][] getResponses(ScatterplotVisualization vis, ModelSource[] models) {
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
	
	public static double[][] getModelInputs(ScatterplotVisualization vis) {
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
	
	// TODO ... (diff getCurveInputs)
	protected static double[][] getHeatMapInputs(ScatterplotVisualization vis) {
		int outputPrecision = MODEL_OUTPUT_PRECISION;
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
	
	protected static double[][] getCurveInputs(ScatterplotVisualization vis) {
		int outputPrecision = MODEL_OUTPUT_PRECISION;
		double[] inputsSetting = vis.getInputsSetting();
		double[][] modelInputs = new double[outputPrecision + 1][inputsSetting.length];
        double step = (vis.getxAxisRangeUpper() - vis.getxAxisRangeLower()) / (double) (outputPrecision);
        for (int i = 0; i < outputPrecision + 1; i++) {
        	double x = vis.getxAxisRangeLower() + (i - 0.5) * step;
            System.arraycopy(inputsSetting, 0, modelInputs[i], 0, inputsSetting.length);
            	modelInputs[i][vis.getxAxisAttributeIndex()] = x;
        }
        return modelInputs;
	}
	
	protected static double[][][] getModelOutputs(ScatterplotVisualization vis, ModelSource[] models) {
		int modelCnt = models.length;
		double[][][] modelOutputs = new double[modelCnt][][];
		double[][] modelInputs = getModelInputs(vis);
    	for (int i = 0; i < modelCnt; i++) {
	        	modelOutputs[i] = models[i].getModelResponses(modelInputs);
        }
		return modelOutputs;
	}
	
}
