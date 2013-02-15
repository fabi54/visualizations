package org.fabi.visualizations.tools.math;

public class Product implements ManyToOne {

	@Override
	public double getResult(double[] inputs) {
		double res = 1;
		for (int i = 0; i < inputs.length; i++) {
			res *= inputs[i];
		}
		return res;
	}

}
