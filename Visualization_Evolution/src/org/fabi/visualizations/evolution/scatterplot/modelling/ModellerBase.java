package org.fabi.visualizations.evolution.scatterplot.modelling;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

@Component(name="Modeller")
public class ModellerBase implements Modeller {

	@Property(name="model iterator")
	private ModelSourceIterator iterator;
	
	
	
	@Override
	public ModelSource[] getModels(DataSource data) {
		iterator.reset();
		if (!iterator.hasNext()) {
			return null;
		}
		ModelSource[] best = iterator.next();
		double bestFitness = evaluate(best);
		double act;
		while (iterator.hasNext()) {
			ModelSource[] models = iterator.next();
			if (best == null) {
				best = models;
			} else {
				act = evaluate(models);
				if (act > bestFitness) {
					best = models;
					bestFitness = act;
				}
			}
		}
		return best;
	}

	protected double evaluate(ModelSource[] models) {
		return 0;
	}
	
	public ModelSourceIterator getIterator() {
		return iterator;
	}

	public void setIterator(ModelSourceIterator iterator) {
		this.iterator = iterator;
	}
}
