package org.fabi.visualizations.tools.math;

public abstract class Metrics {
	public static double euclideanDistance(double[] a, double[] b) {
		return euclideanDistance(a, b, null);
	}
	
	/**
	 * @param ignore ordered(!) - lowest to highest - list of dimensions to be ignored
	 */
	public static double euclideanDistance(double[] a, double[] b, int[] ignore) {
		if (ignore == null) {
			ignore = new int[]{};
		}
		if (a.length != b.length) {
			throw new IllegalArgumentException("Vectors must be of same length.");
		}
		double result = 0;
		int ignoreIndex = 0;
		for (int i = 0; i < a.length; i++) {
			if (ignoreIndex < ignore.length && ignore[ignoreIndex] == i) {
				ignoreIndex++;
			} else {
				result += Math.pow(a[i] - b[i], 2);
			}
		}
		return Math.sqrt(result);
	}
}
