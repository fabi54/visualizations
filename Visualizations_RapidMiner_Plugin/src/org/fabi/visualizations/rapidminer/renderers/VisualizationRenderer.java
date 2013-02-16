package org.fabi.visualizations.rapidminer.renderers;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fabi.visualizations.Visualization;
import org.freehep.util.export.ExportDialog;

import com.rapidminer.gui.renderer.AbstractRenderer;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.report.Reportable;

public abstract class VisualizationRenderer extends AbstractRenderer {

	protected abstract Visualization<?> getVisualization(Object renderable);
	
	@Override
	public Reportable createReportable(Object arg0, IOContainer arg1, int arg2,
			int arg3) {
		return null;
	}

	@Override
	public Component getVisualizationComponent(Object renderable, IOContainer iocontainer) {
		Visualization<?> visualization = getVisualization(renderable);
		if (visualization == null) {
			return new JLabel("Can't create visualization.");
		}
		JPanel panel = new JPanel(new BorderLayout());
		final JComponent mainComponent = visualization.getVisualizationAsComponent();
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
		JButton imageButton = new JButton("Export Image...");
		imageButton.setToolTipText("Saves an image of the current graph.");
		leftPanel.add(visualization.getControls(), BorderLayout.NORTH);
		leftPanel.add(imageButton, BorderLayout.SOUTH);
		panel.add(mainComponent, BorderLayout.CENTER);
		panel.add(new JScrollPane(leftPanel), BorderLayout.WEST);
		imageButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Component tosave = mainComponent;
				ExportDialog exportDialog = new ExportDialog("RapidMiner");
				exportDialog.showExportDialog(mainComponent, "Save Image...", tosave, "plot");
			}
		});
		return panel;
	}

}
