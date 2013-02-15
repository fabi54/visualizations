package org.fabi.visualizations.tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;

public interface TreeVisualizationGraphicStyleProvider<V> {

	public static final class Presets {
		
		public static final class Size {
			public static final int AUTO = 0;
		}
		
		static Font getVertexFont(AbstractTreeVisualization<?> visualization) {
			return new Font("SansSerif", Font.BOLD, visualization.vertexFontSize);
		}
		
		static Font getEdgeFont(AbstractTreeVisualization<?> visualization) {
			return new Font("SansSerif", Font.PLAIN, visualization.edgeFontSize);
		}
		
		static Color getEdgeFontColor() {
			return Color.black;
		}
	}
	
    public String getEdgeLabel(Edge<Object> edge);
    public String getVertexName(V vertex);
    public Paint getVertexFillPaint(V vertex);
    public Shape getVertexShape(V vertex, float x1, float y1, float x2, float y2);
}
