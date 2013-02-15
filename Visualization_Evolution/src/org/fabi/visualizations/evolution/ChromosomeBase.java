package org.fabi.visualizations.evolution;

public abstract class ChromosomeBase implements Chromosome {

	protected Double fitness;
	
	protected FitnessFunction fitnessFunction;
	
	protected void setFitnessFunction(FitnessFunction fitness) {
		this.fitnessFunction = fitness;
		fitness = null;
	}
	
	@Override
	public double getFitness() {
		if (fitness == null) {
			fitness = fitnessFunction.getFitness(this);
		}
		return fitness;
	}
	
	protected void resetFitness() {
		fitness = null;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Chromosome) {
			return compareTo((Chromosome) other) == 0;
		} else {
			return false;
		}
	}

}
