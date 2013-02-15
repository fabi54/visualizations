package org.fabi.visualizations.tree;

import java.awt.Component;
import java.awt.Dimension;

public interface NodeContentRenderer<T> {
	public Component getVertexContentComponent(T node);
	public Dimension getPreferredNodeContentSize();
	public String getName();
}
