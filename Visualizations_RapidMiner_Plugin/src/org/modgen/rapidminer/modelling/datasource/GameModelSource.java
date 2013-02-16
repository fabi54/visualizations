package org.modgen.rapidminer.modelling.datasource;

import game.models.Model;

import org.fabi.visualizations.scatter.sources.ModelSource;

public class GameModelSource implements ModelSource {

	protected Model model;
	
	public GameModelSource(Model model) {
		this.model = model;
	}
	
	@Override
	public double[][] getModelResponses(double[][] inputs) {
		double[][] outputs = new double[inputs.length][1];
		for (int i = 0; i < inputs.length; i++) {
			outputs[i][0] = model.getOutput(inputs[i]);
		}
		return outputs;
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
		return model.getName();
	}

}
