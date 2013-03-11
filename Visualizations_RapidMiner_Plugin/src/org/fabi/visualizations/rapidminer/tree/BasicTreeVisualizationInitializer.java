package org.fabi.visualizations.rapidminer.tree;

import org.fabi.visualizations.VisualizationInitializer;
import org.fabi.visualizations.tree.TreeVisualizationGraphicStyleProvider;

public class BasicTreeVisualizationInitializer implements VisualizationInitializer<TreeIOObjectVisualization> {
	
	@Override
	public TreeIOObjectVisualization initialize(TreeIOObjectVisualization orig) {
		orig.setNodeWidth(TreeVisualizationGraphicStyleProvider.Presets.Size.AUTO);
		orig.setNodeHeight(TreeVisualizationGraphicStyleProvider.Presets.Size.AUTO);
		orig.setVertexFontSize(9);
		orig.setEdgeFontSize(9);
		orig.setLevelsHorizontal(true);
		return orig;
	}

}
