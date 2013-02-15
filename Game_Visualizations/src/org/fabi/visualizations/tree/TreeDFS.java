package org.fabi.visualizations.tree;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class TreeDFS<T> {
	protected Set<TreeProcessor<T>> processors;
	protected int maxDepth;
	
	public TreeDFS() {
		processors = new HashSet<TreeProcessor<T>>();
		maxDepth = -1;
	}
	
	public void setMaxDepth(int depth) {
		this.maxDepth = depth;
	}
	
	public int getMaxDepth() {
		return maxDepth;
	}
	
	public void registerProcessor(TreeProcessor<T> processor) {
		processors.add(processor);
	}
	
	public void unregisterProcessor(TreeProcessor<T> processor) {
		processors.remove(processor);
	}
	
	public void process(T tree) {
		for (TreeProcessor<T> p : processors) {
			p.prepare();
		}
		processNode(tree, null, 0);
		for (TreeProcessor<T> p : processors) {
			p.finalise();
		}
	}
	
	public void processNode(T node, T parent, int level) {
		Collection<T> children = null;
		if (level != maxDepth) {
			children = getChildren(node);
		}
		boolean leaf = (children == null || children.size() == 0);
		for (TreeProcessor<T> p : processors) {
			p.processNodeDownwards(node, parent, level, leaf);
		}
		if (children != null) {
			for (T c : children) {
				processNode(c, node, level + 1);
			}
		}
		for (TreeProcessor<T> p : processors) {
			p.processNodeUpwards(node, parent, level, leaf);
		}
	}
	
	/**
	 * @return if node is leaf, returns null
	 */
	public abstract Collection<T> getChildren(T node);
}
