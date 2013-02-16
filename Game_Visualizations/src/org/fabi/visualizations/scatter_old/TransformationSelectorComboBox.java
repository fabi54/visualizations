package org.fabi.visualizations.scatter_old;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.fabi.visualizations.tools.transformation.TransformationProvider;

@Deprecated
public class TransformationSelectorComboBox extends JComboBox implements Refreshable {
	
	private static final long serialVersionUID = 6698585946172004672L;
	
	protected ScatterplotVisualization visualization;
	protected ActionListener listener;
	
	protected String CUSTOM_TRANSFORMATION = "Custom transformation";
	
	public TransformationSelectorComboBox(final ScatterplotVisualization visualization) {
		this.visualization = visualization;
		addItem(TransformationProvider.NO_TRANSFORMATION);
		addItem(TransformationProvider.PCA_TRANSFORMATION);
		this.listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!getSelectedItem().equals(CUSTOM_TRANSFORMATION)) {
					removeItem(CUSTOM_TRANSFORMATION);
				}
				visualization.setProperty(ScatterplotVisualization.PROPERTY_TRANSFORMATION, getSelectedItem());
			}
		};
		refresh();
	}
	
	@Override
	public void refresh() {
		try {
			this.removeActionListener(listener);
		} catch (Exception ex) { }
		String transformation = visualization.getSource().getTransformation();
		if (!contains(transformation)) {
			addItem(CUSTOM_TRANSFORMATION);
			setSelectedItem(CUSTOM_TRANSFORMATION);
		} else {
			setSelectedItem(transformation);
		}
		this.addActionListener(listener);
	}
	
	protected boolean contains(Object item) {
		for (int i = 0; i < getItemCount(); i++) {
			if (getItemAt(i).equals(item)) {
				return true;
			}
		}
		return false;
	}
}
