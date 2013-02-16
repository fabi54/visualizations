package org.fabi.visualizations.rapidminer.tree;

import java.awt.Paint;
import java.awt.Shape;
import java.util.Collection;
import java.util.List;

import org.fabi.visualizations.tree.NodeContentRenderer;

public interface TreeNode {
	public String getName();
	
	/**
	 * @return list of children, null if there are no children
	 */
	Collection<TreeNode> getChildren();	
	
	List<NodeContentRenderer<TreeNode>> getNodeContentRenderers();
	
	public Paint getFillPaint();
	public Shape getShape(float x1, float y1, float x2, float y2);
	
	public String getTreeTypeName();
	
	public String getParentEdgeLabel();
	public String getChildrenEdgeLabel();
}
