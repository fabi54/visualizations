package org.fabi.visualizations.scatter2.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import org.fabi.visualizations.scatter.sources.Metadata;
import org.fabi.visualizations.scatter2.ScatterplotVisualization;
import org.fabi.visualizations.scatter2.sources.ScatterplotSource;
import org.fabi.visualizations.tools.math.Arrays;
import org.jfree.data.Range;

public class InputsSettingPanel extends JPanel {

	private static final long serialVersionUID = -6033226003009052198L;
	
	protected final static String TITLE = "Inputs setting";
	
	protected JSlider[] sliders;
	protected JLabel[] labels;
	protected JLabel[] attrLabels;
	double[] lowerBounds;
	double[] ranges;
	
	protected boolean ignore = false;
	
	public InputsSettingPanel(ScatterplotVisualization visualization,
			final ScatterplotVisualizationControlPanel panel) {
		this(visualization, panel, 1.0);
	}
	
	public InputsSettingPanel(ScatterplotVisualization visualization,
			final ScatterplotVisualizationControlPanel panel, double multiplyRanges) {
		super(new GridBagLayout());
		setBorder(BorderFactory.createTitledBorder(TITLE));
		ScatterplotSource src = visualization.getSource();
		int cnt = src.getInputsNumber();
		Metadata metadata = src.getMetadata();
		List<AttributeInfo> inputAttributes = (metadata != null) ? metadata.getInputAttributeInfo() : null;
		GridBagConstraints c = new GridBagConstraints();
		sliders = new JSlider[cnt];
		attrLabels = new JLabel[cnt];
		labels = new JLabel[cnt];
		lowerBounds = new double[cnt];
		ranges = new double[cnt];
		SlidersChangeListener listener = new SlidersChangeListener(panel, sliders);
		for (int i = 0; i < cnt; i++) {
			double min = Double.POSITIVE_INFINITY;
			double max = Double.NEGATIVE_INFINITY;
			for (int j = 0; j < src.getDataSourceCount(); j++) {
				Range range = Arrays.getBounds(src.getDataSource(j).getInputDataVectors(), i);
				range = scaleSymetrical(range, multiplyRanges);
				min = Math.min(range.getLowerBound(), min);
				max = Math.max(range.getUpperBound(), max);
			}
			lowerBounds[i] = min;
			ranges[i] = max - min;
			c.gridx = 0;
			c.gridy = i;
			attrLabels[i] = new JLabel((inputAttributes != null) ? inputAttributes.get(i).getName() : ("attr" + i));
			attrLabels[i].setAlignmentX(JComponent.LEFT_ALIGNMENT);
			add(attrLabels[i], c);
			c.gridx = 1;
			sliders[i] = new JSlider();
			sliders[i].setModel(new DefaultBoundedRangeModel(500, 0, 0, 1000));
			sliders[i].addChangeListener(listener);
			add(sliders[i], c);
			c.gridx = 2;
			labels[i] = new JLabel();
			add(labels[i], c);
		}
		updateValues(visualization);
		setEnabled(visualization);
		addListeners(visualization);
	}
	
	protected static final int SLIDER_STEPS = 1000;
	
	protected static Range scaleSymetrical(Range orig, double ratio) {
		double diff = (orig.getLength() * ((ratio - 1.0) / 2.0));
		return new Range(orig.getLowerBound() - diff, orig.getUpperBound() + diff);
	}
	
	protected void addListeners(final ScatterplotVisualization visualization) {
		visualization.addPropertyChangeListener(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!ignore) {
					updateValues(visualization);
				}
			}
		});
		visualization.addPropertyChangeListener(new String[]{
				ScatterplotVisualization.PROPERTY_X_AXIS_ATTRIBUTE_INDEX,
				ScatterplotVisualization.PROPERTY_Y_AXIS_ATTRIBUTE_INDEX},
				new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setEnabled(visualization);
			}
		});
	}
	
	protected void updateValues(ScatterplotVisualization visualization) {
		double[] inputsSetting = visualization.getInputsSetting();
		for (int i = 0; i < inputsSetting.length; i++) {
			sliders[i].setValue((int) (((inputsSetting[i] - lowerBounds[i]) / ranges[i]) * SLIDER_STEPS));
			labels[i].setText(Double.toString(inputsSetting[i])); // TODO improve
			if (labels[i].getText().length() > 5) {
				labels[i].setText(labels[i].getText().substring(0, 5));
			}
		}
	}
	
	protected void setEnabled(ScatterplotVisualization visualization) {
		for (int i = 0; i < sliders.length; i++) {
			if (i == visualization.getxAxisAttributeIndex()
				|| i == visualization.getyAxisAttributeIndex()) {
				attrLabels[i].setEnabled(false);
				sliders[i].setEnabled(false);
				labels[i].setVisible(false);
			} else {
				attrLabels[i].setEnabled(true);
				sliders[i].setEnabled(true);
				labels[i].setVisible(true);
			}
		}
	}
	
	protected class SlidersChangeListener implements ChangeListener {
		
		protected ScatterplotVisualizationControlPanel panel;
		protected JSlider[] sliders;
		protected double[] values;
		
		public SlidersChangeListener(ScatterplotVisualizationControlPanel panel, JSlider[] sliders) {
			this.panel = panel;
			this.sliders = sliders;
			this.values = new double[sliders.length];
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			ignore = true;
			for (int i = 0; i < sliders.length; i++) {
				values[i] = (sliders[i].getValue() * ranges[i] / (double) SLIDER_STEPS) + lowerBounds[i];
				labels[i].setText(Double.toString(values[i])); // TODO improve
				if (labels[i].getText().length() > 5) {
					labels[i].setText(labels[i].getText().substring(0, 5));
				}
			}
			panel.setProperty(ScatterplotVisualization.PROPERTY_INPUTS_SETTING, values);
			ignore = false;
		}
		
	}
}
