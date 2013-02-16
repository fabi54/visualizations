package org.fabi.visualizations.rapidminer.tree;

import org.fabi.visualizations.VisualizationInitializer;
import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.tree.AbstractTreeVisualization;
import org.fabi.visualizations.tree.TreeVisualizationGraphicStyleProvider;

public class BasicTreeVisualizationInitializer implements VisualizationInitializer<TreeNode,TreeIOObjectVisualization> {

	protected VisualizationConfig defaultConfig;
	
	public BasicTreeVisualizationInitializer() {
		defaultConfig = null;
	}
	
	public BasicTreeVisualizationInitializer(VisualizationConfig defaultConfig) {
		setDefaultConfig(defaultConfig);
	}
	
	public void setDefaultConfig(VisualizationConfig defaultConfig) {
		if (defaultConfig == null) {
			this.defaultConfig = null;
		} else {
			Class<?> classRef = defaultConfig.getTypedProperty(VisualizationConfig.PROPERTY_CLASS_REF);
			if (!TreeIOObjectVisualization.class.equals(classRef)) {
				throw new IllegalArgumentException("Incompatible config");
			}
			this.defaultConfig = defaultConfig;
		}
	}
	
	@Override
	public VisualizationConfig getConfig(TreeNode source) {
		VisualizationConfig cfg = new VisualizationConfig(TreeIOObjectVisualization.class);
		cfg.setTypedProperty(AbstractTreeVisualization.PROPERTY_NODE_WIDTH, TreeVisualizationGraphicStyleProvider.Presets.Size.AUTO);
		cfg.setTypedProperty(AbstractTreeVisualization.PROPERTY_NODE_HEIGHT, TreeVisualizationGraphicStyleProvider.Presets.Size.AUTO);
		cfg.setTypedProperty(AbstractTreeVisualization.PROPERTY_VERTEX_FONT_SIZE, 9);
		cfg.setTypedProperty(AbstractTreeVisualization.PROPERTY_EDGE_FONT_SIZE, 9);
		cfg.setTypedProperty(AbstractTreeVisualization.PROPERTY_LEVELS_HORIZONTAL, true);
		if (defaultConfig != null) {
			cfg.antiMerge(defaultConfig);
		}
		return cfg;
	}

	@Override
	public TreeIOObjectVisualization getVisualization(TreeNode source) {
		return new TreeIOObjectVisualization(source, getConfig(source));
	}

}
