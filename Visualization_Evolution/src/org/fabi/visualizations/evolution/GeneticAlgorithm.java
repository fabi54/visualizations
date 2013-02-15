package org.fabi.visualizations.evolution;

import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Genetic algorithm")
public class GeneticAlgorithm {
	
	@Property(name="Population size")
	public int populationSize;
	
	protected Population population;
	protected EvolutionStrategy strategy;
	
	public void init(Population initialPopulation, EvolutionStrategy strategy) {
		this.population = initialPopulation;
		this.strategy = strategy;
	}
	
	public void optimize() {
		population = strategy.getNextGeneration(population);
	}
	
	public Chromosome getBest() {
		return population.getBest();
	}
	
	public Population getPopulation() {
		return population;
	}
}
