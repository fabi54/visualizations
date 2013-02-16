package org.fabi.visualizations.scatter.gui;

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

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.dotsize.DotSizeModel;
import org.fabi.visualizations.scatter.dotsize.MinkowskiDistanceDotSizeModel;

public class DotSizeModelControlDialog extends JDialog {

	private static final long serialVersionUID = -8064496517216169474L;

	protected DotSizeModel tmp;
	protected ScatterplotVisualization visualization;

	protected JPanel panel;
	protected JComponent actControls;
	
	// TODO refactor
	public DotSizeModelControlDialog(final ScatterplotVisualization visualization,
			final ScatterplotVisualizationControlPanel controlPanel) {
		super(SwingUtilities.windowForComponent(controlPanel), "Dot Size Model Settings");
		this.visualization = visualization;
		this.panel = new JPanel(new BorderLayout()); // TODO use CardLayout
		add(panel);
		final DotSizeModelComboBox chooserCombB = new DotSizeModelComboBox(visualization);
		panel.add(chooserCombB, BorderLayout.NORTH);
		chooserCombB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {;
				if (actControls != null) {
					panel.remove(actControls);
				}
				if (chooserCombB.getSelectedItem() instanceof String) {
					tmp = null;
					actControls = new JLabel("No controls available");
				} else {
					tmp = (DotSizeModel) chooserCombB.getSelectedItem();
					actControls = getControls(tmp);
				}
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
				visualization.setProperty(ScatterplotVisualization.PROPERTY_DOT_SIZE_MODEL, tmp);
				setVisible(false);
			}
		});
		bottom.add(okButton, c);
		c.gridx = 1;
		JButton applyButton = new JButton("Apply");
		applyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualization.setProperty(ScatterplotVisualization.PROPERTY_DOT_SIZE_MODEL, tmp);
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
		tmp = visualization.getDotSizeModel();
		if (tmp != null) {
			JComponent controls = getControls(tmp);
			if (actControls != null) panel.remove(actControls);
			if (controls != null) {
				actControls = getControls(tmp);
				panel.add(actControls, BorderLayout.CENTER);
			} else {
				actControls = new JLabel("No controls available for current dot size model.");
				panel.add(actControls, BorderLayout.CENTER);
			}
		}
	}
	
	protected JComponent getControls(DotSizeModel model) {
		if (model instanceof MinkowskiDistanceDotSizeModel) {
			return new MinkowskiDistanceDotSizeModelControlPanel((MinkowskiDistanceDotSizeModel) model);
		}
		return new JLabel("Control panel not available.");
	}

}
