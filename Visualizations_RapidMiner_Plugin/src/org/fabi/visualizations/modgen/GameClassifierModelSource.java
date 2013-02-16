package org.fabi.visualizations.modgen;

import game.classifiers.Classifier;

import org.fabi.visualizations.scatter.sources.ModelSource;

public class GameClassifierModelSource implements ModelSource {

	protected Classifier classifier;
	
	public GameClassifierModelSource(Classifier classifier) {
		this.classifier = classifier;
	}
	
	@Override
	public double[][] getModelResponses(double[][] inputs) {
		double[][] result = new double[inputs.length][];
		for (int i = 0; i < inputs.length; i++) {
			result[i] = classifier.getOutputProbabilities(inputs[i]);
		}
		return result;
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
