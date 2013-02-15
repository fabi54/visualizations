package org.fabi.visualizations.tree;

public class EdgeGeneratorInteger implements EdgeGenerator<Integer> {
	protected int i;
	
	public EdgeGeneratorInteger() {
		i = 0;
	}
	
	public Integer generateEdge() {
		return i++;
	}
}
