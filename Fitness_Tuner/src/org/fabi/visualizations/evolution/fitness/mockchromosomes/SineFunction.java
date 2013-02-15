package org.fabi.visualizations.evolution.fitness.mockchromosomes;

public class SineFunction extends AbstractFunctionBase {

	protected double period;
	protected double amplitude;
	protected double yShift;
	
	public SineFunction(double period, double amplitude, double yShift) {
		this.period = (2 * Math.PI) / period;
		this.amplitude = amplitude;
		this.yShift = yShift;
	}
	
	@Override
	public double getValue(double x) {
		return (amplitude * Math.sin(period * x)) + yShift;
	}

}
