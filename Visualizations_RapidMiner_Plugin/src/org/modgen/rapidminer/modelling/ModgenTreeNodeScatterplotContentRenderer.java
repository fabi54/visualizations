package org.modgen.rapidminer.modelling;

import game.classifiers.Classifier;
import game.models.Model;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.rapidminer.tree.TreeNode;
import org.fabi.visualizations.scatter.sources.ModelSource;
import org.fabi.visualizations.scatter_old.DatasetGenerator;
import org.fabi.visualizations.scatter_old.ScatterplotVisualization;
import org.fabi.visualizations.tree.NodeContentRenderer;
import org.modgen.rapidminer.modelling.datasource.GameClassifierSource;
import org.modgen.rapidminer.modelling.datasource.GameModelSource;

public class ModgenTreeNodeScatterplotContentRenderer implements NodeContentRenderer<TreeNode> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Component getVertexContentComponent(TreeNode node) {
		if (!(node instanceof ModgenTreeNode)) {
			return null;
		}
		Object item = ((ModgenTreeNode) node).item;
		DatasetGenerator generator = null;
		ModelSource modelSource = null;
		if (item instanceof Classifier) {
			modelSource = new GameClassifierSource((Classifier) item);
			generator = new DatasetGenerator(modelSource, null);
		} else if (item instanceof Model) {
			modelSource = new GameModelSource((Model) item);
			generator = new DatasetGenerator(modelSource, null);
		}
		if (generator == null) {
			return null;
		}
		VisualizationConfig cfg = new VisualizationConfig(ScatterplotVisualization.class);
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_INPUT, (modelSource.outputsNumber() > 1));
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_LOWER, new Double(0));
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_X_AXIS_RANGE_UPPER, new Double(1));
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_LOWER, new Double(0));
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_RANGE_UPPER, new Double(1));
		List inputsSetting = new ArrayList(modelSource.inputsNumber());
		for (int i = 0; i < modelSource.inputsNumber(); i++) {
			inputsSetting.add(0.5);
		}
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, inputsSetting);
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_LEGEND_VISIBLE, false);
		cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_AXES_TICK_UNITS_VISIBLE, false);
		ScatterplotVisualization visualization = new ScatterplotVisualization(generator, cfg);
		return visualization.getVisualizationAsComponent();
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
