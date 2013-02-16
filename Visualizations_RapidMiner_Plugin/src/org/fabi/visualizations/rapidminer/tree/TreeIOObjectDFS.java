package org.fabi.visualizations.rapidminer.tree;

import java.util.Collection;

import org.fabi.visualizations.tree.TreeDFS;

public class TreeIOObjectDFS extends TreeDFS<TreeNode> {
	
	@Override
	public Collection<TreeNode> getChildren(TreeNode node) {
		return node.getChildren();
	}
}
