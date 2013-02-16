package org.fabi.visualizations.rapidminer.renderers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.rapidminer.tree.BasicTreeVisualizationInitializer;
import org.fabi.visualizations.rapidminer.tree.TreeIOObject;
import org.fabi.visualizations.rapidminer.tree.TreeIOObjectVisualization;
import org.fabi.visualizations.rapidminer.tree.TreeNode;
import org.fabi.visualizations.tree.AbstractTreeVisualization;
import org.freehep.util.export.ExportDialog;

import com.rapidminer.gui.renderer.AbstractRenderer;
import com.rapidminer.gui.renderer.RendererService;
import com.rapidminer.gui.tools.RadioCardPanel;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.report.Reportable;
import com.rapidminer.tools.LogService;

public class TreeVisualizationRenderer extends AbstractRenderer {

	@Override
	public String getName() {
		return "Tree";
	}

	@Override
	public Reportable createReportable(Object arg0, IOContainer arg1, int arg2,
			int arg3) {
		return null;
	}

	@Override
	public Component getVisualizationComponent(Object renderable, IOContainer container) {
		if (renderable instanceof TreeIOObject) {
			return createVisualization((TreeIOObject) renderable);
		} else {
			return null;
		}
	}
	
	/* Visualizations for every root (type of tree).
	 * 
	 * Copied from com.rapidminer.gui.processeditor.results.ResultDisplayTools,
	 * but with RadioCardPanel additional buttons disabled. */
	protected Component createVisualization(TreeIOObject renderable) {
		String resultName = RendererService.getName(renderable.getClass());
		if (resultName == null) {
			resultName = "";
		}
		VisualizationConfig defaultConfig = new VisualizationConfig(TreeIOObjectVisualization.class);
		defaultConfig.setTypedProperty(AbstractTreeVisualization.PROPERTY_LEVELS_HORIZONTAL, false);
		BasicTreeVisualizationInitializer initializer = new BasicTreeVisualizationInitializer(defaultConfig);
		RadioCardPanel visualizationComponent = new RadioCardPanel(null, null, false, false);
		Collection<TreeNode> trees = renderable.getRoots();
	    for (TreeNode root : trees) {
	    	try {
	    		TreeIOObjectVisualization visualization = initializer.getVisualization(root);
	    		if (visualization != null) {
	    			JPanel panel = new JPanel(new BorderLayout());
	    			final JComponent mainComponent = visualization.getVisualizationAsComponent();
	    			JPanel leftPanel = new JPanel(new BorderLayout());
	    			JButton imageButton = new JButton("Export Image...");
	    			imageButton.setToolTipText("Saves an image of the current graph.");
	    			leftPanel.add(visualization.getControls(), BorderLayout.NORTH);
	    			leftPanel.add(imageButton, BorderLayout.SOUTH);
	    			panel.add(mainComponent, BorderLayout.CENTER);
	    			panel.add(leftPanel, BorderLayout.WEST);
	    			imageButton.addActionListener(new ActionListener() {
	    				
	    				@Override
	    				public void actionPerformed(ActionEvent e) {
	    					Component tosave = mainComponent;
	    					ExportDialog exportDialog = new ExportDialog("RapidMiner");
	    					exportDialog.showExportDialog(mainComponent, "Save Image...", tosave, "plot");
	    				}
	    			});
	    			visualizationComponent.addCard(root.getTreeTypeName(), panel);
	    		}
	    	} catch (Exception e) {
	    		LogService.getRoot().log(Level.WARNING, "Error creating renderer: " + this.getName(), e);
	    		visualizationComponent.addCard(root.getTreeTypeName(), new JLabel("Error creating renderer (see log)."));
	    	}
	    }
	    return visualizationComponent;
	}
}
