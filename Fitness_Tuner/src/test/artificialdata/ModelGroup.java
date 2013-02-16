package test.artificialdata;

//import game.classifiers.ConnectableClassifier;
import game.data.ArrayGameData;
import game.data.GameData;
import game.models.ConnectableModel;

import org.fabi.visualizations.scatter.sources.DataSource;
import org.fabi.visualizations.scatter.sources.ModelSource;

import configuration.CfgTemplate;
//import configuration.classifiers.ClassifierConfig;
import configuration.models.ConnectableModelConfig;
import configuration.models.ModelConfig;

public class ModelGroup {
	
	protected ModelSource[] sources;
	
	public ModelGroup(CfgTemplate[] cfg, DataSource trainingData) {
		sources = new ModelSource[cfg.length];
		GameData data = new ArrayGameData(trainingData.getInputDataVectors(), trainingData.getOutputDataVectors());
		for (int i = 0; i < cfg.length; i++) {
			System.out.println("Learning model #" + i + ".");
			if (cfg[i] instanceof ModelConfig) {
				ConnectableModel mod = new ConnectableModel();
				// preprocessings?
				ConnectableModelConfig connectableConfig = new ExtConnectableModelConfig(data.getINumber(), (ModelConfig) cfg[i]);
				mod.init(connectableConfig);
				mod.setMaxLearningVectors(data.getInstanceNumber());
				double[][] inputs = data.getInputVectors();
		        double[][] outputs = data.getOutputAttrs();
		        for (int j = 0; j < data.getInstanceNumber(); j++) {
		        	mod.storeLearningVector(inputs[j], outputs[j][0]);
		        }
		        mod.learn();
		        sources[i] = new GameRegressionModelSource(mod);
			} // TODO 
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
		}
	}
	
	public ModelSource[] getModels() {
		return sources;
	}
	
	protected class ExtConnectableModelConfig extends ConnectableModelConfig {

		private static final long serialVersionUID = -7309627478458456208L;

		public ExtConnectableModelConfig(int arg0, ModelConfig cfg) {
			super(arg0);
			config = cfg;
		}
	}

}
