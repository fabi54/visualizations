package org.fabi.visualizations.tree;

import java.awt.Dimension;
import java.util.Map;


import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class TreeGenerator<T> {
	
	Map<Object,Dimension> generalLayout;
	Graph<Object,Edge<Object>> graph;
	Dimension generalSize;
	
	public TreeGenerator(AbstractTreeVisualization<T> visualization, T source) {
		TreeDFS<T> dfs = visualization.getTree();
		graph = new DirectedSparseGraph<Object, Edge<Object>>();
		TreeProcessorGeneralLayout<T> layoutProcessor = new TreeProcessorGeneralLayout<T>();
		TreeProcessorJungGraph<T> jungProcessor = new TreeProcessorJungGraph<T>(graph);
		dfs.registerProcessor(layoutProcessor);
		dfs.registerProcessor(jungProcessor);
		dfs.process(source);
		generalLayout = layoutProcessor.getGeneralLayout();
		generalSize = layoutProcessor.getGeneralSize();
	}
	
	public Map<Object,Dimension> getGeneralLayout() {
		return generalLayout;
	}
	
	public Dimension getGeneralSize() {
		return generalSize;
	}
	
	public Graph<Object,Edge<Object>> getGraph() {
		return graph;
	}
}
