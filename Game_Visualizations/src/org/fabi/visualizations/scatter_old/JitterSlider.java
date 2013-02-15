package org.fabi.visualizations.scatter_old;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JitterSlider extends JSlider {
	
	private static final long serialVersionUID = 8984711053719387815L;

	protected static final int MAX_JITTER = 30;
	
	public JitterSlider(final ScatterplotVisualization visualization) {
		int value = visualization.getSource().getJitterAmount();
		if (value < 0) {
			value = 0;
		} else if (value > MAX_JITTER) {
			value = MAX_JITTER;
		}
		setModel(new DefaultBoundedRangeModel(value, 0, 0, MAX_JITTER));
		addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				visualization.setProperty(ScatterplotVisualization.PROPERTY_JITTER_AMOUNT, getValue());
			}
		});
	}
	
	// TODO ? refresh() {...}
}
