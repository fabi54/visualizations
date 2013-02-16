package org.fabi.visualizations.evolution.scatterplot;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.FitnessFunction;
import org.fabi.visualizations.evolution.GeneticAlgorithm;
import org.fabi.visualizations.evolution.Population;
import org.fabi.visualizations.evolution.PopulationBase;
import org.fabi.visualizations.evolution.SymetricCrossoverEvolutionStrategy;
import org.fabi.visualizations.evolution.scatterplot.modelling.Modeller;
import org.fabi.visualizations.evolution.scatterplot.modelling.evolution.EvolutionModeller;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;

public class VisualizationEvolution {

	protected static Logger logger = Logger.getLogger("Visualization evolution");

	protected static int POPULATION_SIZE = 100;
	protected static int STEPS = 100;
	protected static double SELECTION_PRESSURE = 0.5;
	protected static double MUTATION_PROBABILITY = 0.05;
	protected static int ELITISM = 1;
	protected static double SIMILARITY_SIGNIFICANCE = 1.0;
	protected static double SIMILARITY_SIGNIFICANCE2 = 2.0;
	protected static double VARIANCE_SIGNIFICANCE = 1.0;
	protected static double SIZE_SIGNIFICANCE = 1.0;
	protected static int MODEL_OUTPUT_PRECISION = 20;
	protected static double MIN_WEIGHTED_DISTANCE = 0.0;
	
	protected static int VISUALIZATIONS_NUMBER = 5;
	
	protected Modeller modeller = new EvolutionModeller();
	
	public ScatterplotVisualization[] evolve(DataSource data) {
		ModelSource[] models = modeller.getModels(data);
		return evolve(data, models);
	}
	
	public ScatterplotVisualization[] evolve(DataSource data, ModelSource[] models) {
		logger.log(Level.INFO, "Initializing evolution.");
		List<Chromosome> c = null;
		FitnessFunction fitness = new ScatterplotChromosomeFitnessFunction(models, data);
		if (data.outputsNumber() == 1) {
			c = evolveRegression(data, fitness);
		} else {
			c = evolveClassification(data, fitness);
		}
		ScatterplotSource source = new ScatterplotSourceBase(new DataSource[]{data}, models);
		logger.log(Level.INFO, "Best chromosomes:");
		ScatterplotVisualization[] res = new ScatterplotVisualization[c.size()];
		for (int i = 0; i < c.size(); i++) {
			res[i] = (ScatterplotVisualization) c.get(i).getPhenotype();
			res[i].setSource(source);
			logger.log(Level.INFO, "  {" + c.get(i).getFitness() + "} " + res[i].hashCode());
		}
		return res;
	}
	

    protected static List<Chromosome> evolveRegression(DataSource data, FitnessFunction fitness) {
    	List<Chromosome> best = new LinkedList<Chromosome>();
		for (int j = 0; j < data.inputsNumber(); j++) {
	    	ScatterplotChromosomeGenerator generator = new ScatterplotChromosomeGenerator(fitness,
	    			data.getInputDataVectors(),
	    			true,
	    			new int[]{j});
	    	Population population = new PopulationBase(generator, POPULATION_SIZE);
	    	Population p = evolve(population);
	    	int cnt = 0;
				for (int k = 0; k < p.size() && cnt++ < VISUALIZATIONS_NUMBER; k++) {
					boolean add = true;
					for (Chromosome b : best) {
						if (((ScatterplotChromosome) p.getIth(k)).weightedDistanceTo((ScatterplotChromosome) b) < MIN_WEIGHTED_DISTANCE) {
							add = false;
							break;
						}
					}
					if (add) {
						best.add(p.getIth(k));
					}
				}
				Collections.sort(best, new Comparator<Chromosome>() {

					@Override
					public int compare(Chromosome o1, Chromosome o2) {
						return -Double.compare(o1.getFitness(), o2.getFitness());
					}
				});
		}
		return best;
    }
    
    protected static List<Chromosome> evolveClassification(DataSource data, FitnessFunction fitness) {
    	List<Chromosome> best = new LinkedList<Chromosome>();
    	ScatterplotChromosomeGenerator generator = new ScatterplotChromosomeGenerator(fitness,
    			data.getInputDataVectors(),
    			false,
    			null);
    	Population population = new PopulationBase(generator, POPULATION_SIZE);
		Population p = evolve(population);
		if (best.size() < VISUALIZATIONS_NUMBER) {
			for (int k = 0; k < p.size() && best.size() < VISUALIZATIONS_NUMBER; k++) {
				boolean add = true;
				for (Chromosome b : best) {
					if (((ScatterplotChromosome) p.getIth(k)).weightedDistanceTo((ScatterplotChromosome) b) < MIN_WEIGHTED_DISTANCE) {
						add = false;
						break;
					}
				}
				if (add) {
					best.add(p.getIth(k));
				}
			}
			Collections.sort(best, new Comparator<Chromosome>() {

				@Override
				public int compare(Chromosome o1, Chromosome o2) {
					return -Double.compare(o1.getFitness(), o2.getFitness());
				}
			});
		} else {
			for (int k = 0; k < p.size() &&
	    			(p.getIth(k).getFitness() > ((best.size() > 0) ? best.get(best.size() - 1).getFitness() : Double.MIN_VALUE)); k++) {
				boolean add = true;
				for (Chromosome b : best) {
					if (((ScatterplotChromosome) p.getIth(k)).weightedDistanceTo((ScatterplotChromosome) b) < MIN_WEIGHTED_DISTANCE) {
						add = false;
						break;
					}
				}
				if (add) {
					for (int i = 0; i < best.size(); i++) {
						if (p.getIth(k).getFitness() > best.get(i).getFitness()) {
							best.add(i, p.getIth(k));
							break;
						}
					}
					while (best.size() > VISUALIZATIONS_NUMBER) {
						best.remove(best.size() - 1);
					}
				}
	    	}
		}
		return best;
    }
    
    protected static Population evolve(Population population) {
		GeneticAlgorithm algorithm = new GeneticAlgorithm();
		algorithm.init(population, new SymetricCrossoverEvolutionStrategy(ELITISM,
				MUTATION_PROBABILITY, SELECTION_PRESSURE));
		Chromosome b = algorithm.getBest();
		logger.log(Level.INFO, "0: [" + b.getFitness() + "] " + b);
		for (int i = 0; i < STEPS; i++) {
			algorithm.optimize();
			b = algorithm.getBest();
			logger.log(Level.INFO, (i + 1) + ": [" + b.getFitness() + "] " + b);
		}
		return algorithm.getPopulation();
    }
    
    public void setModeller(Modeller modeller) {
    	this.modeller = modeller;
    }
}
