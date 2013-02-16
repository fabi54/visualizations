package org.fabi.visualizations.evolution.scatterplot.modelling;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;

public interface Modeller {

	public ModelSource[] getModels(DataSource data);

}
