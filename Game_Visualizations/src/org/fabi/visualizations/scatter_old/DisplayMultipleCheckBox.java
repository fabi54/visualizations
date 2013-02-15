package org.fabi.visualizations.scatter_old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class DisplayMultipleCheckBox extends JCheckBox implements Refreshable {

	private static final long serialVersionUID = -5244899146949363145L;

	protected ScatterplotVisualization visualization;
	
	protected ActionListener listener;
	
	public DisplayMultipleCheckBox(final ScatterplotVisualization visualization) {
		super("show multiple models");
		this.visualization = visualization;
		listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualization.setProperty(ScatterplotVisualization.PROPERTY_DISPLAY_MULTIPLE_MODELS, isSelected());
			} 
		};
		refresh();
	}

	@Override
	public void refresh() {
		removeActionListener(listener);
		setSelected(visualization.isDisplayMultiple());
		addActionListener(listener);
	}
}
