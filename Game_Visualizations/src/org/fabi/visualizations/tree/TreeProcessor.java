package org.fabi.visualizations.tree;

public interface TreeProcessor<T> {
	
	/**
	 * @param parent null for root
	 * @param level depth of node (root = 0)
	 */
	public void processNodeDownwards(T node, T parent, int level, boolean leaf);
	
	/**
	 * On way back to top.
	 */
	public void processNodeUpwards(T node, T parent, int level, boolean leaf);
	
	public void prepare();
	public void finalise();
}
