package org.fabi.visualizations.rapidminer.operators;

import java.util.List;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.rapidminer.MultipleVisualizationContainer;
import org.fabi.visualizations.rapidminer.VisualizationContainer;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.OutputPortExtender;
import com.rapidminer.operator.ports.OutputPort;

public class SplitMultipleVisualizationOperator extends Operator {

	private InputPort visualizationsIn = getInputPorts().createPort("visualizations");
	private final OutputPortExtender outputExtender = new OutputPortExtender("visualization", getOutputPorts());
	
	public SplitMultipleVisualizationOperator (OperatorDescription description) {
		super(description);
		outputExtender.start();
	}
	
	// TODO method setup
	
	@Override
	public void doWork() {
		try {
			List<OutputPort> outputPorts = outputExtender.getManagedPorts();
			List<Visualization<?>> visualizations = visualizationsIn.getData(MultipleVisualizationContainer.class).getVisualizations();
			int index = 0;
			for (OutputPort p : outputPorts) {
				if (p.isConnected()) {
					p.deliver(new VisualizationContainer(visualizations.get(index++)));
				}
			}	
		} catch (UserError e) {
			e.printStackTrace();
		}
		
	}
}
