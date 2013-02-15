package org.fabi.visualizations.scatter_old;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.fabi.visualizations.scatter.sources.AttributeInfo;
import org.fabi.visualizations.tools.math.Arrays;
import org.jfree.data.Range;

public class InputsSettingPanel extends JPanel implements Refreshable {

	private static final long serialVersionUID = -6033226003009052198L;
	
	protected final static String TITLE = "Inputs setting";
	
	protected SlidersChangeListener listener;
	protected ScatterplotVisualization visualization;
	protected JSlider[] sliders;
	protected JLabel[] labels;
		
	public InputsSettingPanel(ScatterplotVisualization visualization) {
		super(new GridBagLayout());
		this.visualization = visualization;
		setBorder(BorderFactory.createTitledBorder(TITLE));
		DatasetGenerator source = visualization.getSource();
		int size = source.getInputsNumber();
		GridBagConstraints c = new GridBagConstraints();
		sliders = new JSlider[size];
		labels = new JLabel[size];
		listener = new SlidersChangeListener(visualization, sliders);
		for (int attribute = 0; attribute < source.getInputsNumber(); attribute++) {
			c.gridx = 0;
			c.gridy = attribute;
			labels[attribute] = new JLabel();
			labels[attribute].setAlignmentX(JComponent.LEFT_ALIGNMENT);
			add(labels[attribute], c);
			c.gridx = 1;
			sliders[attribute] = new JSlider();
			add(sliders[attribute], c);
		}
		refresh();
	}
	
	protected final static double SLIDER_RATIO = 3;
	
	@Override
	public void refresh() {
		DatasetGenerator source = visualization.getSource();
		double[][] vectors = source.getTransformedInputs();
		final List<Double> inputs = visualization.getInputsSetting();
		List<AttributeInfo> attributes = source.getMetadata().getInputAttributeInfo();
		for (int attribute = 0; attribute < sliders.length; attribute++) {
			String name = attributes.get(attribute).getName();
			if (name.length() > 30) {
				labels[attribute].setText(name.substring(0, 27) + "...");
				labels[attribute].setToolTipText(name);
			} else {
				labels[attribute].setText(name);
			}
			sliders[attribute].removeChangeListener(listener);
			Range range = Arrays.getBounds(vectors, attribute);
			range = ScatterplotVisualization.scaleSymetrical(range, SLIDER_RATIO);
			int actValue = new Double(inputs.get(attribute)).intValue();
			if (actValue > range.getUpperBound()) {
				actValue = new Double(range.getUpperBound()).intValue();
			} else if (actValue < range.getLowerBound()) {
				actValue=  new Double(range.getLowerBound()).intValue();
			}
			sliders[attribute].setModel(new DefaultBoundedRangeModel(actValue, 0,
					new Double(range.getLowerBound()).intValue(),
					new Double(range.getUpperBound()).intValue()));
			sliders[attribute].addChangeListener(listener);
			if (attribute == visualization.getxAxisIndex() || (visualization.isyAxisInput() && attribute == visualization.getyAxisIndex())) {
				labels[attribute].setEnabled(false);
				sliders[attribute].setEnabled(false);
			} else {
				labels[attribute].setEnabled(true);
				sliders[attribute].setEnabled(true);
			}
		}
	}
	
	protected class SlidersChangeListener implements ChangeListener {
		
		protected ScatterplotVisualization visualization;
		protected JSlider[] sliders;
		
		public SlidersChangeListener(ScatterplotVisualization visualization, JSlider[] sliders) {
			this.visualization = visualization;
			this.sliders = sliders;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			List<Double> values = new ArrayList<Double>(sliders.length);
			for (int i = 0; i < sliders.length; i++) {
				values.add(new Double(sliders[i].getValue()));
			}
			visualization.setProperty(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, values);
		}
		
	}
}
