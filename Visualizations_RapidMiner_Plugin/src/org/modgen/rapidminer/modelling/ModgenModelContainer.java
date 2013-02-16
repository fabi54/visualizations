package org.modgen.rapidminer.modelling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.fabi.visualizations.rapidminer.tree.TreeIOObject;
import org.fabi.visualizations.rapidminer.tree.TreeNode;
import org.modgen.rapidminer.data.RapidGameData;
import org.modgen.rapidminer.modelling.template.ModelStringParser;
import org.modgen.rapidminer.modelling.template.ModgenTemplateTreeNode;

import game.data.GameData;
import game.models.ConnectableModel;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.PredictionModel;

public class ModgenModelContainer extends PredictionModel implements TreeIOObject {

	private static final long serialVersionUID = 1262339307478820854L;
	
	private ConnectableModel model;
	
	public ModgenModelContainer(ExampleSet trainingExampleSet, ConnectableModel model) {
		super(trainingExampleSet);
		this.model = model;
	}

	@Override
	public boolean isInTargetEncoding() {
		return false;
	}
	
	@Override
	public ExampleSet performPrediction(ExampleSet exampleSet, Attribute predictedLabel) throws OperatorException {
		Iterator<Example> originalReader = exampleSet.iterator();
		GameData data = new RapidGameData(exampleSet);
		int iNumber = data.getINumber();
		for (int i = 0; originalReader.hasNext(); i++) {
			Example example = originalReader.next();
			double[] d = new double[iNumber];
			System.arraycopy(data.getInputVector(i), 0, d, 0, iNumber);
			example.setPredictedLabel(model.getOutput(d));
		}
		return exampleSet;
	}

	@Override
	public String toString() {
		return model.toString();
	}

	@Override
	public Collection<TreeNode> getRoots() {
		List<TreeNode> result = new ArrayList<TreeNode>(2);
		result.add(new ModgenTreeNode(model.getModel()));
		ModgenTemplateTreeNode config = ModelStringParser.getConfig(model.getModel().getConfig().toString());
		config.standardize();
		result.add(config);
		return result;
	}
}
