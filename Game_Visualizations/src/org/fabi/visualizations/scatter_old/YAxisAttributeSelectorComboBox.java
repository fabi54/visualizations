package org.fabi.visualizations.scatter_old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.fabi.visualizations.config.VisualizationConfig;
import org.fabi.visualizations.scatter.sources.AttributeInfo;

@Deprecated
public class YAxisAttributeSelectorComboBox extends JComboBox implements Refreshable {

	private static final long serialVersionUID = 4297687576611663785L;
	
	protected ScatterplotVisualization visualization;
	protected ActionListener listener;
	
	
	public YAxisAttributeSelectorComboBox(final ScatterplotVisualization visualization) {
		this.visualization = visualization;
		this.listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getSelectedIndex() == getItemCount() - 1) {
					visualization.setProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_INPUT, false);
				} else {
					VisualizationConfig cfg = visualization.getConfig();
					cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_ATTRIBUTE_INDEX, getSelectedIndex());
					cfg.setTypedProperty(ScatterplotVisualization.PROPERTY_Y_AXIS_INPUT, true);
					visualization.setConfig(cfg);
				}
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
		addItem("<output>");
		if (!visualization.isyAxisInput()) {
			setSelectedIndex(getItemCount() - 1);
		} else {
			setSelectedIndex(visualization.getyAxisIndex());
		}
		this.addActionListener(listener);
	}
}
