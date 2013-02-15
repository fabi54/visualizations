package org.fabi.visualizations.tree;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.commons.collections15.Transformer;
import org.fabi.visualizations.tree.TreeVisualizationGraphicStyleProvider.Presets;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationServer;
//import edu.uci.ics.jung.visualization.VisualizationViewer;
//import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

import java.awt.*;

public class VisualizationServerPreparator<V> {
	
	AbstractTreeVisualization<V> visualization;
	TreeGenerator<V> generator;
	Graph<Object,Edge<Object>> graph;

	//static CrossoverScalingControl scaler = new CrossoverScalingControl();
	
	protected Dimension nodeContentSize;
	
	public VisualizationServerPreparator(AbstractTreeVisualization<V> visualization) {
		this.visualization = visualization;
	}
	
	protected final static int LABEL_OFFSET = 1;
	
	public VisualizationServer<Object,Edge<Object>> prepareVisualizationServer(VisualizationServer<Object,Edge<Object>> server) {

		generator = visualization.getTreeGenerator();
		graph = generator.getGraph();

		final TreeVisualizationGraphicStyleProvider<V> graphicStyle = visualization.getGraphicStyle();
		RenderContext<Object,Edge<Object>> renderContext = server.getRenderContext();
		
		/* EDGES */
		
		renderContext.setEdgeShapeTransformer(new EdgeShape.Line<Object,Edge<Object>>());

        renderContext.setEdgeLabelTransformer(new Transformer<Edge<Object>,String>() {
            @Override
            public String transform(Edge<Object> edge) {
            	String label = graphicStyle.getEdgeLabel(edge);
            	if (label != null) {
	            	String colorString = Integer.toHexString(TreeVisualizationGraphicStyleProvider.Presets.getEdgeFontColor().getRGB());
	            	colorString = colorString.substring(2);
	            	StringBuilder sb = new StringBuilder();
	            	sb.append("<html><font color=\"#").append(colorString).append("\">").append(label).append("</font></html>");
	            	label = sb.toString();
            	}
            	return label;
            }
        });
        
        renderContext.setEdgeFontTransformer(new Transformer<Edge<Object>, Font>() {
        	
        	protected Font font = TreeVisualizationGraphicStyleProvider.Presets.getEdgeFont(visualization);
        	
			@Override
			public Font transform(Edge<Object> arg0) {
                return font;
			}
        	
        });
        
        renderContext.setLabelOffset(LABEL_OFFSET);
                
        /* VERTICES */
        renderContext.setVertexFontTransformer(new Transformer<Object,Font>() {
        	
        	protected Font font = TreeVisualizationGraphicStyleProvider.Presets.getVertexFont(visualization);
        	
            @Override
            public Font transform(Object vertex) {
                return font;
            }
        });
        renderContext.setVertexLabelTransformer(new Transformer<Object,String>() {
            @SuppressWarnings("unchecked")
			@Override
            public String transform(Object object) {
                if (object instanceof BentLineSupporter) {
                	return "";
                } else {
                	return graphicStyle.getVertexName((V) object);
                }
            }
        });
        renderContext.setVertexFillPaintTransformer(new Transformer<Object, Paint>() {
        	@SuppressWarnings("unchecked")
            @Override
            public Paint transform(Object vertex) {
                if (vertex instanceof BentLineSupporter) {
                	return Color.BLACK;
                } else {
                	return graphicStyle.getVertexFillPaint((V) vertex);
                }
            }
        });
        
        visualization.setNodeContents(visualization.nodeContents, server);
        
		return server;
	}
	
