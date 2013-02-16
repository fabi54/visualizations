package org.modgen.rapidminer.modelling;

import game.classifiers.ConnectableClassifier;
import game.data.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.fabi.visualizations.rapidminer.tree.TreeIOObject;
import org.fabi.visualizations.rapidminer.tree.TreeNode;
import org.modgen.rapidminer.data.RapidGameData;
import org.modgen.rapidminer.modelling.template.ModelStringParser;
import org.modgen.rapidminer.modelling.template.ModgenTemplateTreeNode;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.PredictionModel;

public class ModgenClassifierContainer extends PredictionModel implements TreeIOObject {

	private static final long serialVersionUID = -6891293953464074264L;
	
	private ConnectableClassifier classifier;
	
	public ModgenClassifierContainer(ExampleSet trainingExampleSet, ConnectableClassifier classifier) {
		super(trainingExampleSet);
		this.classifier = classifier;
	}
	
	protected ModgenClassifierContainer(ExampleSet trainingExampleSet) {
		super(trainingExampleSet);
	}

	public ConnectableClassifier getConnectableClassifier() {
		return classifier;
	}

	@Override
	public ExampleSet performPrediction(ExampleSet exampleSet, Attribute predictedLabel) throws OperatorException {
		Iterator<Example> originalReader = exampleSet.iterator();
		GameData data = new RapidGameData(exampleSet);
		double[] outputs;
		int iNumber = data.getINumber();
		for (int i = 0; originalReader.hasNext(); i++) {
			Example example = originalReader.next();
			double[] d = new double[iNumber];
			System.arraycopy(data.getInputVector(i), 0, d, 0, iNumber);
			outputs = classifier.getOutputProbabilities(d);
			for (int j = 0; j < outputs.length; j++) {
				example.setConfidence(predictedLabel.getMapping().mapIndex(j), outputs[j]);
			}
			example.setPredictedLabel(classifier.getOutput(d));
		}
		return exampleSet;
	}

	@Override
	public String toString() {
		return classifier.toString();
	}

	@Override
	public Collection<TreeNode> getRoots() {
		List<TreeNode> result = new ArrayList<TreeNode>(2);
		result.add(new ModgenTreeNode(classifier.getClassifier()));
		ModgenTemplateTreeNode config = ModelStringParser.getConfig(classifier.getClassifier().getConfig().toString());
		config.standardize();
		result.add(config);
		return result;
	}

}
