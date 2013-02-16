package org.fabi.visualizations.evolution.scatterplot.modelling;

import java.util.Iterator;

import org.fabi.visualizations.scatter.sources.ModelSource;

public interface ModelSourceIterator extends Iterator<ModelSource[]> {
	
	public void reset();

}
