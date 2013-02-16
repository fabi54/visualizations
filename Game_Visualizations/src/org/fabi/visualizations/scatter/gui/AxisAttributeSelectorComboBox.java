package org.fabi.visualizations.scatter.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;

import org.fabi.visualizations.scatter.ScatterplotVisualization;
import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter.sources.ScatterplotSource;

public class AxisAttributeSelectorComboBox extends JComboBox {
	
	private static final long serialVersionUID = 4297687576611663785L;
	
	protected int axisIndex;
	protected boolean allowOutput;
	
	protected boolean ignore = false;
	protected String propertyName;
	
	public AxisAttributeSelectorComboBox(final ScatterplotVisualization visualization,
			final ScatterplotVisualizationControlPanel panel, final int axisIndex) {
		this(visualization, panel, axisIndex, (axisIndex == ScatterplotVisualization.Y_AXIS));
	}
	
	public AxisAttributeSelectorComboBox(final ScatterplotVisualization visualization,
			final ScatterplotVisualizationControlPanel panel, final int axisIndex, final boolean allowOutput) {
		this.axisIndex = axisIndex;
		this.allowOutput = allowOutput;
		addItems(visualization);
		switch (axisIndex) {
			case ScatterplotVisualization.X_AXIS:
				propertyName = ScatterplotVisualization.PROPERTY_X_AXIS_ATTRIBUTE_INDEX;
				break;
			case ScatterplotVisualization.Y_AXIS:
				propertyName = ScatterplotVisualization.PROPERTY_Y_AXIS_ATTRIBUTE_INDEX;
				break;
		}
		visualization.addPropertyChangeListener(propertyName, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!ignore) {
					selectAttribute(visualization);
				}
			}
		});
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ignore = true;
				if (allowOutput && getSelectedIndex() == getItemCount() - 1) {
					panel.setProperty(propertyName, ScatterplotVisualization.OUTPUT_AXIS);
				} else {
					panel.setProperty(propertyName, getSelectedIndex());
				}
				ignore = false;
			}
		});
	}
	
	public void addItems(ScatterplotVisualization visualization) {
		ScatterplotSource source = visualization.getSource(); 
		Metadata metadata = source.getMetadata();
		if (metadata != null) {
			for (AttributeInfo attribute : metadata.getInputAttributeInfo()) {
				String name = attribute.getName();
				if (name.length() > 30) {
					name = name.substring(0, 27) + "...";
				}
				addItem(name);
			}
		} else {
			for (int i = 0; i < source.getInputsNumber(); i++) {
				addItem("attr" + i);
			}
		}
		if (allowOutput) {
			addItem("<output>");
		}
		selectAttribute(visualization);
	}
	
	protected void selectAttribute(ScatterplotVisualization visualization) {
		int selectedIndex = visualization.getAxisAttributeIndex(axisIndex);
		if (selectedIndex == ScatterplotVisualization.OUTPUT_AXIS && allowOutput) {
			setSelectedIndex(getItemCount() - 1);
		} else {
			setSelectedIndex(selectedIndex);
		}
	}
}
