package org.fabi.visualizations.tree;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Map;

public class LayoutInitializer implements org.apache.commons.collections15.Transformer<Object,java.awt.geom.Point2D> {

	protected Map<Object,Dimension> generalLayout;
	protected Dimension nodeSpacing;
	protected Dimension nodeSize;
	protected int padding;
	protected boolean transpose;
	
	protected final static Dimension DEFAULT_SPACING = new Dimension(200, 200);
	protected final static Dimension DEFAULT_NODE_SIZE = new Dimension(100, 50);
	
	public LayoutInitializer(Map<Object,Dimension> generalLayout) {
		this.generalLayout = generalLayout;
		nodeSpacing = DEFAULT_SPACING;
		nodeSize = DEFAULT_NODE_SIZE;
		transpose = false;
	}
	
	public void setNodeSpacing(Dimension nodeSpacing) {
		this.nodeSpacing = nodeSpacing;
	}

	public Dimension getNodeSpacing() {
		return nodeSpacing;
	}

	public void setNodeSize(Dimension nodeSize) {
		this.nodeSize = nodeSize;
	}
	
	public Dimension getNodeSize() {
		return nodeSize;
	}
	
	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public void setTranspose(boolean transpose) {
		this.transpose = transpose;
	}
	
	public boolean getTranspose() {
		return transpose;
	}
	
	@Override
	public Point2D transform(Object node) {
		Dimension spacing = new Dimension(nodeSpacing.width + nodeSize.width, nodeSpacing.height + nodeSize.height);
		double x, y;
		if (node instanceof BentLineSupporter) {
			Point2D parentPosition = transform(((BentLineSupporter) node).parent);
			Point2D childPosition = null;
			if (node instanceof BentLineChildSupporter) {
				childPosition = transform(((BentLineChildSupporter) node).child);
			} else {
				childPosition = transform(((BentLineSupporter) node).parent);
			}
			if (transpose) {
				x = parentPosition.getY() + spacing.height;
				y = childPosition.getX();
				return new Point2D.Double(y, x);
			} else {
				x = parentPosition.getX() + spacing.width;
				y = childPosition.getY();
				return new Point2D.Double(x, y);
			}
		} else {
			Dimension nodeOffset = generalLayout.get(node);
			if (nodeOffset == null) {
				return new Point2D.Double(0.0d, 0.0d);
			}
			x = nodeOffset.width;
			y = nodeOffset.height;
			if (nodeOffset.width > 0) {
			}
		}
		if (transpose) {
			Point2D.Double res = new Point2D.Double((y / 2.0) * spacing.height + (nodeSize.width / 2) + padding, (x  * 2) * spacing.width + (nodeSize.height / 2) + padding);
			return res;
		}
		//System.out.println(x + ", " + y + " ... " + nodeOffset.width + ", " + nodeOffset.height + " * " + nodeSpacing);
			Point2D.Double res = new Point2D.Double((x * 2) * spacing.width + (nodeSize.width / 2) + padding, (y / 2.0) * spacing.height + (nodeSize.height / 2) + padding);
		return res;
	}

}
