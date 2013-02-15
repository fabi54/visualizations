package org.fabi.visualizations.tree;

import java.awt.Dimension;
import java.util.Map;
import java.util.HashMap;

public class TreeProcessorGeneralLayout<T> implements TreeProcessor<T> {

	protected Map<Object,Dimension> generalPositions;
	protected Dimension generalSize;
	protected int maxNodesInLevel;
	protected int lastNodeY;
	protected int maxLevel;
	protected int wasBacktracked;
	
	public TreeProcessorGeneralLayout() {
		generalPositions = null;
	}
	
	public Map<Object,Dimension> getGeneralLayout() {
		return generalPositions;
	}
	
	public Dimension getGeneralSize() {
		return generalSize;
	}
	
	@Override
	public void prepare() {
		generalPositions = new HashMap<Object,Dimension>();
		maxNodesInLevel = 0;
		maxLevel = -1;
		wasBacktracked = 0;
		lastNodeY = 0;
	}

	@Override
	public void finalise() {
		generalSize = new Dimension(maxLevel,maxNodesInLevel);
	}
	
	@Override
	public void processNodeDownwards(T node, T parent, int level, boolean leaf) {
		if (wasBacktracked > 0) {
			maxNodesInLevel++;
		}
		wasBacktracked = 0;
		generalPositions.put(node, new Dimension(level,-(maxNodesInLevel * 2)));
			// negative height means it has to be processed
		if (level > maxLevel) {
			maxLevel = level;
		}
	}

	@Override
	public void processNodeUpwards(T node, T parent, int level, boolean leaf) {
		Dimension position = generalPositions.get(node);
		if (position.height < 0) {
			position.height = -position.height;
		}
		if (wasBacktracked > 0) {
			position.height = (lastNodeY + position.height) / 2;
		}
		lastNodeY = position.height;
		generalPositions.put(node, position);
		wasBacktracked++;
	}
}
