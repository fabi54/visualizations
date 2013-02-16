package org.modgen.rapidminer.modelling.template;

import org.modgen.rapidminer.data.RapidGameData;
import org.modgen.rapidminer.modelling.ModgenClassifierContainer;
import org.modgen.rapidminer.modelling.ModgenModelContainer;

import game.classifiers.ConnectableClassifier;
import game.data.GameData;
import game.models.ConnectableModel;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;

import configuration.CfgTemplate;
import configuration.classifiers.ClassifierConfig;
import configuration.models.ModelConfig;

public class CreateModelFromTemplateOperator extends Operator {
	
	
	private InputPort conf = getInputPorts().createPort("template", ModgenTemplateContainer.class);
	private InputPort train = getInputPorts().createPort("training", ExampleSet.class);
	
	private OutputPort modelOut = getOutputPorts().createPort("model");
	private OutputPort trainOut = getOutputPorts().createPort("training");

	public CreateModelFromTemplateOperator(OperatorDescription description) {
		super(description);
        getTransformer().addGenerationRule(trainOut,ExampleSet.class);
		getTransformer().addPassThroughRule(train, trainOut);
	}
	
	@Override
	public void doWork() {
		try {
			ExampleSet es = train.getData(ExampleSet.class);
			GameData data = new RapidGameData(es);
			CfgTemplate config = conf.getData(ModgenTemplateContainer.class).getConfig();
			if (config instanceof ModelConfig) {
				modelOut.deliver(new ModgenModelContainer(es,createRregreModel((ModelConfig) config, data)));
			} else if (config instanceof ClassifierConfig) {
				modelOut.deliver(new ModgenClassifierContainer(es, createClassifierModel((ClassifierConfig) config, data)));
			}
			trainOut.deliver(es);		
		} catch (UserError e) {
			e.printStackTrace();
		}
		
	}
	
	private ConnectableModel createRregreModel(ModelConfig modelConfig, GameData data) {
		ConnectableModel mod = new ConnectableModel();
		// preprocessings?
		mod.init(modelConfig);
		mod.setMaxLearningVectors(data.getInstanceNumber());
		double[][] inputs = data.getInputVectors();
        double[][] outputs = data.getOutputAttrs();
        for (int i = 0; i < data.getInstanceNumber(); i++) {
        	mod.storeLearningVector(inputs[i], outputs[i][0]);
        }
        mod.learn();
        return mod;
	}
	
	private ConnectableClassifier createClassifierModel(ClassifierConfig classifierConfig, GameData data) {
		ConnectableClassifier cls = new ConnectableClassifier();
		// preprocessings?
		cls.init(classifierConfig);
		cls.setMaxLearningVectors(data.getInstanceNumber());
		double[][] inputs = data.getInputVectors();
        double[][] outputs = data.getOutputAttrs();
        for (int i = 0; i < data.getInstanceNumber(); i++) {
        	cls.storeLearningVector(inputs[i], outputs[i]);
        }
        cls.learn();
        return cls;
	}

}
