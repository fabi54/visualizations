package org.fabi.visualizations.scatter2.gui;

import java.awt.Dimension;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fabi.visualizations.scatter2.ScatterplotVisualization;

public class BackgroundControlDialog extends JDialog {

	private static final long serialVersionUID = 5820045046896548276L;

	public BackgroundControlDialog(final ScatterplotVisualization visualization, JComponent parent) {
		super(SwingUtilities.windowForComponent(parent), "Background Settings");
		final JColorChooser chooser1 = new JColorChooser(visualization.getBackground());
		add(chooser1);
		AbstractColorChooserPanel[] panels = chooser1.getChooserPanels();
		chooser1.setChooserPanels(new AbstractColorChooserPanel[]{panels[1]});
		chooser1.getSelectionModel().addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				visualization.setBackground(chooser1.getColor());
			}
		});
		setSize(new Dimension(600, 600));
	}
	
}
