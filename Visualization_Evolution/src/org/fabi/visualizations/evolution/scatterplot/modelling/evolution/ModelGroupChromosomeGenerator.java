package org.fabi.visualizations.evolution.scatterplot.modelling.evolution;

import org.fabi.visualizations.evolution.Chromosome;
import org.fabi.visualizations.evolution.ChromosomeGeneratorBase;
import org.fabi.visualizations.evolution.FitnessFunction;

public class ModelGroupChromosomeGenerator extends ChromosomeGeneratorBase {

		protected int chromosomeLength;
		protected FitnessFunction fitness;
		
		public ModelGroupChromosomeGenerator(int chromosomeLength, FitnessFunction fitness) {
			this.chromosomeLength = chromosomeLength;
			this.fitness = fitness;
		}
		
		@Override
		public Chromosome generate() {
			ModelGroupChromosome c = new ModelGroupChromosome(chromosomeLength, fitness);
			return c;
		}
	}