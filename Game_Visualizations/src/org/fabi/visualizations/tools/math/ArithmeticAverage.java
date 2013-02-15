package org.fabi.visualizations.tools.math;

public class ArithmeticAverage implements ManyToOne {

	@Override
	public double getResult(double[] inputs) {
		double res = 0;
		for (int i = 0; i < inputs.length; i++) {
			res += inputs[i];
		}
		return res / inputs.length;
	}

}
