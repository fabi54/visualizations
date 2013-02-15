package org.fabi.visualizations.tools.math;

import java.util.Random;

public class UniformDistribution implements Distribution {

	protected double lower;
	protected double upper;
	protected Random rnd;
	
	public UniformDistribution() {
		this(0.0, 1.0);
	}
	
	public UniformDistribution(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;
		this.rnd = new Random(System.currentTimeMillis());
	}
	
	@Override
	public double next() {
		return (rnd.nextDouble() * upper) + lower;
	}
	
}
