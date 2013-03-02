package org.fabi.visualizations.evolution.scatterplot;

public class FitnessTools {
	public static double evaluateSimilarity(double[][][] responses) {
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
	
	public static double evaluateInterestingness(double[][][] responses) {
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
	
	public static double evaluateSimilarityAndInterestingnessLocal(double[][][] responses) {
		double result = 0.0;
		double prevAvg = Double.NaN;
		double actAvg, actVar;
		double[] values = new double[responses.length];
		for (int i = 0; i < responses[0].length; i++) {
			for (int j = 0; j < responses.length; j++) {
				values[j] = responses[j][i][0];
			}
			java.util.Arrays.sort(values);
			actAvg = 0.0;
			actVar = 0.0;
			for (int j = 1; j < responses.length - 1; j++) {
				if (!Double.isNaN(values[j])) {
					actAvg += values[j];
				}
			}
			actAvg /= responses.length;
			for (int j = 0; j < responses.length; j++) {
				actVar += (Math.abs(responses[j][i][0] - actAvg)); // absolute variance
			}
			actVar /= responses.length;
//			System.out.println(actVar + " " + (!Double.isNaN(prevAvg) ? (prevAvg - actAvg): ""));
			if (!Double.isNaN(prevAvg)) {
				result += (1 / actVar) * Math.abs(prevAvg - actAvg);
			}
			prevAvg = actAvg;
		}
		return result / (responses[0].length);
	} 
}
