package org.fabi.visualizations.tools.math;

public class Minimum implements ManyToOne {

	@Override
	public double getResult(double[] inputs) {
		return getMinimum(inputs);
	}
	
	public static double getMinimum(double[] inputs) {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < inputs.length; i++) {
			min = Math.min(min, inputs[i]);
		}
		return min;
	}

}
