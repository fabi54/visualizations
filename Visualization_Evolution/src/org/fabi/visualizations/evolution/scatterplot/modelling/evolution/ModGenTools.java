package org.fabi.visualizations.evolution.scatterplot.modelling.evolution;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;

import game.data.ArrayGameData;
import game.data.GameData;
import game.models.ConnectableModel;
import configuration.CfgTemplate;
import configuration.models.ConnectableModelConfig;
import configuration.models.ModelConfig;

public class ModGenTools {
	
	public static ModelSource learnRegressionModel(CfgTemplate cfg, DataSource trainingData) {
		ConnectableModelConfig connectableConfig;
		GameData data = new ArrayGameData(trainingData.getInputDataVectors(), trainingData.getOutputDataVectors());
		if (cfg instanceof ModelConfig) {
			// preprocessings?
			connectableConfig = new ExtConnectableModelConfig(data.getINumber(), (ModelConfig) cfg);
		} else if (cfg instanceof ConnectableModelConfig) {
			connectableConfig = (ConnectableModelConfig) cfg;
		} else {
			throw new IllegalArgumentException(cfg.getClass().getSimpleName() + " not allowed.");
		}
		ConnectableModel mod = new ConnectableModel();
		mod.init(connectableConfig);
		mod.setMaxLearningVectors(data.getInstanceNumber());
		double[][] inputs = data.getInputVectors();
        double[][] outputs = data.getOutputAttrs();
        for (int j = 0; j < data.getInstanceNumber(); j++) {
        	mod.storeLearningVector(inputs[j], outputs[j][0]);
        }
        mod.learn();
        return new GameRegressionModelSource(mod);
	}
	
	// TODO 
	/*else if (cfg[i] instanceof ClassifierConfig) {
		ConnectableClassifier cls = new ConnectableClassifier();
		// preprocessings?
		cls.init((ClassifierConfig) cfg[i]);
		cls.setMaxLearningVectors(data.getInstanceNumber());
		double[][] inputs = data.getInputVectors();
        double[][] outputs = data.getOutputAttrs();
        for (int j = 0; j < data.getInstanceNumber(); j++) {
        	cls.storeLearningVector(inputs[j], outputs[j]);
        }
        cls.learn();
        sources[i] = new GameClassifierModelSource(cls);
	}*/
	
	protected static class ExtConnectableModelConfig extends ConnectableModelConfig {

		private static final long serialVersionUID = -7309627478458456208L;

		public ExtConnectableModelConfig(int arg0, ModelConfig cfg) {
			super(arg0);
			config = cfg;
		}
	}
}
