package org.fabi.visualizations.scatter2.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.fabi.visualizations.scatter2.ScatterplotVisualization;
import org.fabi.visualizations.scatter2.color.ColorModel;

public class ColorModelControlDialog extends JDialog {

	private static final long serialVersionUID = 5209238255222877527L;

	protected ColorModel tmp;
	protected ScatterplotVisualization visualization;

	protected JPanel panel;
	protected JComponent actControls;
	
	// TODO refactor
	public ColorModelControlDialog(final ScatterplotVisualization visualization,
			final ScatterplotVisualizationControlPanel controlPanel) {
		super(SwingUtilities.windowForComponent(controlPanel), "Color Model Settings");
		this.visualization = visualization;
		this.panel = new JPanel(new BorderLayout()); // TODO use CardLayout
		add(panel);
		final ColorModelComboBox chooserCombB = new ColorModelComboBox(visualization);
		panel.add(chooserCombB, BorderLayout.NORTH);
		chooserCombB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tmp = (ColorModel) chooserCombB.getSelectedItem();
				if (actControls != null) panel.remove(actControls);
				actControls = tmp.getControls(); // TODO getControls(tmp);
				panel.add(actControls, BorderLayout.CENTER);
				panel.validate();
			}
		});
		
		// OK-Apply-Cancel
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		JPanel bottom = new JPanel(new GridBagLayout());
		c.gridy = 0;
		c.gridx = 0;
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualization.setProperty(ScatterplotVisualization.PROPERTY_COLOR_MODEL, tmp);
				setVisible(false);
			}
		});
		bottom.add(okButton, c);
		c.gridx = 1;
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualization.setProperty(ScatterplotVisualization.PROPERTY_COLOR_MODEL, tmp);
			}
		});
		bottom.add(applyButton, c);
		c.gridx = 2;
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		bottom.add(cancelButton, c);
		setSize(600, 500);
		panel.add(bottom, BorderLayout.SOUTH);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		tmp = visualization.getColorModel();
		if (tmp != null) {
			JComponent controls = tmp.getControls();
			if (actControls != null) panel.remove(actControls);
			if (controls != null) {
				actControls = tmp.getControls();
				panel.add(actControls, BorderLayout.CENTER);
			} else {
				actControls = new JLabel("No controls available for current color model.");
				panel.add(actControls, BorderLayout.CENTER);
			}
		}
	}
}
