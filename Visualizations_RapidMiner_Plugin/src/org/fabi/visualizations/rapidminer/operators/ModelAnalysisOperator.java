package org.fabi.visualizations.rapidminer.operators;


import org.fabi.visualizations.rapidminer.scatter.RapidMinerDatasetGeneratorObjectAdapter;

import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.PredictionModel;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SimplePrecondition;

public class ModelAnalysisOperator extends Operator {
	
	/** Input for <code>PredictionModel</code> (model input) */
	protected InputPort modelInput = getInputPorts().createPort("model");

	/** Input for <code>ExampleSet</code> (data input) */
	protected InputPort dataInput = getInputPorts().createPort("data");
	
	/** Output delivering the <code>ChartIOObject</code> instance (chart output). */
	protected OutputPort chartOutput = getOutputPorts().createPort("visualization");
	
	public ModelAnalysisOperator(OperatorDescription description) {
		super(description);
		modelInput.addPrecondition(new SimplePrecondition(modelInput,
				new MetaData(PredictionModel.class), true));
		dataInput.addPrecondition(new SimplePrecondition(dataInput,
				new MetaData(ExampleSet.class), true));
	}
	
	@Override
	public void doWork() {
		try {
			chartOutput.deliver(new RapidMinerDatasetGeneratorObjectAdapter((PredictionModel) modelInput.getData(), (ExampleSet) dataInput.getData()));
		} catch (OperatorException ex) {
			return;
		}
		
	}
	
}