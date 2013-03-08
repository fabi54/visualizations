package org.fabi.visualizations.evolution.observers;

import java.util.LinkedList;
import java.util.List;

import org.fabi.visualizations.evolution.Population;

public class BestFitnessObserver implements EvolutionObserver {

	List<Double> fitnessValues;
	
	public List<Double> getValues() {
		return fitnessValues;
	}
	
	@Override
	public void init() {
		fitnessValues = new LinkedList<Double>();
	}

	@Override
	public void step(Population actPopulation) {
		fitnessValues.add(actPopulation.getBest().getFitness());
	}

	@Override
	public void finalise() { }

}
