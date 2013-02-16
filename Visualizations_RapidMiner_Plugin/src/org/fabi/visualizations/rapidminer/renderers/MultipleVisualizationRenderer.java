package org.fabi.visualizations.rapidminer.renderers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.HashSet;
import java.util.List;
//import java.util.Set;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fabi.visualizations.Visualization;
import org.fabi.visualizations.rapidminer.MultipleVisualizationContainer;
import org.freehep.util.export.ExportDialog;

import com.rapidminer.gui.renderer.AbstractRenderer;
import com.rapidminer.gui.renderer.RendererService;
import com.rapidminer.gui.tools.RadioCardPanel;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.report.Reportable;
import com.rapidminer.tools.LogService;

public class MultipleVisualizationRenderer extends AbstractRenderer {

	@Override
	public String getName() {
		return "Visualizations";
	}

	@Override
	public Reportable createReportable(Object arg0, IOContainer arg1, int arg2,
			int arg3) {
		return null;
	}

	@Override
	public Component getVisualizationComponent(Object renderable, IOContainer container) {
		if (renderable instanceof MultipleVisualizationContainer) {
			return createVisualization((MultipleVisualizationContainer) renderable);
		} else {
			return null;
		}
	}
	
	protected Component createVisualization(MultipleVisualizationContainer renderable) {
		String resultName = RendererService.getName(renderable.getClass());
		if (resultName == null) {
			resultName = "";
		}
		RadioCardPanel visualizationComponent = new RadioCardPanel(null, null, false, false);
		List<Visualization<?>> visualizations = renderable.getVisualizations();
//		int i = 2;
//		Set<String> used = new HashSet<String>();
	    for (Visualization<?> v : visualizations) {
	    	try {
	    		JPanel panel = new JPanel(new BorderLayout());
	    		final JComponent mainComponent = v.getVisualizationAsComponent();
    			JPanel leftPanel = new JPanel(new BorderLayout());
    			JButton imageButton = new JButton("Export Image...");
    			imageButton.setToolTipText("Saves an image of the current graph.");
    			leftPanel.add(v.getControls(), BorderLayout.NORTH);
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
	    		String s = v.toString();
//	    		while (used.contains(s)) {
//	    			s = s + " (" + (i++) + ")"; 
//	    		}
//	    		used.add(s);
	    		s += " (" + v.hashCode() + ")";
    			visualizationComponent.addCard(s, panel);
    		} catch (Exception e) {
	    		LogService.getRoot().log(Level.WARNING, "Error creating renderer: " + this.getName(), e);
	    		visualizationComponent.addCard("Error", new JLabel("Error creating renderer (see log)."));
	    	}
	    }
	    return visualizationComponent;
	}

}
