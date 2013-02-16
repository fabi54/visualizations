package org.fabi.visualizations.rapidminer.tree;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.fabi.visualizations.tree.NodeContentRenderer;

public abstract class TreeNodeBase implements TreeNode {
	
	@Override
	public Paint getFillPaint() {
		return new Color(233, 236, 242);
	}

	@Override
	public Shape getShape(float x1, float y1, float x2, float y2) {
		return new Rectangle2D.Float(x1, y1, x2, y2);
	}
	
	@Override
	public String getParentEdgeLabel() {
		return null;
	}
	
	@Override
	public String getChildrenEdgeLabel() {
		return null;
	}

	@Override
	public List<NodeContentRenderer<TreeNode>> getNodeContentRenderers() {
		return null;
	}
}