	//@SuppressWarnings("rawtypes")
	public void updateNodeSizeAndLayout(VisualizationServer<Object,Edge<Object>> server) {
		Dimension nodeSize = getNodeSize(visualization, graph);
		Layout<Object, Edge<Object>> layout = new StaticLayout<Object,Edge<Object>>(graph);
		LayoutInitializer initializer = new LayoutInitializer(generator.getGeneralLayout());
		initializer.setTranspose(visualization.levelsHorizontal);
		initializer.setNodeSpacing(new Dimension(visualization.nodeSpacingHorizontal, visualization.nodeSpacingVertical));
		int labelHeight = 0;
		if (visualization.nodeContents != AbstractTreeVisualization.NodeContents.NONE) {
			labelHeight = TreeVisualizationGraphicStyleProvider.Presets.getVertexFont(visualization).getSize() * 3;
		}
		initializer.setNodeSize(new Dimension(nodeSize.width,nodeSize.height + labelHeight));
		initializer.setPadding(visualization.visualizationPadding);
		layout.setInitializer(initializer);
		server.setGraphLayout(layout);
		
		Dimension spacing = new Dimension((int) ((visualization.nodeSpacingHorizontal / 100.0d) * nodeSize.width),
				(int) ((visualization.nodeSpacingVertical / 100.0d) * nodeSize.height));
		Dimension generalSize = generator.getGeneralSize();
		if (visualization.levelsHorizontal) {
			visualization.preferredSize = new Dimension(
					generalSize.height * spacing.width + nodeSize.width + visualization.visualizationPadding * 2,
					generalSize.width * spacing.height * 2 + nodeSize.height + visualization.visualizationPadding * 2);
		} else {
			visualization.preferredSize = new Dimension(
					generalSize.width * spacing.width * 2 + nodeSize.width + visualization.visualizationPadding * 2,
					generalSize.height * spacing.height + nodeSize.height + visualization.visualizationPadding * 2);
		}
		RenderContext<Object,Edge<Object>> renderContext = server.getRenderContext();
		
		renderContext.setVertexShapeTransformer(new ExtendedVertexShapeTransformer<V>(visualization, nodeSize));
		//if (server instanceof VisualizationViewer) {
		//	((VisualizationViewer) server).scaleToLayout(scaler);
		//}
	}
	
	protected Dimension getNodeSize(AbstractTreeVisualization<V> visualization, Graph<Object, Edge<Object>> graph) {
		Dimension nodeSize = new Dimension(visualization.nodeWidth, visualization.nodeHeight);
		NodeContentRenderer<V> renderer = visualization.getActualNodeContentRenderer();
		Dimension nodeContentSize = (renderer != null) ? renderer.getPreferredNodeContentSize() : new Dimension(0, 0);
		if (nodeSize.height == Presets.Size.AUTO) {
			int contentHeight = (nodeContentSize.height > 0) ? nodeContentSize.height : visualization.vertexFontSize;
			nodeSize.height = contentHeight + visualization.nodePadding * 2;
		}
		if (nodeSize.width == Presets.Size.AUTO) {
			int maxWidth = 0;
			Font font = TreeVisualizationGraphicStyleProvider.Presets.getVertexFont(visualization);
        	JComponent c = new JLabel();
        	c.setFont(font);
        	FontMetrics fm = c.getFontMetrics(font);
        	TreeVisualizationGraphicStyleProvider<V> gs = visualization.getGraphicStyle();
			for (Object vertex : graph.getVertices()) {
                if (!(vertex instanceof BentLineSupporter)) {
                	@SuppressWarnings("unchecked")
					int w = fm.stringWidth(gs.getVertexName((V) vertex));
                	if (w > maxWidth) {
                		maxWidth = w;
                	}
                }
			}
			nodeSize.width = Math.max(maxWidth, nodeContentSize.width) + visualization.nodePadding * 2;
		}
		this.nodeContentSize = new Dimension(nodeSize.width - visualization.nodePadding * 2, nodeContentSize.height);
		return nodeSize;
	}
	
	/** For TreeVertexRenderer: **********************************************/
	
	@SuppressWarnings("unchecked")
	Component getNodeContents(Object node) {
		if (node instanceof BentLineSupporter) {
			return null;
		} else {
			try {
				return visualization.getActualNodeContentRenderer().getVertexContentComponent((V) node);
			} catch (ClassCastException ex) {
				return null;
			} catch (NullPointerException ex) {
				return null;
			}
		}
	}
	
	Dimension getNodeContentSize() {
		return nodeContentSize;
	}
	
	int getNodePadding() {
		return visualization.nodePadding;
	}
}
