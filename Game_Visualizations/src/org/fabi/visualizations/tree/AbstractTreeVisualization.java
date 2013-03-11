package org.fabi.visualizations.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.tree.gui.NodeContentsComboBox;
import org.ytoh.configurations.annotations.Component;
import org.ytoh.configurations.annotations.Property;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.renderers.Renderer;

@Component(name="Tree")
public abstract class AbstractTreeVisualization<T> extends Visualization<T> {

	protected VisualizationServerPreparator<T> preparator;
	
	protected VisualizationViewer<Object,Edge<Object>> viewer;
	
	public abstract TreeDFS<T> getTree();

	public abstract TreeVisualizationGraphicStyleProvider<T> getGraphicStyle();
	
	final TreeGenerator<T> getTreeGenerator() {
		return new TreeGenerator<T>(this, source);
	}
	
	public AbstractTreeVisualization() {
		super();
		preparator = new VisualizationServerPreparator<T>(this);
		viewer = null;
	}
	
	public AbstractTreeVisualization(T source) {
		this();
		setSource(source);
	}

/* property labels *****************************************************************************/

	public static final String PROPERTY_DISPLAY_NODE_LABELS = "display_node_labels";
	public static final String PROPERTY_NODE_CONTENTS = "node_contents";
	public static final String PROPERTY_NODE_WIDTH = "node_width";
	public static final String PROPERTY_NODE_HEIGHT = "node_height";
	public static final String PROPERTY_NODE_SPACING_HORIZONTAL = "node_spacing_horizontal";
	public static final String PROPERTY_NODE_SPACING_VERTICAL = "node_spacing_vertical";
	public static final String PROPERTY_MAXIMAL_DEPTH = "maximal_depth";
	public static final String PROPERTY_LEVELS_HORIZONTAL = "levels_horizontal";
	public static final String PROPERTY_VERTEX_FONT_SIZE = "vertex_font_size";
	public static final String PROPERTY_EDGE_FONT_SIZE = "edge_font_size";
	public static final String PROPERTY_VISUALIZATION_PADDING = "visualization_padding";
	public static final String PROPERTY_NODE_PADDING = "node_padding";

/* properties **********************************************************************************/

	@Property(name=PROPERTY_DISPLAY_NODE_LABELS)
	boolean displayNodeLabels = true;
	@Property(name=PROPERTY_NODE_CONTENTS)
	int nodeContents = NodeContents.NONE;
	@Property(name=PROPERTY_NODE_WIDTH)
	int nodeWidth = 100;
	@Property(name=PROPERTY_NODE_HEIGHT)
	int nodeHeight = 50;
	@Property(name=PROPERTY_NODE_SPACING_HORIZONTAL)
	int nodeSpacingHorizontal = 10;
	@Property(name=PROPERTY_NODE_SPACING_VERTICAL)
	int nodeSpacingVertical = 10;
	@Property(name=PROPERTY_MAXIMAL_DEPTH)
	int maxDepth = -1;
	@Property(name=PROPERTY_VERTEX_FONT_SIZE)
	int vertexFontSize = 10;
	@Property(name=PROPERTY_EDGE_FONT_SIZE)
	int edgeFontSize = 10;
	@Property(name=PROPERTY_VISUALIZATION_PADDING)
	int visualizationPadding = 15;
	@Property(name=PROPERTY_LEVELS_HORIZONTAL)
	boolean levelsHorizontal = false;
	@Property(name=PROPERTY_NODE_PADDING)
	int nodePadding = 5;

/* *********************************************************************************************/
	
	public static class NodeContents {
		public static final int NONE = 0;
	}
	
	protected final NodeContentRenderer<T> getNodeContentRenderer(int index) {
		if (index == NodeContents.NONE) { // 0
				return null;
		}
		List<NodeContentRenderer<T>> renderers = getNodeContentRenderers();
		if (renderers == null || index < NodeContents.NONE || index > renderers.size()) {
				throw new IndexOutOfBoundsException("Node contents not available. [" + this.getClass().getSimpleName() + ": #" + index + "]");
		} else {
			return renderers.get(index - 1);
		}
	}
	
	public final List<String> getNodeContentsLabels() {
		List<NodeContentRenderer<T>> renderers = getNodeContentRenderers();
		List<String> names = new ArrayList<String>(renderers.size());
		for (NodeContentRenderer<T> renderer : renderers) {
			names.add(renderer.getName());
		}
		return names;
	}
	
	protected abstract List<NodeContentRenderer<T>> getNodeContentRenderers();
	
	NodeContentRenderer<T> getActualNodeContentRenderer() {
		return getNodeContentRenderer(nodeContents);
	}
	
/* *********************************************************************************************/
	
	protected Dimension preferredSize;
	
	public Dimension getPreferredSize() {
		return preferredSize;
	}
	
