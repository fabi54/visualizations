package org.fabi.visualizations.evolution;

import java.util.ArrayList;
import java.util.List;

import org.fabi.visualizations.evolution.observers.EvolutionObserver;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Genetic algorithm")
public class GeneticAlgorithm {
	
	@Property(name="Population size")
	public int populationSize;
	
	protected List<EvolutionObserver> observers = new ArrayList<EvolutionObserver>(); 
	
	protected Population population;
	protected EvolutionStrategy strategy;
	
	public void init(Population initialPopulation, EvolutionStrategy strategy) {
		this.population = initialPopulation;
		for (EvolutionObserver o : observers) {
			o.init();
			o.step(population);
		}
		this.strategy = strategy;
	}
	
	public void optimize() {
		population = strategy.getNextGeneration(population);
		for (EvolutionObserver o : observers) {
			o.step(population);
		}
	}
	
	public Chromosome getBest() {
		return population.getBest();
	}
	
	public Population getPopulation() {
		return population;
	}
	
	public void addEvolutionObserver(EvolutionObserver observer) {
		observers.add(observer);
	}
}
