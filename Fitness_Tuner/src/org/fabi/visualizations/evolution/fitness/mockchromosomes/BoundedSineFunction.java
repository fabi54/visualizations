package org.fabi.visualizations.evolution.fitness.mockchromosomes;

public class BoundedSineFunction extends AbstractFunctionBase {

	protected double period;
	protected double amplitude;
	protected double yShift;
	protected double lower;
	protected double upper;
	
	public BoundedSineFunction(double period, double amplitude, double yShift, double lower, double upper) {
		this.period = (2 * Math.PI) / period;
		this.amplitude = amplitude;
		this.yShift = yShift;
		this.lower = lower;
		this.upper = upper;
	}
	
	@Override
	public double getValue(double x) {
		return (x < lower) ? (amplitude * Math.sin(period * lower)) + yShift
				: (x > upper) ? (amplitude * Math.sin(period * upper)) + yShift
				: (amplitude * Math.sin(period * x)) + yShift;
	}
}
