package org.fabi.visualizations.rapidminer.tree;

import java.awt.Paint;
import java.awt.Shape;

import org.fabi.visualizations.tree.Edge;
import org.fabi.visualizations.tree.TreeVisualizationGraphicStyleProvider;

public class BasicTreeGraphicStyle implements TreeVisualizationGraphicStyleProvider<TreeNode> {
	
	@Override
	public String getVertexName(TreeNode vertex) {
		return vertex.getName();
	}

	@Override
	public Paint getVertexFillPaint(TreeNode vertex) {
		return vertex.getFillPaint();
	}

	@Override
	public Shape getVertexShape(TreeNode vertex, float x1, float y1, float x2, float y2) {
		return vertex.getShape(x1, y1, x2, y2);
	}

	@Override
	public String getEdgeLabel(Edge<Object> edge) {
		if (edge.getParent() instanceof TreeNode) {
			return ((TreeNode) edge.getParent()).getChildrenEdgeLabel();
		} else if (edge.getChild() instanceof TreeNode) {
			return ((TreeNode) edge.getChild()).getParentEdgeLabel();
		} else {
			return null;
		}
	}

}
