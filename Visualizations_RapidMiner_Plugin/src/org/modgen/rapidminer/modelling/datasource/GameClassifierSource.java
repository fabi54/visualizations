package org.modgen.rapidminer.modelling.datasource;

import game.classifiers.Classifier;

import org.fabi.visualizations.scatter.sources.ModelSource;

public class GameClassifierSource implements ModelSource {

	protected Classifier classifier;
	
	public GameClassifierSource(Classifier classifier) {
		this.classifier = classifier;
	}
	
	@Override
	public double[][] getModelResponses(double[][] inputs) {
		double[][] outputs = new double[inputs.length][];
		for (int i = 0; i < inputs.length; i++) {
			outputs[i] = classifier.getOutputProbabilities(inputs[i]);
		}
		return outputs;
	}

	@Override
	public int inputsNumber() {
		return classifier.getInputsNumber();
	}

	@Override
	public int outputsNumber() {
		return classifier.getOutputsNumber();
	}

	@Override
	public String getName() {
		return classifier.getName();
	}

}
