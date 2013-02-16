package org.fabi.visualizations;

import java.util.List;

import org.fabi.visualizations.tree.AbstractTreeVisualization;
import org.fabi.visualizations.tree.NodeContentRenderer;
import org.fabi.visualizations.tree.TreeDFS;
import org.fabi.visualizations.tree.TreeVisualizationGraphicStyleProvider;

/* For Global.init() only */
@Deprecated
class InstantiableTreeVisualization extends AbstractTreeVisualization<Object> {

	public InstantiableTreeVisualization(Object source) {
		super(source);
	}

	@Override
	public TreeDFS<Object> getTree() {
		return null;
	}

	@Override
	public TreeVisualizationGraphicStyleProvider<Object> getGraphicStyle() {
		return null;
	}

	@Override
	protected List<NodeContentRenderer<Object>> getNodeContentRenderers() {
		return null;
	}

}
