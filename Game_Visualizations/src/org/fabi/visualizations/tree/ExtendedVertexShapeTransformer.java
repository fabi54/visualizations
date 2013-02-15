package org.fabi.visualizations.tree;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Shape;

import org.apache.commons.collections15.Transformer;


public class ExtendedVertexShapeTransformer<T> implements Transformer<Object, Shape> {

	protected AbstractTreeVisualization<T> visualization;
	protected Dimension nodeSize;
	
	public ExtendedVertexShapeTransformer(AbstractTreeVisualization<T> visualization, Dimension nodeSize) {
		this.visualization = visualization;
		this.nodeSize = nodeSize;
	}
	
	@SuppressWarnings("unchecked")
	public Shape transform(Object object) {
		if (object instanceof BentLineSupporter) {
			return new Rectangle(0,0);
		} else {
			TreeVisualizationGraphicStyleProvider<T> graphicStyle = visualization.getGraphicStyle();
			int width = nodeSize.width;
			int height = nodeSize.height;
			return graphicStyle.getVertexShape((T) object, -width / 2.0f - 6.0f, -height / 2.0f - 2.0f, width + 8.0f, height + 4.0f);
		}
	}
}