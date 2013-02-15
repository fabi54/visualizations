package test.artificialdata;

import game.models.ConnectableModel;
import game.models.Model;

import org.fabi.visualizations.scatter.sources.ModelSource;

public class GameRegressionModelSource implements ModelSource {

	protected Model model;
	
	public GameRegressionModelSource(Model model) {
		this.model = model;
	}
	
	@Override
	public double[][] getModelResponses(double[][] inputs) {
		double[][] result = new double[inputs.length][1];
		for (int i = 0; i < inputs.length; i++) {
			result[i][0] = model.getOutput(inputs[i]);
		}
		return result;
	}

	@Override
	public int inputsNumber() {
		return model.getInputsNumber();
	}

	@Override
	public int outputsNumber() {
		return 1;
	}

	@Override
	public String getName() {
		if (model instanceof ConnectableModel) {
			return ((ConnectableModel) model).getModel().getConfig().getClassRef().getSimpleName();
		} else {
			return model.getConfig().getClassRef().getSimpleName();
		}
	}

}
