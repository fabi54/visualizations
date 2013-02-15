package org.fabi.visualizations.tools.math;

import java.util.Random;

public class GaussianDistribution implements Distribution {

	protected double mean;
	protected double stdev;
	protected Random rnd;
	
	private static int i = 0; 
	
	public GaussianDistribution() {
		this(0.0, 1.0);
	}
	
	public GaussianDistribution(double mean, double stdev) {
		this.mean = mean;
		this.stdev = stdev;
		this.rnd = new Random(System.currentTimeMillis() + i);
		i += this.rnd.nextInt();
	}
	
	@Override
	public double next() {
		return (rnd.nextGaussian() * stdev) + mean;
	}

}
