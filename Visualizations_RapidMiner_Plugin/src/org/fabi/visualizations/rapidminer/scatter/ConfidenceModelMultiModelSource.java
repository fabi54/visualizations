package org.fabi.visualizations.rapidminer.scatter;

import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.MultiModelSourceBase;
import org.modgen.rapidminer.modelling.ConfidenceModel;

public class ConfidenceModelMultiModelSource extends MultiModelSourceBase {
	
	public ConfidenceModelMultiModelSource(ConfidenceModel model) {
		super(getModels(model), model.getMethod());
	}
	
	protected static ModelSource[] getModels(ConfidenceModel model) {
		ModelSource[] models = new ModelSource[model.getModelCount()];
		for (int i = 0; i < model.getModelCount(); i++) {
			models[i] = new RapidMinerModelSource(model.getModel(i));
		}
		return models;
	}
	
}
