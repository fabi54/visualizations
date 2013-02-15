package org.fabi.visualizations.tools.math;

public class Maximum implements ManyToOne {

	@Override
	public double getResult(double[] inputs) {
		return getMaximum(inputs);
	}
	
	public static double getMaximum(double[] inputs) {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < inputs.length; i++) {
			max = Math.max(max, inputs[i]);
		}
		return max;
	}
}
