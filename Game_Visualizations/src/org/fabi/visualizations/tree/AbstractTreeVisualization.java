package org.fabi.visualizations.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.config.VisualizationConfig;
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
	
	public AbstractTreeVisualization(T source) {
		super(source);
		preparator = new VisualizationServerPreparator<T>(this);
		viewer = null;
	}
	
	public AbstractTreeVisualization(T source, VisualizationConfig cfg) {
		super(source, cfg);
		preparator = new VisualizationServerPreparator<T>(this);
		viewer = null;
	}

/* configurations ******************************************************************************/

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

	static {
		VisualizationConfig.addTypeCast(PROPERTY_DISPLAY_NODE_LABELS, Boolean.class);
		VisualizationConfig.addTypeCast(PROPERTY_LEVELS_HORIZONTAL, Boolean.class);
		VisualizationConfig.addTypeCast(PROPERTY_NODE_CONTENTS, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_NODE_WIDTH, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_NODE_HEIGHT, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_NODE_SPACING_HORIZONTAL, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_NODE_SPACING_VERTICAL, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_MAXIMAL_DEPTH, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_VERTEX_FONT_SIZE, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_EDGE_FONT_SIZE, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_VISUALIZATION_PADDING, Integer.class);
		VisualizationConfig.addTypeCast(PROPERTY_NODE_PADDING, Integer.class);
	}

	@Override
	public VisualizationConfig getDefaultConfig() {
		VisualizationConfig defaultConfig = new VisualizationConfig(this.getClass());
		defaultConfig.setTypedProperty(PROPERTY_LEVELS_HORIZONTAL, false);
		defaultConfig.setTypedProperty(PROPERTY_DISPLAY_NODE_LABELS, true);
		defaultConfig.setTypedProperty(PROPERTY_NODE_CONTENTS, NodeContents.NONE);
		defaultConfig.setTypedProperty(PROPERTY_NODE_WIDTH, 100);
		defaultConfig.setTypedProperty(PROPERTY_NODE_HEIGHT, 50);
		defaultConfig.setTypedProperty(PROPERTY_NODE_SPACING_HORIZONTAL, 10);
		defaultConfig.setTypedProperty(PROPERTY_NODE_SPACING_VERTICAL, 10);
		defaultConfig.setTypedProperty(PROPERTY_MAXIMAL_DEPTH, -1);
		defaultConfig.setTypedProperty(PROPERTY_VERTEX_FONT_SIZE, 10);
		defaultConfig.setTypedProperty(PROPERTY_EDGE_FONT_SIZE, 10);
		defaultConfig.setTypedProperty(PROPERTY_VISUALIZATION_PADDING, 15);
		defaultConfig.setTypedProperty(PROPERTY_NODE_PADDING, 5);
		return defaultConfig;
	}
	
	@Property(name=PROPERTY_DISPLAY_NODE_LABELS)
	boolean displayNodeLabels;
	@Property(name=PROPERTY_NODE_CONTENTS)
	int nodeContents;
	@Property(name=PROPERTY_NODE_WIDTH)
	int nodeWidth;
	@Property(name=PROPERTY_NODE_HEIGHT)
	int nodeHeight;
	@Property(name=PROPERTY_NODE_SPACING_HORIZONTAL)
	int nodeSpacingHorizontal;
	@Property(name=PROPERTY_NODE_SPACING_VERTICAL)
	int nodeSpacingVertical;
	@Property(name=PROPERTY_MAXIMAL_DEPTH)
	int maxDepth;
	@Property(name=PROPERTY_VERTEX_FONT_SIZE)
	int vertexFontSize;
	@Property(name=PROPERTY_EDGE_FONT_SIZE)
	int edgeFontSize;
	@Property(name=PROPERTY_VISUALIZATION_PADDING)
	int visualizationPadding;
	@Property(name=PROPERTY_LEVELS_HORIZONTAL)
	boolean levelsHorizontal;
	@Property(name=PROPERTY_NODE_PADDING)
	int nodePadding;

	@Override
	public VisualizationConfig getConfig() {
		VisualizationConfig cfg = new VisualizationConfig(this.getClass());
		cfg.setTypedProperty(PROPERTY_DISPLAY_NODE_LABELS, displayNodeLabels);
		cfg.setTypedProperty(PROPERTY_LEVELS_HORIZONTAL, levelsHorizontal);
		cfg.setTypedProperty(PROPERTY_NODE_CONTENTS, nodeContents);
		cfg.setTypedProperty(PROPERTY_NODE_WIDTH, nodeWidth);
		cfg.setTypedProperty(PROPERTY_NODE_HEIGHT, nodeHeight);
		cfg.setTypedProperty(PROPERTY_NODE_SPACING_HORIZONTAL, nodeSpacingHorizontal);
		cfg.setTypedProperty(PROPERTY_NODE_SPACING_VERTICAL, nodeSpacingVertical);
		cfg.setTypedProperty(PROPERTY_MAXIMAL_DEPTH, maxDepth);
		cfg.setTypedProperty(PROPERTY_VERTEX_FONT_SIZE, vertexFontSize);
		cfg.setTypedProperty(PROPERTY_EDGE_FONT_SIZE, edgeFontSize);
		cfg.setTypedProperty(PROPERTY_VISUALIZATION_PADDING, visualizationPadding);
		cfg.setTypedProperty(PROPERTY_NODE_PADDING, nodePadding);
		return cfg;
	}

	@Override
	protected void setConfiguration(VisualizationConfig cfg, Collection<String> changedProperties) {
		if (changedProperties.contains(PROPERTY_NODE_CONTENTS)) {
			setNodeContents(cfg.<Integer>getTypedProperty(PROPERTY_NODE_CONTENTS), viewer);
		}
		
		// TODO improve also:
		displayNodeLabels = cfg.<Boolean>getTypedProperty(PROPERTY_DISPLAY_NODE_LABELS);
		nodeWidth = cfg.<Integer>getTypedProperty(PROPERTY_NODE_WIDTH);
		nodeHeight = cfg.<Integer>getTypedProperty(PROPERTY_NODE_HEIGHT);
		nodeSpacingHorizontal = cfg.<Integer>getTypedProperty(PROPERTY_NODE_SPACING_HORIZONTAL);
		nodeSpacingVertical = cfg.<Integer>getTypedProperty(PROPERTY_NODE_SPACING_VERTICAL);
		maxDepth = cfg.<Integer>getTypedProperty(PROPERTY_MAXIMAL_DEPTH);
		levelsHorizontal = cfg.<Boolean>getTypedProperty(PROPERTY_LEVELS_HORIZONTAL);
		vertexFontSize = cfg.<Integer>getTypedProperty(PROPERTY_VERTEX_FONT_SIZE);
		edgeFontSize = cfg.<Integer>getTypedProperty(PROPERTY_EDGE_FONT_SIZE);
		visualizationPadding = cfg.<Integer>getTypedProperty(PROPERTY_VISUALIZATION_PADDING);
		nodePadding = cfg.<Integer>getTypedProperty(PROPERTY_NODE_PADDING);
		
		if (viewer != null) {
			viewer.repaint();
		}
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
	}
}
