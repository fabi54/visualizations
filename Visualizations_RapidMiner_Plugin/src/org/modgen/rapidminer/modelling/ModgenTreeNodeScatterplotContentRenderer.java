package org.modgen.rapidminer.modelling;

import game.classifiers.Classifier;
import game.models.Model;

import java.awt.Component;
import java.awt.Dimension;

import org.fabi.visualizations.rapidminer.tree.TreeNode;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;
import org.fabi.visualizations.scatter.sources.ScatterplotSourceBase;
import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.tree.NodeContentRenderer;
import org.modgen.rapidminer.modelling.datasource.GameClassifierSource;
import org.modgen.rapidminer.modelling.datasource.GameModelSource;

public class ModgenTreeNodeScatterplotContentRenderer implements NodeContentRenderer<TreeNode> {

	@Override
	public Component getVertexContentComponent(TreeNode node) {
		if (!(node instanceof ModgenTreeNode)) {
			return null;
		}
		Object item = ((ModgenTreeNode) node).item;
		ScatterplotSource source = null;
		ModelSource modelSource = null;
		if (item instanceof Classifier) {
			modelSource = new GameClassifierSource((Classifier) item);
			source = new ScatterplotSourceBase(new ModelSource[]{modelSource});
		} else if (item instanceof Model) {
			modelSource = new GameModelSource((Model) item);
			source = new ScatterplotSourceBase(new ModelSource[]{modelSource});
		}
		if (source == null) {
			return null;
		}
		ScatterplotVisualization vis = new ScatterplotVisualization(source);
		vis.setyAxisAttributeIndex((modelSource.outputsNumber() > 1) ? ScatterplotVisualization.OUTPUT_AXIS : 1);
		vis.setxAxisRangeLower(0.0);
		vis.setxAxisRangeUpper(1.0);
		vis.setyAxisRangeLower(0.0);
		vis.setyAxisRangeUpper(1.0);
		double[] inputs = new double[modelSource.inputsNumber()];
		for (int i = 0; i < modelSource.inputsNumber(); i++) {
			inputs[i] = 0.5;
		}
		vis.setInputsSetting(inputs);
		vis.setLegendVisible(false);
		vis.setGridVisible(false);
		return vis.getVisualizationAsComponent();
	}

	@Override
	public Dimension getPreferredNodeContentSize() {
		return new Dimension(100, 100);
	}

	@Override
	public String getName() {
		return "Scatterplot";
	}

}
