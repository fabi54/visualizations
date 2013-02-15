package org.fabi.visualizations.tree;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.swing.CellRendererPane;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;

public class TreeVertexRenderer<T> implements Renderer.Vertex<Object,Edge<Object>> {

	VisualizationServerPreparator<T> preparator;
	
	public TreeVertexRenderer(VisualizationServerPreparator<T> preparator) {
		this.preparator = preparator;
	}
	
	@Override
	public void paintVertex(RenderContext<Object, Edge<Object>> rc, Layout<Object, Edge<Object>> layout, final Object vertex) {
		if (vertex instanceof BentLineSupporter) {
			return;
		}
		
		final GraphicsDecorator g = rc.getGraphicsContext();
		
		Shape shape = rc.getVertexShapeTransformer().transform(vertex);
		Point2D p = layout.transform(vertex);
        p = rc.getMultiLayerTransformer().transform(Layer.LAYOUT, p);
        AffineTransform xform = AffineTransform.getTranslateInstance((float) p.getX(), (float) p.getY());
        shape = xform.createTransformedShape(shape);
        
        Paint fillPaint = rc.getVertexFillPaintTransformer().transform(vertex);
		Paint oldPaint = g.getPaint();
        if(fillPaint != null) {
            g.setPaint(fillPaint);
            g.fill(shape);
            g.setPaint(oldPaint);
        }
        Paint drawPaint = rc.getVertexDrawPaintTransformer().transform(vertex);
        if(drawPaint != null) {
            g.setPaint(drawPaint);
            Stroke oldStroke = g.getStroke();
            Stroke stroke = rc.getVertexStrokeTransformer().transform(vertex);
            if(stroke != null) {
                g.setStroke(stroke);
            }
            g.draw(shape);
            g.setPaint(oldPaint);
            g.setStroke(oldStroke);
        }
        
        final Component c = preparator.getNodeContents(vertex);
        
        if (c == null) { 
        	return;
        }

        final Shape finalShape = shape;
        
        final Dimension nodeContentSize = preparator.getNodeContentSize();
////        	double width = Math.min(icon.getIconWidth(), nodeContentSize.width);
////        	double height = Math.min(icon.getIconHeight(), nodeContentSize.height);
////            double xPos = finalShape.getBounds().getX() + (finalShape.getBounds().width / 2) - (width / 2);
////            double yPos = finalShape.getBounds().getY() + (finalShape.getBounds().height / 2) - (height / 2);
//            double xPos = shape.getBounds().getX() + preparator.getNodePadding();
//            double yPos = shape.getBounds().getY() + preparator.getNodePadding();
//            JLabel label = new JLabel("<html><font color=\"gray\">... loading ...</font></html>");
//            label.setFont(new Font("Sans Serif", Font.PLAIN, 6));
//            label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
//            label.setAlignmentY(JComponent.CENTER_ALIGNMENT);
//        	g.draw(label, new CellRendererPane(), (int) xPos, (int) yPos, (int) nodeContentSize.width, (int) nodeContentSize.height, false);
////        }
////        
//        SwingUtilities.invokeLater(new Runnable() {
//        	
//			@Override
//			public void run() {
	            double xPos = finalShape.getBounds().getX() + preparator.getNodePadding();
	            double yPos = finalShape.getBounds().getY() + preparator.getNodePadding();
	        	g.draw(c, new CellRendererPane(), (int) xPos, (int) yPos, nodeContentSize.width, nodeContentSize.height, false);
//			}
//		});
	}
	
//	protected static String IMAGE_ICON_PATH = "/org/fabi/visualizations/icons/loading.gif";
	
//	protected ImageIcon createLoaderIcon() {
//		java.net.URL imgURL = getClass().getResource(IMAGE_ICON_PATH);
//		if (imgURL != null) {
//			return new ImageIcon(imgURL);
//		} else {
//			return null;
//		}
//	}

}