	@Override
	public JComponent getVisualizationAsComponent() {
		if (viewer == null) {
			viewer = new VisualizationViewer<Object, Edge<Object>>(new StaticLayout<Object,Edge<Object>>(new DirectedSparseGraph<Object,Edge<Object>>()));
			preparator.prepareVisualizationServer(viewer);
			viewer.setBackground(Color.WHITE);
			DefaultModalGraphMouse<Object,Edge<Object>> graphMouse = new DefaultModalGraphMouse<Object,Edge<Object>>(1 / 1.1f, 1.1f);
			graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
			viewer.setGraphMouse(graphMouse);
		}
		return viewer;
	}
	
	protected void update() {
		if (viewer != null) {
			preparator.prepareVisualizationServer(viewer);
			viewer.repaint();
		}
	}

	@Override
	public BufferedImage getVisualizationAsImage(int width, int height) {
		VisualizationImageServer<Object,Edge<Object>> server = new VisualizationImageServer<Object, Edge<Object>>(new StaticLayout<Object,Edge<Object>>(new DirectedSparseGraph<Object,Edge<Object>>()),
				new Dimension(width, height));
		preparator.prepareVisualizationServer(server);
		server.setSize(new Dimension(width, height));
		server.setBackground(Color.WHITE);
		Image img = server.getImage(new Point2D.Double(width / 2.0, height / 2.0), new Dimension(width, height));
		BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		bufferedImage.getGraphics().drawImage(img, 0, 0, null);
		return bufferedImage;
	}

	public BufferedImage getVisualizationAsImage() {
		return getVisualizationAsImage(preferredSize.width, preferredSize.height);
	}
	
	@Override
	public JComponent getControls() {
		JPanel result = new JPanel();
		result.add(new NodeContentsComboBox(this));
		return result;
	}

	@Override
	public String toString() {
		return "Tree";
	}

/* HANDLING PROPERTIES CHANGES ***************************************************************/
	
	protected void setNodeContents(int nodeContentsValue, VisualizationServer<Object,Edge<Object>> server) {
		nodeContents = nodeContentsValue;
        if (server != null) {
    		preparator.updateNodeSizeAndLayout(server);
        	if (getActualNodeContentRenderer() == null) {
	        	server.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
	        } else {
	        	server.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.N);
	        }
        	server.getRenderer().setVertexRenderer(new TreeVertexRenderer<T>(preparator));
        }
		update();
	}

	public boolean isDisplayNodeLabels() {
		return displayNodeLabels;
	}

	public void setDisplayNodeLabels(boolean displayNodeLabels) {
		this.displayNodeLabels = displayNodeLabels;
		update();
	}

	public int getNodeContents() {
		return nodeContents;
	}

	public void setNodeContents(int nodeContents) {
		this.nodeContents = nodeContents;
		update();
	}

	public int getNodeWidth() {
		return nodeWidth;
	}

	public void setNodeWidth(int nodeWidth) {
		this.nodeWidth = nodeWidth;
		update();
	}

	public int getNodeHeight() {
		return nodeHeight;
	}

	public void setNodeHeight(int nodeHeight) {
		this.nodeHeight = nodeHeight;
		update();
	}

	public int getNodeSpacingHorizontal() {
		return nodeSpacingHorizontal;
	}

	public void setNodeSpacingHorizontal(int nodeSpacingHorizontal) {
		this.nodeSpacingHorizontal = nodeSpacingHorizontal;
		update();
	}

	public int getNodeSpacingVertical() {
		return nodeSpacingVertical;
	}

	public void setNodeSpacingVertical(int nodeSpacingVertical) {
		this.nodeSpacingVertical = nodeSpacingVertical;
		update();
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
		update();
	}

	public int getVertexFontSize() {
		return vertexFontSize;
	}

	public void setVertexFontSize(int vertexFontSize) {
		this.vertexFontSize = vertexFontSize;
		update();
	}

	public int getEdgeFontSize() {
		return edgeFontSize;
	}

	public void setEdgeFontSize(int edgeFontSize) {
		this.edgeFontSize = edgeFontSize;
		update();
	}

	public int getVisualizationPadding() {
		return visualizationPadding;
	}

	public void setVisualizationPadding(int visualizationPadding) {
		this.visualizationPadding = visualizationPadding;
		update();
	}

	public boolean isLevelsHorizontal() {
		return levelsHorizontal;
	}

	public void setLevelsHorizontal(boolean levelsHorizontal) {
		this.levelsHorizontal = levelsHorizontal;
		update();
	}

	public int getNodePadding() {
		return nodePadding;
	}

	public void setNodePadding(int nodePadding) {
		this.nodePadding = nodePadding;
		update();
	}
	
/* getters and setters *************************************************************************/
	
	
}
