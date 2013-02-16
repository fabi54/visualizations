package org.fabi.visualizations.scatter.sources;


public abstract class DerivedModelSourceCreator {
	public ScatterplotSource addTo(ScatterplotSource orig) {
		DataSource[] data = new DataSource[orig.getDataSourceCount()];
		for (int i = 0; i < data.length - 1; i++) {
			data[i] = orig.getDataSource(i);
		}
		ModelSource[] models = new ModelSource[orig.getModelSourceCount() + 1];
		for (int i = 0; i < models.length; i++) {
			models[i] = orig.getModelSource(i);
		}
		models[models.length - 1] = getDerivedModelSource(orig);
		return new ScatterplotSourceBase(data, models, orig.getMetadata());
	}
	
	protected abstract ModelSource getDerivedModelSource(ScatterplotSource orig);
}
