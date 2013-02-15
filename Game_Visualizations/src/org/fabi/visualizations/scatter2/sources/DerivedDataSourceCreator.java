package org.fabi.visualizations.scatter2.sources;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;

public abstract class DerivedDataSourceCreator {
	public ScatterplotSource addTo(ScatterplotSource orig) {
		DataSource[] data = new DataSource[orig.getDataSourceCount() + 1];
		for (int i = 0; i < data.length - 1; i++) {
			data[i] = orig.getDataSource(i);
		}
		data[data.length - 1] = getDerivedDataSource(orig);
		ModelSource[] models = new ModelSource[orig.getModelSourceCount()];
		for (int i = 0; i < models.length; i++) {
			models[i] = orig.getModelSource(i);
		}
		return new ScatterplotSourceBase(data, models, orig.getMetadata());
	}
	
	protected abstract DataSource getDerivedDataSource(ScatterplotSource orig);
}
