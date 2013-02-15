package org.fabi.visualizations.scatter_old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.fabi.visualizations.scatter.sources.AttributeInfo;

public class XAxisAttributeSelectorComboBox extends JComboBox implements Refreshable {

	private static final long serialVersionUID = 4297687576611663785L;
	
	protected ScatterplotVisualization visualization;
	protected ActionListener listener;
	
	
	public XAxisAttributeSelectorComboBox(final ScatterplotVisualization visualization) {
		this.visualization = visualization;
		this.listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				visualization.setProperty(ScatterplotVisualization.PROPERTY_X_AXIS_ATTRIBUTE_INDEX, getSelectedIndex());
			}
		};
		refresh();
	}
	
	@Override
	public void refresh() {
		try {
			this.removeActionListener(listener);
		} catch (Exception ex) { }
		removeAllItems();
		for (AttributeInfo attribute : visualization.getSource().getMetadata().getInputAttributeInfo()) {
			String name = attribute.getName();
			if (name.length() > 30) {
				name = name.substring(0, 27) + "...";
			}
			addItem(name);
		}
		setSelectedIndex(visualization.getxAxisIndex());
		this.addActionListener(listener);
	}
}
