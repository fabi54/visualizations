package org.modgen.rapidminer.modelling;

import java.util.Iterator;

import org.fabi.visualizations.tools.math.ManyToOne;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.PredictionModel;

public class ConfidenceModel extends PredictionModel {

	private static final long serialVersionUID = -2721229327085176383L;
	
	protected PredictionModel[] models;
	protected ManyToOne method;
	
	public ConfidenceModel(PredictionModel[] models, ManyToOne method) {
		super(models[0].getTrainingHeader());
        this.models = models;
        this.method = method;
    }

	@Override
	public ExampleSet performPrediction(ExampleSet exampleSet, Attribute predictedLabel)
			throws OperatorException {
		ExampleSet[] predicted = new ExampleSet[models.length];
		for (int i = 0; i < models.length; i++) {
			predicted[i] = models[i].performPrediction((ExampleSet) exampleSet.clone(), predictedLabel);
		}
		Iterator<Example> originalReader = exampleSet.iterator();
		@SuppressWarnings("unchecked")
		Iterator<Example>[] iterators = new Iterator[models.length];
		for (int i = 0; i < models.length; i++) {
			iterators[i] = predicted[i].iterator();
		}
		while (originalReader.hasNext()) {
			// TODO count confidence
			Example example = originalReader.next();
			double[] pl = new double[models.length];
			Example e = null;
			for (int i = 0; i < models.length; i++) {
				e = iterators[i].next();
				pl[i] = e.getPredictedLabel();
			}
			example.setPredictedLabel(method.getResult(pl));
		}
		return exampleSet;
	}
	
	public ManyToOne getMethod() {
		return method;
	}
	
	public int getModelCount() {
		return models.length;
	}
	
	public PredictionModel getModel(int i) {
		return models[i];
	}
}
