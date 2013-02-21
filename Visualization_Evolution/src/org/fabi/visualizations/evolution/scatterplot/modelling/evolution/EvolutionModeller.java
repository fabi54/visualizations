package org.fabi.visualizations.evolution.scatterplot.modelling.evolution;

import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fabi.visualizations.evolution.AsymetricCrossoverEvolutionStrategy;
import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.GeneticAlgorithm;
import org.fabi.visualizations.evolution.Population;
import org.fabi.visualizations.evolution.PopulationBase;
import org.fabi.visualizations.evolution.scatterplot.modelling.Modeller;
import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;

import configuration.CfgTemplate;

/*
 *  2013-02-17 14:16 added feature: two times more models evolved than needed, imprecise omitted after evolution
 */

public class EvolutionModeller implements Modeller {

	public static final Logger logger = Logger.getLogger("Model Selection Evolution");
	
	protected static final int POPULATION_SIZE = 30;
	protected static final int MODEL_COUNT = 10;
	
	@Override
	public ModelSource[] getModels(final DataSource data) {
		logger.log(Level.INFO, "Initializing model selection evolution");
		logger.log(Level.INFO, "For data: " + data.getName());
		Population p = new PopulationBase(new ModelGroupChromosomeGenerator(MODEL_COUNT /** 2*/, new ModelGroupFitnessFunction(data)), POPULATION_SIZE);
		GeneticAlgorithm algorithm = new GeneticAlgorithm();
		algorithm.init(p, new AsymetricCrossoverEvolutionStrategy());
		Chromosome c = algorithm.getBest();
		logger.log(Level.INFO, "0: " + c.getFitness() + " " + c);
		for (int i = 0; i < 20; i++) {
			algorithm.optimize();
			c = algorithm.getBest();
			logger.log(Level.INFO, (i + 1) + ": " + c.getFitness() + " " + c);
		}
		logger.log(Level.INFO, "Model selection evolution finished.");
		CfgTemplate[] cfg = (CfgTemplate[]) c.getPhenotype();
		ModelSource[] models = new ModelSource[cfg.length];
		for (int i = 0; i < models.length; i++) {
			models[i] = ModGenTools.learnRegressionModel(cfg[i], data);
		}
		return models;
//		Arrays.sort(models, new Comparator<ModelSource>() {
//
//			@Override
//			public int compare(ModelSource o1, ModelSource o2) {
//				return Double.compare(ModelGroupFitnessFunction.meanSquareError(o1, data),
//						ModelGroupFitnessFunction.meanSquareError(o2, data));
//			}
//			
//		});
//		ModelSource[] result = new ModelSource[MODEL_COUNT];
//		for (int i = 0; i < result.length; i++) {
//			result[i] = models[i];
//		}
//		return result;
	}

}
