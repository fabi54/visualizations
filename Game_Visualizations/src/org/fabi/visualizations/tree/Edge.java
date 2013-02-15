package org.fabi.visualizations.tree;

public class Edge<T> {
	T parent;
	T child;
	
	public Edge(T child, T parent) {
		super();
		this.parent = parent;
		this.child = child;
	}

	public T getParent() {
		return parent;
	}

	public T getChild() {
		return child;
	}
	
	
}
