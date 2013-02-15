package org.fabi.visualizations.tree;

import java.util.Collection;
import java.util.HashSet;

import edu.uci.ics.jung.graph.Graph;

public class TreeProcessorJungGraph<T> implements TreeProcessor<T> {

	protected Graph<Object,Edge<Object>> graph;
		
	public TreeProcessorJungGraph(Graph<Object,Edge<Object>> graph) {
		this.graph = graph;
	}

	@Override
	public void prepare() {
		for (Edge<Object> edge : graph.getEdges()) {
			graph.removeEdge(edge);
		}
		for (Object vertex : graph.getVertices()) {
			graph.removeVertex(vertex);
		}
	}

	@Override
	public void finalise() { }

	@Override
	public void processNodeDownwards(T node, T parent, int level, boolean leaf) {
		graph.addVertex(node);
		if (parent != null) {
			BentLineChildSupporter supporter = new BentLineChildSupporter(parent, node);
			graph.addVertex(supporter);
			graph.addEdge(new Edge<Object>(supporter, parent), supporter, parent);
			graph.addEdge(new Edge<Object>(node, supporter), node, supporter);
		}
	}

	@Override
	public void processNodeUpwards(T node, T parent, int level, boolean leaf) {
		 Collection<Edge<Object>> edges = graph.getInEdges(node);
		 if (!edges.isEmpty()) {
			 BentLineSupporter supporter = new BentLineSupporter(node);
			 graph.addVertex(supporter);
			 Collection<Edge<Object>> edges2 = new HashSet<Edge<Object>>();
			 edges2.addAll(edges);
			 for (Edge<Object> e : edges2) {
				 Object o = e.child;
				 graph.removeEdge(e);
				 graph.addEdge(new Edge<Object>(o, supporter), o, supporter);
			 }
			 graph.addEdge(new Edge<Object>(supporter,node), supporter, node);
		 }
	}
}
