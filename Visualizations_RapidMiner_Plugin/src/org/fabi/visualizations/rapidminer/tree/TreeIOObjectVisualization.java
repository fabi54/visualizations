package org.fabi.visualizations.rapidminer.tree;

import java.util.List;

import org.fabi.visualizations.tree.AbstractTreeVisualization;
import org.fabi.visualizations.tree.NodeContentRenderer;
import org.fabi.visualizations.tree.TreeDFS;
import org.fabi.visualizations.tree.TreeVisualizationGraphicStyleProvider;

public class TreeIOObjectVisualization extends AbstractTreeVisualization<TreeNode> {

	protected List<NodeContentRenderer<TreeNode>> nodeContentRenderers;
	
	public TreeIOObjectVisualization(TreeNode source) {
		super(source);
		nodeContentRenderers = null;
	}

	@Override
	public TreeDFS<TreeNode> getTree() {
		return new TreeIOObjectDFS();
	}

	@Override
	public TreeVisualizationGraphicStyleProvider<TreeNode> getGraphicStyle() {
		return new BasicTreeGraphicStyle();
	}
	
	@Override
	protected List<NodeContentRenderer<TreeNode>> getNodeContentRenderers() {
		if (nodeContentRenderers == null) {
			nodeContentRenderers = source.getNodeContentRenderers();
		}
		return nodeContentRenderers;
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

}
