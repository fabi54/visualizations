package org.fabi.visualizations.scatter2.sources;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.ModelSource;

public interface ScatterplotSource {
	int getDataSourceCount();
	int getModelSourceCount();
	int getInputsNumber();
	int getOutputsNumber();
	DataSource getDataSource(int index);
	ModelSource getModelSource(int index);
	Metadata getMetadata();
}
