package org.fabi.visualizations.scatter.sources;


public interface ScatterplotSource {
	int getDataSourceCount();
	int getModelSourceCount();
	int getInputsNumber();
	int getOutputsNumber();
	DataSource getDataSource(int index);
	ModelSource getModelSource(int index);
	Metadata getMetadata();
}
