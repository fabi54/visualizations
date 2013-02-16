package org.modgen.rapidminer.modelling.template;

import java.util.List;

//import org.fabi.visualizations.tools.math.ArithmeticAverage;
import org.fabi.visualizations.tools.math.Product;
import org.modgen.rapidminer.modelling.ConfidenceModel;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.learner.PredictionModel;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.InputPortExtender;
import com.rapidminer.operator.ports.OutputPort;

public class CreateConfidenceModelFromTemplateOperator extends Operator {

	private final InputPortExtender inputExtender = new InputPortExtender("model", getInputPorts());
	private OutputPort modelOut = getOutputPorts().createPort("confidence");
	
	public CreateConfidenceModelFromTemplateOperator(OperatorDescription description) {
		super(description);
		inputExtender.start();
		getTransformer().addGenerationRule(modelOut, ConfidenceModel.class);
	}
	
	// TODO method setup
	
	@Override
	public void doWork() {
		try {
			List<InputPort> inputPorts = inputExtender.getManagedPorts();
			int size = 0;
			for (InputPort p : inputPorts) { if (p.isConnected()) size++; }
			PredictionModel[] models = new PredictionModel[size];
			for (int i = 0; i < inputPorts.size(); i++) {
				if (inputPorts.get(i).isConnected()) {
					models[i] = inputPorts.get(i).getData(PredictionModel.class);
				}
			}
			modelOut.deliver(new ConfidenceModel(models, new Product()));	// TODO improve	
		} catch (UserError e) {
			e.printStackTrace();
		}
		
	}
}
