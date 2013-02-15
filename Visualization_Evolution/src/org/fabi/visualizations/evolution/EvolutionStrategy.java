package org.fabi.visualizations.evolution;

public interface EvolutionStrategy {
	/**
	 * May be destructive to orig.
	 */
	public Population getNextGeneration(Population orig);
}
