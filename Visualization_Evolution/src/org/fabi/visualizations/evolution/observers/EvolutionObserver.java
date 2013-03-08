package org.fabi.visualizations.evolution.observers;

import org.fabi.visualizations.evolution.Population;

public interface EvolutionObserver {

	/**
	 * Called by GeneticAlgorithm.
	 */
	public void init();
	
	/**
	 * Called by GeneticAlgorithm.
	 */
	public void step(Population actPopulation);
	
	/**
	 * Needs to be called manually.
	 */
	public void finalise();
	
}
